package software.aoc.day08.b;

import software.aoc.day08.*;
import java.util.List;

public class PlaygroundCompleter {
    public long calculateLastConnectionXProduct(List<Point3D> junctions) {
        List<Connection> connections = GraphBuilder.buildSortedConnections(junctions);
        CircuitUnionFind dsu = new CircuitUnionFind(junctions.size());
        int isolatedCircuits = junctions.size();

        for (Connection conn : connections) {
            if (dsu.union(conn.id1(), conn.id2())) {
                if (--isolatedCircuits == 1) {
                    return (long) junctions.get(conn.id1()).x() * junctions.get(conn.id2()).x();
                }
            }
        }
        throw new IllegalStateException("No se pudo conectar todo el grafo.");
    }
}