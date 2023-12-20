import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {
    private Node head; // root object of the list
    private int size; // how many items are stored

    public int size() {
        return size;
    }

    private class Node {
        // this class holds data in the variable data and a pointer to the next Node in the variable next

        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // adds a new item to the list
    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode; // if no head, add the new data as the header
        } else {
            Node current = head;
            while (current.next != null) { // iterates to find the last item
                current = current.next;
            }
            current.next = newNode; // add the data as the last element
        }
        size++;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    // iterator implementation
    private class LinkedListIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}
