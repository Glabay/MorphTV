package com.google.gson.internal.reflect;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

final class UnsafeReflectionAccessor extends ReflectionAccessor {
    private static Class unsafeClass;
    private final Field overrideField = getOverrideField();
    private final Object theUnsafe = getUnsafeInstance();

    UnsafeReflectionAccessor() {
    }

    public void makeAccessible(AccessibleObject accessibleObject) {
        if (!makeAccessibleWithUnsafe(accessibleObject)) {
            try {
                accessibleObject.setAccessible(true);
            } catch (Throwable e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Gson couldn't modify fields for ");
                stringBuilder.append(accessibleObject);
                stringBuilder.append("\nand sun.misc.Unsafe not found.\nEither write a custom type adapter, or make fields accessible, or include sun.misc.Unsafe.");
                throw new JsonIOException(stringBuilder.toString(), e);
            }
        }
    }

    boolean makeAccessibleWithUnsafe(java.lang.reflect.AccessibleObject r11) {
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
        r10 = this;
        r0 = r10.theUnsafe;
        r1 = 0;
        if (r0 == 0) goto L_0x0058;
    L_0x0005:
        r0 = r10.overrideField;
        if (r0 == 0) goto L_0x0058;
    L_0x0009:
        r0 = unsafeClass;	 Catch:{ Exception -> 0x0058 }
        r2 = "objectFieldOffset";	 Catch:{ Exception -> 0x0058 }
        r3 = 1;	 Catch:{ Exception -> 0x0058 }
        r4 = new java.lang.Class[r3];	 Catch:{ Exception -> 0x0058 }
        r5 = java.lang.reflect.Field.class;	 Catch:{ Exception -> 0x0058 }
        r4[r1] = r5;	 Catch:{ Exception -> 0x0058 }
        r0 = r0.getMethod(r2, r4);	 Catch:{ Exception -> 0x0058 }
        r2 = r10.theUnsafe;	 Catch:{ Exception -> 0x0058 }
        r4 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0058 }
        r5 = r10.overrideField;	 Catch:{ Exception -> 0x0058 }
        r4[r1] = r5;	 Catch:{ Exception -> 0x0058 }
        r0 = r0.invoke(r2, r4);	 Catch:{ Exception -> 0x0058 }
        r0 = (java.lang.Long) r0;	 Catch:{ Exception -> 0x0058 }
        r4 = r0.longValue();	 Catch:{ Exception -> 0x0058 }
        r0 = unsafeClass;	 Catch:{ Exception -> 0x0058 }
        r2 = "putBoolean";	 Catch:{ Exception -> 0x0058 }
        r6 = 3;	 Catch:{ Exception -> 0x0058 }
        r7 = new java.lang.Class[r6];	 Catch:{ Exception -> 0x0058 }
        r8 = java.lang.Object.class;	 Catch:{ Exception -> 0x0058 }
        r7[r1] = r8;	 Catch:{ Exception -> 0x0058 }
        r8 = java.lang.Long.TYPE;	 Catch:{ Exception -> 0x0058 }
        r7[r3] = r8;	 Catch:{ Exception -> 0x0058 }
        r8 = java.lang.Boolean.TYPE;	 Catch:{ Exception -> 0x0058 }
        r9 = 2;	 Catch:{ Exception -> 0x0058 }
        r7[r9] = r8;	 Catch:{ Exception -> 0x0058 }
        r0 = r0.getMethod(r2, r7);	 Catch:{ Exception -> 0x0058 }
        r2 = r10.theUnsafe;	 Catch:{ Exception -> 0x0058 }
        r6 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x0058 }
        r6[r1] = r11;	 Catch:{ Exception -> 0x0058 }
        r11 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x0058 }
        r6[r3] = r11;	 Catch:{ Exception -> 0x0058 }
        r11 = java.lang.Boolean.valueOf(r3);	 Catch:{ Exception -> 0x0058 }
        r6[r9] = r11;	 Catch:{ Exception -> 0x0058 }
        r0.invoke(r2, r6);	 Catch:{ Exception -> 0x0058 }
        return r3;
    L_0x0058:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.reflect.UnsafeReflectionAccessor.makeAccessibleWithUnsafe(java.lang.reflect.AccessibleObject):boolean");
    }

    private static java.lang.Object getUnsafeInstance() {
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
        r0 = 0;
        r1 = "sun.misc.Unsafe";	 Catch:{ Exception -> 0x001a }
        r1 = java.lang.Class.forName(r1);	 Catch:{ Exception -> 0x001a }
        unsafeClass = r1;	 Catch:{ Exception -> 0x001a }
        r1 = unsafeClass;	 Catch:{ Exception -> 0x001a }
        r2 = "theUnsafe";	 Catch:{ Exception -> 0x001a }
        r1 = r1.getDeclaredField(r2);	 Catch:{ Exception -> 0x001a }
        r2 = 1;	 Catch:{ Exception -> 0x001a }
        r1.setAccessible(r2);	 Catch:{ Exception -> 0x001a }
        r1 = r1.get(r0);	 Catch:{ Exception -> 0x001a }
        return r1;
    L_0x001a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.reflect.UnsafeReflectionAccessor.getUnsafeInstance():java.lang.Object");
    }

    private static java.lang.reflect.Field getOverrideField() {
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
        r0 = java.lang.reflect.AccessibleObject.class;	 Catch:{ NoSuchFieldException -> 0x0009 }
        r1 = "override";	 Catch:{ NoSuchFieldException -> 0x0009 }
        r0 = r0.getDeclaredField(r1);	 Catch:{ NoSuchFieldException -> 0x0009 }
        return r0;
    L_0x0009:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.reflect.UnsafeReflectionAccessor.getOverrideField():java.lang.reflect.Field");
    }
}
