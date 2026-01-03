package software.aoc.day10.b;

import java.util.List;

public class Factory {

    public long calculateTotalJoltagePresses(List<String> lines) {
        return lines.stream()
                .filter(line -> !line.isBlank())
                .map(Machine::new)
                .mapToLong(Machine::solveMinJoltagePresses)
                .sum();
    }
}
