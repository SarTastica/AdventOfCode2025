package test;

import org.junit.jupiter.api.Test;
import software.aoc.day01.b.Dial;
import software.aoc.day01.b.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day01BTest {

    @Test
    public void solveDay01PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day01-b/orders.txt");

        List<String> lines = Files.readAllLines(path);

        Dial dial = new Dial();

        for (String line : lines) {
            if (!line.isBlank()) {
                Order order = new Order(line.trim());
                dial.apply(order);
            }
        }

        int password = dial.getZeroHits();
        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 1 - PART B: " + password);
        System.out.println("***********************************");

        assertTrue(password >= 0);
    }
}
