package okhttp3.internal.tls;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;

public final class BasicCertificateChainCleaner extends CertificateChainCleaner {
    private static final int MAX_SIGNERS = 9;
    private final TrustRootIndex trustRootIndex;

    public BasicCertificateChainCleaner(TrustRootIndex trustRootIndex) {
        this.trustRootIndex = trustRootIndex;
    }

    public List<Certificate> clean(List<Certificate> list, String str) throws SSLPeerUnverifiedException {
        str = new ArrayDeque(list);
        list = new ArrayList();
        list.add(str.removeFirst());
        Object obj = null;
        for (int i = 0; i < 9; i++) {
            X509Certificate x509Certificate = (X509Certificate) list.get(list.size() - 1);
            X509Certificate findByIssuerAndSignature = this.trustRootIndex.findByIssuerAndSignature(x509Certificate);
            if (findByIssuerAndSignature != null) {
                if (list.size() > 1 || !x509Certificate.equals(findByIssuerAndSignature)) {
                    list.add(findByIssuerAndSignature);
                }
                if (verifySignature(findByIssuerAndSignature, findByIssuerAndSignature)) {
                    return list;
                }
                obj = 1;
            } else {
                Iterator it = str.iterator();
                while (it.hasNext()) {
                    findByIssuerAndSignature = (X509Certificate) it.next();
                    if (verifySignature(x509Certificate, findByIssuerAndSignature)) {
                        it.remove();
                        list.add(findByIssuerAndSignature);
                    }
                }
                if (obj != null) {
                    return list;
                }
                str = new StringBuilder();
                str.append("Failed to find a trusted cert that signed ");
                str.append(x509Certificate);
                throw new SSLPeerUnverifiedException(str.toString());
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Certificate chain too long: ");
        stringBuilder.append(list);
        throw new SSLPeerUnverifiedException(stringBuilder.toString());
    }

    private boolean verifySignature(java.security.cert.X509Certificate r3, java.security.cert.X509Certificate r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = this;
        r0 = r3.getIssuerDN();
        r1 = r4.getSubjectDN();
        r0 = r0.equals(r1);
        r1 = 0;
        if (r0 != 0) goto L_0x0010;
    L_0x000f:
        return r1;
    L_0x0010:
        r4 = r4.getPublicKey();	 Catch:{ GeneralSecurityException -> 0x0019 }
        r3.verify(r4);	 Catch:{ GeneralSecurityException -> 0x0019 }
        r3 = 1;
        return r3;
    L_0x0019:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.tls.BasicCertificateChainCleaner.verifySignature(java.security.cert.X509Certificate, java.security.cert.X509Certificate):boolean");
    }

    public int hashCode() {
        return this.trustRootIndex.hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BasicCertificateChainCleaner) || ((BasicCertificateChainCleaner) obj).trustRootIndex.equals(this.trustRootIndex) == null) {
            z = false;
        }
        return z;
    }
}
