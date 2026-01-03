package software.aoc.day09.a;

import java.util.List;
import java.util.stream.Collectors;

public class MovieTheater {
    private static class Tile {
        final int x;
        final int y;

        public Tile(String line) {
            String[] parts = line.split(",");
            this.x = Integer.parseInt(parts[0].trim());
            this.y = Integer.parseInt(parts[1].trim());
        }
    }

    public long findLargestRectangleArea(List<String> lines) {
        List<Tile> tiles = lines.stream()
                .filter(line -> !line.isBlank())
                .map(Tile::new)
                .collect(Collectors.toList());

        long maxArea = 0;

        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile t1 = tiles.get(i);
                Tile t2 = tiles.get(j);

                long width = Math.abs(t1.x - t2.x) + 1;
                long height = Math.abs(t1.y - t2.y) + 1;
                long area = width * height;

                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }

        return maxArea;
    }
}
