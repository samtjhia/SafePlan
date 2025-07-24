package com.b07safetyplanapp;

public class Medication {
    private String name;
    private String dosage;

    Medication(){
    }
    Medication(String name, String dosage){
        this.name = name;
        this.dosage = dosage;

    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }
}
