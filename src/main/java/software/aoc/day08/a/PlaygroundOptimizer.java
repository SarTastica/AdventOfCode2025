package software.aoc.day08.a;

import software.aoc.day08.CircuitUnionFind;
import software.aoc.day08.Connection;
import software.aoc.day08.Point3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaygroundOptimizer {

    public long calculateLargestCircuitsMetric(List<Point3D> junctions, int maxConnections) {
        List<Connection> connections = new ArrayList<>();
        int n = junctions.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long distSq = junctions.get(i).distanceSquaredTo(junctions.get(j));
                connections.add(new Connection(i, j, distSq));
            }
        }

        Collections.sort(connections);
        CircuitUnionFind dsu = new CircuitUnionFind(n);

        for (int i = 0; i < maxConnections && i < connections.size(); i++) {
            Connection conn = connections.get(i);
            dsu.union(conn.id1(), conn.id2());
        }

        List<Integer> circuitSizes = dsu.getCircuitSizes();
        circuitSizes.sort(Collections.reverseOrder());

        long result = 1;
        for (int i = 0; i < Math.min(3, circuitSizes.size()); i++) {
            result *= circuitSizes.get(i);
        }

        return result;
    }
}