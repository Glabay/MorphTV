package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.SortedLists.KeyAbsentBehavior;
import com.google.common.collect.SortedLists.KeyPresentBehavior;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtIncompatible("NavigableMap")
@Beta
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V> {
    private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(ImmutableList.of(), ImmutableList.of());
    private final ImmutableList<Range<K>> ranges;
    private final ImmutableList<V> values;

    public static final class Builder<K extends Comparable<?>, V> {
        private final RangeSet<K> keyRanges = TreeRangeSet.create();
        private final RangeMap<K, V> rangeMap = TreeRangeMap.create();

        public Builder<K, V> put(Range<K> range, V v) {
            Preconditions.checkNotNull(range);
            Preconditions.checkNotNull(v);
            Preconditions.checkArgument(range.isEmpty() ^ true, "Range must not be empty, but was %s", range);
            if (!this.keyRanges.complement().encloses(range)) {
                for (Entry entry : this.rangeMap.asMapOfRanges().entrySet()) {
                    Range range2 = (Range) entry.getKey();
                    if (range2.isConnected(range) && !range2.intersection(range).isEmpty()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Overlapping ranges: range ");
                        stringBuilder.append(range);
                        stringBuilder.append(" overlaps with entry ");
                        stringBuilder.append(entry);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
            }
            this.keyRanges.add(range);
            this.rangeMap.put(range, v);
            return this;
        }

        public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
            rangeMap = rangeMap.asMapOfRanges().entrySet().iterator();
            while (rangeMap.hasNext()) {
                Entry entry = (Entry) rangeMap.next();
                put((Range) entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableRangeMap<K, V> build() {
            Map asMapOfRanges = this.rangeMap.asMapOfRanges();
            com.google.common.collect.ImmutableList.Builder builder = new com.google.common.collect.ImmutableList.Builder(asMapOfRanges.size());
            com.google.common.collect.ImmutableList.Builder builder2 = new com.google.common.collect.ImmutableList.Builder(asMapOfRanges.size());
            for (Entry entry : asMapOfRanges.entrySet()) {
                builder.add(entry.getKey());
                builder2.add(entry.getValue());
            }
            return new ImmutableRangeMap(builder.build(), builder2.build());
        }
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
        return EMPTY;
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V v) {
        return new ImmutableRangeMap(ImmutableList.of(range), ImmutableList.of(v));
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
        if (rangeMap instanceof ImmutableRangeMap) {
            return (ImmutableRangeMap) rangeMap;
        }
        rangeMap = rangeMap.asMapOfRanges();
        com.google.common.collect.ImmutableList.Builder builder = new com.google.common.collect.ImmutableList.Builder(rangeMap.size());
        com.google.common.collect.ImmutableList.Builder builder2 = new com.google.common.collect.ImmutableList.Builder(rangeMap.size());
        rangeMap = rangeMap.entrySet().iterator();
        while (rangeMap.hasNext()) {
            Entry entry = (Entry) rangeMap.next();
            builder.add(entry.getKey());
            builder2.add(entry.getValue());
        }
        return new ImmutableRangeMap(builder.build(), builder2.build());
    }

    public static <K extends Comparable<?>, V> Builder<K, V> builder() {
        return new Builder();
    }

    ImmutableRangeMap(ImmutableList<Range<K>> immutableList, ImmutableList<V> immutableList2) {
        this.ranges = immutableList;
        this.values = immutableList2;
    }

    @Nullable
    public V get(K k) {
        int binarySearch = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(k), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        V v = null;
        if (binarySearch == -1) {
            return null;
        }
        if (((Range) this.ranges.get(binarySearch)).contains(k) != null) {
            v = this.values.get(binarySearch);
        }
        return v;
    }

    @Nullable
    public Entry<Range<K>, V> getEntry(K k) {
        int binarySearch = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(k), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        Entry<Range<K>, V> entry = null;
        if (binarySearch == -1) {
            return null;
        }
        Range range = (Range) this.ranges.get(binarySearch);
        if (range.contains(k) != null) {
            entry = Maps.immutableEntry(range, this.values.get(binarySearch));
        }
        return entry;
    }

    public Range<K> span() {
        if (this.ranges.isEmpty()) {
            throw new NoSuchElementException();
        }
        return Range.create(((Range) this.ranges.get(0)).lowerBound, ((Range) this.ranges.get(this.ranges.size() - 1)).upperBound);
    }

    public void put(Range<K> range, V v) {
        throw new UnsupportedOperationException();
    }

    public void putAll(RangeMap<K, V> rangeMap) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public void remove(Range<K> range) {
        throw new UnsupportedOperationException();
    }

    public ImmutableMap<Range<K>, V> asMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.of();
        }
        return new RegularImmutableSortedMap(new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING), this.values);
    }

    public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
        if (((Range) Preconditions.checkNotNull(range)).isEmpty()) {
            return of();
        }
        if (!this.ranges.isEmpty()) {
            if (!range.encloses(span())) {
                final int binarySearch = SortedLists.binarySearch(this.ranges, Range.upperBoundFn(), range.lowerBound, KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER);
                int binarySearch2 = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), range.upperBound, KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
                if (binarySearch >= binarySearch2) {
                    return of();
                }
                final int i = binarySearch2 - binarySearch;
                final Range<K> range2 = range;
                final ImmutableRangeMap immutableRangeMap = this;
                return new ImmutableRangeMap<K, V>(new ImmutableList<Range<K>>() {
                    boolean isPartialView() {
                        return true;
                    }

                    public int size() {
                        return i;
                    }

                    public Range<K> get(int i) {
                        Preconditions.checkElementIndex(i, i);
                        if (i != 0) {
                            if (i != i - 1) {
                                return (Range) ImmutableRangeMap.this.ranges.get(i + binarySearch);
                            }
                        }
                        return ((Range) ImmutableRangeMap.this.ranges.get(i + binarySearch)).intersection(range);
                    }
                }, this.values.subList(binarySearch, binarySearch2)) {
                    public /* bridge */ /* synthetic */ Map asMapOfRanges() {
                        return super.asMapOfRanges();
                    }

                    public ImmutableRangeMap<K, V> subRangeMap(Range<K> range) {
                        if (range2.isConnected(range)) {
                            return immutableRangeMap.subRangeMap(range.intersection(range2));
                        }
                        return ImmutableRangeMap.of();
                    }
                };
            }
        }
        return this;
    }

    public int hashCode() {
        return asMapOfRanges().hashCode();
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof RangeMap)) {
            return null;
        }
        return asMapOfRanges().equals(((RangeMap) obj).asMapOfRanges());
    }

    public String toString() {
        return asMapOfRanges().toString();
    }
}
