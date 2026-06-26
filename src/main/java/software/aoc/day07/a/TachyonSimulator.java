package software.aoc.day07.a;

import software.aoc.day07.Manifold;
import software.aoc.day07.TachyonCell;

import java.util.HashSet;
import java.util.Set;

public class TachyonSimulator {
    private final Manifold manifold;

    public TachyonSimulator(Manifold manifold) {
        this.manifold = manifold;
    }

    public long countSplits() {
        long splitCount = 0;

        Set<Integer> activeBeams = new HashSet<>();
        activeBeams.add(manifold.getStartCol());

        for (int r = manifold.getStartRow() + 1; r < manifold.getRows(); r++) {
            Set<Integer> nextBeams = new HashSet<>();

            for (int col : activeBeams) {
                TachyonCell cell = manifold.getCellAt(r, col);

                if (cell == TachyonCell.SPLITTER) {
                    splitCount++;
                    nextBeams.add(col - 1);
                    nextBeams.add(col + 1);
                } else {
                    nextBeams.add(col);
                }
            }
            activeBeams = nextBeams;
        }

        return splitCount;
    }
}