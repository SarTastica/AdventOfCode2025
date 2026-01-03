package software.aoc.day08.a;

import java.util.*;
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

    public long calculateCircuitScore(List<String> lines) {
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

        int limit = Math.min(1000, allConnections.size());
        for (int i = 0; i < limit; i++) {
            Connection conn = allConnections.get(i);
            union(parent, conn.indexA, conn.indexB);
        }

        Map<Integer, Integer> groupSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(parent, i);
            groupSizes.put(root, groupSizes.getOrDefault(root, 0) + 1);
        }

        return groupSizes.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(1, (a, b) -> a * b);
    }

    private int find(int[] parent, int i) {
        if (parent[i] == i) {
            return i;
        }
        parent[i] = find(parent, parent[i]);
        return parent[i];
    }

    private void union(int[] parent, int i, int j) {
        int rootA = find(parent, i);
        int rootB = find(parent, j);
        if (rootA != rootB) {
            parent[rootA] = rootB;
        }
    }
}
