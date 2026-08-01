package com.deskgoblin.model.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AVLTreeTest {

    private AVLTree<Integer, String> tree;

    @BeforeEach
    public void setUp() {
        tree = new AVLTree<>();
    }

    @Test
    public void testInsertAndSearch() {
        tree.insert(10, "Dez");
        assertEquals("Dez", tree.search(10), "Deve encontrar o valor inserido.");
        assertNull(tree.search(20), "Não deve encontrar chave não inserida.");
    }

    @Test
    public void testUpdateExistingKey() {
        tree.insert(5, "Cinco");
        tree.insert(5, "Cinco Novo");
        assertEquals("Cinco Novo", tree.search(5), "A inserção de chave existente deve atualizar o valor.");
        assertEquals(1, tree.size(), "O tamanho não deve aumentar ao atualizar chave.");
    }

    @Test
    public void testDeleteLeaf() {
        tree.insert(10, "Dez");
        tree.delete(10);
        assertNull(tree.search(10), "O nó não deve ser encontrado após remoção.");
        assertEquals(0, tree.size(), "A árvore deve ficar vazia.");
    }

    @Test
    public void testDeleteNodeWithOneChild() {
        tree.insert(10, "Dez");
        tree.insert(5, "Cinco"); // Filho à esquerda
        tree.delete(10);
        assertEquals("Cinco", tree.search(5), "O filho deve assumir a raiz.");
        assertEquals(1, tree.size(), "A árvore deve ter tamanho 1.");
    }

    @Test
    public void testDeleteNodeWithTwoChildren() {
        tree.insert(10, "Dez");
        tree.insert(5, "Cinco");
        tree.insert(15, "Quinze");
        tree.insert(12, "Doze");
        tree.insert(17, "Dezessete");

        tree.delete(15);
        assertNull(tree.search(15), "A chave 15 não deve mais existir.");
        assertEquals("Doze", tree.search(12), "O nó sucessor ou estrutura remanescente deve estar intacta.");
        assertEquals("Dezessete", tree.search(17), "O filho direito deve permanecer.");
        assertEquals(4, tree.size(), "O tamanho deve diminuir em 1.");
    }

    @Test
    public void testRightRotation() {
        // Desbalanceamento para a esquerda (Left-Left)
        tree.insert(30, "Trinta");
        tree.insert(20, "Vinte");
        tree.insert(10, "Dez"); // Aciona rotação à direita

        assertEquals(2, tree.height(), "A altura deve ser 2 após a rotação.");
        // A raiz deve ser 20
        // inOrder: 10, 20, 30
        SinglyLinkedList<String> list = tree.inOrder();
        assertEquals("Dez", list.popFront());
        assertEquals("Vinte", list.popFront());
        assertEquals("Trinta", list.popFront());
    }

    @Test
    public void testLeftRotation() {
        // Desbalanceamento para a direita (Right-Right)
        tree.insert(10, "Dez");
        tree.insert(20, "Vinte");
        tree.insert(30, "Trinta"); // Aciona rotação à esquerda

        assertEquals(2, tree.height(), "A altura deve ser 2 após a rotação.");
        SinglyLinkedList<String> list = tree.inOrder();
        assertEquals("Dez", list.popFront());
        assertEquals("Vinte", list.popFront());
        assertEquals("Trinta", list.popFront());
    }

    @Test
    public void testLeftRightRotation() {
        // Left-Right case
        tree.insert(30, "Trinta");
        tree.insert(10, "Dez");
        tree.insert(20, "Vinte"); // Aciona rotação esquerda (em 10) e direita (em 30)

        assertEquals(2, tree.height());
        assertEquals("Vinte", tree.search(20));
    }

    @Test
    public void testRightLeftRotation() {
        // Right-Left case
        tree.insert(10, "Dez");
        tree.insert(30, "Trinta");
        tree.insert(20, "Vinte"); // Aciona rotação direita (em 30) e esquerda (em 10)

        assertEquals(2, tree.height());
        assertEquals("Vinte", tree.search(20));
    }

    @Test
    public void testSizeAndIsEmpty() {
        assertTrue(tree.isEmpty(), "Deve estar vazia no início.");
        assertEquals(0, tree.size(), "Tamanho 0.");

        tree.insert(1, "A");
        assertFalse(tree.isEmpty(), "Não deve estar vazia após inserção.");
        assertEquals(1, tree.size(), "Tamanho 1.");

        tree.delete(1);
        assertTrue(tree.isEmpty(), "Deve estar vazia após deleção.");
        assertEquals(0, tree.size(), "Tamanho 0.");
    }

    @Test
    public void testInOrderTraversal() {
        tree.insert(50, "Cinquenta");
        tree.insert(30, "Trinta");
        tree.insert(70, "Setenta");
        tree.insert(20, "Vinte");
        tree.insert(40, "Quarenta");

        SinglyLinkedList<String> list = tree.inOrder();
        assertEquals("Vinte", list.popFront());
        assertEquals("Trinta", list.popFront());
        assertEquals("Quarenta", list.popFront());
        assertEquals("Cinquenta", list.popFront());
        assertEquals("Setenta", list.popFront());
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testDeleteNonExistent() {
        tree.insert(10, "A");
        tree.delete(99);
        assertEquals(1, tree.size(), "Tamanho não deve mudar ao remover chave inexistente.");
    }
}
