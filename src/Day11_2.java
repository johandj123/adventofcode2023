import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day11_2 {
    private CharMatrix charMatrix;
    private List<Position> positions = new ArrayList<>();
    private Set<Long> emptyX = new HashSet<>();
    private Set<Long> emptyY = new HashSet<>();
    private List<Position> expandedPositions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day11_2().start();
    }

    private void start() throws IOException {
        charMatrix = new CharMatrix(InputUtil.readAsLines("input11.txt"), '.');
        determinePositions();
        second();
    }

    private void second()
    {
        long second = 0;
        for (int i = 0; i < expandedPositions.size(); i++) {
            for (int j = i + 1; j < expandedPositions.size(); j++) {
                second += expandedPositions.get(i).manhattanDistance(expandedPositions.get(j));
            }
        }
        System.out.println(second);
    }

    private void determinePositions() {
        for (int x = 0; x < charMatrix.getWidth(); x++) {
            emptyX.add((long)x);
        }
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            emptyY.add((long)y);
        }
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == '#') {
                    positions.add(new Position(x, y));
                    emptyX.remove((long)x);
                    emptyY.remove((long)y);
                }
            }
        }
        for (Position position : positions) {
            expandedPositions.add(new Position(
                    expand(emptyX, position.x),
                    expand(emptyY, position.y)
            ));
        }
    }

    private long expand(Set<Long> set, long value)
    {
        long result = value;
        for (long i = 0; i < value; i++) {
            if (set.contains(i)) {
                result += 999999L;
            }
        }
        return result;
    }

    static class Position
    {
        final long x;
        final long y;

        public Position(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        long manhattanDistance(Position position)
        {
            long dx = x - position.x;
            long dy = y - position.y;
            return Math.abs(dx) + Math.abs(dy);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }
}
