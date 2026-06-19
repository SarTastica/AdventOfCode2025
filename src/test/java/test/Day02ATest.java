package test;

import org.junit.jupiter.api.Test;
import software.aoc.day02.GiftShop;
import software.aoc.day02.ValidationRule;
import software.aoc.day02.a.RepeatedHalfRule;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02ATest {

    @Test
    public void solveDay02PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day02-a/input.txt");
        if (resource == null) {
            throw new RuntimeException("No se encuentra el archivo input.txt en src/test/resources/day02-a/");
        }

        String input = Files.readString(Path.of(resource.toURI()));

        ValidationRule miRegla = new RepeatedHalfRule();
        GiftShop giftShop = new GiftShop(miRegla);

        long result = giftShop.calculateInvalidIdSum(input);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 2 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(55916882972L, result, "La suma total no coincide con el puzzle answer.");
    }
}