package org.apache.commons.lang3.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import kotlin.text.Typography;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;

public class TypeUtils {
    public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(Object.class).build();

    private static final class GenericArrayTypeImpl implements GenericArrayType {
        private final Type componentType;

        private GenericArrayTypeImpl(Type type) {
            this.componentType = type;
        }

        public Type getGenericComponentType() {
            return this.componentType;
        }

        public String toString() {
            return TypeUtils.toString(this);
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof GenericArrayType) || TypeUtils.equals((GenericArrayType) this, (Type) (GenericArrayType) obj) == null) {
                    return null;
                }
            }
            return true;
        }

        public int hashCode() {
            return this.componentType.hashCode() | 1072;
        }
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> raw;
        private final Type[] typeArguments;
        private final Type useOwner;

        private ParameterizedTypeImpl(Class<?> cls, Type type, Type[] typeArr) {
            this.raw = cls;
            this.useOwner = type;
            this.typeArguments = (Type[]) typeArr.clone();
        }

        public Type getRawType() {
            return this.raw;
        }

        public Type getOwnerType() {
            return this.useOwner;
        }

        public Type[] getActualTypeArguments() {
            return (Type[]) this.typeArguments.clone();
        }

        public String toString() {
            return TypeUtils.toString(this);
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof ParameterizedType) || TypeUtils.equals((ParameterizedType) this, (Type) (ParameterizedType) obj) == null) {
                    return null;
                }
            }
            return true;
        }

        public int hashCode() {
            return ((((this.raw.hashCode() | 1136) << 4) | ObjectUtils.hashCode(this.useOwner)) << 8) | Arrays.hashCode(this.typeArguments);
        }
    }

    public static class WildcardTypeBuilder implements Builder<WildcardType> {
        private Type[] lowerBounds;
        private Type[] upperBounds;

        private WildcardTypeBuilder() {
        }

        public WildcardTypeBuilder withUpperBounds(Type... typeArr) {
            this.upperBounds = typeArr;
            return this;
        }

        public WildcardTypeBuilder withLowerBounds(Type... typeArr) {
            this.lowerBounds = typeArr;
            return this;
        }

        public WildcardType build() {
            return new WildcardTypeImpl(this.upperBounds, this.lowerBounds);
        }
    }

    private static final class WildcardTypeImpl implements WildcardType {
        private static final Type[] EMPTY_BOUNDS = new Type[0];
        private final Type[] lowerBounds;
        private final Type[] upperBounds;

        private WildcardTypeImpl(Type[] typeArr, Type[] typeArr2) {
            this.upperBounds = (Type[]) ObjectUtils.defaultIfNull(typeArr, EMPTY_BOUNDS);
            this.lowerBounds = (Type[]) ObjectUtils.defaultIfNull(typeArr2, EMPTY_BOUNDS);
        }

        public Type[] getUpperBounds() {
            return (Type[]) this.upperBounds.clone();
        }

        public Type[] getLowerBounds() {
            return (Type[]) this.lowerBounds.clone();
        }

        public String toString() {
            return TypeUtils.toString(this);
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof WildcardType) || TypeUtils.equals((WildcardType) this, (Type) (WildcardType) obj) == null) {
                    return null;
                }
            }
            return true;
        }

        public int hashCode() {
            return ((Arrays.hashCode(this.upperBounds) | 18688) << 8) | Arrays.hashCode(this.lowerBounds);
        }
    }

    public static boolean isAssignable(Type type, Type type2) {
        return isAssignable(type, type2, null);
    }

    private static boolean isAssignable(Type type, Type type2, Map<TypeVariable<?>, Type> map) {
        if (type2 != null) {
            if (!(type2 instanceof Class)) {
                if (type2 instanceof ParameterizedType) {
                    return isAssignable(type, (ParameterizedType) type2, (Map) map);
                }
                if (type2 instanceof GenericArrayType) {
                    return isAssignable(type, (GenericArrayType) type2, (Map) map);
                }
                if (type2 instanceof WildcardType) {
                    return isAssignable(type, (WildcardType) type2, (Map) map);
                }
                if (type2 instanceof TypeVariable) {
                    return isAssignable(type, (TypeVariable) type2, (Map) map);
                }
                map = new StringBuilder();
                map.append("found an unhandled type: ");
                map.append(type2);
                throw new IllegalStateException(map.toString());
            }
        }
        return isAssignable(type, (Class) type2);
    }

    private static boolean isAssignable(Type type, Class<?> cls) {
        boolean z = true;
        if (type == null) {
            if (cls != null) {
                if (cls.isPrimitive() != null) {
                    z = false;
                }
            }
            return z;
        } else if (cls == null) {
            return false;
        } else {
            if (cls.equals(type)) {
                return true;
            }
            if (type instanceof Class) {
                return ClassUtils.isAssignable((Class) type, (Class) cls);
            }
            if (type instanceof ParameterizedType) {
                return isAssignable(getRawType((ParameterizedType) type), (Class) cls);
            }
            if (type instanceof TypeVariable) {
                for (Type isAssignable : ((TypeVariable) type).getBounds()) {
                    if (isAssignable(isAssignable, (Class) cls)) {
                        return true;
                    }
                }
                return false;
            } else if (type instanceof GenericArrayType) {
                if (!cls.equals(Object.class)) {
                    if (!cls.isArray() || isAssignable(((GenericArrayType) type).getGenericComponentType(), cls.getComponentType()) == null) {
                        z = false;
                    }
                }
                return z;
            } else if ((type instanceof WildcardType) != null) {
                return false;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("found an unhandled type: ");
                stringBuilder.append(type);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    private static boolean isAssignable(Type type, ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> map) {
        if (type == null) {
            return true;
        }
        if (parameterizedType == null) {
            return false;
        }
        if (parameterizedType.equals(type)) {
            return true;
        }
        Class rawType = getRawType(parameterizedType);
        type = getTypeArguments(type, rawType, null);
        if (type == null) {
            return false;
        }
        if (type.isEmpty()) {
            return true;
        }
        parameterizedType = getTypeArguments(parameterizedType, rawType, (Map) map);
        for (TypeVariable typeVariable : parameterizedType.keySet()) {
            Type unrollVariableAssignments = unrollVariableAssignments(typeVariable, parameterizedType);
            Type unrollVariableAssignments2 = unrollVariableAssignments(typeVariable, type);
            if (unrollVariableAssignments != null || !(unrollVariableAssignments2 instanceof Class)) {
                if (!(unrollVariableAssignments2 == null || unrollVariableAssignments.equals(unrollVariableAssignments2))) {
                    if (!(unrollVariableAssignments instanceof WildcardType) || !isAssignable(unrollVariableAssignments2, unrollVariableAssignments, (Map) map)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static Type unrollVariableAssignments(TypeVariable<?> typeVariable, Map<TypeVariable<?>, Type> map) {
        Type type;
        while (true) {
            type = (Type) map.get(typeVariable);
            if (!(type instanceof TypeVariable) || type.equals(typeVariable) != null) {
                return type;
            }
            typeVariable = (TypeVariable) type;
        }
        return type;
    }

    private static boolean isAssignable(Type type, GenericArrayType genericArrayType, Map<TypeVariable<?>, Type> map) {
        boolean z = true;
        if (type == null) {
            return true;
        }
        if (genericArrayType == null) {
            return false;
        }
        if (genericArrayType.equals(type)) {
            return true;
        }
        Type genericComponentType = genericArrayType.getGenericComponentType();
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray() == null || isAssignable(cls.getComponentType(), genericComponentType, (Map) map) == null) {
                z = false;
            }
            return z;
        } else if (type instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType) type).getGenericComponentType(), genericComponentType, (Map) map);
        } else {
            if ((type instanceof WildcardType) != null) {
                for (Type isAssignable : getImplicitUpperBounds((WildcardType) type)) {
                    if (isAssignable(isAssignable, (Type) genericArrayType)) {
                        return true;
                    }
                }
                return false;
            } else if ((type instanceof TypeVariable) != null) {
                for (Type isAssignable2 : getImplicitBounds((TypeVariable) type)) {
                    if (isAssignable(isAssignable2, (Type) genericArrayType)) {
                        return true;
                    }
                }
                return false;
            } else if ((type instanceof ParameterizedType) != null) {
                return false;
            } else {
                map = new StringBuilder();
                map.append("found an unhandled type: ");
                map.append(type);
                throw new IllegalStateException(map.toString());
            }
        }
    }

    private static boolean isAssignable(Type type, WildcardType wildcardType, Map<TypeVariable<?>, Type> map) {
        if (type == null) {
            return true;
        }
        if (wildcardType == null) {
            return false;
        }
        if (wildcardType.equals(type)) {
            return true;
        }
        Type[] implicitUpperBounds = getImplicitUpperBounds(wildcardType);
        wildcardType = getImplicitLowerBounds(wildcardType);
        Type substituteTypeVariables;
        if (type instanceof WildcardType) {
            WildcardType wildcardType2 = (WildcardType) type;
            Type[] implicitUpperBounds2 = getImplicitUpperBounds(wildcardType2);
            type = getImplicitLowerBounds(wildcardType2);
            for (Type substituteTypeVariables2 : implicitUpperBounds) {
                Type substituteTypeVariables22 = substituteTypeVariables(substituteTypeVariables22, map);
                for (Type isAssignable : implicitUpperBounds2) {
                    if (!isAssignable(isAssignable, substituteTypeVariables22, (Map) map)) {
                        return false;
                    }
                }
            }
            for (Type substituteTypeVariables3 : wildcardType) {
                substituteTypeVariables3 = substituteTypeVariables(substituteTypeVariables3, map);
                for (Type isAssignable2 : type) {
                    if (!isAssignable(substituteTypeVariables3, isAssignable2, (Map) map)) {
                        return false;
                    }
                }
            }
            return true;
        }
        for (Type substituteTypeVariables4 : implicitUpperBounds) {
            if (!isAssignable(type, substituteTypeVariables(substituteTypeVariables4, map), (Map) map)) {
                return false;
            }
        }
        for (Type substituteTypeVariables32 : wildcardType) {
            if (!isAssignable(substituteTypeVariables(substituteTypeVariables32, map), type, (Map) map)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignable(Type type, TypeVariable<?> typeVariable, Map<TypeVariable<?>, Type> map) {
        if (type == null) {
            return true;
        }
        if (typeVariable == null) {
            return false;
        }
        if (typeVariable.equals(type)) {
            return true;
        }
        if (type instanceof TypeVariable) {
            for (Type isAssignable : getImplicitBounds((TypeVariable) type)) {
                if (isAssignable(isAssignable, (TypeVariable) typeVariable, (Map) map)) {
                    return true;
                }
            }
        }
        if ((type instanceof Class) == null && (type instanceof ParameterizedType) == null && (type instanceof GenericArrayType) == null) {
            if ((type instanceof WildcardType) == null) {
                map = new StringBuilder();
                map.append("found an unhandled type: ");
                map.append(type);
                throw new IllegalStateException(map.toString());
            }
        }
        return false;
    }

    private static Type substituteTypeVariables(Type type, Map<TypeVariable<?>, Type> map) {
        if (!(type instanceof TypeVariable) || map == null) {
            return type;
        }
        Type type2 = (Type) map.get(type);
        if (type2 != null) {
            return type2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("missing assignment type for type variable ");
        stringBuilder.append(type);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType) {
        return getTypeArguments(parameterizedType, getRawType(parameterizedType), null);
    }

    public static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> cls) {
        return getTypeArguments(type, (Class) cls, null);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Type type, Class<?> cls, Map<TypeVariable<?>, Type> map) {
        if (type instanceof Class) {
            return getTypeArguments((Class) type, (Class) cls, (Map) map);
        }
        if (type instanceof ParameterizedType) {
            return getTypeArguments((ParameterizedType) type, (Class) cls, (Map) map);
        }
        if (type instanceof GenericArrayType) {
            Class componentType;
            type = ((GenericArrayType) type).getGenericComponentType();
            if (cls.isArray()) {
                componentType = cls.getComponentType();
            }
            return getTypeArguments(type, componentType, (Map) map);
        }
        int i = 0;
        int length;
        Type type2;
        if (type instanceof WildcardType) {
            type = getImplicitUpperBounds((WildcardType) type);
            length = type.length;
            while (i < length) {
                type2 = type[i];
                if (isAssignable(type2, (Class) cls)) {
                    return getTypeArguments(type2, (Class) cls, (Map) map);
                }
                i++;
            }
            return null;
        } else if (type instanceof TypeVariable) {
            type = getImplicitBounds((TypeVariable) type);
            length = type.length;
            while (i < length) {
                type2 = type[i];
                if (isAssignable(type2, (Class) cls)) {
                    return getTypeArguments(type2, (Class) cls, (Map) map);
                }
                i++;
            }
            return null;
        } else {
            map = new StringBuilder();
            map.append("found an unhandled type: ");
            map.append(type);
            throw new IllegalStateException(map.toString());
        }
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(ParameterizedType parameterizedType, Class<?> cls, Map<TypeVariable<?>, Type> map) {
        Type rawType = getRawType(parameterizedType);
        if (!isAssignable(rawType, (Class) cls)) {
            return null;
        }
        Map typeArguments;
        Type ownerType = parameterizedType.getOwnerType();
        if (ownerType instanceof ParameterizedType) {
            ParameterizedType parameterizedType2 = (ParameterizedType) ownerType;
            typeArguments = getTypeArguments(parameterizedType2, getRawType(parameterizedType2), (Map) map);
        } else {
            typeArguments = map == null ? new HashMap() : new HashMap(map);
        }
        parameterizedType = parameterizedType.getActualTypeArguments();
        TypeVariable[] typeParameters = rawType.getTypeParameters();
        for (int i = 0; i < typeParameters.length; i++) {
            Object obj = parameterizedType[i];
            Object obj2 = typeParameters[i];
            if (typeArguments.containsKey(obj)) {
                obj = (Type) typeArguments.get(obj);
            }
            typeArguments.put(obj2, obj);
        }
        if (cls.equals(rawType) != null) {
            return typeArguments;
        }
        return getTypeArguments(getClosestParentType(rawType, cls), (Class) cls, typeArguments);
    }

    private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, Class<?> cls2, Map<TypeVariable<?>, Type> map) {
        if (!isAssignable((Type) cls, (Class) cls2)) {
            return null;
        }
        if (cls.isPrimitive()) {
            if (cls2.isPrimitive()) {
                return new HashMap();
            }
            cls = ClassUtils.primitiveToWrapper(cls);
        }
        Map hashMap = map == null ? new HashMap() : new HashMap(map);
        if (cls2.equals(cls)) {
            return hashMap;
        }
        return getTypeArguments(getClosestParentType(cls, cls2), (Class) cls2, hashMap);
    }

    public static Map<TypeVariable<?>, Type> determineTypeArguments(Class<?> cls, ParameterizedType parameterizedType) {
        Validate.notNull(cls, "cls is null", new Object[0]);
        Validate.notNull(parameterizedType, "superType is null", new Object[0]);
        Class rawType = getRawType(parameterizedType);
        if (!isAssignable((Type) cls, rawType)) {
            return null;
        }
        if (cls.equals(rawType)) {
            return getTypeArguments(parameterizedType, rawType, null);
        }
        Type closestParentType = getClosestParentType(cls, rawType);
        if (closestParentType instanceof Class) {
            return determineTypeArguments((Class) closestParentType, parameterizedType);
        }
        ParameterizedType parameterizedType2 = (ParameterizedType) closestParentType;
        parameterizedType = determineTypeArguments(getRawType(parameterizedType2), parameterizedType);
        mapTypeVariablesToArguments(cls, parameterizedType2, parameterizedType);
        return parameterizedType;
    }

    private static <T> void mapTypeVariablesToArguments(Class<T> cls, ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> map) {
        Type ownerType = parameterizedType.getOwnerType();
        if (ownerType instanceof ParameterizedType) {
            mapTypeVariablesToArguments(cls, (ParameterizedType) ownerType, map);
        }
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        parameterizedType = getRawType(parameterizedType).getTypeParameters();
        cls = Arrays.asList(cls.getTypeParameters());
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Object obj = parameterizedType[i];
            Object obj2 = actualTypeArguments[i];
            if (cls.contains(obj2) && map.containsKey(obj)) {
                map.put((TypeVariable) obj2, map.get(obj));
            }
        }
    }

    private static Type getClosestParentType(Class<?> cls, Class<?> cls2) {
        if (cls2.isInterface()) {
            Type type = null;
            for (Type type2 : cls.getGenericInterfaces()) {
                Type rawType;
                if (type2 instanceof ParameterizedType) {
                    rawType = getRawType((ParameterizedType) type2);
                } else if (type2 instanceof Class) {
                    rawType = (Class) type2;
                } else {
                    cls2 = new StringBuilder();
                    cls2.append("Unexpected generic interface type found: ");
                    cls2.append(type2);
                    throw new IllegalStateException(cls2.toString());
                }
                if (isAssignable(rawType, (Class) cls2) && isAssignable(type, rawType)) {
                    type = type2;
                }
            }
            if (type != null) {
                return type;
            }
        }
        return cls.getGenericSuperclass();
    }

    public static boolean isInstance(Object obj, Type type) {
        boolean z = false;
        if (type == null) {
            return false;
        }
        if (obj != null) {
            z = isAssignable(obj.getClass(), type, null);
        } else if ((type instanceof Class) == null || ((Class) type).isPrimitive() == null) {
            z = true;
        }
        return z;
    }

    public static Type[] normalizeUpperBounds(Type[] typeArr) {
        Validate.notNull(typeArr, "null value specified for bounds array", new Object[0]);
        if (typeArr.length < 2) {
            return typeArr;
        }
        Set hashSet = new HashSet(typeArr.length);
        for (Type type : typeArr) {
            Object obj;
            for (Type type2 : typeArr) {
                if (type != type2 && isAssignable(type2, type, null)) {
                    obj = 1;
                    break;
                }
            }
            obj = null;
            if (obj == null) {
                hashSet.add(type);
            }
        }
        return (Type[]) hashSet.toArray(new Type[hashSet.size()]);
    }

    public static Type[] getImplicitBounds(TypeVariable<?> typeVariable) {
        Validate.notNull(typeVariable, "typeVariable is null", new Object[0]);
        typeVariable = typeVariable.getBounds();
        if (typeVariable.length != 0) {
            return normalizeUpperBounds(typeVariable);
        }
        return new Type[]{Object.class};
    }

    public static Type[] getImplicitUpperBounds(WildcardType wildcardType) {
        Validate.notNull(wildcardType, "wildcardType is null", new Object[0]);
        wildcardType = wildcardType.getUpperBounds();
        if (wildcardType.length != 0) {
            return normalizeUpperBounds(wildcardType);
        }
        return new Type[]{Object.class};
    }

    public static Type[] getImplicitLowerBounds(WildcardType wildcardType) {
        Validate.notNull(wildcardType, "wildcardType is null", new Object[0]);
        wildcardType = wildcardType.getLowerBounds();
        if (wildcardType.length != 0) {
            return wildcardType;
        }
        return new Type[]{null};
    }

    public static boolean typesSatisfyVariables(Map<TypeVariable<?>, Type> map) {
        Validate.notNull(map, "typeVarAssigns is null", new Object[0]);
        for (Entry entry : map.entrySet()) {
            TypeVariable typeVariable = (TypeVariable) entry.getKey();
            Type type = (Type) entry.getValue();
            for (Type substituteTypeVariables : getImplicitBounds(typeVariable)) {
                if (!isAssignable(type, substituteTypeVariables(substituteTypeVariables, map), (Map) map)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Class<?> getRawType(ParameterizedType parameterizedType) {
        parameterizedType = parameterizedType.getRawType();
        if (parameterizedType instanceof Class) {
            return (Class) parameterizedType;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Wait... What!? Type of rawType: ");
        stringBuilder.append(parameterizedType);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public static Class<?> getRawType(Type type, Type type2) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawType((ParameterizedType) type);
        }
        if (type instanceof TypeVariable) {
            if (type2 == null) {
                return null;
            }
            GenericDeclaration genericDeclaration = ((TypeVariable) type).getGenericDeclaration();
            if (!(genericDeclaration instanceof Class)) {
                return null;
            }
            Map typeArguments = getTypeArguments(type2, (Class) genericDeclaration);
            if (typeArguments == null) {
                return null;
            }
            type = (Type) typeArguments.get(type);
            if (type == null) {
                return null;
            }
            return getRawType(type, type2);
        } else if (type instanceof GenericArrayType) {
            return Array.newInstance(getRawType(((GenericArrayType) type).getGenericComponentType(), type2), null).getClass();
        } else {
            if ((type instanceof WildcardType) != null) {
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unknown type: ");
            stringBuilder.append(type);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static boolean isArrayType(Type type) {
        if (!(type instanceof GenericArrayType)) {
            if (!(type instanceof Class) || ((Class) type).isArray() == null) {
                return null;
            }
        }
        return true;
    }

    public static Type getArrayComponentType(Type type) {
        Type type2 = null;
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                type2 = cls.getComponentType();
            }
            return type2;
        } else if (type instanceof GenericArrayType) {
            return ((GenericArrayType) type).getGenericComponentType();
        } else {
            return null;
        }
    }

    public static Type unrollVariables(Map<TypeVariable<?>, Type> map, Type type) {
        if (map == null) {
            map = Collections.emptyMap();
        }
        if (containsTypeVariables(type)) {
            if (type instanceof TypeVariable) {
                return unrollVariables(map, (Type) map.get(type));
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getOwnerType() != null) {
                    Map<TypeVariable<?>, Type> hashMap = new HashMap(map);
                    hashMap.putAll(getTypeArguments(parameterizedType));
                    map = hashMap;
                }
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    Type unrollVariables = unrollVariables(map, actualTypeArguments[i]);
                    if (unrollVariables != null) {
                        actualTypeArguments[i] = unrollVariables;
                    }
                }
                return parameterizeWithOwner(parameterizedType.getOwnerType(), (Class) parameterizedType.getRawType(), actualTypeArguments);
            } else if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                return wildcardType().withUpperBounds(unrollBounds(map, wildcardType.getUpperBounds())).withLowerBounds(unrollBounds(map, wildcardType.getLowerBounds())).build();
            }
        }
        return type;
    }

    private static Type[] unrollBounds(Map<TypeVariable<?>, Type> map, Type[] typeArr) {
        int i = 0;
        while (i < typeArr.length) {
            Type unrollVariables = unrollVariables(map, typeArr[i]);
            if (unrollVariables == null) {
                typeArr = (Type[]) ArrayUtils.remove((Object[]) typeArr, i);
                i--;
            } else {
                typeArr[i] = unrollVariables;
            }
            i++;
        }
        return typeArr;
    }

    public static boolean containsTypeVariables(Type type) {
        boolean z = true;
        if (type instanceof TypeVariable) {
            return true;
        }
        if (type instanceof Class) {
            if (((Class) type).getTypeParameters().length <= null) {
                z = false;
            }
            return z;
        } else if (type instanceof ParameterizedType) {
            for (Type containsTypeVariables : ((ParameterizedType) type).getActualTypeArguments()) {
                if (containsTypeVariables(containsTypeVariables)) {
                    return true;
                }
            }
            return false;
        } else if (!(type instanceof WildcardType)) {
            return false;
        } else {
            WildcardType wildcardType = (WildcardType) type;
            if (!containsTypeVariables(getImplicitLowerBounds(wildcardType)[0])) {
                if (containsTypeVariables(getImplicitUpperBounds(wildcardType)[0]) == null) {
                    z = false;
                }
            }
            return z;
        }
    }

    public static final ParameterizedType parameterize(Class<?> cls, Type... typeArr) {
        return parameterizeWithOwner(null, (Class) cls, typeArr);
    }

    public static final ParameterizedType parameterize(Class<?> cls, Map<TypeVariable<?>, Type> map) {
        Validate.notNull(cls, "raw class is null", new Object[0]);
        Validate.notNull(map, "typeArgMappings is null", new Object[0]);
        return parameterizeWithOwner(null, (Class) cls, extractTypeArgumentsFrom(map, cls.getTypeParameters()));
    }

    public static final ParameterizedType parameterizeWithOwner(Type type, Class<?> cls, Type... typeArr) {
        Validate.notNull(cls, "raw class is null", new Object[0]);
        if (cls.getEnclosingClass() == null) {
            Validate.isTrue(type == null ? true : null, "no owner allowed for top-level %s", cls);
            type = null;
        } else if (type == null) {
            type = cls.getEnclosingClass();
        } else {
            Validate.isTrue(isAssignable(type, cls.getEnclosingClass()), "%s is invalid owner type for parameterized %s", type, cls);
        }
        Validate.noNullElements((Object[]) typeArr, "null type argument at index %s", new Object[0]);
        Validate.isTrue(cls.getTypeParameters().length == typeArr.length, "invalid number of type parameters specified: expected %d, got %d", Integer.valueOf(cls.getTypeParameters().length), Integer.valueOf(typeArr.length));
        return new ParameterizedTypeImpl(cls, type, typeArr);
    }

    public static final ParameterizedType parameterizeWithOwner(Type type, Class<?> cls, Map<TypeVariable<?>, Type> map) {
        Validate.notNull(cls, "raw class is null", new Object[0]);
        Validate.notNull(map, "typeArgMappings is null", new Object[0]);
        return parameterizeWithOwner(type, (Class) cls, extractTypeArgumentsFrom(map, cls.getTypeParameters()));
    }

    private static Type[] extractTypeArgumentsFrom(Map<TypeVariable<?>, Type> map, TypeVariable<?>[] typeVariableArr) {
        Type[] typeArr = new Type[typeVariableArr.length];
        int length = typeVariableArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            Type type = typeVariableArr[i];
            Validate.isTrue(map.containsKey(type), "missing argument mapping for %s", toString(type));
            int i3 = i2 + 1;
            typeArr[i2] = (Type) map.get(type);
            i++;
            i2 = i3;
        }
        return typeArr;
    }

    public static WildcardTypeBuilder wildcardType() {
        return new WildcardTypeBuilder();
    }

    public static GenericArrayType genericArrayType(Type type) {
        return new GenericArrayTypeImpl((Type) Validate.notNull(type, "componentType is null", new Object[0]));
    }

    public static boolean equals(Type type, Type type2) {
        if (ObjectUtils.equals(type, type2)) {
            return true;
        }
        if (type instanceof ParameterizedType) {
            return equals((ParameterizedType) type, type2);
        }
        if (type instanceof GenericArrayType) {
            return equals((GenericArrayType) type, type2);
        }
        return type instanceof WildcardType ? equals((WildcardType) type, type2) : null;
    }

    private static boolean equals(ParameterizedType parameterizedType, Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType2 = (ParameterizedType) type;
            if (equals(parameterizedType.getRawType(), parameterizedType2.getRawType()) && equals(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType())) {
                return equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments());
            }
        }
        return null;
    }

    private static boolean equals(GenericArrayType genericArrayType, Type type) {
        return (!(type instanceof GenericArrayType) || equals(genericArrayType.getGenericComponentType(), ((GenericArrayType) type).getGenericComponentType()) == null) ? null : true;
    }

    private static boolean equals(WildcardType wildcardType, Type type) {
        boolean z = false;
        if (!(type instanceof WildcardType)) {
            return false;
        }
        WildcardType wildcardType2 = (WildcardType) type;
        if (equals(getImplicitLowerBounds(wildcardType), getImplicitLowerBounds(wildcardType2)) && equals(getImplicitUpperBounds(wildcardType), getImplicitUpperBounds(wildcardType2)) != null) {
            z = true;
        }
        return z;
    }

    private static boolean equals(Type[] typeArr, Type[] typeArr2) {
        if (typeArr.length != typeArr2.length) {
            return false;
        }
        for (int i = 0; i < typeArr.length; i++) {
            if (!equals(typeArr[i], typeArr2[i])) {
                return false;
            }
        }
        return 1;
    }

    public static String toString(Type type) {
        Validate.notNull(type);
        if (type instanceof Class) {
            return classToString((Class) type);
        }
        if (type instanceof ParameterizedType) {
            return parameterizedTypeToString((ParameterizedType) type);
        }
        if (type instanceof WildcardType) {
            return wildcardTypeToString((WildcardType) type);
        }
        if (type instanceof TypeVariable) {
            return typeVariableToString((TypeVariable) type);
        }
        if (type instanceof GenericArrayType) {
            return genericArrayTypeToString((GenericArrayType) type);
        }
        throw new IllegalArgumentException(ObjectUtils.identityToString(type));
    }

    public static String toLongString(TypeVariable<?> typeVariable) {
        Validate.notNull(typeVariable, "var is null", new Object[0]);
        StringBuilder stringBuilder = new StringBuilder();
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            Class cls = (Class) genericDeclaration;
            while (cls.getEnclosingClass() != null) {
                stringBuilder.insert(0, cls.getSimpleName()).insert(0, '.');
                cls = cls.getEnclosingClass();
            }
            stringBuilder.insert(0, cls.getName());
        } else if (genericDeclaration instanceof Type) {
            stringBuilder.append(toString((Type) genericDeclaration));
        } else {
            stringBuilder.append(genericDeclaration);
        }
        stringBuilder.append(':');
        stringBuilder.append(typeVariableToString(typeVariable));
        return stringBuilder.toString();
    }

    public static <T> Typed<T> wrap(final Type type) {
        return new Typed<T>() {
            public Type getType() {
                return type;
            }
        };
    }

    public static <T> Typed<T> wrap(Class<T> cls) {
        return wrap((Type) cls);
    }

    private static String classToString(Class<?> cls) {
        StringBuilder stringBuilder = new StringBuilder();
        if (cls.getEnclosingClass() != null) {
            stringBuilder.append(classToString(cls.getEnclosingClass()));
            stringBuilder.append('.');
            stringBuilder.append(cls.getSimpleName());
        } else {
            stringBuilder.append(cls.getName());
        }
        if (cls.getTypeParameters().length > 0) {
            stringBuilder.append(Typography.less);
            appendAllTo(stringBuilder, ", ", cls.getTypeParameters());
            stringBuilder.append(Typography.greater);
        }
        return stringBuilder.toString();
    }

    private static String typeVariableToString(TypeVariable<?> typeVariable) {
        StringBuilder stringBuilder = new StringBuilder(typeVariable.getName());
        Type[] bounds = typeVariable.getBounds();
        if (bounds.length > 0 && !(bounds.length == 1 && Object.class.equals(bounds[0]))) {
            stringBuilder.append(" extends ");
            appendAllTo(stringBuilder, " & ", typeVariable.getBounds());
        }
        return stringBuilder.toString();
    }

    private static String parameterizedTypeToString(ParameterizedType parameterizedType) {
        StringBuilder stringBuilder = new StringBuilder();
        Type ownerType = parameterizedType.getOwnerType();
        Class cls = (Class) parameterizedType.getRawType();
        parameterizedType = parameterizedType.getActualTypeArguments();
        if (ownerType == null) {
            stringBuilder.append(cls.getName());
        } else {
            if (ownerType instanceof Class) {
                stringBuilder.append(((Class) ownerType).getName());
            } else {
                stringBuilder.append(ownerType.toString());
            }
            stringBuilder.append('.');
            stringBuilder.append(cls.getSimpleName());
        }
        stringBuilder.append(Typography.less);
        appendAllTo(stringBuilder, ", ", parameterizedType).append(Typography.greater);
        return stringBuilder.toString();
    }

    private static String wildcardTypeToString(WildcardType wildcardType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('?');
        Type[] lowerBounds = wildcardType.getLowerBounds();
        wildcardType = wildcardType.getUpperBounds();
        if (lowerBounds.length <= 1) {
            if (lowerBounds.length != 1 || lowerBounds[0] == null) {
                if (wildcardType.length > 1 || (wildcardType.length == 1 && !Object.class.equals(wildcardType[0]))) {
                    stringBuilder.append(" extends ");
                    appendAllTo(stringBuilder, " & ", wildcardType);
                }
                return stringBuilder.toString();
            }
        }
        stringBuilder.append(" super ");
        appendAllTo(stringBuilder, " & ", lowerBounds);
        return stringBuilder.toString();
    }

    private static String genericArrayTypeToString(GenericArrayType genericArrayType) {
        return String.format("%s[]", new Object[]{toString(genericArrayType.getGenericComponentType())});
    }

    private static StringBuilder appendAllTo(StringBuilder stringBuilder, String str, Type... typeArr) {
        Validate.notEmpty(Validate.noNullElements((Object[]) typeArr));
        if (typeArr.length > 0) {
            stringBuilder.append(toString(typeArr[0]));
            for (int i = 1; i < typeArr.length; i++) {
                stringBuilder.append(str);
                stringBuilder.append(toString(typeArr[i]));
            }
        }
        return stringBuilder;
    }
}
