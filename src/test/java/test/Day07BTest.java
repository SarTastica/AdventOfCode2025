package test;

import org.junit.jupiter.api.Test;
import software.aoc.day07.Manifold;
import software.aoc.day07.b.QuantumTachyonSimulator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day07BTest {

    @Test
    public void solveDay07PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day07-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        Manifold manifold = new Manifold(lines);

        QuantumTachyonSimulator simulator = new QuantumTachyonSimulator(manifold);

        long result = simulator.countTimelines();

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 7 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(15811946526915L, result, "El total de líneas temporales no coincide.");
    }
}