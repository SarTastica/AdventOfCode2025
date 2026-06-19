package software.aoc.day04.b;

import software.aoc.day04.AccessibilityRule;
import software.aoc.day04.Grid;

import java.util.ArrayList;
import java.util.List;

public class IterativeForkliftOptimizer {
    private final AccessibilityRule rule;

    public IterativeForkliftOptimizer(AccessibilityRule rule) {
        this.rule = rule;
    }

    public long simulateRemovalProcess(Grid grid) {
        long totalRemoved = 0;

        while (true) {
            List<int[]> rollsToRemove = new ArrayList<>();

            for (int r = 0; r < grid.getRows(); r++) {
                for (int c = 0; c < grid.getCols(); c++) {
                    if (grid.getCharAt(r, c) == '@') {
                        if (rule.isAccessible(grid, r, c)) {
                            rollsToRemove.add(new int[]{r, c});
                        }
                    }
                }
            }

            if (rollsToRemove.isEmpty()) {
                break;
            }

            for (int[] coord : rollsToRemove) {
                grid.setCharAt(coord[0], coord[1], '.');
            }

            totalRemoved += rollsToRemove.size();
        }

        return totalRemoved;
    }
}