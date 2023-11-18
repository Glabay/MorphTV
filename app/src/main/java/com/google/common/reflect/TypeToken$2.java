package com.google.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.reflect.Invokable.ConstructorInvokable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

class TypeToken$2 extends ConstructorInvokable<T> {
    final /* synthetic */ TypeToken this$0;

    TypeToken$2(TypeToken typeToken, Constructor constructor) {
        this.this$0 = typeToken;
        super(constructor);
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
        stringBuilder.append("(");
        stringBuilder.append(Joiner.on(", ").join(getGenericParameterTypes()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
