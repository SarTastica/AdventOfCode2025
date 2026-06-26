package software.aoc.day10.b;

import java.util.List;
import java.util.stream.IntStream;
import static java.lang.Math.pow;

public record JoltageEffect(int[] wiringEffect, int buttonPressCount) {
    public static List<JoltageEffect> allFrom(List<Button> buttons, int machineSize) {
        return IntStream.range(0, (int) pow(2, buttons.size()))
                .mapToObj(mask -> effectOf(mask, buttons, machineSize))
                .toList();
    }

    public int wiringEffectAt(int i) {
        return wiringEffect[i];
    }

    private static JoltageEffect effectOf(int mask, List<Button> buttons, int machineSize) {
        return new JoltageEffect(buttonsWiringEffect(mask, buttons, machineSize), Integer.bitCount(mask));
    }

    private static int[] buttonsWiringEffect(int mask, List<Button> buttons, int machineSize) {
        return IntStream.range(0, buttons.size())
                .filter(i -> (mask & (1 << i)) != 0)
                .mapToObj(i -> buttons.get(i).wiring())
                .reduce(new int[machineSize], JoltageEffect::addArrays);
    }

    private static int[] addArrays(int[] a, int[] b) {
        return IntStream.range(0, a.length).map(i -> a[i] + b[i]).toArray();
    }
}