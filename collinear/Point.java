/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        int deltaX = that.x - this.x;
        int deltaY = that.y - this.y;
        if (deltaX == 0 && deltaY == 0) return Double.NEGATIVE_INFINITY;
        if (deltaX == 0) return Double.POSITIVE_INFINITY;
        if (deltaY == 0) return +0.0;
        return (double) deltaY / deltaX;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0; // points are equal
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }
    
    private class SlopeComparator implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            // calculate slopes from this pt to p1 and this pt to p2 
            double s1 = slopeTo(p1);
            double s2 = slopeTo(p2);
            if (s1 < s2) return -1;
            if (s1 > s2) return +1;
            return 0;
        }
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    private static void testDraw() {
        int nX = 4;
        int nY = 4;
        Point[] pts = new Point[nX * nY];
        
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 8000);
        StdDraw.setYscale(0, 8000);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLUE);
        
        for (int i = 0; i < nX; i++)
            for (int j = 0; j < nY; j++) {
                Point pt = new Point(2000 * i, 2000 * j);
                pts[i * nX + j] = pt;
                pt.draw();
                System.out.println(pt.toString());
        }
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                Point pt1 = pts[i * nX + j]; 
                for (int m = 0; m < nX; m++) {
                    for (int n = 0; n < nY; n++) {
                         pt1.drawTo(pts[m * nX + n]);   
                    }
                }
            }
        }
        StdDraw.show();
    
    }

    
    public static void main(String[] args) {
        //    testDraw();
        Point pt1 = new Point(1, 1);
        Point ptHoriz = new Point(2, 1);
        Point ptVert = new Point(1, 2);
        Point pt2 = new Point(2, 2);
        
        System.out.println("y-x compare: should be -1 " + pt1.compareTo(pt2));
        System.out.println("y-x compare: should be 0 " + pt1.compareTo(pt1));
        System.out.println("y-x compare: should be -1 " + pt1.compareTo(ptVert));
        System.out.println("y-x compare: should be -1 " + pt1.compareTo(ptHoriz));

        System.out.println("Slope tests:");
        System.out.println("slope: should be 1: " + pt1.slopeTo(pt2));
        System.out.println("slope: should be -Inf: " + pt1.slopeTo(pt1));
        System.out.println("slope: should be +Inf: " + pt1.slopeTo(ptVert));
        System.out.println("slope: should be 0: " + pt1.slopeTo(ptHoriz));
    
        Comparator<Point> sO = pt1.slopeOrder();
        System.out.println("slope compare() should be -1: " + sO.compare(pt2, ptVert));
        System.out.println("slope compare() should be 1: " + sO.compare(pt2, ptHoriz));
        System.out.println("slope compare() should be 0: " + sO.compare(pt1, pt1));
    }
}