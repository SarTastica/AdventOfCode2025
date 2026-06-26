package software.aoc.day07;

public enum TachyonCell {
    EMPTY('.'),
    BEAM('S'),
    SPLITTER('^'),
    OUT_OF_BOUNDS('X');

    private final char symbol;

    TachyonCell(char symbol) {
        this.symbol = symbol;
    }

    public static TachyonCell from(char c) {
        return switch (c) {
            case 'S' -> BEAM;
            case '^' -> SPLITTER;
            default -> EMPTY;
        };
    }
}