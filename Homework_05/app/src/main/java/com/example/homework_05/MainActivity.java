package com.example.homework_05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int READ_MEDIA_IMAGES_CODE = 100;

    RecyclerView imageList;
    final ImageListAdapter imageListAdapter = new ImageListAdapter();
    LinearLayout noAccessLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPermissions()) {
            updateImages();
        }
    }

    private void init() {
        imageList = findViewById(R.id.ImageList);
        imageList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        imageList.setAdapter(imageListAdapter);
        noAccessLayout = findViewById(R.id.NoAccessLayout);
        ((Button)findViewById(R.id.AccessReady)).setOnClickListener(this::AccessButtonClick);
    }

    private void AccessButtonClick(View v) {
        if (checkPermissions()) {
            updateImages();
        }
    }

    private boolean checkPermissions() {
        if (!HasPermission(Manifest.permission.READ_MEDIA_IMAGES)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_MEDIA_IMAGES }, READ_MEDIA_IMAGES_CODE);
            return false;
        }

        return true;
    }

    private boolean HasPermission(String permission) {
        return ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_MEDIA_IMAGES_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) updateImages();
            else NoImageAccess();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateImages() {
        noAccessLayout.setVisibility(View.GONE);
        imageListAdapter.data = getImages();

        synchronized(imageListAdapter){
            imageListAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<ImageData> getImages() {
        ArrayList<ImageData> listOfAllImages = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)){
            if (cursor == null) {
                return new ArrayList<>();
            }

            int column_index_data = cursor.getColumnIndexOrThrow(projection[0]);
            int column_index_name = cursor.getColumnIndexOrThrow(projection[1]);

            String imagePath, imageName;
            while (cursor.moveToNext()) {
                imagePath = cursor.getString(column_index_data);
                imageName = cursor.getString(column_index_name);

                listOfAllImages.add(new ImageData(imagePath, imageName));
            }

            return listOfAllImages;
        } catch (Exception e) {
            Log.e("my_log", e.getMessage());
            return new ArrayList<>();
        }
    }

    private void NoImageAccess() {
        noAccessLayout.setVisibility(View.VISIBLE);
    }
}