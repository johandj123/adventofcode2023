import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day14 {

    public static final int CYCLES = 1000000000;

    public static void main(String[] args) throws IOException {
        first();
        second();
    }

    private static void first() throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input14.txt"));
        rollNorth(charMatrix);
        System.out.println(calculateLoad(charMatrix));
    }

    private static void second() throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input14.txt"));
        Map<CharMatrix, Integer> map = new HashMap<>();
        int i = 0;
        int cycleLength = -1;
        while (i < CYCLES) {
            map.put(charMatrix, i);
            charMatrix = cycle(charMatrix);
            i++;
            Integer i1 = map.get(charMatrix);
            if (i1 != null) {
                cycleLength = i - i1;
                break;
            }
        }
        while (i + cycleLength <= CYCLES) {
            i += cycleLength;
        }
        while (i < CYCLES) {
            charMatrix = cycle(charMatrix);
            i++;
        }
        System.out.println(calculateLoad(charMatrix));
    }

    private static int calculateLoad(CharMatrix charMatrix) {
        int load = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == 'O') {
                    load += (charMatrix.getHeight() - y);
                }
            }
        }
        return load;
    }

    private static void rollNorth(CharMatrix charMatrix) {
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == 'O') {
                    int y1 = y;
                    while (y1 > 0 && charMatrix.get(x, y1 - 1) == '.') {
                        y1--;
                    }
                    charMatrix.set(x, y, '.');
                    charMatrix.set(x, y1, 'O');
                }
            }
        }
    }

    private static void rollSouth(CharMatrix charMatrix) {
        for (int y = charMatrix.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == 'O') {
                    int y1 = y;
                    while (y1 < charMatrix.getHeight() - 1 && charMatrix.get(x, y1 + 1) == '.') {
                        y1++;
                    }
                    charMatrix.set(x, y, '.');
                    charMatrix.set(x, y1, 'O');
                }
            }
        }
    }

    private static void rollWest(CharMatrix charMatrix) {
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == 'O') {
                    int x1 = x;
                    while (x1 > 0 && charMatrix.get(x1 - 1, y) == '.') {
                        x1--;
                    }
                    charMatrix.set(x, y, '.');
                    charMatrix.set(x1, y, 'O');
                }
            }
        }
    }

    private static void rollEast(CharMatrix charMatrix) {
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = charMatrix.getWidth() - 1; x >= 0; x--) {
                if (charMatrix.get(x, y) == 'O') {
                    int x1 = x;
                    while (x1 < charMatrix.getWidth() - 1 && charMatrix.get(x1 + 1, y) == '.') {
                        x1++;
                    }
                    charMatrix.set(x, y, '.');
                    charMatrix.set(x1, y, 'O');
                }
            }
        }
    }

    private static CharMatrix cycle(CharMatrix charMatrix) {
        CharMatrix next = new CharMatrix(charMatrix);
        rollNorth(next);
        rollWest(next);
        rollSouth(next);
        rollEast(next);
        return next;
    }
}
