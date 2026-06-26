package software.aoc.day08;

import java.util.ArrayList;
import java.util.List;

public class JunctionParser {
    public List<Point3D> parse(List<String> lines) {
        List<Point3D> points = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            points.add(new Point3D(
                    i,
                    Long.parseLong(parts[0].trim()),
                    Long.parseLong(parts[1].trim()),
                    Long.parseLong(parts[2].trim())
            ));
        }
        return points;
    }
}