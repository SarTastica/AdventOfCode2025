package software.aoc.day09.b;

import java.awt.Polygon;
import java.util.List;
import java.util.stream.Collectors;

public class MovieTheater {

    private static class Tile {
        final int x, y;
        public Tile(String line) {
            String[] parts = line.split(",");
            this.x = Integer.parseInt(parts[0].trim());
            this.y = Integer.parseInt(parts[1].trim());
        }
    }

    public long findLargestValidRectangleArea(List<String> lines) {
        List<Tile> tiles = lines.stream()
                .filter(l -> !l.isBlank())
                .map(Tile::new)
                .collect(Collectors.toList());

        java.awt.Polygon boundary = new java.awt.Polygon();
        for (Tile t : tiles) {
            boundary.addPoint(t.x, t.y);
        }

        long maxArea = 0;

        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile t1 = tiles.get(i);
                Tile t2 = tiles.get(j);

                int minX = Math.min(t1.x, t2.x);
                int minY = Math.min(t1.y, t2.y);
                int width = Math.abs(t1.x - t2.x);
                int height = Math.abs(t1.y - t2.y);

                if (width == 0 || height == 0) continue;

                if (boundary.contains(minX + 0.01, minY + 0.01, width - 0.02, height - 0.02)) {

                    long area = (long) (width + 1) * (height + 1);
                    if (area > maxArea) {
                        maxArea = area;
                    }
                }
            }
        }
        return maxArea;
    }
}