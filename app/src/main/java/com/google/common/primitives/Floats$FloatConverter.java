package com.google.common.primitives;

import com.google.common.base.Converter;
import java.io.Serializable;

final class Floats$FloatConverter extends Converter<String, Float> implements Serializable {
    static final Floats$FloatConverter INSTANCE = new Floats$FloatConverter();
    private static final long serialVersionUID = 1;

    public String toString() {
        return "Floats.stringConverter()";
    }

    private Floats$FloatConverter() {
    }

    protected Float doForward(String str) {
        return Float.valueOf(str);
    }

    protected String doBackward(Float f) {
        return f.toString();
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
