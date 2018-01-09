import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int CLOSED = 0x0000;
    private static final int OPEN = 0x0001;
    private static final int BOTTOM_CONNECTED = 0x0002;
    
    private final int gridSize; // instance variable
    private final int startNodeIdx; // first node idx
    private int[] openSites;
    private int totalOpenSites = 0;
    private final WeightedQuickUnionUF connectedSites;
    private boolean percolates = false;
    
    public Percolation(int n) {          // create n-by-y grid, with all sites blocked
        this.gridSize = n;
        startNodeIdx = xyTo1D(n, n) + 1;
        if (n < 1) {
            throw new java.lang.IllegalArgumentException("grid size < 1");
        }
        // create length n by n openSites array and initialize to closed
        this.openSites = new int[xyTo1D(n, n) + 1];
        for (int row = 1; row < n; row++) {
            for (int col = 1; col <= n; col++) {
                int idx = xyTo1D(row, col);
                this.openSites[idx] = CLOSED;
            }
        }
        
        // initialize last row to be bottom connected
        for (int col = 1; col <= n; col++) {
            int idx = xyTo1D(n, col);
            openSites[idx] = BOTTOM_CONNECTED;
        }
        // initialize Weighted Quick Union UF with start virtual node 
        this.connectedSites = new WeightedQuickUnionUF(startNodeIdx + 1);

//        System.out.println(java.util.Arrays.toString(openSites));
    }
    public void open(int row, int col) {  // open site (row, col), checking if open already
        checkXY(row, col);
        int idx = xyTo1D(row, col);
        if (!isOpen(row, col)) {
            totalOpenSites++;
            
            int newStatus = openSites[idx] | OPEN; // might have bottom connected set already
            
            // set up first row connection to the start node
            if (row == 1) {
                connectedSites.union(idx, startNodeIdx);
            } 
            
            // check upper cell, do connection
            if (row - 1 >= 1 && this.isOpen(row - 1, col)) {
                int upperRootIdx = this.connectedSites.find(xyTo1D(row - 1, col));
                newStatus |= openSites[upperRootIdx];
                this.connectedSites.union(idx, xyTo1D(row - 1, col));
            }
            // check left cell, do connection
            if (col - 1 >= 1 && this.isOpen(row, col - 1)) {
                int leftRootIdx = this.connectedSites.find(xyTo1D(row, col - 1));
                newStatus |= openSites[leftRootIdx];
                this.connectedSites.union(idx, xyTo1D(row, col - 1));
            }
            // check lower cell, do connection
            if (row + 1 <= this.gridSize && this.isOpen(row + 1, col)) {
                int lowerRootIdx = this.connectedSites.find(xyTo1D(row + 1, col));
                newStatus |= openSites[lowerRootIdx];
                this.connectedSites.union(idx, xyTo1D(row + 1, col));
            }
            // check right cell, do connection
            if (col + 1 <= this.gridSize && this.isOpen(row, col + 1)) {
                int rightRootIdx = this.connectedSites.find(xyTo1D(row, col + 1));
                newStatus |= openSites[rightRootIdx];
                this.connectedSites.union(idx, xyTo1D(row, col + 1));
            }
            openSites[idx] |= newStatus;
            int rootIdx = connectedSites.find(idx);
            openSites[rootIdx] |= newStatus;

            // check for percolation
            int startRootIdx = connectedSites.find(startNodeIdx);
            if ((startRootIdx == rootIdx) && ((newStatus & BOTTOM_CONNECTED) != 0)) {
                percolates = true;
            }
//            System.out.println(java.util.Arrays.toString(openSites));
        }
    }
    public boolean isOpen(int row, int col) {
        checkXY(row, col);
        int idx = xyTo1D(row, col);
        return ((this.openSites[idx] & OPEN) != 0);
    }
    public boolean isFull(int row, int col) {
        checkXY(row, col);
        return this.connectedSites.connected(xyTo1D(row, col), startNodeIdx);
    }
    public boolean percolates() {
        return percolates;
    }  // does the system percolate?
        
    public static void main(String[] args) { // test client (optional)
        Percolation perc = new Percolation(1);
        System.out.println(perc.isOpen(1, 1) + " " + perc.isFull(1, 1));
        perc.open(1, 1);
        System.out.println(perc.isOpen(1, 1) + " " + perc.isFull(1, 1));
        System.out.println(perc.percolates());
        System.out.println("***************************");
        Percolation perc2 = new Percolation(2);
        perc2.open(2, 1);
        perc2.open(1, 1);
        System.out.println(perc2.percolates());
    
    }
    public int numberOfOpenSites() {
        return totalOpenSites;
    } 
    private int xyTo1D(int row, int col) {
        return this.gridSize * (row - 1) + (col - 1);
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

