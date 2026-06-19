package software.aoc.day03.b;

import software.aoc.day03.JoltageCalculator;

public class MaxTwelveDigitJoltage implements JoltageCalculator {
    private static final int TARGET_LENGTH = 12;

    @Override
    public long calculate(String batteryBank) {
        int length = batteryBank.length();

        if (length <= TARGET_LENGTH) {
            return Long.parseLong(batteryBank);
        }

        int elementsToRemove = length - TARGET_LENGTH;
        StringBuilder stack = new StringBuilder();

        for (char currentDigit : batteryBank.toCharArray()) {
            while (elementsToRemove > 0 && stack.length() > 0
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