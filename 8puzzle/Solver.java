import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;
import java.util.ArrayDeque;

public class Solver {
    private final boolean isSolvable;
    private final int moves;
    private final Iterable<Board> solution;
    private class SearchNode implements Comparable<SearchNode> {
        public SearchNode predecessor;
        public Board current;
        public int moves;
        public int manhattan;
        SearchNode(SearchNode p, Board c, int m, int n) {
            predecessor = p;
            current = c;
            moves = m;
            manhattan = n;
        }
        public int compareTo( SearchNode compareNode ){
            return manhattan + moves - 
                (compareNode.manhattan + compareNode.moves);
        }
        public String toString(){
            return "moves: " + moves + " manhattan: " + manhattan + " priority: " + (moves + manhattan);
        }
    }
    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        if (initial == null) throw new java.lang.IllegalArgumentException();

        // twin has a random pair of locations switched
        Board twinBoard = initial.twin();
        MinPQ<SearchNode> mainPQ = new MinPQ<SearchNode> ();
        mainPQ.insert(new SearchNode(null, initial, 0, initial.manhattan()));
        Board currentBoard = initial;        
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode> ();
        twinPQ.insert(new SearchNode(null, twinBoard, 0, twinBoard.manhattan()));
        // solver loop
        
        int futureMoves;
        SearchNode mainNode = mainPQ.min();
        Board predecessorBoard;
        boolean doCheck;
        while (!currentBoard.isGoal() && !twinBoard.isGoal()) {
            mainNode = mainPQ.delMin();
            currentBoard = mainNode.current;
            predecessorBoard = (mainNode.predecessor != null) ? mainNode.predecessor.current : null;
            futureMoves = mainNode.moves + 1;
            doCheck = true;
            for (Board board : currentBoard.neighbors()) {
                
                if (doCheck && board.equals(predecessorBoard)) {
                    doCheck = false;
                    continue;
                }
                mainPQ.insert(new SearchNode(mainNode, board, futureMoves, board.manhattan()));
            }
            SearchNode twinNode = twinPQ.delMin();
            twinBoard = twinNode.current;
            predecessorBoard = twinNode.predecessor != null ? twinNode.predecessor.current : null;
            futureMoves = twinNode.moves + 1;
            doCheck = true;
            for (Board board : twinBoard.neighbors()) {
                if (doCheck && board.equals(predecessorBoard)) {
                    doCheck = false;
                    continue;
                }   
                twinPQ.insert(new SearchNode(twinNode, board, futureMoves, board.manhattan()));
            }
        }
        // tmp fix for those final keywords
        if (currentBoard.isGoal()) {
            isSolvable = true;
            moves = mainNode.moves;
            ArrayDeque<Board> tmp = new ArrayDeque<Board>();
            while (mainNode != null) {
                tmp.addFirst(mainNode.current);
                mainNode = mainNode.predecessor;
            }
            solution = tmp;
        } else {
            isSolvable = false;
            moves = -1;
            solution = null;
        }
    }
    public boolean isSolvable() {           // is the initial board solvable?
        return isSolvable;
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return moves;
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        return solution;
    }
    public static void main(String[] args) { // solve a slider puzzle
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            int i = 0;
            for (Board board : solver.solution()) {
                StdOut.println("solution no: " + i);
                StdOut.println(board);
                i++;
            }
        }
    }
}