package test;

import org.junit.jupiter.api.Test;
import software.aoc.day04.AccessibilityRule;
import software.aoc.day04.Grid;
import software.aoc.day04.FewerThanFourRule;
import software.aoc.day04.b.IterativeForkliftOptimizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day04BTest {

    @Test
    public void solveDay04PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day04-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        Grid grid = new Grid(lines);

        AccessibilityRule regla = new FewerThanFourRule();

        IterativeForkliftOptimizer optimizer = new IterativeForkliftOptimizer(regla);

        long result = optimizer.simulateRemovalProcess(grid);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 4 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(8310L, result, "El total de rollos eliminados debe ser 8310");
    }
}