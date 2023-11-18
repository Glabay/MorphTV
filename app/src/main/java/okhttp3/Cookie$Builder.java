package okhttp3;

import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Cookie$Builder {
    String domain;
    long expiresAt = HttpDate.MAX_DATE;
    boolean hostOnly;
    boolean httpOnly;
    String name;
    String path = "/";
    boolean persistent;
    boolean secure;
    String value;

    public Cookie$Builder name(String str) {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (str.trim().equals(str)) {
            this.name = str;
            return this;
        } else {
            throw new IllegalArgumentException("name is not trimmed");
        }
    }

    public Cookie$Builder value(String str) {
        if (str == null) {
            throw new NullPointerException("value == null");
        } else if (str.trim().equals(str)) {
            this.value = str;
            return this;
        } else {
            throw new IllegalArgumentException("value is not trimmed");
        }
    }

    public Cookie$Builder expiresAt(long j) {
        if (j <= 0) {
            j = Long.MIN_VALUE;
        }
        if (j > HttpDate.MAX_DATE) {
            j = HttpDate.MAX_DATE;
        }
        this.expiresAt = j;
        this.persistent = 1;
        return this;
    }

    public Cookie$Builder domain(String str) {
        return domain(str, false);
    }

    public Cookie$Builder hostOnlyDomain(String str) {
        return domain(str, true);
    }

    private Cookie$Builder domain(String str, boolean z) {
        if (str == null) {
            throw new NullPointerException("domain == null");
        }
        String domainToAscii = Util.domainToAscii(str);
        if (domainToAscii == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected domain: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.domain = domainToAscii;
        this.hostOnly = z;
        return this;
    }

    public Cookie$Builder path(String str) {
        if (str.startsWith("/")) {
            this.path = str;
            return this;
        }
        throw new IllegalArgumentException("path must start with '/'");
    }

    public Cookie$Builder secure() {
        this.secure = true;
        return this;
    }

    public Cookie$Builder httpOnly() {
        this.httpOnly = true;
        return this;
    }

    public Cookie build() {
        return new Cookie(this);
    }
}
