import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private final int numberOfSegments;
    private final LineSegment[] segments;
    public BruteCollinearPoints(Point[] points) {  // finds all line segments containing 4 points
        if (points == null) throw new java.lang.IllegalArgumentException("Constructor called with null argument");
        // check for null points in array
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException("At least one null argument in Point array");
        // check for repeated points in array without sorting
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException("dup point found: " + points[i].toString());
        
        // check if less than 4 pts input
        Point[] collinearPts = new Point[4];

        if (points.length < 4) {
            segments = new LineSegment[0];
            numberOfSegments = 0;
        }
        else { // count line segments containing 4 collinear pts
            int m = 0;
            for (int i = 0; i < points.length; i++) {
                Point p = points[i];
                for (int j = i + 1; j < points.length; j++) {
                    Point q = points[j];
                    double pqS = p.slopeTo(q);
                    for (int k = j + 1; k < points.length; k++) {
                        Point r = points[k];
                        double prS = p.slopeTo(r);
                        if (pqS != prS) continue; // skip 3rd slope if first 2 don't match
                        for (int l = k + 1; l < points.length; l++) {
                            Point s = points[l];
                            double psS = p.slopeTo(s);
                            if (pqS == psS) // found collinear set!
                                m++;
                                
                        }
                    }
                }
            }
            numberOfSegments = m;
            segments = new LineSegment[numberOfSegments];
            // record line segments containing 4 collinear pts
            m = 0;
            for (int i = 0; i < points.length; i++) {
                Point p = points[i];
                for (int j = i + 1; j < points.length; j++) {
                    Point q = points[j];
                    double pqS = p.slopeTo(q);
                    for (int k = j + 1; k < points.length; k++) {
                        Point r = points[k];
                        double prS = p.slopeTo(r);
                        if (pqS != prS) continue; // skip 3rd slope if first 2 don't match
                        for (int l = k + 1; l < points.length; l++) {
                            Point s = points[l];
                            double psS = p.slopeTo(s);
                            if (pqS == psS) { // found collinear set!
                                collinearPts[0] = p;
                                collinearPts[1] = q;
                                collinearPts[2] = r;
                                collinearPts[3] = s;
                                // order pts by y then x coordinates
                                java.util.Arrays.sort(collinearPts);
                                // take farthest-apart pts
                                Point pt1 = collinearPts[0];
                                Point pt2 = collinearPts[3];
                                segments[m++] = new LineSegment(pt1, pt2);
                            }
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments() {       // the number of line segments
        return numberOfSegments;
    }
    public LineSegment[] segments() {               // the line segments
        LineSegment[] aux = new LineSegment[segments.length];
        for (int i = 0; i < segments.length; i++)
            aux[i] = segments[i];
        return aux;
    }
    public static void main(String[] args) {
        
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}