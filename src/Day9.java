import lib.Extrapolator;
import lib.InputUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input9.txt");
        List<List<Integer>> values = input.stream().map(InputUtil::splitIntoIntegers).collect(Collectors.toList());
        int first = values.stream().mapToInt(Day9::extrapolateRight).sum();
        System.out.println(first);
        int second = values.stream().mapToInt(Day9::extrapolateLeft).sum();
        System.out.println(second);
    }

    private static int extrapolateLeft(List<Integer> input)
    {
        Collections.reverse(input);
        return extrapolateRight(input);
    }

    private static int extrapolateRight(List<Integer> input)
    {
        Extrapolator extrapolator = new Extrapolator(input.stream().map(x -> (long) x).collect(Collectors.toList()));
        return (int) extrapolator.advance();
    }
}
