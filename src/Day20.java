import lib.InputUtil;
import lib.MathUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day20 {
    private Map<String, Module> modules;

    public static void main(String[] args) throws IOException {
        new Day20().start();
    }

    private void start() throws IOException {
        modules = InputUtil.readAsLines("input20.txt")
                .stream()
                .map(Module::new)
                .collect(Collectors.toMap(m -> m.name, m -> m));
        pulse(false);
        pulse(true);
        // Some manual work in part 2.
        // The four numbers below are the periods found for kl, vm, kv and vb; found with pulse(true) and hardcoding the name
        System.out.println("Second: " + MathUtil.lcm(3917, 4051, 4013, 3793));
    }

    private void pulse(boolean second) {
        long low = 0L;
        long high = 0L;
        Queue<Pulse> pulses = new ArrayDeque<>();
        Map<String, Boolean> flipflops = modules.values().stream()
                .filter(module -> "%".equals(module.type))
                .collect(Collectors.toMap(module -> module.name, name -> false));
        Map<String, Map<String, Boolean>> conjuntion = new HashMap<>();
        for (Module module : modules.values()) {
            for (String dest : module.dest) {
                Module destModule = modules.get(dest);
                if (destModule != null && "&".equals(destModule.type)) {
                    Map<String, Boolean> map = conjuntion.computeIfAbsent(destModule.name, key -> new HashMap<>());
                    map.put(module.name, false);
                }
            }
        }
        for (int i = 0; i < (second ? 50000 : 1000); i++) {
            pulses.add(new Pulse("button", "broadcaster", false));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.remove();
                if (second && "vb".equals(pulse.name) && !pulse.high) {
                    System.out.println("Found after: " + (i + 1));
                }
//                System.out.println(pulse);
                if (pulse.high) {
                    high++;
                } else {
                    low++;
                }
                Module module = modules.get(pulse.name);
                if (module != null) {
                    if ("&".equals(module.type)) {
                        Map<String, Boolean> map = conjuntion.get(module.name);
                        map.put(pulse.from, pulse.high);
                        boolean newHigh = !map.values().stream().allMatch(x -> x);
                        for (String dest : module.dest) {
                            Pulse newPulse = new Pulse(pulse.name, dest, newHigh);
                            pulses.add(newPulse);
                        }
                    } else if ("%".equals(module.type)) {
                        if (!pulse.high) {
                            boolean state = !flipflops.get(pulse.name);
                            flipflops.put(pulse.name, state);
                            for (String dest : module.dest) {
                                Pulse newPulse = new Pulse(pulse.name, dest, state);
                                pulses.add(newPulse);
                            }
                        }
                    } else {
                        for (String dest : module.dest) {
                            Pulse newPulse = new Pulse(pulse.name, dest, pulse.high);
                            pulses.add(newPulse);
                        }
                    }
                }
            }
        }
        System.out.println("First: " + low * high);
    }

    static class Pulse {
        final String from;
        final boolean high;
        final String name;

        public Pulse(String from, String name, boolean high) {
            this.from = from;
            this.name = name;
            this.high = high;
        }

        @Override
        public String toString() {
            return "Pulse{" +
                    "from='" + from + '\'' +
                    ", high=" + (high ? "high" : "low") +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    static class Module {
        String name;
        String type;
        List<String> dest;

        Module(String s) {
            String[] sp = s.split(" -> ");
            if (sp[0].startsWith("%") || sp[0].startsWith("&")) {
                name = sp[0].substring(1);
                type = sp[0].substring(0, 1);
            } else {
                name = sp[0];
            }
            dest = Arrays.stream(sp[1].split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return "Module{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", dest=" + dest +
                    '}';
        }
    }
}
