package software.aoc.day03.a;

import software.aoc.day03.JoltageCalculator;

public class MaxTwoDigitJoltage implements JoltageCalculator {

    @Override
    public long calculate(String batteryBank) {
        long maxJoltage = 0;
        int len = batteryBank.length();

        for (int i = 0; i < len - 1; i++) {
            int tens = Character.getNumericValue(batteryBank.charAt(i));

            for (int j = i + 1; j < len; j++) {
                int units = Character.getNumericValue(batteryBank.charAt(j));
                long currentJoltage = (tens * 10L) + units;

                if (currentJoltage > maxJoltage) {
                    maxJoltage = currentJoltage;
                }
            }
        }
        return maxJoltage;
    }
}