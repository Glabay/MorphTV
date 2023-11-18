package org.threeten.bp.chrono;

import com.google.common.base.Ascii;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.zone.ZoneOffsetTransition;
import org.threeten.bp.zone.ZoneRules;

final class ChronoZonedDateTimeImpl<D extends ChronoLocalDate> extends ChronoZonedDateTime<D> implements Serializable {
    private static final long serialVersionUID = -5261813987200935591L;
    private final ChronoLocalDateTimeImpl<D> dateTime;
    private final ZoneOffset offset;
    private final ZoneId zone;

    static <R extends ChronoLocalDate> ChronoZonedDateTime<R> ofBest(ChronoLocalDateTimeImpl<R> chronoLocalDateTimeImpl, ZoneId zoneId, ZoneOffset zoneOffset) {
        Jdk8Methods.requireNonNull(chronoLocalDateTimeImpl, "localDateTime");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new ChronoZonedDateTimeImpl(chronoLocalDateTimeImpl, (ZoneOffset) zoneId, zoneId);
        }
        ZoneRules rules = zoneId.getRules();
        LocalDateTime from = LocalDateTime.from(chronoLocalDateTimeImpl);
        List validOffsets = rules.getValidOffsets(from);
        if (validOffsets.size() == 1) {
            zoneOffset = (ZoneOffset) validOffsets.get(0);
        } else if (validOffsets.size() == 0) {
            zoneOffset = rules.getTransition(from);
            chronoLocalDateTimeImpl = chronoLocalDateTimeImpl.plusSeconds(zoneOffset.getDuration().getSeconds());
            zoneOffset = zoneOffset.getOffsetAfter();
        } else if (zoneOffset == null || !validOffsets.contains(zoneOffset)) {
            zoneOffset = (ZoneOffset) validOffsets.get(0);
        }
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        return new ChronoZonedDateTimeImpl(chronoLocalDateTimeImpl, zoneOffset, zoneId);
    }

    static <R extends ChronoLocalDate> ChronoZonedDateTimeImpl<R> ofInstant(Chronology chronology, Instant instant, ZoneId zoneId) {
        ZoneOffset offset = zoneId.getRules().getOffset(instant);
        Jdk8Methods.requireNonNull(offset, "offset");
        return new ChronoZonedDateTimeImpl((ChronoLocalDateTimeImpl) chronology.localDateTime(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), offset)), offset, zoneId);
    }

    private ChronoZonedDateTimeImpl<D> create(Instant instant, ZoneId zoneId) {
        return ofInstant(toLocalDate().getChronology(), instant, zoneId);
    }

    private ChronoZonedDateTimeImpl(ChronoLocalDateTimeImpl<D> chronoLocalDateTimeImpl, ZoneOffset zoneOffset, ZoneId zoneId) {
        this.dateTime = (ChronoLocalDateTimeImpl) Jdk8Methods.requireNonNull(chronoLocalDateTimeImpl, "dateTime");
        this.offset = (ZoneOffset) Jdk8Methods.requireNonNull(zoneOffset, "offset");
        this.zone = (ZoneId) Jdk8Methods.requireNonNull(zoneId, "zone");
    }

    public boolean isSupported(TemporalUnit temporalUnit) {
        boolean z = true;
        if (temporalUnit instanceof ChronoUnit) {
            if (!temporalUnit.isDateBased()) {
                if (temporalUnit.isTimeBased() == null) {
                    z = false;
                }
            }
            return z;
        }
        if (temporalUnit == null || temporalUnit.isSupportedBy(this) == null) {
            z = false;
        }
        return z;
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public ChronoZonedDateTime<D> withEarlierOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(LocalDateTime.from(this));
        if (transition != null && transition.isOverlap()) {
            ZoneOffset offsetBefore = transition.getOffsetBefore();
            if (!offsetBefore.equals(this.offset)) {
                return new ChronoZonedDateTimeImpl(this.dateTime, offsetBefore, this.zone);
            }
        }
        return this;
    }

    public ChronoZonedDateTime<D> withLaterOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(LocalDateTime.from(this));
        if (transition != null) {
            ZoneOffset offsetAfter = transition.getOffsetAfter();
            if (!offsetAfter.equals(getOffset())) {
                return new ChronoZonedDateTimeImpl(this.dateTime, offsetAfter, this.zone);
            }
        }
        return this;
    }

    public ChronoLocalDateTime<D> toLocalDateTime() {
        return this.dateTime;
    }

    public ZoneId getZone() {
        return this.zone;
    }

    public ChronoZonedDateTime<D> withZoneSameLocal(ZoneId zoneId) {
        return ofBest(this.dateTime, zoneId, this.offset);
    }

    public ChronoZonedDateTime<D> withZoneSameInstant(ZoneId zoneId) {
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : create(this.dateTime.toInstant(this.offset), zoneId);
    }

    public boolean isSupported(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            if (temporalField == null || temporalField.isSupportedBy(this) == null) {
                return null;
            }
        }
        return true;
    }

    public ChronoZonedDateTime<D> with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return toLocalDate().getChronology().ensureChronoZonedDateTime(temporalField.adjustInto(this, j));
        }
        ChronoField chronoField = (ChronoField) temporalField;
        switch (chronoField) {
            case INSTANT_SECONDS:
                return plus(j - toEpochSecond(), ChronoUnit.SECONDS);
            case OFFSET_SECONDS:
                return create(this.dateTime.toInstant(ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(j))), this.zone);
            default:
                return ofBest(this.dateTime.with(temporalField, j), this.zone, this.offset);
        }
    }

    public ChronoZonedDateTime<D> plus(long j, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return with((TemporalAdjuster) this.dateTime.plus(j, temporalUnit));
        }
        return toLocalDate().getChronology().ensureChronoZonedDateTime(temporalUnit.addTo(this, j));
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = toLocalDate().getChronology().zonedDateTime(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        return this.dateTime.until(temporal.withZoneSameInstant(this.offset).toLocalDateTime(), temporalUnit);
    }

    private Object writeReplace() {
        return new Ser(Ascii.CR, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.dateTime);
        objectOutput.writeObject(this.offset);
        objectOutput.writeObject(this.zone);
    }

    static ChronoZonedDateTime<?> readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        ChronoLocalDateTime chronoLocalDateTime = (ChronoLocalDateTime) objectInput.readObject();
        ZoneOffset zoneOffset = (ZoneOffset) objectInput.readObject();
        return chronoLocalDateTime.atZone(zoneOffset).withZoneSameLocal((ZoneId) objectInput.readObject());
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChronoZonedDateTime)) {
            return false;
        }
        if (compareTo((ChronoZonedDateTime) obj) != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (toLocalDateTime().hashCode() ^ getOffset().hashCode()) ^ Integer.rotateLeft(getZone().hashCode(), 3);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toLocalDateTime().toString());
        stringBuilder.append(getOffset().toString());
        String stringBuilder2 = stringBuilder.toString();
        if (getOffset() == getZone()) {
            return stringBuilder2;
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append('[');
        stringBuilder3.append(getZone().toString());
        stringBuilder3.append(']');
        return stringBuilder3.toString();
    }
}
