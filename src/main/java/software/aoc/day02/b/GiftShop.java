package software.aoc.day02.b;

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
        for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
            if (len % patternLen == 0) {
                String pattern = s.substring(0, patternLen);
                int repeats = len / patternLen;
                String expected = pattern.repeat(repeats);
                if (expected.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }
}
