import lib.CharMatrix;
import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day16 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input16.txt"));

        first(charMatrix);
        second(charMatrix);
    }

    private static void first(CharMatrix charMatrix) {
        Beam startBeam = new Beam(charMatrix.new Position(-1, 0), 1, 0);
        System.out.println(countEnergized(startBeam));
    }

    private static void second(CharMatrix charMatrix) {
        List<Beam> startBeams = new ArrayList<>();

        for (int x = 0; x < charMatrix.getWidth(); x++) {
            startBeams.add(new Beam(charMatrix.new Position(x, -1), 0, 1));
            startBeams.add(new Beam(charMatrix.new Position(x, charMatrix.getHeight()), 0, -1));
        }

        for (int y = 0; y < charMatrix.getHeight(); y++) {
            startBeams.add(new Beam(charMatrix.new Position(-1, y), 1, 0));
            startBeams.add(new Beam(charMatrix.new Position(charMatrix.getWidth(), y), -1, 0));
        }

        int result = startBeams.stream()
                .mapToInt(Day16::countEnergized)
                .max()
                .orElseThrow();
        System.out.println(result);
    }

    private static int countEnergized(Beam startBeam) {
        Set<Beam> seen = GraphUtil.reachable(startBeam, Beam::getNext);

        Set<CharMatrix.Position> positions = seen.stream()
                .map(beam -> beam.position)
                .filter(CharMatrix.Position::isValid)
                .collect(Collectors.toSet());
        return positions.size();
    }

    static class Beam
    {
        final CharMatrix.Position position;
        final int dx;
        final int dy;

        public Beam(CharMatrix.Position position, int dx, int dy) {
            this.position = position;
            this.dx = dx;
            this.dy = dy;
        }

        List<Beam> getNext()
        {
            List<Beam> result = new ArrayList<>();
            CharMatrix.Position nextPosition = position.add(dx, dy);
            if (nextPosition.isValid()) {
                char c = nextPosition.get();
                switch (c) {
                    case '.':
                        result.add(new Beam(nextPosition, dx, dy));
                        break;
                    case '|':
                        if (dx != 0) {
                            result.add(new Beam(nextPosition, 0, -1));
                            result.add(new Beam(nextPosition, 0, 1));
                        } else {
                            result.add(new Beam(nextPosition, dx, dy));
                        }
                        break;
                    case '-':
                        if (dy != 0) {
                            result.add(new Beam(nextPosition, -1, 0));
                            result.add(new Beam(nextPosition, 1, 0));
                        } else {
                            result.add(new Beam(nextPosition, dx, dy));
                        }
                        break;
                    case '/':
                        result.add(new Beam(nextPosition, -dy, -dx));
                        break;
                    case '\\':
                        result.add(new Beam(nextPosition, dy, dx));
                        break;
                }
            }

            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beam beam = (Beam) o;
            return dx == beam.dx && dy == beam.dy && Objects.equals(position, beam.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, dx, dy);
        }
    }
}
