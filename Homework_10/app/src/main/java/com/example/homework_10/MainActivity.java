package com.example.homework_10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText nameField, ageField;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameField = findViewById(R.id.field_name);
        ageField = findViewById(R.id.field_age);
        findViewById(R.id.send).setOnClickListener(this::onSendClick);
    }

    private void onSendClick(View v) {
        String errorMessage = "";

        String name = nameField.getText().toString().trim();
        if (name.length() == 0) {
            errorMessage = "Введите имя.\n\n";
        }

        String ageStr = ageField.getText().toString();
        int age = 0;

        if (ageStr.length() == 0) {
            errorMessage += "Введите возраст.";
        } else {
            try {
                age = Integer.parseInt(ageStr);

                if (age <= 0 || age > 120) {
                    errorMessage += "Неверное значение для возраста.";
                }

            } catch (Exception ex) {
                errorMessage += "Введите корректный возраст.";
            }
        }

        if (errorMessage.length() == 0) errorMessage = "\nВаши данные успешно отправлены 😁";
        else errorMessage = "\nНеверный ввод:\n\n" + errorMessage.trim();

        new AlertDialog.Builder(this)
                .setTitle("Секретная форма")
                .setMessage(errorMessage)
                .setPositiveButton("Ок", null)
                .create().show();
    }
}