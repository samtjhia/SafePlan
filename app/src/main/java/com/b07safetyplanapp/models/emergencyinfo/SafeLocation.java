package com.b07safetyplanapp;

public class SafeLocation {
    //private String id;
    private String name;
    private String address;
    private String notes;

    SafeLocation(){
    }
    SafeLocation( String name, String address, String notes){
        //this.id = id;
        this.name = name;
        this.address = address;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }
}
