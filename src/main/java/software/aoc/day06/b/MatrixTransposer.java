package software.aoc.day06.b;

import java.util.ArrayList;
import java.util.List;

public class MatrixTransposer {
    public List<String> transpose(List<String> lines) {
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
}