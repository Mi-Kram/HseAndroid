package com.example.homework_22;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.homework_22.Models.Contact;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    final String SAVE_TEXT_KEY = "HW21_CONTACTS";
    EditText search;
    ContactsListAdapter contactsAdapter;
    ImageButton searchClear;

    private class SearchTimeoutTask extends TimerTask {
        @Override
        public void run() {
            searchTimeout = null;
            MainActivity.this.runOnUiThread(() -> {
                contactsAdapter.setFilter(search.getText().toString());
            });
        }
    };

    Timer searchTimeout = null;

    TextWatcher searchTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            searchClear.setVisibility(cs.length() == 0 ? View.GONE : View.VISIBLE);

            if (searchTimeout != null) {
                searchTimeout.cancel();
            }

            searchTimeout = new Timer();
            searchTimeout.schedule(new SearchTimeoutTask(), 500);
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };

    Contact infoContact = null;
    ActivityResultLauncher<Intent> onInfoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != Activity.RESULT_OK) return;

                    if (infoContact == null) return;

                    Intent data = result.getData();
                    if (data == null) return;

                    Bundle b = data.getExtras();
                    if (b == null) return;

                    String name = b.getString("name", null);
                    String phone = b.getString("phone", null);
                    String description = b.getString("description", null);

                    if (name == null || phone == null || description == null) return;

                    contactsAdapter.updateContact(infoContact, new Contact(name, phone, description));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        search = findViewById(R.id.search_text);
        search.addTextChangedListener(searchTextWatcher);
        searchClear = findViewById(R.id.search_clear);
        searchClear.setOnClickListener(this::onSearchClearClick);

        ArrayList<Contact> contacts = loadContacts();
        contactsAdapter = new ContactsListAdapter(contacts);
        contactsAdapter.onCall = this::onCallClick;
        contactsAdapter.onSms = this::onSmsClick;
        contactsAdapter.onInfo = this::onInfoClick;
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactsAdapter);
    }


    public static class TMPContacts {
        public ArrayList<Contact> contacts;
    }


    private ArrayList<Contact> loadContacts() {
        SharedPreferences savePref = getSharedPreferences(SAVE_TEXT_KEY, MODE_PRIVATE);
        String jsonString = savePref.getString(SAVE_TEXT_KEY, "");

        if (jsonString.length() == 0) return new ArrayList<>();

        Gson gson = new Gson();
        try {
            TMPContacts tmp = gson.fromJson(jsonString, TMPContacts.class);
            if (tmp == null || tmp.contacts == null) throw new Exception();
            return tmp.contacts;
        } catch (Exception ex) {
            Toast.makeText(this, "Did not manage to load data", Toast.LENGTH_SHORT).show();
        }

        return new ArrayList<>();
    }

    @Override
    protected void onStop() {
        TMPContacts tmp = new TMPContacts();
        tmp.contacts = contactsAdapter.formAllContactList();

        Gson gson = new Gson();;
        String jsonString = gson.toJson(tmp);
        Log.d("mines", "save: " + jsonString);

        SharedPreferences save = getSharedPreferences(SAVE_TEXT_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit(); //создаём редактор shared preferences

        editor.putString(SAVE_TEXT_KEY, jsonString);
        editor.apply();

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add) {
            contactsAdapter.clearSelection();
            createContact();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSearchClearClick(View v) {
        contactsAdapter.setFilter("");
        search.setText("");
    }

    private void onCallClick(Contact contact) {
        contactsAdapter.clearSelection();

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.phone));
        if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No permissions for calls", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSmsClick(Contact contact) {
        contactsAdapter.clearSelection();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.phone));
        startActivity(intent);
    }

    private void onInfoClick(Contact contact) {
        contactsAdapter.clearSelection();
        infoContact = contact;

        Intent intent = new Intent(this, ContactInfo.class);
        intent.putExtra("name", contact.name);
        intent.putExtra("phone", contact.phone);
        intent.putExtra("description", contact.description);
        onInfoLauncher.launch(intent);
    }

    private void createContact() {
        View userView = getLayoutInflater().inflate(R.layout.create_contact, null);
        EditText name = userView.findViewById(R.id.name);
        EditText phone = userView.findViewById(R.id.phone);
        EditText description = userView.findViewById(R.id.description);

        new AlertDialog.Builder(this)
                .setTitle("Create contact")
                .setView(userView)
                .setPositiveButton("Create", (dialog, which) -> {
                    String nameStr = name.getText().toString().trim();
                    if (nameStr.length() == 0) {
                        dialog.dismiss();
                        name.setText("");
                        Toast.makeText(this, "Incorrect Name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String phoneStr = phone.getText().toString().trim();
                    if (phoneStr.length() == 0) {
                        dialog.dismiss();
                        phone.setText("");
                        Toast.makeText(this, "Incorrect Phone", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    contactsAdapter.addContact(new Contact(nameStr, phoneStr, description.getText().toString().trim()));
                })
                .setNegativeButton("Cancel", null)
                .create().show();
    }


}