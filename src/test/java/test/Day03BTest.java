package test;

import org.junit.jupiter.api.Test;
import software.aoc.day03.b.PowerSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day03BTest {

    @Test
    public void solveDay03PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day03-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        PowerSystem powerSystem = new PowerSystem();
        long totalJoltage = powerSystem.calculateTotalPower(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 3 - PART B: " + totalJoltage);
        System.out.println("***********************************");

        assertTrue(totalJoltage > 0);
    }
}
