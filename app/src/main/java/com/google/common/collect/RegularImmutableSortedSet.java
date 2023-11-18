package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.SortedLists.KeyAbsentBehavior;
import com.google.common.collect.SortedLists.KeyPresentBehavior;
import java.util.Collections;
import java.util.Comparator;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    private final transient ImmutableList<E> elements;

    public boolean isEmpty() {
        return false;
    }

    RegularImmutableSortedSet(ImmutableList<E> immutableList, Comparator<? super E> comparator) {
        super(comparator);
        this.elements = immutableList;
        Preconditions.checkArgument(immutableList.isEmpty() ^ 1);
    }

    public UnmodifiableIterator<E> iterator() {
        return this.elements.iterator();
    }

    @GwtIncompatible("NavigableSet")
    public UnmodifiableIterator<E> descendingIterator() {
        return this.elements.reverse().iterator();
    }

    public int size() {
        return this.elements.size();
    }

    public boolean contains(java.lang.Object r2) {
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
        r1 = this;
        r0 = 0;
        if (r2 == 0) goto L_0x000c;
    L_0x0003:
        r2 = r1.unsafeBinarySearch(r2);	 Catch:{ ClassCastException -> 0x000b }
        if (r2 < 0) goto L_0x000c;
    L_0x0009:
        r0 = 1;
        goto L_0x000c;
    L_0x000b:
        return r0;
    L_0x000c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.RegularImmutableSortedSet.contains(java.lang.Object):boolean");
    }

    public boolean containsAll(java.util.Collection<?> r6) {
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
        r5 = this;
        r0 = r6 instanceof com.google.common.collect.Multiset;
        if (r0 == 0) goto L_0x000a;
    L_0x0004:
        r6 = (com.google.common.collect.Multiset) r6;
        r6 = r6.elementSet();
    L_0x000a:
        r0 = r5.comparator();
        r0 = com.google.common.collect.SortedIterables.hasSameComparator(r0, r6);
        if (r0 == 0) goto L_0x0055;
    L_0x0014:
        r0 = r6.size();
        r1 = 1;
        if (r0 > r1) goto L_0x001c;
    L_0x001b:
        goto L_0x0055;
    L_0x001c:
        r0 = r5.iterator();
        r0 = com.google.common.collect.Iterators.peekingIterator(r0);
        r6 = r6.iterator();
        r2 = r6.next();
    L_0x002c:
        r3 = 0;
        r4 = r0.hasNext();	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
        if (r4 == 0) goto L_0x0052;	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
    L_0x0033:
        r4 = r0.peek();	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
        r4 = r5.unsafeCompare(r4, r2);	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
        if (r4 >= 0) goto L_0x0041;	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
    L_0x003d:
        r0.next();	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
        goto L_0x002c;	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
    L_0x0041:
        if (r4 != 0) goto L_0x004f;	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
    L_0x0043:
        r2 = r6.hasNext();	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
        if (r2 != 0) goto L_0x004a;	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
    L_0x0049:
        return r1;	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
    L_0x004a:
        r2 = r6.next();	 Catch:{ NullPointerException -> 0x0054, ClassCastException -> 0x0053 }
        goto L_0x002c;
    L_0x004f:
        if (r4 <= 0) goto L_0x002c;
    L_0x0051:
        return r3;
    L_0x0052:
        return r3;
    L_0x0053:
        return r3;
    L_0x0054:
        return r3;
    L_0x0055:
        r6 = super.containsAll(r6);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.RegularImmutableSortedSet.containsAll(java.util.Collection):boolean");
    }

    private int unsafeBinarySearch(Object obj) throws ClassCastException {
        return Collections.binarySearch(this.elements, obj, unsafeComparator());
    }

    boolean isPartialView() {
        return this.elements.isPartialView();
    }

    int copyIntoArray(Object[] objArr, int i) {
        return this.elements.copyIntoArray(objArr, i);
    }

    public boolean equals(@javax.annotation.Nullable java.lang.Object r6) {
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
        r5 = this;
        r0 = 1;
        if (r6 != r5) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r6 instanceof java.util.Set;
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r6 = (java.util.Set) r6;
        r1 = r5.size();
        r3 = r6.size();
        if (r1 == r3) goto L_0x0017;
    L_0x0016:
        return r2;
    L_0x0017:
        r1 = r5.comparator;
        r1 = com.google.common.collect.SortedIterables.hasSameComparator(r1, r6);
        if (r1 == 0) goto L_0x0041;
    L_0x001f:
        r6 = r6.iterator();
        r1 = r5.iterator();	 Catch:{ ClassCastException -> 0x0040, NoSuchElementException -> 0x003f }
    L_0x0027:
        r3 = r1.hasNext();	 Catch:{ ClassCastException -> 0x0040, NoSuchElementException -> 0x003f }
        if (r3 == 0) goto L_0x003e;	 Catch:{ ClassCastException -> 0x0040, NoSuchElementException -> 0x003f }
    L_0x002d:
        r3 = r1.next();	 Catch:{ ClassCastException -> 0x0040, NoSuchElementException -> 0x003f }
        r4 = r6.next();	 Catch:{ ClassCastException -> 0x0040, NoSuchElementException -> 0x003f }
        if (r4 == 0) goto L_0x003d;	 Catch:{ ClassCastException -> 0x0040, NoSuchElementException -> 0x003f }
    L_0x0037:
        r3 = r5.unsafeCompare(r3, r4);	 Catch:{ ClassCastException -> 0x0040, NoSuchElementException -> 0x003f }
        if (r3 == 0) goto L_0x0027;
    L_0x003d:
        return r2;
    L_0x003e:
        return r0;
    L_0x003f:
        return r2;
    L_0x0040:
        return r2;
    L_0x0041:
        r6 = r5.containsAll(r6);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.RegularImmutableSortedSet.equals(java.lang.Object):boolean");
    }

    public E first() {
        return this.elements.get(0);
    }

    public E last() {
        return this.elements.get(size() - 1);
    }

    public E lower(E e) {
        e = headIndex(e, false) - 1;
        if (e == -1) {
            return null;
        }
        return this.elements.get(e);
    }

    public E floor(E e) {
        e = headIndex(e, true) - 1;
        if (e == -1) {
            return null;
        }
        return this.elements.get(e);
    }

    public E ceiling(E e) {
        e = tailIndex(e, true);
        return e == size() ? null : this.elements.get(e);
    }

    public E higher(E e) {
        e = tailIndex(e, false);
        return e == size() ? null : this.elements.get(e);
    }

    ImmutableSortedSet<E> headSetImpl(E e, boolean z) {
        return getSubSet(false, headIndex(e, z));
    }

    int headIndex(E e, boolean z) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(e), comparator(), z ? KeyPresentBehavior.FIRST_AFTER : KeyPresentBehavior.FIRST_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
    }

    ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2) {
        return tailSetImpl(e, z).headSetImpl(e2, z2);
    }

    ImmutableSortedSet<E> tailSetImpl(E e, boolean z) {
        return getSubSet(tailIndex(e, z), size());
    }

    int tailIndex(E e, boolean z) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(e), comparator(), z ? KeyPresentBehavior.FIRST_PRESENT : KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER);
    }

    Comparator<Object> unsafeComparator() {
        return this.comparator;
    }

    ImmutableSortedSet<E> getSubSet(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        if (i < i2) {
            return new RegularImmutableSortedSet(this.elements.subList(i, i2), this.comparator);
        }
        return ImmutableSortedSet.emptySet(this.comparator);
    }

    int indexOf(@javax.annotation.Nullable java.lang.Object r6) {
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
        r5 = this;
        r0 = -1;
        if (r6 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r5.elements;	 Catch:{ ClassCastException -> 0x0017 }
        r2 = r5.unsafeComparator();	 Catch:{ ClassCastException -> 0x0017 }
        r3 = com.google.common.collect.SortedLists.KeyPresentBehavior.ANY_PRESENT;	 Catch:{ ClassCastException -> 0x0017 }
        r4 = com.google.common.collect.SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX;	 Catch:{ ClassCastException -> 0x0017 }
        r6 = com.google.common.collect.SortedLists.binarySearch(r1, r6, r2, r3, r4);	 Catch:{ ClassCastException -> 0x0017 }
        if (r6 < 0) goto L_0x0015;
    L_0x0014:
        goto L_0x0016;
    L_0x0015:
        r6 = -1;
    L_0x0016:
        return r6;
    L_0x0017:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.RegularImmutableSortedSet.indexOf(java.lang.Object):int");
    }

    ImmutableList<E> createAsList() {
        return new ImmutableSortedAsList(this, this.elements);
    }

    ImmutableSortedSet<E> createDescendingSet() {
        return new RegularImmutableSortedSet(this.elements.reverse(), Ordering.from(this.comparator).reverse());
    }
}
