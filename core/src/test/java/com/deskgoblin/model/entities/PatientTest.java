package com.deskgoblin.model.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {

    @Test
    public void testConstructorAndGetters() {
        Patient p = new Patient("p1", "João", "Febre", 3);
        assertEquals("p1", p.getId(), "O ID não corresponde");
        assertEquals("João", p.getName(), "O nome não corresponde");
        assertEquals("Febre", p.getSymptom(), "O sintoma não corresponde");
        assertEquals(3, p.getSeverityScore(), "A severidade não corresponde");
    }

    @Test
    public void testCompareToLessThan() {
        Patient p1 = new Patient("1", "A", "S1", 1);
        Patient p2 = new Patient("2", "B", "S2", 3);
        assertTrue(p1.compareTo(p2) < 0, "Paciente com severidade 1 deve ser menor que 3");
    }

    @Test
    public void testCompareToGreaterThan() {
        Patient p1 = new Patient("1", "A", "S1", 5);
        Patient p2 = new Patient("2", "B", "S2", 2);
        assertTrue(p1.compareTo(p2) > 0, "Paciente com severidade 5 deve ser maior que 2");
    }

    @Test
    public void testCompareToEqual() {
        Patient p1 = new Patient("1", "A", "S1", 3);
        Patient p2 = new Patient("2", "B", "S2", 3);
        assertEquals(0, p1.compareTo(p2), "Pacientes com mesma severidade devem retornar 0");
    }

    @Test
    public void testToStringFormat() {
        Patient p = new Patient("p1", "João", "Febre", 3);
        assertEquals("João (ID: p1, Severidade: 3)", p.toString(), "Formato de string incorreto");
    }
}
