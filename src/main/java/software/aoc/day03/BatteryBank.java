package software.aoc.day03;

public record BatteryBank(String digits) {
    public int length() {
        return digits.length();
    }
}