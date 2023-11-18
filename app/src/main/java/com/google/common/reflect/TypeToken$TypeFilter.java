package com.google.common.reflect;

import com.google.common.base.Predicate;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

enum TypeToken$TypeFilter implements Predicate<TypeToken<?>> {
    IGNORE_TYPE_VARIABLE_OR_WILDCARD {
        public boolean apply(TypeToken<?> typeToken) {
            return ((TypeToken.access$400(typeToken) instanceof TypeVariable) || (TypeToken.access$400(typeToken) instanceof WildcardType) != null) ? null : true;
        }
    },
    INTERFACE_ONLY {
        public boolean apply(TypeToken<?> typeToken) {
            return typeToken.getRawType().isInterface();
        }
    }
}
