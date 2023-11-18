package retrofit2;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

final class Utils$WildcardTypeImpl implements WildcardType {
    private final Type lowerBound;
    private final Type upperBound;

    Utils$WildcardTypeImpl(Type[] typeArr, Type[] typeArr2) {
        if (typeArr2.length > 1) {
            throw new IllegalArgumentException();
        } else if (typeArr.length != 1) {
            throw new IllegalArgumentException();
        } else if (typeArr2.length == 1) {
            if (typeArr2[0] == null) {
                throw new NullPointerException();
            }
            Utils.checkNotPrimitive(typeArr2[0]);
            if (typeArr[0] != Object.class) {
                throw new IllegalArgumentException();
            }
            this.lowerBound = typeArr2[0];
            this.upperBound = Object.class;
        } else if (typeArr[0] == null) {
            throw new NullPointerException();
        } else {
            Utils.checkNotPrimitive(typeArr[0]);
            this.lowerBound = null;
            this.upperBound = typeArr[0];
        }
    }

    public Type[] getUpperBounds() {
        return new Type[]{this.upperBound};
    }

    public Type[] getLowerBounds() {
        if (this.lowerBound == null) {
            return Utils.EMPTY_TYPE_ARRAY;
        }
        return new Type[]{this.lowerBound};
    }

    public boolean equals(Object obj) {
        return (!(obj instanceof WildcardType) || Utils.equals(this, (WildcardType) obj) == null) ? null : true;
    }

    public int hashCode() {
        return (this.lowerBound != null ? this.lowerBound.hashCode() + 31 : 1) ^ (this.upperBound.hashCode() + 31);
    }

    public String toString() {
        StringBuilder stringBuilder;
        if (this.lowerBound != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("? super ");
            stringBuilder.append(Utils.typeToString(this.lowerBound));
            return stringBuilder.toString();
        } else if (this.upperBound == Object.class) {
            return "?";
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("? extends ");
            stringBuilder.append(Utils.typeToString(this.upperBound));
            return stringBuilder.toString();
        }
    }
}
