import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class Permutation { 
    public static void main(String[] args) {
        int m = 0;
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        if (k != 0) {
            while (!StdIn.isEmpty()) {
                String str = StdIn.readString();
                if (rq.size() < k) // add k items
                    rq.enqueue(str);
                else if (rq.size() == k){ // swap out an item with decreasing probability
                    int swap = StdRandom.uniform(m + 1);
                    if (swap == 0) {
                        // remove one item and add one back in
                        rq.dequeue();
                        rq.enqueue(str);
                    }
                }
                else System.out.println("k, m: " + k + ", " + m);
                m++;
            }
        }
        Iterator<String> i = rq.iterator();
        while (i.hasNext()) {
            String s = i.next();
            System.out.println(s);
        }
    }
}
