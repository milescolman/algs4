import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class FastCollinearPoints {
    private int numberOfSegments = 0;
    private LineSegment[] segments = new LineSegment[1];
    public FastCollinearPoints(Point[] points) {   // finds all line segments containing 4 or more points
        if (points == null) throw new java.lang.IllegalArgumentException("null argument to constructor");
        for (Point p : points) {
            if (p == null)
                throw new java.lang.IllegalArgumentException("null in points array");
        }
        Point[] auxPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++)
            auxPoints[i] = points[i];

        java.util.Arrays.sort(auxPoints);
        for (int i = 0; i < auxPoints.length - 1; i++) {
            if (auxPoints[i].compareTo(auxPoints[i + 1]) == 0)
                throw new java.lang.IllegalArgumentException("repeated point");
         }
        
        for (int i = 0; i < auxPoints.length; i++) {
            //java.util.Arrays.sort(auxPoints, 0, auxPoints.length); // sort by y, xcoordinate : include ith pt
            Point p1 = points[i]; //OK to use points instead of auxPoints ?
            Comparator<Point> slopeOrder = p1.slopeOrder(); 
            java.util.Arrays.sort(auxPoints, 0, auxPoints.length, slopeOrder); // sort by slope with p1
            for (int j = 0; j < auxPoints.length -  2; j++) {
                if (p1.slopeTo(auxPoints[j]) == Double.NEGATIVE_INFINITY) continue; //  pt1, should be at front
                int k = 1;
                for (; j + k < auxPoints.length &&
                     slopeOrder.compare(auxPoints[j], auxPoints[j + k]) == 0; k++) continue;
                k--; // k gets incremented an extra time
                if (k < 2) continue; // less than 4 consecutive values had same slope
                
                if (numberOfSegments == segments.length)
                    resize(2 * segments.length);
                // p1 may have lowest y value, auxPoints[j + k] has highest y value
                java.util.Arrays.sort(auxPoints, j, j + k + 1);
                // add only if origin is lowest point
                if (p1.compareTo(auxPoints[j]) < 0) {
                    LineSegment segment = new LineSegment(p1, auxPoints[j + k]);
                    segments[numberOfSegments++] = segment;
                }
                // increment j by k
                j += k;
            }
        }
        resize(numberOfSegments);
    }
        
    private void resize(int capacity) {
        LineSegment[] aux = new LineSegment[capacity];
        for (int i = 0; i < numberOfSegments; i++)
            aux[i] = segments[i];
        segments = aux;
    }
    
    public int numberOfSegments() {        // the number of line segments
        return numberOfSegments;
    }
    public LineSegment[] segments() {              // the line segments
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
        StdDraw.setPenRadius(0.005);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        StdDraw.setPenRadius(0.001);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println("Number of Segments: " + collinear.numberOfSegments());
        StdDraw.show();
    }
}