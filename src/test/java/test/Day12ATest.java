package test;

import org.junit.jupiter.api.Test;
import software.aoc.day12.a.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day12ATest {

    @Test
    public void solveDay12PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day12-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");
        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        PackingParser parser = new PackingParser();
        PackingParser.ParsedData data = parser.parse(lines);

        PackingStrategy strategy = new DfsPackingOptimizer();
        long successfulRegions = 0;

        for (PackingParser.Region r : data.regions()) {
            Grid grid = new Grid(r.w(), r.h());
            if (strategy.canPackAll(grid, r.pieces(), data.shapesVariationsCache())) {
                successfulRegions++;
            }
        }

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 12 - PART A: " + successfulRegions);
        System.out.println("***********************************");

        assertEquals(485L, successfulRegions);
    }
}