package test;

import org.junit.jupiter.api.Test;
import software.aoc.day07.Manifold;
import software.aoc.day07.a.TachyonSimulator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day07ATest {

    @Test
    public void solveDay07PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day07-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        Manifold manifold = new Manifold(lines);

        TachyonSimulator simulator = new TachyonSimulator(manifold);

        long result = simulator.countSplits();

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 7 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(1553L, result, "El número de divisiones no es correcto.");
    }
}