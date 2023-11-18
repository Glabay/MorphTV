package org.threeten.bp.temporal;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.Chronology;

public final class TemporalQueries {
    static final TemporalQuery<Chronology> CHRONO = new C15702();
    static final TemporalQuery<LocalDate> LOCAL_DATE = new C15746();
    static final TemporalQuery<LocalTime> LOCAL_TIME = new C15757();
    static final TemporalQuery<ZoneOffset> OFFSET = new C15735();
    static final TemporalQuery<TemporalUnit> PRECISION = new C15713();
    static final TemporalQuery<ZoneId> ZONE = new C15724();
    static final TemporalQuery<ZoneId> ZONE_ID = new C15691();

    /* renamed from: org.threeten.bp.temporal.TemporalQueries$1 */
    static class C15691 implements TemporalQuery<ZoneId> {
        C15691() {
        }

        public ZoneId queryFrom(TemporalAccessor temporalAccessor) {
            return (ZoneId) temporalAccessor.query(this);
        }
    }

    /* renamed from: org.threeten.bp.temporal.TemporalQueries$2 */
    static class C15702 implements TemporalQuery<Chronology> {
        C15702() {
        }

        public Chronology queryFrom(TemporalAccessor temporalAccessor) {
            return (Chronology) temporalAccessor.query(this);
        }
    }

    /* renamed from: org.threeten.bp.temporal.TemporalQueries$3 */
    static class C15713 implements TemporalQuery<TemporalUnit> {
        C15713() {
        }

        public TemporalUnit queryFrom(TemporalAccessor temporalAccessor) {
            return (TemporalUnit) temporalAccessor.query(this);
        }
    }

    /* renamed from: org.threeten.bp.temporal.TemporalQueries$4 */
    static class C15724 implements TemporalQuery<ZoneId> {
        C15724() {
        }

        public ZoneId queryFrom(TemporalAccessor temporalAccessor) {
            ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.ZONE_ID);
            return zoneId != null ? zoneId : (ZoneId) temporalAccessor.query(TemporalQueries.OFFSET);
        }
    }

    /* renamed from: org.threeten.bp.temporal.TemporalQueries$5 */
    static class C15735 implements TemporalQuery<ZoneOffset> {
        C15735() {
        }

        public ZoneOffset queryFrom(TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS) ? ZoneOffset.ofTotalSeconds(temporalAccessor.get(ChronoField.OFFSET_SECONDS)) : null;
        }
    }

    /* renamed from: org.threeten.bp.temporal.TemporalQueries$6 */
    static class C15746 implements TemporalQuery<LocalDate> {
        C15746() {
        }

        public LocalDate queryFrom(TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.EPOCH_DAY) ? LocalDate.ofEpochDay(temporalAccessor.getLong(ChronoField.EPOCH_DAY)) : null;
        }
    }

    /* renamed from: org.threeten.bp.temporal.TemporalQueries$7 */
    static class C15757 implements TemporalQuery<LocalTime> {
        C15757() {
        }

        public LocalTime queryFrom(TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.NANO_OF_DAY) ? LocalTime.ofNanoOfDay(temporalAccessor.getLong(ChronoField.NANO_OF_DAY)) : null;
        }
    }

    private TemporalQueries() {
    }

    public static final TemporalQuery<ZoneId> zoneId() {
        return ZONE_ID;
    }

    public static final TemporalQuery<Chronology> chronology() {
        return CHRONO;
    }

    public static final TemporalQuery<TemporalUnit> precision() {
        return PRECISION;
    }

    public static final TemporalQuery<ZoneId> zone() {
        return ZONE;
    }

    public static final TemporalQuery<ZoneOffset> offset() {
        return OFFSET;
    }

    public static final TemporalQuery<LocalDate> localDate() {
        return LOCAL_DATE;
    }

    public static final TemporalQuery<LocalTime> localTime() {
        return LOCAL_TIME;
    }
}
