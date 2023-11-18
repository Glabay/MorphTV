package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.SortedLists.KeyAbsentBehavior;
import com.google.common.collect.SortedLists.KeyPresentBehavior;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Beta
public final class ImmutableRangeSet<C extends Comparable> extends AbstractRangeSet<C> implements Serializable {
    private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(ImmutableList.of(Range.all()));
    private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(ImmutableList.of());
    private transient ImmutableRangeSet<C> complement;
    private final transient ImmutableList<Range<C>> ranges;

    private final class AsSet extends ImmutableSortedSet<C> {
        private final DiscreteDomain<C> domain;
        private transient Integer size;

        /* renamed from: com.google.common.collect.ImmutableRangeSet$AsSet$1 */
        class C09301 extends AbstractIterator<C> {
            Iterator<C> elemItr = Iterators.emptyIterator();
            final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();

            C09301() {
            }

            protected C computeNext() {
                while (!this.elemItr.hasNext()) {
                    if (!this.rangeItr.hasNext()) {
                        return (Comparable) endOfData();
                    }
                    this.elemItr = ContiguousSet.create((Range) this.rangeItr.next(), AsSet.this.domain).iterator();
                }
                return (Comparable) this.elemItr.next();
            }
        }

        /* renamed from: com.google.common.collect.ImmutableRangeSet$AsSet$2 */
        class C09312 extends AbstractIterator<C> {
            Iterator<C> elemItr = Iterators.emptyIterator();
            final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();

            C09312() {
            }

            protected C computeNext() {
                while (!this.elemItr.hasNext()) {
                    if (!this.rangeItr.hasNext()) {
                        return (Comparable) endOfData();
                    }
                    this.elemItr = ContiguousSet.create((Range) this.rangeItr.next(), AsSet.this.domain).descendingIterator();
                }
                return (Comparable) this.elemItr.next();
            }
        }

        AsSet(DiscreteDomain<C> discreteDomain) {
            super(Ordering.natural());
            this.domain = discreteDomain;
        }

        public int size() {
            Integer num = this.size;
            if (num == null) {
                long j = 0;
                Iterator it = ImmutableRangeSet.this.ranges.iterator();
                while (it.hasNext()) {
                    long size = j + ((long) ContiguousSet.create((Range) it.next(), this.domain).size());
                    if (size >= 2147483647L) {
                        j = size;
                        break;
                    }
                    j = size;
                }
                num = Integer.valueOf(Ints.saturatedCast(j));
                this.size = num;
            }
            return num.intValue();
        }

        public UnmodifiableIterator<C> iterator() {
            return new C09301();
        }

        @GwtIncompatible("NavigableSet")
        public UnmodifiableIterator<C> descendingIterator() {
            return new C09312();
        }

        ImmutableSortedSet<C> subSet(Range<C> range) {
            return ImmutableRangeSet.this.subRangeSet((Range) range).asSet(this.domain);
        }

        ImmutableSortedSet<C> headSetImpl(C c, boolean z) {
            return subSet(Range.upTo(c, BoundType.forBoolean(z)));
        }

        ImmutableSortedSet<C> subSetImpl(C c, boolean z, C c2, boolean z2) {
            if (z || z2 || Range.compareOrThrow(c, c2) != 0) {
                return subSet(Range.range(c, BoundType.forBoolean(z), c2, BoundType.forBoolean(z2)));
            }
            return ImmutableSortedSet.of();
        }

        ImmutableSortedSet<C> tailSetImpl(C c, boolean z) {
            return subSet(Range.downTo(c, BoundType.forBoolean(z)));
        }

