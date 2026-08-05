package com.deskgoblin.model.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe MedicalProcess.
 */
public class MedicalProcessTest {

    private Patient patient;
    private Bed bed;
    private MedicalProcess process;

    @BeforeEach
    void setUp() {
        patient = new Patient("P1", "Test", "Symptom", 3);
        bed = new Bed("M1");
        process = new MedicalProcess(patient, bed);
    }

    @Test
    void testInitialState() {
        assertFalse(process.isFinished(), "O processo não deve começar finalizado.");
        assertEquals(0, process.getCurrentStageIndex(), "O estágio inicial deve ser o índice 0.");
        assertEquals(30.0f, process.getTotalTimeRemaining(), 0.001f, "O tempo total inicial deve ser 30.0f segundos.");
    }

    @Test
    void testUpdateAdvancesStage() {
        process.update(5.1f);
        assertEquals(1, process.getCurrentStageIndex(), "Atualizar mais de 5.0f deve avançar para o estágio 1.");
    }

    @Test
    void testPartialUpdate() {
        process.update(2.0f);
        assertEquals(0, process.getCurrentStageIndex(), "Uma atualização de 2.0f deve manter o processo no estágio 0.");
        assertEquals(28.0f, process.getTotalTimeRemaining(), 0.001f, "Restam 28.0f do tempo total.");
        assertEquals(3.0f, process.getStageTimer(0), 0.001f, "O estágio atual deve ter aproximadamente 3.0f segundos restantes.");
    }

    @Test
    void testAllStagesComplete() {
        // update() avança no máximo 1 estágio por chamada, simulamos o game loop
        for (int i = 0; i < 600; i++) {
            process.update(0.1f); // 600 * 0.1 = 60s (bem mais que 30s)
        }
        assertTrue(process.isFinished(), "Após tempo suficiente em múltiplas atualizações, o processo deve estar finalizado.");
    }

    @Test
    void testGetStageCount() {
        assertEquals(6, process.getStageCount(), "O processo médico deve conter exatamente 6 estágios.");
    }

    @Test
    void testGetStageName() {
        assertEquals("Procedimento 1", process.getStageName(0), "O nome do estágio 0 deve ser 'Procedimento 1'.");
        assertEquals("Procedimento 6", process.getStageName(5), "O nome do estágio 5 deve ser 'Procedimento 6'.");
    }

    @Test
    void testGetPatientAndBed() {
        assertEquals(patient, process.getPatient(), "A referência do paciente deve ser a mesma passada no construtor.");
        assertEquals(bed, process.getBed(), "A referência do leito deve ser a mesma passada no construtor.");
    }

    @Test
    void testGetTotalTimeRemainingDecrements() {
        // Avançar 10 segundos no total com múltiplas chamadas
        for (int i = 0; i < 100; i++) {
            process.update(0.1f); // 100 * 0.1 = 10s
        }
        assertEquals(20.0f, process.getTotalTimeRemaining(), 1.0f, "Após ~10.0f de atualização, devem restar aproximadamente 20.0f segundos.");
    }

    @Test
    void testMultipleSmallUpdates() {
        // 600 * 0.1 = 60s (bem mais que 30s necessários)
        for (int i = 0; i < 600; i++) {
            process.update(0.1f);
        }
        assertTrue(process.isFinished(), "Múltiplas pequenas atualizações que somam mais que 30.0f devem finalizar o processo.");
    }
}
