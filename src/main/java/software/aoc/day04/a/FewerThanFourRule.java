package software.aoc.day04.a;

import software.aoc.day04.AccessibilityRule;
import software.aoc.day04.Grid;

public class FewerThanFourRule implements AccessibilityRule {

    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    @Override
    public boolean isAccessible(Grid grid, int row, int col) {
        int adjacentRolls = 0;

        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (grid.getCharAt(newRow, newCol) == '@') {
                adjacentRolls++;
            }
        }

        return adjacentRolls < 4;
    }
}