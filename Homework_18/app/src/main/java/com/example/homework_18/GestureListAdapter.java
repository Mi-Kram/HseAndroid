package com.example.homework_18;

import android.gesture.Gesture;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GestureListAdapter extends RecyclerView.Adapter<GestureListAdapter.ViewHolder> {

    public GestureEvent onDelete = null;
    private ArrayList<Pair<String, Gesture>> gestures;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageButton delete;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            delete = view.findViewById(R.id.delete);
        }

        public TextView getName() {
            return name;
        }
    }

    public interface GestureEvent {
        void onGesture(int i);
    }

    public GestureListAdapter(ArrayList<Pair<String, Gesture>> data) {
        gestures = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.delete.setOnClickListener(v -> {
            if (onDelete != null) onDelete.onGesture((int)v.getTag());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d("mines", "onBindViewHolder " + position);
        viewHolder.getName().setText(gestures.get(position).first);
        viewHolder.delete.setTag(position);
    }

    @Override
    public int getItemCount() {
        return gestures.size();
    }
}