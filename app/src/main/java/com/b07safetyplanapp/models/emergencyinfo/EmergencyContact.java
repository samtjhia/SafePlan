package com.b07safetyplanapp.models.emergencyinfo;

public class EmergencyContact {
    private String id;
    private String name;
    private String relationship;
    private String phone;

    public EmergencyContact() {
        // Default constructor
    }

    public EmergencyContact(String id, String name, String relationship, String phone) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }

    // Getters
    public String getId() {
        return id;
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

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}