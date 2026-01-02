package software.aoc.day02.a;

import java.util.Arrays;
import java.util.stream.LongStream;

public class GiftShop {
    public long calculateInvalidIdSum(String input) {
        if (input == null || input.isBlank()) {
            return 0;
        }

        return Arrays.stream(input.trim().split(","))
                .map(String::trim)
                .flatMapToLong(this::rangeToStream)
                .filter(this::isRepeatedSequence)
                .sum();
    }

    private LongStream rangeToStream(String range) {
        String[] parts = range.split("-");
        long start = Long.parseLong(parts[0]);
        long end = Long.parseLong(parts[1]);
        return LongStream.rangeClosed(start, end);
    }
    private boolean isRepeatedSequence(long id) {
        String s = String.valueOf(id);
        int len = s.length();

        if (len % 2 != 0) {
            return false;
        }

        int mid = len / 2;
        String firstHalf = s.substring(0, mid);
        String secondHalf = s.substring(mid);

        return firstHalf.equals(secondHalf);
    }
}
