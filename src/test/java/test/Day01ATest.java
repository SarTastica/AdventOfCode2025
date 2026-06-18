package test;

import org.junit.jupiter.api.Test;
import software.aoc.day01.CajaFuerte;
import software.aoc.day01.CommandParser;
import software.aoc.day01.a.Dial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01ATest {

    @Test
    public void solveDay01PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day01-a/orders.txt");

        CajaFuerte miCajaFuerte = new Dial();

        Files.lines(path)
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .map(CommandParser::parse)
                .forEach(cmd -> cmd.execute(miCajaFuerte));

        int password = miCajaFuerte.getZeroHits();

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 1 - PART A: " + password);
        System.out.println("***********************************");

        assertEquals(1132, password);
    }
}