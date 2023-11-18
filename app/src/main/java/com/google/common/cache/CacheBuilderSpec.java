package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@Beta
public final class CacheBuilderSpec {
    private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
    private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", new MaximumSizeParser()).put("maximumWeight", new MaximumWeightParser()).put("concurrencyLevel", new ConcurrencyLevelParser()).put("weakKeys", new KeyStrengthParser(Strength.WEAK)).put("softValues", new ValueStrengthParser(Strength.SOFT)).put("weakValues", new ValueStrengthParser(Strength.WEAK)).put("recordStats", new RecordStatsParser()).put("expireAfterAccess", new AccessDurationParser()).put("expireAfterWrite", new WriteDurationParser()).put("refreshAfterWrite", new RefreshDurationParser()).put("refreshInterval", new RefreshDurationParser()).build();
    @VisibleForTesting
    long accessExpirationDuration;
    @VisibleForTesting
    TimeUnit accessExpirationTimeUnit;
    @VisibleForTesting
    Integer concurrencyLevel;
    @VisibleForTesting
    Integer initialCapacity;
    @VisibleForTesting
    Strength keyStrength;
    @VisibleForTesting
    Long maximumSize;
    @VisibleForTesting
    Long maximumWeight;
    @VisibleForTesting
    Boolean recordStats;
    @VisibleForTesting
    long refreshDuration;
    @VisibleForTesting
    TimeUnit refreshTimeUnit;
    private final String specification;
    @VisibleForTesting
    Strength valueStrength;
    @VisibleForTesting
    long writeExpirationDuration;
    @VisibleForTesting
    TimeUnit writeExpirationTimeUnit;

