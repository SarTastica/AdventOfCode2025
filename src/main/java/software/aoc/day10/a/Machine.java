package software.aoc.day10.a;

import java.util.*;

public class Machine {
    private final long target;
    private final List<Long> buttons = new ArrayList<>();

    public Machine(String line) {
        String stateStr = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
        long t = 0;
        for (int i = 0; i < stateStr.length(); i++) {
            if (stateStr.charAt(i) == '#') {
                t |= (1L << i);
            }
        }
        this.target = t;

        String[] parts = line.substring(line.indexOf(']') + 1).split("\\(");

        for (String part : parts) {
            int closingIndex = part.indexOf(')');

            if (closingIndex == -1) continue;

            String clean = part.substring(0, closingIndex);

            long mask = 0;
            for (String n : clean.split(",")) {
                mask |= (1L << Integer.parseInt(n.trim()));
            }
            buttons.add(mask);
        }
    }

    public int solveMinPresses() {
        Queue<Long> queue = new ArrayDeque<>();
        Map<Long, Integer> visited = new HashMap<>();

        long start = 0L;
        queue.add(start);
        visited.put(start, 0);

        while (!queue.isEmpty()) {
            long current = queue.poll();
            int steps = visited.get(current);

            if (current == target) return steps;

            for (long buttonMask : buttons) {
                long next = current ^ buttonMask;

                if (!visited.containsKey(next)) {
                    visited.put(next, steps + 1);
                    queue.add(next);
                }
            }
        }
        return -1;
    }
}