package com.deskgoblin.model.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {
    private MinHeap<Integer> heap;

    @BeforeEach
    public void setUp() {
        heap = new MinHeap<>();
    }

    @Test
    public void testInsertAndPeekMin() {
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        assertEquals(1, heap.peekMin(), "O menor elemento deve ser 1");
    }

    @Test
    public void testExtractMinOrder() {
        heap.insert(5);
        heap.insert(3);
        heap.insert(8);
        heap.insert(1);
        heap.insert(4);
        assertEquals(1, heap.extractMin(), "Deve extrair 1");
        assertEquals(3, heap.extractMin(), "Deve extrair 3");
        assertEquals(4, heap.extractMin(), "Deve extrair 4");
        assertEquals(5, heap.extractMin(), "Deve extrair 5");
        assertEquals(8, heap.extractMin(), "Deve extrair 8");
    }

    @Test
    public void testExtractMinFromEmpty() {
        assertNull(heap.extractMin(), "Deve retornar null se a heap estiver vazia");
    }

    @Test
    public void testPeekMinFromEmpty() {
        assertNull(heap.peekMin(), "Deve retornar null se a heap estiver vazia");
    }

    @Test
    public void testSingleElement() {
        heap.insert(10);
        assertEquals(10, heap.peekMin(), "Deve retornar o único elemento");
        assertEquals(10, heap.extractMin(), "Deve extrair o único elemento");
        assertTrue(heap.isEmpty(), "Heap deve estar vazia após a extração");
    }

    @Test
    public void testDuplicateValues() {
        heap.insert(3);
        heap.insert(3);
        heap.insert(1);
        heap.insert(1);
        assertEquals(1, heap.extractMin());
        assertEquals(1, heap.extractMin());
        assertEquals(3, heap.extractMin());
        assertEquals(3, heap.extractMin());
    }

    @Test
    public void testResizeOnManyInserts() {
        for (int i = 15; i >= 1; i--) {
            heap.insert(i);
        }
        assertEquals(15, heap.size(), "Tamanho deve ser 15");
        for (int i = 1; i <= 15; i++) {
            assertEquals(i, heap.extractMin());
        }
    }

    @Test
    public void testSizeTracking() {
        assertEquals(0, heap.size());
        heap.insert(10);
        assertEquals(1, heap.size());
        heap.insert(20);
        assertEquals(2, heap.size());
        heap.extractMin();
        assertEquals(1, heap.size());
    }

    @Test
    public void testPeekDoesNotRemove() {
        heap.insert(10);
        assertEquals(10, heap.peekMin());
        assertEquals(10, heap.peekMin());
        assertEquals(1, heap.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(heap.isEmpty());
        heap.insert(5);
        assertFalse(heap.isEmpty());
    }
}
