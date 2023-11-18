package retrofit2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

final class Utils$ParameterizedTypeImpl implements ParameterizedType {
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    Utils$ParameterizedTypeImpl(Type type, Type type2, Type... typeArr) {
        if (type2 instanceof Class) {
            Object obj = 1;
            Object obj2 = type == null ? 1 : null;
            if (((Class) type2).getEnclosingClass() != null) {
                obj = null;
            }
            if (obj2 != obj) {
                throw new IllegalArgumentException();
            }
        }
        for (Type type3 : typeArr) {
            Utils.checkNotNull(type3, "typeArgument == null");
            Utils.checkNotPrimitive(type3);
        }
        this.ownerType = type;
        this.rawType = type2;
        this.typeArguments = (Type[]) typeArr.clone();
    }

    public Type[] getActualTypeArguments() {
        return (Type[]) this.typeArguments.clone();
    }

    public Type getRawType() {
        return this.rawType;
    }

    public Type getOwnerType() {
        return this.ownerType;
    }

    public boolean equals(Object obj) {
        return (!(obj instanceof ParameterizedType) || Utils.equals(this, (ParameterizedType) obj) == null) ? null : true;
    }

    public int hashCode() {
        return (Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode()) ^ Utils.hashCodeOrZero(this.ownerType);
    }

    public String toString() {
        if (this.typeArguments.length == 0) {
            return Utils.typeToString(this.rawType);
        }
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder((this.typeArguments.length + 1) * 30);
        stringBuilder.append(Utils.typeToString(this.rawType));
        stringBuilder.append("<");
        stringBuilder.append(Utils.typeToString(this.typeArguments[0]));
        while (i < this.typeArguments.length) {
            stringBuilder.append(", ");
            stringBuilder.append(Utils.typeToString(this.typeArguments[i]));
            i++;
        }
        stringBuilder.append(">");
        return stringBuilder.toString();
    }
}
