package com.google.android.exoplayer2;

import android.support.annotation.Nullable;
import android.util.Pair;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;

final class MediaPeriodQueue {
    private static final int MAXIMUM_BUFFER_AHEAD_PERIODS = 100;
    private int length;
    private MediaPeriodHolder loading;
    private long nextWindowSequenceNumber;
    private final Period period = new Period();
    private MediaPeriodHolder playing;
    private MediaPeriodHolder reading;
    private int repeatMode;
    private boolean shuffleModeEnabled;
    private Timeline timeline;
    private final Window window = new Window();

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public boolean updateRepeatMode(int i) {
        this.repeatMode = i;
        return updateForPlaybackModeChange();
    }

    public boolean updateShuffleModeEnabled(boolean z) {
        this.shuffleModeEnabled = z;
        return updateForPlaybackModeChange();
    }

    public boolean isLoading(MediaPeriod mediaPeriod) {
        return (this.loading == null || this.loading.mediaPeriod != mediaPeriod) ? null : true;
    }

    public void reevaluateBuffer(long j) {
        if (this.loading != null) {
            this.loading.reevaluateBuffer(j);
        }
    }

    public boolean shouldLoadNextMediaPeriod() {
        if (this.loading != null) {
            if (this.loading.info.isFinal || !this.loading.isFullyBuffered() || this.loading.info.durationUs == C0649C.TIME_UNSET || this.length >= 100) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    public MediaPeriodInfo getNextMediaPeriodInfo(long j, PlaybackInfo playbackInfo) {
        if (this.loading == null) {
            return getFirstMediaPeriodInfo(playbackInfo);
        }
        return getFollowingMediaPeriodInfo(this.loading, j);
    }

    public MediaPeriod enqueueNextMediaPeriod(RendererCapabilities[] rendererCapabilitiesArr, long j, TrackSelector trackSelector, Allocator allocator, MediaSource mediaSource, Object obj, MediaPeriodInfo mediaPeriodInfo) {
        MediaPeriodInfo mediaPeriodInfo2;
        long j2;
        if (this.loading == null) {
            mediaPeriodInfo2 = mediaPeriodInfo;
            j2 = mediaPeriodInfo2.startPositionUs + j;
        } else {
            mediaPeriodInfo2 = mediaPeriodInfo;
            j2 = r0.loading.getRendererOffset() + r0.loading.info.durationUs;
        }
        MediaPeriodHolder mediaPeriodHolder = new MediaPeriodHolder(rendererCapabilitiesArr, j2, trackSelector, allocator, mediaSource, obj, mediaPeriodInfo2);
        if (r0.loading != null) {
            Assertions.checkState(hasPlayingPeriod());
            r0.loading.next = mediaPeriodHolder;
        }
        r0.loading = mediaPeriodHolder;
        r0.length++;
        return mediaPeriodHolder.mediaPeriod;
    }

    public TrackSelectorResult handleLoadingPeriodPrepared(float f) throws ExoPlaybackException {
        return this.loading.handlePrepared(f);
    }

    public MediaPeriodHolder getLoadingPeriod() {
        return this.loading;
    }

    public MediaPeriodHolder getPlayingPeriod() {
        return this.playing;
    }

    public MediaPeriodHolder getReadingPeriod() {
        return this.reading;
    }

    public MediaPeriodHolder getFrontPeriod() {
        return hasPlayingPeriod() ? this.playing : this.loading;
    }

    public boolean hasPlayingPeriod() {
        return this.playing != null;
    }

    public MediaPeriodHolder advanceReadingPeriod() {
        boolean z = (this.reading == null || this.reading.next == null) ? false : true;
        Assertions.checkState(z);
        this.reading = this.reading.next;
        return this.reading;
    }

    public MediaPeriodHolder advancePlayingPeriod() {
        if (this.playing != null) {
            if (this.playing == this.reading) {
                this.reading = this.playing.next;
            }
            this.playing.release();
            this.playing = this.playing.next;
            this.length--;
            if (this.length == 0) {
                this.loading = null;
            }
        } else {
            this.playing = this.loading;
            this.reading = this.loading;
        }
        return this.playing;
    }

    public boolean removeAfter(MediaPeriodHolder mediaPeriodHolder) {
        boolean z = false;
        Assertions.checkState(mediaPeriodHolder != null);
        this.loading = mediaPeriodHolder;
        while (mediaPeriodHolder.next != null) {
            mediaPeriodHolder = mediaPeriodHolder.next;
            if (mediaPeriodHolder == this.reading) {
                this.reading = this.playing;
                z = true;
            }
            mediaPeriodHolder.release();
            this.length--;
        }
        this.loading.next = null;
        return z;
    }

    public void clear() {
        MediaPeriodHolder frontPeriod = getFrontPeriod();
        if (frontPeriod != null) {
            frontPeriod.release();
            removeAfter(frontPeriod);
        }
        this.playing = null;
        this.loading = null;
        this.reading = null;
        this.length = 0;
    }

    public boolean updateQueuedPeriods(MediaPeriodId mediaPeriodId, long j) {
        int i = mediaPeriodId.periodIndex;
        mediaPeriodId = null;
        MediaPeriodHolder frontPeriod = getFrontPeriod();
        while (frontPeriod != null) {
            if (mediaPeriodId == null) {
                frontPeriod.info = getUpdatedMediaPeriodInfo(frontPeriod.info, i);
            } else {
                if (i != -1) {
                    if (frontPeriod.uid.equals(this.timeline.getPeriod(i, this.period, true).uid)) {
                        MediaPeriodInfo followingMediaPeriodInfo = getFollowingMediaPeriodInfo(mediaPeriodId, j);
                        if (followingMediaPeriodInfo == null) {
                            return removeAfter(mediaPeriodId) ^ 1;
                        }
                        frontPeriod.info = getUpdatedMediaPeriodInfo(frontPeriod.info, i);
                        if (!canKeepMediaPeriodHolder(frontPeriod, followingMediaPeriodInfo)) {
                            return removeAfter(mediaPeriodId) ^ 1;
                        }
                    }
                }
                return removeAfter(mediaPeriodId) ^ 1;
            }
            if (frontPeriod.info.isLastInTimelinePeriod != null) {
                i = this.timeline.getNextPeriodIndex(i, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            }
            MediaPeriodHolder mediaPeriodHolder = frontPeriod;
            Object obj = frontPeriod.next;
            Object obj2 = mediaPeriodHolder;
        }
        return true;
    }

    public MediaPeriodInfo getUpdatedMediaPeriodInfo(MediaPeriodInfo mediaPeriodInfo, int i) {
        return getUpdatedMediaPeriodInfo(mediaPeriodInfo, mediaPeriodInfo.id.copyWithPeriodIndex(i));
    }

    public MediaPeriodId resolveMediaPeriodIdForAds(int i, long j) {
        return resolveMediaPeriodIdForAds(i, j, resolvePeriodIndexToWindowSequenceNumber(i));
    }

    private MediaPeriodId resolveMediaPeriodIdForAds(int i, long j, long j2) {
        this.timeline.getPeriod(i, this.period);
        int adGroupIndexForPositionUs = this.period.getAdGroupIndexForPositionUs(j);
        if (adGroupIndexForPositionUs == -1) {
            return new MediaPeriodId(i, j2);
        }
        return new MediaPeriodId(i, adGroupIndexForPositionUs, this.period.getFirstAdIndexToPlay(adGroupIndexForPositionUs), j2);
    }

    private long resolvePeriodIndexToWindowSequenceNumber(int i) {
        MediaPeriodHolder frontPeriod;
        i = this.timeline.getPeriod(i, this.period, true).uid;
        for (frontPeriod = getFrontPeriod(); frontPeriod != null; frontPeriod = frontPeriod.next) {
            if (frontPeriod.uid.equals(i)) {
                return frontPeriod.info.id.windowSequenceNumber;
            }
        }
        i = this.period.windowIndex;
        for (frontPeriod = getFrontPeriod(); frontPeriod != null; frontPeriod = frontPeriod.next) {
            int indexOfPeriod = this.timeline.getIndexOfPeriod(frontPeriod.uid);
            if (indexOfPeriod != -1 && this.timeline.getPeriod(indexOfPeriod, this.period).windowIndex == i) {
                return frontPeriod.info.id.windowSequenceNumber;
            }
        }
        long j = this.nextWindowSequenceNumber;
        this.nextWindowSequenceNumber = j + 1;
        return j;
    }

    private boolean canKeepMediaPeriodHolder(MediaPeriodHolder mediaPeriodHolder, MediaPeriodInfo mediaPeriodInfo) {
        mediaPeriodHolder = mediaPeriodHolder.info;
        return (mediaPeriodHolder.startPositionUs == mediaPeriodInfo.startPositionUs && mediaPeriodHolder.endPositionUs == mediaPeriodInfo.endPositionUs && mediaPeriodHolder.id.equals(mediaPeriodInfo.id) != null) ? true : null;
    }

    private boolean updateForPlaybackModeChange() {
        MediaPeriodHolder frontPeriod = getFrontPeriod();
        boolean z = true;
        if (frontPeriod == null) {
            return true;
        }
        while (true) {
            int nextPeriodIndex = this.timeline.getNextPeriodIndex(frontPeriod.info.id.periodIndex, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            while (frontPeriod.next != null && !frontPeriod.info.isLastInTimelinePeriod) {
                frontPeriod = frontPeriod.next;
            }
            if (nextPeriodIndex == -1 || frontPeriod.next == null) {
                break;
            } else if (frontPeriod.next.info.id.periodIndex != nextPeriodIndex) {
                break;
            } else {
                frontPeriod = frontPeriod.next;
            }
        }
        boolean removeAfter = removeAfter(frontPeriod);
        frontPeriod.info = getUpdatedMediaPeriodInfo(frontPeriod.info, frontPeriod.info.id);
        if (removeAfter) {
            if (hasPlayingPeriod()) {
                z = false;
            }
        }
        return z;
    }

    private MediaPeriodInfo getFirstMediaPeriodInfo(PlaybackInfo playbackInfo) {
        return getMediaPeriodInfo(playbackInfo.periodId, playbackInfo.contentPositionUs, playbackInfo.startPositionUs);
    }

    @Nullable
    private MediaPeriodInfo getFollowingMediaPeriodInfo(MediaPeriodHolder mediaPeriodHolder, long j) {
        MediaPeriodQueue mediaPeriodQueue = this;
        MediaPeriodHolder mediaPeriodHolder2 = mediaPeriodHolder;
        MediaPeriodInfo mediaPeriodInfo = mediaPeriodHolder2.info;
        MediaPeriodInfo mediaPeriodInfo2 = null;
        int nextPeriodIndex;
        long longValue;
        int i;
        if (mediaPeriodInfo.isLastInTimelinePeriod) {
            nextPeriodIndex = mediaPeriodQueue.timeline.getNextPeriodIndex(mediaPeriodInfo.id.periodIndex, mediaPeriodQueue.period, mediaPeriodQueue.window, mediaPeriodQueue.repeatMode, mediaPeriodQueue.shuffleModeEnabled);
            if (nextPeriodIndex == -1) {
                return null;
            }
            int i2 = mediaPeriodQueue.timeline.getPeriod(nextPeriodIndex, mediaPeriodQueue.period, true).windowIndex;
            Object obj = mediaPeriodQueue.period.uid;
            long j2 = mediaPeriodInfo.id.windowSequenceNumber;
            long j3 = 0;
            if (mediaPeriodQueue.timeline.getWindow(i2, mediaPeriodQueue.window).firstPeriodIndex == nextPeriodIndex) {
                j2 = (mediaPeriodHolder.getRendererOffset() + mediaPeriodInfo.durationUs) - j;
                Timeline timeline = mediaPeriodQueue.timeline;
                Pair periodPosition = timeline.getPeriodPosition(mediaPeriodQueue.window, mediaPeriodQueue.period, i2, C0649C.TIME_UNSET, Math.max(0, j2));
                if (periodPosition == null) {
                    return null;
                }
                long j4;
                nextPeriodIndex = ((Integer) periodPosition.first).intValue();
                longValue = ((Long) periodPosition.second).longValue();
                if (mediaPeriodHolder2.next == null || !mediaPeriodHolder2.next.uid.equals(obj)) {
                    j4 = mediaPeriodQueue.nextWindowSequenceNumber;
                    mediaPeriodQueue.nextWindowSequenceNumber = j4 + 1;
                } else {
                    j4 = mediaPeriodHolder2.next.info.id.windowSequenceNumber;
                }
                j3 = longValue;
                longValue = j4;
                i = nextPeriodIndex;
            } else {
                i = nextPeriodIndex;
                longValue = j2;
            }
            long j5 = j3;
            return getMediaPeriodInfo(resolveMediaPeriodIdForAds(i, j5, longValue), j5, j3);
        }
        MediaPeriodId mediaPeriodId = mediaPeriodInfo.id;
        mediaPeriodQueue.timeline.getPeriod(mediaPeriodId.periodIndex, mediaPeriodQueue.period);
        int adCountInAdGroup;
        if (mediaPeriodId.isAd()) {
            nextPeriodIndex = mediaPeriodId.adGroupIndex;
            adCountInAdGroup = mediaPeriodQueue.period.getAdCountInAdGroup(nextPeriodIndex);
            if (adCountInAdGroup == -1) {
                return null;
            }
            int nextAdIndexToPlay = mediaPeriodQueue.period.getNextAdIndexToPlay(nextPeriodIndex, mediaPeriodId.adIndexInAdGroup);
            if (nextAdIndexToPlay < adCountInAdGroup) {
                if (mediaPeriodQueue.period.isAdAvailable(nextPeriodIndex, nextAdIndexToPlay)) {
                    mediaPeriodInfo2 = getMediaPeriodInfoForAd(mediaPeriodId.periodIndex, nextPeriodIndex, nextAdIndexToPlay, mediaPeriodInfo.contentPositionUs, mediaPeriodId.windowSequenceNumber);
                }
                return mediaPeriodInfo2;
            }
            return getMediaPeriodInfoForContent(mediaPeriodId.periodIndex, mediaPeriodInfo.contentPositionUs, mediaPeriodId.windowSequenceNumber);
        } else if (mediaPeriodInfo.endPositionUs != Long.MIN_VALUE) {
            nextPeriodIndex = mediaPeriodQueue.period.getAdGroupIndexForPositionUs(mediaPeriodInfo.endPositionUs);
            if (nextPeriodIndex == -1) {
                return getMediaPeriodInfoForContent(mediaPeriodId.periodIndex, mediaPeriodInfo.endPositionUs, mediaPeriodId.windowSequenceNumber);
            }
            adCountInAdGroup = mediaPeriodQueue.period.getFirstAdIndexToPlay(nextPeriodIndex);
            if (mediaPeriodQueue.period.isAdAvailable(nextPeriodIndex, adCountInAdGroup)) {
                mediaPeriodInfo2 = getMediaPeriodInfoForAd(mediaPeriodId.periodIndex, nextPeriodIndex, adCountInAdGroup, mediaPeriodInfo.endPositionUs, mediaPeriodId.windowSequenceNumber);
            }
            return mediaPeriodInfo2;
        } else {
            i = mediaPeriodQueue.period.getAdGroupCount();
            if (i == 0) {
                return null;
            }
            nextPeriodIndex = i - 1;
            if (mediaPeriodQueue.period.getAdGroupTimeUs(nextPeriodIndex) == Long.MIN_VALUE) {
                if (!mediaPeriodQueue.period.hasPlayedAdGroup(nextPeriodIndex)) {
                    adCountInAdGroup = mediaPeriodQueue.period.getFirstAdIndexToPlay(nextPeriodIndex);
                    if (!mediaPeriodQueue.period.isAdAvailable(nextPeriodIndex, adCountInAdGroup)) {
                        return null;
                    }
                    longValue = mediaPeriodQueue.period.getDurationUs();
                    return getMediaPeriodInfoForAd(mediaPeriodId.periodIndex, nextPeriodIndex, adCountInAdGroup, longValue, mediaPeriodId.windowSequenceNumber);
                }
            }
            return null;
        }
    }

    private MediaPeriodInfo getUpdatedMediaPeriodInfo(MediaPeriodInfo mediaPeriodInfo, MediaPeriodId mediaPeriodId) {
        long adDurationUs;
        long j;
        long j2 = mediaPeriodInfo.startPositionUs;
        long j3 = mediaPeriodInfo.endPositionUs;
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId, j3);
        boolean isLastInTimeline = isLastInTimeline(mediaPeriodId, isLastInPeriod);
        this.timeline.getPeriod(mediaPeriodId.periodIndex, this.period);
        if (mediaPeriodId.isAd()) {
            adDurationUs = this.period.getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup);
        } else if (j3 == Long.MIN_VALUE) {
            adDurationUs = this.period.getDurationUs();
        } else {
            j = j3;
            return new MediaPeriodInfo(mediaPeriodId, j2, j3, mediaPeriodInfo.contentPositionUs, j, isLastInPeriod, isLastInTimeline);
        }
        j = adDurationUs;
        return new MediaPeriodInfo(mediaPeriodId, j2, j3, mediaPeriodInfo.contentPositionUs, j, isLastInPeriod, isLastInTimeline);
    }

    private MediaPeriodInfo getMediaPeriodInfo(MediaPeriodId mediaPeriodId, long j, long j2) {
        this.timeline.getPeriod(mediaPeriodId.periodIndex, this.period);
        if (!mediaPeriodId.isAd()) {
            return getMediaPeriodInfoForContent(mediaPeriodId.periodIndex, j2, mediaPeriodId.windowSequenceNumber);
        } else if (this.period.isAdAvailable(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup) == null) {
            return null;
        } else {
            return getMediaPeriodInfoForAd(mediaPeriodId.periodIndex, mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup, j, mediaPeriodId.windowSequenceNumber);
        }
    }

    private MediaPeriodInfo getMediaPeriodInfoForAd(int i, int i2, int i3, long j, long j2) {
        MediaPeriodId mediaPeriodId = new MediaPeriodId(i, i2, i3, j2);
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId, Long.MIN_VALUE);
        boolean isLastInTimeline = isLastInTimeline(mediaPeriodId, isLastInPeriod);
        return new MediaPeriodInfo(mediaPeriodId, i3 == this.period.getFirstAdIndexToPlay(i2) ? r0.period.getAdResumePositionUs() : 0, Long.MIN_VALUE, j, this.timeline.getPeriod(mediaPeriodId.periodIndex, this.period).getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup), isLastInPeriod, isLastInTimeline);
    }

