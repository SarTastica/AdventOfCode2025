package software.aoc.day04;

public class ForkliftOptimizer {
    private final AccessibilityRule rule;

    public ForkliftOptimizer(AccessibilityRule rule) {
        this.rule = rule;
    }

    public long countAccessibleRolls(Grid grid) {
        long count = 0;

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (grid.getCharAt(r, c) == '@') {
                    if (rule.isAccessible(grid, r, c)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}