package software.aoc.day09.b;

import software.aoc.day09.*;
import java.util.List;

public class PolygonConstrainedOptimizer implements AreaOptimizationStrategy {

    @Override
    public long optimize(List<Tile> tiles) {
        long maxArea = 0;
        int n = tiles.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Rectangle rect = createRectangle(tiles.get(i), tiles.get(j));

                if (rect.area() <= maxArea) continue;

                if (isInside(rect, tiles)) {
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

    private boolean isInside(Rectangle rect, List<Tile> polygon) {
        double x = rect.centerX();
        double y = rect.centerY();
        boolean inside = false;
        int n = polygon.size();

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Tile pi = polygon.get(i);
            Tile pj = polygon.get(j);

            if (((pi.y() > y) != (pj.y() > y)) &&
                    (x < (pj.x() - pi.x()) * (y - pi.y()) / (double)(pj.y() - pi.y()) + pi.x())) {
                inside = !inside;
            }
        }
        return inside;
    }
}