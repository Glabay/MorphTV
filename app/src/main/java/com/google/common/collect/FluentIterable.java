package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class FluentIterable<E> implements Iterable<E> {
    private final Iterable<E> iterable;

    private static class FromIterableFunction<E> implements Function<Iterable<E>, FluentIterable<E>> {
        private FromIterableFunction() {
        }

        public FluentIterable<E> apply(Iterable<E> iterable) {
            return FluentIterable.from((Iterable) iterable);
        }
    }

    protected FluentIterable() {
        this.iterable = this;
    }

    FluentIterable(Iterable<E> iterable) {
        this.iterable = (Iterable) Preconditions.checkNotNull(iterable);
    }

    public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
        return iterable instanceof FluentIterable ? (FluentIterable) iterable : new FluentIterable<E>(iterable) {
            public Iterator<E> iterator() {
                return iterable.iterator();
            }
        };
    }

    @Deprecated
    public static <E> FluentIterable<E> from(FluentIterable<E> fluentIterable) {
        return (FluentIterable) Preconditions.checkNotNull(fluentIterable);
    }

    @Beta
    public static <E> FluentIterable<E> of(E[] eArr) {
        return from(Lists.newArrayList((Object[]) eArr));
    }

    public String toString() {
        return Iterables.toString(this.iterable);
    }

    public final int size() {
        return Iterables.size(this.iterable);
    }

    public final boolean contains(@Nullable Object obj) {
        return Iterables.contains(this.iterable, obj);
    }

    @CheckReturnValue
    public final FluentIterable<E> cycle() {
        return from(Iterables.cycle(this.iterable));
    }

    @CheckReturnValue
    @Beta
    public final FluentIterable<E> append(Iterable<? extends E> iterable) {
        return from(Iterables.concat(this.iterable, iterable));
    }

    @CheckReturnValue
    @Beta
    public final FluentIterable<E> append(E... eArr) {
        return from(Iterables.concat(this.iterable, Arrays.asList(eArr)));
    }

    @CheckReturnValue
    public final FluentIterable<E> filter(Predicate<? super E> predicate) {
        return from(Iterables.filter(this.iterable, (Predicate) predicate));
    }

    @CheckReturnValue
    @GwtIncompatible("Class.isInstance")
    public final <T> FluentIterable<T> filter(Class<T> cls) {
        return from(Iterables.filter(this.iterable, (Class) cls));
    }

    public final boolean anyMatch(Predicate<? super E> predicate) {
        return Iterables.any(this.iterable, predicate);
    }

    public final boolean allMatch(Predicate<? super E> predicate) {
        return Iterables.all(this.iterable, predicate);
    }

    public final Optional<E> firstMatch(Predicate<? super E> predicate) {
        return Iterables.tryFind(this.iterable, predicate);
    }

    public final <T> FluentIterable<T> transform(Function<? super E, T> function) {
        return from(Iterables.transform(this.iterable, function));
    }

    public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) {
        return from(Iterables.concat(transform(function)));
    }

    public final Optional<E> first() {
        Iterator it = this.iterable.iterator();
        return it.hasNext() ? Optional.of(it.next()) : Optional.absent();
    }

    public final Optional<E> last() {
        if (this.iterable instanceof List) {
            List list = (List) this.iterable;
            if (list.isEmpty()) {
                return Optional.absent();
            }
            return Optional.of(list.get(list.size() - 1));
        }
        Iterator it = this.iterable.iterator();
        if (!it.hasNext()) {
            return Optional.absent();
        }
        if (this.iterable instanceof SortedSet) {
            return Optional.of(((SortedSet) this.iterable).last());
        }
        Object next;
        do {
            next = it.next();
        } while (it.hasNext());
        return Optional.of(next);
    }

    @CheckReturnValue
    public final FluentIterable<E> skip(int i) {
        return from(Iterables.skip(this.iterable, i));
    }

    @CheckReturnValue
    public final FluentIterable<E> limit(int i) {
        return from(Iterables.limit(this.iterable, i));
    }

    public final boolean isEmpty() {
        return this.iterable.iterator().hasNext() ^ 1;
    }

    public final ImmutableList<E> toList() {
        return ImmutableList.copyOf(this.iterable);
    }

    public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) {
        return Ordering.from((Comparator) comparator).immutableSortedCopy(this.iterable);
    }

    public final ImmutableSet<E> toSet() {
        return ImmutableSet.copyOf(this.iterable);
    }

    public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) {
        return ImmutableSortedSet.copyOf((Comparator) comparator, this.iterable);
    }

    public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> function) {
        return Maps.toMap(this.iterable, (Function) function);
    }

    public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> function) {
        return Multimaps.index(this.iterable, (Function) function);
    }

    public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> function) {
        return Maps.uniqueIndex(this.iterable, (Function) function);
    }

    @GwtIncompatible("Array.newArray(Class, int)")
    public final E[] toArray(Class<E> cls) {
        return Iterables.toArray(this.iterable, (Class) cls);
    }

    public final <C extends Collection<? super E>> C copyInto(C c) {
        Preconditions.checkNotNull(c);
        if (this.iterable instanceof Collection) {
            c.addAll(Collections2.cast(this.iterable));
        } else {
            for (Object add : this.iterable) {
                c.add(add);
            }
        }
        return c;
    }

    @Beta
    public final String join(Joiner joiner) {
        return joiner.join((Iterable) this);
    }

    public final E get(int i) {
        return Iterables.get(this.iterable, i);
    }
}
