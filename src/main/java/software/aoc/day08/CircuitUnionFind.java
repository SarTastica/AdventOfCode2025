package software.aoc.day08;

import java.util.ArrayList;
import java.util.List;

public class CircuitUnionFind {
    private final int[] parent;
    private final int[] size;

    public CircuitUnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }
    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI == rootJ) {
            return false;
        }
        if (size[rootI] < size[rootJ]) {
            parent[rootI] = rootJ;
            size[rootJ] += size[rootI];
        } else {
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
        }
        return true;
    }

    public List<Integer> getCircuitSizes() {
        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) {
                sizes.add(size[i]);
            }
        }
        return sizes;
    }
}