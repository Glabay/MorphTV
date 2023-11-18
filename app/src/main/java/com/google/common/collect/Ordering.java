package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Ordering<T> implements Comparator<T> {
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;

    @VisibleForTesting
    static class ArbitraryOrdering extends Ordering<Object> {
        private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new C10441());

        /* renamed from: com.google.common.collect.Ordering$ArbitraryOrdering$1 */
        class C10441 implements Function<Object, Integer> {
            final AtomicInteger counter = new AtomicInteger(0);

            C10441() {
            }

            public Integer apply(Object obj) {
                return Integer.valueOf(this.counter.getAndIncrement());
            }
        }

        public String toString() {
            return "Ordering.arbitrary()";
        }

        ArbitraryOrdering() {
        }

        public int compare(Object obj, Object obj2) {
            if (obj == obj2) {
                return null;
            }
            int i = -1;
            if (obj == null) {
                return -1;
            }
            if (obj2 == null) {
                return 1;
            }
            int identityHashCode = identityHashCode(obj);
            int identityHashCode2 = identityHashCode(obj2);
            if (identityHashCode != identityHashCode2) {
                if (identityHashCode >= identityHashCode2) {
                    i = 1;
                }
                return i;
            }
            obj = ((Integer) this.uids.get(obj)).compareTo((Integer) this.uids.get(obj2));
            if (obj != null) {
                return obj;
            }
            throw new AssertionError();
        }

        int identityHashCode(Object obj) {
            return System.identityHashCode(obj);
        }
    }

    private static class ArbitraryOrderingHolder {
        static final Ordering<Object> ARBITRARY_ORDERING = new ArbitraryOrdering();

        private ArbitraryOrderingHolder() {
        }
    }

    @VisibleForTesting
    static class IncomparableValueException extends ClassCastException {
        private static final long serialVersionUID = 0;
        final Object value;

        IncomparableValueException(Object obj) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot compare value: ");
            stringBuilder.append(obj);
            super(stringBuilder.toString());
            this.value = obj;
        }
    }

    public abstract int compare(@Nullable T t, @Nullable T t2);

    @GwtCompatible(serializable = true)
    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> from(Comparator<T> comparator) {
        return comparator instanceof Ordering ? (Ordering) comparator : new ComparatorOrdering(comparator);
    }

    @GwtCompatible(serializable = true)
    @Deprecated
    public static <T> Ordering<T> from(Ordering<T> ordering) {
        return (Ordering) Preconditions.checkNotNull(ordering);
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(List<T> list) {
        return new ExplicitOrdering((List) list);
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(T t, T... tArr) {
        return explicit(Lists.asList(t, tArr));
    }

    @GwtCompatible(serializable = true)
    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }

    @GwtCompatible(serializable = true)
    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }

    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }

    protected Ordering() {
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
        return new ByFunctionOrdering(function, this);
    }

    <T2 extends T> Ordering<Entry<T2, ?>> onKeys() {
        return onResultOf(Maps.keyFunction());
    }

    @GwtCompatible(serializable = true)
    public <U extends T> Ordering<U> compound(Comparator<? super U> comparator) {
        return new CompoundOrdering(this, (Comparator) Preconditions.checkNotNull(comparator));
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> iterable) {
        return new CompoundOrdering(iterable);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return new LexicographicalOrdering(this);
    }

    public <E extends T> E min(Iterator<E> it) {
        E next = it.next();
        while (it.hasNext()) {
            next = min(next, it.next());
        }
        return next;
    }

    public <E extends T> E min(Iterable<E> iterable) {
        return min(iterable.iterator());
    }

    public <E extends T> E min(@Nullable E e, @Nullable E e2) {
        return compare(e, e2) <= 0 ? e : e2;
    }

    public <E extends T> E min(@Nullable E e, @Nullable E e2, @Nullable E e3, E... eArr) {
        e = min(min(e, e2), e3);
        for (Object min : eArr) {
            e = min(e, min);
        }
        return e;
    }

    public <E extends T> E max(Iterator<E> it) {
        E next = it.next();
        while (it.hasNext()) {
            next = max(next, it.next());
        }
        return next;
    }

    public <E extends T> E max(Iterable<E> iterable) {
        return max(iterable.iterator());
    }

    public <E extends T> E max(@Nullable E e, @Nullable E e2) {
        return compare(e, e2) >= 0 ? e : e2;
    }

    public <E extends T> E max(@Nullable E e, @Nullable E e2, @Nullable E e3, E... eArr) {
        e = max(max(e, e2), e3);
        for (Object max : eArr) {
            e = max(e, max);
        }
        return e;
    }

    public <E extends T> List<E> leastOf(Iterable<E> iterable, int i) {
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            if (((long) collection.size()) <= ((long) i) * 2) {
                iterable = collection.toArray();
                Arrays.sort(iterable, this);
                if (iterable.length > i) {
                    iterable = ObjectArrays.arraysCopyOf(iterable, i);
                }
                return Collections.unmodifiableList(Arrays.asList(iterable));
            }
        }
        return leastOf(iterable.iterator(), i);
    }

    public <E extends T> List<E> leastOf(Iterator<E> it, int i) {
        Preconditions.checkNotNull(it);
        CollectPreconditions.checkNonnegative(i, "k");
        if (i != 0) {
            if (it.hasNext()) {
                if (i >= 1073741823) {
                    it = Lists.newArrayList((Iterator) it);
                    Collections.sort(it, this);
                    if (it.size() > i) {
                        it.subList(i, it.size()).clear();
                    }
                    it.trimToSize();
                    return Collections.unmodifiableList(it);
                }
                Object next;
                int i2;
                int i3 = i * 2;
                Object[] objArr = new Object[i3];
                Object next2 = it.next();
                objArr[0] = next2;
                Object obj = next2;
                int i4 = 1;
                while (i4 < i && it.hasNext()) {
                    next = it.next();
                    i2 = i4 + 1;
                    objArr[i4] = next;
                    obj = max(obj, next);
                    i4 = i2;
                }
                while (it.hasNext()) {
                    next = it.next();
                    if (compare(next, obj) < 0) {
                        i2 = i4 + 1;
                        objArr[i4] = next;
                        if (i2 == i3) {
                            int i5 = i3 - 1;
                            i4 = 0;
                            int i6 = 0;
                            while (i4 < i5) {
                                i2 = partition(objArr, i4, i5, ((i4 + i5) + 1) >>> 1);
                                if (i2 <= i) {
                                    if (i2 >= i) {
                                        break;
                                    }
                                    i4 = Math.max(i2, i4 + 1);
                                    i6 = i2;
                                } else {
                                    i5 = i2 - 1;
                                }
                            }
                            next2 = objArr[i6];
                            while (true) {
                                i6++;
                                if (i6 >= i) {
                                    break;
                                }
                                next2 = max(next2, objArr[i6]);
                            }
                            obj = next2;
                            i4 = i;
                        } else {
                            i4 = i2;
                        }
                    }
                }
                Arrays.sort(objArr, 0, i4, this);
                return Collections.unmodifiableList(Arrays.asList(ObjectArrays.arraysCopyOf(objArr, Math.min(i4, i))));
            }
        }
        return ImmutableList.of();
    }

    private <E extends T> int partition(E[] eArr, int i, int i2, int i3) {
        Object obj = eArr[i3];
        eArr[i3] = eArr[i2];
        eArr[i2] = obj;
        i3 = i;
        while (i < i2) {
            if (compare(eArr[i], obj) < 0) {
                ObjectArrays.swap(eArr, i3, i);
                i3++;
            }
            i++;
        }
        ObjectArrays.swap(eArr, i2, i3);
        return i3;
    }

    public <E extends T> List<E> greatestOf(Iterable<E> iterable, int i) {
        return reverse().leastOf((Iterable) iterable, i);
    }

    public <E extends T> List<E> greatestOf(Iterator<E> it, int i) {
        return reverse().leastOf((Iterator) it, i);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
        Object[] toArray = Iterables.toArray(iterable);
        Arrays.sort(toArray, this);
        return Lists.newArrayList(Arrays.asList(toArray));
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) {
        Object[] toArray = Iterables.toArray(iterable);
        for (Object checkNotNull : toArray) {
            Preconditions.checkNotNull(checkNotNull);
        }
        Arrays.sort(toArray, this);
        return ImmutableList.asImmutableList(toArray);
    }

    public boolean isOrdered(Iterable<? extends T> iterable) {
        iterable = iterable.iterator();
        if (iterable.hasNext()) {
            Object next = iterable.next();
            while (iterable.hasNext()) {
                Object next2 = iterable.next();
                if (compare(next, next2) > 0) {
                    return null;
                }
                next = next2;
            }
        }
        return true;
    }

    public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
        iterable = iterable.iterator();
        if (iterable.hasNext()) {
            Object next = iterable.next();
            while (iterable.hasNext()) {
                Object next2 = iterable.next();
                if (compare(next, next2) >= 0) {
                    return null;
                }
                next = next2;
            }
        }
        return true;
    }

    public int binarySearch(List<? extends T> list, @Nullable T t) {
        return Collections.binarySearch(list, t, this);
    }
}
