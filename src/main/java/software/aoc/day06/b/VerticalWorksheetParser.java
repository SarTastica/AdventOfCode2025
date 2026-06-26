package software.aoc.day06.b;

import software.aoc.day06.MathProblem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerticalWorksheetParser {

    public List<MathProblem> parse(List<String> lines) {
        List<String> columns = transposeMatrix(lines);
        Collections.reverse(columns);

        List<MathProblem> problems = new ArrayList<>();
        List<String> currentBuffer = new ArrayList<>();

        for (String col : columns) {
            if (col.trim().isEmpty()) {
                if (!currentBuffer.isEmpty()) {
                    problems.add(extractFromBuffer(currentBuffer));
                    currentBuffer.clear();
                }
            } else {
                currentBuffer.add(col);
            }
        }

        if (!currentBuffer.isEmpty()) {
            problems.add(extractFromBuffer(currentBuffer));
        }

        return problems;
    }

    private List<String> transposeMatrix(List<String> lines) {
        int maxLen = lines.stream().mapToInt(String::length).max().orElse(0);
        List<String> columns = new ArrayList<>();

        for (int c = 0; c < maxLen; c++) {
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                String paddedLine = String.format("%-" + maxLen + "s", line);
                sb.append(paddedLine.charAt(c));
            }
            columns.add(sb.toString());
        }
        return columns;
    }

    private MathProblem extractFromBuffer(List<String> buffer) {
        String sanitized = String.join(" ", buffer).replace("+", " + ").replace("*", " * ");
        String[] tokens = sanitized.trim().split("\\s+");

        List<Long> operands = new ArrayList<>();
        char operator = ' ';

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