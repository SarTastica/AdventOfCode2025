package test;

import org.junit.jupiter.api.Test;
import software.aoc.day05.b.InventoryManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day05BTest {

    @Test
    public void solveDay05PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day05-b/input.txt");
        String fullInput = Files.readString(path);

        InventoryManager manager = new InventoryManager();
        long result = manager.countTotalFreshIds(fullInput);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 5 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
