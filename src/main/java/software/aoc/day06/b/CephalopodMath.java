package software.aoc.day06.b;

import java.util.ArrayList;
import java.util.List;

public class CephalopodMath {

    private enum Operation {
        ADD { @Override long apply(long a, long b) { return a + b; } },
        MULTIPLY { @Override long apply(long a, long b) { return a * b; } };

        abstract long apply(long a, long b);

        static Operation from(String symbol) {
            switch (symbol) {
                case "+": return ADD;
                case "*": return MULTIPLY;
                default: throw new IllegalArgumentException("Operación desconocida: " + symbol);
            }
        }
    }

    public long calculateGrandTotal(List<String> lines) {
        if (lines == null || lines.isEmpty()) return 0;

        int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);
        long grandTotal = 0;
        List<Integer> currentBlockCols = new ArrayList<>();

        for (int col = 0; col < maxWidth; col++) {
            if (isEmptyColumn(col, lines)) {
                if (!currentBlockCols.isEmpty()) {
                    grandTotal += solveProblem(currentBlockCols, lines);
                    currentBlockCols.clear();
                }
            } else {
                currentBlockCols.add(col);
            }
        }
        if (!currentBlockCols.isEmpty()) {
            grandTotal += solveProblem(currentBlockCols, lines);
        }

        return grandTotal;
    }

    private boolean isEmptyColumn(int col, List<String> lines) {
        for (String line : lines) {
            if (col < line.length() && line.charAt(col) != ' ') {
                return false;
            }
        }
        return true;
    }

    private long solveProblem(List<Integer> cols, List<String> lines) {
        List<Long> numbers = new ArrayList<>();
        Operation operation = null;
        int lastRowIndex = lines.size() - 1;

        for (int col : cols) {
            char c = getSafeChar(lines.get(lastRowIndex), col);
            if (c == '+' || c == '*') {
                operation = Operation.from(String.valueOf(c));
                break;
            }
        }

        for (int col : cols) {
            StringBuilder numBuilder = new StringBuilder();

            for (int row = 0; row < lastRowIndex; row++) {
                char c = getSafeChar(lines.get(row), col);
                if (Character.isDigit(c)) {
                    numBuilder.append(c);
                }
            }

            if (numBuilder.length() > 0) {
                numbers.add(Long.parseLong(numBuilder.toString()));
            }
        }

        if (operation == null || numbers.isEmpty()) return 0;

        long result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = operation.apply(result, numbers.get(i));
        }

        return result;
    }

    private char getSafeChar(String line, int col) {
        if (col < line.length()) {
            return line.charAt(col);
        }
        return ' ';
    }
}
