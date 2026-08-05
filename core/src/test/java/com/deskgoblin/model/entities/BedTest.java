package com.deskgoblin.model.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BedTest {

    private Bed bed;

    @BeforeEach
    public void setUp() {
        bed = new Bed("b1");
    }

    @Test
    public void testNewBedIsUnoccupied() {
        assertNull(bed.getPatient(), "Leito novo não deve ter paciente");
        assertFalse(bed.isOccupied(), "Leito novo não deve estar ocupado");
    }

    @Test
    public void testSetAndGetPatient() {
        Patient p = new Patient("p1", "Maria", "Dor de cabeça", 2);
        bed.setPatient(p);
        assertEquals(p, bed.getPatient(), "Deve retornar o paciente atribuído");
    }

    @Test
    public void testIsOccupiedAfterAssignment() {
        Patient p = new Patient("p1", "Maria", "Dor de cabeça", 2);
        bed.setPatient(p);
        assertTrue(bed.isOccupied(), "Leito deve estar ocupado após atribuição de paciente");
    }

    @Test
    public void testClearPatient() {
        Patient p = new Patient("p1", "Maria", "Dor de cabeça", 2);
        bed.setPatient(p);
        bed.setPatient(null);
        assertNull(bed.getPatient(), "Leito não deve ter paciente");
        assertFalse(bed.isOccupied(), "Leito não deve estar ocupado");
    }

    @Test
    public void testGetId() {
        assertEquals("b1", bed.getId(), "Deve retornar o ID atribuído no construtor");
    }

    @Test
    public void testToStringOccupied() {
        Patient p = new Patient("p1", "Maria", "Dor de cabeça", 2);
        bed.setPatient(p);
        assertNotNull(bed.toString());
        // Verify format with patient
        String str = bed.toString();
        assertTrue(str.contains("b1") && str.contains("Maria"), "String com formato inválido para ocupado");
    }

    @Test
    public void testToStringFree() {
        assertNotNull(bed.toString());
        String str = bed.toString();
        assertTrue(str.contains("b1") && !str.contains("Maria"), "String com formato inválido para desocupado");
    }
}
