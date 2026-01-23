package ru.saruat.homework.homework1;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] buckets;

    private int size;

    public int getSize(){
        return size;
    }


    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
        //return 1;
    }

    public void put(K key, V value) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        int index = hash(key);
        Node<K, V> current = buckets[index];
        boolean collisionOccurred = false;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value; // Обновляем значение, если ключ уже существует
                return;
            }
            current = current.next;
            collisionOccurred = true; // Коллизия возникла, так как bucket уже был занят
        }

        if (collisionOccurred) {
            System.out.println("Collision occurred at index " + index + ". Adding a new node to the chain.");
        }

        buckets[index] = new Node<>(key, value, buckets[index]);
        size++;

        if ((float)size / buckets.length > LOAD_FACTOR) resize();
    }

    public V get(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        int index = hash(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public V remove(K key) {
        if (key == null) throw new NullPointerException("Key cannot be null");

        int index = hash(key);

        Node<K, V> prev = null;

        Node<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                size--;
                return current.value;
            }

            prev = current;
            current = current.next;
        }

        return null;
    }

    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        int newCapacity = oldBuckets.length * 2;
        buckets = new Node[newCapacity];
        size = 0;

        for (Node<K, V> bucket : oldBuckets) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MyHashMap:\n");

        for (int i = 0; i < buckets.length; i++) {
            Node<K, V> current = buckets[i];

            if (current != null) {
                sb.append("bucket № ").append(i).append(": ");

                while (current != null) {
                    sb.append("(").append(current.key).append(" - ").append(current.value).append(")");
                    if (current.next != null) {
                        sb.append(", ");
                    }
                    current = current.next;
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}