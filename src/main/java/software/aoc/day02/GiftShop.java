package software.aoc.day02;

import java.util.Arrays;

public class GiftShop {
    private final ValidationRule rule;

    public GiftShop(ValidationRule rule) {
        this.rule = rule;
    }

    public long calculateInvalidIdSum(String input) {
        String cleanInput = input.replaceAll("[^0-9,-]", "");
        String[] ranges = cleanInput.split(",");

        return Arrays.stream(ranges)
                .filter(r -> r.contains("-"))
                .mapToLong(range -> {
                    String[] bounds = range.split("-");
                    long start = Long.parseLong(bounds[0]);
                    long end = Long.parseLong(bounds[1]);
                    return sumInvalidInRange(start, end);
                })
                .sum();
    }

    private long sumInvalidInRange(long start, long end) {
        long sum = 0;
        for (long i = start; i <= end; i++) {
            if (this.rule.isInvalid(i)) {
                sum += i;
            }
        }
        return sum;
    }
}