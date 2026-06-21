package test;

import org.junit.jupiter.api.Test;
import software.aoc.day10.FactoryParser;
import software.aoc.day10.Machine;
import software.aoc.day10.b.JoltageManager;
import software.aoc.day10.b.JoltageOptimizationStrategy;
import software.aoc.day10.b.MemoizedBfsJoltageOptimizer; // NUEVO IMPORT

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10BTest {

    @Test
    public void solveDay10PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day10-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        FactoryParser parser = new FactoryParser();
        List<Machine> machines = parser.parse(lines);

        JoltageOptimizationStrategy strategy = new MemoizedBfsJoltageOptimizer();
        JoltageManager manager = new JoltageManager(strategy);

        long result = manager.calculateTotalPresses(machines);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 10 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(15017L, result);
    }
}