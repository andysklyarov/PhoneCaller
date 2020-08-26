package com.sklyarov.phonecaller.contacts;

public class Contacts {

    private String name;
    private int value;

    public Contacts(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return String.valueOf(value);
    }
}
