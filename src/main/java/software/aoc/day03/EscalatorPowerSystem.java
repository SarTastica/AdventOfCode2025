package software.aoc.day03;

import java.util.stream.Stream;

public class EscalatorPowerSystem {
    private final JoltageCalculator calculator;

    public EscalatorPowerSystem(JoltageCalculator calculator) {
        this.calculator = calculator;
    }

    public long calculateTotalJoltage(Stream<String> batteryBanks) {
        return batteryBanks
                .filter(bank -> !bank.isBlank())
                .map(BatteryBank::new)
                .mapToLong(calculator::calculate)
                .sum();
    }
}