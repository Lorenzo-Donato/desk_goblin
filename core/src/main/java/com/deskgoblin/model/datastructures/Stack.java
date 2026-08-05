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
        // System.out.println("[Stack] Pilha criada.");
    }

    /**
     * Adiciona um elemento no topo da pilha.
     *
     * @param data O elemento a ser empilhado.
     */
    public void push(T data) {
        list.pushFront(data);
        // System.out.println("[Stack] push: elemento " + data + " empilhado.");
    }

    /**
     * Remove e retorna o elemento do topo da pilha.
     *
     * @return O elemento desempilhado, ou null se a pilha estiver vazia.
     */
    public T pop() {
        if (isEmpty()) {
            // System.out.println("[Stack] pop: pilha vazia.");
            return null;
        }
        T data = list.popFront();
        // System.out.println("[Stack] pop: elemento " + data + " desempilhado.");
        return data;
    }

    /**
     * Retorna, sem remover, o elemento do topo da pilha.
     *
     * @return O elemento do topo, ou null se a pilha estiver vazia.
     */
    public T peek() {
        if (isEmpty()) {
            // System.out.println("[Stack] peek: pilha vazia.");
            return null;
        }
        T data = list.peekFront();
        // System.out.println("[Stack] peek: elemento " + data + " acessado.");
        return data;
    }

    /**
     * Retorna o número de elementos na pilha.
     *
     * @return O tamanho da pilha.
     */
    public int size() {
        int size = list.size();
        // System.out.println("[Stack] size: " + size);
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
