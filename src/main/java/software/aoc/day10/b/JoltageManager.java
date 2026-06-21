package software.aoc.day10.b;

import software.aoc.day10.Machine;
import java.util.List;

public class JoltageManager {
    private final JoltageOptimizationStrategy strategy;

    public JoltageManager(JoltageOptimizationStrategy strategy) {
        this.strategy = strategy;
    }

    public long calculateTotalPresses(List<Machine> machines) {
        return machines.stream()
                .mapToLong(strategy::calculateMinimumPresses)
                .sum();
    }
}