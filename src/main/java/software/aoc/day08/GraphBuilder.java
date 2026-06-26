package software.aoc.day08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphBuilder {
    public static List<Connection> buildSortedConnections(List<Point3D> junctions) {
        List<Connection> connections = new ArrayList<>();
        int n = junctions.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                connections.add(new Connection(i, j, junctions.get(i).distanceSquaredTo(junctions.get(j))));
            }
        }
        Collections.sort(connections);
        return connections;
    }
}