package com.example.homework_05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    public ArrayList<ImageData> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.Text);
            imageView = view.findViewById(R.id.Image);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public ImageListAdapter() {
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = new File(data.get(position).path);
        int removed = 0;

        while (!file.exists()) {
            removed++;
            data.remove(position);
            if (position == data.size()) {
                return;
            }
            file = new File(data.get(position).path);
        }

        if (removed != 0) {
            notifyItemRangeRemoved(position, removed);
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        holder.getImageView().setImageBitmap(bitmap);
        holder.getTextView().setText(data.get(position).name);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}