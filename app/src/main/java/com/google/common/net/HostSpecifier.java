package com.google.common.net;

import com.google.common.annotations.Beta;
import java.text.ParseException;
import javax.annotation.Nullable;

@Beta
public final class HostSpecifier {
    private final String canonicalForm;

    private HostSpecifier(String str) {
        this.canonicalForm = str;
    }

    public static com.google.common.net.HostSpecifier fromValid(java.lang.String r3) {
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
        r3 = com.google.common.net.HostAndPort.fromString(r3);
        r0 = r3.hasPort();
        r0 = r0 ^ 1;
        com.google.common.base.Preconditions.checkArgument(r0);
        r3 = r3.getHostText();
        r0 = 0;
        r1 = com.google.common.net.InetAddresses.forString(r3);	 Catch:{ IllegalArgumentException -> 0x0017 }
        r0 = r1;
    L_0x0017:
        if (r0 == 0) goto L_0x0023;
    L_0x0019:
        r3 = new com.google.common.net.HostSpecifier;
        r0 = com.google.common.net.InetAddresses.toUriString(r0);
        r3.<init>(r0);
        return r3;
    L_0x0023:
        r0 = com.google.common.net.InternetDomainName.from(r3);
        r1 = r0.hasPublicSuffix();
        if (r1 == 0) goto L_0x0037;
    L_0x002d:
        r3 = new com.google.common.net.HostSpecifier;
        r0 = r0.toString();
        r3.<init>(r0);
        return r3;
    L_0x0037:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Domain name does not have a recognized public suffix: ";
        r1.append(r2);
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.HostSpecifier.fromValid(java.lang.String):com.google.common.net.HostSpecifier");
    }

    public static HostSpecifier from(String str) throws ParseException {
        try {
            return fromValid(str);
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid host specifier: ");
            stringBuilder.append(str);
            ParseException parseException = new ParseException(stringBuilder.toString(), 0);
            parseException.initCause(e);
            throw parseException;
        }
    }

    public static boolean isValid(java.lang.String r0) {
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
        fromValid(r0);	 Catch:{ IllegalArgumentException -> 0x0005 }
        r0 = 1;
        return r0;
    L_0x0005:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.HostSpecifier.isValid(java.lang.String):boolean");
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostSpecifier)) {
            return null;
        }
        return this.canonicalForm.equals(((HostSpecifier) obj).canonicalForm);
    }

    public int hashCode() {
        return this.canonicalForm.hashCode();
    }

    public String toString() {
        return this.canonicalForm;
    }
}
