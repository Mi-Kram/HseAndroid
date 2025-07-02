package com.example.homework_21;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Gallery {
    private static Gallery instance = null;
    private ArrayList<Bitmap> images = new ArrayList<>();
    private Gallery() {
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public static Gallery getInstance() {
        if (instance == null) instance = new Gallery();
        return instance;
    }
}
