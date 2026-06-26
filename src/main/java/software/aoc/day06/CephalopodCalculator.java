package software.aoc.day06;

import java.util.List;

public class CephalopodCalculator {
    private final StrategyProvider provider;

    public CephalopodCalculator(StrategyProvider provider) {
        this.provider = provider;
    }

    public long calculateGrandTotal(List<MathProblem> problems) {
        return problems.stream()
                .mapToLong(problem -> {
                    OperationStrategy strategy = provider.getStrategyFor(problem.operatorSymbol());
                    return problem.solve(strategy);
                })
                .sum();
    }
}