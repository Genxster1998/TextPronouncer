package com.example.textpronouncer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "SpeakPrefs";
    private static final String SERVICE_ENABLED_KEY = "service_enabled";
    private Switch serviceSwitch;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceSwitch = findViewById(R.id.serviceSwitch);
        statusText = findViewById(R.id.statusText);

        // Load saved state
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean serviceEnabled = settings.getBoolean(SERVICE_ENABLED_KEY, true);
        serviceSwitch.setChecked(serviceEnabled);
        updateStatusText(serviceEnabled);

        serviceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save state
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(SERVICE_ENABLED_KEY, isChecked);
            editor.apply();

            updateStatusText(isChecked);
        });
    }

    private void updateStatusText(boolean enabled) {
        statusText.setText(enabled ? R.string.service_enabled : R.string.service_disabled);
    }
} 