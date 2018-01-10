import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private int nDequeued;
    private Item[] s; 
    public RandomizedQueue() {                 // construct an empty randomized queue
        n = 0; // n - 1 points to last enqueue item
        s = (Item[]) new Object[1]; // start  with size-1 storage
        nDequeued = 0;
    }
    public boolean isEmpty() {                // is the randomized queue empty?
        return (n - nDequeued) == 0;
    }
    public int size() {                       // return the number of items on the randomized queue
        return n - nDequeued;
    }
    public void enqueue(Item item) {          // add the item
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (n == s.length) resize(2 * n);  // resize bigger when s is full
        s[n++] = item;
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] != null)
                copy[k++] = s[i];
        }
        s = copy;
        n = k;
        nDequeued = 0;
    }

    public Item dequeue() {                   // remove and return a random item
        if (n == 0) throw new java.util.NoSuchElementException();
        if (nDequeued > (3 * n / 4)) resize(n/2); // resize down when 3/4 of s is junk
        int randIdx = StdRandom.uniform(n);
        while (s[randIdx] == null)
            randIdx = StdRandom.uniform(n);
        Item item = s[randIdx];
        s[randIdx] = null; // release memory
        
        nDequeued++;
        return item;
    }
    public Item sample() {                    // return a random item (but do not remove it)
        if (n == 0) throw new java.util.NoSuchElementException();
        int sampleIdx = StdRandom.uniform(n);
        while (s[sampleIdx] == null)
            sampleIdx = StdRandom.uniform(n);
        return s[sampleIdx];
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new RandomizedQueueIterator(); }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private final int[] validRandomIdxs;
        private int idx = 0; 
        public RandomizedQueueIterator() {
            // initialize validRandomIdxs by copying in the valid array addresses
            int k = 0;
            validRandomIdxs = new int[n - nDequeued];
            for (int i = 0; i < n; i++) {
                if (s[i] != null) {
                    validRandomIdxs[k++] = i;
                }
            }
            // in-place Fisher-Yates shuffle
            for (int i = validRandomIdxs.length - 1; i > 0; i--) {
                int j = StdRandom.uniform(i + 1);
                int tmp = validRandomIdxs[i];
                validRandomIdxs[i] = validRandomIdxs[j];
                validRandomIdxs[j] = tmp;
            }
        }
        
        public boolean hasNext() { 
            return idx < validRandomIdxs.length;
        }
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
        public Item next() {
            if (idx >= validRandomIdxs.length) throw new java.util.NoSuchElementException();
            return s[validRandomIdxs[idx++]];
        }
    }
    public static void main(String[] args) {  // unit testing (optional)
        RandomizedQueue<String> rqi = new RandomizedQueue<>();
        System.out.println("empty? " + rqi.isEmpty());
        
        rqi.enqueue("A");
        System.out.println(rqi.dequeue());
        
        for (int i = 0; i < 7; i++)
            rqi.enqueue(" " + i);
        System.out.println("sample " + rqi.sample());
        
        for (String s : rqi)
            System.out.print(s);
        System.out.println();
        System.out.print("dequeue: ");
        while (!rqi.isEmpty()) {
            System.out.print(rqi.dequeue());
        }
        System.out.println();
        for (int i = 0; i < 10; i++)
            rqi.enqueue(" " + i);
        for (String s : rqi)
            System.out.print(s);
        System.out.println();
        
        System.out.println("check members are the same");
        for (String s : rqi)
            System.out.print(s);
        System.out.println();
        System.out.println("size " + rqi.size());
        
        while(!rqi.isEmpty())
            System.out.print(rqi.dequeue());
        System.out.println();
    }
}