package com.google.android.exoplayer2.source.ads;

import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.ForwardingTimeline;
import com.google.android.exoplayer2.util.Assertions;

final class SinglePeriodAdTimeline extends ForwardingTimeline {
    private final AdPlaybackState adPlaybackState;

    public SinglePeriodAdTimeline(Timeline timeline, AdPlaybackState adPlaybackState) {
        super(timeline);
        boolean z = false;
        Assertions.checkState(timeline.getPeriodCount() == 1);
        if (timeline.getWindowCount() == 1) {
            z = true;
        }
        Assertions.checkState(z);
        this.adPlaybackState = adPlaybackState;
    }

    public Period getPeriod(int i, Period period, boolean z) {
        this.timeline.getPeriod(i, period, z);
        period.set(period.id, period.uid, period.windowIndex, period.durationUs, period.getPositionInWindowUs(), this.adPlaybackState);
        return period;
    }

    public Window getWindow(int i, Window window, boolean z, long j) {
        i = super.getWindow(i, window, z, j);
        if (i.durationUs == C0649C.TIME_UNSET) {
            i.durationUs = this.adPlaybackState.contentDurationUs;
        }
        return i;
    }
}
