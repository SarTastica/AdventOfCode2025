package software.aoc.day01.b;

public class Dial {
    private int currentPosition;
    private int zeroHits;
    private static final int MAX_POSITIONS = 100;

    public Dial() {
        this.currentPosition = 50;
        this.zeroHits = 0;
    }

    public void apply(Order order) {
        // En lugar de calcular el final directamente, simulamos paso a paso
        // para detectar si pasamos por el 0 DURANTE la rotación.
        for (int i = 0; i < order.getAmount(); i++) {

            if (order.getDirection() == 'L') {
                this.currentPosition--;
                // Si bajamos de 0, damos la vuelta al 99
                if (this.currentPosition < 0) {
                    this.currentPosition = 99;
                }
            } else if (order.getDirection() == 'R') {
                this.currentPosition++;
                // Si subimos de 99, damos la vuelta al 0
                if (this.currentPosition >= MAX_POSITIONS) {
                    this.currentPosition = 0;
                }
            }

            // Comprobamos en CADA paso (click)
            if (this.currentPosition == 0) {
                this.zeroHits++;
            }
        }
    }

    public int getZeroHits() {
        return zeroHits;
    }
}
