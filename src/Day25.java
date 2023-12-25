import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day25 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input25.txt");
        Set<String> parts = new HashSet<>();
        List<Pair> pairs = new ArrayList<>();
        for (String s : input) {
            String[] sp = s.split(":");
            String left = sp[0].trim();
            parts.add(left);
            String[] spsp = sp[1].trim().split(" ");
            for (String t : spsp) {
                parts.add(t);
                pairs.add(new Pair(left, t));
            }
        }

        Map<String, Node> nodes = makeNodes(parts, pairs);

        Map<Pair, Integer> stats = new HashMap<>();
        for (Node node : nodes.values()) {
            explore(node, stats);
        }
        List<Map.Entry<Pair, Integer>> statsSorted = stats.entrySet()
                .stream()
                .sorted(Map.Entry.<Pair, Integer>comparingByValue().reversed()
                        .thenComparing(e -> e.getKey().a)
                        .thenComparing(e -> e.getKey().b))
                .collect(Collectors.toList());
        System.out.println(statsSorted);

        List<Pair> pairsSorted = statsSorted.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (int i = 0; i < pairsSorted.size(); i++) {
            for (int j = i + 1; j < pairsSorted.size(); j++) {
                for (int k = j + 1; k < pairsSorted.size(); k++) {
                    List<Pair> skipPairs = List.of(
                            pairsSorted.get(i),
                            pairsSorted.get(j),
                            pairsSorted.get(k)
                    );

                    List<Set<String>> components = determineComponents(nodes, skipPairs);
                    if (components.size() > 1) {
                        System.out.println(components);
                        int first = components.stream()
                                .map(Set::size)
                                .reduce((a, b) -> a * b)
                                .orElseThrow();
                        System.out.println(pairsSorted.get(i));
                        System.out.println(pairsSorted.get(j));
                        System.out.println(pairsSorted.get(k));
                        System.out.println(first);
                        return;
                    }
                }
            }
        }
    }

    private static void explore(Node start, Map<Pair, Integer> stats) {
        Set<Node> explored = new HashSet<>();
        Set<Node> current = new HashSet<>(List.of(start));
        while (!current.isEmpty()) {
            explored.addAll(current);
            Set<Node> next = new HashSet<>();
            for (Node node : current) {
                for (Node nextNode : node.nodes) {
                    if (!explored.contains(nextNode)) {
                        Pair pair = new Pair(node.name, nextNode.name);
                        int count = stats.computeIfAbsent(pair, key -> 0);
                        stats.put(pair, 1 + count);
                        next.add(nextNode);
                    }
                }
            }
            current = next;
        }
    }

    private static Map<String, Node> makeNodes(Set<String> parts, List<Pair> pairs) {
        Map<String, Node> nodes = new HashMap<>();
        for (String s : parts) {
            nodes.put(s, new Node(s));
        }
        for (var pair : pairs) {
            nodes.get(pair.a).nodes.add(nodes.get(pair.b));
            nodes.get(pair.b).nodes.add(nodes.get(pair.a));
        }
        return nodes;
    }

    private static class Node {
        private String name;
        private Set<Node> nodes = new HashSet<>();

        public Node(String name) {
            this.name = name;
        }
    }

    private static List<Set<String>> determineComponents(Map<String, Node> nodes, List<Pair> skipPairs) {
        Set<String> parts = new HashSet<>(nodes.keySet());
        List<Set<String>> components = new ArrayList<>();
        while (!parts.isEmpty()) {
            String start = parts.stream().findFirst().orElseThrow();

            Set<String> component = new HashSet<>();
            component.add(start);
            Set<String> todo = new HashSet<>();
            todo.add(start);

            while (!todo.isEmpty()) {
                String current = todo.stream().findFirst().orElseThrow();
                todo.remove(current);

                for (Node nei : nodes.get(current).nodes) {
                    String next = nei.name;
                    if (component.contains(next) || todo.contains(next)) {
                        continue;
                    }
                    if (skipPairs.contains(new Pair(current, next)) ||
                            skipPairs.contains(new Pair(next, current))) {
                        continue;
                    }
                    component.add(next);
                    todo.add(next);
                }
            }

            parts.removeAll(component);
            components.add(component);
        }
        return components;
    }

    static class Pair {
        String a;
        String b;

        public Pair(String a, String b) {
            if (a.compareTo(b) < 0) {
                this.a = a;
                this.b = b;
            } else {
                this.a = b;
                this.b = a;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(a, pair.a) && Objects.equals(b, pair.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }

        @Override
        public String toString() {
            return "(" + a + "--" + b + ")";
        }
    }
}
