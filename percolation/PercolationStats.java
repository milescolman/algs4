import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;


public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    
    private final int trials;
    private final double mean;
    private final double stddev;

    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        double [] toPercolate;
        
        if (n < 1 || trials < 1) {
            throw new java.lang.IllegalArgumentException("grid size or num trials was less than 1");
        }
        this.trials = trials;

        toPercolate = new double[trials]; // initalize test results array
            
        for (int m = 0; m < trials; m++) {
            Percolation perc = new Percolation(n);
            int toPerc = 0;
            while (!perc.percolates()) {
                int i = StdRandom.uniform(n) + 1;
                int j = StdRandom.uniform(n) + 1;
                if (!perc.isOpen(i, j)) {
                    perc.open(i, j);
                    toPerc++;
                }
            }
            toPercolate[m] = toPerc / ((double) n * n);
        }
        mean = StdStats.mean(toPercolate);
        stddev = (trials == 1) ? Double.NaN : StdStats.stddev(toPercolate); 

        // array of count of open() operations to percolate / (total grid squares)
        // System.out.println("to Percolate: " + java.util.Arrays.toString(toPercolate));
    }
    public double mean() { // experimental mean of percolation threshold
        return mean;
    }
    public double stddev() { // experimental std dev of percolation threshold
        return stddev;
    }
    public double confidenceLo() { // low endpoint of 95% confidence interval
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }
    public double confidenceHi() { // hi endpoint of 95% confidence interval
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }
    
    public static void main(String[] args) { // test client
        
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        System.out.println(n + " by " + n + " grid with " + trials + " trials");
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats pstats = new PercolationStats(n, trials);
        double elapsedTime = stopwatch.elapsedTime();
        System.out.println("time to run: " + elapsedTime);
        System.out.println("mean = " + pstats.mean());
        System.out.println("stddev = " + pstats.stddev());
        double [] confidences;
        confidences = new double[2];
        confidences[0] = pstats.confidenceLo();
        confidences[1] = pstats.confidenceHi();
        System.out.println("95% confidence interval = [" + confidences[0] + ", " + confidences[1] + "]");
    }
}