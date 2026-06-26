package software.aoc.day05;

import java.util.ArrayList;
import java.util.List;

public class InventoryParser {

    public record ParsedData(List<Range> ranges, List<Long> availableIds) {}

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