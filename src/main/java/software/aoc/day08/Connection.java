package software.aoc.day08;

public record Connection(int id1, int id2, long distanceSq) implements Comparable<Connection> {
    @Override
    public int compareTo(Connection other) {
        return Long.compare(this.distanceSq, other.distanceSq);
    }
}