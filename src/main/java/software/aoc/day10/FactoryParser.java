package software.aoc.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FactoryParser {
    public List<Machine> parse(List<String> lines) {
        List<Machine> machines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(" ");

            String targetStr = parts[0].substring(1, parts[0].length() - 1);
            int targetMask = 0;
            for (int i = 0; i < targetStr.length(); i++) {
                if (targetStr.charAt(i) == '#') {
                    targetMask |= (1 << i);
                }
            }

            List<Integer> buttonMasks = new ArrayList<>();
            List<List<Integer>> buttonWiring = new ArrayList<>();

            int partIdx = 1;
            while (partIdx < parts.length && parts[partIdx].startsWith("(")) {
                String btnStr = parts[partIdx].substring(1, parts[partIdx].length() - 1);
                int btnMask = 0;
                List<Integer> wiring = new ArrayList<>();
                if (!btnStr.isEmpty()) {
                    for (String idx : btnStr.split(",")) {
                        int pin = Integer.parseInt(idx);
                        btnMask |= (1 << pin);
                        wiring.add(pin);
                    }
                }
                buttonMasks.add(btnMask);
                buttonWiring.add(wiring);
                partIdx++;
            }

            List<Integer> targetJoltages = new ArrayList<>();
            if (partIdx < parts.length && parts[partIdx].startsWith("{")) {
                String joltStr = parts[partIdx].substring(1, parts[partIdx].length() - 1);
                if (!joltStr.isEmpty()) {
                    targetJoltages = Arrays.stream(joltStr.split(","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                }
            }

            machines.add(new Machine(targetMask, buttonMasks, targetStr.length(), targetJoltages, buttonWiring));
        }
        return machines;
    }
}