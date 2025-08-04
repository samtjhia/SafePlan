package com.b07safetyplanapp;

public class Contact {
    private String name;
    private String relationship;
    private String phone;

    public Contact(){

    }
    public Contact(String name,String relationship, String phone) {
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public String getRelationship() {
        return relationship;
    }

    public String getPhone() {
        return phone;
    }
}
