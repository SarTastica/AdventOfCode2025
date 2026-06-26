package software.aoc.day10.b;

import java.util.List;
import java.util.stream.IntStream;

public record JoltageState(List<Integer> values) {

    public boolean isSolved() {
        return values.stream().allMatch(v -> v == 0);
    }

    public boolean canApply(JoltageEffect effect) {
        return IntStream.range(0, values.size())
                .allMatch(i -> effect.wiringEffectAt(i) <= values.get(i) &&
                        matchesParity(effect.wiringEffectAt(i), values.get(i)));
    }

    public JoltageState nextState(JoltageEffect effect) {
        return new JoltageState(minusAndHalveWith(effect));
    }

    public int size() {
        return values.size();
    }

    private List<Integer> minusAndHalveWith(JoltageEffect effect) {
        return IntStream.range(0, values.size())
                .map(i -> (values.get(i) - effect.wiringEffectAt(i)) / 2).boxed()
                .toList();
    }

    private static boolean matchesParity(int a, int b) {
        return (a & 1) == (b & 1);
    }
}