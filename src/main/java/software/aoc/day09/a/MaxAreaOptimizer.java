package software.aoc.day09.a;

import software.aoc.day09.AreaOptimizationStrategy;
import software.aoc.day09.Tile;
import java.util.List;

public class MaxAreaOptimizer implements AreaOptimizationStrategy {
    @Override
    public long optimize(List<Tile> tiles) {
        long maxArea = 0;
        int n = tiles.size();

        for (int i = 0; i < n; i++) {
            Tile t1 = tiles.get(i);
            for (int j = i + 1; j < n; j++) {
                long area = t1.calculateBoundingArea(tiles.get(j));
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }
}