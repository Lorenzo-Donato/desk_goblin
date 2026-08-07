package com.deskgoblin.model.datastructures;

public class Stack<T> {

    private final SinglyLinkedList<T> list;

    public Stack() {
        this.list = new SinglyLinkedList<>();
    }

    public void push(T data) {
        list.pushFront(data);
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T data = list.popFront();
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        T data = list.peekFront();
        return data;
    }

    public int size() {
        int size = list.size();
        return size;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return "Stack: " + list.toString();
    }
}
