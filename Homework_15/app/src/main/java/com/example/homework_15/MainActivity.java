package com.example.homework_15;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homework_15.Models.Database;
import com.example.homework_15.Models.User;
import com.example.homework_15.Models.UserListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText saveText;
    final String SAVE_TEXT_KEY = "SAVE_TEXT";
    ArrayList<User> users = new ArrayList<>();
    UserListAdapter listAdapter;

    ActivityResultLauncher<Intent> updateUserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::OnUserUpdated);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        SharedPreferences savePref = getSharedPreferences(SAVE_TEXT_KEY, MODE_PRIVATE);
        saveText = findViewById(R.id.new_name);
        saveText.setText(savePref.getString(SAVE_TEXT_KEY, ""));

        Database.Initialize(this);
        users = Database.getUsers(this);

        listAdapter = new UserListAdapter(users);
        listAdapter.onDelete = this::onDeleteUser;
        listAdapter.onEdit = this::onEditUser;

        ((RecyclerView)findViewById(R.id.list)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView)findViewById(R.id.list)).setAdapter(listAdapter);

        findViewById(R.id.add).setOnClickListener(this::onAddClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        SharedPreferences save = getSharedPreferences(SAVE_TEXT_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit(); //создаём редактор shared preferences
        editor.putString(SAVE_TEXT_KEY, saveText.getText().toString());
//        editor.commit(); //применение редактирования shared preferences
        editor.apply();

        super.onStop();
    }

    private void onAddClick(View v) {
        String name = saveText.getText().toString().trim();
        saveText.setText("");
        if (name.length() == 0) return;

        int id = Database.createUser(this, name);
        if (id == -1) return;

        users.add(new User(id, name));
        listAdapter.notifyItemInserted(users.size() - 1);
    }

    private void onDeleteUser(User user) {
        Database.deleteUser(this, user.id);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id != user.id) continue;

            users.remove(i);
            listAdapter.notifyItemRemoved(i);
            break;
        }
    }

    private void onEditUser(User user) {
        Intent intent = new Intent(this, UserUpdate.class);
        intent.putExtra("id", user.id);
        intent.putExtra("name", user.name);
        updateUserLauncher.launch(intent);
    }

    private void OnUserUpdated(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK) return;
        Intent data = result.getData();
        if (data == null) return;

        Bundle b = data.getExtras();
        if (b == null) return;

        int id = b.getInt("id");
        String name = b.getString("name");
        Database.updateUser(this, id, name);

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id != id) continue;

            users.get(i).name = name;
            listAdapter.notifyItemChanged(i);
            break;
        }
    }


}