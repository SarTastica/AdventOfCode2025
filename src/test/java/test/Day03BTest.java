package test;

import org.junit.jupiter.api.Test;
import software.aoc.day03.EscalatorPowerSystem;
import software.aoc.day03.JoltageCalculator;
import software.aoc.day03.b.MaxTwelveDigitJoltage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day03BTest {

    @Test
    public void solveDay03PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day03-a/input.txt");
        if (resource == null) {
            throw new RuntimeException("No se encuentra el archivo input.txt");
        }

        JoltageCalculator nuevaEstrategia = new MaxTwelveDigitJoltage();
        EscalatorPowerSystem system = new EscalatorPowerSystem(nuevaEstrategia);

        long result = 0;
        try (Stream<String> lines = Files.lines(Path.of(resource.toURI()))) {
            result = system.calculateTotalJoltage(lines);
        }

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 3 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(172601598658203L, result, "El total no coincide con la Parte B.");
    }
}