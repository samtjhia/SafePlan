package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents a medication that a user may need to document in their emergency safety plan.
 * This includes the name of the medication, dosage instructions, and a unique identifier.
 */
public class Medication {

    /**
     * The name of the medication (e.g., "Ibuprofen").
     */
    private String name;

    /**
     * The dosage instructions for the medication (e.g., "200mg twice daily").
     */
    private String dosage;

    /**
     * Unique identifier for this medication, used for referencing or database operations.
     */
    private String medicationId;

    /**
     * Default constructor required for Firebase or other serialization libraries.
     */
    public Medication() {
    }

    /**
     * Constructs a new Medication instance with the provided name, dosage, and ID.
     *
     * @param name         the name of the medication
     * @param dosage       the dosage instructions
     * @param contactId    the unique identifier for the medication (commonly stored as `contactId`)
     */
    public Medication(String name, String dosage, String contactId) {
        this.name = name;
        this.dosage = dosage;
        this.medicationId = contactId;
    }

    /**
     * Gets the name of the medication.
     *
     * @return the medication name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the medication.
     *
     * @param name the medication name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the dosage instructions for the medication.
     *
     * @return the dosage string
     */
    public String getDosage() {
        return dosage;
    }

    /**
     * Sets the dosage instructions for the medication.
     *
     * @param dosage the dosage string to set
     */
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    /**
     * Gets the unique ID associated with this medication.
     *
     * @return the medication ID
     */
    public String getMedicationId() {
        return medicationId;
    }

    /**
     * Sets the unique ID for this medication.
     *
     * @param medicationId the ID to set
     */
    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
}
