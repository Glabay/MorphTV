package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import java.util.concurrent.CancellationException;
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

class MapMakerInternalMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    static final long CLEANUP_EXECUTOR_DELAY_SECS = 60;
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final Queue<? extends Object> DISCARDING_QUEUE = new C09802();
    static final int DRAIN_MAX = 16;
    static final int DRAIN_THRESHOLD = 63;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final ValueReference<Object, Object> UNSET = new C09791();
    private static final Logger logger = Logger.getLogger(MapMakerInternalMap.class.getName());
    private static final long serialVersionUID = 5;
    final int concurrencyLevel;
    final transient EntryFactory entryFactory;
    transient Set<Entry<K, V>> entrySet;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final Equivalence<Object> keyEquivalence;
    transient Set<K> keySet;
    final Strength keyStrength;
    final int maximumSize;
    final RemovalListener<K, V> removalListener;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V>[] segments;
    final Ticker ticker;
    final Equivalence<Object> valueEquivalence = this.valueStrength.defaultEquivalence();
    final Strength valueStrength;
    transient Collection<V> values;

    interface ValueReference<K, V> {
        void clear(@Nullable ValueReference<K, V> valueReference);

        ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, @Nullable V v, ReferenceEntry<K, V> referenceEntry);

        V get();

        ReferenceEntry<K, V> getEntry();

        boolean isComputingReference();

        V waitForValue() throws ExecutionException;
    }

    static class Segment<K, V> extends ReentrantLock {
        volatile int count;
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> evictionQueue;
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> expirationQueue;
        final ReferenceQueue<K> keyReferenceQueue;
        final MapMakerInternalMap<K, V> map;
        final int maxSegmentSize;
        int modCount;
        final AtomicInteger readCount = new AtomicInteger();
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        int threshold;
        final ReferenceQueue<V> valueReferenceQueue;

        Segment(MapMakerInternalMap<K, V> mapMakerInternalMap, int i, int i2) {
            this.map = mapMakerInternalMap;
            this.maxSegmentSize = i2;
            initTable(newEntryArray(i));
            i2 = 0;
            this.keyReferenceQueue = mapMakerInternalMap.usesKeyReferences() != 0 ? new ReferenceQueue() : 0;
            if (mapMakerInternalMap.usesValueReferences() != 0) {
                i2 = new ReferenceQueue();
            }
            this.valueReferenceQueue = i2;
            if (mapMakerInternalMap.evictsBySize() == 0) {
                if (mapMakerInternalMap.expiresAfterAccess() == 0) {
                    i = MapMakerInternalMap.discardingQueue();
                    this.recencyQueue = i;
                    this.evictionQueue = mapMakerInternalMap.evictsBySize() == 0 ? new EvictionQueue() : MapMakerInternalMap.discardingQueue();
                    this.expirationQueue = mapMakerInternalMap.expires() == null ? new ExpirationQueue() : MapMakerInternalMap.discardingQueue();
                }
            }
            i = new ConcurrentLinkedQueue();
            this.recencyQueue = i;
            if (mapMakerInternalMap.evictsBySize() == 0) {
            }
            this.evictionQueue = mapMakerInternalMap.evictsBySize() == 0 ? new EvictionQueue() : MapMakerInternalMap.discardingQueue();
            if (mapMakerInternalMap.expires() == null) {
            }
            this.expirationQueue = mapMakerInternalMap.expires() == null ? new ExpirationQueue() : MapMakerInternalMap.discardingQueue();
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int i) {
            return new AtomicReferenceArray(i);
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> atomicReferenceArray) {
            this.threshold = (atomicReferenceArray.length() * 3) / 4;
            if (this.threshold == this.maxSegmentSize) {
                this.threshold++;
            }
            this.table = atomicReferenceArray;
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> newEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            return this.map.entryFactory.newEntry(this, k, i, referenceEntry);
        }

        @GuardedBy("this")
        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            if (referenceEntry.getKey() == null) {
                return null;
            }
            ValueReference valueReference = referenceEntry.getValueReference();
            Object obj = valueReference.get();
            if (obj == null && !valueReference.isComputingReference()) {
                return null;
            }
            referenceEntry = this.map.entryFactory.copyEntry(this, referenceEntry, referenceEntry2);
            referenceEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, obj, referenceEntry));
            return referenceEntry;
        }

        @GuardedBy("this")
        void setValue(ReferenceEntry<K, V> referenceEntry, V v) {
            referenceEntry.setValueReference(this.map.valueStrength.referenceValue(this, referenceEntry, v));
            recordWrite(referenceEntry);
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

        void recordRead(ReferenceEntry<K, V> referenceEntry) {
            if (this.map.expiresAfterAccess()) {
                recordExpirationTime(referenceEntry, this.map.expireAfterAccessNanos);
            }
            this.recencyQueue.add(referenceEntry);
        }

        @GuardedBy("this")
        void recordLockedRead(ReferenceEntry<K, V> referenceEntry) {
            this.evictionQueue.add(referenceEntry);
            if (this.map.expiresAfterAccess()) {
                recordExpirationTime(referenceEntry, this.map.expireAfterAccessNanos);
                this.expirationQueue.add(referenceEntry);
            }
        }

        @GuardedBy("this")
        void recordWrite(ReferenceEntry<K, V> referenceEntry) {
            drainRecencyQueue();
            this.evictionQueue.add(referenceEntry);
            if (this.map.expires()) {
                recordExpirationTime(referenceEntry, this.map.expiresAfterAccess() ? this.map.expireAfterAccessNanos : this.map.expireAfterWriteNanos);
                this.expirationQueue.add(referenceEntry);
            }
        }

        @GuardedBy("this")
        void drainRecencyQueue() {
            while (true) {
                ReferenceEntry referenceEntry = (ReferenceEntry) this.recencyQueue.poll();
                if (referenceEntry != null) {
                    if (this.evictionQueue.contains(referenceEntry)) {
                        this.evictionQueue.add(referenceEntry);
                    }
                    if (this.map.expiresAfterAccess() && this.expirationQueue.contains(referenceEntry)) {
                        this.expirationQueue.add(referenceEntry);
                    }
                } else {
                    return;
                }
            }
        }

        void recordExpirationTime(ReferenceEntry<K, V> referenceEntry, long j) {
            referenceEntry.setExpirationTime(this.map.ticker.read() + j);
        }

        void tryExpireEntries() {
            if (tryLock()) {
                try {
                    expireEntries();
                } finally {
                    unlock();
                }
            }
        }

        @GuardedBy("this")
        void expireEntries() {
            drainRecencyQueue();
            if (!this.expirationQueue.isEmpty()) {
                long read = this.map.ticker.read();
                ReferenceEntry referenceEntry;
                do {
                    referenceEntry = (ReferenceEntry) this.expirationQueue.peek();
                    if (referenceEntry == null || !this.map.isExpired(referenceEntry, read)) {
                        return;
                    }
                } while (removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.EXPIRED));
                throw new AssertionError();
            }
        }

        void enqueueNotification(ReferenceEntry<K, V> referenceEntry, RemovalCause removalCause) {
            enqueueNotification(referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry.getValueReference().get(), removalCause);
        }

        void enqueueNotification(@Nullable K k, int i, @Nullable V v, RemovalCause removalCause) {
            if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                this.map.removalNotificationQueue.offer(new RemovalNotification(k, v, removalCause));
            }
        }

        @GuardedBy("this")
        boolean evictEntries() {
            if (!this.map.evictsBySize() || this.count < this.maxSegmentSize) {
                return false;
            }
            drainRecencyQueue();
            ReferenceEntry referenceEntry = (ReferenceEntry) this.evictionQueue.remove();
            if (removeEntry(referenceEntry, referenceEntry.getHash(), RemovalCause.SIZE)) {
                return true;
            }
            throw new AssertionError();
        }

        ReferenceEntry<K, V> getFirst(int i) {
            AtomicReferenceArray atomicReferenceArray = this.table;
            return (ReferenceEntry) atomicReferenceArray.get(i & (atomicReferenceArray.length() - 1));
        }

        ReferenceEntry<K, V> getEntry(Object obj, int i) {
            if (this.count != 0) {
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
            }
            return null;
        }

        ReferenceEntry<K, V> getLiveEntry(Object obj, int i) {
            obj = getEntry(obj, i);
            if (obj == null) {
                return null;
            }
            if (!this.map.expires() || !this.map.isExpired(obj)) {
                return obj;
            }
            tryExpireEntries();
            return null;
        }

        V get(Object obj, int i) {
            try {
                obj = getLiveEntry(obj, i);
                if (obj == null) {
                    return null;
                }
                i = obj.getValueReference().get();
                if (i != 0) {
                    recordRead(obj);
                } else {
                    tryDrainReferenceQueues();
                }
                postReadCleanup();
                return i;
            } finally {
                postReadCleanup();
            }
        }

        boolean containsKey(Object obj, int i) {
            try {
                boolean z = false;
                if (this.count != 0) {
                    obj = getLiveEntry(obj, i);
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
                    AtomicReferenceArray atomicReferenceArray = this.table;
                    int length = atomicReferenceArray.length();
                    for (int i = 0; i < length; i++) {
                        for (ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                            Object liveValue = getLiveValue(referenceEntry);
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

        V put(K k, int i, V v, boolean z) {
            lock();
            try {
                preWriteCleanup();
                int i2 = this.count + 1;
                if (i2 > this.threshold) {
                    expand();
                    i2 = this.count + 1;
                }
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        V v2 = valueReference.get();
                        if (v2 == null) {
                            this.modCount++;
                            setValue(referenceEntry2, v);
                            if (valueReference.isComputingReference() == null) {
                                enqueueNotification(k, i, v2, RemovalCause.COLLECTED);
                                i2 = this.count;
                            } else if (evictEntries() != null) {
                                i2 = this.count + 1;
                            }
                            this.count = i2;
                            unlock();
                            postWriteCleanup();
                            return null;
                        }
                        if (z) {
                            recordLockedRead(referenceEntry2);
                        } else {
                            this.modCount++;
                            enqueueNotification(k, i, v2, RemovalCause.REPLACED);
                            setValue(referenceEntry2, v);
                        }
                        unlock();
                        postWriteCleanup();
                        return v2;
                    }
                }
                this.modCount++;
                k = newEntry(k, i, referenceEntry);
                setValue(k, v);
                atomicReferenceArray.set(length, k);
                if (evictEntries() != null) {
                    i2 = this.count + 1;
                }
                this.count = i2;
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
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        Object obj = valueReference.get();
                        if (obj == null) {
                            if (isCollected(valueReference) != null) {
                                k = this.count;
                                this.modCount += 1;
                                enqueueNotification(key, i, obj, RemovalCause.COLLECTED);
                                i = this.count - 1;
                                atomicReferenceArray.set(length, removeFromChain(referenceEntry, referenceEntry2));
                                this.count = i;
                            }
                        } else if (this.map.valueEquivalence.equivalent(v, obj) != null) {
                            this.modCount += 1;
                            enqueueNotification(k, i, obj, RemovalCause.REPLACED);
                            setValue(referenceEntry2, v2);
                            unlock();
                            postWriteCleanup();
                            return true;
                        } else {
                            recordLockedRead(referenceEntry2);
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

        V replace(K k, int i, V v) {
            lock();
            try {
                preWriteCleanup();
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        ValueReference valueReference = referenceEntry2.getValueReference();
                        V v2 = valueReference.get();
                        if (v2 == null) {
                            if (isCollected(valueReference) != null) {
                                k = this.count;
                                this.modCount++;
                                enqueueNotification(key, i, v2, RemovalCause.COLLECTED);
                                i = this.count - 1;
                                atomicReferenceArray.set(length, removeFromChain(referenceEntry, referenceEntry2));
                                this.count = i;
                            }
                            unlock();
                            postWriteCleanup();
                            return null;
                        }
                        this.modCount++;
                        enqueueNotification(k, i, v2, RemovalCause.REPLACED);
                        setValue(referenceEntry2, v);
                        unlock();
                        postWriteCleanup();
                        return v2;
                    }
                }
                unlock();
                postWriteCleanup();
                return null;
            } catch (Throwable th) {
                unlock();
                postWriteCleanup();
            }
        }

        V remove(Object obj, int i) {
            lock();
            try {
                preWriteCleanup();
                int i2 = this.count;
                AtomicReferenceArray atomicReferenceArray = this.table;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    V hash = referenceEntry2.getHash();
                    if (hash == i && key != null) {
                        hash = this.map.keyEquivalence.equivalent(obj, key);
                        if (hash != null) {
                            obj = referenceEntry2.getValueReference();
                            hash = obj.get();
                            if (hash != null) {
                                obj = RemovalCause.EXPLICIT;
                            } else {
                                if (isCollected(obj) != null) {
                                    obj = RemovalCause.COLLECTED;
                                }
                                unlock();
                                postWriteCleanup();
                                return null;
                            }
                            this.modCount++;
                            enqueueNotification(key, i, hash, obj);
                            i = this.count - 1;
                            atomicReferenceArray.set(length, removeFromChain(referenceEntry, referenceEntry2));
                            this.count = i;
                            return hash;
                        }
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

        boolean remove(Object obj, int i, Object obj2) {
            lock();
            try {
                preWriteCleanup();
                int i2 = this.count;
                AtomicReferenceArray atomicReferenceArray = this.table;
                boolean z = true;
                int length = (atomicReferenceArray.length() - 1) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(obj, key)) {
                        obj = referenceEntry2.getValueReference();
                        Object obj3 = obj.get();
                        if (this.map.valueEquivalence.equivalent(obj2, obj3) != null) {
                            obj = RemovalCause.EXPLICIT;
                        } else {
                            if (isCollected(obj) != null) {
                                obj = RemovalCause.COLLECTED;
                            }
                            unlock();
                            postWriteCleanup();
                            return false;
                        }
                        this.modCount += 1;
                        enqueueNotification(key, i, obj3, obj);
                        obj2 = this.count - 1;
                        atomicReferenceArray.set(length, removeFromChain(referenceEntry, referenceEntry2));
                        this.count = obj2;
                        if (obj != RemovalCause.EXPLICIT) {
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
                    if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                        for (i = 0; i < atomicReferenceArray.length(); i++) {
                            for (ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                                if (!referenceEntry.getValueReference().isComputingReference()) {
                                    enqueueNotification(referenceEntry, RemovalCause.EXPLICIT);
                                }
                            }
                        }
                    }
                    for (i = 0; i < atomicReferenceArray.length(); i++) {
                        atomicReferenceArray.set(i, null);
                    }
                    clearReferenceQueues();
                    this.evictionQueue.clear();
                    this.expirationQueue.clear();
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
        ReferenceEntry<K, V> removeFromChain(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            this.evictionQueue.remove(referenceEntry2);
            this.expirationQueue.remove(referenceEntry2);
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

        void removeCollectedEntry(ReferenceEntry<K, V> referenceEntry) {
            enqueueNotification(referenceEntry, RemovalCause.COLLECTED);
            this.evictionQueue.remove(referenceEntry);
            this.expirationQueue.remove(referenceEntry);
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
                        enqueueNotification(referenceEntry3.getKey(), i, referenceEntry3.getValueReference().get(), RemovalCause.COLLECTED);
                        i = this.count - z;
                        atomicReferenceArray.set(length, removeFromChain(referenceEntry2, referenceEntry3));
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
                        enqueueNotification(k, i, valueReference.get(), RemovalCause.COLLECTED);
                        i = this.count - z;
                        atomicReferenceArray.set(length, removeFromChain(referenceEntry, referenceEntry2));
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

        boolean clearValue(K k, int i, ValueReference<K, V> valueReference) {
            lock();
            try {
                AtomicReferenceArray atomicReferenceArray = this.table;
                boolean z = true;
                int length = (atomicReferenceArray.length() - z) & i;
                ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(length);
                for (ReferenceEntry referenceEntry2 = referenceEntry; referenceEntry2 != null; referenceEntry2 = referenceEntry2.getNext()) {
                    Object key = referenceEntry2.getKey();
                    if (referenceEntry2.getHash() == i && key != null && this.map.keyEquivalence.equivalent(k, key)) {
                        if (referenceEntry2.getValueReference() == valueReference) {
                            atomicReferenceArray.set(length, removeFromChain(referenceEntry, referenceEntry2));
                            return z;
                        }
                        unlock();
                        postWriteCleanup();
                        return false;
                    }
                }
                unlock();
                postWriteCleanup();
                return false;
            } finally {
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
                    enqueueNotification(referenceEntry3.getKey(), i, referenceEntry3.getValueReference().get(), removalCause);
                    i = this.count - 1;
                    atomicReferenceArray.set(length, removeFromChain(referenceEntry2, referenceEntry3));
                    this.count = i;
                    return true;
                }
            }
            return null;
        }

        boolean isCollected(ValueReference<K, V> valueReference) {
            boolean z = false;
            if (valueReference.isComputingReference()) {
                return false;
            }
            if (valueReference.get() == null) {
                z = true;
            }
            return z;
        }

        V getLiveValue(ReferenceEntry<K, V> referenceEntry) {
            if (referenceEntry.getKey() == null) {
                tryDrainReferenceQueues();
                return null;
            }
            V v = referenceEntry.getValueReference().get();
            if (v == null) {
                tryDrainReferenceQueues();
                return null;
            } else if (!this.map.expires() || this.map.isExpired(referenceEntry) == null) {
                return v;
            } else {
                tryExpireEntries();
                return null;
            }
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 63) == 0) {
                runCleanup();
            }
        }

        @GuardedBy("this")
        void preWriteCleanup() {
            runLockedCleanup();
        }

        void postWriteCleanup() {
            runUnlockedCleanup();
        }

        void runCleanup() {
            runLockedCleanup();
            runUnlockedCleanup();
        }

        void runLockedCleanup() {
            if (tryLock()) {
                try {
                    drainReferenceQueues();
                    expireEntries();
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

    static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
        private static final long serialVersionUID = 3;
        final int concurrencyLevel;
        transient ConcurrentMap<K, V> delegate;
        final long expireAfterAccessNanos;
        final long expireAfterWriteNanos;
        final Equivalence<Object> keyEquivalence;
        final Strength keyStrength;
        final int maximumSize;
        final RemovalListener<? super K, ? super V> removalListener;
        final Equivalence<Object> valueEquivalence;
        final Strength valueStrength;

        AbstractSerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, long j, long j2, int i, int i2, RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> concurrentMap) {
            this.keyStrength = strength;
            this.valueStrength = strength2;
            this.keyEquivalence = equivalence;
            this.valueEquivalence = equivalence2;
            this.expireAfterWriteNanos = j;
            this.expireAfterAccessNanos = j2;
            this.maximumSize = i;
            this.concurrencyLevel = i2;
            this.removalListener = removalListener;
            this.delegate = concurrentMap;
        }

        protected ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }

        void writeMapTo(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeInt(this.delegate.size());
            for (Entry entry : this.delegate.entrySet()) {
                objectOutputStream.writeObject(entry.getKey());
                objectOutputStream.writeObject(entry.getValue());
            }
            objectOutputStream.writeObject(null);
        }

        MapMaker readMapMaker(ObjectInputStream objectInputStream) throws IOException {
            objectInputStream = new MapMaker().initialCapacity(objectInputStream.readInt()).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
            objectInputStream.removalListener(this.removalListener);
            if (this.expireAfterWriteNanos > 0) {
                objectInputStream.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
            }
            if (this.expireAfterAccessNanos > 0) {
                objectInputStream.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
            }
            if (this.maximumSize != -1) {
                objectInputStream.maximumSize(this.maximumSize);
            }
            return objectInputStream;
        }

        void readEntries(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            while (true) {
                Object readObject = objectInputStream.readObject();
                if (readObject != null) {
                    this.delegate.put(readObject, objectInputStream.readObject());
                } else {
                    return;
                }
            }
        }
    }

    /* renamed from: com.google.common.collect.MapMakerInternalMap$1 */
    static class C09791 implements ValueReference<Object, Object> {
        public void clear(ValueReference<Object, Object> valueReference) {
        }

        public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> referenceQueue, @Nullable Object obj, ReferenceEntry<Object, Object> referenceEntry) {
            return this;
        }

        public Object get() {
            return null;
        }

        public ReferenceEntry<Object, Object> getEntry() {
            return null;
        }

        public boolean isComputingReference() {
            return false;
        }

        public Object waitForValue() {
            return null;
        }

        C09791() {
        }
    }

    /* renamed from: com.google.common.collect.MapMakerInternalMap$2 */
    static class C09802 extends AbstractQueue<Object> {
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

        C09802() {
        }

        public Iterator<Object> iterator() {
            return Iterators.emptyIterator();
        }
    }

    interface ReferenceEntry<K, V> {
        long getExpirationTime();

        int getHash();

        K getKey();

        ReferenceEntry<K, V> getNext();

        ReferenceEntry<K, V> getNextEvictable();

        ReferenceEntry<K, V> getNextExpirable();

        ReferenceEntry<K, V> getPreviousEvictable();

        ReferenceEntry<K, V> getPreviousExpirable();

        ValueReference<K, V> getValueReference();

        void setExpirationTime(long j);

        void setNextEvictable(ReferenceEntry<K, V> referenceEntry);

        void setNextExpirable(ReferenceEntry<K, V> referenceEntry);

        void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry);

        void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry);

        void setValueReference(ValueReference<K, V> valueReference);
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

        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        public void setExpirationTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }
    }

    static final class CleanupMapTask implements Runnable {
        final WeakReference<MapMakerInternalMap<?, ?>> mapReference;

        public CleanupMapTask(MapMakerInternalMap<?, ?> mapMakerInternalMap) {
            this.mapReference = new WeakReference(mapMakerInternalMap);
        }

        public void run() {
            MapMakerInternalMap mapMakerInternalMap = (MapMakerInternalMap) this.mapReference.get();
            if (mapMakerInternalMap == null) {
                throw new CancellationException();
            }
            for (Segment runCleanup : mapMakerInternalMap.segments) {
                runCleanup.runCleanup();
            }
        }
    }

    enum EntryFactory {
        STRONG {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongEntry(k, i, referenceEntry);
            }
        },
        STRONG_EXPIRABLE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongExpirableEntry(k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyExpirableEntry(referenceEntry, segment);
                return segment;
            }
        },
        STRONG_EVICTABLE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongEvictableEntry(k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyEvictableEntry(referenceEntry, segment);
                return segment;
            }
        },
        STRONG_EXPIRABLE_EVICTABLE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new StrongExpirableEvictableEntry(k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyExpirableEntry(referenceEntry, segment);
                copyEvictableEntry(referenceEntry, segment);
                return segment;
            }
        },
        WEAK {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }
        },
        WEAK_EXPIRABLE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakExpirableEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyExpirableEntry(referenceEntry, segment);
                return segment;
            }
        },
        WEAK_EVICTABLE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakEvictableEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyEvictableEntry(referenceEntry, segment);
                return segment;
            }
        },
        WEAK_EXPIRABLE_EVICTABLE {
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
                return new WeakExpirableEvictableEntry(segment.keyReferenceQueue, k, i, referenceEntry);
            }

            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                segment = super.copyEntry(segment, referenceEntry, referenceEntry2);
                copyExpirableEntry(referenceEntry, segment);
                copyEvictableEntry(referenceEntry, segment);
                return segment;
            }
        };
        
        static final int EVICTABLE_MASK = 2;
        static final int EXPIRABLE_MASK = 1;
        static final EntryFactory[][] factories = null;

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry);

        static {
            r0 = new EntryFactory[3][];
            r0[0] = new EntryFactory[]{STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE};
            r0[1] = new EntryFactory[0];
            r0[2] = new EntryFactory[]{WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE};
            factories = r0;
        }

        static EntryFactory getFactory(Strength strength, boolean z, boolean z2) {
            return factories[strength.ordinal()][z | (z2 ? true : false)];
        }

        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            return newEntry(segment, referenceEntry.getKey(), referenceEntry.getHash(), referenceEntry2);
        }

        <K, V> void copyExpirableEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            referenceEntry2.setExpirationTime(referenceEntry.getExpirationTime());
            MapMakerInternalMap.connectExpirables(referenceEntry.getPreviousExpirable(), referenceEntry2);
            MapMakerInternalMap.connectExpirables(referenceEntry2, referenceEntry.getNextExpirable());
            MapMakerInternalMap.nullifyExpirable(referenceEntry);
        }

        <K, V> void copyEvictableEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            MapMakerInternalMap.connectEvictables(referenceEntry.getPreviousEvictable(), referenceEntry2);
            MapMakerInternalMap.connectEvictables(referenceEntry2, referenceEntry.getNextEvictable());
            MapMakerInternalMap.nullifyEvictable(referenceEntry);
        }
    }

    abstract class HashIterator<E> implements Iterator<E> {
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        WriteThroughEntry lastReturned;
        ReferenceEntry<K, V> nextEntry;
        WriteThroughEntry nextExternal;
        int nextSegmentIndex;
        int nextTableIndex = -1;

        public abstract E next();

        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            advance();
        }

        final void advance() {
            this.nextExternal = null;
            if (!nextInChain() && !nextInTable()) {
                while (this.nextSegmentIndex >= 0) {
                    Segment[] segmentArr = MapMakerInternalMap.this.segments;
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
                Object key = referenceEntry.getKey();
                referenceEntry = MapMakerInternalMap.this.getLiveValue(referenceEntry);
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
            CollectPreconditions.checkRemove(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
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

    final class EntrySet extends AbstractSet<Entry<K, V>> {
        EntrySet() {
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
            key = MapMakerInternalMap.this.get(key);
            if (!(key == null || MapMakerInternalMap.this.valueEquivalence.equivalent(entry.getValue(), key) == null)) {
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
            if (!(key == null || MapMakerInternalMap.this.remove(key, entry.getValue()) == null)) {
                z = true;
            }
            return z;
        }

        public int size() {
            return MapMakerInternalMap.this.size();
        }

        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    static final class EvictionQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new C09891();

        /* renamed from: com.google.common.collect.MapMakerInternalMap$EvictionQueue$1 */
        class C09891 extends AbstractReferenceEntry<K, V> {
            ReferenceEntry<K, V> nextEvictable = this;
            ReferenceEntry<K, V> previousEvictable = this;

            C09891() {
            }

            public ReferenceEntry<K, V> getNextEvictable() {
                return this.nextEvictable;
            }

            public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
                this.nextEvictable = referenceEntry;
            }

            public ReferenceEntry<K, V> getPreviousEvictable() {
                return this.previousEvictable;
            }

            public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
                this.previousEvictable = referenceEntry;
            }
        }

        EvictionQueue() {
        }

        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            MapMakerInternalMap.connectEvictables(referenceEntry.getPreviousEvictable(), referenceEntry.getNextEvictable());
            MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), referenceEntry);
            MapMakerInternalMap.connectEvictables(referenceEntry, this.head);
            return true;
        }

        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> nextEvictable = this.head.getNextEvictable();
            return nextEvictable == this.head ? null : nextEvictable;
        }

        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> nextEvictable = this.head.getNextEvictable();
            if (nextEvictable == this.head) {
                return null;
            }
            remove(nextEvictable);
            return nextEvictable;
        }

        public boolean remove(Object obj) {
            ReferenceEntry referenceEntry = (ReferenceEntry) obj;
            ReferenceEntry previousEvictable = referenceEntry.getPreviousEvictable();
            ReferenceEntry nextEvictable = referenceEntry.getNextEvictable();
            MapMakerInternalMap.connectEvictables(previousEvictable, nextEvictable);
            MapMakerInternalMap.nullifyEvictable(referenceEntry);
            return nextEvictable != NullEntry.INSTANCE ? true : null;
        }

        public boolean contains(Object obj) {
            return ((ReferenceEntry) obj).getNextEvictable() != NullEntry.INSTANCE ? true : null;
        }

        public boolean isEmpty() {
            return this.head.getNextEvictable() == this.head;
        }

        public int size() {
            int i = 0;
            for (ReferenceEntry nextEvictable = this.head.getNextEvictable(); nextEvictable != this.head; nextEvictable = nextEvictable.getNextEvictable()) {
                i++;
            }
            return i;
        }

        public void clear() {
            ReferenceEntry nextEvictable = this.head.getNextEvictable();
            while (nextEvictable != this.head) {
                ReferenceEntry nextEvictable2 = nextEvictable.getNextEvictable();
                MapMakerInternalMap.nullifyEvictable(nextEvictable);
                nextEvictable = nextEvictable2;
            }
            this.head.setNextEvictable(this.head);
            this.head.setPreviousEvictable(this.head);
        }

        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    referenceEntry = referenceEntry.getNextEvictable();
                    return referenceEntry == EvictionQueue.this.head ? null : referenceEntry;
                }
            };
        }
    }

    static final class ExpirationQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new C09911();

        /* renamed from: com.google.common.collect.MapMakerInternalMap$ExpirationQueue$1 */
        class C09911 extends AbstractReferenceEntry<K, V> {
            ReferenceEntry<K, V> nextExpirable = this;
            ReferenceEntry<K, V> previousExpirable = this;

            public long getExpirationTime() {
                return Long.MAX_VALUE;
            }

            public void setExpirationTime(long j) {
            }

            C09911() {
            }

            public ReferenceEntry<K, V> getNextExpirable() {
                return this.nextExpirable;
            }

            public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
                this.nextExpirable = referenceEntry;
            }

            public ReferenceEntry<K, V> getPreviousExpirable() {
                return this.previousExpirable;
            }

            public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
                this.previousExpirable = referenceEntry;
            }
        }

        ExpirationQueue() {
        }

        public boolean offer(ReferenceEntry<K, V> referenceEntry) {
            MapMakerInternalMap.connectExpirables(referenceEntry.getPreviousExpirable(), referenceEntry.getNextExpirable());
            MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), referenceEntry);
            MapMakerInternalMap.connectExpirables(referenceEntry, this.head);
            return true;
        }

        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> nextExpirable = this.head.getNextExpirable();
            return nextExpirable == this.head ? null : nextExpirable;
        }

        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> nextExpirable = this.head.getNextExpirable();
            if (nextExpirable == this.head) {
                return null;
            }
            remove(nextExpirable);
            return nextExpirable;
        }

        public boolean remove(Object obj) {
            ReferenceEntry referenceEntry = (ReferenceEntry) obj;
            ReferenceEntry previousExpirable = referenceEntry.getPreviousExpirable();
            ReferenceEntry nextExpirable = referenceEntry.getNextExpirable();
            MapMakerInternalMap.connectExpirables(previousExpirable, nextExpirable);
            MapMakerInternalMap.nullifyExpirable(referenceEntry);
            return nextExpirable != NullEntry.INSTANCE ? true : null;
        }

        public boolean contains(Object obj) {
            return ((ReferenceEntry) obj).getNextExpirable() != NullEntry.INSTANCE ? true : null;
        }

        public boolean isEmpty() {
            return this.head.getNextExpirable() == this.head;
        }

        public int size() {
            int i = 0;
            for (ReferenceEntry nextExpirable = this.head.getNextExpirable(); nextExpirable != this.head; nextExpirable = nextExpirable.getNextExpirable()) {
                i++;
            }
            return i;
        }

        public void clear() {
            ReferenceEntry nextExpirable = this.head.getNextExpirable();
            while (nextExpirable != this.head) {
                ReferenceEntry nextExpirable2 = nextExpirable.getNextExpirable();
                MapMakerInternalMap.nullifyExpirable(nextExpirable);
                nextExpirable = nextExpirable2;
            }
            this.head.setNextExpirable(this.head);
            this.head.setPreviousExpirable(this.head);
        }

        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> referenceEntry) {
                    referenceEntry = referenceEntry.getNextExpirable();
                    return referenceEntry == ExpirationQueue.this.head ? null : referenceEntry;
                }
            };
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

    final class KeySet extends AbstractSet<K> {
        KeySet() {
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public int size() {
            return MapMakerInternalMap.this.size();
        }

        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        public boolean contains(Object obj) {
            return MapMakerInternalMap.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return MapMakerInternalMap.this.remove(obj) != null ? true : null;
        }

        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    private enum NullEntry implements ReferenceEntry<Object, Object> {
        INSTANCE;

        public long getExpirationTime() {
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

        public ReferenceEntry<Object, Object> getNextEvictable() {
            return this;
        }

        public ReferenceEntry<Object, Object> getNextExpirable() {
            return this;
        }

        public ReferenceEntry<Object, Object> getPreviousEvictable() {
            return this;
        }

        public ReferenceEntry<Object, Object> getPreviousExpirable() {
            return this;
        }

        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        public void setExpirationTime(long j) {
        }

        public void setNextEvictable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setNextExpirable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setPreviousEvictable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setPreviousExpirable(ReferenceEntry<Object, Object> referenceEntry) {
        }

        public void setValueReference(ValueReference<Object, Object> valueReference) {
        }
    }

    private static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V> {
        private static final long serialVersionUID = 3;

        SerializationProxy(Strength strength, Strength strength2, Equivalence<Object> equivalence, Equivalence<Object> equivalence2, long j, long j2, int i, int i2, RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> concurrentMap) {
            super(strength, strength2, equivalence, equivalence2, j, j2, i, i2, removalListener, concurrentMap);
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            writeMapTo(objectOutputStream);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.delegate = readMapMaker(objectInputStream).makeMap();
            readEntries(objectInputStream);
        }

        private Object readResolve() {
            return this.delegate;
        }
    }

    static class SoftEntry<K, V> extends SoftReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        SoftEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, referenceQueue);
            this.hash = i;
            this.next = referenceEntry;
        }

        public K getKey() {
            return get();
        }

        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        public void setExpirationTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference valueReference2 = this.valueReference;
            this.valueReference = valueReference;
            valueReference2.clear(valueReference);
        }

        public int getHash() {
            return this.hash;
        }

        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class SoftEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        SoftEvictableEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class SoftExpirableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        volatile long time = -1;

        SoftExpirableEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public long getExpirationTime() {
            return this.time;
        }

        public void setExpirationTime(long j) {
            this.time = j;
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }
    }

    static final class SoftExpirableEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        volatile long time = -1;

        SoftExpirableEvictableEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public long getExpirationTime() {
            return this.time;
        }

        public void setExpirationTime(long j) {
            this.time = j;
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        public boolean isComputingReference() {
            return false;
        }

        SoftValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, referenceQueue);
            this.entry = referenceEntry;
        }

        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        public void clear(ValueReference<K, V> valueReference) {
            clear();
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
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v) {
                return new StrongValueReference(v);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        },
        SOFT {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v) {
                return new SoftValueReference(segment.valueReferenceQueue, v, referenceEntry);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        },
        WEAK {
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v) {
                return new WeakValueReference(segment.valueReferenceQueue, v, referenceEntry);
            }

            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };

        abstract Equivalence<Object> defaultEquivalence();

        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> referenceEntry, V v);
    }

    static class StrongEntry<K, V> implements ReferenceEntry<K, V> {
        final int hash;
        final K key;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        StrongEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            this.key = k;
            this.hash = i;
            this.next = referenceEntry;
        }

        public K getKey() {
            return this.key;
        }

        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        public void setExpirationTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference valueReference2 = this.valueReference;
            this.valueReference = valueReference;
            valueReference2.clear(valueReference);
        }

        public int getHash() {
            return this.hash;
        }

        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class StrongEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        StrongEvictableEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class StrongExpirableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        volatile long time = -1;

        StrongExpirableEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
        }

        public long getExpirationTime() {
            return this.time;
        }

        public void setExpirationTime(long j) {
            this.time = j;
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }
    }

    static final class StrongExpirableEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        volatile long time = -1;

        StrongExpirableEvictableEntry(K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, i, referenceEntry);
        }

        public long getExpirationTime() {
            return this.time;
        }

        public void setExpirationTime(long j) {
            this.time = j;
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        public void clear(ValueReference<K, V> valueReference) {
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        public boolean isComputingReference() {
            return false;
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

    final class ValueIterator extends HashIterator<V> {
        ValueIterator() {
            super();
        }

        public V next() {
            return nextEntry().getValue();
        }
    }

    final class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public int size() {
            return MapMakerInternalMap.this.size();
        }

        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        public boolean contains(Object obj) {
            return MapMakerInternalMap.this.containsValue(obj);
        }

        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        WeakEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(k, referenceQueue);
            this.hash = i;
            this.next = referenceEntry;
        }

        public K getKey() {
            return get();
        }

        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        public void setExpirationTime(long j) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            throw new UnsupportedOperationException();
        }

        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference valueReference2 = this.valueReference;
            this.valueReference = valueReference;
            valueReference2.clear(valueReference);
        }

        public int getHash() {
            return this.hash;
        }

        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class WeakEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        WeakEvictableEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class WeakExpirableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        volatile long time = -1;

        WeakExpirableEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public long getExpirationTime() {
            return this.time;
        }

        public void setExpirationTime(long j) {
            this.time = j;
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }
    }

    static final class WeakExpirableEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> {
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        volatile long time = -1;

        WeakExpirableEvictableEntry(ReferenceQueue<K> referenceQueue, K k, int i, @Nullable ReferenceEntry<K, V> referenceEntry) {
            super(referenceQueue, k, i, referenceEntry);
        }

        public long getExpirationTime() {
            return this.time;
        }

        public void setExpirationTime(long j) {
            this.time = j;
        }

        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        public void setNextExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.nextExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        public void setPreviousExpirable(ReferenceEntry<K, V> referenceEntry) {
            this.previousExpirable = referenceEntry;
        }

        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        public void setNextEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.nextEvictable = referenceEntry;
        }

        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        public void setPreviousEvictable(ReferenceEntry<K, V> referenceEntry) {
            this.previousEvictable = referenceEntry;
        }
    }

    static final class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        public boolean isComputingReference() {
            return false;
        }

        WeakValueReference(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, referenceQueue);
            this.entry = referenceEntry;
        }

        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        public void clear(ValueReference<K, V> valueReference) {
            clear();
        }

        public ValueReference<K, V> copyFor(ReferenceQueue<V> referenceQueue, V v, ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference(referenceQueue, v, referenceEntry);
        }

        public V waitForValue() {
            return get();
        }
    }

    final class WriteThroughEntry extends AbstractMapEntry<K, V> {
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
            V put = MapMakerInternalMap.this.put(this.key, v);
            this.value = v;
            return put;
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

    MapMakerInternalMap(MapMaker mapMaker) {
        this.concurrencyLevel = Math.min(mapMaker.getConcurrencyLevel(), 65536);
        this.keyStrength = mapMaker.getKeyStrength();
        this.valueStrength = mapMaker.getValueStrength();
        this.keyEquivalence = mapMaker.getKeyEquivalence();
        this.maximumSize = mapMaker.maximumSize;
        this.expireAfterAccessNanos = mapMaker.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = mapMaker.getExpireAfterWriteNanos();
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, expires(), evictsBySize());
        this.ticker = mapMaker.getTicker();
        this.removalListener = mapMaker.getRemovalListener();
        this.removalNotificationQueue = this.removalListener == NullListener.INSTANCE ? discardingQueue() : new ConcurrentLinkedQueue();
        mapMaker = Math.min(mapMaker.getInitialCapacity(), 1073741824);
        if (evictsBySize()) {
            mapMaker = Math.min(mapMaker, this.maximumSize);
        }
        int i = 0;
        int i2 = 1;
        int i3 = 0;
        while (i2 < this.concurrencyLevel && (!evictsBySize() || i2 * 2 <= this.maximumSize)) {
            i3++;
            i2 <<= 1;
        }
        this.segmentShift = 32 - i3;
        this.segmentMask = i2 - 1;
        this.segments = newSegmentArray(i2);
        i3 = mapMaker / i2;
        if (i3 * i2 < mapMaker) {
            i3++;
        }
        mapMaker = true;
        while (mapMaker < i3) {
            mapMaker <<= 1;
        }
        if (evictsBySize()) {
            i3 = (this.maximumSize / i2) + 1;
            int i4 = this.maximumSize % i2;
            while (i < this.segments.length) {
                if (i == i4) {
                    i3--;
                }
                this.segments[i] = createSegment(mapMaker, i3);
                i++;
            }
            return;
        }
        while (i < this.segments.length) {
            this.segments[i] = createSegment(mapMaker, -1);
            i++;
        }
    }

    boolean evictsBySize() {
        return this.maximumSize != -1;
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
        return segmentFor(i).newEntry(k, i, referenceEntry);
    }

    @VisibleForTesting
    ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        return segmentFor(referenceEntry.getHash()).copyEntry(referenceEntry, referenceEntry2);
    }

    @VisibleForTesting
    ValueReference<K, V> newValueReference(ReferenceEntry<K, V> referenceEntry, V v) {
        return this.valueStrength.referenceValue(segmentFor(referenceEntry.getHash()), referenceEntry, v);
    }

    int hash(Object obj) {
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
    boolean isLive(ReferenceEntry<K, V> referenceEntry) {
        return segmentFor(referenceEntry.getHash()).getLiveValue(referenceEntry) != null ? true : null;
    }

    Segment<K, V> segmentFor(int i) {
        return this.segments[(i >>> this.segmentShift) & this.segmentMask];
    }

    Segment<K, V> createSegment(int i, int i2) {
        return new Segment(this, i, i2);
    }

    V getLiveValue(ReferenceEntry<K, V> referenceEntry) {
        if (referenceEntry.getKey() == null) {
            return null;
        }
        V v = referenceEntry.getValueReference().get();
        if (v == null) {
            return null;
        }
        if (!expires() || isExpired(referenceEntry) == null) {
            return v;
        }
        return null;
    }

    boolean isExpired(ReferenceEntry<K, V> referenceEntry) {
        return isExpired(referenceEntry, this.ticker.read());
    }

    boolean isExpired(ReferenceEntry<K, V> referenceEntry, long j) {
        return j - referenceEntry.getExpirationTime() > null ? true : null;
    }

    static <K, V> void connectExpirables(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextExpirable(referenceEntry2);
        referenceEntry2.setPreviousExpirable(referenceEntry);
    }

    static <K, V> void nullifyExpirable(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry nullEntry = nullEntry();
        referenceEntry.setNextExpirable(nullEntry);
        referenceEntry.setPreviousExpirable(nullEntry);
    }

    void processPendingNotifications() {
        while (true) {
            RemovalNotification removalNotification = (RemovalNotification) this.removalNotificationQueue.poll();
            if (removalNotification != null) {
                try {
                    this.removalListener.onRemoval(removalNotification);
                } catch (Throwable e) {
                    logger.log(Level.WARNING, "Exception thrown by removal listener", e);
                }
            } else {
                return;
            }
        }
    }

    static <K, V> void connectEvictables(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
        referenceEntry.setNextEvictable(referenceEntry2);
        referenceEntry2.setPreviousEvictable(referenceEntry);
    }

    static <K, V> void nullifyEvictable(ReferenceEntry<K, V> referenceEntry) {
        ReferenceEntry nullEntry = nullEntry();
        referenceEntry.setNextEvictable(nullEntry);
        referenceEntry.setPreviousEvictable(nullEntry);
    }

    final Segment<K, V>[] newSegmentArray(int i) {
        return new Segment[i];
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

    public int size() {
        Segment[] segmentArr = this.segments;
        long j = 0;
        int i = 0;
        while (i < segmentArr.length) {
            i++;
            j += (long) segmentArr[i].count;
        }
        return Ints.saturatedCast(j);
    }

    public V get(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        return segmentFor(hash).get(obj, hash);
    }

    ReferenceEntry<K, V> getEntry(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        return segmentFor(hash).getEntry(obj, hash);
    }

    public boolean containsKey(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        int hash = hash(obj);
        return segmentFor(hash).containsKey(obj, hash);
    }

    public boolean containsValue(@Nullable Object obj) {
        MapMakerInternalMap mapMakerInternalMap = this;
        Object obj2 = obj;
        if (obj2 == null) {
            return false;
        }
        Segment[] segmentArr = mapMakerInternalMap.segments;
        long j = -1;
        int i = 0;
        while (i < 3) {
            int length = segmentArr.length;
            long j2 = 0;
            int i2 = 0;
            while (i2 < length) {
                Segment segment = segmentArr[i2];
                int i3 = segment.count;
                AtomicReferenceArray atomicReferenceArray = segment.table;
                for (int i4 = 0; i4 < atomicReferenceArray.length(); i4++) {
                    for (ReferenceEntry referenceEntry = (ReferenceEntry) atomicReferenceArray.get(i4); referenceEntry != null; referenceEntry = referenceEntry.getNext()) {
                        Object liveValue = segment.getLiveValue(referenceEntry);
                        if (liveValue != null && mapMakerInternalMap.valueEquivalence.equivalent(obj2, liveValue)) {
                            return true;
                        }
                    }
                }
                i2++;
                j2 += (long) segment.modCount;
            }
            if (j2 == j) {
                break;
            }
            i++;
            j = j2;
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

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        set = new KeySet();
        this.keySet = set;
        return set;
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = new Values();
        this.values = collection;
        return collection;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        set = new EntrySet();
        this.entrySet = set;
        return set;
    }

    Object writeReplace() {
        return new SerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this);
    }
}
