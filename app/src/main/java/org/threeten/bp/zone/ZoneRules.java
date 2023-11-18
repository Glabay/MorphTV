package org.threeten.bp.zone;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.jdk8.Jdk8Methods;

public abstract class ZoneRules {

    static final class Fixed extends ZoneRules implements Serializable {
        private static final long serialVersionUID = -8733721350312276297L;
        private final ZoneOffset offset;

        public ZoneOffsetTransition getTransition(LocalDateTime localDateTime) {
            return null;
        }

        public boolean isDaylightSavings(Instant instant) {
            return false;
        }

        public boolean isFixedOffset() {
            return true;
        }

        public ZoneOffsetTransition nextTransition(Instant instant) {
            return null;
        }

        public ZoneOffsetTransition previousTransition(Instant instant) {
            return null;
        }

        Fixed(ZoneOffset zoneOffset) {
            this.offset = zoneOffset;
        }

        public ZoneOffset getOffset(Instant instant) {
            return this.offset;
        }

        public ZoneOffset getOffset(LocalDateTime localDateTime) {
            return this.offset;
        }

        public List<ZoneOffset> getValidOffsets(LocalDateTime localDateTime) {
            return Collections.singletonList(this.offset);
        }

        public boolean isValidOffset(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
            return this.offset.equals(zoneOffset);
        }

        public ZoneOffset getStandardOffset(Instant instant) {
            return this.offset;
        }

        public Duration getDaylightSavings(Instant instant) {
            return Duration.ZERO;
        }

        public List<ZoneOffsetTransition> getTransitions() {
            return Collections.emptyList();
        }

        public List<ZoneOffsetTransitionRule> getTransitionRules() {
            return Collections.emptyList();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj instanceof Fixed) {
                return this.offset.equals(((Fixed) obj).offset);
            }
            if (!(obj instanceof StandardZoneRules)) {
                return false;
            }
            StandardZoneRules standardZoneRules = (StandardZoneRules) obj;
            if (!standardZoneRules.isFixedOffset() || this.offset.equals(standardZoneRules.getOffset(Instant.EPOCH)) == null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return ((((this.offset.hashCode() + 31) ^ 1) ^ 1) ^ (this.offset.hashCode() + 31)) ^ 1;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FixedRules:");
            stringBuilder.append(this.offset);
            return stringBuilder.toString();
        }
    }

    public abstract boolean equals(Object obj);

    public abstract Duration getDaylightSavings(Instant instant);

    public abstract ZoneOffset getOffset(Instant instant);

    public abstract ZoneOffset getOffset(LocalDateTime localDateTime);

    public abstract ZoneOffset getStandardOffset(Instant instant);

    public abstract ZoneOffsetTransition getTransition(LocalDateTime localDateTime);

    public abstract List<ZoneOffsetTransitionRule> getTransitionRules();

    public abstract List<ZoneOffsetTransition> getTransitions();

    public abstract List<ZoneOffset> getValidOffsets(LocalDateTime localDateTime);

    public abstract int hashCode();

    public abstract boolean isDaylightSavings(Instant instant);

    public abstract boolean isFixedOffset();

    public abstract boolean isValidOffset(LocalDateTime localDateTime, ZoneOffset zoneOffset);

    public abstract ZoneOffsetTransition nextTransition(Instant instant);

    public abstract ZoneOffsetTransition previousTransition(Instant instant);

    public static ZoneRules of(ZoneOffset zoneOffset, ZoneOffset zoneOffset2, List<ZoneOffsetTransition> list, List<ZoneOffsetTransition> list2, List<ZoneOffsetTransitionRule> list3) {
        Jdk8Methods.requireNonNull(zoneOffset, "baseStandardOffset");
        Jdk8Methods.requireNonNull(zoneOffset2, "baseWallOffset");
        Jdk8Methods.requireNonNull(list, "standardOffsetTransitionList");
        Jdk8Methods.requireNonNull(list2, "transitionList");
        Jdk8Methods.requireNonNull(list3, "lastRules");
        return new StandardZoneRules(zoneOffset, zoneOffset2, (List) list, (List) list2, (List) list3);
    }

    public static ZoneRules of(ZoneOffset zoneOffset) {
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        return new Fixed(zoneOffset);
    }

    ZoneRules() {
    }
}
