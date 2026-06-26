package software.aoc.day04;

public interface AccessibilityRule {
    boolean isAccessible(Grid grid, Position pos);
}