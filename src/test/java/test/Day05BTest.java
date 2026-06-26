package test;

import org.junit.jupiter.api.Test;
import software.aoc.day05.InventoryParser;
import software.aoc.day05.b.TotalFreshnessCalculator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05BTest {

    @Test
    public void solveDay05PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day05-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        InventoryParser.ParsedData data = InventoryParser.parse(lines);

        TotalFreshnessCalculator calculator = new TotalFreshnessCalculator();

        long result = calculator.calculateTotalFresh(data.ranges());

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 5 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(350513176552950L, result, "El total de ingredientes frescos no coincide");
    }
}