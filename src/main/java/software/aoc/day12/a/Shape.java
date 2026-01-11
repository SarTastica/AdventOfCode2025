package software.aoc.day12.a;

import java.util.*;
import java.util.stream.Collectors;

public class Shape {
    private final int id;
    private final List<Set<Point>> variations;

    public Shape(int id, Set<Point> rawPoints) {
        this.id = id;
        this.variations = generateVariations(normalize(rawPoints));
    }

    public int getId() { return id; }
    public int getArea() { return variations.get(0).size(); }
    public List<Set<Point>> getVariations() { return variations; }

    private List<Set<Point>> generateVariations(Set<Point> base) {
        Set<Set<Point>> uniqueVars = new HashSet<>();
        Set<Point> current = base;

        for (int i = 0; i < 4; i++) {
            uniqueVars.add(normalize(current));

            Set<Point> flipped = current.stream().map(Point::flip).collect(Collectors.toSet());

            uniqueVars.add(normalize(flipped));

            current = current.stream().map(Point::rotate).collect(Collectors.toSet());
        }
        return new ArrayList<>(uniqueVars);
    }

    private Set<Point> normalize(Set<Point> input) {
        int minR = input.stream().mapToInt(p -> p.r).min().orElse(0);
        int minC = input.stream().mapToInt(p -> p.c).min().orElse(0);
        return input.stream()
                .map(p -> new Point(p.r - minR, p.c - minC))
                .collect(Collectors.toSet());
    }
}
