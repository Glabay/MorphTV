package com.google.common.reflect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import kotlin.text.Typography;

final class Types {
    private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");
    private static final Function<Type, String> TYPE_NAME = new Types$1();

    private enum ClassOwnership {
        OWNED_BY_ENCLOSING_CLASS {
            @Nullable
            Class<?> getOwnerType(Class<?> cls) {
                return cls.getEnclosingClass();
            }
        },
        LOCAL_CLASS_HAS_NO_OWNER {
            @Nullable
            Class<?> getOwnerType(Class<?> cls) {
                if (cls.isLocalClass()) {
                    return null;
                }
                return cls.getEnclosingClass();
            }
        };
        
        static final ClassOwnership JVM_BEHAVIOR = null;

        @Nullable
        abstract Class<?> getOwnerType(Class<?> cls);

        static {
            JVM_BEHAVIOR = detectJvmBehavior();
        }

        private static ClassOwnership detectJvmBehavior() {
            ParameterizedType parameterizedType = (ParameterizedType) new Types$ClassOwnership$3().getClass().getGenericSuperclass();
            for (ClassOwnership classOwnership : values()) {
                if (classOwnership.getOwnerType(Types$ClassOwnership$1LocalClass.class) == parameterizedType.getOwnerType()) {
                    return classOwnership;
                }
            }
            throw new AssertionError();
        }
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableList<Type> argumentsList;
        private final Type ownerType;
        private final Class<?> rawType;

        ParameterizedTypeImpl(@Nullable Type type, Class<?> cls, Type[] typeArr) {
            Preconditions.checkNotNull(cls);
            Preconditions.checkArgument(typeArr.length == cls.getTypeParameters().length);
            Types.disallowPrimitiveType(typeArr, "type parameter");
            this.ownerType = type;
            this.rawType = cls;
            this.argumentsList = Types$JavaVersion.CURRENT.usedInGenericType(typeArr);
        }

        public Type[] getActualTypeArguments() {
            return Types.toArray(this.argumentsList);
        }

        public Type getRawType() {
            return this.rawType;
        }

        public Type getOwnerType() {
            return this.ownerType;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.ownerType != null) {
                stringBuilder.append(Types$JavaVersion.CURRENT.typeName(this.ownerType));
                stringBuilder.append('.');
            }
            stringBuilder.append(this.rawType.getName());
            stringBuilder.append(Typography.less);
            stringBuilder.append(Types.COMMA_JOINER.join(Iterables.transform(this.argumentsList, Types.TYPE_NAME)));
            stringBuilder.append(Typography.greater);
            return stringBuilder.toString();
        }

        public int hashCode() {
            return ((this.ownerType == null ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode()) ^ this.rawType.hashCode();
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType parameterizedType = (ParameterizedType) obj;
            if (getRawType().equals(parameterizedType.getRawType()) && Objects.equal(getOwnerType(), parameterizedType.getOwnerType()) && Arrays.equals(getActualTypeArguments(), parameterizedType.getActualTypeArguments()) != null) {
                z = true;
            }
            return z;
        }
    }

    static Type newArrayType(Type type) {
        if (!(type instanceof WildcardType)) {
            return Types$JavaVersion.CURRENT.newArrayType(type);
        }
        WildcardType wildcardType = (WildcardType) type;
        Type[] lowerBounds = wildcardType.getLowerBounds();
        boolean z = true;
        Preconditions.checkArgument(lowerBounds.length <= 1, "Wildcard cannot have more than one lower bounds.");
        if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
        }
        type = wildcardType.getUpperBounds();
        if (type.length != 1) {
            z = false;
        }
        Preconditions.checkArgument(z, "Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(type[0]));
    }

    static ParameterizedType newParameterizedTypeWithOwner(@Nullable Type type, Class<?> cls, Type... typeArr) {
        if (type == null) {
            return newParameterizedType(cls, typeArr);
        }
        Preconditions.checkNotNull(typeArr);
        Preconditions.checkArgument(cls.getEnclosingClass() != null, "Owner type for unenclosed %s", cls);
        return new ParameterizedTypeImpl(type, cls, typeArr);
    }

    static ParameterizedType newParameterizedType(Class<?> cls, Type... typeArr) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(cls), cls, typeArr);
    }

    static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D d, String str, Type... typeArr) {
        if (typeArr.length == 0) {
            typeArr = new Type[]{Object.class};
        }
        return new Types$TypeVariableImpl(d, str, typeArr);
    }

    @VisibleForTesting
    static WildcardType subtypeOf(Type type) {
        return new Types$WildcardTypeImpl(new Type[0], new Type[]{type});
    }

    @VisibleForTesting
    static WildcardType supertypeOf(Type type) {
        return new Types$WildcardTypeImpl(new Type[]{type}, new Type[]{Object.class});
    }

    static String toString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }

    @Nullable
    static Type getComponentType(Type type) {
        Preconditions.checkNotNull(type);
        AtomicReference atomicReference = new AtomicReference();
        new Types$2(atomicReference).visit(new Type[]{type});
        return (Type) atomicReference.get();
    }

    @Nullable
    private static Type subtypeOfComponentType(Type[] typeArr) {
        for (Type componentType : typeArr) {
            Type componentType2 = getComponentType(componentType2);
            if (componentType2 != null) {
                if ((componentType2 instanceof Class) != null) {
                    Class cls = (Class) componentType2;
                    if (cls.isPrimitive()) {
                        return cls;
                    }
                }
                return subtypeOf(componentType2);
            }
        }
        return null;
    }

    private static Type[] toArray(Collection<Type> collection) {
        return (Type[]) collection.toArray(new Type[collection.size()]);
    }

    private static Iterable<Type> filterUpperBounds(Iterable<Type> iterable) {
        return Iterables.filter((Iterable) iterable, Predicates.not(Predicates.equalTo(Object.class)));
    }

    private static void disallowPrimitiveType(Type[] typeArr, String str) {
        for (Type type : typeArr) {
            if (type instanceof Class) {
                Preconditions.checkArgument(((Class) type).isPrimitive() ^ true, "Primitive type '%s' used as %s", (Class) type, str);
            }
        }
    }

    static Class<?> getArrayClass(Class<?> cls) {
        return Array.newInstance(cls, 0).getClass();
    }

    private Types() {
    }
}
