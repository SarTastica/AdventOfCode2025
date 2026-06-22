package software.aoc.day10.a;

import software.aoc.day10.Machine;
import software.aoc.day10.MachineSolver; //corregir eso que daba mas clases

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BfsInitializationOptimizer implements MachineSolver {

    private record State(int mask, int steps) {}

    @Override
    public long solve(Machine machine) {
        Queue<State> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(new State(0, 0));
        visited.add(0);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.mask() == machine.targetMask()) {
                return current.steps();
            }

            for (int buttonMask : machine.buttonMasks()) {
                int nextMask = current.mask() ^ buttonMask;
                if (visited.add(nextMask)) {
                    queue.add(new State(nextMask, current.steps() + 1));
                }
            }
        }

        throw new IllegalStateException("Imposible inicializar la máquina con la configuración actual");
    }
}