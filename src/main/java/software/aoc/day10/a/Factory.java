package software.aoc.day10.a;

import java.util.List;

public class Factory {

    public int calculateTotalPresses(List<String> lines) {
        return lines.stream()
                .filter(line -> !line.isBlank())
                .map(Machine::new)
                .mapToInt(Machine::solveMinPresses)
                .sum();
    }
}
