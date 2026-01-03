package test;

import org.junit.jupiter.api.Test;
import software.aoc.day11.b.ReactorManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day11BTest {

    @Test
    public void solveDay11PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day11-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        ReactorManager manager = new ReactorManager();
        long result = manager.countPathsViaComponents(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 11 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result >= 0);
    }
}
