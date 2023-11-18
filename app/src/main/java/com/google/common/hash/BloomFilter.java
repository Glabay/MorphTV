package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.primitives.Longs;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.annotation.Nullable;

@Beta
public final class BloomFilter<T> implements Predicate<T>, Serializable {
    private static final Strategy DEFAULT_STRATEGY = BloomFilterStrategies.MURMUR128_MITZ_64;
    private final BitArray bits;
    private final Funnel<? super T> funnel;
    private final int numHashFunctions;
    private final Strategy strategy;

    private static class SerialForm<T> implements Serializable {
        private static final long serialVersionUID = 1;
        final long[] data;
        final Funnel<? super T> funnel;
        final int numHashFunctions;
        final Strategy strategy;

        SerialForm(BloomFilter<T> bloomFilter) {
            this.data = bloomFilter.bits.data;
            this.numHashFunctions = bloomFilter.numHashFunctions;
            this.funnel = bloomFilter.funnel;
            this.strategy = bloomFilter.strategy;
        }

        Object readResolve() {
            return new BloomFilter(new BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
        }
    }

    interface Strategy extends Serializable {
        <T> boolean mightContain(T t, Funnel<? super T> funnel, int i, BitArray bitArray);

        int ordinal();

        <T> boolean put(T t, Funnel<? super T> funnel, int i, BitArray bitArray);
    }

    private BloomFilter(BitArray bitArray, int i, Funnel<? super T> funnel, Strategy strategy) {
        Preconditions.checkArgument(i > 0, "numHashFunctions (%s) must be > 0", Integer.valueOf(i));
        Preconditions.checkArgument(i <= 255, "numHashFunctions (%s) must be <= 255", Integer.valueOf(i));
        this.bits = (BitArray) Preconditions.checkNotNull(bitArray);
        this.numHashFunctions = i;
        this.funnel = (Funnel) Preconditions.checkNotNull(funnel);
        this.strategy = (Strategy) Preconditions.checkNotNull(strategy);
    }

    public BloomFilter<T> copy() {
        return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
    }

    public boolean mightContain(T t) {
        return this.strategy.mightContain(t, this.funnel, this.numHashFunctions, this.bits);
    }

    @Deprecated
    public boolean apply(T t) {
        return mightContain(t);
    }

    public boolean put(T t) {
        return this.strategy.put(t, this.funnel, this.numHashFunctions, this.bits);
    }

    public double expectedFpp() {
        return Math.pow(((double) this.bits.bitCount()) / ((double) bitSize()), (double) this.numHashFunctions);
    }

    @VisibleForTesting
    long bitSize() {
        return this.bits.bitSize();
    }

    public boolean isCompatible(BloomFilter<T> bloomFilter) {
        Preconditions.checkNotNull(bloomFilter);
        return (this != bloomFilter && this.numHashFunctions == bloomFilter.numHashFunctions && bitSize() == bloomFilter.bitSize() && this.strategy.equals(bloomFilter.strategy) && this.funnel.equals(bloomFilter.funnel) != null) ? true : null;
    }

    public void putAll(BloomFilter<T> bloomFilter) {
        Preconditions.checkNotNull(bloomFilter);
        Preconditions.checkArgument(this != bloomFilter, "Cannot combine a BloomFilter with itself.");
        Preconditions.checkArgument(this.numHashFunctions == bloomFilter.numHashFunctions, "BloomFilters must have the same number of hash functions (%s != %s)", Integer.valueOf(this.numHashFunctions), Integer.valueOf(bloomFilter.numHashFunctions));
        Preconditions.checkArgument(bitSize() == bloomFilter.bitSize(), "BloomFilters must have the same size underlying bit arrays (%s != %s)", Long.valueOf(bitSize()), Long.valueOf(bloomFilter.bitSize()));
        Preconditions.checkArgument(this.strategy.equals(bloomFilter.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, bloomFilter.strategy);
        Preconditions.checkArgument(this.funnel.equals(bloomFilter.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, bloomFilter.funnel);
        this.bits.putAll(bloomFilter.bits);
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BloomFilter)) {
            return false;
        }
        BloomFilter bloomFilter = (BloomFilter) obj;
        if (this.numHashFunctions != bloomFilter.numHashFunctions || !this.funnel.equals(bloomFilter.funnel) || !this.bits.equals(bloomFilter.bits) || this.strategy.equals(bloomFilter.strategy) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits);
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int i, double d) {
        return create(funnel, i, d, DEFAULT_STRATEGY);
    }

