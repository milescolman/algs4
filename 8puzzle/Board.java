import java.util.Stack;
public class Board {
    private int[][] blocks = null;
    private final int manhattanDist;
    private final int hammingDist;
    public Board(int[][] blocks) {          // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) throw new java.lang.IllegalArgumentException();
        // defensive copy
        int n = blocks.length;
        int[][] arr = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                arr[i][j] = blocks[i][j];
        this.blocks = arr;
        manhattanDist = manhattanCalc();
        hammingDist = hammingCalc();
    }
    public int dimension() {                // board dimension n
        return this.blocks.length;
    }
    private int hammingCalc() {                   // number of blocks out of place
        int n = dimension();
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != (i * n + j + 1))
                    hamming++;
            }
        }
        hamming--; // for zero block
        return hamming;
    }
    public int hamming() {
        return hammingDist;
    }
        
    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        return manhattanDist;
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
                    int vDistance = Math.abs(((val - 1) % n) - j);
                    int hDistance = Math.abs((val - 1) / n - i);
                    manhattan += vDistance + hDistance;                   
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
    public Board twin() {  // a board that is obtained by exchanging any pair of adjacent blocks
        // swap upper left with second from upper left location
        int i = 0;
        int j = 0;
        int k = 1; // 2?
        int m = 0;
        
        // check for empty block and re-choose if we selected it
        int a = blocks[i][j];
        if (a == 0) {
            i = 1;
            j = 1;
        }
        int tmp = blocks[k][m];
        if (tmp == 0) {
            k = 0;
            m = 1;
            tmp = blocks[k][m];
        }
        blocks[k][m] = blocks[i][j];
        blocks[i][j] = tmp;
        Board newBoard = new Board(blocks);
        // unswap!
        blocks[i][j] = blocks[k][m];
        blocks[k][m] = tmp;
        return newBoard;
    }
    public boolean equals(Object y) {       // does this board equal y?
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        int n = dimension();
        int nY = ((Board) y).blocks.length;
        if (n != nY) return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != ((Board) y).blocks[i][j])
                    return false;
        return true;
    }
    private void addNeighbor(Stack<Board> s, int i, int j, int iOffset, int jOffset) {
        blocks[i][j] = blocks[i+iOffset][j+jOffset];
        blocks[i+iOffset][j+jOffset] = 0;
        //create new Board, add to list
        Board b = new Board(blocks);
        s.push(b);
        //undo swap
        blocks[i+iOffset][j+jOffset] = blocks[i][j];
        blocks[i][j] = 0;
    }
    public Iterable<Board> neighbors() {     // all neighboring boards
        Stack<Board> boards = new Stack<Board>(); 
        int n = dimension();
        // find 0 location
        int i;
        int j = 0;
        rowLoop:
        for (i = 0; i < n; i++)
            for (j = 0; j < n; j++)
                if (blocks[i][j] == 0)
                    break rowLoop;
        if (i != 0)  // create Board with swapping with element above
            addNeighbor(boards, i, j, -1, 0);
        if (i != n - 1) { //create Board swapping element below
            addNeighbor(boards, i, j, +1, 0);
        }
        if (j != 0) { // create Board with swapping element to left
            addNeighbor(boards, i, j, 0, -1);
        }
        if (j != n - 1) { // create Board swapping element to right
            addNeighbor(boards, i, j, 0, +1);
        }             
        return boards;
    }
        
    public String toString() {              // string representation of this board (in the output format specified below)
        int n = dimension();
        StringBuilder builder = new StringBuilder();
        builder.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                builder.append(" " + blocks[i][j] + " ");
            builder.append("\n");
        }
        builder.append("\n");
        return builder.toString();
    }
    public static void main(String[] args) { // unit tests (not graded)
        int[][] arr = new int[][] {
                new int[] {8,1,3},
                new int[] {4,0,2},
                new int[] {7,6,5},
        };
        Board b = new Board(arr);
        System.out.println("hamming distance: " + b.hamming());
        System.out.println("manhattan distance: " + b.manhattan());
        System.out.println("is solved?: " + b.isGoal());
        int[][] arr2 = new int[][] {
                new int[] {0,2,3},
                new int[] {4,1,6},
                new int[] {7,8,5},
        };
        Board b2 = new Board(arr2);
        System.out.print("b2 pre-twin: " + b2.toString());
        Board b3 = b2.twin();
        Board b4 = new Board(arr2);
        System.out.print("b2 post-twin: " + b2.toString());
        System.out.print("twin: " + b3.toString());
        System.out.println("b2 equals b3? " + b2.equals(b3));
        System.out.println("b2 equals b4? " + b2.equals(b4));
        System.out.print("b4: " + b4.toString());
        System.out.println("***** Neighbors test. starting position in b4 above *****");
        Iterable<Board> neighbors = b4.neighbors();
        for (Board bb : neighbors)
            System.out.println(bb.toString());
    }
}
