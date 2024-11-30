import java.util.Comparator;

public class PriorityQueue<E> {
    private Object[] elements;// It initializes an array to store the elements of the priority queue
    private int size;//  number of elements in the priority queue
    private Comparator<E> comparator;

    private static final int DEFAULT_CAPACITY = 10;// It shows default initial capacity

    public PriorityQueue(Comparator<E> comparator) {//It creates a constructor with a comparator.
        this(DEFAULT_CAPACITY, comparator);
    }

    public PriorityQueue(int initialCapacity, Comparator<E> comparator) {//This creates a constructor with initial capacity and comparator.
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.elements = new Object[initialCapacity];// It initializes the array with the specified capacity
        this.comparator = comparator;
    }

    public void offer(E element) { // It adds an element to the queue
        if (element == null) {
            throw new NullPointerException("Cannot add null element");
        }

        if (size == elements.length) {//It doubles the capacity if the array is full
            increaseCapacity();
        }

        elements[size] = element;
        percolateup(size);
        size++;
    }

    public E poll() {// // It removes and returns the smallest element
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        E minElement = (E) elements[0];

        size--;
        if (size > 0) {
            elements[0] = elements[size];// It moves the last element to the root
            elements[size] = null;
            percolatedown(0);//It adjusts the heap to maintain the heap property
        }

        return minElement;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void increaseCapacity() {// It doubles the capacity of the  array when it is full
        int newCapacity = elements.length * 2;
        Object[] newArray = new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newArray[i] = elements[i];
        }
        elements = newArray;
    }

    private void percolateup(int index) {// It moves the element at the given index up to maintain the heap property
        E elementtomove = (E) elements[index];

        while (index > 0) { // It moves the element up while it is smaller than its parent
            int parentIndex = (index - 1) / 2;
            E parent = (E) elements[parentIndex];

            if (compareElements(elementtomove, parent) >= 0) {// It stops if the current element is larger than or equal to the parent
                break;
            }

            elements[index] = parent;// It moves the parent down
            index = parentIndex;// It moves up to the parent's position
        }

        elements[index] = elementtomove;// It places the element in its correct position
    }

    private void percolatedown(int index) {// It moves the element at the given index down to restore the heap property
        E ElementToMove = (E) elements[index];

        while (index < size / 2) { // It continues moving the element down while it has children
            int leftChildIndex = 2 * index + 1;
            int RightChildIndex = leftChildIndex + 1;

            int SmallestChildIndex = leftChildIndex;
            E smallestChild = (E) elements[leftChildIndex];

            if (RightChildIndex < size) {//It  checks if the right child exists and is smaller than the left child
                E rightChild = (E) elements[RightChildIndex];
                if (compareElements(rightChild, smallestChild) < 0) {
                    SmallestChildIndex = RightChildIndex;// It updates the smallest child index
                    smallestChild = rightChild;
                }
            }

            if (compareElements(ElementToMove, smallestChild) <= 0) {// It stops if the current element is smaller than or equal to the smallest child
                break;
            }

            elements[index] = smallestChild;//It moves the smallest child up
            index = SmallestChildIndex;// It moves down to the child's position
        }

        elements[index] = ElementToMove; // It places the element in its correct position
    }

    private int compareElements(E element1, E element2) { // Compares two elements using the comparator or natural ordering.
        if (comparator != null) {
            return comparator.compare(element1, element2);// Uses  comparator if provided
        }
        return ((Comparable<E>) element1).compareTo(element2);// Uses natural ordering otherwise
    }
}