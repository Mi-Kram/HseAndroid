package com.example.homework_23;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    StringBuilder sb = new StringBuilder();
    TextView result;
    int cnt = 0;

    @SuppressLint("DefaultLocale")
    ActivityResultLauncher<Intent> recordVoiceLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK) return;

                Intent intent = result.getData();
                if (intent == null) return;

                ArrayList<String> results = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (results == null || results.isEmpty()) return;

                sb.append(String.format("Запись №%d.\n", ++cnt));
                sb.append(String.join("\n", results));
                sb.append("\n");

                this.result.setText(sb.toString());
                sb.append("\n");
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        ((Button)findViewById(R.id.voice)).setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("ru","RU"));
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Записывается...");
            recordVoiceLauncher.launch(intent);
        });
    }
}