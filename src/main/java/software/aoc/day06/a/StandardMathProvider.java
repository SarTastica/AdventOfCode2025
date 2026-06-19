package software.aoc.day06.a;

import software.aoc.day06.OperationStrategy;
import software.aoc.day06.StrategyProvider;

public class StandardMathProvider implements StrategyProvider {
    @Override
    public OperationStrategy getStrategyFor(char symbol) {
        return switch (symbol) {
            case '+' -> new AdditionStrategy();
            case '*' -> new MultiplicationStrategy();
            default -> throw new IllegalArgumentException("Operador no soportado en Parte A: " + symbol);
        };
    }
}