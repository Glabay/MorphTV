package com.google.common.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.concurrent.atomic.AtomicReference;

class Types$2 extends TypeVisitor {
    final /* synthetic */ AtomicReference val$result;

    Types$2(AtomicReference atomicReference) {
        this.val$result = atomicReference;
    }

    void visitTypeVariable(TypeVariable<?> typeVariable) {
        this.val$result.set(Types.access$100(typeVariable.getBounds()));
    }

    void visitWildcardType(WildcardType wildcardType) {
        this.val$result.set(Types.access$100(wildcardType.getUpperBounds()));
    }

    void visitGenericArrayType(GenericArrayType genericArrayType) {
        this.val$result.set(genericArrayType.getGenericComponentType());
    }

    void visitClass(Class<?> cls) {
        this.val$result.set(cls.getComponentType());
    }
}
