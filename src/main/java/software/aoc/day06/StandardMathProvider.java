package software.aoc.day06;

public class StandardMathProvider implements StrategyProvider {
    @Override
    public OperationStrategy getStrategyFor(char symbol) {
        return switch (symbol) {
            case '+' -> new AdditionStrategy();
            case '*' -> new MultiplicationStrategy();
            default -> throw new IllegalArgumentException("Operador no soportado: " + symbol);
        };
    }
}