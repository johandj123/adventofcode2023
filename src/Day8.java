import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input8.txt");
        Map<String, String> left = new HashMap<>();
        Map<String, String> right = new HashMap<>();
        for (String s : input.get(1).trim().split("\n")) {
            String key = s.substring(0, 3);
            left.put(key, s.substring(7, 10));
            right.put(key, s.substring(12, 15));
        }
        first(input, left, right);
        second(input, left, right);
    }

    private static void first(List<String> input, Map<String, String> left, Map<String, String> right) {
        String current = "AAA";
        int first = 0;
        outer:
        while (true) {
            for (char c : input.get(0).trim().toCharArray()) {
                current = follow(left, right, c, current);
                first++;
                if ("ZZZ".equals(current)) {
                    break outer;
                }
            }
        }
        System.out.println(first);
    }

    static class KeySteps {
        String key;
        long steps;

        public KeySteps(String key, long steps) {
            this.key = key;
            this.steps = steps;
        }
    }

    private static void second(List<String> input, Map<String, String> left, Map<String, String> right) {
        Map<String, KeySteps> atozMap = new HashMap<>();
        Map<String, KeySteps> periodMap = new HashMap<>();
        for (String current : left.keySet()) {
            if (current.endsWith("A")) {
                atozMap.put(current, countToZ(input, left, right, current));
            } else if (current.endsWith("Z")) {
                periodMap.put(current, countToZ(input, left, right, current));
            }
        }

        for (String key : atozMap.keySet()) {
            KeySteps atoz = atozMap.get(key);
            System.out.println(key + " " + atoz.steps + " " + periodMap.get(atoz.key).steps);
        }

        List<Long> values = atozMap.values().stream().map(ks -> ks.steps).collect(Collectors.toList());
        long second = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            second = lcm(second, values.get(i));
        }
        System.out.println(second);
    }

    private static KeySteps countToZ(List<String> input, Map<String, String> left, Map<String, String> right, String current) {
        int count = 0;
        outer:
        while (true) {
            for (char c : input.get(0).trim().toCharArray()) {
                current = follow(left, right, c, current);
                count++;
                if (current.endsWith("Z")) {
                    break outer;
                }
            }
        }
        return new KeySteps(current, count);
    }

    private static String follow(Map<String, String> left, Map<String, String> right, char c, String current) {
        if (c == 'L') {
            current = left.get(current);
        } else if (c == 'R') {
            current = right.get(current);
        }
        return current;
    }

    private static long lcm(long a, long b)
    {
        return (a * b) / gcd(a, b);
    }

    private static long gcd(long a, long b)
    {
        // Find Minimum of a and b
        long result = Math.min(a, b);
        while (result > 0) {
            if (a % result == 0 && b % result == 0) {
                break;
            }
            result--;
        }

        // Return gcd of a and b
        return result;
    }
}
