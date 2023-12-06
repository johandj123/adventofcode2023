import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day6 {
    public static void main(String[] args) throws IOException {
        List<String> lines = InputUtil.readAsLines("input6.txt");
        List<Integer> time = InputUtil.splitIntoIntegers(lines.get(0).substring(lines.get(0).indexOf(':') + 1));
        List<Integer> distance = InputUtil.splitIntoIntegers(lines.get(1).substring(lines.get(1).indexOf(':') + 1));
        long first = 1L;
        for (int i = 0; i < time.size(); i++) {
            first *= waysToBeat(time.get(i), distance.get(i));
        }
        System.out.println(first);
        long time1 = Long.parseLong(digitFilter(lines.get(0)));
        long distance1 = Long.parseLong(digitFilter(lines.get(1)));
        long second = waysToBeat(time1, distance1);
        System.out.println(second);
    }

    public static int waysToBeat(long time,long distance)
    {
        int wins = 0;
        for (long i = 0; i < time; i++) {
            long hold = (time - i);
            long travelled = hold * i;
            if (travelled > distance) {
                wins++;
            }
        }
        return wins;
    }

    private static String digitFilter(String s)
    {
        return s.chars().filter(d -> d >= '0' && d <= '9').collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
