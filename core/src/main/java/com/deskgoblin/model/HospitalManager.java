package com.deskgoblin.model;

import com.deskgoblin.model.datastructures.*;
import com.deskgoblin.model.entities.*;

/**
 * Controller principal que amarra as estruturas de dados à lógica do jogo.
 */
public class HospitalManager {
    
    // 1. Pergaminho: Armazena todos os pacientes registrados (BST / AVL Tree)
    // Chave: ID do Paciente
    private AVLTree<String, Patient> patientRecords;
    
    // 2. Orbe Esquerdo: Fila de prioridade de pacientes (Min Heap)
    private MinHeap<Patient> patientQueue;
    
    // 3. Orbe Direito: Gerenciamento de Macas (Hash Table)
    // Chave: ID da Maca
    private HashTable<String, Bed> beds;

    // 4. Processos Médicos: Fila de processos acontecendo no momento
    private QueueWithTwoStacks<MedicalProcess> medicalProcesses;

    public HospitalManager() {
        patientRecords = new AVLTree<>();
        patientQueue = new MinHeap<>();
        beds = new HashTable<>();
        medicalProcesses = new QueueWithTwoStacks<>();
        
        initializeBeds();
    }

    private void initializeBeds() {
        // Criar algumas macas iniciais
        for (int i = 1; i <= 3; i++) {
            beds.put("M" + i, new Bed("M" + i));
        }
    }

    // --- NOVO MÉTODO ADICIONADO PARA CORRIGIR O ERRO ---
    public SinglyLinkedList<Patient> getAllPatients() {
        return patientRecords.inOrder();
    }

    /**
     * S10: Salvar cadastro na BST -> Mandar os dados para o min heap
     */
    public void registerPatient(Patient p) {
        patientRecords.insert(p.getId(), p);
        patientQueue.insert(p);
        System.out.println("[Manager] Paciente registrado na AVL e adicionado ao MinHeap: " + p.getName());
    }

    /**
     * S12: Filtrar os pacientes pelo ID inserido (Pesquisa na AVL)
     */
    public Patient getPatientRecord(String id) {
        return patientRecords.search(id);
    }

    /**
     * Retorna o próximo paciente do Orbe Esquerdo (primeira posição da Min Heap) sem remover
     */
    public Patient peekNextPatient() {
        return patientQueue.peekMin();
    }

    /**
     * Remove o próximo paciente do Orbe Esquerdo (Min Heap)
     */
    public Patient popNextPatient() {
        return patientQueue.extractMin();
    }

    /**
     * S19: Pesquisar maca (Orbe Direito - HashTable)
     */
    public Bed getBed(String id) {
        return beds.get(id);
    }

    /**
     * S17: Associar paciente a maca -> Começar processos médicos
     */
    public boolean assignPatientToBed(Patient p, String bedId, float processTime) {
        Bed bed = beds.get(bedId);
        if (bed != null && !bed.isOccupied()) {
            bed.setPatient(p);
            medicalProcesses.enqueue(new MedicalProcess(p, bed, processTime));
            System.out.println("[Manager] Paciente " + p.getName() + " associado a maca " + bedId);
            return true;
        }
        System.out.println("[Manager] Falha ao associar paciente " + p.getName() + " a maca " + bedId);
        return false;
    }

    /**
     * S15: Retirar o paciente da maca selecionada (Botão Direito)
     */
    public boolean removePatientFromBed(String bedId) {
        Bed bed = beds.get(bedId);
        if (bed != null && bed.isOccupied()) {
            System.out.println("[Manager] Paciente " + bed.getPatient().getName() + " retirado da maca " + bedId);
            bed.setPatient(null);
            return true;
        }
        return false;
    }

    /**
     * Atualiza o estado dos processos médicos.
     * Retorna true se algum processo terminou nesta iteração (para piscar o botão).
     */
    public boolean updateMedicalProcesses(float delta) {
        boolean processFinished = false;
        
        // Como não podemos iterar facilmente na fila sem remover, vamos retirar e recolocar
        int size = medicalProcesses.size();
        for (int i = 0; i < size; i++) {
            MedicalProcess process = medicalProcesses.dequeue();
            process.updateTime(delta);
            if (process.isFinished()) {
                System.out.println("[Manager] Processo médico concluído para: " + process.getPatient().getName());
                processFinished = true;
                // Não recolocamos na fila, o processo terminou
            } else {
                medicalProcesses.enqueue(process);
            }
        }
        
        return processFinished;
    }
}