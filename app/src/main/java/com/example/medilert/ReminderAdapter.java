package com.example.medilert;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medilert.data.Reminder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private List<Reminder> reminders = new ArrayList<>();
    private OnReminderClickListener listener;

    public interface OnReminderClickListener {
        void onReminderClick(Reminder reminder);
    }

    public void setOnReminderClickListener(OnReminderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.medicineName.setText(reminder.medicineName);
        holder.reminderTime.setText(String.format(Locale.getDefault(), 
                "Time: %02d:%02d", reminder.hour, reminder.minute));
        holder.reminderDays.setText("Days: " + getDaysString(reminder));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    private String getDaysString(Reminder reminder) {
        StringBuilder days = new StringBuilder();
        if (reminder.monday) days.append("M ");
        if (reminder.tuesday) days.append("T ");
        if (reminder.wednesday) days.append("W ");
        if (reminder.thursday) days.append("T ");
        if (reminder.friday) days.append("F ");
        if (reminder.saturday) days.append("S ");
        if (reminder.sunday) days.append("S");
        return days.toString();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {
        private TextView medicineName;
        private TextView reminderTime;
        private TextView reminderDays;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            reminderTime = itemView.findViewById(R.id.reminderTime);
            reminderDays = itemView.findViewById(R.id.reminderDays);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onReminderClick(reminders.get(position));
                }
            });
        }
    }
} 