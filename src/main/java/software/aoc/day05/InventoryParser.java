package software.aoc.day05;

import java.util.ArrayList;
import java.util.List;

public class InventoryParser {
    public static class ParsedData {
        public final List<Range> ranges;
        public final List<Long> availableIds;
        public ParsedData(List<Range> r, List<Long> i) { this.ranges = r; this.availableIds = i; }
    }

    public static ParsedData parse(List<String> lines) {
        List<Range> ranges = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        boolean isParsingRanges = true;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                isParsingRanges = false;
                continue;
            }
            if (isParsingRanges) {
                String[] parts = line.split("-");
                ranges.add(new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
            } else {
                ids.add(Long.parseLong(line.trim()));
            }
        }
        return new ParsedData(ranges, ids);
    }
}