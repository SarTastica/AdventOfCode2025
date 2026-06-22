package software.aoc.day12.a;

import java.util.*;

public class DfsPackingOptimizer implements PackingStrategy {

    private record ShapeGroup(int remainingQty, List<BitSet> validPlacements) {}

    @Override
    public boolean canPackAll(Grid grid, List<Integer> requiredShapes, Map<Integer, List<Polyomino>> variationsCache) {
        int totalPresents = requiredShapes.size();
        int safeZonesX = grid.getWidth() / 3;
        int safeZonesY = grid.getHeight() / 3;

        if (totalPresents <= safeZonesX * safeZonesY) {
            return true;
        }

        int totalPixelsNeeded = 0;
        for (int shapeId : requiredShapes) {
            totalPixelsNeeded += variationsCache.get(shapeId).get(0).blocks().size();
        }
        if (totalPixelsNeeded > grid.getWidth() * grid.getHeight()) {
            return false;
        }

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

            if (validPlacements.size() < qty) return false;
            initialGroups.add(new ShapeGroup(qty, validPlacements));
        }

        return solveCSP(initialGroups);
    }

    private boolean solveCSP(List<ShapeGroup> groups) {
        int targetGroupIdx = -1;
        double minRatio = Double.MAX_VALUE;

        for (int i = 0; i < groups.size(); i++) {
            ShapeGroup sg = groups.get(i);
            if (sg.remainingQty() > 0) {
                double ratio = (double) sg.validPlacements().size() / sg.remainingQty();
                if (ratio < minRatio) {
                    minRatio = ratio;
                    targetGroupIdx = i;
                }
            }
        }

        if (targetGroupIdx == -1) return true;

        ShapeGroup targetGroup = groups.get(targetGroupIdx);

        for (int i = 0; i < targetGroup.validPlacements().size(); i++) {
            BitSet candidate = targetGroup.validPlacements().get(i);
            boolean possible = true;
            List<ShapeGroup> nextGroups = new ArrayList<>(groups.size());

            for (int g = 0; g < groups.size(); g++) {
                ShapeGroup sg = groups.get(g);
                if (sg.remainingQty() == 0) {
                    nextGroups.add(sg);
                    continue;
                }

                int newRemaining = (g == targetGroupIdx) ? sg.remainingQty() - 1 : sg.remainingQty();
                List<BitSet> nextPlacements = new ArrayList<>();
                int startIdx = (g == targetGroupIdx) ? (i + 1) : 0;

                for (int p = startIdx; p < sg.validPlacements().size(); p++) {
                    BitSet pSet = sg.validPlacements().get(p);
                    if (!pSet.intersects(candidate)) {
                        nextPlacements.add(pSet);
                    }
                }

                if (nextPlacements.size() < newRemaining) {
                    possible = false;
                    break;
                }
                nextGroups.add(new ShapeGroup(newRemaining, nextPlacements));
            }

            if (possible && solveCSP(nextGroups)) {
                return true;
            }
        }
        return false;
    }
}