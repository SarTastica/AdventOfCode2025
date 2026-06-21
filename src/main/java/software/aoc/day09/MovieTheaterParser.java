package software.aoc.day09;

import java.util.List;

public class MovieTheaterParser {
    public List<Tile> parse(List<String> lines) {
        return lines.stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Tile(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                })
                .toList();
    }
}