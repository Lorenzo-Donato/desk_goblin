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
        // Pacientes com maior severityScore terão prioridade na MaxHeap
        return Integer.compare(this.severityScore, other.severityScore);
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ", Severidade: " + severityScore + ")";
    }
}
