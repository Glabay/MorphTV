package com.google.common.reflect;

import com.google.common.base.Function;
import java.lang.reflect.Type;

class Types$1 implements Function<Type, String> {
    Types$1() {
    }

    public String apply(Type type) {
        return Types$JavaVersion.CURRENT.typeName(type);
    }
}
