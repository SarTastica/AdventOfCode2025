package software.aoc.day09.b;

import software.aoc.day09.AreaOptimizationStrategy;
import software.aoc.day09.Rectangle;
import software.aoc.day09.Tile;

import java.util.List;

public class PolygonConstrainedOptimizer implements AreaOptimizationStrategy {

    @Override
    public long optimize(List<Tile> tiles) {
        long maxArea = 0;
        int n = tiles.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Rectangle rect = createRectangle(tiles.get(i), tiles.get(j));

                if (rect.area() > maxArea && isFullyInside(rect, tiles)) {
                    maxArea = rect.area();
                }
            }
        }
        return maxArea;
    }

    private Rectangle createRectangle(Tile t1, Tile t2) {
        return new Rectangle(
                Math.min(t1.x(), t2.x()), Math.max(t1.x(), t2.x()),
                Math.min(t1.y(), t2.y()), Math.max(t1.y(), t2.y())
        );
    }
    private boolean isFullyInside(Rectangle rect, List<Tile> polygon) {
        if (!isCenterInsidePolygon(rect, polygon)) return false;
        if (hasPolygonVerticesInsideRectangle(rect, polygon)) return false;
        if (hasPolygonEdgesCrossingRectangle(rect, polygon)) return false;

        return true;
    }

    private boolean isCenterInsidePolygon(Rectangle rect, List<Tile> polygon) {
        double x = rect.centerX();
        double y = rect.centerY();
        boolean inside = false;
        int n = polygon.size();

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Tile pi = polygon.get(i);
            Tile pj = polygon.get(j);

            if (((pi.y() > y) != (pj.y() > y)) &&
                    (x < (pj.x() - pi.x()) * (y - pi.y()) / (double) (pj.y() - pi.y()) + pi.x())) {
                inside = !inside;
            }
        }
        return inside;
    }

    private boolean hasPolygonVerticesInsideRectangle(Rectangle rect, List<Tile> polygon) {
        return polygon.stream().anyMatch(vertex -> isPointStrictlyInsideRectangle(vertex, rect));
    }

    private boolean isPointStrictlyInsideRectangle(Tile point, Rectangle rect) {
        return point.x() > rect.minX() && point.x() < rect.maxX() &&
                point.y() > rect.minY() && point.y() < rect.maxY();
    }

    private boolean hasPolygonEdgesCrossingRectangle(Rectangle rect, List<Tile> polygon) {
        int n = polygon.size();
        for (int i = 0; i < n; i++) {
            Tile p1 = polygon.get(i);
            Tile p2 = polygon.get((i + 1) % n);

            if (edgeCrossesRectangle(p1, p2, rect)) {
                return true;
            }
        }
        return false;
    }

    private boolean edgeCrossesRectangle(Tile p1, Tile p2, Rectangle rect) {
        if (isHorizontalEdge(p1, p2)) {
            return p1.y() > rect.minY() && p1.y() < rect.maxY() &&
                    Math.min(p1.x(), p2.x()) <= rect.minX() &&
                    Math.max(p1.x(), p2.x()) >= rect.maxX();
        }
        if (isVerticalEdge(p1, p2)) {
            return p1.x() > rect.minX() && p1.x() < rect.maxX() &&
                    Math.min(p1.y(), p2.y()) <= rect.minY() &&
                    Math.max(p1.y(), p2.y()) >= rect.maxY();
        }
        return false;
    }

    private boolean isHorizontalEdge(Tile p1, Tile p2) {
        return p1.y() == p2.y();
    }

    private boolean isVerticalEdge(Tile p1, Tile p2) {
        return p1.x() == p2.x();
    }
}