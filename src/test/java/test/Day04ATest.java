package test;

import org.junit.jupiter.api.Test;
import software.aoc.day04.a.Warehouse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day04ATest {

    @Test
    public void solveDay04PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day04-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        Warehouse warehouse = new Warehouse(lines);
        long result = warehouse.countAccessibleRolls();

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 4 - PART A: " + result);
        System.out.println("***********************************");

        assertTrue(result >= 0);
    }
}
