package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Beta
public abstract class Striped<L> {
    private static final int ALL_SET = -1;
    private static final int LARGE_LAZY_CUTOFF = 1024;
    private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new C12595();

    /* renamed from: com.google.common.util.concurrent.Striped$1 */
    static class C12551 implements Supplier<Lock> {
        C12551() {
        }

        public Lock get() {
            return new PaddedLock();
        }
    }

    /* renamed from: com.google.common.util.concurrent.Striped$2 */
    static class C12562 implements Supplier<Lock> {
        C12562() {
        }

        public Lock get() {
            return new ReentrantLock(false);
        }
    }

    /* renamed from: com.google.common.util.concurrent.Striped$5 */
    static class C12595 implements Supplier<ReadWriteLock> {
        C12595() {
        }

        public ReadWriteLock get() {
            return new ReentrantReadWriteLock();
        }
    }

    private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
        final int mask;

        PowerOfTwoStriped(int i) {
            super();
            Preconditions.checkArgument(i > 0, "Stripes must be positive");
            if (i > 1073741824) {
                i = -1;
            } else {
                i = Striped.ceilToPowerOfTwo(i) - 1;
            }
            this.mask = i;
        }

        final int indexFor(Object obj) {
            return Striped.smear(obj.hashCode()) & this.mask;
        }

        public final L get(Object obj) {
            return getAt(indexFor(obj));
        }
    }

    private static class CompactStriped<L> extends PowerOfTwoStriped<L> {
        private final Object[] array;

        private CompactStriped(int i, Supplier<L> supplier) {
            super(i);
            Preconditions.checkArgument(i <= 1073741824 ? 1 : 0, "Stripes must be <= 2^30)");
            this.array = new Object[(this.mask + 1)];
            for (int i2 = 0; i2 < this.array.length; i2++) {
                this.array[i2] = supplier.get();
            }
        }

        public L getAt(int i) {
            return this.array[i];
        }

        public int size() {
            return this.array.length;
        }
    }

    @VisibleForTesting
    static class LargeLazyStriped<L> extends PowerOfTwoStriped<L> {
        final ConcurrentMap<Integer, L> locks;
        final int size;
        final Supplier<L> supplier;

        LargeLazyStriped(int i, Supplier<L> supplier) {
            super(i);
            this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
            this.supplier = supplier;
            this.locks = new MapMaker().weakValues().makeMap();
        }

        public L getAt(int i) {
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(i, size());
            }
            L l = this.locks.get(Integer.valueOf(i));
            if (l != null) {
                return l;
            }
            Object obj = this.supplier.get();
            return MoreObjects.firstNonNull(this.locks.putIfAbsent(Integer.valueOf(i), obj), obj);
        }

        public int size() {
            return this.size;
        }
    }

    private static class PaddedLock extends ReentrantLock {
        long q1;
        long q2;
        long q3;

        PaddedLock() {
            super(false);
        }
    }

    private static class PaddedSemaphore extends Semaphore {
        long q1;
        long q2;
        long q3;

        PaddedSemaphore(int i) {
            super(i, false);
        }
    }

    @VisibleForTesting
    static class SmallLazyStriped<L> extends PowerOfTwoStriped<L> {
        final AtomicReferenceArray<ArrayReference<? extends L>> locks;
        final ReferenceQueue<L> queue = new ReferenceQueue();
        final int size;
        final Supplier<L> supplier;

        private static final class ArrayReference<L> extends WeakReference<L> {
            final int index;

            ArrayReference(L l, int i, ReferenceQueue<L> referenceQueue) {
                super(l, referenceQueue);
                this.index = i;
            }
        }

        SmallLazyStriped(int i, Supplier<L> supplier) {
            super(i);
            this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
            this.locks = new AtomicReferenceArray(this.size);
            this.supplier = supplier;
        }

        public L getAt(int i) {
            L l;
            if (this.size != Integer.MAX_VALUE) {
                Preconditions.checkElementIndex(i, size());
            }
            Object obj = (ArrayReference) this.locks.get(i);
            if (obj == null) {
                l = null;
            } else {
                l = obj.get();
            }
            if (l != null) {
                return l;
            }
            l = this.supplier.get();
            ArrayReference arrayReference = new ArrayReference(l, i, this.queue);
            while (!this.locks.compareAndSet(i, obj, arrayReference)) {
                L l2;
                ArrayReference arrayReference2 = (ArrayReference) this.locks.get(i);
                if (arrayReference2 == null) {
                    l2 = null;
                    continue;
                } else {
                    l2 = arrayReference2.get();
                    continue;
                }
                if (l2 != null) {
                    return l2;
                }
            }
            drainQueue();
            return l;
        }

        private void drainQueue() {
            while (true) {
                Reference poll = this.queue.poll();
                if (poll != null) {
                    ArrayReference arrayReference = (ArrayReference) poll;
                    this.locks.compareAndSet(arrayReference.index, arrayReference, null);
                } else {
                    return;
                }
            }
        }

        public int size() {
            return this.size;
        }
    }

    private static int smear(int i) {
        i ^= (i >>> 20) ^ (i >>> 12);
        return (i >>> 4) ^ ((i >>> 7) ^ i);
    }

    public abstract L get(Object obj);

    public abstract L getAt(int i);

    abstract int indexFor(Object obj);

    public abstract int size();

    private Striped() {
    }

    public Iterable<L> bulkGet(Iterable<?> iterable) {
        iterable = Iterables.toArray(iterable, Object.class);
        if (iterable.length == 0) {
            return ImmutableList.of();
        }
        int i;
        int[] iArr = new int[iterable.length];
        for (i = 0; i < iterable.length; i++) {
            iArr[i] = indexFor(iterable[i]);
        }
        Arrays.sort(iArr);
        i = iArr[0];
        iterable[0] = getAt(i);
        for (int i2 = 1; i2 < iterable.length; i2++) {
            int i3 = iArr[i2];
            if (i3 == i) {
                iterable[i2] = iterable[i2 - 1];
            } else {
                iterable[i2] = getAt(i3);
                i = i3;
            }
        }
        return Collections.unmodifiableList(Arrays.asList(iterable));
    }

    public static Striped<Lock> lock(int i) {
        return new CompactStriped(i, new C12551());
    }

    public static Striped<Lock> lazyWeakLock(int i) {
        return lazy(i, new C12562());
    }

    private static <L> Striped<L> lazy(int i, Supplier<L> supplier) {
        return i < 1024 ? new SmallLazyStriped(i, supplier) : new LargeLazyStriped(i, supplier);
    }

    public static Striped<Semaphore> semaphore(int i, final int i2) {
        return new CompactStriped(i, new Supplier<Semaphore>() {
            public Semaphore get() {
                return new PaddedSemaphore(i2);
            }
        });
    }

    public static Striped<Semaphore> lazyWeakSemaphore(int i, final int i2) {
        return lazy(i, new Supplier<Semaphore>() {
            public Semaphore get() {
                return new Semaphore(i2, false);
            }
        });
    }

    public static Striped<ReadWriteLock> readWriteLock(int i) {
        return new CompactStriped(i, READ_WRITE_LOCK_SUPPLIER);
    }

    public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int i) {
        return lazy(i, READ_WRITE_LOCK_SUPPLIER);
    }

    private static int ceilToPowerOfTwo(int i) {
        return 1 << IntMath.log2(i, RoundingMode.CEILING);
    }
}
