package software.aoc.day12.a;

import java.util.List;
import java.util.Map;

public class DfsPackingOptimizer implements PackingStrategy {

    private final PackingHeuristics heuristics;
    private final BitmaskPlacementGenerator generator;
    private final RecursiveCspSolver solver;

    public DfsPackingOptimizer(PackingHeuristics heuristics, BitmaskPlacementGenerator generator, RecursiveCspSolver solver) {
        this.heuristics = heuristics;
        this.generator = generator;
        this.solver = solver;
    }

    public DfsPackingOptimizer() {
        this(new PackingHeuristics(), new BitmaskPlacementGenerator(), new RecursiveCspSolver());
    }

    @Override
    public boolean canPackAll(Grid grid, List<Integer> requiredShapes, Map<Integer, List<Polyomino>> variationsCache) {

        if (!heuristics.isViable(grid, requiredShapes, variationsCache)) {
            return false;
        }

        List<ShapeGroup> initialGroups = generator.generate(grid, requiredShapes, variationsCache);
        if (initialGroups == null) {
            return false;
        }

        return solver.solve(initialGroups);
    }
}