public class HashMap<K, V> {
    private static class Entry<K, V> {
        private K key;//initializes key
        private V value;//initializes value
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }// Getter for the key

        public V getValue() {
            return value;
        }// Getter for the value

        public void setValue(V value) {
            this.value = value;
        }//sets value
    }


    private Entry<K, V>[] slots;// Array of slots to store entries
    private int capacity;// Current capacity of the HashMap
    private int size;// Number of key value pairs stored in the hashmap

    public HashMap() {
        this(16);
    }


    public HashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        capacity = initialCapacity;// Sets the initial capacity
        slots = new Entry[capacity];// Initializes the slots array
        size = 0;// It starts with zero size
    }

    private int hash(K key) {// It creates hash function to compute the index for a key
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {//  inserts or updates a key value pair
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not allowed");
        }

        if (size >= capacity * 0.75) {// Resizes the hashmap if the load factor exceeds 0.75
            resize();
        }

        int index = hash(key);// Computes the slot index for the key
        Entry<K, V> current = slots[index];

        while (current != null) { // Traverses the linked list to check for an existing key
            if (current.getKey().equals(key)) { // If the key exists, updates the value
                current.setValue(value);
                return;
            }
            current = current.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value); // If the key is new, creates a new entry and adds it to the slot.
        newEntry.next = slots[index];// It points the new entry to the existing slot chain
        slots[index] = newEntry;// Places the new entry at the head of the slot.
        size++;
    }

    public V get(K key) {//Retrieves the value associated with key.
        if (key == null) return null;

        int index = hash(key);
        Entry<K, V> current = slots[index];

        while (current != null) {
            if (current.getKey().equals(key)) {
                return current.getValue();
            }
            current = current.next;
        }

        return null;
    }



    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private void resize() {//It doubles the capacity and rehash all entries when resizing
        capacity *= 2;
        Entry<K, V>[] oldBuckets = slots;
        slots = new Entry[capacity];
        size = 0;

        for (Entry<K, V> bucket : oldBuckets) {// It rehashs all entries from the old slot array
            Entry<K, V> current = bucket;
            while (current != null) {
                put(current.getKey(), current.getValue());
                current = current.next;
            }
        }
    }
}