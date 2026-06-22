package software.aoc.day12.a;

import java.util.List;
import java.util.Map;

public interface PackingStrategy {
    boolean canPackAll(Grid grid, List<Integer> requiredShapes, Map<Integer, List<Polyomino>> variationsCache);
}