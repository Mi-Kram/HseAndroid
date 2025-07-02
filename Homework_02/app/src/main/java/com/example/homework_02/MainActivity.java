package com.example.homework_02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText enter;
    private TextView caption;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        enter = findViewById(R.id.Enter);
        caption = findViewById(R.id.OutputCaption);
        output = findViewById(R.id.Output);
        findViewById(R.id.Print).setOnClickListener(this::OnPrintClick);
    }

    private void OnPrintClick(View view) {
        output.setText(enter.getText());
        caption.setVisibility(output.getText().toString().trim().equals("") ? View.INVISIBLE : View.VISIBLE);
    }
}