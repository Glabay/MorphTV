package com.google.common.reflect;

import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Map;

class TypeToken$TypeCollector$4 extends Ordering<K> {
    final /* synthetic */ Map val$map;
    final /* synthetic */ Comparator val$valueComparator;

    TypeToken$TypeCollector$4(Comparator comparator, Map map) {
        this.val$valueComparator = comparator;
        this.val$map = map;
    }

    public int compare(K k, K k2) {
        return this.val$valueComparator.compare(this.val$map.get(k), this.val$map.get(k2));
    }
}
