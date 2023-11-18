package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.SortedMap;

@GwtCompatible
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V> implements SortedMap<K, V> {

    @Beta
    protected class StandardKeySet extends SortedKeySet<K, V> {
        public StandardKeySet() {
            super(ForwardingSortedMap.this);
        }
    }

    protected abstract SortedMap<K, V> delegate();

    protected ForwardingSortedMap() {
    }

    public Comparator<? super K> comparator() {
        return delegate().comparator();
    }

    public K firstKey() {
        return delegate().firstKey();
    }

    public SortedMap<K, V> headMap(K k) {
        return delegate().headMap(k);
    }

    public K lastKey() {
        return delegate().lastKey();
    }

    public SortedMap<K, V> subMap(K k, K k2) {
        return delegate().subMap(k, k2);
    }

    public SortedMap<K, V> tailMap(K k) {
        return delegate().tailMap(k);
    }

    private int unsafeCompare(Object obj, Object obj2) {
        Comparator comparator = comparator();
        if (comparator == null) {
            return ((Comparable) obj).compareTo(obj2);
        }
        return comparator.compare(obj, obj2);
    }

    @com.google.common.annotations.Beta
    protected boolean standardContainsKey(@javax.annotation.Nullable java.lang.Object r3) {
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
        r1 = r2.tailMap(r3);	 Catch:{ ClassCastException -> 0x0013, NoSuchElementException -> 0x0012, NullPointerException -> 0x0011 }
        r1 = r1.firstKey();	 Catch:{ ClassCastException -> 0x0013, NoSuchElementException -> 0x0012, NullPointerException -> 0x0011 }
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ForwardingSortedMap.standardContainsKey(java.lang.Object):boolean");
    }

    @Beta
    protected SortedMap<K, V> standardSubMap(K k, K k2) {
        Preconditions.checkArgument(unsafeCompare(k, k2) <= 0, "fromKey must be <= toKey");
        return tailMap(k).headMap(k2);
    }
}
