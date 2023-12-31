package com.google.android.exoplayer2.source.ads;

import android.net.Uri;
import android.support.annotation.CheckResult;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.util.Assertions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class AdPlaybackState {
    public static final int AD_STATE_AVAILABLE = 1;
    public static final int AD_STATE_ERROR = 4;
    public static final int AD_STATE_PLAYED = 3;
    public static final int AD_STATE_SKIPPED = 2;
    public static final int AD_STATE_UNAVAILABLE = 0;
    public static final AdPlaybackState NONE = new AdPlaybackState(new long[0]);
    public final int adGroupCount;
    public final long[] adGroupTimesUs;
    public final AdGroup[] adGroups;
    public final long adResumePositionUs;
    public final long contentDurationUs;

    public static final class AdGroup {
        public final int count;
        public final long[] durationsUs;
        public final int[] states;
        public final Uri[] uris;

        public AdGroup() {
            this(-1, new int[0], new Uri[0], new long[0]);
        }

        private AdGroup(int i, int[] iArr, Uri[] uriArr, long[] jArr) {
            Assertions.checkArgument(iArr.length == uriArr.length);
            this.count = i;
            this.states = iArr;
            this.uris = uriArr;
            this.durationsUs = jArr;
        }

        public int getFirstAdIndexToPlay() {
            return getNextAdIndexToPlay(-1);
        }

        public int getNextAdIndexToPlay(int i) {
            i++;
            while (i < this.states.length && this.states[i] != 0) {
                if (this.states[i] == 1) {
                    break;
                }
                i++;
            }
            return i;
        }

        public boolean hasUnplayedAds() {
            if (this.count != -1) {
                if (getFirstAdIndexToPlay() >= this.count) {
                    return false;
                }
            }
            return true;
        }

        @CheckResult
        public AdGroup withAdCount(int i) {
            boolean z = this.count == -1 && this.states.length <= i;
            Assertions.checkArgument(z);
            return new AdGroup(i, copyStatesWithSpaceForAdCount(this.states, i), (Uri[]) Arrays.copyOf(this.uris, i), copyDurationsUsWithSpaceForAdCount(this.durationsUs, i));
        }

        @CheckResult
        public AdGroup withAdUri(Uri uri, int i) {
            boolean z;
            int[] copyStatesWithSpaceForAdCount;
            long[] jArr;
            Uri[] uriArr;
            boolean z2 = false;
            if (this.count != -1) {
                if (i >= this.count) {
                    z = false;
                    Assertions.checkArgument(z);
                    copyStatesWithSpaceForAdCount = copyStatesWithSpaceForAdCount(this.states, i + 1);
                    if (copyStatesWithSpaceForAdCount[i] == 0) {
                        z2 = true;
                    }
                    Assertions.checkArgument(z2);
                    if (this.durationsUs.length != copyStatesWithSpaceForAdCount.length) {
                        jArr = this.durationsUs;
                    } else {
                        jArr = copyDurationsUsWithSpaceForAdCount(this.durationsUs, copyStatesWithSpaceForAdCount.length);
                    }
                    uriArr = (Uri[]) Arrays.copyOf(this.uris, copyStatesWithSpaceForAdCount.length);
                    uriArr[i] = uri;
                    copyStatesWithSpaceForAdCount[i] = 1;
                    return new AdGroup(this.count, copyStatesWithSpaceForAdCount, uriArr, jArr);
                }
            }
            z = true;
            Assertions.checkArgument(z);
            copyStatesWithSpaceForAdCount = copyStatesWithSpaceForAdCount(this.states, i + 1);
            if (copyStatesWithSpaceForAdCount[i] == 0) {
                z2 = true;
            }
            Assertions.checkArgument(z2);
            if (this.durationsUs.length != copyStatesWithSpaceForAdCount.length) {
                jArr = copyDurationsUsWithSpaceForAdCount(this.durationsUs, copyStatesWithSpaceForAdCount.length);
            } else {
                jArr = this.durationsUs;
            }
            uriArr = (Uri[]) Arrays.copyOf(this.uris, copyStatesWithSpaceForAdCount.length);
            uriArr[i] = uri;
            copyStatesWithSpaceForAdCount[i] = 1;
            return new AdGroup(this.count, copyStatesWithSpaceForAdCount, uriArr, jArr);
        }

        @CheckResult
        public AdGroup withAdState(int i, int i2) {
            boolean z;
            int[] copyStatesWithSpaceForAdCount;
            long[] jArr;
            Uri[] uriArr;
            boolean z2 = false;
            if (this.count != -1) {
                if (i2 >= this.count) {
                    z = false;
                    Assertions.checkArgument(z);
                    copyStatesWithSpaceForAdCount = copyStatesWithSpaceForAdCount(this.states, i2 + 1);
                    if (copyStatesWithSpaceForAdCount[i2] == 0 || copyStatesWithSpaceForAdCount[i2] == 1 || copyStatesWithSpaceForAdCount[i2] == i) {
                        z2 = true;
                    }
                    Assertions.checkArgument(z2);
                    if (this.durationsUs.length != copyStatesWithSpaceForAdCount.length) {
                        jArr = this.durationsUs;
                    } else {
                        jArr = copyDurationsUsWithSpaceForAdCount(this.durationsUs, copyStatesWithSpaceForAdCount.length);
                    }
                    if (this.uris.length != copyStatesWithSpaceForAdCount.length) {
                        uriArr = this.uris;
                    } else {
                        uriArr = (Uri[]) Arrays.copyOf(this.uris, copyStatesWithSpaceForAdCount.length);
                    }
                    copyStatesWithSpaceForAdCount[i2] = i;
                    return new AdGroup(this.count, copyStatesWithSpaceForAdCount, uriArr, jArr);
                }
            }
            z = true;
            Assertions.checkArgument(z);
            copyStatesWithSpaceForAdCount = copyStatesWithSpaceForAdCount(this.states, i2 + 1);
            z2 = true;
            Assertions.checkArgument(z2);
            if (this.durationsUs.length != copyStatesWithSpaceForAdCount.length) {
                jArr = copyDurationsUsWithSpaceForAdCount(this.durationsUs, copyStatesWithSpaceForAdCount.length);
            } else {
                jArr = this.durationsUs;
            }
            if (this.uris.length != copyStatesWithSpaceForAdCount.length) {
                uriArr = (Uri[]) Arrays.copyOf(this.uris, copyStatesWithSpaceForAdCount.length);
            } else {
                uriArr = this.uris;
            }
            copyStatesWithSpaceForAdCount[i2] = i;
            return new AdGroup(this.count, copyStatesWithSpaceForAdCount, uriArr, jArr);
        }

        @CheckResult
        public AdGroup withAdDurationsUs(long[] jArr) {
            boolean z;
            if (this.count != -1) {
                if (jArr.length > this.uris.length) {
                    z = false;
                    Assertions.checkArgument(z);
                    if (jArr.length < this.uris.length) {
                        jArr = copyDurationsUsWithSpaceForAdCount(jArr, this.uris.length);
                    }
                    return new AdGroup(this.count, this.states, this.uris, jArr);
                }
            }
            z = true;
            Assertions.checkArgument(z);
            if (jArr.length < this.uris.length) {
                jArr = copyDurationsUsWithSpaceForAdCount(jArr, this.uris.length);
            }
            return new AdGroup(this.count, this.states, this.uris, jArr);
        }

        @CheckResult
        public AdGroup withAllAdsSkipped() {
            int i = 0;
            if (this.count == -1) {
                return new AdGroup(0, new int[0], new Uri[0], new long[0]);
            }
            int length = this.states.length;
            int[] copyOf = Arrays.copyOf(this.states, length);
            while (i < length) {
                if (copyOf[i] == 1 || copyOf[i] == 0) {
                    copyOf[i] = 2;
                }
                i++;
            }
            return new AdGroup(length, copyOf, this.uris, this.durationsUs);
        }

        @CheckResult
        private static int[] copyStatesWithSpaceForAdCount(int[] iArr, int i) {
            int length = iArr.length;
            i = Math.max(i, length);
            iArr = Arrays.copyOf(iArr, i);
            Arrays.fill(iArr, length, i, 0);
            return iArr;
        }

        @CheckResult
        private static long[] copyDurationsUsWithSpaceForAdCount(long[] jArr, int i) {
            int length = jArr.length;
            i = Math.max(i, length);
            jArr = Arrays.copyOf(jArr, i);
            Arrays.fill(jArr, length, i, C0649C.TIME_UNSET);
            return jArr;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AdState {
    }

    public AdPlaybackState(long... jArr) {
        int length = jArr.length;
        this.adGroupCount = length;
        this.adGroupTimesUs = Arrays.copyOf(jArr, length);
        this.adGroups = new AdGroup[length];
        for (jArr = null; jArr < length; jArr++) {
            this.adGroups[jArr] = new AdGroup();
        }
        this.adResumePositionUs = 0;
        this.contentDurationUs = C0649C.TIME_UNSET;
    }

    private AdPlaybackState(long[] jArr, AdGroup[] adGroupArr, long j, long j2) {
        this.adGroupCount = adGroupArr.length;
        this.adGroupTimesUs = jArr;
        this.adGroups = adGroupArr;
        this.adResumePositionUs = j;
        this.contentDurationUs = j2;
    }

    public int getAdGroupIndexForPositionUs(long j) {
        int length = this.adGroupTimesUs.length - 1;
        while (length >= 0 && (this.adGroupTimesUs[length] == Long.MIN_VALUE || this.adGroupTimesUs[length] > j)) {
            length--;
        }
        return (length < 0 || this.adGroups[length].hasUnplayedAds() == null) ? -1 : length;
    }

    public int getAdGroupIndexAfterPositionUs(long j) {
        int i = 0;
        while (i < this.adGroupTimesUs.length && this.adGroupTimesUs[i] != Long.MIN_VALUE && (j >= this.adGroupTimesUs[i] || !this.adGroups[i].hasUnplayedAds())) {
            i++;
        }
        return i < this.adGroupTimesUs.length ? i : -1;
    }

    @CheckResult
    public AdPlaybackState withAdCount(int i, int i2) {
        Assertions.checkArgument(i2 > 0);
        if (this.adGroups[i].count == i2) {
            return this;
        }
        AdGroup[] adGroupArr = (AdGroup[]) Arrays.copyOf(this.adGroups, this.adGroups.length);
        adGroupArr[i] = this.adGroups[i].withAdCount(i2);
        return new AdPlaybackState(this.adGroupTimesUs, adGroupArr, this.adResumePositionUs, this.contentDurationUs);
    }

    @CheckResult
    public AdPlaybackState withAdUri(int i, int i2, Uri uri) {
        AdGroup[] adGroupArr = (AdGroup[]) Arrays.copyOf(this.adGroups, this.adGroups.length);
        adGroupArr[i] = adGroupArr[i].withAdUri(uri, i2);
        return new AdPlaybackState(this.adGroupTimesUs, adGroupArr, this.adResumePositionUs, this.contentDurationUs);
    }

    @CheckResult
    public AdPlaybackState withPlayedAd(int i, int i2) {
        AdGroup[] adGroupArr = (AdGroup[]) Arrays.copyOf(this.adGroups, this.adGroups.length);
        adGroupArr[i] = adGroupArr[i].withAdState(3, i2);
        return new AdPlaybackState(this.adGroupTimesUs, adGroupArr, this.adResumePositionUs, this.contentDurationUs);
    }

    @CheckResult
    public AdPlaybackState withAdLoadError(int i, int i2) {
        AdGroup[] adGroupArr = (AdGroup[]) Arrays.copyOf(this.adGroups, this.adGroups.length);
        adGroupArr[i] = adGroupArr[i].withAdState(4, i2);
        return new AdPlaybackState(this.adGroupTimesUs, adGroupArr, this.adResumePositionUs, this.contentDurationUs);
    }

    @CheckResult
    public AdPlaybackState withSkippedAdGroup(int i) {
        AdGroup[] adGroupArr = (AdGroup[]) Arrays.copyOf(this.adGroups, this.adGroups.length);
        adGroupArr[i] = adGroupArr[i].withAllAdsSkipped();
        return new AdPlaybackState(this.adGroupTimesUs, adGroupArr, this.adResumePositionUs, this.contentDurationUs);
    }

    @CheckResult
    public AdPlaybackState withAdDurationsUs(long[][] jArr) {
        AdGroup[] adGroupArr = (AdGroup[]) Arrays.copyOf(this.adGroups, this.adGroups.length);
        for (int i = 0; i < this.adGroupCount; i++) {
            adGroupArr[i] = adGroupArr[i].withAdDurationsUs(jArr[i]);
        }
        return new AdPlaybackState(this.adGroupTimesUs, adGroupArr, this.adResumePositionUs, this.contentDurationUs);
    }

    @CheckResult
    public AdPlaybackState withAdResumePositionUs(long j) {
        if (this.adResumePositionUs == j) {
            return this;
        }
        return new AdPlaybackState(this.adGroupTimesUs, this.adGroups, j, this.contentDurationUs);
    }

    @CheckResult
    public AdPlaybackState withContentDurationUs(long j) {
        if (this.contentDurationUs == j) {
            return this;
        }
        return new AdPlaybackState(this.adGroupTimesUs, this.adGroups, this.adResumePositionUs, j);
    }
}
