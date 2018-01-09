import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte CLOSED = 0;
    private static final byte OPEN = 1;
    private static final byte BOTTOM_CONNECTED = 2;
    private static final byte TOP_CONNECTED = 4;
    
    private final int gridSize;
    private byte[] openSites;
    private int totalOpenSites = 0;
    private final WeightedQuickUnionUF connectedSites;
    private boolean percolates = false;
    
    public Percolation(int n) {          // create n-by-y grid, with all sites blocked
        gridSize = n;
        if (n < 1) {
            throw new java.lang.IllegalArgumentException("grid size < 1");
        }
        // create length n by n openSites array and initialize to closed
        openSites = new byte[xyTo1D(n, n) + 1];
        for (int row = 2; row < n; row++) {
            for (int col = 1; col <= n; col++) {
                int idx = xyTo1D(row, col);
                openSites[idx] = CLOSED;
            }
        }
        // initialize top row to be top connected
        // initialize last row to be bottom connected
        // they might be the same row!
        for (int col = 1; col <= n; col++) {
            int idx = xyTo1D(n, col);
            openSites[idx] = BOTTOM_CONNECTED;
            idx = xyTo1D(1, col);
            openSites[idx] |= TOP_CONNECTED; // use |= bc 1 x 1 grids are possible
        }
        // initialize Weighted Quick Union UF 
        connectedSites = new WeightedQuickUnionUF(xyTo1D(n, n) + 1);

     //   System.out.println(java.util.Arrays.toString(openSites));
    }
    public void open(int row, int col) {  // open site (row, col), checking if open already
        checkXY(row, col);
        int idx = xyTo1D(row, col);
        if (!isOpen(row, col)) {
            totalOpenSites++;
            
            int newStatus = openSites[idx] | OPEN; // might have top / bottom connected set already
            
            int upperRootIdx = Integer.MIN_VALUE;
            int leftRootIdx = Integer.MIN_VALUE;
            int lowerRootIdx = Integer.MIN_VALUE;
            int rightRootIdx = Integer.MIN_VALUE;
            // check upper cell, do connection
            if (row - 1 >= 1 && isOpen(row - 1, col)) {
                upperRootIdx = connectedSites.find(xyTo1D(row - 1, col));
                newStatus |= openSites[upperRootIdx];
                connectedSites.union(idx, xyTo1D(row - 1, col));
            }
            // check left cell, do connection
            if (col - 1 >= 1 && isOpen(row, col - 1)) {
                leftRootIdx = connectedSites.find(xyTo1D(row, col - 1));
                newStatus |= openSites[leftRootIdx];
                connectedSites.union(idx, xyTo1D(row, col - 1));
            }
            // check lower cell, do connection
            if (row + 1 <= gridSize && isOpen(row + 1, col)) {
                lowerRootIdx = connectedSites.find(xyTo1D(row + 1, col));
                newStatus |= openSites[lowerRootIdx];
                connectedSites.union(idx, xyTo1D(row + 1, col));
            }
            // check right cell, do connection
            if (col + 1 <= gridSize && isOpen(row, col + 1)) {
                rightRootIdx = connectedSites.find(xyTo1D(row, col + 1));
                newStatus |= openSites[rightRootIdx];
                connectedSites.union(idx, xyTo1D(row, col + 1));
            }
            // either element is its own root or one of its neighbor has the same root
            // updating all the neighboring root statuses skips another find for the element root 
            openSites[idx] |= newStatus;
            if (upperRootIdx != Integer.MIN_VALUE)
                openSites[upperRootIdx] |= newStatus;
            if (leftRootIdx != Integer.MIN_VALUE)
                openSites[leftRootIdx] |= newStatus;
            if (lowerRootIdx != Integer.MIN_VALUE)
                openSites[lowerRootIdx] |= newStatus;
            if (rightRootIdx != Integer.MIN_VALUE)
                openSites[rightRootIdx] |= newStatus;
            
            // check for percolation
            if (((newStatus & TOP_CONNECTED) != 0) && ((newStatus & BOTTOM_CONNECTED) != 0)) {
                percolates = true;
            }
//            System.out.println(java.util.Arrays.toString(openSites));
        }
    }
    public boolean isOpen(int row, int col) {
        checkXY(row, col);
        int idx = xyTo1D(row, col);
        return ((openSites[idx] & OPEN) != 0);
    }
    public boolean isFull(int row, int col) {
        checkXY(row, col);
        int root = connectedSites.find(xyTo1D(row, col));
        return isOpen(row, col) && ((openSites[root] & TOP_CONNECTED) != 0);
    }
    public boolean percolates() {
        return percolates;
    } 
        
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

