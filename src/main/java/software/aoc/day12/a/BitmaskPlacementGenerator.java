package software.aoc.day12.a;

import java.util.*;

public class BitmaskPlacementGenerator {

    public List<ShapeGroup> generate(Grid grid, List<Integer> requiredShapes, Map<Integer, List<Polyomino>> variationsCache) {
        Map<Integer, Integer> shapeCounts = new HashMap<>();
        for (int shapeId : requiredShapes) {
            shapeCounts.put(shapeId, shapeCounts.getOrDefault(shapeId, 0) + 1);
        }

        int totalCells = grid.getWidth() * grid.getHeight();
        List<ShapeGroup> initialGroups = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : shapeCounts.entrySet()) {
            int shapeId = entry.getKey();
            int qty = entry.getValue();

            List<BitSet> validPlacements = new ArrayList<>();
            for (Polyomino variation : variationsCache.get(shapeId)) {
                for (int y = 0; y <= grid.getHeight() - variation.height(); y++) {
                    for (int x = 0; x <= grid.getWidth() - variation.width(); x++) {
                        BitSet mask = new BitSet(totalCells);
                        for (Point p : variation.blocks()) {
                            mask.set((y + p.y()) * grid.getWidth() + (x + p.x()));
                        }
                        validPlacements.add(mask);
                    }
                }
            }

            if (validPlacements.size() < qty) return null;
            initialGroups.add(new ShapeGroup(qty, validPlacements));
        }

        return initialGroups;
    }
}