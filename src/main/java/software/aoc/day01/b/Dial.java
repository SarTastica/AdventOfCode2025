package software.aoc.day01.b;

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
        applyRotation(amount, -1);
    }

    @Override
    public void rotateRight(int amount) {
        applyRotation(amount, 1);
    }

    private void applyRotation(int amount, int direction) {
        for (int i = 0; i < amount; i++) {
            this.currentPosition = Math.floorMod(this.currentPosition + direction, MAX_POSITIONS);

            if (this.currentPosition == 0) {
                this.zeroHits++;
            }
        }
    }

    @Override
    public int getZeroHits() {
        return zeroHits;
    }
}