package test;

import org.junit.jupiter.api.Test;
import software.aoc.day09.a.MovieTheater;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day09ATest {

    @Test
    public void solveDay09PartA() throws IOException {
        Path path = Paths.get("src/test/resources/day09-a/input.txt");
        List<String> lines = Files.readAllLines(path);

        MovieTheater theater = new MovieTheater();
        long result = theater.findLargestRectangleArea(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 9 - PART A: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
