package okhttp3;

import javax.annotation.Nullable;

public final class Challenge {
    private final String realm;
    private final String scheme;

    public Challenge(String str, String str2) {
        if (str == null) {
            throw new NullPointerException("scheme == null");
        } else if (str2 == null) {
            throw new NullPointerException("realm == null");
        } else {
            this.scheme = str;
            this.realm = str2;
        }
    }

    public String scheme() {
        return this.scheme;
    }

    public String realm() {
        return this.realm;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Challenge) {
            Challenge challenge = (Challenge) obj;
            if (challenge.scheme.equals(this.scheme) && challenge.realm.equals(this.realm) != null) {
                return true;
            }
        }
        return null;
    }

    public int hashCode() {
        return ((899 + this.realm.hashCode()) * 31) + this.scheme.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.scheme);
        stringBuilder.append(" realm=\"");
        stringBuilder.append(this.realm);
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }
}
