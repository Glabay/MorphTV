package com.google.common.reflect;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Iterator;

final class Types$WildcardTypeImpl implements WildcardType, Serializable {
    private static final long serialVersionUID = 0;
    private final ImmutableList<Type> lowerBounds;
    private final ImmutableList<Type> upperBounds;

    Types$WildcardTypeImpl(Type[] typeArr, Type[] typeArr2) {
        Types.access$200(typeArr, "lower bound for wildcard");
        Types.access$200(typeArr2, "upper bound for wildcard");
        this.lowerBounds = Types$JavaVersion.CURRENT.usedInGenericType(typeArr);
        this.upperBounds = Types$JavaVersion.CURRENT.usedInGenericType(typeArr2);
    }

    public Type[] getLowerBounds() {
        return Types.access$300(this.lowerBounds);
    }

    public Type[] getUpperBounds() {
        return Types.access$300(this.upperBounds);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof WildcardType)) {
            return false;
        }
        WildcardType wildcardType = (WildcardType) obj;
        if (this.lowerBounds.equals(Arrays.asList(wildcardType.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(wildcardType.getUpperBounds())) != null) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("?");
        Iterator it = this.lowerBounds.iterator();
        while (it.hasNext()) {
            Type type = (Type) it.next();
            stringBuilder.append(" super ");
            stringBuilder.append(Types$JavaVersion.CURRENT.typeName(type));
        }
        for (Type type2 : Types.access$600(this.upperBounds)) {
            stringBuilder.append(" extends ");
            stringBuilder.append(Types$JavaVersion.CURRENT.typeName(type2));
        }
        return stringBuilder.toString();
    }
}
