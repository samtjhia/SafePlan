package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents a medication that a user may need to document in their emergency safety plan.
 * This includes the name of the medication, dosage instructions, and a unique identifier.
 */
public class Medication {

    private String name;

    private String dosage;

    private String medicationId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
}
