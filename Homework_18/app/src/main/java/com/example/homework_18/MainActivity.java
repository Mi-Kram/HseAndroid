package com.example.homework_18;

import android.app.Activity;
import android.app.AlertDialog;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends Activity {
    private Button createBtn, saveBtn, cancelBtn, clearBtn;
    private TextView text;

    private GestureLibrary gestureLibrary;
    private GestureOverlayView gestureView;

    private GestureListAdapter listAdapter;
    private ArrayList<Pair<String, Gesture>> gestures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File data = new File(getFilesDir(),"data.txt");
        if (!checkDataFile(data)) {
            finish();
            return;
        }

        gestureLibrary = GestureLibraries.fromFile(data);
        if (!gestureLibrary.load()) {

            try {
                if (!data.createNewFile()) throw new Exception("Did not manage to read data file.");
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            return;
        }

        Set<String> keys = gestureLibrary.getGestureEntries();
        for (String key : keys) {
            for (Gesture gesture : gestureLibrary.getGestures(key)) {
                gestures.add(new Pair<>(key, gesture));
            }
        }

        text = findViewById(R.id.text);

        createBtn = findViewById(R.id.create);
        saveBtn = findViewById(R.id.save);
        cancelBtn = findViewById(R.id.cancel);
        clearBtn = findViewById(R.id.clear);

        createBtn.setOnClickListener(this::onCreateClick);
        saveBtn.setOnClickListener(this::onSaveClick);
        cancelBtn.setOnClickListener(this::onCancelClick);
        clearBtn.setOnClickListener(this::onClearClick);

        gestureView = findViewById(R.id.gestureOverlayView);
        gestureView.addOnGesturePerformedListener(this::onGesture);

        listAdapter = new GestureListAdapter(gestures);
        listAdapter.onDelete = this::onGestureDelete;
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    private boolean checkDataFile(File data) {
        if (data.exists() && data.isFile()) return true;

        try {
            if (data.createNewFile()) return true;
            Toast.makeText(this, "Did not manage to open the data file.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void onCreateClick(View v) {
        createBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
        clearBtn.setVisibility(View.VISIBLE);

        gestureView.setFadeOffset(Long.MAX_VALUE);
        text.setText("");
    }

    private void onGestureDelete(int i) {
        gestureLibrary.removeGesture(gestures.get(i).first, gestures.get(i).second);
        gestureLibrary.save();

        gestures.remove(i);
        listAdapter.notifyItemRemoved(i);
    }

    private void onSaveClick(View v) {
        Gesture lastGesture = gestureView.getGesture();
        if (lastGesture == null) return;

        EditText nameEditText = new EditText(this);
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        nameEditText.setHint("Gesture");

        new AlertDialog.Builder(this)
                .setTitle("Enter gesture name")
                .setView(nameEditText)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String name = nameEditText.getText().toString().trim();

                    if (name.length() == 0) {
                        dialog.dismiss();
                        return;
                    }

                    gestureLibrary.addGesture(name, lastGesture);
                    gestureLibrary.save();
                    gestures.add(new Pair<>(name, lastGesture));
                    listAdapter.notifyItemInserted(gestures.size() - 1);
                    onCancelClick(null);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                }).show();
    }

    private void onCancelClick(View v) {
        saveBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
        clearBtn.setVisibility(View.GONE);
        createBtn.setVisibility(View.VISIBLE);

        gestureView.setFadeOffset(420);
        gestureView.clear(false);
    }

    private void onClearClick(View v) {
        gestureView.clear(false);
    }

    private void onGesture(GestureOverlayView v, Gesture g) {
        if (createBtn.getVisibility() == View.GONE) return;

        ArrayList<Prediction> predictions = gestureLibrary.recognize(g);
//        for (int i = 0; i < predictions.size(); i++) {
//            Log.d("mines", String.format("%f - %s", predictions.get(i).score, predictions.get(i).name));
//        }

        if (predictions != null && predictions.size() > 0) {
            Prediction bestPrediction = predictions.get(0);

            if (bestPrediction.score > 1.0) {
                String gestureName = bestPrediction.name;
                text.setText(gestureName);
            } else {
                text.setText("Unknown Gesture");
            }
        }
    }
}