import java.util.ArrayList;
import java.util.Iterator;

public class HashSet<E> implements Iterable<E> {
    private ArrayList<E> elements;

    public HashSet() {
        this.elements = new ArrayList<>();
    }

    public boolean contains(Object o) {//It checks if it contains a determined element.
        return elements.contains(o);
    }

    public boolean add(E e) {//It adds an element if it is not already exist.
        if (!contains(e)) {
            return elements.add(e);
        }
        return false;
    }

    public boolean remove(Object o) {//It removes an element.
        return elements.remove(o);
    }

    public int size() {
        return elements.size();
    }


    public Iterator<E> iterator() {
        return elements.iterator();
    }
}