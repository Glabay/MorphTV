package com.google.common.util.concurrent;

import de.timroes.axmlrpc.XMLRPCClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class AtomicDouble extends Number implements Serializable {
    private static final long serialVersionUID = 0;
    private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, XMLRPCClient.VALUE);
    private volatile transient long value;

    public AtomicDouble(double d) {
        this.value = Double.doubleToRawLongBits(d);
    }

    public final double get() {
        return Double.longBitsToDouble(this.value);
    }

    public final void set(double d) {
        this.value = Double.doubleToRawLongBits(d);
    }

    public final void lazySet(double d) {
        set(d);
    }

    public final double getAndSet(double d) {
        return Double.longBitsToDouble(updater.getAndSet(this, Double.doubleToRawLongBits(d)));
    }

    public final boolean compareAndSet(double d, double d2) {
        return updater.compareAndSet(this, Double.doubleToRawLongBits(d), Double.doubleToRawLongBits(d2));
    }

    public final boolean weakCompareAndSet(double d, double d2) {
        return updater.weakCompareAndSet(this, Double.doubleToRawLongBits(d), Double.doubleToRawLongBits(d2));
    }

    public final double getAndAdd(double d) {
        double longBitsToDouble;
        long j;
        do {
            j = this.value;
            longBitsToDouble = Double.longBitsToDouble(j);
        } while (!updater.compareAndSet(this, j, Double.doubleToRawLongBits(longBitsToDouble + d)));
        return longBitsToDouble;
    }

    public final double addAndGet(double d) {
        double longBitsToDouble;
        long j;
        do {
            j = this.value;
            longBitsToDouble = Double.longBitsToDouble(j) + d;
        } while (!updater.compareAndSet(this, j, Double.doubleToRawLongBits(longBitsToDouble)));
        return longBitsToDouble;
    }

    public String toString() {
        return Double.toString(get());
    }

    public int intValue() {
        return (int) get();
    }

    public long longValue() {
        return (long) get();
    }

    public float floatValue() {
        return (float) get();
    }

    public double doubleValue() {
        return get();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeDouble(get());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        set(objectInputStream.readDouble());
    }
}
