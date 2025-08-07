package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents an emergency contact for the user.
 * This contact can be reached in times of crisis or emergency.
 */
public class EmergencyContact {

    private String id;

    private String name;

    private String relationship;

    private String phone;

    public EmergencyContact() {
        // Default constructor
    }

    /**
     * Constructs a new EmergencyContact with the given details.
     *
     * @param id           unique identifier for the contact
     * @param name         name of the contact
     * @param relationship relationship to the user
     * @param phone        contact phone number
     */
    public EmergencyContact(String id, String name, String relationship, String phone) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
