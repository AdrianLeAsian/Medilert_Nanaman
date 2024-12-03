package com.example.medilert;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends BaseActivity {
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userManager = new UserManager(this);
        
        setupToolbar();
        setupButtons();
        setupBottomNavigation();
        updateWelcomeMessage();
    }

    private void updateWelcomeMessage() {
        TextView usernameText = findViewById(R.id.usernameText);
        String username = userManager.getUsername();
        usernameText.setText(getString(R.string.hi_username_format, username));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupButtons() {
        Button medicalReminderButton = findViewById(R.id.medicalReminderButton);
        Button healthRemindersButton = findViewById(R.id.healthRemindersButton);
        Button emergencyLocatorButton = findViewById(R.id.emergencyLocatorButton);
        Button callHelpButton = findViewById(R.id.callHelpButton);
        Button inventoryButton = findViewById(R.id.inventoryButton);

        medicalReminderButton.setOnClickListener(v -> 
            startActivity(new Intent(this, MedicalRemindersActivity.class)));
            
        healthRemindersButton.setOnClickListener(v -> 
            Toast.makeText(this, "Health Reminders clicked", Toast.LENGTH_SHORT).show());
            
        emergencyLocatorButton.setOnClickListener(v -> 
            startActivity(new Intent(this, EmergencyLocatorActivity.class)));
            
        callHelpButton.setOnClickListener(v -> 
            startActivity(new Intent(this, CallHelpActivity.class)));

        inventoryButton.setOnClickListener(v -> 
            startActivity(new Intent(this, InventoryActivity.class)));
    }
} 