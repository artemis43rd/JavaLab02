package ru.spbstu.telematics.java;

import java.util.Iterator;
import java.util.Stack;
import java.util.Map;

public class App<K extends Comparable<K>, V> implements Iterable<Map.Entry<K, V>>
{
    private Node root;

    // Методы TreeMap
    public int size() { return size(root); }

    public boolean contains(K key) { return find(root, key) != null; }

    public void add(K key, V value) { root = add(root, key, value); }

    public void remove(K key) { root = remove(root, key); }

    public V get(K key)
    {
        Node node = find(root, key);
        return (node != null) ? (V) node.value : null;
    }

    // Представление узла в дереве
    private static class Node<K, V>
    {
        private K key;
        private V value;
        private Node<K, V> left, right;

        public Node(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
    }

    // Метод для подсчета размера дерева
    private int size(Node<K, V> node)
    {
        if (node == null)
            return 0;
        else
            return 1 + size(node.left) + size(node.right);
    }

    // Метод для поиска узла по ключу
    private Node<K, V> find(Node<K, V> node, K key)
    {
        if (node == null || key.equals(node.key))
            return node;

        if (key.compareTo(node.key) < 0)
            return find(node.left, key);
        else
            return find(node.right, key);
    }

    // Метод для добавления узла в дерево
    private Node<K, V> add(Node<K, V> node, K key, V value)
    {
        if (node == null)
            return new Node<>(key, value);

        if (key.compareTo(node.key) < 0)
            node.left = add(node.left, key, value);
        else if (key.compareTo(node.key) > 0)
            node.right = add(node.right, key, value);
        else
            node.value = value;

        return node;
    }

    // Метод для удаления узла из дерева
    private Node<K, V> remove(Node<K, V> node, K key)
    {
        if (node == null)
            return null;

        if (key.compareTo(node.key) < 0)
            node.left = remove(node.left, key);
        else if (key.compareTo(node.key) > 0)
            node.right = remove(node.right, key);
        else
        {
            if (node.left == null)
                return node.right;
            else if (node.right == null)
                return node.left;

            node.key = minValue(node.right);

            node.right = remove(node.right, node.key);
        }

        return node;
    }

    private K minValue(Node<K, V> node)
    {
        K minValue = node.key;
        while (node.left != null) 
        {
            minValue = node.left.key;
            node = node.left;
        }
        return minValue;
    }
    
    @Override
    public Iterator<Map.Entry<K, V>> iterator() { return new TreeMapIterator(root); }

    private class TreeMapIterator implements Iterator<Map.Entry<K, V>>
    {
        private Stack<Node<K, V>> stack;

        public TreeMapIterator(Node<K, V> root)
        {
            stack = new Stack<>();
            pushLeftChildren(root);
        }

        @Override
        public boolean hasNext() { return !stack.isEmpty(); }

        @Override
        public Map.Entry<K, V> next()
        {
            Node<K, V> node = stack.pop();
            pushLeftChildren(node.right);
            return new java.util.AbstractMap.SimpleEntry<>(node.key, node.value);
        }

        private void pushLeftChildren(Node<K, V> node)
        {
            while (node != null)
            {
                stack.push(node);
                node = node.left;
            }
        }
    }
}