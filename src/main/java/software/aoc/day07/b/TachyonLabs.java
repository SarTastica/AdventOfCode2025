package software.aoc.day07.b;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TachyonLabs {

    private final char[][] grid;
    private final int rows;
    private final int cols;

    public TachyonLabs(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            this.grid[i] = lines.get(i).toCharArray();
        }
    }

    public long countTimelines() {
        Map<Integer, Long> activeTimelines = new HashMap<>();

        for (int c = 0; c < cols; c++) {
            if (grid[0][c] == 'S') {
                activeTimelines.put(c, 1L);
                break;
            }
        }

        // 2. Simulación Fila a Fila
        for (int r = 0; r < rows; r++) {
            Map<Integer, Long> nextTimelines = new HashMap<>();

            for (Map.Entry<Integer, Long> entry : activeTimelines.entrySet()) {
                int col = entry.getKey();
                long count = entry.getValue();

                char cell = grid[r][col];

                if (cell == '^') {
                    if (isValid(col - 1)) {
                        addTimelines(nextTimelines, col - 1, count);
                    }
                    if (isValid(col + 1)) {
                        addTimelines(nextTimelines, col + 1, count);
                    }
                } else {
                    addTimelines(nextTimelines, col, count);
                }
            }

            activeTimelines = nextTimelines;

            if (activeTimelines.isEmpty()) break;
        }

        return activeTimelines.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    private void addTimelines(Map<Integer, Long> map, int col, long count) {
        map.merge(col, count, Long::sum);
    }

    private boolean isValid(int col) {
        return col >= 0 && col < cols;
    }
}
