package com.example.homework_11;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    EditText timeField;
    final String CHANEL_ID = "TimerChanel";
    int idd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeField = findViewById(R.id.time);
        findViewById(R.id.start).setOnClickListener(this::onStartClick);
    }

    private void onStartClick(View v) {
        String str = timeField.getText().toString().trim();
        timeField.setText("");
        if (str.length() == 0) return;

        int seconds = Integer.parseInt(str);
        if (seconds == 0) {
            MakeNotification(seconds);
            return;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MakeNotification(seconds);
            }
        }, seconds * 1000L);
    }

    private void MakeNotification(int seconds) {
        NotificationChannel chanel = new NotificationChannel(CHANEL_ID, CHANEL_ID, NotificationManager.IMPORTANCE_HIGH);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();

        chanel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getPackageName()+"/"+R.raw.tuturu), audioAttributes);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(chanel);

        Notification notification = new Notification.Builder(MainActivity.this,CHANEL_ID)
                .setContentTitle("Homework 11")
                .setContentText("Timer: " + seconds + " seconds")
                .setTicker("new notification!")
                .setChannelId(CHANEL_ID)
                .setSmallIcon(android.R.drawable.star_on)
                .setOngoing(true)
                .build();

        notificationManager.notify(idd++, notification);    }
}