    private interface ValueParser {
        void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2);
    }

    static abstract class DurationParser implements ValueParser {
        protected abstract void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit);

        DurationParser() {
        }

        public void parse(com.google.common.cache.CacheBuilderSpec r7, java.lang.String r8, java.lang.String r9) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r6 = this;
            r0 = 0;
            r1 = 1;
            if (r9 == 0) goto L_0x000c;
        L_0x0004:
            r2 = r9.isEmpty();
            if (r2 != 0) goto L_0x000c;
        L_0x000a:
            r2 = 1;
            goto L_0x000d;
        L_0x000c:
            r2 = 0;
        L_0x000d:
            r3 = "value of key %s omitted";
            r4 = new java.lang.Object[r1];
            r4[r0] = r8;
            com.google.common.base.Preconditions.checkArgument(r2, r3, r4);
            r2 = 2;
            r3 = r9.length();	 Catch:{ NumberFormatException -> 0x005e }
            r3 = r3 - r1;	 Catch:{ NumberFormatException -> 0x005e }
            r3 = r9.charAt(r3);	 Catch:{ NumberFormatException -> 0x005e }
            r4 = 100;	 Catch:{ NumberFormatException -> 0x005e }
            if (r3 == r4) goto L_0x004b;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x0024:
            r4 = 104; // 0x68 float:1.46E-43 double:5.14E-322;	 Catch:{ NumberFormatException -> 0x005e }
            if (r3 == r4) goto L_0x0048;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x0028:
            r4 = 109; // 0x6d float:1.53E-43 double:5.4E-322;	 Catch:{ NumberFormatException -> 0x005e }
            if (r3 == r4) goto L_0x0045;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x002c:
            r4 = 115; // 0x73 float:1.61E-43 double:5.7E-322;	 Catch:{ NumberFormatException -> 0x005e }
            if (r3 == r4) goto L_0x0042;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x0030:
            r7 = new java.lang.IllegalArgumentException;	 Catch:{ NumberFormatException -> 0x005e }
            r3 = "key %s invalid format.  was %s, must end with one of [dDhHmMsS]";	 Catch:{ NumberFormatException -> 0x005e }
            r4 = new java.lang.Object[r2];	 Catch:{ NumberFormatException -> 0x005e }
            r4[r0] = r8;	 Catch:{ NumberFormatException -> 0x005e }
            r4[r1] = r9;	 Catch:{ NumberFormatException -> 0x005e }
            r3 = java.lang.String.format(r3, r4);	 Catch:{ NumberFormatException -> 0x005e }
            r7.<init>(r3);	 Catch:{ NumberFormatException -> 0x005e }
            throw r7;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x0042:
            r3 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ NumberFormatException -> 0x005e }
            goto L_0x004d;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x0045:
            r3 = java.util.concurrent.TimeUnit.MINUTES;	 Catch:{ NumberFormatException -> 0x005e }
            goto L_0x004d;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x0048:
            r3 = java.util.concurrent.TimeUnit.HOURS;	 Catch:{ NumberFormatException -> 0x005e }
            goto L_0x004d;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x004b:
            r3 = java.util.concurrent.TimeUnit.DAYS;	 Catch:{ NumberFormatException -> 0x005e }
        L_0x004d:
            r4 = r9.length();	 Catch:{ NumberFormatException -> 0x005e }
            r4 = r4 - r1;	 Catch:{ NumberFormatException -> 0x005e }
            r4 = r9.substring(r0, r4);	 Catch:{ NumberFormatException -> 0x005e }
            r4 = java.lang.Long.parseLong(r4);	 Catch:{ NumberFormatException -> 0x005e }
            r6.parseDuration(r7, r4, r3);	 Catch:{ NumberFormatException -> 0x005e }
            return;
        L_0x005e:
            r7 = new java.lang.IllegalArgumentException;
            r3 = "key %s value set to %s, must be integer";
            r2 = new java.lang.Object[r2];
            r2[r0] = r8;
            r2[r1] = r9;
            r8 = java.lang.String.format(r3, r2);
            r7.<init>(r8);
            throw r7;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.cache.CacheBuilderSpec.DurationParser.parse(com.google.common.cache.CacheBuilderSpec, java.lang.String, java.lang.String):void");
        }
    }

    static class AccessDurationParser extends DurationParser {
        AccessDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit) {
            Preconditions.checkArgument(cacheBuilderSpec.accessExpirationTimeUnit == null, "expireAfterAccess already set");
            cacheBuilderSpec.accessExpirationDuration = j;
            cacheBuilderSpec.accessExpirationTimeUnit = timeUnit;
        }
    }

    static abstract class IntegerParser implements ValueParser {
        protected abstract void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i);

        IntegerParser() {
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, String str2) {
            boolean z = (str2 == null || str2.isEmpty()) ? false : true;
            Preconditions.checkArgument(z, "value of key %s omitted", str);
            try {
                parseInteger(cacheBuilderSpec, Integer.parseInt(str2));
            } catch (CacheBuilderSpec cacheBuilderSpec2) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", new Object[]{str, str2}), cacheBuilderSpec2);
            }
        }
    }

    static class ConcurrencyLevelParser extends IntegerParser {
        ConcurrencyLevelParser() {
        }

        protected void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i) {
            Preconditions.checkArgument(cacheBuilderSpec.concurrencyLevel == null, "concurrency level was already set to ", cacheBuilderSpec.concurrencyLevel);
            cacheBuilderSpec.concurrencyLevel = Integer.valueOf(i);
        }
    }

    static class InitialCapacityParser extends IntegerParser {
        InitialCapacityParser() {
        }

        protected void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i) {
            Preconditions.checkArgument(cacheBuilderSpec.initialCapacity == null, "initial capacity was already set to ", cacheBuilderSpec.initialCapacity);
            cacheBuilderSpec.initialCapacity = Integer.valueOf(i);
        }
    }

    static class KeyStrengthParser implements ValueParser {
        private final Strength strength;

        public KeyStrengthParser(Strength strength) {
            this.strength = strength;
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2) {
            Preconditions.checkArgument(str2 == null ? true : null, "key %s does not take values", str);
            Preconditions.checkArgument(cacheBuilderSpec.keyStrength == null ? true : null, "%s was already set to %s", str, cacheBuilderSpec.keyStrength);
            cacheBuilderSpec.keyStrength = this.strength;
        }
    }

    static abstract class LongParser implements ValueParser {
        protected abstract void parseLong(CacheBuilderSpec cacheBuilderSpec, long j);

        LongParser() {
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, String str2) {
            boolean z = (str2 == null || str2.isEmpty()) ? false : true;
            Preconditions.checkArgument(z, "value of key %s omitted", str);
            try {
                parseLong(cacheBuilderSpec, Long.parseLong(str2));
            } catch (CacheBuilderSpec cacheBuilderSpec2) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", new Object[]{str, str2}), cacheBuilderSpec2);
            }
        }
    }

    static class MaximumSizeParser extends LongParser {
        MaximumSizeParser() {
        }

        protected void parseLong(CacheBuilderSpec cacheBuilderSpec, long j) {
            Preconditions.checkArgument(cacheBuilderSpec.maximumSize == null, "maximum size was already set to ", cacheBuilderSpec.maximumSize);
            Preconditions.checkArgument(cacheBuilderSpec.maximumWeight == null, "maximum weight was already set to ", cacheBuilderSpec.maximumWeight);
            cacheBuilderSpec.maximumSize = Long.valueOf(j);
        }
    }

    static class MaximumWeightParser extends LongParser {
        MaximumWeightParser() {
        }

        protected void parseLong(CacheBuilderSpec cacheBuilderSpec, long j) {
            Preconditions.checkArgument(cacheBuilderSpec.maximumWeight == null, "maximum weight was already set to ", cacheBuilderSpec.maximumWeight);
            Preconditions.checkArgument(cacheBuilderSpec.maximumSize == null, "maximum size was already set to ", cacheBuilderSpec.maximumSize);
            cacheBuilderSpec.maximumWeight = Long.valueOf(j);
        }
    }

    static class RecordStatsParser implements ValueParser {
        RecordStatsParser() {
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2) {
            str = null;
            Preconditions.checkArgument(str2 == null ? true : null, "recordStats does not take values");
            if (cacheBuilderSpec.recordStats == null) {
                str = true;
            }
            Preconditions.checkArgument(str, "recordStats already set");
            cacheBuilderSpec.recordStats = Boolean.valueOf(true);
        }
    }

    static class RefreshDurationParser extends DurationParser {
        RefreshDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit) {
            Preconditions.checkArgument(cacheBuilderSpec.refreshTimeUnit == null, "refreshAfterWrite already set");
            cacheBuilderSpec.refreshDuration = j;
            cacheBuilderSpec.refreshTimeUnit = timeUnit;
        }
    }

    static class ValueStrengthParser implements ValueParser {
        private final Strength strength;

        public ValueStrengthParser(Strength strength) {
            this.strength = strength;
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2) {
            Preconditions.checkArgument(str2 == null ? true : null, "key %s does not take values", str);
            Preconditions.checkArgument(cacheBuilderSpec.valueStrength == null ? true : null, "%s was already set to %s", str, cacheBuilderSpec.valueStrength);
            cacheBuilderSpec.valueStrength = this.strength;
        }
    }

    static class WriteDurationParser extends DurationParser {
        WriteDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit) {
            Preconditions.checkArgument(cacheBuilderSpec.writeExpirationTimeUnit == null, "expireAfterWrite already set");
            cacheBuilderSpec.writeExpirationDuration = j;
            cacheBuilderSpec.writeExpirationTimeUnit = timeUnit;
        }
    }

    private CacheBuilderSpec(String str) {
        this.specification = str;
    }

    public static CacheBuilderSpec parse(String str) {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec(str);
        if (!str.isEmpty()) {
            for (String split : KEYS_SPLITTER.split(str)) {
                List copyOf = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(split));
                Preconditions.checkArgument(copyOf.isEmpty() ^ true, "blank key-value pair");
                Preconditions.checkArgument(copyOf.size() <= 2, "key-value pair %s with more than one equals sign", (String) str.next());
                String split2 = (String) copyOf.get(0);
                ValueParser valueParser = (ValueParser) VALUE_PARSERS.get(split2);
                Preconditions.checkArgument(valueParser != null, "unknown key %s", split2);
                valueParser.parse(cacheBuilderSpec, split2, copyOf.size() == 1 ? null : (String) copyOf.get(1));
            }
        }
        return cacheBuilderSpec;
    }

    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }

    CacheBuilder<Object, Object> toCacheBuilder() {
        CacheBuilder<Object, Object> newBuilder = CacheBuilder.newBuilder();
        if (this.initialCapacity != null) {
            newBuilder.initialCapacity(this.initialCapacity.intValue());
        }
        if (this.maximumSize != null) {
            newBuilder.maximumSize(this.maximumSize.longValue());
        }
        if (this.maximumWeight != null) {
            newBuilder.maximumWeight(this.maximumWeight.longValue());
        }
        if (this.concurrencyLevel != null) {
            newBuilder.concurrencyLevel(this.concurrencyLevel.intValue());
        }
        if (this.keyStrength != null) {
            if (C08311.$SwitchMap$com$google$common$cache$LocalCache$Strength[this.keyStrength.ordinal()] != 1) {
                throw new AssertionError();
            }
            newBuilder.weakKeys();
        }
        if (this.valueStrength != null) {
            switch (this.valueStrength) {
                case WEAK:
                    newBuilder.weakValues();
                    break;
                case SOFT:
                    newBuilder.softValues();
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (this.recordStats != null && this.recordStats.booleanValue()) {
            newBuilder.recordStats();
        }
        if (this.writeExpirationTimeUnit != null) {
            newBuilder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
        }
        if (this.accessExpirationTimeUnit != null) {
            newBuilder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
        }
        if (this.refreshTimeUnit != null) {
            newBuilder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
        }
        return newBuilder;
    }

    public String toParsableString() {
        return this.specification;
    }

    public String toString() {
        return MoreObjects.toStringHelper((Object) this).addValue(toParsableString()).toString();
    }

    public int hashCode() {
        return Objects.hashCode(this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit));
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheBuilderSpec)) {
            return false;
        }
        CacheBuilderSpec cacheBuilderSpec = (CacheBuilderSpec) obj;
        if (!Objects.equal(this.initialCapacity, cacheBuilderSpec.initialCapacity) || !Objects.equal(this.maximumSize, cacheBuilderSpec.maximumSize) || !Objects.equal(this.maximumWeight, cacheBuilderSpec.maximumWeight) || !Objects.equal(this.concurrencyLevel, cacheBuilderSpec.concurrencyLevel) || !Objects.equal(this.keyStrength, cacheBuilderSpec.keyStrength) || !Objects.equal(this.valueStrength, cacheBuilderSpec.valueStrength) || !Objects.equal(this.recordStats, cacheBuilderSpec.recordStats) || !Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(cacheBuilderSpec.writeExpirationDuration, cacheBuilderSpec.writeExpirationTimeUnit)) || !Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(cacheBuilderSpec.accessExpirationDuration, cacheBuilderSpec.accessExpirationTimeUnit)) || Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(cacheBuilderSpec.refreshDuration, cacheBuilderSpec.refreshTimeUnit)) == null) {
            z = false;
        }
        return z;
    }

    @Nullable
    private static Long durationInNanos(long j, @Nullable TimeUnit timeUnit) {
        return timeUnit == null ? 0 : Long.valueOf(timeUnit.toNanos(j));
    }
}
