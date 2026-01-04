package software.aoc.day12.a;

import java.util.Objects;

public class Point implements Comparable<Point> {
    final int r, c;

    public Point(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public Point rotate() {
        return new Point(c, -r);
    }

    public Point flip() {
        return new Point(r, -c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return r == point.r && c == point.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }

    @Override
    public int compareTo(Point o) {
        if (this.r != o.r) return Integer.compare(this.r, o.r);
        return Integer.compare(this.c, o.c);
    }
}
