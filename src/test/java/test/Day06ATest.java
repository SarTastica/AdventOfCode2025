package test;

import org.junit.jupiter.api.Test;
import software.aoc.day06.CephalopodCalculator;
import software.aoc.day06.MathProblem;
import software.aoc.day06.StrategyProvider;
import software.aoc.day06.StandardMathProvider;
import software.aoc.day06.a.HorizontalWorksheetParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day06ATest {

    @Test
    public void solveDay06PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day06-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        HorizontalWorksheetParser parser = new HorizontalWorksheetParser();
        List<MathProblem> problems = parser.parse(lines);

        StrategyProvider mathProvider = new StandardMathProvider();
        CephalopodCalculator calculator = new CephalopodCalculator(mathProvider);

        long result = calculator.calculateGrandTotal(problems);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 6 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(6171290547579L, result);
    }
}