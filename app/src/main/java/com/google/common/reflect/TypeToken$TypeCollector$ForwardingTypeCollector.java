package com.google.common.reflect;

import com.google.common.reflect.TypeToken.TypeCollector;

class TypeToken$TypeCollector$ForwardingTypeCollector<K> extends TypeCollector<K> {
    private final TypeCollector<K> delegate;

    TypeToken$TypeCollector$ForwardingTypeCollector(TypeCollector<K> typeCollector) {
        super(null);
        this.delegate = typeCollector;
    }

    Class<?> getRawType(K k) {
        return this.delegate.getRawType(k);
    }

    Iterable<? extends K> getInterfaces(K k) {
        return this.delegate.getInterfaces(k);
    }

    K getSuperclass(K k) {
        return this.delegate.getSuperclass(k);
    }
}
