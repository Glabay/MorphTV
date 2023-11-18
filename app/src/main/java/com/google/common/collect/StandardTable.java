package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class StandardTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
    private static final long serialVersionUID = 0;
    @GwtTransient
    final Map<R, Map<C, V>> backingMap;
    private transient Set<C> columnKeySet;
    private transient ColumnMap columnMap;
    @GwtTransient
    final Supplier<? extends Map<C, V>> factory;
    private transient Map<R, Map<C, V>> rowMap;

    class RowMap extends ViewCachingAbstractMap<R, Map<C, V>> {

        class EntrySet extends TableSet<Entry<R, Map<C, V>>> {

            /* renamed from: com.google.common.collect.StandardTable$RowMap$EntrySet$1 */
            class C10791 implements Function<R, Map<C, V>> {
                C10791() {
                }

                public Map<C, V> apply(R r) {
                    return StandardTable.this.row(r);
                }
            }

            EntrySet() {
                super();
            }

            public Iterator<Entry<R, Map<C, V>>> iterator() {
                return Maps.asMapEntryIterator(StandardTable.this.backingMap.keySet(), new C10791());
            }

            public int size() {
                return StandardTable.this.backingMap.size();
            }

            public boolean contains(Object obj) {
                boolean z = false;
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                if (!(entry.getKey() == null || !(entry.getValue() instanceof Map) || Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry) == null)) {
                    z = true;
                }
                return z;
            }

            public boolean remove(Object obj) {
                boolean z = false;
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                if (!(entry.getKey() == null || !(entry.getValue() instanceof Map) || StandardTable.this.backingMap.entrySet().remove(entry) == null)) {
                    z = true;
                }
                return z;
            }
        }

        RowMap() {
        }

        public boolean containsKey(Object obj) {
            return StandardTable.this.containsRow(obj);
        }

        public Map<C, V> get(Object obj) {
            return StandardTable.this.containsRow(obj) ? StandardTable.this.row(obj) : null;
        }

        public Map<C, V> remove(Object obj) {
            return obj == null ? null : (Map) StandardTable.this.backingMap.remove(obj);
        }

        protected Set<Entry<R, Map<C, V>>> createEntrySet() {
            return new EntrySet();
        }
    }

    private class CellIterator implements Iterator<Cell<R, C, V>> {
        Iterator<Entry<C, V>> columnIterator;
        Entry<R, Map<C, V>> rowEntry;
        final Iterator<Entry<R, Map<C, V>>> rowIterator;

        private CellIterator() {
            this.rowIterator = StandardTable.this.backingMap.entrySet().iterator();
            this.columnIterator = Iterators.emptyModifiableIterator();
        }

        public boolean hasNext() {
            if (!this.rowIterator.hasNext()) {
                if (!this.columnIterator.hasNext()) {
                    return false;
                }
            }
            return true;
        }

        public Cell<R, C, V> next() {
            if (!this.columnIterator.hasNext()) {
                this.rowEntry = (Entry) this.rowIterator.next();
                this.columnIterator = ((Map) this.rowEntry.getValue()).entrySet().iterator();
            }
            Entry entry = (Entry) this.columnIterator.next();
            return Tables.immutableCell(this.rowEntry.getKey(), entry.getKey(), entry.getValue());
        }

        public void remove() {
            this.columnIterator.remove();
            if (((Map) this.rowEntry.getValue()).isEmpty()) {
                this.rowIterator.remove();
            }
        }
    }

    private class Column extends ViewCachingAbstractMap<R, V> {
        final C columnKey;

        private class EntrySet extends ImprovedAbstractSet<Entry<R, V>> {
            private EntrySet() {
            }

            public Iterator<Entry<R, V>> iterator() {
                return new EntrySetIterator();
            }

            public int size() {
                int i = 0;
                for (Map containsKey : StandardTable.this.backingMap.values()) {
                    if (containsKey.containsKey(Column.this.columnKey)) {
                        i++;
                    }
                }
                return i;
            }

            public boolean isEmpty() {
                return StandardTable.this.containsColumn(Column.this.columnKey) ^ 1;
            }

            public void clear() {
                Column.this.removeFromColumnIf(Predicates.alwaysTrue());
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Entry)) {
                    return null;
                }
                Entry entry = (Entry) obj;
                return StandardTable.this.containsMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Entry)) {
                    return null;
                }
                Entry entry = (Entry) obj;
                return StandardTable.this.removeMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
            }

            public boolean retainAll(Collection<?> collection) {
                return Column.this.removeFromColumnIf(Predicates.not(Predicates.in(collection)));
            }
        }

        private class EntrySetIterator extends AbstractIterator<Entry<R, V>> {
            final Iterator<Entry<R, Map<C, V>>> iterator;

            private EntrySetIterator() {
                this.iterator = StandardTable.this.backingMap.entrySet().iterator();
            }

            protected Entry<R, V> computeNext() {
                while (this.iterator.hasNext()) {
                    final Entry entry = (Entry) this.iterator.next();
                    if (((Map) entry.getValue()).containsKey(Column.this.columnKey)) {
                        return new AbstractMapEntry<R, V>() {
                            public R getKey() {
                                return entry.getKey();
                            }

                            public V getValue() {
                                return ((Map) entry.getValue()).get(Column.this.columnKey);
                            }

                            public V setValue(V v) {
                                return ((Map) entry.getValue()).put(Column.this.columnKey, Preconditions.checkNotNull(v));
                            }
                        };
                    }
                }
                return (Entry) endOfData();
            }
        }

        private class KeySet extends KeySet<R, V> {
            KeySet() {
                super(Column.this);
            }

            public boolean contains(Object obj) {
                return StandardTable.this.contains(obj, Column.this.columnKey);
            }

            public boolean remove(Object obj) {
                return StandardTable.this.remove(obj, Column.this.columnKey) != null ? true : null;
            }

            public boolean retainAll(Collection<?> collection) {
                return Column.this.removeFromColumnIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(collection))));
            }
        }

        private class Values extends Values<R, V> {
            Values() {
                super(Column.this);
            }

            public boolean remove(Object obj) {
                return (obj == null || Column.this.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.equalTo(obj))) == null) ? null : true;
            }

            public boolean removeAll(Collection<?> collection) {
                return Column.this.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.in(collection)));
            }

            public boolean retainAll(Collection<?> collection) {
                return Column.this.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(collection))));
            }
        }

        Column(C c) {
            this.columnKey = Preconditions.checkNotNull(c);
        }

        public V put(R r, V v) {
            return StandardTable.this.put(r, this.columnKey, v);
        }

        public V get(Object obj) {
            return StandardTable.this.get(obj, this.columnKey);
        }

        public boolean containsKey(Object obj) {
            return StandardTable.this.contains(obj, this.columnKey);
        }

        public V remove(Object obj) {
            return StandardTable.this.remove(obj, this.columnKey);
        }

        boolean removeFromColumnIf(Predicate<? super Entry<R, V>> predicate) {
            Iterator it = StandardTable.this.backingMap.entrySet().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                Map map = (Map) entry.getValue();
                Object obj = map.get(this.columnKey);
                if (obj != null && predicate.apply(Maps.immutableEntry(entry.getKey(), obj))) {
                    map.remove(this.columnKey);
                    z = true;
                    if (map.isEmpty()) {
                        it.remove();
                    }
                }
            }
            return z;
        }

        Set<Entry<R, V>> createEntrySet() {
            return new EntrySet();
        }

        Set<R> createKeySet() {
            return new KeySet();
        }

        Collection<V> createValues() {
            return new Values();
        }
    }

    private class ColumnKeyIterator extends AbstractIterator<C> {
        Iterator<Entry<C, V>> entryIterator;
        final Iterator<Map<C, V>> mapIterator;
        final Map<C, V> seen;

        private ColumnKeyIterator() {
            this.seen = (Map) StandardTable.this.factory.get();
            this.mapIterator = StandardTable.this.backingMap.values().iterator();
            this.entryIterator = Iterators.emptyIterator();
        }

        protected C computeNext() {
            while (true) {
                if (this.entryIterator.hasNext()) {
                    Entry entry = (Entry) this.entryIterator.next();
                    if (!this.seen.containsKey(entry.getKey())) {
                        this.seen.put(entry.getKey(), entry.getValue());
                        return entry.getKey();
                    }
                } else if (!this.mapIterator.hasNext()) {
                    return endOfData();
                } else {
                    this.entryIterator = ((Map) this.mapIterator.next()).entrySet().iterator();
                }
            }
        }
    }

    private abstract class TableSet<T> extends ImprovedAbstractSet<T> {
        private TableSet() {
        }

        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }

        public void clear() {
            StandardTable.this.backingMap.clear();
        }
    }

    private class ColumnKeySet extends TableSet<C> {
        private ColumnKeySet() {
            super();
        }

        public Iterator<C> iterator() {
            return StandardTable.this.createColumnKeyIterator();
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public boolean remove(Object obj) {
            boolean z = false;
            if (obj == null) {
                return false;
            }
            Iterator it = StandardTable.this.backingMap.values().iterator();
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (map.keySet().remove(obj)) {
                    z = true;
                    if (map.isEmpty()) {
                        it.remove();
                    }
                }
            }
            return z;
        }

        public boolean removeAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            Iterator it = StandardTable.this.backingMap.values().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (Iterators.removeAll(map.keySet().iterator(), collection)) {
                    z = true;
                    if (map.isEmpty()) {
                        it.remove();
                    }
                }
            }
            return z;
        }

        public boolean retainAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            Iterator it = StandardTable.this.backingMap.values().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (map.keySet().retainAll(collection)) {
                    z = true;
                    if (map.isEmpty()) {
                        it.remove();
                    }
                }
            }
            return z;
        }

        public boolean contains(Object obj) {
            return StandardTable.this.containsColumn(obj);
        }
    }

    private class ColumnMap extends ViewCachingAbstractMap<C, Map<R, V>> {

        class ColumnMapEntrySet extends TableSet<Entry<C, Map<R, V>>> {

            /* renamed from: com.google.common.collect.StandardTable$ColumnMap$ColumnMapEntrySet$1 */
            class C10761 implements Function<C, Map<R, V>> {
                C10761() {
                }

                public Map<R, V> apply(C c) {
                    return StandardTable.this.column(c);
                }
            }

            ColumnMapEntrySet() {
                super();
            }

            public Iterator<Entry<C, Map<R, V>>> iterator() {
                return Maps.asMapEntryIterator(StandardTable.this.columnKeySet(), new C10761());
            }

            public int size() {
                return StandardTable.this.columnKeySet().size();
            }

            public boolean contains(Object obj) {
                if (obj instanceof Entry) {
                    Entry entry = (Entry) obj;
                    if (StandardTable.this.containsColumn(entry.getKey())) {
                        return ColumnMap.this.get(entry.getKey()).equals(entry.getValue());
                    }
                }
                return null;
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return null;
                }
                StandardTable.this.removeColumn(((Entry) obj).getKey());
                return true;
            }

            public boolean removeAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                return Sets.removeAllImpl((Set) this, collection.iterator());
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                Iterator it = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Object next = it.next();
                    if (!collection.contains(Maps.immutableEntry(next, StandardTable.this.column(next)))) {
                        StandardTable.this.removeColumn(next);
                        z = true;
                    }
                }
                return z;
            }
        }

        private class ColumnMapValues extends Values<C, Map<R, V>> {
            ColumnMapValues() {
                super(ColumnMap.this);
            }

            public boolean remove(Object obj) {
                for (Entry entry : ColumnMap.this.entrySet()) {
                    if (((Map) entry.getValue()).equals(obj)) {
                        StandardTable.this.removeColumn(entry.getKey());
                        return true;
                    }
                }
                return null;
            }

            public boolean removeAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                Iterator it = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Object next = it.next();
                    if (collection.contains(StandardTable.this.column(next))) {
                        StandardTable.this.removeColumn(next);
                        z = true;
                    }
                }
                return z;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                Iterator it = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Object next = it.next();
                    if (!collection.contains(StandardTable.this.column(next))) {
                        StandardTable.this.removeColumn(next);
                        z = true;
                    }
                }
                return z;
            }
        }

        private ColumnMap() {
        }

        public Map<R, V> get(Object obj) {
            return StandardTable.this.containsColumn(obj) ? StandardTable.this.column(obj) : null;
        }

        public boolean containsKey(Object obj) {
            return StandardTable.this.containsColumn(obj);
        }

        public Map<R, V> remove(Object obj) {
            return StandardTable.this.containsColumn(obj) ? StandardTable.this.removeColumn(obj) : null;
        }

        public Set<Entry<C, Map<R, V>>> createEntrySet() {
            return new ColumnMapEntrySet();
        }

        public Set<C> keySet() {
            return StandardTable.this.columnKeySet();
        }

        Collection<Map<R, V>> createValues() {
            return new ColumnMapValues();
        }
    }

    class Row extends ViewCachingAbstractMap<C, V> {
        Map<C, V> backingRowMap;
        final R rowKey;

        private final class RowEntrySet extends EntrySet<C, V> {
            private RowEntrySet() {
            }

            Map<C, V> map() {
                return Row.this;
            }

            public int size() {
                Map backingRowMap = Row.this.backingRowMap();
                if (backingRowMap == null) {
                    return 0;
                }
                return backingRowMap.size();
            }

            public Iterator<Entry<C, V>> iterator() {
                Map backingRowMap = Row.this.backingRowMap();
                if (backingRowMap == null) {
                    return Iterators.emptyModifiableIterator();
                }
                final Iterator it = backingRowMap.entrySet().iterator();
                return new Iterator<Entry<C, V>>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public Entry<C, V> next() {
                        final Entry entry = (Entry) it.next();
                        return new ForwardingMapEntry<C, V>() {
                            protected Entry<C, V> delegate() {
                                return entry;
                            }

                            public V setValue(V v) {
                                return super.setValue(Preconditions.checkNotNull(v));
                            }

                            public boolean equals(Object obj) {
                                return standardEquals(obj);
                            }
                        };
                    }

                    public void remove() {
                        it.remove();
                        Row.this.maintainEmptyInvariant();
                    }
                };
            }
        }

        Row(R r) {
            this.rowKey = Preconditions.checkNotNull(r);
        }

        Map<C, V> backingRowMap() {
            if (this.backingRowMap != null) {
                if (!this.backingRowMap.isEmpty() || !StandardTable.this.backingMap.containsKey(this.rowKey)) {
                    return this.backingRowMap;
                }
            }
            Map<C, V> computeBackingRowMap = computeBackingRowMap();
            this.backingRowMap = computeBackingRowMap;
            return computeBackingRowMap;
        }

        Map<C, V> computeBackingRowMap() {
            return (Map) StandardTable.this.backingMap.get(this.rowKey);
        }

        void maintainEmptyInvariant() {
            if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
                StandardTable.this.backingMap.remove(this.rowKey);
                this.backingRowMap = null;
            }
        }

        public boolean containsKey(Object obj) {
            Map backingRowMap = backingRowMap();
            return (obj == null || backingRowMap == null || Maps.safeContainsKey(backingRowMap, obj) == null) ? null : true;
        }

        public V get(Object obj) {
            Map backingRowMap = backingRowMap();
            return (obj == null || backingRowMap == null) ? null : Maps.safeGet(backingRowMap, obj);
        }

        public V put(C c, V v) {
            Preconditions.checkNotNull(c);
            Preconditions.checkNotNull(v);
            if (this.backingRowMap == null || this.backingRowMap.isEmpty()) {
                return StandardTable.this.put(this.rowKey, c, v);
            }
            return this.backingRowMap.put(c, v);
        }

        public V remove(Object obj) {
            Map backingRowMap = backingRowMap();
            if (backingRowMap == null) {
                return null;
            }
            obj = Maps.safeRemove(backingRowMap, obj);
            maintainEmptyInvariant();
            return obj;
        }

        public void clear() {
            Map backingRowMap = backingRowMap();
            if (backingRowMap != null) {
                backingRowMap.clear();
            }
            maintainEmptyInvariant();
        }

        protected Set<Entry<C, V>> createEntrySet() {
            return new RowEntrySet();
        }
    }

    StandardTable(Map<R, Map<C, V>> map, Supplier<? extends Map<C, V>> supplier) {
        this.backingMap = map;
        this.factory = supplier;
    }

    public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
        return (obj == null || obj2 == null || super.contains(obj, obj2) == null) ? null : true;
    }

    public boolean containsColumn(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        for (Map safeContainsKey : this.backingMap.values()) {
            if (Maps.safeContainsKey(safeContainsKey, obj)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRow(@Nullable Object obj) {
        return (obj == null || Maps.safeContainsKey(this.backingMap, obj) == null) ? null : true;
    }

    public boolean containsValue(@Nullable Object obj) {
        return (obj == null || super.containsValue(obj) == null) ? null : true;
    }

    public V get(@Nullable Object obj, @Nullable Object obj2) {
        if (obj != null) {
            if (obj2 != null) {
                return super.get(obj, obj2);
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    public int size() {
        int i = 0;
        for (Map size : this.backingMap.values()) {
            i += size.size();
        }
        return i;
    }

    public void clear() {
        this.backingMap.clear();
    }

    private Map<C, V> getOrCreate(R r) {
        Map<C, V> map = (Map) this.backingMap.get(r);
        if (map != null) {
            return map;
        }
        map = (Map) this.factory.get();
        this.backingMap.put(r, map);
        return map;
    }

    public V put(R r, C c, V v) {
        Preconditions.checkNotNull(r);
        Preconditions.checkNotNull(c);
        Preconditions.checkNotNull(v);
        return getOrCreate(r).put(c, v);
    }

    public V remove(@Nullable Object obj, @Nullable Object obj2) {
        if (obj != null) {
            if (obj2 != null) {
                Map map = (Map) Maps.safeGet(this.backingMap, obj);
                if (map == null) {
                    return null;
                }
                obj2 = map.remove(obj2);
                if (map.isEmpty()) {
                    this.backingMap.remove(obj);
                }
                return obj2;
            }
        }
        return null;
    }

    private Map<R, V> removeColumn(Object obj) {
        Map<R, V> linkedHashMap = new LinkedHashMap();
        Iterator it = this.backingMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Object remove = ((Map) entry.getValue()).remove(obj);
            if (remove != null) {
                linkedHashMap.put(entry.getKey(), remove);
                if (((Map) entry.getValue()).isEmpty()) {
                    it.remove();
                }
            }
        }
        return linkedHashMap;
    }

    private boolean containsMapping(Object obj, Object obj2, Object obj3) {
        return (obj3 == null || obj3.equals(get(obj, obj2)) == null) ? null : true;
    }

    private boolean removeMapping(Object obj, Object obj2, Object obj3) {
        if (containsMapping(obj, obj2, obj3) == null) {
            return null;
        }
        remove(obj, obj2);
        return true;
    }

    public Set<Cell<R, C, V>> cellSet() {
        return super.cellSet();
    }

    Iterator<Cell<R, C, V>> cellIterator() {
        return new CellIterator();
    }

    public Map<C, V> row(R r) {
        return new Row(r);
    }

    public Map<R, V> column(C c) {
        return new Column(c);
    }

    public Set<R> rowKeySet() {
        return rowMap().keySet();
    }

    public Set<C> columnKeySet() {
        Set<C> set = this.columnKeySet;
        if (set != null) {
            return set;
        }
        set = new ColumnKeySet();
        this.columnKeySet = set;
        return set;
    }

    Iterator<C> createColumnKeyIterator() {
        return new ColumnKeyIterator();
    }

    public Collection<V> values() {
        return super.values();
    }

    public Map<R, Map<C, V>> rowMap() {
        Map<R, Map<C, V>> map = this.rowMap;
        if (map != null) {
            return map;
        }
        map = createRowMap();
        this.rowMap = map;
        return map;
    }

    Map<R, Map<C, V>> createRowMap() {
        return new RowMap();
    }

    public Map<C, Map<R, V>> columnMap() {
        Map<C, Map<R, V>> map = this.columnMap;
        if (map != null) {
            return map;
        }
        Map columnMap = new ColumnMap();
        this.columnMap = columnMap;
        return columnMap;
    }
}
