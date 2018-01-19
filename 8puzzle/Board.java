import java.lang.Math;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] blocks = null;
    private int manhattanDist = 0;
    private int moves = 0;
    public Board(int[][] blocks) {          // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) throw new java.lang.IllegalArgumentException();
        this.blocks = blocks;
    }
    public int dimension() {                // board dimension n
        return this.blocks.length;
    }
    public int hamming() {                   // number of blocks out of place
        int n = dimension();
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != (i * n + j + 1))
                    hamming++;
            }
        }
        hamming--; // for zero block
        return hamming + moves;
    }
        
    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        manhattanDist = manhattanCalc();
        return manhattanDist + moves;
    }
    private int manhattanCalc() {
        int manhattan = 0;
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = blocks[i][j];
                if (val != (i * n + j + 1) ) {
                    if (val == 0) 
                        continue;
                    int Vdistance = Math.abs(((val - 1) % n) - j);
                    int Hdistance = Math.abs((val - 1) / n - i);
                    System.out.println(val + " " + (Vdistance + Hdistance));
                    manhattan += Vdistance + Hdistance;                   
                }
            }
        }
        return manhattan;
    }    
    public boolean isGoal() {                // is this board the goal board?
        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i * n + j + 1)
                    return false;
            }
        }
        return true;
    }
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int n = dimension();
        int[][] arr = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                arr[i][j] = blocks[i][j];
        // swap random location
        int i = StdRandom.uniform(n);
        int j = StdRandom.uniform(n);
        int k = StdRandom.uniform(n);
        int m = StdRandom.uniform(n);
        while (i == k && j == m) // change m if i,j = j,m
            m = StdRandom.uniform(n);
        int tmp = arr[k][m];
        arr[k][m] = arr[i][j];
        arr[i][j] = tmp;
        
        return new Board(arr);
    }
//    public boolean equals(Object y)        // does this board equal y?
//    public Iterable<Board> neighbors()     // all neighboring boards
//    public String toString()               // string representation of this board (in the output format specified below)

    public static void main(String[] args) { // unit tests (not graded)
        int[][] arr = new int[][] {
                new int[] {8,1,3},
                new int[] {4,0,2},
                new int[] {7,6,5},
        };
        Board b = new Board(arr);
        System.out.println("manhattan distance: " + b.manhattan());
        System.out.println("is solved?: " + b.isGoal());
        int[][] arr2 = new int[][] {
                new int[] {1,2,3},
                new int[] {4,5,6},
                new int[] {7,8,0},
        };
        Board b2 = new Board(arr2);
        Board b3 = b2.twin();
    }
}