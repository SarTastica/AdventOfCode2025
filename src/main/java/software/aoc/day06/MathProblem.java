package software.aoc.day06;

import java.util.List;

public record MathProblem(List<Long> operands, char operatorSymbol) {
    public long solve(OperationStrategy strategy) {
        return strategy.execute(this.operands);
    }
}