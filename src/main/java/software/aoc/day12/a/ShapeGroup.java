package software.aoc.day12.a;

import java.util.BitSet;
import java.util.List;

public record ShapeGroup(int remainingQty, List<BitSet> validPlacements) {}