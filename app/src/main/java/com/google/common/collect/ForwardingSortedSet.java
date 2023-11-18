package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.SortedSet;

@GwtCompatible
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E> implements SortedSet<E> {
    protected abstract SortedSet<E> delegate();

    protected ForwardingSortedSet() {
    }

    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    public E first() {
        return delegate().first();
    }

    public SortedSet<E> headSet(E e) {
        return delegate().headSet(e);
    }

    public E last() {
        return delegate().last();
    }

    public SortedSet<E> subSet(E e, E e2) {
        return delegate().subSet(e, e2);
    }

    public SortedSet<E> tailSet(E e) {
        return delegate().tailSet(e);
    }

    private int unsafeCompare(Object obj, Object obj2) {
        Comparator comparator = comparator();
        return comparator == null ? ((Comparable) obj).compareTo(obj2) : comparator.compare(obj, obj2);
    }

    @com.google.common.annotations.Beta
    protected boolean standardContains(@javax.annotation.Nullable java.lang.Object r3) {
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
        r0 = 0;
        r1 = r2.tailSet(r3);	 Catch:{ ClassCastException -> 0x0013, NoSuchElementException -> 0x0012, NullPointerException -> 0x0011 }
        r1 = r1.first();	 Catch:{ ClassCastException -> 0x0013, NoSuchElementException -> 0x0012, NullPointerException -> 0x0011 }
        r3 = r2.unsafeCompare(r1, r3);	 Catch:{ ClassCastException -> 0x0013, NoSuchElementException -> 0x0012, NullPointerException -> 0x0011 }
        if (r3 != 0) goto L_0x0010;
    L_0x000f:
        r0 = 1;
    L_0x0010:
        return r0;
    L_0x0011:
        return r0;
    L_0x0012:
        return r0;
    L_0x0013:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ForwardingSortedSet.standardContains(java.lang.Object):boolean");
    }

    @com.google.common.annotations.Beta
    protected boolean standardRemove(@javax.annotation.Nullable java.lang.Object r4) {
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
        r3 = this;
        r0 = 0;
        r1 = r3.tailSet(r4);	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
        r1 = r1.iterator();	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
        r2 = r1.hasNext();	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
        if (r2 == 0) goto L_0x001e;	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
    L_0x000f:
        r2 = r1.next();	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
        r4 = r3.unsafeCompare(r2, r4);	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
        if (r4 != 0) goto L_0x001e;	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
    L_0x0019:
        r1.remove();	 Catch:{ ClassCastException -> 0x0020, NullPointerException -> 0x001f }
        r4 = 1;
        return r4;
    L_0x001e:
        return r0;
    L_0x001f:
        return r0;
    L_0x0020:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ForwardingSortedSet.standardRemove(java.lang.Object):boolean");
    }

    @Beta
    protected SortedSet<E> standardSubSet(E e, E e2) {
        return tailSet(e).headSet(e2);
    }
}
