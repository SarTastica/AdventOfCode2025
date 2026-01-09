package software.aoc.day05.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryManager {
    private static class Range implements Comparable<Range> {
        long start;
        long end;

        public Range(String rangeStr) {
            String[] parts = rangeStr.split("-");
            this.start = Long.parseLong(parts[0]);
            this.end = Long.parseLong(parts[1]);
        }

        public long length() {
            return end - start + 1;
        }

        @Override
        public int compareTo(Range other) {
            return Long.compare(this.start, other.start);
        }
    }

    public long countTotalFreshIds(String fullInput) {
        if (fullInput == null || fullInput.isBlank()) return 0;

        String[] sections = fullInput.split("\\r?\\n\\r?\\n");
        if (sections.length == 0) return 0;

        List<Range> ranges = Arrays.stream(sections[0].split("\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Range::new)
                .collect(Collectors.toList());

        if (ranges.isEmpty()) return 0;

        Collections.sort(ranges);

        List<Range> mergedRanges = new ArrayList<>();
        Range current = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            Range next = ranges.get(i);

            if (next.start <= current.end) {
                current.end = Math.max(current.end, next.end);
            } else {
                mergedRanges.add(current);
                current = next;
            }
        }
        mergedRanges.add(current);

        return mergedRanges.stream()
                .mapToLong(Range::length)
                .sum();
    }
}
