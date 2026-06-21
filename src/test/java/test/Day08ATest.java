package test;

import org.junit.jupiter.api.Test;
import software.aoc.day08.JunctionParser;
import software.aoc.day08.Point3D;
import software.aoc.day08.a.PlaygroundOptimizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day08ATest {

    @Test
    public void solveDay08PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day08-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        JunctionParser parser = new JunctionParser();
        List<Point3D> points = parser.parse(lines);

        PlaygroundOptimizer optimizer = new PlaygroundOptimizer();
        long result = optimizer.calculateLargestCircuitsMetric(points, 1000);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 8 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(122430L, result);
    }
}