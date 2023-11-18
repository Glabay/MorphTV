package org.threeten.bp.chrono;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;

final class ChronoPeriodImpl extends ChronoPeriod implements Serializable {
    private static final long serialVersionUID = 275618735781L;
    private final Chronology chronology;
    private final int days;
    private final int months;
    private final int years;

    public ChronoPeriodImpl(Chronology chronology, int i, int i2, int i3) {
        this.chronology = chronology;
        this.years = i;
        this.months = i2;
        this.days = i3;
    }

    public long get(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.YEARS) {
            return (long) this.years;
        }
        if (temporalUnit == ChronoUnit.MONTHS) {
            return (long) this.months;
        }
        if (temporalUnit == ChronoUnit.DAYS) {
            return (long) this.days;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported unit: ");
        stringBuilder.append(temporalUnit);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public List<TemporalUnit> getUnits() {
        return Collections.unmodifiableList(Arrays.asList(new TemporalUnit[]{ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS}));
    }

    public Chronology getChronology() {
        return this.chronology;
    }

    public ChronoPeriod plus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof ChronoPeriodImpl) {
            ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl) temporalAmount;
            if (chronoPeriodImpl.getChronology().equals(getChronology())) {
                return new ChronoPeriodImpl(this.chronology, Jdk8Methods.safeAdd(this.years, chronoPeriodImpl.years), Jdk8Methods.safeAdd(this.months, chronoPeriodImpl.months), Jdk8Methods.safeAdd(this.days, chronoPeriodImpl.days));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to add amount: ");
        stringBuilder.append(temporalAmount);
        throw new DateTimeException(stringBuilder.toString());
    }

    public ChronoPeriod minus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof ChronoPeriodImpl) {
            ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl) temporalAmount;
            if (chronoPeriodImpl.getChronology().equals(getChronology())) {
                return new ChronoPeriodImpl(this.chronology, Jdk8Methods.safeSubtract(this.years, chronoPeriodImpl.years), Jdk8Methods.safeSubtract(this.months, chronoPeriodImpl.months), Jdk8Methods.safeSubtract(this.days, chronoPeriodImpl.days));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to subtract amount: ");
        stringBuilder.append(temporalAmount);
        throw new DateTimeException(stringBuilder.toString());
    }

    public ChronoPeriod multipliedBy(int i) {
        return new ChronoPeriodImpl(this.chronology, Jdk8Methods.safeMultiply(this.years, i), Jdk8Methods.safeMultiply(this.months, i), Jdk8Methods.safeMultiply(this.days, i));
    }

    public ChronoPeriod normalized() {
        if (!this.chronology.range(ChronoField.MONTH_OF_YEAR).isFixed()) {
            return this;
        }
        long maximum = (this.chronology.range(ChronoField.MONTH_OF_YEAR).getMaximum() - this.chronology.range(ChronoField.MONTH_OF_YEAR).getMinimum()) + 1;
        long j = (((long) this.years) * maximum) + ((long) this.months);
        return new ChronoPeriodImpl(this.chronology, Jdk8Methods.safeToInt(j / maximum), Jdk8Methods.safeToInt(j % maximum), this.days);
    }

    public Temporal addTo(Temporal temporal) {
        Jdk8Methods.requireNonNull(temporal, "temporal");
        Chronology chronology = (Chronology) temporal.query(TemporalQueries.chronology());
        if (chronology == null || this.chronology.equals(chronology)) {
            if (this.years != 0) {
                temporal = temporal.plus((long) this.years, ChronoUnit.YEARS);
            }
            if (this.months != 0) {
                temporal = temporal.plus((long) this.months, ChronoUnit.MONTHS);
            }
            return this.days != 0 ? temporal.plus((long) this.days, ChronoUnit.DAYS) : temporal;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid chronology, required: ");
            stringBuilder.append(this.chronology.getId());
            stringBuilder.append(", but was: ");
            stringBuilder.append(chronology.getId());
            throw new DateTimeException(stringBuilder.toString());
        }
    }

    public Temporal subtractFrom(Temporal temporal) {
        Jdk8Methods.requireNonNull(temporal, "temporal");
        Chronology chronology = (Chronology) temporal.query(TemporalQueries.chronology());
        if (chronology == null || this.chronology.equals(chronology)) {
            if (this.years != 0) {
                temporal = temporal.minus((long) this.years, ChronoUnit.YEARS);
            }
            if (this.months != 0) {
                temporal = temporal.minus((long) this.months, ChronoUnit.MONTHS);
            }
            return this.days != 0 ? temporal.minus((long) this.days, ChronoUnit.DAYS) : temporal;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid chronology, required: ");
            stringBuilder.append(this.chronology.getId());
            stringBuilder.append(", but was: ");
            stringBuilder.append(chronology.getId());
            throw new DateTimeException(stringBuilder.toString());
        }
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChronoPeriodImpl)) {
            return false;
        }
        ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl) obj;
        if (this.years != chronoPeriodImpl.years || this.months != chronoPeriodImpl.months || this.days != chronoPeriodImpl.days || this.chronology.equals(chronoPeriodImpl.chronology) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((this.chronology.hashCode() + Integer.rotateLeft(this.years, 16)) + Integer.rotateLeft(this.months, 8)) + this.days;
    }

    public String toString() {
        if (isZero()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.chronology);
            stringBuilder.append(" P0D");
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.chronology);
        stringBuilder.append(' ');
        stringBuilder.append('P');
        if (this.years != 0) {
            stringBuilder.append(this.years);
            stringBuilder.append('Y');
        }
        if (this.months != 0) {
            stringBuilder.append(this.months);
            stringBuilder.append('M');
        }
        if (this.days != 0) {
            stringBuilder.append(this.days);
            stringBuilder.append('D');
        }
        return stringBuilder.toString();
    }
}
