package software.aoc.day10.a;

import java.util.List;

public class FactoryManager {
    private final InitializationStrategy strategy;

    public FactoryManager(InitializationStrategy strategy) {
        this.strategy = strategy;
    }

    public long calculateTotalPresses(List<Machine> machines) {
        long total = 0;
        for (Machine machine : machines) {
            total += strategy.calculateMinimumPresses(machine);
        }
        return total;
    }
}