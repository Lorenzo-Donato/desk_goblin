package com.deskgoblin.model.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new SinglyLinkedList<>();
    }

    @Test
    public void testPushFrontAndPeek() {
        list.pushFront(10);
        list.pushFront(20);
        assertEquals(20, list.peekFront(), "PeekFront deve retornar o último elemento adicionado no início.");
        assertEquals(10, list.peekBack(), "PeekBack deve retornar o primeiro elemento adicionado.");
    }

    @Test
    public void testPushBackAndPeek() {
        list.pushBack(10);
        list.pushBack(20);
        assertEquals(10, list.peekFront(), "PeekFront deve retornar o primeiro elemento adicionado no final.");
        assertEquals(20, list.peekBack(), "PeekBack deve retornar o último elemento adicionado no final.");
    }

    @Test
    public void testPopFront() {
        list.pushBack(10);
        list.pushBack(20);
        assertEquals(10, list.popFront(), "PopFront deve remover e retornar o primeiro elemento.");
        assertEquals(20, list.peekFront(), "Após PopFront, PeekFront deve ser o próximo elemento.");
        assertEquals(1, list.size(), "O tamanho deve diminuir após popFront.");
    }

    @Test
    public void testPopBack() {
        list.pushBack(10);
        list.pushBack(20);
        assertEquals(20, list.popBack(), "PopBack deve remover e retornar o último elemento.");
        assertEquals(10, list.peekBack(), "Após PopBack, PeekBack deve ser o elemento anterior.");
        assertEquals(1, list.size(), "O tamanho deve diminuir após popBack.");
    }

    @Test
    public void testPopEmptyList() {
        assertNull(list.popFront(), "PopFront em lista vazia deve retornar null.");
        assertNull(list.popBack(), "PopBack em lista vazia deve retornar null.");
    }

    @Test
    public void testSizeAndIsEmpty() {
        assertTrue(list.isEmpty(), "Lista deve estar vazia inicialmente.");
        assertEquals(0, list.size(), "Tamanho inicial deve ser 0.");
        
        list.pushFront(1);
        assertFalse(list.isEmpty(), "Lista não deve estar vazia após inserção.");
        assertEquals(1, list.size(), "Tamanho deve ser 1 após inserção.");
        
        list.popFront();
        assertTrue(list.isEmpty(), "Lista deve estar vazia após remover todos os elementos.");
        assertEquals(0, list.size(), "Tamanho deve ser 0 após remoção de tudo.");
    }

    @Test
    public void testContains() {
        list.pushBack(100);
        list.pushBack(200);
        assertTrue(list.contains(100), "A lista deve conter 100.");
        assertTrue(list.contains(200), "A lista deve conter 200.");
        assertFalse(list.contains(300), "A lista não deve conter 300.");
    }

    @Test
    public void testPopFrontSingleElement() {
        list.pushBack(42);
        assertEquals(42, list.popFront(), "Deve retornar o único elemento.");
        assertTrue(list.isEmpty(), "Lista deve estar vazia após popFront do único elemento.");
        assertNull(list.peekBack(), "tail deve ser null se lista ficou vazia.");
    }

    @Test
    public void testPopBackSingleElement() {
        list.pushFront(42);
        assertEquals(42, list.popBack(), "Deve retornar o único elemento.");
        assertTrue(list.isEmpty(), "Lista deve estar vazia após popBack do único elemento.");
        assertNull(list.peekFront(), "head deve ser null se lista ficou vazia.");
    }

    @Test
    public void testToString() {
        assertEquals("[]", list.toString(), "A representação de string de lista vazia deve ser []");
        list.pushBack(1);
        list.pushBack(2);
        list.pushBack(3);
        assertEquals("[1, 2, 3]", list.toString(), "A representação de string deve listar os elementos.");
    }
}
