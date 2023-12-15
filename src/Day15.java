import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day15 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input15.txt").trim();
        String[] parts = input.split(",");
        int first = Arrays.stream(parts).mapToInt(Day15::hash).sum();
        System.out.println(first);
        List<Map<String, Integer>> boxes = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            boxes.add(new LinkedHashMap<>());
        }
        for (String part : parts) {
            if (part.endsWith("-")) {
                String label = part.substring(0, part.length() - 1);
                int box = hash(label);
                boxes.get(box).remove(label);
            } else {
                String[] sp = part.split("=");
                String label = sp[0];
                int focal = Integer.parseInt(sp[1]);
                int box = hash(label);
                boxes.get(box).put(label, focal);
            }
        }
        int second = 0;
        for (int i = 0; i < boxes.size(); i++) {
            Map<String, Integer> box = boxes.get(i);
            int slot = 1;
            for (Map.Entry<String, Integer> entry : box.entrySet()) {
                int value = (i + 1) * (slot++) * entry.getValue();
                second += value;
            }
        }
        System.out.println(second);
    }

    private static int hash(String s)
    {
        int r = 0;
        for (char c : s.toCharArray()) {
            r += c;
            r *= 17;
            r %= 256;
        }
        return r;
    }
}
