import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;

public class Solver {
    private final boolean isSolvable;
    private final int moves;
    private final Iterable<Board> solution;
    private class SearchNode implements Comparable<SearchNode>{
        public Board predecessor;
        public Board current;
        public int moves;
        SearchNode(Board p, Board c, int m) {
            predecessor = p;
            current = c;
            moves = m;
        }
        public int compareTo( SearchNode compareNode ){
            return current.manhattan() + moves - 
                (compareNode.current.manhattan() + compareNode.moves);
        }
    }
    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        // twin has a random pair of locations switched
        Board twinBoard = initial.twin();
        MinPQ<SearchNode> mainPQ = new MinPQ<SearchNode> ();
        mainPQ.insert(new SearchNode(null, initial, 0));
        Stack<Board> mainBoardsToWin = new Stack<Board>();
        Board currentBoard = initial;
        
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode> ();
        twinPQ.insert(new SearchNode(null, twinBoard, 0));
        // solver loop
        
        int futureMoves;
        SearchNode mainNode = mainPQ.min();
        Board predecessorBoard;
        while (!currentBoard.isGoal() && !twinBoard.isGoal()) {
            mainNode = mainPQ.delMin();
            // handle empty queues ?
            currentBoard = mainNode.current;
            mainBoardsToWin.push(currentBoard);
            predecessorBoard = mainNode.predecessor;
            futureMoves = mainNode.moves + 1;
            for (Board board : currentBoard.neighbors())
                if (!board.equals(predecessorBoard)) 
                    mainPQ.insert(new SearchNode(currentBoard, board, futureMoves));
            
            SearchNode twinNode = twinPQ.delMin();
            twinBoard = twinNode.current;
            predecessorBoard = twinNode.predecessor;
            futureMoves = twinNode.moves + 1;
            for (Board board : twinBoard.neighbors())
                if (!board.equals(predecessorBoard)) 
                    twinPQ.insert(new SearchNode(twinBoard, board, futureMoves));
        }
        // tmp fix for those final keywords
        if (currentBoard.isGoal()) {
            isSolvable = true;
            moves = mainNode.moves;
            solution = mainBoardsToWin;
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
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}