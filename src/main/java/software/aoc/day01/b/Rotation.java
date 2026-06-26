package software.aoc.day01.b;

public record Rotation(int direction, int amount) {
    public static Rotation from(String order) {
        int dir = order.charAt(0) == 'L' ? -1 : 1;
        int val = Integer.parseInt(order.substring(1));
        return new Rotation(dir, val);
    }
}