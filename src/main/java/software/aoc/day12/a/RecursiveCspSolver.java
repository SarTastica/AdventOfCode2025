package software.aoc.day12.a;

import java.util.*;

public class RecursiveCspSolver {

    public boolean solve(List<ShapeGroup> groups) {
        int targetGroupIdx = findMostConstrainedGroupIndex(groups);
        if (targetGroupIdx == -1) return true;

        ShapeGroup targetGroup = groups.get(targetGroupIdx);

        for (int i = 0; i < targetGroup.validPlacements().size(); i++) {
            BitSet candidate = targetGroup.validPlacements().get(i);
            List<ShapeGroup> nextGroups = generateNextState(groups, targetGroupIdx, i, candidate);

            if (nextGroups != null && solve(nextGroups)) {
                return true;
            }
        }
        return false;
    }

    private int findMostConstrainedGroupIndex(List<ShapeGroup> groups) {
        int targetGroupIdx = -1;
        double minRatio = Double.MAX_VALUE;

        for (int i = 0; i < groups.size(); i++) {
            ShapeGroup sg = groups.get(i);
            if (sg.remainingQty() > 0) {
                double ratio = (double) sg.validPlacements().size() / sg.remainingQty();
                if (ratio < minRatio) {
                    minRatio = ratio;
                    targetGroupIdx = i;
                }
            }
        }
        return targetGroupIdx;
    }

    private List<ShapeGroup> generateNextState(List<ShapeGroup> currentGroups, int targetGroupIdx, int candidateIdx, BitSet candidate) {
        List<ShapeGroup> nextGroups = new ArrayList<>(currentGroups.size());

        for (int g = 0; g < currentGroups.size(); g++) {
            ShapeGroup sg = currentGroups.get(g);
            if (sg.remainingQty() == 0) {
                nextGroups.add(sg);
                continue;
            }

            int newRemaining = (g == targetGroupIdx) ? sg.remainingQty() - 1 : sg.remainingQty();
            int startIdx = (g == targetGroupIdx) ? (candidateIdx + 1) : 0;

            List<BitSet> nextPlacements = filterCompatiblePlacements(sg.validPlacements(), candidate, startIdx);

            if (nextPlacements.size() < newRemaining) {
                return null;
            }
            nextGroups.add(new ShapeGroup(newRemaining, nextPlacements));
        }
        return nextGroups;
    }

    private List<BitSet> filterCompatiblePlacements(List<BitSet> placements, BitSet candidate, int startIdx) {
        List<BitSet> compatible = new ArrayList<>();
        for (int p = startIdx; p < placements.size(); p++) {
            BitSet pSet = placements.get(p);
            if (!pSet.intersects(candidate)) {
                compatible.add(pSet);
            }
        }
        return compatible;
    }
}