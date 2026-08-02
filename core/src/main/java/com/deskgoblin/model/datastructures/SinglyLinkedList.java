package com.deskgoblin.model.datastructures;

/**
 * Classe que implementa uma Lista Encadeada Simples (Singly Linked List).
 *
 * @param <T> O tipo de dado armazenado na lista.
 */
public class SinglyLinkedList<T> {

    /**
     * Classe interna que representa um nó na lista.
     *
     * @param <T> O tipo de dado armazenado no nó.
     */
    public static class Node<T> {
        public T data;
        public Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Construtor da lista encadeada. Inicializa uma lista vazia.
     */
    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        System.out.println("[LinkedList] Lista criada.");
    }

    // --- MÉTODO ADICIONADO PARA RESOLVER O ERRO DO GAME SCREEN ---
    public Node<T> getHead() {
        return head;
    }
    // -------------------------------------------------------------

    /**
     * Adiciona um elemento no início da lista.
     *
     * @param data O elemento a ser adicionado.
     */
    public void pushFront(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
        System.out.println("[LinkedList] pushFront: adicionado " + data + " no início.");
    }

    /**
     * Adiciona um elemento no final da lista.
     *
     * @param data O elemento a ser adicionado.
     */
    public void pushBack(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        System.out.println("[LinkedList] pushBack: adicionado " + data + " no final.");
    }

    /**
     * Remove e retorna o elemento do início da lista.
     *
     * @return O elemento removido, ou null se a lista estiver vazia.
     */
    public T popFront() {
        if (isEmpty()) {
            System.out.println("[LinkedList] popFront: lista vazia.");
            return null;
        }
        T data = head.data;
        head = head.next;
        size--;
        if (isEmpty()) {
            tail = null;
        }
        System.out.println("[LinkedList] popFront: removido " + data + " do início.");
        return data;
    }

    /**
     * Remove e retorna o elemento do final da lista.
     *
     * @return O elemento removido, ou null se a lista estiver vazia.
     */
    public T popBack() {
        if (isEmpty()) {
            System.out.println("[LinkedList] popBack: lista vazia.");
            return null;
        }
        T data = tail.data;
        if (head == tail) {
            head = tail = null;
        } else {
            Node<T> current = head;
            while (current.next != tail) {
                current = current.next;
            }
            current.next = null;
            tail = current;
        }
        size--;
        System.out.println("[LinkedList] popBack: removido " + data + " do final.");
        return data;
    }

    /**
     * Retorna, sem remover, o elemento do início da lista.
     *
     * @return O elemento do início, ou null se a lista estiver vazia.
     */
    public T peekFront() {
        if (isEmpty()) {
            System.out.println("[LinkedList] peekFront: lista vazia.");
            return null;
        }
        System.out.println("[LinkedList] peekFront: acessado " + head.data);
        return head.data;
    }

    /**
     * Retorna, sem remover, o elemento do final da lista.
     *
     * @return O elemento do final, ou null se a lista estiver vazia.
     */
    public T peekBack() {
        if (isEmpty()) {
            System.out.println("[LinkedList] peekBack: lista vazia.");
            return null;
        }
        System.out.println("[LinkedList] peekBack: acessado " + tail.data);
        return tail.data;
    }

    /**
     * Retorna o tamanho atual da lista.
     *
     * @return O número de elementos na lista.
     */
    public int size() {
        System.out.println("[LinkedList] size: " + size);
        return size;
    }

    /**
     * Verifica se a lista está vazia.
     *
     * @return true se a lista estiver vazia, false caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Verifica se a lista contém o elemento especificado.
     *
     * @param data O elemento a ser buscado.
     * @return true se o elemento for encontrado, false caso contrário.
     */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                System.out.println("[LinkedList] contains: elemento " + data + " encontrado.");
                return true;
            }
            current = current.next;
        }
        System.out.println("[LinkedList] contains: elemento " + data + " não encontrado.");
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}