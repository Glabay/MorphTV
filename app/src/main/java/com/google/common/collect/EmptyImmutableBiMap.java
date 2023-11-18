package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class EmptyImmutableBiMap extends ImmutableBiMap<Object, Object> {
    static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();

    public Object get(@Nullable Object obj) {
        return null;
    }

    public ImmutableBiMap<Object, Object> inverse() {
        return this;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return 0;
    }

    private EmptyImmutableBiMap() {
    }

    public ImmutableSet<Entry<Object, Object>> entrySet() {
        return ImmutableSet.of();
    }

    ImmutableSet<Entry<Object, Object>> createEntrySet() {
        throw new AssertionError("should never be called");
    }

    public ImmutableSetMultimap<Object, Object> asMultimap() {
        return ImmutableSetMultimap.of();
    }

    public ImmutableSet<Object> keySet() {
        return ImmutableSet.of();
    }

    Object readResolve() {
        return INSTANCE;
    }
}
