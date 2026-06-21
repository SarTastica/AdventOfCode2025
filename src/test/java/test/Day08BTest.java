package test;

import org.junit.jupiter.api.Test;
import software.aoc.day08.JunctionParser;
import software.aoc.day08.Point3D;
import software.aoc.day08.b.PlaygroundCompleter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day08BTest {

    @Test
    public void solveDay08PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day08-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        JunctionParser parser = new JunctionParser();
        List<Point3D> points = parser.parse(lines);

        PlaygroundCompleter completer = new PlaygroundCompleter();
        long result = completer.calculateLastConnectionXProduct(points);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 8 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(8135565324L, result, "El producto de las coordenadas X no es correcto.");
    }
}