package org.threeten.bp.temporal;

import com.google.android.exoplayer2.C0649C;
import org.threeten.bp.Duration;

public enum ChronoUnit implements TemporalUnit {
    NANOS("Nanos", Duration.ofNanos(1)),
    MICROS("Micros", Duration.ofNanos(1000)),
    MILLIS("Millis", Duration.ofNanos(C0649C.MICROS_PER_SECOND)),
    SECONDS("Seconds", Duration.ofSeconds(1)),
    MINUTES("Minutes", Duration.ofSeconds(60)),
    HOURS("Hours", Duration.ofSeconds(3600)),
    HALF_DAYS("HalfDays", Duration.ofSeconds(43200)),
    DAYS("Days", Duration.ofSeconds(86400)),
    WEEKS("Weeks", Duration.ofSeconds(604800)),
    MONTHS("Months", Duration.ofSeconds(2629746)),
    YEARS("Years", Duration.ofSeconds(31556952)),
    DECADES("Decades", Duration.ofSeconds(315569520)),
    CENTURIES("Centuries", Duration.ofSeconds(3155695200L)),
    MILLENNIA("Millennia", Duration.ofSeconds(31556952000L)),
    ERAS("Eras", Duration.ofSeconds(31556952000000000L)),
    FOREVER("Forever", Duration.ofSeconds(Long.MAX_VALUE, 999999999));
    
    private final Duration duration;
    private final String name;

    private ChronoUnit(String str, Duration duration) {
        this.name = str;
        this.duration = duration;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public boolean isDurationEstimated() {
        if (!isDateBased()) {
            if (this != FOREVER) {
                return false;
            }
        }
        return true;
    }

    public boolean isDateBased() {
        return compareTo(DAYS) >= 0 && this != FOREVER;
    }

    public boolean isTimeBased() {
        return compareTo(DAYS) < 0;
    }

    public boolean isSupportedBy(org.threeten.bp.temporal.Temporal r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r5 = this;
        r0 = FOREVER;
        r1 = 0;
        if (r5 != r0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r6 instanceof org.threeten.bp.chrono.ChronoLocalDate;
        if (r0 == 0) goto L_0x000f;
    L_0x000a:
        r6 = r5.isDateBased();
        return r6;
    L_0x000f:
        r0 = r6 instanceof org.threeten.bp.chrono.ChronoLocalDateTime;
        r2 = 1;
        if (r0 != 0) goto L_0x0026;
    L_0x0014:
        r0 = r6 instanceof org.threeten.bp.chrono.ChronoZonedDateTime;
        if (r0 == 0) goto L_0x0019;
    L_0x0018:
        goto L_0x0026;
    L_0x0019:
        r3 = 1;
        r6.plus(r3, r5);	 Catch:{ RuntimeException -> 0x001f }
        return r2;
    L_0x001f:
        r3 = -1;
        r6.plus(r3, r5);	 Catch:{ RuntimeException -> 0x0025 }
        return r2;
    L_0x0025:
        return r1;
    L_0x0026:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.temporal.ChronoUnit.isSupportedBy(org.threeten.bp.temporal.Temporal):boolean");
    }

    public <R extends Temporal> R addTo(R r, long j) {
        return r.plus(j, this);
    }

    public long between(Temporal temporal, Temporal temporal2) {
        return temporal.until(temporal2, this);
    }

    public String toString() {
        return this.name;
    }
}
