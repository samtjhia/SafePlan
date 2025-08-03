package com.b07safetyplanapp.models.emergencyinfo;

public class SafeLocation {
    //private String id;
    private String name;
    private String address;
    private String notes;

    private String id;

    SafeLocation(){
    }
    public SafeLocation(String name, String address, String notes, String id){
        //this.id = id;
        this.name = name;
        this.address = address;
        this.notes = notes;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setId(String id) {
        this.id = id;
    }
}

