package software.aoc.day05;

import java.util.List;

public class CafeteriaManager {
    private final FreshnessRule rule;

    public CafeteriaManager(FreshnessRule rule) {
        this.rule = rule;
    }

    public long countFreshIngredients(List<Long> availableIds) {
        return availableIds.stream()
                .filter(rule::isFresh)
                .count();
    }
}