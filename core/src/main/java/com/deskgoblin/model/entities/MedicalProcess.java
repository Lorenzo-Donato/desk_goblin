package com.deskgoblin.model.entities;

public class MedicalProcess {
    private Patient patient;
    private Bed bed;
    private float timeRemaining;

    public MedicalProcess(Patient patient, Bed bed, float timeRemaining) {
        this.patient = patient;
        this.bed = bed;
        this.timeRemaining = timeRemaining;
    }

    public Patient getPatient() { return patient; }
    public Bed getBed() { return bed; }
    public float getTimeRemaining() { return timeRemaining; }

    public void updateTime(float delta) {
        this.timeRemaining -= delta;
    }

    public boolean isFinished() {
        return this.timeRemaining <= 0;
    }
}
