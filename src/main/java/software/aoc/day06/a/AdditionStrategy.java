package software.aoc.day06.a;

import software.aoc.day06.OperationStrategy;
import java.util.List;

public class AdditionStrategy implements OperationStrategy {
    @Override
    public long execute(List<Long> operands) {
        return operands.stream().mapToLong(Long::longValue).sum();
    }
}