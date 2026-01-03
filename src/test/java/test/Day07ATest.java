package test;

import org.junit.jupiter.api.Test;
import software.aoc.day07.a.TachyonLabs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day07ATest {

    @Test
    public void solveDay07PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day07-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        TachyonLabs labs = new TachyonLabs(lines);
        long result = labs.countSplits();

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 7 - PART A: " + result);
        System.out.println("***********************************");

        assertTrue(result >= 0);
    }
}
