package test;

import org.junit.jupiter.api.Test;
import software.aoc.day10.b.Factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day10BTest {

    @Test
    public void solveDay10PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day10-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        Factory factory = new Factory();
        long result = factory.calculateTotalJoltagePresses(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 10 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
