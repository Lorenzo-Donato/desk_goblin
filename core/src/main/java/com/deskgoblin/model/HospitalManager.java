package com.deskgoblin.model;

import com.deskgoblin.model.datastructures.*;
import com.deskgoblin.model.entities.*;

public class HospitalManager {
    
    private AVLTree<String, Patient> patientRecords;
    private MinHeap<Patient> patientQueue;
    private HashTable<String, Bed> beds;
    private QueueWithTwoStacks<MedicalProcess> medicalProcesses;

    public HospitalManager() {
        patientRecords = new AVLTree<>();
        patientQueue = new MinHeap<>();
        beds = new HashTable<>();
        medicalProcesses = new QueueWithTwoStacks<>();
        
        initializeBeds();
    }

    private void initializeBeds() {
        for (int i = 1; i <= 12; i++) {
            beds.put("M" + i, new Bed("M" + i));
        }
    }

    public SinglyLinkedList<Patient> getAllPatients() {
        return patientRecords.inOrder();
    }

    public SinglyLinkedList<Patient> getPatientQueueSnapshot() {
        SinglyLinkedList<Patient> result = new SinglyLinkedList<>();
        SinglyLinkedList<Patient> temp = new SinglyLinkedList<>();
        while (patientQueue.size() > 0) {
            Patient p = patientQueue.extractMin();
            temp.pushBack(p);
            result.pushBack(p);
        }
        while (!temp.isEmpty()) {
            patientQueue.insert(temp.popFront());
        }
        return result;
    }

    public void addPatientToQueue(Patient p) {
        patientQueue.insert(p);
    }

    public void registerPatient(Patient p) {
        patientRecords.insert(p.getId(), p);
        patientQueue.insert(p);
        System.out.println("[Manager] Paciente registrado na AVL e adicionado ao MinHeap: " + p.getName());
    }

    public Patient getPatientRecord(String id) {
        return patientRecords.search(id);
    }

    public Patient peekNextPatient() {
        return patientQueue.peekMin();
    }

    public Patient popNextPatient() {
        return patientQueue.extractMin();
    }

    public Bed getBed(String id) {
        return beds.get(id);
    }

    public MedicalProcess getActiveProcessForBed(String bedId) {
        int size = medicalProcesses.size();
        MedicalProcess foundProcess = null;
        
        for (int i = 0; i < size; i++) {
            MedicalProcess process = medicalProcesses.dequeue();
            if (process.getBed().getId().equals(bedId)) {
                foundProcess = process;
            }
            medicalProcesses.enqueue(process);
        }
        return foundProcess;
    }

    public boolean assignPatientToBed(Patient p, String bedId, float processTime) {
        Bed bed = beds.get(bedId);
        if (bed != null && !bed.isOccupied()) {
            bed.setPatient(p);
            medicalProcesses.enqueue(new MedicalProcess(p, bed));
            System.out.println("[Manager] Paciente " + p.getName() + " associado a maca " + bedId);
            return true;
        }
        System.out.println("[Manager] Falha ao associar paciente " + p.getName() + " a maca " + bedId);
        return false;
    }

    public boolean removePatientFromBed(String bedId) {
        Bed bed = beds.get(bedId);
        if (bed != null && bed.isOccupied()) {
            System.out.println("[Manager] Paciente " + bed.getPatient().getName() + " retirado da maca " + bedId);
            bed.setPatient(null);
            return true;
        }
        return false;
    }

    public boolean updateMedicalProcesses(float delta) {
        boolean processFinished = false;
        int size = medicalProcesses.size();
        
        for (int i = 0; i < size; i++) {
            MedicalProcess process = medicalProcesses.dequeue();
            process.update(delta);
            
            if (process.isFinished()) {
                System.out.println("[Manager] Processo médico concluído para: " + process.getPatient().getName());
                processFinished = true;
                // --- LIBERAÇÃO AUTOMÁTICA DA MACA (Já que o botão vermelho foi removido) ---
                process.getBed().setPatient(null);
            } else {
                medicalProcesses.enqueue(process);
            }
        }
        return processFinished;
    }
}