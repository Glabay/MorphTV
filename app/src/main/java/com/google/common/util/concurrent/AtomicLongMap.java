package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible
public final class AtomicLongMap<K> {
    private transient Map<K, Long> asMap;
    private final ConcurrentHashMap<K, AtomicLong> map;

    /* renamed from: com.google.common.util.concurrent.AtomicLongMap$1 */
    class C12191 implements Function<AtomicLong, Long> {
        C12191() {
        }

        public Long apply(AtomicLong atomicLong) {
            return Long.valueOf(atomicLong.get());
        }
    }

    private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> concurrentHashMap) {
        this.map = (ConcurrentHashMap) Preconditions.checkNotNull(concurrentHashMap);
    }

    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap(new ConcurrentHashMap());
    }

    public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> map) {
        AtomicLongMap<K> create = create();
        create.putAll(map);
        return create;
    }

    public long get(K k) {
        AtomicLong atomicLong = (AtomicLong) this.map.get(k);
        if (atomicLong == null) {
            return 0;
        }
        return atomicLong.get();
    }

    public long incrementAndGet(K k) {
        return addAndGet(k, 1);
    }

    public long decrementAndGet(K k) {
        return addAndGet(k, -1);
    }

    public long addAndGet(K k, long j) {
        AtomicLong atomicLong;
        do {
            atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong == null) {
                atomicLong = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong == null) {
                    return j;
                }
            }
            while (true) {
                long j2 = atomicLong.get();
                if (j2 != 0) {
                    long j3 = j2 + j;
                    if (atomicLong.compareAndSet(j2, j3)) {
                        return j3;
                    }
                }
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return j;
    }

    public long getAndIncrement(K k) {
        return getAndAdd(k, 1);
    }

    public long getAndDecrement(K k) {
        return getAndAdd(k, -1);
    }

    public long getAndAdd(K k, long j) {
        AtomicLong atomicLong;
        do {
            atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong == null) {
                atomicLong = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong == null) {
                    return 0;
                }
            }
            while (true) {
                long j2 = atomicLong.get();
                if (j2 != 0) {
                    if (atomicLong.compareAndSet(j2, j2 + j)) {
                        return j2;
                    }
                }
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return 0;
    }

    public long put(K k, long j) {
        AtomicLong atomicLong;
        do {
            atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong == null) {
                atomicLong = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong == null) {
                    return 0;
                }
            }
            while (true) {
                long j2 = atomicLong.get();
                if (j2 != 0) {
                    if (atomicLong.compareAndSet(j2, j)) {
                        return j2;
                    }
                }
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return 0;
    }

    public void putAll(Map<? extends K, ? extends Long> map) {
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            put(entry.getKey(), ((Long) entry.getValue()).longValue());
        }
    }

    public long remove(K k) {
        AtomicLong atomicLong = (AtomicLong) this.map.get(k);
        if (atomicLong == null) {
            return 0;
        }
        long j;
        do {
            j = atomicLong.get();
            if (j == 0) {
                break;
            }
        } while (!atomicLong.compareAndSet(j, 0));
        this.map.remove(k, atomicLong);
        return j;
    }

    public void removeAllZeros() {
        Iterator it = this.map.entrySet().iterator();
        while (it.hasNext()) {
            AtomicLong atomicLong = (AtomicLong) ((Entry) it.next()).getValue();
            if (atomicLong != null && atomicLong.get() == 0) {
                it.remove();
            }
        }
    }

    public long sum() {
        long j = 0;
        for (AtomicLong atomicLong : this.map.values()) {
            j += atomicLong.get();
        }
        return j;
    }

    public Map<K, Long> asMap() {
        Map<K, Long> map = this.asMap;
        if (map != null) {
            return map;
        }
        map = createAsMap();
        this.asMap = map;
        return map;
    }

    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap(Maps.transformValues(this.map, new C12191()));
    }

    public boolean containsKey(Object obj) {
        return this.map.containsKey(obj);
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

    public String toString() {
        return this.map.toString();
    }

    long putIfAbsent(K k, long j) {
        AtomicLong atomicLong;
        do {
            atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong == null) {
                atomicLong = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong == null) {
                    return 0;
                }
            }
            long j2 = atomicLong.get();
            if (j2 != 0) {
                return j2;
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return 0;
    }

    boolean replace(K k, long j, long j2) {
        boolean z = false;
        if (j == 0) {
            if (putIfAbsent(k, j2) == null) {
                z = true;
            }
            return z;
        }
        AtomicLong atomicLong = (AtomicLong) this.map.get(k);
        if (atomicLong != null) {
            z = atomicLong.compareAndSet(j, j2);
        }
        return z;
    }

    boolean remove(K k, long j) {
        AtomicLong atomicLong = (AtomicLong) this.map.get(k);
        if (atomicLong == null) {
            return false;
        }
        long j2 = atomicLong.get();
        if (j2 != j) {
            return false;
        }
        if (j2 != 0) {
            if (atomicLong.compareAndSet(j2, 0) == null) {
                return false;
            }
        }
        this.map.remove(k, atomicLong);
        return true;
    }
}
