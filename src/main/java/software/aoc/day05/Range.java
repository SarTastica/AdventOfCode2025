package software.aoc.day05;

public class Range implements Comparable<Range> {
    private final long start;
    private final long end;

    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() { return start; }
    public long getEnd() { return end; }

    @Override
    public int compareTo(Range other) {
        return Long.compare(this.start, other.start);
    }
}