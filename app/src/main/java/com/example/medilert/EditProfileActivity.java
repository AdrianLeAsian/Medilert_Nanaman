package com.example.medilert;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    private TextInputEditText dobInput;
    private AutoCompleteTextView genderInput;
    private AutoCompleteTextView bloodGroupInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setupToolbar();
        setupInputs();
        setupSaveButton();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Edit Profile");
        }
    }

    private void setupInputs() {
        // Setup Gender Dropdown
        genderInput = findViewById(R.id.genderInput);
        String[] genders = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders);
        genderInput.setAdapter(genderAdapter);

        // Setup Blood Group Dropdown
        bloodGroupInput = findViewById(R.id.bloodGroupInput);
        String[] bloodGroups = new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bloodGroups);
        bloodGroupInput.setAdapter(bloodGroupAdapter);

        // Setup Date Picker
        dobInput = findViewById(R.id.dobInput);
        dobInput.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    dobInput.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupSaveButton() {
        FloatingActionButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        // TODO: Implement profile saving logic
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
} 