package software.aoc.day10.b;

import java.util.*;
import java.util.stream.Collectors;

public class Machine {
    private final List<Long> targets;
    private final Map<List<Long>, Integer> patterns = new HashMap<>();

    public Machine(String line) {
        String targetStr = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
        this.targets = Arrays.stream(targetStr.split(","))
                .map(s -> Long.parseLong(s.trim()))
                .collect(Collectors.toList());

        List<List<Integer>> buttons = new ArrayList<>();
        String[] btnParts = line.substring(line.indexOf(']') + 1, line.indexOf('{')).split("\\)");
        for (String part : btnParts) {
            if (part.isBlank()) continue;
            String clean = part.replace("(", "").trim();
            if (clean.isEmpty()) { buttons.add(List.of()); continue; }

            buttons.add(Arrays.stream(clean.split(","))
                    .map(s -> Integer.parseInt(s.trim()))
                    .collect(Collectors.toList()));
        }

        int limit = 1 << buttons.size();
        int numRegisters = targets.size();

        for (int mask = 0; mask < limit; mask++) {
            List<Long> effect = new ArrayList<>(Collections.nCopies(numRegisters, 0L));
            int cost = 0;

            for (int i = 0; i < buttons.size(); i++) {
                if ((mask & (1 << i)) != 0) {
                    cost++;
                    for (int regIndex : buttons.get(i)) {
                        if (regIndex < numRegisters) {
                            effect.set(regIndex, effect.get(regIndex) + 1);
                        }
                    }
                }
            }
            if (!patterns.containsKey(effect) || cost < patterns.get(effect)) {
                patterns.put(effect, cost);
            }
        }
    }

    public long solveMinJoltagePresses() {
        return solve(targets, new HashMap<>());
    }

    private long solve(List<Long> current, Map<List<Long>, Long> memo) {
        if (current.stream().allMatch(v -> v == 0)) return 0;
        if (memo.containsKey(current)) return memo.get(current);

        long minCost = Long.MAX_VALUE;

        for (Map.Entry<List<Long>, Integer> entry : patterns.entrySet()) {
            List<Long> pattern = entry.getKey();
            int patternCost = entry.getValue();

            List<Long> nextState = new ArrayList<>();
            boolean valid = true;

            for (int i = 0; i < current.size(); i++) {
                long diff = current.get(i) - pattern.get(i);
                if (diff < 0 || diff % 2 != 0) {
                    valid = false;
                    break;
                }
                nextState.add(diff / 2);
            }

            if (valid) {
                long costRest = solve(nextState, memo);
                if (costRest != Long.MAX_VALUE) {
                    minCost = Math.min(minCost, patternCost + 2 * costRest);
                }
            }
        }

        memo.put(current, minCost);
        return minCost;
    }
}