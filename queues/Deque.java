import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;
    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    public Deque() {                           // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }
    public boolean isEmpty() {                // is the deque empty?
        return (size == 0);
    }
    public int size() {                        // return the number of items on the deque
        return size;
    }
    public void addFirst(Item item) {         // add the item to the front
        if (item == null) throw new IllegalArgumentException();

        
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        
        if (oldFirst != null) oldFirst.prev = first;
        if (size == 0) last = first;
        size++;
    }
    public void addLast(Item item) {          // add the item to the end
        if (item == null) throw new IllegalArgumentException();
        
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        
        if (oldLast != null) oldLast.next = last;
        if (size == 0) first = last;
        size++;
    }
    public Item removeFirst() {                // remove and return the item from the front
        if (isEmpty()) throw new java.util.NoSuchElementException();
        size--;
        
        Item item = first.item;
        first.item = null;
        first = first.next;
        if (!isEmpty()) first.prev = null;
        else last = null;
        return item;
    }
    public Item removeLast() {                // remove and return the item from the end
        if (isEmpty()) throw new java.util.NoSuchElementException();
        size--;
        
        Item item = last.item;
        last.item = null;
        last = last.prev;
        if (!isEmpty()) last.next = null;
        else first = null;
        return item;
    }
    public Iterator<Item> iterator() { return new DequeIterator(); }        // return an iterator over items in order from front to end
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    public static void main(String[] args) {  // unit testing (optional)
        Deque<Integer> deque2 = new Deque<>();
        deque2.addLast(1);
        deque2.removeLast();
        Deque<String> deque = new Deque<>();
        System.out.println("size " + deque.size());
        deque.addFirst("1");
        deque.addFirst("A");
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
        deque.addFirst("A");
        System.out.println(deque.removeLast());
        deque.addLast("B");
        System.out.println(deque.removeLast());
        deque.addLast("C");
        System.out.println(deque.removeFirst());
        for (String s : deque)
            System.out.print(s + " ");
        System.out.println();
    }
}