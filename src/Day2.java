import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = InputUtil.readAsLines("input2.txt");
        List<Game> games = lines.stream()
                .map(Game::new)
                .collect(Collectors.toList());
        int first = games.stream().filter(Game::possible).mapToInt(Game::getId).sum();
        long second = games.stream().map(Game::power).mapToLong(x -> x).sum();
        System.out.println(first);
        System.out.println(second);
    }

    static class Subset
    {
        Map<String, Integer> map = new HashMap<>();

        Subset(String s)
        {
            String[] sp = s.split(", ");
            for (String p : sp) {
                String[] ar = p.split(" ");
                map.put(ar[1], Integer.parseInt(ar[0]));
            }
        }

        boolean possible()
        {
            return map.getOrDefault("red", 0) <= 12 &&
                    map.getOrDefault("green", 0) <= 13 &&
                    map.getOrDefault("blue", 0) <= 14;
        }
    }

    static class Game
    {
        int id;
        List<Subset> subsets = new ArrayList<>();

        public int getId() {
            return id;
        }

        Game(String s)
        {
            String[] ar = s.split(": ");
            id = Integer.parseInt(ar[0].substring(5));
            String[] sp = ar[1].split("; ");
            for (String p : sp) {
                subsets.add(new Subset(p));
            }
        }

        boolean possible()
        {
            return subsets.stream().allMatch(Subset::possible);
        }

        long power()
        {
            Map<String, Integer> map = new HashMap<>();
            for (Subset s : subsets) {
                for (var entry : s.map.entrySet()) {
                    int m = Math.max(map.getOrDefault(entry.getKey(), 0), entry.getValue());
                    map.put(entry.getKey(), m);
                }
            }
            long red = map.getOrDefault("red", 0);
            long green = map.getOrDefault("green", 0);
            long blue = map.getOrDefault("blue", 0);
            return red * green * blue;
        }
    }
}
