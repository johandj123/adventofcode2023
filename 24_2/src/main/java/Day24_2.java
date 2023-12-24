import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.api.NumeralFormula.IntegerFormula;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day24_2 {
    IntegerFormulaManager imgr;
    BooleanFormulaManager bmgr;

    public static void main(String[] args) throws Exception {
        new Day24_2().start();
    }

    private void start() throws Exception {
        List<Line> lines = InputUtil.readAsLines("input24.txt")
                .stream()
                .map(Line::new)
                .collect(Collectors.toList());

        try (SolverContext context = SolverContextFactory.createSolverContext(SolverContextFactory.Solvers.PRINCESS)) {
            imgr = context.getFormulaManager().getIntegerFormulaManager();
            bmgr = context.getFormulaManager().getBooleanFormulaManager();

            IntegerFormula rx = imgr.makeVariable("rx");
            IntegerFormula ry = imgr.makeVariable("ry");
            IntegerFormula rz = imgr.makeVariable("rz");
            IntegerFormula rvx = imgr.makeVariable("rvx");
            IntegerFormula rvy = imgr.makeVariable("rvy");
            IntegerFormula rvz = imgr.makeVariable("rvz");

            List<BooleanFormula> constraints = new ArrayList<>();
            int i = 0;
            for (Line line : lines) {
                IntegerFormula t = imgr.makeVariable("t" + i);
                constraints.add(imgr.greaterOrEquals(t, imgr.makeNumber(0)));
                constraints.add(imgr.equal(imgr.add(imgr.makeNumber(line.start.x), imgr.multiply(t, imgr.makeNumber(line.v.x))), imgr.add(rx, imgr.multiply(t, rvx))));
                constraints.add(imgr.equal(imgr.add(imgr.makeNumber(line.start.y), imgr.multiply(t, imgr.makeNumber(line.v.y))), imgr.add(ry, imgr.multiply(t, rvy))));
                constraints.add(imgr.equal(imgr.add(imgr.makeNumber(line.start.z), imgr.multiply(t, imgr.makeNumber(line.v.z))), imgr.add(rz, imgr.multiply(t, rvz))));
                i++;
            }

            try (ProverEnvironment prover = context.newProverEnvironment(SolverContext.ProverOptions.GENERATE_MODELS)) {
                for (BooleanFormula constraint : constraints) {
                    prover.addConstraint(constraint);
                }
                if (prover.isUnsat()) {
                    System.out.println("Unsat");
                }

                try (Model model = prover.getModel()) {
                    printResult(model, rx, "rx");
                    printResult(model, ry, "ry");
                    printResult(model, rz, "rz");
                    BigInteger result = model.evaluate(rx).add(model.evaluate(ry)).add(model.evaluate(rz));
                    System.out.println(result);
                }
            }
        }
    }

    private static void printResult(Model model, IntegerFormula formula, String name) {
        System.out.println(name + ":" + model.evaluate(formula));
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
