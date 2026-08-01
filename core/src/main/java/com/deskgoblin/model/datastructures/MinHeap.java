package com.deskgoblin.model.datastructures;

public class MinHeap<T extends Comparable<T>> {

    private Object[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

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

    public void insert(T data) {
        if (size == heap.length) {
            resize();
        }
        heap[size] = data;
        siftUp(size);
        size++;
    }

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

    @SuppressWarnings("unchecked")
    public T peekMin() {
        if (isEmpty()) return null;
        return (T) heap[0];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
