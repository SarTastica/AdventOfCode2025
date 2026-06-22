package software.aoc.day12.a;

public class Grid {
    private final boolean[][] cells;
    private final int width;
    private final int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new boolean[height][width];
    }

    public boolean canPlace(Polyomino p, int startX, int startY) {
        if (startX + p.width() > width || startY + p.height() > height) return false;

        for (Point block : p.blocks()) {
            if (cells[startY + block.y()][startX + block.x()]) {
                return false;
            }
        }
        return true;
    }

    public void place(Polyomino p, int startX, int startY, boolean state) {
        for (Point block : p.blocks()) {
            cells[startY + block.y()][startX + block.x()] = state;
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}