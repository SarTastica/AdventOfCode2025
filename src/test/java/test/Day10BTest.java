package test;

import org.junit.jupiter.api.Test;
// ¡Solo imports de la carpeta B!
import software.aoc.day10.b.FactoryManager;
import software.aoc.day10.b.FactoryParser;
import software.aoc.day10.b.Machine;
import software.aoc.day10.b.MachineSolver;
import software.aoc.day10.b.MemoizedMachineSolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day10BTest {

    @Test
    public void solveDay10PartB() throws Exception {
        var resource = getClass().getClassLoader().getResource("day10-a/input.txt");
        if (resource == null) throw new RuntimeException("Archivo no encontrado");

        String input = Files.readString(Path.of(resource.toURI()));

        FactoryParser parser = new FactoryParser();
        List<Machine> machines = parser.parse(input);

        MachineSolver strategy = new MemoizedMachineSolver();
        FactoryManager manager = new FactoryManager(strategy);

        long result = manager.configureAll(machines);

        System.out.println("***********************************");
        System.out.println("SOLUCIÓN DAY 10 - PART B: " + result);
        System.out.println("***********************************");

        assertEquals(15017L, result);
    }
}