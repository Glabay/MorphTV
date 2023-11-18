package com.google.android.exoplayer2.trackselection;

import android.os.SystemClock;
import com.google.android.exoplayer2.source.TrackGroup;
import java.util.Random;

public final class RandomTrackSelection extends BaseTrackSelection {
    private final Random random;
    private int selectedIndex;

    public static final class Factory implements com.google.android.exoplayer2.trackselection.TrackSelection.Factory {
        private final Random random;

        public Factory() {
            this.random = new Random();
        }

        public Factory(int i) {
            this.random = new Random((long) i);
        }

        public RandomTrackSelection createTrackSelection(TrackGroup trackGroup, int... iArr) {
            return new RandomTrackSelection(trackGroup, iArr, this.random);
        }
    }

    public Object getSelectionData() {
        return null;
    }

    public int getSelectionReason() {
        return 3;
    }

    public RandomTrackSelection(TrackGroup trackGroup, int... iArr) {
        super(trackGroup, iArr);
        this.random = new Random();
        this.selectedIndex = this.random.nextInt(this.length);
    }

    public RandomTrackSelection(TrackGroup trackGroup, int[] iArr, long j) {
        this(trackGroup, iArr, new Random(j));
    }

    public RandomTrackSelection(TrackGroup trackGroup, int[] iArr, Random random) {
        super(trackGroup, iArr);
        this.random = random;
        this.selectedIndex = random.nextInt(this.length);
    }

    public void updateSelectedTrack(long j, long j2, long j3) {
        int i;
        j = SystemClock.elapsedRealtime();
        j3 = null;
        for (i = 0; i < this.length; i++) {
            if (!isBlacklisted(i, j)) {
                j3++;
            }
        }
        this.selectedIndex = this.random.nextInt(j3);
        if (j3 != this.length) {
            i = 0;
            for (j2 = null; j2 < this.length; j2++) {
                if (isBlacklisted(j2, j) == null) {
                    int i2 = i + 1;
                    if (this.selectedIndex == i) {
                        this.selectedIndex = j2;
                        return;
                    }
                    i = i2;
                }
            }
        }
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }
}
