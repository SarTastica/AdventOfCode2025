package software.aoc.day08.b;

import software.aoc.day08.CircuitUnionFind;
import software.aoc.day08.Connection;
import software.aoc.day08.Point3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaygroundCompleter {

    public long calculateLastConnectionXProduct(List<Point3D> junctions) {
        int n = junctions.size();
        List<Connection> connections = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long distSq = junctions.get(i).distanceSquaredTo(junctions.get(j));
                connections.add(new Connection(i, j, distSq));
            }
        }
        Collections.sort(connections);
        CircuitUnionFind dsu = new CircuitUnionFind(n);
        int isolatedCircuits = n;

        for (Connection conn : connections) {
            if (dsu.union(conn.id1(), conn.id2())) {
                isolatedCircuits--;

                if (isolatedCircuits == 1) {
                    Point3D p1 = junctions.get(conn.id1());
                    Point3D p2 = junctions.get(conn.id2());

                    return p1.x() * p2.x();
                }
            }
        }

        throw new IllegalStateException("No fue posble conectar todo el grafo.");
    }
}