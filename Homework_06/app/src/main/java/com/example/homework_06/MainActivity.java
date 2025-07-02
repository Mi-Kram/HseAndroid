package com.example.homework_06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button scaleButton;
    Button translateButton;
    Button openButton;
    Button closeButton;
    ImageView stone;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scaleButton = findViewById(R.id.ScaleButton);
        translateButton = findViewById(R.id.TranslateButton);
        openButton = findViewById(R.id.OpenButton);
        closeButton = findViewById(R.id.CloseButton);
        stone = findViewById(R.id.Stone);

        scaleButton.setOnClickListener(this::OnSizeButtonClick);
        translateButton.setOnClickListener(this::OnTranslateButtonClick);
        openButton.setOnClickListener(this::OnOpenButtonClick);
        closeButton.setOnClickListener(this::OnCloseButtonClick);
    }

    private void OnSizeButtonClick(View v) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);
        scaleButton.startAnimation(anim);
    }

    private void OnTranslateButtonClick(View v) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        translateButton.startAnimation(anim);
    }

    private void OnOpenButtonClick(View v) {
        if (isOpen) return;

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.open);
        stone.startAnimation(anim);
        isOpen = true;
    }

    private void OnCloseButtonClick(View v) {
        if (!isOpen) return;

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.close);
        stone.startAnimation(anim);
        isOpen = false;
    }
}