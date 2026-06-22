package software.aoc.day11;

public class ReactorManager {
    private final PathCounterStrategy strategy;

    public ReactorManager(PathCounterStrategy strategy) {
        this.strategy = strategy;
    }

    public long analyzeDataFlow(NetworkGraph graph, String startNode, String targetNode) {
        return strategy.countPaths(graph, startNode, targetNode);
    }
}