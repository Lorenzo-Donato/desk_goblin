package com.deskgoblin.model.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaxHeapTest {

    private MaxHeap<Integer> maxHeap;

    @BeforeEach
    public void setUp() {
        maxHeap = new MaxHeap<>();
    }

    @Test
    public void testInsertAndPeekMax() {
        maxHeap.insert(10);
        assertEquals(10, maxHeap.peekMax(), "O máximo deve ser 10.");

        maxHeap.insert(30);
        assertEquals(30, maxHeap.peekMax(), "O máximo deve ser atualizado para 30.");

        maxHeap.insert(20);
        assertEquals(30, maxHeap.peekMax(), "O máximo ainda deve ser 30.");
    }

    @Test
    public void testExtractMax() {
        maxHeap.insert(15);
        maxHeap.insert(10);
        maxHeap.insert(25);
        maxHeap.insert(5);

        assertEquals(25, maxHeap.extractMax(), "Deve extrair 25.");
        assertEquals(15, maxHeap.extractMax(), "Deve extrair 15.");
        assertEquals(10, maxHeap.extractMax(), "Deve extrair 10.");
        assertEquals(5, maxHeap.extractMax(), "Deve extrair 5.");
        assertNull(maxHeap.extractMax(), "Deve retornar null para heap vazio.");
    }

    @Test
    public void testExtractMaxEmpty() {
        assertNull(maxHeap.extractMax(), "Heap vazio deve retornar null no extractMax.");
    }

    @Test
    public void testPeekMaxEmpty() {
        assertNull(maxHeap.peekMax(), "Heap vazio deve retornar null no peekMax.");
    }

    @Test
    public void testSizeAndIsEmpty() {
        assertTrue(maxHeap.isEmpty(), "Heap deve estar vazio inicialmente.");
        assertEquals(0, maxHeap.size(), "Tamanho deve ser 0.");

        maxHeap.insert(42);
        assertFalse(maxHeap.isEmpty(), "Heap não deve estar vazio após inserção.");
        assertEquals(1, maxHeap.size(), "Tamanho deve ser 1.");

        maxHeap.extractMax();
        assertTrue(maxHeap.isEmpty(), "Heap deve estar vazio após extrair tudo.");
        assertEquals(0, maxHeap.size(), "Tamanho deve ser 0.");
    }

    @Test
    public void testResize() {
        // Insere 20 elementos, o dobro da capacidade inicial (10)
        for (int i = 0; i < 20; i++) {
            maxHeap.insert(i);
        }
        assertEquals(20, maxHeap.size(), "Tamanho deve ser 20 após 20 inserções.");
        assertEquals(19, maxHeap.peekMax(), "O maior deve ser 19.");
    }

    @Test
    public void testHeapSort() {
        maxHeap.insert(3);
        maxHeap.insert(10);
        maxHeap.insert(5);
        maxHeap.insert(1);
        maxHeap.insert(8);

        Object[] sorted = maxHeap.heapSort();
        assertEquals(5, sorted.length, "O array ordenado deve ter o tamanho original.");
        
        // Em um MaxHeap, os extraídos vêm em ordem decrescente
        assertEquals(10, (Integer) sorted[0]);
        assertEquals(8, (Integer) sorted[1]);
        assertEquals(5, (Integer) sorted[2]);
        assertEquals(3, (Integer) sorted[3]);
        assertEquals(1, (Integer) sorted[4]);

        assertTrue(maxHeap.isEmpty(), "O heap deve ficar vazio após heapSort.");
    }

    @Test
    public void testDuplicateInserts() {
        maxHeap.insert(10);
        maxHeap.insert(10);
        maxHeap.insert(5);

        assertEquals(10, maxHeap.extractMax());
        assertEquals(10, maxHeap.extractMax());
        assertEquals(5, maxHeap.extractMax());
    }
}
