package com.deskgoblin.model.datastructures;

/**
 * Classe que implementa uma Pilha (Stack) utilizando composição com SinglyLinkedList.
 *
 * @param <T> O tipo de dado armazenado na pilha.
 */
public class Stack<T> {

    private final SinglyLinkedList<T> list;

    /**
     * Construtor da Pilha. Inicializa a estrutura interna vazia.
     */
    public Stack() {
        this.list = new SinglyLinkedList<>();
    }

    /**
     * Adiciona um elemento no topo da pilha.
     *
     * @param data O elemento a ser empilhado.
     */
    public void push(T data) {
        list.pushFront(data);
    }

    /**
     * Remove e retorna o elemento do topo da pilha.
     *
     * @return O elemento desempilhado, ou null se a pilha estiver vazia.
     */
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T data = list.popFront();
        return data;
    }

    /**
     * Retorna, sem remover, o elemento do topo da pilha.
     *
     * @return O elemento do topo, ou null se a pilha estiver vazia.
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        T data = list.peekFront();
        return data;
    }

    /**
     * Retorna o número de elementos na pilha.
     *
     * @return O tamanho da pilha.
     */
    public int size() {
        int size = list.size();
        return size;
    }

    /**
     * Verifica se a pilha está vazia.
     *
     * @return true se a pilha estiver vazia, false caso contrário.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return "Stack: " + list.toString();
    }
}
