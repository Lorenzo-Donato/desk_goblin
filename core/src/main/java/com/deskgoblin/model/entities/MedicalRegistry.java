package com.deskgoblin.model.entities;

public class MedicalRegistry {
    private String patientId;
    private boolean admitted;
    private String notes;

    public MedicalRegistry(String patientId, boolean admitted, String notes) {
        this.patientId = patientId;
        this.admitted = admitted;
        this.notes = notes;
    }

    public String getPatientId() { return patientId; }
    public boolean isAdmitted() { return admitted; }
    public String getNotes() { return notes; }
    
    @Override
    public String toString() {
        return "Registro[" + patientId + ": " + (admitted ? "INTERNADO" : "EXPULSO") + " - " + notes + "]";
    }
}
