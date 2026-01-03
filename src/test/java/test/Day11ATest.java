package test;

import org.junit.jupiter.api.Test;
import software.aoc.day11.a.ReactorManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day11ATest {

    @Test
    public void solveDay11PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day11-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        ReactorManager manager = new ReactorManager();
        long result = manager.countPaths(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 11 - PART A: " + result);
        System.out.println("***********************************");

        assertTrue(result >= 0);
    }
}
