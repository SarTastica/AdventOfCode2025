package software.aoc.day02.a;

import software.aoc.day02.ValidationRule;

public class RepeatedHalfRule implements ValidationRule {
    @Override
    public boolean isInvalid(long id) {
        String s = Long.toString(id);
        int len = s.length();
        if (len % 2 != 0) return false;

        String firstHalf = s.substring(0, len / 2);
        String secondHalf = s.substring(len / 2);
        return firstHalf.equals(secondHalf);
    }
}