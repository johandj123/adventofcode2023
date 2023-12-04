import lib.InputUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 {
    public static void main(String[] args) throws IOException {
        List<Card> cards = InputUtil.readAsLines("input4.txt")
                .stream()
                .map(Card::new)
                .collect(Collectors.toList());
        long first = cards.stream()
                .mapToLong(Card::points)
                .sum();
        System.out.println(first);
        long[] amount = new long[cards.size()];
        Arrays.fill(amount, 1);
        for (int i = 0; i < cards.size(); i++) {
            long curCopies = amount[i];
            int matches = cards.get(i).matches();
            for (int j = 1; j <= matches; j++) {
                amount[i + j] += curCopies;
            }
        }
        long second = Arrays.stream(amount).sum();
        System.out.println(second);
    }

    static class Card
    {
        Set<Integer> a;
        Set<Integer> b;

        Card(String line)
        {
            line = line.substring(line.indexOf(":") + 2);
            String[] sp = line.split("\\|");
            a = new HashSet<>(InputUtil.splitIntoIntegers(sp[0]));
            b = new HashSet<>(InputUtil.splitIntoIntegers(sp[1]));
        }

        long points()
        {
            int c = matches();
            if (c == 0) return 0;
            return (1L << (c - 1));
        }

        private int matches() {
            int c = 0;
            for (int i : a) {
                if (b.contains(i)) {
                    c++;
                }
            }
            return c;
        }
    }
}
