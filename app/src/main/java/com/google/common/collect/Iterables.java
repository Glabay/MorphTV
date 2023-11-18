package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Iterables {

    private static class ConsumingQueueIterator<T> extends AbstractIterator<T> {
        private final Queue<T> queue;

        private ConsumingQueueIterator(Queue<T> queue) {
            this.queue = queue;
        }

        public T computeNext() {
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
            r0 = r1.queue;	 Catch:{ NoSuchElementException -> 0x0007 }
            r0 = r0.remove();	 Catch:{ NoSuchElementException -> 0x0007 }
            return r0;
        L_0x0007:
            r0 = r1.endOfData();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Iterables.ConsumingQueueIterator.computeNext():T");
        }
    }

    private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
        private final Iterable<T> iterable;

        private UnmodifiableIterable(Iterable<T> iterable) {
            this.iterable = iterable;
        }

        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(this.iterable.iterator());
        }

        public String toString() {
            return this.iterable.toString();
        }
    }

    private Iterables() {
    }

    public static <T> Iterable<T> unmodifiableIterable(Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        if (!(iterable instanceof UnmodifiableIterable)) {
            if (!(iterable instanceof ImmutableCollection)) {
                return new UnmodifiableIterable(iterable);
            }
        }
        return iterable;
    }

    @Deprecated
    public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> immutableCollection) {
        return (Iterable) Preconditions.checkNotNull(immutableCollection);
    }

    public static int size(Iterable<?> iterable) {
        return iterable instanceof Collection ? ((Collection) iterable).size() : Iterators.size(iterable.iterator());
    }

    public static boolean contains(Iterable<?> iterable, @Nullable Object obj) {
        if (iterable instanceof Collection) {
            return Collections2.safeContains((Collection) iterable, obj);
        }
        return Iterators.contains(iterable.iterator(), obj);
    }

    public static boolean removeAll(Iterable<?> iterable, Collection<?> collection) {
        return iterable instanceof Collection ? ((Collection) iterable).removeAll((Collection) Preconditions.checkNotNull(collection)) : Iterators.removeAll(iterable.iterator(), collection);
    }

    public static boolean retainAll(Iterable<?> iterable, Collection<?> collection) {
        return iterable instanceof Collection ? ((Collection) iterable).retainAll((Collection) Preconditions.checkNotNull(collection)) : Iterators.retainAll(iterable.iterator(), collection);
    }

    public static <T> boolean removeIf(Iterable<T> iterable, Predicate<? super T> predicate) {
        if ((iterable instanceof RandomAccess) && (iterable instanceof List)) {
            return removeIfFromRandomAccessList((List) iterable, (Predicate) Preconditions.checkNotNull(predicate));
        }
        return Iterators.removeIf(iterable.iterator(), predicate);
    }

    private static <T> boolean removeIfFromRandomAccessList(java.util.List<T> r6, com.google.common.base.Predicate<? super T> r7) {
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
        r0 = 0;
        r1 = 0;
        r2 = 0;
    L_0x0003:
        r3 = r6.size();
        r4 = 1;
        if (r1 >= r3) goto L_0x0023;
    L_0x000a:
        r3 = r6.get(r1);
        r5 = r7.apply(r3);
        if (r5 != 0) goto L_0x0020;
    L_0x0014:
        if (r1 <= r2) goto L_0x001e;
    L_0x0016:
        r6.set(r2, r3);	 Catch:{ UnsupportedOperationException -> 0x001a }
        goto L_0x001e;
    L_0x001a:
        slowRemoveIfForRemainingElements(r6, r7, r2, r1);
        return r4;
    L_0x001e:
        r2 = r2 + 1;
    L_0x0020:
        r1 = r1 + 1;
        goto L_0x0003;
    L_0x0023:
        r7 = r6.size();
        r6 = r6.subList(r2, r7);
        r6.clear();
        if (r1 == r2) goto L_0x0031;
    L_0x0030:
        r0 = 1;
    L_0x0031:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Iterables.removeIfFromRandomAccessList(java.util.List, com.google.common.base.Predicate):boolean");
    }

    private static <T> void slowRemoveIfForRemainingElements(List<T> list, Predicate<? super T> predicate, int i, int i2) {
        for (int size = list.size() - 1; size > i2; size--) {
            if (predicate.apply(list.get(size))) {
                list.remove(size);
            }
        }
        for (i2--; i2 >= i; i2--) {
            list.remove(i2);
        }
    }

    @Nullable
    static <T> T removeFirstMatching(Iterable<T> iterable, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        iterable = iterable.iterator();
        while (iterable.hasNext()) {
            T next = iterable.next();
            if (predicate.apply(next)) {
                iterable.remove();
                return next;
            }
        }
        return null;
    }

    public static boolean elementsEqual(Iterable<?> iterable, Iterable<?> iterable2) {
        if ((iterable instanceof Collection) && (iterable2 instanceof Collection)) {
            if (((Collection) iterable).size() != ((Collection) iterable2).size()) {
                return null;
            }
        }
        return Iterators.elementsEqual(iterable.iterator(), iterable2.iterator());
    }

    public static String toString(Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }

    public static <T> T getOnlyElement(Iterable<T> iterable) {
        return Iterators.getOnlyElement(iterable.iterator());
    }

    @Nullable
    public static <T> T getOnlyElement(Iterable<? extends T> iterable, @Nullable T t) {
        return Iterators.getOnlyElement(iterable.iterator(), t);
    }

    @GwtIncompatible("Array.newInstance(Class, int)")
    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> cls) {
        iterable = toCollection(iterable);
        return iterable.toArray(ObjectArrays.newArray((Class) cls, iterable.size()));
    }

    static <T> T[] toArray(Iterable<? extends T> iterable, T[] tArr) {
        return toCollection(iterable).toArray(tArr);
    }

    static Object[] toArray(Iterable<?> iterable) {
        return toCollection(iterable).toArray();
    }

    private static <E> Collection<E> toCollection(Iterable<E> iterable) {
        return iterable instanceof Collection ? (Collection) iterable : Lists.newArrayList(iterable.iterator());
    }

    public static <T> boolean addAll(Collection<T> collection, Iterable<? extends T> iterable) {
        if (iterable instanceof Collection) {
            return collection.addAll(Collections2.cast(iterable));
        }
        return Iterators.addAll(collection, ((Iterable) Preconditions.checkNotNull(iterable)).iterator());
    }

    public static int frequency(Iterable<?> iterable, @Nullable Object obj) {
        if (iterable instanceof Multiset) {
            return ((Multiset) iterable).count(obj);
        }
        if (iterable instanceof Set) {
            return ((Set) iterable).contains(obj);
        }
        return Iterators.frequency(iterable.iterator(), obj);
    }

    public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.cycle(iterable);
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(iterable.toString());
                stringBuilder.append(" (cycled)");
                return stringBuilder.toString();
            }
        };
    }

    public static <T> Iterable<T> cycle(T... tArr) {
        return cycle(Lists.newArrayList((Object[]) tArr));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> iterable, Iterable<? extends T> iterable2) {
        return concat(ImmutableList.of(iterable, iterable2));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> iterable, Iterable<? extends T> iterable2, Iterable<? extends T> iterable3) {
        return concat(ImmutableList.of(iterable, iterable2, iterable3));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> iterable, Iterable<? extends T> iterable2, Iterable<? extends T> iterable3, Iterable<? extends T> iterable4) {
        return concat(ImmutableList.of(iterable, iterable2, iterable3, iterable4));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T>... iterableArr) {
        return concat(ImmutableList.copyOf((Object[]) iterableArr));
    }

    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> iterable) {
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.concat(Iterables.iterators(iterable));
            }
        };
    }

    private static <T> Iterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> iterable) {
        return new TransformedIterator<Iterable<? extends T>, Iterator<? extends T>>(iterable.iterator()) {
            Iterator<? extends T> transform(Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }

    public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i > 0);
        return new FluentIterable<List<T>>() {
            public Iterator<List<T>> iterator() {
                return Iterators.partition(iterable.iterator(), i);
            }
        };
    }

    public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i > 0);
        return new FluentIterable<List<T>>() {
            public Iterator<List<T>> iterator() {
                return Iterators.paddedPartition(iterable.iterator(), i);
            }
        };
    }

    public static <T> Iterable<T> filter(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(predicate);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }

    @GwtIncompatible("Class.isInstance")
    public static <T> Iterable<T> filter(final Iterable<?> iterable, final Class<T> cls) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(cls);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), cls);
            }
        };
    }

    public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.any(iterable.iterator(), predicate);
    }

    public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.all(iterable.iterator(), predicate);
    }

    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.find(iterable.iterator(), predicate);
    }

    @Nullable
    public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @Nullable T t) {
        return Iterators.find(iterable.iterator(), predicate, t);
    }

    public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.tryFind(iterable.iterator(), predicate);
    }

    public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.indexOf(iterable.iterator(), predicate);
    }

    public static <F, T> Iterable<T> transform(final Iterable<F> iterable, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(function);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.transform(iterable.iterator(), function);
            }
        };
    }

    public static <T> T get(Iterable<T> iterable, int i) {
        Preconditions.checkNotNull(iterable);
        return iterable instanceof List ? ((List) iterable).get(i) : Iterators.get(iterable.iterator(), i);
    }

    @Nullable
    public static <T> T get(Iterable<? extends T> iterable, int i, @Nullable T t) {
        Preconditions.checkNotNull(iterable);
        Iterators.checkNonnegative(i);
        if (iterable instanceof List) {
            iterable = Lists.cast(iterable);
            if (i < iterable.size()) {
                t = iterable.get(i);
            }
            return t;
        }
        iterable = iterable.iterator();
        Iterators.advance(iterable, i);
        return Iterators.getNext(iterable, t);
    }

    @Nullable
    public static <T> T getFirst(Iterable<? extends T> iterable, @Nullable T t) {
        return Iterators.getNext(iterable.iterator(), t);
    }

    public static <T> T getLast(Iterable<T> iterable) {
        if (!(iterable instanceof List)) {
            return Iterators.getLast(iterable.iterator());
        }
        List list = (List) iterable;
        if (!list.isEmpty()) {
            return getLastInNonemptyList(list);
        }
        throw new NoSuchElementException();
    }

    @Nullable
    public static <T> T getLast(Iterable<? extends T> iterable, @Nullable T t) {
        if (iterable instanceof Collection) {
            if (Collections2.cast(iterable).isEmpty()) {
                return t;
            }
            if (iterable instanceof List) {
                return getLastInNonemptyList(Lists.cast(iterable));
            }
        }
        return Iterators.getLast(iterable.iterator(), t);
    }

    private static <T> T getLastInNonemptyList(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> Iterable<T> skip(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i >= 0, "number to skip cannot be negative");
        if (!(iterable instanceof List)) {
            return new FluentIterable<T>() {
                public Iterator<T> iterator() {
                    final Iterator it = iterable.iterator();
                    Iterators.advance(it, i);
                    return new Iterator<T>() {
                        boolean atStart = true;

                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        public T next() {
                            T next = it.next();
                            this.atStart = false;
                            return next;
                        }

                        public void remove() {
                            CollectPreconditions.checkRemove(this.atStart ^ 1);
                            it.remove();
                        }
                    };
                }
            };
        }
        final List list = (List) iterable;
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return list.subList(Math.min(list.size(), i), list.size()).iterator();
            }
        };
    }

    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i >= 0, "limit is negative");
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.limit(iterable.iterator(), i);
            }
        };
    }

    public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
        if (iterable instanceof Queue) {
            return new FluentIterable<T>() {
                public String toString() {
                    return "Iterables.consumingIterable(...)";
                }

                public Iterator<T> iterator() {
                    return new ConsumingQueueIterator((Queue) iterable);
                }
            };
        }
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() {
            public String toString() {
                return "Iterables.consumingIterable(...)";
            }

            public Iterator<T> iterator() {
                return Iterators.consumingIterator(iterable.iterator());
            }
        };
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection) iterable).isEmpty();
        }
        return iterable.iterator().hasNext() ^ 1;
    }

    @Beta
    public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterable, final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterable, "iterables");
        Preconditions.checkNotNull(comparator, "comparator");
        return new UnmodifiableIterable(new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.mergeSorted(Iterables.transform(iterable, Iterables.toIterator()), comparator);
            }
        });
    }

    private static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
        return new Function<Iterable<? extends T>, Iterator<? extends T>>() {
            public Iterator<? extends T> apply(Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }
}
