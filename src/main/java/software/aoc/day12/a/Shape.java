package software.aoc.day12.a;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Shape {
    private final int id;
    private final Set<Point> points;
    private final List<Set<Point>> variations;

    public Shape(int id, Set<Point> rawPoints) {
        this.id = id;
        this.points = normalize(rawPoints);
        this.variations = generateVariations();
    }

    public int getId() { return id; }
    public int getArea() { return points.size(); }
    public List<Set<Point>> getVariations() { return variations; }

    private List<Set<Point>> generateVariations() {
        Set<Set<Point>> uniqueVars = new HashSet<>();
        Set<Point> current = this.points;

        for (int i = 0; i < 4; i++) {
            uniqueVars.add(normalize(current));
            Set<Point> flipped = new HashSet<>();
            for (Point p : current) flipped.add(p.flip());
            uniqueVars.add(normalize(flipped));
            Set<Point> rotated = new HashSet<>();
            for (Point p : current) rotated.add(p.rotate());
            current = rotated;
        }

        return new ArrayList<>(uniqueVars);
    }

    private Set<Point> normalize(Set<Point> input) {
        int minR = Integer.MAX_VALUE;
        int minC = Integer.MAX_VALUE;
        for (Point p : input) {
            minR = Math.min(minR, p.r);
            minC = Math.min(minC, p.c);
        }
        Set<Point> normalized = new HashSet<>();
        for (Point p : input) {
            normalized.add(new Point(p.r - minR, p.c - minC));
        }
        return normalized;
    }
}
