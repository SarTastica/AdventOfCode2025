package software.aoc.day01.a;

import java.util.List;

public record Dial(int currentPosition, int zeroHits) {
    private static final int MAX_POSITIONS = 100;
    private static final int START_POSITION = 50;

    public Dial() {
        this(START_POSITION, 0);
    }

    public Dial executeAll(List<String> orders) {
        Dial current = this;
        for (String order : orders) {
            current = current.execute(Rotation.from(order));
        }
        return current;
    }

    private Dial execute(Rotation rotation) {
        int nextPosition = Math.floorMod(this.currentPosition + rotation.amount(), MAX_POSITIONS);

        int newHits = this.zeroHits + (nextPosition == 0 ? 1 : 0);

        return new Dial(nextPosition, newHits);
    }
}