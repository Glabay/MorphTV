package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtIncompatible("NavigableMap")
@Beta
public final class TreeRangeMap<K extends Comparable, V> implements RangeMap<K, V> {
    private static final RangeMap EMPTY_SUB_RANGE_MAP = new C10971();
    private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound = Maps.newTreeMap();

    /* renamed from: com.google.common.collect.TreeRangeMap$1 */
    static class C10971 implements RangeMap {
        public void clear() {
        }

        @Nullable
        public Object get(Comparable comparable) {
            return null;
        }

        @Nullable
        public Entry<Range, Object> getEntry(Comparable comparable) {
            return null;
        }

        C10971() {
        }

        public Range span() {
            throw new NoSuchElementException();
        }

        public void put(Range range, Object obj) {
            Preconditions.checkNotNull(range);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot insert range ");
            stringBuilder.append(range);
            stringBuilder.append(" into an empty subRangeMap");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public void putAll(RangeMap rangeMap) {
            if (rangeMap.asMapOfRanges().isEmpty() == null) {
                throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
            }
        }

        public void remove(Range range) {
            Preconditions.checkNotNull(range);
        }

        public Map<Range, Object> asMapOfRanges() {
            return Collections.emptyMap();
        }

        public RangeMap subRangeMap(Range range) {
            Preconditions.checkNotNull(range);
            return this;
        }
    }

    private final class AsMapOfRanges extends AbstractMap<Range<K>, V> {

        /* renamed from: com.google.common.collect.TreeRangeMap$AsMapOfRanges$1 */
        class C10981 extends AbstractSet<Entry<Range<K>, V>> {
            C10981() {
            }

            public Iterator<Entry<Range<K>, V>> iterator() {
                return TreeRangeMap.this.entriesByLowerBound.values().iterator();
            }

            public int size() {
                return TreeRangeMap.this.entriesByLowerBound.size();
            }
        }

        private AsMapOfRanges() {
        }

        public boolean containsKey(@Nullable Object obj) {
            return get(obj) != null ? true : null;
        }

        public V get(@Nullable Object obj) {
            if (obj instanceof Range) {
                Range range = (Range) obj;
                RangeMapEntry rangeMapEntry = (RangeMapEntry) TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
                if (!(rangeMapEntry == null || rangeMapEntry.getKey().equals(range) == null)) {
                    return rangeMapEntry.getValue();
                }
            }
            return null;
        }

        public Set<Entry<Range<K>, V>> entrySet() {
            return new C10981();
        }
    }

    private static final class RangeMapEntry<K extends Comparable, V> extends AbstractMapEntry<Range<K>, V> {
        private final Range<K> range;
        private final V value;

        RangeMapEntry(Cut<K> cut, Cut<K> cut2, V v) {
            this(Range.create(cut, cut2), v);
        }

        RangeMapEntry(Range<K> range, V v) {
            this.range = range;
            this.value = v;
        }

        public Range<K> getKey() {
            return this.range;
        }

        public V getValue() {
            return this.value;
        }

        public boolean contains(K k) {
            return this.range.contains(k);
        }

        Cut<K> getLowerBound() {
            return this.range.lowerBound;
        }

        Cut<K> getUpperBound() {
            return this.range.upperBound;
        }
    }

    private class SubRangeMap implements RangeMap<K, V> {
        private final Range<K> subRange;

        class SubRangeMapAsMap extends AbstractMap<Range<K>, V> {

            /* renamed from: com.google.common.collect.TreeRangeMap$SubRangeMap$SubRangeMapAsMap$2 */
            class C11012 extends EntrySet<Range<K>, V> {
                C11012() {
                }

                Map<Range<K>, V> map() {
                    return SubRangeMapAsMap.this;
                }

