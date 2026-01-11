package software.aoc.day12.a;

import java.util.*;

public class ChristmasTreeFarm {

    private final Map<Integer, Shape> shapeRegistry = new HashMap<>();

    public long solve(List<String> lines) {
        Iterator<String> it = lines.iterator();

        while (it.hasNext()) {
            String line = it.next();
            if (line.isBlank()) continue;

            if (line.contains("x") && line.contains(":")) {
                return solvePuzzles(line, it);
            }
            parseShape(line, it);
        }
        return 0;
    }

    private void parseShape(String header, Iterator<String> it) {
        int id = Integer.parseInt(header.replace(":", "").trim());
        Set<Point> points = new HashSet<>();
        int r = 0;

        while (it.hasNext()) {
            String line = it.next();
            if (line.isBlank()) break;
            for (int c = 0; c < line.length(); c++) {
                if (line.charAt(c) == '#') points.add(new Point(r, c));
            }
            r++;
        }
        shapeRegistry.put(id, new Shape(id, points));
    }

    private long solvePuzzles(String firstLine, Iterator<String> it) {
        long validCount = 0;
        if (solveSinglePuzzle(firstLine)) validCount++;

        while (it.hasNext()) {
            String line = it.next();
            if (!line.isBlank() && solveSinglePuzzle(line)) {
                validCount++;
            }
        }
        return validCount;
    }

    private boolean solveSinglePuzzle(String line) {
        String[] parts = line.split(":");
        String[] dims = parts[0].split("x");
        int W = Integer.parseInt(dims[0]);
        int H = Integer.parseInt(dims[1]);

        List<Integer> pieces = new ArrayList<>();
        String[] counts = parts[1].trim().split("\\s+");
        for (int id = 0; id < counts.length; id++) {
            int count = Integer.parseInt(counts[id]);
            for (int k = 0; k < count; k++) pieces.add(id);
        }

        pieces.sort((a, b) -> Integer.compare(shapeRegistry.get(b).getArea(), shapeRegistry.get(a).getArea()));

        int totalArea = pieces.stream().mapToInt(id -> shapeRegistry.get(id).getArea()).sum();

        return totalArea <= W * H && canFit(new boolean[H][W], pieces, 0);
    }

    private boolean canFit(boolean[][] grid, List<Integer> pieces, int idx) {
        if (idx == pieces.size()) return true; // Todas las piezas colocadas

        Shape shape = shapeRegistry.get(pieces.get(idx));

        for (Set<Point> variant : shape.getVariations()) {
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (tryToggle(grid, r, c, variant, true)) {
                        if (canFit(grid, pieces, idx + 1)) return true;
                        tryToggle(grid, r, c, variant, false);
                    }
                }
            }
        }
        return false;
    }

    private boolean tryToggle(boolean[][] grid, int rOff, int cOff, Set<Point> points, boolean placing) {
        if (placing) {
            for (Point p : points) {
                int nr = rOff + p.r;
                int nc = cOff + p.c;
                if (nr < 0 || nr >= grid.length || nc < 0 || nc >= grid[0].length || grid[nr][nc]) {
                    return false;
                }
            }
        }
        for (Point p : points) {
            grid[rOff + p.r][cOff + p.c] = placing;
        }
        return true;
    }
}
