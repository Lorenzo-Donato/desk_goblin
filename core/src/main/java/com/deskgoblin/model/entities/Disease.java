package com.deskgoblin.model.entities;

public class Disease {
    private String name;
    private String symptom;
    private String cure;

    public Disease(String name, String symptom, String cure) {
        this.name = name;
        this.symptom = symptom;
        this.cure = cure;
    }

    public String getName() { return name; }
    public String getSymptom() { return symptom; }
    public String getCure() { return cure; }

    @Override
    public String toString() {
        return name + " [Cura: " + cure + "]";
    }
}
