package com.deskgoblin.model.datastructures;

public class HashTable<K, V> {

    public static class Entry<K, V> {
        public K key;
        public V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private SinglyLinkedList<Entry<K, V>>[] table;
    private int size;
    private int capacity;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = 16;
        this.table = new SinglyLinkedList[this.capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new SinglyLinkedList<>();
        }
        this.size = 0;
        System.out.println("[HashTable] Tabela criada com capacidade " + capacity);
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        SinglyLinkedList<Entry<K, V>>[] oldTable = table;

        table = new SinglyLinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new SinglyLinkedList<>();
        }

        size = 0;
        for (int i = 0; i < oldCapacity; i++) {
            SinglyLinkedList<Entry<K, V>> bucket = oldTable[i];
            while (!bucket.isEmpty()) {
                Entry<K, V> entry = bucket.popFront();
                putWithoutResizeCheck(entry.key, entry.value);
            }
        }
    }

    private void putWithoutResizeCheck(K key, V value) {
        int index = hash(key);
        SinglyLinkedList<Entry<K, V>> bucket = table[index];

        SinglyLinkedList<Entry<K, V>> temp = new SinglyLinkedList<>();
        boolean updated = false;

        while (!bucket.isEmpty()) {
            Entry<K, V> entry = bucket.popFront();
            if (entry.key.equals(key)) {
                entry.value = value;
                updated = true;
            }
            temp.pushBack(entry);
        }

        if (!updated) {
            temp.pushBack(new Entry<>(key, value));
            size++;
            if (temp.size() > 1) {
                System.out.println("[HashTable] put: colisão resolvida no índice " + index + " para a chave " + key);
            }
        }

        while (!temp.isEmpty()) {
            bucket.pushBack(temp.popFront());
        }
    }

    public void put(K key, V value) {
        System.out.println("[HashTable] put: inserindo/atualizando chave " + key);
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        putWithoutResizeCheck(key, value);
    }

    public V get(K key) {
        int index = hash(key);
        SinglyLinkedList<Entry<K, V>> bucket = table[index];
        SinglyLinkedList<Entry<K, V>> temp = new SinglyLinkedList<>();
        
        V result = null;

        while (!bucket.isEmpty()) {
            Entry<K, V> entry = bucket.popFront();
            if (entry.key.equals(key)) {
                result = entry.value;
            }
            temp.pushBack(entry);
        }

        while (!temp.isEmpty()) {
            bucket.pushBack(temp.popFront());
        }

        return result;
    }

    public void remove(K key) {
        System.out.println("[HashTable] remove: tentando remover chave " + key);
        int index = hash(key);
        SinglyLinkedList<Entry<K, V>> bucket = table[index];
        SinglyLinkedList<Entry<K, V>> temp = new SinglyLinkedList<>();
        
        boolean removed = false;

        while (!bucket.isEmpty()) {
            Entry<K, V> entry = bucket.popFront();
            if (entry.key.equals(key)) {
                removed = true;
                size--;
            } else {
                temp.pushBack(entry);
            }
        }

        while (!temp.isEmpty()) {
            bucket.pushBack(temp.popFront());
        }

        if (removed) {
            System.out.println("[HashTable] remove: chave " + key + " removida.");
        }
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public SinglyLinkedList<K> keys() {
        SinglyLinkedList<K> allKeys = new SinglyLinkedList<>();
        
        for (int i = 0; i < capacity; i++) {
            SinglyLinkedList<Entry<K, V>> bucket = table[i];
            SinglyLinkedList<Entry<K, V>> temp = new SinglyLinkedList<>();
            
            while (!bucket.isEmpty()) {
                Entry<K, V> entry = bucket.popFront();
                allKeys.pushBack(entry.key);
                temp.pushBack(entry);
            }
            
            while (!temp.isEmpty()) {
                bucket.pushBack(temp.popFront());
            }
        }
        return allKeys;
    }
}
