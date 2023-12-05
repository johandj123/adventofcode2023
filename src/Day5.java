import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Day5 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input5.txt");
        List<Long> inputNumbers = InputUtil.splitIntoLongs(input.get(0).substring(input.get(0).indexOf(":") + 2));
        List<AMap> maps = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            maps.add(new AMap(input.get(i)));
        }

        first(inputNumbers, maps);

        second(inputNumbers, maps);
    }

    private static void first(List<Long> inputNumbers, List<AMap> maps) {
        List<Long> locations = new ArrayList<>();
        for (long n : inputNumbers) {
            for (AMap map : maps) {
                n = map.map(n);
            }
            locations.add(n);
        }
        long first = locations.stream().min(Comparator.comparing(x -> x)).orElseThrow();
        System.out.println(first);
    }

    private static void second(List<Long> inputNumbers, List<AMap> maps) {
        List<Long> locations = new ArrayList<>();
        for (int i = 0; i < inputNumbers.size(); i += 2) {
            List<Range> ranges = Collections.singletonList(new Range(inputNumbers.get(i), inputNumbers.get(i + 1)));
            for (AMap map : maps) {
                List<Range> nextRanges = new ArrayList<>();
                for (Range range : ranges) {
                    nextRanges.addAll(map.mapRange(range));
                }
                ranges = nextRanges;
            }
            locations.add(ranges.stream().map(range -> range.start).min(Comparator.comparing(x -> x)).orElseThrow());
        }
        long second = locations.stream().min(Comparator.comparing(x -> x)).orElseThrow();
        System.out.println(second);
    }

    static class Range
    {
        long start;
        long length;

        public Range(long start, long length) {
            this.start = start;
            this.length = length;
        }

        long limit()
        {
            return start + length;
        }
    }

    static class Mapping
    {
        long dest;
        long start;
        long length;

        Mapping(String s)
        {
            String[] sp = s.trim().split("\\s+");
            dest = Long.parseLong(sp[0]);
            start = Long.parseLong(sp[1]);
            length = Long.parseLong(sp[2]);
        }

        public Mapping(long dest, long start, long length) {
            this.dest = dest;
            this.start = start;
            this.length = length;
        }

        boolean inRange(long n)
        {
            return (n >= start && n < limit());
        }

        private long limit() {
            return start + length;
        }

        long map(long n)
        {
            return (n - start) + dest;
        }

        Range map(Range range)
        {
            long rstart = range.start;
            long rlength = range.length;
            if (rstart < start) {
                long d = start - rstart;
                rstart += d;
                rlength -= d;
                if (rlength <= 0) {
                    return null;
                }
            }
            // rstart >= start
            long rlimit = rstart + rlength;
            if (rlimit > limit()) {
                long d = rlimit - limit();
                rlength -= d;
                if (rlength <= 0) {
                    return null;
                }
            }
            return new Range(map(rstart), rlength);
        }
    }

    static class AMap
    {
        List<Mapping> mappings = new ArrayList<>();

        AMap(String s)
        {
            String[] sp = s.split("\n");
            for (int i = 1; i < sp.length; i++) {
                mappings.add(new Mapping(sp[i]));
            }
            mappings.sort(Comparator.comparing(mapping -> mapping.start));
            fillGaps();
        }

        private void fillGaps() {
            List<Mapping> mappings1 = new ArrayList<>();
            long current = 0;
            for (Mapping mapping : mappings) {
                long length = mapping.start - current;
                if (length > 0) {
                    mappings1.add(new Mapping(current, current, length));
                }
                mappings1.add(mapping);
                current = mapping.limit();
            }
            long length = Long.MAX_VALUE - current;
            mappings1.add(new Mapping(current, current, length));
            mappings = mappings1;
        }

        long map(long n)
        {
            for (Mapping mapping : mappings) {
                if (mapping.inRange(n)) {
                    return mapping.map(n);
                }
            }
            return n;
        }

        List<Range> mapRange(Range range)
        {
            List<Range> ranges = new ArrayList<>();
            for (Mapping mapping : mappings) {
                Range newRange = mapping.map(range);
                if (newRange != null) {
                    ranges.add(newRange);
                }
            }
            return ranges;
        }
    }
}
