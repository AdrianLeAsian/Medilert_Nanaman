package com.example.medilert;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                if (!(this instanceof DashboardActivity)) {
                    startActivity(new Intent(this, DashboardActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                return true;
            } else if (itemId == R.id.navigation_profile) {
                if (!(this instanceof ProfileActivity)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                return true;
            } else if (itemId == R.id.navigation_settings) {
                if (!(this instanceof SettingsActivity)) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                return true;
            }
            return false;
        });

        // Set the correct item as selected
        if (this instanceof DashboardActivity) {
            bottomNav.setSelectedItemId(R.id.navigation_home);
        } else if (this instanceof ProfileActivity) {
            bottomNav.setSelectedItemId(R.id.navigation_profile);
        } else if (this instanceof SettingsActivity) {
            bottomNav.setSelectedItemId(R.id.navigation_settings);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
} 