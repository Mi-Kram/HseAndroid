package com.example.homework_07_1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity {

    MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("0cc3084e-c726-4daf-af85-f23e6a04d523");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.getMapWindow().getMap().move(new CameraPosition(
                new Point(55.754010, 37.650109),
                17, 0, 0));

        PlacemarkMapObject mark = map.getMapWindow().getMap().getMapObjects().addPlacemark();
        mark.setGeometry(new Point(55.754010, 37.650109));
        mark.setIcon(ImageProvider.fromBitmap(drawSimpleBitmap("HSE")));

        map.getMapWindow().getMap().setRotateGesturesEnabled(true);
        map.getMapWindow().getMap().setScrollGesturesEnabled(true);
        map.getMapWindow().getMap().setTiltGesturesEnabled(true);
        map.getMapWindow().getMap().setZoomGesturesEnabled(true);
    }

    public Bitmap drawSimpleBitmap(String text) {
        int picSize = 128;
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // отрисовка плейсмарка
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(picSize / 2, picSize / 2, picSize / 2, paint);
        // отрисовка текста
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(32);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, picSize / 2, picSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);
        return bitmap;
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        map.onStart();
    }

    @Override
    protected void onStop() {
        map.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}