package software.aoc.day02;

import java.util.stream.LongStream;

public record Range(long start, long end) {

    public static Range from(String rangeStr) {
        String[] bounds = rangeStr.trim().split("-");
        return new Range(Long.parseLong(bounds[0]), Long.parseLong(bounds[1]));
    }

    public LongStream stream() {
        return LongStream.rangeClosed(start, end);
    }
}