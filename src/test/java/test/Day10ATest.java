package test;

import org.junit.jupiter.api.Test;
import software.aoc.day10.a.Factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day10ATest {

    @Test
    public void solveDay10PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day10-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        Factory factory = new Factory();
        int totalPresses = factory.calculateTotalPresses(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 10 - PART A: " + totalPresses);
        System.out.println("***********************************");

        assertTrue(totalPresses >= 0);
    }
}
