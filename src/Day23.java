import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day23 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input23.txt"));
        first(charMatrix);
        second(charMatrix); // Used -Xss1g VM option to increase the stack size
    }

    private static void first(CharMatrix charMatrix) {
        List<State> states = new ArrayList<>();
        states.add(new State(charMatrix.new Position(1, 0), charMatrix.getHeight(), charMatrix.getWidth()));
        int first = 0;
        while (!states.isEmpty()) {
            List<State> nextStates = new ArrayList<>();
            states.forEach(state -> nextStates.addAll(state.getNext()));
            states = nextStates;
            for (State state : states) {
                if (state.position.getY() == charMatrix.getHeight() - 1) {
                    first = Math.max(first, state.getSteps());
                }
            }
        }
        System.out.println(first);
    }

    private static void second(CharMatrix charMatrix) {
        int second = findmax(charMatrix, charMatrix.new Position(1, 0), 0);
        System.out.println(second);
    }

    private static int findmax(CharMatrix charMatrix, CharMatrix.Position position, int count) {
        if (!position.isValid()) {
            return Integer.MIN_VALUE;
        }

        char c = position.get();
        if (c == '#' || c == 'O') {
            return Integer.MIN_VALUE;
        }

        if (position.getY() == charMatrix.getHeight() - 1) {
            return count;
        }

        position.set('O');
        int result = Integer.MIN_VALUE;
        for (var position1 : position.getNeighbours()) {
            int value = findmax(charMatrix, position1, count + 1);
            result = Math.max(result, value);
        }
        position.set('.');
        return result;
    }

    static class State {
        final CharMatrix visited;
        final CharMatrix.Position position;

        public State(CharMatrix.Position start, int height, int width) {
            visited = new CharMatrix(height, width);
            position = start;
            visited.set(start.getX(), start.getY(), 'O');
        }

        public State(CharMatrix visited, CharMatrix.Position position) {
            this.visited = visited;
            this.position = position;
        }

        List<State> getNext() {
            List<State> result = new ArrayList<>();
            List<CharMatrix.Position> positions;
            char c = position.get();
            switch (c) {
                case '<':
                    positions = List.of(position.add(-1, 0));
                    break;
                case '>':
                    positions = List.of(position.add(1, 0));
                    break;
                case '^':
                    positions = List.of(position.add(0, -1));
                    break;
                case 'v':
                    positions = List.of(position.add(0, 1));
                    break;
                default:
                    positions = position.getNeighbours();
                    break;
            }
            for (var nextPosition : positions) {
                if (nextPosition.get() != '#' && visited.get(nextPosition.getX(), nextPosition.getY()) == '.') {
                    CharMatrix nextVisited = new CharMatrix(visited);
                    nextVisited.set(nextPosition.getX(), nextPosition.getY(), 'O');
                    result.add(new State(nextVisited, nextPosition));
                }
            }
            return result;
        }

        int getSteps() {
            int result = 0;
            for (int y = 0; y < visited.getHeight(); y++) {
                for (int x = 0; x < visited.getWidth(); x++) {
                    if (visited.get(x, y) == 'O') {
                        result++;
                    }
                }
            }
            return result - 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Objects.equals(visited, state.visited) && Objects.equals(position, state.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(visited, position);
        }
    }
}
