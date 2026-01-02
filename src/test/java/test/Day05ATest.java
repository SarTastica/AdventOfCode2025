package test;

import org.junit.jupiter.api.Test;
import software.aoc.day05.a.InventoryManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day05ATest {

    @Test
    public void solveDay05PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day05-a/input.txt");
        String fullInput = Files.readString(path);

        InventoryManager manager = new InventoryManager();
        long result = manager.countFreshIngredients(fullInput);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 5 - PART A: " + result);
        System.out.println("***********************************");

        assertTrue(result >= 0);
    }
}
