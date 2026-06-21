package software.aoc.day09;

import java.util.List;

public class MovieTheaterManager {
    private final AreaOptimizationStrategy strategy;

    public MovieTheaterManager(AreaOptimizationStrategy strategy) {
        this.strategy = strategy;
    }

    public long calculateOptimalArea(List<Tile> tiles) {
        return strategy.optimize(tiles);
    }
}