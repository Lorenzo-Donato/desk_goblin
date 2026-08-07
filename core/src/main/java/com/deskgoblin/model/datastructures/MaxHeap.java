package com.deskgoblin.model.datastructures;

public class MaxHeap<T extends Comparable<T>> {

    private Object[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MaxHeap() {
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        System.out.println("[MaxHeap] Heap criado com capacidade inicial de " + DEFAULT_CAPACITY);
    }

    private void resize() {
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

    public void insert(T data) {
        System.out.println("[MaxHeap] insert: inserindo elemento " + data);
        if (size == heap.length) {
            resize();
        }
        heap[size] = data;
        siftUp(size);
        size++;
    }

    @SuppressWarnings("unchecked")
    public T extractMax() {
        if (isEmpty()) {
            System.out.println("[MaxHeap] extractMax: heap vazio.");
            return null;
        }
        
        T max = (T) heap[0];
        System.out.println("[MaxHeap] extractMax: removendo elemento máximo " + max);
        
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        if (size > 0) {
            siftDown(0, size);
        }
        
        return max;
    }

    @SuppressWarnings("unchecked")
    public T peekMax() {
        if (isEmpty()) {
            return null;
        }
        T max = (T) heap[0];
        return max;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

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
