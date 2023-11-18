package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.MediaPeriod.Callback;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;

final class MergingMediaPeriod implements MediaPeriod, Callback {
    private Callback callback;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private MediaPeriod[] enabledPeriods;
    private int pendingChildPrepareCount;
    public final MediaPeriod[] periods;
    private final IdentityHashMap<SampleStream, Integer> streamPeriodIndices = new IdentityHashMap();
    private TrackGroupArray trackGroups;

    public MergingMediaPeriod(CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, MediaPeriod... mediaPeriodArr) {
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.periods = mediaPeriodArr;
    }

    public void prepare(Callback callback, long j) {
        this.callback = callback;
        this.pendingChildPrepareCount = this.periods.length;
        for (MediaPeriod prepare : this.periods) {
            prepare.prepare(this, j);
        }
    }

    public void maybeThrowPrepareError() throws IOException {
        for (MediaPeriod maybeThrowPrepareError : this.periods) {
            maybeThrowPrepareError.maybeThrowPrepareError();
        }
    }

    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    public long selectTracks(TrackSelection[] trackSelectionArr, boolean[] zArr, SampleStream[] sampleStreamArr, boolean[] zArr2, long j) {
        ArrayList arrayList;
        MergingMediaPeriod mergingMediaPeriod = this;
        TrackSelection[] trackSelectionArr2 = trackSelectionArr;
        SampleStream[] sampleStreamArr2 = sampleStreamArr;
        int[] iArr = new int[trackSelectionArr2.length];
        int[] iArr2 = new int[trackSelectionArr2.length];
        for (int i = 0; i < trackSelectionArr2.length; i++) {
            int i2;
            if (sampleStreamArr2[i] == null) {
                i2 = -1;
            } else {
                i2 = ((Integer) mergingMediaPeriod.streamPeriodIndices.get(sampleStreamArr2[i])).intValue();
            }
            iArr[i] = i2;
            iArr2[i] = -1;
            if (trackSelectionArr2[i] != null) {
                TrackGroup trackGroup = trackSelectionArr2[i].getTrackGroup();
                for (int i3 = 0; i3 < mergingMediaPeriod.periods.length; i3++) {
                    if (mergingMediaPeriod.periods[i3].getTrackGroups().indexOf(trackGroup) != -1) {
                        iArr2[i] = i3;
                        break;
                    }
                }
            }
        }
        mergingMediaPeriod.streamPeriodIndices.clear();
        Object obj = new SampleStream[trackSelectionArr2.length];
        SampleStream[] sampleStreamArr3 = new SampleStream[trackSelectionArr2.length];
        TrackSelection[] trackSelectionArr3 = new TrackSelection[trackSelectionArr2.length];
        ArrayList arrayList2 = new ArrayList(mergingMediaPeriod.periods.length);
        long j2 = j;
        int i4 = 0;
        while (i4 < mergingMediaPeriod.periods.length) {
            int i5 = 0;
            while (i5 < trackSelectionArr2.length) {
                TrackSelection trackSelection = null;
                sampleStreamArr3[i5] = iArr[i5] == i4 ? sampleStreamArr2[i5] : null;
                if (iArr2[i5] == i4) {
                    trackSelection = trackSelectionArr2[i5];
                }
                trackSelectionArr3[i5] = trackSelection;
                i5++;
            }
            arrayList = arrayList2;
            TrackSelection[] trackSelectionArr4 = trackSelectionArr3;
            int i6 = i4;
            long selectTracks = mergingMediaPeriod.periods[i4].selectTracks(trackSelectionArr3, zArr, sampleStreamArr3, zArr2, j2);
            if (i6 == 0) {
                j2 = selectTracks;
            } else if (selectTracks != j2) {
                throw new IllegalStateException("Children enabled at different positions");
            }
            Object obj2 = null;
            for (i5 = 0; i5 < trackSelectionArr2.length; i5++) {
                boolean z = true;
                if (iArr2[i5] == i6) {
                    Assertions.checkState(sampleStreamArr3[i5] != null);
                    obj[i5] = sampleStreamArr3[i5];
                    mergingMediaPeriod.streamPeriodIndices.put(sampleStreamArr3[i5], Integer.valueOf(i6));
                    obj2 = 1;
                } else if (iArr[i5] == i6) {
                    if (sampleStreamArr3[i5] != null) {
                        z = false;
                    }
                    Assertions.checkState(z);
                }
            }
            if (obj2 != null) {
                arrayList.add(mergingMediaPeriod.periods[i6]);
            }
            i4 = i6 + 1;
            arrayList2 = arrayList;
            trackSelectionArr3 = trackSelectionArr4;
        }
        arrayList = arrayList2;
        System.arraycopy(obj, 0, sampleStreamArr2, 0, obj.length);
        mergingMediaPeriod.enabledPeriods = new MediaPeriod[arrayList.size()];
        arrayList.toArray(mergingMediaPeriod.enabledPeriods);
        mergingMediaPeriod.compositeSequenceableLoader = mergingMediaPeriod.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(mergingMediaPeriod.enabledPeriods);
        return j2;
    }

