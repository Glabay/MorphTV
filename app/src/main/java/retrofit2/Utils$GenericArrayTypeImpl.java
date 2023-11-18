package retrofit2;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

final class Utils$GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    Utils$GenericArrayTypeImpl(Type type) {
        this.componentType = type;
    }

    public Type getGenericComponentType() {
        return this.componentType;
    }

    public boolean equals(Object obj) {
        return (!(obj instanceof GenericArrayType) || Utils.equals(this, (GenericArrayType) obj) == null) ? null : true;
    }

    public int hashCode() {
        return this.componentType.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utils.typeToString(this.componentType));
        stringBuilder.append("[]");
        return stringBuilder.toString();
    }
}