                public Iterator<Entry<Range<K>, V>> iterator() {
                    if (SubRangeMap.this.subRange.isEmpty()) {
                        return Iterators.emptyIterator();
                    }
                    final Iterator it = TreeRangeMap.this.entriesByLowerBound.tailMap((Cut) MoreObjects.firstNonNull(TreeRangeMap.this.entriesByLowerBound.floorKey(SubRangeMap.this.subRange.lowerBound), SubRangeMap.this.subRange.lowerBound), true).values().iterator();
                    return new AbstractIterator<Entry<Range<K>, V>>() {
                        protected Entry<Range<K>, V> computeNext() {
                            while (it.hasNext()) {
                                RangeMapEntry rangeMapEntry = (RangeMapEntry) it.next();
                                if (rangeMapEntry.getLowerBound().compareTo(SubRangeMap.this.subRange.upperBound) >= 0) {
                                    break;
                                } else if (rangeMapEntry.getUpperBound().compareTo(SubRangeMap.this.subRange.lowerBound) > 0) {
                                    return Maps.immutableEntry(rangeMapEntry.getKey().intersection(SubRangeMap.this.subRange), rangeMapEntry.getValue());
                                }
                            }
                            return (Entry) endOfData();
                        }
                    };
                }

                public boolean retainAll(Collection<?> collection) {
                    return SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(collection)));
                }

                public int size() {
                    return Iterators.size(iterator());
                }

                public boolean isEmpty() {
                    return iterator().hasNext() ^ 1;
                }
            }

            SubRangeMapAsMap() {
            }

            public boolean containsKey(Object obj) {
                return get(obj) != null ? true : null;
            }

