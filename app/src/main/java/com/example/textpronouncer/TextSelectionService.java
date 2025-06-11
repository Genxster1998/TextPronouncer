package com.example.textpronouncer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.view.textservice.TextSelection;
import android.widget.Toast;
import java.util.Locale;

public class TextSelectionService extends Service implements TextToSpeech.OnInitListener {
    private static final String PREFS_NAME = "SpeakPrefs";
    private static final String SERVICE_ENABLED_KEY = "service_enabled";
    
    private TextToSpeech textToSpeech;
    private String selectedText;

    @Override
    public void onCreate() {
        super.onCreate();
        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Text to Speech initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onTextSelected(TextSelection selection) {
        // Check if service is enabled
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean serviceEnabled = settings.getBoolean(SERVICE_ENABLED_KEY, true);
        
        if (!serviceEnabled) {
            return;
        }

        selectedText = selection.getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            textToSpeech.speak(selectedText, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
} 