import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private int nDequeued;
    private Item[] s;
    private int[] randomIdxsToDequeue;
    public RandomizedQueue() {                 // construct an empty randomized queue
        n = 0; // n - 1 points to last enqueue item
        s = (Item[]) new Object[1]; // start  with size-1 storage
        randomIdxsToDequeue = new int[1];
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
        // if (n == nDequeued) s[0] = item; // all items have been removed, use left slot
        s[n] = item;
        // n - nDequeued + 1 b/c want to include idx we have not added yet 
        int choice = StdRandom.uniform(n - nDequeued + 1);
        // p = 1/(nValidIdxs + 1) event: new address = n  
        if (choice == (n - nDequeued)) randomIdxsToDequeue[n - nDequeued] = n; 
        else { // put chosen idx at end, put new idx in old spot to avoid duplication
            randomIdxsToDequeue[n - nDequeued] = randomIdxsToDequeue[choice];
            randomIdxsToDequeue[choice] = n;
        }
        n++;
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int[] randomIdxsToDequeueCopy = new int[capacity];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] != null) {
                randomIdxsToDequeueCopy[k] = k;
                copy[k++] = s[i];
            }
        }
        s = copy;
        n = k;
        nDequeued = 0;
        randomIdxsToDequeue = randomIdxsToDequeueCopy;
    }

    public Item dequeue() {                   // remove and return a random item
        if (size() == 0) throw new java.util.NoSuchElementException();
        if (size() == (s.length / 4)) resize(2 * size()); // resize down when 3/4 of s is empty
        int randIdx = randomIdxsToDequeue[n - nDequeued - 1];
        randomIdxsToDequeue[n - nDequeued - 1] = 0; // overwrite just-released idx

        Item item = s[randIdx];
        s[randIdx] = null; // release memory
        nDequeued++;
        return item;
    }
    public Item sample() {                    // return a random item (but do not remove it)
        if (size() == 0) throw new java.util.NoSuchElementException();
        int sampleIdx = StdRandom.uniform(n - nDequeued);
        sampleIdx = randomIdxsToDequeue[sampleIdx];
        return s[sampleIdx];
    }
    // return an independent iterator over items in independent random order
    public Iterator<Item> iterator() { return new RandomizedQueueIterator(); }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int idx = 0;
        private final int [] shuffledIdxsToDequeue;
        
        private RandomizedQueueIterator() {
            shuffledIdxsToDequeue = new int[size()];
            for (int i = 0; i < size(); i++)
                shuffledIdxsToDequeue[i] = randomIdxsToDequeue[i];
            StdRandom.shuffle(shuffledIdxsToDequeue);
        }
        public boolean hasNext() { 
            return idx < size();
        }
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
        public Item next() {
            if (idx >= size()) throw new java.util.NoSuchElementException();
            return s[shuffledIdxsToDequeue[idx++]];
        }
    }
    private static void basicTests() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(222);
        System.out.println(rq.dequeue());
        rq.enqueue(219);
        System.out.println(rq.dequeue());
        rq.enqueue(285);
        System.out.println(rq.dequeue());
        RandomizedQueue<Integer> rqq = new RandomizedQueue<Integer>();
        rqq.enqueue(6);
        System.out.println(rqq.dequeue());
        rqq.enqueue(38);
        System.out.println(rqq.dequeue());
        rqq.enqueue(14);
        System.out.println(rqq.dequeue());
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
        
        while (!rqi.isEmpty())
            System.out.print(rqi.dequeue());
        System.out.println();
    }
    private static void freqTests() {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int [] results = new int[6];
        String result;
        for (int i = 0; i < 12000; i++) {
            rq.enqueue("A");
            rq.enqueue("B");
            rq.enqueue("C");
            
            result = "";
            for (int j = 0; j < 3; j++)
                result += rq.dequeue();
            
            switch (result) {
                case "ABC": results[0]++;
                break;
                case "ACB": results[1]++;
                break;
                case "BAC": results[2]++;
                break;
                case "BCA": results[3]++;
                break;
                case "CAB": results[4]++;
                break;
                case "CBA": results[5]++;
                break;
                default: System.out.println("unhandled result: " + result);
            }
        }
        System.out.println("ABC ACB BAC BCA CAB CBA");
        for (int i = 0; i < results.length; i++)
            System.out.print(results[i] + " ");
        System.out.println();
        
    }
    public static void main(String[] args) {  // unit testing (optional)
        freqTests();
    }
}