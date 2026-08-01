package com.deskgoblin.model.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {

    private HashTable<String, Integer> hashTable;

    @BeforeEach
    public void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    public void testPutAndGet() {
        hashTable.put("Um", 1);
        hashTable.put("Dois", 2);

        assertEquals(1, hashTable.get("Um"), "Deve retornar o valor correto para 'Um'");
        assertEquals(2, hashTable.get("Dois"), "Deve retornar o valor correto para 'Dois'");
        assertNull(hashTable.get("Tres"), "Deve retornar null para chave inexistente");
    }

    @Test
    public void testUpdateExistingKey() {
        hashTable.put("Moeda", 100);
        hashTable.put("Moeda", 150);

        assertEquals(150, hashTable.get("Moeda"), "Deve atualizar o valor de uma chave existente");
        assertEquals(1, hashTable.size(), "O tamanho não deve mudar na atualização");
    }

    @Test
    public void testRemove() {
        hashTable.put("Goblin", 50);
        hashTable.put("Orc", 80);
        
        hashTable.remove("Goblin");
        assertNull(hashTable.get("Goblin"), "Deve ser nulo após a remoção");
        assertEquals(1, hashTable.size(), "Tamanho deve diminuir após remoção");
        
        assertEquals(80, hashTable.get("Orc"), "Outros elementos não devem ser afetados");
    }

    @Test
    public void testRemoveNonExistent() {
        hashTable.put("Goblin", 50);
        hashTable.remove("Dragao");
        assertEquals(1, hashTable.size(), "Tamanho não deve mudar se a chave não existir");
    }

    @Test
    public void testContainsKey() {
        hashTable.put("A", 1);
        assertTrue(hashTable.containsKey("A"), "Deve conter a chave 'A'");
        assertFalse(hashTable.containsKey("B"), "Não deve conter a chave 'B'");
    }

    @Test
    public void testSizeAndIsEmpty() {
        assertTrue(hashTable.isEmpty(), "Deve estar vazia inicialmente");
        assertEquals(0, hashTable.size(), "Tamanho inicial 0");

        hashTable.put("Teste", 999);
        assertFalse(hashTable.isEmpty(), "Não deve estar vazia após inserção");
        assertEquals(1, hashTable.size(), "Tamanho deve ser 1");

        hashTable.remove("Teste");
        assertTrue(hashTable.isEmpty(), "Deve estar vazia após remover o único elemento");
        assertEquals(0, hashTable.size(), "Tamanho deve ser 0");
    }

    @Test
    public void testResizeAndCollision() {
        // Força colisões e rehashing, a capacidade inicial é 16
        // Preenche com 20 elementos para acionar o resize (load factor 0.75 -> resize ao atingir 12)
        for (int i = 0; i < 20; i++) {
            hashTable.put("Chave" + i, i);
        }
        
        assertEquals(20, hashTable.size(), "Tamanho deve ser 20 após inserções massivas");
        
        for (int i = 0; i < 20; i++) {
            assertEquals(i, hashTable.get("Chave" + i), "Valores devem ser retidos após rehash");
        }
    }

    @Test
    public void testKeys() {
        hashTable.put("A", 10);
        hashTable.put("B", 20);
        hashTable.put("C", 30);
        
        SinglyLinkedList<String> keys = hashTable.keys();
        assertEquals(3, keys.size(), "Deve retornar todas as 3 chaves");
        
        // As chaves podem estar em qualquer ordem, mas a lista deve contê-las
        boolean foundA = false, foundB = false, foundC = false;
        while (!keys.isEmpty()) {
            String k = keys.popFront();
            if (k.equals("A")) foundA = true;
            if (k.equals("B")) foundB = true;
            if (k.equals("C")) foundC = true;
        }
        
        assertTrue(foundA && foundB && foundC, "A lista de chaves deve conter A, B e C");
    }
}
