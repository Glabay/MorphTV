package okhttp3;

import okio.ByteString;

final class CertificatePinner$Pin {
    private static final String WILDCARD = "*.";
    final String canonicalHostname;
    final ByteString hash;
    final String hashAlgorithm;
    final String pattern;

    CertificatePinner$Pin(String str, String str2) {
        StringBuilder stringBuilder;
        this.pattern = str;
        if (str.startsWith(WILDCARD)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("http://");
            stringBuilder.append(str.substring(WILDCARD.length()));
            str = HttpUrl.parse(stringBuilder.toString()).host();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("http://");
            stringBuilder.append(str);
            str = HttpUrl.parse(stringBuilder.toString()).host();
        }
        this.canonicalHostname = str;
        if (str2.startsWith("sha1/") != null) {
            this.hashAlgorithm = "sha1/";
            this.hash = ByteString.decodeBase64(str2.substring("sha1/".length()));
        } else if (str2.startsWith("sha256/") != null) {
            this.hashAlgorithm = "sha256/";
            this.hash = ByteString.decodeBase64(str2.substring("sha256/".length()));
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("pins must start with 'sha256/' or 'sha1/': ");
            stringBuilder.append(str2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        if (this.hash == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("pins must be base64: ");
            stringBuilder.append(str2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    boolean matches(String str) {
        if (!this.pattern.startsWith(WILDCARD)) {
            return str.equals(this.canonicalHostname);
        }
        return str.regionMatches(false, str.indexOf(46) + 1, this.canonicalHostname, 0, this.canonicalHostname.length());
    }

    public boolean equals(Object obj) {
        if (obj instanceof CertificatePinner$Pin) {
            CertificatePinner$Pin certificatePinner$Pin = (CertificatePinner$Pin) obj;
            if (this.pattern.equals(certificatePinner$Pin.pattern) && this.hashAlgorithm.equals(certificatePinner$Pin.hashAlgorithm) && this.hash.equals(certificatePinner$Pin.hash) != null) {
                return true;
            }
        }
        return null;
    }

    public int hashCode() {
        return ((((527 + this.pattern.hashCode()) * 31) + this.hashAlgorithm.hashCode()) * 31) + this.hash.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.hashAlgorithm);
        stringBuilder.append(this.hash.base64());
        return stringBuilder.toString();
    }
}
