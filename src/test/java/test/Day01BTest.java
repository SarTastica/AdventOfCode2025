package test;

import org.junit.jupiter.api.Test;
import software.aoc.day01.b.Dial; // Importación directa de la Parte B

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01BTest {

    @Test
    public void solveDay01PartB() throws Exception {
        Path path = Paths.get("src/test/resources/day01-b/orders.txt");

        List<String> orders = Files.lines(path)
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .toList();

        Dial estadoInicial = new Dial();

        Dial estadoFinal = estadoInicial.executeAll(orders);

        int password = estadoFinal.zeroHits();

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 1 - PART B: " + password);
        System.out.println("***********************************");

        assertEquals(6623, password, "La contraseña de la parte B debe ser 6623");
    }
}