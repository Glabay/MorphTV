package com.google.common.reflect;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

final class Types$GenericArrayTypeImpl implements GenericArrayType, Serializable {
    private static final long serialVersionUID = 0;
    private final Type componentType;

    Types$GenericArrayTypeImpl(Type type) {
        this.componentType = Types$JavaVersion.CURRENT.usedInGenericType(type);
    }

    public Type getGenericComponentType() {
        return this.componentType;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Types.toString(this.componentType));
        stringBuilder.append("[]");
        return stringBuilder.toString();
    }

    public int hashCode() {
        return this.componentType.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GenericArrayType)) {
            return null;
        }
        return Objects.equal(getGenericComponentType(), ((GenericArrayType) obj).getGenericComponentType());
    }
}
