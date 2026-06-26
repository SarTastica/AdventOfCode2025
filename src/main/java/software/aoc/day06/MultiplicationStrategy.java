package software.aoc.day06;
import java.util.List;

public class MultiplicationStrategy implements OperationStrategy {
    @Override
    public long execute(List<Long> operands) {
        return operands.stream().reduce(1L, (a, b) -> a * b);
    }
}