package com.example.homework_22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactInfo extends AppCompatActivity {

    EditText name, phone, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        setSupportActionBar(findViewById(R.id.toolbar));
        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        Bundle b = intent.getExtras();
        if (b == null) {
            finish();
            return;
        }

        String nameStr = b.getString("name", null);
        String phoneStr = b.getString("phone", null);
        String descriptionStr = b.getString("description", null);

        if (nameStr == null || phoneStr == null || descriptionStr == null) {
            finish();
            return;
        }

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);

        name.setText(nameStr);
        phone.setText(phoneStr);
        description.setText(descriptionStr);

        ((Button)findViewById(R.id.back)).setOnClickListener(v -> finish());
        ((Button)findViewById(R.id.save)).setOnClickListener(v -> {
            String name = this.name.getText().toString().trim();
            String phone = this.phone.getText().toString().trim();
            String description = this.description.getText().toString().trim();

            if (name.length() == 0) {
                this.name.setText("");
                Toast.makeText(this, "Incorrect Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() == 0) {
                this.phone.setText("");
                Toast.makeText(this, "Incorrect Phone", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent res = new Intent();
            res.putExtra("name", name);
            res.putExtra("phone", phone);
            res.putExtra("description", description);
            setResult(RESULT_OK, res);
            finish();
        });
    }
}