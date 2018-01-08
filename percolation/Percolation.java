import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final boolean CLOSED = false;
    private static final boolean OPEN = true;

    private final int gridSize; // instance variable
    private final int startNodeIdx; // first node idx
    private final int endNodeIdx; // end node idx
    private boolean[] openSites;
    private int totalOpenSites = 0;
    private final WeightedQuickUnionUF connectedSites;
    
    public Percolation(int n) {          // create n-by-y grid, with all sites blocked
        this.gridSize = n;
        startNodeIdx = xyTo1D(n, n) + 1;
        endNodeIdx = xyTo1D(n, n) + 2;
        if (n < 1) {
            throw new java.lang.IllegalArgumentException("grid size < 1");
        }
        // create openSites array and initialize to closed
        this.openSites = new boolean[xyTo1D(n, n) + 1];
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                int idx = xyTo1D(row, col);
                this.openSites[idx] = CLOSED;
            }
        }
        // initialize Weighted Quick Union UF start and end sites 
        this.connectedSites = new WeightedQuickUnionUF(endNodeIdx + 1);
    }
    public void open(int row, int col) {  // open site (row, col), checking if open already
        checkXY(row, col);
        int idx = xyTo1D(row, col);
        if (!isOpen(row, col)) {
            this.openSites[idx] = OPEN;
            totalOpenSites++;
            // check upper cell, do connection
            if (row - 1 >= 1 && this.isOpen(row - 1, col)) {
                this.connectedSites.union(idx, xyTo1D(row - 1, col));
            }
            // check left cell, do connection
            if (col - 1 >= 1 && this.isOpen(row, col - 1)) {
                this.connectedSites.union(idx, xyTo1D(row, col - 1));
            }
            // check lower cell, do connection
            if (row + 1 <= this.gridSize && this.isOpen(row + 1, col)) {
                this.connectedSites.union(idx, xyTo1D(row + 1, col));
            }
            // check right cell, do connection
            if (col + 1 <= this.gridSize && this.isOpen(row, col + 1)) {
                this.connectedSites.union(idx, xyTo1D(row, col + 1));
            }
            // first row additional connection
            if (row == 1) {
                this.connectedSites.union(idx, startNodeIdx);
            } 
            if (row == gridSize) { // last row additional connection
                this.connectedSites.union(idx, endNodeIdx);
            }
        }
    }
    public boolean isOpen(int row, int col) {
        checkXY(row, col);
        int idx = xyTo1D(row, col);
        return (this.openSites[idx] == OPEN);
    }
    public boolean isFull(int row, int col) {
        checkXY(row, col);
        // don't neeed isOpen check
        return this.connectedSites.connected(xyTo1D(row, col), startNodeIdx);
    }
    public boolean percolates() {
        return this.connectedSites.connected(startNodeIdx, endNodeIdx);
    }  // does the system percolate?
        
    public static void main(String[] args) { // test client (optional)
        Percolation perc = new Percolation(2);
        System.out.println(perc.isOpen(2, 1) + " " + perc.isFull(2, 1));
        perc.open(2, 1);
        System.out.println(perc.isOpen(2, 1) + " " + perc.isFull(2, 1));
        System.out.println(perc.percolates());
    }
    public int numberOfOpenSites() {
        return totalOpenSites;
    } 
    private int xyTo1D(int row, int col) {
        return this.gridSize * row + col;
    }
    private void checkXY(int row, int col) {
        if (row < 1 || row > this.gridSize) {
            throw new java.lang.IllegalArgumentException("invalid row index");
        }
        if (col < 1 || col > this.gridSize) {
            throw new java.lang.IllegalArgumentException("invalid col index");
        }
    }
}

