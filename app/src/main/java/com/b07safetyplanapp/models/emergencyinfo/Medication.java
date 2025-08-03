package com.b07safetyplanapp.models.emergencyinfo;

public class Medication {
    private String name;
    private String dosage;

    private  String medicationId;

    Medication(){
    }
    public Medication(String name, String dosage, String  contactId){
        this.name = name;
        this.dosage = dosage;
        this.medicationId =  contactId;


    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }
}
