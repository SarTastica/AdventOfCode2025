package software.aoc.day07;

import java.util.List;

public class Manifold {
    private final char[][] grid;
    private final int rows;
    private final int cols;
    private int startRow = -1;
    private int startCol = -1;

    public Manifold(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.grid = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            this.grid[r] = line.toCharArray();
            int sIndex = line.indexOf('S');

            if (sIndex != -1) {
                this.startRow = r;
                this.startCol = sIndex;
            }
        }
    }

    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public int getRows() { return rows; }

    public char getCharAt(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return '.';
        }
        return grid[r][c];
    }
}