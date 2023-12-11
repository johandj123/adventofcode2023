import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day11 {
    private CharMatrix charMatrix;
    private List<Position> positions = new ArrayList<>();
    private Set<Integer> emptyX = new HashSet<>();
    private Set<Integer> emptyY = new HashSet<>();
    private List<Position> expandedPositions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new Day11().start();
    }

    private void start() throws IOException {
        charMatrix = new CharMatrix(InputUtil.readAsLines("input11.txt"), '.');
        determinePositions();
        first();
    }

    private void first()
    {
        int first = 0;
        for (int i = 0; i < expandedPositions.size(); i++) {
            for (int j = i + 1; j < expandedPositions.size(); j++) {
                first += expandedPositions.get(i).manhattanDistance(expandedPositions.get(j));
            }
        }
        System.out.println(first);
    }

    private void determinePositions() {
        for (int x = 0; x < charMatrix.getWidth(); x++) {
            emptyX.add(x);
        }
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            emptyY.add(y);
        }
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == '#') {
                    positions.add(new Position(x, y));
                    emptyX.remove(x);
                    emptyY.remove(y);
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

    private int expand(Set<Integer> set, int value)
    {
        int result = value;
        for (int i = 0; i < value; i++) {
            if (set.contains(i)) {
                result++;
            }
        }
        return result;
    }

    static class Position
    {
        final int x;
        final int y;

        public Position(int x, int y) {
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

        int manhattanDistance(Position position)
        {
            int dx = x - position.x;
            int dy = y - position.y;
            return Math.abs(dx) + Math.abs(dy);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }
}
