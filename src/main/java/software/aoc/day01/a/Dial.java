package software.aoc.day01.a;

import software.aoc.day01.CajaFuerte;

public class Dial implements CajaFuerte {
    private int currentPosition;
    private int zeroHits;

    private static final int MAX_POSITIONS = 100;
    private static final int START_POSITION = 50;

    public Dial() {
        this.currentPosition = START_POSITION;
        this.zeroHits = 0;
    }

    @Override
    public void rotateLeft(int amount) {
        applyRotation(-amount);
    }

    @Override
    public void rotateRight(int amount) {
        applyRotation(amount);
    }

    private void applyRotation(int movement) {
        this.currentPosition = Math.floorMod(this.currentPosition + movement, MAX_POSITIONS);

        if (this.currentPosition == 0) {
            this.zeroHits++;
        }
    }

    @Override
    public int getZeroHits() { return zeroHits; }
}