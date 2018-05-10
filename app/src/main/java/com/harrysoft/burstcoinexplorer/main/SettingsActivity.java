package com.harrysoft.burstcoinexplorer.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.harrysoft.burstcoinexplorer.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction().add(R.id.settings_container, new SettingsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}