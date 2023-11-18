package org.threeten.bp.zone;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.jdk8.Jdk8Methods;

public final class ZoneOffsetTransition implements Comparable<ZoneOffsetTransition>, Serializable {
    private static final long serialVersionUID = -6946044323557704546L;
    private final ZoneOffset offsetAfter;
    private final ZoneOffset offsetBefore;
    private final LocalDateTime transition;

    public static ZoneOffsetTransition of(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneOffset zoneOffset2) {
        Jdk8Methods.requireNonNull(localDateTime, "transition");
        Jdk8Methods.requireNonNull(zoneOffset, "offsetBefore");
        Jdk8Methods.requireNonNull(zoneOffset2, "offsetAfter");
        if (zoneOffset.equals(zoneOffset2)) {
            throw new IllegalArgumentException("Offsets must not be equal");
        } else if (localDateTime.getNano() == 0) {
            return new ZoneOffsetTransition(localDateTime, zoneOffset, zoneOffset2);
        } else {
            throw new IllegalArgumentException("Nano-of-second must be zero");
        }
    }

    ZoneOffsetTransition(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneOffset zoneOffset2) {
        this.transition = localDateTime;
        this.offsetBefore = zoneOffset;
        this.offsetAfter = zoneOffset2;
    }

    ZoneOffsetTransition(long j, ZoneOffset zoneOffset, ZoneOffset zoneOffset2) {
        this.transition = LocalDateTime.ofEpochSecond(j, 0, zoneOffset);
        this.offsetBefore = zoneOffset;
        this.offsetAfter = zoneOffset2;
    }

    private Object writeReplace() {
        return new Ser((byte) 2, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        Ser.writeEpochSec(toEpochSecond(), dataOutput);
        Ser.writeOffset(this.offsetBefore, dataOutput);
        Ser.writeOffset(this.offsetAfter, dataOutput);
    }

    static ZoneOffsetTransition readExternal(DataInput dataInput) throws IOException {
        long readEpochSec = Ser.readEpochSec(dataInput);
        ZoneOffset readOffset = Ser.readOffset(dataInput);
        ZoneOffset readOffset2 = Ser.readOffset(dataInput);
        if (!readOffset.equals(readOffset2)) {
            return new ZoneOffsetTransition(readEpochSec, readOffset, readOffset2);
        }
        throw new IllegalArgumentException("Offsets must not be equal");
    }

    public Instant getInstant() {
        return this.transition.toInstant(this.offsetBefore);
    }

    public long toEpochSecond() {
        return this.transition.toEpochSecond(this.offsetBefore);
    }

    public LocalDateTime getDateTimeBefore() {
        return this.transition;
    }

    public LocalDateTime getDateTimeAfter() {
        return this.transition.plusSeconds((long) getDurationSeconds());
    }

    public ZoneOffset getOffsetBefore() {
        return this.offsetBefore;
    }

    public ZoneOffset getOffsetAfter() {
        return this.offsetAfter;
    }

    public Duration getDuration() {
        return Duration.ofSeconds((long) getDurationSeconds());
    }

    private int getDurationSeconds() {
        return getOffsetAfter().getTotalSeconds() - getOffsetBefore().getTotalSeconds();
    }

    public boolean isGap() {
        return getOffsetAfter().getTotalSeconds() > getOffsetBefore().getTotalSeconds();
    }

    public boolean isOverlap() {
        return getOffsetAfter().getTotalSeconds() < getOffsetBefore().getTotalSeconds();
    }

    public boolean isValidOffset(ZoneOffset zoneOffset) {
        if (isGap()) {
            return false;
        }
        return getOffsetBefore().equals(zoneOffset) || getOffsetAfter().equals(zoneOffset) != null;
    }

    List<ZoneOffset> getValidOffsets() {
        if (isGap()) {
            return Collections.emptyList();
        }
        return Arrays.asList(new ZoneOffset[]{getOffsetBefore(), getOffsetAfter()});
    }

    public int compareTo(ZoneOffsetTransition zoneOffsetTransition) {
        return getInstant().compareTo(zoneOffsetTransition.getInstant());
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ZoneOffsetTransition)) {
            return false;
        }
        ZoneOffsetTransition zoneOffsetTransition = (ZoneOffsetTransition) obj;
        if (!this.transition.equals(zoneOffsetTransition.transition) || !this.offsetBefore.equals(zoneOffsetTransition.offsetBefore) || this.offsetAfter.equals(zoneOffsetTransition.offsetAfter) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (this.transition.hashCode() ^ this.offsetBefore.hashCode()) ^ Integer.rotateLeft(this.offsetAfter.hashCode(), 16);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Transition[");
        stringBuilder.append(isGap() ? "Gap" : "Overlap");
        stringBuilder.append(" at ");
        stringBuilder.append(this.transition);
        stringBuilder.append(this.offsetBefore);
        stringBuilder.append(" to ");
        stringBuilder.append(this.offsetAfter);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
