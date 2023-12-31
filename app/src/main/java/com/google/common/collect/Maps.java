package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.MapDifference.ValueDifference;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Maps {
    static final MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

    static abstract class EntrySet<K, V> extends ImprovedAbstractSet<Entry<K, V>> {
        abstract Map<K, V> map();

        EntrySet() {
        }

        public int size() {
            return map().size();
        }

        public void clear() {
            map().clear();
        }

        public boolean contains(Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object key = entry.getKey();
            Object safeGet = Maps.safeGet(map(), key);
            if (!(Objects.equal(safeGet, entry.getValue()) == null || (safeGet == null && map().containsKey(key) == null))) {
                z = true;
            }
            return z;
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return null;
            }
            return map().keySet().remove(((Entry) obj).getKey());
        }

        public boolean removeAll(java.util.Collection<?> r2) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r1 = this;
            r0 = com.google.common.base.Preconditions.checkNotNull(r2);	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = (java.util.Collection) r0;	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = super.removeAll(r0);	 Catch:{ UnsupportedOperationException -> 0x000b }
            return r0;
        L_0x000b:
            r2 = r2.iterator();
            r2 = com.google.common.collect.Sets.removeAllImpl(r1, r2);
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.EntrySet.removeAll(java.util.Collection):boolean");
        }

        public boolean retainAll(java.util.Collection<?> r4) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r0 = com.google.common.base.Preconditions.checkNotNull(r4);	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = (java.util.Collection) r0;	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = super.retainAll(r0);	 Catch:{ UnsupportedOperationException -> 0x000b }
            return r0;
        L_0x000b:
            r0 = r4.size();
            r0 = com.google.common.collect.Sets.newHashSetWithExpectedSize(r0);
            r4 = r4.iterator();
        L_0x0017:
            r1 = r4.hasNext();
            if (r1 == 0) goto L_0x0031;
        L_0x001d:
            r1 = r4.next();
            r2 = r3.contains(r1);
            if (r2 == 0) goto L_0x0017;
        L_0x0027:
            r1 = (java.util.Map.Entry) r1;
            r1 = r1.getKey();
            r0.add(r1);
            goto L_0x0017;
        L_0x0031:
            r4 = r3.map();
            r4 = r4.keySet();
            r4 = r4.retainAll(r0);
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.EntrySet.retainAll(java.util.Collection):boolean");
        }
    }

    @GwtCompatible
    static abstract class ViewCachingAbstractMap<K, V> extends AbstractMap<K, V> {
        private transient Set<Entry<K, V>> entrySet;
        private transient Set<K> keySet;
        private transient Collection<V> values;

        abstract Set<Entry<K, V>> createEntrySet();

        ViewCachingAbstractMap() {
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = createEntrySet();
            this.entrySet = set;
            return set;
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = createKeySet();
            this.keySet = set;
            return set;
        }

        Set<K> createKeySet() {
            return new KeySet(this);
        }

        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = createValues();
            this.values = collection;
            return collection;
        }

        Collection<V> createValues() {
            return new Values(this);
        }
    }

    static class KeySet<K, V> extends ImprovedAbstractSet<K> {
        final Map<K, V> map;

        KeySet(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        Map<K, V> map() {
            return this.map;
        }

        public Iterator<K> iterator() {
            return Maps.keyIterator(map().entrySet().iterator());
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(Object obj) {
            return map().containsKey(obj);
        }

        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return null;
            }
            map().remove(obj);
            return true;
        }

        public void clear() {
            map().clear();
        }
    }

    @GwtIncompatible("NavigableMap")
    static abstract class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {
        private transient Comparator<? super K> comparator;
        private transient Set<Entry<K, V>> entrySet;
        private transient NavigableSet<K> navigableKeySet;

        /* renamed from: com.google.common.collect.Maps$DescendingMap$1 */
        class C10061 extends EntrySet<K, V> {
            C10061() {
            }

            Map<K, V> map() {
                return DescendingMap.this;
            }

            public Iterator<Entry<K, V>> iterator() {
                return DescendingMap.this.entryIterator();
            }
        }

        abstract Iterator<Entry<K, V>> entryIterator();

        abstract NavigableMap<K, V> forward();

        DescendingMap() {
        }

        protected final Map<K, V> delegate() {
            return forward();
        }

        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator = this.comparator;
            if (comparator != null) {
                return comparator;
            }
            Comparator comparator2 = forward().comparator();
            if (comparator2 == null) {
                comparator2 = Ordering.natural();
            }
            comparator = reverse(comparator2);
            this.comparator = comparator;
            return comparator;
        }

        private static <T> Ordering<T> reverse(Comparator<T> comparator) {
            return Ordering.from((Comparator) comparator).reverse();
        }

        public K firstKey() {
            return forward().lastKey();
        }

        public K lastKey() {
            return forward().firstKey();
        }

        public Entry<K, V> lowerEntry(K k) {
            return forward().higherEntry(k);
        }

        public K lowerKey(K k) {
            return forward().higherKey(k);
        }

        public Entry<K, V> floorEntry(K k) {
            return forward().ceilingEntry(k);
        }

        public K floorKey(K k) {
            return forward().ceilingKey(k);
        }

        public Entry<K, V> ceilingEntry(K k) {
            return forward().floorEntry(k);
        }

        public K ceilingKey(K k) {
            return forward().floorKey(k);
        }

        public Entry<K, V> higherEntry(K k) {
            return forward().lowerEntry(k);
        }

        public K higherKey(K k) {
            return forward().lowerKey(k);
        }

        public Entry<K, V> firstEntry() {
            return forward().lastEntry();
        }

        public Entry<K, V> lastEntry() {
            return forward().firstEntry();
        }

        public Entry<K, V> pollFirstEntry() {
            return forward().pollLastEntry();
        }

        public Entry<K, V> pollLastEntry() {
            return forward().pollFirstEntry();
        }

        public NavigableMap<K, V> descendingMap() {
            return forward();
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = createEntrySet();
            this.entrySet = set;
            return set;
        }

        Set<Entry<K, V>> createEntrySet() {
            return new C10061();
        }

        public Set<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> navigableKeySet() {
            NavigableSet<K> navigableSet = this.navigableKeySet;
            if (navigableSet != null) {
                return navigableSet;
            }
            navigableSet = new NavigableKeySet(this);
            this.navigableKeySet = navigableSet;
            return navigableSet;
        }

        public NavigableSet<K> descendingKeySet() {
            return forward().navigableKeySet();
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            return forward().subMap(k2, z2, k, z).descendingMap();
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            return forward().tailMap(k, z).descendingMap();
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            return forward().headMap(k, z).descendingMap();
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        public SortedMap<K, V> headMap(K k) {
            return headMap(k, false);
        }

        public SortedMap<K, V> tailMap(K k) {
            return tailMap(k, true);
        }

        public Collection<V> values() {
            return new Values(this);
        }

        public String toString() {
            return standardToString();
        }
    }

    static class Values<K, V> extends AbstractCollection<V> {
        final Map<K, V> map;

        Values(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        final Map<K, V> map() {
            return this.map;
        }

        public Iterator<V> iterator() {
            return Maps.valueIterator(map().entrySet().iterator());
        }

        public boolean remove(java.lang.Object r4) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r0 = super.remove(r4);	 Catch:{ UnsupportedOperationException -> 0x0005 }
            return r0;
        L_0x0005:
            r0 = r3.map();
            r0 = r0.entrySet();
            r0 = r0.iterator();
        L_0x0011:
            r1 = r0.hasNext();
            if (r1 == 0) goto L_0x0034;
        L_0x0017:
            r1 = r0.next();
            r1 = (java.util.Map.Entry) r1;
            r2 = r1.getValue();
            r2 = com.google.common.base.Objects.equal(r4, r2);
            if (r2 == 0) goto L_0x0011;
        L_0x0027:
            r4 = r3.map();
            r0 = r1.getKey();
            r4.remove(r0);
            r4 = 1;
            return r4;
        L_0x0034:
            r4 = 0;
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.Values.remove(java.lang.Object):boolean");
        }

        public boolean removeAll(java.util.Collection<?> r5) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = com.google.common.base.Preconditions.checkNotNull(r5);	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = (java.util.Collection) r0;	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = super.removeAll(r0);	 Catch:{ UnsupportedOperationException -> 0x000b }
            return r0;
        L_0x000b:
            r0 = com.google.common.collect.Sets.newHashSet();
            r1 = r4.map();
            r1 = r1.entrySet();
            r1 = r1.iterator();
        L_0x001b:
            r2 = r1.hasNext();
            if (r2 == 0) goto L_0x0039;
        L_0x0021:
            r2 = r1.next();
            r2 = (java.util.Map.Entry) r2;
            r3 = r2.getValue();
            r3 = r5.contains(r3);
            if (r3 == 0) goto L_0x001b;
        L_0x0031:
            r2 = r2.getKey();
            r0.add(r2);
            goto L_0x001b;
        L_0x0039:
            r5 = r4.map();
            r5 = r5.keySet();
            r5 = r5.removeAll(r0);
            return r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.Values.removeAll(java.util.Collection):boolean");
        }

        public boolean retainAll(java.util.Collection<?> r5) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = com.google.common.base.Preconditions.checkNotNull(r5);	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = (java.util.Collection) r0;	 Catch:{ UnsupportedOperationException -> 0x000b }
            r0 = super.retainAll(r0);	 Catch:{ UnsupportedOperationException -> 0x000b }
            return r0;
        L_0x000b:
            r0 = com.google.common.collect.Sets.newHashSet();
            r1 = r4.map();
            r1 = r1.entrySet();
            r1 = r1.iterator();
        L_0x001b:
            r2 = r1.hasNext();
            if (r2 == 0) goto L_0x0039;
        L_0x0021:
            r2 = r1.next();
            r2 = (java.util.Map.Entry) r2;
            r3 = r2.getValue();
            r3 = r5.contains(r3);
            if (r3 == 0) goto L_0x001b;
        L_0x0031:
            r2 = r2.getKey();
            r0.add(r2);
            goto L_0x001b;
        L_0x0039:
            r5 = r4.map();
            r5 = r5.keySet();
            r5 = r5.retainAll(r0);
            return r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.Values.retainAll(java.util.Collection):boolean");
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(@Nullable Object obj) {
            return map().containsValue(obj);
        }

        public void clear() {
            map().clear();
        }
    }

    static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
        SortedKeySet(SortedMap<K, V> sortedMap) {
            super(sortedMap);
        }

        SortedMap<K, V> map() {
            return (SortedMap) super.map();
        }

        public Comparator<? super K> comparator() {
            return map().comparator();
        }

        public SortedSet<K> subSet(K k, K k2) {
            return new SortedKeySet(map().subMap(k, k2));
        }

        public SortedSet<K> headSet(K k) {
            return new SortedKeySet(map().headMap(k));
        }

        public SortedSet<K> tailSet(K k) {
            return new SortedKeySet(map().tailMap(k));
        }

        public K first() {
            return map().firstKey();
        }

        public K last() {
            return map().lastKey();
        }
    }

    @GwtIncompatible("NavigableMap")
    static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
        NavigableKeySet(NavigableMap<K, V> navigableMap) {
            super(navigableMap);
        }

        NavigableMap<K, V> map() {
            return (NavigableMap) this.map;
        }

        public K lower(K k) {
            return map().lowerKey(k);
        }

        public K floor(K k) {
            return map().floorKey(k);
        }

        public K ceiling(K k) {
            return map().ceilingKey(k);
        }

        public K higher(K k) {
            return map().higherKey(k);
        }

        public K pollFirst() {
            return Maps.keyOrNull(map().pollFirstEntry());
        }

        public K pollLast() {
            return Maps.keyOrNull(map().pollLastEntry());
        }

        public NavigableSet<K> descendingSet() {
            return map().descendingKeySet();
        }

        public Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        public NavigableSet<K> subSet(K k, boolean z, K k2, boolean z2) {
            return map().subMap(k, z, k2, z2).navigableKeySet();
        }

        public NavigableSet<K> headSet(K k, boolean z) {
            return map().headMap(k, z).navigableKeySet();
        }

        public NavigableSet<K> tailSet(K k, boolean z) {
            return map().tailMap(k, z).navigableKeySet();
        }

        public SortedSet<K> subSet(K k, K k2) {
            return subSet(k, true, k2, false);
        }

        public SortedSet<K> headSet(K k) {
            return headSet(k, false);
        }

        public SortedSet<K> tailSet(K k) {
            return tailSet(k, true);
        }
    }

    public interface EntryTransformer<K, V1, V2> {
        V2 transformEntry(@Nullable K k, @Nullable V1 v1);
    }

    private static abstract class AbstractFilteredMap<K, V> extends ViewCachingAbstractMap<K, V> {
        final Predicate<? super Entry<K, V>> predicate;
        final Map<K, V> unfiltered;

        AbstractFilteredMap(Map<K, V> map, Predicate<? super Entry<K, V>> predicate) {
            this.unfiltered = map;
            this.predicate = predicate;
        }

        boolean apply(@Nullable Object obj, @Nullable V v) {
            return this.predicate.apply(Maps.immutableEntry(obj, v));
        }

        public V put(K k, V v) {
            Preconditions.checkArgument(apply(k, v));
            return this.unfiltered.put(k, v);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            for (Entry entry : map.entrySet()) {
                Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
            }
            this.unfiltered.putAll(map);
        }

        public boolean containsKey(Object obj) {
            return (!this.unfiltered.containsKey(obj) || apply(obj, this.unfiltered.get(obj)) == null) ? null : true;
        }

        public V get(Object obj) {
            V v = this.unfiltered.get(obj);
            return (v == null || apply(obj, v) == null) ? null : v;
        }

        public boolean isEmpty() {
            return entrySet().isEmpty();
        }

        public V remove(Object obj) {
            return containsKey(obj) ? this.unfiltered.remove(obj) : null;
        }

        Collection<V> createValues() {
            return new FilteredMapValues(this, this.unfiltered, this.predicate);
        }
    }

    private static class AsMapView<K, V> extends ViewCachingAbstractMap<K, V> {
        final Function<? super K, V> function;
        private final Set<K> set;

        /* renamed from: com.google.common.collect.Maps$AsMapView$1 */
        class C10051 extends EntrySet<K, V> {
            C10051() {
            }

            Map<K, V> map() {
                return AsMapView.this;
            }

            public Iterator<Entry<K, V>> iterator() {
                return Maps.asMapEntryIterator(AsMapView.this.backingSet(), AsMapView.this.function);
            }
        }

        Set<K> backingSet() {
            return this.set;
        }

        AsMapView(Set<K> set, Function<? super K, V> function) {
            this.set = (Set) Preconditions.checkNotNull(set);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public Set<K> createKeySet() {
            return Maps.removeOnlySet(backingSet());
        }

        Collection<V> createValues() {
            return Collections2.transform(this.set, this.function);
        }

        public int size() {
            return backingSet().size();
        }

        public boolean containsKey(@Nullable Object obj) {
            return backingSet().contains(obj);
        }

        public V get(@Nullable Object obj) {
            return Collections2.safeContains(backingSet(), obj) ? this.function.apply(obj) : null;
        }

        public V remove(@Nullable Object obj) {
            return backingSet().remove(obj) ? this.function.apply(obj) : null;
        }

        public void clear() {
            backingSet().clear();
        }

        protected Set<Entry<K, V>> createEntrySet() {
            return new C10051();
        }
    }

    private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable {
        private static final long serialVersionUID = 0;
        private final BiMap<A, B> bimap;

        BiMapConverter(BiMap<A, B> biMap) {
            this.bimap = (BiMap) Preconditions.checkNotNull(biMap);
        }

        protected B doForward(A a) {
            return convert(this.bimap, a);
        }

        protected A doBackward(B b) {
            return convert(this.bimap.inverse(), b);
        }

        private static <X, Y> Y convert(BiMap<X, Y> biMap, X x) {
            biMap = biMap.get(x);
            Preconditions.checkArgument(biMap != null, "No non-null mapping present for input: %s", x);
            return biMap;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof BiMapConverter)) {
                return null;
            }
            return this.bimap.equals(((BiMapConverter) obj).bimap);
        }

        public int hashCode() {
            return this.bimap.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Maps.asConverter(");
            stringBuilder.append(this.bimap);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private enum EntryFunction implements Function<Entry<?, ?>, Object> {
        KEY {
            @Nullable
            public Object apply(Entry<?, ?> entry) {
                return entry.getKey();
            }
        },
        VALUE {
            @Nullable
            public Object apply(Entry<?, ?> entry) {
                return entry.getValue();
            }
        }
    }

    static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V> {
        final Set<Entry<K, V>> filteredEntrySet;

        private class EntrySet extends ForwardingSet<Entry<K, V>> {
            private EntrySet() {
            }

            protected Set<Entry<K, V>> delegate() {
                return FilteredEntryMap.this.filteredEntrySet;
            }

            public Iterator<Entry<K, V>> iterator() {
                return new TransformedIterator<Entry<K, V>, Entry<K, V>>(FilteredEntryMap.this.filteredEntrySet.iterator()) {
                    Entry<K, V> transform(final Entry<K, V> entry) {
                        return new ForwardingMapEntry<K, V>() {
                            protected Entry<K, V> delegate() {
                                return entry;
                            }

                            public V setValue(V v) {
                                Preconditions.checkArgument(FilteredEntryMap.this.apply(getKey(), v));
                                return super.setValue(v);
                            }
                        };
                    }
                };
            }
        }

        class KeySet extends KeySet<K, V> {
            KeySet() {
                super(FilteredEntryMap.this);
            }

            public boolean remove(Object obj) {
                if (!FilteredEntryMap.this.containsKey(obj)) {
                    return null;
                }
                FilteredEntryMap.this.unfiltered.remove(obj);
                return true;
            }

            private boolean removeIf(Predicate<? super K> predicate) {
                return Iterables.removeIf(FilteredEntryMap.this.unfiltered.entrySet(), Predicates.and(FilteredEntryMap.this.predicate, Maps.keyPredicateOnEntries(predicate)));
            }

            public boolean removeAll(Collection<?> collection) {
                return removeIf(Predicates.in(collection));
            }

            public boolean retainAll(Collection<?> collection) {
                return removeIf(Predicates.not(Predicates.in(collection)));
            }

            public Object[] toArray() {
                return Lists.newArrayList(iterator()).toArray();
            }

            public <T> T[] toArray(T[] tArr) {
                return Lists.newArrayList(iterator()).toArray(tArr);
            }
        }

        FilteredEntryMap(Map<K, V> map, Predicate<? super Entry<K, V>> predicate) {
            super(map, predicate);
            this.filteredEntrySet = Sets.filter(map.entrySet(), this.predicate);
        }

        protected Set<Entry<K, V>> createEntrySet() {
            return new EntrySet();
        }

        Set<K> createKeySet() {
            return new KeySet();
        }
    }

    static final class FilteredEntryBiMap<K, V> extends FilteredEntryMap<K, V> implements BiMap<K, V> {
        private final BiMap<V, K> inverse;

        private static <K, V> Predicate<Entry<V, K>> inversePredicate(final Predicate<? super Entry<K, V>> predicate) {
            return new Predicate<Entry<V, K>>() {
                public boolean apply(Entry<V, K> entry) {
                    return predicate.apply(Maps.immutableEntry(entry.getValue(), entry.getKey()));
                }
            };
        }

        FilteredEntryBiMap(BiMap<K, V> biMap, Predicate<? super Entry<K, V>> predicate) {
            super(biMap, predicate);
            this.inverse = new FilteredEntryBiMap(biMap.inverse(), inversePredicate(predicate), this);
        }

        private FilteredEntryBiMap(BiMap<K, V> biMap, Predicate<? super Entry<K, V>> predicate, BiMap<V, K> biMap2) {
            super(biMap, predicate);
            this.inverse = biMap2;
        }

        BiMap<K, V> unfiltered() {
            return (BiMap) this.unfiltered;
        }

        public V forcePut(@Nullable K k, @Nullable V v) {
            Preconditions.checkArgument(apply(k, v));
            return unfiltered().forcePut(k, v);
        }

        public BiMap<V, K> inverse() {
            return this.inverse;
        }

        public Set<V> values() {
            return this.inverse.keySet();
        }
    }

    @GwtIncompatible("NavigableMap")
    private static class FilteredEntryNavigableMap<K, V> extends AbstractNavigableMap<K, V> {
        private final Predicate<? super Entry<K, V>> entryPredicate;
        private final Map<K, V> filteredDelegate;
        private final NavigableMap<K, V> unfiltered;

        FilteredEntryNavigableMap(NavigableMap<K, V> navigableMap, Predicate<? super Entry<K, V>> predicate) {
            this.unfiltered = (NavigableMap) Preconditions.checkNotNull(navigableMap);
            this.entryPredicate = predicate;
            this.filteredDelegate = new FilteredEntryMap(navigableMap, predicate);
        }

        public Comparator<? super K> comparator() {
            return this.unfiltered.comparator();
        }

        public NavigableSet<K> navigableKeySet() {
            return new NavigableKeySet<K, V>(this) {
                public boolean removeAll(Collection<?> collection) {
                    return Iterators.removeIf(FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.in(collection))));
                }

                public boolean retainAll(Collection<?> collection) {
                    return Iterators.removeIf(FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(collection)))));
                }
            };
        }

        public Collection<V> values() {
            return new FilteredMapValues(this, this.unfiltered, this.entryPredicate);
        }

        Iterator<Entry<K, V>> entryIterator() {
            return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
        }

        Iterator<Entry<K, V>> descendingEntryIterator() {
            return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
        }

        public int size() {
            return this.filteredDelegate.size();
        }

        public boolean isEmpty() {
            return Iterables.any(this.unfiltered.entrySet(), this.entryPredicate) ^ 1;
        }

        @Nullable
        public V get(@Nullable Object obj) {
            return this.filteredDelegate.get(obj);
        }

        public boolean containsKey(@Nullable Object obj) {
            return this.filteredDelegate.containsKey(obj);
        }

        public V put(K k, V v) {
            return this.filteredDelegate.put(k, v);
        }

        public V remove(@Nullable Object obj) {
            return this.filteredDelegate.remove(obj);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            this.filteredDelegate.putAll(map);
        }

        public void clear() {
            this.filteredDelegate.clear();
        }

        public Set<Entry<K, V>> entrySet() {
            return this.filteredDelegate.entrySet();
        }

        public Entry<K, V> pollFirstEntry() {
            return (Entry) Iterables.removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
        }

        public Entry<K, V> pollLastEntry() {
            return (Entry) Iterables.removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
        }

        public NavigableMap<K, V> descendingMap() {
            return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            return Maps.filterEntries(this.unfiltered.subMap(k, z, k2, z2), this.entryPredicate);
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            return Maps.filterEntries(this.unfiltered.headMap(k, z), this.entryPredicate);
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            return Maps.filterEntries(this.unfiltered.tailMap(k, z), this.entryPredicate);
        }
    }

    private static class FilteredEntrySortedMap<K, V> extends FilteredEntryMap<K, V> implements SortedMap<K, V> {

        class SortedKeySet extends KeySet implements SortedSet<K> {
            SortedKeySet() {
                super();
            }

            public Comparator<? super K> comparator() {
                return FilteredEntrySortedMap.this.sortedMap().comparator();
            }

            public SortedSet<K> subSet(K k, K k2) {
                return (SortedSet) FilteredEntrySortedMap.this.subMap(k, k2).keySet();
            }

            public SortedSet<K> headSet(K k) {
                return (SortedSet) FilteredEntrySortedMap.this.headMap(k).keySet();
            }

            public SortedSet<K> tailSet(K k) {
                return (SortedSet) FilteredEntrySortedMap.this.tailMap(k).keySet();
            }

            public K first() {
                return FilteredEntrySortedMap.this.firstKey();
            }

            public K last() {
                return FilteredEntrySortedMap.this.lastKey();
            }
        }

        FilteredEntrySortedMap(SortedMap<K, V> sortedMap, Predicate<? super Entry<K, V>> predicate) {
            super(sortedMap, predicate);
        }

        SortedMap<K, V> sortedMap() {
            return (SortedMap) this.unfiltered;
        }

        public SortedSet<K> keySet() {
            return (SortedSet) super.keySet();
        }

        SortedSet<K> createKeySet() {
            return new SortedKeySet();
        }

        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        public K firstKey() {
            return keySet().iterator().next();
        }

        public K lastKey() {
            SortedMap sortedMap = sortedMap();
            while (true) {
                K lastKey = sortedMap.lastKey();
                if (apply(lastKey, this.unfiltered.get(lastKey))) {
                    return lastKey;
                }
                sortedMap = sortedMap().headMap(lastKey);
            }
        }

        public SortedMap<K, V> headMap(K k) {
            return new FilteredEntrySortedMap(sortedMap().headMap(k), this.predicate);
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return new FilteredEntrySortedMap(sortedMap().subMap(k, k2), this.predicate);
        }

        public SortedMap<K, V> tailMap(K k) {
            return new FilteredEntrySortedMap(sortedMap().tailMap(k), this.predicate);
        }
    }

    private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
        Predicate<? super K> keyPredicate;

        FilteredKeyMap(Map<K, V> map, Predicate<? super K> predicate, Predicate<? super Entry<K, V>> predicate2) {
            super(map, predicate2);
            this.keyPredicate = predicate;
        }

        protected Set<Entry<K, V>> createEntrySet() {
            return Sets.filter(this.unfiltered.entrySet(), this.predicate);
        }

        Set<K> createKeySet() {
            return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
        }

        public boolean containsKey(Object obj) {
            return (!this.unfiltered.containsKey(obj) || this.keyPredicate.apply(obj) == null) ? null : true;
        }
    }

    private static final class FilteredMapValues<K, V> extends Values<K, V> {
        Predicate<? super Entry<K, V>> predicate;
        Map<K, V> unfiltered;

        FilteredMapValues(Map<K, V> map, Map<K, V> map2, Predicate<? super Entry<K, V>> predicate) {
            super(map);
            this.unfiltered = map2;
            this.predicate = predicate;
        }

        public boolean remove(Object obj) {
            return Iterables.removeFirstMatching(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(obj)))) != null ? true : null;
        }

        private boolean removeIf(Predicate<? super V> predicate) {
            return Iterables.removeIf(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(predicate)));
        }

        public boolean removeAll(Collection<?> collection) {
            return removeIf(Predicates.in(collection));
        }

        public boolean retainAll(Collection<?> collection) {
            return removeIf(Predicates.not(Predicates.in(collection)));
        }

        public Object[] toArray() {
            return Lists.newArrayList(iterator()).toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return Lists.newArrayList(iterator()).toArray(tArr);
        }
    }

    static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
        final Map<K, ValueDifference<V>> differences;
        final Map<K, V> onBoth;
        final Map<K, V> onlyOnLeft;
        final Map<K, V> onlyOnRight;

        MapDifferenceImpl(Map<K, V> map, Map<K, V> map2, Map<K, V> map3, Map<K, ValueDifference<V>> map4) {
            this.onlyOnLeft = Maps.unmodifiableMap(map);
            this.onlyOnRight = Maps.unmodifiableMap(map2);
            this.onBoth = Maps.unmodifiableMap(map3);
            this.differences = Maps.unmodifiableMap(map4);
        }

        public boolean areEqual() {
            return this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty();
        }

        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }

        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }

        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }

        public Map<K, ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof MapDifference)) {
                return false;
            }
            MapDifference mapDifference = (MapDifference) obj;
            if (!entriesOnlyOnLeft().equals(mapDifference.entriesOnlyOnLeft()) || !entriesOnlyOnRight().equals(mapDifference.entriesOnlyOnRight()) || !entriesInCommon().equals(mapDifference.entriesInCommon()) || entriesDiffering().equals(mapDifference.entriesDiffering()) == null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hashCode(entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering());
        }

        public String toString() {
            if (areEqual()) {
                return "equal";
            }
            StringBuilder stringBuilder = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
                stringBuilder.append(": only on left=");
                stringBuilder.append(this.onlyOnLeft);
            }
            if (!this.onlyOnRight.isEmpty()) {
                stringBuilder.append(": only on right=");
                stringBuilder.append(this.onlyOnRight);
            }
            if (!this.differences.isEmpty()) {
                stringBuilder.append(": value differences=");
                stringBuilder.append(this.differences);
            }
            return stringBuilder.toString();
        }
    }

    @GwtIncompatible("NavigableMap")
    private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V> {
        private final Function<? super K, V> function;
        private final NavigableSet<K> set;

        NavigableAsMapView(NavigableSet<K> navigableSet, Function<? super K, V> function) {
            this.set = (NavigableSet) Preconditions.checkNotNull(navigableSet);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            return Maps.asMap(this.set.subSet(k, z, k2, z2), this.function);
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            return Maps.asMap(this.set.headSet(k, z), this.function);
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            return Maps.asMap(this.set.tailSet(k, z), this.function);
        }

        public Comparator<? super K> comparator() {
            return this.set.comparator();
        }

        @Nullable
        public V get(@Nullable Object obj) {
            return Collections2.safeContains(this.set, obj) ? this.function.apply(obj) : null;
        }

        public void clear() {
            this.set.clear();
        }

        Iterator<Entry<K, V>> entryIterator() {
            return Maps.asMapEntryIterator(this.set, this.function);
        }

        Iterator<Entry<K, V>> descendingEntryIterator() {
            return descendingMap().entrySet().iterator();
        }

        public NavigableSet<K> navigableKeySet() {
            return Maps.removeOnlyNavigableSet(this.set);
        }

        public int size() {
            return this.set.size();
        }

        public NavigableMap<K, V> descendingMap() {
            return Maps.asMap(this.set.descendingSet(), this.function);
        }
    }

    private static class SortedAsMapView<K, V> extends AsMapView<K, V> implements SortedMap<K, V> {
        SortedAsMapView(SortedSet<K> sortedSet, Function<? super K, V> function) {
            super(sortedSet, function);
        }

        SortedSet<K> backingSet() {
            return (SortedSet) super.backingSet();
        }

        public Comparator<? super K> comparator() {
            return backingSet().comparator();
        }

        public Set<K> keySet() {
            return Maps.removeOnlySortedSet(backingSet());
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return Maps.asMap(backingSet().subSet(k, k2), this.function);
        }

        public SortedMap<K, V> headMap(K k) {
            return Maps.asMap(backingSet().headSet(k), this.function);
        }

        public SortedMap<K, V> tailMap(K k) {
            return Maps.asMap(backingSet().tailSet(k), this.function);
        }

        public K firstKey() {
            return backingSet().first();
        }

        public K lastKey() {
            return backingSet().last();
        }
    }

    static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V> implements SortedMapDifference<K, V> {
        SortedMapDifferenceImpl(SortedMap<K, V> sortedMap, SortedMap<K, V> sortedMap2, SortedMap<K, V> sortedMap3, SortedMap<K, ValueDifference<V>> sortedMap4) {
            super(sortedMap, sortedMap2, sortedMap3, sortedMap4);
        }

        public SortedMap<K, ValueDifference<V>> entriesDiffering() {
            return (SortedMap) super.entriesDiffering();
        }

        public SortedMap<K, V> entriesInCommon() {
            return (SortedMap) super.entriesInCommon();
        }

        public SortedMap<K, V> entriesOnlyOnLeft() {
            return (SortedMap) super.entriesOnlyOnLeft();
        }

        public SortedMap<K, V> entriesOnlyOnRight() {
            return (SortedMap) super.entriesOnlyOnRight();
        }
    }

    static class TransformedEntriesMap<K, V1, V2> extends ViewCachingAbstractMap<K, V2> {
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;

        /* renamed from: com.google.common.collect.Maps$TransformedEntriesMap$1 */
        class C10131 extends EntrySet<K, V2> {
            C10131() {
            }

            Map<K, V2> map() {
                return TransformedEntriesMap.this;
            }

            public Iterator<Entry<K, V2>> iterator() {
                return Iterators.transform(TransformedEntriesMap.this.fromMap.entrySet().iterator(), Maps.asEntryToEntryFunction(TransformedEntriesMap.this.transformer));
            }
        }

        TransformedEntriesMap(Map<K, V1> map, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            this.fromMap = (Map) Preconditions.checkNotNull(map);
            this.transformer = (EntryTransformer) Preconditions.checkNotNull(entryTransformer);
        }

        public int size() {
            return this.fromMap.size();
        }

        public boolean containsKey(Object obj) {
            return this.fromMap.containsKey(obj);
        }

        public V2 get(Object obj) {
            Object obj2 = this.fromMap.get(obj);
            if (obj2 == null) {
                if (!this.fromMap.containsKey(obj)) {
                    return null;
                }
            }
            return this.transformer.transformEntry(obj, obj2);
        }

        public V2 remove(Object obj) {
            return this.fromMap.containsKey(obj) ? this.transformer.transformEntry(obj, this.fromMap.remove(obj)) : null;
        }

        public void clear() {
            this.fromMap.clear();
        }

        public Set<K> keySet() {
            return this.fromMap.keySet();
        }

        protected Set<Entry<K, V2>> createEntrySet() {
            return new C10131();
        }
    }

    static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
        protected SortedMap<K, V1> fromMap() {
            return (SortedMap) this.fromMap;
        }

        TransformedEntriesSortedMap(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            super(sortedMap, entryTransformer);
        }

        public Comparator<? super K> comparator() {
            return fromMap().comparator();
        }

        public K firstKey() {
            return fromMap().firstKey();
        }

        public SortedMap<K, V2> headMap(K k) {
            return Maps.transformEntries(fromMap().headMap(k), this.transformer);
        }

        public K lastKey() {
            return fromMap().lastKey();
        }

        public SortedMap<K, V2> subMap(K k, K k2) {
            return Maps.transformEntries(fromMap().subMap(k, k2), this.transformer);
        }

        public SortedMap<K, V2> tailMap(K k) {
            return Maps.transformEntries(fromMap().tailMap(k), this.transformer);
        }
    }

    @GwtIncompatible("NavigableMap")
    private static class TransformedEntriesNavigableMap<K, V1, V2> extends TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2> {
        TransformedEntriesNavigableMap(NavigableMap<K, V1> navigableMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            super(navigableMap, entryTransformer);
        }

        public Entry<K, V2> ceilingEntry(K k) {
            return transformEntry(fromMap().ceilingEntry(k));
        }

        public K ceilingKey(K k) {
            return fromMap().ceilingKey(k);
        }

        public NavigableSet<K> descendingKeySet() {
            return fromMap().descendingKeySet();
        }

        public NavigableMap<K, V2> descendingMap() {
            return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
        }

        public Entry<K, V2> firstEntry() {
            return transformEntry(fromMap().firstEntry());
        }

        public Entry<K, V2> floorEntry(K k) {
            return transformEntry(fromMap().floorEntry(k));
        }

        public K floorKey(K k) {
            return fromMap().floorKey(k);
        }

        public NavigableMap<K, V2> headMap(K k) {
            return headMap(k, false);
        }

        public NavigableMap<K, V2> headMap(K k, boolean z) {
            return Maps.transformEntries(fromMap().headMap(k, z), this.transformer);
        }

        public Entry<K, V2> higherEntry(K k) {
            return transformEntry(fromMap().higherEntry(k));
        }

        public K higherKey(K k) {
            return fromMap().higherKey(k);
        }

        public Entry<K, V2> lastEntry() {
            return transformEntry(fromMap().lastEntry());
        }

        public Entry<K, V2> lowerEntry(K k) {
            return transformEntry(fromMap().lowerEntry(k));
        }

        public K lowerKey(K k) {
            return fromMap().lowerKey(k);
        }

        public NavigableSet<K> navigableKeySet() {
            return fromMap().navigableKeySet();
        }

        public Entry<K, V2> pollFirstEntry() {
            return transformEntry(fromMap().pollFirstEntry());
        }

        public Entry<K, V2> pollLastEntry() {
            return transformEntry(fromMap().pollLastEntry());
        }

        public NavigableMap<K, V2> subMap(K k, boolean z, K k2, boolean z2) {
            return Maps.transformEntries(fromMap().subMap(k, z, k2, z2), this.transformer);
        }

        public NavigableMap<K, V2> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        public NavigableMap<K, V2> tailMap(K k) {
            return tailMap(k, true);
        }

        public NavigableMap<K, V2> tailMap(K k, boolean z) {
            return Maps.transformEntries(fromMap().tailMap(k, z), this.transformer);
        }

        @Nullable
        private Entry<K, V2> transformEntry(@Nullable Entry<K, V1> entry) {
            return entry == null ? null : Maps.transformEntry(this.transformer, entry);
        }

        protected NavigableMap<K, V1> fromMap() {
            return (NavigableMap) super.fromMap();
        }
    }

    private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final BiMap<? extends K, ? extends V> delegate;
        BiMap<V, K> inverse;
        final Map<K, V> unmodifiableMap;
        transient Set<V> values;

        UnmodifiableBiMap(BiMap<? extends K, ? extends V> biMap, @Nullable BiMap<V, K> biMap2) {
            this.unmodifiableMap = Collections.unmodifiableMap(biMap);
            this.delegate = biMap;
            this.inverse = biMap2;
        }

        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }

        public V forcePut(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public BiMap<V, K> inverse() {
            BiMap<V, K> biMap = this.inverse;
            if (biMap != null) {
                return biMap;
            }
            biMap = new UnmodifiableBiMap(this.delegate.inverse(), this);
            this.inverse = biMap;
            return biMap;
        }

        public Set<V> values() {
            Set<V> set = this.values;
            if (set != null) {
                return set;
            }
            set = Collections.unmodifiableSet(this.delegate.values());
            this.values = set;
            return set;
        }
    }

    static class UnmodifiableEntries<K, V> extends ForwardingCollection<Entry<K, V>> {
        private final Collection<Entry<K, V>> entries;

        UnmodifiableEntries(Collection<Entry<K, V>> collection) {
            this.entries = collection;
        }

        protected Collection<Entry<K, V>> delegate() {
            return this.entries;
        }

        public Iterator<Entry<K, V>> iterator() {
            return Maps.unmodifiableEntryIterator(this.entries.iterator());
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }
    }

    static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V> implements Set<Entry<K, V>> {
        UnmodifiableEntrySet(Set<Entry<K, V>> set) {
            super(set);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    @GwtIncompatible("NavigableMap")
    static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
        private final NavigableMap<K, V> delegate;
        private transient UnmodifiableNavigableMap<K, V> descendingMap;

        UnmodifiableNavigableMap(NavigableMap<K, V> navigableMap) {
            this.delegate = navigableMap;
        }

        UnmodifiableNavigableMap(NavigableMap<K, V> navigableMap, UnmodifiableNavigableMap<K, V> unmodifiableNavigableMap) {
            this.delegate = navigableMap;
            this.descendingMap = unmodifiableNavigableMap;
        }

        protected SortedMap<K, V> delegate() {
            return Collections.unmodifiableSortedMap(this.delegate);
        }

        public Entry<K, V> lowerEntry(K k) {
            return Maps.unmodifiableOrNull(this.delegate.lowerEntry(k));
        }

        public K lowerKey(K k) {
            return this.delegate.lowerKey(k);
        }

        public Entry<K, V> floorEntry(K k) {
            return Maps.unmodifiableOrNull(this.delegate.floorEntry(k));
        }

        public K floorKey(K k) {
            return this.delegate.floorKey(k);
        }

        public Entry<K, V> ceilingEntry(K k) {
            return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(k));
        }

        public K ceilingKey(K k) {
            return this.delegate.ceilingKey(k);
        }

        public Entry<K, V> higherEntry(K k) {
            return Maps.unmodifiableOrNull(this.delegate.higherEntry(k));
        }

        public K higherKey(K k) {
            return this.delegate.higherKey(k);
        }

        public Entry<K, V> firstEntry() {
            return Maps.unmodifiableOrNull(this.delegate.firstEntry());
        }

        public Entry<K, V> lastEntry() {
            return Maps.unmodifiableOrNull(this.delegate.lastEntry());
        }

        public final Entry<K, V> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        public final Entry<K, V> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        public NavigableMap<K, V> descendingMap() {
            NavigableMap<K, V> navigableMap = this.descendingMap;
            if (navigableMap != null) {
                return navigableMap;
            }
            NavigableMap unmodifiableNavigableMap = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this);
            this.descendingMap = unmodifiableNavigableMap;
            return unmodifiableNavigableMap;
        }

        public Set<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> navigableKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
        }

        public NavigableSet<K> descendingKeySet() {
            return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        public SortedMap<K, V> headMap(K k) {
            return headMap(k, false);
        }

        public SortedMap<K, V> tailMap(K k) {
            return tailMap(k, true);
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            return Maps.unmodifiableNavigableMap(this.delegate.subMap(k, z, k2, z2));
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            return Maps.unmodifiableNavigableMap(this.delegate.headMap(k, z));
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            return Maps.unmodifiableNavigableMap(this.delegate.tailMap(k, z));
        }
    }

    static class ValueDifferenceImpl<V> implements ValueDifference<V> {
        private final V left;
        private final V right;

        static <V> ValueDifference<V> create(@Nullable V v, @Nullable V v2) {
            return new ValueDifferenceImpl(v, v2);
        }

        private ValueDifferenceImpl(@Nullable V v, @Nullable V v2) {
            this.left = v;
            this.right = v2;
        }

        public V leftValue() {
            return this.left;
        }

        public V rightValue() {
            return this.right;
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof ValueDifference)) {
                return false;
            }
            ValueDifference valueDifference = (ValueDifference) obj;
            if (Objects.equal(this.left, valueDifference.leftValue()) && Objects.equal(this.right, valueDifference.rightValue()) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hashCode(this.left, this.right);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(this.left);
            stringBuilder.append(", ");
            stringBuilder.append(this.right);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private Maps() {
    }

    static <K> Function<Entry<K, ?>, K> keyFunction() {
        return EntryFunction.KEY;
    }

    static <V> Function<Entry<?, V>, V> valueFunction() {
        return EntryFunction.VALUE;
    }

    static <K, V> Iterator<K> keyIterator(Iterator<Entry<K, V>> it) {
        return Iterators.transform(it, keyFunction());
    }

    static <K, V> Iterator<V> valueIterator(Iterator<Entry<K, V>> it) {
        return Iterators.transform(it, valueFunction());
    }

    @GwtCompatible(serializable = true)
    @Beta
    public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
        if (map instanceof ImmutableEnumMap) {
            return (ImmutableEnumMap) map;
        }
        if (map.isEmpty()) {
            return ImmutableMap.of();
        }
        for (Entry entry : map.entrySet()) {
            Preconditions.checkNotNull(entry.getKey());
            Preconditions.checkNotNull(entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(new EnumMap(map));
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int i) {
        return new HashMap(capacity(i));
    }

    static int capacity(int i) {
        if (i >= 3) {
            return i < 1073741824 ? (int) ((((float) i) / 1061158912) + 1065353216) : Integer.MAX_VALUE;
        } else {
            CollectPreconditions.checkNonnegative(i, "expectedSize");
            return i + 1;
        }
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap(map);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return new LinkedHashMap(map);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new MapMaker().makeMap();
    }

    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap();
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> sortedMap) {
        return new TreeMap(sortedMap);
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
        return new TreeMap(comparator);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> cls) {
        return new EnumMap((Class) Preconditions.checkNotNull(cls));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
        return new EnumMap(map);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap();
    }

    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> map, Map<? extends K, ? extends V> map2) {
        if (map instanceof SortedMap) {
            return difference((SortedMap) map, (Map) map2);
        }
        return difference(map, map2, Equivalence.equals());
    }

    @Beta
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> map, Map<? extends K, ? extends V> map2, Equivalence<? super V> equivalence) {
        Preconditions.checkNotNull(equivalence);
        Map newLinkedHashMap = newLinkedHashMap();
        Map linkedHashMap = new LinkedHashMap(map2);
        Map newLinkedHashMap2 = newLinkedHashMap();
        Map newLinkedHashMap3 = newLinkedHashMap();
        doDifference(map, map2, equivalence, newLinkedHashMap, linkedHashMap, newLinkedHashMap2, newLinkedHashMap3);
        return new MapDifferenceImpl(newLinkedHashMap, linkedHashMap, newLinkedHashMap2, newLinkedHashMap3);
    }

    private static <K, V> void doDifference(Map<? extends K, ? extends V> map, Map<? extends K, ? extends V> map2, Equivalence<? super V> equivalence, Map<K, V> map3, Map<K, V> map4, Map<K, V> map5, Map<K, ValueDifference<V>> map6) {
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (map2.containsKey(key)) {
                Object remove = map4.remove(key);
                if (equivalence.equivalent(value, remove)) {
                    map5.put(key, value);
                } else {
                    map6.put(key, ValueDifferenceImpl.create(value, remove));
                }
            } else {
                map3.put(key, value);
            }
        }
    }

    private static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        if (map instanceof SortedMap) {
            return Collections.unmodifiableSortedMap((SortedMap) map);
        }
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> sortedMap, Map<? extends K, ? extends V> map) {
        Preconditions.checkNotNull(sortedMap);
        Preconditions.checkNotNull(map);
        Comparator orNaturalOrder = orNaturalOrder(sortedMap.comparator());
        SortedMap newTreeMap = newTreeMap(orNaturalOrder);
        SortedMap newTreeMap2 = newTreeMap(orNaturalOrder);
        newTreeMap2.putAll(map);
        SortedMap newTreeMap3 = newTreeMap(orNaturalOrder);
        SortedMap newTreeMap4 = newTreeMap(orNaturalOrder);
        doDifference(sortedMap, map, Equivalence.equals(), newTreeMap, newTreeMap2, newTreeMap3, newTreeMap4);
        return new SortedMapDifferenceImpl(newTreeMap, newTreeMap2, newTreeMap3, newTreeMap4);
    }

    static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
        return comparator != null ? comparator : Ordering.natural();
    }

    @Beta
    public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
        if (set instanceof SortedSet) {
            return asMap((SortedSet) set, (Function) function);
        }
        return new AsMapView(set, function);
    }

    @Beta
    public static <K, V> SortedMap<K, V> asMap(SortedSet<K> sortedSet, Function<? super K, V> function) {
        return Platform.mapsAsMapSortedSet(sortedSet, function);
    }

    static <K, V> SortedMap<K, V> asMapSortedIgnoreNavigable(SortedSet<K> sortedSet, Function<? super K, V> function) {
        return new SortedAsMapView(sortedSet, function);
    }

    @GwtIncompatible("NavigableMap")
    @Beta
    public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> navigableSet, Function<? super K, V> function) {
        return new NavigableAsMapView(navigableSet, function);
    }

    static <K, V> Iterator<Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
        return new TransformedIterator<K, Entry<K, V>>(set.iterator()) {
            Entry<K, V> transform(K k) {
                return Maps.immutableEntry(k, function.apply(k));
            }
        };
    }

    private static <E> Set<E> removeOnlySet(final Set<E> set) {
        return new ForwardingSet<E>() {
            protected Set<E> delegate() {
                return set;
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> sortedSet) {
        return new ForwardingSortedSet<E>() {
            protected SortedSet<E> delegate() {
                return sortedSet;
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }

            public SortedSet<E> headSet(E e) {
                return Maps.removeOnlySortedSet(super.headSet(e));
            }

            public SortedSet<E> subSet(E e, E e2) {
                return Maps.removeOnlySortedSet(super.subSet(e, e2));
            }

            public SortedSet<E> tailSet(E e) {
                return Maps.removeOnlySortedSet(super.tailSet(e));
            }
        };
    }

    @GwtIncompatible("NavigableSet")
    private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> navigableSet) {
        return new ForwardingNavigableSet<E>() {
            protected NavigableSet<E> delegate() {
                return navigableSet;
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }

            public SortedSet<E> headSet(E e) {
                return Maps.removeOnlySortedSet(super.headSet(e));
            }

            public SortedSet<E> subSet(E e, E e2) {
                return Maps.removeOnlySortedSet(super.subSet(e, e2));
            }

            public SortedSet<E> tailSet(E e) {
                return Maps.removeOnlySortedSet(super.tailSet(e));
            }

            public NavigableSet<E> headSet(E e, boolean z) {
                return Maps.removeOnlyNavigableSet(super.headSet(e, z));
            }

            public NavigableSet<E> tailSet(E e, boolean z) {
                return Maps.removeOnlyNavigableSet(super.tailSet(e, z));
            }

            public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
                return Maps.removeOnlyNavigableSet(super.subSet(e, z, e2, z2));
            }

            public NavigableSet<E> descendingSet() {
                return Maps.removeOnlyNavigableSet(super.descendingSet());
            }
        };
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> iterable, Function<? super K, V> function) {
        return toMap(iterable.iterator(), (Function) function);
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> it, Function<? super K, V> function) {
        Preconditions.checkNotNull(function);
        Map newLinkedHashMap = newLinkedHashMap();
        while (it.hasNext()) {
            Object next = it.next();
            newLinkedHashMap.put(next, function.apply(next));
        }
        return ImmutableMap.copyOf(newLinkedHashMap);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> iterable, Function<? super V, K> function) {
        return uniqueIndex(iterable.iterator(), (Function) function);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> it, Function<? super V, K> function) {
        Preconditions.checkNotNull(function);
        Builder builder = ImmutableMap.builder();
        while (it.hasNext()) {
            Object next = it.next();
            builder.put(function.apply(next), next);
        }
        return builder.build();
    }

    @GwtIncompatible("java.util.Properties")
    public static ImmutableMap<String, String> fromProperties(Properties properties) {
        Builder builder = ImmutableMap.builder();
        Enumeration propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String str = (String) propertyNames.nextElement();
            builder.put(str, properties.getProperty(str));
        }
        return builder.build();
    }

    @GwtCompatible(serializable = true)
    public static <K, V> Entry<K, V> immutableEntry(@Nullable K k, @Nullable V v) {
        return new ImmutableEntry(k, v);
    }

    static <K, V> Set<Entry<K, V>> unmodifiableEntrySet(Set<Entry<K, V>> set) {
        return new UnmodifiableEntrySet(Collections.unmodifiableSet(set));
    }

    static <K, V> Entry<K, V> unmodifiableEntry(final Entry<? extends K, ? extends V> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V>() {
            public K getKey() {
                return entry.getKey();
            }

            public V getValue() {
                return entry.getValue();
            }
        };
    }

    static <K, V> UnmodifiableIterator<Entry<K, V>> unmodifiableEntryIterator(final Iterator<Entry<K, V>> it) {
        return new UnmodifiableIterator<Entry<K, V>>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public Entry<K, V> next() {
                return Maps.unmodifiableEntry((Entry) it.next());
            }
        };
    }

    @Beta
    public static <A, B> Converter<A, B> asConverter(BiMap<A, B> biMap) {
        return new BiMapConverter(biMap);
    }

    public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> biMap) {
        return Synchronized.biMap(biMap, null);
    }

    public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> biMap) {
        return new UnmodifiableBiMap(biMap, null);
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> map, Function<? super V1, V2> function) {
        return transformEntries((Map) map, asEntryTransformer(function));
    }

    public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> sortedMap, Function<? super V1, V2> function) {
        return transformEntries((SortedMap) sortedMap, asEntryTransformer(function));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> navigableMap, Function<? super V1, V2> function) {
        return transformEntries((NavigableMap) navigableMap, asEntryTransformer(function));
    }

    public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> map, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        if (map instanceof SortedMap) {
            return transformEntries((SortedMap) map, (EntryTransformer) entryTransformer);
        }
        return new TransformedEntriesMap(map, entryTransformer);
    }

    public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return Platform.mapsTransformEntriesSortedMap(sortedMap, entryTransformer);
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> navigableMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return new TransformedEntriesNavigableMap(navigableMap, entryTransformer);
    }

    static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return new TransformedEntriesSortedMap(sortedMap, entryTransformer);
    }

    static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K k, V1 v1) {
                return function.apply(v1);
            }
        };
    }

    static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> entryTransformer, final K k) {
        Preconditions.checkNotNull(entryTransformer);
        return new Function<V1, V2>() {
            public V2 apply(@Nullable V1 v1) {
                return entryTransformer.transformEntry(k, v1);
            }
        };
    }

    static <K, V1, V2> Function<Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        Preconditions.checkNotNull(entryTransformer);
        return new Function<Entry<K, V1>, V2>() {
            public V2 apply(Entry<K, V1> entry) {
                return entryTransformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }

    static <V2, K, V1> Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> entryTransformer, final Entry<K, V1> entry) {
        Preconditions.checkNotNull(entryTransformer);
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V2>() {
            public K getKey() {
                return entry.getKey();
            }

            public V2 getValue() {
                return entryTransformer.transformEntry(entry.getKey(), entry.getValue());
            }
        };
    }

    static <K, V1, V2> Function<Entry<K, V1>, Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        Preconditions.checkNotNull(entryTransformer);
        return new Function<Entry<K, V1>, Entry<K, V2>>() {
            public Entry<K, V2> apply(Entry<K, V1> entry) {
                return Maps.transformEntry(entryTransformer, entry);
            }
        };
    }

    static <K> Predicate<Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> predicate) {
        return Predicates.compose(predicate, keyFunction());
    }

    static <V> Predicate<Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> predicate) {
        return Predicates.compose(predicate, valueFunction());
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> map, Predicate<? super K> predicate) {
        if (map instanceof SortedMap) {
            return filterKeys((SortedMap) map, (Predicate) predicate);
        }
        if (map instanceof BiMap) {
            return filterKeys((BiMap) map, (Predicate) predicate);
        }
        Preconditions.checkNotNull(predicate);
        Predicate keyPredicateOnEntries = keyPredicateOnEntries(predicate);
        return map instanceof AbstractFilteredMap ? filterFiltered((AbstractFilteredMap) map, keyPredicateOnEntries) : new FilteredKeyMap((Map) Preconditions.checkNotNull(map), predicate, keyPredicateOnEntries);
    }

    public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> sortedMap, Predicate<? super K> predicate) {
        return filterEntries((SortedMap) sortedMap, keyPredicateOnEntries(predicate));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> navigableMap, Predicate<? super K> predicate) {
        return filterEntries((NavigableMap) navigableMap, keyPredicateOnEntries(predicate));
    }

    public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> biMap, Predicate<? super K> predicate) {
        Preconditions.checkNotNull(predicate);
        return filterEntries((BiMap) biMap, keyPredicateOnEntries(predicate));
    }

    public static <K, V> Map<K, V> filterValues(Map<K, V> map, Predicate<? super V> predicate) {
        if (map instanceof SortedMap) {
            return filterValues((SortedMap) map, (Predicate) predicate);
        }
        if (map instanceof BiMap) {
            return filterValues((BiMap) map, (Predicate) predicate);
        }
        return filterEntries((Map) map, valuePredicateOnEntries(predicate));
    }

    public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> sortedMap, Predicate<? super V> predicate) {
        return filterEntries((SortedMap) sortedMap, valuePredicateOnEntries(predicate));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> navigableMap, Predicate<? super V> predicate) {
        return filterEntries((NavigableMap) navigableMap, valuePredicateOnEntries(predicate));
    }

    public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> biMap, Predicate<? super V> predicate) {
        return filterEntries((BiMap) biMap, valuePredicateOnEntries(predicate));
    }

    public static <K, V> Map<K, V> filterEntries(Map<K, V> map, Predicate<? super Entry<K, V>> predicate) {
        if (map instanceof SortedMap) {
            return filterEntries((SortedMap) map, (Predicate) predicate);
        }
        if (map instanceof BiMap) {
            return filterEntries((BiMap) map, (Predicate) predicate);
        }
        Preconditions.checkNotNull(predicate);
        return map instanceof AbstractFilteredMap ? filterFiltered((AbstractFilteredMap) map, (Predicate) predicate) : new FilteredEntryMap((Map) Preconditions.checkNotNull(map), predicate);
    }

    public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> sortedMap, Predicate<? super Entry<K, V>> predicate) {
        return Platform.mapsFilterSortedMap(sortedMap, predicate);
    }

    static <K, V> SortedMap<K, V> filterSortedIgnoreNavigable(SortedMap<K, V> sortedMap, Predicate<? super Entry<K, V>> predicate) {
        Preconditions.checkNotNull(predicate);
        return sortedMap instanceof FilteredEntrySortedMap ? filterFiltered((FilteredEntrySortedMap) sortedMap, (Predicate) predicate) : new FilteredEntrySortedMap((SortedMap) Preconditions.checkNotNull(sortedMap), predicate);
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> navigableMap, Predicate<? super Entry<K, V>> predicate) {
        Preconditions.checkNotNull(predicate);
        return navigableMap instanceof FilteredEntryNavigableMap ? filterFiltered((FilteredEntryNavigableMap) navigableMap, (Predicate) predicate) : new FilteredEntryNavigableMap((NavigableMap) Preconditions.checkNotNull(navigableMap), predicate);
    }

    public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> biMap, Predicate<? super Entry<K, V>> predicate) {
        Preconditions.checkNotNull(biMap);
        Preconditions.checkNotNull(predicate);
        return biMap instanceof FilteredEntryBiMap ? filterFiltered((FilteredEntryBiMap) biMap, (Predicate) predicate) : new FilteredEntryBiMap(biMap, predicate);
    }

    private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> abstractFilteredMap, Predicate<? super Entry<K, V>> predicate) {
        return new FilteredEntryMap(abstractFilteredMap.unfiltered, Predicates.and(abstractFilteredMap.predicate, predicate));
    }

    private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> filteredEntrySortedMap, Predicate<? super Entry<K, V>> predicate) {
        return new FilteredEntrySortedMap(filteredEntrySortedMap.sortedMap(), Predicates.and(filteredEntrySortedMap.predicate, predicate));
    }

    @GwtIncompatible("NavigableMap")
    private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> filteredEntryNavigableMap, Predicate<? super Entry<K, V>> predicate) {
        return new FilteredEntryNavigableMap(filteredEntryNavigableMap.unfiltered, Predicates.and(filteredEntryNavigableMap.entryPredicate, predicate));
    }

    private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> filteredEntryBiMap, Predicate<? super Entry<K, V>> predicate) {
        return new FilteredEntryBiMap(filteredEntryBiMap.unfiltered(), Predicates.and(filteredEntryBiMap.predicate, predicate));
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, V> navigableMap) {
        Preconditions.checkNotNull(navigableMap);
        if (navigableMap instanceof UnmodifiableNavigableMap) {
            return navigableMap;
        }
        return new UnmodifiableNavigableMap(navigableMap);
    }

    @Nullable
    private static <K, V> Entry<K, V> unmodifiableOrNull(@Nullable Entry<K, V> entry) {
        return entry == null ? null : unmodifiableEntry(entry);
    }

    @GwtIncompatible("NavigableMap")
    public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
        return Synchronized.navigableMap(navigableMap);
    }

    static <V> V safeGet(java.util.Map<?, V> r1, @javax.annotation.Nullable java.lang.Object r2) {
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
        com.google.common.base.Preconditions.checkNotNull(r1);
        r0 = 0;
        r1 = r1.get(r2);	 Catch:{ ClassCastException -> 0x000a, NullPointerException -> 0x0009 }
        return r1;
    L_0x0009:
        return r0;
    L_0x000a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.safeGet(java.util.Map, java.lang.Object):V");
    }

    static boolean safeContainsKey(java.util.Map<?, ?> r1, java.lang.Object r2) {
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
        com.google.common.base.Preconditions.checkNotNull(r1);
        r0 = 0;
        r1 = r1.containsKey(r2);	 Catch:{ ClassCastException -> 0x000a, NullPointerException -> 0x0009 }
        return r1;
    L_0x0009:
        return r0;
    L_0x000a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.safeContainsKey(java.util.Map, java.lang.Object):boolean");
    }

    static <V> V safeRemove(java.util.Map<?, V> r1, java.lang.Object r2) {
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
        com.google.common.base.Preconditions.checkNotNull(r1);
        r0 = 0;
        r1 = r1.remove(r2);	 Catch:{ ClassCastException -> 0x000a, NullPointerException -> 0x0009 }
        return r1;
    L_0x0009:
        return r0;
    L_0x000a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Maps.safeRemove(java.util.Map, java.lang.Object):V");
    }

    static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object obj) {
        return Iterators.contains(keyIterator(map.entrySet().iterator()), obj);
    }

    static boolean containsValueImpl(Map<?, ?> map, @Nullable Object obj) {
        return Iterators.contains(valueIterator(map.entrySet().iterator()), obj);
    }

    static <K, V> boolean containsEntryImpl(Collection<Entry<K, V>> collection, Object obj) {
        if (obj instanceof Entry) {
            return collection.contains(unmodifiableEntry((Entry) obj));
        }
        return null;
    }

    static <K, V> boolean removeEntryImpl(Collection<Entry<K, V>> collection, Object obj) {
        if (obj instanceof Entry) {
            return collection.remove(unmodifiableEntry((Entry) obj));
        }
        return null;
    }

    static boolean equalsImpl(Map<?, ?> map, Object obj) {
        if (map == obj) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return null;
        }
        return map.entrySet().equals(((Map) obj).entrySet());
    }

    static String toStringImpl(Map<?, ?> map) {
        StringBuilder newStringBuilderForCollection = Collections2.newStringBuilderForCollection(map.size());
        newStringBuilderForCollection.append('{');
        STANDARD_JOINER.appendTo(newStringBuilderForCollection, (Map) map);
        newStringBuilderForCollection.append('}');
        return newStringBuilderForCollection.toString();
    }

    static <K, V> void putAllImpl(Map<K, V> map, Map<? extends K, ? extends V> map2) {
        map2 = map2.entrySet().iterator();
        while (map2.hasNext()) {
            Entry entry = (Entry) map2.next();
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @Nullable
    static <K> K keyOrNull(@Nullable Entry<K, ?> entry) {
        return entry == null ? null : entry.getKey();
    }

    @Nullable
    static <V> V valueOrNull(@Nullable Entry<?, V> entry) {
        return entry == null ? null : entry.getValue();
    }

    static <E> ImmutableMap<E, Integer> indexMap(Collection<E> collection) {
        Builder builder = new Builder(collection.size());
        int i = 0;
        for (E put : collection) {
            int i2 = i + 1;
            builder.put(put, Integer.valueOf(i));
            i = i2;
        }
        return builder.build();
    }
}
