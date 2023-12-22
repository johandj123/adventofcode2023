import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day22 {
    public static void main(String[] args) throws IOException {
        List<Brick> bricks = InputUtil.readAsLines("input22.txt")
                .stream()
                .map(Brick::new)
                .collect(Collectors.toList());
        for (int i = 0; i < bricks.size(); i++) {
            bricks.get(i).name = "" + (char)('A' + i);
        }
        while (true) {
            Map<Position, Brick> placed = calcPlaced(bricks);
            List<Brick> f = bricks.stream().filter(brick -> brick.canFall(placed)).collect(Collectors.toList());
            if (f.isEmpty()) break;
            f.forEach(Brick::fall);
        }
        Map<Position, Brick> placed = calcPlaced(bricks);
        Map<Brick, Set<Brick>> supportedBy = calcSupportedBy(bricks, placed);
        Map<Brick, Set<Brick>> supports = calcSupports(supportedBy);
        Set<Brick> first = new HashSet<>(bricks);
        for (var entry : supportedBy.entrySet()) {
            if (entry.getValue().size() == 1) {
                first.remove(entry.getValue().stream().findFirst().orElseThrow());
            }
        }
        System.out.println(first.size());

        int second = 0;
        for (Brick brick : bricks) {
            second += wouldFall(brick, bricks);
        }
        System.out.println(second);
    }

    private static int wouldFall(Brick removedBrick, List<Brick> bricks) {
        bricks = bricks.stream().filter(brick -> brick != removedBrick).map(Brick::new).collect(Collectors.toList());
        Set<Brick> fallen = new HashSet<>();
        while (true) {
            Map<Position, Brick> placed = calcPlaced(bricks);
            List<Brick> f = bricks.stream().filter(brick -> brick.canFall(placed)).collect(Collectors.toList());
            if (f.isEmpty()) break;
            f.forEach(Brick::fall);
            fallen.addAll(f);
        }
        return fallen.size();
    }

    private static Map<Position, Brick> calcPlaced(List<Brick> bricks) {
        Map<Position, Brick> placed = new HashMap<>();
        bricks.forEach(brick -> brick.fill(placed));
        return placed;
    }

    private static Map<Brick, Set<Brick>> calcSupportedBy(List<Brick> bricks, Map<Position, Brick> placed) {
        return bricks.stream()
                .collect(Collectors.toMap(brick -> brick, brick -> brick.supportedBy(placed)));
    }

    private static Map<Brick, Set<Brick>> calcSupports(Map<Brick, Set<Brick>> supportedBy) {
        Map<Brick, Set<Brick>> result = new HashMap<>();
        for (var entry : supportedBy.entrySet()) {
            Brick brick = entry.getKey();
            for (Brick brick2 : entry.getValue()) {
                result.computeIfAbsent(brick2, key -> new HashSet<>()).add(brick);
            }
        }
        return result;
    }

    static class Brick {
        String name;
        Position a;
        Position b;

        public Brick(Brick o) {
            this.name = o.name;
            this.a = o.a;
            this.b = o.b;
        }

        public Brick(String s) {
            String[] sp = s.split("~");
            a = new Position(sp[0]);
            b = new Position(sp[1]);
        }

        void fill(Map<Position, Brick> placed) {
            for (int z = a.z; z <= b.z; z++) {
                for (int y = a.y; y <= b.y; y++) {
                    for (int x = a.x; x <= b.x; x++) {
                        placed.put(new Position(x, y, z), this);
                    }
                }
            }
        }

        Set<Brick> supportedBy(Map<Position, Brick> placed) {
            Set<Brick> result = new HashSet<>();
            int z = a.z - 1;
            for (int y = a.y; y <= b.y; y++) {
                for (int x = a.x; x <= b.x; x++) {
                    Position position = new Position(x, y, z);
                    Brick e = placed.get(position);
                    if (e != null) {
                        result.add(e);
                    }
                }
            }
            return result;
        }

        boolean canFall(Map<Position, Brick> placed) {
            return a.z > 1 && supportedBy(placed).isEmpty();
        }

        void fall() {
            a = new Position(a.x, a.y, a.z - 1);
            b = new Position(b.x, b.y, b.z - 1);
        }

        boolean free(Set<Position> placed) {
            int z = b.z + 1;
            for (int y = a.y; y <= b.y; y++) {
                for (int x = a.x; x <= b.x; x++) {
                    Position position = new Position(x, y, z);
                    if (placed.contains(position)) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "Brick{" +
                    "name='" + name + '\'' +
                    ", a=" + a +
                    ", b=" + b +
                    '}';
        }
    }

    static class Position {
        final int x;
        final int y;
        final int z;

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Position(String s) {
            String[] sp = s.split(",");
            x = Integer.parseInt(sp[0]);
            y = Integer.parseInt(sp[1]);
            z = Integer.parseInt(sp[2]);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y && z == position.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }
}
