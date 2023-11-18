package com.google.android.exoplayer2.source;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.Allocator;
import java.io.IOException;

public interface MediaSource {
    public static final String MEDIA_SOURCE_REUSED_ERROR_MESSAGE = "MediaSource instances are not allowed to be reused.";

    public interface Listener {
        void onSourceInfoRefreshed(MediaSource mediaSource, Timeline timeline, @Nullable Object obj);
    }

    public static final class MediaPeriodId {
        public final int adGroupIndex;
        public final int adIndexInAdGroup;
        public final int periodIndex;
        public final long windowSequenceNumber;

        public MediaPeriodId(int i) {
            this(i, -1);
        }

        public MediaPeriodId(int i, long j) {
            this(i, -1, -1, j);
        }

        public MediaPeriodId(int i, int i2, int i3, long j) {
            this.periodIndex = i;
            this.adGroupIndex = i2;
            this.adIndexInAdGroup = i3;
            this.windowSequenceNumber = j;
        }

        public MediaPeriodId copyWithPeriodIndex(int i) {
            if (this.periodIndex == i) {
                return this;
            }
            return new MediaPeriodId(i, this.adGroupIndex, this.adIndexInAdGroup, this.windowSequenceNumber);
        }

        public boolean isAd() {
            return this.adGroupIndex != -1;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (getClass() == obj.getClass()) {
                    MediaPeriodId mediaPeriodId = (MediaPeriodId) obj;
                    if (this.periodIndex != mediaPeriodId.periodIndex || this.adGroupIndex != mediaPeriodId.adGroupIndex || this.adIndexInAdGroup != mediaPeriodId.adIndexInAdGroup || this.windowSequenceNumber != mediaPeriodId.windowSequenceNumber) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        public int hashCode() {
            return ((((((527 + this.periodIndex) * 31) + this.adGroupIndex) * 31) + this.adIndexInAdGroup) * 31) + ((int) this.windowSequenceNumber);
        }
    }

    MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator);

    void maybeThrowSourceInfoRefreshError() throws IOException;

    void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener);

    void releasePeriod(MediaPeriod mediaPeriod);

    void releaseSource();
}
