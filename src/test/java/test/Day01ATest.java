package test;

import org.junit.jupiter.api.Test;
import software.aoc.day01.a.Dial; // Importación directa de la Parte A

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01ATest {

    @Test
    public void solveDay01PartA() throws Exception {
        Path path = Paths.get("src/test/resources/day01-a/orders.txt");

        List<String> orders = Files.lines(path)
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .toList();

        Dial estadoInicial = new Dial();

        Dial estadoFinal = estadoInicial.executeAll(orders);

        int password = estadoFinal.zeroHits();

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 1 - PART A: " + password);
        System.out.println("***********************************");

        assertEquals(1132, password);
    }
}