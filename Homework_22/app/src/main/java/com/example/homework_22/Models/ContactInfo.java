package com.example.homework_22.Models;

import androidx.annotation.Nullable;

public class ContactInfo implements Comparable<ContactInfo> {
    public Contact contact;
    public boolean isSelected;

    public ContactInfo() {
        contact = null;
        isSelected = false;
    }

    public ContactInfo(Contact contact) {
        this.contact = contact;
        isSelected = false;
    }

    @Override
    public int compareTo(ContactInfo contactInfo) {
        if (contactInfo == null) return 1;
        return -contact.name.compareToIgnoreCase(contactInfo.contact.name);
    }
}
