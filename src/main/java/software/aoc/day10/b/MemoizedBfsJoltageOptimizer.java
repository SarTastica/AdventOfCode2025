package software.aoc.day10.b;

import software.aoc.day10.Machine;

import java.util.*;

public class MemoizedBfsJoltageOptimizer implements JoltageOptimizationStrategy {
    private record State(List<Integer> counters) {}

    @Override
    public long calculateMinimumPresses(Machine machine) {
        List<Integer> target = machine.targetJoltages();
        List<List<Integer>> buttons = machine.buttonWiring();
        int numCounters = target.size();

        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        List<Integer> initialCounters = Collections.nCopies(numCounters, 0);
        State startState = new State(initialCounters);

        queue.add(startState);
        visited.add(startState);

        long presses = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                State curr = queue.poll();

                if (curr.counters().equals(target)) {
                    return presses;
                }

                for (List<Integer> buttonPins : buttons) {
                    List<Integer> nextCounters = new ArrayList<>(curr.counters());
                    boolean isValid = true;

                    for (int pin : buttonPins) {
                        int newVal = nextCounters.get(pin) + 1;
                        if (newVal > target.get(pin)) {
                            isValid = false;
                            break;
                        }
                        nextCounters.set(pin, newVal);
                    }

                    if (isValid) {
                        State nextState = new State(nextCounters);
                        if (visited.add(nextState)) {
                            queue.add(nextState);
                        }
                    }
                }
            }
            presses++;
        }

        throw new IllegalStateException("Imposible alcanzar el voltaje objetivo.");
    }
}