package com.google.common.primitives;

import com.google.common.base.Converter;
import java.io.Serializable;

final class Ints$IntConverter extends Converter<String, Integer> implements Serializable {
    static final Ints$IntConverter INSTANCE = new Ints$IntConverter();
    private static final long serialVersionUID = 1;

    public String toString() {
        return "Ints.stringConverter()";
    }

    private Ints$IntConverter() {
    }

    protected Integer doForward(String str) {
        return Integer.decode(str);
    }

    protected String doBackward(Integer num) {
        return num.toString();
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
