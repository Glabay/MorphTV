package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache.SimpleStatsCounter;
import com.google.common.cache.AbstractCache.StatsCounter;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

@GwtCompatible(emulated = true)
class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final Queue<? extends Object> DISCARDING_QUEUE = new C08352();
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final ValueReference<Object, Object> UNSET = new C08341();
    static final Logger logger = Logger.getLogger(LocalCache.class.getName());
    final int concurrencyLevel;
    @Nullable
    final CacheLoader<? super K, V> defaultLoader;
    final EntryFactory entryFactory;
    Set<Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final StatsCounter globalStatsCounter;
    final Equivalence<Object> keyEquivalence;
    Set<K> keySet;
    final Strength keyStrength;
    final long maxWeight;
    final long refreshNanos;
    final RemovalListener<K, V> removalListener;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence;
    final Strength valueStrength;
    Collection<V> values;
    final Weigher<K, V> weigher;

    interface ValueReference<K, V> {
        ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, @Nullable V v, ReferenceEntry<K, V> referenceEntry);

        @Nullable
        V get();

        @Nullable
        ReferenceEntry<K, V> getEntry();

        int getWeight();

        boolean isActive();

        boolean isLoading();

        void notifyNewValue(@Nullable V v);

        V waitForValue() throws ExecutionException;
    }

    /* renamed from: com.google.common.cache.LocalCache$1 */
    static class C08341 implements ValueReference<Object, Object> {
        public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> referenceQueue, @Nullable Object obj, ReferenceEntry<Object, Object> referenceEntry) {
            return this;
        }

        public Object get() {
            return null;
        }

        public ReferenceEntry<Object, Object> getEntry() {
            return null;
        }

        public int getWeight() {
            return 0;
        }

        public boolean isActive() {
            return false;
        }

        public boolean isLoading() {
            return false;
        }

        public void notifyNewValue(Object obj) {
        }

        public Object waitForValue() {
            return null;
        }

        C08341() {
        }
    }

    /* renamed from: com.google.common.cache.LocalCache$2 */
    static class C08352 extends AbstractQueue<Object> {
        public boolean offer(Object obj) {
            return true;
        }

        public Object peek() {
            return null;
        }

        public Object poll() {
            return null;
        }

        public int size() {
            return 0;
        }

        C08352() {
        }

        public Iterator<Object> iterator() {
            return ImmutableSet.of().iterator();
        }
    }

    abstract class AbstractCacheSet<T> extends AbstractSet<T> {
        final ConcurrentMap<?, ?> map;

        AbstractCacheSet(ConcurrentMap<?, ?> concurrentMap) {
            this.map = concurrentMap;
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public void clear() {
            this.map.clear();
        }
    }

    interface ReferenceEntry<K, V> {
        long getAccessTime();

        int getHash();

        @Nullable
        K getKey();

        @Nullable
        ReferenceEntry<K, V> getNext();

        ReferenceEntry<K, V> getNextInAccessQueue();

        ReferenceEntry<K, V> getNextInWriteQueue();

        ReferenceEntry<K, V> getPreviousInAccessQueue();

        ReferenceEntry<K, V> getPreviousInWriteQueue();

        ValueReference<K, V> getValueReference();

        long getWriteTime();

        void setAccessTime(long j);

        void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry);

        void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry);

        void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry);

        void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry);

        void setValueReference(ValueReference<K, V> valueReference);

        void setWriteTime(long j);
    }

    static abstract class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
        AbstractReferenceEntry() {
        }

        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }

        public int getHash() {
            throw new UnsupportedOperationException();
        }

        public K getKey() {
            throw new UnsupportedOperationException();
        }

        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        public void setAccessTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        public void setWriteTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
    }

    static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new C08361();

        /* renamed from: com.google.common.cache.LocalCache$AccessQueue$1 */
        class C08361 extends AbstractReferenceEntry<K, V> {
            ReferenceEntry<K, V> nextAccess = this;
            ReferenceEntry<K, V> previousAccess = this;

            public long getAccessTime() {
                return Long.MAX_VALUE;
            }

            public void setAccessTime(long j) {
            }

            C08361() {
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return this.nextAccess;
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
                this.nextAccess = referenceEntry;
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return this.previousAccess;
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
                this.previousAccess = referenceEntry;
            }
        }

        AccessQueue() {
        }

        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry.getNextInAccessQueue());
            LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), referenceEntry);
            LocalCache.connectAccessOrder(referenceEntry, this.head);
            return true;
        }

        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue();
            return nextInAccessQueue == this.head ? null : nextInAccessQueue;
        }

        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> nextInAccessQueue = this.head.getNextInAccessQueue();
            if (nextInAccessQueue == this.head) {
                return null;
            }
            remove(nextInAccessQueue);
            return nextInAccessQueue;
        }

        public boolean remove(Object obj) {
            ReferenceEntry referenceEntry = (ReferenceEntry) obj;
            ReferenceEntry previousInAccessQueue = referenceEntry.getPreviousInAccessQueue();
            ReferenceEntry nextInAccessQueue = referenceEntry.getNextInAccessQueue();
            LocalCache.connectAccessOrder(previousInAccessQueue, nextInAccessQueue);
            LocalCache.nullifyAccessOrder(referenceEntry);
            return nextInAccessQueue != NullEntry.INSTANCE ? true : null;
        }

        public boolean contains(Object obj) {
            return ((ReferenceEntry) obj).getNextInAccessQueue() != NullEntry.INSTANCE ? true : null;
        }

        public boolean isEmpty() {
            return this.head.getNextInAccessQueue() == this.head;
        }

        public int size() {
            int i = 0;
            for (ReferenceEntry nextInAccessQueue = this.head.getNextInAccessQueue(); nextInAccessQueue != this.head; nextInAccessQueue = nextInAccessQueue.getNextInAccessQueue()) {
                i++;
            }
            return i;
        }

        public void clear() {
            ReferenceEntry nextInAccessQueue = this.head.getNextInAccessQueue();
            while (nextInAccessQueue != this.head) {
                ReferenceEntry nextInAccessQueue2 = nextInAccessQueue.getNextInAccessQueue();
                LocalCache.nullifyAccessOrder(nextInAccessQueue);
                nextInAccessQueue = nextInAccessQueue2;
            }
            this.head.setNextInAccessQueue(this.head);
            this.head.setPreviousInAccessQueue(this.head);
        }

        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    referenceEntry = referenceEntry.getNextInAccessQueue();
                    return referenceEntry == AccessQueue.this.head ? null : referenceEntry;
                }
            };
        }
    }

    enum EntryFactory {
        STRONG {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry(k, i, referenceEntry);
            }
        },
        STRONG_ACCESS {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessEntry(k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyAccessEntry(referenceEntry, segment);
                return segment;
            }
        },
        STRONG_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongWriteEntry(k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyWriteEntry(referenceEntry, segment);
                return segment;
            }
        },
        STRONG_ACCESS_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongAccessWriteEntry(k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyAccessEntry(referenceEntry, segment);
                copyWriteEntry(referenceEntry, segment);
                return segment;
            }
        },
        WEAK {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        },
        WEAK_ACCESS {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyAccessEntry(referenceEntry, segment);
                return segment;
            }
        },
        WEAK_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakWriteEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyWriteEntry(referenceEntry, segment);
                return segment;
            }
        },
        WEAK_ACCESS_WRITE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakAccessWriteEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyAccessEntry(referenceEntry, segment);
                copyWriteEntry(referenceEntry, segment);
                return segment;
            }
        };
        
        static final int ACCESS_MASK = 1;
        static final int WEAK_MASK = 4;
        static final int WRITE_MASK = 2;
        static final EntryFactory[] factories = null;

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry);

        static {
            factories = new EntryFactory[]{STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE};
        }

        static EntryFactory getFactory(Strength strength, boolean z, boolean z2) {
            int i = 0;
            strength = (strength == Strength.WEAK ? 4 : null) | z;
            if (z2) {
                i = 2;
            }
            return factories[strength | i];
        }

        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            return newEntry(segment, referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry2);
        }

        <K, V> void copyAccessEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setAccessTime(referenceEntry.getAccessTime());
            LocalCache.connectAccessOrder(referenceEntry.getPreviousInAccessQueue(), referenceEntry2);
            LocalCache.connectAccessOrder(referenceEntry2, referenceEntry.getNextInAccessQueue());
            LocalCache.nullifyAccessOrder(referenceEntry);
        }

        <K, V> void copyWriteEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setWriteTime(referenceEntry.getWriteTime());
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry2);
            LocalCache.connectWriteOrder(referenceEntry2, referenceEntry.getNextInWriteQueue());
            LocalCache.nullifyWriteOrder(referenceEntry);
        }
    }

    abstract class HashIterator<T> implements Iterator<T> {
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        WriteThroughEntry lastReturned;
        ReferenceEntry<K, V> nextEntry;
        WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex = -1;

        public abstract T next();

        HashIterator() {
            this.nextSegmentIndex = LocalCache.this.segments.length - 1;
            advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (!nextInChain() && !nextInTable()) {
                while (this.nextSegmentIndex >= 0) {
                    Segment[] segmentArr = LocalCache.this.segments;
                    int i = this.nextSegmentIndex;
                    this.nextSegmentIndex = i - 1;
                    this.currentSegment = segmentArr[i];
                    if (this.currentSegment.count != 0) {
                        this.currentTable = this.currentSegment.table;
                        this.nextTableIndex = this.currentTable.length() - 1;
                        if (nextInTable()) {
                            return;
                        }
                    }
                }
            }
        }

        boolean nextInChain() {
            if (this.nextEntry != null) {
                do {
                    this.nextEntry = this.nextEntry.getNext();
                    if (this.nextEntry != null) {
                    }
                } while (!advanceTo(this.nextEntry));
                return true;
            }
            return false;
        }

        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                AtomicReferenceArray atomicReferenceArray = this.currentTable;
                int i = this.nextTableIndex;
                this.nextTableIndex = i - 1;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i);
                this.nextEntry = referenceEntry;
                if (referenceEntry != null && (advanceTo(this.nextEntry) || nextInChain())) {
                    return true;
                }
            }
            return false;
        }

        boolean advanceTo(ReferenceEntry<K, V> referenceEntry) {
            try {
                long read = LocalCache.this.ticker.read();
                Object key = referenceEntry.getKey();
                referenceEntry = LocalCache.this.getLiveValue(referenceEntry, read);
                if (referenceEntry != null) {
                    this.nextExternal = new WriteThroughEntry(key, referenceEntry);
                    referenceEntry = true;
                } else {
                    referenceEntry = null;
                }
                this.currentSegment.postReadCleanup();
                return referenceEntry;
            } catch (Throwable th) {
                this.currentSegment.postReadCleanup();
            }
        }

        public boolean hasNext() {
            return this.nextExternal != null;
        }

        WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            advance();
            return this.lastReturned;
        }

        public void remove() {
            Preconditions.checkState(this.lastReturned != null);
            LocalCache.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    final class EntryIterator extends HashIterator<Entry<K, V>> {
        EntryIterator() {
            super();
        }

        public Entry<K, V> next() {
            return nextEntry();
        }
    }

    final class EntrySet extends AbstractCacheSet<Entry<K, V>> {
        EntrySet(ConcurrentMap<?, ?> concurrentMap) {
            super(concurrentMap);
        }

        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object key = entry.getKey();
            if (key == null) {
                return false;
            }
            key = LocalCache.this.get(key);
            if (!(key == null || LocalCache.this.valueEquivalence.equivalent(entry.getValue(), key) == null)) {
                z = true;
            }
            return z;
        }

        public boolean remove(Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object key = entry.getKey();
            if (!(key == null || LocalCache.this.remove(key, entry.getValue()) == null)) {
                z = true;
            }
            return z;
        }
    }

    final class KeyIterator extends HashIterator<K> {
        KeyIterator() {
            super();
        }

        public K next() {
            return nextEntry().getKey();
        }
    }

    final class KeySet extends AbstractCacheSet<K> {
        KeySet(ConcurrentMap<?, ?> concurrentMap) {
            super(concurrentMap);
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public boolean contains(Object obj) {
            return this.map.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return this.map.remove(obj) != null ? true : null;
        }
    }

    static class ManualSerializationProxy<K, V> extends ForwardingCache<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        final int concurrencyLevel;
        transient Cache<K, V> delegate;
        final long expireAfterAccessNanos;
        final long expireAfterWriteNanos;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final CacheLoader<? super K, V> loader;
        final long maxWeight;
        final RemovalListener<? super K, ? super V> removalListener;
        final Ticker ticker;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;
        final Weigher<K, V> weigher;

        ManualSerializationProxy(LocalCache<K, V> localCache) {
            LocalCache<K, V> localCache2 = localCache;
            this(localCache2.keyStrength, localCache2.valueStrength, localCache2.keyEquivalence, localCache2.valueEquivalence, localCache2.expireAfterWriteNanos, localCache2.expireAfterAccessNanos, localCache2.maxWeight, localCache2.weigher, localCache2.concurrencyLevel, localCache2.removalListener, localCache2.ticker, localCache2.defaultLoader);
        }

        private ManualSerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, long j, long j2, long j3, Weigher<K, V> weigher, int i, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> cacheLoader) {
            this.keyStrength = strength;
            this.valueStrength = strength2;
            this.keyEquivalence = equivalence;
            this.valueEquivalence = equivalence2;
            this.expireAfterWriteNanos = j;
            this.expireAfterAccessNanos = j2;
            this.maxWeight = j3;
            this.weigher = weigher;
            this.concurrencyLevel = i;
            this.removalListener = removalListener;
            if (ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) {
                ticker = null;
            }
            this.ticker = ticker;
            this.loader = cacheLoader;
        }

        CacheBuilder<K, V> recreateCacheBuilder() {
            CacheBuilder<K, V> removalListener = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
            removalListener.strictParsing = false;
            if (this.expireAfterWriteNanos > 0) {
                removalListener.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
            }
            if (this.expireAfterAccessNanos > 0) {
                removalListener.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
            }
            if (this.weigher != OneWeigher.INSTANCE) {
                removalListener.weigher(this.weigher);
                if (this.maxWeight != -1) {
                    removalListener.maximumWeight(this.maxWeight);
                }
            } else if (this.maxWeight != -1) {
                removalListener.maximumSize(this.maxWeight);
            }
            if (this.ticker != null) {
                removalListener.ticker(this.ticker);
            }
            return removalListener;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.delegate = recreateCacheBuilder().build();
        }

        private Object readResolve() {
            return this.delegate;
        }

        protected Cache<K, V> delegate() {
            return this.delegate;
        }
    }

    static final class LoadingSerializationProxy<K, V> extends ManualSerializationProxy<K, V> implements LoadingCache<K, V>, Serializable {
        private static final long serialVersionUID = 1;
        transient LoadingCache<K, V> autoDelegate;

        LoadingSerializationProxy(LocalCache<K, V> localCache) {
            super(localCache);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.autoDelegate = recreateCacheBuilder().build(this.loader);
        }

        public V get(K k) throws ExecutionException {
            return this.autoDelegate.get(k);
        }

        public V getUnchecked(K k) {
            return this.autoDelegate.getUnchecked(k);
        }

        public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
            return this.autoDelegate.getAll(iterable);
        }

        public final V apply(K k) {
            return this.autoDelegate.apply(k);
        }

        public void refresh(K k) {
            this.autoDelegate.refresh(k);
        }

        private Object readResolve() {
            return this.autoDelegate;
        }
    }

    static class LoadingValueReference<K, V> implements ValueReference<K, V> {
        final SettableFuture<V> futureValue;
        volatile ValueReference<K, V> oldValue;
        final Stopwatch stopwatch;

        /* renamed from: com.google.common.cache.LocalCache$LoadingValueReference$1 */
        class C08461 implements Function<V, V> {
            C08461() {
            }

            public V apply(V v) {
                LoadingValueReference.this.set(v);
                return v;
            }
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, @Nullable V v, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        public boolean isLoading() {
            return true;
        }

        public LoadingValueReference() {
            this(LocalCache.unset());
        }

        public LoadingValueReference(ValueReference<K, V> valueReference) {
            this.futureValue = SettableFuture.create();
            this.stopwatch = Stopwatch.createUnstarted();
            this.oldValue = valueReference;
        }

        public boolean isActive() {
            return this.oldValue.isActive();
        }

        public int getWeight() {
            return this.oldValue.getWeight();
        }

        public boolean set(@Nullable V v) {
            return this.futureValue.set(v);
        }

        public boolean setException(Throwable th) {
            return this.futureValue.setException(th);
        }

        private ListenableFuture<V> fullyFailedFuture(Throwable th) {
            return Futures.immediateFailedFuture(th);
        }

        public void notifyNewValue(@Nullable V v) {
            if (v != null) {
                set(v);
            } else {
                this.oldValue = LocalCache.unset();
            }
        }

        public ListenableFuture<V> loadFuture(K k, CacheLoader<? super K, V> cacheLoader) {
            this.stopwatch.start();
            Object obj = this.oldValue.get();
            if (obj == null) {
                try {
                    k = cacheLoader.load(k);
                    return set(k) != null ? this.futureValue : Futures.immediateFuture(k);
                } catch (K k2) {
                    if ((k2 instanceof InterruptedException) != null) {
                        Thread.currentThread().interrupt();
                    }
                    return setException(k2) != null ? this.futureValue : fullyFailedFuture(k2);
                }
            }
            ListenableFuture reload = cacheLoader.reload(k2, obj);
            if (reload == null) {
                return Futures.immediateFuture(null);
            }
            return Futures.transform(reload, new C08461());
        }

        public long elapsedNanos() {
            return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        }

        public V waitForValue() throws ExecutionException {
            return Uninterruptibles.getUninterruptibly(this.futureValue);
        }

        public V get() {
            return this.oldValue.get();
        }

        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }
    }

    static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
        private static final long serialVersionUID = 1;
        final LocalCache<K, V> localCache;

        LocalManualCache(CacheBuilder<? super K, ? super V> cacheBuilder) {
            this(new LocalCache(cacheBuilder, null));
        }

        private LocalManualCache(LocalCache<K, V> localCache) {
            this.localCache = localCache;
        }

        @Nullable
        public V getIfPresent(Object obj) {
            return this.localCache.getIfPresent(obj);
        }

        public V get(K k, final Callable<? extends V> callable) throws ExecutionException {
            Preconditions.checkNotNull(callable);
            return this.localCache.get(k, new CacheLoader<Object, V>() {
                public V load(Object obj) throws Exception {
                    return callable.call();
                }
            });
        }

        public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
            return this.localCache.getAllPresent(iterable);
        }

        public void put(K k, V v) {
            this.localCache.put(k, v);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            this.localCache.putAll(map);
        }

        public void invalidate(Object obj) {
            Preconditions.checkNotNull(obj);
            this.localCache.remove(obj);
        }

        public void invalidateAll(Iterable<?> iterable) {
            this.localCache.invalidateAll(iterable);
        }

        public void invalidateAll() {
            this.localCache.clear();
        }

        public long size() {
            return this.localCache.longSize();
        }

        public ConcurrentMap<K, V> asMap() {
            return this.localCache;
        }

        public CacheStats stats() {
            SimpleStatsCounter simpleStatsCounter = new SimpleStatsCounter();
            simpleStatsCounter.incrementBy(this.localCache.globalStatsCounter);
            for (Segment segment : this.localCache.segments) {
                simpleStatsCounter.incrementBy(segment.statsCounter);
            }
            return simpleStatsCounter.snapshot();
        }

        public void cleanUp() {
            this.localCache.cleanUp();
        }

        Object writeReplace() {
            return new ManualSerializationProxy(this.localCache);
        }
    }

    static class LocalLoadingCache<K, V> extends LocalManualCache<K, V> implements LoadingCache<K, V> {
        private static final long serialVersionUID = 1;

        LocalLoadingCache(CacheBuilder<? super K, ? super V> cacheBuilder, CacheLoader<? super K, V> cacheLoader) {
            super();
        }

        public V get(K k) throws ExecutionException {
            return this.localCache.getOrLoad(k);
        }

        public V getUnchecked(K k) {
            try {
                return get(k);
            } catch (K k2) {
                throw new UncheckedExecutionException(k2.getCause());
            }
        }

        public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
            return this.localCache.getAll(iterable);
        }

        public void refresh(K k) {
            this.localCache.refresh(k);
        }

        public final V apply(K k) {
            return getUnchecked(k);
        }

        Object writeReplace() {
            return new LoadingSerializationProxy(this.localCache);
        }
    }

    private enum NullEntry implements ReferenceEntry<Object, Object> {
        INSTANCE;

        public long getAccessTime() {
            return 0;
        }

        public int getHash() {
            return 0;
        }

        public Object getKey() {
            return null;
        }

        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }

        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }

        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }

        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }

        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }

        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        public long getWriteTime() {
            return 0;
        }

        public void setAccessTime(long j) {
        }

        public void setNextInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setNextInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setValueReference(ValueReference<Object, Object> valueReference) {
        }

        public void setWriteTime(long j) {
        }
    }

    static class Segment<K, V> extends ReentrantLock {
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> accessQueue;
        volatile int count;
        final ReferenceQueue<K> keyReferenceQueue;
        final LocalCache<K, V> map;
        final long maxSegmentWeight;
        int modCount;
        final AtomicInteger readCount = new AtomicInteger();
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final StatsCounter statsCounter;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        @GuardedBy("this")
        long totalWeight;
        final ReferenceQueue<V> valueReferenceQueue;
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> writeQueue;

        Segment(LocalCache<K, V> localCache, int i, long j, StatsCounter statsCounter) {
            this.map = localCache;
            this.maxSegmentWeight = j;
            this.statsCounter = (StatsCounter) Preconditions.checkNotNull(statsCounter);
            initTable(newEntryArray(i));
            j = null;
            this.keyReferenceQueue = localCache.usesKeyReferences() != 0 ? new ReferenceQueue() : 0;
            if (localCache.usesValueReferences() != 0) {
                j = new ReferenceQueue();
            }
            this.valueReferenceQueue = j;
            this.recencyQueue = localCache.usesAccessQueue() != 0 ? new ConcurrentLinkedQueue() : LocalCache.discardingQueue();
            this.writeQueue = localCache.usesWriteQueue() != 0 ? new WriteQueue() : LocalCache.discardingQueue();
            this.accessQueue = localCache.usesAccessQueue() != null ? new AccessQueue() : LocalCache.discardingQueue();
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int i) {
            return new AtomicReferenceArray(i);
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray) {
            this.threshold = (atomicReferenceArray.length() * 3) / 4;
            if (!this.map.customWeigher() && ((long) this.threshold) == this.maxSegmentWeight) {
                this.threshold++;
            }
            this.table = atomicReferenceArray;
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> newEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(k), i, referenceEntry);
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            if (referenceEntry.getKey() == null) {
                return null;
            }
            ValueReference valueReference = referenceEntry.getValueReference();
            Object obj = valueReference.get();
            if (obj == null && valueReference.isActive()) {
                return null;
            }
            referenceEntry = this.map.entryFactory.copyEntry(this, referenceEntry, referenceEntry2);
            referenceEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, obj, referenceEntry));
            return referenceEntry;
        }

        @GuardedBy("this")
        void setValue(ReferenceEntry<K, V> referenceEntry, K k, V v, long j) {
            ValueReference valueReference = referenceEntry.getValueReference();
            k = this.map.weigher.weigh(k, v);
            Preconditions.checkState(k >= null, "Weights must be non-negative");
            referenceEntry.setValueReference(this.map.valueStrength.referenceValue(this, referenceEntry, v, k));
            recordWrite(referenceEntry, k, j);
            valueReference.notifyNewValue(v);
        }

        V get(K k, int i, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
            Preconditions.checkNotNull(k);
            Preconditions.checkNotNull(cacheLoader);
            try {
                if (this.count != 0) {
                    ReferenceEntry entry = getEntry(k, i);
                    if (entry != null) {
                        long read = this.map.ticker.read();
                        Object liveValue = getLiveValue(entry, read);
                        if (liveValue != null) {
                            recordRead(entry, read);
                            this.statsCounter.recordHits(1);
                            k = scheduleRefresh(entry, k, i, liveValue, read, cacheLoader);
                            postReadCleanup();
                            return k;
                        }
                        ValueReference valueReference = entry.getValueReference();
                        if (valueReference.isLoading()) {
                            k = waitForLoadingValue(entry, k, valueReference);
                            postReadCleanup();
                            return k;
                        }
                    }
                }
                k = lockedGetOrLoad(k, i, cacheLoader);
                postReadCleanup();
                return k;
            } catch (K k2) {
                Throwable cause = k2.getCause();
                if ((cause instanceof Error) != null) {
                    throw new ExecutionError((Error) cause);
                } else if ((cause instanceof RuntimeException) != null) {
                    throw new UncheckedExecutionException(cause);
                } else {
                    throw k2;
                }
            } catch (Throwable th) {
                postReadCleanup();
            }
        }

        V lockedGetOrLoad(K r18, int r19, com.google.common.cache.CacheLoader<? super K, V> r20) throws java.util.concurrent.ExecutionException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.utils.BlockUtils.getBlockByInsn(BlockUtils.java:171)
	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMerge(EliminatePhiNodes.java:90)
	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMergeInstructions(EliminatePhiNodes.java:68)
	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.visit(EliminatePhiNodes.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r17 = this;
            r1 = r17;
            r2 = r18;
            r3 = r19;
            r17.lock();
            r4 = r1.map;	 Catch:{ all -> 0x00c8 }
            r4 = r4.ticker;	 Catch:{ all -> 0x00c8 }
            r4 = r4.read();	 Catch:{ all -> 0x00c8 }
            r1.preWriteCleanup(r4);	 Catch:{ all -> 0x00c8 }
            r6 = r1.count;	 Catch:{ all -> 0x00c8 }
            r7 = 1;	 Catch:{ all -> 0x00c8 }
            r6 = r6 - r7;	 Catch:{ all -> 0x00c8 }
            r8 = r1.table;	 Catch:{ all -> 0x00c8 }
            r9 = r8.length();	 Catch:{ all -> 0x00c8 }
            r9 = r9 - r7;	 Catch:{ all -> 0x00c8 }
            r9 = r9 & r3;	 Catch:{ all -> 0x00c8 }
            r10 = r8.get(r9);	 Catch:{ all -> 0x00c8 }
            r10 = (com.google.common.cache.LocalCache.ReferenceEntry) r10;	 Catch:{ all -> 0x00c8 }
            r11 = r10;	 Catch:{ all -> 0x00c8 }
        L_0x0027:
            if (r11 == 0) goto L_0x0086;	 Catch:{ all -> 0x00c8 }
        L_0x0029:
            r13 = r11.getKey();	 Catch:{ all -> 0x00c8 }
            r14 = r11.getHash();	 Catch:{ all -> 0x00c8 }
            if (r14 != r3) goto L_0x0081;	 Catch:{ all -> 0x00c8 }
        L_0x0033:
            if (r13 == 0) goto L_0x0081;	 Catch:{ all -> 0x00c8 }
        L_0x0035:
            r14 = r1.map;	 Catch:{ all -> 0x00c8 }
            r14 = r14.keyEquivalence;	 Catch:{ all -> 0x00c8 }
            r14 = r14.equivalent(r2, r13);	 Catch:{ all -> 0x00c8 }
            if (r14 == 0) goto L_0x0081;	 Catch:{ all -> 0x00c8 }
        L_0x003f:
            r14 = r11.getValueReference();	 Catch:{ all -> 0x00c8 }
            r15 = r14.isLoading();	 Catch:{ all -> 0x00c8 }
            if (r15 == 0) goto L_0x004b;	 Catch:{ all -> 0x00c8 }
        L_0x0049:
            r4 = 0;	 Catch:{ all -> 0x00c8 }
            goto L_0x0088;	 Catch:{ all -> 0x00c8 }
        L_0x004b:
            r15 = r14.get();	 Catch:{ all -> 0x00c8 }
            if (r15 != 0) goto L_0x0057;	 Catch:{ all -> 0x00c8 }
        L_0x0051:
            r4 = com.google.common.cache.RemovalCause.COLLECTED;	 Catch:{ all -> 0x00c8 }
            r1.enqueueNotification(r13, r3, r14, r4);	 Catch:{ all -> 0x00c8 }
            goto L_0x0064;	 Catch:{ all -> 0x00c8 }
        L_0x0057:
            r12 = r1.map;	 Catch:{ all -> 0x00c8 }
            r12 = r12.isExpired(r11, r4);	 Catch:{ all -> 0x00c8 }
            if (r12 == 0) goto L_0x0072;	 Catch:{ all -> 0x00c8 }
        L_0x005f:
            r4 = com.google.common.cache.RemovalCause.EXPIRED;	 Catch:{ all -> 0x00c8 }
            r1.enqueueNotification(r13, r3, r14, r4);	 Catch:{ all -> 0x00c8 }
        L_0x0064:
            r4 = r1.writeQueue;	 Catch:{ all -> 0x00c8 }
            r4.remove(r11);	 Catch:{ all -> 0x00c8 }
            r4 = r1.accessQueue;	 Catch:{ all -> 0x00c8 }
            r4.remove(r11);	 Catch:{ all -> 0x00c8 }
            r1.count = r6;	 Catch:{ all -> 0x00c8 }
            r4 = 1;	 Catch:{ all -> 0x00c8 }
            goto L_0x0088;	 Catch:{ all -> 0x00c8 }
        L_0x0072:
            r1.recordLockedRead(r11, r4);	 Catch:{ all -> 0x00c8 }
            r2 = r1.statsCounter;	 Catch:{ all -> 0x00c8 }
            r2.recordHits(r7);	 Catch:{ all -> 0x00c8 }
            r17.unlock();
            r17.postWriteCleanup();
            return r15;
        L_0x0081:
            r11 = r11.getNext();	 Catch:{ all -> 0x00c8 }
            goto L_0x0027;	 Catch:{ all -> 0x00c8 }
        L_0x0086:
            r4 = 1;	 Catch:{ all -> 0x00c8 }
            r14 = 0;	 Catch:{ all -> 0x00c8 }
        L_0x0088:
            if (r4 == 0) goto L_0x00a0;	 Catch:{ all -> 0x00c8 }
        L_0x008a:
            r12 = new com.google.common.cache.LocalCache$LoadingValueReference;	 Catch:{ all -> 0x00c8 }
            r12.<init>();	 Catch:{ all -> 0x00c8 }
            if (r11 != 0) goto L_0x009c;	 Catch:{ all -> 0x00c8 }
        L_0x0091:
            r11 = r1.newEntry(r2, r3, r10);	 Catch:{ all -> 0x00c8 }
            r11.setValueReference(r12);	 Catch:{ all -> 0x00c8 }
            r8.set(r9, r11);	 Catch:{ all -> 0x00c8 }
            goto L_0x00a1;	 Catch:{ all -> 0x00c8 }
        L_0x009c:
            r11.setValueReference(r12);	 Catch:{ all -> 0x00c8 }
            goto L_0x00a1;
        L_0x00a0:
            r12 = 0;
        L_0x00a1:
            r17.unlock();
            r17.postWriteCleanup();
            if (r4 == 0) goto L_0x00c3;
        L_0x00a9:
            monitor-enter(r11);	 Catch:{ all -> 0x00bb }
            r4 = r20;
            r2 = r1.loadSync(r2, r3, r12, r4);	 Catch:{ all -> 0x00b7 }
            monitor-exit(r11);	 Catch:{ all -> 0x00b7 }
            r3 = r1.statsCounter;
            r3.recordMisses(r7);
            return r2;
        L_0x00b7:
            r0 = move-exception;
            r2 = r0;
            monitor-exit(r11);	 Catch:{ all -> 0x00b7 }
            throw r2;	 Catch:{ all -> 0x00bb }
        L_0x00bb:
            r0 = move-exception;
            r2 = r0;
            r3 = r1.statsCounter;
            r3.recordMisses(r7);
            throw r2;
        L_0x00c3:
            r2 = r1.waitForLoadingValue(r11, r2, r14);
            return r2;
        L_0x00c8:
            r0 = move-exception;
            r2 = r0;
            r17.unlock();
            r17.postWriteCleanup();
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.cache.LocalCache.Segment.lockedGetOrLoad(java.lang.Object, int, com.google.common.cache.CacheLoader):V");
        }

        V waitForLoadingValue(ReferenceEntry<K, V> referenceEntry, K k, ValueReference<K, V> valueReference) throws ExecutionException {
            if (valueReference.isLoading()) {
                Preconditions.checkState(Thread.holdsLock(referenceEntry) ^ true, "Recursive load of: %s", k);
                try {
                    valueReference = valueReference.waitForValue();
                    if (valueReference == null) {
                        valueReference = new StringBuilder();
                        valueReference.append("CacheLoader returned null for key ");
                        valueReference.append(k);
                        valueReference.append(".");
                        throw new InvalidCacheLoadException(valueReference.toString());
                    }
                    recordRead(referenceEntry, this.map.ticker.read());
                    return valueReference;
                } finally {
                    this.statsCounter.recordMisses(1);
                }
            } else {
                throw new AssertionError();
            }
        }

        V loadSync(K k, int i, LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
            return getAndRecordStats(k, i, loadingValueReference, loadingValueReference.loadFuture(k, cacheLoader));
        }

        ListenableFuture<V> loadAsync(K k, int i, LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> cacheLoader) {
            cacheLoader = loadingValueReference.loadFuture(k, cacheLoader);
            final K k2 = k;
            final int i2 = i;
            final LoadingValueReference<K, V> loadingValueReference2 = loadingValueReference;
            final CacheLoader<? super K, V> cacheLoader2 = cacheLoader;
            cacheLoader.addListener(new Runnable() {
                public void run() {
                    try {
                        Segment.this.getAndRecordStats(k2, i2, loadingValueReference2, cacheLoader2);
                    } catch (Throwable th) {
                        LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", th);
                        loadingValueReference2.setException(th);
                    }
                }
            }, MoreExecutors.directExecutor());
            return cacheLoader;
        }

        V getAndRecordStats(K k, int i, LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> listenableFuture) throws ExecutionException {
            Throwable th;
            try {
                listenableFuture = Uninterruptibles.getUninterruptibly(listenableFuture);
                if (listenableFuture == null) {
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("CacheLoader returned null for key ");
                        stringBuilder.append(k);
                        stringBuilder.append(".");
                        throw new InvalidCacheLoadException(stringBuilder.toString());
                    } catch (Throwable th2) {
                        th = th2;
                        if (listenableFuture == null) {
                            this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                            removeLoadingValue(k, i, loadingValueReference);
                        }
                        throw th;
                    }
                }
                this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
                storeLoadedValue(k, i, loadingValueReference, listenableFuture);
                if (listenableFuture == null) {
                    this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                    removeLoadingValue(k, i, loadingValueReference);
                }
                return listenableFuture;
            } catch (Throwable th3) {
                th = th3;
                listenableFuture = null;
                if (listenableFuture == null) {
                    this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                    removeLoadingValue(k, i, loadingValueReference);
                }
                throw th;
            }
        }

        V scheduleRefresh(ReferenceEntry<K, V> referenceEntry, K k, int i, V v, long j, CacheLoader<? super K, V> cacheLoader) {
            if (this.map.refreshes() && j - referenceEntry.getWriteTime() > this.map.refreshNanos && referenceEntry.getValueReference().isLoading() == null) {
                referenceEntry = refresh(k, i, cacheLoader, true);
                if (referenceEntry != null) {
                    return referenceEntry;
                }
            }
            return v;
        }

        @javax.annotation.Nullable
        V refresh(K r2, int r3, com.google.common.cache.CacheLoader<? super K, V> r4, boolean r5) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r1 = this;
            r5 = r1.insertLoadingValueReference(r2, r3, r5);
            r0 = 0;
            if (r5 != 0) goto L_0x0008;
        L_0x0007:
            return r0;
        L_0x0008:
            r2 = r1.loadAsync(r2, r3, r5, r4);
            r3 = r2.isDone();
            if (r3 == 0) goto L_0x0017;
        L_0x0012:
            r2 = com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(r2);	 Catch:{ Throwable -> 0x0017 }
            return r2;
        L_0x0017:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.cache.LocalCache.Segment.refresh(java.lang.Object, int, com.google.common.cache.CacheLoader, boolean):V");
        }

        @Nullable
        LoadingValueReference<K, V> insertLoadingValueReference(K k, int i, boolean z) {
            lock();
            try {
                long read = this.map.ticker.read();
                preWriteCleanup(read);
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                ReferenceEntry referenceEntry2 = referenceEntry;
                while (referenceEntry2 != null) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        k = referenceEntry2.getValueReference();
                        if (k.isLoading() == 0) {
                            if (!z || read - referenceEntry2.getWriteTime() >= this.map.refreshNanos) {
                                this.modCount++;
                                i = new LoadingValueReference(k);
                                referenceEntry2.setValueReference(i);
                                unlock();
                                postWriteCleanup();
                                return i;
                            }
                        }
                        unlock();
                        postWriteCleanup();
                        return null;
                    }
                    referenceEntry2 = referenceEntry2.getNext();
                }
                this.modCount++;
                z = new LoadingValueReference();
                k = newEntry(k, i, referenceEntry);
                k.setValueReference(z);
                atomicReferenceArray.set(length, k);
                unlock();
                postWriteCleanup();
                return z;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        void tryDrainReferenceQueues() {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                } finally {
                    unlock();
                }
            }
        }

        @GuardedBy("this")
        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                drainValueReferenceQueue();
            }
        }

        @GuardedBy("this")
        void drainKeyReferenceQueue() {
            int i = 0;
            do {
                Reference poll = this.keyReferenceQueue.poll();
                if (poll != null) {
                    this.map.reclaimKey((ReferenceEntry) poll);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        @GuardedBy("this")
        void drainValueReferenceQueue() {
            int i = 0;
            do {
                Reference poll = this.valueReferenceQueue.poll();
                if (poll != null) {
                    this.map.reclaimValue((ValueReference) poll);
                    i++;
                } else {
                    return;
                }
            } while (i != 16);
        }

        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                clearValueReferenceQueue();
            }
        }

        void clearKeyReferenceQueue() {
            while (this.keyReferenceQueue.poll() != null) {
            }
        }

        void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {
            }
        }

        void recordRead(ReferenceEntry<K, V> referenceEntry, long j) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(j);
            }
            this.recencyQueue.add(referenceEntry);
        }

        @GuardedBy("this")
        void recordLockedRead(ReferenceEntry<K, V> referenceEntry, long j) {
            if (this.map.recordsAccess()) {
                referenceEntry.setAccessTime(j);
            }
            this.accessQueue.add(referenceEntry);
        }

        @GuardedBy("this")
        void recordWrite(ReferenceEntry<K, V> referenceEntry, int i, long j) {
            drainRecencyQueue();
            this.totalWeight += (long) i;
            if (this.map.recordsAccess() != 0) {
                referenceEntry.setAccessTime(j);
            }
            if (this.map.recordsWrite() != 0) {
                referenceEntry.setWriteTime(j);
            }
            this.accessQueue.add(referenceEntry);
            this.writeQueue.add(referenceEntry);
        }

        @GuardedBy("this")
        void drainRecencyQueue() {
            while (true) {
                ReferenceEntry referenceEntry = (ReferenceEntry) this.recencyQueue.poll();
                if (referenceEntry == null) {
                    return;
                }
                if (this.accessQueue.contains(referenceEntry)) {
                    this.accessQueue.add(referenceEntry);
                }
            }
        }

        void tryExpireEntries(long j) {
            if (tryLock()) {
                try {
                    expireEntries(j);
                } finally {
                    unlock();
                }
            }
        }

        @GuardedBy("this")
        void expireEntries(long j) {
            drainRecencyQueue();
            ReferenceEntry referenceEntry;
            do {
                referenceEntry = (ReferenceEntry) this.writeQueue.peek();
                if (referenceEntry == null || !this.map.isExpired(referenceEntry, j)) {
                    do {
                        referenceEntry = (ReferenceEntry) this.accessQueue.peek();
                        if (referenceEntry == null || !this.map.isExpired(referenceEntry, j)) {
                            return;
                        }
                    } while (removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.EXPIRED));
                    throw new AssertionError();
                }
            } while (removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.EXPIRED));
            throw new AssertionError();
        }

        @GuardedBy("this")
        void enqueueNotification(ReferenceEntry<K, V> referenceEntry, RemovalCause removalCause) {
            enqueueNotification(referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry.getValueReference(), removalCause);
        }

        @GuardedBy("this")
        void enqueueNotification(@Nullable K k, int i, ValueReference<K, V> valueReference, RemovalCause removalCause) {
            this.totalWeight -= (long) valueReference.getWeight();
            if (removalCause.wasEvicted() != 0) {
                this.statsCounter.recordEviction();
            }
            if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
                this.map.removalNotificationQueue.offer(new RemovalNotification(k, valueReference.get(), removalCause));
            }
        }

        @GuardedBy("this")
        void evictEntries() {
            if (this.map.evictsBySize()) {
                drainRecencyQueue();
                while (this.totalWeight > this.maxSegmentWeight) {
                    ReferenceEntry nextEvictable = getNextEvictable();
                    if (!removeEntry(nextEvictable, nextEvictable.getHash(), RemovalCause.SIZE)) {
                        throw new AssertionError();
                    }
                }
            }
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> getNextEvictable() {
            for (ReferenceEntry<K, V> referenceEntry : this.accessQueue) {
                if (referenceEntry.getValueReference().getWeight() > 0) {
                    return referenceEntry;
                }
            }
            throw new AssertionError();
        }

        ReferenceEntry<K, V> getFirst(int i) {
            AtomicReferenceArray atomicReferenceArray = this.table;
            return (ReferenceEntry) atomicReferenceArray.get(i & (atomicReferenceArray.length() - 1));
        }

        @Nullable
        ReferenceEntry<K, V> getEntry(Object obj, int i) {
            for (ReferenceEntry<K, V> first = getFirst(i); first != null; first = first.getNext()) {
                if (first.getHash() == i) {
                    Object key = first.getKey();
                    if (key == null) {
                        tryDrainReferenceQueues();
                    } else if (this.map.keyEquivalence.equivalent(obj, key)) {
                        return first;
                    }
                }
            }
            return null;
        }

        @Nullable
        ReferenceEntry<K, V> getLiveEntry(Object obj, int i, long j) {
            obj = getEntry(obj, i);
            if (obj == null) {
                return null;
            }
            if (!this.map.isExpired(obj, j)) {
                return obj;
            }
            tryExpireEntries(j);
            return null;
        }

        V getLiveValue(ReferenceEntry<K, V> referenceEntry, long j) {
            if (referenceEntry.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V v = referenceEntry.getValueReference().get();
            if (v == null) {
                tryDrainReferenceQueues();
                return null;
            } else if (this.map.isExpired(referenceEntry, j) == null) {
                return v;
            } else {
                tryExpireEntries(j);
                return null;
            }
        }

        @Nullable
        V get(Object obj, int i) {
            try {
                V v = null;
                if (this.count != 0) {
                    long read = this.map.ticker.read();
                    ReferenceEntry liveEntry = getLiveEntry(obj, i, read);
                    if (liveEntry == null) {
                        return v;
                    }
                    Object obj2 = liveEntry.getValueReference().get();
                    if (obj2 != null) {
                        recordRead(liveEntry, read);
                        obj = scheduleRefresh(liveEntry, liveEntry.getKey(), i, obj2, read, this.map.defaultLoader);
                        postReadCleanup();
                        return obj;
                    }
                    tryDrainReferenceQueues();
                }
                postReadCleanup();
                return v;
            } finally {
                postReadCleanup();
            }
        }

        boolean containsKey(Object obj, int i) {
            try {
                boolean z = false;
                if (this.count != 0) {
                    obj = getLiveEntry(obj, i, this.map.ticker.read());
                    if (obj == null) {
                        return z;
                    }
                    if (obj.getValueReference().get() != null) {
                        z = true;
                    }
                    postReadCleanup();
                    return z;
                }
                postReadCleanup();
                return z;
            } finally {
                postReadCleanup();
            }
        }

        @VisibleForTesting
        boolean containsValue(Object obj) {
            try {
                if (this.count != 0) {
                    long read = this.map.ticker.read();
                    AtomicReferenceArray atomicReferenceArray = this.table;
                    int length = atomicReferenceArray.length();
                    for (int i = 0; i < length; i++) {
                        for (ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                            Object liveValue = getLiveValue(referenceEntry, read);
                            if (liveValue != null) {
                                if (this.map.valueEquivalence.equivalent(obj, liveValue)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                postReadCleanup();
                return false;
            } finally {
                postReadCleanup();
            }
        }

        @Nullable
        V put(K k, int i, V v, boolean z) {
            lock();
            try {
                long read = this.map.ticker.read();
                preWriteCleanup(read);
                if (this.count + 1 > this.threshold) {
                    expand();
                    int i2 = this.count;
                }
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = i & (atomicReferenceArray.length() - 1);
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        V v2 = valueReference.get();
                        if (v2 == null) {
                            this.modCount++;
                            if (valueReference.isActive()) {
                                enqueueNotification(k, i, valueReference, RemovalCause.COLLECTED);
                                setValue(referenceEntry2, k, v, read);
                                k = this.count;
                            } else {
                                setValue(referenceEntry2, k, v, read);
                                k = this.count + 1;
                            }
                            this.count = k;
                            evictEntries();
                            unlock();
                            postWriteCleanup();
                            return null;
                        }
                        if (z) {
                            recordLockedRead(referenceEntry2, read);
                        } else {
                            this.modCount++;
                            enqueueNotification(k, i, valueReference, RemovalCause.REPLACED);
                            setValue(referenceEntry2, k, v, read);
                            evictEntries();
                        }
                        unlock();
                        postWriteCleanup();
                        return v2;
                    }
                }
                this.modCount++;
                i = newEntry(k, i, referenceEntry);
                setValue(i, k, v, read);
                atomicReferenceArray.set(length, i);
                this.count++;
                evictEntries();
                unlock();
                postWriteCleanup();
                return null;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        @GuardedBy("this")
        void expand() {
            AtomicReferenceArray atomicReferenceArray = this.table;
            int length = atomicReferenceArray.length();
            if (length < 1073741824) {
                int i = this.count;
                AtomicReferenceArray newEntryArray = newEntryArray(length << 1);
                this.threshold = (newEntryArray.length() * 3) / 4;
                int length2 = newEntryArray.length() - 1;
                for (int i2 = 0; i2 < length; i2++) {
                    ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i2);
                    if (referenceEntry != null) {
                        ReferenceEntry next = referenceEntry.getNext();
                        int hash = referenceEntry.getHash() & length2;
                        if (next == null) {
                            newEntryArray.set(hash, referenceEntry);
                        } else {
                            ReferenceEntry referenceEntry2 = referenceEntry;
                            while (next != null) {
                                int hash2 = next.getHash() & length2;
                                if (hash2 != hash) {
                                    referenceEntry2 = next;
                                    hash = hash2;
                                }
                                next = next.getNext();
                            }
                            newEntryArray.set(hash, referenceEntry2);
                            while (referenceEntry != referenceEntry2) {
                                int hash3 = referenceEntry.getHash() & length2;
                                ReferenceEntry copyEntry = copyEntry(referenceEntry, (ReferenceEntry) newEntryArray.get(hash3));
                                if (copyEntry != null) {
                                    newEntryArray.set(hash3, copyEntry);
                                } else {
                                    removeCollectedEntry(referenceEntry);
                                    i--;
                                }
                                referenceEntry = referenceEntry.getNext();
                            }
                        }
                    }
                }
                this.table = newEntryArray;
                this.count = i;
            }
        }

        boolean replace(K k, int i, V v, V v2) {
            K k2 = k;
            int i2 = i;
            lock();
            try {
                long read = r8.map.ticker.read();
                preWriteCleanup(read);
                AtomicReferenceArray atomicReferenceArray = r8.table;
                int length = i2 & (atomicReferenceArray.length() - 1);
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i2 && key != null && r8.map.keyEquivalence.equivalent(k2, key)) {
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        Object obj = valueReference.get();
                        if (obj == null) {
                            if (valueReference.isActive()) {
                                int i3 = r8.count;
                                r8.modCount++;
                                ReferenceEntry removeValueFromChain = removeValueFromChain(referenceEntry, referenceEntry2, key, i2, valueReference, RemovalCause.COLLECTED);
                                int i4 = r8.count - 1;
                                atomicReferenceArray.set(length, removeValueFromChain);
                                r8.count = i4;
                            }
                        } else if (r8.map.valueEquivalence.equivalent(v, obj)) {
                            r8.modCount++;
                            enqueueNotification(k2, i2, valueReference, RemovalCause.REPLACED);
                            setValue(referenceEntry2, k2, v2, read);
                            evictEntries();
                            unlock();
                            postWriteCleanup();
                            return true;
                        } else {
                            recordLockedRead(referenceEntry2, read);
                        }
                        unlock();
                        postWriteCleanup();
                        return false;
                    }
                    V v3 = v;
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th) {
                Throwable th2 = th;
                unlock();
                postWriteCleanup();
            }
        }

        @Nullable
        V replace(K k, int i, V v) {
            K k2 = k;
            int i2 = i;
            lock();
            try {
                long read = r8.map.ticker.read();
                preWriteCleanup(read);
                AtomicReferenceArray atomicReferenceArray = r8.table;
                int length = i2 & (atomicReferenceArray.length() - 1);
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i2 && key != null && r8.map.keyEquivalence.equivalent(k2, key)) {
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        V v2 = valueReference.get();
                        if (v2 == null) {
                            if (valueReference.isActive()) {
                                int i3 = r8.count;
                                r8.modCount++;
                                ReferenceEntry removeValueFromChain = removeValueFromChain(referenceEntry, referenceEntry2, key, i2, valueReference, RemovalCause.COLLECTED);
                                int i4 = r8.count - 1;
                                atomicReferenceArray.set(length, removeValueFromChain);
                                r8.count = i4;
                            }
                            unlock();
                            postWriteCleanup();
                            return null;
                        }
                        r8.modCount++;
                        enqueueNotification(k2, i2, valueReference, RemovalCause.REPLACED);
                        setValue(referenceEntry2, k2, v, read);
                        evictEntries();
                        unlock();
                        postWriteCleanup();
                        return v2;
                    }
                }
                unlock();
                postWriteCleanup();
                return null;
            } catch (Throwable th) {
                Throwable th2 = th;
                unlock();
                postWriteCleanup();
            }
        }

        @Nullable
        V remove(Object obj, int i) {
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                int i2 = this.count;
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(obj, key)) {
                        RemovalCause removalCause;
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        obj = valueReference.get();
                        if (obj != null) {
                            removalCause = RemovalCause.EXPLICIT;
                        } else {
                            if (valueReference.isActive()) {
                                removalCause = RemovalCause.COLLECTED;
                            }
                            unlock();
                            postWriteCleanup();
                            return null;
                        }
                        RemovalCause removalCause2 = removalCause;
                        this.modCount++;
                        int i3 = this.count - 1;
                        atomicReferenceArray.set(length, removeValueFromChain(referenceEntry, referenceEntry2, key, i, valueReference, removalCause2));
                        this.count = i3;
                        return obj;
                    }
                }
                unlock();
                postWriteCleanup();
                return null;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean storeLoadedValue(K k, int i, LoadingValueReference<K, V> loadingValueReference, V v) {
            lock();
            long read = this.map.ticker.read();
            preWriteCleanup(read);
            int i2 = this.count + 1;
            if (i2 > this.threshold) {
                expand();
                i2 = this.count + 1;
            }
            AtomicReferenceArray atomicReferenceArray = this.table;
            int length = i & (atomicReferenceArray.length() - 1);
            ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
            ReferenceEntry referenceEntry2 = referenceEntry;
            while (referenceEntry2 != null) {
                Object key = referenceEntry2.getKey();
                if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                    ValueReference valueReference = referenceEntry2.getValueReference();
                    key = valueReference.get();
                    if (loadingValueReference != valueReference) {
                        if (key != null || valueReference == LocalCache.UNSET) {
                            enqueueNotification(k, i, new WeightedStrongValueReference(v, 0), RemovalCause.REPLACED);
                            unlock();
                            postWriteCleanup();
                            return false;
                        }
                    }
                    this.modCount++;
                    if (loadingValueReference.isActive()) {
                        enqueueNotification(k, i, loadingValueReference, key == null ? RemovalCause.COLLECTED : RemovalCause.REPLACED);
                        i2--;
                    }
                    setValue(referenceEntry2, k, v, read);
                    this.count = i2;
                    evictEntries();
                    unlock();
                    postWriteCleanup();
                    return true;
                }
                try {
                    referenceEntry2 = referenceEntry2.getNext();
                } catch (Throwable th) {
                    unlock();
                    postWriteCleanup();
                }
            }
            this.modCount += 1;
            i = newEntry(k, i, referenceEntry);
            setValue(i, k, v, read);
            atomicReferenceArray.set(length, i);
            this.count = i2;
            evictEntries();
            unlock();
            postWriteCleanup();
            return true;
        }

        boolean remove(Object obj, int i, Object obj2) {
            lock();
            try {
                preWriteCleanup(this.map.ticker.read());
                int i2 = this.count;
                AtomicReferenceArray atomicReferenceArray = this.table;
                boolean z = true;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(obj, key)) {
                        RemovalCause removalCause;
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        obj = valueReference.get();
                        if (this.map.valueEquivalence.equivalent(obj2, obj) != null) {
                            removalCause = RemovalCause.EXPLICIT;
                        } else {
                            if (obj == null && valueReference.isActive() != null) {
                                removalCause = RemovalCause.COLLECTED;
                            }
                            unlock();
                            postWriteCleanup();
                            return false;
                        }
                        this.modCount += 1;
                        obj2 = this.count - 1;
                        atomicReferenceArray.set(length, removeValueFromChain(referenceEntry, referenceEntry2, key, i, valueReference, removalCause));
                        this.count = obj2;
                        if (removalCause != RemovalCause.EXPLICIT) {
                            z = false;
                        }
                        unlock();
                        postWriteCleanup();
                        return z;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        void clear() {
            if (this.count != 0) {
                lock();
                try {
                    int i;
                    AtomicReferenceArray atomicReferenceArray = this.table;
                    for (i = 0; i < atomicReferenceArray.length(); i++) {
                        for (ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                            if (referenceEntry.getValueReference().isActive()) {
                                enqueueNotification(referenceEntry, RemovalCause.EXPLICIT);
                            }
                        }
                    }
                    for (i = 0; i < atomicReferenceArray.length(); i++) {
                        atomicReferenceArray.set(i, null);
                    }
                    clearReferenceQueues();
                    this.writeQueue.clear();
                    this.accessQueue.clear();
                    this.readCount.set(0);
                    this.modCount++;
                    this.count = 0;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }
        }

        @GuardedBy("this")
        @Nullable
        ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2, @Nullable K k, int i, ValueReference<K, V> valueReference, RemovalCause removalCause) {
            enqueueNotification(k, i, valueReference, removalCause);
            this.writeQueue.remove(referenceEntry2);
            this.accessQueue.remove(referenceEntry2);
            if (valueReference.isLoading() == null) {
                return removeEntryFromChain(referenceEntry, referenceEntry2);
            }
            valueReference.notifyNewValue(null);
            return referenceEntry;
        }

        @GuardedBy("this")
        @Nullable
        ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            int i = this.count;
            ReferenceEntry<K, V> next = referenceEntry2.getNext();
            while (referenceEntry != referenceEntry2) {
                ReferenceEntry<K, V> copyEntry = copyEntry(referenceEntry, next);
                if (copyEntry != null) {
                    next = copyEntry;
                } else {
                    removeCollectedEntry(referenceEntry);
                    i--;
                }
                referenceEntry = referenceEntry.getNext();
            }
            this.count = i;
            return next;
        }

        @GuardedBy("this")
        void removeCollectedEntry(ReferenceEntry<K, V> referenceEntry) {
            enqueueNotification(referenceEntry, RemovalCause.COLLECTED);
            this.writeQueue.remove(referenceEntry);
            this.accessQueue.remove(referenceEntry);
        }

        boolean reclaimKey(ReferenceEntry<K, V> referenceEntry, int i) {
            lock();
            try {
                int i2 = this.count;
                AtomicReferenceArray atomicReferenceArray = this.table;
                boolean z = true;
                int length = (atomicReferenceArray.length() - z) & i;
                ReferenceEntry<K, V> referenceEntry2 = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry<K, V> referenceEntry3 = referenceEntry2; referenceEntry3 != null; referenceEntry3 = referenceEntry3.getNext()) {
                    if (referenceEntry3 == referenceEntry) {
                        this.modCount += z;
                        i = this.count - z;
                        atomicReferenceArray.set(length, removeValueFromChain(referenceEntry2, referenceEntry3, referenceEntry3.getKey(), i, referenceEntry3.getValueReference(), RemovalCause.COLLECTED));
                        this.count = i;
                        return z;
                    }
                }
                unlock();
                postWriteCleanup();
                return null;
            } finally {
                unlock();
                postWriteCleanup();
            }
        }

        boolean reclaimValue(K k, int i, ValueReference<K, V> valueReference) {
            lock();
            try {
                int i2 = this.count;
                AtomicReferenceArray atomicReferenceArray = this.table;
                boolean z = true;
                int length = (atomicReferenceArray.length() - z) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                ReferenceEntry referenceEntry2 = referenceEntry;
                while (referenceEntry2 != null) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() != i || key == null || !this.map.keyEquivalence.equivalent(k, key)) {
                        referenceEntry2 = referenceEntry2.getNext();
                    } else if (referenceEntry2.getValueReference() == valueReference) {
                        this.modCount += z;
                        i = this.count - z;
                        atomicReferenceArray.set(length, removeValueFromChain(referenceEntry, referenceEntry2, key, i, valueReference, RemovalCause.COLLECTED));
                        this.count = i;
                        return z;
                    } else {
                        unlock();
                        if (isHeldByCurrentThread() == null) {
                            postWriteCleanup();
                        }
                        return false;
                    }
                }
                unlock();
                if (isHeldByCurrentThread() == null) {
                    postWriteCleanup();
                }
                return false;
            } finally {
                unlock();
                if (isHeldByCurrentThread() == 0) {
                    postWriteCleanup();
                }
            }
        }

        boolean removeLoadingValue(K k, int i, LoadingValueReference<K, V> loadingValueReference) {
            lock();
            try {
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        if (referenceEntry2.getValueReference() == loadingValueReference) {
                            if (loadingValueReference.isActive() != null) {
                                referenceEntry2.setValueReference(loadingValueReference.getOldValue());
                            } else {
                                atomicReferenceArray.set(length, removeEntryFromChain(referenceEntry, referenceEntry2));
                            }
                            unlock();
                            postWriteCleanup();
                            return true;
                        }
                        unlock();
                        postWriteCleanup();
                        return false;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        @GuardedBy("this")
        boolean removeEntry(ReferenceEntry<K, V> referenceEntry, int i, RemovalCause removalCause) {
            int i2 = this.count;
            AtomicReferenceArray atomicReferenceArray = this.table;
            int length = (atomicReferenceArray.length() - 1) & i;
            ReferenceEntry<K, V> referenceEntry2 = (ReferenceEntry) atomicReferenceArray.get(length);
            for (ReferenceEntry<K, V> referenceEntry3 = referenceEntry2; referenceEntry3 != null; referenceEntry3 = referenceEntry3.getNext()) {
                if (referenceEntry3 == referenceEntry) {
                    this.modCount += 1;
                    i = this.count - 1;
                    atomicReferenceArray.set(length, removeValueFromChain(referenceEntry2, referenceEntry3, referenceEntry3.getKey(), i, referenceEntry3.getValueReference(), removalCause));
                    this.count = i;
                    return true;
                }
            }
            return null;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                cleanUp();
            }
        }

        @GuardedBy("this")
        void preWriteCleanup(long j) {
            runLockedCleanup(j);
        }

        void postWriteCleanup() {
            runUnlockedCleanup();
        }

        void cleanUp() {
            runLockedCleanup(this.map.ticker.read());
            runUnlockedCleanup();
        }

        void runLockedCleanup(long j) {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                    expireEntries(j);
                    this.readCount.set(0);
                } finally {
                    unlock();
                }
            }
        }

        void runUnlockedCleanup() {
            if (!isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
    }

    static class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        public int getWeight() {
            return 1;
        }

        public boolean isActive() {
            return true;
        }

        public boolean isLoading() {
            return false;
        }

        public void notifyNewValue(V v) {
        }

        SoftValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, referenceQueue);
            this.entry = referenceEntry;
        }

        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new SoftValueReference(referenceQueue, v, referenceEntry);
        }

        public V waitForValue() {
            return get();
        }
    }

    enum Strength {
        STRONG {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i) {
                return i == 1 ? new StrongValueReference(v) : new WeightedStrongValueReference(v, i);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        },
        SOFT {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i) {
                return i == 1 ? new SoftValueReference(segment.valueReferenceQueue, v, referenceEntry) : new WeightedSoftValueReference(segment.valueReferenceQueue, v, referenceEntry, i);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        },
        WEAK {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i) {
                return i == 1 ? new WeakValueReference(segment.valueReferenceQueue, v, referenceEntry) : new WeightedWeakValueReference(segment.valueReferenceQueue, v, referenceEntry, i);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };

        abstract Equivalence<Object> defaultEquivalence();

        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v, int i);
    }

    static class StrongEntry<K, V> extends AbstractReferenceEntry<K, V> {
        final int hash;
        final K key;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

        StrongEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            this.key = k;
            this.hash = i;
            this.next = referenceEntry;
        }

        public K getKey() {
            return this.key;
        }

        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        public int getHash() {
            return this.hash;
        }

        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime = -1;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        StrongAccessEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }
    }

    static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;

        StrongAccessWriteEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long j) {
            this.writeTime = j;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        public int getWeight() {
            return 1;
        }

        public boolean isActive() {
            return true;
        }

        public boolean isLoading() {
            return false;
        }

        public void notifyNewValue(V v) {
        }

        StrongValueReference(V v) {
            this.referent = v;
        }

        public V get() {
            return this.referent;
        }

        public V waitForValue() {
            return get();
        }
    }

    static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> {
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = -1;

        StrongWriteEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long j) {
            this.writeTime = j;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    final class ValueIterator extends HashIterator<V> {
        ValueIterator() {
            super();
        }

        public V next() {
            return nextEntry().getValue();
        }
    }

    final class Values extends AbstractCollection<V> {
        private final ConcurrentMap<?, ?> map;

        Values(ConcurrentMap<?, ?> concurrentMap) {
            this.map = concurrentMap;
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public void clear() {
            this.map.clear();
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public boolean contains(Object obj) {
            return this.map.containsValue(obj);
        }
    }

    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = LocalCache.unset();

        WeakEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, referenceQueue);
            this.hash = i;
            this.next = referenceEntry;
        }

        public K getKey() {
            return get();
        }

        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }

        public void setAccessTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }

        public void setWriteTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        public int getHash() {
            return this.hash;
        }

        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime = -1;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();

        WeakAccessEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }
    }

    static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime = Long.MAX_VALUE;
        ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = Long.MAX_VALUE;

        WeakAccessWriteEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public long getAccessTime() {
            return this.accessTime;
        }

        public void setAccessTime(long j) {
            this.accessTime = j;
        }

        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }

        public void setNextInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextAccess = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }

        public void setPreviousInAccessQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousAccess = referenceEntry;
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long j) {
            this.writeTime = j;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    static class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        public int getWeight() {
            return 1;
        }

        public boolean isActive() {
            return true;
        }

        public boolean isLoading() {
            return false;
        }

        public void notifyNewValue(V v) {
        }

        WeakValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, referenceQueue);
            this.entry = referenceEntry;
        }

        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference(referenceQueue, v, referenceEntry);
        }

        public V waitForValue() {
            return get();
        }
    }

    static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> {
        ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
        ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
        volatile long writeTime = -1;

        WeakWriteEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public long getWriteTime() {
            return this.writeTime;
        }

        public void setWriteTime(long j) {
            this.writeTime = j;
        }

        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }

        public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.nextWrite = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }

        public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
            this.previousWrite = referenceEntry;
        }
    }

    static final class WeightedSoftValueReference<K, V> extends SoftValueReference<K, V> {
        final int weight;

        WeightedSoftValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry, int i) {
            super(referenceQueue, v, referenceEntry);
            this.weight = i;
        }

        public int getWeight() {
            return this.weight;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new WeightedSoftValueReference(referenceQueue, v, referenceEntry, this.weight);
        }
    }

    static final class WeightedStrongValueReference<K, V> extends StrongValueReference<K, V> {
        final int weight;

        WeightedStrongValueReference(V v, int i) {
            super(v);
            this.weight = i;
        }

        public int getWeight() {
            return this.weight;
        }
    }

    static final class WeightedWeakValueReference<K, V> extends WeakValueReference<K, V> {
        final int weight;

        WeightedWeakValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry, int i) {
            super(referenceQueue, v, referenceEntry);
            this.weight = i;
        }

        public int getWeight() {
            return this.weight;
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new WeightedWeakValueReference(referenceQueue, v, referenceEntry, this.weight);
        }
    }

    static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new C08521();

        /* renamed from: com.google.common.cache.LocalCache$WriteQueue$1 */
        class C08521 extends AbstractReferenceEntry<K, V> {
            ReferenceEntry<K, V> nextWrite = this;
            ReferenceEntry<K, V> previousWrite = this;

            public long getWriteTime() {
                return Long.MAX_VALUE;
            }

            public void setWriteTime(long j) {
            }

            C08521() {
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return this.nextWrite;
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
                this.nextWrite = referenceEntry;
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return this.previousWrite;
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> referenceEntry) {
                this.previousWrite = referenceEntry;
            }
        }

        WriteQueue() {
        }

        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            LocalCache.connectWriteOrder(referenceEntry.getPreviousInWriteQueue(), referenceEntry.getNextInWriteQueue());
            LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), referenceEntry);
            LocalCache.connectWriteOrder(referenceEntry, this.head);
            return true;
        }

        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue();
            return nextInWriteQueue == this.head ? null : nextInWriteQueue;
        }

        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> nextInWriteQueue = this.head.getNextInWriteQueue();
            if (nextInWriteQueue == this.head) {
                return null;
            }
            remove(nextInWriteQueue);
            return nextInWriteQueue;
        }

        public boolean remove(Object obj) {
            ReferenceEntry referenceEntry = (ReferenceEntry) obj;
            ReferenceEntry previousInWriteQueue = referenceEntry.getPreviousInWriteQueue();
            ReferenceEntry nextInWriteQueue = referenceEntry.getNextInWriteQueue();
            LocalCache.connectWriteOrder(previousInWriteQueue, nextInWriteQueue);
            LocalCache.nullifyWriteOrder(referenceEntry);
            return nextInWriteQueue != NullEntry.INSTANCE ? true : null;
        }

        public boolean contains(Object obj) {
            return ((ReferenceEntry) obj).getNextInWriteQueue() != NullEntry.INSTANCE ? true : null;
        }

        public boolean isEmpty() {
            return this.head.getNextInWriteQueue() == this.head;
        }

        public int size() {
            int i = 0;
            for (ReferenceEntry nextInWriteQueue = this.head.getNextInWriteQueue(); nextInWriteQueue != this.head; nextInWriteQueue = nextInWriteQueue.getNextInWriteQueue()) {
                i++;
            }
            return i;
        }

        public void clear() {
            ReferenceEntry nextInWriteQueue = this.head.getNextInWriteQueue();
            while (nextInWriteQueue != this.head) {
                ReferenceEntry nextInWriteQueue2 = nextInWriteQueue.getNextInWriteQueue();
                LocalCache.nullifyWriteOrder(nextInWriteQueue);
                nextInWriteQueue = nextInWriteQueue2;
            }
            this.head.setNextInWriteQueue(this.head);
            this.head.setPreviousInWriteQueue(this.head);
        }

        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    referenceEntry = referenceEntry.getNextInWriteQueue();
                    return referenceEntry == WriteQueue.this.head ? null : referenceEntry;
                }
            };
        }
    }

    final class WriteThroughEntry implements Entry<K, V> {
        final K key;
        V value;

        WriteThroughEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (this.key.equals(entry.getKey()) && this.value.equals(entry.getValue()) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getKey());
            stringBuilder.append("=");
            stringBuilder.append(getValue());
            return stringBuilder.toString();
        }
    }

    static int rehash(int i) {
        i += (i << 15) ^ -12931;
        i ^= i >>> 10;
        i += i << 3;
        i ^= i >>> 6;
        i += (i << 2) + (i << 14);
        return i ^ (i >>> 16);
    }

    LocalCache(CacheBuilder<? super K, ? super V> cacheBuilder, @Nullable CacheLoader<? super K, V> cacheLoader) {
        this.concurrencyLevel = Math.min(cacheBuilder.getConcurrencyLevel(), 65536);
        this.keyStrength = cacheBuilder.getKeyStrength();
        this.valueStrength = cacheBuilder.getValueStrength();
        this.keyEquivalence = cacheBuilder.getKeyEquivalence();
        this.valueEquivalence = cacheBuilder.getValueEquivalence();
        this.maxWeight = cacheBuilder.getMaximumWeight();
        this.weigher = cacheBuilder.getWeigher();
        this.expireAfterAccessNanos = cacheBuilder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = cacheBuilder.getExpireAfterWriteNanos();
        this.refreshNanos = cacheBuilder.getRefreshNanos();
        this.removalListener = cacheBuilder.getRemovalListener();
        this.removalNotificationQueue = this.removalListener == NullListener.INSTANCE ? discardingQueue() : new ConcurrentLinkedQueue();
        this.ticker = cacheBuilder.getTicker(recordsTime());
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
        this.globalStatsCounter = (StatsCounter) cacheBuilder.getStatsCounterSupplier().get();
        this.defaultLoader = cacheLoader;
        cacheLoader = Math.min(cacheBuilder.getInitialCapacity(), 1073741824);
        if (evictsBySize() && !customWeigher()) {
            cacheLoader = Math.min(cacheLoader, (int) this.maxWeight);
        }
        int i = 0;
        int i2 = 1;
        int i3 = 1;
        int i4 = 0;
        while (i3 < this.concurrencyLevel && (!evictsBySize() || ((long) (i3 * 20)) <= this.maxWeight)) {
            i4++;
            i3 <<= 1;
        }
        this.segmentShift = 32 - i4;
        this.segmentMask = i3 - 1;
        this.segments = newSegmentArray(i3);
        i4 = cacheLoader / i3;
        if (i4 * i3 < cacheLoader) {
            i4++;
        }
        while (i2 < i4) {
            i2 <<= 1;
        }
        if (evictsBySize() != null) {
            long j = (long) i3;
            long j2 = (this.maxWeight / j) + 1;
            long j3 = this.maxWeight % j;
            while (i < this.segments.length) {
                if (((long) i) == j3) {
                    j2--;
                }
                this.segments[i] = createSegment(i2, j2, (StatsCounter) cacheBuilder.getStatsCounterSupplier().get());
                i++;
            }
            return;
        }
        while (i < this.segments.length) {
            this.segments[i] = createSegment(i2, -1, (StatsCounter) cacheBuilder.getStatsCounterSupplier().get());
            i++;
        }
    }

    boolean evictsBySize() {
        return this.maxWeight >= 0;
    }

    boolean customWeigher() {
        return this.weigher != OneWeigher.INSTANCE;
    }

    boolean expires() {
        if (!expiresAfterWrite()) {
            if (!expiresAfterAccess()) {
                return false;
            }
        }
        return true;
    }

    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0;
    }

    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0;
    }

    boolean refreshes() {
        return this.refreshNanos > 0;
    }

    boolean usesAccessQueue() {
        if (!expiresAfterAccess()) {
            if (!evictsBySize()) {
                return false;
            }
        }
        return true;
    }

    boolean usesWriteQueue() {
        return expiresAfterWrite();
    }

    boolean recordsWrite() {
        if (!expiresAfterWrite()) {
            if (!refreshes()) {
                return false;
            }
        }
        return true;
    }

    boolean recordsAccess() {
        return expiresAfterAccess();
    }

    boolean recordsTime() {
        if (!recordsWrite()) {
            if (!recordsAccess()) {
                return false;
            }
        }
        return true;
    }

    boolean usesWriteEntries() {
        if (!usesWriteQueue()) {
            if (!recordsWrite()) {
                return false;
            }
        }
        return true;
    }

    boolean usesAccessEntries() {
        if (!usesAccessQueue()) {
            if (!recordsAccess()) {
                return false;
            }
        }
        return true;
    }

    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }

    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }

    static <K, V> ValueReference<K, V> unset() {
        return UNSET;
    }

    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return NullEntry.INSTANCE;
    }

    static <E> Queue<E> discardingQueue() {
        return DISCARDING_QUEUE;
    }

    @VisibleForTesting
    ReferenceEntry<K, V> newEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
        Segment segmentFor = segmentFor(i);
        segmentFor.lock();
        try {
            k = segmentFor.newEntry(k, i, referenceEntry);
            return k;
        } finally {
            segmentFor.unlock();
        }
    }

    @VisibleForTesting
    ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        return segmentFor(referenceEntry.getHash()).copyEntry(referenceEntry, referenceEntry2);
    }

    @VisibleForTesting
    ValueReference<K, V> newValueReference(ReferenceEntry<K, V> referenceEntry, V v, int i) {
        return this.valueStrength.referenceValue(segmentFor(referenceEntry.getHash()), referenceEntry, Preconditions.checkNotNull(v), i);
    }

    int hash(@Nullable Object obj) {
        return rehash(this.keyEquivalence.hash(obj));
    }

    void reclaimValue(ValueReference<K, V> valueReference) {
        ReferenceEntry entry = valueReference.getEntry();
        int hash = entry.getHash();
        segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }

    void reclaimKey(ReferenceEntry<K, V> referenceEntry) {
        int hash = referenceEntry.getHash();
        segmentFor(hash).reclaimKey(referenceEntry, hash);
    }

    @VisibleForTesting
    boolean isLive(ReferenceEntry<K, V> referenceEntry, long j) {
        return segmentFor(referenceEntry.getHash()).getLiveValue(referenceEntry, j) != null ? true : null;
    }

    Segment<K, V> segmentFor(int i) {
        return this.segments[(i >>> this.segmentShift) & this.segmentMask];
    }

    Segment<K, V> createSegment(int i, long j, StatsCounter statsCounter) {
        return new Segment(this, i, j, statsCounter);
    }

    @Nullable
    V getLiveValue(ReferenceEntry<K, V> referenceEntry, long j) {
        if (referenceEntry.getKey() == null) {
            return null;
        }
        V v = referenceEntry.getValueReference().get();
        if (v != null && isExpired(referenceEntry, j) == null) {
            return v;
        }
        return null;
    }

    boolean isExpired(ReferenceEntry<K, V> referenceEntry, long j) {
        Preconditions.checkNotNull(referenceEntry);
        if (expiresAfterAccess() && j - referenceEntry.getAccessTime() >= this.expireAfterAccessNanos) {
            return true;
        }
        if (!expiresAfterWrite() || j - referenceEntry.getWriteTime() < this.expireAfterWriteNanos) {
            return null;
        }
        return true;
    }

    static <K, V> void connectAccessOrder(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextInAccessQueue(referenceEntry2);
        referenceEntry2.setPreviousInAccessQueue(referenceEntry);
    }

    static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry nullEntry = nullEntry();
        referenceEntry.setNextInAccessQueue(nullEntry);
        referenceEntry.setPreviousInAccessQueue(nullEntry);
    }

    static <K, V> void connectWriteOrder(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextInWriteQueue(referenceEntry2);
        referenceEntry2.setPreviousInWriteQueue(referenceEntry);
    }

    static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry nullEntry = nullEntry();
        referenceEntry.setNextInWriteQueue(nullEntry);
        referenceEntry.setPreviousInWriteQueue(nullEntry);
    }

    void processPendingNotifications() {
        while (true) {
            RemovalNotification removalNotification = (RemovalNotification) this.removalNotificationQueue.poll();
            if (removalNotification != null) {
                try {
                    this.removalListener.onRemoval(removalNotification);
                } catch (Throwable th) {
                    logger.log(Level.WARNING, "Exception thrown by removal listener", th);
                }
            } else {
                return;
            }
        }
    }

    final Segment<K, V>[] newSegmentArray(int i) {
        return new Segment[i];
    }

    public void cleanUp() {
        for (Segment cleanUp : this.segments) {
            cleanUp.cleanUp();
        }
    }

    public boolean isEmpty() {
        Segment[] segmentArr = this.segments;
        long j = 0;
        int i = 0;
        while (i < segmentArr.length) {
            if (segmentArr[i].count != 0) {
                return false;
            }
            i++;
            j += (long) segmentArr[i].modCount;
        }
        if (j != 0) {
            i = 0;
            while (i < segmentArr.length) {
                if (segmentArr[i].count != 0) {
                    return false;
                }
                i++;
                j -= (long) segmentArr[i].modCount;
            }
            if (j != 0) {
                return false;
            }
        }
        return true;
    }

    long longSize() {
        Segment[] segmentArr = this.segments;
        long j = 0;
        int i = 0;
        while (i < segmentArr.length) {
            i++;
            j += (long) segmentArr[i].count;
        }
        return j;
    }

    public int size() {
        return Ints.saturatedCast(longSize());
    }

    @Nullable
    public V get(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        return segmentFor(hash).get(obj, hash);
    }

    @Nullable
    public V getIfPresent(Object obj) {
        int hash = hash(Preconditions.checkNotNull(obj));
        obj = segmentFor(hash).get(obj, hash);
        if (obj == null) {
            this.globalStatsCounter.recordMisses(1);
        } else {
            this.globalStatsCounter.recordHits(1);
        }
        return obj;
    }

    V get(K k, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
        int hash = hash(Preconditions.checkNotNull(k));
        return segmentFor(hash).get(k, hash, cacheLoader);
    }

    V getOrLoad(K k) throws ExecutionException {
        return get(k, this.defaultLoader);
    }

    ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
        Map newLinkedHashMap = Maps.newLinkedHashMap();
        int i = 0;
        int i2 = 0;
        for (Object next : iterable) {
            Object obj = get(next);
            if (obj == null) {
                i2++;
            } else {
                newLinkedHashMap.put(next, obj);
                i++;
            }
        }
        this.globalStatsCounter.recordHits(i);
        this.globalStatsCounter.recordMisses(i2);
        return ImmutableMap.copyOf(newLinkedHashMap);
    }

    com.google.common.collect.ImmutableMap<K, V> getAll(java.lang.Iterable<? extends K> r8) throws java.util.concurrent.ExecutionException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r7 = this;
        r0 = com.google.common.collect.Maps.newLinkedHashMap();
        r1 = com.google.common.collect.Sets.newLinkedHashSet();
        r8 = r8.iterator();
        r2 = 0;
        r3 = 0;
    L_0x000e:
        r4 = r8.hasNext();
        if (r4 == 0) goto L_0x0030;
    L_0x0014:
        r4 = r8.next();
        r5 = r7.get(r4);
        r6 = r0.containsKey(r4);
        if (r6 != 0) goto L_0x000e;
    L_0x0022:
        r0.put(r4, r5);
        if (r5 != 0) goto L_0x002d;
    L_0x0027:
        r2 = r2 + 1;
        r1.add(r4);
        goto L_0x000e;
    L_0x002d:
        r3 = r3 + 1;
        goto L_0x000e;
    L_0x0030:
        r8 = r1.isEmpty();	 Catch:{ all -> 0x0094 }
        if (r8 != 0) goto L_0x0085;
    L_0x0036:
        r8 = r7.defaultLoader;	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r8 = r7.loadAll(r1, r8);	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r4 = r1.iterator();	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
    L_0x0040:
        r5 = r4.hasNext();	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        if (r5 == 0) goto L_0x0085;	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
    L_0x0046:
        r5 = r4.next();	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r6 = r8.get(r5);	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        if (r6 != 0) goto L_0x0067;	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
    L_0x0050:
        r8 = new com.google.common.cache.CacheLoader$InvalidCacheLoadException;	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r4 = new java.lang.StringBuilder;	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r4.<init>();	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r6 = "loadAll failed to return a value for ";	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r4.append(r6);	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r4.append(r5);	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r4 = r4.toString();	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        r8.<init>(r4);	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        throw r8;	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
    L_0x0067:
        r0.put(r5, r6);	 Catch:{ UnsupportedLoadingOperationException -> 0x006b }
        goto L_0x0040;
    L_0x006b:
        r8 = r1.iterator();	 Catch:{ all -> 0x0094 }
    L_0x006f:
        r1 = r8.hasNext();	 Catch:{ all -> 0x0094 }
        if (r1 == 0) goto L_0x0085;	 Catch:{ all -> 0x0094 }
    L_0x0075:
        r1 = r8.next();	 Catch:{ all -> 0x0094 }
        r2 = r2 + -1;	 Catch:{ all -> 0x0094 }
        r4 = r7.defaultLoader;	 Catch:{ all -> 0x0094 }
        r4 = r7.get(r1, r4);	 Catch:{ all -> 0x0094 }
        r0.put(r1, r4);	 Catch:{ all -> 0x0094 }
        goto L_0x006f;	 Catch:{ all -> 0x0094 }
    L_0x0085:
        r8 = com.google.common.collect.ImmutableMap.copyOf(r0);	 Catch:{ all -> 0x0094 }
        r0 = r7.globalStatsCounter;
        r0.recordHits(r3);
        r0 = r7.globalStatsCounter;
        r0.recordMisses(r2);
        return r8;
    L_0x0094:
        r8 = move-exception;
        r0 = r7.globalStatsCounter;
        r0.recordHits(r3);
        r0 = r7.globalStatsCounter;
        r0.recordMisses(r2);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.cache.LocalCache.getAll(java.lang.Iterable):com.google.common.collect.ImmutableMap<K, V>");
    }

    @Nullable
    Map<K, V> loadAll(Set<? extends K> set, CacheLoader<? super K, V> cacheLoader) throws ExecutionException {
        Preconditions.checkNotNull(cacheLoader);
        Preconditions.checkNotNull(set);
        Stopwatch createStarted = Stopwatch.createStarted();
        Object obj = null;
        try {
            set = cacheLoader.loadAll(set);
            StringBuilder stringBuilder;
            if (set == null) {
                this.globalStatsCounter.recordLoadException(createStarted.elapsed(TimeUnit.NANOSECONDS));
                stringBuilder = new StringBuilder();
                stringBuilder.append(cacheLoader);
                stringBuilder.append(" returned null map from loadAll");
                throw new InvalidCacheLoadException(stringBuilder.toString());
            }
            createStarted.stop();
            for (Entry entry : set.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key != null) {
                    if (value != null) {
                        put(key, value);
                    }
                }
                obj = 1;
            }
            if (obj != null) {
                this.globalStatsCounter.recordLoadException(createStarted.elapsed(TimeUnit.NANOSECONDS));
                stringBuilder = new StringBuilder();
                stringBuilder.append(cacheLoader);
                stringBuilder.append(" returned null keys or values from loadAll");
                throw new InvalidCacheLoadException(stringBuilder.toString());
            }
            this.globalStatsCounter.recordLoadSuccess(createStarted.elapsed(TimeUnit.NANOSECONDS));
            return set;
        } catch (Set<? extends K> set2) {
            throw set2;
        } catch (Set<? extends K> set22) {
            Thread.currentThread().interrupt();
            throw new ExecutionException(set22);
        } catch (Throwable e) {
            throw new UncheckedExecutionException(e);
        } catch (Set<? extends K> set222) {
            throw new ExecutionException(set222);
        } catch (Error e2) {
            throw new ExecutionError(e2);
        } catch (Throwable th) {
            set222 = th;
            obj = 1;
        }
        if (obj == null) {
            this.globalStatsCounter.recordLoadException(createStarted.elapsed(TimeUnit.NANOSECONDS));
        }
        throw set222;
    }

    ReferenceEntry<K, V> getEntry(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        return segmentFor(hash).getEntry(obj, hash);
    }

    void refresh(K k) {
        int hash = hash(Preconditions.checkNotNull(k));
        segmentFor(hash).refresh(k, hash, this.defaultLoader, false);
    }

    public boolean containsKey(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        return segmentFor(hash).containsKey(obj, hash);
    }

    public boolean containsValue(@Nullable Object obj) {
        LocalCache localCache = this;
        Object obj2 = obj;
        if (obj2 == null) {
            return false;
        }
        long read = localCache.ticker.read();
        Segment[] segmentArr = localCache.segments;
        long j = -1;
        int i = 0;
        while (i < 3) {
            Segment[] segmentArr2;
            long j2;
            int length = segmentArr.length;
            long j3 = 0;
            int i2 = 0;
            while (i2 < length) {
                Segment segment = segmentArr[i2];
                int i3 = segment.count;
                AtomicReferenceArray atomicReferenceArray = segment.table;
                for (int i4 = 0; i4 < atomicReferenceArray.length(); i4++) {
                    ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i4);
                    while (referenceEntry != null) {
                        segmentArr2 = segmentArr;
                        Object liveValue = segment.getLiveValue(referenceEntry, read);
                        if (liveValue != null) {
                            j2 = read;
                            if (localCache.valueEquivalence.equivalent(obj2, liveValue)) {
                                return true;
                            }
                        } else {
                            j2 = read;
                        }
                        referenceEntry = referenceEntry.getNext();
                        segmentArr = segmentArr2;
                        read = j2;
                    }
                    j2 = read;
                    segmentArr2 = segmentArr;
                }
                i2++;
                j3 += (long) segment.modCount;
                segmentArr = segmentArr;
                read = read;
            }
            j2 = read;
            segmentArr2 = segmentArr;
            if (j3 == j) {
                break;
            }
            i++;
            j = j3;
            segmentArr = segmentArr2;
            read = j2;
        }
        return false;
    }

    public V put(K k, V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        int hash = hash(k);
        return segmentFor(hash).put(k, hash, v, false);
    }

    public V putIfAbsent(K k, V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        int hash = hash(k);
        return segmentFor(hash).put(k, hash, v, true);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    public V remove(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        return segmentFor(hash).remove(obj, hash);
    }

    public boolean remove(@Nullable Object obj, @Nullable Object obj2) {
        if (obj != null) {
            if (obj2 != null) {
                int hash = hash(obj);
                return segmentFor(hash).remove(obj, hash, obj2);
            }
        }
        return null;
    }

    public boolean replace(K k, @Nullable V v, V v2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v2);
        if (v == null) {
            return null;
        }
        int hash = hash(k);
        return segmentFor(hash).replace(k, hash, v, v2);
    }

    public V replace(K k, V v) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        int hash = hash(k);
        return segmentFor(hash).replace(k, hash, v);
    }

    public void clear() {
        for (Segment clear : this.segments) {
            clear.clear();
        }
    }

    void invalidateAll(Iterable<?> iterable) {
        for (Object remove : iterable) {
            remove(remove);
        }
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        set = new KeySet(this);
        this.keySet = set;
        return set;
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = new Values(this);
        this.values = collection;
        return collection;
    }

    @GwtIncompatible("Not supported.")
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        set = new EntrySet(this);
        this.entrySet = set;
        return set;
    }
}
