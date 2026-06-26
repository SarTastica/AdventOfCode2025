package test;

import org.junit.jupiter.api.Test;
import software.aoc.day05.CafeteriaManager;
import software.aoc.day05.InventoryParser;
import software.aoc.day05.a.MergedIntervalRule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05ATest {

    @Test
    public void solveDay05PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day05-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        InventoryParser.ParsedData data = InventoryParser.parse(lines);

        MergedIntervalRule rule = new MergedIntervalRule(data.ranges());
        CafeteriaManager manager = new CafeteriaManager(rule);

        long result = manager.countFreshIngredients(data.availableIds());

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 5 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(577L, result);
    }
}