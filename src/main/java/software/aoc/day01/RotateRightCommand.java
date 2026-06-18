package software.aoc.day01;

import software.aoc.day01.CajaFuerte;
import software.aoc.day01.Command;

public class RotateRightCommand implements Command {
    private final int amount;

    public RotateRightCommand(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(CajaFuerte caja) {
        caja.rotateRight(amount);
    }
}