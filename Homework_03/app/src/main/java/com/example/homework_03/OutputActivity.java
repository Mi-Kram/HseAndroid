package com.example.homework_03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class OutputActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_activity);

        TextView output = findViewById(R.id.Output);
        String data = getIntent().getStringExtra("data");
        output.setText(data);

        Button back = findViewById(R.id.Back);
        back.setOnClickListener(v -> finish());
    }
}