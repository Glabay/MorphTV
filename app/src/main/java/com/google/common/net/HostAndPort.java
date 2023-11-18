package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
@Beta
public final class HostAndPort implements Serializable {
    private static final int NO_PORT = -1;
    private static final long serialVersionUID = 0;
    private final boolean hasBracketlessColons;
    private final String host;
    private final int port;

    private static boolean isValidPort(int i) {
        return i >= 0 && i <= 65535;
    }

    private HostAndPort(String str, int i, boolean z) {
        this.host = str;
        this.port = i;
        this.hasBracketlessColons = z;
    }

    public String getHostText() {
        return this.host;
    }

    public boolean hasPort() {
        return this.port >= 0;
    }

    public int getPort() {
        Preconditions.checkState(hasPort());
        return this.port;
    }

    public int getPortOrDefault(int i) {
        return hasPort() ? this.port : i;
    }

    public static HostAndPort fromParts(String str, int i) {
        Preconditions.checkArgument(isValidPort(i), "Port out of range: %s", Integer.valueOf(i));
        HostAndPort fromString = fromString(str);
        Preconditions.checkArgument(fromString.hasPort() ^ true, "Host has a port: %s", str);
        return new HostAndPort(fromString.host, i, fromString.hasBracketlessColons);
    }

    public static HostAndPort fromHost(String str) {
        HostAndPort fromString = fromString(str);
        Preconditions.checkArgument(fromString.hasPort() ^ true, "Host has a port: %s", str);
        return fromString;
    }

    public static com.google.common.net.HostAndPort fromString(java.lang.String r9) {
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
        com.google.common.base.Preconditions.checkNotNull(r9);
        r0 = "[";
        r0 = r9.startsWith(r0);
        r1 = -1;
        r2 = 1;
        r3 = 0;
        if (r0 == 0) goto L_0x001b;
    L_0x000e:
        r0 = getHostAndPortFromBracketedHost(r9);
        r4 = r0[r3];
        r0 = r0[r2];
        r5 = 0;
        r8 = r4;
        r4 = r0;
        r0 = r8;
        goto L_0x003d;
    L_0x001b:
        r0 = 58;
        r4 = r9.indexOf(r0);
        if (r4 < 0) goto L_0x0035;
    L_0x0023:
        r5 = r4 + 1;
        r0 = r9.indexOf(r0, r5);
        if (r0 != r1) goto L_0x0035;
    L_0x002b:
        r0 = r9.substring(r3, r4);
        r4 = r9.substring(r5);
        r5 = 0;
        goto L_0x003d;
    L_0x0035:
        if (r4 < 0) goto L_0x0039;
    L_0x0037:
        r0 = 1;
        goto L_0x003a;
    L_0x0039:
        r0 = 0;
    L_0x003a:
        r4 = 0;
        r5 = r0;
        r0 = r9;
    L_0x003d:
        r6 = com.google.common.base.Strings.isNullOrEmpty(r4);
        if (r6 != 0) goto L_0x007c;
    L_0x0043:
        r1 = "+";
        r1 = r4.startsWith(r1);
        r1 = r1 ^ r2;
        r6 = "Unparseable port number: %s";
        r7 = new java.lang.Object[r2];
        r7[r3] = r9;
        com.google.common.base.Preconditions.checkArgument(r1, r6, r7);
        r1 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x0065 }
        r4 = isValidPort(r1);
        r6 = "Port number out of range: %s";
        r2 = new java.lang.Object[r2];
        r2[r3] = r9;
        com.google.common.base.Preconditions.checkArgument(r4, r6, r2);
        goto L_0x007c;
    L_0x0065:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unparseable port number: ";
        r1.append(r2);
        r1.append(r9);
        r9 = r1.toString();
        r0.<init>(r9);
        throw r0;
    L_0x007c:
        r9 = new com.google.common.net.HostAndPort;
        r9.<init>(r0, r1, r5);
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.HostAndPort.fromString(java.lang.String):com.google.common.net.HostAndPort");
    }

    private static String[] getHostAndPortFromBracketedHost(String str) {
        Preconditions.checkArgument(str.charAt(0) == '[', "Bracketed host-port string must start with a bracket: %s", str);
        int indexOf = str.indexOf(58);
        int lastIndexOf = str.lastIndexOf(93);
        boolean z = indexOf > -1 && lastIndexOf > indexOf;
        Preconditions.checkArgument(z, "Invalid bracketed host/port: %s", str);
        String substring = str.substring(1, lastIndexOf);
        int i = lastIndexOf + 1;
        if (i == str.length()) {
            return new String[]{substring, ""};
        }
        Preconditions.checkArgument(str.charAt(i) == ':', "Only a colon may follow a close bracket: %s", str);
        for (int i2 = lastIndexOf + 2; i2 < str.length(); i2++) {
            Preconditions.checkArgument(Character.isDigit(str.charAt(i2)), "Port must be numeric: %s", str);
        }
        return new String[]{substring, str.substring(lastIndexOf)};
    }

    public HostAndPort withDefaultPort(int i) {
        Preconditions.checkArgument(isValidPort(i));
        if (!hasPort()) {
            if (this.port != i) {
                return new HostAndPort(this.host, i, this.hasBracketlessColons);
            }
        }
        return this;
    }

    public HostAndPort requireBracketsForIPv6() {
        Preconditions.checkArgument(this.hasBracketlessColons ^ true, "Possible bracketless IPv6 literal: %s", this.host);
        return this;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostAndPort)) {
            return false;
        }
        HostAndPort hostAndPort = (HostAndPort) obj;
        if (!Objects.equal(this.host, hostAndPort.host) || this.port != hostAndPort.port || this.hasBracketlessColons != hostAndPort.hasBracketlessColons) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hashCode(this.host, Integer.valueOf(this.port), Boolean.valueOf(this.hasBracketlessColons));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.host.length() + 8);
        if (this.host.indexOf(58) >= 0) {
            stringBuilder.append('[');
            stringBuilder.append(this.host);
            stringBuilder.append(']');
        } else {
            stringBuilder.append(this.host);
        }
        if (hasPort()) {
            stringBuilder.append(':');
            stringBuilder.append(this.port);
        }
        return stringBuilder.toString();
    }
}
