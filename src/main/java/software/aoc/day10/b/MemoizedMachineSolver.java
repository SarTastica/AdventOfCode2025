package software.aoc.day10.b;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.List;

public class MemoizedMachineSolver implements MachineSolver {
    private Map<JoltageState, OptionalInt> knownStates;
    private List<JoltageEffect> availableEffects;

    @Override
    public int solve(Machine machine) {
        this.knownStates = new HashMap<>();
        this.availableEffects = JoltageEffect.allFrom(machine.buttons(), machine.targetJoltage().size());

        return configureJoltage(machine.targetJoltage()).orElse(0);
    }

    private OptionalInt configureJoltage(JoltageState joltageState) {
        if (joltageState.isSolved()) return OptionalInt.of(0);
        if (knownStates.containsKey(joltageState)) return knownStates.get(joltageState);

        OptionalInt result = calculateMinPresses(joltageState);
        knownStates.put(joltageState, result);
        return result;
    }

    private OptionalInt calculateMinPresses(JoltageState joltageState) {
        return availableEffects.stream()
                .filter(joltageState::canApply)
                .map(effect -> nextStateWith(joltageState, effect))
                .flatMapToInt(OptionalInt::stream)
                .min();
    }

    private OptionalInt nextStateWith(JoltageState joltageState, JoltageEffect effect) {
        return configureJoltage(joltageState.nextState(effect)).stream()
                .map(subResult -> effect.buttonPressCount() + 2 * subResult)
                .findFirst();
    }
}