    @VisibleForTesting
    static <T> BloomFilter<T> create(Funnel<? super T> funnel, int i, double d, Strategy strategy) {
        Preconditions.checkNotNull(funnel);
        Preconditions.checkArgument(i >= 0, "Expected insertions (%s) must be >= 0", Integer.valueOf(i));
        Preconditions.checkArgument(d > 0.0d, "False positive probability (%s) must be > 0.0", Double.valueOf(d));
        Preconditions.checkArgument(d < 1.0d, "False positive probability (%s) must be < 1.0", Double.valueOf(d));
        Preconditions.checkNotNull(strategy);
        if (i == 0) {
            i = 1;
        }
        long j = (long) i;
        long optimalNumOfBits = optimalNumOfBits(j, d);
        try {
            return new BloomFilter(new BitArray(optimalNumOfBits), optimalNumOfHashFunctions(j, optimalNumOfBits), funnel, strategy);
        } catch (Funnel<? super T> funnel2) {
            strategy = new StringBuilder();
            strategy.append("Could not create BloomFilter of ");
            strategy.append(optimalNumOfBits);
            strategy.append(" bits");
            throw new IllegalArgumentException(strategy.toString(), funnel2);
        }
    }

    public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int i) {
        return create(funnel, i, 0.03d);
    }

    @VisibleForTesting
    static int optimalNumOfHashFunctions(long j, long j2) {
        return Math.max(1, (int) Math.round((((double) j2) / ((double) j)) * Math.log(Longs.MAX_POWER_OF_TWO)));
    }

    @VisibleForTesting
    static long optimalNumOfBits(long j, double d) {
        if (d == 0.0d) {
            d = Double.MIN_VALUE;
        }
        return (long) ((((double) (-j)) * Math.log(d)) / (Math.log(2.0d) * Math.log(2.0d)));
    }

    private Object writeReplace() {
        return new SerialForm(this);
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeByte(SignedBytes.checkedCast((long) this.strategy.ordinal()));
        dataOutputStream.writeByte(UnsignedBytes.checkedCast((long) this.numHashFunctions));
        dataOutputStream.writeInt(this.bits.data.length);
        for (long writeLong : this.bits.data) {
            dataOutputStream.writeLong(writeLong);
        }
    }

    public static <T> BloomFilter<T> readFrom(InputStream inputStream, Funnel<T> funnel) throws IOException {
        int toInt;
        int readInt;
        StringBuilder stringBuilder;
        IOException iOException;
        Preconditions.checkNotNull(inputStream, "InputStream");
        Preconditions.checkNotNull(funnel, "Funnel");
        try {
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            inputStream = dataInputStream.readByte();
            try {
                toInt = UnsignedBytes.toInt(dataInputStream.readByte());
                try {
                    readInt = dataInputStream.readInt();
                } catch (RuntimeException e) {
                    funnel = e;
                    readInt = -1;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to deserialize BloomFilter from InputStream. strategyOrdinal: ");
                    stringBuilder.append(inputStream);
                    stringBuilder.append(" numHashFunctions: ");
                    stringBuilder.append(toInt);
                    stringBuilder.append(" dataLength: ");
                    stringBuilder.append(readInt);
                    iOException = new IOException(stringBuilder.toString());
                    iOException.initCause(funnel);
                    throw iOException;
                }
            } catch (RuntimeException e2) {
                funnel = e2;
                toInt = -1;
                readInt = -1;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to deserialize BloomFilter from InputStream. strategyOrdinal: ");
                stringBuilder.append(inputStream);
                stringBuilder.append(" numHashFunctions: ");
                stringBuilder.append(toInt);
                stringBuilder.append(" dataLength: ");
                stringBuilder.append(readInt);
                iOException = new IOException(stringBuilder.toString());
                iOException.initCause(funnel);
                throw iOException;
            }
            try {
                Strategy strategy = BloomFilterStrategies.values()[inputStream];
                long[] jArr = new long[readInt];
                for (int i = 0; i < jArr.length; i++) {
                    jArr[i] = dataInputStream.readLong();
                }
                return new BloomFilter(new BitArray(jArr), toInt, funnel, strategy);
            } catch (RuntimeException e3) {
                funnel = e3;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to deserialize BloomFilter from InputStream. strategyOrdinal: ");
                stringBuilder.append(inputStream);
                stringBuilder.append(" numHashFunctions: ");
                stringBuilder.append(toInt);
                stringBuilder.append(" dataLength: ");
                stringBuilder.append(readInt);
                iOException = new IOException(stringBuilder.toString());
                iOException.initCause(funnel);
                throw iOException;
            }
        } catch (RuntimeException e4) {
            funnel = e4;
            inputStream = -1;
            toInt = -1;
            readInt = -1;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to deserialize BloomFilter from InputStream. strategyOrdinal: ");
            stringBuilder.append(inputStream);
            stringBuilder.append(" numHashFunctions: ");
            stringBuilder.append(toInt);
            stringBuilder.append(" dataLength: ");
            stringBuilder.append(readInt);
            iOException = new IOException(stringBuilder.toString());
            iOException.initCause(funnel);
            throw iOException;
        }
    }
}
