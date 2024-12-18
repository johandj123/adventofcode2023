import lib.InputUtil;
import lib.Interval;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day19 {
    public static void main(String[] args) throws IOException {
        List<String> groups = InputUtil.readAsStringGroups("input19.txt");
        Map<String,Rule> rules = Arrays.stream(groups.get(0).split("\n")).map(Rule::new)
                .collect(Collectors.toMap(r -> r.name, r -> r));
        List<Map<String, Integer>> parts = Arrays.stream(groups.get(1).split("\n")).map(Day19::parsemap).collect(Collectors.toList());
        first(parts, rules);
        second(rules);
    }

    private static void first(List<Map<String, Integer>> parts, Map<String, Rule> rules) {
        int acceptRating = 0;
        for (var part : parts) {
            String state = "in";
            while (true) {
                Rule rule = rules.get(state);
                if (rule == null) {
                    break;
                }
                state = rule.step(part);
            }
            if ("A".equals(state)) {
                acceptRating += rating(part);
            }
        }
        System.out.println(acceptRating);
    }

    private static void second(Map<String, Rule> rules) {
        long result = count(rules, "in", new Mask());
        System.out.println(result);
    }

    private static long count(Map<String, Rule> rules, String state, Mask mask) {
        if ("A".equals(state)) {
            return mask.count();
        }
        if ("R".equals(state)) {
            return 0L;
        }
        long sum = 0L;
        Rule rule = rules.get(state);
        Mask curMask = new Mask(mask);
        for (RulePart rulePart : rule.ruleParts) {
            Mask nextMask = curMask.apply(rulePart);
            sum += count(rules, rulePart.to, nextMask);
            curMask = curMask.applyInverse(rulePart);
        }
        sum += count(rules, rule.to, curMask);
        return sum;
    }

    static Map<String,Integer> parsemap(String s) {
        s = s.trim().substring(1, s.length() - 1);
        String[] split = s.split(",");
        Map<String,Integer> result = new HashMap<>();
        for (String t : split) {
            String[] split2 = t.split("=");
            result.put(split2[0], Integer.parseInt(split2[1]));
        }
        return result;
    }

    static int rating(Map<String, Integer> map) {
        return map.values().stream().mapToInt(x -> x).sum();
    }

    static class Rule {
        String name;
        List<RulePart> ruleParts;
        String to;

        Rule(String s) {
            String[] split = s.trim().split("[{}]");
            name = split[0];
            String[] split2 = split[1].split(",");
            ruleParts = Arrays.stream(split2).map(ss -> new RulePart(ss, this)).filter(r -> r.name != null).collect(Collectors.toList());
        }

        String step(Map<String,Integer> map) {
            return ruleParts.stream()
                    .filter(rulePart -> rulePart.check(map))
                    .findFirst()
                    .map(rulePart -> rulePart.to)
                    .orElse(to);
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "name='" + name + '\'' +
                    ", ruleParts=" + ruleParts +
                    ", to='" + to + '\'' +
                    '}';
        }
    }

    static class RulePart {
        String name;
        boolean greater;
        int number;
        String to;

        RulePart(String part, Rule rule) {
            String[] split = part.split("[<>:]");
            if (split.length > 1) {
                name = split[0];
                greater = part.contains(">");
                number = Integer.parseInt(split[1]);
                to = split[2];
            } else {
                rule.to = split[0];
            }
        }

        boolean check(Map<String,Integer> map) {
            int value = map.get(name);
            return greater ? (value > number) : (value < number);
        }

        @Override
        public String toString() {
            return "RulePart{" +
                    "name='" + name + '\'' +
                    ", greater=" + greater +
                    ", number=" + number +
                    ", to='" + to + '\'' +
                    '}';
        }
    }

    static class Mask {
        Map<String, Interval> maskParts = new HashMap<>();

        Mask() {
            maskParts.put("x", Interval.ofClosed(1, 4000));
            maskParts.put("m", Interval.ofClosed(1, 4000));
            maskParts.put("a", Interval.ofClosed(1, 4000));
            maskParts.put("s", Interval.ofClosed(1, 4000));
        }

        Mask(Mask o) {
            maskParts = new HashMap<>(o.maskParts);
        }

        long count() {
            return maskParts.values().stream().mapToLong(Interval::getLength).reduce((a, b) -> a * b).orElseThrow();
        }

        Mask apply(RulePart rulePart) {
            Mask mask = new Mask(this);
            Interval partInterval = rulePart.greater ? Interval.ofClosed(rulePart.number + 1, 4000) : Interval.ofClosed(1, rulePart.number - 1);
            Interval newInterval = Interval.and(maskParts.get(rulePart.name), partInterval);
            mask.maskParts.put(rulePart.name, newInterval);
            return mask;
        }

        Mask applyInverse(RulePart rulePart) {
            Mask mask = new Mask(this);
            Interval partInterval = rulePart.greater ? Interval.ofClosed(1, rulePart.number) : Interval.ofClosed(rulePart.number, 4000);
            Interval newInterval = Interval.and(maskParts.get(rulePart.name), partInterval);
            mask.maskParts.put(rulePart.name, newInterval);
            return mask;
        }
    }
}
