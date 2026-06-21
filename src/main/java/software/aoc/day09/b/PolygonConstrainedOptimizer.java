package software.aoc.day09.b;

import software.aoc.day09.AreaOptimizationStrategy;
import software.aoc.day09.Tile;

import java.util.List;

public class PolygonConstrainedOptimizer implements AreaOptimizationStrategy {

    @Override
    public long optimize(List<Tile> tiles) {
        long maxArea = 0;
        int n = tiles.size();

        for (int i = 0; i < n; i++) {
            Tile t1 = tiles.get(i);
            for (int j = i + 1; j < n; j++) {
                long area = t1.calculateBoundingArea(tiles.get(j));
                if (area <= maxArea) continue;
                if (isRectangleInsidePolygon(t1, tiles.get(j), tiles)) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    private boolean isRectangleInsidePolygon(Tile p1, Tile p2, List<Tile> polygon) {
        long minX = Math.min(p1.x(), p2.x());
        long maxX = Math.max(p1.x(), p2.x());
        long minY = Math.min(p1.y(), p2.y());
        long maxY = Math.max(p1.y(), p2.y());
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Tile current = polygon.get(i);
            Tile next = polygon.get((i + 1) % n);
            long segMinX = Math.min(current.x(), next.x());
            long segMaxX = Math.max(current.x(), next.x());
            long segMinY = Math.min(current.y(), next.y());
            long segMaxY = Math.max(current.y(), next.y());

            if (current.y() == next.y()) {
                if (current.y() > minY && current.y() < maxY) {
                    if (Math.max(segMinX, minX) < Math.min(segMaxX, maxX)) {
                        return false;
                    }
                }
            } else if (current.x() == next.x()) {
                if (current.x() > minX && current.x() < maxX) {
                    if (Math.max(segMinY, minY) < Math.min(segMaxY, maxY)) {
                        return false;
                    }
                }
            }
        }
        double testX = (minX + maxX) / 2.0;
        double testY = (minY + maxY) / 2.0;

        for (int i = 0; i < n; i++) {
            Tile current = polygon.get(i);
            Tile next = polygon.get((i + 1) % n);
            long segMinX = Math.min(current.x(), next.x());
            long segMaxX = Math.max(current.x(), next.x());
            long segMinY = Math.min(current.y(), next.y());
            long segMaxY = Math.max(current.y(), next.y());

            if (current.y() == next.y()) {
                if (testY == current.y() && testX >= segMinX && testX <= segMaxX) return true;
            } else {
                if (testX == current.x() && testY >= segMinY && testY <= segMaxY) return true;
            }
        }
        boolean inside = false;
        for (int i = 0; i < n; i++) {
            Tile current = polygon.get(i);
            Tile next = polygon.get((i + 1) % n);

            if (current.x() == next.x()) {
                long segMinY = Math.min(current.y(), next.y());
                long segMaxY = Math.max(current.y(), next.y());
                long segX = current.x();

                if (segX > testX && testY >= segMinY && testY < segMaxY) {
                    inside = !inside;
                }
            }
        }
        return inside;
    }
}