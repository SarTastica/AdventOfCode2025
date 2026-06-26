package software.aoc.day05.a;

import software.aoc.day05.FreshnessRule;
import software.aoc.day05.Range;
import software.aoc.day05.RangeMerger;

import java.util.List;

public class MergedIntervalRule implements FreshnessRule {
    private final List<Range> mergedRanges;

    public MergedIntervalRule(List<Range> rawRanges) {
        this.mergedRanges = RangeMerger.merge(rawRanges);
    }

    @Override
    public boolean isFresh(long id) {
        for (Range r : mergedRanges) {
            if (id >= r.start() && id <= r.end()) return true;
            if (r.start() > id) break;
        }
        return false;
    }
}