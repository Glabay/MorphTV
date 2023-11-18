package org.threeten.bp.format;

import com.google.android.exoplayer2.C0649C;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.ChronoLocalDateTime;
import org.threeten.bp.chrono.ChronoZonedDateTime;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.jdk8.DefaultInterfaceTemporalAccessor;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;

final class DateTimeBuilder extends DefaultInterfaceTemporalAccessor implements TemporalAccessor, Cloneable {
    Chronology chrono;
    ChronoLocalDate date;
    Period excessDays;
    final Map<TemporalField, Long> fieldValues = new HashMap();
    boolean leapSecond;
    LocalTime time;
    ZoneId zone;

    public DateTimeBuilder(TemporalField temporalField, long j) {
        addFieldValue(temporalField, j);
    }

    private Long getFieldValue0(TemporalField temporalField) {
        return (Long) this.fieldValues.get(temporalField);
    }

    DateTimeBuilder addFieldValue(TemporalField temporalField, long j) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        Long fieldValue0 = getFieldValue0(temporalField);
        if (fieldValue0 == null || fieldValue0.longValue() == j) {
            return putFieldValue0(temporalField, j);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Conflict found: ");
        stringBuilder.append(temporalField);
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(fieldValue0);
        stringBuilder.append(" differs from ");
        stringBuilder.append(temporalField);
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(j);
        stringBuilder.append(": ");
        stringBuilder.append(this);
        throw new DateTimeException(stringBuilder.toString());
    }

    private DateTimeBuilder putFieldValue0(TemporalField temporalField, long j) {
        this.fieldValues.put(temporalField, Long.valueOf(j));
        return this;
    }

    void addObject(ChronoLocalDate chronoLocalDate) {
        this.date = chronoLocalDate;
    }

    void addObject(LocalTime localTime) {
        this.time = localTime;
    }

    public DateTimeBuilder resolve(ResolverStyle resolverStyle, Set<TemporalField> set) {
        if (set != null) {
            this.fieldValues.keySet().retainAll(set);
        }
        mergeInstantFields();
        mergeDate(resolverStyle);
        mergeTime(resolverStyle);
        if (resolveFields(resolverStyle) != null) {
            mergeInstantFields();
            mergeDate(resolverStyle);
            mergeTime(resolverStyle);
        }
        resolveTimeInferZeroes(resolverStyle);
        crossCheck();
        if (!(this.excessDays == null || this.excessDays.isZero() != null || this.date == null || this.time == null)) {
            this.date = this.date.plus(this.excessDays);
            this.excessDays = Period.ZERO;
        }
        resolveFractional();
        resolveInstant();
        return this;
    }

