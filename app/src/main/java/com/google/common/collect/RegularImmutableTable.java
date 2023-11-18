package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {

    private final class CellSet extends ImmutableSet<Cell<R, C, V>> {

        /* renamed from: com.google.common.collect.RegularImmutableTable$CellSet$1 */
        class C10561 extends ImmutableAsList<Cell<R, C, V>> {
            C10561() {
            }

            public Cell<R, C, V> get(int i) {
                return RegularImmutableTable.this.getCell(i);
            }

            ImmutableCollection<Cell<R, C, V>> delegateCollection() {
                return CellSet.this;
            }
        }

        boolean isPartialView() {
            return false;
        }

        private CellSet() {
        }

        public int size() {
            return RegularImmutableTable.this.size();
        }

        public UnmodifiableIterator<Cell<R, C, V>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Cell<R, C, V>> createAsList() {
            return new C10561();
        }

        public boolean contains(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell cell = (Cell) obj;
            Object obj2 = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
            if (!(obj2 == null || obj2.equals(cell.getValue()) == null)) {
                z = true;
            }
            return z;
        }
    }

    private final class Values extends ImmutableList<V> {
        boolean isPartialView() {
            return true;
        }

        private Values() {
        }

        public int size() {
            return RegularImmutableTable.this.size();
        }

        public V get(int i) {
            return RegularImmutableTable.this.getValue(i);
        }
    }

    abstract Cell<R, C, V> getCell(int i);

    abstract V getValue(int i);

    RegularImmutableTable() {
    }

    final ImmutableSet<Cell<R, C, V>> createCellSet() {
        return isEmpty() ? ImmutableSet.of() : new CellSet();
    }

    final ImmutableCollection<V> createValues() {
        return isEmpty() ? ImmutableList.of() : new Values();
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Cell<R, C, V>> list, @Nullable final Comparator<? super R> comparator, @Nullable final Comparator<? super C> comparator2) {
        Preconditions.checkNotNull(list);
        if (!(comparator == null && comparator2 == null)) {
            Collections.sort(list, new Comparator<Cell<R, C, V>>() {
                public int compare(Cell<R, C, V> cell, Cell<R, C, V> cell2) {
                    int i = 0;
                    int compare = comparator == null ? 0 : comparator.compare(cell.getRowKey(), cell2.getRowKey());
                    if (compare != 0) {
                        return compare;
                    }
                    if (comparator2 != null) {
                        i = comparator2.compare(cell.getColumnKey(), cell2.getColumnKey());
                    }
                    return i;
                }
            });
        }
        return forCellsInternal(list, comparator, comparator2);
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Cell<R, C, V>> iterable) {
        return forCellsInternal(iterable, null, null);
    }

    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Cell<R, C, V>> iterable, @Nullable Comparator<? super R> comparator, @Nullable Comparator<? super C> comparator2) {
        Collection linkedHashSet = new LinkedHashSet();
        Collection linkedHashSet2 = new LinkedHashSet();
        ImmutableList copyOf = ImmutableList.copyOf((Iterable) iterable);
        for (Cell cell : iterable) {
            linkedHashSet.add(cell.getRowKey());
            linkedHashSet2.add(cell.getColumnKey());
        }
        iterable = comparator == null ? ImmutableSet.copyOf(linkedHashSet) : ImmutableSet.copyOf(Ordering.from((Comparator) comparator).immutableSortedCopy(linkedHashSet));
        comparator = comparator2 == null ? ImmutableSet.copyOf(linkedHashSet2) : ImmutableSet.copyOf(Ordering.from((Comparator) comparator2).immutableSortedCopy(linkedHashSet2));
        return ((long) copyOf.size()) > (((long) iterable.size()) * ((long) comparator.size())) / 2 ? new DenseImmutableTable(copyOf, iterable, comparator) : new SparseImmutableTable(copyOf, iterable, comparator);
    }
}
