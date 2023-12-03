import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 {
    CharMatrix charMatrix;
    Map<CharMatrix.Position, List<Integer>> stars = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new Day3().start();
    }

    private void start() throws IOException {
        charMatrix = new CharMatrix(InputUtil.readAsLines("input3.txt"), '.');
        int first = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                char c = charMatrix.get(x, y);
                if (Character.isDigit(c)) {
                    int xstart = x;
                    StringBuilder sb = new StringBuilder();
                    sb.append(c);
                    while (Character.isDigit(charMatrix.getUnbounded(x + 1, y))) {
                        x++;
                        sb.append(charMatrix.get(x, y));
                    }
                    int number = Integer.parseInt(sb.toString());
                    if (aroundSymbol(xstart, x, y)) {
                        first += number;
                    }
                    aroundSymbolStar(xstart, x, y, number);
                }
            }
        }
        System.out.println(first);
        long second = 0;
        for (var entry : stars.entrySet()) {
            if (entry.getValue().size() == 2) {
                long v1 = entry.getValue().get(0);
                long v2 = entry.getValue().get(1);
                second += (v1 * v2);
            }
        }
        System.out.println(second);
    }

    private boolean aroundSymbol(int x1,int x2,int y1)
    {
        for (int x = x1 - 1; x <= x2 + 1; x++) {
            for (int y = y1 - 1; y <= y1 + 1; y++) {
                char c = charMatrix.getUnbounded(x, y);
                if (c != '.' && !Character.isDigit(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void aroundSymbolStar(int x1,int x2,int y1,int number)
    {
        for (int x = x1 - 1; x <= x2 + 1; x++) {
            for (int y = y1 - 1; y <= y1 + 1; y++) {
                char c = charMatrix.getUnbounded(x, y);
                if (c == '*') {
                    CharMatrix.Position starpos = charMatrix.new Position(x, y);
                    List<Integer> li = stars.computeIfAbsent(starpos, key -> new ArrayList<>());
                    li.add(number);
                }
            }
        }
    }
}
