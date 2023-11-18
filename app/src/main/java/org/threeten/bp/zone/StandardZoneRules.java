package org.threeten.bp.zone;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.ChronoLocalDateTime;
import org.threeten.bp.jdk8.Jdk8Methods;

final class StandardZoneRules extends ZoneRules implements Serializable {
    private static final int LAST_CACHED_YEAR = 2100;
    private static final long serialVersionUID = 3044319355680032515L;
    private final ZoneOffsetTransitionRule[] lastRules;
    private final ConcurrentMap<Integer, ZoneOffsetTransition[]> lastRulesCache = new ConcurrentHashMap();
    private final long[] savingsInstantTransitions;
    private final LocalDateTime[] savingsLocalTransitions;
    private final ZoneOffset[] standardOffsets;
    private final long[] standardTransitions;
    private final ZoneOffset[] wallOffsets;

    StandardZoneRules(ZoneOffset zoneOffset, ZoneOffset zoneOffset2, List<ZoneOffsetTransition> list, List<ZoneOffsetTransition> list2, List<ZoneOffsetTransitionRule> list3) {
        this.standardTransitions = new long[list.size()];
        this.standardOffsets = new ZoneOffset[(list.size() + 1)];
        int i = 0;
        this.standardOffsets[0] = zoneOffset;
        zoneOffset = null;
        while (zoneOffset < list.size()) {
            this.standardTransitions[zoneOffset] = ((ZoneOffsetTransition) list.get(zoneOffset)).toEpochSecond();
            int i2 = zoneOffset + 1;
            this.standardOffsets[i2] = ((ZoneOffsetTransition) list.get(zoneOffset)).getOffsetAfter();
            zoneOffset = i2;
        }
        zoneOffset = new ArrayList();
        list = new ArrayList();
        list.add(zoneOffset2);
        for (ZoneOffsetTransition zoneOffsetTransition : list2) {
            if (zoneOffsetTransition.isGap()) {
                zoneOffset.add(zoneOffsetTransition.getDateTimeBefore());
                zoneOffset.add(zoneOffsetTransition.getDateTimeAfter());
            } else {
                zoneOffset.add(zoneOffsetTransition.getDateTimeAfter());
                zoneOffset.add(zoneOffsetTransition.getDateTimeBefore());
            }
            list.add(zoneOffsetTransition.getOffsetAfter());
        }
        this.savingsLocalTransitions = (LocalDateTime[]) zoneOffset.toArray(new LocalDateTime[zoneOffset.size()]);
        this.wallOffsets = (ZoneOffset[]) list.toArray(new ZoneOffset[list.size()]);
        this.savingsInstantTransitions = new long[list2.size()];
        while (i < list2.size()) {
            this.savingsInstantTransitions[i] = ((ZoneOffsetTransition) list2.get(i)).getInstant().getEpochSecond();
            i++;
        }
        if (list3.size() > 15) {
            throw new IllegalArgumentException("Too many transition rules");
        }
        this.lastRules = (ZoneOffsetTransitionRule[]) list3.toArray(new ZoneOffsetTransitionRule[list3.size()]);
    }

    private StandardZoneRules(long[] jArr, ZoneOffset[] zoneOffsetArr, long[] jArr2, ZoneOffset[] zoneOffsetArr2, ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr) {
        this.standardTransitions = jArr;
        this.standardOffsets = zoneOffsetArr;
        this.savingsInstantTransitions = jArr2;
        this.wallOffsets = zoneOffsetArr2;
        this.lastRules = zoneOffsetTransitionRuleArr;
        jArr = new ArrayList();
        zoneOffsetArr = null;
        while (zoneOffsetArr < jArr2.length) {
            int i = zoneOffsetArr + 1;
            ZoneOffsetTransition zoneOffsetTransition = new ZoneOffsetTransition(jArr2[zoneOffsetArr], zoneOffsetArr2[zoneOffsetArr], zoneOffsetArr2[i]);
            if (zoneOffsetTransition.isGap() != null) {
                jArr.add(zoneOffsetTransition.getDateTimeBefore());
                jArr.add(zoneOffsetTransition.getDateTimeAfter());
            } else {
                jArr.add(zoneOffsetTransition.getDateTimeAfter());
                jArr.add(zoneOffsetTransition.getDateTimeBefore());
            }
            zoneOffsetArr = i;
        }
        this.savingsLocalTransitions = (LocalDateTime[]) jArr.toArray(new LocalDateTime[jArr.size()]);
    }

