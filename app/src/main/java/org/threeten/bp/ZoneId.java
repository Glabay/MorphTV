package org.threeten.bp;

import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.jdk8.DefaultInterfaceTemporalAccessor;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.zone.ZoneRules;
import org.threeten.bp.zone.ZoneRulesProvider;

public abstract class ZoneId implements Serializable {
    public static final TemporalQuery<ZoneId> FROM = new C15321();
    public static final Map<String, String> SHORT_IDS;
    private static final long serialVersionUID = 8352817235686L;

    /* renamed from: org.threeten.bp.ZoneId$1 */
    static class C15321 implements TemporalQuery<ZoneId> {
        C15321() {
        }

        public ZoneId queryFrom(TemporalAccessor temporalAccessor) {
            return ZoneId.from(temporalAccessor);
        }
    }

    /* renamed from: org.threeten.bp.ZoneId$2 */
    class C15332 extends DefaultInterfaceTemporalAccessor {
        public boolean isSupported(TemporalField temporalField) {
            return false;
        }

        C15332() {
        }

        public long getLong(TemporalField temporalField) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported field: ");
            stringBuilder.append(temporalField);
            throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }

        public <R> R query(TemporalQuery<R> temporalQuery) {
            if (temporalQuery == TemporalQueries.zoneId()) {
                return ZoneId.this;
            }
            return super.query(temporalQuery);
        }
    }

    public abstract String getId();

    public abstract ZoneRules getRules();

    abstract void write(DataOutput dataOutput) throws IOException;

    static {
        Map hashMap = new HashMap();
        hashMap.put("ACT", "Australia/Darwin");
        hashMap.put("AET", "Australia/Sydney");
        hashMap.put("AGT", "America/Argentina/Buenos_Aires");
        hashMap.put("ART", "Africa/Cairo");
        hashMap.put("AST", "America/Anchorage");
        hashMap.put("BET", "America/Sao_Paulo");
        hashMap.put("BST", "Asia/Dhaka");
        hashMap.put("CAT", "Africa/Harare");
        hashMap.put("CNT", "America/St_Johns");
        hashMap.put("CST", "America/Chicago");
        hashMap.put("CTT", "Asia/Shanghai");
        hashMap.put("EAT", "Africa/Addis_Ababa");
        hashMap.put("ECT", "Europe/Paris");
        hashMap.put("IET", "America/Indiana/Indianapolis");
        hashMap.put("IST", "Asia/Kolkata");
        hashMap.put("JST", "Asia/Tokyo");
        hashMap.put("MIT", "Pacific/Apia");
        hashMap.put("NET", "Asia/Yerevan");
        hashMap.put("NST", "Pacific/Auckland");
        hashMap.put("PLT", "Asia/Karachi");
        hashMap.put("PNT", "America/Phoenix");
        hashMap.put("PRT", "America/Puerto_Rico");
        hashMap.put("PST", "America/Los_Angeles");
        hashMap.put("SST", "Pacific/Guadalcanal");
        hashMap.put("VST", "Asia/Ho_Chi_Minh");
        hashMap.put("EST", "-05:00");
        hashMap.put("MST", "-07:00");
        hashMap.put("HST", "-10:00");
        SHORT_IDS = Collections.unmodifiableMap(hashMap);
    }

    public static ZoneId systemDefault() {
        return of(TimeZone.getDefault().getID(), SHORT_IDS);
    }

    public static Set<String> getAvailableZoneIds() {
        return ZoneRulesProvider.getAvailableZoneIds();
    }

    public static ZoneId of(String str, Map<String, String> map) {
        Jdk8Methods.requireNonNull(str, "zoneId");
        Jdk8Methods.requireNonNull(map, "aliasMap");
        String str2 = (String) map.get(str);
        if (str2 != null) {
            str = str2;
        }
        return of(str);
    }

    public static ZoneId of(String str) {
        Jdk8Methods.requireNonNull(str, "zoneId");
        if (str.equals("Z")) {
            return ZoneOffset.UTC;
        }
        if (str.length() == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid zone: ");
            stringBuilder.append(str);
            throw new DateTimeException(stringBuilder.toString());
        }
        if (!str.startsWith("+")) {
            if (!str.startsWith("-")) {
                if (!(str.equals("UTC") || str.equals("GMT"))) {
                    if (!str.equals("UT")) {
                        if (!(str.startsWith("UTC+") || str.startsWith("GMT+") || str.startsWith("UTC-"))) {
                            if (!str.startsWith("GMT-")) {
                                if (!str.startsWith("UT+")) {
                                    if (!str.startsWith("UT-")) {
                                        return ZoneRegion.ofId(str, true);
                                    }
                                }
                                str = ZoneOffset.of(str.substring(2));
                                if (str.getTotalSeconds() == 0) {
                                    return new ZoneRegion("UT", str.getRules());
                                }
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("UT");
                                stringBuilder.append(str.getId());
                                return new ZoneRegion(stringBuilder.toString(), str.getRules());
                            }
                        }
                        ZoneOffset of = ZoneOffset.of(str.substring(3));
                        if (of.getTotalSeconds() == 0) {
                            return new ZoneRegion(str.substring(0, 3), of.getRules());
                        }
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str.substring(0, 3));
                        stringBuilder2.append(of.getId());
                        return new ZoneRegion(stringBuilder2.toString(), of.getRules());
                    }
                }
                return new ZoneRegion(str, ZoneOffset.UTC.getRules());
            }
        }
        return ZoneOffset.of(str);
    }

    public static ZoneId ofOffset(String str, ZoneOffset zoneOffset) {
        Jdk8Methods.requireNonNull(str, "prefix");
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        if (str.length() == 0) {
            return zoneOffset;
        }
        if (!(str.equals("GMT") || str.equals("UTC"))) {
            if (!str.equals("UT")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid prefix, must be GMT, UTC or UT: ");
                stringBuilder.append(str);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        if (zoneOffset.getTotalSeconds() == 0) {
            return new ZoneRegion(str, zoneOffset.getRules());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(zoneOffset.getId());
        return new ZoneRegion(stringBuilder2.toString(), zoneOffset.getRules());
    }

    public static ZoneId from(TemporalAccessor temporalAccessor) {
        ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zone());
        if (zoneId != null) {
            return zoneId;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to obtain ZoneId from TemporalAccessor: ");
        stringBuilder.append(temporalAccessor);
        stringBuilder.append(", type ");
        stringBuilder.append(temporalAccessor.getClass().getName());
        throw new DateTimeException(stringBuilder.toString());
    }

    ZoneId() {
        if (getClass() != ZoneOffset.class && getClass() != ZoneRegion.class) {
            throw new AssertionError("Invalid subclass");
        }
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendZoneText(textStyle).toFormatter(locale).format(new C15332());
    }

    public org.threeten.bp.ZoneId normalized() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = this;
        r0 = r2.getRules();	 Catch:{ ZoneRulesException -> 0x0011 }
        r1 = r0.isFixedOffset();	 Catch:{ ZoneRulesException -> 0x0011 }
        if (r1 == 0) goto L_0x0011;	 Catch:{ ZoneRulesException -> 0x0011 }
    L_0x000a:
        r1 = org.threeten.bp.Instant.EPOCH;	 Catch:{ ZoneRulesException -> 0x0011 }
        r0 = r0.getOffset(r1);	 Catch:{ ZoneRulesException -> 0x0011 }
        return r0;
    L_0x0011:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.ZoneId.normalized():org.threeten.bp.ZoneId");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZoneId)) {
            return null;
        }
        return getId().equals(((ZoneId) obj).getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public String toString() {
        return getId();
    }
}
