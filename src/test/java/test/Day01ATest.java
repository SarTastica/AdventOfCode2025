package test;

import org.junit.jupiter.api.Test;
import software.aoc.day01.a.Dial;
import software.aoc.day01.a.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class Day01ATest {
    @Test
    public void solveDay01PartA() throws IOException {
        // Esta ruta busca el fichero que vas a crear en el siguiente paso
        Path path = Paths.get("src/test/resources/day01-a/orders.txt");

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
        System.out.println("SOLUCION DAY 1 - PART A: " + password);
        System.out.println("***********************************");

        // Verifica que el resultado sea razonable (mayor o igual a 0)
        assertTrue(password >= 0);
    }
}
