package software.aoc.day12.a;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Polyomino(Set<Point> blocks, int width, int height) {

    public List<Polyomino> generateUniqueVariations() {
        Set<Set<Point>> uniqueConfigurations = new HashSet<>();
        List<Polyomino> variations = new ArrayList<>();

        List<Point> current = new ArrayList<>(blocks);
        for (int flip = 0; flip < 2; flip++) {
            for (int rot = 0; rot < 4; rot++) {
                Polyomino normalized = normalize(current);
                if (uniqueConfigurations.add(normalized.blocks())) {
                    variations.add(normalized);
                }
                current = rotate90(current);
            }
            current = flipHorizontal(current);
        }
        return variations;
    }

    private Polyomino normalize(List<Point> pts) {
        int minX = pts.stream().mapToInt(Point::x).min().orElse(0);
        int minY = pts.stream().mapToInt(Point::y).min().orElse(0);
        int maxX = pts.stream().mapToInt(Point::x).max().orElse(0);
        int maxY = pts.stream().mapToInt(Point::y).max().orElse(0);

        Set<Point> norm = new HashSet<>();
        for (Point p : pts) {
            norm.add(new Point(p.x() - minX, p.y() - minY));
        }
        return new Polyomino(norm, maxX - minX + 1, maxY - minY + 1);
    }

    private List<Point> rotate90(List<Point> pts) {
        return pts.stream().map(p -> new Point(-p.y(), p.x())).toList();
    }

    private List<Point> flipHorizontal(List<Point> pts) {
        return pts.stream().map(p -> new Point(-p.x(), p.y())).toList();
    }
}