package com.example.homework_17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView text;
    GestureDetector detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        detector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            text.setText("Touch");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            text.setText("Show Press");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            text.setText("Single Tap Up");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            text.setText("Scroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            text.setText("Long Press");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            text.setText("Fling");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            text.setText("Double Tap");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            text.setText("Double Tap Event");
            return true;
        }

//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            text.setText("Single Tap Confirmed");
//            return true;
//        }
    }
}