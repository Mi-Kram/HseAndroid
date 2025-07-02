package com.example.homework_04;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.finish1).setOnClickListener(v -> Finish(1));
        findViewById(R.id.finish2).setOnClickListener(v -> Finish(2));
        findViewById(R.id.finish3).setOnClickListener(v -> Finish(3));
    }

    private void Finish(int num) {
        Toast.makeText(this, String.format("Finish %d", num), Toast.LENGTH_SHORT).show();
        finish();
    }
}