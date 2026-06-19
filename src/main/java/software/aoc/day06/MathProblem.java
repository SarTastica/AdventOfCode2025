package software.aoc.day06;

import java.util.List;

public class MathProblem {
    private final List<Long> operands;
    private final char operatorSymbol;

    public MathProblem(List<Long> operands, char operatorSymbol) {
        this.operands = operands;
        this.operatorSymbol = operatorSymbol;
    }
    public char getOperatorSymbol() {
        return operatorSymbol;
    }
    public long solve(OperationStrategy strategy) {
        return strategy.execute(this.operands);
    }
}