package software.aoc.day12.a;

import java.util.*;

public class ChristmasTreeFarm {

    private final Map<Integer, Shape> shapeRegistry = new HashMap<>();

    public long solve(List<String> lines) {
        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i);
            if (line.isBlank()) { i++; continue; }
            if (line.contains("x") && line.contains(":")) break;

            int id = Integer.parseInt(line.replace(":", "").trim());
            i++;

            Set<Point> points = new HashSet<>();
            int r = 0;
            while (i < lines.size()) {
                String shapeLine = lines.get(i);
                if (shapeLine.isBlank()) break;
                for (int c = 0; c < shapeLine.length(); c++) {
                    if (shapeLine.charAt(c) == '#') {
                        points.add(new Point(r, c));
                    }
                }
                r++;
                i++;
            }
            shapeRegistry.put(id, new Shape(id, points));
        }

        long validRegions = 0;

        while (i < lines.size()) {
            String line = lines.get(i);
            if (line.isBlank()) { i++; continue; }

            String[] parts = line.split(":");
            String[] dims = parts[0].split("x");
            int width = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);

            List<Integer> presentsToFit = new ArrayList<>();
            String[] counts = parts[1].trim().split("\\s+");
            for (int shapeIdx = 0; shapeIdx < counts.length; shapeIdx++) {
                int count = Integer.parseInt(counts[shapeIdx]);
                for (int k = 0; k < count; k++) {
                    presentsToFit.add(shapeIdx);
                }
            }

            presentsToFit.sort((id1, id2) ->
                    Integer.compare(shapeRegistry.get(id2).getArea(), shapeRegistry.get(id1).getArea())
            );

            int totalAreaNeeded = presentsToFit.stream().mapToInt(id -> shapeRegistry.get(id).getArea()).sum();
            if (totalAreaNeeded <= width * height) {
                if (canFit(new boolean[height][width], presentsToFit, 0)) {
                    validRegions++;
                }
            }
            i++;
        }

        return validRegions;
    }

    private boolean canFit(boolean[][] grid, List<Integer> presents, int idx) {
        if (idx == presents.size()) {
            return true;
        }

        int shapeId = presents.get(idx);
        Shape shape = shapeRegistry.get(shapeId);
        int rows = grid.length;
        int cols = grid[0].length;

        for (Set<Point> variant : shape.getVariations()) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidPlacement(grid, r, c, variant)) {
                        place(grid, r, c, variant, true);
                        if (canFit(grid, presents, idx + 1)) {
                            return true;
                        }
                        place(grid, r, c, variant, false);
                    }
                }
            }
        }

        return false;
    }

    private boolean isValidPlacement(boolean[][] grid, int rOffset, int cOffset, Set<Point> points) {
        for (Point p : points) {
            int r = rOffset + p.r;
            int c = cOffset + p.c;
            if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c]) {
                return false;
            }
        }
        return true;
    }

    private void place(boolean[][] grid, int rOffset, int cOffset, Set<Point> points, boolean value) {
        for (Point p : points) {
            grid[rOffset + p.r][cOffset + p.c] = value;
        }
    }
}
