package software.aoc.day06.b;

import software.aoc.day06.MathProblem;
import java.util.ArrayList;
import java.util.List;

public class ProblemExtractor {
    public MathProblem fromColumn(String columnContent) {
        List<Long> operands = new ArrayList<>();
        char operator = ' ';
        String sanitized = columnContent.replace("+", " + ").replace("*", " * ");
        String[] tokens = sanitized.trim().split("\\s+");

        for (String token : tokens) {
            if (token.equals("+") || token.equals("*")) {
                operator = token.charAt(0);
            } else {
                operands.add(Long.parseLong(token));
            }
        }
        return new MathProblem(operands, operator);
    }
}