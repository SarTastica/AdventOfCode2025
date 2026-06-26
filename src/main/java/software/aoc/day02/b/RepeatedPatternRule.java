package software.aoc.day02.b;

import software.aoc.day02.ValidationRule;

public class RepeatedPatternRule implements ValidationRule {
    @Override
    public boolean isInvalid(long id) {
        return String.valueOf(id).matches("^([0-9]+)\\1+$");
    }
}