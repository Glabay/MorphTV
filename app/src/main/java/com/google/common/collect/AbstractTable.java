package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Table.Cell;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractTable<R, C, V> implements Table<R, C, V> {
    private transient Set<Cell<R, C, V>> cellSet;
    private transient Collection<V> values;

    class CellSet extends AbstractSet<Cell<R, C, V>> {
        CellSet() {
        }

        public boolean contains(Object obj) {
            boolean z = false;
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell cell = (Cell) obj;
            Map map = (Map) Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            if (!(map == null || Collections2.safeContains(map.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())) == null)) {
                z = true;
            }
            return z;
        }

        public boolean remove(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell cell = (Cell) obj;
            Map map = (Map) Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            if (!(map == null || Collections2.safeRemove(map.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())) == null)) {
                z = true;
            }
            return z;
        }

        public void clear() {
            AbstractTable.this.clear();
        }

        public Iterator<Cell<R, C, V>> iterator() {
            return AbstractTable.this.cellIterator();
        }

        public int size() {
            return AbstractTable.this.size();
        }
    }

    class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            return AbstractTable.this.valuesIterator();
        }

        public boolean contains(Object obj) {
            return AbstractTable.this.containsValue(obj);
        }

        public void clear() {
            AbstractTable.this.clear();
        }

        public int size() {
            return AbstractTable.this.size();
        }
    }

    abstract Iterator<Cell<R, C, V>> cellIterator();

    AbstractTable() {
    }

    public boolean containsRow(@Nullable Object obj) {
        return Maps.safeContainsKey(rowMap(), obj);
    }

    public boolean containsColumn(@Nullable Object obj) {
        return Maps.safeContainsKey(columnMap(), obj);
    }

    public Set<R> rowKeySet() {
        return rowMap().keySet();
    }

    public Set<C> columnKeySet() {
        return columnMap().keySet();
    }

    public boolean containsValue(@Nullable Object obj) {
        for (Map containsValue : rowMap().values()) {
            if (containsValue.containsValue(obj)) {
                return true;
            }
        }
        return null;
    }

    public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
        Map map = (Map) Maps.safeGet(rowMap(), obj);
        return (map == null || Maps.safeContainsKey(map, obj2) == null) ? null : true;
    }

    public V get(@Nullable Object obj, @Nullable Object obj2) {
        Map map = (Map) Maps.safeGet(rowMap(), obj);
        if (map == null) {
            return null;
        }
        return Maps.safeGet(map, obj2);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        Iterators.clear(cellSet().iterator());
    }

    public V remove(@Nullable Object obj, @Nullable Object obj2) {
        Map map = (Map) Maps.safeGet(rowMap(), obj);
        if (map == null) {
            return null;
        }
        return Maps.safeRemove(map, obj2);
    }

    public V put(R r, C c, V v) {
        return row(r).put(c, v);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        table = table.cellSet().iterator();
        while (table.hasNext()) {
            Cell cell = (Cell) table.next();
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    public Set<Cell<R, C, V>> cellSet() {
        Set<Cell<R, C, V>> set = this.cellSet;
        if (set != null) {
            return set;
        }
        set = createCellSet();
        this.cellSet = set;
        return set;
    }

    Set<Cell<R, C, V>> createCellSet() {
        return new CellSet();
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
        return new Values();
    }

    Iterator<V> valuesIterator() {
        return new TransformedIterator<Cell<R, C, V>, V>(cellSet().iterator()) {
            V transform(Cell<R, C, V> cell) {
                return cell.getValue();
            }
        };
    }

    public boolean equals(@Nullable Object obj) {
        return Tables.equalsImpl(this, obj);
    }

    public int hashCode() {
        return cellSet().hashCode();
    }

    public String toString() {
        return rowMap().toString();
    }
}
