package com.example.medilert.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminders")
    LiveData<List<Reminder>> getAllReminders();

    @Insert
    void insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);
} 