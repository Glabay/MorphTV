package kotlin.internal;

import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.jvm.JvmField;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\b\u0010\b\u001a\u00020\u0005H\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "getJavaVersion", "kotlin-stdlib"}, k = 2, mv = {1, 1, 10})
/* compiled from: PlatformImplementations.kt */
public final class PlatformImplementationsKt {
    @NotNull
    @JvmField
    public static final PlatformImplementations IMPLEMENTATIONS;

    static {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = getJavaVersion();
        r1 = 65544; // 0x10008 float:9.1847E-41 double:3.2383E-319;
        if (r0 < r1) goto L_0x0037;
    L_0x0009:
        r1 = "kotlin.internal.jdk8.JDK8PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x0020 }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x0020 }
        r1 = r1.newInstance();	 Catch:{ ClassNotFoundException -> 0x0020 }
        if (r1 != 0) goto L_0x001d;	 Catch:{ ClassNotFoundException -> 0x0020 }
    L_0x0015:
        r1 = new kotlin.TypeCastException;	 Catch:{ ClassNotFoundException -> 0x0020 }
        r2 = "null cannot be cast to non-null type kotlin.internal.PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x0020 }
        r1.<init>(r2);	 Catch:{ ClassNotFoundException -> 0x0020 }
        throw r1;	 Catch:{ ClassNotFoundException -> 0x0020 }
    L_0x001d:
        r1 = (kotlin.internal.PlatformImplementations) r1;	 Catch:{ ClassNotFoundException -> 0x0020 }
        goto L_0x0071;
    L_0x0020:
        r1 = "kotlin.internal.JRE8PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x0037 }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x0037 }
        r1 = r1.newInstance();	 Catch:{ ClassNotFoundException -> 0x0037 }
        if (r1 != 0) goto L_0x0034;	 Catch:{ ClassNotFoundException -> 0x0037 }
    L_0x002c:
        r1 = new kotlin.TypeCastException;	 Catch:{ ClassNotFoundException -> 0x0037 }
        r2 = "null cannot be cast to non-null type kotlin.internal.PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x0037 }
        r1.<init>(r2);	 Catch:{ ClassNotFoundException -> 0x0037 }
        throw r1;	 Catch:{ ClassNotFoundException -> 0x0037 }
    L_0x0034:
        r1 = (kotlin.internal.PlatformImplementations) r1;	 Catch:{ ClassNotFoundException -> 0x0037 }
        goto L_0x0071;
    L_0x0037:
        r1 = 65543; // 0x10007 float:9.1845E-41 double:3.23825E-319;
        if (r0 < r1) goto L_0x006c;
    L_0x003c:
        r0 = "kotlin.internal.jdk7.JDK7PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x0054 }
        r0 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x0054 }
        r0 = r0.newInstance();	 Catch:{ ClassNotFoundException -> 0x0054 }
        if (r0 != 0) goto L_0x0050;	 Catch:{ ClassNotFoundException -> 0x0054 }
    L_0x0048:
        r0 = new kotlin.TypeCastException;	 Catch:{ ClassNotFoundException -> 0x0054 }
        r1 = "null cannot be cast to non-null type kotlin.internal.PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x0054 }
        r0.<init>(r1);	 Catch:{ ClassNotFoundException -> 0x0054 }
        throw r0;	 Catch:{ ClassNotFoundException -> 0x0054 }
    L_0x0050:
        r1 = r0;	 Catch:{ ClassNotFoundException -> 0x0054 }
        r1 = (kotlin.internal.PlatformImplementations) r1;	 Catch:{ ClassNotFoundException -> 0x0054 }
        goto L_0x0071;
    L_0x0054:
        r0 = "kotlin.internal.JRE7PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x006c }
        r0 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x006c }
        r0 = r0.newInstance();	 Catch:{ ClassNotFoundException -> 0x006c }
        if (r0 != 0) goto L_0x0068;	 Catch:{ ClassNotFoundException -> 0x006c }
    L_0x0060:
        r0 = new kotlin.TypeCastException;	 Catch:{ ClassNotFoundException -> 0x006c }
        r1 = "null cannot be cast to non-null type kotlin.internal.PlatformImplementations";	 Catch:{ ClassNotFoundException -> 0x006c }
        r0.<init>(r1);	 Catch:{ ClassNotFoundException -> 0x006c }
        throw r0;	 Catch:{ ClassNotFoundException -> 0x006c }
    L_0x0068:
        r1 = r0;	 Catch:{ ClassNotFoundException -> 0x006c }
        r1 = (kotlin.internal.PlatformImplementations) r1;	 Catch:{ ClassNotFoundException -> 0x006c }
        goto L_0x0071;
    L_0x006c:
        r1 = new kotlin.internal.PlatformImplementations;
        r1.<init>();
    L_0x0071:
        IMPLEMENTATIONS = r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementationsKt.<clinit>():void");
    }

    private static final int getJavaVersion() {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = "java.specification.version";
        r0 = java.lang.System.getProperty(r0);
        r1 = 65542; // 0x10006 float:9.1844E-41 double:3.2382E-319;
        if (r0 == 0) goto L_0x006b;
    L_0x000b:
        r8 = r0;
        r8 = (java.lang.CharSequence) r8;
        r3 = 46;
        r4 = 0;
        r5 = 0;
        r6 = 6;
        r7 = 0;
        r2 = r8;
        r9 = kotlin.text.StringsKt.indexOf$default(r2, r3, r4, r5, r6, r7);
        r10 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        if (r9 >= 0) goto L_0x0024;
    L_0x001d:
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x0023 }
        r1 = r0 * r10;
    L_0x0023:
        return r1;
    L_0x0024:
        r3 = 46;
        r11 = r9 + 1;
        r5 = 0;
        r6 = 4;
        r7 = 0;
        r2 = r8;
        r4 = r11;
        r2 = kotlin.text.StringsKt.indexOf$default(r2, r3, r4, r5, r6, r7);
        if (r2 >= 0) goto L_0x0037;
    L_0x0033:
        r2 = r0.length();
    L_0x0037:
        r3 = 0;
        if (r0 != 0) goto L_0x0042;
    L_0x003a:
        r0 = new kotlin.TypeCastException;
        r1 = "null cannot be cast to non-null type java.lang.String";
        r0.<init>(r1);
        throw r0;
    L_0x0042:
        r3 = r0.substring(r3, r9);
        r4 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4);
        if (r0 != 0) goto L_0x0055;
    L_0x004d:
        r0 = new kotlin.TypeCastException;
        r1 = "null cannot be cast to non-null type java.lang.String";
        r0.<init>(r1);
        throw r0;
    L_0x0055:
        r0 = r0.substring(r11, r2);
        r2 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r0, r2);
        r2 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x006a }
        r2 = r2 * r10;	 Catch:{ NumberFormatException -> 0x006a }
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x006a }
        r1 = r2 + r0;
    L_0x006a:
        return r1;
    L_0x006b:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementationsKt.getJavaVersion():int");
    }

    @SinceKotlin(version = "1.2")
    @PublishedApi
    public static final boolean apiVersionIsAtLeast(int i, int i2, int i3) {
        return KotlinVersion.CURRENT.isAtLeast(i, i2, i3);
    }
}
