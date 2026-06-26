package software.aoc.day06;
import java.util.List;

public interface OperationStrategy {
    long execute(List<Long> operands);
}