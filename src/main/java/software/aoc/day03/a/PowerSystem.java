package software.aoc.day03.a;

import java.util.List;

public class PowerSystem {
    public int calculateTotalPower(List<String> lines) {
        return lines.stream()
                .filter(line -> !line.isBlank())
                .map(BatteryBank::new)
                .mapToInt(BatteryBank::getMaxJoltage)
                .sum();
    }
}
