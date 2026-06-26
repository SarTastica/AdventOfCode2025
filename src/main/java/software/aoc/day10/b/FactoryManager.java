package software.aoc.day10.b;

import java.util.List;

public class FactoryManager {
    private final MachineSolver solver;

    public FactoryManager(MachineSolver solver) {
        this.solver = solver;
    }

    public int configureAll(List<Machine> machines) {
        return machines.stream()
                .mapToInt(solver::solve)
                .sum();
    }
}