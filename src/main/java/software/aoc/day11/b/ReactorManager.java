package software.aoc.day11.b;

import java.util.*;

public class ReactorManager {

    private final Map<String, List<String>> adjList = new HashMap<>();
    private final Map<String, Long> memo = new HashMap<>();

    public long countPathsViaComponents(List<String> lines) {
        adjList.clear();
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

        long path1 = calculateSequentialPath("svr", "dac", "fft", "out");
        long path2 = calculateSequentialPath("svr", "fft", "dac", "out");
        return path1 + path2;
    }

    private long calculateSequentialPath(String start, String mid1, String mid2, String end) {
        long leg1 = countPaths(start, mid1);
        if (leg1 == 0) return 0;

        long leg2 = countPaths(mid1, mid2);
        if (leg2 == 0) return 0;

        long leg3 = countPaths(mid2, end);

        return leg1 * leg2 * leg3;
    }

    private long countPaths(String start, String end) {
        memo.clear();
        return dfs(start, end);
    }

    private long dfs(String current, String target) {
        if (current.equals(target)) {
            return 1;
        }
        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        long total = 0;
        if (adjList.containsKey(current)) {
            for (String neighbor : adjList.get(current)) {
                total += dfs(neighbor, target);
            }
        }

        memo.put(current, total);
        return total;
    }
}
