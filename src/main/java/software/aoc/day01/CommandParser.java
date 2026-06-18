package software.aoc.day01;

public class CommandParser {

    private CommandParser() {}

    public static Command parse(String rawOrder) {
        if (rawOrder == null || rawOrder.length() < 2) {
            throw new IllegalArgumentException("Comando inválido: " + rawOrder);
        }

        char direction = rawOrder.charAt(0);
        int amount = Integer.parseInt(rawOrder.substring(1));

        if (direction == 'L') {
            return new RotateLeftCommand(amount);
        } else if (direction == 'R') {
            return new RotateRightCommand(amount);
        } else {
            throw new IllegalArgumentException("Dirección desconocida: " + direction);
        }
    }
}