    public void discardBuffer(long j, boolean z) {
        for (MediaPeriod discardBuffer : this.enabledPeriods) {
            discardBuffer.discardBuffer(j, z);
        }
    }

    public void reevaluateBuffer(long j) {
        this.compositeSequenceableLoader.reevaluateBuffer(j);
    }

    public boolean continueLoading(long j) {
        return this.compositeSequenceableLoader.continueLoading(j);
    }

    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }

    public long readDiscontinuity() {
        long readDiscontinuity = this.periods[0].readDiscontinuity();
        for (int i = 1; i < this.periods.length; i++) {
            if (this.periods[i].readDiscontinuity() != C0649C.TIME_UNSET) {
                throw new IllegalStateException("Child reported discontinuity");
            }
        }
        if (readDiscontinuity != C0649C.TIME_UNSET) {
            MediaPeriod[] mediaPeriodArr = this.enabledPeriods;
            int length = mediaPeriodArr.length;
            int i2 = 0;
            while (i2 < length) {
                MediaPeriod mediaPeriod = mediaPeriodArr[i2];
                if (mediaPeriod == this.periods[0] || mediaPeriod.seekToUs(readDiscontinuity) == readDiscontinuity) {
                    i2++;
                } else {
                    throw new IllegalStateException("Children seeked to different positions");
                }
            }
        }
        return readDiscontinuity;
    }

    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }

    public long seekToUs(long j) {
        j = this.enabledPeriods[0].seekToUs(j);
        for (int i = 1; i < this.enabledPeriods.length; i++) {
            if (this.enabledPeriods[i].seekToUs(j) != j) {
                throw new IllegalStateException("Children seeked to different positions");
            }
        }
        return j;
    }

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        return this.enabledPeriods[0].getAdjustedSeekPositionUs(j, seekParameters);
    }

    public void onPrepared(MediaPeriod mediaPeriod) {
        mediaPeriod = this.pendingChildPrepareCount - 1;
        this.pendingChildPrepareCount = mediaPeriod;
        if (mediaPeriod <= null) {
            int i = 0;
            for (MediaPeriod trackGroups : this.periods) {
                i += trackGroups.getTrackGroups().length;
            }
            mediaPeriod = new TrackGroup[i];
            MediaPeriod[] mediaPeriodArr = this.periods;
            int length = mediaPeriodArr.length;
            i = 0;
            int i2 = 0;
            while (i < length) {
                TrackGroupArray trackGroups2 = mediaPeriodArr[i].getTrackGroups();
                int i3 = trackGroups2.length;
                int i4 = i2;
                i2 = 0;
                while (i2 < i3) {
                    int i5 = i4 + 1;
                    mediaPeriod[i4] = trackGroups2.get(i2);
                    i2++;
                    i4 = i5;
                }
                i++;
                i2 = i4;
            }
            this.trackGroups = new TrackGroupArray(mediaPeriod);
            this.callback.onPrepared(this);
        }
    }

    public void onContinueLoadingRequested(MediaPeriod mediaPeriod) {
        if (this.trackGroups != null) {
            this.callback.onContinueLoadingRequested(this);
        }
    }
}
