package lib;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class BigRational implements Comparable<BigRational> {
    public static final BigRational ZERO = new BigRational(0);
    public static final BigRational ONE = new BigRational(1);
    public static final BigRational MINUS_ONE = new BigRational(-1);

    private final BigInteger numerator;
    private final BigInteger denominator;

    public BigRational(long numerator) {
        this(BigInteger.valueOf(numerator), BigInteger.ONE);
    }

    public BigRational(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public BigRational(BigInteger numerator, BigInteger denominator) {
        if (BigInteger.ZERO.equals(denominator)) {
            throw new ArithmeticException("Denominator may not be zero");
        }
        BigInteger g = numerator.gcd(denominator);
        numerator = numerator.divide(g);
        denominator = denominator.divide(g);
        if (denominator.signum() > 0) {
            this.numerator = numerator;
            this.denominator = denominator;
        } else {
            this.numerator = numerator.negate();
            this.denominator = denominator.negate();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigRational that = (BigRational) o;
        return Objects.equals(numerator, that.numerator) && Objects.equals(denominator, that.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public String toString() {
        return isIntegral() ? numerator + "" : numerator + "/" + denominator;
    }

    @Override
    public int compareTo(BigRational b) {
        BigRational a = this;
        return a.numerator.multiply(b.denominator).compareTo(a.denominator.multiply(b.numerator));
    }

    public boolean isZero() {
        return numerator.signum() == 0;
    }

    public boolean isPositive() {
        return numerator.signum() > 0;
    }

    public boolean isNegative() {
        return numerator.signum() < 0;
    }

    public boolean isIntegral() {
        return BigInteger.ONE.equals(denominator);
    }

    public BigRational add(BigRational b) {
        BigRational a = this;
        return new BigRational(a.numerator.multiply(b.denominator).add(b.numerator.multiply(a.denominator)), a.denominator.multiply(b.denominator));
    }

    public BigRational subtract(BigRational b) {
        BigRational a = this;
        return new BigRational(a.numerator.multiply(b.denominator).subtract(b.numerator.multiply(a.denominator)), a.denominator.multiply(b.denominator));
    }

    public BigRational negate() {
        return new BigRational(numerator.negate(), denominator);
    }

    public BigRational multiply(BigRational b) {
        BigRational a = this;
        return new BigRational(a.numerator.multiply(b.numerator), a.denominator.multiply(b.denominator));
    }

    public BigRational divide(BigRational b) {
        if (b.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        BigRational a = this;
        return new BigRational(a.numerator.multiply(b.denominator), a.denominator.multiply(b.numerator));
    }

    public BigRational reciprocal() {
        return new BigRational(denominator, numerator);
    }

    public BigRational floor() {
        BigDecimal d = new BigDecimal(numerator).divide(new BigDecimal(denominator), 0, RoundingMode.FLOOR);
        return new BigRational(d.toBigInteger(), BigInteger.ONE);
    }

    public BigRational ceil() {
        BigDecimal d = new BigDecimal(numerator).divide(new BigDecimal(denominator), 0, RoundingMode.CEILING);
        return new BigRational(d.toBigInteger(), BigInteger.ONE);
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public double doubleValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), 32, RoundingMode.HALF_EVEN).doubleValue();
    }

    public long longValue() {
        return numerator.divide(denominator).longValue();
    }

    public long longValueExact() {
        if (!isIntegral()) {
            throw new ArithmeticException("Not an integral number");
        }
        return numerator.longValueExact();
    }
}
