package com.b07safetyplanapp;


/**
 * Represents an emergency contact with a name, relationship, and phone number.
 */
public class Contact {
    private String name;
    private String relationship;
    private String phone;

    /**
     * Constructs an empty Contact.
     */
    public Contact(){

    }

    /**
     * Constructs a Contact with the specified name, relationship, and phone number.
     *
     * @param name the contact's name
     * @param relationship the contact's relationship to the user
     * @param phone the contact's phone number (10 nums max)
     */
    public Contact(String name,String relationship, String phone) {
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }

    /**
     * Returns the contact's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the contact's relationship to the user.
     *
     * @return the relationship
     */
    public String getRelationship() {
        return relationship;
    }


    /**
     * Returns the contact's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }
}
