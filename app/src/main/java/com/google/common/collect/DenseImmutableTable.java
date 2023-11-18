package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    private final int[] columnCounts = new int[this.columnKeyToIndex.size()];
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] iterationOrderColumn;
    private final int[] iterationOrderRow;
    private final int[] rowCounts = new int[this.rowKeyToIndex.size()];
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final V[][] values;

    private static abstract class ImmutableArrayMap<K, V> extends IteratorBasedImmutableMap<K, V> {
        private final int size;

        /* renamed from: com.google.common.collect.DenseImmutableTable$ImmutableArrayMap$1 */
        class C08951 extends AbstractIterator<Entry<K, V>> {
            private int index = -1;
            private final int maxIndex = ImmutableArrayMap.this.keyToIndex().size();

            C08951() {
            }

            protected Entry<K, V> computeNext() {
                int i = this.index;
                while (true) {
                    this.index = i + 1;
                    if (this.index >= this.maxIndex) {
                        return (Entry) endOfData();
                    }
                    Object value = ImmutableArrayMap.this.getValue(this.index);
                    if (value != null) {
                        return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                    }
                    i = this.index;
                }
            }
        }

        @Nullable
        abstract V getValue(int i);

        abstract ImmutableMap<K, Integer> keyToIndex();

        ImmutableArrayMap(int i) {
            this.size = i;
        }

        private boolean isFull() {
            return this.size == keyToIndex().size();
        }

        K getKey(int i) {
            return keyToIndex().keySet().asList().get(i);
        }

        ImmutableSet<K> createKeySet() {
            return isFull() ? keyToIndex().keySet() : super.createKeySet();
        }

        public int size() {
            return this.size;
        }

        public V get(@Nullable Object obj) {
            Integer num = (Integer) keyToIndex().get(obj);
            if (num == null) {
                return null;
            }
            return getValue(num.intValue());
        }

        UnmodifiableIterator<Entry<K, V>> entryIterator() {
            return new C08951();
        }
    }

    private final class Column extends ImmutableArrayMap<R, V> {
        private final int columnIndex;

        boolean isPartialView() {
            return true;
        }

        Column(int i) {
            super(DenseImmutableTable.this.columnCounts[i]);
            this.columnIndex = i;
        }

        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        V getValue(int i) {
            return DenseImmutableTable.this.values[i][this.columnIndex];
        }
    }

    private final class ColumnMap extends ImmutableArrayMap<C, Map<R, V>> {
        boolean isPartialView() {
            return false;
        }

        private ColumnMap() {
            super(DenseImmutableTable.this.columnCounts.length);
        }

        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        Map<R, V> getValue(int i) {
            return new Column(i);
        }
    }

    private final class Row extends ImmutableArrayMap<C, V> {
        private final int rowIndex;

        boolean isPartialView() {
            return true;
        }

        Row(int i) {
            super(DenseImmutableTable.this.rowCounts[i]);
            this.rowIndex = i;
        }

        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        V getValue(int i) {
            return DenseImmutableTable.this.values[this.rowIndex][i];
        }
    }

    private final class RowMap extends ImmutableArrayMap<R, Map<C, V>> {
        boolean isPartialView() {
            return false;
        }

        private RowMap() {
            super(DenseImmutableTable.this.rowCounts.length);
        }

        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        Map<C, V> getValue(int i) {
            return new Row(i);
        }
    }

    DenseImmutableTable(ImmutableList<Cell<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        this.values = (Object[][]) Array.newInstance(Object.class, new int[]{immutableSet.size(), immutableSet2.size()});
        this.rowKeyToIndex = Maps.indexMap(immutableSet);
        this.columnKeyToIndex = Maps.indexMap(immutableSet2);
        immutableSet = new int[immutableList.size()];
        immutableSet2 = new int[immutableList.size()];
        for (int i = 0; i < immutableList.size(); i++) {
            Cell cell = (Cell) immutableList.get(i);
            Object rowKey = cell.getRowKey();
            Object columnKey = cell.getColumnKey();
            int intValue = ((Integer) this.rowKeyToIndex.get(rowKey)).intValue();
            int intValue2 = ((Integer) this.columnKeyToIndex.get(columnKey)).intValue();
            Preconditions.checkArgument(this.values[intValue][intValue2] == null, "duplicate key: (%s, %s)", rowKey, columnKey);
            this.values[intValue][intValue2] = cell.getValue();
            int[] iArr = this.rowCounts;
            iArr[intValue] = iArr[intValue] + 1;
            iArr = this.columnCounts;
            iArr[intValue2] = iArr[intValue2] + 1;
            immutableSet[i] = intValue;
            immutableSet2[i] = intValue2;
        }
        this.iterationOrderRow = immutableSet;
        this.iterationOrderColumn = immutableSet2;
        this.rowMap = new RowMap();
        this.columnMap = new ColumnMap();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    public V get(@Nullable Object obj, @Nullable Object obj2) {
        Integer num = (Integer) this.rowKeyToIndex.get(obj);
        Integer num2 = (Integer) this.columnKeyToIndex.get(obj2);
        if (num != null) {
            if (num2 != null) {
                return this.values[num.intValue()][num2.intValue()];
            }
        }
        return null;
    }

    public int size() {
        return this.iterationOrderRow.length;
    }

    Cell<R, C, V> getCell(int i) {
        int i2 = this.iterationOrderRow[i];
        i = this.iterationOrderColumn[i];
        return ImmutableTable.cellOf(rowKeySet().asList().get(i2), columnKeySet().asList().get(i), this.values[i2][i]);
    }

    V getValue(int i) {
        return this.values[this.iterationOrderRow[i]][this.iterationOrderColumn[i]];
    }
}
