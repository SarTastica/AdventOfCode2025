package software.aoc.day05;

public record Range(long start, long end) implements Comparable<Range> {

    public long size() {
        return end - start + 1;
    }

    @Override
    public int compareTo(Range other) {
        return Long.compare(this.start, other.start);
    }
}