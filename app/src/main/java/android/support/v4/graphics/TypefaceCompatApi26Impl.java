package android.support.v4.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.v4.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import android.support.v4.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

@RequiresApi(26)
@RestrictTo({Scope.LIBRARY_GROUP})
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
    private static final String ABORT_CREATION_METHOD = "abortCreation";
    private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
    private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String FREEZE_METHOD = "freeze";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi26Impl";
    private static final Method sAbortCreation;
    private static final Method sAddFontFromAssetManager;
    private static final Method sAddFontFromBuffer;
    private static final Method sCreateFromFamiliesWithDefault;
    private static final Class sFontFamily;
    private static final Constructor sFontFamilyCtor;
    private static final Method sFreeze;

    static {
        Class cls;
        Method method;
        Method method2;
        Method method3;
        Method method4;
        Method declaredMethod;
        Constructor constructor = null;
        try {
            cls = Class.forName(FONT_FAMILY_CLASS);
            Constructor constructor2 = cls.getConstructor(new Class[0]);
            method = cls.getMethod(ADD_FONT_FROM_ASSET_MANAGER_METHOD, new Class[]{AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class});
            method2 = cls.getMethod(ADD_FONT_FROM_BUFFER_METHOD, new Class[]{ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE});
            method3 = cls.getMethod(FREEZE_METHOD, new Class[0]);
            method4 = cls.getMethod(ABORT_CREATION_METHOD, new Class[0]);
            Object newInstance = Array.newInstance(cls, 1);
            declaredMethod = Typeface.class.getDeclaredMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, new Class[]{newInstance.getClass(), Integer.TYPE, Integer.TYPE});
            declaredMethod.setAccessible(true);
            constructor = constructor2;
        } catch (Throwable e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to collect necessary methods for class ");
            stringBuilder.append(e.getClass().getName());
            Log.e(str, stringBuilder.toString(), e);
            cls = null;
            declaredMethod = cls;
            method = declaredMethod;
            method2 = method;
            method3 = method2;
            method4 = method3;
        }
        sFontFamilyCtor = constructor;
        sFontFamily = cls;
        sAddFontFromAssetManager = method;
        sAddFontFromBuffer = method2;
        sFreeze = method3;
        sAbortCreation = method4;
        sCreateFromFamiliesWithDefault = declaredMethod;
    }

    private static boolean isFontFamilyPrivateAPIAvailable() {
        if (sAddFontFromAssetManager == null) {
            Log.w(TAG, "Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        return sAddFontFromAssetManager != null;
    }

    private static Object newFamily() {
        try {
            return sFontFamilyCtor.newInstance(new Object[0]);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean addFontFromAssetManager(Context context, Object obj, String str, int i, int i2, int i3) {
        try {
            return ((Boolean) sAddFontFromAssetManager.invoke(obj, new Object[]{context.getAssets(), str, Integer.valueOf(0), Boolean.valueOf(false), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), null})).booleanValue();
        } catch (Context context2) {
            throw new RuntimeException(context2);
        }
    }

    private static boolean addFontFromBuffer(Object obj, ByteBuffer byteBuffer, int i, int i2, int i3) {
        try {
            return ((Boolean) sAddFontFromBuffer.invoke(obj, new Object[]{byteBuffer, Integer.valueOf(i), 0, Integer.valueOf(i2), Integer.valueOf(i3)})).booleanValue();
        } catch (Object obj2) {
            throw new RuntimeException(obj2);
        }
    }

    private static Typeface createFromFamiliesWithDefault(Object obj) {
        try {
            Array.set(Array.newInstance(sFontFamily, 1), 0, obj);
            return (Typeface) sCreateFromFamiliesWithDefault.invoke(null, new Object[]{r0, Integer.valueOf(-1), Integer.valueOf(-1)});
        } catch (Object obj2) {
            throw new RuntimeException(obj2);
        }
    }

    private static boolean freeze(Object obj) {
        try {
            return ((Boolean) sFreeze.invoke(obj, new Object[0])).booleanValue();
        } catch (Object obj2) {
            throw new RuntimeException(obj2);
        }
    }

    private static void abortCreation(Object obj) {
        try {
            sAbortCreation.invoke(obj, new Object[0]);
        } catch (Object obj2) {
            throw new RuntimeException(obj2);
        }
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        resources = newFamily();
        fontFamilyFilesResourceEntry = fontFamilyFilesResourceEntry.getEntries();
        i = fontFamilyFilesResourceEntry.length;
        int i2 = 0;
        while (i2 < i) {
            FontFileResourceEntry fontFileResourceEntry = fontFamilyFilesResourceEntry[i2];
            if (addFontFromAssetManager(context, resources, fontFileResourceEntry.getFileName(), 0, fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) {
                i2++;
            } else {
                abortCreation(resources);
                return null;
            }
        }
        if (freeze(resources) == null) {
            return null;
        }
        return createFromFamiliesWithDefault(resources);
    }

    public android.graphics.Typeface createFromFontInfo(android.content.Context r10, @android.support.annotation.Nullable android.os.CancellationSignal r11, @android.support.annotation.NonNull android.support.v4.provider.FontsContractCompat.FontInfo[] r12, int r13) {
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
        r9 = this;
        r0 = r12.length;
        r1 = 1;
        r2 = 0;
        if (r0 >= r1) goto L_0x0006;
    L_0x0005:
        return r2;
    L_0x0006:
        r0 = isFontFamilyPrivateAPIAvailable();
        if (r0 != 0) goto L_0x0064;
    L_0x000c:
        r12 = r9.findBestInfo(r12, r13);
        r10 = r10.getContentResolver();
        r13 = r12.getUri();	 Catch:{ IOException -> 0x0063 }
        r0 = "r";	 Catch:{ IOException -> 0x0063 }
        r10 = r10.openFileDescriptor(r13, r0, r11);	 Catch:{ IOException -> 0x0063 }
        if (r10 != 0) goto L_0x0026;	 Catch:{ IOException -> 0x0063 }
    L_0x0020:
        if (r10 == 0) goto L_0x0025;	 Catch:{ IOException -> 0x0063 }
    L_0x0022:
        r10.close();	 Catch:{ IOException -> 0x0063 }
    L_0x0025:
        return r2;
    L_0x0026:
        r11 = new android.graphics.Typeface$Builder;	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r13 = r10.getFileDescriptor();	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r11.<init>(r13);	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r13 = r12.getWeight();	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r11 = r11.setWeight(r13);	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r12 = r12.isItalic();	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r11 = r11.setItalic(r12);	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        r11 = r11.build();	 Catch:{ Throwable -> 0x004c, all -> 0x0049 }
        if (r10 == 0) goto L_0x0048;
    L_0x0045:
        r10.close();	 Catch:{ IOException -> 0x0063 }
    L_0x0048:
        return r11;
    L_0x0049:
        r11 = move-exception;
        r12 = r2;
        goto L_0x0052;
    L_0x004c:
        r11 = move-exception;
        throw r11;	 Catch:{ all -> 0x004e }
    L_0x004e:
        r12 = move-exception;
        r8 = r12;
        r12 = r11;
        r11 = r8;
    L_0x0052:
        if (r10 == 0) goto L_0x0062;
    L_0x0054:
        if (r12 == 0) goto L_0x005f;
    L_0x0056:
        r10.close();	 Catch:{ Throwable -> 0x005a }
        goto L_0x0062;
    L_0x005a:
        r10 = move-exception;
        r12.addSuppressed(r10);	 Catch:{ IOException -> 0x0063 }
        goto L_0x0062;	 Catch:{ IOException -> 0x0063 }
    L_0x005f:
        r10.close();	 Catch:{ IOException -> 0x0063 }
    L_0x0062:
        throw r11;	 Catch:{ IOException -> 0x0063 }
    L_0x0063:
        return r2;
    L_0x0064:
        r10 = android.support.v4.provider.FontsContractCompat.prepareFontData(r10, r12, r11);
        r11 = newFamily();
        r0 = r12.length;
        r3 = 0;
        r4 = 0;
    L_0x006f:
        if (r3 >= r0) goto L_0x009a;
    L_0x0071:
        r5 = r12[r3];
        r6 = r5.getUri();
        r6 = r10.get(r6);
        r6 = (java.nio.ByteBuffer) r6;
        if (r6 != 0) goto L_0x0080;
    L_0x007f:
        goto L_0x0097;
    L_0x0080:
        r4 = r5.getTtcIndex();
        r7 = r5.getWeight();
        r5 = r5.isItalic();
        r4 = addFontFromBuffer(r11, r6, r4, r7, r5);
        if (r4 != 0) goto L_0x0096;
    L_0x0092:
        abortCreation(r11);
        return r2;
    L_0x0096:
        r4 = 1;
    L_0x0097:
        r3 = r3 + 1;
        goto L_0x006f;
    L_0x009a:
        if (r4 != 0) goto L_0x00a0;
    L_0x009c:
        abortCreation(r11);
        return r2;
    L_0x00a0:
        r10 = freeze(r11);
        if (r10 != 0) goto L_0x00a7;
    L_0x00a6:
        return r2;
    L_0x00a7:
        r10 = createFromFamiliesWithDefault(r11);
        r10 = android.graphics.Typeface.create(r10, r13);
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatApi26Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, android.support.v4.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }

    @Nullable
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, i, str, i2);
        }
        resources = newFamily();
        if (addFontFromAssetManager(context, resources, str, 0, -1, -1) == null) {
            abortCreation(resources);
            return null;
        } else if (freeze(resources) == null) {
            return null;
        } else {
            return createFromFamiliesWithDefault(resources);
        }
    }
}
