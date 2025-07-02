package com.example.homework_20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
public class MainActivity extends AppCompatActivity {

    RelativeLayout mainLayout;
    TextView gpsNoPermission;

    TextView latitude, longitude, gps;


    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);
        gpsNoPermission = findViewById(R.id.permission_status);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        gps = findViewById(R.id.gps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            startTrackingLocation();
        }
    }

    private void startTrackingLocation() {
        gpsNoPermission.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

        // Проверяем статус GPS
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps.setText("ON");
        } else {
            gps.setText("OFF");
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // Обновление координат
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            MainActivity.this.latitude.setText(Double.toString(latitude));
            MainActivity.this.longitude.setText(Double.toString(longitude));
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            gps.setText("ON");
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            gps.setText("OFF");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTrackingLocation();
        } else {
            Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}