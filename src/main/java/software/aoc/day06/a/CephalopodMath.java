package software.aoc.day06.a;

import java.util.ArrayList;
import java.util.List;

public class CephalopodMath {

    private enum Operation {
        ADD {
            @Override
            long apply(long a, long b) { return a + b; }
        },
        MULTIPLY {
            @Override
            long apply(long a, long b) { return a * b; }
        };

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
        int startCol = cols.get(0);
        int endCol = cols.get(cols.size() - 1) + 1;

        List<Long> numbers = new ArrayList<>();
        Operation operation = null;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String segment = "";
            if (startCol < line.length()) {
                segment = line.substring(startCol, Math.min(endCol, line.length()));
            }
            segment = segment.trim();

            if (segment.isEmpty()) continue;
            if (i == lines.size() - 1) {
                operation = Operation.from(segment);
            } else {
                try {
                    numbers.add(Long.parseLong(segment));
                } catch (NumberFormatException e) {
                    if (segment.equals("+") || segment.equals("*")) {
                        operation = Operation.from(segment);
                    }
                }
            }
        }

        if (operation == null || numbers.isEmpty()) return 0;

        long result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = operation.apply(result, numbers.get(i));
        }

        return result;
    }
}
