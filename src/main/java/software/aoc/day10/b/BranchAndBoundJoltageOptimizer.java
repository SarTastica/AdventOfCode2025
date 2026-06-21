package software.aoc.day10.b;

import software.aoc.day10.Machine;
import java.util.List;

public class BranchAndBoundJoltageOptimizer implements JoltageOptimizationStrategy {

    @Override
    public long calculateMinimumPresses(Machine machine) {
        List<Integer> targets = machine.targetJoltages();
        List<List<Integer>> buttons = machine.buttonWiring();

        int[] targetArr = targets.stream().mapToInt(i -> i).toArray();
        int[] currentArr = new int[targetArr.length];

        long[] minPresses = { Long.MAX_VALUE };

        solve(0, buttons, targetArr, currentArr, 0, minPresses);

        if (minPresses[0] == Long.MAX_VALUE) {
            throw new IllegalStateException("Imposible alcanzar el voltaje objetivo.");
        }

        return minPresses[0];
    }

    private void solve(int buttonIdx, List<List<Integer>> buttons, int[] target, int[] current, long presses, long[] minPresses) {
        if (presses >= minPresses[0]) {
            return;
        }

        if (buttonIdx == buttons.size()) {
            boolean isValid = true;
            for (int i = 0; i < target.length; i++) {
                if (current[i] != target[i]) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                minPresses[0] = Math.min(minPresses[0], presses);
            }
            return;
        }
        List<Integer> affectedCounters = buttons.get(buttonIdx);
        int maxPressesForThisButton = Integer.MAX_VALUE;

        for (int counterIdx : affectedCounters) {
            int margin = target[counterIdx] - current[counterIdx];
            if (margin < maxPressesForThisButton) {
                maxPressesForThisButton = margin;
            }
        }

        if (maxPressesForThisButton < 0) {
            return;
        }
        for (int p = maxPressesForThisButton; p >= 0; p--) {

            for (int counterIdx : affectedCounters) {
                current[counterIdx] += p;
            }

            solve(buttonIdx + 1, buttons, target, current, presses + p, minPresses);
            for (int counterIdx : affectedCounters) {
                current[counterIdx] -= p;
            }
        }
    }
}