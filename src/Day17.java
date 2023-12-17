import lib.CharMatrix;
import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Day17 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input17.txt"));
        CharMatrix.Position topLeft = charMatrix.new Position(0, 0);
        CharMatrix.Position bottomRight = charMatrix.new Position(charMatrix.getWidth() - 1, charMatrix.getHeight() - 1);

        int first = GraphUtil.dijkstra(new State(topLeft),
                State::getNext,
                state -> bottomRight.equals(state.position));
        System.out.println(first);

        int second = GraphUtil.dijkstra(new State(topLeft),
                State::getUltraNext,
                state -> bottomRight.equals(state.position) && state.sameDirectionCount >= 4);
        System.out.println(second);
    }

    static class State implements Comparable<State>
    {
        final CharMatrix.Position position;
        final int dx;
        final int dy;
        final int sameDirectionCount;

        public State(CharMatrix.Position position, int dx, int dy, int sameDirectionCount) {
            this.position = position;
            this.dx = dx;
            this.dy = dy;
            this.sameDirectionCount = sameDirectionCount;
        }

        public State(CharMatrix.Position position) {
            this(position, 0, 0, 0);
        }

        Map<State, Integer> getNext() {
            Map<State, Integer> result = new HashMap<>();
            addNext(result, -1, 0);
            addNext(result, 1, 0);
            addNext(result, 0, -1);
            addNext(result, 0, 1);
            return result;
        }

        void addNext(Map<State, Integer> result, int ndx, int ndy) {
            if (ndx == -dx && ndy == -dy) {
                return;
            }
            if (sameDirectionCount == 3 && ndx == dx && ndy == dy) {
                return;
            }
            int nSameDirectionCount = sameDirectionCount + 1;
            if (ndx != dx || ndy != dy) {
                nSameDirectionCount = 1;
            }
            CharMatrix.Position nPosition = position.add(ndx, ndy);
            if (nPosition.isValid()) {
                int heatLoss = nPosition.get() - '0';
                State nState = new State(nPosition, ndx, ndy, nSameDirectionCount);
                result.put(nState, heatLoss);
            }
        }

        Map<State, Integer> getUltraNext() {
            Map<State, Integer> result = new HashMap<>();
            addUltraNext(result, -1, 0);
            addUltraNext(result, 1, 0);
            addUltraNext(result, 0, -1);
            addUltraNext(result, 0, 1);
            return result;
        }

        void addUltraNext(Map<State, Integer> result, int ndx, int ndy) {
            if (ndx == -dx && ndy == -dy) {
                return;
            }
            if (sameDirectionCount > 0 && sameDirectionCount < 4 && (ndx != dx || ndy != dy)) {
                return;
            }
            if (sameDirectionCount == 10 && ndx == dx && ndy == dy) {
                return;
            }
            int nSameDirectionCount = sameDirectionCount + 1;
            if (ndx != dx || ndy != dy) {
                nSameDirectionCount = 1;
            }
            CharMatrix.Position nPosition = position.add(ndx, ndy);
            if (nPosition.isValid()) {
                int heatLoss = nPosition.get() - '0';
                State nState = new State(nPosition, ndx, ndy, nSameDirectionCount);
                result.put(nState, heatLoss);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return dx == state.dx && dy == state.dy && sameDirectionCount == state.sameDirectionCount && Objects.equals(position, state.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, dx, dy, sameDirectionCount);
        }

        @Override
        public String toString() {
            return "State{" +
                    "position=" + position +
                    ", dx=" + dx +
                    ", dy=" + dy +
                    ", sameDirectionCount=" + sameDirectionCount +
                    '}';
        }

        @Override
        public int compareTo(State o) {
            if (position.getX() < o.position.getX()) return -1;
            if (position.getX() > o.position.getX()) return 1;
            if (position.getY() < o.position.getY()) return -1;
            if (position.getY() > o.position.getY()) return 1;
            if (dx < o.dx) return -1;
            if (dx > o.dx) return 1;
            if (dy < o.dy) return -1;
            if (dy > o.dy) return 1;
            if (sameDirectionCount < o.sameDirectionCount) return -1;
            if (sameDirectionCount > o.sameDirectionCount) return 1;
            return 0;
        }
    }
}
