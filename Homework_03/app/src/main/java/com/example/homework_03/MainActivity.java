package com.example.homework_03;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private EditText enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        enter = findViewById(R.id.Enter);
        findViewById(R.id.Print).setOnClickListener(this::OnPrintClick);
    }

    private void OnPrintClick(View view) {
        String data = enter.getText().toString().trim();
        if (data.isEmpty()) {
            enter.setError("Обязательное поле.");
            enter.setText("");
            enter.setHintTextColor(Color.parseColor("#FF0000"));
            return;
        }

        Intent intent = new Intent(MainActivity.this, OutputActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);


        enter.setError(null);
        enter.setText("");
        enter.setHintTextColor(Color.parseColor("#777777"));
    }
}