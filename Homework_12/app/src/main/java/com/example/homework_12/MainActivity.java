package com.example.homework_12;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    ImageButton resumeBtn, pauseBtn;
    TextView currentTime;
    ProgressBar progress;

    Timer timer;
    TimeUpdater timeUpdater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.music);
        mediaPlayer.setLooping(true);

        ((TextView)findViewById(R.id.totalTime)).setText(msToString(mediaPlayer.getDuration()));
        currentTime = findViewById(R.id.currentTime);
        progress = findViewById(R.id.progress);
        progress.setMax(mediaPlayer.getDuration());

        resumeBtn = findViewById(R.id.resume);
        pauseBtn = findViewById(R.id.pause);

        resumeBtn.setOnClickListener(this::onResumeClick);
        pauseBtn.setOnClickListener(this::onPauseClick);
    }

    private void onResumeClick(View v) {
        resumeBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        mediaPlayer.start();

        timer = new Timer();
        timeUpdater = new TimeUpdater();
        timer.schedule(timeUpdater, 0, 1000);
    }

    private void onPauseClick(View v) {
        pauseBtn.setVisibility(View.GONE);
        resumeBtn.setVisibility(View.VISIBLE);
        mediaPlayer.pause();

        timeUpdater.cancel();
        timer.cancel();
    }

    @SuppressLint("DefaultLocale")
    private String msToString(int ms) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        return String.format("%02d:%02d ", minutes, TimeUnit.MILLISECONDS.toSeconds(ms) - minutes * 60);
    }

    private class TimeUpdater extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                currentTime.setText(msToString(mediaPlayer.getCurrentPosition()));
                progress.setProgress(mediaPlayer.getCurrentPosition());
            });
        }
    }
}