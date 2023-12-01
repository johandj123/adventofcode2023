import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Day1 {
    public static final Map<String, Integer> MAP =
            Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5,
                    "six", 6, "seven", 7, "eight", 8, "nine", 9);

    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input1.txt");
        int first = input.stream()
                .map(Day1::digitFilter)
                .map(i -> i.substring(0, 1) + i.substring(i.length() - 1, i.length()))
                .mapToInt(Integer::parseInt)
                .sum();
        int second = input.stream()
                .map(Day1::replaceWords)
                .map(Day1::digitFilter)
                .map(i -> i.substring(0, 1) + i.substring(i.length() - 1, i.length()))
                .mapToInt(Integer::parseInt)
                .sum();
        System.out.println(first);
        System.out.println(second);
    }

    private static String replaceWords(String s)
    {
        StringBuilder sb = new StringBuilder();
        outer:
        for (int i = 0; i < s.length(); i++) {
            String st = s.substring(i);
            for (var entry : MAP.entrySet()) {
                if (st.startsWith(entry.getKey())) {
                    sb.append(Character.toString(entry.getValue() + '0'));
                    continue outer;
                }
            }
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    private static String digitFilter(String s)
    {
        return s.chars().filter(d -> d >= '0' && d <= '9').collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
