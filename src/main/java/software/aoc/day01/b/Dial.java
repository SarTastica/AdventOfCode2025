package software.aoc.day01.b;

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
        int tempPosition = this.currentPosition;
        int hitsInThisMove = 0;

        for (int i = 0; i < rotation.amount(); i++) {
            tempPosition = Math.floorMod(tempPosition + rotation.direction(), MAX_POSITIONS);
            if (tempPosition == 0) {
                hitsInThisMove++;
            }
        }

        return new Dial(tempPosition, this.zeroHits + hitsInThisMove);
    }
}