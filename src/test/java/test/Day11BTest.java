package test;

import org.junit.jupiter.api.Test;
import software.aoc.day11.NetworkGraph;
import software.aoc.day11.NetworkParser;
import software.aoc.day11.PathCounterStrategy;
import software.aoc.day11.ReactorManager;
import software.aoc.day11.b.MandatoryNodesPathCounter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day11BTest {

    @Test
    public void solveDay11PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day11-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        List<String> lines = Files.readAllLines(Path.of(resource.toURI()));

        NetworkParser parser = new NetworkParser();
        NetworkGraph graph = parser.parse(lines);

        PathCounterStrategy strategy = new MandatoryNodesPathCounter("dac", "fft");
        ReactorManager manager = new ReactorManager(strategy);

        long result = manager.analyzeDataFlow(graph, "svr", "out");

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 11 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(417190406827152L, result);
    }
}