package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;

@GwtCompatible
public final class Collections2 {
    static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");

    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Predicate<? super E> predicate;
        final Collection<E> unfiltered;

        FilteredCollection(Collection<E> collection, Predicate<? super E> predicate) {
            this.unfiltered = collection;
            this.predicate = predicate;
        }

        FilteredCollection<E> createCombined(Predicate<? super E> predicate) {
            return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, predicate));
        }

        public boolean add(E e) {
            Preconditions.checkArgument(this.predicate.apply(e));
            return this.unfiltered.add(e);
        }

        public boolean addAll(Collection<? extends E> collection) {
            for (Object apply : collection) {
                Preconditions.checkArgument(this.predicate.apply(apply));
            }
            return this.unfiltered.addAll(collection);
        }

        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }

        public boolean contains(@Nullable Object obj) {
            return Collections2.safeContains(this.unfiltered, obj) ? this.predicate.apply(obj) : null;
        }

        public boolean containsAll(Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }

        public boolean isEmpty() {
            return Iterables.any(this.unfiltered, this.predicate) ^ 1;
        }

        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        public boolean remove(Object obj) {
            return (!contains(obj) || this.unfiltered.remove(obj) == null) ? null : true;
        }

        public boolean removeAll(Collection<?> collection) {
            return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.in(collection)));
        }

        public boolean retainAll(Collection<?> collection) {
            return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.not(Predicates.in(collection))));
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public Object[] toArray() {
            return Lists.newArrayList(iterator()).toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return Lists.newArrayList(iterator()).toArray(tArr);
        }
    }

    private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
        final Comparator<? super E> comparator;
        final ImmutableList<E> inputList;
        final int size;

        public boolean isEmpty() {
            return false;
        }

        OrderedPermutationCollection(Iterable<E> iterable, Comparator<? super E> comparator) {
            this.inputList = Ordering.from((Comparator) comparator).immutableSortedCopy(iterable);
            this.comparator = comparator;
            this.size = calculateSize(this.inputList, comparator);
        }

        private static <E> int calculateSize(List<E> list, Comparator<? super E> comparator) {
            long j = 1;
            int i = 1;
            int i2 = 1;
            while (i < list.size()) {
                if (comparator.compare(list.get(i - 1), list.get(i)) < 0) {
                    j *= LongMath.binomial(i, i2);
                    i2 = 0;
                    if (!Collections2.isPositiveInt(j)) {
                        return Integer.MAX_VALUE;
                    }
                }
                i++;
                i2++;
            }
            j *= LongMath.binomial(i, i2);
            if (Collections2.isPositiveInt(j) == null) {
                return Integer.MAX_VALUE;
            }
            return (int) j;
        }

        public int size() {
            return this.size;
        }

        public Iterator<List<E>> iterator() {
            return new OrderedPermutationIterator(this.inputList, this.comparator);
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof List)) {
                return null;
            }
            return Collections2.isPermutation(this.inputList, (List) obj);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("orderedPermutationCollection(");
            stringBuilder.append(this.inputList);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
        final Comparator<? super E> comparator;
        List<E> nextPermutation;

        OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
            this.nextPermutation = Lists.newArrayList((Iterable) list);
            this.comparator = comparator;
        }

        protected List<E> computeNext() {
            if (this.nextPermutation == null) {
                return (List) endOfData();
            }
            List copyOf = ImmutableList.copyOf(this.nextPermutation);
            calculateNextPermutation();
            return copyOf;
        }

        void calculateNextPermutation() {
            int findNextJ = findNextJ();
            if (findNextJ == -1) {
                this.nextPermutation = null;
                return;
            }
            Collections.swap(this.nextPermutation, findNextJ, findNextL(findNextJ));
            Collections.reverse(this.nextPermutation.subList(findNextJ + 1, this.nextPermutation.size()));
        }

        int findNextJ() {
            for (int size = this.nextPermutation.size() - 2; size >= 0; size--) {
                if (this.comparator.compare(this.nextPermutation.get(size), this.nextPermutation.get(size + 1)) < 0) {
                    return size;
                }
            }
            return -1;
        }

        int findNextL(int i) {
            Object obj = this.nextPermutation.get(i);
            for (int size = this.nextPermutation.size() - 1; size > i; size--) {
                if (this.comparator.compare(obj, this.nextPermutation.get(size)) < 0) {
                    return size;
                }
            }
            throw new AssertionError("this statement should be unreachable");
        }
    }

    private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;

        public boolean isEmpty() {
            return false;
        }

        PermutationCollection(ImmutableList<E> immutableList) {
            this.inputList = immutableList;
        }

        public int size() {
            return IntMath.factorial(this.inputList.size());
        }

        public Iterator<List<E>> iterator() {
            return new PermutationIterator(this.inputList);
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof List)) {
                return null;
            }
            return Collections2.isPermutation(this.inputList, (List) obj);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("permutations(");
            stringBuilder.append(this.inputList);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
        /* renamed from: c */
        final int[] f46c;
        /* renamed from: j */
        int f47j = Integer.MAX_VALUE;
        final List<E> list;
        /* renamed from: o */
        final int[] f48o;

        PermutationIterator(List<E> list) {
            this.list = new ArrayList(list);
            list = list.size();
            this.f46c = new int[list];
            this.f48o = new int[list];
            Arrays.fill(this.f46c, 0);
            Arrays.fill(this.f48o, 1);
        }

        protected List<E> computeNext() {
            if (this.f47j <= 0) {
                return (List) endOfData();
            }
            List copyOf = ImmutableList.copyOf(this.list);
            calculateNextPermutation();
            return copyOf;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void calculateNextPermutation() {
            /*
            r6 = this;
            r0 = r6.list;
            r0 = r0.size();
            r0 = r0 + -1;
            r6.f47j = r0;
            r0 = r6.f47j;
            r1 = -1;
            if (r0 != r1) goto L_0x0010;
        L_0x000f:
            return;
        L_0x0010:
            r0 = 0;
        L_0x0011:
            r1 = r6.f46c;
            r2 = r6.f47j;
            r1 = r1[r2];
            r2 = r6.f48o;
            r3 = r6.f47j;
            r2 = r2[r3];
            r1 = r1 + r2;
            if (r1 >= 0) goto L_0x0024;
        L_0x0020:
            r6.switchDirection();
            goto L_0x0011;
        L_0x0024:
            r2 = r6.f47j;
            r2 = r2 + 1;
            if (r1 != r2) goto L_0x0035;
        L_0x002a:
            r1 = r6.f47j;
            if (r1 != 0) goto L_0x002f;
        L_0x002e:
            goto L_0x004e;
        L_0x002f:
            r0 = r0 + 1;
            r6.switchDirection();
            goto L_0x0011;
        L_0x0035:
            r2 = r6.list;
            r3 = r6.f47j;
            r4 = r6.f46c;
            r5 = r6.f47j;
            r4 = r4[r5];
            r3 = r3 - r4;
            r3 = r3 + r0;
            r4 = r6.f47j;
            r4 = r4 - r1;
            r4 = r4 + r0;
            java.util.Collections.swap(r2, r3, r4);
            r0 = r6.f46c;
            r2 = r6.f47j;
            r0[r2] = r1;
        L_0x004e:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Collections2.PermutationIterator.calculateNextPermutation():void");
        }

        void switchDirection() {
            this.f48o[this.f47j] = -this.f48o[this.f47j];
            this.f47j--;
        }
    }

    static class TransformedCollection<F, T> extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> collection, Function<? super F, ? extends T> function) {
            this.fromCollection = (Collection) Preconditions.checkNotNull(collection);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public void clear() {
            this.fromCollection.clear();
        }

        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }

        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }

        public int size() {
            return this.fromCollection.size();
        }
    }

    private static boolean isPositiveInt(long j) {
        return j >= 0 && j <= 2147483647L;
    }

    private Collections2() {
    }

    public static <E> Collection<E> filter(Collection<E> collection, Predicate<? super E> predicate) {
        if (collection instanceof FilteredCollection) {
            return ((FilteredCollection) collection).createCombined(predicate);
        }
        return new FilteredCollection((Collection) Preconditions.checkNotNull(collection), (Predicate) Preconditions.checkNotNull(predicate));
    }

    static boolean safeContains(java.util.Collection<?> r1, @javax.annotation.Nullable java.lang.Object r2) {
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
        r1 = r1.contains(r2);	 Catch:{ ClassCastException -> 0x000a, NullPointerException -> 0x0009 }
        return r1;
    L_0x0009:
        return r0;
    L_0x000a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Collections2.safeContains(java.util.Collection, java.lang.Object):boolean");
    }

    static boolean safeRemove(java.util.Collection<?> r1, @javax.annotation.Nullable java.lang.Object r2) {
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Collections2.safeRemove(java.util.Collection, java.lang.Object):boolean");
    }

    public static <F, T> Collection<T> transform(Collection<F> collection, Function<? super F, T> function) {
        return new TransformedCollection(collection, function);
    }

    static boolean containsAllImpl(Collection<?> collection, Collection<?> collection2) {
        return Iterables.all(collection2, Predicates.in(collection));
    }

    static String toStringImpl(final Collection<?> collection) {
        StringBuilder newStringBuilderForCollection = newStringBuilderForCollection(collection.size());
        newStringBuilderForCollection.append('[');
        STANDARD_JOINER.appendTo(newStringBuilderForCollection, Iterables.transform(collection, new Function<Object, Object>() {
            public Object apply(Object obj) {
                return obj == collection ? "(this Collection)" : obj;
            }
        }));
        newStringBuilderForCollection.append(']');
        return newStringBuilderForCollection.toString();
    }

    static StringBuilder newStringBuilderForCollection(int i) {
        CollectPreconditions.checkNonnegative(i, "size");
        return new StringBuilder((int) Math.min(((long) i) * 8, FileUtils.ONE_GB));
    }

    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection) iterable;
    }

    @Beta
    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> iterable) {
        return orderedPermutations(iterable, Ordering.natural());
    }

    @Beta
    public static <E> Collection<List<E>> orderedPermutations(Iterable<E> iterable, Comparator<? super E> comparator) {
        return new OrderedPermutationCollection(iterable, comparator);
    }

    @Beta
    public static <E> Collection<List<E>> permutations(Collection<E> collection) {
        return new PermutationCollection(ImmutableList.copyOf((Collection) collection));
    }

    private static boolean isPermutation(List<?> list, List<?> list2) {
        if (list.size() != list2.size()) {
            return null;
        }
        return HashMultiset.create((Iterable) list).equals(HashMultiset.create((Iterable) list2));
    }
}
