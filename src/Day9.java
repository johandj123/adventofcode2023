import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input9.txt");
        List<List<Integer>> values = input.stream().map(InputUtil::splitIntoIntegers).collect(Collectors.toList());
        int first = values.stream().mapToInt(Day9::predictRight).sum();
        System.out.println(first);
        int second = values.stream().mapToInt(Day9::predictLeft).sum();
        System.out.println(second);
    }

    private static int predictLeft(List<Integer> input)
    {
        List<List<Integer>> x = start(input);
        getLast(x).add(0, 0);
        for (int i = x.size() - 2; i >= 0; i--) {
            int a = x.get(i).get(0);
            int b = x.get(i + 1).get(0);
            x.get(i).add(0, a - b);
        }
        return x.get(0).get(0);
    }

    private static int predictRight(List<Integer> input)
    {
        List<List<Integer>> x = start(input);
        getLast(x).add(0);
        for (int i = x.size() - 2; i >= 0; i--) {
            int a = getLast(x.get(i));
            int b = getLast(x.get(i + 1));
            x.get(i).add(a + b);
        }
        return getLast(x.get(0));
    }

    private static List<List<Integer>> start(List<Integer> input) {
        List<List<Integer>> x = new ArrayList<>();
        x.add(new ArrayList<>(input));
        while (!allZeroes(getLast(x))) {
            List<Integer> last = getLast(x);
            List<Integer> next = new ArrayList<>();
            for (int i = 0; i < last.size() - 1; i++) {
                int a = last.get(i);
                int b = last.get(i + 1);
                next.add(b - a);
            }
            x.add(next);
        }
        return x;
    }

    private static boolean allZeroes(List<Integer> l)
    {
        return l.stream().allMatch(i -> i == 0);
    }

    private static <X> X getLast(List<X> list)
    {
        return list.get(list.size() - 1);
    }
}
