import lib.BigRational;
import lib.InputUtil;
import lib.MathUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<Line> lines = InputUtil.readAsLines("input24.txt")
                .stream()
                .map(Line::new)
                .collect(Collectors.toList());
        first(lines);
        second(lines);
        // SMT solution in 24_2 subproject!
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
        long x1 = lines.get(0).start.x;
        long y1 = lines.get(0).start.y;
        long z1 = lines.get(0).start.z;
        long vx1 = lines.get(0).v.x;
        long vy1 = lines.get(0).v.y;
        long vz1 = lines.get(0).v.z;
        long x2 = lines.get(1).start.x;
        long y2 = lines.get(1).start.y;
        long z2 = lines.get(1).start.z;
        long vx2 = lines.get(1).v.x;
        long vy2 = lines.get(1).v.y;
        long vz2 = lines.get(1).v.z;
        long x3 = lines.get(2).start.x;
        long y3 = lines.get(2).start.y;
        long z3 = lines.get(2).start.z;
        long vx3 = lines.get(2).v.x;
        long vy3 = lines.get(2).v.y;
        long vz3 = lines.get(2).v.z;
        long[][] factors = new long[6][7];
        // a
        factors[0][0] = 0;
        factors[1][0] = vz1 - vz2;
        factors[2][0] = vy2 - vy1;
        factors[3][0] = 0;
        factors[4][0] = vz1 - vz3;
        factors[5][0] = vy3 - vy1;
        // b
        factors[0][1] = vz2 - vz1;
        factors[1][1] = 0;
        factors[2][1] = vx1 - vx2;
        factors[3][1] = vz3 - vz1;
        factors[4][1] = 0;
        factors[5][1] = vx1 - vx3;
        // c
        factors[0][2] = vy1 - vy2;
        factors[1][2] = vx2 - vx1;
        factors[2][2] = 0;
        factors[3][2] = vy1 - vy3;
        factors[4][2] = vx3 - vx1;
        factors[5][2] = 0;
        // d
        factors[0][3] = 0;
        factors[1][3] = z2 - z1;
        factors[2][3] = y1 - y2;
        factors[3][3] = 0;
        factors[4][3] = z3 - z1;
        factors[5][3] = y1 - y3;
        // e
        factors[0][4] = z1 - z2;
        factors[1][4] = 0;
        factors[2][4] = x2 - x1;
        factors[3][4] = z1 - z3;
        factors[4][4] = 0;
        factors[5][4] = x3 - x1;
        // f
        factors[0][5] = y2 - y1;
        factors[1][5] = x1 - x2;
        factors[2][5] = 0;
        factors[3][5] = y3 - y1;
        factors[4][5] = x1 - x3;
        factors[5][5] = 0;
        // g
        factors[0][6] = -(y1 * vz1 - z1 * vy1 - y2 * vz2 + z2 * vy2);
        factors[1][6] = -(z1 * vx1 - x1 * vz1 - z2 * vx2 + x2 * vz2);
        factors[2][6] = -(x1 * vy1 - y1 * vx1 - x2 * vy2 + y2 * vx2);
        factors[3][6] = -(y1 * vz1 - z1 * vy1 - y3 * vz3 + z3 * vy3);
        factors[4][6] = -(z1 * vx1 - x1 * vz1 - z3 * vx3 + x3 * vz3);
        factors[5][6] = -(x1 * vy1 - y1 * vx1 - x3 * vy3 + y3 * vx3);

        BigRational[][] A = new BigRational[6][7];
        for (int i = 0; i < factors.length; i++) {
            for (int j = 0; j < factors[i].length; j++) {
                A[i][j] = new BigRational(factors[i][j]);
            }
        }
        MathUtil.gaussianElimination(A);
        System.out.println(A[0][6].longValueExact() + A[1][6].longValueExact() + A[2][6].longValueExact());
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
