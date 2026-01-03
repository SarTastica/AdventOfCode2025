package software.aoc.day11.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactorManager {

    private final Map<String, List<String>> adjList = new HashMap<>();

    private final Map<String, Long> memo = new HashMap<>();

    public long countPaths(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(":");
            String source = parts[0].trim();

            if (parts.length > 1) {
                String[] destinations = parts[1].trim().split("\\s+");
                adjList.put(source, Arrays.asList(destinations));
            } else {
                adjList.put(source, new ArrayList<>());
            }
        }
        memo.clear();
        return dfs("you");
    }

    private long dfs(String current) {
        if ("out".equals(current)) {
            return 1;
        }

        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long totalPaths = 0;

        if (adjList.containsKey(current)) {
            for (String neighbor : adjList.get(current)) {
                totalPaths += dfs(neighbor);
            }
        }
        memo.put(current, totalPaths);
        return totalPaths;
    }
}
