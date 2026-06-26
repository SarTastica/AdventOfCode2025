package software.aoc.day11;

import java.util.List;
import java.util.Map;

public class NetworkGraph {
    private final Map<String, List<String>> adjacencyList;

    public NetworkGraph(Map<String, List<String>> adjacencyList) {
        this.adjacencyList = Map.copyOf(adjacencyList);
    }

    public List<String> getNeighbors(String nodeId) {
        return adjacencyList.getOrDefault(nodeId, List.of());
    }
}