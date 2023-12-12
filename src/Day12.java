import lib.InputUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<Config> input = InputUtil.readAsLines("input12.txt")
                .stream()
                .map(Config::new)
                .collect(Collectors.toList());
        configs(input);
        List<Config> unfolded = input.stream().map(Config::unfold).collect(Collectors.toList());
        configs(unfolded);
    }

    private static void configs(List<Config> input) {
        long count = 0L;
        for (Config config : input) {
            count += config.configs();
        }
        System.out.println(count);
    }

    static class Config {
        String config;
        List<Integer> counts;

        static class MemoKey {
            int inputIndex;
            int groupIndex;

            public MemoKey(int inputIndex, int groupIndex) {
                this.inputIndex = inputIndex;
                this.groupIndex = groupIndex;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                MemoKey memoKey = (MemoKey) o;
                return inputIndex == memoKey.inputIndex && groupIndex == memoKey.groupIndex;
            }

            @Override
            public int hashCode() {
                return Objects.hash(inputIndex, groupIndex);
            }
        }

        Config(String line) {
            String[] sp = line.split(" ");
            config = sp[0];
            counts = InputUtil.splitIntoIntegers(sp[1].replace(',', ' '));
        }

        public Config(String config, List<Integer> counts) {
            this.config = config;
            this.counts = counts;
        }

        long configs() {
            byte[] bb = config.getBytes(StandardCharsets.UTF_8);
            Map<MemoKey, Long> memo = new HashMap<>();
            return configs(bb, memo, 0, 0);
        }

        long configs(byte[] bb, Map<MemoKey, Long> memo, int inputIndex, int groupIndex) {
            MemoKey key = new MemoKey(inputIndex, groupIndex);
            Long value = memo.get(key);
            if (value != null) {
                return value;
            } else {
                long result = configsInner(bb, memo, inputIndex, groupIndex);
                memo.put(key, result);
                return result;
            }
        }

        long configsInner(byte[] bb, Map<MemoKey, Long> memo, int inputIndex, int groupIndex) {
            if (inputIndex == bb.length && groupIndex == counts.size()) {
                return 1L;
            }
            long result = 0L;
            if (inputIndex < bb.length && bb[inputIndex] != '#') {
                result += configs(bb, memo, inputIndex + 1, groupIndex);
            }
            if (groupIndex < counts.size()) {
                int n = counts.get(groupIndex);
                if (inputIndex + n <= bb.length) {
                    boolean ok = true;
                    for (int i = inputIndex; i < inputIndex + n; i++) {
                        if (bb[i] == '.') {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        int newIndex = inputIndex + n;
                        if (newIndex == bb.length) {
                            result += configs(bb, memo, newIndex, groupIndex + 1);
                        } else if (bb[newIndex] != '#') {
                            result += configs(bb, memo, newIndex + 1, groupIndex + 1);
                        }
                    }
                }
            }
            return result;
        }

        Config unfold() {
            StringBuilder sb = new StringBuilder();
            List<Integer> ii = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                if (i > 0) {
                    sb.append('?');
                }
                sb.append(config);
                ii.addAll(counts);
            }
            return new Config(sb.toString(), ii);
        }
    }
}
