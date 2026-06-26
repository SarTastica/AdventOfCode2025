package software.aoc.day04.a;

import software.aoc.day04.AccessibilityRule;
import software.aoc.day04.Grid;

public class ForkliftOptimizer {
    private final AccessibilityRule rule;

    public ForkliftOptimizer(AccessibilityRule rule) {
        this.rule = rule;
    }

    public long countAccessibleRolls(Grid grid) {
        return grid.getRollPositions()
                .filter(p -> rule.isAccessible(grid, p))
                .count();
    }
}