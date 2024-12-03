package com.example.medilert.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicines")
public class Medicine {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int quantity;
    public String expiryDate;
    public String notes;
} 