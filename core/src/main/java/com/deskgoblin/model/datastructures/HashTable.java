package com.deskgoblin.model.datastructures;

/**
 * Classe que implementa uma Tabela Hash (Hash Table) com resolução de conflitos via encadeamento (Chaining).
 * Utiliza a SinglyLinkedList internamente.
 *
 * @param <K> O tipo da chave.
 * @param <V> O tipo do valor associado.
 */
public class HashTable<K, V> {

    /**
     * Classe interna representando um par Chave-Valor na tabela.
     */
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

    /**
     * Construtor da Tabela Hash com capacidade inicial.
     */
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

        size = 0; // O size será recalculado durante os puts
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

        // Esvazia o bucket atual para procurar se a chave já existe
        while (!bucket.isEmpty()) {
            Entry<K, V> entry = bucket.popFront();
            if (entry.key.equals(key)) {
                entry.value = value; // Atualiza o valor
                updated = true;
            }
            temp.pushBack(entry);
        }

        // Se não atualizou, significa que é um novo elemento
        if (!updated) {
            temp.pushBack(new Entry<>(key, value));
            size++;
            if (temp.size() > 1) {
                System.out.println("[HashTable] put: colisão resolvida no índice " + index + " para a chave " + key);
            }
        }

        // Devolve os itens ao bucket original
        while (!temp.isEmpty()) {
            bucket.pushBack(temp.popFront());
        }
    }

    /**
     * Insere ou atualiza um par chave-valor na tabela.
     *
     * @param key   A chave.
     * @param value O valor.
     */
    public void put(K key, V value) {
        System.out.println("[HashTable] put: inserindo/atualizando chave " + key);
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        putWithoutResizeCheck(key, value);
    }

    /**
     * Busca um valor na tabela a partir de sua chave.
     *
     * @param key A chave a ser buscada.
     * @return O valor associado, ou null se não for encontrado.
     */
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

    /**
     * Remove um par chave-valor da tabela.
     *
     * @param key A chave a ser removida.
     */
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

    /**
     * Verifica se a tabela contém a chave especificada.
     *
     * @param key A chave.
     * @return true se a chave existir, false caso contrário.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Retorna a quantidade de pares armazenados na tabela.
     *
     * @return O tamanho da tabela.
     */
    public int size() {
        return size;
    }

    /**
     * Verifica se a tabela está vazia.
     *
     * @return true se vazia, false caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna uma lista encadeada contendo todas as chaves da tabela.
     *
     * @return SinglyLinkedList contendo as chaves.
     */
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
