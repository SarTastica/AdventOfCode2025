package software.aoc.day03.b;

public class BatteryBank {
    private final String sequence;
    private static final int TARGET_LENGTH = 12;

    public BatteryBank(String sequence) {
        this.sequence = sequence;
    }

    public long getMaxJoltage() {
        if (sequence.length() < TARGET_LENGTH) {
            return 0;
        }

        StringBuilder resultBuilder = new StringBuilder();
        int currentSearchIndex = 0;
        int charsNeeded = TARGET_LENGTH;

        while (charsNeeded > 0) {
            int searchLimit = sequence.length() - charsNeeded;

            char maxDigitFound = '0' - 1;
            int indexOfMax = -1;

            for (int i = currentSearchIndex; i <= searchLimit; i++) {
                char currentDigit = sequence.charAt(i);
                if (currentDigit > maxDigitFound) {
                    maxDigitFound = currentDigit;
                    indexOfMax = i;
                    if (maxDigitFound == '9') {
                        break;
                    }
                }
            }
            resultBuilder.append(maxDigitFound);
            currentSearchIndex = indexOfMax + 1;
            charsNeeded--;
        }

        return Long.parseLong(resultBuilder.toString());
    }
}