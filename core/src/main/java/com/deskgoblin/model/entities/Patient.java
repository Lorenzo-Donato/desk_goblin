package com.deskgoblin.model.entities;

public class Patient implements Comparable<Patient> {
    private String id;
    private String name;
    private String symptom;
    private int severityScore;

    public Patient(String id, String name, String symptom, int severityScore) {
        this.id = id;
        this.name = name;
        this.symptom = symptom;
        this.severityScore = severityScore;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSymptom() { return symptom; }
    public int getSeverityScore() { return severityScore; }

    @Override
    public int compareTo(Patient other) {
        return Integer.compare(this.severityScore, other.severityScore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id.equals(patient.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ", Severidade: " + severityScore + ")";
    }
}
