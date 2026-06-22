package test;

import org.junit.jupiter.api.Test;
import software.aoc.day11.NetworkGraph;
import software.aoc.day11.NetworkParser;
import software.aoc.day11.PathCounterStrategy;
import software.aoc.day11.ReactorManager;
import software.aoc.day11.a.MemoizedDfsPathCounter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day11ATest {

    @Test
    public void solveDay11PartA() throws Exception {
        var resource = getClass().getClassLoader().getResource("day11-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        NetworkParser parser = new NetworkParser();
        NetworkGraph graph = parser.parse(lines);

        PathCounterStrategy strategy = new MemoizedDfsPathCounter();
        ReactorManager manager = new ReactorManager(strategy);

        long result = manager.analyzeDataFlow(graph, "you", "out");

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 11 - PART A: " + result);
        System.out.println("***********************************");

        assertEquals(643L, result);
    }
}