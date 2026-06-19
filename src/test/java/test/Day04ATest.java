package test;

import org.junit.jupiter.api.Test;
import software.aoc.day04.ForkliftOptimizer;
import software.aoc.day04.Grid;
import software.aoc.day04.AccessibilityRule;
import software.aoc.day04.a.FewerThanFourRule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day04ATest {

    @Test
    public void solveDay04PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day04-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        Grid grid = new Grid(lines);

        AccessibilityRule regla = new FewerThanFourRule();
        ForkliftOptimizer optimizer = new ForkliftOptimizer(regla);

        long result = optimizer.countAccessibleRolls(grid);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 4 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(1457L, result);
    }
}