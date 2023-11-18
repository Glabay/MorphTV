package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;

public final class CertificatePinner {
    public static final CertificatePinner DEFAULT = new CertificatePinner$Builder().build();
    @Nullable
    private final CertificateChainCleaner certificateChainCleaner;
    private final Set<CertificatePinner$Pin> pins;

    CertificatePinner(Set<CertificatePinner$Pin> set, @Nullable CertificateChainCleaner certificateChainCleaner) {
        this.pins = set;
        this.certificateChainCleaner = certificateChainCleaner;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj instanceof CertificatePinner) {
            CertificatePinner certificatePinner = (CertificatePinner) obj;
            if (Util.equal(this.certificateChainCleaner, certificatePinner.certificateChainCleaner) && this.pins.equals(certificatePinner.pins) != null) {
                return z;
            }
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return ((this.certificateChainCleaner != null ? this.certificateChainCleaner.hashCode() : 0) * 31) + this.pins.hashCode();
    }

    public void check(String str, List<Certificate> list) throws SSLPeerUnverifiedException {
        List findMatchingPins = findMatchingPins(str);
        if (!findMatchingPins.isEmpty()) {
            int i;
            if (this.certificateChainCleaner != null) {
                list = this.certificateChainCleaner.clean(list, str);
            }
            int size = list.size();
            for (i = 0; i < size; i++) {
                X509Certificate x509Certificate = (X509Certificate) list.get(i);
                int size2 = findMatchingPins.size();
                Object obj = null;
                Object obj2 = obj;
                for (int i2 = 0; i2 < size2; i2++) {
                    CertificatePinner$Pin certificatePinner$Pin = (CertificatePinner$Pin) findMatchingPins.get(i2);
                    if (certificatePinner$Pin.hashAlgorithm.equals("sha256/")) {
                        if (obj == null) {
                            obj = sha256(x509Certificate);
                        }
                        if (certificatePinner$Pin.hash.equals(obj)) {
                            return;
                        }
                    } else if (certificatePinner$Pin.hashAlgorithm.equals("sha1/")) {
                        if (obj2 == null) {
                            obj2 = sha1(x509Certificate);
                        }
                        if (certificatePinner$Pin.hash.equals(obj2)) {
                            return;
                        }
                    } else {
                        throw new AssertionError();
                    }
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Certificate pinning failure!");
            stringBuilder.append("\n  Peer certificate chain:");
            i = list.size();
            for (int i3 = 0; i3 < i; i3++) {
                X509Certificate x509Certificate2 = (X509Certificate) list.get(i3);
                stringBuilder.append("\n    ");
                stringBuilder.append(pin(x509Certificate2));
                stringBuilder.append(": ");
                stringBuilder.append(x509Certificate2.getSubjectDN().getName());
            }
            stringBuilder.append("\n  Pinned certificates for ");
            stringBuilder.append(str);
            stringBuilder.append(":");
            str = findMatchingPins.size();
            for (int i4 = 0; i4 < str; i4++) {
                CertificatePinner$Pin certificatePinner$Pin2 = (CertificatePinner$Pin) findMatchingPins.get(i4);
                stringBuilder.append("\n    ");
                stringBuilder.append(certificatePinner$Pin2);
            }
            throw new SSLPeerUnverifiedException(stringBuilder.toString());
        }
    }

    public void check(String str, Certificate... certificateArr) throws SSLPeerUnverifiedException {
        check(str, Arrays.asList(certificateArr));
    }

    List<CertificatePinner$Pin> findMatchingPins(String str) {
        List<CertificatePinner$Pin> emptyList = Collections.emptyList();
        for (CertificatePinner$Pin certificatePinner$Pin : this.pins) {
            if (certificatePinner$Pin.matches(str)) {
                if (emptyList.isEmpty()) {
                    emptyList = new ArrayList();
                }
                emptyList.add(certificatePinner$Pin);
            }
        }
        return emptyList;
    }

    CertificatePinner withCertificateChainCleaner(CertificateChainCleaner certificateChainCleaner) {
        if (Util.equal(this.certificateChainCleaner, certificateChainCleaner)) {
            return this;
        }
        return new CertificatePinner(this.pins, certificateChainCleaner);
    }

    public static String pin(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sha256/");
            stringBuilder.append(sha256((X509Certificate) certificate).base64());
            return stringBuilder.toString();
        }
        throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
    }

    static ByteString sha1(X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha1();
    }

    static ByteString sha256(X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha256();
    }
}