        public boolean contains(@javax.annotation.Nullable java.lang.Object r3) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r2 = this;
            r0 = 0;
            if (r3 != 0) goto L_0x0004;
        L_0x0003:
            return r0;
        L_0x0004:
            r3 = (java.lang.Comparable) r3;	 Catch:{ ClassCastException -> 0x000d }
            r1 = com.google.common.collect.ImmutableRangeSet.this;	 Catch:{ ClassCastException -> 0x000d }
            r3 = r1.contains(r3);	 Catch:{ ClassCastException -> 0x000d }
            return r3;
        L_0x000d:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ImmutableRangeSet.AsSet.contains(java.lang.Object):boolean");
        }

        int indexOf(Object obj) {
            if (!contains(obj)) {
                return -1;
            }
            Comparable comparable = (Comparable) obj;
            long j = 0;
            Iterator it = ImmutableRangeSet.this.ranges.iterator();
            while (it.hasNext()) {
                Range range = (Range) it.next();
                if (range.contains(comparable)) {
                    return Ints.saturatedCast(j + ((long) ContiguousSet.create(range, this.domain).indexOf(comparable)));
                }
                j += (long) ContiguousSet.create(range, this.domain).size();
            }
            throw new AssertionError("impossible");
        }

        boolean isPartialView() {
            return ImmutableRangeSet.this.ranges.isPartialView();
        }

        public String toString() {
            return ImmutableRangeSet.this.ranges.toString();
        }

