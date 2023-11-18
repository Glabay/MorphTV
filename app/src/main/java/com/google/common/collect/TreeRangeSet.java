package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtIncompatible("uses NavigableMap")
@Beta
public class TreeRangeSet<C extends Comparable<?>> extends AbstractRangeSet<C> {
    private transient Set<Range<C>> asRanges;
    private transient RangeSet<C> complement;
    @VisibleForTesting
    final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;

    final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
        AsRanges() {
        }

        protected Collection<Range<C>> delegate() {
            return TreeRangeSet.this.rangesByLowerBound.values();
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }

    private final class Complement extends TreeRangeSet<C> {
        Complement() {
            super(new ComplementRangesByLowerBound(TreeRangeSet.this.rangesByLowerBound));
        }

        public void add(Range<C> range) {
            TreeRangeSet.this.remove(range);
        }

        public void remove(Range<C> range) {
            TreeRangeSet.this.add(range);
        }

        public boolean contains(C c) {
            return TreeRangeSet.this.contains(c) ^ 1;
        }

        public RangeSet<C> complement() {
            return TreeRangeSet.this;
        }
    }

    private static final class ComplementRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final Range<Cut<C>> complementLowerBoundWindow;
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;

        ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> navigableMap) {
            this(navigableMap, Range.all());
        }

        private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> navigableMap, Range<Cut<C>> range) {
            this.positiveRangesByLowerBound = navigableMap;
            this.positiveRangesByUpperBound = new RangesByUpperBound(navigableMap);
            this.complementLowerBoundWindow = range;
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> range) {
            if (!this.complementLowerBoundWindow.isConnected(range)) {
                return ImmutableSortedMap.of();
            }
            return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, range.intersection(this.complementLowerBoundWindow));
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> cut, boolean z, Cut<C> cut2, boolean z2) {
            return subMap(Range.range(cut, BoundType.forBoolean(z), cut2, BoundType.forBoolean(z2)));
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> cut, boolean z) {
            return subMap(Range.upTo(cut, BoundType.forBoolean(z)));
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> cut, boolean z) {
            return subMap(Range.downTo(cut, BoundType.forBoolean(z)));
        }

        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
            Collection values;
            Cut belowAll;
            if (this.complementLowerBoundWindow.hasLowerBound()) {
                values = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values();
            } else {
                values = this.positiveRangesByUpperBound.values();
            }
            final PeekingIterator peekingIterator = Iterators.peekingIterator(values.iterator());
            if (this.complementLowerBoundWindow.contains(Cut.belowAll()) && (!peekingIterator.hasNext() || ((Range) peekingIterator.peek()).lowerBound != Cut.belowAll())) {
                belowAll = Cut.belowAll();
            } else if (!peekingIterator.hasNext()) {
                return Iterators.emptyIterator();
            } else {
                belowAll = ((Range) peekingIterator.next()).upperBound;
            }
            return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {
                Cut<C> nextComplementRangeLowerBound = belowAll;

                protected Entry<Cut<C>, Range<C>> computeNext() {
                    if (!ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound)) {
                        if (this.nextComplementRangeLowerBound != Cut.aboveAll()) {
                            Range create;
                            if (peekingIterator.hasNext()) {
                                Range range = (Range) peekingIterator.next();
                                create = Range.create(this.nextComplementRangeLowerBound, range.lowerBound);
                                this.nextComplementRangeLowerBound = range.upperBound;
                            } else {
                                create = Range.create(this.nextComplementRangeLowerBound, Cut.aboveAll());
                                this.nextComplementRangeLowerBound = Cut.aboveAll();
                            }
                            return Maps.immutableEntry(create.lowerBound, create);
                        }
                    }
                    return (Entry) endOfData();
                }
            };
        }

        Iterator<Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            Object obj;
            Object aboveAll = this.complementLowerBoundWindow.hasUpperBound() ? (Cut) this.complementLowerBoundWindow.upperEndpoint() : Cut.aboveAll();
            boolean z = this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED;
            final PeekingIterator peekingIterator = Iterators.peekingIterator(this.positiveRangesByUpperBound.headMap(aboveAll, z).descendingMap().values().iterator());
            if (peekingIterator.hasNext()) {
                obj = ((Range) peekingIterator.peek()).upperBound == Cut.aboveAll() ? ((Range) peekingIterator.next()).lowerBound : (Cut) this.positiveRangesByLowerBound.higherKey(((Range) peekingIterator.peek()).upperBound);
            } else {
                if (this.complementLowerBoundWindow.contains(Cut.belowAll())) {
                    if (!this.positiveRangesByLowerBound.containsKey(Cut.belowAll())) {
                        obj = (Cut) this.positiveRangesByLowerBound.higherKey(Cut.belowAll());
                    }
                }
                return Iterators.emptyIterator();
            }
            final Cut cut = (Cut) MoreObjects.firstNonNull(obj, Cut.aboveAll());
            return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {
                Cut<C> nextComplementRangeUpperBound = cut;

                protected Entry<Cut<C>, Range<C>> computeNext() {
                    if (this.nextComplementRangeUpperBound == Cut.belowAll()) {
                        return (Entry) endOfData();
                    }
                    Range range;
                    if (peekingIterator.hasNext()) {
                        range = (Range) peekingIterator.next();
                        Range create = Range.create(range.upperBound, this.nextComplementRangeUpperBound);
                        this.nextComplementRangeUpperBound = range.lowerBound;
                        if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(create.lowerBound)) {
                            return Maps.immutableEntry(create.lowerBound, create);
                        }
                    } else if (ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
                        range = Range.create(Cut.belowAll(), this.nextComplementRangeUpperBound);
                        this.nextComplementRangeUpperBound = Cut.belowAll();
                        return Maps.immutableEntry(Cut.belowAll(), range);
                    }
                    return (Entry) endOfData();
                }
            };
        }

        public int size() {
            return Iterators.size(entryIterator());
        }

        @javax.annotation.Nullable
        public com.google.common.collect.Range<C> get(java.lang.Object r4) {
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
            r3 = this;
            r0 = r4 instanceof com.google.common.collect.Cut;
            r1 = 0;
            if (r0 == 0) goto L_0x0026;
        L_0x0005:
            r4 = (com.google.common.collect.Cut) r4;	 Catch:{ ClassCastException -> 0x0025 }
            r0 = 1;	 Catch:{ ClassCastException -> 0x0025 }
            r0 = r3.tailMap(r4, r0);	 Catch:{ ClassCastException -> 0x0025 }
            r0 = r0.firstEntry();	 Catch:{ ClassCastException -> 0x0025 }
            if (r0 == 0) goto L_0x0026;	 Catch:{ ClassCastException -> 0x0025 }
        L_0x0012:
            r2 = r0.getKey();	 Catch:{ ClassCastException -> 0x0025 }
            r2 = (com.google.common.collect.Cut) r2;	 Catch:{ ClassCastException -> 0x0025 }
            r4 = r2.equals(r4);	 Catch:{ ClassCastException -> 0x0025 }
            if (r4 == 0) goto L_0x0026;	 Catch:{ ClassCastException -> 0x0025 }
        L_0x001e:
            r4 = r0.getValue();	 Catch:{ ClassCastException -> 0x0025 }
            r4 = (com.google.common.collect.Range) r4;	 Catch:{ ClassCastException -> 0x0025 }
            return r4;
        L_0x0025:
            return r1;
        L_0x0026:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeRangeSet.ComplementRangesByLowerBound.get(java.lang.Object):com.google.common.collect.Range<C>");
        }

        public boolean containsKey(Object obj) {
            return get(obj) != null ? true : null;
        }
    }

    @VisibleForTesting
    static final class RangesByUpperBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final Range<Cut<C>> upperBoundWindow;

        RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> navigableMap) {
            this.rangesByLowerBound = navigableMap;
            this.upperBoundWindow = Range.all();
        }

        private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> navigableMap, Range<Cut<C>> range) {
            this.rangesByLowerBound = navigableMap;
            this.upperBoundWindow = range;
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> range) {
            if (range.isConnected(this.upperBoundWindow)) {
                return new RangesByUpperBound(this.rangesByLowerBound, range.intersection(this.upperBoundWindow));
            }
            return ImmutableSortedMap.of();
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> cut, boolean z, Cut<C> cut2, boolean z2) {
            return subMap(Range.range(cut, BoundType.forBoolean(z), cut2, BoundType.forBoolean(z2)));
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> cut, boolean z) {
            return subMap(Range.upTo(cut, BoundType.forBoolean(z)));
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> cut, boolean z) {
            return subMap(Range.downTo(cut, BoundType.forBoolean(z)));
        }

        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        public boolean containsKey(@Nullable Object obj) {
            return get(obj) != null ? true : null;
        }

        public com.google.common.collect.Range<C> get(@javax.annotation.Nullable java.lang.Object r4) {
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
            r3 = this;
            r0 = r4 instanceof com.google.common.collect.Cut;
            r1 = 0;
            if (r0 == 0) goto L_0x002e;
        L_0x0005:
            r4 = (com.google.common.collect.Cut) r4;	 Catch:{ ClassCastException -> 0x002d }
            r0 = r3.upperBoundWindow;	 Catch:{ ClassCastException -> 0x002d }
            r0 = r0.contains(r4);	 Catch:{ ClassCastException -> 0x002d }
            if (r0 != 0) goto L_0x0010;	 Catch:{ ClassCastException -> 0x002d }
        L_0x000f:
            return r1;	 Catch:{ ClassCastException -> 0x002d }
        L_0x0010:
            r0 = r3.rangesByLowerBound;	 Catch:{ ClassCastException -> 0x002d }
            r0 = r0.lowerEntry(r4);	 Catch:{ ClassCastException -> 0x002d }
            if (r0 == 0) goto L_0x002e;	 Catch:{ ClassCastException -> 0x002d }
        L_0x0018:
            r2 = r0.getValue();	 Catch:{ ClassCastException -> 0x002d }
            r2 = (com.google.common.collect.Range) r2;	 Catch:{ ClassCastException -> 0x002d }
            r2 = r2.upperBound;	 Catch:{ ClassCastException -> 0x002d }
            r4 = r2.equals(r4);	 Catch:{ ClassCastException -> 0x002d }
            if (r4 == 0) goto L_0x002e;	 Catch:{ ClassCastException -> 0x002d }
        L_0x0026:
            r4 = r0.getValue();	 Catch:{ ClassCastException -> 0x002d }
            r4 = (com.google.common.collect.Range) r4;	 Catch:{ ClassCastException -> 0x002d }
            return r4;
        L_0x002d:
            return r1;
        L_0x002e:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeRangeSet.RangesByUpperBound.get(java.lang.Object):com.google.common.collect.Range<C>");
        }

        Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
            Iterator it;
            if (this.upperBoundWindow.hasLowerBound()) {
                Entry lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
                if (lowerEntry == null) {
                    it = this.rangesByLowerBound.values().iterator();
                } else if (this.upperBoundWindow.lowerBound.isLessThan(((Range) lowerEntry.getValue()).upperBound)) {
                    it = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
                } else {
                    it = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
                }
            } else {
                it = this.rangesByLowerBound.values().iterator();
            }
            return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {
                protected Entry<Cut<C>, Range<C>> computeNext() {
                    if (!it.hasNext()) {
                        return (Entry) endOfData();
                    }
                    Range range = (Range) it.next();
                    if (RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
                        return (Entry) endOfData();
                    }
                    return Maps.immutableEntry(range.upperBound, range);
                }
            };
        }

        Iterator<Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            Collection values;
            if (this.upperBoundWindow.hasUpperBound()) {
                values = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
            } else {
                values = this.rangesByLowerBound.descendingMap().values();
            }
            final PeekingIterator peekingIterator = Iterators.peekingIterator(values.iterator());
            if (peekingIterator.hasNext() && this.upperBoundWindow.upperBound.isLessThan(((Range) peekingIterator.peek()).upperBound)) {
                peekingIterator.next();
            }
            return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {
                protected Entry<Cut<C>, Range<C>> computeNext() {
                    if (!peekingIterator.hasNext()) {
                        return (Entry) endOfData();
                    }
                    Range range = (Range) peekingIterator.next();
                    return RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound) ? Maps.immutableEntry(range.upperBound, range) : (Entry) endOfData();
                }
            };
        }

        public int size() {
            if (this.upperBoundWindow.equals(Range.all())) {
                return this.rangesByLowerBound.size();
            }
            return Iterators.size(entryIterator());
        }

        public boolean isEmpty() {
            if (this.upperBoundWindow.equals(Range.all())) {
                return this.rangesByLowerBound.isEmpty();
            }
            return !entryIterator().hasNext();
        }
    }

    private final class SubRangeSet extends TreeRangeSet<C> {
        private final Range<C> restriction;

        SubRangeSet(Range<C> range) {
            super(new SubRangeSetRangesByLowerBound(Range.all(), range, TreeRangeSet.this.rangesByLowerBound));
            this.restriction = range;
        }

        public boolean encloses(Range<C> range) {
            boolean z = false;
            if (this.restriction.isEmpty() || !this.restriction.encloses(range)) {
                return false;
            }
            range = TreeRangeSet.this.rangeEnclosing(range);
            if (range != null && range.intersection(this.restriction).isEmpty() == null) {
                z = true;
            }
            return z;
        }

        @Nullable
        public Range<C> rangeContaining(C c) {
            Range<C> range = null;
            if (!this.restriction.contains(c)) {
                return null;
            }
            c = TreeRangeSet.this.rangeContaining(c);
            if (c != null) {
                range = c.intersection(this.restriction);
            }
            return range;
        }

        public void add(Range<C> range) {
            Preconditions.checkArgument(this.restriction.encloses(range), "Cannot add range %s to subRangeSet(%s)", range, this.restriction);
            super.add(range);
        }

        public void remove(Range<C> range) {
            if (range.isConnected(this.restriction)) {
                TreeRangeSet.this.remove(range.intersection(this.restriction));
            }
        }

        public boolean contains(C c) {
            return (!this.restriction.contains(c) || TreeRangeSet.this.contains(c) == null) ? null : true;
        }

        public void clear() {
            TreeRangeSet.this.remove(this.restriction);
        }

        public RangeSet<C> subRangeSet(Range<C> range) {
            if (range.encloses(this.restriction)) {
                return this;
            }
            if (range.isConnected(this.restriction)) {
                return new SubRangeSet(this.restriction.intersection(range));
            }
            return ImmutableRangeSet.of();
        }
    }

    private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
        private final Range<Cut<C>> lowerBoundWindow;
        private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
        private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
        private final Range<C> restriction;

        private SubRangeSetRangesByLowerBound(Range<Cut<C>> range, Range<C> range2, NavigableMap<Cut<C>, Range<C>> navigableMap) {
            this.lowerBoundWindow = (Range) Preconditions.checkNotNull(range);
            this.restriction = (Range) Preconditions.checkNotNull(range2);
            this.rangesByLowerBound = (NavigableMap) Preconditions.checkNotNull(navigableMap);
            this.rangesByUpperBound = new RangesByUpperBound(navigableMap);
        }

        private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> range) {
            if (range.isConnected(this.lowerBoundWindow)) {
                return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow.intersection(range), this.restriction, this.rangesByLowerBound);
            }
            return ImmutableSortedMap.of();
        }

        public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> cut, boolean z, Cut<C> cut2, boolean z2) {
            return subMap(Range.range(cut, BoundType.forBoolean(z), cut2, BoundType.forBoolean(z2)));
        }

        public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> cut, boolean z) {
            return subMap(Range.upTo(cut, BoundType.forBoolean(z)));
        }

        public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> cut, boolean z) {
            return subMap(Range.downTo(cut, BoundType.forBoolean(z)));
        }

        public Comparator<? super Cut<C>> comparator() {
            return Ordering.natural();
        }

        public boolean containsKey(@Nullable Object obj) {
            return get(obj) != null ? true : null;
        }

        @javax.annotation.Nullable
        public com.google.common.collect.Range<C> get(@javax.annotation.Nullable java.lang.Object r4) {
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
            r3 = this;
            r0 = r4 instanceof com.google.common.collect.Cut;
            r1 = 0;
            if (r0 == 0) goto L_0x0062;
        L_0x0005:
            r4 = (com.google.common.collect.Cut) r4;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r3.lowerBoundWindow;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r0.contains(r4);	 Catch:{ ClassCastException -> 0x0061 }
            if (r0 == 0) goto L_0x0060;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x000f:
            r0 = r3.restriction;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r0.lowerBound;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r4.compareTo(r0);	 Catch:{ ClassCastException -> 0x0061 }
            if (r0 < 0) goto L_0x0060;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x0019:
            r0 = r3.restriction;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r0.upperBound;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r4.compareTo(r0);	 Catch:{ ClassCastException -> 0x0061 }
            if (r0 < 0) goto L_0x0024;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x0023:
            goto L_0x0060;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x0024:
            r0 = r3.restriction;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r0.lowerBound;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r4.equals(r0);	 Catch:{ ClassCastException -> 0x0061 }
            if (r0 == 0) goto L_0x004f;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x002e:
            r0 = r3.rangesByLowerBound;	 Catch:{ ClassCastException -> 0x0061 }
            r4 = r0.floorEntry(r4);	 Catch:{ ClassCastException -> 0x0061 }
            r4 = com.google.common.collect.Maps.valueOrNull(r4);	 Catch:{ ClassCastException -> 0x0061 }
            r4 = (com.google.common.collect.Range) r4;	 Catch:{ ClassCastException -> 0x0061 }
            if (r4 == 0) goto L_0x0062;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x003c:
            r0 = r4.upperBound;	 Catch:{ ClassCastException -> 0x0061 }
            r2 = r3.restriction;	 Catch:{ ClassCastException -> 0x0061 }
            r2 = r2.lowerBound;	 Catch:{ ClassCastException -> 0x0061 }
            r0 = r0.compareTo(r2);	 Catch:{ ClassCastException -> 0x0061 }
            if (r0 <= 0) goto L_0x0062;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x0048:
            r0 = r3.restriction;	 Catch:{ ClassCastException -> 0x0061 }
            r4 = r4.intersection(r0);	 Catch:{ ClassCastException -> 0x0061 }
            return r4;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x004f:
            r0 = r3.rangesByLowerBound;	 Catch:{ ClassCastException -> 0x0061 }
            r4 = r0.get(r4);	 Catch:{ ClassCastException -> 0x0061 }
            r4 = (com.google.common.collect.Range) r4;	 Catch:{ ClassCastException -> 0x0061 }
            if (r4 == 0) goto L_0x0062;	 Catch:{ ClassCastException -> 0x0061 }
        L_0x0059:
            r0 = r3.restriction;	 Catch:{ ClassCastException -> 0x0061 }
            r4 = r4.intersection(r0);	 Catch:{ ClassCastException -> 0x0061 }
            return r4;
        L_0x0060:
            return r1;
        L_0x0061:
            return r1;
        L_0x0062:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeRangeSet.SubRangeSetRangesByLowerBound.get(java.lang.Object):com.google.common.collect.Range<C>");
        }

        Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
            if (this.restriction.isEmpty()) {
                return Iterators.emptyIterator();
            }
            if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound)) {
                return Iterators.emptyIterator();
            }
            Iterator it;
            boolean z = false;
            if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
                it = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
            } else {
                NavigableMap navigableMap = this.rangesByLowerBound;
                Comparable endpoint = this.lowerBoundWindow.lowerBound.endpoint();
                if (this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED) {
                    z = true;
                }
                it = navigableMap.tailMap(endpoint, z).values().iterator();
            }
            final Cut cut = (Cut) Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {
                protected Entry<Cut<C>, Range<C>> computeNext() {
                    if (!it.hasNext()) {
                        return (Entry) endOfData();
                    }
                    Range range = (Range) it.next();
                    if (cut.isLessThan(range.lowerBound)) {
                        return (Entry) endOfData();
                    }
                    range = range.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                    return Maps.immutableEntry(range.lowerBound, range);
                }
            };
        }

        Iterator<Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
            if (this.restriction.isEmpty()) {
                return Iterators.emptyIterator();
            }
            Cut cut = (Cut) Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            final Iterator it = this.rangesByLowerBound.headMap(cut.endpoint(), cut.typeAsUpperBound() == BoundType.CLOSED).descendingMap().values().iterator();
            return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {
                protected Entry<Cut<C>, Range<C>> computeNext() {
                    if (!it.hasNext()) {
                        return (Entry) endOfData();
                    }
                    Range range = (Range) it.next();
                    if (SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(range.upperBound) >= 0) {
                        return (Entry) endOfData();
                    }
                    range = range.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                    if (SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(range.lowerBound)) {
                        return Maps.immutableEntry(range.lowerBound, range);
                    }
                    return (Entry) endOfData();
                }
            };
        }

        public int size() {
            return Iterators.size(entryIterator());
        }
    }

    public /* bridge */ /* synthetic */ void addAll(RangeSet rangeSet) {
        super.addAll(rangeSet);
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

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ void removeAll(RangeSet rangeSet) {
        super.removeAll(rangeSet);
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create() {
        return new TreeRangeSet(new TreeMap());
    }

    public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet) {
        TreeRangeSet<C> create = create();
        create.addAll(rangeSet);
        return create;
    }

    private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> navigableMap) {
        this.rangesByLowerBound = navigableMap;
    }

    public Set<Range<C>> asRanges() {
        Set<Range<C>> set = this.asRanges;
        if (set != null) {
            return set;
        }
        set = new AsRanges();
        this.asRanges = set;
        return set;
    }

    @Nullable
    public Range<C> rangeContaining(C c) {
        Preconditions.checkNotNull(c);
        Entry floorEntry = this.rangesByLowerBound.floorEntry(Cut.belowValue(c));
        return (floorEntry == null || ((Range) floorEntry.getValue()).contains(c) == null) ? null : (Range) floorEntry.getValue();
    }

    public boolean encloses(Range<C> range) {
        Preconditions.checkNotNull(range);
        Entry floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
        return (floorEntry == null || ((Range) floorEntry.getValue()).encloses(range) == null) ? null : true;
    }

    @Nullable
    private Range<C> rangeEnclosing(Range<C> range) {
        Preconditions.checkNotNull(range);
        Entry floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
        return (floorEntry == null || ((Range) floorEntry.getValue()).encloses(range) == null) ? null : (Range) floorEntry.getValue();
    }

    public Range<C> span() {
        Entry firstEntry = this.rangesByLowerBound.firstEntry();
        Entry lastEntry = this.rangesByLowerBound.lastEntry();
        if (firstEntry != null) {
            return Range.create(((Range) firstEntry.getValue()).lowerBound, ((Range) lastEntry.getValue()).upperBound);
        }
        throw new NoSuchElementException();
    }

    public void add(Range<C> range) {
        Preconditions.checkNotNull(range);
        if (!range.isEmpty()) {
            Range range2;
            Cut cut = range.lowerBound;
            range = range.upperBound;
            Entry lowerEntry = this.rangesByLowerBound.lowerEntry(cut);
            if (lowerEntry != null) {
                range2 = (Range) lowerEntry.getValue();
                if (range2.upperBound.compareTo(cut) >= 0) {
                    if (range2.upperBound.compareTo((Cut) range) >= 0) {
                        range = range2.upperBound;
                    }
                    cut = range2.lowerBound;
                }
            }
            lowerEntry = this.rangesByLowerBound.floorEntry(range);
            if (lowerEntry != null) {
                range2 = (Range) lowerEntry.getValue();
                if (range2.upperBound.compareTo((Cut) range) >= 0) {
                    range = range2.upperBound;
                }
            }
            this.rangesByLowerBound.subMap(cut, range).clear();
            replaceRangeWithSameLowerBound(Range.create(cut, range));
        }
    }

    public void remove(Range<C> range) {
        Preconditions.checkNotNull(range);
        if (!range.isEmpty()) {
            Range range2;
            Entry lowerEntry = this.rangesByLowerBound.lowerEntry(range.lowerBound);
            if (lowerEntry != null) {
                range2 = (Range) lowerEntry.getValue();
                if (range2.upperBound.compareTo(range.lowerBound) >= 0) {
                    if (range.hasUpperBound() && range2.upperBound.compareTo(range.upperBound) >= 0) {
                        replaceRangeWithSameLowerBound(Range.create(range.upperBound, range2.upperBound));
                    }
                    replaceRangeWithSameLowerBound(Range.create(range2.lowerBound, range.lowerBound));
                }
            }
            lowerEntry = this.rangesByLowerBound.floorEntry(range.upperBound);
            if (lowerEntry != null) {
                range2 = (Range) lowerEntry.getValue();
                if (range.hasUpperBound() && range2.upperBound.compareTo(range.upperBound) >= 0) {
                    replaceRangeWithSameLowerBound(Range.create(range.upperBound, range2.upperBound));
                }
            }
            this.rangesByLowerBound.subMap(range.lowerBound, range.upperBound).clear();
        }
    }

    private void replaceRangeWithSameLowerBound(Range<C> range) {
        if (range.isEmpty()) {
            this.rangesByLowerBound.remove(range.lowerBound);
        } else {
            this.rangesByLowerBound.put(range.lowerBound, range);
        }
    }

    public RangeSet<C> complement() {
        RangeSet<C> rangeSet = this.complement;
        if (rangeSet != null) {
            return rangeSet;
        }
        rangeSet = new Complement();
        this.complement = rangeSet;
        return rangeSet;
    }

    public RangeSet<C> subRangeSet(Range<C> range) {
        return range.equals(Range.all()) ? this : new SubRangeSet(range);
    }
}
