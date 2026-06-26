package software.aoc.day10.b;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import static java.lang.String.valueOf;

public class FactoryParser {
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((.*?)\\)");

    public List<Machine> parse(String input) {
        return Arrays.stream(input.split("\\r?\\n"))
                .filter(s -> !s.isBlank())
                .map(this::parseMachine)
                .toList();
    }

    private Machine parseMachine(String line) {
        String[] parts = line.split(" ");
        String joltageStr = stripParentheses(parts[parts.length - 1]);
        JoltageState target = new JoltageState(parseIntegerList(joltageStr));

        List<Button> buttons = BUTTON_PATTERN.matcher(line).results()
                .map(MatchResult::group)
                .map(grp -> parseButton(grp, target.size()))
                .toList();

        return new Machine(target, buttons);
    }

    private Button parseButton(String grp, int length) {
        String clean = stripParentheses(grp);
        String[] wiringStrs = clean.split(",");
        int[] wiring = IntStream.range(0, length)
                .map(i -> Arrays.asList(wiringStrs).contains(valueOf(i)) ? 1 : 0)
                .toArray();
        return new Button(wiring);
    }

    private String stripParentheses(String diagram) {
        return diagram.substring(1, diagram.length() - 1);
    }

    private List<Integer> parseIntegerList(String values) {
        return Arrays.stream(values.split(","))
                .mapToInt(Integer::parseInt).boxed()
                .toList();
    }
}