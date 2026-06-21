package software.aoc.day10;

import java.util.List;

public record Machine(int targetMask, List<Integer> buttonMasks, int numLights, List<Integer> targetJoltages,
                      List<List<Integer>> buttonWiring
) {}