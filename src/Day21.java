import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day21 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input21.txt"));
        var start = charMatrix.find('S').orElseThrow();
        start.set('.');
        first(start);
        second(start, charMatrix.getWidth());
    }

    private static void first(CharMatrix.Position start) {
        Set<CharMatrix.Position> cur = new HashSet<>(List.of(start));
        for (int i = 0; i < 64; i++) {
            Set<CharMatrix.Position> next = new HashSet<>();
            for (var pos : cur) {
                for (var nei : pos.getNeighbours()) {
                    if (nei.isValid() && nei.get() == '.') {
                        next.add(nei);
                    }
                }
            }
            cur = next;
        }
        System.out.println(cur.size());
    }

    private static void second(CharMatrix.Position start, int size) {
        Set<CharMatrix.Position> cur = new HashSet<>(List.of(start));
        List<Long> counts = new ArrayList<>();
        List<Long> deltas = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Set<CharMatrix.Position> next = new HashSet<>();
            for (var pos : cur) {
                for (var nei : pos.getUnboundedNeighbours()) {
                    if (nei.getWrap() == '.') {
                        next.add(nei);
                    }
                }
            }
            deltas.add((long)(next.size() - cur.size()));
            cur = next;
            counts.add((long) next.size());
        }
        System.out.println(cur.size());

        System.out.println(deltas);
        List<Long> deltadelta = new ArrayList<>();
        for (int i = size; i < deltas.size(); i++) {
            deltadelta.add(deltas.get(i) - deltas.get(i - size));
        }
        System.out.println(deltadelta);

        int period = size;
        while (counts.size() < 26501365) {
            int i = counts.size();
            long delta1 = counts.get(i - period) - counts.get(i - (period + 1));
            long delta0 = counts.get(i - (2 * period)) - counts.get(i - (2 * period + 1));
            long delta2 = delta1 + (delta1 - delta0);
            counts.add(counts.get(i - 1) + delta2);
        }

        System.out.println(counts.get(counts.size() - 1));
    }
}
