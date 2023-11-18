package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
    private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();

    @Nullable
    public <T extends B> T getInstance(Class<T> cls) {
        return trustedGet(TypeToken.of((Class) cls));
    }

    @Nullable
    public <T extends B> T putInstance(Class<T> cls, @Nullable T t) {
        return trustedPut(TypeToken.of((Class) cls), t);
    }

    @Nullable
    public <T extends B> T getInstance(TypeToken<T> typeToken) {
        return trustedGet(typeToken.rejectTypeVariables());
    }

    @Nullable
    public <T extends B> T putInstance(TypeToken<T> typeToken, @Nullable T t) {
        return trustedPut(typeToken.rejectTypeVariables(), t);
    }

    public B put(TypeToken<? extends B> typeToken, B b) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }

    public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
        throw new UnsupportedOperationException("Please use putInstance() instead.");
    }

    public Set<Entry<TypeToken<? extends B>, B>> entrySet() {
        return MutableTypeToInstanceMap$UnmodifiableEntry.transformEntries(super.entrySet());
    }

    protected Map<TypeToken<? extends B>, B> delegate() {
        return this.backingMap;
    }

    @Nullable
    private <T extends B> T trustedPut(TypeToken<T> typeToken, @Nullable T t) {
        return this.backingMap.put(typeToken, t);
    }

    @Nullable
    private <T extends B> T trustedGet(TypeToken<T> typeToken) {
        return this.backingMap.get(typeToken);
    }
}
