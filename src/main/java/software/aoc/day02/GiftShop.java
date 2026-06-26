package software.aoc.day02;

import java.util.Arrays;

public class GiftShop {
    private final ValidationRule rule;

    public GiftShop(ValidationRule rule) {
        this.rule = rule;
    }

    public long calculateInvalidIdSum(String input) {
        return Arrays.stream(input.split(","))
                .map(Range::from)
                .flatMapToLong(Range::stream)
                .filter(rule::isInvalid)
                .sum();
    }
}