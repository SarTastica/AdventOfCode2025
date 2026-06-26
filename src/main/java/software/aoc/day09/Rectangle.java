package software.aoc.day09;

public record Rectangle(long minX, long maxX, long minY, long maxY) {
    public long area() {
        return (maxX - minX + 1) * (maxY - minY + 1);
    }

    public double centerX() { return (minX + maxX) / 2.0; }
    public double centerY() { return (minY + maxY) / 2.0; }
}