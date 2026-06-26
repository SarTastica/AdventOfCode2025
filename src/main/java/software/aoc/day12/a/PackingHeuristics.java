package software.aoc.day12.a;

import java.util.List;
import java.util.Map;

public class PackingHeuristics {

    public boolean isViable(Grid grid, List<Integer> requiredShapes, Map<Integer, List<Polyomino>> variationsCache) {
        int safeZonesX = grid.getWidth() / 3;
        int safeZonesY = grid.getHeight() / 3;

        if (requiredShapes.size() <= safeZonesX * safeZonesY) {
            return true;
        }

        int totalPixelsNeeded = 0;
        for (int shapeId : requiredShapes) {
            totalPixelsNeeded += variationsCache.get(shapeId).get(0).blocks().size();
        }

        return totalPixelsNeeded <= grid.getWidth() * grid.getHeight();
    }
}