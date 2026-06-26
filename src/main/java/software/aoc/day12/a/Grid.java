package software.aoc.day12.a;

public record Grid(int width, int height) {
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}