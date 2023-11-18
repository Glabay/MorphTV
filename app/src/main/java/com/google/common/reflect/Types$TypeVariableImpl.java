package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class Types$TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D> {
    private final ImmutableList<Type> bounds;
    private final D genericDeclaration;
    private final String name;

    Types$TypeVariableImpl(D d, String str, Type[] typeArr) {
        Types.access$200(typeArr, "bound for type variable");
        this.genericDeclaration = (GenericDeclaration) Preconditions.checkNotNull(d);
        this.name = (String) Preconditions.checkNotNull(str);
        this.bounds = ImmutableList.copyOf(typeArr);
    }

    public Type[] getBounds() {
        return Types.access$300(this.bounds);
    }

    public D getGenericDeclaration() {
        return this.genericDeclaration;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return this.genericDeclaration.hashCode() ^ this.name.hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (Types$NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
            if (!(obj instanceof Types$TypeVariableImpl)) {
                return false;
            }
            Types$TypeVariableImpl types$TypeVariableImpl = (Types$TypeVariableImpl) obj;
            if (!this.name.equals(types$TypeVariableImpl.getName()) || !this.genericDeclaration.equals(types$TypeVariableImpl.getGenericDeclaration()) || this.bounds.equals(types$TypeVariableImpl.bounds) == null) {
                z = false;
            }
            return z;
        } else if (!(obj instanceof TypeVariable)) {
            return false;
        } else {
            TypeVariable typeVariable = (TypeVariable) obj;
            if (!this.name.equals(typeVariable.getName()) || this.genericDeclaration.equals(typeVariable.getGenericDeclaration()) == null) {
                z = false;
            }
            return z;
        }
    }
}
