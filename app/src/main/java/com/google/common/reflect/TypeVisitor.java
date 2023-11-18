package com.google.common.reflect;

import com.google.common.collect.Sets;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Set;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
abstract class TypeVisitor {
    private final Set<Type> visited = Sets.newHashSet();

    void visitClass(Class<?> cls) {
    }

    void visitGenericArrayType(GenericArrayType genericArrayType) {
    }

    void visitParameterizedType(ParameterizedType parameterizedType) {
    }

    void visitTypeVariable(TypeVariable<?> typeVariable) {
    }

    void visitWildcardType(WildcardType wildcardType) {
    }

    TypeVisitor() {
    }

    public final void visit(Type... typeArr) {
        for (Object obj : typeArr) {
            if (obj != null) {
                if (this.visited.add(obj)) {
                    try {
                        if (obj instanceof TypeVariable) {
                            visitTypeVariable((TypeVariable) obj);
                        } else if (obj instanceof WildcardType) {
                            visitWildcardType((WildcardType) obj);
                        } else if (obj instanceof ParameterizedType) {
                            visitParameterizedType((ParameterizedType) obj);
                        } else if (obj instanceof Class) {
                            visitClass((Class) obj);
                        } else if (obj instanceof GenericArrayType) {
                            visitGenericArrayType((GenericArrayType) obj);
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Unknown type: ");
                            stringBuilder.append(obj);
                            throw new AssertionError(stringBuilder.toString());
                        }
                    } catch (Throwable th) {
                        this.visited.remove(obj);
                    }
                }
            }
        }
    }
}
