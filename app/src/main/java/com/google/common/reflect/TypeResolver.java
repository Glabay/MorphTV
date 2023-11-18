package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

@Beta
public final class TypeResolver {
    private final TypeTable typeTable;

    private static final class TypeMappingIntrospector extends TypeVisitor {
        private static final TypeResolver$WildcardCapturer wildcardCapturer = new TypeResolver$WildcardCapturer(null);
        private final Map<TypeVariableKey, Type> mappings = Maps.newHashMap();

        private TypeMappingIntrospector() {
        }

        static ImmutableMap<TypeVariableKey, Type> getTypeMappings(Type type) {
            TypeMappingIntrospector typeMappingIntrospector = new TypeMappingIntrospector();
            typeMappingIntrospector.visit(wildcardCapturer.capture(type));
            return ImmutableMap.copyOf(typeMappingIntrospector.mappings);
        }

        void visitClass(Class<?> cls) {
            visit(cls.getGenericSuperclass());
            visit(cls.getGenericInterfaces());
        }

        void visitParameterizedType(ParameterizedType parameterizedType) {
            TypeVariable[] typeParameters = ((Class) parameterizedType.getRawType()).getTypeParameters();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(typeParameters.length == actualTypeArguments.length);
            for (int i = 0; i < typeParameters.length; i++) {
                map(new TypeVariableKey(typeParameters[i]), actualTypeArguments[i]);
            }
            visit(r0);
            visit(parameterizedType.getOwnerType());
        }

        void visitTypeVariable(TypeVariable<?> typeVariable) {
            visit(typeVariable.getBounds());
        }

        void visitWildcardType(WildcardType wildcardType) {
            visit(wildcardType.getUpperBounds());
        }

        private void map(TypeVariableKey typeVariableKey, Type type) {
            if (!this.mappings.containsKey(typeVariableKey)) {
                Type type2 = type;
                while (type2 != null) {
                    if (typeVariableKey.equalsType(type2)) {
                        while (type != null) {
                            type = (Type) this.mappings.remove(TypeVariableKey.forLookup(type));
                        }
                        return;
                    }
                    type2 = (Type) this.mappings.get(TypeVariableKey.forLookup(type2));
                }
                this.mappings.put(typeVariableKey, type);
            }
        }
    }

    private static class TypeTable {
        private final ImmutableMap<TypeVariableKey, Type> map;

        TypeTable() {
            this.map = ImmutableMap.of();
        }

        private TypeTable(ImmutableMap<TypeVariableKey, Type> immutableMap) {
            this.map = immutableMap;
        }

        final TypeTable where(Map<TypeVariableKey, ? extends Type> map) {
            Builder builder = ImmutableMap.builder();
            builder.putAll(this.map);
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                TypeVariableKey typeVariableKey = (TypeVariableKey) entry.getKey();
                Type type = (Type) entry.getValue();
                Preconditions.checkArgument(typeVariableKey.equalsType(type) ^ true, "Type variable %s bound to itself", new Object[]{typeVariableKey});
                builder.put(typeVariableKey, type);
            }
            return new TypeTable(builder.build());
        }

        final Type resolve(final TypeVariable<?> typeVariable) {
            return resolveInternal(typeVariable, new TypeTable() {
                public Type resolveInternal(TypeVariable<?> typeVariable, TypeTable typeTable) {
                    if (typeVariable.getGenericDeclaration().equals(typeVariable.getGenericDeclaration())) {
                        return typeVariable;
                    }
                    return this.resolveInternal(typeVariable, typeTable);
                }
            });
        }

