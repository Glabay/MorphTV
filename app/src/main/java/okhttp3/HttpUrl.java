package okhttp3;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import kotlin.text.Typography;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import okio.Buffer;
import org.apache.commons.io.IOUtils;

public final class HttpUrl {
    static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    static final String FRAGMENT_ENCODE_SET = "";
    static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
    static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    static final String QUERY_ENCODE_SET = " \"'<>#";
    static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    @Nullable
    private final String fragment;
    final String host;
    private final String password;
    private final List<String> pathSegments;
    final int port;
    @Nullable
    private final List<String> queryNamesAndValues;
    final String scheme;
    private final String url;
    private final String username;

    public static final class Builder {
        @Nullable
        String encodedFragment;
        String encodedPassword = "";
        final List<String> encodedPathSegments = new ArrayList();
        @Nullable
        List<String> encodedQueryNamesAndValues;
        String encodedUsername = "";
        @Nullable
        String host;
        int port = -1;
        @Nullable
        String scheme;

        public Builder() {
            this.encodedPathSegments.add("");
        }

        public Builder scheme(String str) {
            if (str == null) {
                throw new NullPointerException("scheme == null");
            }
            if (str.equalsIgnoreCase("http")) {
                this.scheme = "http";
            } else if (str.equalsIgnoreCase("https")) {
                this.scheme = "https";
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected scheme: ");
                stringBuilder.append(str);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            return this;
        }

        public Builder username(String str) {
            if (str == null) {
                throw new NullPointerException("username == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }

        public Builder encodedUsername(String str) {
            if (str == null) {
                throw new NullPointerException("encodedUsername == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }

        public Builder password(String str) {
            if (str == null) {
                throw new NullPointerException("password == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }

        public Builder encodedPassword(String str) {
            if (str == null) {
                throw new NullPointerException("encodedPassword == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }

        public Builder host(String str) {
            if (str == null) {
                throw new NullPointerException("host == null");
            }
            String canonicalizeHost = canonicalizeHost(str, 0, str.length());
            if (canonicalizeHost == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected host: ");
                stringBuilder.append(str);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.host = canonicalizeHost;
            return this;
        }

        public Builder port(int i) {
            if (i > 0) {
                if (i <= 65535) {
                    this.port = i;
                    return this;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected port: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        int effectivePort() {
            return this.port != -1 ? this.port : HttpUrl.defaultPort(this.scheme);
        }

        public Builder addPathSegment(String str) {
            if (str == null) {
                throw new NullPointerException("pathSegment == null");
            }
            push(str, 0, str.length(), false, false);
            return this;
        }

        public Builder addPathSegments(String str) {
            if (str != null) {
                return addPathSegments(str, false);
            }
            throw new NullPointerException("pathSegments == null");
        }

        public Builder addEncodedPathSegment(String str) {
            if (str == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            push(str, 0, str.length(), false, true);
            return this;
        }

        public Builder addEncodedPathSegments(String str) {
            if (str != null) {
                return addPathSegments(str, true);
            }
            throw new NullPointerException("encodedPathSegments == null");
        }

        private Builder addPathSegments(String str, boolean z) {
            int i = 0;
            do {
                int delimiterOffset = Util.delimiterOffset(str, i, str.length(), "/\\");
                push(str, i, delimiterOffset, delimiterOffset < str.length(), z);
                i = delimiterOffset + 1;
            } while (i <= str.length());
            return this;
        }

        public Builder setPathSegment(int i, String str) {
            if (str == null) {
                throw new NullPointerException("pathSegment == null");
            }
            String canonicalize = HttpUrl.canonicalize(str, 0, str.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, false, false, false, true);
            if (!isDot(canonicalize)) {
                if (!isDotDot(canonicalize)) {
                    this.encodedPathSegments.set(i, canonicalize);
                    return this;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected path segment: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder setEncodedPathSegment(int i, String str) {
            if (str == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            String canonicalize = HttpUrl.canonicalize(str, 0, str.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, true, false, false, true);
            this.encodedPathSegments.set(i, canonicalize);
            if (isDot(canonicalize) == 0) {
                if (isDotDot(canonicalize) == 0) {
                    return this;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected path segment: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder removePathSegment(int i) {
            this.encodedPathSegments.remove(i);
            if (this.encodedPathSegments.isEmpty() != 0) {
                this.encodedPathSegments.add("");
            }
            return this;
        }

        public Builder encodedPath(String str) {
            if (str == null) {
                throw new NullPointerException("encodedPath == null");
            } else if (str.startsWith("/")) {
                resolvePath(str, 0, str.length());
                return this;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected encodedPath: ");
                stringBuilder.append(str);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public Builder query(@Nullable String str) {
            if (str != null) {
                str = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(str, HttpUrl.QUERY_ENCODE_SET, false, false, true, true));
            } else {
                str = null;
            }
            this.encodedQueryNamesAndValues = str;
            return this;
        }

        public Builder encodedQuery(@Nullable String str) {
            if (str != null) {
                str = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(str, HttpUrl.QUERY_ENCODE_SET, true, false, true, true));
            } else {
                str = null;
            }
            this.encodedQueryNamesAndValues = str;
            return this;
        }

        public Builder addQueryParameter(String str, @Nullable String str2) {
            if (str == null) {
                throw new NullPointerException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true));
            str = this.encodedQueryNamesAndValues;
            if (str2 != null) {
                str2 = HttpUrl.canonicalize(str2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true);
            } else {
                str2 = null;
            }
            str.add(str2);
            return this;
        }

        public Builder addEncodedQueryParameter(String str, @Nullable String str2) {
            if (str == null) {
                throw new NullPointerException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, false, true, true));
            str = this.encodedQueryNamesAndValues;
            if (str2 != null) {
                str2 = HttpUrl.canonicalize(str2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, false, true, true);
            } else {
                str2 = null;
            }
            str.add(str2);
            return this;
        }

        public Builder setQueryParameter(String str, @Nullable String str2) {
            removeAllQueryParameters(str);
            addQueryParameter(str, str2);
            return this;
        }

        public Builder setEncodedQueryParameter(String str, @Nullable String str2) {
            removeAllEncodedQueryParameters(str);
            addEncodedQueryParameter(str, str2);
            return this;
        }

        public Builder removeAllQueryParameters(String str) {
            if (str == null) {
                throw new NullPointerException("name == null");
            } else if (this.encodedQueryNamesAndValues == null) {
                return this;
            } else {
                removeAllCanonicalQueryParameters(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true));
                return this;
            }
        }

        public Builder removeAllEncodedQueryParameters(String str) {
            if (str == null) {
                throw new NullPointerException("encodedName == null");
            } else if (this.encodedQueryNamesAndValues == null) {
                return this;
            } else {
                removeAllCanonicalQueryParameters(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, false, true, true));
                return this;
            }
        }

        private void removeAllCanonicalQueryParameters(String str) {
            for (int size = this.encodedQueryNamesAndValues.size() - 2; size >= 0; size -= 2) {
                if (str.equals(this.encodedQueryNamesAndValues.get(size))) {
                    this.encodedQueryNamesAndValues.remove(size + 1);
                    this.encodedQueryNamesAndValues.remove(size);
                    if (this.encodedQueryNamesAndValues.isEmpty()) {
                        this.encodedQueryNamesAndValues = null;
                        return;
                    }
                }
            }
        }

        public Builder fragment(@Nullable String str) {
            if (str != null) {
                str = HttpUrl.canonicalize(str, "", false, false, false, false);
            } else {
                str = null;
            }
            this.encodedFragment = str;
            return this;
        }

        public Builder encodedFragment(@Nullable String str) {
            if (str != null) {
                str = HttpUrl.canonicalize(str, "", true, false, false, false);
            } else {
                str = null;
            }
            this.encodedFragment = str;
            return this;
        }

        Builder reencodeForUri() {
            int size = this.encodedPathSegments.size();
            for (int i = 0; i < size; i++) {
                this.encodedPathSegments.set(i, HttpUrl.canonicalize((String) this.encodedPathSegments.get(i), HttpUrl.PATH_SEGMENT_ENCODE_SET_URI, true, true, false, true));
            }
            if (this.encodedQueryNamesAndValues != null) {
                size = this.encodedQueryNamesAndValues.size();
                for (int i2 = 0; i2 < size; i2++) {
                    String str = (String) this.encodedQueryNamesAndValues.get(i2);
                    if (str != null) {
                        this.encodedQueryNamesAndValues.set(i2, HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET_URI, true, true, true, true));
                    }
                }
            }
            if (this.encodedFragment != null) {
                this.encodedFragment = HttpUrl.canonicalize(this.encodedFragment, HttpUrl.FRAGMENT_ENCODE_SET_URI, true, true, false, false);
            }
            return this;
        }

        public HttpUrl build() {
            if (this.scheme == null) {
                throw new IllegalStateException("scheme == null");
            } else if (this.host != null) {
                return new HttpUrl(this);
            } else {
                throw new IllegalStateException("host == null");
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.scheme);
            stringBuilder.append("://");
            if (!(this.encodedUsername.isEmpty() && this.encodedPassword.isEmpty())) {
                stringBuilder.append(this.encodedUsername);
                if (!this.encodedPassword.isEmpty()) {
                    stringBuilder.append(':');
                    stringBuilder.append(this.encodedPassword);
                }
                stringBuilder.append('@');
            }
            if (this.host.indexOf(58) != -1) {
                stringBuilder.append('[');
                stringBuilder.append(this.host);
                stringBuilder.append(']');
            } else {
                stringBuilder.append(this.host);
            }
            int effectivePort = effectivePort();
            if (effectivePort != HttpUrl.defaultPort(this.scheme)) {
                stringBuilder.append(':');
                stringBuilder.append(effectivePort);
            }
            HttpUrl.pathSegmentsToString(stringBuilder, this.encodedPathSegments);
            if (this.encodedQueryNamesAndValues != null) {
                stringBuilder.append('?');
                HttpUrl.namesAndValuesToQueryString(stringBuilder, this.encodedQueryNamesAndValues);
            }
            if (this.encodedFragment != null) {
                stringBuilder.append('#');
                stringBuilder.append(this.encodedFragment);
            }
            return stringBuilder.toString();
        }

        HttpUrl$Builder$ParseResult parse(@Nullable HttpUrl httpUrl, String str) {
            int delimiterOffset;
            Builder builder = this;
            HttpUrl httpUrl2 = httpUrl;
            String str2 = str;
            int skipLeadingAsciiWhitespace = Util.skipLeadingAsciiWhitespace(str2, 0, str.length());
            int skipTrailingAsciiWhitespace = Util.skipTrailingAsciiWhitespace(str2, skipLeadingAsciiWhitespace, str.length());
            if (schemeDelimiterOffset(str2, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace) != -1) {
                if (str2.regionMatches(true, skipLeadingAsciiWhitespace, "https:", 0, 6)) {
                    builder.scheme = "https";
                    skipLeadingAsciiWhitespace += "https:".length();
                } else {
                    if (!str2.regionMatches(true, skipLeadingAsciiWhitespace, "http:", 0, 5)) {
                        return HttpUrl$Builder$ParseResult.UNSUPPORTED_SCHEME;
                    }
                    builder.scheme = "http";
                    skipLeadingAsciiWhitespace += "http:".length();
                }
            } else if (httpUrl2 == null) {
                return HttpUrl$Builder$ParseResult.MISSING_SCHEME;
            } else {
                builder.scheme = httpUrl2.scheme;
            }
            int slashCount = slashCount(str2, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace);
            char c = '#';
            if (slashCount < 2 && httpUrl2 != null) {
                if (httpUrl2.scheme.equals(builder.scheme)) {
                    builder.encodedUsername = httpUrl.encodedUsername();
                    builder.encodedPassword = httpUrl.encodedPassword();
                    builder.host = httpUrl2.host;
                    builder.port = httpUrl2.port;
                    builder.encodedPathSegments.clear();
                    builder.encodedPathSegments.addAll(httpUrl.encodedPathSegments());
                    if (skipLeadingAsciiWhitespace == skipTrailingAsciiWhitespace || str2.charAt(skipLeadingAsciiWhitespace) == '#') {
                        encodedQuery(httpUrl.encodedQuery());
                    }
                    int i = skipLeadingAsciiWhitespace;
                    delimiterOffset = Util.delimiterOffset(str2, i, skipTrailingAsciiWhitespace, "?#");
                    resolvePath(str2, i, delimiterOffset);
                    if (delimiterOffset < skipTrailingAsciiWhitespace && str2.charAt(delimiterOffset) == '?') {
                        skipLeadingAsciiWhitespace = Util.delimiterOffset(str2, delimiterOffset, skipTrailingAsciiWhitespace, '#');
                        builder.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(str2, delimiterOffset + 1, skipLeadingAsciiWhitespace, HttpUrl.QUERY_ENCODE_SET, true, false, true, true));
                        delimiterOffset = skipLeadingAsciiWhitespace;
                    }
                    if (delimiterOffset < skipTrailingAsciiWhitespace && str2.charAt(delimiterOffset) == '#') {
                        builder.encodedFragment = HttpUrl.canonicalize(str2, 1 + delimiterOffset, skipTrailingAsciiWhitespace, "", true, false, false, false);
                    }
                    return HttpUrl$Builder$ParseResult.SUCCESS;
                }
            }
            slashCount = skipLeadingAsciiWhitespace + slashCount;
            Object obj = null;
            Object obj2 = null;
            while (true) {
                int delimiterOffset2 = Util.delimiterOffset(str2, slashCount, skipTrailingAsciiWhitespace, "@/\\?#");
                char charAt = delimiterOffset2 != skipTrailingAsciiWhitespace ? str2.charAt(delimiterOffset2) : '￿';
                if (!(charAt == '￿' || charAt == r13 || charAt == IOUtils.DIR_SEPARATOR_UNIX || charAt == IOUtils.DIR_SEPARATOR_WINDOWS)) {
                    switch (charAt) {
                        case '?':
                            break;
                        case '@':
                            if (obj == null) {
                                int delimiterOffset3 = Util.delimiterOffset(str2, slashCount, delimiterOffset2, ':');
                                int i2 = delimiterOffset3;
                                i = delimiterOffset2;
                                String canonicalize = HttpUrl.canonicalize(str2, slashCount, delimiterOffset3, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                                if (obj2 != null) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(builder.encodedUsername);
                                    stringBuilder.append("%40");
                                    stringBuilder.append(canonicalize);
                                    canonicalize = stringBuilder.toString();
                                }
                                builder.encodedUsername = canonicalize;
                                if (i2 != i) {
                                    builder.encodedPassword = HttpUrl.canonicalize(str2, i2 + 1, i, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                                    obj = 1;
                                }
                                obj2 = 1;
                            } else {
                                i = delimiterOffset2;
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(builder.encodedPassword);
                                stringBuilder2.append("%40");
                                stringBuilder2.append(HttpUrl.canonicalize(str2, slashCount, i, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true));
                                builder.encodedPassword = stringBuilder2.toString();
                            }
                            slashCount = i + 1;
                            continue;
                        default:
                            continue;
                    }
                }
                i = delimiterOffset2;
                delimiterOffset = portColonOffset(str2, slashCount, i);
                int i3 = delimiterOffset + 1;
                if (i3 < i) {
                    builder.host = canonicalizeHost(str2, slashCount, delimiterOffset);
                    builder.port = parsePort(str2, i3, i);
                    if (builder.port == -1) {
                        return HttpUrl$Builder$ParseResult.INVALID_PORT;
                    }
                }
                builder.host = canonicalizeHost(str2, slashCount, delimiterOffset);
                builder.port = HttpUrl.defaultPort(builder.scheme);
                if (builder.host == null) {
                    return HttpUrl$Builder$ParseResult.INVALID_HOST;
                }
                delimiterOffset = Util.delimiterOffset(str2, i, skipTrailingAsciiWhitespace, "?#");
                resolvePath(str2, i, delimiterOffset);
                skipLeadingAsciiWhitespace = Util.delimiterOffset(str2, delimiterOffset, skipTrailingAsciiWhitespace, '#');
                builder.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(str2, delimiterOffset + 1, skipLeadingAsciiWhitespace, HttpUrl.QUERY_ENCODE_SET, true, false, true, true));
                delimiterOffset = skipLeadingAsciiWhitespace;
                builder.encodedFragment = HttpUrl.canonicalize(str2, 1 + delimiterOffset, skipTrailingAsciiWhitespace, "", true, false, false, false);
                return HttpUrl$Builder$ParseResult.SUCCESS;
                c = '#';
            }
        }

        private void resolvePath(String str, int i, int i2) {
            if (i != i2) {
                int i3;
                boolean z;
                char charAt = str.charAt(i);
                if (charAt != IOUtils.DIR_SEPARATOR_UNIX) {
                    if (charAt != IOUtils.DIR_SEPARATOR_WINDOWS) {
                        this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
                        while (true) {
                            i3 = i;
                            if (i3 < i2) {
                                i = Util.delimiterOffset(str, i3, i2, "/\\");
                                z = i >= i2;
                                push(str, i3, i, z, true);
                                if (z) {
                                    i++;
                                }
                            } else {
                                return;
                            }
                        }
                    }
                }
                this.encodedPathSegments.clear();
                this.encodedPathSegments.add("");
                i++;
                while (true) {
                    i3 = i;
                    if (i3 < i2) {
                        i = Util.delimiterOffset(str, i3, i2, "/\\");
                        if (i >= i2) {
                        }
                        push(str, i3, i, z, true);
                        if (z) {
                            i++;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        private void push(String str, int i, int i2, boolean z, boolean z2) {
            str = HttpUrl.canonicalize(str, i, i2, HttpUrl.PATH_SEGMENT_ENCODE_SET, z2, false, false, true);
            if (isDot(str) == 0) {
                if (isDotDot(str) != 0) {
                    pop();
                    return;
                }
                if (((String) this.encodedPathSegments.get(this.encodedPathSegments.size() - 1)).isEmpty() != 0) {
                    this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, str);
                } else {
                    this.encodedPathSegments.add(str);
                }
                if (z) {
                    this.encodedPathSegments.add("");
                }
            }
        }

        private boolean isDot(String str) {
            if (!str.equals(".")) {
                if (str.equalsIgnoreCase("%2e") == null) {
                    return null;
                }
            }
            return true;
        }

        private boolean isDotDot(String str) {
            if (!(str.equals("..") || str.equalsIgnoreCase("%2e.") || str.equalsIgnoreCase(".%2e"))) {
                if (str.equalsIgnoreCase("%2e%2e") == null) {
                    return null;
                }
            }
            return true;
        }

        private void pop() {
            if (!((String) this.encodedPathSegments.remove(this.encodedPathSegments.size() - 1)).isEmpty() || this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.add("");
            } else {
                this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
            }
        }

        private static int schemeDelimiterOffset(String str, int i, int i2) {
            if (i2 - i < 2) {
                return -1;
            }
            char charAt = str.charAt(i);
            if (charAt < 'a' || charAt > 'z') {
                if (charAt >= 'A') {
                    if (charAt > 'Z') {
                    }
                }
                return -1;
            }
            while (true) {
                i++;
                if (i < i2) {
                    charAt = str.charAt(i);
                    if ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && !((charAt >= '0' && charAt <= '9') || charAt == '+' || charAt == '-'))) {
                        if (charAt != '.') {
                            break;
                        }
                    }
                } else {
                    return -1;
                }
            }
            if (charAt == ':') {
                return i;
            }
            return -1;
        }

        private static int slashCount(String str, int i, int i2) {
            int i3 = 0;
            while (i < i2) {
                char charAt = str.charAt(i);
                if (charAt != IOUtils.DIR_SEPARATOR_WINDOWS && charAt != IOUtils.DIR_SEPARATOR_UNIX) {
                    break;
                }
                i3++;
                i++;
            }
            return i3;
        }

        private static int portColonOffset(String str, int i, int i2) {
            while (i < i2) {
                char charAt = str.charAt(i);
                if (charAt == ':') {
                    return i;
                }
                if (charAt == '[') {
                    do {
                        i++;
                        if (i >= i2) {
                            break;
                        }
                    } while (str.charAt(i) != ']');
                }
                i++;
            }
            return i2;
        }

        private static String canonicalizeHost(String str, int i, int i2) {
            str = HttpUrl.percentDecode(str, i, i2, false);
            if (str.contains(":") == 0) {
                return Util.domainToAscii(str);
            }
            if (str.startsWith("[") == 0 || str.endsWith("]") == 0) {
                str = decodeIpv6(str, 0, str.length());
            } else {
                str = decodeIpv6(str, 1, str.length() - 1);
            }
            if (str == null) {
                return null;
            }
            str = str.getAddress();
            if (str.length == 16) {
                return inet6AddressToAscii(str);
            }
            throw new AssertionError();
        }

        @javax.annotation.Nullable
        private static java.net.InetAddress decodeIpv6(java.lang.String r10, int r11, int r12) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = 16;
            r0 = new byte[r0];
            r1 = -1;
            r2 = 0;
            r3 = 0;
            r4 = -1;
            r5 = -1;
        L_0x0009:
            r6 = 0;
            if (r11 >= r12) goto L_0x007c;
        L_0x000c:
            r7 = r0.length;
            if (r3 != r7) goto L_0x0010;
        L_0x000f:
            return r6;
        L_0x0010:
            r7 = r11 + 2;
            if (r7 > r12) goto L_0x0029;
        L_0x0014:
            r8 = "::";
            r9 = 2;
            r8 = r10.regionMatches(r11, r8, r2, r9);
            if (r8 == 0) goto L_0x0029;
        L_0x001d:
            if (r4 == r1) goto L_0x0020;
        L_0x001f:
            return r6;
        L_0x0020:
            r3 = r3 + 2;
            if (r7 != r12) goto L_0x0026;
        L_0x0024:
            r4 = r3;
            goto L_0x007c;
        L_0x0026:
            r4 = r3;
            r5 = r7;
            goto L_0x004d;
        L_0x0029:
            if (r3 == 0) goto L_0x004c;
        L_0x002b:
            r7 = ":";
            r8 = 1;
            r7 = r10.regionMatches(r11, r7, r2, r8);
            if (r7 == 0) goto L_0x0037;
        L_0x0034:
            r11 = r11 + 1;
            goto L_0x004c;
        L_0x0037:
            r7 = ".";
            r11 = r10.regionMatches(r11, r7, r2, r8);
            if (r11 == 0) goto L_0x004b;
        L_0x003f:
            r11 = r3 + -2;
            r10 = decodeIpv4Suffix(r10, r5, r12, r0, r11);
            if (r10 != 0) goto L_0x0048;
        L_0x0047:
            return r6;
        L_0x0048:
            r3 = r3 + 2;
            goto L_0x007c;
        L_0x004b:
            return r6;
        L_0x004c:
            r5 = r11;
        L_0x004d:
            r11 = r5;
            r7 = 0;
        L_0x004f:
            if (r11 >= r12) goto L_0x0062;
        L_0x0051:
            r8 = r10.charAt(r11);
            r8 = okhttp3.HttpUrl.decodeHexDigit(r8);
            if (r8 != r1) goto L_0x005c;
        L_0x005b:
            goto L_0x0062;
        L_0x005c:
            r7 = r7 << 4;
            r7 = r7 + r8;
            r11 = r11 + 1;
            goto L_0x004f;
        L_0x0062:
            r8 = r11 - r5;
            if (r8 == 0) goto L_0x007b;
        L_0x0066:
            r9 = 4;
            if (r8 <= r9) goto L_0x006a;
        L_0x0069:
            goto L_0x007b;
        L_0x006a:
            r6 = r3 + 1;
            r8 = r7 >>> 8;
            r8 = r8 & 255;
            r8 = (byte) r8;
            r0[r3] = r8;
            r3 = r6 + 1;
            r7 = r7 & 255;
            r7 = (byte) r7;
            r0[r6] = r7;
            goto L_0x0009;
        L_0x007b:
            return r6;
        L_0x007c:
            r10 = r0.length;
            if (r3 == r10) goto L_0x008f;
        L_0x007f:
            if (r4 != r1) goto L_0x0082;
        L_0x0081:
            return r6;
        L_0x0082:
            r10 = r0.length;
            r11 = r3 - r4;
            r10 = r10 - r11;
            java.lang.System.arraycopy(r0, r4, r0, r10, r11);
            r10 = r0.length;
            r10 = r10 - r3;
            r10 = r10 + r4;
            java.util.Arrays.fill(r0, r4, r10, r2);
        L_0x008f:
            r10 = java.net.InetAddress.getByAddress(r0);	 Catch:{ UnknownHostException -> 0x0094 }
            return r10;
        L_0x0094:
            r10 = new java.lang.AssertionError;
            r10.<init>();
            throw r10;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.Builder.decodeIpv6(java.lang.String, int, int):java.net.InetAddress");
        }

        private static boolean decodeIpv4Suffix(String str, int i, int i2, byte[] bArr, int i3) {
            int i4 = i3;
            while (i < i2) {
                if (i4 == bArr.length) {
                    return false;
                }
                if (i4 != i3) {
                    if (str.charAt(i) != '.') {
                        return false;
                    }
                    i++;
                }
                int i5 = i;
                int i6 = 0;
                while (i5 < i2) {
                    char charAt = str.charAt(i5);
                    if (charAt < '0') {
                        break;
                    } else if (charAt > '9') {
                        break;
                    } else if (i6 == 0 && i != i5) {
                        return false;
                    } else {
                        i6 = ((i6 * 10) + charAt) - 48;
                        if (i6 > 255) {
                            return false;
                        }
                        i5++;
                    }
                }
                if (i5 - i == 0) {
                    return false;
                }
                i = i4 + 1;
                bArr[i4] = (byte) i6;
                i4 = i;
                i = i5;
            }
            if (i4 != i3 + 4) {
                return false;
            }
            return true;
        }

        private static String inet6AddressToAscii(byte[] bArr) {
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = -1;
            while (i2 < bArr.length) {
                int i5 = i2;
                while (i5 < 16 && bArr[i5] == (byte) 0 && bArr[i5 + 1] == (byte) 0) {
                    i5 += 2;
                }
                int i6 = i5 - i2;
                if (i6 > i3 && i6 >= 4) {
                    i4 = i2;
                    i3 = i6;
                }
                i2 = i5 + 2;
            }
            Buffer buffer = new Buffer();
            while (i < bArr.length) {
                if (i == i4) {
                    buffer.writeByte(58);
                    i += i3;
                    if (i == 16) {
                        buffer.writeByte(58);
                    }
                } else {
                    if (i > 0) {
                        buffer.writeByte(58);
                    }
                    buffer.writeHexadecimalUnsignedLong((long) (((bArr[i] & 255) << 8) | (bArr[i + 1] & 255)));
                    i += 2;
                }
            }
            return buffer.readUtf8();
        }

        private static int parsePort(java.lang.String r9, int r10, int r11) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = -1;
            r4 = "";	 Catch:{ NumberFormatException -> 0x001b }
            r5 = 0;	 Catch:{ NumberFormatException -> 0x001b }
            r6 = 0;	 Catch:{ NumberFormatException -> 0x001b }
            r7 = 0;	 Catch:{ NumberFormatException -> 0x001b }
            r8 = 1;	 Catch:{ NumberFormatException -> 0x001b }
            r1 = r9;	 Catch:{ NumberFormatException -> 0x001b }
            r2 = r10;	 Catch:{ NumberFormatException -> 0x001b }
            r3 = r11;	 Catch:{ NumberFormatException -> 0x001b }
            r9 = okhttp3.HttpUrl.canonicalize(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ NumberFormatException -> 0x001b }
            r9 = java.lang.Integer.parseInt(r9);	 Catch:{ NumberFormatException -> 0x001b }
            if (r9 <= 0) goto L_0x001a;
        L_0x0014:
            r10 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
            if (r9 > r10) goto L_0x001a;
        L_0x0019:
            return r9;
        L_0x001a:
            return r0;
        L_0x001b:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.Builder.parsePort(java.lang.String, int, int):int");
        }
    }

    static int decodeHexDigit(char c) {
        return (c < '0' || c > '9') ? (c < 'a' || c > 'f') ? (c < 'A' || c > 'F') ? -1 : (c - 65) + 10 : (c - 97) + 10 : c - 48;
    }

    HttpUrl(Builder builder) {
        this.scheme = builder.scheme;
        this.username = percentDecode(builder.encodedUsername, false);
        this.password = percentDecode(builder.encodedPassword, false);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = percentDecode(builder.encodedPathSegments, false);
        String str = null;
        this.queryNamesAndValues = builder.encodedQueryNamesAndValues != null ? percentDecode(builder.encodedQueryNamesAndValues, true) : null;
        if (builder.encodedFragment != null) {
            str = percentDecode(builder.encodedFragment, false);
        }
        this.fragment = str;
        this.url = builder.toString();
    }

    public URL url() {
        try {
            return new URL(this.url);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public java.net.URI uri() {
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
        r4 = this;
        r0 = r4.newBuilder();
        r0 = r0.reencodeForUri();
        r0 = r0.toString();
        r1 = new java.net.URI;	 Catch:{ URISyntaxException -> 0x0012 }
        r1.<init>(r0);	 Catch:{ URISyntaxException -> 0x0012 }
        return r1;
    L_0x0012:
        r1 = move-exception;
        r2 = "[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]";	 Catch:{ Exception -> 0x0020 }
        r3 = "";	 Catch:{ Exception -> 0x0020 }
        r0 = r0.replaceAll(r2, r3);	 Catch:{ Exception -> 0x0020 }
        r0 = java.net.URI.create(r0);	 Catch:{ Exception -> 0x0020 }
        return r0;
    L_0x0020:
        r0 = new java.lang.RuntimeException;
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.uri():java.net.URI");
    }

    public String scheme() {
        return this.scheme;
    }

    public boolean isHttps() {
        return this.scheme.equals("https");
    }

    public String encodedUsername() {
        if (this.username.isEmpty()) {
            return "";
        }
        int length = this.scheme.length() + 3;
        return this.url.substring(length, Util.delimiterOffset(this.url, length, this.url.length(), ":@"));
    }

    public String username() {
        return this.username;
    }

    public String encodedPassword() {
        if (this.password.isEmpty()) {
            return "";
        }
        return this.url.substring(this.url.indexOf(58, this.scheme.length() + 3) + 1, this.url.indexOf(64));
    }

    public String password() {
        return this.password;
    }

    public String host() {
        return this.host;
    }

    public int port() {
        return this.port;
    }

    public static int defaultPort(String str) {
        if (str.equals("http")) {
            return 80;
        }
        return str.equals("https") != null ? 443 : -1;
    }

    public int pathSize() {
        return this.pathSegments.size();
    }

    public String encodedPath() {
        int indexOf = this.url.indexOf(47, this.scheme.length() + 3);
        return this.url.substring(indexOf, Util.delimiterOffset(this.url, indexOf, this.url.length(), "?#"));
    }

    static void pathSegmentsToString(StringBuilder stringBuilder, List<String> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(IOUtils.DIR_SEPARATOR_UNIX);
            stringBuilder.append((String) list.get(i));
        }
    }

    public List<String> encodedPathSegments() {
        int indexOf = this.url.indexOf(47, this.scheme.length() + 3);
        int delimiterOffset = Util.delimiterOffset(this.url, indexOf, this.url.length(), "?#");
        List<String> arrayList = new ArrayList();
        while (indexOf < delimiterOffset) {
            indexOf++;
            int delimiterOffset2 = Util.delimiterOffset(this.url, indexOf, delimiterOffset, IOUtils.DIR_SEPARATOR_UNIX);
            arrayList.add(this.url.substring(indexOf, delimiterOffset2));
            indexOf = delimiterOffset2;
        }
        return arrayList;
    }

    public List<String> pathSegments() {
        return this.pathSegments;
    }

    @Nullable
    public String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        int indexOf = this.url.indexOf(63) + 1;
        return this.url.substring(indexOf, Util.delimiterOffset(this.url, indexOf + 1, this.url.length(), '#'));
    }

    static void namesAndValuesToQueryString(StringBuilder stringBuilder, List<String> list) {
        int size = list.size();
        for (int i = 0; i < size; i += 2) {
            String str = (String) list.get(i);
            String str2 = (String) list.get(i + 1);
            if (i > 0) {
                stringBuilder.append(Typography.amp);
            }
            stringBuilder.append(str);
            if (str2 != null) {
                stringBuilder.append('=');
                stringBuilder.append(str2);
            }
        }
    }

    static List<String> queryStringToNamesAndValues(String str) {
        List<String> arrayList = new ArrayList();
        int i = 0;
        while (i <= str.length()) {
            int indexOf = str.indexOf(38, i);
            if (indexOf == -1) {
                indexOf = str.length();
            }
            int indexOf2 = str.indexOf(61, i);
            if (indexOf2 != -1) {
                if (indexOf2 <= indexOf) {
                    arrayList.add(str.substring(i, indexOf2));
                    arrayList.add(str.substring(indexOf2 + 1, indexOf));
                    i = indexOf + 1;
                }
            }
            arrayList.add(str.substring(i, indexOf));
            arrayList.add(null);
            i = indexOf + 1;
        }
        return arrayList;
    }

    @Nullable
    public String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        namesAndValuesToQueryString(stringBuilder, this.queryNamesAndValues);
        return stringBuilder.toString();
    }

    public int querySize() {
        return this.queryNamesAndValues != null ? this.queryNamesAndValues.size() / 2 : 0;
    }

    @Nullable
    public String queryParameter(String str) {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        int size = this.queryNamesAndValues.size();
        for (int i = 0; i < size; i += 2) {
            if (str.equals(this.queryNamesAndValues.get(i))) {
                return (String) this.queryNamesAndValues.get(i + 1);
            }
        }
        return null;
    }

    public Set<String> queryParameterNames() {
        if (this.queryNamesAndValues == null) {
            return Collections.emptySet();
        }
        Set linkedHashSet = new LinkedHashSet();
        int size = this.queryNamesAndValues.size();
        for (int i = 0; i < size; i += 2) {
            linkedHashSet.add(this.queryNamesAndValues.get(i));
        }
        return Collections.unmodifiableSet(linkedHashSet);
    }

    public List<String> queryParameterValues(String str) {
        if (this.queryNamesAndValues == null) {
            return Collections.emptyList();
        }
        List arrayList = new ArrayList();
        int size = this.queryNamesAndValues.size();
        for (int i = 0; i < size; i += 2) {
            if (str.equals(this.queryNamesAndValues.get(i))) {
                arrayList.add(this.queryNamesAndValues.get(i + 1));
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public String queryParameterName(int i) {
        if (this.queryNamesAndValues != null) {
            return (String) this.queryNamesAndValues.get(i * 2);
        }
        throw new IndexOutOfBoundsException();
    }

    public String queryParameterValue(int i) {
        if (this.queryNamesAndValues != null) {
            return (String) this.queryNamesAndValues.get((i * 2) + 1);
        }
        throw new IndexOutOfBoundsException();
    }

    @Nullable
    public String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        return this.url.substring(this.url.indexOf(35) + 1);
    }

    @Nullable
    public String fragment() {
        return this.fragment;
    }

    public String redact() {
        return newBuilder("/...").username("").password("").build().toString();
    }

    @Nullable
    public HttpUrl resolve(String str) {
        str = newBuilder(str);
        return str != null ? str.build() : null;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.scheme = this.scheme;
        builder.encodedUsername = encodedUsername();
        builder.encodedPassword = encodedPassword();
        builder.host = this.host;
        builder.port = this.port != defaultPort(this.scheme) ? this.port : -1;
        builder.encodedPathSegments.clear();
        builder.encodedPathSegments.addAll(encodedPathSegments());
        builder.encodedQuery(encodedQuery());
        builder.encodedFragment = encodedFragment();
        return builder;
    }

    @Nullable
    public Builder newBuilder(String str) {
        Builder builder = new Builder();
        return builder.parse(this, str) == HttpUrl$Builder$ParseResult.SUCCESS ? builder : null;
    }

    @Nullable
    public static HttpUrl parse(String str) {
        Builder builder = new Builder();
        if (builder.parse(null, str) == HttpUrl$Builder$ParseResult.SUCCESS) {
            return builder.build();
        }
        return null;
    }

    @Nullable
    public static HttpUrl get(URL url) {
        return parse(url.toString());
    }

    static HttpUrl getChecked(String str) throws MalformedURLException, UnknownHostException {
        Builder builder = new Builder();
        HttpUrl$Builder$ParseResult parse = builder.parse(null, str);
        switch (HttpUrl$1.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult[parse.ordinal()]) {
            case 1:
                return builder.build();
            case 2:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid host: ");
                stringBuilder.append(str);
                throw new UnknownHostException(stringBuilder.toString());
            default:
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Invalid URL: ");
                stringBuilder2.append(parse);
                stringBuilder2.append(" for ");
                stringBuilder2.append(str);
                throw new MalformedURLException(stringBuilder2.toString());
        }
    }

    @Nullable
    public static HttpUrl get(URI uri) {
        return parse(uri.toString());
    }

    public boolean equals(@Nullable Object obj) {
        return (!(obj instanceof HttpUrl) || ((HttpUrl) obj).url.equals(this.url) == null) ? null : true;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public String toString() {
        return this.url;
    }

    @Nullable
    public String topPrivateDomain() {
        if (Util.verifyAsIpAddress(this.host)) {
            return null;
        }
        return PublicSuffixDatabase.get().getEffectiveTldPlusOne(this.host);
    }

    static String percentDecode(String str, boolean z) {
        return percentDecode(str, 0, str.length(), z);
    }

    private List<String> percentDecode(List<String> list, boolean z) {
        int size = list.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            String str = (String) list.get(i);
            arrayList.add(str != null ? percentDecode(str, z) : null);
        }
        return Collections.unmodifiableList(arrayList);
    }

    static String percentDecode(String str, int i, int i2, boolean z) {
        int i3 = i;
        while (i3 < i2) {
            char charAt = str.charAt(i3);
            if (charAt != '%') {
                if (charAt != '+' || !z) {
                    i3++;
                }
            }
            Buffer buffer = new Buffer();
            buffer.writeUtf8(str, i, i3);
            percentDecode(buffer, str, i3, i2, z);
            return buffer.readUtf8();
        }
        return str.substring(i, i2);
    }

    static void percentDecode(Buffer buffer, String str, int i, int i2, boolean z) {
        while (i < i2) {
            int codePointAt = str.codePointAt(i);
            if (codePointAt == 37) {
                int i3 = i + 2;
                if (i3 < i2) {
                    int decodeHexDigit = decodeHexDigit(str.charAt(i + 1));
                    int decodeHexDigit2 = decodeHexDigit(str.charAt(i3));
                    if (!(decodeHexDigit == -1 || decodeHexDigit2 == -1)) {
                        buffer.writeByte((decodeHexDigit << 4) + decodeHexDigit2);
                        i = i3;
                        i += Character.charCount(codePointAt);
                    }
                    buffer.writeUtf8CodePoint(codePointAt);
                    i += Character.charCount(codePointAt);
                }
            }
            if (codePointAt == 43 && z) {
                buffer.writeByte(32);
                i += Character.charCount(codePointAt);
            }
            buffer.writeUtf8CodePoint(codePointAt);
            i += Character.charCount(codePointAt);
        }
    }

    static boolean percentEncoded(String str, int i, int i2) {
        int i3 = i + 2;
        if (i3 >= i2 || str.charAt(i) != 37 || decodeHexDigit(str.charAt(i + 1)) == -1 || decodeHexDigit(str.charAt(i3)) == -1) {
            return false;
        }
        return true;
    }

    static String canonicalize(String str, int i, int i2, String str2, boolean z, boolean z2, boolean z3, boolean z4) {
        String str3 = str;
        int i3 = i2;
        int i4 = i;
        while (i4 < i3) {
            String str4;
            int codePointAt = str3.codePointAt(i4);
            if (codePointAt < 32 || codePointAt == 127 || (codePointAt >= 128 && z4)) {
                str4 = str2;
            } else {
                str4 = str2;
                if (str4.indexOf(codePointAt) == -1 && (codePointAt != 37 || (z && (!z2 || percentEncoded(str3, i4, i3))))) {
                    if (codePointAt != 43 || !z3) {
                        i4 += Character.charCount(codePointAt);
                    }
                }
            }
            Buffer buffer = new Buffer();
            buffer.writeUtf8(str3, i, i4);
            canonicalize(buffer, str3, i4, i3, str4, z, z2, z3, z4);
            return buffer.readUtf8();
        }
        return str3.substring(i, i3);
    }

    static void canonicalize(Buffer buffer, String str, int i, int i2, String str2, boolean z, boolean z2, boolean z3, boolean z4) {
        Buffer buffer2 = null;
        while (i < i2) {
            int codePointAt = str.codePointAt(i);
            if (z) {
                if (!(codePointAt == 9 || codePointAt == 10 || codePointAt == 12)) {
                    if (codePointAt == 13) {
                    }
                }
                i += Character.charCount(codePointAt);
            }
            if (codePointAt == 43 && z3) {
                buffer.writeUtf8(z ? "+" : "%2B");
                i += Character.charCount(codePointAt);
            } else {
                if (codePointAt >= 32 && codePointAt != 127 && ((codePointAt < 128 || !z4) && str2.indexOf(codePointAt) == -1)) {
                    if (codePointAt == 37) {
                        if (z) {
                            if (z2 && !percentEncoded(str, i, i2)) {
                            }
                        }
                    }
                    buffer.writeUtf8CodePoint(codePointAt);
                    i += Character.charCount(codePointAt);
                }
                if (buffer2 == null) {
                    buffer2 = new Buffer();
                }
                buffer2.writeUtf8CodePoint(codePointAt);
                while (!buffer2.exhausted()) {
                    int readByte = buffer2.readByte() & 255;
                    buffer.writeByte(37);
                    buffer.writeByte(HEX_DIGITS[(readByte >> 4) & 15]);
                    buffer.writeByte(HEX_DIGITS[readByte & 15]);
                }
                i += Character.charCount(codePointAt);
            }
        }
    }

    static String canonicalize(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4) {
        return canonicalize(str, 0, str.length(), str2, z, z2, z3, z4);
    }
}
