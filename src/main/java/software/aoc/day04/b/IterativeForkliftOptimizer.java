package software.aoc.day04.b;

import software.aoc.day04.AccessibilityRule;
import software.aoc.day04.Grid;
import software.aoc.day04.Position;

import java.util.List;

public class IterativeForkliftOptimizer {
    private final AccessibilityRule rule;

    public IterativeForkliftOptimizer(AccessibilityRule rule) {
        this.rule = rule;
    }

    public long simulateRemovalProcess(Grid initialGrid) {
        long totalRemoved = 0;
        Grid currentGrid = initialGrid;

        while (true) {
            final Grid snapshotGrid = currentGrid;

            List<Position> rollsToRemove = snapshotGrid.getRollPositions()
                    .filter(p -> rule.isAccessible(snapshotGrid, p))
                    .toList();

            if (rollsToRemove.isEmpty()) {
                break;
            }

            currentGrid = snapshotGrid.removeRolls(rollsToRemove);
            totalRemoved += rollsToRemove.size();
        }

        return totalRemoved;
    }
}