package com.deskgoblin.model.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StackTest {

    private Stack<String> stack;

    @BeforeEach
    public void setUp() {
        stack = new Stack<>();
    }

    @Test
    public void testPushAndPeek() {
        stack.push("Goblin");
        assertEquals("Goblin", stack.peek(), "Peek deve retornar o último elemento empilhado.");
        
        stack.push("Orc");
        assertEquals("Orc", stack.peek(), "Peek deve retornar o novo elemento no topo.");
    }

    @Test
    public void testPop() {
        stack.push("A");
        stack.push("B");
        assertEquals("B", stack.pop(), "Pop deve remover e retornar o elemento do topo.");
        assertEquals("A", stack.peek(), "Após o pop, o próximo elemento deve estar no topo.");
    }

    @Test
    public void testPopEmptyStack() {
        assertNull(stack.pop(), "Pop em pilha vazia deve retornar null.");
    }

    @Test
    public void testPeekEmptyStack() {
        assertNull(stack.peek(), "Peek em pilha vazia deve retornar null.");
    }

    @Test
    public void testSize() {
        assertEquals(0, stack.size(), "Tamanho inicial deve ser 0.");
        stack.push("Item1");
        assertEquals(1, stack.size(), "Tamanho deve ser 1 após push.");
        stack.push("Item2");
        assertEquals(2, stack.size(), "Tamanho deve ser 2 após outro push.");
        stack.pop();
        assertEquals(1, stack.size(), "Tamanho deve ser 1 após pop.");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(stack.isEmpty(), "Pilha deve estar vazia inicialmente.");
        stack.push("Test");
        assertFalse(stack.isEmpty(), "Pilha não deve estar vazia após push.");
        stack.pop();
        assertTrue(stack.isEmpty(), "Pilha deve estar vazia após remover o único elemento.");
    }

    @Test
    public void testToString() {
        stack.push("C");
        stack.push("B");
        stack.push("A");
        assertEquals("Stack: [A, B, C]", stack.toString(), "A representação de string deve listar os elementos do topo para a base.");
    }
    
    @Test
    public void testMultiplePushAndPop() {
        for(int i = 0; i < 5; i++) {
            stack.push(String.valueOf(i));
        }
        for(int i = 4; i >= 0; i--) {
            assertEquals(String.valueOf(i), stack.pop(), "Os elementos devem sair na ordem inversa que entraram.");
        }
        assertTrue(stack.isEmpty(), "Pilha deve ficar vazia ao final.");
    }
}
