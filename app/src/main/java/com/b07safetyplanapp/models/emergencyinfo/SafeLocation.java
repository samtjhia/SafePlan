package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents a safe location that a user can save in their emergency safety plan.
 * This may include places like a friend's house, shelter, or other trusted location.
 */
public class SafeLocation {

    private String name;

    private String address;

    private String notes;

    private String id;

    public SafeLocation() {
    }

    /**
     * Constructs a SafeLocation with the given details.
     *
     * @param name    the name/label of the safe location
     * @param address the physical address of the location
     * @param notes   additional notes or instructions
     * @param id      unique identifier for this location
     */
    public SafeLocation(String name, String address, String notes, String id) {
        this.name = name;
        this.address = address;
        this.notes = notes;
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
