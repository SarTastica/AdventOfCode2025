package software.aoc.day04.a;

import java.util.List;

public class Warehouse {
    private final char[][] grid;
    private final int rows;
    private final int cols;

    private static final int[] DX = {-1, -1, -1,  0, 0,  1, 1, 1};
    private static final int[] DY = {-1,  0,  1, -1, 1, -1, 0, 1};

    public Warehouse(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            this.grid[i] = lines.get(i).toCharArray();
        }
    }

    public long countAccessibleRolls() {
        long accessibleCount = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '@') {
                    if (isAccessible(r, c)) {
                        accessibleCount++;
                    }
                }
            }
        }
        return accessibleCount;
    }

    private boolean isAccessible(int r, int c) {
        int neighbors = 0;

        for (int i = 0; i < 8; i++) {
            int newRow = r + DX[i];
            int newCol = c + DY[i];

            if (isValidPosition(newRow, newCol) && grid[newRow][newCol] == '@') {
                neighbors++;
            }
        }
        return neighbors < 4;
    }
    private boolean isValidPosition(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
}
