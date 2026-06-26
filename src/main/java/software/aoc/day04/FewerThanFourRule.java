package software.aoc.day04;

public class FewerThanFourRule implements AccessibilityRule {

    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    @Override
    public boolean isAccessible(Grid grid, Position pos) {
        int adjacentRolls = 0;

        for (int[] dir : DIRECTIONS) {
            Position neighbor = new Position(pos.row() + dir[0], pos.col() + dir[1]);
            if (grid.getCharAt(neighbor) == '@') {
                adjacentRolls++;
            }
        }

        return adjacentRolls < 4;
    }
}