package com.example.medilert;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.card.MaterialCardView;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupBottomNavigation();
        setupToolbar();
        setupButtons();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupButtons() {
        MaterialCardView accountInfoCard = findViewById(R.id.accountInfoCard);
        MaterialCardView medicineAlertsCard = findViewById(R.id.medicineAlertsCard);
        MaterialCardView securityCard = findViewById(R.id.securityCard);
        MaterialCardView logoutCard = findViewById(R.id.logoutCard);

        accountInfoCard.setOnClickListener(v -> 
            startActivity(new Intent(this, EditProfileActivity.class)));

        medicineAlertsCard.setOnClickListener(v -> 
            Toast.makeText(this, "Medicine Alerts clicked", Toast.LENGTH_SHORT).show());

        securityCard.setOnClickListener(v -> 
            Toast.makeText(this, "Security & Data clicked", Toast.LENGTH_SHORT).show());

        logoutCard.setOnClickListener(v -> logout());
    }

    private void logout() {
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 