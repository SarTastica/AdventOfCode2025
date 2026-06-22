package test;

import org.junit.jupiter.api.Test;
import software.aoc.day12.a.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day12ATest {

    record Region(int w, int h, List<Integer> pieces) {}

    @Test
    public void solveDay12PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day12-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");
        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

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

        PackingStrategy strategy = new DfsPackingOptimizer();
        long successfulRegions = 0;

        for (Region r : regions) {
            Grid grid = new Grid(r.w, r.h);
            if (strategy.canPackAll(grid, r.pieces, shapesVariationsCache)) {
                successfulRegions++;
            }
        }

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 12 - PART A: " + successfulRegions);
        System.out.println("***********************************");

        assertEquals(485L, successfulRegions);
    }
}