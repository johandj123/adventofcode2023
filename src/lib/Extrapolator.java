package lib;

import java.util.ArrayList;
import java.util.List;

public class Extrapolator {
    private final List<Long> front = new ArrayList<>();

    public Extrapolator(List<Long> input) {
        List<List<Long>> pyramid = new ArrayList<>();
        pyramid.add(input);
        while (!allZeroes(getLast(pyramid))) {
            List<Long> last = getLast(pyramid);
            front.add(getLast(last));
            List<Long> next = new ArrayList<>();
            for (int i = 0; i < last.size() - 1; i++) {
                long a = last.get(i);
                long b = last.get(i + 1);
                next.add(b - a);
            }
            pyramid.add(next);
        }
    }

    public int getOrder() {
        return front.size() - 1;
    }

    public long advance(long n) {
        for (long i = 0; i < n; i++) {
            advance();
        }
        return front.get(0);
    }

    public long advance() {
        for (int i = front.size() - 2; i >= 0; i--) {
            front.set(i, front.get(i) + front.get(i + 1));
        }
        return front.get(0);
    }

    private static boolean allZeroes(List<Long> l)
    {
        return l.stream().allMatch(i -> i == 0);
    }

    private static <X> X getLast(List<X> list)
    {
        return list.get(list.size() - 1);
    }

    public static void main(String[] args) {
        Extrapolator extrapolator = new Extrapolator(List.of(1L, 3L, 6L, 10L, 15L, 21L));
        for (int i = 0; i < 10; i++) {
            System.out.println(extrapolator.advance());
        }
    }
}
