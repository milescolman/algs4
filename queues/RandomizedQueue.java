import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private int Ndequeued;
    private Item[] s; 
    public RandomizedQueue() {                 // construct an empty randomized queue
        N = 0; // N - 1 points to last enqueue item
        s = (Item[]) new Object[1]; // start  with size-1 storage
        Ndequeued = 0;
    }
    public boolean isEmpty() {                // is the randomized queue empty?
        return (N - Ndequeued) == 0;
    }
    public int size() {                       // return the number of items on the randomized queue
        return N - Ndequeued;
    }
    public void enqueue(Item item) {          // add the item
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (N == s.length) resize(2 * N);  // resize bigger when s is full
        s[N++] = item;
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int k = 0;
        for (int i = 0; i < N; i++) {
            if (s[i] != null)
                copy[k++] = s[i];
        }
        s = copy;
        N = k;
        Ndequeued = 0;
    }

    public Item dequeue() {                   // remove and return a random item
        if (N == 0) throw new java.util.NoSuchElementException();
        if (Ndequeued == (3 * N / 4)) resize(N/2); // resize down when 3/4 of s is junk
        int randIdx = 0;
        while (s[randIdx = StdRandom.uniform(N)] == null)
            randIdx = StdRandom.uniform(N);
        Item item = s[randIdx];
        s[randIdx] = null; // release memory
        
        Ndequeued++;
        return item;
    }
    public Item sample() {                    // return a random item (but do not remove it)
        if (N == 0) throw new java.util.NoSuchElementException();
        int sampleIdx = StdRandom.uniform(N);
        while (s[sampleIdx] == null)
            sampleIdx = StdRandom.uniform(N);
        return s[sampleIdx];
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new RandomizedQueueIterator(); }
    private class RandomizedQueueIterator implements Iterator<Item>{
        private int[] validRandomIdxs;
        private int idx = 0; 
        public RandomizedQueueIterator() {
            // initialize validRandomIdxs by copying in the valid array addresses
            Item item = null;
            int k = 0;
            validRandomIdxs = new int[N - Ndequeued];
            for (int i = 0; i < N; i++) {
                if (s[i] != null) {
                    validRandomIdxs[k++] = i;
                }
            }
            // in-place shuffle
            for (int i = 0; i < validRandomIdxs.length; i++) {
                int tmp = validRandomIdxs[i];
                int rand = StdRandom.uniform(validRandomIdxs.length);
                validRandomIdxs[i] = validRandomIdxs[rand];
                validRandomIdxs[rand] = tmp;
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
        
        for (int i = 0; i < 7; i++)
            rqi.enqueue(" " + i);
        System.out.println("sample " + rqi.sample());
        
        for (String s : rqi)
            System.out.print(s);
        System.out.println();
        System.out.print("dequeue: ");
        for (int i = 0; i < 6; i++) {
            System.out.print(rqi.dequeue());
        }
        System.out.println();
        for (int i = 7; i < 10; i++)
            rqi.enqueue(" " + i);
        for (String s : rqi)
            System.out.print(s);
        System.out.println();
        
        System.out.println("check its the same");
        for (String s : rqi)
            System.out.print(s);
        System.out.println();
        System.out.println("size " + rqi.size());
    }
}