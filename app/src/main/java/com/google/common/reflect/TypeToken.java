package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.TypeResolver.TypeVariableKey;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {
    private final Type runtimeType;
    private transient TypeResolver typeResolver;

    private static abstract class TypeCollector<K> {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new C11621();
        static final TypeCollector<Class<?>> FOR_RAW_TYPE = new C11632();

        /* renamed from: com.google.common.reflect.TypeToken$TypeCollector$1 */
        static class C11621 extends TypeCollector<TypeToken<?>> {
            C11621() {
                super();
            }

            Class<?> getRawType(TypeToken<?> typeToken) {
                return typeToken.getRawType();
            }

            Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> typeToken) {
                return typeToken.getGenericInterfaces();
            }

            @Nullable
            TypeToken<?> getSuperclass(TypeToken<?> typeToken) {
                return typeToken.getGenericSuperclass();
            }
        }

        /* renamed from: com.google.common.reflect.TypeToken$TypeCollector$2 */
        static class C11632 extends TypeCollector<Class<?>> {
            Class<?> getRawType(Class<?> cls) {
                return cls;
            }

            C11632() {
                super();
            }

            Iterable<? extends Class<?>> getInterfaces(Class<?> cls) {
                return Arrays.asList(cls.getInterfaces());
            }

            @Nullable
            Class<?> getSuperclass(Class<?> cls) {
                return cls.getSuperclass();
            }
        }

        abstract Iterable<? extends K> getInterfaces(K k);

        abstract Class<?> getRawType(K k);

        @Nullable
        abstract K getSuperclass(K k);

        private TypeCollector() {
        }

        final TypeCollector<K> classesOnly() {
            return new TypeToken$TypeCollector$3(this, this);
        }

        final ImmutableList<K> collectTypes(K k) {
            return collectTypes(ImmutableList.of(k));
        }

        ImmutableList<K> collectTypes(Iterable<? extends K> iterable) {
            Map newHashMap = Maps.newHashMap();
            for (Object collectTypes : iterable) {
                collectTypes(collectTypes, newHashMap);
            }
            return sortKeysByValue(newHashMap, Ordering.natural().reverse());
        }

        private int collectTypes(K k, Map<? super K, Integer> map) {
            Integer num = (Integer) map.get(this);
            if (num != null) {
                return num.intValue();
            }
            int isInterface = getRawType(k).isInterface();
            for (Object collectTypes : getInterfaces(k)) {
                isInterface = Math.max(isInterface, collectTypes(collectTypes, map));
            }
            Object superclass = getSuperclass(k);
            if (superclass != null) {
                isInterface = Math.max(isInterface, collectTypes(superclass, map));
            }
            isInterface++;
            map.put(k, Integer.valueOf(isInterface));
            return isInterface;
        }

        private static <K, V> ImmutableList<K> sortKeysByValue(Map<K, V> map, Comparator<? super V> comparator) {
            return new TypeToken$TypeCollector$4(comparator, map).immutableSortedCopy(map.keySet());
        }
    }

    protected TypeToken() {
        this.runtimeType = capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }

    protected TypeToken(Class<?> cls) {
        Type capture = super.capture();
        if (capture instanceof Class) {
            this.runtimeType = capture;
        } else {
            this.runtimeType = of((Class) cls).resolveType(capture).runtimeType;
        }
    }

    private TypeToken(Type type) {
        this.runtimeType = (Type) Preconditions.checkNotNull(type);
    }

    public static <T> TypeToken<T> of(Class<T> cls) {
        return new TypeToken$SimpleTypeToken(cls);
    }

    public static TypeToken<?> of(Type type) {
        return new TypeToken$SimpleTypeToken(type);
    }

    public final Class<? super T> getRawType() {
        return getRawType(this.runtimeType);
    }

    private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
        return getRawTypes(this.runtimeType);
    }

    public final Type getType() {
        return this.runtimeType;
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParameter, TypeToken<X> typeToken) {
        return new TypeToken$SimpleTypeToken(new TypeResolver().where(ImmutableMap.of(new TypeVariableKey(typeParameter.typeVariable), typeToken.runtimeType)).resolveType(this.runtimeType));
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParameter, Class<X> cls) {
        return where((TypeParameter) typeParameter, of((Class) cls));
    }

    public final TypeToken<?> resolveType(Type type) {
        Preconditions.checkNotNull(type);
        TypeResolver typeResolver = this.typeResolver;
        if (typeResolver == null) {
            typeResolver = TypeResolver.accordingTo(this.runtimeType);
            this.typeResolver = typeResolver;
        }
        return of(typeResolver.resolveType(type));
    }

    private Type[] resolveInPlace(Type[] typeArr) {
        for (int i = 0; i < typeArr.length; i++) {
            typeArr[i] = resolveType(typeArr[i]).getType();
        }
        return typeArr;
    }

    private TypeToken<?> resolveSupertype(Type type) {
        type = resolveType(type);
        type.typeResolver = this.typeResolver;
        return type;
    }

    @Nullable
    final TypeToken<? super T> getGenericSuperclass() {
        if (this.runtimeType instanceof TypeVariable) {
            return boundAsSuperclass(((TypeVariable) this.runtimeType).getBounds()[0]);
        }
        if (this.runtimeType instanceof WildcardType) {
            return boundAsSuperclass(((WildcardType) this.runtimeType).getUpperBounds()[0]);
        }
        Type genericSuperclass = getRawType().getGenericSuperclass();
        if (genericSuperclass == null) {
            return null;
        }
        return resolveSupertype(genericSuperclass);
    }

    @Nullable
    private TypeToken<? super T> boundAsSuperclass(Type type) {
        type = of(type);
        return type.getRawType().isInterface() ? null : type;
    }

    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if (this.runtimeType instanceof TypeVariable) {
            return boundsAsInterfaces(((TypeVariable) this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return boundsAsInterfaces(((WildcardType) this.runtimeType).getUpperBounds());
        }
        Builder builder = ImmutableList.builder();
        for (Type resolveSupertype : getRawType().getGenericInterfaces()) {
            builder.add(resolveSupertype(resolveSupertype));
        }
        return builder.build();
    }

    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] typeArr) {
        Builder builder = ImmutableList.builder();
        for (Type of : typeArr) {
            Object of2 = of(of);
            if (of2.getRawType().isInterface()) {
                builder.add(of2);
            }
        }
        return builder.build();
    }

    public final TypeToken$TypeSet getTypes() {
        return new TypeToken$TypeSet(this);
    }

    public final TypeToken<? super T> getSupertype(Class<? super T> cls) {
        Preconditions.checkArgument(cls.isAssignableFrom(getRawType()), "%s is not a super class of %s", cls, this);
        if (this.runtimeType instanceof TypeVariable) {
            return getSupertypeFromUpperBounds(cls, ((TypeVariable) this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return getSupertypeFromUpperBounds(cls, ((WildcardType) this.runtimeType).getUpperBounds());
        }
        if (cls.isArray()) {
            return getArraySupertype(cls);
        }
        return resolveSupertype(toGenericType(cls).runtimeType);
    }

    public final TypeToken<? extends T> getSubtype(Class<?> cls) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
        if (this.runtimeType instanceof WildcardType) {
            return getSubtypeFromLowerBounds(cls, ((WildcardType) this.runtimeType).getLowerBounds());
        }
        Preconditions.checkArgument(getRawType().isAssignableFrom(cls), "%s isn't a subclass of %s", cls, this);
        if (isArray()) {
            return getArraySubtype(cls);
        }
        return of(resolveTypeArgsForSubclass(cls));
    }

    public final boolean isAssignableFrom(TypeToken<?> typeToken) {
        return isAssignableFrom(typeToken.runtimeType);
    }

    public final boolean isAssignableFrom(Type type) {
        return isAssignable((Type) Preconditions.checkNotNull(type), this.runtimeType);
    }

    public final boolean isArray() {
        return getComponentType() != null;
    }

    public final boolean isPrimitive() {
        return (this.runtimeType instanceof Class) && ((Class) this.runtimeType).isPrimitive();
    }

    public final TypeToken<T> wrap() {
        return isPrimitive() ? of(Primitives.wrap((Class) this.runtimeType)) : this;
    }

    private boolean isWrapper() {
        return Primitives.allWrapperTypes().contains(this.runtimeType);
    }

    public final TypeToken<T> unwrap() {
        return isWrapper() ? of(Primitives.unwrap((Class) this.runtimeType)) : this;
    }

    @Nullable
    public final TypeToken<?> getComponentType() {
        Type componentType = Types.getComponentType(this.runtimeType);
        if (componentType == null) {
            return null;
        }
        return of(componentType);
    }

    public final Invokable<T, Object> method(Method method) {
        Preconditions.checkArgument(of(method.getDeclaringClass()).isAssignableFrom(this), "%s not declared by %s", method, this);
        return new TypeToken$1(this, method);
    }

    public final Invokable<T, T> constructor(Constructor<?> constructor) {
        Preconditions.checkArgument(constructor.getDeclaringClass() == getRawType(), "%s not declared by %s", constructor, getRawType());
        return new TypeToken$2(this, constructor);
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof TypeToken)) {
            return null;
        }
        return this.runtimeType.equals(((TypeToken) obj).runtimeType);
    }

    public int hashCode() {
        return this.runtimeType.hashCode();
    }

    public String toString() {
        return Types.toString(this.runtimeType);
    }

    protected Object writeReplace() {
        return of(new TypeResolver().resolveType(this.runtimeType));
    }

    final TypeToken<T> rejectTypeVariables() {
        new TypeToken$3(this).visit(new Type[]{this.runtimeType});
        return this;
    }

    private static boolean isAssignable(Type type, Type type2) {
        if (type2.equals(type)) {
            return true;
        }
        if (type2 instanceof WildcardType) {
            return isAssignableToWildcardType(type, (WildcardType) type2);
        }
        if (type instanceof TypeVariable) {
            return isAssignableFromAny(((TypeVariable) type).getBounds(), type2);
        }
        if (type instanceof WildcardType) {
            return isAssignableFromAny(((WildcardType) type).getUpperBounds(), type2);
        }
        if (type instanceof GenericArrayType) {
            return isAssignableFromGenericArrayType((GenericArrayType) type, type2);
        }
        if (type2 instanceof Class) {
            return isAssignableToClass(type, (Class) type2);
        }
        if (type2 instanceof ParameterizedType) {
            return isAssignableToParameterizedType(type, (ParameterizedType) type2);
        }
        return type2 instanceof GenericArrayType ? isAssignableToGenericArrayType(type, (GenericArrayType) type2) : null;
    }

    private static boolean isAssignableFromAny(Type[] typeArr, Type type) {
        for (Type isAssignable : typeArr) {
            if (isAssignable(isAssignable, type)) {
                return 1;
            }
        }
        return false;
    }

    private static boolean isAssignableToClass(Type type, Class<?> cls) {
        return cls.isAssignableFrom(getRawType(type));
    }

    private static boolean isAssignableToWildcardType(Type type, WildcardType wildcardType) {
        return (!isAssignable(type, supertypeBound(wildcardType)) || isAssignableBySubtypeBound(type, wildcardType) == null) ? null : true;
    }

    private static boolean isAssignableBySubtypeBound(Type type, WildcardType wildcardType) {
        wildcardType = subtypeBound(wildcardType);
        if (wildcardType == null) {
            return true;
        }
        type = subtypeBound(type);
        if (type == null) {
            return null;
        }
        return isAssignable(wildcardType, type);
    }

    private static boolean isAssignableToParameterizedType(Type type, ParameterizedType parameterizedType) {
        Class rawType = getRawType(parameterizedType);
        if (!rawType.isAssignableFrom(getRawType(type))) {
            return false;
        }
        TypeVariable[] typeParameters = rawType.getTypeParameters();
        parameterizedType = parameterizedType.getActualTypeArguments();
        type = of(type);
        for (int i = 0; i < typeParameters.length; i++) {
            if (!matchTypeArgument(type.resolveType(typeParameters[i]).runtimeType, parameterizedType[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignableToGenericArrayType(Type type, GenericArrayType genericArrayType) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                return isAssignable(cls.getComponentType(), genericArrayType.getGenericComponentType());
            }
            return false;
        } else if (type instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType) type).getGenericComponentType(), genericArrayType.getGenericComponentType());
        } else {
            return false;
        }
    }

    private static boolean isAssignableFromGenericArrayType(GenericArrayType genericArrayType, Type type) {
        boolean z = false;
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                return isAssignable(genericArrayType.getGenericComponentType(), cls.getComponentType());
            }
            if (cls == Object.class) {
                z = true;
            }
            return z;
        } else if (!(type instanceof GenericArrayType)) {
            return false;
        } else {
            return isAssignable(genericArrayType.getGenericComponentType(), ((GenericArrayType) type).getGenericComponentType());
        }
    }

    private static boolean matchTypeArgument(Type type, Type type2) {
        if (type.equals(type2)) {
            return true;
        }
        return type2 instanceof WildcardType ? isAssignableToWildcardType(type, (WildcardType) type2) : null;
    }

    private static Type supertypeBound(Type type) {
        return type instanceof WildcardType ? supertypeBound((WildcardType) type) : type;
    }

    private static Type supertypeBound(WildcardType wildcardType) {
        Type[] upperBounds = wildcardType.getUpperBounds();
        if (upperBounds.length == 1) {
            return supertypeBound(upperBounds[null]);
        }
        if (upperBounds.length == 0) {
            return Object.class;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("There should be at most one upper bound for wildcard type: ");
        stringBuilder.append(wildcardType);
        throw new AssertionError(stringBuilder.toString());
    }

    @Nullable
    private static Type subtypeBound(Type type) {
        return type instanceof WildcardType ? subtypeBound((WildcardType) type) : type;
    }

    @Nullable
    private static Type subtypeBound(WildcardType wildcardType) {
        Type[] lowerBounds = wildcardType.getLowerBounds();
        if (lowerBounds.length == 1) {
            return subtypeBound(lowerBounds[null]);
        }
        if (lowerBounds.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Wildcard should have at most one lower bound: ");
        stringBuilder.append(wildcardType);
        throw new AssertionError(stringBuilder.toString());
    }

    @VisibleForTesting
    static Class<?> getRawType(Type type) {
        return (Class) getRawTypes(type).iterator().next();
    }

    @VisibleForTesting
    static ImmutableSet<Class<?>> getRawTypes(Type type) {
        Preconditions.checkNotNull(type);
        ImmutableSet.Builder builder = ImmutableSet.builder();
        new TypeToken$4(builder).visit(new Type[]{type});
        return builder.build();
    }

    @VisibleForTesting
    static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
        if (cls.isArray()) {
            return of(Types.newArrayType(toGenericType(cls.getComponentType()).runtimeType));
        }
        Type[] typeParameters = cls.getTypeParameters();
        if (typeParameters.length > 0) {
            return of(Types.newParameterizedType(cls, typeParameters));
        }
        return of((Class) cls);
    }

    private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> cls, Type[] typeArr) {
        for (Type of : typeArr) {
            TypeToken of2 = of(of);
            if (of((Class) cls).isAssignableFrom(of2)) {
                return of2.getSupertype(cls);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cls);
        stringBuilder.append(" isn't a super type of ");
        stringBuilder.append(this);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> cls, Type[] typeArr) {
        if (typeArr.length > 0) {
            return of(typeArr[0]).getSubtype(cls);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cls);
        stringBuilder.append(" isn't a subclass of ");
        stringBuilder.append(this);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private TypeToken<? super T> getArraySupertype(Class<? super T> cls) {
        return of(newArrayClassOrGenericArrayType(((TypeToken) Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", cls, this)).getSupertype(cls.getComponentType()).runtimeType));
    }

    private TypeToken<? extends T> getArraySubtype(Class<?> cls) {
        return of(newArrayClassOrGenericArrayType(getComponentType().getSubtype(cls.getComponentType()).runtimeType));
    }

    private Type resolveTypeArgsForSubclass(Class<?> cls) {
        if (this.runtimeType instanceof Class) {
            return cls;
        }
        cls = toGenericType(cls);
        return new TypeResolver().where(cls.getSupertype(getRawType()).runtimeType, this.runtimeType).resolveType(cls.runtimeType);
    }

    private static Type newArrayClassOrGenericArrayType(Type type) {
        return Types$JavaVersion.JAVA7.newArrayType(type);
    }
}
