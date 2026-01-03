package software.aoc.day10.a;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Machine {
    private final long targetState;
    private final List<Long> buttons;

    public Machine(String line) {
        this.buttons = new ArrayList<>();

        Pattern patternTarget = Pattern.compile("\\[([.#]+)\\]");
        Matcher matcherTarget = patternTarget.matcher(line);

        if (matcherTarget.find()) {
            this.targetState = parseState(matcherTarget.group(1));
        } else {
            throw new IllegalArgumentException("Formato de luces inválido");
        }

        Pattern patternButtons = Pattern.compile("\\(([\\d,]+)\\)");
        Matcher matcherButtons = patternButtons.matcher(line);

        while (matcherButtons.find()) {
            this.buttons.add(parseButton(matcherButtons.group(1)));
        }
    }

    private long parseState(String pattern) {
        long state = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '#') {
                state |= (1L << i);
            }
        }
        return state;
    }
    private long parseButton(String content) {
        long mask = 0;
        String[] indices = content.split(",");
        for (String idx : indices) {
            int position = Integer.parseInt(idx.trim());
            mask |= (1L << position);
        }
        return mask;
    }
    public int solveMinPresses() {
        Queue<Long> queue = new LinkedList<>();
        Map<Long, Integer> visited = new HashMap<>();

        long startState = 0L;
        queue.add(startState);
        visited.put(startState, 0);

        while (!queue.isEmpty()) {
            long current = queue.poll();
            int steps = visited.get(current);

            if (current == targetState) {
                return steps;
            }

            for (long buttonMask : buttons) {
                long nextState = current ^ buttonMask;

                if (!visited.containsKey(nextState)) {
                    visited.put(nextState, steps + 1);
                    queue.add(nextState);
                }
            }
        }
        return -1;
    }
}