    private Object writeReplace() {
        return new Ser((byte) 1, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.standardTransitions.length);
        for (long writeEpochSec : this.standardTransitions) {
            Ser.writeEpochSec(writeEpochSec, dataOutput);
        }
        for (ZoneOffset writeOffset : this.standardOffsets) {
            Ser.writeOffset(writeOffset, dataOutput);
        }
        dataOutput.writeInt(this.savingsInstantTransitions.length);
        for (long writeEpochSec2 : this.savingsInstantTransitions) {
            Ser.writeEpochSec(writeEpochSec2, dataOutput);
        }
        for (ZoneOffset writeOffset2 : this.wallOffsets) {
            Ser.writeOffset(writeOffset2, dataOutput);
        }
        dataOutput.writeByte(this.lastRules.length);
        for (ZoneOffsetTransitionRule writeExternal : this.lastRules) {
            writeExternal.writeExternal(dataOutput);
        }
    }

    static StandardZoneRules readExternal(DataInput dataInput) throws IOException, ClassNotFoundException {
        int readInt = dataInput.readInt();
        long[] jArr = new long[readInt];
        for (int i = 0; i < readInt; i++) {
            jArr[i] = Ser.readEpochSec(dataInput);
        }
        ZoneOffset[] zoneOffsetArr = new ZoneOffset[(readInt + 1)];
        for (readInt = 0; readInt < zoneOffsetArr.length; readInt++) {
            zoneOffsetArr[readInt] = Ser.readOffset(dataInput);
        }
        readInt = dataInput.readInt();
        long[] jArr2 = new long[readInt];
        for (int i2 = 0; i2 < readInt; i2++) {
            jArr2[i2] = Ser.readEpochSec(dataInput);
        }
        ZoneOffset[] zoneOffsetArr2 = new ZoneOffset[(readInt + 1)];
        for (readInt = 0; readInt < zoneOffsetArr2.length; readInt++) {
            zoneOffsetArr2[readInt] = Ser.readOffset(dataInput);
        }
        byte readByte = dataInput.readByte();
        ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr = new ZoneOffsetTransitionRule[readByte];
        for (byte b = (byte) 0; b < readByte; b++) {
            zoneOffsetTransitionRuleArr[b] = ZoneOffsetTransitionRule.readExternal(dataInput);
        }
        return new StandardZoneRules(jArr, zoneOffsetArr, jArr2, zoneOffsetArr2, zoneOffsetTransitionRuleArr);
    }

    public boolean isFixedOffset() {
        return this.savingsInstantTransitions.length == 0;
    }

    public ZoneOffset getOffset(Instant instant) {
        long epochSecond = instant.getEpochSecond();
        if (this.lastRules.length <= null || epochSecond <= this.savingsInstantTransitions[this.savingsInstantTransitions.length - 1]) {
            instant = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
            if (instant < null) {
                instant = (-instant) - 2;
            }
            return this.wallOffsets[instant + 1];
        }
        instant = findTransitionArray(findYear(epochSecond, this.wallOffsets[this.wallOffsets.length - 1]));
        ZoneOffsetTransition zoneOffsetTransition = null;
        for (ZoneOffsetTransition zoneOffsetTransition2 : instant) {
            if (epochSecond < zoneOffsetTransition2.toEpochSecond()) {
                return zoneOffsetTransition2.getOffsetBefore();
            }
        }
        return zoneOffsetTransition2.getOffsetAfter();
    }

    public ZoneOffset getOffset(LocalDateTime localDateTime) {
        localDateTime = getOffsetInfo(localDateTime);
        if (localDateTime instanceof ZoneOffsetTransition) {
            return ((ZoneOffsetTransition) localDateTime).getOffsetBefore();
        }
        return (ZoneOffset) localDateTime;
    }

    public List<ZoneOffset> getValidOffsets(LocalDateTime localDateTime) {
        localDateTime = getOffsetInfo(localDateTime);
        if (localDateTime instanceof ZoneOffsetTransition) {
            return ((ZoneOffsetTransition) localDateTime).getValidOffsets();
        }
        return Collections.singletonList((ZoneOffset) localDateTime);
    }

    public ZoneOffsetTransition getTransition(LocalDateTime localDateTime) {
        localDateTime = getOffsetInfo(localDateTime);
        return localDateTime instanceof ZoneOffsetTransition ? (ZoneOffsetTransition) localDateTime : null;
    }

