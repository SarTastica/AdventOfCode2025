package software.aoc.day06.a;

import software.aoc.day06.MathProblem;
import java.util.ArrayList;
import java.util.List;

public class HorizontalWorksheetParser {

    public List<MathProblem> parse(List<String> lines) {
        List<MathProblem> problems = new ArrayList<>();
        int maxLen = lines.stream().mapToInt(String::length).max().orElse(0);

        List<String> paddedLines = lines.stream()
                .map(line -> String.format("%-" + maxLen + "s", line))
                .toList();

        int startCol = -1;
        for (int c = 0; c <= maxLen; c++) {
            boolean isBlankColumn = true;
            if (c < maxLen) {
                for (String line : paddedLines) {
                    if (line.charAt(c) != ' ') {
                        isBlankColumn = false;
                        break;
                    }
                }
            }

            if (isBlankColumn) {
                if (startCol != -1) {
                    problems.add(extractProblem(paddedLines, startCol, c - 1));
                    startCol = -1;
                }
            } else {
                if (startCol == -1) startCol = c;
            }
        }
        return problems;
    }

    private MathProblem extractProblem(List<String> lines, int startCol, int endCol) {
        List<Long> operands = new ArrayList<>();
        char operator = ' ';

        for (String line : lines) {
            String token = line.substring(startCol, endCol + 1).trim();
            if (token.isEmpty()) continue;

            if (token.equals("+") || token.equals("*")) {
                operator = token.charAt(0);
            } else {
                operands.add(Long.parseLong(token));
            }
        }
        return new MathProblem(operands, operator);
    }
}