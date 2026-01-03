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
    private int maxImpactPerPress = 1;

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
                int counterIdx = Integer.parseInt(s.trim());
                if (counterIdx < targets.length) {
                    affectedCounters.add(counterIdx);
                }
            }
            this.buttons.add(affectedCounters);
        }

        this.maxImpactPerPress = 0;
        for (List<Integer> btn : buttons) {
            this.maxImpactPerPress = Math.max(this.maxImpactPerPress, btn.size());
        }
        if (this.maxImpactPerPress == 0) this.maxImpactPerPress = 1;
    }

    public long solveMinJoltagePresses() {
        this.minPressesFound = Long.MAX_VALUE;
        int[] currentCounters = new int[targets.length];

        int[] maxPressesPerButton = new int[buttons.size()];
        for (int i = 0; i < buttons.size(); i++) {
            int limit = Integer.MAX_VALUE;
            List<Integer> btn = buttons.get(i);

            if (btn.isEmpty()) {
                limit = 0;
            } else {
                for (int counterIdx : btn) {
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

        long remainingGap = 0;
        for (int i = 0; i < targets.length; i++) {
            remainingGap += (targets[i] - currentCounters[i]);
        }

        if (remainingGap == 0) {
            minPressesFound = currentPresses;
            return;
        }

        long minStepsNeeded = (remainingGap + maxImpactPerPress - 1) / maxImpactPerPress;

        if (currentPresses + minStepsNeeded >= minPressesFound) {
            return;
        }

        if (btnIdx == buttons.size()) {
            return;
        }

        int limit = limits[btnIdx];
        List<Integer> affected = buttons.get(btnIdx);

        if (affected.isEmpty()) {
            search(btnIdx + 1, currentCounters, currentPresses, limits);
            return;
        }

        for (int k = 0; k <= limit; k++) {

            boolean overflow = false;
            for (int counterIdx : affected) {
                if (currentCounters[counterIdx] + k > targets[counterIdx]) {
                    overflow = true;
                    break;
                }
            }

            if (overflow) break;

            for (int counterIdx : affected) currentCounters[counterIdx] += k;

            search(btnIdx + 1, currentCounters, currentPresses + k, limits);

            for (int counterIdx : affected) currentCounters[counterIdx] -= k;
        }
    }
}
