package software.aoc.day11.a;

import software.aoc.day11.NetworkGraph;
import software.aoc.day11.PathCounterStrategy;

import java.util.HashMap;
import java.util.Map;

public class MemoizedDfsPathCounter implements PathCounterStrategy {

    @Override
    public long countPaths(NetworkGraph graph, String startNode, String targetNode) {
        return dfs(graph, startNode, targetNode, new HashMap<>());
    }

    private long dfs(NetworkGraph graph, String current, String target, Map<String, Long> memo) {
        if (current.equals(target)) {
            return 1;
        }

        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long totalPaths = 0;
        for (String neighbor : graph.getNeighbors(current)) {
            totalPaths += dfs(graph, neighbor, target, memo);
        }

        memo.put(current, totalPaths);
        return totalPaths;
    }
}