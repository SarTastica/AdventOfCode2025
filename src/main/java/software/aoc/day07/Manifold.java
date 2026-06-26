package software.aoc.day07;

import java.util.List;

public class Manifold {
    private final TachyonCell[][] grid;
    private final int rows;
    private final int cols;
    private int startRow = -1;
    private int startCol = -1;

    public Manifold(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.grid = new TachyonCell[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                this.grid[r][c] = TachyonCell.from(ch);

                if (ch == 'S') {
                    this.startRow = r;
                    this.startCol = c;
                }
            }
        }
    }

    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public int getRows() { return rows; }

    public TachyonCell getCellAt(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return TachyonCell.OUT_OF_BOUNDS;
        }
        return grid[r][c];
    }
}