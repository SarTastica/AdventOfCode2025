package software.aoc.day05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RangeMerger {

    public static List<Range> merge(List<Range> intervals) {
        if (intervals == null || intervals.isEmpty()) return List.of();

        List<Range> sorted = new ArrayList<>(intervals);
        Collections.sort(sorted);

        List<Range> merged = new ArrayList<>();
        Range current = sorted.get(0);

        for (int i = 1; i < sorted.size(); i++) {
            Range next = sorted.get(i);

            if (current.end() >= next.start()) {
                current = new Range(current.start(), Math.max(current.end(), next.end()));
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }
}