        Object writeReplace() {
            return new AsSetSerializedForm(ImmutableRangeSet.this.ranges, this.domain);
        }
    }

    private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
        private final DiscreteDomain<C> domain;
        private final ImmutableList<Range<C>> ranges;

        AsSetSerializedForm(ImmutableList<Range<C>> immutableList, DiscreteDomain<C> discreteDomain) {
            this.ranges = immutableList;
            this.domain = discreteDomain;
        }

        Object readResolve() {
            return new ImmutableRangeSet(this.ranges).asSet(this.domain);
        }
    }

    public static class Builder<C extends Comparable<?>> {
        private final RangeSet<C> rangeSet = TreeRangeSet.create();

        public Builder<C> add(Range<C> range) {
            if (range.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("range must not be empty, but was ");
                stringBuilder.append(range);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (this.rangeSet.complement().encloses(range)) {
                this.rangeSet.add(range);
                return this;
            } else {
                for (Range range2 : this.rangeSet.asRanges()) {
                    boolean z;
                    if (range2.isConnected(range)) {
                        if (!range2.intersection(range).isEmpty()) {
                            z = false;
                            Preconditions.checkArgument(z, "Ranges may not overlap, but received %s and %s", range2, range);
                        }
                    }
                    z = true;
                    Preconditions.checkArgument(z, "Ranges may not overlap, but received %s and %s", range2, range);
                }
                throw new AssertionError("should have thrown an IAE above");
            }
        }

        public Builder<C> addAll(RangeSet<C> rangeSet) {
            for (C add : rangeSet.asRanges()) {
                add(add);
            }
            return this;
        }

        public ImmutableRangeSet<C> build() {
            return ImmutableRangeSet.copyOf(this.rangeSet);
        }
    }

    private final class ComplementRanges extends ImmutableList<Range<C>> {
        private final boolean positiveBoundedAbove;
        private final boolean positiveBoundedBelow;
        private final int size;

        boolean isPartialView() {
            return true;
        }

        ComplementRanges() {
            this.positiveBoundedBelow = ((Range) ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
            this.positiveBoundedAbove = ((Range) Iterables.getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
            ImmutableRangeSet size = ImmutableRangeSet.this.ranges.size() - 1;
            if (this.positiveBoundedBelow) {
                size++;
            }
            if (this.positiveBoundedAbove) {
                size++;
            }
            this.size = size;
        }

        public int size() {
            return this.size;
        }

        public Range<C> get(int i) {
            Preconditions.checkElementIndex(i, this.size);
            Cut belowAll = this.positiveBoundedBelow ? i == 0 ? Cut.belowAll() : ((Range) ImmutableRangeSet.this.ranges.get(i - 1)).upperBound : ((Range) ImmutableRangeSet.this.ranges.get(i)).upperBound;
            if (this.positiveBoundedAbove && i == this.size - 1) {
                i = Cut.aboveAll();
            } else {
                i = ((Range) ImmutableRangeSet.this.ranges.get(i + (this.positiveBoundedBelow ^ 1))).lowerBound;
            }
            return Range.create(belowAll, i);
        }
    }

    private static final class SerializedForm<C extends Comparable> implements Serializable {
        private final ImmutableList<Range<C>> ranges;

        SerializedForm(ImmutableList<Range<C>> immutableList) {
            this.ranges = immutableList;
        }

        Object readResolve() {
            if (this.ranges.isEmpty()) {
                return ImmutableRangeSet.of();
            }
            if (this.ranges.equals(ImmutableList.of(Range.all()))) {
                return ImmutableRangeSet.all();
            }
            return new ImmutableRangeSet(this.ranges);
        }
    }

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ boolean contains(Comparable comparable) {
        return super.contains(comparable);
    }

    public /* bridge */ /* synthetic */ boolean enclosesAll(RangeSet rangeSet) {
        return super.enclosesAll(rangeSet);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static <C extends Comparable> ImmutableRangeSet<C> of() {
        return EMPTY;
    }

    static <C extends Comparable> ImmutableRangeSet<C> all() {
        return ALL;
    }

    public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
        Preconditions.checkNotNull(range);
        if (range.isEmpty()) {
            return of();
        }
        if (range.equals(Range.all())) {
            return all();
        }
        return new ImmutableRangeSet(ImmutableList.of(range));
    }

    public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
        Preconditions.checkNotNull(rangeSet);
        if (rangeSet.isEmpty()) {
            return of();
        }
        if (rangeSet.encloses(Range.all())) {
            return all();
        }
        if (rangeSet instanceof ImmutableRangeSet) {
            ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet) rangeSet;
            if (!immutableRangeSet.isPartialView()) {
                return immutableRangeSet;
            }
        }
        return new ImmutableRangeSet(ImmutableList.copyOf(rangeSet.asRanges()));
    }

    ImmutableRangeSet(ImmutableList<Range<C>> immutableList) {
        this.ranges = immutableList;
    }

    private ImmutableRangeSet(ImmutableList<Range<C>> immutableList, ImmutableRangeSet<C> immutableRangeSet) {
        this.ranges = immutableList;
        this.complement = immutableRangeSet;
    }

    public boolean encloses(Range<C> range) {
        int binarySearch = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), range.lowerBound, Ordering.natural(), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        return (binarySearch == -1 || ((Range) this.ranges.get(binarySearch)).encloses(range) == null) ? null : true;
    }

    public Range<C> rangeContaining(C c) {
        int binarySearch = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(c), Ordering.natural(), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        if (binarySearch == -1) {
            return null;
        }
        Range<C> range = (Range) this.ranges.get(binarySearch);
        if (range.contains(c) == null) {
            range = null;
        }
        return range;
    }

    public Range<C> span() {
        if (!this.ranges.isEmpty()) {
            return Range.create(((Range) this.ranges.get(0)).lowerBound, ((Range) this.ranges.get(this.ranges.size() - 1)).upperBound);
        }
        throw new NoSuchElementException();
    }

    public boolean isEmpty() {
        return this.ranges.isEmpty();
    }

    public void add(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    public void addAll(RangeSet<C> rangeSet) {
        throw new UnsupportedOperationException();
    }

    public void remove(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    public void removeAll(RangeSet<C> rangeSet) {
        throw new UnsupportedOperationException();
    }

    public ImmutableSet<Range<C>> asRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableSet.of();
        }
        return new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING);
    }

    public ImmutableRangeSet<C> complement() {
        ImmutableRangeSet<C> immutableRangeSet = this.complement;
        if (immutableRangeSet != null) {
            return immutableRangeSet;
        }
        if (this.ranges.isEmpty()) {
            immutableRangeSet = all();
            this.complement = immutableRangeSet;
            return immutableRangeSet;
        } else if (this.ranges.size() == 1 && ((Range) this.ranges.get(0)).equals(Range.all())) {
            immutableRangeSet = of();
            this.complement = immutableRangeSet;
            return immutableRangeSet;
        } else {
            ImmutableRangeSet<C> immutableRangeSet2 = new ImmutableRangeSet(new ComplementRanges(), this);
            this.complement = immutableRangeSet2;
            return immutableRangeSet2;
        }
    }

    private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
        if (!this.ranges.isEmpty()) {
            if (!range.isEmpty()) {
                if (range.encloses(span())) {
                    return this.ranges;
                }
                int binarySearch;
                final int binarySearch2 = range.hasLowerBound() ? SortedLists.binarySearch(this.ranges, Range.upperBoundFn(), range.lowerBound, KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER) : 0;
                if (range.hasUpperBound()) {
                    binarySearch = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), range.upperBound, KeyPresentBehavior.FIRST_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
                } else {
                    binarySearch = this.ranges.size();
                }
                binarySearch -= binarySearch2;
                if (binarySearch == 0) {
                    return ImmutableList.of();
                }
                return new ImmutableList<Range<C>>() {
                    boolean isPartialView() {
                        return true;
                    }

                    public int size() {
                        return binarySearch;
                    }

                    public Range<C> get(int i) {
                        Preconditions.checkElementIndex(i, binarySearch);
                        if (i != 0) {
                            if (i != binarySearch - 1) {
                                return (Range) ImmutableRangeSet.this.ranges.get(i + binarySearch2);
                            }
                        }
                        return ((Range) ImmutableRangeSet.this.ranges.get(i + binarySearch2)).intersection(range);
                    }
                };
            }
        }
        return ImmutableList.of();
    }

    public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
        if (!isEmpty()) {
            Range span = span();
            if (range.encloses(span)) {
                return this;
            }
            if (range.isConnected(span)) {
                return new ImmutableRangeSet(intersectRanges(range));
            }
        }
        return of();
    }

    public com.google.common.collect.ImmutableSortedSet<C> asSet(com.google.common.collect.DiscreteDomain<C> r3) {
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
        r2 = this;
        com.google.common.base.Preconditions.checkNotNull(r3);
        r0 = r2.isEmpty();
        if (r0 == 0) goto L_0x000e;
    L_0x0009:
        r3 = com.google.common.collect.ImmutableSortedSet.of();
        return r3;
    L_0x000e:
        r0 = r2.span();
        r0 = r0.canonical(r3);
        r1 = r0.hasLowerBound();
        if (r1 != 0) goto L_0x0024;
    L_0x001c:
        r3 = new java.lang.IllegalArgumentException;
        r0 = "Neither the DiscreteDomain nor this range set are bounded below";
        r3.<init>(r0);
        throw r3;
    L_0x0024:
        r0 = r0.hasUpperBound();
        if (r0 != 0) goto L_0x0036;
    L_0x002a:
        r3.maxValue();	 Catch:{ NoSuchElementException -> 0x002e }
        goto L_0x0036;
    L_0x002e:
        r3 = new java.lang.IllegalArgumentException;
        r0 = "Neither the DiscreteDomain nor this range set are bounded above";
        r3.<init>(r0);
        throw r3;
    L_0x0036:
        r0 = new com.google.common.collect.ImmutableRangeSet$AsSet;
        r0.<init>(r3);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ImmutableRangeSet.asSet(com.google.common.collect.DiscreteDomain):com.google.common.collect.ImmutableSortedSet<C>");
    }

    boolean isPartialView() {
        return this.ranges.isPartialView();
    }

    public static <C extends Comparable<?>> Builder<C> builder() {
        return new Builder();
    }

    Object writeReplace() {
        return new SerializedForm(this.ranges);
    }
}
