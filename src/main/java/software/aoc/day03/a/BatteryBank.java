package software.aoc.day03.a;

public class BatteryBank {
    private final String sequence;

    public BatteryBank(String sequence) {
        this.sequence = sequence;
    }

    public int getMaxJoltage() {
        for (int tens = 9; tens >= 1; tens--) {
            char tensChar = Character.forDigit(tens, 10);
            int firstIndex = sequence.indexOf(tensChar);

            if (firstIndex != -1) {
                for (int units = 9; units >= 1; units--) {
                    char unitsChar = Character.forDigit(units, 10);
                    int secondIndex = sequence.indexOf(unitsChar, firstIndex + 1);

                    if (secondIndex != -1) {
                        return tens * 10 + units;
                    }
                }
            }
        }
        return 0;
    }
}
