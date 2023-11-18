package org.threeten.bp;

import com.google.android.exoplayer2.C0649C;
import java.io.Serializable;
import org.threeten.bp.jdk8.Jdk8Methods;

public abstract class Clock {

    static final class FixedClock extends Clock implements Serializable {
        private static final long serialVersionUID = 7430389292664866958L;
        private final Instant instant;
        private final ZoneId zone;

        FixedClock(Instant instant, ZoneId zoneId) {
            this.instant = instant;
            this.zone = zoneId;
        }

        public ZoneId getZone() {
            return this.zone;
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.zone)) {
                return this;
            }
            return new FixedClock(this.instant, zoneId);
        }

        public long millis() {
            return this.instant.toEpochMilli();
        }

        public Instant instant() {
            return this.instant;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof FixedClock)) {
                return false;
            }
            FixedClock fixedClock = (FixedClock) obj;
            if (this.instant.equals(fixedClock.instant) && this.zone.equals(fixedClock.zone) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.instant.hashCode() ^ this.zone.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FixedClock[");
            stringBuilder.append(this.instant);
            stringBuilder.append(",");
            stringBuilder.append(this.zone);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    static final class OffsetClock extends Clock implements Serializable {
        private static final long serialVersionUID = 2007484719125426256L;
        private final Clock baseClock;
        private final Duration offset;

        OffsetClock(Clock clock, Duration duration) {
            this.baseClock = clock;
            this.offset = duration;
        }

        public ZoneId getZone() {
            return this.baseClock.getZone();
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.baseClock.getZone())) {
                return this;
            }
            return new OffsetClock(this.baseClock.withZone(zoneId), this.offset);
        }

        public long millis() {
            return Jdk8Methods.safeAdd(this.baseClock.millis(), this.offset.toMillis());
        }

        public Instant instant() {
            return this.baseClock.instant().plus(this.offset);
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof OffsetClock)) {
                return false;
            }
            OffsetClock offsetClock = (OffsetClock) obj;
            if (this.baseClock.equals(offsetClock.baseClock) && this.offset.equals(offsetClock.offset) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.baseClock.hashCode() ^ this.offset.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("OffsetClock[");
            stringBuilder.append(this.baseClock);
            stringBuilder.append(",");
            stringBuilder.append(this.offset);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    static final class SystemClock extends Clock implements Serializable {
        private static final long serialVersionUID = 6740630888130243051L;
        private final ZoneId zone;

        SystemClock(ZoneId zoneId) {
            this.zone = zoneId;
        }

        public ZoneId getZone() {
            return this.zone;
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.zone)) {
                return this;
            }
            return new SystemClock(zoneId);
        }

        public long millis() {
            return System.currentTimeMillis();
        }

        public Instant instant() {
            return Instant.ofEpochMilli(millis());
        }

        public boolean equals(Object obj) {
            return obj instanceof SystemClock ? this.zone.equals(((SystemClock) obj).zone) : null;
        }

        public int hashCode() {
            return this.zone.hashCode() + 1;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SystemClock[");
            stringBuilder.append(this.zone);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    static final class TickClock extends Clock implements Serializable {
        private static final long serialVersionUID = 6504659149906368850L;
        private final Clock baseClock;
        private final long tickNanos;

        TickClock(Clock clock, long j) {
            this.baseClock = clock;
            this.tickNanos = j;
        }

        public ZoneId getZone() {
            return this.baseClock.getZone();
        }

        public Clock withZone(ZoneId zoneId) {
            if (zoneId.equals(this.baseClock.getZone())) {
                return this;
            }
            return new TickClock(this.baseClock.withZone(zoneId), this.tickNanos);
        }

        public long millis() {
            long millis = this.baseClock.millis();
            return millis - Jdk8Methods.floorMod(millis, this.tickNanos / C0649C.MICROS_PER_SECOND);
        }

        public Instant instant() {
            if (this.tickNanos % C0649C.MICROS_PER_SECOND == 0) {
                long millis = this.baseClock.millis();
                return Instant.ofEpochMilli(millis - Jdk8Methods.floorMod(millis, this.tickNanos / C0649C.MICROS_PER_SECOND));
            }
            Instant instant = this.baseClock.instant();
            return instant.minusNanos(Jdk8Methods.floorMod((long) instant.getNano(), this.tickNanos));
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof TickClock)) {
                return false;
            }
            TickClock tickClock = (TickClock) obj;
            if (this.baseClock.equals(tickClock.baseClock) && this.tickNanos == tickClock.tickNanos) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.baseClock.hashCode() ^ ((int) (this.tickNanos ^ (this.tickNanos >>> 32)));
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TickClock[");
            stringBuilder.append(this.baseClock);
            stringBuilder.append(",");
            stringBuilder.append(Duration.ofNanos(this.tickNanos));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public abstract ZoneId getZone();

    public abstract Instant instant();

    public abstract Clock withZone(ZoneId zoneId);

    public static Clock systemUTC() {
        return new SystemClock(ZoneOffset.UTC);
    }

    public static Clock systemDefaultZone() {
        return new SystemClock(ZoneId.systemDefault());
    }

    public static Clock system(ZoneId zoneId) {
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return new SystemClock(zoneId);
    }

    public static Clock tickSeconds(ZoneId zoneId) {
        return new TickClock(system(zoneId), C0649C.NANOS_PER_SECOND);
    }

    public static Clock tickMinutes(ZoneId zoneId) {
        return new TickClock(system(zoneId), 60000000000L);
    }

    public static Clock tick(Clock clock, Duration duration) {
        Jdk8Methods.requireNonNull(clock, "baseClock");
        Jdk8Methods.requireNonNull(duration, "tickDuration");
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Tick duration must not be negative");
        }
        long toNanos = duration.toNanos();
        if (toNanos % C0649C.MICROS_PER_SECOND != 0) {
            if (C0649C.NANOS_PER_SECOND % toNanos != 0) {
                throw new IllegalArgumentException("Invalid tick duration");
            }
        }
        if (toNanos <= 1) {
            return clock;
        }
        return new TickClock(clock, toNanos);
    }

    public static Clock fixed(Instant instant, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(instant, "fixedInstant");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return new FixedClock(instant, zoneId);
    }

    public static Clock offset(Clock clock, Duration duration) {
        Jdk8Methods.requireNonNull(clock, "baseClock");
        Jdk8Methods.requireNonNull(duration, "offsetDuration");
        if (duration.equals(Duration.ZERO)) {
            return clock;
        }
        return new OffsetClock(clock, duration);
    }

    protected Clock() {
    }

    public long millis() {
        return instant().toEpochMilli();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
