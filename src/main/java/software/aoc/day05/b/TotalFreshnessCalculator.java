package software.aoc.day05.b;

import software.aoc.day05.Range;
import software.aoc.day05.RangeMerger;

import java.util.List;

public class TotalFreshnessCalculator {

    public long calculateTotalFresh(List<Range> rawRanges) {
        return RangeMerger.merge(rawRanges).stream()
                .mapToLong(Range::size)
                .sum();
    }
}