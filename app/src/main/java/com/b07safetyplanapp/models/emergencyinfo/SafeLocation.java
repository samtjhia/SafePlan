package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents a safe location that a user can save in their emergency safety plan.
 * This may include places like a friend's house, shelter, or other trusted location.
 */
public class SafeLocation {

    /**
     * The name or label of the safe location (e.g., "Friend's House").
     */
    private String name;

    /**
     * The physical address of the safe location.
     */
    private String address;

    /**
     * Any additional notes or context about the location.
     */
    private String notes;

    /**
     * Unique identifier for this location, typically used as a key in databases.
     */
    private String id;

    /**
     * Default constructor required for Firebase or serialization.
     */
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

    /**
     * Gets the name of the safe location.
     *
     * @return the location name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the safe location.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the safe location.
     *
     * @return the location address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the safe location.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets any additional notes for the location.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes for the location.
     *
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the unique identifier for the location.
     *
     * @return the location ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the location.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
