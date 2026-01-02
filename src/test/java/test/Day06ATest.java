package test;

import org.junit.jupiter.api.Test;
import software.aoc.day06.a.CephalopodMath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day06ATest {

    @Test
    public void solveDay06PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day06-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        CephalopodMath solver = new CephalopodMath();
        long result = solver.calculateGrandTotal(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 6 - PART A: " + result);
        System.out.println("***********************************");

        assertTrue(result >= 0);
    }
}
