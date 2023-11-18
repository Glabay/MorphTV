package com.google.android.exoplayer2.source;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.ShuffleOrder.DefaultShuffleOrder;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public final class ConcatenatingMediaSource extends CompositeMediaSource<Integer> {
    private final boolean isAtomic;
    private Listener listener;
    private final Object[] manifests;
    private final MediaSource[] mediaSources;
    private final ShuffleOrder shuffleOrder;
    private final Map<MediaPeriod, Integer> sourceIndexByMediaPeriod;
    private ConcatenatedTimeline timeline;
    private final Timeline[] timelines;

    private static final class ConcatenatedTimeline extends AbstractConcatenatedTimeline {
        private final int[] sourcePeriodOffsets;
        private final int[] sourceWindowOffsets;
        private final Timeline[] timelines;

        public ConcatenatedTimeline(Timeline[] timelineArr, boolean z, ShuffleOrder shuffleOrder) {
            super(z, shuffleOrder);
            z = new int[timelineArr.length];
            shuffleOrder = new int[timelineArr.length];
            long j = 0;
            int i = 0;
            int i2 = 0;
            while (i < timelineArr.length) {
                Timeline timeline = timelineArr[i];
                long periodCount = j + ((long) timeline.getPeriodCount());
                Assertions.checkState(periodCount <= 2147483647L, "ConcatenatingMediaSource children contain too many periods");
                z[i] = (int) periodCount;
                i2 += timeline.getWindowCount();
                shuffleOrder[i] = i2;
                i++;
                j = periodCount;
            }
            this.timelines = timelineArr;
            this.sourcePeriodOffsets = z;
            this.sourceWindowOffsets = shuffleOrder;
        }

        public int getWindowCount() {
            return this.sourceWindowOffsets[this.sourceWindowOffsets.length - 1];
        }

        public int getPeriodCount() {
            return this.sourcePeriodOffsets[this.sourcePeriodOffsets.length - 1];
        }

        protected int getChildIndexByPeriodIndex(int i) {
            return Util.binarySearchFloor(this.sourcePeriodOffsets, i + 1, false, false) + 1;
        }

        protected int getChildIndexByWindowIndex(int i) {
            return Util.binarySearchFloor(this.sourceWindowOffsets, i + 1, false, false) + 1;
        }

        protected int getChildIndexByChildUid(Object obj) {
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue();
            }
            return -1;
        }

        protected Timeline getTimelineByChildIndex(int i) {
            return this.timelines[i];
        }

        protected int getFirstPeriodIndexByChildIndex(int i) {
            return i == 0 ? 0 : this.sourcePeriodOffsets[i - 1];
        }

        protected int getFirstWindowIndexByChildIndex(int i) {
            return i == 0 ? 0 : this.sourceWindowOffsets[i - 1];
        }

        protected Object getChildUidByChildIndex(int i) {
            return Integer.valueOf(i);
        }
    }

    public ConcatenatingMediaSource(MediaSource... mediaSourceArr) {
        this(false, mediaSourceArr);
    }

    public ConcatenatingMediaSource(boolean z, MediaSource... mediaSourceArr) {
        this(z, new DefaultShuffleOrder(mediaSourceArr.length), mediaSourceArr);
    }

    public ConcatenatingMediaSource(boolean z, ShuffleOrder shuffleOrder, MediaSource... mediaSourceArr) {
        boolean z2 = false;
        for (Object checkNotNull : mediaSourceArr) {
            Assertions.checkNotNull(checkNotNull);
        }
        if (shuffleOrder.getLength() == mediaSourceArr.length) {
            z2 = true;
        }
        Assertions.checkArgument(z2);
        this.mediaSources = mediaSourceArr;
        this.isAtomic = z;
        this.shuffleOrder = shuffleOrder;
        this.timelines = new Timeline[mediaSourceArr.length];
        this.manifests = new Object[mediaSourceArr.length];
        this.sourceIndexByMediaPeriod = new HashMap();
    }

    public void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        super.prepareSource(exoPlayer, z, listener);
        this.listener = listener;
        exoPlayer = buildDuplicateFlags(this.mediaSources);
        if (this.mediaSources.length) {
            for (z = false; z < this.mediaSources.length; z++) {
                if (exoPlayer[z] == null) {
                    prepareChildSource(Integer.valueOf(z), this.mediaSources[z]);
                }
            }
            return;
        }
        listener.onSourceInfoRefreshed(this, Timeline.EMPTY, false);
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        int childIndexByPeriodIndex = this.timeline.getChildIndexByPeriodIndex(mediaPeriodId.periodIndex);
        mediaPeriodId = this.mediaSources[childIndexByPeriodIndex].createPeriod(mediaPeriodId.copyWithPeriodIndex(mediaPeriodId.periodIndex - this.timeline.getFirstPeriodIndexByChildIndex(childIndexByPeriodIndex)), allocator);
        this.sourceIndexByMediaPeriod.put(mediaPeriodId, Integer.valueOf(childIndexByPeriodIndex));
        return mediaPeriodId;
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        int intValue = ((Integer) this.sourceIndexByMediaPeriod.get(mediaPeriod)).intValue();
        this.sourceIndexByMediaPeriod.remove(mediaPeriod);
        this.mediaSources[intValue].releasePeriod(mediaPeriod);
    }

    public void releaseSource() {
        super.releaseSource();
        this.listener = null;
        this.timeline = null;
    }

    protected void onChildSourceInfoRefreshed(Integer num, MediaSource mediaSource, Timeline timeline, @Nullable Object obj) {
        this.timelines[num.intValue()] = timeline;
        this.manifests[num.intValue()] = obj;
        num = num.intValue();
        while (true) {
            num++;
            if (num >= this.mediaSources.length) {
                break;
            } else if (this.mediaSources[num] == mediaSource) {
                this.timelines[num] = timeline;
                this.manifests[num] = obj;
            }
        }
        num = this.timelines;
        Timeline length = num.length;
        timeline = null;
        while (timeline < length) {
            if (num[timeline] != null) {
                timeline++;
            } else {
                return;
            }
        }
        this.timeline = new ConcatenatedTimeline((Timeline[]) this.timelines.clone(), this.isAtomic, this.shuffleOrder);
        this.listener.onSourceInfoRefreshed(this, this.timeline, this.manifests.clone());
    }

    private static boolean[] buildDuplicateFlags(MediaSource[] mediaSourceArr) {
        boolean[] zArr = new boolean[mediaSourceArr.length];
        IdentityHashMap identityHashMap = new IdentityHashMap(mediaSourceArr.length);
        for (int i = 0; i < mediaSourceArr.length; i++) {
            Object obj = mediaSourceArr[i];
            if (identityHashMap.containsKey(obj)) {
                zArr[i] = true;
            } else {
                identityHashMap.put(obj, null);
            }
        }
        return zArr;
    }
}
