package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents an emergency contact for the user.
 * This contact can be reached in times of crisis or emergency.
 */
public class EmergencyContact {

    /**
     * Unique identifier for this contact (e.g., Firebase key).
     */
    private String id;

    /**
     * Name of the emergency contact person.
     */
    private String name;

    /**
     * Relationship of the contact to the user (e.g., "Mother", "Friend").
     */
    private String relationship;

    /**
     * Phone number of the emergency contact.
     */
    private String phone;

    /**
     * Default constructor required for Firebase or serialization.
     */
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

    /**
     * Gets the unique ID of the contact.
     *
     * @return the contact ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID of the contact.
     *
     * @param id the contact ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the contact.
     *
     * @return the contact's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the contact.
     *
     * @param name the contact name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the relationship of the contact to the user.
     *
     * @return the relationship string
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * Sets the relationship of the contact to the user.
     *
     * @param relationship the relationship to set
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    /**
     * Gets the phone number of the contact.
     *
     * @return the contact phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the contact.
     *
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
