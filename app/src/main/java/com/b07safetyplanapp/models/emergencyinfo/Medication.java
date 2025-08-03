package com.b07safetyplanapp.models.emergencyinfo;

public class Medication {
    private String name;
    private String dosage;

    Medication(){
    }
    public Medication(String name, String dosage){
        this.name = name;
        this.dosage = dosage;

    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}
