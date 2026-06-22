package software.aoc.day11.b;

import software.aoc.day11.NetworkGraph;
import software.aoc.day11.PathCounterStrategy;

import java.util.HashMap;
import java.util.Map;

public class MandatoryNodesPathCounter implements PathCounterStrategy {

    private final String required1;
    private final String required2;

    public MandatoryNodesPathCounter(String required1, String required2) {
        this.required1 = required1;
        this.required2 = required2;
    }
    private record State(String node, boolean seenReq1, boolean seenReq2) {}

    @Override
    public long countPaths(NetworkGraph graph, String startNode, String targetNode) {
        Map<State, Long> memo = new HashMap<>();
        return dfs(graph, startNode, targetNode, false, false, memo);
    }

    private long dfs(NetworkGraph graph, String current, String target, boolean seen1, boolean seen2, Map<State, Long> memo) {
        boolean nextSeen1 = seen1 || current.equals(required1);
        boolean nextSeen2 = seen2 || current.equals(required2);

        if (current.equals(target)) {
            return (nextSeen1 && nextSeen2) ? 1 : 0;
        }

        State state = new State(current, nextSeen1, nextSeen2);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        long totalPaths = 0;
        for (String neighbor : graph.getNeighbors(current)) {
            totalPaths += dfs(graph, neighbor, target, nextSeen1, nextSeen2, memo);
        }

        memo.put(state, totalPaths);
        return totalPaths;
    }
}