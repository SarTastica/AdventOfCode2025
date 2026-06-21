package test;

import org.junit.jupiter.api.Test;
import software.aoc.day09.AreaOptimizationStrategy;
import software.aoc.day09.MovieTheaterManager;
import software.aoc.day09.MovieTheaterParser;
import software.aoc.day09.Tile;
import software.aoc.day09.a.MaxAreaOptimizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day09ATest {

    @Test
    public void solveDay09PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day09-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        MovieTheaterParser parser = new MovieTheaterParser();
        List<Tile> tiles = parser.parse(lines);

        AreaOptimizationStrategy strategy = new MaxAreaOptimizer();
        MovieTheaterManager manager = new MovieTheaterManager(strategy);

        long result = manager.calculateOptimalArea(tiles);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 9 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(4743645488L, result);
    }
}