package software.aoc.day10;

import java.util.List;

public class FactoryManager {
    private final MachineSolver solver;

    public FactoryManager(MachineSolver solver) {
        this.solver = solver;
    }

    public long processAll(List<Machine> machines) {
        return machines.stream()
                .mapToLong(solver::solve)
                .sum();
    }
}