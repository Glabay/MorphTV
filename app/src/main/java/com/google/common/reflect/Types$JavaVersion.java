package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

enum Types$JavaVersion {
    JAVA6 {
        GenericArrayType newArrayType(Type type) {
            return new Types$GenericArrayTypeImpl(type);
        }

        Type usedInGenericType(Type type) {
            Preconditions.checkNotNull(type);
            if (type instanceof Class) {
                Class cls = (Class) type;
                if (cls.isArray()) {
                    return new Types$GenericArrayTypeImpl(cls.getComponentType());
                }
            }
            return type;
        }
    },
    JAVA7 {
        Type newArrayType(Type type) {
            if (type instanceof Class) {
                return Types.getArrayClass((Class) type);
            }
            return new Types$GenericArrayTypeImpl(type);
        }

        Type usedInGenericType(Type type) {
            return (Type) Preconditions.checkNotNull(type);
        }
    },
    JAVA8 {
        Type newArrayType(Type type) {
            return JAVA7.newArrayType(type);
        }

        Type usedInGenericType(Type type) {
            return JAVA7.usedInGenericType(type);
        }

        java.lang.String typeName(java.lang.reflect.Type r5) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = java.lang.reflect.Type.class;	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            r1 = "getTypeName";	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            r2 = 0;	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            r3 = new java.lang.Class[r2];	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            r0 = r0.getMethod(r1, r3);	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            r1 = new java.lang.Object[r2];	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            r5 = r0.invoke(r5, r1);	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            r5 = (java.lang.String) r5;	 Catch:{ NoSuchMethodException -> 0x0022, InvocationTargetException -> 0x001b, IllegalAccessException -> 0x0014 }
            return r5;
        L_0x0014:
            r5 = move-exception;
            r0 = new java.lang.RuntimeException;
            r0.<init>(r5);
            throw r0;
        L_0x001b:
            r5 = move-exception;
            r0 = new java.lang.RuntimeException;
            r0.<init>(r5);
            throw r0;
        L_0x0022:
            r5 = new java.lang.AssertionError;
            r0 = "Type.getTypeName should be available in Java 8";
            r5.<init>(r0);
            throw r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.reflect.Types.JavaVersion.3.typeName(java.lang.reflect.Type):java.lang.String");
        }
    };
    
    static final Types$JavaVersion CURRENT = null;

    /* renamed from: com.google.common.reflect.Types$JavaVersion$4 */
    static class C11914 extends TypeCapture<int[]> {
        C11914() {
        }
    }

    abstract Type newArrayType(Type type);

    abstract Type usedInGenericType(Type type);

    String typeName(Type type) {
        return Types.toString(type);
    }

    final ImmutableList<Type> usedInGenericType(Type[] typeArr) {
        Builder builder = ImmutableList.builder();
        for (Type usedInGenericType : typeArr) {
            builder.add(usedInGenericType(usedInGenericType));
        }
        return builder.build();
    }
}
