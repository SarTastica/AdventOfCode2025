package test;

import org.junit.jupiter.api.Test;
import software.aoc.day02.b.GiftShop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day02BTest {

    @Test
    public void solveDay02PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day02-b/input.txt");
        String input = Files.readString(path);

        GiftShop giftShop = new GiftShop();
        long result = giftShop.calculateInvalidIdSum(input);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 2 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
