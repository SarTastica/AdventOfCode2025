package test;

import org.junit.jupiter.api.Test;
import software.aoc.day09.b.MovieTheater;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day09BTest {

    @Test
    public void solveDay09PartB() throws IOException {
        Path path = Paths.get("src/test/resources/day09-b/input.txt");
        List<String> lines = Files.readAllLines(path);

        MovieTheater theater = new MovieTheater();
        long result = theater.findLargestValidRectangleArea(lines);

        System.out.println("***********************************");
        System.out.println("SOLUCION DAY 9 - PART B: " + result);
        System.out.println("***********************************");

        assertTrue(result > 0);
    }
}
