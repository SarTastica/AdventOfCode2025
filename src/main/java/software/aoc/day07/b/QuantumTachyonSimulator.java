package software.aoc.day07.b;

import software.aoc.day07.Manifold;
import software.aoc.day07.TachyonCell;

import java.util.HashMap;
import java.util.Map;

public class QuantumTachyonSimulator {
    private final Manifold manifold;

    public QuantumTachyonSimulator(Manifold manifold) {
        this.manifold = manifold;
    }

    public long countTimelines() {
        Map<Integer, Long> activeTimelines = new HashMap<>();

        activeTimelines.put(manifold.getStartCol(), 1L);

        for (int r = manifold.getStartRow() + 1; r < manifold.getRows(); r++) {
            Map<Integer, Long> nextTimelines = new HashMap<>();

            for (Map.Entry<Integer, Long> entry : activeTimelines.entrySet()) {
                int col = entry.getKey();
                long pathCount = entry.getValue();

                TachyonCell cell = manifold.getCellAt(r, col);

                if (cell == TachyonCell.SPLITTER) {
                    nextTimelines.merge(col - 1, pathCount, Long::sum);
                    nextTimelines.merge(col + 1, pathCount, Long::sum);
                } else {
                    nextTimelines.merge(col, pathCount, Long::sum);
                }
            }
            activeTimelines = nextTimelines;
        }

        return activeTimelines.values().stream().mapToLong(Long::longValue).sum();
    }
}