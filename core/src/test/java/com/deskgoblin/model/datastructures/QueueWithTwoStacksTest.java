package com.deskgoblin.model.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueueWithTwoStacksTest {

    private QueueWithTwoStacks<Integer> queue;

    @BeforeEach
    public void setUp() {
        queue = new QueueWithTwoStacks<>();
    }

    @Test
    public void testEnqueueAndPeek() {
        queue.enqueue(10);
        assertEquals(10, queue.peek(), "Peek deve retornar o primeiro elemento enfileirado.");
        
        queue.enqueue(20);
        assertEquals(10, queue.peek(), "Peek ainda deve retornar o primeiro elemento.");
    }

    @Test
    public void testDequeue() {
        queue.enqueue(10);
        queue.enqueue(20);
        
        assertEquals(10, queue.dequeue(), "Dequeue deve remover e retornar o primeiro elemento.");
        assertEquals(20, queue.peek(), "Após o dequeue, o próximo elemento deve estar na frente.");
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertNull(queue.dequeue(), "Dequeue em fila vazia deve retornar null.");
    }

    @Test
    public void testPeekEmptyQueue() {
        assertNull(queue.peek(), "Peek em fila vazia deve retornar null.");
    }

    @Test
    public void testSize() {
        assertEquals(0, queue.size(), "Tamanho inicial deve ser 0.");
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(2, queue.size(), "Tamanho deve ser 2 após duas inserções.");
        queue.dequeue();
        assertEquals(1, queue.size(), "Tamanho deve ser 1 após um dequeue.");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty(), "Fila deve estar vazia inicialmente.");
        queue.enqueue(5);
        assertFalse(queue.isEmpty(), "Fila não deve estar vazia após enqueue.");
        queue.dequeue();
        assertTrue(queue.isEmpty(), "Fila deve estar vazia após remover o único elemento.");
    }

    @Test
    public void testTransferMechanism() {
        // Enfileira A e B
        queue.enqueue(1);
        queue.enqueue(2);
        
        // Dequeue aciona a transferência: outStack agora tem B, A. Retorna A.
        assertEquals(1, queue.dequeue(), "Deve retornar 1.");
        
        // Enfileira C e D (vão para inStack)
        queue.enqueue(3);
        queue.enqueue(4);
        
        // Dequeue não deve acionar transferência ainda, pois outStack tem B
        assertEquals(2, queue.dequeue(), "Deve retornar 2.");
        
        // Dequeue aciona transferência novamente: outStack terá D, C. Retorna C.
        assertEquals(3, queue.dequeue(), "Deve retornar 3.");
        
        // Verifica o último
        assertEquals(4, queue.dequeue(), "Deve retornar 4.");
    }
    
    @Test
    public void testMultipleInterleavedOps() {
        queue.enqueue(100);
        assertEquals(100, queue.dequeue());
        assertTrue(queue.isEmpty());
        
        queue.enqueue(200);
        queue.enqueue(300);
        assertEquals(200, queue.peek());
        assertEquals(200, queue.dequeue());
        
        queue.enqueue(400);
        assertEquals(300, queue.dequeue());
        assertEquals(400, queue.dequeue());
        
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testToString() {
        queue.enqueue(1);
        queue.enqueue(2);
        String s = queue.toString();
        assertTrue(s.contains("Queue"), "toString deve conter Queue");
    }

    @Test
    public void testQueueOrderIntegrity() {
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(i, queue.dequeue());
        }
        for (int i = 10; i < 15; i++) {
            queue.enqueue(i);
        }
        for (int i = 5; i < 15; i++) {
            assertEquals(i, queue.dequeue());
        }
        assertTrue(queue.isEmpty());
    }
}
