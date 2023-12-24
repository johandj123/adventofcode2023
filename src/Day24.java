import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<Line> lines = InputUtil.readAsLines("input24a.txt")
                .stream()
                .map(Line::new)
                .collect(Collectors.toList());
        first(lines);
        // Second part in 24_2 subproject!
    }

    private static void first(List<Line> lines) {
        int first = 0;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                if (lines.get(i).intersect(lines.get(j), 200000000000000L, 400000000000000L)) {
                    first++;
                }
            }
        }
        System.out.println(first);
    }

    private static void second(List<Line> lines) {
        for (Line line : lines) {
            System.out.println("lines.append((" + line.start + "," + line.v + "))");
        }
    }

    static class Line {
        Vector start;
        Vector v;

        Line(String input) {
            String[] sp = input.trim().split("@");
            start = new Vector(sp[0]);
            v = new Vector(sp[1]);
        }

        boolean intersect(Line o, long t1, long t2)
        {
            double x1 = start.x;
            double y1 = start.y;
            double x2_x1 = v.x;
            double y2_y1 = v.y;

            double x3 = o.start.x;
            double y3 = o.start.y;
            double x4_x3 = o.v.x;
            double y4_y3 = o.v.y;

            double div = det(
                    -x2_x1,
                    -x4_x3,
                    -y2_y1,
                    -y4_y3
            );
            double t = det(
                x1 - x3,
                    -x4_x3,
                    y1 - y3,
                    -y4_y3) / div;
            double u = det(
                    x1 - x3,
                    -x2_x1,
                    y1 - y3,
                    -y2_y1) / div;
            if (t >= 0.0 && u >= 0.0) {
                double x = x1 + t * x2_x1;
                double y = y1 + t * y2_y1;
                return in(x, t1, t2) && in(y, t1, t2);
            }

            return false;
        }

        boolean in(double x, double t1, double t2) {
            return x >= t1 && x <= t2;
        }

        double det(double a,double b,double c,double d) {
            return a * d - b * c;
        }
    }

    static class Vector {
        long x;
        long y;
        long z;

        Vector(String input) {
            String[] sp = input.trim().split(",");
            x = Long.parseLong(sp[0].trim());
            y = Long.parseLong(sp[1].trim());
            z = Long.parseLong(sp[2].trim());
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d)", x, y, z);
        }
    }
}
