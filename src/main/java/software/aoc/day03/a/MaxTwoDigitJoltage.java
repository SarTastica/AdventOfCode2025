package software.aoc.day03.a;

import software.aoc.day03.BatteryBank;
import software.aoc.day03.JoltageCalculator;

public class MaxTwoDigitJoltage implements JoltageCalculator {

    @Override
    public long calculate(BatteryBank bank) {
        long maxJoltage = 0;
        String digits = bank.digits();
        int len = bank.length();

        for (int i = 0; i < len - 1; i++) {
            int tens = digits.charAt(i) - '0';

            for (int j = i + 1; j < len; j++) {
                int units = digits.charAt(j) - '0';
                long currentJoltage = (tens * 10L) + units;

                if (currentJoltage > maxJoltage) {
                    maxJoltage = currentJoltage;
                }
            }
        }
        return maxJoltage;
    }
}