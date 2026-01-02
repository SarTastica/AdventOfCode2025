package software.aoc.day03.b;

import java.util.List;

public class PowerSystem {

    public long calculateTotalPower(List<String> lines) {
        return lines.stream()
                .filter(line -> line != null && !line.isBlank())
                .map(BatteryBank::new)
                .mapToLong(BatteryBank::getMaxJoltage)
                .sum();
    }
}
