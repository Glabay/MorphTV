package okhttp3;

import com.google.common.base.Ascii;
import com.google.common.net.HttpHeaders;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Cookie {
    private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    private static final Pattern MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
    private final String domain;
    private final long expiresAt;
    private final boolean hostOnly;
    private final boolean httpOnly;
    private final String name;
    private final String path;
    private final boolean persistent;
    private final boolean secure;
    private final String value;

    private Cookie(String str, String str2, long j, String str3, String str4, boolean z, boolean z2, boolean z3, boolean z4) {
        this.name = str;
        this.value = str2;
        this.expiresAt = j;
        this.domain = str3;
        this.path = str4;
        this.secure = z;
        this.httpOnly = z2;
        this.hostOnly = z3;
        this.persistent = z4;
    }

    Cookie(Cookie$Builder cookie$Builder) {
        if (cookie$Builder.name == null) {
            throw new NullPointerException("builder.name == null");
        } else if (cookie$Builder.value == null) {
            throw new NullPointerException("builder.value == null");
        } else if (cookie$Builder.domain == null) {
            throw new NullPointerException("builder.domain == null");
        } else {
            this.name = cookie$Builder.name;
            this.value = cookie$Builder.value;
            this.expiresAt = cookie$Builder.expiresAt;
            this.domain = cookie$Builder.domain;
            this.path = cookie$Builder.path;
            this.secure = cookie$Builder.secure;
            this.httpOnly = cookie$Builder.httpOnly;
            this.persistent = cookie$Builder.persistent;
            this.hostOnly = cookie$Builder.hostOnly;
        }
    }

    public String name() {
        return this.name;
    }

    public String value() {
        return this.value;
    }

    public boolean persistent() {
        return this.persistent;
    }

    public long expiresAt() {
        return this.expiresAt;
    }

    public boolean hostOnly() {
        return this.hostOnly;
    }

    public String domain() {
        return this.domain;
    }

    public String path() {
        return this.path;
    }

    public boolean httpOnly() {
        return this.httpOnly;
    }

    public boolean secure() {
        return this.secure;
    }

    public boolean matches(HttpUrl httpUrl) {
        boolean equals;
        if (this.hostOnly) {
            equals = httpUrl.host().equals(this.domain);
        } else {
            equals = domainMatch(httpUrl.host(), this.domain);
        }
        if (!equals || !pathMatch(httpUrl, this.path)) {
            return false;
        }
        if (this.secure && httpUrl.isHttps() == null) {
            return false;
        }
        return true;
    }

    private static boolean domainMatch(String str, String str2) {
        if (str.equals(str2)) {
            return true;
        }
        if (str.endsWith(str2) && str.charAt((str.length() - str2.length()) - 1) == 46 && Util.verifyAsIpAddress(str) == null) {
            return true;
        }
        return null;
    }

    private static boolean pathMatch(HttpUrl httpUrl, String str) {
        httpUrl = httpUrl.encodedPath();
        if (httpUrl.equals(str)) {
            return true;
        }
        if (httpUrl.startsWith(str) && (str.endsWith("/") || httpUrl.charAt(str.length()) == 47)) {
            return true;
        }
        return null;
    }

    @Nullable
    public static Cookie parse(HttpUrl httpUrl, String str) {
        return parse(System.currentTimeMillis(), httpUrl, str);
    }

    @javax.annotation.Nullable
    static okhttp3.Cookie parse(long r26, okhttp3.HttpUrl r28, java.lang.String r29) {
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
        r2 = r29;
        r3 = r29.length();
        r4 = 59;
        r5 = 0;
        r6 = okhttp3.internal.Util.delimiterOffset(r2, r5, r3, r4);
        r7 = 61;
        r8 = okhttp3.internal.Util.delimiterOffset(r2, r5, r6, r7);
        r9 = 0;
        if (r8 != r6) goto L_0x0017;
    L_0x0016:
        return r9;
    L_0x0017:
        r11 = okhttp3.internal.Util.trimSubstring(r2, r5, r8);
        r10 = r11.isEmpty();
        if (r10 != 0) goto L_0x014e;
    L_0x0021:
        r10 = okhttp3.internal.Util.indexOfControlOrNonAscii(r11);
        r12 = -1;
        if (r10 == r12) goto L_0x002a;
    L_0x0028:
        goto L_0x014e;
    L_0x002a:
        r10 = 1;
        r8 = r8 + r10;
        r8 = okhttp3.internal.Util.trimSubstring(r2, r8, r6);
        r13 = okhttp3.internal.Util.indexOfControlOrNonAscii(r8);
        if (r13 == r12) goto L_0x0037;
    L_0x0036:
        return r9;
    L_0x0037:
        r6 = r6 + r10;
        r12 = -1;
        r14 = 253402300799999; // 0xe677d21fdbff float:-1.71647681E11 double:1.251973714024093E-309;
        r10 = r9;
        r21 = r10;
        r16 = r12;
        r22 = r14;
        r18 = 0;
        r19 = 0;
        r20 = 1;
        r24 = 0;
    L_0x004e:
        if (r6 >= r3) goto L_0x00c3;
    L_0x0050:
        r9 = okhttp3.internal.Util.delimiterOffset(r2, r6, r3, r4);
        r4 = okhttp3.internal.Util.delimiterOffset(r2, r6, r9, r7);
        r6 = okhttp3.internal.Util.trimSubstring(r2, r6, r4);
        if (r4 >= r9) goto L_0x0065;
    L_0x005e:
        r4 = r4 + 1;
        r4 = okhttp3.internal.Util.trimSubstring(r2, r4, r9);
        goto L_0x0067;
    L_0x0065:
        r4 = "";
    L_0x0067:
        r7 = "expires";
        r7 = r6.equalsIgnoreCase(r7);
        if (r7 == 0) goto L_0x007c;
    L_0x006f:
        r6 = r4.length();	 Catch:{ IllegalArgumentException -> 0x00bb }
        r6 = parseExpires(r4, r5, r6);	 Catch:{ IllegalArgumentException -> 0x00bb }
        r22 = r6;
    L_0x0079:
        r24 = 1;
        goto L_0x00bb;
    L_0x007c:
        r7 = "max-age";
        r7 = r6.equalsIgnoreCase(r7);
        if (r7 == 0) goto L_0x008b;
    L_0x0084:
        r6 = parseMaxAge(r4);	 Catch:{  }
        r16 = r6;
        goto L_0x0079;
    L_0x008b:
        r7 = "domain";
        r7 = r6.equalsIgnoreCase(r7);
        if (r7 == 0) goto L_0x009b;
    L_0x0093:
        r4 = parseDomain(r4);	 Catch:{ IllegalArgumentException -> 0x00bb }
        r10 = r4;
        r20 = 0;
        goto L_0x00bb;
    L_0x009b:
        r7 = "path";
        r7 = r6.equalsIgnoreCase(r7);
        if (r7 == 0) goto L_0x00a6;
    L_0x00a3:
        r21 = r4;
        goto L_0x00bb;
    L_0x00a6:
        r4 = "secure";
        r4 = r6.equalsIgnoreCase(r4);
        if (r4 == 0) goto L_0x00b1;
    L_0x00ae:
        r18 = 1;
        goto L_0x00bb;
    L_0x00b1:
        r4 = "httponly";
        r4 = r6.equalsIgnoreCase(r4);
        if (r4 == 0) goto L_0x00bb;
    L_0x00b9:
        r19 = 1;
    L_0x00bb:
        r6 = r9 + 1;
        r4 = 59;
        r7 = 61;
        r9 = 0;
        goto L_0x004e;
    L_0x00c3:
        r2 = -9223372036854775808;
        r4 = (r16 > r2 ? 1 : (r16 == r2 ? 0 : -1));
        if (r4 != 0) goto L_0x00cb;
    L_0x00c9:
        r13 = r2;
        goto L_0x00f1;
    L_0x00cb:
        r2 = (r16 > r12 ? 1 : (r16 == r12 ? 0 : -1));
        if (r2 == 0) goto L_0x00ef;
    L_0x00cf:
        r2 = 9223372036854775; // 0x20c49ba5e353f7 float:-3.943512E-16 double:4.663754807431093E-308;
        r4 = (r16 > r2 ? 1 : (r16 == r2 ? 0 : -1));
        if (r4 > 0) goto L_0x00dd;
    L_0x00d8:
        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r16 = r16 * r2;
        goto L_0x00e2;
    L_0x00dd:
        r16 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x00e2:
        r2 = 0;
        r2 = r26 + r16;
        r4 = (r2 > r26 ? 1 : (r2 == r26 ? 0 : -1));
        if (r4 < 0) goto L_0x00ed;
    L_0x00e9:
        r0 = (r2 > r14 ? 1 : (r2 == r14 ? 0 : -1));
        if (r0 <= 0) goto L_0x00c9;
    L_0x00ed:
        r13 = r14;
        goto L_0x00f1;
    L_0x00ef:
        r13 = r22;
    L_0x00f1:
        r0 = r28.host();
        if (r10 != 0) goto L_0x00fa;
    L_0x00f7:
        r15 = r0;
        r1 = 0;
        goto L_0x0104;
    L_0x00fa:
        r1 = domainMatch(r0, r10);
        if (r1 != 0) goto L_0x0102;
    L_0x0100:
        r1 = 0;
        return r1;
    L_0x0102:
        r1 = 0;
        r15 = r10;
    L_0x0104:
        r0 = r0.length();
        r2 = r15.length();
        if (r0 == r2) goto L_0x0119;
    L_0x010e:
        r0 = okhttp3.internal.publicsuffix.PublicSuffixDatabase.get();
        r0 = r0.getEffectiveTldPlusOne(r15);
        if (r0 != 0) goto L_0x0119;
    L_0x0118:
        return r1;
    L_0x0119:
        r9 = r21;
        if (r9 == 0) goto L_0x0129;
    L_0x011d:
        r0 = "/";
        r0 = r9.startsWith(r0);
        if (r0 != 0) goto L_0x0126;
    L_0x0125:
        goto L_0x0129;
    L_0x0126:
        r16 = r9;
        goto L_0x013e;
    L_0x0129:
        r0 = r28.encodedPath();
        r1 = 47;
        r1 = r0.lastIndexOf(r1);
        if (r1 == 0) goto L_0x013a;
    L_0x0135:
        r0 = r0.substring(r5, r1);
        goto L_0x013c;
    L_0x013a:
        r0 = "/";
    L_0x013c:
        r16 = r0;
    L_0x013e:
        r0 = new okhttp3.Cookie;
        r10 = r0;
        r12 = r8;
        r17 = r18;
        r18 = r19;
        r19 = r20;
        r20 = r24;
        r10.<init>(r11, r12, r13, r15, r16, r17, r18, r19, r20);
        return r0;
    L_0x014e:
        r0 = r9;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cookie.parse(long, okhttp3.HttpUrl, java.lang.String):okhttp3.Cookie");
    }

    private static long parseExpires(String str, int i, int i2) {
        i = dateCharacterOffset(str, i, i2, false);
        Matcher matcher = TIME_PATTERN.matcher(str);
        int i3 = -1;
        int i4 = -1;
        int i5 = -1;
        int i6 = -1;
        int i7 = -1;
        int i8 = -1;
        while (i < i2) {
            int dateCharacterOffset = dateCharacterOffset(str, i + 1, i2, true);
            matcher.region(i, dateCharacterOffset);
            if (i3 == -1 && matcher.usePattern(TIME_PATTERN).matches() != 0) {
                i = Integer.parseInt(matcher.group(1));
                i3 = Integer.parseInt(matcher.group(2));
                i8 = Integer.parseInt(matcher.group(3));
                i7 = i3;
                i3 = i;
            } else if (i5 == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches() != 0) {
                i5 = Integer.parseInt(matcher.group(1));
            } else if (i6 == -1 && matcher.usePattern(MONTH_PATTERN).matches() != 0) {
                i6 = MONTH_PATTERN.pattern().indexOf(matcher.group(1).toLowerCase(Locale.US)) / 4;
            } else if (i4 == -1 && matcher.usePattern(YEAR_PATTERN).matches() != 0) {
                i4 = Integer.parseInt(matcher.group(1));
            }
            i = dateCharacterOffset(str, dateCharacterOffset + 1, i2, false);
        }
        if (i4 >= 70 && i4 <= 99) {
            i4 += 1900;
        }
        if (i4 >= 0 && i4 <= 69) {
            i4 += 2000;
        }
        if (i4 < 1601) {
            throw new IllegalArgumentException();
        } else if (i6 == -1) {
            throw new IllegalArgumentException();
        } else {
            if (i5 >= 1) {
                if (i5 <= 31) {
                    if (i3 >= 0) {
                        if (i3 <= 23) {
                            if (i7 >= 0) {
                                if (i7 <= 59) {
                                    if (i8 >= 0) {
                                        if (i8 <= 59) {
                                            str = new GregorianCalendar(Util.UTC);
                                            str.setLenient(false);
                                            str.set(1, i4);
                                            str.set(2, i6 - 1);
                                            str.set(5, i5);
                                            str.set(11, i3);
                                            str.set(12, i7);
                                            str.set(13, i8);
                                            str.set(14, 0);
                                            return str.getTimeInMillis();
                                        }
                                    }
                                    throw new IllegalArgumentException();
                                }
                            }
                            throw new IllegalArgumentException();
                        }
                    }
                    throw new IllegalArgumentException();
                }
            }
            throw new IllegalArgumentException();
        }
    }

    private static int dateCharacterOffset(String str, int i, int i2, boolean z) {
        while (i < i2) {
            int i3;
            char charAt = str.charAt(i);
            if ((charAt >= ' ' || charAt == '\t') && charAt < Ascii.MAX && ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'z') && (charAt < 'A' || charAt > 'Z')))) {
                if (charAt != ':') {
                    i3 = 0;
                    if (i3 == (z ^ 1)) {
                        return i;
                    }
                    i++;
                }
            }
            i3 = 1;
            if (i3 == (z ^ 1)) {
                return i;
            }
            i++;
        }
        return i2;
    }

    private static long parseMaxAge(String str) {
        long j = Long.MIN_VALUE;
        try {
            long parseLong = Long.parseLong(str);
            if (parseLong > 0) {
                j = parseLong;
            }
            return j;
        } catch (NumberFormatException e) {
            if (str.matches("-?\\d+")) {
                if (str.startsWith("-") == null) {
                    j = Long.MAX_VALUE;
                }
                return j;
            }
            throw e;
        }
    }

    private static String parseDomain(String str) {
        if (str.endsWith(".")) {
            throw new IllegalArgumentException();
        }
        if (str.startsWith(".")) {
            str = str.substring(1);
        }
        str = Util.domainToAscii(str);
        if (str != null) {
            return str;
        }
        throw new IllegalArgumentException();
    }

    public static List<Cookie> parseAll(HttpUrl httpUrl, Headers headers) {
        headers = headers.values(HttpHeaders.SET_COOKIE);
        int size = headers.size();
        List list = null;
        for (int i = 0; i < size; i++) {
            Cookie parse = parse(httpUrl, (String) headers.get(i));
            if (parse != null) {
                if (list == null) {
                    list = new ArrayList();
                }
                list.add(parse);
            }
        }
        if (list != null) {
            return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

    public String toString() {
        return toString(false);
    }

    String toString(boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        stringBuilder.append('=');
        stringBuilder.append(this.value);
        if (this.persistent) {
            if (this.expiresAt == Long.MIN_VALUE) {
                stringBuilder.append("; max-age=0");
            } else {
                stringBuilder.append("; expires=");
                stringBuilder.append(HttpDate.format(new Date(this.expiresAt)));
            }
        }
        if (!this.hostOnly) {
            stringBuilder.append("; domain=");
            if (z) {
                stringBuilder.append(".");
            }
            stringBuilder.append(this.domain);
        }
        stringBuilder.append("; path=");
        stringBuilder.append(this.path);
        if (this.secure) {
            stringBuilder.append("; secure");
        }
        if (this.httpOnly) {
            stringBuilder.append("; httponly");
        }
        return stringBuilder.toString();
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof Cookie)) {
            return false;
        }
        Cookie cookie = (Cookie) obj;
        if (cookie.name.equals(this.name) && cookie.value.equals(this.value) && cookie.domain.equals(this.domain) && cookie.path.equals(this.path) && cookie.expiresAt == this.expiresAt && cookie.secure == this.secure && cookie.httpOnly == this.httpOnly && cookie.persistent == this.persistent && cookie.hostOnly == this.hostOnly) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return ((((((((((((((((527 + this.name.hashCode()) * 31) + this.value.hashCode()) * 31) + this.domain.hashCode()) * 31) + this.path.hashCode()) * 31) + ((int) (this.expiresAt ^ (this.expiresAt >>> 32)))) * 31) + (this.secure ^ 1)) * 31) + (this.httpOnly ^ 1)) * 31) + (this.persistent ^ 1)) * 31) + (this.hostOnly ^ 1);
    }
}