    private boolean resolveFields(ResolverStyle resolverStyle) {
        int i = 0;
        loop0:
        while (i < 100) {
            for (Entry key : this.fieldValues.entrySet()) {
                TemporalField temporalField = (TemporalField) key.getKey();
                Object resolve = temporalField.resolve(this.fieldValues, this, resolverStyle);
                if (resolve != null) {
                    StringBuilder stringBuilder;
                    if (resolve instanceof ChronoZonedDateTime) {
                        ChronoZonedDateTime chronoZonedDateTime = (ChronoZonedDateTime) resolve;
                        if (this.zone == null) {
                            this.zone = chronoZonedDateTime.getZone();
                        } else if (!this.zone.equals(chronoZonedDateTime.getZone())) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("ChronoZonedDateTime must use the effective parsed zone: ");
                            stringBuilder.append(this.zone);
                            throw new DateTimeException(stringBuilder.toString());
                        }
                        resolve = chronoZonedDateTime.toLocalDateTime();
                    }
                    if (resolve instanceof ChronoLocalDate) {
                        resolveMakeChanges(temporalField, (ChronoLocalDate) resolve);
                        i++;
                    } else if (resolve instanceof LocalTime) {
                        resolveMakeChanges(temporalField, (LocalTime) resolve);
                        i++;
                    } else if (resolve instanceof ChronoLocalDateTime) {
                        ChronoLocalDateTime chronoLocalDateTime = (ChronoLocalDateTime) resolve;
                        resolveMakeChanges(temporalField, chronoLocalDateTime.toLocalDate());
                        resolveMakeChanges(temporalField, chronoLocalDateTime.toLocalTime());
                        i++;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown type: ");
                        stringBuilder.append(resolve.getClass().getName());
                        throw new DateTimeException(stringBuilder.toString());
                    }
                } else if (!this.fieldValues.containsKey(temporalField)) {
                    i++;
                }
            }
        }
        if (i == 100) {
            throw new DateTimeException("Badly written field");
        } else if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void resolveMakeChanges(TemporalField temporalField, ChronoLocalDate chronoLocalDate) {
        if (this.chrono.equals(chronoLocalDate.getChronology())) {
            long toEpochDay = chronoLocalDate.toEpochDay();
            Long l = (Long) this.fieldValues.put(ChronoField.EPOCH_DAY, Long.valueOf(toEpochDay));
            if (l != null && l.longValue() != toEpochDay) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Conflict found: ");
                stringBuilder.append(LocalDate.ofEpochDay(l.longValue()));
                stringBuilder.append(" differs from ");
                stringBuilder.append(LocalDate.ofEpochDay(toEpochDay));
                stringBuilder.append(" while resolving  ");
                stringBuilder.append(temporalField);
                throw new DateTimeException(stringBuilder.toString());
            }
            return;
        }
        chronoLocalDate = new StringBuilder();
        chronoLocalDate.append("ChronoLocalDate must use the effective parsed chronology: ");
        chronoLocalDate.append(this.chrono);
        throw new DateTimeException(chronoLocalDate.toString());
    }

    private void resolveMakeChanges(TemporalField temporalField, LocalTime localTime) {
        long toNanoOfDay = localTime.toNanoOfDay();
        Long l = (Long) this.fieldValues.put(ChronoField.NANO_OF_DAY, Long.valueOf(toNanoOfDay));
        if (l != null && l.longValue() != toNanoOfDay) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Conflict found: ");
            stringBuilder.append(LocalTime.ofNanoOfDay(l.longValue()));
            stringBuilder.append(" differs from ");
            stringBuilder.append(localTime);
            stringBuilder.append(" while resolving  ");
            stringBuilder.append(temporalField);
            throw new DateTimeException(stringBuilder.toString());
        }
    }

    private void mergeDate(ResolverStyle resolverStyle) {
        if (this.chrono instanceof IsoChronology) {
            checkDate(IsoChronology.INSTANCE.resolveDate(this.fieldValues, resolverStyle));
        } else if (this.fieldValues.containsKey(ChronoField.EPOCH_DAY) != null) {
            checkDate(LocalDate.ofEpochDay(((Long) this.fieldValues.remove(ChronoField.EPOCH_DAY)).longValue()));
        }
    }

    private void checkDate(org.threeten.bp.LocalDate r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r8 = this;
        if (r9 == 0) goto L_0x0070;
    L_0x0002:
        r8.addObject(r9);
        r0 = r8.fieldValues;
        r0 = r0.keySet();
        r0 = r0.iterator();
    L_0x000f:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x0070;
    L_0x0015:
        r1 = r0.next();
        r1 = (org.threeten.bp.temporal.TemporalField) r1;
        r2 = r1 instanceof org.threeten.bp.temporal.ChronoField;
        if (r2 == 0) goto L_0x000f;
    L_0x001f:
        r2 = r1.isDateBased();
        if (r2 == 0) goto L_0x000f;
    L_0x0025:
        r2 = r9.getLong(r1);	 Catch:{ DateTimeException -> 0x000f }
        r4 = r8.fieldValues;
        r4 = r4.get(r1);
        r4 = (java.lang.Long) r4;
        r5 = r4.longValue();
        r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r7 == 0) goto L_0x000f;
    L_0x0039:
        r0 = new org.threeten.bp.DateTimeException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Conflict found: Field ";
        r5.append(r6);
        r5.append(r1);
        r6 = " ";
        r5.append(r6);
        r5.append(r2);
        r2 = " differs from ";
        r5.append(r2);
        r5.append(r1);
        r1 = " ";
        r5.append(r1);
        r5.append(r4);
        r1 = " derived from ";
        r5.append(r1);
        r5.append(r9);
        r9 = r5.toString();
        r0.<init>(r9);
        throw r0;
    L_0x0070:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.format.DateTimeBuilder.checkDate(org.threeten.bp.LocalDate):void");
    }

    private void mergeTime(ResolverStyle resolverStyle) {
        long longValue;
        long longValue2;
        long longValue3;
        long j = 0;
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_DAY)) {
            longValue = ((Long) this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_DAY)).longValue();
            if (resolverStyle != ResolverStyle.LENIENT) {
                if (resolverStyle != ResolverStyle.SMART || longValue != 0) {
                    ChronoField.CLOCK_HOUR_OF_DAY.checkValidValue(longValue);
                }
            }
            TemporalField temporalField = ChronoField.HOUR_OF_DAY;
            if (longValue == 24) {
                longValue = 0;
            }
            addFieldValue(temporalField, longValue);
        }
        if (this.fieldValues.containsKey(ChronoField.CLOCK_HOUR_OF_AMPM)) {
            longValue2 = ((Long) this.fieldValues.remove(ChronoField.CLOCK_HOUR_OF_AMPM)).longValue();
            if (resolverStyle != ResolverStyle.LENIENT) {
                if (resolverStyle != ResolverStyle.SMART || longValue2 != 0) {
                    ChronoField.CLOCK_HOUR_OF_AMPM.checkValidValue(longValue2);
                }
            }
            temporalField = ChronoField.HOUR_OF_AMPM;
            if (longValue2 != 12) {
                j = longValue2;
            }
            addFieldValue(temporalField, j);
        }
        if (resolverStyle != ResolverStyle.LENIENT) {
            if (this.fieldValues.containsKey(ChronoField.AMPM_OF_DAY)) {
                ChronoField.AMPM_OF_DAY.checkValidValue(((Long) this.fieldValues.get(ChronoField.AMPM_OF_DAY)).longValue());
            }
            if (this.fieldValues.containsKey(ChronoField.HOUR_OF_AMPM)) {
                ChronoField.HOUR_OF_AMPM.checkValidValue(((Long) this.fieldValues.get(ChronoField.HOUR_OF_AMPM)).longValue());
            }
        }
        if (this.fieldValues.containsKey(ChronoField.AMPM_OF_DAY) && this.fieldValues.containsKey(ChronoField.HOUR_OF_AMPM)) {
            long longValue4 = ((Long) this.fieldValues.remove(ChronoField.AMPM_OF_DAY)).longValue();
            addFieldValue(ChronoField.HOUR_OF_DAY, (longValue4 * 12) + ((Long) this.fieldValues.remove(ChronoField.HOUR_OF_AMPM)).longValue());
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_DAY)) {
            longValue4 = ((Long) this.fieldValues.remove(ChronoField.NANO_OF_DAY)).longValue();
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.NANO_OF_DAY.checkValidValue(longValue4);
            }
            addFieldValue(ChronoField.SECOND_OF_DAY, longValue4 / C0649C.NANOS_PER_SECOND);
            addFieldValue(ChronoField.NANO_OF_SECOND, longValue4 % C0649C.NANOS_PER_SECOND);
        }
        if (this.fieldValues.containsKey(ChronoField.MICRO_OF_DAY)) {
            longValue = ((Long) this.fieldValues.remove(ChronoField.MICRO_OF_DAY)).longValue();
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MICRO_OF_DAY.checkValidValue(longValue);
            }
            addFieldValue(ChronoField.SECOND_OF_DAY, longValue / C0649C.MICROS_PER_SECOND);
            addFieldValue(ChronoField.MICRO_OF_SECOND, longValue % C0649C.MICROS_PER_SECOND);
        }
        if (this.fieldValues.containsKey(ChronoField.MILLI_OF_DAY)) {
            longValue2 = ((Long) this.fieldValues.remove(ChronoField.MILLI_OF_DAY)).longValue();
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MILLI_OF_DAY.checkValidValue(longValue2);
            }
            addFieldValue(ChronoField.SECOND_OF_DAY, longValue2 / 1000);
            addFieldValue(ChronoField.MILLI_OF_SECOND, longValue2 % 1000);
        }
        if (this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY)) {
            longValue3 = ((Long) this.fieldValues.remove(ChronoField.SECOND_OF_DAY)).longValue();
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.SECOND_OF_DAY.checkValidValue(longValue3);
            }
            addFieldValue(ChronoField.HOUR_OF_DAY, longValue3 / 3600);
            addFieldValue(ChronoField.MINUTE_OF_HOUR, (longValue3 / 60) % 60);
            addFieldValue(ChronoField.SECOND_OF_MINUTE, longValue3 % 60);
        }
        if (this.fieldValues.containsKey(ChronoField.MINUTE_OF_DAY)) {
            longValue3 = ((Long) this.fieldValues.remove(ChronoField.MINUTE_OF_DAY)).longValue();
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.MINUTE_OF_DAY.checkValidValue(longValue3);
            }
            addFieldValue(ChronoField.HOUR_OF_DAY, longValue3 / 60);
            addFieldValue(ChronoField.MINUTE_OF_HOUR, longValue3 % 60);
        }
        if (resolverStyle != ResolverStyle.LENIENT) {
            if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND) != null) {
                ChronoField.MILLI_OF_SECOND.checkValidValue(((Long) this.fieldValues.get(ChronoField.MILLI_OF_SECOND)).longValue());
            }
            if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND) != null) {
                ChronoField.MICRO_OF_SECOND.checkValidValue(((Long) this.fieldValues.get(ChronoField.MICRO_OF_SECOND)).longValue());
            }
        }
        if (!(this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND) == null || this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND) == null)) {
            addFieldValue(ChronoField.MICRO_OF_SECOND, (((Long) this.fieldValues.remove(ChronoField.MILLI_OF_SECOND)).longValue() * 1000) + (((Long) this.fieldValues.get(ChronoField.MICRO_OF_SECOND)).longValue() % 1000));
        }
        if (!(this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND) == null || this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND) == null)) {
            addFieldValue(ChronoField.MICRO_OF_SECOND, ((Long) this.fieldValues.get(ChronoField.NANO_OF_SECOND)).longValue() / 1000);
            this.fieldValues.remove(ChronoField.MICRO_OF_SECOND);
        }
        if (!(this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND) == null || this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND) == null)) {
            addFieldValue(ChronoField.MILLI_OF_SECOND, ((Long) this.fieldValues.get(ChronoField.NANO_OF_SECOND)).longValue() / C0649C.MICROS_PER_SECOND);
            this.fieldValues.remove(ChronoField.MILLI_OF_SECOND);
        }
        if (this.fieldValues.containsKey(ChronoField.MICRO_OF_SECOND) != null) {
            addFieldValue(ChronoField.NANO_OF_SECOND, ((Long) this.fieldValues.remove(ChronoField.MICRO_OF_SECOND)).longValue() * 1000);
        } else if (this.fieldValues.containsKey(ChronoField.MILLI_OF_SECOND) != null) {
            addFieldValue(ChronoField.NANO_OF_SECOND, ((Long) this.fieldValues.remove(ChronoField.MILLI_OF_SECOND)).longValue() * C0649C.MICROS_PER_SECOND);
        }
    }

    private void resolveTimeInferZeroes(ResolverStyle resolverStyle) {
        Long l = (Long) this.fieldValues.get(ChronoField.HOUR_OF_DAY);
        Long l2 = (Long) this.fieldValues.get(ChronoField.MINUTE_OF_HOUR);
        Long l3 = (Long) this.fieldValues.get(ChronoField.SECOND_OF_MINUTE);
        Long l4 = (Long) this.fieldValues.get(ChronoField.NANO_OF_SECOND);
        if (l != null) {
            if (l2 == null && (l3 != null || l4 != null)) {
                return;
            }
            if (l2 == null || l3 != null || l4 == null) {
                if (resolverStyle != ResolverStyle.LENIENT) {
                    if (l != null) {
                        if (resolverStyle == ResolverStyle.SMART && l.longValue() == 24 && ((l2 == null || l2.longValue() == 0) && ((l3 == null || l3.longValue() == 0) && (l4 == null || l4.longValue() == 0)))) {
                            l = Long.valueOf(0);
                            this.excessDays = Period.ofDays(1);
                        }
                        resolverStyle = ChronoField.HOUR_OF_DAY.checkValidIntValue(l.longValue());
                        if (l2 != null) {
                            int checkValidIntValue = ChronoField.MINUTE_OF_HOUR.checkValidIntValue(l2.longValue());
                            if (l3 != null) {
                                int checkValidIntValue2 = ChronoField.SECOND_OF_MINUTE.checkValidIntValue(l3.longValue());
                                if (l4 != null) {
                                    addObject(LocalTime.of(resolverStyle, checkValidIntValue, checkValidIntValue2, ChronoField.NANO_OF_SECOND.checkValidIntValue(l4.longValue())));
                                } else {
                                    addObject(LocalTime.of(resolverStyle, checkValidIntValue, checkValidIntValue2));
                                }
                            } else if (l4 == null) {
                                addObject(LocalTime.of(resolverStyle, checkValidIntValue));
                            }
                        } else if (l3 == null && l4 == null) {
                            addObject(LocalTime.of(resolverStyle, 0));
                        }
                    }
                } else if (l != null) {
                    long longValue = l.longValue();
                    if (l2 == null) {
                        resolverStyle = Jdk8Methods.safeToInt(Jdk8Methods.floorDiv(longValue, 24));
                        addObject(LocalTime.of((int) ((long) Jdk8Methods.floorMod(longValue, 24)), 0));
                        this.excessDays = Period.ofDays(resolverStyle);
                    } else if (l3 != null) {
                        if (l4 == null) {
                            l4 = Long.valueOf(0);
                        }
                        r0 = Jdk8Methods.safeAdd(Jdk8Methods.safeAdd(Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(longValue, 3600000000000L), Jdk8Methods.safeMultiply(l2.longValue(), 60000000000L)), Jdk8Methods.safeMultiply(l3.longValue(), (long) C0649C.NANOS_PER_SECOND)), l4.longValue());
                        resolverStyle = (int) Jdk8Methods.floorDiv(r0, 86400000000000L);
                        addObject(LocalTime.ofNanoOfDay(Jdk8Methods.floorMod(r0, 86400000000000L)));
                        this.excessDays = Period.ofDays(resolverStyle);
                    } else {
                        r0 = Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(longValue, 3600), Jdk8Methods.safeMultiply(l2.longValue(), 60));
                        resolverStyle = (int) Jdk8Methods.floorDiv(r0, 86400);
                        addObject(LocalTime.ofSecondOfDay(Jdk8Methods.floorMod(r0, 86400)));
                        this.excessDays = Period.ofDays(resolverStyle);
                    }
                }
                this.fieldValues.remove(ChronoField.HOUR_OF_DAY);
                this.fieldValues.remove(ChronoField.MINUTE_OF_HOUR);
                this.fieldValues.remove(ChronoField.SECOND_OF_MINUTE);
                this.fieldValues.remove(ChronoField.NANO_OF_SECOND);
            }
        }
    }

    private void mergeInstantFields() {
        if (!this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS)) {
            return;
        }
        if (this.zone != null) {
            mergeInstantFields0(this.zone);
            return;
        }
        Long l = (Long) this.fieldValues.get(ChronoField.OFFSET_SECONDS);
        if (l != null) {
            mergeInstantFields0(ZoneOffset.ofTotalSeconds(l.intValue()));
        }
    }

    private void mergeInstantFields0(ZoneId zoneId) {
        zoneId = this.chrono.zonedDateTime(Instant.ofEpochSecond(((Long) this.fieldValues.remove(ChronoField.INSTANT_SECONDS)).longValue()), zoneId);
        if (this.date == null) {
            addObject(zoneId.toLocalDate());
        } else {
            resolveMakeChanges(ChronoField.INSTANT_SECONDS, zoneId.toLocalDate());
        }
        addFieldValue(ChronoField.SECOND_OF_DAY, (long) zoneId.toLocalTime().toSecondOfDay());
    }

    private void crossCheck() {
        if (this.fieldValues.size() <= 0) {
            return;
        }
        if (this.date != null && this.time != null) {
            crossCheck(this.date.atTime(this.time));
        } else if (this.date != null) {
            crossCheck(this.date);
        } else if (this.time != null) {
            crossCheck(this.time);
        }
    }

    private void crossCheck(org.threeten.bp.temporal.TemporalAccessor r8) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r7 = this;
        r0 = r7.fieldValues;
        r0 = r0.entrySet();
        r0 = r0.iterator();
    L_0x000a:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x0067;
    L_0x0010:
        r1 = r0.next();
        r1 = (java.util.Map.Entry) r1;
        r2 = r1.getKey();
        r2 = (org.threeten.bp.temporal.TemporalField) r2;
        r1 = r1.getValue();
        r1 = (java.lang.Long) r1;
        r3 = r1.longValue();
        r1 = r8.isSupported(r2);
        if (r1 == 0) goto L_0x000a;
    L_0x002c:
        r5 = r8.getLong(r2);	 Catch:{ RuntimeException -> 0x000a }
        r1 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1));
        if (r1 == 0) goto L_0x0063;
    L_0x0034:
        r8 = new org.threeten.bp.DateTimeException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Cross check failed: ";
        r0.append(r1);
        r0.append(r2);
        r1 = " ";
        r0.append(r1);
        r0.append(r5);
        r1 = " vs ";
        r0.append(r1);
        r0.append(r2);
        r1 = " ";
        r0.append(r1);
        r0.append(r3);
        r0 = r0.toString();
        r8.<init>(r0);
        throw r8;
    L_0x0063:
        r0.remove();
        goto L_0x000a;
    L_0x0067:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.format.DateTimeBuilder.crossCheck(org.threeten.bp.temporal.TemporalAccessor):void");
    }

    private void resolveFractional() {
        if (this.time != null) {
            return;
        }
        if (!this.fieldValues.containsKey(ChronoField.INSTANT_SECONDS) && !this.fieldValues.containsKey(ChronoField.SECOND_OF_DAY) && !this.fieldValues.containsKey(ChronoField.SECOND_OF_MINUTE)) {
            return;
        }
        if (this.fieldValues.containsKey(ChronoField.NANO_OF_SECOND)) {
            long longValue = ((Long) this.fieldValues.get(ChronoField.NANO_OF_SECOND)).longValue();
            this.fieldValues.put(ChronoField.MICRO_OF_SECOND, Long.valueOf(longValue / 1000));
            this.fieldValues.put(ChronoField.MILLI_OF_SECOND, Long.valueOf(longValue / C0649C.MICROS_PER_SECOND));
            return;
        }
        this.fieldValues.put(ChronoField.NANO_OF_SECOND, Long.valueOf(0));
        this.fieldValues.put(ChronoField.MICRO_OF_SECOND, Long.valueOf(0));
        this.fieldValues.put(ChronoField.MILLI_OF_SECOND, Long.valueOf(0));
    }

    private void resolveInstant() {
        if (this.date != null && this.time != null) {
            if (this.zone != null) {
                this.fieldValues.put(ChronoField.INSTANT_SECONDS, Long.valueOf(this.date.atTime(this.time).atZone(this.zone).getLong(ChronoField.INSTANT_SECONDS)));
                return;
            }
            Long l = (Long) this.fieldValues.get(ChronoField.OFFSET_SECONDS);
            if (l != null) {
                this.fieldValues.put(ChronoField.INSTANT_SECONDS, Long.valueOf(this.date.atTime(this.time).atZone(ZoneOffset.ofTotalSeconds(l.intValue())).getLong(ChronoField.INSTANT_SECONDS)));
            }
        }
    }

    public <R> R build(TemporalQuery<R> temporalQuery) {
        return temporalQuery.queryFrom(this);
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = false;
        if (temporalField == null) {
            return false;
        }
        if (this.fieldValues.containsKey(temporalField) || ((this.date != null && this.date.isSupported(temporalField)) || !(this.time == null || this.time.isSupported(temporalField) == null))) {
            z = true;
        }
        return z;
    }

    public long getLong(TemporalField temporalField) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        Long fieldValue0 = getFieldValue0(temporalField);
        if (fieldValue0 != null) {
            return fieldValue0.longValue();
        }
        if (this.date != null && this.date.isSupported(temporalField)) {
            return this.date.getLong(temporalField);
        }
        if (this.time != null && this.time.isSupported(temporalField)) {
            return this.time.getLong(temporalField);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Field not found: ");
        stringBuilder.append(temporalField);
        throw new DateTimeException(stringBuilder.toString());
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zoneId()) {
            return this.zone;
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return this.chrono;
        }
        R r = null;
        if (temporalQuery == TemporalQueries.localDate()) {
            if (this.date != null) {
                r = LocalDate.from(this.date);
            }
            return r;
        } else if (temporalQuery == TemporalQueries.localTime()) {
            return this.time;
        } else {
            if (temporalQuery != TemporalQueries.zone()) {
                if (temporalQuery != TemporalQueries.offset()) {
                    if (temporalQuery == TemporalQueries.precision()) {
                        return null;
                    }
                    return temporalQuery.queryFrom(this);
                }
            }
            return temporalQuery.queryFrom(this);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("DateTimeBuilder[");
        if (this.fieldValues.size() > 0) {
            stringBuilder.append("fields=");
            stringBuilder.append(this.fieldValues);
        }
        stringBuilder.append(", ");
        stringBuilder.append(this.chrono);
        stringBuilder.append(", ");
        stringBuilder.append(this.zone);
        stringBuilder.append(", ");
        stringBuilder.append(this.date);
        stringBuilder.append(", ");
        stringBuilder.append(this.time);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
