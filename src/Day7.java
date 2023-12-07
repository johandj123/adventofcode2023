import lib.InputUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day7 {
    private static final String CARDS = "23456789TJQKA";

    public static void main(String[] args) throws IOException {
        List<String> lines = InputUtil.readAsLines("input7.txt");
        List<Hand> hands = lines.stream().map(Hand::new).collect(Collectors.toList());
        Comparator<Hand> comparator = Comparator.comparing(Hand::type).thenComparing(Hand::cards);
        hands.sort(comparator);
        long first = 0;
        for (int i = 0; i < hands.size(); i++) {
            long rank = i + 1;
            Hand hand = hands.get(i);
            first += (rank * hand.bid);
        }
        System.out.println(first);
    }

    static class Hand
    {
        int[] hand;
        long bid;

        Hand(String input)
        {
            hand = new int[5];
            for (int i = 0; i < 5; i++) {
                String card = input.substring(i, i + 1);
                hand[i] = CARDS.indexOf(card);
            }
            bid = Long.parseLong(input.split(" ")[1]);
        }

        int type()
        {
            Map<Integer, Integer> hist = new HashMap<>();
            for (int h : hand) {
                int count = hist.getOrDefault(h, 0);
                hist.put(h, count + 1);
            }
            Map<Integer, Integer> hist2 = new HashMap<>();
            for (int count : hist.values()) {
                int count2 = hist2.getOrDefault(count, 0);
                hist2.put(count, count2 + 1);
            }
            // 5 of a kind
            if (hist2.getOrDefault(5, 0) == 1) {
                return 6;
            }
            // 4 of a kind
            if (hist2.getOrDefault(4, 0) == 1) {
                return 5;
            }
            // Full house
            if (hist2.getOrDefault(3, 0) == 1 && hist2.getOrDefault(2, 0) == 1) {
                return 4;
            }
            // 3 of a kind
            if (hist2.getOrDefault(3, 0) == 1) {
                return 3;
            }
            // 2 pair
            if (hist2.getOrDefault(2, 0) == 2) {
                return 2;
            }
            // 1 pair
            if (hist2.getOrDefault(2, 0) == 1) {
                return 1;
            }
            return 0;
        }

        int cards()
        {
            int r = 0;
            for (int i = 0; i < hand.length; i++) {
                r = (100 * r) + hand[i];
            }
            return r;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            for (int i : hand) {
                sb.append(CARDS.charAt(i));
            }
            return sb.toString();
        }
    }
}
