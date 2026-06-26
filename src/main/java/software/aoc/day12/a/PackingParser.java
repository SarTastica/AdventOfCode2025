package software.aoc.day12.a;

import java.util.*;

public class PackingParser {

    public record ParsedData(Map<Integer, List<Polyomino>> shapesVariationsCache, List<Region> regions) {}
    public record Region(int w, int h, List<Integer> pieces) {}

    public ParsedData parse(List<String> lines) {
        Map<Integer, List<Polyomino>> shapesVariationsCache = new HashMap<>();
        List<Region> regions = new ArrayList<>();

        int idx = 0;
        while (idx < lines.size()) {
            String line = lines.get(idx).trim();
            if (line.isEmpty()) {
                idx++; continue;
            }
            if (line.matches("\\d+:")) {
                int id = Integer.parseInt(line.replace(":", ""));
                List<Point> blocks = new ArrayList<>();
                int y = 0;
                while (++idx < lines.size() && !lines.get(idx).trim().isEmpty() && !lines.get(idx).contains("x")) {
                    String row = lines.get(idx);
                    for (int x = 0; x < row.length(); x++) {
                        if (row.charAt(x) == '#') blocks.add(new Point(x, y));
                    }
                    y++;
                }
                Polyomino base = new Polyomino(new HashSet<>(blocks), 0, 0);
                shapesVariationsCache.put(id, base.generateUniqueVariations());
            } else if (line.contains("x")) {
                String[] parts = line.split(": ");
                String[] dims = parts[0].split("x");
                String[] counts = parts[1].split(" ");

                List<Integer> piecesToPack = new ArrayList<>();
                for (int shapeId = 0; shapeId < counts.length; shapeId++) {
                    int qty = Integer.parseInt(counts[shapeId]);
                    for (int i = 0; i < qty; i++) piecesToPack.add(shapeId);
                }
                Collections.sort(piecesToPack);
                regions.add(new Region(Integer.parseInt(dims[0]), Integer.parseInt(dims[1]), piecesToPack));
                idx++;
            }
        }
        return new ParsedData(shapesVariationsCache, regions);
    }
}