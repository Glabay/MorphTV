package com.google.android.exoplayer2.source;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public final class ClippingMediaSource extends CompositeMediaSource<Void> {
    private IllegalClippingException clippingError;
    private final boolean enableInitialDiscontinuity;
    private final long endUs;
    private final ArrayList<ClippingMediaPeriod> mediaPeriods;
    private final MediaSource mediaSource;
    private Listener sourceListener;
    private final long startUs;

    private static final class ClippingTimeline extends ForwardingTimeline {
        private final long endUs;
        private final long startUs;

        public ClippingTimeline(Timeline timeline, long j, long j2) throws IllegalClippingException {
            super(timeline);
            if (timeline.getPeriodCount() != 1) {
                throw new IllegalClippingException(0);
            } else if (timeline.getPeriod(0, new Period()).getPositionInWindowUs() != 0) {
                throw new IllegalClippingException(1);
            } else {
                timeline = timeline.getWindow(0, new Window(), false);
                if (j2 == Long.MIN_VALUE) {
                    j2 = timeline.durationUs;
                }
                if (timeline.durationUs != C0649C.TIME_UNSET) {
                    if (j2 > timeline.durationUs) {
                        j2 = timeline.durationUs;
                    }
                    if (j != 0 && timeline.isSeekable == null) {
                        throw new IllegalClippingException(2);
                    } else if (j > j2) {
                        throw new IllegalClippingException(3);
                    }
                }
                this.startUs = j;
                this.endUs = j2;
            }
        }

        public Window getWindow(int i, Window window, boolean z, long j) {
            i = this.timeline.getWindow(0, window, z, j);
            i.durationUs = this.endUs != -9223372036854775807 ? this.endUs - this.startUs : C0649C.TIME_UNSET;
            if (i.defaultPositionUs != -9223372036854775807) {
                i.defaultPositionUs = Math.max(i.defaultPositionUs, this.startUs);
                if (this.endUs == -9223372036854775807) {
                    window = i.defaultPositionUs;
                } else {
                    window = Math.min(i.defaultPositionUs, this.endUs);
                }
                i.defaultPositionUs = window;
                i.defaultPositionUs -= this.startUs;
            }
            window = C0649C.usToMs(this.startUs);
            if (i.presentationStartTimeMs != C0649C.TIME_UNSET) {
                i.presentationStartTimeMs += window;
            }
            if (i.windowStartTimeMs != C0649C.TIME_UNSET) {
                i.windowStartTimeMs += window;
            }
            return i;
        }

        public Period getPeriod(int i, Period period, boolean z) {
            i = this.timeline.getPeriod(0, period, z);
            period = this.endUs;
            long j = C0649C.TIME_UNSET;
            if (period != -9223372036854775807) {
                j = this.endUs - this.startUs;
            }
            i.durationUs = j;
            return i;
        }
    }

    public static final class IllegalClippingException extends IOException {
        public static final int REASON_INVALID_PERIOD_COUNT = 0;
        public static final int REASON_NOT_SEEKABLE_TO_START = 2;
        public static final int REASON_PERIOD_OFFSET_IN_WINDOW = 1;
        public static final int REASON_START_EXCEEDS_END = 3;
        public final int reason;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Reason {
        }

        public IllegalClippingException(int i) {
            this.reason = i;
        }
    }

    public ClippingMediaSource(MediaSource mediaSource, long j, long j2) {
        this(mediaSource, j, j2, true);
    }

    public ClippingMediaSource(MediaSource mediaSource, long j, long j2, boolean z) {
        Assertions.checkArgument(j >= 0);
        this.mediaSource = (MediaSource) Assertions.checkNotNull(mediaSource);
        this.startUs = j;
        this.endUs = j2;
        this.enableInitialDiscontinuity = z;
        this.mediaPeriods = new ArrayList();
    }

    public void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        super.prepareSource(exoPlayer, z, listener);
        this.sourceListener = listener;
        prepareChildSource(false, this.mediaSource);
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        if (this.clippingError != null) {
            throw this.clippingError;
        }
        super.maybeThrowSourceInfoRefreshError();
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        MediaPeriod clippingMediaPeriod = new ClippingMediaPeriod(this.mediaSource.createPeriod(mediaPeriodId, allocator), this.enableInitialDiscontinuity);
        this.mediaPeriods.add(clippingMediaPeriod);
        clippingMediaPeriod.setClipping(this.startUs, this.endUs);
        return clippingMediaPeriod;
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        Assertions.checkState(this.mediaPeriods.remove(mediaPeriod));
        this.mediaSource.releasePeriod(((ClippingMediaPeriod) mediaPeriod).mediaPeriod);
    }

    public void releaseSource() {
        super.releaseSource();
        this.clippingError = null;
        this.sourceListener = null;
    }

    protected void onChildSourceInfoRefreshed(Void voidR, MediaSource mediaSource, Timeline timeline, @Nullable Object obj) {
        if (this.clippingError == null) {
            try {
                this.sourceListener.onSourceInfoRefreshed(this, new ClippingTimeline(timeline, this.startUs, this.endUs), obj);
                MediaSource size = this.mediaPeriods.size();
                for (mediaSource = null; mediaSource < size; mediaSource++) {
                    ((ClippingMediaPeriod) this.mediaPeriods.get(mediaSource)).setClipping(this.startUs, this.endUs);
                }
            } catch (Void voidR2) {
                this.clippingError = voidR2;
            }
        }
    }
}
