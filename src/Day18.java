import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day18 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input18.txt");
        first(input);
        second(input);
    }

    private static void first(List<String> input) {
        Position cur = new Position(0, 0);
        Set<Position> set = new HashSet<>();
        set.add(cur);
        for (String s : input) {
            String[] sp = s.split("\\s");
            int dx = "L".equals(sp[0]) ? -1 : ("R".equals(sp[0]) ? 1 : 0);
            int dy = "U".equals(sp[0]) ? -1 : ("D".equals(sp[0]) ? 1 : 0);
            int count = Integer.parseInt(sp[1]);
            for (int i = 0; i < count; i++) {
                cur = new Position(cur.x + dx, cur.y + dy);
                set.add(cur);
            }
        }
        int xmin = set.stream().min(Comparator.comparing(p -> p.x)).map(p -> p.x).orElseThrow();
        int ymin = set.stream().min(Comparator.comparing(p -> p.y)).map(p -> p.y).orElseThrow();
        int xmax = set.stream().max(Comparator.comparing(p -> p.x)).map(p -> p.x).orElseThrow();
        int ymax = set.stream().max(Comparator.comparing(p -> p.y)).map(p -> p.y).orElseThrow();
//        printset(xmin, ymin, ymax, xmax, set);
        floodfill(set, new Position(1, 1));
        System.out.println(set.size());
//        printset(xmin, ymin, ymax, xmax, set);
    }

    private static void second(List<String> input) {
        LPosition cur = new LPosition(0, 0);
        List<LPosition> list = new ArrayList<>();
        list.add(cur);
        long perim = 0;
        for (String s : input) {
            String h = s.substring(1 + s.indexOf('#'));
            long count = Long.parseLong(h.substring(0, 5), 16);
            String dir = h.substring(5, 6);
            int dx = "2".equals(dir) ? -1 : ("0".equals(dir) ? 1 : 0);
            int dy = "3".equals(dir) ? -1 : ("1".equals(dir) ? 1 : 0);
            perim += count;
            cur = new LPosition(cur.x + count * dx, cur.y + count * dy);
            list.add(cur);
        }
        long sum = 0;
        for (int i = 0; i < list.size() - 1; i += 2) {
            LPosition x0 = list.get(i);
            LPosition x1 = list.get(i + 1);
            LPosition x2 = list.get(i + 2);
            sum += (x1.x * (x2.y - x0.y) + x1.y * (x0.x - x2.x));
        }
        System.out.println(sum / 2 + perim / 2 + 1);
    }

    private static void floodfill(Set<Position> set, Position position) {
        Deque<Position> todo = new ArrayDeque<>();
        set.add(position);
        todo.add(position);
        while (!todo.isEmpty()) {
            Position cur = todo.remove();
            set.add(cur);
            List<Position> nei = List.of(new Position(cur.x + 1, cur.y),
                    new Position(cur.x - 1, cur.y),
                    new Position(cur.x, cur.y + 1),
                    new Position(cur.x, cur.y - 1));
            nei.stream()
                    .filter(n -> !set.contains(n))
                    .forEach(n -> {
                        todo.add(n);
                        set.add(n);
                    });
        }
    }

    private static void printset(int xmin,int ymin,int ymax, int xmax, Set<Position> set) {
        CharMatrix charMatrix = new CharMatrix(ymax - ymin + 1, xmax - xmin + 1);
        for (Position p : set) {
            charMatrix.set(p.x - xmin, p.y - ymin, '#');
        }
        System.out.println(charMatrix);
    }

    static class Position {
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

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    static class LPosition {
        final long x;
        final long y;

        public LPosition(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LPosition lPosition = (LPosition) o;
            return x == lPosition.x && y == lPosition.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "LPosition{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
