package software.aoc.day05.a;

import software.aoc.day05.FreshnessRule;
import software.aoc.day05.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergedIntervalRule implements FreshnessRule {
    private final List<Range> mergedRanges;

    public MergedIntervalRule(List<Range> rawRanges) {
        this.mergedRanges = mergeIntervals(new ArrayList<>(rawRanges));
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

    @Override
    public boolean isFresh(long id) {
        for (Range r : mergedRanges) {
            if (id >= r.getStart() && id <= r.getEnd()) return true;
            if (r.getStart() > id) break;
        }
        return false;
    }
}