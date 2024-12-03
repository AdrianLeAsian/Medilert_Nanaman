package com.example.medilert;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Locale;
import com.example.medilert.data.AppDatabase;
import com.example.medilert.data.Reminder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import com.example.medilert.notification.ReminderReceiver;

public class MedicalRemindersActivity extends AppCompatActivity {
    private int selectedHour = 12;
    private int selectedMinute = 0;
    private AppDatabase db;
    private ReminderAdapter adapter;
    private ExecutorService executorService;
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_reminders);

        checkPermissions();
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        
        setupToolbar();
        setupRecyclerView();
        setupFab();
        loadReminders();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, 
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 
                    PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.remindersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReminderAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnReminderClickListener(reminder -> {
            // Show edit dialog
            showEditReminderDialog(reminder);
        });
    }

    private void loadReminders() {
        db.reminderDao().getAllReminders().observe(this, reminders -> {
            adapter.setReminders(reminders);
        });
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.addReminderFab);
        fab.setOnClickListener(v -> showAddReminderDialog());
    }

    private void showAddReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_reminder, null);
        
        TextInputEditText medicineNameInput = dialogView.findViewById(R.id.medicineNameInput);
        Button timePickerButton = dialogView.findViewById(R.id.timePickerButton);
        
        timePickerButton.setOnClickListener(v -> showTimePicker(timePickerButton));

        builder.setView(dialogView)
                .setTitle("Add New Reminder")
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    // Get medicine name
                    String medicineName = medicineNameInput.getText().toString();
                    
                    // Get selected days
                    boolean[] selectedDays = new boolean[7];
                    selectedDays[0] = ((CheckBox) dialogView.findViewById(R.id.monday)).isChecked();
                    selectedDays[1] = ((CheckBox) dialogView.findViewById(R.id.tuesday)).isChecked();
                    selectedDays[2] = ((CheckBox) dialogView.findViewById(R.id.wednesday)).isChecked();
                    selectedDays[3] = ((CheckBox) dialogView.findViewById(R.id.thursday)).isChecked();
                    selectedDays[4] = ((CheckBox) dialogView.findViewById(R.id.friday)).isChecked();
                    selectedDays[5] = ((CheckBox) dialogView.findViewById(R.id.saturday)).isChecked();
                    selectedDays[6] = ((CheckBox) dialogView.findViewById(R.id.sunday)).isChecked();
                    
                    saveReminder(medicineName, selectedDays, selectedHour, selectedMinute);
                })
                .setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTimePicker(Button timeButton) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                },
                selectedHour,
                selectedMinute,
                false
        );
        timePickerDialog.show();
    }

    private void saveReminder(String medicineName, boolean[] selectedDays, int hour, int minute) {
        Reminder reminder = new Reminder();
        reminder.medicineName = medicineName;
        reminder.hour = hour;
        reminder.minute = minute;
        reminder.monday = selectedDays[0];
        reminder.tuesday = selectedDays[1];
        reminder.wednesday = selectedDays[2];
        reminder.thursday = selectedDays[3];
        reminder.friday = selectedDays[4];
        reminder.saturday = selectedDays[5];
        reminder.sunday = selectedDays[6];

        executorService.execute(() -> {
            db.reminderDao().insert(reminder);
            // Schedule alarms for each selected day
            for (int i = 0; i < selectedDays.length; i++) {
                if (selectedDays[i]) {
                    scheduleAlarm(medicineName, i + 1, hour, minute);
                }
            }
        });
    }

    private void scheduleAlarm(String medicineName, int dayOfWeek, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("medicineName", medicineName);

        // Create unique ID for each alarm
        int requestCode = (dayOfWeek * 10000) + (hour * 100) + minute;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            this, 
            requestCode, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Calculate next alarm time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // If time has passed, add 7 days
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        // Schedule the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
            );
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
            );
        }
    }

    private void showEditReminderDialog(Reminder reminder) {
        // Similar to showAddReminderDialog but pre-fill the fields
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_reminder, null);
        
        TextInputEditText medicineNameInput = dialogView.findViewById(R.id.medicineNameInput);
        medicineNameInput.setText(reminder.medicineName);
        
        Button timePickerButton = dialogView.findViewById(R.id.timePickerButton);
        timePickerButton.setText(String.format(Locale.getDefault(), "%02d:%02d", 
                reminder.hour, reminder.minute));
        selectedHour = reminder.hour;
        selectedMinute = reminder.minute;
        
        CheckBox[] dayCheckboxes = new CheckBox[]{
            dialogView.findViewById(R.id.monday),
            dialogView.findViewById(R.id.tuesday),
            dialogView.findViewById(R.id.wednesday),
            dialogView.findViewById(R.id.thursday),
            dialogView.findViewById(R.id.friday),
            dialogView.findViewById(R.id.saturday),
            dialogView.findViewById(R.id.sunday)
        };
        
        dayCheckboxes[0].setChecked(reminder.monday);
        dayCheckboxes[1].setChecked(reminder.tuesday);
        dayCheckboxes[2].setChecked(reminder.wednesday);
        dayCheckboxes[3].setChecked(reminder.thursday);
        dayCheckboxes[4].setChecked(reminder.friday);
        dayCheckboxes[5].setChecked(reminder.saturday);
        dayCheckboxes[6].setChecked(reminder.sunday);
        
        timePickerButton.setOnClickListener(v -> showTimePicker(timePickerButton));

        builder.setView(dialogView)
                .setTitle("Edit Reminder")
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    reminder.medicineName = medicineNameInput.getText().toString();
                    reminder.hour = selectedHour;
                    reminder.minute = selectedMinute;
                    reminder.monday = dayCheckboxes[0].isChecked();
                    reminder.tuesday = dayCheckboxes[1].isChecked();
                    reminder.wednesday = dayCheckboxes[2].isChecked();
                    reminder.thursday = dayCheckboxes[3].isChecked();
                    reminder.friday = dayCheckboxes[4].isChecked();
                    reminder.saturday = dayCheckboxes[5].isChecked();
                    reminder.sunday = dayCheckboxes[6].isChecked();
                    
                    executorService.execute(() -> {
                        db.reminderDao().update(reminder);
                    });
                })
                .setNegativeButton(R.string.cancel, null)
                .setNeutralButton(R.string.delete, (dialog, which) -> {
                    executorService.execute(() -> {
                        db.reminderDao().delete(reminder);
                    });
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 