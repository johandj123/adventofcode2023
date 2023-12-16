import lib.CharMatrix;
import lib.InputUtil;
import lib.StepUtil;

import java.io.IOException;

public class Day14 {

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
        charMatrix = StepUtil.performStepsWithCycleDetection(charMatrix, 1000000000, Day14::cycle);
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
