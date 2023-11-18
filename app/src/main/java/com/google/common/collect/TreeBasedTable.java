package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
@Beta
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
    private static final long serialVersionUID = 0;
    private final Comparator<? super C> columnComparator;

    /* renamed from: com.google.common.collect.TreeBasedTable$1 */
    class C10891 implements Function<Map<C, V>, Iterator<C>> {
        C10891() {
        }

        public Iterator<C> apply(Map<C, V> map) {
            return map.keySet().iterator();
        }
    }

    private static class Factory<C, V> implements Supplier<TreeMap<C, V>>, Serializable {
        private static final long serialVersionUID = 0;
        final Comparator<? super C> comparator;

        Factory(Comparator<? super C> comparator) {
            this.comparator = comparator;
        }

        public TreeMap<C, V> get() {
            return new TreeMap(this.comparator);
        }
    }

    private class TreeRow extends Row implements SortedMap<C, V> {
        @Nullable
        final C lowerBound;
        @Nullable
        final C upperBound;
        transient SortedMap<C, V> wholeRow;

        TreeRow(TreeBasedTable treeBasedTable, R r) {
            this(r, null, null);
        }

        TreeRow(R r, @Nullable C c, @Nullable C c2) {
            TreeBasedTable treeBasedTable;
            super(r);
            this.lowerBound = c;
            this.upperBound = c2;
            if (!(c == null || c2 == null)) {
                if (compare(c, c2) > null) {
                    treeBasedTable = null;
                    Preconditions.checkArgument(treeBasedTable);
                }
            }
            treeBasedTable = true;
            Preconditions.checkArgument(treeBasedTable);
        }

        public SortedSet<C> keySet() {
            return new SortedKeySet(this);
        }

        public Comparator<? super C> comparator() {
            return TreeBasedTable.this.columnComparator();
        }

        int compare(Object obj, Object obj2) {
            return comparator().compare(obj, obj2);
        }

        boolean rangeContains(@Nullable Object obj) {
            return (obj == null || ((this.lowerBound != null && compare(this.lowerBound, obj) > 0) || (this.upperBound != null && compare(this.upperBound, obj) <= null))) ? null : true;
        }

        public SortedMap<C, V> subMap(C c, C c2) {
            boolean z = rangeContains(Preconditions.checkNotNull(c)) && rangeContains(Preconditions.checkNotNull(c2));
            Preconditions.checkArgument(z);
            return new TreeRow(this.rowKey, c, c2);
        }

        public SortedMap<C, V> headMap(C c) {
            Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(c)));
            return new TreeRow(this.rowKey, this.lowerBound, c);
        }

        public SortedMap<C, V> tailMap(C c) {
            Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(c)));
            return new TreeRow(this.rowKey, c, this.upperBound);
        }

        public C firstKey() {
            if (backingRowMap() != null) {
                return backingRowMap().firstKey();
            }
            throw new NoSuchElementException();
        }

        public C lastKey() {
            if (backingRowMap() != null) {
                return backingRowMap().lastKey();
            }
            throw new NoSuchElementException();
        }

        SortedMap<C, V> wholeRow() {
            if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey))) {
                this.wholeRow = (SortedMap) TreeBasedTable.this.backingMap.get(this.rowKey);
            }
            return this.wholeRow;
        }

        SortedMap<C, V> backingRowMap() {
            return (SortedMap) super.backingRowMap();
        }

        SortedMap<C, V> computeBackingRowMap() {
            SortedMap<C, V> wholeRow = wholeRow();
            if (wholeRow == null) {
                return null;
            }
            if (this.lowerBound != null) {
                wholeRow = wholeRow.tailMap(this.lowerBound);
            }
            if (this.upperBound != null) {
                wholeRow = wholeRow.headMap(this.upperBound);
            }
            return wholeRow;
        }

        void maintainEmptyInvariant() {
            if (wholeRow() != null && this.wholeRow.isEmpty()) {
                TreeBasedTable.this.backingMap.remove(this.rowKey);
                this.wholeRow = null;
                this.backingRowMap = null;
            }
        }

        public boolean containsKey(Object obj) {
            return (!rangeContains(obj) || super.containsKey(obj) == null) ? null : true;
        }

        public V put(C c, V v) {
            Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(c)));
            return super.put(c, v);
        }
    }

    public /* bridge */ /* synthetic */ Set cellSet() {
        return super.cellSet();
    }

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ Map column(Object obj) {
        return super.column(obj);
    }

    public /* bridge */ /* synthetic */ Set columnKeySet() {
        return super.columnKeySet();
    }

    public /* bridge */ /* synthetic */ Map columnMap() {
        return super.columnMap();
    }

    public /* bridge */ /* synthetic */ boolean contains(Object obj, Object obj2) {
        return super.contains(obj, obj2);
    }

    public /* bridge */ /* synthetic */ boolean containsColumn(Object obj) {
        return super.containsColumn(obj);
    }

    public /* bridge */ /* synthetic */ boolean containsRow(Object obj) {
        return super.containsRow(obj);
    }

    public /* bridge */ /* synthetic */ boolean containsValue(Object obj) {
        return super.containsValue(obj);
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

    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2, Object obj3) {
        return super.put(obj, obj2, obj3);
    }

    public /* bridge */ /* synthetic */ void putAll(Table table) {
        super.putAll(table);
    }

    public /* bridge */ /* synthetic */ Object remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public /* bridge */ /* synthetic */ Collection values() {
        return super.values();
    }

    public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
        return new TreeBasedTable(Ordering.natural(), Ordering.natural());
    }

    public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> comparator, Comparator<? super C> comparator2) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(comparator2);
        return new TreeBasedTable(comparator, comparator2);
    }

    public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> treeBasedTable) {
        TreeBasedTable<R, C, V> treeBasedTable2 = new TreeBasedTable(treeBasedTable.rowComparator(), treeBasedTable.columnComparator());
        treeBasedTable2.putAll(treeBasedTable);
        return treeBasedTable2;
    }

    TreeBasedTable(Comparator<? super R> comparator, Comparator<? super C> comparator2) {
        super(new TreeMap(comparator), new Factory(comparator2));
        this.columnComparator = comparator2;
    }

    public Comparator<? super R> rowComparator() {
        return rowKeySet().comparator();
    }

    public Comparator<? super C> columnComparator() {
        return this.columnComparator;
    }

    public SortedMap<C, V> row(R r) {
        return new TreeRow(this, r);
    }

    public SortedSet<R> rowKeySet() {
        return super.rowKeySet();
    }

    public SortedMap<R, Map<C, V>> rowMap() {
        return super.rowMap();
    }

    Iterator<C> createColumnKeyIterator() {
        final Comparator columnComparator = columnComparator();
        final Iterator mergeSorted = Iterators.mergeSorted(Iterables.transform(this.backingMap.values(), new C10891()), columnComparator);
        return new AbstractIterator<C>() {
            C lastValue;

            protected C computeNext() {
                while (mergeSorted.hasNext()) {
                    Object obj;
                    Object next = mergeSorted.next();
                    if (this.lastValue == null || columnComparator.compare(next, this.lastValue) != 0) {
                        obj = null;
                        continue;
                    } else {
                        obj = 1;
                        continue;
                    }
                    if (obj == null) {
                        this.lastValue = next;
                        return this.lastValue;
                    }
                }
                this.lastValue = null;
                return endOfData();
            }
        };
    }
}
