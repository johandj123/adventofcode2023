import lib.InputUtil;
import lib.Interval;

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
            List<Interval> intervals = Collections.singletonList(Interval.ofStartAndLength(inputNumbers.get(i), inputNumbers.get(i + 1)));
            for (AMap map : maps) {
                List<Interval> nextIntervals = new ArrayList<>();
                for (Interval interval : intervals) {
                    nextIntervals.addAll(map.mapInterval(interval));
                }
                intervals = nextIntervals;
            }
            locations.add(intervals.stream().map(Interval::getStart).min(Comparator.comparing(x -> x)).orElseThrow());
        }
        long second = locations.stream().min(Comparator.comparing(x -> x)).orElseThrow();
        System.out.println(second);
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

        Interval map(Interval interval)
        {
            long rstart = interval.getStart();
            long rlength = interval.getLength();
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
            return Interval.ofStartAndLength(map(rstart), rlength);
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

        List<Interval> mapInterval(Interval interval)
        {
            List<Interval> intervals = new ArrayList<>();
            for (Mapping mapping : mappings) {
                Interval newInterval = mapping.map(interval);
                if (newInterval != null) {
                    intervals.add(newInterval);
                }
            }
            return intervals;
        }
    }
}
