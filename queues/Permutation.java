import edu.princeton.cs.algs4.StdIn;


public class Permutation {
    
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) { 
            rq.enqueue(StdIn.readString());
            if (rq.size() > k)
              rq.dequeue();  
        }
         for (String s : rq)
            System.out.println(s);
    }
}