package software.aoc.day01.a;

public record Rotation(int amount) {
    public static Rotation from(String order) {
        int value = Integer.parseInt(order.substring(1));
        return new Rotation(order.charAt(0) == 'L' ? -value : value);
    }
}