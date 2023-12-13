import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day13 {
    public static void main(String[] args) throws IOException {
        List<String> inputParts = InputUtil.readAsStringGroups("input13.txt");
        List<CharMatrix> charMatrices = inputParts.stream()
                .map(CharMatrix::new)
                .collect(Collectors.toList());
        int first = charMatrices.stream()
                .mapToInt(Day13::findMirror)
                .sum();
        System.out.println(first);
        int second = charMatrices.stream()
                .mapToInt(Day13::findSmudgedMirror)
                .sum();
        System.out.println(second);
    }

    private static int findMirror(CharMatrix c)
    {
        int h = findMirror(c::getColumn, c.getWidth());
        int v = findMirror(c::getRow, c.getHeight());
        return v * 100 + h;
    }

    private static int findSmudgedMirror(CharMatrix c)
    {
        int h = findSmudgedMirror(c::getColumn, c.getWidth());
        int v = findSmudgedMirror(c::getRow, c.getHeight());
        return v * 100 + h;
    }

    private static int findMirror(Function<Integer, String> get, int count)
    {
        for (int i = 1; i < count; i++) {
            int k = i - 1;
            boolean ok = true;
            for (int j = i; j < count && k >= 0; j++, k--) {
                String jj = get.apply(j);
                String kk = get.apply(k);
                if (!jj.equals(kk)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return i;
            }
        }
        return 0;
    }

    private static int findSmudgedMirror(Function<Integer, String> get, int count)
    {
        for (int i = 1; i < count; i++) {
            int k = i - 1;
            int d = 0;
            for (int j = i; j < count && k >= 0 && d <= 1; j++, k--) {
                String jj = get.apply(j);
                String kk = get.apply(k);
                d += differences(jj, kk);
            }
            if (d == 1) {
                return i;
            }
        }
        return 0;
    }

    private static int differences(String a, String b)
    {
        int count = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                count++;
            }
        }
        return count;
    }
}
