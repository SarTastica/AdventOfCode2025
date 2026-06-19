package software.aoc.day04;

import java.util.List;

public class Grid {
    private final char[][] map;
    private final int rows;
    private final int cols;

    public Grid(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.map = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            this.map[r] = lines.get(r).toCharArray();
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public char getCharAt(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return '.';
        }
        return map[row][col];
    }

    //para la parte b
    public void setCharAt(int row, int col, char c) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            map[row][col] = c;
        }
    }
}