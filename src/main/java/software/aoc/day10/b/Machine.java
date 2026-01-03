package software.aoc.day10.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Machine {
    private final int[] targets;
    private final List<List<Integer>> buttons;

    private long minPressesFound = Long.MAX_VALUE;

    public Machine(String line) {
        this.buttons = new ArrayList<>();
        Pattern patternTargets = Pattern.compile("\\{([\\d,]+)\\}");
        Matcher matcherTargets = patternTargets.matcher(line);

        if (matcherTargets.find()) {
            this.targets = Arrays.stream(matcherTargets.group(1).split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } else {
            throw new IllegalArgumentException("No se encontraron targets de voltaje");
        }

        Pattern patternButtons = Pattern.compile("\\(([\\d,]+)\\)");
        Matcher matcherButtons = patternButtons.matcher(line);

        while (matcherButtons.find()) {
            List<Integer> affectedCounters = new ArrayList<>();
            for (String s : matcherButtons.group(1).split(",")) {
                affectedCounters.add(Integer.parseInt(s.trim()));
            }
            this.buttons.add(affectedCounters);
        }
    }

    public long solveMinJoltagePresses() {
        this.minPressesFound = Long.MAX_VALUE;
        int[] currentCounters = new int[targets.length];
        int[] maxPressesPerButton = new int[buttons.size()];

        for (int i = 0; i < buttons.size(); i++) {
            int limit = Integer.MAX_VALUE;
            for (int counterIdx : buttons.get(i)) {
                if (counterIdx < targets.length) {
                    limit = Math.min(limit, targets[counterIdx]);
                }
            }
            maxPressesPerButton[i] = limit;
        }

        search(0, currentCounters, 0, maxPressesPerButton);

        return (minPressesFound == Long.MAX_VALUE) ? 0 : minPressesFound;
    }

    private void search(int btnIdx, int[] currentCounters, long currentPresses, int[] limits) {
        if (currentPresses >= minPressesFound) {
            return;
        }

        if (btnIdx == buttons.size()) {
            if (Arrays.equals(currentCounters, targets)) {
                minPressesFound = currentPresses;
            }
            return;
        }


        int limit = limits[btnIdx];
        List<Integer> affected = buttons.get(btnIdx);

        for (int k = 0; k <= limit; k++) {
            boolean possible = true;
            for (int counterIdx : affected) {
                if (currentCounters[counterIdx] + k > targets[counterIdx]) {
                    possible = false;
                    break;
                }
            }

            if (!possible) {
                break;
            }

            for (int counterIdx : affected) {
                currentCounters[counterIdx] += k;
            }

            search(btnIdx + 1, currentCounters, currentPresses + k, limits);

            for (int counterIdx : affected) {
                currentCounters[counterIdx] -= k;
            }
        }
    }
}
