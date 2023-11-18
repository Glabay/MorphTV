package com.google.common.util.concurrent;

import com.google.common.base.Optional;
import java.util.List;

interface Futures$FutureCombiner<V, C> {
    C combine(List<Optional<V>> list);
}
