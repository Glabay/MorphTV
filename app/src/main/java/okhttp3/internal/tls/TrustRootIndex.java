package okhttp3.internal.tls;

import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public abstract class TrustRootIndex {

    static final class AndroidTrustRootIndex extends TrustRootIndex {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;

        AndroidTrustRootIndex(X509TrustManager x509TrustManager, Method method) {
            this.findByIssuerAndSignatureMethod = method;
            this.trustManager = x509TrustManager;
        }

        public java.security.cert.X509Certificate findByIssuerAndSignature(java.security.cert.X509Certificate r6) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r5 = this;
            r0 = 0;
            r1 = r5.findByIssuerAndSignatureMethod;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            r2 = r5.trustManager;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            r3 = 1;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            r3 = new java.lang.Object[r3];	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            r4 = 0;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            r3[r4] = r6;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            r6 = r1.invoke(r2, r3);	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            r6 = (java.security.cert.TrustAnchor) r6;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            if (r6 == 0) goto L_0x0018;	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
        L_0x0013:
            r6 = r6.getTrustedCert();	 Catch:{ IllegalAccessException -> 0x001b, InvocationTargetException -> 0x001a }
            goto L_0x0019;
        L_0x0018:
            r6 = r0;
        L_0x0019:
            return r6;
        L_0x001a:
            return r0;
        L_0x001b:
            r6 = new java.lang.AssertionError;
            r6.<init>();
            throw r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.tls.TrustRootIndex.AndroidTrustRootIndex.findByIssuerAndSignature(java.security.cert.X509Certificate):java.security.cert.X509Certificate");
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AndroidTrustRootIndex)) {
                return false;
            }
            AndroidTrustRootIndex androidTrustRootIndex = (AndroidTrustRootIndex) obj;
            if (!this.trustManager.equals(androidTrustRootIndex.trustManager) || this.findByIssuerAndSignatureMethod.equals(androidTrustRootIndex.findByIssuerAndSignatureMethod) == null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return this.trustManager.hashCode() + (this.findByIssuerAndSignatureMethod.hashCode() * 31);
        }
    }

    static final class BasicTrustRootIndex extends TrustRootIndex {
        private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts = new LinkedHashMap();

        BasicTrustRootIndex(X509Certificate... x509CertificateArr) {
            for (X509Certificate x509Certificate : x509CertificateArr) {
                X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
                Set set = (Set) this.subjectToCaCerts.get(subjectX500Principal);
                if (set == null) {
                    set = new LinkedHashSet(1);
                    this.subjectToCaCerts.put(subjectX500Principal, set);
                }
                set.add(x509Certificate);
            }
        }

        public java.security.cert.X509Certificate findByIssuerAndSignature(java.security.cert.X509Certificate r5) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r4 = this;
            r0 = r5.getIssuerX500Principal();
            r1 = r4.subjectToCaCerts;
            r0 = r1.get(r0);
            r0 = (java.util.Set) r0;
            r1 = 0;
            if (r0 != 0) goto L_0x0010;
        L_0x000f:
            return r1;
        L_0x0010:
            r0 = r0.iterator();
        L_0x0014:
            r2 = r0.hasNext();
            if (r2 == 0) goto L_0x0028;
        L_0x001a:
            r2 = r0.next();
            r2 = (java.security.cert.X509Certificate) r2;
            r3 = r2.getPublicKey();
            r5.verify(r3);	 Catch:{ Exception -> 0x0014 }
            return r2;
        L_0x0028:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.tls.TrustRootIndex.BasicTrustRootIndex.findByIssuerAndSignature(java.security.cert.X509Certificate):java.security.cert.X509Certificate");
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof BasicTrustRootIndex) || ((BasicTrustRootIndex) obj).subjectToCaCerts.equals(this.subjectToCaCerts) == null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return this.subjectToCaCerts.hashCode();
        }
    }

    public abstract X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate);

    public static okhttp3.internal.tls.TrustRootIndex get(javax.net.ssl.X509TrustManager r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r6.getClass();	 Catch:{ NoSuchMethodException -> 0x001b }
        r1 = "findTrustAnchorByIssuerAndSignature";	 Catch:{ NoSuchMethodException -> 0x001b }
        r2 = 1;	 Catch:{ NoSuchMethodException -> 0x001b }
        r3 = new java.lang.Class[r2];	 Catch:{ NoSuchMethodException -> 0x001b }
        r4 = 0;	 Catch:{ NoSuchMethodException -> 0x001b }
        r5 = java.security.cert.X509Certificate.class;	 Catch:{ NoSuchMethodException -> 0x001b }
        r3[r4] = r5;	 Catch:{ NoSuchMethodException -> 0x001b }
        r0 = r0.getDeclaredMethod(r1, r3);	 Catch:{ NoSuchMethodException -> 0x001b }
        r0.setAccessible(r2);	 Catch:{ NoSuchMethodException -> 0x001b }
        r1 = new okhttp3.internal.tls.TrustRootIndex$AndroidTrustRootIndex;	 Catch:{ NoSuchMethodException -> 0x001b }
        r1.<init>(r6, r0);	 Catch:{ NoSuchMethodException -> 0x001b }
        return r1;
    L_0x001b:
        r6 = r6.getAcceptedIssuers();
        r6 = get(r6);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.tls.TrustRootIndex.get(javax.net.ssl.X509TrustManager):okhttp3.internal.tls.TrustRootIndex");
    }

    public static TrustRootIndex get(X509Certificate... x509CertificateArr) {
        return new BasicTrustRootIndex(x509CertificateArr);
    }
}
