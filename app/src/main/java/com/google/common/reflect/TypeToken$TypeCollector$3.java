package com.google.common.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken.TypeCollector;

class TypeToken$TypeCollector$3 extends TypeToken$TypeCollector$ForwardingTypeCollector<K> {
    final /* synthetic */ TypeCollector this$0;

    TypeToken$TypeCollector$3(TypeCollector typeCollector, TypeCollector typeCollector2) {
        this.this$0 = typeCollector;
        super(typeCollector2);
    }

    Iterable<? extends K> getInterfaces(K k) {
        return ImmutableSet.of();
    }

    ImmutableList<K> collectTypes(Iterable<? extends K> iterable) {
        Builder builder = ImmutableList.builder();
        for (Object next : iterable) {
            if (!getRawType(next).isInterface()) {
                builder.add(next);
            }
        }
        return super.collectTypes(builder.build());
    }
}
