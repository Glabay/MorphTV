package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ImmutableTable<R, C, V> extends AbstractTable<R, C, V> {
    private static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());

    public static final class Builder<R, C, V> {
        private final List<Cell<R, C, V>> cells = Lists.newArrayList();
        private Comparator<? super C> columnComparator;
        private Comparator<? super R> rowComparator;

        public Builder<R, C, V> orderRowsBy(Comparator<? super R> comparator) {
            this.rowComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        public Builder<R, C, V> orderColumnsBy(Comparator<? super C> comparator) {
            this.columnComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        public Builder<R, C, V> put(R r, C c, V v) {
            this.cells.add(ImmutableTable.cellOf(r, c, v));
            return this;
        }

        public Builder<R, C, V> put(Cell<? extends R, ? extends C, ? extends V> cell) {
            if (cell instanceof ImmutableCell) {
                Preconditions.checkNotNull(cell.getRowKey());
                Preconditions.checkNotNull(cell.getColumnKey());
                Preconditions.checkNotNull(cell.getValue());
                this.cells.add(cell);
            } else {
                put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }
            return this;
        }

        public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
            table = table.cellSet().iterator();
            while (table.hasNext()) {
                put((Cell) table.next());
            }
            return this;
        }

        public ImmutableTable<R, C, V> build() {
            switch (this.cells.size()) {
                case 0:
                    return ImmutableTable.of();
                case 1:
                    return new SingletonImmutableTable((Cell) Iterables.getOnlyElement(this.cells));
                default:
                    return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
            }
        }
    }

    public abstract ImmutableMap<C, Map<R, V>> columnMap();

    abstract ImmutableSet<Cell<R, C, V>> createCellSet();

    abstract ImmutableCollection<V> createValues();

    public abstract ImmutableMap<R, Map<C, V>> rowMap();

    public /* bridge */ /* synthetic */ boolean containsColumn(Object obj) {
        return super.containsColumn(obj);
    }

    public /* bridge */ /* synthetic */ boolean containsRow(Object obj) {
        return super.containsRow(obj);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ Object get(Object obj, Object obj2) {
        return super.get(obj, obj2);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <R, C, V> ImmutableTable<R, C, V> of() {
        return EMPTY;
    }

    public static <R, C, V> ImmutableTable<R, C, V> of(R r, C c, V v) {
        return new SingletonImmutableTable(r, c, v);
    }

    public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
        if (table instanceof ImmutableTable) {
            return (ImmutableTable) table;
        }
        int size = table.size();
        switch (size) {
            case 0:
                return of();
            case 1:
                Cell cell = (Cell) Iterables.getOnlyElement(table.cellSet());
                return of(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            default:
                com.google.common.collect.ImmutableSet.Builder builder = new com.google.common.collect.ImmutableSet.Builder(size);
                table = table.cellSet().iterator();
                while (table.hasNext()) {
                    Cell cell2 = (Cell) table.next();
                    builder.add(cellOf(cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue()));
                }
                return RegularImmutableTable.forCells(builder.build());
        }
    }

    public static <R, C, V> Builder<R, C, V> builder() {
        return new Builder();
    }

    static <R, C, V> Cell<R, C, V> cellOf(R r, C c, V v) {
        return Tables.immutableCell(Preconditions.checkNotNull(r), Preconditions.checkNotNull(c), Preconditions.checkNotNull(v));
    }

    ImmutableTable() {
    }

    public ImmutableSet<Cell<R, C, V>> cellSet() {
        return (ImmutableSet) super.cellSet();
    }

    final UnmodifiableIterator<Cell<R, C, V>> cellIterator() {
        throw new AssertionError("should never be called");
    }

    public ImmutableCollection<V> values() {
        return (ImmutableCollection) super.values();
    }

    final Iterator<V> valuesIterator() {
        throw new AssertionError("should never be called");
    }

    public ImmutableMap<R, V> column(C c) {
        Preconditions.checkNotNull(c);
        return (ImmutableMap) MoreObjects.firstNonNull((ImmutableMap) columnMap().get(c), ImmutableMap.of());
    }

    public ImmutableSet<C> columnKeySet() {
        return columnMap().keySet();
    }

    public ImmutableMap<C, V> row(R r) {
        Preconditions.checkNotNull(r);
        return (ImmutableMap) MoreObjects.firstNonNull((ImmutableMap) rowMap().get(r), ImmutableMap.of());
    }

    public ImmutableSet<R> rowKeySet() {
        return rowMap().keySet();
    }

    public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
        return get(obj, obj2) != null ? true : null;
    }

    public boolean containsValue(@Nullable Object obj) {
        return values().contains(obj);
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final V put(R r, C c, V v) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final V remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }
}
