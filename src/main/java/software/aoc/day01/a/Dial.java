package software.aoc.day01.a;

public class Dial {
    private int currentPosition;
    private int zeroHits;
    private static final int MAX_POSITIONS = 100;

    public Dial() {
        this.currentPosition = 50;
        this.zeroHits = 0;
    }

    public void apply(Order order) {
        if (order.getDirection() == 'L') {
            this.currentPosition -= order.getAmount();
        } else if (order.getDirection() == 'R') {
            this.currentPosition += order.getAmount();
        }
        this.currentPosition = Math.floorMod(this.currentPosition, MAX_POSITIONS);

        if (this.currentPosition == 0) {
            this.zeroHits++;
        }
    }

    public int getZeroHits() {
        return zeroHits;
    }
}
