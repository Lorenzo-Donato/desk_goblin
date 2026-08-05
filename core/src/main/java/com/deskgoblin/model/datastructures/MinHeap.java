package com.deskgoblin.model.datastructures;

/**
 * Classe que implementa um Heap Mínimo (Min Heap) baseado em array.
 *
 * @param <T> O tipo de dado armazenado, que deve ser comparável.
 */
public class MinHeap<T extends Comparable<T>> {

    private Object[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Construtor do MinHeap. Inicializa o array interno.
     */
    public MinHeap() {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    private void resize() {
        Object[] newHeap = new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            T current = (T) heap[index];
            T parent = (T) heap[parentIndex];
            
            // For MinHeap, if current is LESS than parent, swap them
            if (current.compareTo(parent) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index, int maxIndex) {
        while (index < maxIndex) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < maxIndex) {
                T currentLeft = (T) heap[leftChild];
                T currentSmallest = (T) heap[smallest];
                // For MinHeap, we want the smallest child
                if (currentLeft.compareTo(currentSmallest) < 0) {
                    smallest = leftChild;
                }
            }

            if (rightChild < maxIndex) {
                T currentRight = (T) heap[rightChild];
                T currentSmallest = (T) heap[smallest];
                // For MinHeap, we want the smallest child
                if (currentRight.compareTo(currentSmallest) < 0) {
                    smallest = rightChild;
                }
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    /**
     * Insere um novo elemento no Heap Mínimo.
     *
     * @param data O elemento a ser inserido.
     */
    public void insert(T data) {
        if (size == heap.length) {
            resize();
        }
        heap[size] = data;
        siftUp(size);
        size++;
    }

    /**
     * Remove e retorna o menor elemento (raiz) do heap.
     *
     * @return O menor elemento, ou null se estiver vazio.
     */
    @SuppressWarnings("unchecked")
    public T extractMin() {
        if (isEmpty()) return null;
        
        T min = (T) heap[0];
        
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        if (size > 0) {
            siftDown(0, size);
        }
        
        return min;
    }

    /**
     * Retorna, sem remover, o menor elemento do heap.
     *
     * @return O menor elemento, ou null se estiver vazio.
     */
    @SuppressWarnings("unchecked")
    public T peekMin() {
        if (isEmpty()) return null;
        return (T) heap[0];
    }

    /**
     * Retorna a quantidade de elementos no heap.
     *
     * @return O tamanho do heap.
     */
    public int size() {
        return size;
    }

    /**
     * Verifica se o heap está vazio.
     *
     * @return true se estiver vazio, false caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }
}
