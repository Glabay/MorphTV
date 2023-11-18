package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.math.BigInteger;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
public final class UnsignedLong extends Number implements Comparable<UnsignedLong>, Serializable {
    public static final UnsignedLong MAX_VALUE = new UnsignedLong(-1);
    public static final UnsignedLong ONE = new UnsignedLong(1);
    private static final long UNSIGNED_MASK = Long.MAX_VALUE;
    public static final UnsignedLong ZERO = new UnsignedLong(0);
    private final long value;

    private UnsignedLong(long j) {
        this.value = j;
    }

    public static UnsignedLong fromLongBits(long j) {
        return new UnsignedLong(j);
    }

    public static UnsignedLong valueOf(long j) {
        Preconditions.checkArgument(j >= 0, "value (%s) is outside the range for an unsigned long value", Long.valueOf(j));
        return fromLongBits(j);
    }

    public static UnsignedLong valueOf(BigInteger bigInteger) {
        Preconditions.checkNotNull(bigInteger);
        boolean z = bigInteger.signum() >= 0 && bigInteger.bitLength() <= 64;
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned long value", bigInteger);
        return fromLongBits(bigInteger.longValue());
    }

    public static UnsignedLong valueOf(String str) {
        return valueOf(str, 10);
    }

    public static UnsignedLong valueOf(String str, int i) {
        return fromLongBits(UnsignedLongs.parseUnsignedLong(str, i));
    }

    public UnsignedLong plus(UnsignedLong unsignedLong) {
        return fromLongBits(this.value + ((UnsignedLong) Preconditions.checkNotNull(unsignedLong)).value);
    }

    public UnsignedLong minus(UnsignedLong unsignedLong) {
        return fromLongBits(this.value - ((UnsignedLong) Preconditions.checkNotNull(unsignedLong)).value);
    }

    @CheckReturnValue
    public UnsignedLong times(UnsignedLong unsignedLong) {
        return fromLongBits(this.value * ((UnsignedLong) Preconditions.checkNotNull(unsignedLong)).value);
    }

    @CheckReturnValue
    public UnsignedLong dividedBy(UnsignedLong unsignedLong) {
        return fromLongBits(UnsignedLongs.divide(this.value, ((UnsignedLong) Preconditions.checkNotNull(unsignedLong)).value));
    }

    @CheckReturnValue
    public UnsignedLong mod(UnsignedLong unsignedLong) {
        return fromLongBits(UnsignedLongs.remainder(this.value, ((UnsignedLong) Preconditions.checkNotNull(unsignedLong)).value));
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return this.value;
    }

    public float floatValue() {
        float f = (float) (this.value & Long.MAX_VALUE);
        return this.value < 0 ? f + 9.223372E18f : f;
    }

    public double doubleValue() {
        double d = (double) (this.value & Long.MAX_VALUE);
        return this.value < 0 ? d + 9.223372036854776E18d : d;
    }

    public BigInteger bigIntegerValue() {
        BigInteger valueOf = BigInteger.valueOf(this.value & Long.MAX_VALUE);
        return this.value < 0 ? valueOf.setBit(63) : valueOf;
    }

    public int compareTo(UnsignedLong unsignedLong) {
        Preconditions.checkNotNull(unsignedLong);
        return UnsignedLongs.compare(this.value, unsignedLong.value);
    }

    public int hashCode() {
        return Longs.hashCode(this.value);
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof UnsignedLong)) {
            return false;
        }
        if (this.value == ((UnsignedLong) obj).value) {
            z = true;
        }
        return z;
    }

    public String toString() {
        return UnsignedLongs.toString(this.value);
    }

    public String toString(int i) {
        return UnsignedLongs.toString(this.value, i);
    }
}
