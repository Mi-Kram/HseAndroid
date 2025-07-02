package com.example.homework_09;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button[] group1 = new Button[3];
    Button[] group2 = new Button[3];
    Button[] group3 = new Button[3];

    Button[] currentGroup = new Button[0];
    Button currentButton = null;
    final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        group1[0] = findViewById(R.id.btn1);
        group1[1] = findViewById(R.id.btn5);
        group1[2] = findViewById(R.id.btn9);

        group2[0] = findViewById(R.id.btn2);
        group2[1] = findViewById(R.id.btn3);
        group2[2] = findViewById(R.id.btn6);

        group3[0] = findViewById(R.id.btn4);
        group3[1] = findViewById(R.id.btn7);
        group3[2] = findViewById(R.id.btn8);

        for (Button btn : group1) registerForContextMenu(btn);
        for (Button btn : group2) registerForContextMenu(btn);
        for (Button btn : group3) registerForContextMenu(btn);

        for (Button btn : group1) btn.setOnLongClickListener(this::LongClickHandler);
        for (Button btn : group2) btn.setOnLongClickListener(this::LongClickHandler);
        for (Button btn : group3) btn.setOnLongClickListener(this::LongClickHandler);
    }

    public boolean LongClickHandler(View v) {
        currentButton = (Button) v;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        for (Button btn : currentGroup) btn.setVisibility(View.INVISIBLE);

        int id = item.getItemId();
        if (id == R.id.group1) currentGroup = group1;
        else if (id == R.id.group2) currentGroup = group2;
        else if (id == R.id.group3) currentGroup = group3;

        for (Button btn : currentGroup) btn.setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ctxitem1) SetStyle1(currentButton);
        else if (id == R.id.ctxitem2) SetStyle2(currentButton);
        else if (id == R.id.ctxitem3) SetStyle3(currentButton);
        else if (id == R.id.reset) Reset(currentButton);
        return super.onContextItemSelected(item);
    }

    private void Reset(Button btn) {
        btn.setBackgroundColor(0xFF4A1C9B);
        btn.setScaleX(1);
        btn.setScaleY(1);
        btn.setRotation(0f);
        btn.setTextSize(15);
    }

    private void SetStyle1(Button btn) {
        btn.setBackgroundColor(0xFFFF0000);
    }

    private void SetStyle2(Button btn) {
        float scale = rand.nextFloat() * 1.2f + 0.6f;
        btn.setScaleX(scale);
        btn.setScaleY(scale);

        float rotation = rand.nextFloat() * 20f + 10f;
        if (rand.nextInt(100) < 50) rotation = -rotation;
        btn.setRotation(rotation);
    }

    private void SetStyle3(Button btn) {
        btn.setTextSize(20);
    }
}