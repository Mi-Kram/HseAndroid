package com.example.homework_15;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserUpdate extends AppCompatActivity {
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        setResult(RESULT_CANCELED);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(this, "Developer fail", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ((EditText)findViewById(R.id.name)).setText(intent.getStringExtra("name"));
        findViewById(R.id.cancel).setOnClickListener(this::onCancelClick);
        findViewById(R.id.submit).setOnClickListener(this::onSubmitClick);
    }

    void onCancelClick(View v) {
        finish();
    }

    void onSubmitClick(View v) {
        EditText nameText = findViewById(R.id.name);
        String name = nameText.getText().toString().trim();

        if (name.length() == 0) {
            finish();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("name", name);

        setResult(RESULT_OK, intent);
        finish();
    }
}