        Type resolveInternal(TypeVariable<?> typeVariable, TypeTable typeTable) {
            Type type = (Type) this.map.get(new TypeVariableKey(typeVariable));
            if (type != null) {
                return new TypeResolver(typeTable).resolveType(type);
            }
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length == 0) {
                return typeVariable;
            }
            typeTable = new TypeResolver(typeTable).resolveTypes(bounds);
            if (Types$NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals(bounds, typeTable)) {
                return typeVariable;
            }
            return Types.newArtificialTypeVariable(typeVariable.getGenericDeclaration(), typeVariable.getName(), typeTable);
        }
    }

    static final class TypeVariableKey {
        private final TypeVariable<?> var;

        TypeVariableKey(TypeVariable<?> typeVariable) {
            this.var = (TypeVariable) Preconditions.checkNotNull(typeVariable);
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.var.getGenericDeclaration(), this.var.getName()});
        }

        public boolean equals(Object obj) {
            return obj instanceof TypeVariableKey ? equalsTypeVariable(((TypeVariableKey) obj).var) : null;
        }

        public String toString() {
            return this.var.toString();
        }

        static Object forLookup(Type type) {
            return type instanceof TypeVariable ? new TypeVariableKey((TypeVariable) type) : null;
        }

        boolean equalsType(Type type) {
            return type instanceof TypeVariable ? equalsTypeVariable((TypeVariable) type) : null;
        }

        private boolean equalsTypeVariable(TypeVariable<?> typeVariable) {
            return (!this.var.getGenericDeclaration().equals(typeVariable.getGenericDeclaration()) || this.var.getName().equals(typeVariable.getName()) == null) ? null : true;
        }
    }

    public TypeResolver() {
        this.typeTable = new TypeTable();
    }

    private TypeResolver(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    static TypeResolver accordingTo(Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }

    public TypeResolver where(Type type, Type type2) {
        Map newHashMap = Maps.newHashMap();
        populateTypeMappings(newHashMap, (Type) Preconditions.checkNotNull(type), (Type) Preconditions.checkNotNull(type2));
        return where(newHashMap);
    }

    TypeResolver where(Map<TypeVariableKey, ? extends Type> map) {
        return new TypeResolver(this.typeTable.where(map));
    }

    private static void populateTypeMappings(final Map<TypeVariableKey, Type> map, Type type, final Type type2) {
        if (!type.equals(type2)) {
            new TypeVisitor() {
                void visitTypeVariable(TypeVariable<?> typeVariable) {
                    map.put(new TypeVariableKey(typeVariable), type2);
                }

                void visitWildcardType(WildcardType wildcardType) {
                    WildcardType wildcardType2 = (WildcardType) TypeResolver.expectArgument(WildcardType.class, type2);
                    Type[] upperBounds = wildcardType.getUpperBounds();
                    Type[] upperBounds2 = wildcardType2.getUpperBounds();
                    Type[] lowerBounds = wildcardType.getLowerBounds();
                    Type[] lowerBounds2 = wildcardType2.getLowerBounds();
                    int i = 0;
                    boolean z = upperBounds.length == upperBounds2.length && lowerBounds.length == lowerBounds2.length;
                    Preconditions.checkArgument(z, "Incompatible type: %s vs. %s", new Object[]{wildcardType, type2});
                    for (wildcardType = null; wildcardType < upperBounds.length; wildcardType++) {
                        TypeResolver.populateTypeMappings(map, upperBounds[wildcardType], upperBounds2[wildcardType]);
                    }
                    while (i < lowerBounds.length) {
                        TypeResolver.populateTypeMappings(map, lowerBounds[i], lowerBounds2[i]);
                        i++;
                    }
                }

                void visitParameterizedType(ParameterizedType parameterizedType) {
                    ParameterizedType parameterizedType2 = (ParameterizedType) TypeResolver.expectArgument(ParameterizedType.class, type2);
                    r4 = new Object[2];
                    int i = 0;
                    r4[0] = parameterizedType;
                    r4[1] = type2;
                    Preconditions.checkArgument(parameterizedType.getRawType().equals(parameterizedType2.getRawType()), "Inconsistent raw type: %s vs. %s", r4);
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
                    Preconditions.checkArgument(actualTypeArguments.length == actualTypeArguments2.length, "%s not compatible with %s", new Object[]{parameterizedType, parameterizedType2});
                    while (i < actualTypeArguments.length) {
                        TypeResolver.populateTypeMappings(map, actualTypeArguments[i], actualTypeArguments2[i]);
                        i++;
                    }
                }

                void visitGenericArrayType(GenericArrayType genericArrayType) {
                    Type componentType = Types.getComponentType(type2);
                    Preconditions.checkArgument(componentType != null, "%s is not an array type.", new Object[]{type2});
                    TypeResolver.populateTypeMappings(map, genericArrayType.getGenericComponentType(), componentType);
                }

                void visitClass(Class<?> cls) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("No type mapping from ");
                    stringBuilder.append(cls);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }.visit(new Type[]{type});
        }
    }

    public Type resolveType(Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof TypeVariable) {
            return this.typeTable.resolve((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type);
        }
        if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type);
        }
        return type instanceof WildcardType ? resolveWildcardType((WildcardType) type) : type;
    }

    private Type[] resolveTypes(Type[] typeArr) {
        Type[] typeArr2 = new Type[typeArr.length];
        for (int i = 0; i < typeArr.length; i++) {
            typeArr2[i] = resolveType(typeArr[i]);
        }
        return typeArr2;
    }

    private WildcardType resolveWildcardType(WildcardType wildcardType) {
        return new Types$WildcardTypeImpl(resolveTypes(wildcardType.getLowerBounds()), resolveTypes(wildcardType.getUpperBounds()));
    }

    private Type resolveGenericArrayType(GenericArrayType genericArrayType) {
        return Types.newArrayType(resolveType(genericArrayType.getGenericComponentType()));
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType parameterizedType) {
        Type ownerType = parameterizedType.getOwnerType();
        if (ownerType == null) {
            ownerType = null;
        } else {
            ownerType = resolveType(ownerType);
        }
        return Types.newParameterizedTypeWithOwner(ownerType, (Class) resolveType(parameterizedType.getRawType()), resolveTypes(parameterizedType.getActualTypeArguments()));
    }

    private static <T> T expectArgument(java.lang.Class<T> r2, java.lang.Object r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = r2.cast(r3);	 Catch:{ ClassCastException -> 0x0005 }
        return r0;
    L_0x0005:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r3);
        r3 = " is not a ";
        r1.append(r3);
        r2 = r2.getSimpleName();
        r1.append(r2);
        r2 = r1.toString();
        r0.<init>(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.reflect.TypeResolver.expectArgument(java.lang.Class, java.lang.Object):T");
    }
}
