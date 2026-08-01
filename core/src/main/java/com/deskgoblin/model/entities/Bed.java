package com.deskgoblin.model.entities;

public class Bed {
    private String id;
    private Patient patient;

    public Bed(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public Patient getPatient() { return patient; }
    
    public void setPatient(Patient patient) { this.patient = patient; }

    public boolean isOccupied() { return patient != null; }

    @Override
    public String toString() {
        return "Maca " + id + (isOccupied() ? " (Ocupada por: " + patient.getName() + ")" : " (Livre)");
    }
}
