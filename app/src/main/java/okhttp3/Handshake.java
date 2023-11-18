package okhttp3;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;

public final class Handshake {
    private final CipherSuite cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;
    private final TlsVersion tlsVersion;

    private Handshake(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> list, List<Certificate> list2) {
        this.tlsVersion = tlsVersion;
        this.cipherSuite = cipherSuite;
        this.peerCertificates = list;
        this.localCertificates = list2;
    }

    public static okhttp3.Handshake get(javax.net.ssl.SSLSession r4) {
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
        r0 = r4.getCipherSuite();
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r4 = new java.lang.IllegalStateException;
        r0 = "cipherSuite == null";
        r4.<init>(r0);
        throw r4;
    L_0x000e:
        r0 = okhttp3.CipherSuite.forJavaName(r0);
        r1 = r4.getProtocol();
        if (r1 != 0) goto L_0x0020;
    L_0x0018:
        r4 = new java.lang.IllegalStateException;
        r0 = "tlsVersion == null";
        r4.<init>(r0);
        throw r4;
    L_0x0020:
        r1 = okhttp3.TlsVersion.forJavaName(r1);
        r2 = r4.getPeerCertificates();	 Catch:{ SSLPeerUnverifiedException -> 0x0029 }
        goto L_0x002a;
    L_0x0029:
        r2 = 0;
    L_0x002a:
        if (r2 == 0) goto L_0x0031;
    L_0x002c:
        r2 = okhttp3.internal.Util.immutableList(r2);
        goto L_0x0035;
    L_0x0031:
        r2 = java.util.Collections.emptyList();
    L_0x0035:
        r4 = r4.getLocalCertificates();
        if (r4 == 0) goto L_0x0040;
    L_0x003b:
        r4 = okhttp3.internal.Util.immutableList(r4);
        goto L_0x0044;
    L_0x0040:
        r4 = java.util.Collections.emptyList();
    L_0x0044:
        r3 = new okhttp3.Handshake;
        r3.<init>(r1, r0, r2, r4);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Handshake.get(javax.net.ssl.SSLSession):okhttp3.Handshake");
    }

    public static Handshake get(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> list, List<Certificate> list2) {
        if (tlsVersion == null) {
            throw new NullPointerException("tlsVersion == null");
        } else if (cipherSuite != null) {
            return new Handshake(tlsVersion, cipherSuite, Util.immutableList(list), Util.immutableList(list2));
        } else {
            throw new NullPointerException("cipherSuite == null");
        }
    }

    public TlsVersion tlsVersion() {
        return this.tlsVersion;
    }

    public CipherSuite cipherSuite() {
        return this.cipherSuite;
    }

    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }

    @Nullable
    public Principal peerPrincipal() {
        return !this.peerCertificates.isEmpty() ? ((X509Certificate) this.peerCertificates.get(0)).getSubjectX500Principal() : null;
    }

    public List<Certificate> localCertificates() {
        return this.localCertificates;
    }

    @Nullable
    public Principal localPrincipal() {
        return !this.localCertificates.isEmpty() ? ((X509Certificate) this.localCertificates.get(0)).getSubjectX500Principal() : null;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof Handshake)) {
            return false;
        }
        Handshake handshake = (Handshake) obj;
        if (this.tlsVersion.equals(handshake.tlsVersion) && this.cipherSuite.equals(handshake.cipherSuite) && this.peerCertificates.equals(handshake.peerCertificates) && this.localCertificates.equals(handshake.localCertificates) != null) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return ((((((527 + this.tlsVersion.hashCode()) * 31) + this.cipherSuite.hashCode()) * 31) + this.peerCertificates.hashCode()) * 31) + this.localCertificates.hashCode();
    }
}
