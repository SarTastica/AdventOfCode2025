package software.aoc.day03.b;

import software.aoc.day03.BatteryBank;
import software.aoc.day03.JoltageCalculator;

public class MaxTwelveDigitJoltage implements JoltageCalculator {
    private static final int TARGET_LENGTH = 12;

    @Override
    public long calculate(BatteryBank bank) {
        String digits = bank.digits();
        int length = bank.length();

        if (length <= TARGET_LENGTH) {
            return Long.parseLong(digits);
        }

        int elementsToRemove = length - TARGET_LENGTH;
        StringBuilder stack = new StringBuilder();

        for (char currentDigit : digits.toCharArray()) {
            while (elementsToRemove > 0 && !stack.isEmpty()
                    && stack.charAt(stack.length() - 1) < currentDigit) {

                stack.deleteCharAt(stack.length() - 1);
                elementsToRemove--;
            }
            stack.append(currentDigit);
        }

        stack.setLength(TARGET_LENGTH);

        return Long.parseLong(stack.toString());
    }
}