package software.aoc.day09.b;

import java.util.ArrayList;
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
        public Tile(int x, int y) { this.x = x; this.y = y; }
    }

    public long findLargestValidRectangleArea(List<String> lines) {
        List<Tile> tiles = lines.stream()
                .filter(line -> !line.isBlank())
                .map(Tile::new)
                .collect(Collectors.toList());

        long maxArea = 0;

        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile t1 = tiles.get(i);
                Tile t2 = tiles.get(j);

                int minX = Math.min(t1.x, t2.x);
                int maxX = Math.max(t1.x, t2.x);
                int minY = Math.min(t1.y, t2.y);
                int maxY = Math.max(t1.y, t2.y);

                if (boundaryCrossesInterior(tiles, minX, maxX, minY, maxY)) {
                    continue;
                }

                double midX = (minX + maxX) / 2.0;
                double midY = (minY + maxY) / 2.0;
                if (!isPointInPolygon(midX, midY, tiles)) {
                    continue;
                }

                long width = (maxX - minX) + 1;
                long height = (maxY - minY) + 1;
                long area = width * height;
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    private boolean boundaryCrossesInterior(List<Tile> polygon, int rMinX, int rMaxX, int rMinY, int rMaxY) {
        int n = polygon.size();
        for (int i = 0; i < n; i++) {
            Tile p1 = polygon.get(i);
            Tile p2 = polygon.get((i + 1) % n);

            if (p1.x == p2.x) {
                int wallX = p1.x;
                int wallY1 = Math.min(p1.y, p2.y);
                int wallY2 = Math.max(p1.y, p2.y);
                if (wallX > rMinX && wallX < rMaxX) {
                    if (Math.max(wallY1, rMinY) < Math.min(wallY2, rMaxY)) {
                        return true;
                    }
                }
            }
            else if (p1.y == p2.y) {
                int wallY = p1.y;
                int wallX1 = Math.min(p1.x, p2.x);
                int wallX2 = Math.max(p1.x, p2.x);
                if (wallY > rMinY && wallY < rMaxY) {
                    if (Math.max(wallX1, rMinX) < Math.min(wallX2, rMaxX)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPointInPolygon(double x, double y, List<Tile> polygon) {
        boolean inside = false;
        int n = polygon.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Tile pi = polygon.get(i);
            Tile pj = polygon.get(j);

            boolean intersect = ((pi.y > y) != (pj.y > y)) &&
                    (x < (pj.x - pi.x) * (y - pi.y) / (double)(pj.y - pi.y) + pi.x);

            if (intersect) {
          package software.aoc.day09.b;

import java.util.ArrayList;
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
                        public Tile(int x, int y) { this.x = x; this.y = y; }
                    }

                    public long findLargestValidRectangleArea(List<String> lines) {
                        List<software.aoc.day09.b.MovieTheater.Tile> tiles = lines.stream()
                                .filter(line -> !line.isBlank())
                                .map(software.aoc.day09.b.MovieTheater.Tile::new)
                                .collect(Collectors.toList());

                        long maxArea = 0;

                        for (int i = 0; i < tiles.size(); i++) {
                            for (int j = i + 1; j < tiles.size(); j++) {
                                software.aoc.day09.b.MovieTheater.Tile t1 = tiles.get(i);
                                software.aoc.day09.b.MovieTheater.Tile t2 = tiles.get(j);

                                int minX = Math.min(t1.x, t2.x);
                                int maxX = Math.max(t1.x, t2.x);
                                int minY = Math.min(t1.y, t2.y);
                                int maxY = Math.max(t1.y, t2.y);

                                if (boundaryCrossesInterior(tiles, minX, maxX, minY, maxY)) {
                                    continue;
                                }

                                double midX = (minX + maxX) / 2.0;
                                double midY = (minY + maxY) / 2.0;
                                if (!isPointInPolygon(midX, midY, tiles)) {
                                    continue;
                                }

                                long width = (maxX - minX) + 1;
                                long height = (maxY - minY) + 1;
                                long area = width * height;
                                if (area > maxArea) {
                                    maxArea = area;
                                }
                            }
                        }
                        return maxArea;
                    }

                    private boolean boundaryCrossesInterior(List<software.aoc.day09.b.MovieTheater.Tile> polygon, int rMinX, int rMaxX, int rMinY, int rMaxY) {
                        int n = polygon.size();
                        for (int i = 0; i < n; i++) {
                            software.aoc.day09.b.MovieTheater.Tile p1 = polygon.get(i);
                            software.aoc.day09.b.MovieTheater.Tile p2 = polygon.get((i + 1) % n);

                            if (p1.x == p2.x) {
                                int wallX = p1.x;
                                int wallY1 = Math.min(p1.y, p2.y);
                                int wallY2 = Math.max(p1.y, p2.y);
                                if (wallX > rMinX && wallX < rMaxX) {
                                    if (Math.max(wallY1, rMinY) < Math.min(wallY2, rMaxY)) {
                                        return true;
                                    }
                                }
                            }
                            else if (p1.y == p2.y) {
                                int wallY = p1.y;
                                int wallX1 = Math.min(p1.x, p2.x);
                                int wallX2 = Math.max(p1.x, p2.x);
                                if (wallY > rMinY && wallY < rMaxY) {
                                    if (Math.max(wallX1, rMinX) < Math.min(wallX2, rMaxX)) {
                                        return true;
                                    }
                                }
                            }
                        }
                        return false;
                    }

                    private boolean isPointInPolygon(double x, double y, List<software.aoc.day09.b.MovieTheater.Tile> polygon) {
                        boolean inside = false;
                        int n = polygon.size();
                        for (int i = 0, j = n - 1; i < n; j = i++) {
                            software.aoc.day09.b.MovieTheater.Tile pi = polygon.get(i);
                            software.aoc.day09.b.MovieTheater.Tile pj = polygon.get(j);

                            boolean intersect = ((pi.y > y) != (pj.y > y)) &&
                                    (x < (pj.x - pi.x) * (y - pi.y) / (double)(pj.y - pi.y) + pi.x);

                            if (intersect) {
                                inside = !inside;
                            }
                        }
                        return inside;
                    }
                }      inside = !inside;
            }
        }
        return inside;
    }
}