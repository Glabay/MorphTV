package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Table.Cell;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] iterationOrderColumn;
    private final int[] iterationOrderRow;
    private final ImmutableMap<R, Map<C, V>> rowMap;

    SparseImmutableTable(ImmutableList<Cell<R, C, V>> immutableList, ImmutableSet<R> immutableSet, ImmutableSet<C> immutableSet2) {
        Map indexMap = Maps.indexMap(immutableSet);
        Map newLinkedHashMap = Maps.newLinkedHashMap();
        immutableSet = immutableSet.iterator();
        while (immutableSet.hasNext()) {
            newLinkedHashMap.put(immutableSet.next(), new LinkedHashMap());
        }
        immutableSet = Maps.newLinkedHashMap();
        immutableSet2 = immutableSet2.iterator();
        while (immutableSet2.hasNext()) {
            immutableSet.put(immutableSet2.next(), new LinkedHashMap());
        }
        immutableSet2 = new int[immutableList.size()];
        int[] iArr = new int[immutableList.size()];
        for (int i = 0; i < immutableList.size(); i++) {
            Cell cell = (Cell) immutableList.get(i);
            Object rowKey = cell.getRowKey();
            Object columnKey = cell.getColumnKey();
            Object value = cell.getValue();
            immutableSet2[i] = ((Integer) indexMap.get(rowKey)).intValue();
            Map map = (Map) newLinkedHashMap.get(rowKey);
            iArr[i] = map.size();
            Object put = map.put(columnKey, value);
            if (put != null) {
                immutableSet = new StringBuilder();
                immutableSet.append("Duplicate value for row=");
                immutableSet.append(rowKey);
                immutableSet.append(", column=");
                immutableSet.append(columnKey);
                immutableSet.append(": ");
                immutableSet.append(value);
                immutableSet.append(", ");
                immutableSet.append(put);
                throw new IllegalArgumentException(immutableSet.toString());
            }
            ((Map) immutableSet.get(columnKey)).put(rowKey, value);
        }
        this.iterationOrderRow = immutableSet2;
        this.iterationOrderColumn = iArr;
        immutableList = new Builder(newLinkedHashMap.size());
        for (C c : newLinkedHashMap.entrySet()) {
            immutableList.put(c.getKey(), ImmutableMap.copyOf((Map) c.getValue()));
        }
        this.rowMap = immutableList.build();
        immutableList = new Builder(immutableSet.size());
        for (R r : immutableSet.entrySet()) {
            immutableList.put(r.getKey(), ImmutableMap.copyOf((Map) r.getValue()));
        }
        this.columnMap = immutableList.build();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    public int size() {
        return this.iterationOrderRow.length;
    }

    Cell<R, C, V> getCell(int i) {
        Entry entry = (Entry) this.rowMap.entrySet().asList().get(this.iterationOrderRow[i]);
        ImmutableMap immutableMap = (ImmutableMap) entry.getValue();
        Entry entry2 = (Entry) immutableMap.entrySet().asList().get(this.iterationOrderColumn[i]);
        return ImmutableTable.cellOf(entry.getKey(), entry2.getKey(), entry2.getValue());
    }

    V getValue(int i) {
        ImmutableMap immutableMap = (ImmutableMap) this.rowMap.values().asList().get(this.iterationOrderRow[i]);
        return immutableMap.values().asList().get(this.iterationOrderColumn[i]);
    }
}
