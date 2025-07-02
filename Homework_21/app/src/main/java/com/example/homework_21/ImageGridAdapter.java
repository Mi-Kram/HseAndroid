package com.example.homework_21;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ViewHolder> {

    private ArrayList<Bitmap> images;

    public ImageAction onImageClick = null;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton image;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
        }

        public ImageButton getImage() {
            return image;
        }
    }

    public interface ImageAction {
        void notify(Bitmap bitmap);
    }

    public ImageGridAdapter(ArrayList<Bitmap> data) {
        images = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.image.setOnClickListener(v -> {
            if (onImageClick != null) onImageClick.notify((Bitmap)v.getTag());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getImage().setImageBitmap(images.get(position));
        viewHolder.getImage().setTag(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}