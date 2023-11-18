package com.google.common.util.concurrent;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

class Futures$8 implements Futures$FutureCombiner<V, List<V>> {
    Futures$8() {
    }

    public List<V> combine(List<Optional<V>> list) {
        List newArrayList = Lists.newArrayList();
        for (Optional optional : list) {
            newArrayList.add(optional != null ? optional.orNull() : null);
        }
        return Collections.unmodifiableList(newArrayList);
    }
}
