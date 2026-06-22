package software.aoc.day11;

public interface PathCounterStrategy {
    long countPaths(NetworkGraph graph, String startNode, String targetNode);
}