    private Object getOffsetInfo(LocalDateTime localDateTime) {
        int i = 0;
        if (this.lastRules.length <= 0 || !localDateTime.isAfter(this.savingsLocalTransitions[this.savingsLocalTransitions.length - 1])) {
            localDateTime = Arrays.binarySearch(this.savingsLocalTransitions, localDateTime);
            if (localDateTime == -1) {
                return this.wallOffsets[0];
            }
            if (localDateTime < null) {
                localDateTime = (-localDateTime) - 2;
            } else if (localDateTime < this.savingsLocalTransitions.length - 1) {
                int i2 = localDateTime + 1;
                if (this.savingsLocalTransitions[localDateTime].equals(this.savingsLocalTransitions[i2])) {
                    localDateTime = i2;
                }
            }
            if ((localDateTime & 1) != 0) {
                return this.wallOffsets[(localDateTime / 2) + 1];
            }
            LocalDateTime localDateTime2 = this.savingsLocalTransitions[localDateTime];
            LocalDateTime localDateTime3 = this.savingsLocalTransitions[localDateTime + 1];
            localDateTime /= 2;
            ZoneOffset zoneOffset = this.wallOffsets[localDateTime];
            ZoneOffset zoneOffset2 = this.wallOffsets[localDateTime + 1];
            if (zoneOffset2.getTotalSeconds() > zoneOffset.getTotalSeconds()) {
                return new ZoneOffsetTransition(localDateTime2, zoneOffset, zoneOffset2);
            }
            return new ZoneOffsetTransition(localDateTime3, zoneOffset, zoneOffset2);
        }
        ZoneOffsetTransition[] findTransitionArray = findTransitionArray(localDateTime.getYear());
        Object obj = null;
        int length = findTransitionArray.length;
        while (i < length) {
            ZoneOffsetTransition zoneOffsetTransition = findTransitionArray[i];
            Object findOffsetInfo = findOffsetInfo(localDateTime, zoneOffsetTransition);
            if (!(findOffsetInfo instanceof ZoneOffsetTransition)) {
                if (!findOffsetInfo.equals(zoneOffsetTransition.getOffsetBefore())) {
                    i++;
                    obj = findOffsetInfo;
                }
            }
            return findOffsetInfo;
        }
        return obj;
    }

    private Object findOffsetInfo(LocalDateTime localDateTime, ZoneOffsetTransition zoneOffsetTransition) {
        ChronoLocalDateTime dateTimeBefore = zoneOffsetTransition.getDateTimeBefore();
        if (zoneOffsetTransition.isGap()) {
            if (localDateTime.isBefore(dateTimeBefore)) {
                return zoneOffsetTransition.getOffsetBefore();
            }
            if (localDateTime.isBefore(zoneOffsetTransition.getDateTimeAfter()) != null) {
                return zoneOffsetTransition;
            }
            return zoneOffsetTransition.getOffsetAfter();
        } else if (localDateTime.isBefore(dateTimeBefore)) {
            return localDateTime.isBefore(zoneOffsetTransition.getDateTimeAfter()) != null ? zoneOffsetTransition.getOffsetBefore() : zoneOffsetTransition;
        } else {
            return zoneOffsetTransition.getOffsetAfter();
        }
    }

    public boolean isValidOffset(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        return getValidOffsets(localDateTime).contains(zoneOffset);
    }

    private ZoneOffsetTransition[] findTransitionArray(int i) {
        Integer valueOf = Integer.valueOf(i);
        ZoneOffsetTransition[] zoneOffsetTransitionArr = (ZoneOffsetTransition[]) this.lastRulesCache.get(valueOf);
        if (zoneOffsetTransitionArr != null) {
            return zoneOffsetTransitionArr;
        }
        ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr = this.lastRules;
        Object obj = new ZoneOffsetTransition[zoneOffsetTransitionRuleArr.length];
        for (int i2 = 0; i2 < zoneOffsetTransitionRuleArr.length; i2++) {
            obj[i2] = zoneOffsetTransitionRuleArr[i2].createTransition(i);
        }
        if (i < 2100) {
            this.lastRulesCache.putIfAbsent(valueOf, obj);
        }
        return obj;
    }

    public ZoneOffset getStandardOffset(Instant instant) {
        instant = Arrays.binarySearch(this.standardTransitions, instant.getEpochSecond());
        if (instant < null) {
            instant = (-instant) - 2;
        }
        return this.standardOffsets[instant + 1];
    }

    public Duration getDaylightSavings(Instant instant) {
        return Duration.ofSeconds((long) (getOffset(instant).getTotalSeconds() - getStandardOffset(instant).getTotalSeconds()));
    }

    public boolean isDaylightSavings(Instant instant) {
        return getStandardOffset(instant).equals(getOffset(instant)) ^ 1;
    }

