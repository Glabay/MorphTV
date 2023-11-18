package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.math.BigInteger;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import net.lingala.zip4j.util.InternalZipConstants;

@GwtCompatible(emulated = true)
public final class UnsignedInteger extends Number implements Comparable<UnsignedInteger> {
    public static final UnsignedInteger MAX_VALUE = fromIntBits(-1);
    public static final UnsignedInteger ONE = fromIntBits(1);
    public static final UnsignedInteger ZERO = fromIntBits(0);
    private final int value;

    private UnsignedInteger(int i) {
        this.value = i & -1;
    }

    public static UnsignedInteger fromIntBits(int i) {
        return new UnsignedInteger(i);
    }

    public static UnsignedInteger valueOf(long j) {
        Preconditions.checkArgument((j & InternalZipConstants.ZIP_64_LIMIT) == j, "value (%s) is outside the range for an unsigned integer value", Long.valueOf(j));
        return fromIntBits((int) j);
    }

    public static UnsignedInteger valueOf(BigInteger bigInteger) {
        Preconditions.checkNotNull(bigInteger);
        boolean z = bigInteger.signum() >= 0 && bigInteger.bitLength() <= 32;
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned integer value", bigInteger);
        return fromIntBits(bigInteger.intValue());
    }

    public static UnsignedInteger valueOf(String str) {
        return valueOf(str, 10);
    }

    public static UnsignedInteger valueOf(String str, int i) {
        return fromIntBits(UnsignedInts.parseUnsignedInt(str, i));
    }

    @CheckReturnValue
    public UnsignedInteger plus(UnsignedInteger unsignedInteger) {
        return fromIntBits(this.value + ((UnsignedInteger) Preconditions.checkNotNull(unsignedInteger)).value);
    }

    @CheckReturnValue
    public UnsignedInteger minus(UnsignedInteger unsignedInteger) {
        return fromIntBits(this.value - ((UnsignedInteger) Preconditions.checkNotNull(unsignedInteger)).value);
    }

    @CheckReturnValue
    @GwtIncompatible("Does not truncate correctly")
    public UnsignedInteger times(UnsignedInteger unsignedInteger) {
        return fromIntBits(this.value * ((UnsignedInteger) Preconditions.checkNotNull(unsignedInteger)).value);
    }

    @CheckReturnValue
    public UnsignedInteger dividedBy(UnsignedInteger unsignedInteger) {
        return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger) Preconditions.checkNotNull(unsignedInteger)).value));
    }

    @CheckReturnValue
    public UnsignedInteger mod(UnsignedInteger unsignedInteger) {
        return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger) Preconditions.checkNotNull(unsignedInteger)).value));
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return UnsignedInts.toLong(this.value);
    }

    public float floatValue() {
        return (float) longValue();
    }

    public double doubleValue() {
        return (double) longValue();
    }

    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(longValue());
    }

    public int compareTo(UnsignedInteger unsignedInteger) {
        Preconditions.checkNotNull(unsignedInteger);
        return UnsignedInts.compare(this.value, unsignedInteger.value);
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof UnsignedInteger)) {
            return false;
        }
        if (this.value == ((UnsignedInteger) obj).value) {
            z = true;
        }
        return z;
    }

    public String toString() {
        return toString(10);
    }

    public String toString(int i) {
        return UnsignedInts.toString(this.value, i);
    }
}
