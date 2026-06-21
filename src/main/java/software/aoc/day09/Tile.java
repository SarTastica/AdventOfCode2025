package software.aoc.day09;

public record Tile(long x, long y) {

    public long calculateBoundingArea(Tile other) {
        long width = Math.abs(this.x - other.x) + 1;
        long height = Math.abs(this.y - other.y) + 1;
        return width * height;
    }
}