package com.deskgoblin.model.datastructures;

public class QueueWithTwoStacks<T> {

    private final Stack<T> inStack;
    private final Stack<T> outStack;

    public QueueWithTwoStacks() {
        this.inStack = new Stack<>();
        this.outStack = new Stack<>();
    }

    public void enqueue(T data) {
        inStack.push(data);
    }

    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        transferIfNeeded();
        T data = outStack.pop();
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        transferIfNeeded();
        T data = outStack.peek();
        return data;
    }

    private void transferIfNeeded() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    public int size() {
        int totalSize = inStack.size() + outStack.size();
        return totalSize;
    }

    public boolean isEmpty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    @Override
    public String toString() {
        return "Queue [inStack=" + inStack.toString() + ", outStack=" + outStack.toString() + "]";
    }
}
