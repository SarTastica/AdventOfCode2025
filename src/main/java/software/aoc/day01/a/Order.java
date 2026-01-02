package software.aoc.day01.a;

public class Order {
    private final char direction;
    private final int amount;

    public Order(String rawOrder) {
        if (rawOrder == null || rawOrder.isBlank()) {
            throw new IllegalArgumentException("La orden no puede estar vacía.");
        }
        this.direction = rawOrder.charAt(0);
        this.amount = Integer.parseInt(rawOrder.substring(1));
    }

    public char getDirection() {
        return direction;
    }

    public int getAmount() {
        return amount;
    }
}
