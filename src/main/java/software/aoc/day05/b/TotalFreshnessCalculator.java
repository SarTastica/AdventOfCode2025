package software.aoc.day05.b;

import software.aoc.day05.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TotalFreshnessCalculator {

    public long calculateTotalFresh(List<Range> rawRanges) {
        List<Range> mergedRanges = mergeIntervals(new ArrayList<>(rawRanges));

        long totalCapacity = 0;
        for (Range r : mergedRanges) {
            totalCapacity += (r.getEnd() - r.getStart() + 1);
        }

        return totalCapacity;
    }

    private List<Range> mergeIntervals(List<Range> intervals) {
        if (intervals.isEmpty()) return new ArrayList<>();

        Collections.sort(intervals);
        List<Range> merged = new ArrayList<>();
        Range current = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            Range next = intervals.get(i);

            if (current.getEnd() >= next.getStart()) {
                current = new Range(current.getStart(), Math.max(current.getEnd(), next.getEnd()));
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }
}