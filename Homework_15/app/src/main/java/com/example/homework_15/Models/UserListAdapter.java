package com.example.homework_15.Models;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework_15.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    public interface UserAction {
        void onAction(User user);
    }

    public UserAction onDelete = null, onEdit = null;
    private ArrayList<User> users;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;
        public final Button editButton;
        public final Button deleteButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.name);
            editButton = (Button) view.findViewById(R.id.edit);
            deleteButton = (Button) view.findViewById(R.id.delete);
        }
    }

    public UserListAdapter(ArrayList<User> data) {
        users = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item, viewGroup, false);

        view.findViewById(R.id.edit).setOnClickListener((View v) -> {
            if (onEdit != null) onEdit.onAction((User)v.getTag());
        });

        view.findViewById(R.id.delete).setOnClickListener((View v) -> {
            if (onDelete != null) onDelete.onAction((User)v.getTag());
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.nameTextView.setText(users.get(position).name);
        viewHolder.editButton.setTag(users.get(position));
        viewHolder.deleteButton.setTag(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}