package software.aoc.day07.a;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TachyonLabs {

    private final char[][] grid;
    private final int rows;
    private final int cols;

    public TachyonLabs(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            this.grid[i] = lines.get(i).toCharArray();
        }
    }

    public long countSplits() {
        long splitCount = 0;

        Set<Integer> activeColumns = new HashSet<>();

        for (int c = 0; c < cols; c++) {
            if (grid[0][c] == 'S') {
                activeColumns.add(c);
                break;
            }
        }

        for (int r = 0; r < rows; r++) {
            Set<Integer> nextColumns = new HashSet<>();

            for (int col : activeColumns) {
                char cell = grid[r][col];

                if (cell == '^') {
                    splitCount++;
                    if (isValid(col - 1)) nextColumns.add(col - 1);
                    if (isValid(col + 1)) nextColumns.add(col + 1);

                } else {
                    nextColumns.add(col);
                }
            }

            activeColumns = nextColumns;

            if (activeColumns.isEmpty()) {
                break;
            }
        }

        return splitCount;
    }

    private boolean isValid(int col) {
        return col >= 0 && col < cols;
    }
}
