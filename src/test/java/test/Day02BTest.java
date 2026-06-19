package test;

import org.junit.jupiter.api.Test;
import software.aoc.day02.GiftShop;               // Motor común
import software.aoc.day02.ValidationRule;         // Contrato común
import software.aoc.day02.b.RepeatedPatternRule;  // NUEVA regla específica de B

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02BTest {

    @Test
    public void solveDay02PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day02-a/input.txt");
        if (resource == null) {
            throw new RuntimeException("No se encuentra el archivo input.txt");
        }

        String input = Files.readString(Path.of(resource.toURI()));

        ValidationRule nuevaRegla = new RepeatedPatternRule();
        GiftShop giftShop = new GiftShop(nuevaRegla);

        long result = giftShop.calculateInvalidIdSum(input);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 2 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(76169125915L, result, "La suma total no coincide con el puzzle answer de la Parte B.");
    }
}