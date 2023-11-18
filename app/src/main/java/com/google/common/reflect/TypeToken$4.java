package com.google.common.reflect;

import com.google.common.collect.ImmutableSet.Builder;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

class TypeToken$4 extends TypeVisitor {
    final /* synthetic */ Builder val$builder;

    TypeToken$4(Builder builder) {
        this.val$builder = builder;
    }

    void visitTypeVariable(TypeVariable<?> typeVariable) {
        visit(typeVariable.getBounds());
    }

    void visitWildcardType(WildcardType wildcardType) {
        visit(wildcardType.getUpperBounds());
    }

    void visitParameterizedType(ParameterizedType parameterizedType) {
        this.val$builder.add((Class) parameterizedType.getRawType());
    }

    void visitClass(Class<?> cls) {
        this.val$builder.add(cls);
    }

    void visitGenericArrayType(GenericArrayType genericArrayType) {
        this.val$builder.add(Types.getArrayClass(TypeToken.getRawType(genericArrayType.getGenericComponentType())));
    }
}
