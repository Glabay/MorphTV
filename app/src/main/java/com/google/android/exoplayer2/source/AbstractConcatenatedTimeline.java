package com.google.android.exoplayer2.source;

import android.util.Pair;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;

abstract class AbstractConcatenatedTimeline extends Timeline {
    private final int childCount;
    private final boolean isAtomic;
    private final ShuffleOrder shuffleOrder;

    protected abstract int getChildIndexByChildUid(Object obj);

    protected abstract int getChildIndexByPeriodIndex(int i);

    protected abstract int getChildIndexByWindowIndex(int i);

    protected abstract Object getChildUidByChildIndex(int i);

    protected abstract int getFirstPeriodIndexByChildIndex(int i);

    protected abstract int getFirstWindowIndexByChildIndex(int i);

    protected abstract Timeline getTimelineByChildIndex(int i);

    public AbstractConcatenatedTimeline(boolean z, ShuffleOrder shuffleOrder) {
        this.isAtomic = z;
        this.shuffleOrder = shuffleOrder;
        this.childCount = shuffleOrder.getLength();
    }

    public int getNextWindowIndex(int i, int i2, boolean z) {
        int i3 = 0;
        if (this.isAtomic) {
            if (i2 == true) {
                i2 = 2;
            }
            z = false;
        }
        int childIndexByWindowIndex = getChildIndexByWindowIndex(i);
        int firstWindowIndexByChildIndex = getFirstWindowIndexByChildIndex(childIndexByWindowIndex);
        Timeline timelineByChildIndex = getTimelineByChildIndex(childIndexByWindowIndex);
        i -= firstWindowIndexByChildIndex;
        if (i2 != 2) {
            i3 = i2;
        }
        i = timelineByChildIndex.getNextWindowIndex(i, i3, z);
        if (i != -1) {
            return firstWindowIndexByChildIndex + i;
        }
        i = getNextChildIndex(childIndexByWindowIndex, z);
        while (i != -1 && getTimelineByChildIndex(i).isEmpty()) {
            i = getNextChildIndex(i, z);
        }
        if (i != -1) {
            return getFirstWindowIndexByChildIndex(i) + getTimelineByChildIndex(i).getFirstWindowIndex(z);
        }
        if (i2 == 2) {
            return getFirstWindowIndex(z);
        }
        return -1;
    }

    public int getPreviousWindowIndex(int i, int i2, boolean z) {
        int i3 = 0;
        if (this.isAtomic) {
            if (i2 == true) {
                i2 = 2;
            }
            z = false;
        }
        int childIndexByWindowIndex = getChildIndexByWindowIndex(i);
        int firstWindowIndexByChildIndex = getFirstWindowIndexByChildIndex(childIndexByWindowIndex);
        Timeline timelineByChildIndex = getTimelineByChildIndex(childIndexByWindowIndex);
        i -= firstWindowIndexByChildIndex;
        if (i2 != 2) {
            i3 = i2;
        }
        i = timelineByChildIndex.getPreviousWindowIndex(i, i3, z);
        if (i != -1) {
            return firstWindowIndexByChildIndex + i;
        }
        i = getPreviousChildIndex(childIndexByWindowIndex, z);
        while (i != -1 && getTimelineByChildIndex(i).isEmpty()) {
            i = getPreviousChildIndex(i, z);
        }
        if (i != -1) {
            return getFirstWindowIndexByChildIndex(i) + getTimelineByChildIndex(i).getLastWindowIndex(z);
        }
        if (i2 == 2) {
            return getLastWindowIndex(z);
        }
        return -1;
    }

    public int getLastWindowIndex(boolean z) {
        if (this.childCount == 0) {
            return -1;
        }
        if (this.isAtomic) {
            z = false;
        }
        int lastIndex = z ? this.shuffleOrder.getLastIndex() : this.childCount - 1;
        while (getTimelineByChildIndex(lastIndex).isEmpty()) {
            lastIndex = getPreviousChildIndex(lastIndex, z);
            if (lastIndex == -1) {
                return -1;
            }
        }
        return getFirstWindowIndexByChildIndex(lastIndex) + getTimelineByChildIndex(lastIndex).getLastWindowIndex(z);
    }

    public int getFirstWindowIndex(boolean z) {
        if (this.childCount == 0) {
            return -1;
        }
        int i = 0;
        if (this.isAtomic) {
            z = false;
        }
        if (z) {
            i = this.shuffleOrder.getFirstIndex();
        }
        while (getTimelineByChildIndex(i).isEmpty()) {
            i = getNextChildIndex(i, z);
            if (i == -1) {
                return -1;
            }
        }
        return getFirstWindowIndexByChildIndex(i) + getTimelineByChildIndex(i).getFirstWindowIndex(z);
    }

    public final Window getWindow(int i, Window window, boolean z, long j) {
        int childIndexByWindowIndex = getChildIndexByWindowIndex(i);
        int firstWindowIndexByChildIndex = getFirstWindowIndexByChildIndex(childIndexByWindowIndex);
        int firstPeriodIndexByChildIndex = getFirstPeriodIndexByChildIndex(childIndexByWindowIndex);
        getTimelineByChildIndex(childIndexByWindowIndex).getWindow(i - firstWindowIndexByChildIndex, window, z, j);
        window.firstPeriodIndex += firstPeriodIndexByChildIndex;
        window.lastPeriodIndex += firstPeriodIndexByChildIndex;
        return window;
    }

    public final Period getPeriod(int i, Period period, boolean z) {
        int childIndexByPeriodIndex = getChildIndexByPeriodIndex(i);
        int firstWindowIndexByChildIndex = getFirstWindowIndexByChildIndex(childIndexByPeriodIndex);
        getTimelineByChildIndex(childIndexByPeriodIndex).getPeriod(i - getFirstPeriodIndexByChildIndex(childIndexByPeriodIndex), period, z);
        period.windowIndex += firstWindowIndexByChildIndex;
        if (z) {
            period.uid = Pair.create(getChildUidByChildIndex(childIndexByPeriodIndex), period.uid);
        }
        return period;
    }

    public final int getIndexOfPeriod(Object obj) {
        int i = -1;
        if (!(obj instanceof Pair)) {
            return -1;
        }
        Pair pair = (Pair) obj;
        Object obj2 = pair.first;
        obj = pair.second;
        int childIndexByChildUid = getChildIndexByChildUid(obj2);
        if (childIndexByChildUid == -1) {
            return -1;
        }
        obj = getTimelineByChildIndex(childIndexByChildUid).getIndexOfPeriod(obj);
        if (obj != -1) {
            i = getFirstPeriodIndexByChildIndex(childIndexByChildUid) + obj;
        }
        return i;
    }

    private int getNextChildIndex(int i, boolean z) {
        if (z) {
            return this.shuffleOrder.getNextIndex(i);
        }
        return i < this.childCount + -1 ? i + 1 : -1;
    }

    private int getPreviousChildIndex(int i, boolean z) {
        if (z) {
            return this.shuffleOrder.getPreviousIndex(i);
        }
        return i > 0 ? i - 1 : -1;
    }
}
