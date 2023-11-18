package com.google.common.primitives;

import com.google.common.base.Converter;
import java.io.Serializable;

final class Doubles$DoubleConverter extends Converter<String, Double> implements Serializable {
    static final Doubles$DoubleConverter INSTANCE = new Doubles$DoubleConverter();
    private static final long serialVersionUID = 1;

    public String toString() {
        return "Doubles.stringConverter()";
    }

    private Doubles$DoubleConverter() {
    }

    protected Double doForward(String str) {
        return Double.valueOf(str);
    }

    protected String doBackward(Double d) {
        return d.toString();
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
