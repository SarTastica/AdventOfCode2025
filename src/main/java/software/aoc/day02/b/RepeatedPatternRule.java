package software.aoc.day02.b;
import software.aoc.day02.ValidationRule;

public class RepeatedPatternRule implements ValidationRule {

    @Override
    public boolean isInvalid(long id) {
        String s = Long.toString(id);
        int len = s.length();

        for (int i = 1; i <= len / 2; i++) {

            if (len % i == 0) {
                String pattern = s.substring(0, i);
                int repetitions = len / i;

                if (pattern.repeat(repetitions).equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }
}