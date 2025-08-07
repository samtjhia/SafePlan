package com.b07safetyplanapp;


/**
 * Represents an emergency contact with a name, relationship, and phone number.
 */
public class Contact {
    private String name;
    private String relationship;
    private String phone;

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
