package com.google.android.exoplayer2.source;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.ShuffleOrder.UnshuffledShuffleOrder;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;

public final class LoopingMediaSource extends CompositeMediaSource<Void> {
    private int childPeriodCount;
    private final MediaSource childSource;
    private Listener listener;
    private final int loopCount;

    private static final class InfinitelyLoopingTimeline extends ForwardingTimeline {
        public InfinitelyLoopingTimeline(Timeline timeline) {
            super(timeline);
        }

        public int getNextWindowIndex(int i, int i2, boolean z) {
            i = this.timeline.getNextWindowIndex(i, i2, z);
            return i == -1 ? getFirstWindowIndex(z) : i;
        }

        public int getPreviousWindowIndex(int i, int i2, boolean z) {
            i = this.timeline.getPreviousWindowIndex(i, i2, z);
            return i == -1 ? getLastWindowIndex(z) : i;
        }
    }

    private static final class LoopingTimeline extends AbstractConcatenatedTimeline {
        private final int childPeriodCount;
        private final Timeline childTimeline;
        private final int childWindowCount;
        private final int loopCount;

        public LoopingTimeline(Timeline timeline, int i) {
            boolean z = false;
            super(false, new UnshuffledShuffleOrder(i));
            this.childTimeline = timeline;
            this.childPeriodCount = timeline.getPeriodCount();
            this.childWindowCount = timeline.getWindowCount();
            this.loopCount = i;
            if (this.childPeriodCount > null) {
                if (i <= Integer.MAX_VALUE / this.childPeriodCount) {
                    z = true;
                }
                Assertions.checkState(z, "LoopingMediaSource contains too many periods");
            }
        }

        public int getWindowCount() {
            return this.childWindowCount * this.loopCount;
        }

        public int getPeriodCount() {
            return this.childPeriodCount * this.loopCount;
        }

        protected int getChildIndexByPeriodIndex(int i) {
            return i / this.childPeriodCount;
        }

        protected int getChildIndexByWindowIndex(int i) {
            return i / this.childWindowCount;
        }

        protected int getChildIndexByChildUid(Object obj) {
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue();
            }
            return -1;
        }

        protected Timeline getTimelineByChildIndex(int i) {
            return this.childTimeline;
        }

        protected int getFirstPeriodIndexByChildIndex(int i) {
            return i * this.childPeriodCount;
        }

        protected int getFirstWindowIndexByChildIndex(int i) {
            return i * this.childWindowCount;
        }

        protected Object getChildUidByChildIndex(int i) {
            return Integer.valueOf(i);
        }
    }

    public LoopingMediaSource(MediaSource mediaSource) {
        this(mediaSource, Integer.MAX_VALUE);
    }

    public LoopingMediaSource(MediaSource mediaSource, int i) {
        Assertions.checkArgument(i > 0);
        this.childSource = mediaSource;
        this.loopCount = i;
    }

    public void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        super.prepareSource(exoPlayer, z, listener);
        this.listener = listener;
        prepareChildSource(false, this.childSource);
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        if (this.loopCount != Integer.MAX_VALUE) {
            return this.childSource.createPeriod(mediaPeriodId.copyWithPeriodIndex(mediaPeriodId.periodIndex % this.childPeriodCount), allocator);
        }
        return this.childSource.createPeriod(mediaPeriodId, allocator);
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        this.childSource.releasePeriod(mediaPeriod);
    }

    public void releaseSource() {
        super.releaseSource();
        this.listener = null;
        this.childPeriodCount = 0;
    }

    protected void onChildSourceInfoRefreshed(Void voidR, MediaSource mediaSource, Timeline timeline, @Nullable Object obj) {
        this.childPeriodCount = timeline.getPeriodCount();
        this.listener.onSourceInfoRefreshed(this, this.loopCount != Integer.MAX_VALUE ? new LoopingTimeline(timeline, this.loopCount) : new InfinitelyLoopingTimeline(timeline), obj);
    }
}
