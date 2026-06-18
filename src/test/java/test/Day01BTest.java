package test;

import org.junit.jupiter.api.Test;
import software.aoc.day01.CajaFuerte;
import software.aoc.day01.CommandParser;
import software.aoc.day01.b.Dial;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01BTest {

    @Test
    public void solveDay01PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day01-b/orders.txt");

        CajaFuerte miCajaFuerte = new Dial();

        try (Stream<String> lines = Files.lines(path)) {
            lines.filter(line -> !line.isBlank())
                    .map(String::trim)
                    .map(CommandParser::parse)
                    .forEach(cmd -> cmd.execute(miCajaFuerte));
        }

        int password = miCajaFuerte.getZeroHits();

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 1 - PART B: " + password);
        System.out.println("***********************************");

        assertEquals(6623, password, "La contraseña de la parte B debe ser 6623");
    }
}