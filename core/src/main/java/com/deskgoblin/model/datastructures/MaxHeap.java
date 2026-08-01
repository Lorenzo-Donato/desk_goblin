package com.deskgoblin.model.datastructures;

/**
 * Classe que implementa um Heap Máximo (Max Heap) baseado em array.
 *
 * @param <T> O tipo de dado armazenado, que deve ser comparável.
 */
public class MaxHeap<T extends Comparable<T>> {

    private Object[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Construtor do MaxHeap. Inicializa o array interno.
     */
    public MaxHeap() {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        System.out.println("[MaxHeap] Heap criado com capacidade inicial de " + DEFAULT_CAPACITY);
    }

    private void resize() {
        System.out.println("[MaxHeap] resize: aumentando a capacidade do array.");
        Object[] newHeap = new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    private void swap(int i, int j) {
        System.out.println("[MaxHeap] swap: trocando elementos nas posições " + i + " e " + j);
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        System.out.println("[MaxHeap] siftUp: ajustando elemento no índice " + index);
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            T current = (T) heap[index];
            T parent = (T) heap[parentIndex];
            
            if (current.compareTo(parent) > 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index, int maxIndex) {
        System.out.println("[MaxHeap] siftDown: ajustando elemento no índice " + index);
        while (index < maxIndex) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = index;

            if (leftChild < maxIndex) {
                T currentLeft = (T) heap[leftChild];
                T currentLargest = (T) heap[largest];
                if (currentLeft.compareTo(currentLargest) > 0) {
                    largest = leftChild;
                }
            }

            if (rightChild < maxIndex) {
                T currentRight = (T) heap[rightChild];
                T currentLargest = (T) heap[largest];
                if (currentRight.compareTo(currentLargest) > 0) {
                    largest = rightChild;
                }
            }

            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }

    /**
     * Insere um novo elemento no Heap Máximo.
     *
     * @param data O elemento a ser inserido.
     */
    public void insert(T data) {
        System.out.println("[MaxHeap] insert: inserindo elemento " + data);
        if (size == heap.length) {
            resize();
        }
        heap[size] = data;
        siftUp(size);
        size++;
    }

    /**
     * Remove e retorna o maior elemento (raiz) do heap.
     *
     * @return O maior elemento, ou null se estiver vazio.
     */
    @SuppressWarnings("unchecked")
    public T extractMax() {
        if (isEmpty()) {
            System.out.println("[MaxHeap] extractMax: heap vazio.");
            return null;
        }
        
        T max = (T) heap[0];
        System.out.println("[MaxHeap] extractMax: removendo elemento máximo " + max);
        
        heap[0] = heap[size - 1];
        heap[size - 1] = null; // evita vazamento de memória
        size--;
        
        if (size > 0) {
            siftDown(0, size);
        }
        
        return max;
    }

    /**
     * Retorna, sem remover, o maior elemento do heap.
     *
     * @return O maior elemento, ou null se estiver vazio.
     */
    @SuppressWarnings("unchecked")
    public T peekMax() {
        if (isEmpty()) {
            System.out.println("[MaxHeap] peekMax: heap vazio.");
            return null;
        }
        T max = (T) heap[0];
        System.out.println("[MaxHeap] peekMax: acessado máximo " + max);
        return max;
    }

    /**
     * Retorna a quantidade de elementos no heap.
     *
     * @return O tamanho do heap.
     */
    public int size() {
        System.out.println("[MaxHeap] size: " + size);
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

    /**
     * Realiza a ordenação (HeapSort) consumindo todos os elementos, 
     * retornando um array de forma decrescente (ou crescente dependendo do uso externo, 
     * como extraímos sempre o máximo, os primeiros serão os maiores).
     * Nota: Este método destrói o heap (esvazia).
     *
     * @return Um array genérico contendo os elementos extraídos.
     */
    public Object[] heapSort() {
        System.out.println("[MaxHeap] heapSort: ordenando elementos (destruindo o heap).");
        Object[] sorted = new Object[size];
        int originalSize = size;
        for (int i = 0; i < originalSize; i++) {
            sorted[i] = extractMax();
        }
        return sorted;
    }
}
