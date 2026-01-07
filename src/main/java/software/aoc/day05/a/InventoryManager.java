package software.aoc.day05.a;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryManager {

    private static class Range {
        private final long start;
        private final long end;

        public Range(String rangeStr) {
            String[] parts = rangeStr.split("-");
            this.start = Long.parseLong(parts[0]);
            this.end = Long.parseLong(parts[1]);
        }

        public boolean contains(long id) {
            return id >= start && id <= end;
        }
    }

    public long countFreshIngredients(String fullInput) {
        if (fullInput == null || fullInput.isBlank()) {
            return 0;
        }

        String[] sections = fullInput.split("\\r?\\n\\r?\\n");

        if (sections.length < 2) return 0;

        List<Range> freshRanges = Arrays.stream(sections[0].split("\\n")) // Dividir por líneas
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Range::new)
                .collect(Collectors.toList());

        List<Long> availableIds = Arrays.stream(sections[1].split("\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return availableIds.stream()
                .filter(id -> isFresh(id, freshRanges))
                .count();
    }

    private boolean isFresh(long id, List<Range> ranges) {
        return ranges.stream().anyMatch(range -> range.contains(id));
    }
}
