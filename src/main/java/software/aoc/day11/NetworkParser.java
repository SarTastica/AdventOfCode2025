package software.aoc.day11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkParser {
    public NetworkGraph parse(List<String> lines) {
        Map<String, List<String>> tempAdjList = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(": ");
            String source = parts[0];
            List<String> destinations = parts.length > 1
                    ? Arrays.asList(parts[1].split(" "))
                    : List.of();

            tempAdjList.put(source, destinations);
        }

        return new NetworkGraph(tempAdjList);
    }
}