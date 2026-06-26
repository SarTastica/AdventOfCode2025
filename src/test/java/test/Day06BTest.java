package test;

import org.junit.jupiter.api.Test;
import software.aoc.day06.CephalopodCalculator;
import software.aoc.day06.MathProblem;
import software.aoc.day06.StrategyProvider;
import software.aoc.day06.StandardMathProvider;
import software.aoc.day06.b.VerticalWorksheetParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Day06BTest {

    @Test
    public void solveDay06PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day06-a/input.txt");
        assertNotNull(resource, "El archivo de input no existe");
        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        VerticalWorksheetParser parser = new VerticalWorksheetParser();
        List<MathProblem> problems = parser.parse(lines);

        StrategyProvider mathProvider = new StandardMathProvider();
        CephalopodCalculator calculator = new CephalopodCalculator(mathProvider);

        long result = calculator.calculateGrandTotal(problems);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 6 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(8811937976367L, result, "El total no coincide con el resultado esperado.");
    }
}