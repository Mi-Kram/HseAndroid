package com.example.homework_22.Models;

import java.io.Serializable;


public class Contact {
    public String name;
    public String phone;
    public String description;

    public Contact() {
        name = "";
        phone = "";
        description = "";
    }

    public Contact(String name, String phone, String description) {
        this.name = name;
        this.phone = phone;
        this.description = description;
    }
}
