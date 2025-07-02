package com.example.homework_22;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework_22.Models.Contact;
import com.example.homework_22.Models.ContactInfo;

import java.util.ArrayList;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder> {

    private SortedList<ContactInfo> contacts;
    private SortedList<ContactInfo> filtered;
    private String filter = "";
    private ContactInfo selectedContact = null;

    ContactAction onCall = null, onSms = null, onInfo = null;

    interface ContactAction {
        void notify(Contact contact);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final View layout;

        private final LinearLayout buttons;
        private final ImageButton call;
        private final ImageButton sms;
        private final ImageButton info;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            name = view.findViewById(R.id.name);
            buttons = view.findViewById(R.id.buttons);
            call = view.findViewById(R.id.call);
            sms = view.findViewById(R.id.sms);
            info = view.findViewById(R.id.info);
        }

        public TextView getName() {
            return name;
        }

        public View getLayout() {
            return layout;
        }
    }

    private int getIndex(ContactInfo contact) {
        if (contact == null) return -1;
        for (int i = 0; i < filtered.size(); i++) {
            if (filtered.get(i) == contact) return i;
        }
        return -1;
    }

    private int getIndex(Contact contact) {
        for (int i = 0; i < filtered.size(); i++) {
            if (filtered.get(i).contact == contact) return i;
        }
        return -1;
    }


    public ContactsListAdapter(ArrayList<Contact> data) {
        contacts = new SortedList<>(data.size());
        for (Contact contact : data) {
            contacts.insert(new ContactInfo(contact));
        }
        filtered = new SortedList<>(contacts);
    }

    public void addContact(Contact contact) {
        contacts.insert(new ContactInfo(contact));

        if (contact.name.toLowerCase().contains(filter))
            notifyItemInserted(filtered.insert(new ContactInfo(contact)));
    }

    public void clearSelection() {
        if (selectedContact == null) return;
        selectedContact.isSelected = false;
        notifyItemChanged(getIndex(selectedContact));
        selectedContact = null;
    }

    public void select(Contact contact) {
        int idx = getIndex(contact);
        ContactInfo info = idx == -1 ? null : filtered.get(idx);
        select(info);
    }

    private void select(ContactInfo contact) {
        if (selectedContact == contact) return;
        boolean isNoSelection = selectedContact == null;

        if (!isNoSelection) {
            selectedContact.isSelected = false;
            notifyItemChanged(getIndex(selectedContact));
            selectedContact = null;
        }

        int idx = getIndex(contact);
        if (contact == null || idx == -1) return;

        selectedContact = contact;
        selectedContact.isSelected = true;
        notifyItemChanged(idx);
    }

    public ArrayList<Contact> formAllContactList() {
        ArrayList<Contact> result = new ArrayList<>();
        for (ContactInfo info : contacts) {
            result.add(info.contact);
        }
        return result;
    }

    public Contact getSelectedContact() {
        return selectedContact == null ? null : selectedContact.contact;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(String newFilter) {
        if (newFilter == null) newFilter = "";

        if (filter.compareToIgnoreCase(newFilter) == 0) return;
        filter = newFilter.toLowerCase();

        int size = filtered.size();
        filtered.clear();

        for (ContactInfo contact : contacts) {
            if (contact.contact.name.toLowerCase().contains(filter)) {
                filtered.pushEnd(contact);
            }
        }

        notifyDataSetChanged();
    }

    public void updateContact(Contact contact, Contact newData) {
        int i = getIndex(contact);

        if (i != -1) {
            filtered.get(i).contact.name = newData.name;
            filtered.get(i).contact.phone = newData.phone;
            filtered.get(i).contact.description = newData.description;
            notifyItemChanged(i);
            return;
        }


        for (i = 0; i < contacts.size(); i++) {
            if (filtered.get(i).contact == contact) {
                filtered.get(i).contact.name = newData.name;
                filtered.get(i).contact.phone = newData.phone;
                filtered.get(i).contact.description = newData.description;
                return;
            }
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        holder.layout.setOnClickListener(v -> {
            ContactInfo contact = (ContactInfo) v.getTag();

            if (contact == selectedContact) clearSelection();
            else select(contact);
        });

        holder.call.setOnClickListener(v -> {
            if (onCall == null || selectedContact == null) return;
            onCall.notify(selectedContact.contact);
        });

        holder.sms.setOnClickListener(v -> {
            if (onSms == null || selectedContact == null) return;
            onSms.notify(selectedContact.contact);
        });

        holder.info.setOnClickListener(v -> {
            if (onInfo == null || selectedContact == null) return;
            onInfo.notify(selectedContact.contact);
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ContactInfo contact = filtered.get(position);
        viewHolder.getName().setText(contact.contact.name);
        viewHolder.getLayout().setTag(contact);
        viewHolder.getLayout().setBackgroundResource(contact.isSelected ? R.color.selected : R.color.transparent);
        viewHolder.buttons.setVisibility(contact.isSelected ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }
}
