package software.aoc.day01;

import software.aoc.day01.CajaFuerte;
import software.aoc.day01.Command;

public class RotateLeftCommand implements Command {
    private final int amount;

    public RotateLeftCommand(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(CajaFuerte caja) {
        caja.rotateLeft(amount);
    }
}