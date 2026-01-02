package test;

import org.junit.jupiter.api.Test;
import software.aoc.day03.a.PowerSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day03ATest {

    @Test
    public void solveDay03PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day03-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        PowerSystem powerSystem = new PowerSystem();
        int totalJoltage = powerSystem.calculateTotalPower(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 3 - PART A: " + totalJoltage);
        System.out.println("***********************************");

        assertTrue(totalJoltage > 0);
    }
}
