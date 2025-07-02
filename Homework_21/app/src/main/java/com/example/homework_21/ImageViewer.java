package com.example.homework_21;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewer extends AppCompatActivity {
    Gallery gallery;
    int idx;

    ImageButton left, right;
    TextView current, total;
    ActionBar actionBar;

    PhotoView photoView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        setSupportActionBar(findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        if (actionBar == null) {
            finish();
            return;
        }

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        photoView = findViewById(R.id.photo_view);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        current = findViewById(R.id.current);
        total = findViewById(R.id.total);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        gallery = Gallery.getInstance();
        idx = intent.getIntExtra("idx", -1);

        if (idx < 0 || idx >= gallery.getImages().size()) {
            finish();
            return;
        }

        photoView.setImageBitmap(gallery.getImages().get(idx));
        current.setText(Integer.toString(idx + 1));
        total.setText(Integer.toString(gallery.getImages().size()));
        left.setOnClickListener(this::onLeftClick);
        right.setOnClickListener(this::onRightClick);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @SuppressLint("SetTextI18n")
    private void onLeftClick(View v) {
        if (--idx < 0) idx = gallery.getImages().size() - 1;
        current.setText(Integer.toString(idx + 1));
        photoView.setImageBitmap(gallery.getImages().get(idx));
    }

    @SuppressLint("SetTextI18n")
    private void onRightClick(View v) {
        if (++idx >= gallery.getImages().size()) idx = 0;
        current.setText(Integer.toString(idx + 1));
        photoView.setImageBitmap(gallery.getImages().get(idx));
    }
}