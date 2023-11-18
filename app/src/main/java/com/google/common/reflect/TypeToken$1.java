package com.google.common.reflect;

import com.google.common.reflect.Invokable.MethodInvokable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class TypeToken$1 extends MethodInvokable<T> {
    final /* synthetic */ TypeToken this$0;

    TypeToken$1(TypeToken typeToken, Method method) {
        this.this$0 = typeToken;
        super(method);
    }

    Type getGenericReturnType() {
        return this.this$0.resolveType(super.getGenericReturnType()).getType();
    }

    Type[] getGenericParameterTypes() {
        return TypeToken.access$000(this.this$0, super.getGenericParameterTypes());
    }

    Type[] getGenericExceptionTypes() {
        return TypeToken.access$000(this.this$0, super.getGenericExceptionTypes());
    }

    public TypeToken<T> getOwnerType() {
        return this.this$0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getOwnerType());
        stringBuilder.append(".");
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}
