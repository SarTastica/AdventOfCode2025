package test;

import org.junit.jupiter.api.Test;
import software.aoc.day10.FactoryManager;
import software.aoc.day10.FactoryParser;
import software.aoc.day10.Machine;
import software.aoc.day10.MachineSolver; // Importamos el nuevo contrato
import software.aoc.day10.a.BfsInitializationOptimizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10ATest {

    @Test
    public void solveDay10PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day10-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        FactoryParser parser = new FactoryParser();
        List<Machine> machines = parser.parse(lines);
        MachineSolver strategy = new BfsInitializationOptimizer();

        FactoryManager manager = new FactoryManager(strategy);

        long result = manager.processAll(machines);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 10 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(401L, result);
    }
}