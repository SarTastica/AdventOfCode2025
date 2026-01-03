package test;

import org.junit.jupiter.api.Test;
import software.aoc.day08.b.CircuitManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day08BTest {

    @Test
    public void solveDay08PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day08-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        CircuitManager manager = new CircuitManager();
        long result = manager.findLastConnectionScore(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 8 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