            public V get(java.lang.Object r5) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                /*
                r4 = this;
                r0 = 0;
                r1 = r5 instanceof com.google.common.collect.Range;	 Catch:{ ClassCastException -> 0x0080 }
                if (r1 == 0) goto L_0x007f;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0005:
                r5 = (com.google.common.collect.Range) r5;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = com.google.common.collect.TreeRangeMap.SubRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = r1.subRange;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = r1.encloses(r5);	 Catch:{ ClassCastException -> 0x0080 }
                if (r1 == 0) goto L_0x007e;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0013:
                r1 = r5.isEmpty();	 Catch:{ ClassCastException -> 0x0080 }
                if (r1 == 0) goto L_0x001a;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0019:
                goto L_0x007e;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x001a:
                r1 = r5.lowerBound;	 Catch:{ ClassCastException -> 0x0080 }
                r2 = com.google.common.collect.TreeRangeMap.SubRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r2 = r2.subRange;	 Catch:{ ClassCastException -> 0x0080 }
                r2 = r2.lowerBound;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = r1.compareTo(r2);	 Catch:{ ClassCastException -> 0x0080 }
                if (r1 != 0) goto L_0x0043;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x002a:
                r1 = com.google.common.collect.TreeRangeMap.SubRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = com.google.common.collect.TreeRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = r1.entriesByLowerBound;	 Catch:{ ClassCastException -> 0x0080 }
                r2 = r5.lowerBound;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = r1.floorEntry(r2);	 Catch:{ ClassCastException -> 0x0080 }
                if (r1 == 0) goto L_0x0041;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x003a:
                r1 = r1.getValue();	 Catch:{ ClassCastException -> 0x0080 }
                r1 = (com.google.common.collect.TreeRangeMap.RangeMapEntry) r1;	 Catch:{ ClassCastException -> 0x0080 }
                goto L_0x0053;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0041:
                r1 = r0;	 Catch:{ ClassCastException -> 0x0080 }
                goto L_0x0053;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0043:
                r1 = com.google.common.collect.TreeRangeMap.SubRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = com.google.common.collect.TreeRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = r1.entriesByLowerBound;	 Catch:{ ClassCastException -> 0x0080 }
                r2 = r5.lowerBound;	 Catch:{ ClassCastException -> 0x0080 }
                r1 = r1.get(r2);	 Catch:{ ClassCastException -> 0x0080 }
                r1 = (com.google.common.collect.TreeRangeMap.RangeMapEntry) r1;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0053:
                if (r1 == 0) goto L_0x007f;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0055:
                r2 = r1.getKey();	 Catch:{ ClassCastException -> 0x0080 }
                r3 = com.google.common.collect.TreeRangeMap.SubRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r3 = r3.subRange;	 Catch:{ ClassCastException -> 0x0080 }
                r2 = r2.isConnected(r3);	 Catch:{ ClassCastException -> 0x0080 }
                if (r2 == 0) goto L_0x007f;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0065:
                r2 = r1.getKey();	 Catch:{ ClassCastException -> 0x0080 }
                r3 = com.google.common.collect.TreeRangeMap.SubRangeMap.this;	 Catch:{ ClassCastException -> 0x0080 }
                r3 = r3.subRange;	 Catch:{ ClassCastException -> 0x0080 }
                r2 = r2.intersection(r3);	 Catch:{ ClassCastException -> 0x0080 }
                r5 = r2.equals(r5);	 Catch:{ ClassCastException -> 0x0080 }
                if (r5 == 0) goto L_0x007f;	 Catch:{ ClassCastException -> 0x0080 }
            L_0x0079:
                r5 = r1.getValue();	 Catch:{ ClassCastException -> 0x0080 }
                return r5;
            L_0x007e:
                return r0;
            L_0x007f:
                return r0;
            L_0x0080:
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeRangeMap.SubRangeMap.SubRangeMapAsMap.get(java.lang.Object):V");
            }

            public V remove(Object obj) {
                V v = get(obj);
                if (v == null) {
                    return null;
                }
                TreeRangeMap.this.remove((Range) obj);
                return v;
            }

            public void clear() {
                SubRangeMap.this.clear();
            }

            private boolean removeEntryIf(Predicate<? super Entry<Range<K>, V>> predicate) {
                List<Range> newArrayList = Lists.newArrayList();
                for (Entry entry : entrySet()) {
                    if (predicate.apply(entry)) {
                        newArrayList.add(entry.getKey());
                    }
                }
                for (Range remove : newArrayList) {
                    TreeRangeMap.this.remove(remove);
                }
                return newArrayList.isEmpty() ^ 1;
            }

            public Set<Range<K>> keySet() {
                return new KeySet<Range<K>, V>(this) {
                    public boolean remove(@Nullable Object obj) {
                        return SubRangeMapAsMap.this.remove(obj) != null ? true : null;
                    }

                    public boolean retainAll(Collection<?> collection) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(collection)), Maps.keyFunction()));
                    }
                };
            }

            public Set<Entry<Range<K>, V>> entrySet() {
                return new C11012();
            }

            public Collection<V> values() {
                return new Values<Range<K>, V>(this) {
                    public boolean removeAll(Collection<?> collection) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(collection), Maps.valueFunction()));
                    }

                    public boolean retainAll(Collection<?> collection) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(collection)), Maps.valueFunction()));
                    }
                };
            }
        }

        SubRangeMap(Range<K> range) {
            this.subRange = range;
        }

        @Nullable
        public V get(K k) {
            return this.subRange.contains(k) ? TreeRangeMap.this.get(k) : null;
        }

        @Nullable
        public Entry<Range<K>, V> getEntry(K k) {
            if (this.subRange.contains(k)) {
                k = TreeRangeMap.this.getEntry(k);
                if (k != null) {
                    return Maps.immutableEntry(((Range) k.getKey()).intersection(this.subRange), k.getValue());
                }
            }
            return null;
        }

        public Range<K> span() {
            Entry floorEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
            Cut cut;
            if (floorEntry == null || ((RangeMapEntry) floorEntry.getValue()).getUpperBound().compareTo(this.subRange.lowerBound) <= 0) {
                cut = (Cut) TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
                if (cut != null) {
                    if (cut.compareTo(this.subRange.upperBound) >= 0) {
                    }
                }
                throw new NoSuchElementException();
            }
            cut = this.subRange.lowerBound;
            Entry lowerEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
            if (lowerEntry == null) {
                throw new NoSuchElementException();
            }
            Cut cut2;
            if (((RangeMapEntry) lowerEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
                cut2 = this.subRange.upperBound;
            } else {
                cut2 = ((RangeMapEntry) lowerEntry.getValue()).getUpperBound();
            }
            return Range.create(cut, cut2);
        }

        public void put(Range<K> range, V v) {
            Preconditions.checkArgument(this.subRange.encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
            TreeRangeMap.this.put(range, v);
        }

        public void putAll(RangeMap<K, V> rangeMap) {
            if (!rangeMap.asMapOfRanges().isEmpty()) {
                Object[] objArr = new Object[]{rangeMap.span(), this.subRange};
                Preconditions.checkArgument(this.subRange.encloses(rangeMap.span()), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", objArr);
                TreeRangeMap.this.putAll(rangeMap);
            }
        }

        public void clear() {
            TreeRangeMap.this.remove(this.subRange);
        }

        public void remove(Range<K> range) {
            if (range.isConnected(this.subRange)) {
                TreeRangeMap.this.remove(range.intersection(this.subRange));
            }
        }

        public RangeMap<K, V> subRangeMap(Range<K> range) {
            if (range.isConnected(this.subRange)) {
                return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
            }
            return TreeRangeMap.this.emptySubRangeMap();
        }

        public Map<Range<K>, V> asMapOfRanges() {
            return new SubRangeMapAsMap();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof RangeMap)) {
                return null;
            }
            return asMapOfRanges().equals(((RangeMap) obj).asMapOfRanges());
        }

        public int hashCode() {
            return asMapOfRanges().hashCode();
        }

        public String toString() {
            return asMapOfRanges().toString();
        }
    }

    public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
        return new TreeRangeMap();
    }

    private TreeRangeMap() {
    }

    @Nullable
    public V get(K k) {
        k = getEntry(k);
        if (k == null) {
            return null;
        }
        return k.getValue();
    }

    @Nullable
    public Entry<Range<K>, V> getEntry(K k) {
        Entry floorEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(k));
        return (floorEntry == null || ((RangeMapEntry) floorEntry.getValue()).contains(k) == null) ? null : (Entry) floorEntry.getValue();
    }

    public void put(Range<K> range, V v) {
        if (!range.isEmpty()) {
            Preconditions.checkNotNull(v);
            remove(range);
            this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry(range, v));
        }
    }

    public void putAll(RangeMap<K, V> rangeMap) {
        rangeMap = rangeMap.asMapOfRanges().entrySet().iterator();
        while (rangeMap.hasNext()) {
            Entry entry = (Entry) rangeMap.next();
            put((Range) entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        this.entriesByLowerBound.clear();
    }

    public Range<K> span() {
        Entry firstEntry = this.entriesByLowerBound.firstEntry();
        Entry lastEntry = this.entriesByLowerBound.lastEntry();
        if (firstEntry != null) {
            return Range.create(((RangeMapEntry) firstEntry.getValue()).getKey().lowerBound, ((RangeMapEntry) lastEntry.getValue()).getKey().upperBound);
        }
        throw new NoSuchElementException();
    }

    private void putRangeMapEntry(Cut<K> cut, Cut<K> cut2, V v) {
        this.entriesByLowerBound.put(cut, new RangeMapEntry(cut, cut2, v));
    }

    public void remove(Range<K> range) {
        if (!range.isEmpty()) {
            RangeMapEntry rangeMapEntry;
            Entry lowerEntry = this.entriesByLowerBound.lowerEntry(range.lowerBound);
            if (lowerEntry != null) {
                rangeMapEntry = (RangeMapEntry) lowerEntry.getValue();
                if (rangeMapEntry.getUpperBound().compareTo(range.lowerBound) > 0) {
                    if (rangeMapEntry.getUpperBound().compareTo(range.upperBound) > 0) {
                        putRangeMapEntry(range.upperBound, rangeMapEntry.getUpperBound(), ((RangeMapEntry) lowerEntry.getValue()).getValue());
                    }
                    putRangeMapEntry(rangeMapEntry.getLowerBound(), range.lowerBound, ((RangeMapEntry) lowerEntry.getValue()).getValue());
                }
            }
            lowerEntry = this.entriesByLowerBound.lowerEntry(range.upperBound);
            if (lowerEntry != null) {
                rangeMapEntry = (RangeMapEntry) lowerEntry.getValue();
                if (rangeMapEntry.getUpperBound().compareTo(range.upperBound) > 0) {
                    putRangeMapEntry(range.upperBound, rangeMapEntry.getUpperBound(), ((RangeMapEntry) lowerEntry.getValue()).getValue());
                    this.entriesByLowerBound.remove(range.lowerBound);
                }
            }
            this.entriesByLowerBound.subMap(range.lowerBound, range.upperBound).clear();
        }
    }

    public Map<Range<K>, V> asMapOfRanges() {
        return new AsMapOfRanges();
    }

    public RangeMap<K, V> subRangeMap(Range<K> range) {
        if (range.equals(Range.all())) {
            return this;
        }
        return new SubRangeMap(range);
    }

    private RangeMap<K, V> emptySubRangeMap() {
        return EMPTY_SUB_RANGE_MAP;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof RangeMap)) {
            return null;
        }
        return asMapOfRanges().equals(((RangeMap) obj).asMapOfRanges());
    }

    public int hashCode() {
        return asMapOfRanges().hashCode();
    }

    public String toString() {
        return this.entriesByLowerBound.values().toString();
    }
}
