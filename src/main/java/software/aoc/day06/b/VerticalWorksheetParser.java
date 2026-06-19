package software.aoc.day06.b;

import software.aoc.day06.MathProblem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerticalWorksheetParser {
    private final MatrixTransposer transposer = new MatrixTransposer();
    private final ProblemExtractor extractor = new ProblemExtractor();

    public List<MathProblem> parse(List<String> lines) {
        List<String> columns = transposer.transpose(lines);
        Collections.reverse(columns);

        List<MathProblem> problems = new ArrayList<>();
        List<String> currentProblemBuffer = new ArrayList<>();

        for (String col : columns) {
            boolean isSeparator = col.trim().isEmpty();

            if (isSeparator) {
                if (!currentProblemBuffer.isEmpty()) {
                    problems.add(extractor.fromColumn(String.join(" ", currentProblemBuffer)));
                    currentProblemBuffer.clear();
                }
            } else {
                currentProblemBuffer.add(col);
            }
        }

        if (!currentProblemBuffer.isEmpty()) {
            problems.add(extractor.fromColumn(String.join(" ", currentProblemBuffer)));
        }
        return problems;
    }
}