package software.aoc.day06;

public interface StrategyProvider {
    OperationStrategy getStrategyFor(char symbol);
}