package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private final transient ConcurrentMap<E, AtomicInteger> countMap;

    /* renamed from: com.google.common.collect.ConcurrentHashMultiset$2 */
    class C08912 extends AbstractIterator<Entry<E>> {
        private Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();

        C08912() {
        }

        protected Entry<E> computeNext() {
            while (this.mapEntries.hasNext()) {
                Map.Entry entry = (Map.Entry) this.mapEntries.next();
                int i = ((AtomicInteger) entry.getValue()).get();
                if (i != 0) {
                    return Multisets.immutableEntry(entry.getKey(), i);
                }
            }
            return (Entry) endOfData();
        }
    }

    private class EntrySet extends EntrySet {
        private EntrySet() {
            super();
        }

        ConcurrentHashMultiset<E> multiset() {
            return ConcurrentHashMultiset.this;
        }

        public Object[] toArray() {
            return snapshot().toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return snapshot().toArray(tArr);
        }

        private List<Entry<E>> snapshot() {
            Object newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
            Iterators.addAll(newArrayListWithExpectedSize, iterator());
            return newArrayListWithExpectedSize;
        }
    }

    private static class FieldSettersHolder {
        static final FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");

        private FieldSettersHolder() {
        }
    }

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        return super.add(obj);
    }

    public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
        return super.addAll(collection);
    }

    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <E> ConcurrentHashMultiset<E> create() {
        return new ConcurrentHashMultiset(new ConcurrentHashMap());
    }

    public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> iterable) {
        Object create = create();
        Iterables.addAll(create, iterable);
        return create;
    }

    @Beta
    public static <E> ConcurrentHashMultiset<E> create(MapMaker mapMaker) {
        return new ConcurrentHashMultiset(mapMaker.makeMap());
    }

    @VisibleForTesting
    ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> concurrentMap) {
        Preconditions.checkArgument(concurrentMap.isEmpty());
        this.countMap = concurrentMap;
    }

    public int count(@Nullable Object obj) {
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, obj);
        if (atomicInteger == null) {
            return null;
        }
        return atomicInteger.get();
    }

    public int size() {
        long j = 0;
        for (AtomicInteger atomicInteger : this.countMap.values()) {
            j += (long) atomicInteger.get();
        }
        return Ints.saturatedCast(j);
    }

    public Object[] toArray() {
        return snapshot().toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return snapshot().toArray(tArr);
    }

    private List<E> snapshot() {
        List<E> newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
        for (Entry entry : entrySet()) {
            Object element = entry.getElement();
            for (int count = entry.getCount(); count > 0; count--) {
                newArrayListWithExpectedSize.add(element);
            }
        }
        return newArrayListWithExpectedSize;
    }

    public int add(E r6, int r7) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        com.google.common.base.Preconditions.checkNotNull(r6);
        if (r7 != 0) goto L_0x000a;
    L_0x0005:
        r6 = r5.count(r6);
        return r6;
    L_0x000a:
        r0 = 1;
        r1 = 0;
        if (r7 <= 0) goto L_0x0010;
    L_0x000e:
        r2 = 1;
        goto L_0x0011;
    L_0x0010:
        r2 = 0;
    L_0x0011:
        r3 = "Invalid occurrences: %s";
        r0 = new java.lang.Object[r0];
        r4 = java.lang.Integer.valueOf(r7);
        r0[r1] = r4;
        com.google.common.base.Preconditions.checkArgument(r2, r3, r0);
    L_0x001e:
        r0 = r5.countMap;
        r0 = com.google.common.collect.Maps.safeGet(r0, r6);
        r0 = (java.util.concurrent.atomic.AtomicInteger) r0;
        if (r0 != 0) goto L_0x0038;
    L_0x0028:
        r0 = r5.countMap;
        r2 = new java.util.concurrent.atomic.AtomicInteger;
        r2.<init>(r7);
        r0 = r0.putIfAbsent(r6, r2);
        r0 = (java.util.concurrent.atomic.AtomicInteger) r0;
        if (r0 != 0) goto L_0x0038;
    L_0x0037:
        return r1;
    L_0x0038:
        r2 = r0.get();
        if (r2 == 0) goto L_0x0068;
    L_0x003e:
        r3 = com.google.common.math.IntMath.checkedAdd(r2, r7);	 Catch:{ ArithmeticException -> 0x0049 }
        r3 = r0.compareAndSet(r2, r3);	 Catch:{ ArithmeticException -> 0x0049 }
        if (r3 == 0) goto L_0x0038;
    L_0x0048:
        return r2;
    L_0x0049:
        r6 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Overflow adding ";
        r0.append(r1);
        r0.append(r7);
        r7 = " occurrences to a count of ";
        r0.append(r7);
        r0.append(r2);
        r7 = r0.toString();
        r6.<init>(r7);
        throw r6;
    L_0x0068:
        r2 = new java.util.concurrent.atomic.AtomicInteger;
        r2.<init>(r7);
        r3 = r5.countMap;
        r3 = r3.putIfAbsent(r6, r2);
        if (r3 == 0) goto L_0x007d;
    L_0x0075:
        r3 = r5.countMap;
        r0 = r3.replace(r6, r0, r2);
        if (r0 == 0) goto L_0x001e;
    L_0x007d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.add(java.lang.Object, int):int");
    }

    public int remove(@Nullable Object obj, int i) {
        if (i == 0) {
            return count(obj);
        }
        Preconditions.checkArgument(i > 0, "Invalid occurrences: %s", Integer.valueOf(i));
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, obj);
        if (atomicInteger == null) {
            return 0;
        }
        int i2;
        int max;
        do {
            i2 = atomicInteger.get();
            if (i2 == 0) {
                return 0;
            }
            max = Math.max(0, i2 - i);
        } while (!atomicInteger.compareAndSet(i2, max));
        if (max == 0) {
            this.countMap.remove(obj, atomicInteger);
        }
        return i2;
    }

    public boolean removeExactly(@Nullable Object obj, int i) {
        if (i == 0) {
            return true;
        }
        Preconditions.checkArgument(i > 0, "Invalid occurrences: %s", Integer.valueOf(i));
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, obj);
        if (atomicInteger == null) {
            return false;
        }
        int i2;
        int i3;
        do {
            i2 = atomicInteger.get();
            if (i2 < i) {
                return false;
            }
            i3 = i2 - i;
        } while (!atomicInteger.compareAndSet(i2, i3));
        if (i3 == 0) {
            this.countMap.remove(obj, atomicInteger);
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int setCount(E r5, int r6) {
        /*
        r4 = this;
        com.google.common.base.Preconditions.checkNotNull(r5);
        r0 = "count";
        com.google.common.collect.CollectPreconditions.checkNonnegative(r6, r0);
    L_0x0008:
        r0 = r4.countMap;
        r0 = com.google.common.collect.Maps.safeGet(r0, r5);
        r0 = (java.util.concurrent.atomic.AtomicInteger) r0;
        r1 = 0;
        if (r0 != 0) goto L_0x0026;
    L_0x0013:
        if (r6 != 0) goto L_0x0016;
    L_0x0015:
        return r1;
    L_0x0016:
        r0 = r4.countMap;
        r2 = new java.util.concurrent.atomic.AtomicInteger;
        r2.<init>(r6);
        r0 = r0.putIfAbsent(r5, r2);
        r0 = (java.util.concurrent.atomic.AtomicInteger) r0;
        if (r0 != 0) goto L_0x0026;
    L_0x0025:
        return r1;
    L_0x0026:
        r2 = r0.get();
        if (r2 != 0) goto L_0x0045;
    L_0x002c:
        if (r6 != 0) goto L_0x002f;
    L_0x002e:
        return r1;
    L_0x002f:
        r2 = new java.util.concurrent.atomic.AtomicInteger;
        r2.<init>(r6);
        r3 = r4.countMap;
        r3 = r3.putIfAbsent(r5, r2);
        if (r3 == 0) goto L_0x0044;
    L_0x003c:
        r3 = r4.countMap;
        r0 = r3.replace(r5, r0, r2);
        if (r0 == 0) goto L_0x0008;
    L_0x0044:
        return r1;
    L_0x0045:
        r3 = r0.compareAndSet(r2, r6);
        if (r3 == 0) goto L_0x0026;
    L_0x004b:
        if (r6 != 0) goto L_0x0052;
    L_0x004d:
        r6 = r4.countMap;
        r6.remove(r5, r0);
    L_0x0052:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.setCount(java.lang.Object, int):int");
    }

    public boolean setCount(E e, int i, int i2) {
        Preconditions.checkNotNull(e);
        CollectPreconditions.checkNonnegative(i, "oldCount");
        CollectPreconditions.checkNonnegative(i2, "newCount");
        AtomicInteger atomicInteger = (AtomicInteger) Maps.safeGet(this.countMap, e);
        boolean z = false;
        if (atomicInteger != null) {
            int i3 = atomicInteger.get();
            if (i3 == i) {
                if (i3 == 0) {
                    if (i2 == 0) {
                        this.countMap.remove(e, atomicInteger);
                        return true;
                    }
                    i = new AtomicInteger(i2);
                    if (this.countMap.putIfAbsent(e, i) == 0 || this.countMap.replace(e, atomicInteger, i) != null) {
                        z = true;
                    }
                    return z;
                } else if (atomicInteger.compareAndSet(i3, i2) != 0) {
                    if (i2 == 0) {
                        this.countMap.remove(e, atomicInteger);
                    }
                    return true;
                }
            }
            return false;
        } else if (i != 0) {
            return false;
        } else {
            if (i2 == 0) {
                return true;
            }
            if (this.countMap.putIfAbsent(e, new AtomicInteger(i2)) == null) {
                z = true;
            }
            return z;
        }
    }

    Set<E> createElementSet() {
        final Set keySet = this.countMap.keySet();
        return new ForwardingSet<E>() {
            protected Set<E> delegate() {
                return keySet;
            }

            public boolean contains(@Nullable Object obj) {
                return (obj == null || Collections2.safeContains(keySet, obj) == null) ? null : true;
            }

            public boolean containsAll(Collection<?> collection) {
                return standardContainsAll(collection);
            }

            public boolean remove(Object obj) {
                return (obj == null || Collections2.safeRemove(keySet, obj) == null) ? null : true;
            }

            public boolean removeAll(Collection<?> collection) {
                return standardRemoveAll(collection);
            }
        };
    }

    public Set<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    int distinctElements() {
        return this.countMap.size();
    }

    public boolean isEmpty() {
        return this.countMap.isEmpty();
    }

    Iterator<Entry<E>> entryIterator() {
        final Iterator c08912 = new C08912();
        return new ForwardingIterator<Entry<E>>() {
            private Entry<E> last;

            protected Iterator<Entry<E>> delegate() {
                return c08912;
            }

            public Entry<E> next() {
                this.last = (Entry) super.next();
                return this.last;
            }

            public void remove() {
                CollectPreconditions.checkRemove(this.last != null);
                ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
                this.last = null;
            }
        };
    }

    public void clear() {
        this.countMap.clear();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.countMap);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set((Object) this, (ConcurrentMap) objectInputStream.readObject());
    }
}
