package com.deskgoblin.model.datastructures;

public class AVLTree<K extends Comparable<K>, V> {

    public static class AVLNode<K, V> {
        public K key;
        public V value;
        public AVLNode<K, V> left;
        public AVLNode<K, V> right;
        public int height;

        public AVLNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private AVLNode<K, V> root;
    private int size;

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    public AVLNode<K, V> getRoot() {
        return root;
    }

    public int height() {
        return height(root);
    }

    private int height(AVLNode<K, V> node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode<K, V> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AVLNode<K, V> node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private AVLNode<K, V> rotateRight(AVLNode<K, V> y) {
        System.out.println("[AVL] rotateRight: realizando rotação à DIREITA no nó " + y.key);
        AVLNode<K, V> x = y.left;
        AVLNode<K, V> T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode<K, V> rotateLeft(AVLNode<K, V> x) {
        System.out.println("[AVL] rotateLeft: realizando rotação à ESQUERDA no nó " + x.key);
        AVLNode<K, V> y = x.right;
        AVLNode<K, V> T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private AVLNode<K, V> rebalance(AVLNode<K, V> node) {
        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1) {
            System.out.println("[AVL] rebalance: nó " + node.key + " desbalanceado para a esquerda (fator " + balance + ").");
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balance < -1) {
            System.out.println("[AVL] rebalance: nó " + node.key + " desbalanceado para a direita (fator " + balance + ").");
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    public void insert(K key, V value) {
        System.out.println("[AVL] insert: inserindo chave " + key);
        int oldSize = size;
        root = insertRec(root, key, value);
        if (size > oldSize) {
            System.out.println("[AVL] insert: chave " + key + " inserida com sucesso.");
        } else {
            System.out.println("[AVL] insert: chave " + key + " atualizada.");
        }
    }

    private AVLNode<K, V> insertRec(AVLNode<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new AVLNode<>(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRec(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        return rebalance(node);
    }

    public V search(K key) {
        AVLNode<K, V> result = searchRec(root, key);
        if (result != null) {
            return result.value;
        }
        return null;
    }

    private AVLNode<K, V> searchRec(AVLNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return searchRec(node.left, key);
        } else if (cmp > 0) {
            return searchRec(node.right, key);
        } else {
            return node;
        }
    }

    public void delete(K key) {
        System.out.println("[AVL] delete: tentando remover chave " + key);
        int oldSize = size;
        root = deleteRec(root, key);
        if (size < oldSize) {
            System.out.println("[AVL] delete: chave " + key + " removida.");
        }
    }

    private AVLNode<K, V> deleteRec(AVLNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = deleteRec(node.left, key);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                AVLNode<K, V> temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
                size--;
            } else {
                AVLNode<K, V> temp = minValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = deleteRec(node.right, temp.key);
            }
        }

        if (node == null) {
            return null;
        }

        return rebalance(node);
    }

    private AVLNode<K, V> minValueNode(AVLNode<K, V> node) {
        AVLNode<K, V> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public SinglyLinkedList<V> inOrder() {
        SinglyLinkedList<V> list = new SinglyLinkedList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(AVLNode<K, V> node, SinglyLinkedList<V> list) {
        if (node != null) {
            inOrderRec(node.left, list);
            list.pushBack(node.value);
            inOrderRec(node.right, list);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
