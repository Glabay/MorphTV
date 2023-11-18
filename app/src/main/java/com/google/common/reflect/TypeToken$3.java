package com.google.common.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

class TypeToken$3 extends TypeVisitor {
    final /* synthetic */ TypeToken this$0;

    TypeToken$3(TypeToken typeToken) {
        this.this$0 = typeToken;
    }

    void visitTypeVariable(TypeVariable<?> typeVariable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TypeToken.access$400(this.this$0));
        stringBuilder.append("contains a type variable and is not safe for the operation");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    void visitWildcardType(WildcardType wildcardType) {
        visit(wildcardType.getLowerBounds());
        visit(wildcardType.getUpperBounds());
    }

    void visitParameterizedType(ParameterizedType parameterizedType) {
        visit(parameterizedType.getActualTypeArguments());
        visit(parameterizedType.getOwnerType());
    }

    void visitGenericArrayType(GenericArrayType genericArrayType) {
        visit(genericArrayType.getGenericComponentType());
    }
}
