package software.aoc.day10.b;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Machine {

    private List<List<Integer>> buttons;
    private List<Integer> joltageRequirements;
    private Boolean[][] buttonInfluences;
    private Map<List<Integer>, Integer> joltageIncrToNrPresses;

    public Machine(final String line) {
        parse(line);
        calculatePatterns();
    }

    public long solveMinJoltagePresses() {
        int result = getNrOfPressesForVoltages(joltageRequirements, new HashMap<>());
        return (long) result;
    }

    private int getNrOfPressesForVoltages(final List<Integer> current, final Map<List<Integer>, Integer> memoMap) {
        if (current.stream().allMatch(i -> i == 0)) {
            return 0;
        }
        if (memoMap.containsKey(current)) {
            return memoMap.get(current);
        }

        int minTotalPresses = Integer.MAX_VALUE;

        for (final Entry<List<Integer>, Integer> entry : joltageIncrToNrPresses.entrySet()) {
            final List<Integer> pattern = entry.getKey();

            if (isValid(current, pattern)) {
                final List<Integer> newGoal = new ArrayList<>(current.size());
                for (int i = 0; i < current.size(); i++) {
                    newGoal.add((current.get(i) - pattern.get(i)) / 2);
                }

                // Recursión
                final int pressesForRest = getNrOfPressesForVoltages(newGoal, memoMap);

                if (pressesForRest < Integer.MAX_VALUE) {
                    minTotalPresses = Math.min(minTotalPresses, entry.getValue() + (2 * pressesForRest));
                }
            }
        }

        memoMap.put(current, minTotalPresses);
        return minTotalPresses;
    }

    private boolean isValid(final List<Integer> current, final List<Integer> pattern) {
        for (int i = 0; i < pattern.size(); i++) {
            final int incr = pattern.get(i);
            final int cur = current.get(i);
            if (!(incr <= cur && Math.abs(incr % 2) == Math.abs(cur % 2))) {
                return false;
            }
        }
        return true;
    }

    private void calculatePatterns() {
        final int nrPatterns = (int) Math.pow(2, buttons.size());
        joltageIncrToNrPresses = new HashMap<>();

        for (int i = 0; i < nrPatterns; i++) {
            final List<Integer> joltageIncreases = new ArrayList<>(Collections.nCopies(joltageRequirements.size(), 0));

            final String binary = getBinaryRepresentation(i);
            int nrButtonsPressed = 0;

            for (int buttonNr = 0; buttonNr < binary.length(); buttonNr++) {
                if (binary.charAt(buttonNr) == '1') {
                    final Boolean[] influence = buttonInfluences[buttonNr];
                    for (int k = 0; k < influence.length; k++) {
                        if (influence[k]) {
                            joltageIncreases.set(k, joltageIncreases.get(k) + 1);
                        }
                    }
                    nrButtonsPressed++;
                }
            }

            if (!joltageIncrToNrPresses.containsKey(joltageIncreases)
                    || joltageIncrToNrPresses.get(joltageIncreases) > nrButtonsPressed) {
                joltageIncrToNrPresses.put(joltageIncreases, nrButtonsPressed);
            }
        }
    }

    private void parse(final String line) {
        String buttonsSegment = line.substring(line.indexOf("]") + 1, line.indexOf("{")).trim();
        String[] rawButtons = buttonsSegment.split("\\)");

        buttons = new ArrayList<>();
        for (String rawBtn : rawButtons) {
            if (rawBtn.isBlank()) continue;
            String clean = rawBtn.replace("(", "").replace(",", " ").trim();
            if (clean.isEmpty()) {
                buttons.add(new ArrayList<>());
                continue;
            }
            List<Integer> wiring = Arrays.stream(clean.split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            buttons.add(wiring);
        }

        final String joltageReqsString = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
        joltageRequirements = Arrays.stream(joltageReqsString.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        buttonInfluences = new Boolean[buttons.size()][joltageRequirements.size()];
        for (int i = 0; i < buttons.size(); i++) {
            final Boolean[] influence = new Boolean[joltageRequirements.size()];
            Arrays.fill(influence, false);
            final List<Integer> button = buttons.get(i);
            for (final int j : button) {
                if (j < influence.length) {
                    influence[j] = true;
                }
            }
            buttonInfluences[i] = influence;
        }
    }

    private String getBinaryRepresentation(final int i) {
        final StringBuilder sb = new StringBuilder(Integer.toBinaryString(i)).reverse();
        while (sb.length() < buttons.size()) {
            sb.append('0');
        }
        return sb.toString().substring(0, buttons.size());
    }
}
