package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.NoSuchElementException;

@GwtCompatible
@Beta
public abstract class DiscreteDomain<C extends Comparable> {

    private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
        private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
        private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
        private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
        private static final long serialVersionUID = 0;

        public String toString() {
            return "DiscreteDomain.bigIntegers()";
        }

        private BigIntegerDomain() {
        }

        public BigInteger next(BigInteger bigInteger) {
            return bigInteger.add(BigInteger.ONE);
        }

        public BigInteger previous(BigInteger bigInteger) {
            return bigInteger.subtract(BigInteger.ONE);
        }

        public long distance(BigInteger bigInteger, BigInteger bigInteger2) {
            return bigInteger2.subtract(bigInteger).max(MIN_LONG).min(MAX_LONG).longValue();
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
        private static final IntegerDomain INSTANCE = new IntegerDomain();
        private static final long serialVersionUID = 0;

        public String toString() {
            return "DiscreteDomain.integers()";
        }

        private IntegerDomain() {
        }

        public Integer next(Integer num) {
            num = num.intValue();
            if (num == Integer.MAX_VALUE) {
                return null;
            }
            return Integer.valueOf(num + 1);
        }

        public Integer previous(Integer num) {
            num = num.intValue();
            if (num == Integer.MIN_VALUE) {
                return null;
            }
            return Integer.valueOf(num - 1);
        }

        public long distance(Integer num, Integer num2) {
            return ((long) num2.intValue()) - ((long) num.intValue());
        }

        public Integer minValue() {
            return Integer.valueOf(Integer.MIN_VALUE);
        }

        public Integer maxValue() {
            return Integer.valueOf(Integer.MAX_VALUE);
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
        private static final LongDomain INSTANCE = new LongDomain();
        private static final long serialVersionUID = 0;

        public String toString() {
            return "DiscreteDomain.longs()";
        }

        private LongDomain() {
        }

        public Long next(Long l) {
            long longValue = l.longValue();
            if (longValue == Long.MAX_VALUE) {
                return null;
            }
            return Long.valueOf(longValue + 1);
        }

        public Long previous(Long l) {
            long longValue = l.longValue();
            if (longValue == Long.MIN_VALUE) {
                return null;
            }
            return Long.valueOf(longValue - 1);
        }

        public long distance(Long l, Long l2) {
            long longValue = l2.longValue() - l.longValue();
            if (l2.longValue() <= l.longValue() || longValue >= 0) {
                return (l2.longValue() >= l.longValue() || longValue <= 0) ? longValue : 0;
            } else {
                return -1;
            }
        }

        public Long minValue() {
            return Long.valueOf(Long.MIN_VALUE);
        }

        public Long maxValue() {
            return Long.valueOf(Long.MAX_VALUE);
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    public abstract long distance(C c, C c2);

    public abstract C next(C c);

    public abstract C previous(C c);

    public static DiscreteDomain<Integer> integers() {
        return IntegerDomain.INSTANCE;
    }

    public static DiscreteDomain<Long> longs() {
        return LongDomain.INSTANCE;
    }

    public static DiscreteDomain<BigInteger> bigIntegers() {
        return BigIntegerDomain.INSTANCE;
    }

    protected DiscreteDomain() {
    }

    public C minValue() {
        throw new NoSuchElementException();
    }

    public C maxValue() {
        throw new NoSuchElementException();
    }
}
