package test;

import org.junit.jupiter.api.Test;
import software.aoc.day06.b.CephalopodMath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day06BTest {

    @Test
    public void solveDay06PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day06-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        CephalopodMath solver = new CephalopodMath();
        long result = solver.calculateGrandTotal(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 6 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
