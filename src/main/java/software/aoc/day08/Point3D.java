package software.aoc.day08;

public record Point3D(int id, long x, long y, long z) {
    public long distanceSquaredTo(Point3D other) {
        long dx = this.x - other.x;
        long dy = this.y - other.y;
        long dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }
}