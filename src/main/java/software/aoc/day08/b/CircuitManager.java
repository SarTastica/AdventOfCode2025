package software.aoc.day08.b;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CircuitManager {

    private static class Connection implements Comparable<Connection> {
        int indexA;
        int indexB;
        double distance;

        public Connection(int indexA, int indexB, double distance) {
            this.indexA = indexA;
            this.indexB = indexB;
            this.distance = distance;
        }

        @Override
        public int compareTo(Connection other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public long findLastConnectionScore(List<String> lines) {
        List<Point3D> points = lines.stream()
                .map(Point3D::new)
                .collect(Collectors.toList());
        int n = points.size();

        List<Connection> allConnections = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = points.get(i).distanceTo(points.get(j));
                allConnections.add(new Connection(i, j, dist));
            }
        }

        Collections.sort(allConnections);

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int numComponents = n;

        for (Connection conn : allConnections) {
            int rootA = find(parent, conn.indexA);
            int rootB = find(parent, conn.indexB);
            if (rootA != rootB) {
                parent[rootA] = rootB;
                numComponents--;
                if (numComponents == 1) {
                    Point3D p1 = points.get(conn.indexA);
                    Point3D p2 = points.get(conn.indexB);
                    return p1.getX() * p2.getX();
                }
            }
        }

        return 0;
    }

    private int find(int[] parent, int i) {
        if (parent[i] == i) return i;
        parent[i] = find(parent, parent[i]);
        return parent[i];
    }
}