    private MediaPeriodInfo getMediaPeriodInfoForContent(int i, long j, long j2) {
        long j3;
        MediaPeriodId mediaPeriodId = new MediaPeriodId(i, j2);
        this.timeline.getPeriod(mediaPeriodId.periodIndex, this.period);
        long j4 = j;
        int adGroupIndexAfterPositionUs = this.period.getAdGroupIndexAfterPositionUs(j4);
        if (adGroupIndexAfterPositionUs == -1) {
            j3 = Long.MIN_VALUE;
        } else {
            j3 = r0.period.getAdGroupTimeUs(adGroupIndexAfterPositionUs);
        }
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId, j3);
        return new MediaPeriodInfo(mediaPeriodId, j4, j3, C0649C.TIME_UNSET, j3 == Long.MIN_VALUE ? r0.period.getDurationUs() : j3, isLastInPeriod, isLastInTimeline(mediaPeriodId, isLastInPeriod));
    }

    private boolean isLastInPeriod(MediaPeriodId mediaPeriodId, long j) {
        int adGroupCount = this.timeline.getPeriod(mediaPeriodId.periodIndex, this.period).getAdGroupCount();
        boolean z = true;
        if (adGroupCount == 0) {
            return true;
        }
        adGroupCount--;
        boolean isAd = mediaPeriodId.isAd();
        if (this.period.getAdGroupTimeUs(adGroupCount) != Long.MIN_VALUE) {
            if (isAd || j != Long.MIN_VALUE) {
                z = false;
            }
            return z;
        }
        j = this.period.getAdCountInAdGroup(adGroupCount);
        if (j == -1) {
            return false;
        }
        mediaPeriodId = (isAd && mediaPeriodId.adGroupIndex == adGroupCount && mediaPeriodId.adIndexInAdGroup == j - 1) ? true : null;
        if (mediaPeriodId == null) {
            if (isAd || this.period.getFirstAdIndexToPlay(adGroupCount) != j) {
                z = false;
            }
        }
        return z;
    }

    private boolean isLastInTimeline(MediaPeriodId mediaPeriodId, boolean z) {
        return (this.timeline.getWindow(this.timeline.getPeriod(mediaPeriodId.periodIndex, this.period).windowIndex, this.window).isDynamic || this.timeline.isLastPeriod(mediaPeriodId.periodIndex, this.period, this.window, this.repeatMode, this.shuffleModeEnabled) == null || !z) ? null : true;
    }
}
