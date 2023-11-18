package okhttp3.internal.tls;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import okhttp3.internal.Util;

public final class OkHostnameVerifier implements HostnameVerifier {
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

    private OkHostnameVerifier() {
    }

    public boolean verify(java.lang.String r2, javax.net.ssl.SSLSession r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r1 = this;
        r0 = 0;
        r3 = r3.getPeerCertificates();	 Catch:{ SSLException -> 0x000e }
        r3 = r3[r0];	 Catch:{ SSLException -> 0x000e }
        r3 = (java.security.cert.X509Certificate) r3;	 Catch:{ SSLException -> 0x000e }
        r2 = r1.verify(r2, r3);	 Catch:{ SSLException -> 0x000e }
        return r2;
    L_0x000e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.tls.OkHostnameVerifier.verify(java.lang.String, javax.net.ssl.SSLSession):boolean");
    }

    public boolean verify(String str, X509Certificate x509Certificate) {
        if (Util.verifyAsIpAddress(str)) {
            return verifyIpAddress(str, x509Certificate);
        }
        return verifyHostname(str, x509Certificate);
    }

    private boolean verifyIpAddress(String str, X509Certificate x509Certificate) {
        x509Certificate = getSubjectAltNames(x509Certificate, 7);
        int size = x509Certificate.size();
        for (int i = 0; i < size; i++) {
            if (str.equalsIgnoreCase((String) x509Certificate.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyHostname(String str, X509Certificate x509Certificate) {
        str = str.toLowerCase(Locale.US);
        List subjectAltNames = getSubjectAltNames(x509Certificate, 2);
        int size = subjectAltNames.size();
        int i = 0;
        Object obj = null;
        while (i < size) {
            if (verifyHostname(str, (String) subjectAltNames.get(i))) {
                return true;
            }
            i++;
            obj = 1;
        }
        if (obj == null) {
            String findMostSpecific = new DistinguishedNameParser(x509Certificate.getSubjectX500Principal()).findMostSpecific("cn");
            if (findMostSpecific != null) {
                return verifyHostname(str, findMostSpecific);
            }
        }
        return false;
    }

    public static List<String> allSubjectAltNames(X509Certificate x509Certificate) {
        Collection subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        x509Certificate = getSubjectAltNames(x509Certificate, 2);
        List<String> arrayList = new ArrayList(subjectAltNames.size() + x509Certificate.size());
        arrayList.addAll(subjectAltNames);
        arrayList.addAll(x509Certificate);
        return arrayList;
    }

    private static java.util.List<java.lang.String> getSubjectAltNames(java.security.cert.X509Certificate r4, int r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new java.util.ArrayList;
        r0.<init>();
        r4 = r4.getSubjectAlternativeNames();	 Catch:{ CertificateParsingException -> 0x0048 }
        if (r4 != 0) goto L_0x0010;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x000b:
        r4 = java.util.Collections.emptyList();	 Catch:{ CertificateParsingException -> 0x0048 }
        return r4;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x0010:
        r4 = r4.iterator();	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x0014:
        r1 = r4.hasNext();	 Catch:{ CertificateParsingException -> 0x0048 }
        if (r1 == 0) goto L_0x0047;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x001a:
        r1 = r4.next();	 Catch:{ CertificateParsingException -> 0x0048 }
        r1 = (java.util.List) r1;	 Catch:{ CertificateParsingException -> 0x0048 }
        if (r1 == 0) goto L_0x0014;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x0022:
        r2 = r1.size();	 Catch:{ CertificateParsingException -> 0x0048 }
        r3 = 2;	 Catch:{ CertificateParsingException -> 0x0048 }
        if (r2 >= r3) goto L_0x002a;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x0029:
        goto L_0x0014;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x002a:
        r2 = 0;	 Catch:{ CertificateParsingException -> 0x0048 }
        r2 = r1.get(r2);	 Catch:{ CertificateParsingException -> 0x0048 }
        r2 = (java.lang.Integer) r2;	 Catch:{ CertificateParsingException -> 0x0048 }
        if (r2 != 0) goto L_0x0034;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x0033:
        goto L_0x0014;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x0034:
        r2 = r2.intValue();	 Catch:{ CertificateParsingException -> 0x0048 }
        if (r2 != r5) goto L_0x0014;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x003a:
        r2 = 1;	 Catch:{ CertificateParsingException -> 0x0048 }
        r1 = r1.get(r2);	 Catch:{ CertificateParsingException -> 0x0048 }
        r1 = (java.lang.String) r1;	 Catch:{ CertificateParsingException -> 0x0048 }
        if (r1 == 0) goto L_0x0014;	 Catch:{ CertificateParsingException -> 0x0048 }
    L_0x0043:
        r0.add(r1);	 Catch:{ CertificateParsingException -> 0x0048 }
        goto L_0x0014;
    L_0x0047:
        return r0;
    L_0x0048:
        r4 = java.util.Collections.emptyList();
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.tls.OkHostnameVerifier.getSubjectAltNames(java.security.cert.X509Certificate, int):java.util.List<java.lang.String>");
    }

    public boolean verifyHostname(String str, String str2) {
        if (!(str == null || str.length() == 0 || str.startsWith("."))) {
            if (!str.endsWith("..")) {
                if (!(str2 == null || str2.length() == 0 || str2.startsWith("."))) {
                    if (!str2.endsWith("..")) {
                        StringBuilder stringBuilder;
                        if (!str.endsWith(".")) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str);
                            stringBuilder.append('.');
                            str = stringBuilder.toString();
                        }
                        if (!str2.endsWith(".")) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append('.');
                            str2 = stringBuilder.toString();
                        }
                        str2 = str2.toLowerCase(Locale.US);
                        if (!str2.contains("*")) {
                            return str.equals(str2);
                        }
                        if (str2.startsWith("*.")) {
                            if (str2.indexOf(42, 1) == -1) {
                                if (str.length() < str2.length() || "*.".equals(str2)) {
                                    return false;
                                }
                                str2 = str2.substring(1);
                                if (!str.endsWith(str2)) {
                                    return false;
                                }
                                int length = str.length() - str2.length();
                                if (length <= 0 || str.lastIndexOf(46, length - 1) == -1) {
                                    return true;
                                }
                                return false;
                            }
                        }
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
    }
}