    public ZoneOffsetTransition nextTransition(Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return null;
        }
        long epochSecond = instant.getEpochSecond();
        if (epochSecond < this.savingsInstantTransitions[this.savingsInstantTransitions.length - 1]) {
            instant = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
            instant = instant < null ? (-instant) - 1 : instant + 1;
            return new ZoneOffsetTransition(this.savingsInstantTransitions[instant], this.wallOffsets[instant], this.wallOffsets[instant + 1]);
        } else if (this.lastRules.length == null) {
            return null;
        } else {
            instant = findYear(epochSecond, this.wallOffsets[this.wallOffsets.length - 1]);
            for (ZoneOffsetTransition zoneOffsetTransition : findTransitionArray(instant)) {
                if (epochSecond < zoneOffsetTransition.toEpochSecond()) {
                    return zoneOffsetTransition;
                }
            }
            if (instant < 999999999) {
                return findTransitionArray(instant + 1)[0];
            }
            return null;
        }
    }

    public ZoneOffsetTransition previousTransition(Instant instant) {
        if (this.savingsInstantTransitions.length == 0) {
            return null;
        }
        long epochSecond = instant.getEpochSecond();
        if (instant.getNano() > null && epochSecond < Long.MAX_VALUE) {
            epochSecond++;
        }
        long j = this.savingsInstantTransitions[this.savingsInstantTransitions.length - 1];
        if (this.lastRules.length > null && epochSecond > j) {
            instant = this.wallOffsets[this.wallOffsets.length - 1];
            int findYear = findYear(epochSecond, instant);
            ZoneOffsetTransition[] findTransitionArray = findTransitionArray(findYear);
            for (int length = findTransitionArray.length - 1; length >= 0; length--) {
                if (epochSecond > findTransitionArray[length].toEpochSecond()) {
                    return findTransitionArray[length];
                }
            }
            findYear--;
            if (findYear > findYear(j, instant)) {
                instant = findTransitionArray(findYear);
                return instant[instant.length - 1];
            }
        }
        instant = Arrays.binarySearch(this.savingsInstantTransitions, epochSecond);
        if (instant < null) {
            instant = (-instant) - 1;
        }
        if (instant <= null) {
            return null;
        }
        int i = instant - 1;
        return new ZoneOffsetTransition(this.savingsInstantTransitions[i], this.wallOffsets[i], this.wallOffsets[instant]);
    }

    private int findYear(long j, ZoneOffset zoneOffset) {
        return LocalDate.ofEpochDay(Jdk8Methods.floorDiv(j + ((long) zoneOffset.getTotalSeconds()), 86400)).getYear();
    }

    public List<ZoneOffsetTransition> getTransitions() {
        List arrayList = new ArrayList();
        int i = 0;
        while (i < this.savingsInstantTransitions.length) {
            long j = this.savingsInstantTransitions[i];
            ZoneOffset zoneOffset = this.wallOffsets[i];
            i++;
            arrayList.add(new ZoneOffsetTransition(j, zoneOffset, this.wallOffsets[i]));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public List<ZoneOffsetTransitionRule> getTransitionRules() {
        return Collections.unmodifiableList(Arrays.asList(this.lastRules));
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj instanceof StandardZoneRules) {
            StandardZoneRules standardZoneRules = (StandardZoneRules) obj;
            if (!Arrays.equals(this.standardTransitions, standardZoneRules.standardTransitions) || !Arrays.equals(this.standardOffsets, standardZoneRules.standardOffsets) || !Arrays.equals(this.savingsInstantTransitions, standardZoneRules.savingsInstantTransitions) || !Arrays.equals(this.wallOffsets, standardZoneRules.wallOffsets) || Arrays.equals(this.lastRules, standardZoneRules.lastRules) == null) {
                z = false;
            }
            return z;
        } else if (!(obj instanceof Fixed)) {
            return false;
        } else {
            if (!isFixedOffset() || getOffset(Instant.EPOCH).equals(((Fixed) obj).getOffset(Instant.EPOCH)) == null) {
                z = false;
            }
            return z;
        }
    }

    public int hashCode() {
        return (((Arrays.hashCode(this.standardTransitions) ^ Arrays.hashCode(this.standardOffsets)) ^ Arrays.hashCode(this.savingsInstantTransitions)) ^ Arrays.hashCode(this.wallOffsets)) ^ Arrays.hashCode(this.lastRules);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("StandardZoneRules[currentStandardOffset=");
        stringBuilder.append(this.standardOffsets[this.standardOffsets.length - 1]);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
