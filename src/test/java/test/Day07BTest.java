package test;

import org.junit.jupiter.api.Test;
import software.aoc.day07.b.TachyonLabs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day07BTest {

    @Test
    public void solveDay07PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day07-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        TachyonLabs labs = new TachyonLabs(lines);
        long result = labs.countTimelines();

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 7 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
