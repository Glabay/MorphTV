package org.apache.commons.lang3.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.apache.commons.lang3.Validate;

public abstract class TypeLiteral<T> implements Typed<T> {
    /* renamed from: T */
    private static final TypeVariable<Class<TypeLiteral>> f66T = TypeLiteral.class.getTypeParameters()[0];
    private final String toString = String.format("%s<%s>", new Object[]{TypeLiteral.class.getSimpleName(), TypeUtils.toString(this.value)});
    public final Type value = ((Type) Validate.notNull(TypeUtils.getTypeArguments(getClass(), TypeLiteral.class).get(f66T), "%s does not assign type parameter %s", getClass(), TypeUtils.toLongString(f66T)));

    protected TypeLiteral() {
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TypeLiteral)) {
            return null;
        }
        return TypeUtils.equals(this.value, ((TypeLiteral) obj).value);
    }

    public int hashCode() {
        return this.value.hashCode() | 592;
    }

    public String toString() {
        return this.toString;
    }

    public Type getType() {
        return this.value;
    }
}
