package ru.spbstu.telematics.java;

import java.util.*;

public class myTreeMap<K extends Comparable<K>, V> implements Iterable<Map.Entry<K, V>>
{
    private Node root;
    private Comparator<? super K> comparator;

    public myTreeMap()
    {
        this.root = null;
        this.comparator = null;
    }

    public myTreeMap(Comparator<? super K> comparator)
    {
        this.root = null;
        this.comparator = comparator;
    }

    public myTreeMap(Map<? extends K, ? extends V> m)
    {
        this();
        putAll(m);
    }

    public myTreeMap(SortedMap<K, ? extends V> m)
    {
        this(m.comparator());
        putAll(m);
    }

    public boolean isEmpty() { return root == null; }

    public boolean containsValue(Object value) { return containsValue(root, value); }

    private boolean containsValue(Node<K, V> node, Object value)
    {
        if (node == null)
            return false;

        if (Objects.equals(value, node.value))
            return true;

        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    public K firstKey()
    {
        if (root == null)
            throw new NoSuchElementException("TreeMap is empty");
        return firstKey(root).key;
    }

    private Node<K, V> firstKey(Node<K, V> node)
    {
        while (node.left != null)
            node = node.left;
        return node;
    }

    public K lastKey()
    {
        if (root == null)
            throw new NoSuchElementException("TreeMap is empty");
        return lastKey(root).key;
    }

    private Node<K, V> lastKey(Node<K, V> node)
    {
        while (node.right != null)
            node = node.right;
        return node;
    }

    public Set<K> keySet()
    {
        Set<K> keys = new HashSet<>();
        inorderTraversalForKeys(root, keys);
        return keys;
    }

    private void inorderTraversalForKeys(Node<K, V> node, Set<K> keys)
    {
        if (node != null)
        {
            inorderTraversalForKeys(node.left, keys);
            keys.add(node.key);
            inorderTraversalForKeys(node.right, keys);
        }
    }

    public List<V> values()
    {
        List<Map.Entry<K, V>> entryList = new ArrayList<>();
        inorderTraversal(root, entryList);

        List<V> values = new ArrayList<>();
        for (Map.Entry<K, V> entry : entryList)
            values.add(entry.getValue());

        return values;
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        inorderTraversal(root, entries);
        return entries;
    }

    public SortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
    {
        if (fromKey == null || toKey == null)
            throw new NullPointerException("fromKey and toKey cannot be null");

        if (fromKey.compareTo(toKey) > 0)
            throw new IllegalArgumentException("fromKey must be less than or equal to toKey");

        TreeMap<K, V> subMap = new TreeMap<>();
        subMap(root, fromKey, fromInclusive, toKey, toInclusive, subMap);
        return subMap;
    }

    private void subMap(Node<K, V> node, K fromKey, boolean fromInclusive, K toKey, boolean toInclusive, TreeMap<K, V> subMap)
    {
        if (node == null)
            return;

        int fromCmp = compare(fromKey, node.key);
        int toCmp = compare(toKey, node.key);

        if ((fromCmp < 0 || (fromCmp == 0 && fromInclusive)) && (toCmp > 0 || (toCmp == 0 && toInclusive)))
        {
            subMap.put(node.key, node.value);
            subMap(node.left, fromKey, fromInclusive, toKey, toInclusive, subMap);
            subMap(node.right, fromKey, fromInclusive, toKey, toInclusive, subMap);
        }
        else if (fromCmp <= 0)
            subMap(node.left, fromKey, fromInclusive, toKey, toInclusive, subMap);
        else
            subMap(node.right, fromKey, fromInclusive, toKey, toInclusive, subMap);
    }

    public void put(K key, V value) { root = put(root, key, value); }

    private Node<K, V> put(Node<K, V> node, K key, V value)
    {
        if (node == null)
            return new Node<>(key, value);

        int cmp = compare(key, node.key);

        if (cmp < 0)
            node.left = put(node.left, key, value);
        else if (cmp > 0)
            node.right = put(node.right, key, value);
        else
            node.value = value;
        return node;
    }

    public void putAll(Map<? extends K, ? extends V> m)
    {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    public void remove(K key) { root = remove(root, key); }

    private Node<K, V> remove(Node<K, V> node, K key)
    {
        if (node == null)
            return null;

        int cmp = compare(key, node.key);

        if (cmp < 0)
            node.left = remove(node.left, key);
        else if (cmp > 0)
            node.right = remove(node.right, key);
        else
        {
            if (node.left == null)
                return node.right;
            else if (node.right == null)
                return node.left;

            node.key = minKey(node.right);
            node.value = get(node.right, node.key);
            node.right = remove(node.right, node.key);
        }

        return node;
    }

    private K minKey(Node<K, V> node)
    {
        while (node.left != null)
            node = node.left;
        return node.key;
    }

    private V get(Node<K, V> node, K key)
    {
        while (node != null)
        {
            int cmp = compare(key, node.key);
            if (cmp == 0)
                return node.value;
            else if (cmp < 0)
                node = node.left;
            else
                node = node.right;
        }
        return null;
    }

    public int size() { return size(root); }

    private int size(Node<K, V> node)
    {
        if (node == null)
            return 0;

        return 1 + size(node.left) + size(node.right);
    }

    public boolean containsKey(Object key) { return get(root, (K) key) != null; }

    public void clear() { root = null; }

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

    private void inorderTraversal(Node<K, V> node, Collection<Map.Entry<K, V>> collection)
    {
        if (node != null)
        {
            inorderTraversal(node.left, collection);
            collection.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inorderTraversal(node.right, collection);
        }
    }

    private int compare(K key1, K key2)
    {
        if (comparator != null)
            return comparator.compare(key1, key2);
        else
            return key1.compareTo(key2);
    }

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
}