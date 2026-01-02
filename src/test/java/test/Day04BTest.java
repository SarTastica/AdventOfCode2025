package test;

import org.junit.jupiter.api.Test;
import software.aoc.day04.b.Warehouse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day04BTest {

    @Test
    public void solveDay04PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day04-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        Warehouse warehouse = new Warehouse(lines);
        long result = warehouse.removeAllAccessibleRolls();

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 4 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
