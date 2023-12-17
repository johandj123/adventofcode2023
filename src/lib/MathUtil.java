package lib;

import java.util.Arrays;
import java.util.List;

public class MathUtil {
    private MathUtil() {
    }

    public static long gcd(long a,long b)
    {
        while (b != 0) {
            long bnew = Math.floorMod(a, b);
            a = b;
            b = bnew;
        }
        return a;
    }

    public static long lcm(long a, long b)
    {
        return (a * b) / gcd(a, b);
    }

    public static long lcm(long... values)
    {
        return Arrays.stream(values)
                .reduce(MathUtil::lcm)
                .orElseThrow();
    }

    public static long lcm(List<Long> values)
    {
        return values.stream()
                .reduce(MathUtil::lcm)
                .orElseThrow();
    }
}
