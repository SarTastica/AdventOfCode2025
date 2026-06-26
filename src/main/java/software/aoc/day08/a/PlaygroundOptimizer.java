package software.aoc.day08.a;

import software.aoc.day08.*;
import java.util.Collections;
import java.util.List;

public class PlaygroundOptimizer {
    public long calculateLargestCircuitsMetric(List<Point3D> junctions, int maxConnections) {
        List<Connection> connections = GraphBuilder.buildSortedConnections(junctions);
        CircuitUnionFind dsu = new CircuitUnionFind(junctions.size());

        for (int i = 0; i < Math.min(maxConnections, connections.size()); i++) {
            Connection conn = connections.get(i);
            dsu.union(conn.id1(), conn.id2());
        }

        List<Integer> circuitSizes = dsu.getCircuitSizes();
        circuitSizes.sort(Collections.reverseOrder());

        return circuitSizes.stream()
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(1, (a, b) -> a * b);
    }
}