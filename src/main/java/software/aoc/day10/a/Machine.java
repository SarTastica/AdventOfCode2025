package software.aoc.day10.a;

import java.util.List;

public record Machine(int targetMask, List<Integer> buttonMasks, int numLights) {
}