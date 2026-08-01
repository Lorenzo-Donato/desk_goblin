package com.deskgoblin.model;

import com.deskgoblin.model.datastructures.*;
import com.deskgoblin.model.entities.*;

/**
 * Controller principal que amarra as estruturas de dados à lógica do jogo.
 */
public class HospitalManager {
    
    // 1. Fila de Espera Normal
    private QueueWithTwoStacks<Patient> regularQueue;
    
    // 2. Triagem de Emergência (Ativada pelos orbes)
    private MaxHeap<Patient> emergencyQueue;
    
    // 3. Grimório de Doenças (Chave: Sintoma, Valor: Doença)
    private AVLTree<String, Disease> grimoire;
    
    // 4. Arquivo de Decisões do dia (Chave: ID do paciente, Valor: Registro)
    private HashTable<String, MedicalRegistry> dailyRecords;

    public HospitalManager() {
        regularQueue = new QueueWithTwoStacks<>();
        emergencyQueue = new MaxHeap<>();
        grimoire = new AVLTree<>();
        dailyRecords = new HashTable<>();
        
        populateGrimoire();
    }

    /**
     * Alimenta a AVL Tree com doenças iniciais.
     */
    private void populateGrimoire() {
        grimoire.insert("Tosse com Faíscas", new Disease("Fadiga de Fogo", "Tosse com Faíscas", "Xarope de Gelo"));
        grimoire.insert("Pele Escamosa Verde", new Disease("Maldição do Lagarto", "Pele Escamosa Verde", "Erva de Escama"));
        grimoire.insert("Visão Turva e Flutuante", new Disease("Sindrome do Fantasma", "Visão Turva e Flutuante", "Chá de Ancoragem"));
        grimoire.insert("Ossos de Gelatina", new Disease("Mal da Gosma", "Ossos de Gelatina", "Pó de Cálcio Mágico"));
    }

    /**
     * Adiciona paciente na fila normal.
     */
    public void addPatient(Patient p) {
        regularQueue.enqueue(p);
    }
    
    /**
     * Adiciona paciente direto na fila de prioridade (Emergência).
     */
    public void addEmergencyPatient(Patient p) {
        emergencyQueue.insert(p);
    }

    /**
     * Chama o próximo paciente, priorizando casos de emergência (MaxHeap).
     */
    public Patient getNextPatient() {
        if (!emergencyQueue.isEmpty()) {
            System.out.println("[Manager] Retirando paciente da Fila de EMERGÊNCIA!");
            return emergencyQueue.extractMax();
        }
        System.out.println("[Manager] Retirando paciente da Fila Normal.");
        return regularQueue.dequeue();
    }

    /**
     * Analisa o sintoma na Árvore AVL em O(log n).
     */
    public Disease diagnose(String symptom) {
        return grimoire.search(symptom);
    }

    /**
     * Registra a decisão sobre o paciente na Tabela Hash.
     */
    public void recordDecision(Patient p, boolean admitted) {
        MedicalRegistry registry = new MedicalRegistry(p.getId(), admitted, "Atendido pelo Goblin.");
        dailyRecords.put(p.getId(), registry);
        System.out.println("[Manager] Decisão salva: " + registry);
    }
}
