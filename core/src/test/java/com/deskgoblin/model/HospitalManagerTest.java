package com.deskgoblin.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.deskgoblin.model.datastructures.SinglyLinkedList;
import com.deskgoblin.model.entities.*;

/**
 * Testes unitários para a classe HospitalManager.
 */
public class HospitalManagerTest {

    private HospitalManager manager;

    @BeforeEach
    void setUp() {
        manager = new HospitalManager();
    }

    @Test
    void testRegisterAndRetrievePatient() {
        Patient p = new Patient("P1", "João", "Febre", 3);
        manager.registerPatient(p);
        
        Patient retrieved = manager.getPatientRecord("P1");
        assertNotNull(retrieved, "O paciente deve ser encontrado após o registro.");
        assertEquals(p, retrieved, "O paciente retornado deve ser o mesmo que foi registrado.");
    }

    @Test
    void testGetPatientRecordNotFound() {
        Patient retrieved = manager.getPatientRecord("P999");
        assertNull(retrieved, "A busca por um ID inexistente deve retornar null.");
    }

    @Test
    void testPatientQueuePriority() {
        Patient p1 = new Patient("P1", "Ana", "Grave", 3);
        Patient p2 = new Patient("P2", "Carlos", "Leve", 1);
        Patient p3 = new Patient("P3", "Beto", "Médio", 2);

        manager.registerPatient(p1);
        manager.registerPatient(p2);
        manager.registerPatient(p3);

        assertEquals(p2, manager.popNextPatient(), "O paciente com menor gravidade (1) deve ser atendido primeiro.");
        assertEquals(p3, manager.popNextPatient(), "O próximo paciente deve ter gravidade (2).");
        assertEquals(p1, manager.popNextPatient(), "O último paciente deve ter gravidade (3).");
    }

    @Test
    void testAssignPatientToBed() {
        Patient p = new Patient("P1", "João", "Febre", 3);
        manager.registerPatient(p);
        
        Patient nextPatient = manager.popNextPatient();
        boolean success = manager.assignPatientToBed(nextPatient, "M1", 30.0f);
        
        assertTrue(success, "O paciente deve ser alocado ao leito com sucesso.");
        assertTrue(manager.getBed("M1").isOccupied(), "O leito M1 deve constar como ocupado.");
    }

    @Test
    void testAssignToOccupiedBed() {
        Patient p1 = new Patient("P1", "João", "Febre", 3);
        Patient p2 = new Patient("P2", "Ana", "Tosse", 2);
        manager.registerPatient(p1);
        manager.registerPatient(p2);

        manager.assignPatientToBed(manager.popNextPatient(), "M1", 30.0f);
        
        boolean successOnOccupied = manager.assignPatientToBed(manager.popNextPatient(), "M1", 30.0f);
        assertFalse(successOnOccupied, "Não deve ser possível alocar um paciente a um leito já ocupado.");
    }

    @Test
    void testAssignToInvalidBed() {
        Patient p = new Patient("P1", "João", "Febre", 3);
        manager.registerPatient(p);
        
        boolean success = manager.assignPatientToBed(manager.popNextPatient(), "M99", 30.0f);
        assertFalse(success, "Não deve ser possível alocar um paciente a um leito inválido/inexistente.");
    }

    @Test
    void testRemovePatientFromBed() {
        Patient p = new Patient("P1", "João", "Febre", 3);
        manager.registerPatient(p);
        manager.assignPatientToBed(manager.popNextPatient(), "M1", 30.0f);
        
        boolean success = manager.removePatientFromBed("M1");
        assertTrue(success, "Deve remover o paciente com sucesso.");
        assertFalse(manager.getBed("M1").isOccupied(), "O leito M1 deve estar vazio após a remoção.");
    }

    @Test
    void testRemoveFromEmptyBed() {
        boolean success = manager.removePatientFromBed("M1");
        assertFalse(success, "Tentar remover paciente de um leito vazio deve retornar false.");
    }

    @Test
    void testGetBed() {
        assertNotNull(manager.getBed("M1"), "O leito M1 deve existir.");
        assertNull(manager.getBed("M99"), "O leito M99 não deve existir.");
    }

    @Test
    void testGetPatientQueueSnapshotPreservesQueue() {
        Patient p1 = new Patient("P1", "João", "Febre", 3);
        manager.registerPatient(p1);
        
        SinglyLinkedList<Patient> snapshot = manager.getPatientQueueSnapshot();
        assertNotNull(snapshot, "O snapshot da fila não deve ser nulo.");
        
        Patient nextPatient = manager.popNextPatient();
        assertNotNull(nextPatient, "O paciente deve permanecer na fila (MinHeap) após tirar o snapshot.");
        assertEquals("P1", nextPatient.getId(), "O ID do paciente retirado deve ser P1.");
    }

    @Test
    void testUpdateMedicalProcessesFinishes() {
        Patient p = new Patient("P1", "João", "Febre", 3);
        manager.registerPatient(p);
        manager.assignPatientToBed(manager.popNextPatient(), "M1", 30.0f);
        
        // update() avança no máximo 1 estágio por chamada, simulamos o game loop
        boolean finished = false;
        for (int i = 0; i < 600; i++) {
            if (manager.updateMedicalProcesses(0.1f)) {
                finished = true;
            }
        }
        assertTrue(finished, "Após tempo suficiente, o processo deve finalizar.");
        assertFalse(manager.getBed("M1").isOccupied(), "O leito M1 deve estar liberado após a finalização.");
    }

    @Test
    void testUpdateMedicalProcessesNotFinished() {
        Patient p = new Patient("P1", "João", "Febre", 3);
        manager.registerPatient(p);
        manager.assignPatientToBed(manager.popNextPatient(), "M1", 30.0f);
        
        boolean finished = manager.updateMedicalProcesses(1.0f);
        assertFalse(finished, "Atualizar 1.0f não deve finalizar o processo.");
        assertTrue(manager.getBed("M1").isOccupied(), "O leito M1 ainda deve estar ocupado.");
    }
}
