package org.threeten.bp.zone;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.TemporalAdjusters;

public final class ZoneOffsetTransitionRule implements Serializable {
    private static final long serialVersionUID = 6889046316657758795L;
    private final byte dom;
    private final DayOfWeek dow;
    private final Month month;
    private final ZoneOffset offsetAfter;
    private final ZoneOffset offsetBefore;
    private final ZoneOffset standardOffset;
    private final LocalTime time;
    private final TimeDefinition timeDefinition;
    private final boolean timeEndOfDay;

    public enum TimeDefinition {
        UTC,
        WALL,
        STANDARD;

        public LocalDateTime createDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneOffset zoneOffset2) {
            switch (this) {
                case UTC:
                    return localDateTime.plusSeconds((long) (zoneOffset2.getTotalSeconds() - ZoneOffset.UTC.getTotalSeconds()));
                case STANDARD:
                    return localDateTime.plusSeconds((long) (zoneOffset2.getTotalSeconds() - zoneOffset.getTotalSeconds()));
                default:
                    return localDateTime;
            }
        }
    }

    public static ZoneOffsetTransitionRule of(Month month, int i, DayOfWeek dayOfWeek, LocalTime localTime, boolean z, TimeDefinition timeDefinition, ZoneOffset zoneOffset, ZoneOffset zoneOffset2, ZoneOffset zoneOffset3) {
        int i2 = i;
        LocalTime localTime2 = localTime;
        Month month2 = month;
        Jdk8Methods.requireNonNull(month2, "month");
        Jdk8Methods.requireNonNull(localTime2, "time");
        TimeDefinition timeDefinition2 = timeDefinition;
        Jdk8Methods.requireNonNull(timeDefinition2, "timeDefnition");
        ZoneOffset zoneOffset4 = zoneOffset;
        Jdk8Methods.requireNonNull(zoneOffset4, "standardOffset");
        ZoneOffset zoneOffset5 = zoneOffset2;
        Jdk8Methods.requireNonNull(zoneOffset5, "offsetBefore");
        ZoneOffset zoneOffset6 = zoneOffset3;
        Jdk8Methods.requireNonNull(zoneOffset6, "offsetAfter");
        if (i2 >= -28 && i2 <= 31) {
            if (i2 != 0) {
                if (!z || localTime2.equals(LocalTime.MIDNIGHT)) {
                    return new ZoneOffsetTransitionRule(month2, i2, dayOfWeek, localTime2, z, timeDefinition2, zoneOffset4, zoneOffset5, zoneOffset6);
                }
                throw new IllegalArgumentException("Time must be midnight when end of day flag is true");
            }
        }
        throw new IllegalArgumentException("Day of month indicator must be between -28 and 31 inclusive excluding zero");
    }

    ZoneOffsetTransitionRule(Month month, int i, DayOfWeek dayOfWeek, LocalTime localTime, boolean z, TimeDefinition timeDefinition, ZoneOffset zoneOffset, ZoneOffset zoneOffset2, ZoneOffset zoneOffset3) {
        this.month = month;
        this.dom = (byte) i;
        this.dow = dayOfWeek;
        this.time = localTime;
        this.timeEndOfDay = z;
        this.timeDefinition = timeDefinition;
        this.standardOffset = zoneOffset;
        this.offsetBefore = zoneOffset2;
        this.offsetAfter = zoneOffset3;
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        int toSecondOfDay = this.timeEndOfDay ? 86400 : this.time.toSecondOfDay();
        int totalSeconds = this.standardOffset.getTotalSeconds();
        int totalSeconds2 = this.offsetBefore.getTotalSeconds() - totalSeconds;
        int totalSeconds3 = this.offsetAfter.getTotalSeconds() - totalSeconds;
        int hour = toSecondOfDay % 3600 == 0 ? this.timeEndOfDay ? 24 : this.time.getHour() : 31;
        int i = totalSeconds % 900 == 0 ? (totalSeconds / 900) + 128 : 255;
        if (!(totalSeconds2 == 0 || totalSeconds2 == 1800)) {
            if (totalSeconds2 != 3600) {
                totalSeconds2 = 3;
                if (!(totalSeconds3 == 0 || totalSeconds3 == 1800)) {
                    if (totalSeconds3 == 3600) {
                        totalSeconds3 = 3;
                        dataOutput.writeInt((((((((this.month.getValue() << 28) + ((this.dom + 32) << 22)) + ((this.dow != null ? 0 : this.dow.getValue()) << 19)) + (hour << 14)) + (this.timeDefinition.ordinal() << 12)) + (i << 4)) + (totalSeconds2 << 2)) + totalSeconds3);
                        if (hour == 31) {
                            dataOutput.writeInt(toSecondOfDay);
                        }
                        if (i == 255) {
                            dataOutput.writeInt(totalSeconds);
                        }
                        if (totalSeconds2 == 3) {
                            dataOutput.writeInt(this.offsetBefore.getTotalSeconds());
                        }
                        if (totalSeconds3 != 3) {
                            dataOutput.writeInt(this.offsetAfter.getTotalSeconds());
                        }
                    }
                }
                totalSeconds3 /= 1800;
                if (this.dow != null) {
                }
                dataOutput.writeInt((((((((this.month.getValue() << 28) + ((this.dom + 32) << 22)) + ((this.dow != null ? 0 : this.dow.getValue()) << 19)) + (hour << 14)) + (this.timeDefinition.ordinal() << 12)) + (i << 4)) + (totalSeconds2 << 2)) + totalSeconds3);
                if (hour == 31) {
                    dataOutput.writeInt(toSecondOfDay);
                }
                if (i == 255) {
                    dataOutput.writeInt(totalSeconds);
                }
                if (totalSeconds2 == 3) {
                    dataOutput.writeInt(this.offsetBefore.getTotalSeconds());
                }
                if (totalSeconds3 != 3) {
                    dataOutput.writeInt(this.offsetAfter.getTotalSeconds());
                }
            }
        }
        totalSeconds2 /= 1800;
        if (totalSeconds3 == 3600) {
            totalSeconds3 = 3;
            if (this.dow != null) {
            }
            dataOutput.writeInt((((((((this.month.getValue() << 28) + ((this.dom + 32) << 22)) + ((this.dow != null ? 0 : this.dow.getValue()) << 19)) + (hour << 14)) + (this.timeDefinition.ordinal() << 12)) + (i << 4)) + (totalSeconds2 << 2)) + totalSeconds3);
            if (hour == 31) {
                dataOutput.writeInt(toSecondOfDay);
            }
            if (i == 255) {
                dataOutput.writeInt(totalSeconds);
            }
            if (totalSeconds2 == 3) {
                dataOutput.writeInt(this.offsetBefore.getTotalSeconds());
            }
            if (totalSeconds3 != 3) {
                dataOutput.writeInt(this.offsetAfter.getTotalSeconds());
            }
        }
        totalSeconds3 /= 1800;
        if (this.dow != null) {
        }
        dataOutput.writeInt((((((((this.month.getValue() << 28) + ((this.dom + 32) << 22)) + ((this.dow != null ? 0 : this.dow.getValue()) << 19)) + (hour << 14)) + (this.timeDefinition.ordinal() << 12)) + (i << 4)) + (totalSeconds2 << 2)) + totalSeconds3);
        if (hour == 31) {
            dataOutput.writeInt(toSecondOfDay);
        }
        if (i == 255) {
            dataOutput.writeInt(totalSeconds);
        }
        if (totalSeconds2 == 3) {
            dataOutput.writeInt(this.offsetBefore.getTotalSeconds());
        }
        if (totalSeconds3 != 3) {
            dataOutput.writeInt(this.offsetAfter.getTotalSeconds());
        }
    }

    static ZoneOffsetTransitionRule readExternal(DataInput dataInput) throws IOException {
        DayOfWeek dayOfWeek;
        int readInt = dataInput.readInt();
        Month of = Month.of(readInt >>> 28);
        int i = ((264241152 & readInt) >>> 22) - 32;
        int i2 = (3670016 & readInt) >>> 19;
        if (i2 == 0) {
            dayOfWeek = null;
        } else {
            dayOfWeek = DayOfWeek.of(i2);
        }
        DayOfWeek dayOfWeek2 = dayOfWeek;
        i2 = (507904 & readInt) >>> 14;
        TimeDefinition timeDefinition = TimeDefinition.values()[(readInt & 12288) >>> 12];
        int i3 = (readInt & 4080) >>> 4;
        int i4 = (readInt & 12) >>> 2;
        readInt &= 3;
        LocalTime ofSecondOfDay = i2 == 31 ? LocalTime.ofSecondOfDay((long) dataInput.readInt()) : LocalTime.of(i2 % 24, 0);
        ZoneOffset ofTotalSeconds = ZoneOffset.ofTotalSeconds(i3 == 255 ? dataInput.readInt() : (i3 - 128) * 900);
        return of(of, i, dayOfWeek2, ofSecondOfDay, i2 == 24, timeDefinition, ofTotalSeconds, ZoneOffset.ofTotalSeconds(i4 == 3 ? dataInput.readInt() : ofTotalSeconds.getTotalSeconds() + (i4 * 1800)), ZoneOffset.ofTotalSeconds(readInt == 3 ? dataInput.readInt() : ofTotalSeconds.getTotalSeconds() + (readInt * 1800)));
    }

    public Month getMonth() {
        return this.month;
    }

    public int getDayOfMonthIndicator() {
        return this.dom;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dow;
    }

    public LocalTime getLocalTime() {
        return this.time;
    }

    public boolean isMidnightEndOfDay() {
        return this.timeEndOfDay;
    }

    public TimeDefinition getTimeDefinition() {
        return this.timeDefinition;
    }

    public ZoneOffset getStandardOffset() {
        return this.standardOffset;
    }

    public ZoneOffset getOffsetBefore() {
        return this.offsetBefore;
    }

    public ZoneOffset getOffsetAfter() {
        return this.offsetAfter;
    }

    public ZoneOffsetTransition createTransition(int i) {
        if (this.dom < (byte) 0) {
            i = LocalDate.of(i, this.month, (this.month.length(IsoChronology.INSTANCE.isLeapYear((long) i)) + 1) + this.dom);
            if (this.dow != null) {
                i = i.with(TemporalAdjusters.previousOrSame(this.dow));
            }
        } else {
            i = LocalDate.of(i, this.month, this.dom);
            if (this.dow != null) {
                i = i.with(TemporalAdjusters.nextOrSame(this.dow));
            }
        }
        if (this.timeEndOfDay) {
            i = i.plusDays(1);
        }
        return new ZoneOffsetTransition(this.timeDefinition.createDateTime(LocalDateTime.of(i, this.time), this.standardOffset, this.offsetBefore), this.offsetBefore, this.offsetAfter);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ZoneOffsetTransitionRule)) {
            return false;
        }
        ZoneOffsetTransitionRule zoneOffsetTransitionRule = (ZoneOffsetTransitionRule) obj;
        if (this.month != zoneOffsetTransitionRule.month || this.dom != zoneOffsetTransitionRule.dom || this.dow != zoneOffsetTransitionRule.dow || this.timeDefinition != zoneOffsetTransitionRule.timeDefinition || !this.time.equals(zoneOffsetTransitionRule.time) || this.timeEndOfDay != zoneOffsetTransitionRule.timeEndOfDay || !this.standardOffset.equals(zoneOffsetTransitionRule.standardOffset) || !this.offsetBefore.equals(zoneOffsetTransitionRule.offsetBefore) || this.offsetAfter.equals(zoneOffsetTransitionRule.offsetAfter) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((((((((this.time.toSecondOfDay() + this.timeEndOfDay) << 15) + (this.month.ordinal() << 11)) + ((this.dom + 32) << 5)) + ((this.dow == null ? 7 : this.dow.ordinal()) << 2)) + this.timeDefinition.ordinal()) ^ this.standardOffset.hashCode()) ^ this.offsetBefore.hashCode()) ^ this.offsetAfter.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TransitionRule[");
        stringBuilder.append(this.offsetBefore.compareTo(this.offsetAfter) > 0 ? "Gap " : "Overlap ");
        stringBuilder.append(this.offsetBefore);
        stringBuilder.append(" to ");
        stringBuilder.append(this.offsetAfter);
        stringBuilder.append(", ");
        if (this.dow == null) {
            stringBuilder.append(this.month.name());
            stringBuilder.append(' ');
            stringBuilder.append(this.dom);
        } else if (this.dom == (byte) -1) {
            stringBuilder.append(this.dow.name());
            stringBuilder.append(" on or before last day of ");
            stringBuilder.append(this.month.name());
        } else if (this.dom < (byte) 0) {
            stringBuilder.append(this.dow.name());
            stringBuilder.append(" on or before last day minus ");
            stringBuilder.append((-this.dom) - 1);
            stringBuilder.append(" of ");
            stringBuilder.append(this.month.name());
        } else {
            stringBuilder.append(this.dow.name());
            stringBuilder.append(" on or after ");
            stringBuilder.append(this.month.name());
            stringBuilder.append(' ');
            stringBuilder.append(this.dom);
        }
        stringBuilder.append(" at ");
        stringBuilder.append(this.timeEndOfDay ? "24:00" : this.time.toString());
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(this.timeDefinition);
        stringBuilder.append(", standard offset ");
        stringBuilder.append(this.standardOffset);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
