package com.google.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import kotlin.text.Typography;

final class TypeResolver$WildcardCapturer {
    private final AtomicInteger id;

    private TypeResolver$WildcardCapturer() {
        this.id = new AtomicInteger();
    }

    Type capture(Type type) {
        Preconditions.checkNotNull(type);
        if ((type instanceof Class) || (type instanceof TypeVariable)) {
            return type;
        }
        if (type instanceof GenericArrayType) {
            return Types.newArrayType(capture(((GenericArrayType) type).getGenericComponentType()));
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return Types.newParameterizedTypeWithOwner(captureNullable(parameterizedType.getOwnerType()), (Class) parameterizedType.getRawType(), capture(parameterizedType.getActualTypeArguments()));
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            if (wildcardType.getLowerBounds().length != 0) {
                return type;
            }
            Object[] upperBounds = wildcardType.getUpperBounds();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("capture#");
            stringBuilder.append(this.id.incrementAndGet());
            stringBuilder.append("-of ? extends ");
            stringBuilder.append(Joiner.on((char) Typography.amp).join(upperBounds));
            return Types.newArtificialTypeVariable(TypeResolver$WildcardCapturer.class, stringBuilder.toString(), wildcardType.getUpperBounds());
        } else {
            throw new AssertionError("must have been one of the known types");
        }
    }

    private Type captureNullable(@Nullable Type type) {
        return type == null ? null : capture(type);
    }

    private Type[] capture(Type[] typeArr) {
        Type[] typeArr2 = new Type[typeArr.length];
        for (int i = 0; i < typeArr.length; i++) {
            typeArr2[i] = capture(typeArr[i]);
        }
        return typeArr2;
    }
}
