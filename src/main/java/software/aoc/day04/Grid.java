package software.aoc.day04;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid {
    private final char[][] map;
    private final int rows;
    private final int cols;

    public Grid(List<String> lines) {
        this.rows = lines.size();
        this.cols = lines.get(0).length();
        this.map = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            this.map[r] = lines.get(r).toCharArray();
        }
    }

    private Grid(char[][] map, int rows, int cols) {
        this.map = map;
        this.rows = rows;
        this.cols = cols;
    }

    public char getCharAt(Position p) {
        if (p.row() < 0 || p.row() >= rows || p.col() < 0 || p.col() >= cols) {
            return '.';
        }
        return map[p.row()][p.col()];
    }

    public Stream<Position> getRollPositions() {
        return IntStream.range(0, rows).boxed()
                .flatMap(r -> IntStream.range(0, cols).mapToObj(c -> new Position(r, c)))
                .filter(p -> getCharAt(p) == '@');
    }

    public Grid removeRolls(List<Position> rollsToRemove) {
        char[][] newMap = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(this.map[r], 0, newMap[r], 0, cols);
        }

        for (Position p : rollsToRemove) {
            newMap[p.row()][p.col()] = '.';
        }

        return new Grid(newMap, rows, cols);
    }
}