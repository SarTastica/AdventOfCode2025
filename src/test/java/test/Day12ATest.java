package test;

import org.junit.jupiter.api.Test;
import software.aoc.day12.a.ChristmasTreeFarm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day12ATest {

    @Test
    public void solveDay12PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day12-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        ChristmasTreeFarm farm = new ChristmasTreeFarm();
        long result = farm.solve(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 12 - PART A: " + result);
        System.out.println("***********************************");

        assertTrue(result >= 0);

    }
}

