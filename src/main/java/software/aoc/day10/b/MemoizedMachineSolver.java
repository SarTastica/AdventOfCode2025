package software.aoc.day10.b;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.List;

public class MemoizedMachineSolver implements MachineSolver {

    @Override
    public int solve(Machine machine) {
        Map<JoltageState, OptionalInt> knownStates = new HashMap<>();
        List<JoltageEffect> availableEffects = JoltageEffect.allFrom(
                machine.buttons(),
                machine.targetJoltage().size()
        );

        return configureJoltage(machine.targetJoltage(), knownStates, availableEffects).orElse(0);
    }

    private OptionalInt configureJoltage(JoltageState state,
                                         Map<JoltageState, OptionalInt> memo,
                                         List<JoltageEffect> effects) {
        if (state.isSolved()) return OptionalInt.of(0);
        if (memo.containsKey(state)) return memo.get(state);

        OptionalInt result = calculateMinPresses(state, memo, effects);
        memo.put(state, result);
        return result;
    }

    private OptionalInt calculateMinPresses(JoltageState state,
                                            Map<JoltageState, OptionalInt> memo,
                                            List<JoltageEffect> effects) {
        return effects.stream()
                .filter(state::canApply)
                .map(effect -> nextStateWith(state, effect, memo, effects))
                .flatMapToInt(OptionalInt::stream)
                .min();
    }

    private OptionalInt nextStateWith(JoltageState state,
                                      JoltageEffect effect,
                                      Map<JoltageState, OptionalInt> memo,
                                      List<JoltageEffect> effects) {
        return configureJoltage(state.nextState(effect), memo, effects).stream()
                .map(subResult -> effect.buttonPressCount() + 2 * subResult)
                .findFirst();
    }
}