package software.aoc.day10.a;

import java.util.ArrayList;
import java.util.List;

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
            for (int i = 1; i < parts.length; i++) {
                if (parts[i].startsWith("(")) {
                    String btnStr = parts[i].substring(1, parts[i].length() - 1);
                    int btnMask = 0;
                    if (!btnStr.isEmpty()) {
                        for (String idx : btnStr.split(",")) {
                            btnMask |= (1 << Integer.parseInt(idx));
                        }
                    }
                    buttonMasks.add(btnMask);
                }
            }

            machines.add(new Machine(targetMask, buttonMasks, targetStr.length()));
        }
        return machines;
    }
}