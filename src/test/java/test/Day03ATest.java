package test;

import org.junit.jupiter.api.Test;
import software.aoc.day03.EscalatorPowerSystem;
import software.aoc.day03.JoltageCalculator;
import software.aoc.day03.a.MaxTwoDigitJoltage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day03ATest {

    @Test
    public void solveDay03PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day03-a/input.txt");
        if (resource == null) {
            throw new RuntimeException("No se encuentra el archivo input.txt en day03-a");
        }

        JoltageCalculator strategy = new MaxTwoDigitJoltage();
        EscalatorPowerSystem system = new EscalatorPowerSystem(strategy);

        long result = 0;

        try (Stream<String> lines = Files.lines(Path.of(resource.toURI()))) {
            result = system.calculateTotalJoltage(lines);
        }

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 3 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(17383L, result, "El resultado debe ser el esperado para el input.");
    }
}