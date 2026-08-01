package com.deskgoblin.model.datastructures;

/**
 * Classe que implementa uma Fila (Queue) utilizando duas Pilhas (Stacks).
 *
 * @param <T> O tipo de dado armazenado na fila.
 */
public class QueueWithTwoStacks<T> {

    private final Stack<T> inStack;
    private final Stack<T> outStack;

    /**
     * Construtor da Fila. Inicializa as duas pilhas internas.
     */
    public QueueWithTwoStacks() {
        this.inStack = new Stack<>();
        this.outStack = new Stack<>();
        System.out.println("[Queue] Fila com duas pilhas criada.");
    }

    /**
     * Adiciona um elemento ao final da fila.
     *
     * @param data O elemento a ser enfileirado.
     */
    public void enqueue(T data) {
        inStack.push(data);
        System.out.println("[Queue] enqueue: elemento " + data + " adicionado à fila.");
    }

    /**
     * Remove e retorna o elemento da frente da fila.
     * Realiza a transferência da inStack para a outStack se necessário.
     *
     * @return O elemento desenfileirado, ou null se a fila estiver vazia.
     */
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("[Queue] dequeue: fila vazia.");
            return null;
        }
        transferIfNeeded();
        T data = outStack.pop();
        System.out.println("[Queue] dequeue: elemento " + data + " removido da fila.");
        return data;
    }

    /**
     * Retorna, sem remover, o elemento da frente da fila.
     * Realiza a transferência da inStack para a outStack se necessário.
     *
     * @return O elemento da frente, ou null se a fila estiver vazia.
     */
    public T peek() {
        if (isEmpty()) {
            System.out.println("[Queue] peek: fila vazia.");
            return null;
        }
        transferIfNeeded();
        T data = outStack.peek();
        System.out.println("[Queue] peek: elemento " + data + " acessado na fila.");
        return data;
    }

    /**
     * Método auxiliar privado que transfere elementos da pilha de entrada
     * para a pilha de saída apenas se a pilha de saída estiver vazia.
     */
    private void transferIfNeeded() {
        if (outStack.isEmpty()) {
            System.out.println("[Queue] transfer: movendo elementos da inStack para a outStack.");
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    /**
     * Retorna o número total de elementos na fila.
     *
     * @return O tamanho da fila.
     */
    public int size() {
        int totalSize = inStack.size() + outStack.size();
        System.out.println("[Queue] size: " + totalSize);
        return totalSize;
    }

    /**
     * Verifica se a fila está vazia.
     *
     * @return true se a fila estiver vazia, false caso contrário.
     */
    public boolean isEmpty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    @Override
    public String toString() {
        return "Queue [inStack=" + inStack.toString() + ", outStack=" + outStack.toString() + "]";
    }
}
