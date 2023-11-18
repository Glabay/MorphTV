package com.google.android.exoplayer2.source;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlayerMessage.Target;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.MediaSource.Listener;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.ShuffleOrder.DefaultShuffleOrder;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class DynamicConcatenatingMediaSource extends CompositeMediaSource<MediaSourceHolder> implements Target {
    private static final int MSG_ADD = 0;
    private static final int MSG_ADD_MULTIPLE = 1;
    private static final int MSG_MOVE = 3;
    private static final int MSG_ON_COMPLETION = 4;
    private static final int MSG_REMOVE = 2;
    private final List<DeferredMediaPeriod> deferredMediaPeriods;
    private final boolean isAtomic;
    private Listener listener;
    private final Map<MediaPeriod, MediaSourceHolder> mediaSourceByMediaPeriod;
    private final List<MediaSourceHolder> mediaSourceHolders;
    private final List<MediaSource> mediaSourcesPublic;
    private int periodCount;
    private ExoPlayer player;
    private boolean preventListenerNotification;
    private final MediaSourceHolder query;
    private ShuffleOrder shuffleOrder;
    private int windowCount;

    private static final class ConcatenatedTimeline extends AbstractConcatenatedTimeline {
        private final SparseIntArray childIndexByUid = new SparseIntArray();
        private final int[] firstPeriodInChildIndices;
        private final int[] firstWindowInChildIndices;
        private final int periodCount;
        private final Timeline[] timelines;
        private final int[] uids;
        private final int windowCount;

        public ConcatenatedTimeline(Collection<MediaSourceHolder> collection, int i, int i2, ShuffleOrder shuffleOrder, boolean z) {
            super(z, shuffleOrder);
            this.windowCount = i;
            this.periodCount = i2;
            i = collection.size();
            this.firstPeriodInChildIndices = new int[i];
            this.firstWindowInChildIndices = new int[i];
            this.timelines = new Timeline[i];
            this.uids = new int[i];
            i = 0;
            for (MediaSourceHolder mediaSourceHolder : collection) {
                this.timelines[i] = mediaSourceHolder.timeline;
                this.firstPeriodInChildIndices[i] = mediaSourceHolder.firstPeriodIndexInChild;
                this.firstWindowInChildIndices[i] = mediaSourceHolder.firstWindowIndexInChild;
                this.uids[i] = mediaSourceHolder.uid;
                z = i + 1;
                this.childIndexByUid.put(this.uids[i], i);
                boolean z2 = z;
            }
        }

        protected int getChildIndexByPeriodIndex(int i) {
            return Util.binarySearchFloor(this.firstPeriodInChildIndices, i + 1, false, false);
        }

        protected int getChildIndexByWindowIndex(int i) {
            return Util.binarySearchFloor(this.firstWindowInChildIndices, i + 1, false, false);
        }

        protected int getChildIndexByChildUid(Object obj) {
            if (!(obj instanceof Integer)) {
                return -1;
            }
            obj = this.childIndexByUid.get(((Integer) obj).intValue(), -1);
            if (obj == -1) {
                obj = -1;
            }
            return obj;
        }

        protected Timeline getTimelineByChildIndex(int i) {
            return this.timelines[i];
        }

        protected int getFirstPeriodIndexByChildIndex(int i) {
            return this.firstPeriodInChildIndices[i];
        }

        protected int getFirstWindowIndexByChildIndex(int i) {
            return this.firstWindowInChildIndices[i];
        }

        protected Object getChildUidByChildIndex(int i) {
            return Integer.valueOf(this.uids[i]);
        }

        public int getWindowCount() {
            return this.windowCount;
        }

        public int getPeriodCount() {
            return this.periodCount;
        }
    }

    private static final class DeferredTimeline extends ForwardingTimeline {
        private static final Object DUMMY_ID = new Object();
        private static final DummyTimeline dummyTimeline = new DummyTimeline();
        private static final Period period = new Period();
        private final Object replacedId;

        public DeferredTimeline() {
            this(dummyTimeline, null);
        }

        private DeferredTimeline(Timeline timeline, Object obj) {
            super(timeline);
            this.replacedId = obj;
        }

        public DeferredTimeline cloneWithNewTimeline(Timeline timeline) {
            Object obj = (this.replacedId != null || timeline.getPeriodCount() <= 0) ? this.replacedId : timeline.getPeriod(0, period, true).uid;
            return new DeferredTimeline(timeline, obj);
        }

        public Timeline getTimeline() {
            return this.timeline;
        }

        public Period getPeriod(int i, Period period, boolean z) {
            this.timeline.getPeriod(i, period, z);
            if (Util.areEqual(period.uid, this.replacedId) != 0) {
                period.uid = DUMMY_ID;
            }
            return period;
        }

        public int getIndexOfPeriod(Object obj) {
            Timeline timeline = this.timeline;
            if (DUMMY_ID.equals(obj)) {
                obj = this.replacedId;
            }
            return timeline.getIndexOfPeriod(obj);
        }
    }

    private static final class DummyTimeline extends Timeline {
        public int getIndexOfPeriod(Object obj) {
            return obj == null ? 0 : -1;
        }

        public int getPeriodCount() {
            return 1;
        }

        public int getWindowCount() {
            return 1;
        }

        private DummyTimeline() {
        }

        public Window getWindow(int i, Window window, boolean z, long j) {
            return window.set(null, C0649C.TIME_UNSET, C0649C.TIME_UNSET, false, true, 0, C0649C.TIME_UNSET, 0, 0, 0);
        }

        public Period getPeriod(int i, Period period, boolean z) {
            return period.set(null, null, 0, C0649C.TIME_UNSET, C0649C.TIME_UNSET);
        }
    }

    private static final class EventDispatcher {
        public final Handler eventHandler;
        public final Runnable runnable;

        public EventDispatcher(Runnable runnable) {
            Looper myLooper;
            this.runnable = runnable;
            if (Looper.myLooper() != null) {
                myLooper = Looper.myLooper();
            } else {
                myLooper = Looper.getMainLooper();
            }
            this.eventHandler = new Handler(myLooper);
        }

        public void dispatchEvent() {
            this.eventHandler.post(this.runnable);
        }
    }

    static final class MediaSourceHolder implements Comparable<MediaSourceHolder> {
        public int activeMediaPeriods;
        public int childIndex;
        public int firstPeriodIndexInChild;
        public int firstWindowIndexInChild;
        public boolean isPrepared;
        public boolean isRemoved;
        public final MediaSource mediaSource;
        public DeferredTimeline timeline;
        public final int uid = System.identityHashCode(this);

        public MediaSourceHolder(MediaSource mediaSource, DeferredTimeline deferredTimeline, int i, int i2, int i3) {
            this.mediaSource = mediaSource;
            this.timeline = deferredTimeline;
            this.childIndex = i;
            this.firstWindowIndexInChild = i2;
            this.firstPeriodIndexInChild = i3;
        }

        public int compareTo(@NonNull MediaSourceHolder mediaSourceHolder) {
            return this.firstPeriodIndexInChild - mediaSourceHolder.firstPeriodIndexInChild;
        }
    }

    private static final class MessageData<T> {
        @Nullable
        public final EventDispatcher actionOnCompletion;
        public final T customData;
        public final int index;

        public MessageData(int i, T t, @Nullable Runnable runnable) {
            this.index = i;
            this.actionOnCompletion = runnable != null ? new EventDispatcher(runnable) : 0;
            this.customData = t;
        }
    }

    public DynamicConcatenatingMediaSource() {
        this(false, new DefaultShuffleOrder(0));
    }

    public DynamicConcatenatingMediaSource(boolean z) {
        this(z, new DefaultShuffleOrder(0));
    }

    public DynamicConcatenatingMediaSource(boolean z, ShuffleOrder shuffleOrder) {
        this.shuffleOrder = shuffleOrder;
        this.mediaSourceByMediaPeriod = new IdentityHashMap();
        this.mediaSourcesPublic = new ArrayList();
        this.mediaSourceHolders = new ArrayList();
        this.deferredMediaPeriods = new ArrayList(1);
        this.query = new MediaSourceHolder(null, null, -1, -1, -1);
        this.isAtomic = z;
    }

    public synchronized void addMediaSource(MediaSource mediaSource) {
        addMediaSource(this.mediaSourcesPublic.size(), mediaSource, null);
    }

    public synchronized void addMediaSource(MediaSource mediaSource, @Nullable Runnable runnable) {
        addMediaSource(this.mediaSourcesPublic.size(), mediaSource, runnable);
    }

    public synchronized void addMediaSource(int i, MediaSource mediaSource) {
        addMediaSource(i, mediaSource, null);
    }

    public synchronized void addMediaSource(int i, MediaSource mediaSource, @Nullable Runnable runnable) {
        Assertions.checkNotNull(mediaSource);
        Assertions.checkArgument(this.mediaSourcesPublic.contains(mediaSource) ^ 1);
        this.mediaSourcesPublic.add(i, mediaSource);
        if (this.player != null) {
            this.player.createMessage(this).setType(0).setPayload(new MessageData(i, mediaSource, runnable)).send();
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public synchronized void addMediaSources(Collection<MediaSource> collection) {
        addMediaSources(this.mediaSourcesPublic.size(), collection, null);
    }

    public synchronized void addMediaSources(Collection<MediaSource> collection, @Nullable Runnable runnable) {
        addMediaSources(this.mediaSourcesPublic.size(), collection, runnable);
    }

    public synchronized void addMediaSources(int i, Collection<MediaSource> collection) {
        addMediaSources(i, collection, null);
    }

    public synchronized void addMediaSources(int i, Collection<MediaSource> collection, @Nullable Runnable runnable) {
        for (MediaSource mediaSource : collection) {
            Assertions.checkNotNull(mediaSource);
            Assertions.checkArgument(this.mediaSourcesPublic.contains(mediaSource) ^ true);
        }
        this.mediaSourcesPublic.addAll(i, collection);
        if (this.player != null && !collection.isEmpty()) {
            this.player.createMessage(this).setType(1).setPayload(new MessageData(i, collection, runnable)).send();
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public synchronized void removeMediaSource(int i) {
        removeMediaSource(i, null);
    }

    public synchronized void removeMediaSource(int i, @Nullable Runnable runnable) {
        this.mediaSourcesPublic.remove(i);
        if (this.player != null) {
            this.player.createMessage(this).setType(2).setPayload(new MessageData(i, null, runnable)).send();
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public synchronized void moveMediaSource(int i, int i2) {
        moveMediaSource(i, i2, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void moveMediaSource(int r3, int r4, @android.support.annotation.Nullable java.lang.Runnable r5) {
        /*
        r2 = this;
        monitor-enter(r2);
        if (r3 != r4) goto L_0x0005;
    L_0x0003:
        monitor-exit(r2);
        return;
    L_0x0005:
        r0 = r2.mediaSourcesPublic;	 Catch:{ all -> 0x0037 }
        r1 = r2.mediaSourcesPublic;	 Catch:{ all -> 0x0037 }
        r1 = r1.remove(r3);	 Catch:{ all -> 0x0037 }
        r0.add(r4, r1);	 Catch:{ all -> 0x0037 }
        r0 = r2.player;	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x0030;
    L_0x0014:
        r0 = r2.player;	 Catch:{ all -> 0x0037 }
        r0 = r0.createMessage(r2);	 Catch:{ all -> 0x0037 }
        r1 = 3;
        r0 = r0.setType(r1);	 Catch:{ all -> 0x0037 }
        r1 = new com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource$MessageData;	 Catch:{ all -> 0x0037 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x0037 }
        r1.<init>(r3, r4, r5);	 Catch:{ all -> 0x0037 }
        r3 = r0.setPayload(r1);	 Catch:{ all -> 0x0037 }
        r3.send();	 Catch:{ all -> 0x0037 }
        goto L_0x0035;
    L_0x0030:
        if (r5 == 0) goto L_0x0035;
    L_0x0032:
        r5.run();	 Catch:{ all -> 0x0037 }
    L_0x0035:
        monitor-exit(r2);
        return;
    L_0x0037:
        r3 = move-exception;
        monitor-exit(r2);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource.moveMediaSource(int, int, java.lang.Runnable):void");
    }

    public synchronized int getSize() {
        return this.mediaSourcesPublic.size();
    }

    public synchronized MediaSource getMediaSource(int i) {
        return (MediaSource) this.mediaSourcesPublic.get(i);
    }

    public synchronized void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener) {
        super.prepareSource(exoPlayer, z, listener);
        this.player = exoPlayer;
        this.listener = listener;
        this.preventListenerNotification = true;
        this.shuffleOrder = this.shuffleOrder.cloneAndInsert(0, this.mediaSourcesPublic.size());
        addMediaSourcesInternal(0, this.mediaSourcesPublic);
        this.preventListenerNotification = false;
        maybeNotifyListener(null);
    }

    public MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator) {
        MediaPeriod createPeriod;
        MediaSourceHolder mediaSourceHolder = (MediaSourceHolder) this.mediaSourceHolders.get(findMediaSourceHolderByPeriodIndex(mediaPeriodId.periodIndex));
        mediaPeriodId = mediaPeriodId.copyWithPeriodIndex(mediaPeriodId.periodIndex - mediaSourceHolder.firstPeriodIndexInChild);
        if (mediaSourceHolder.isPrepared) {
            createPeriod = mediaSourceHolder.mediaSource.createPeriod(mediaPeriodId, allocator);
        } else {
            createPeriod = new DeferredMediaPeriod(mediaSourceHolder.mediaSource, mediaPeriodId, allocator);
            this.deferredMediaPeriods.add((DeferredMediaPeriod) createPeriod);
        }
        this.mediaSourceByMediaPeriod.put(createPeriod, mediaSourceHolder);
        mediaSourceHolder.activeMediaPeriods++;
        return createPeriod;
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        MediaSourceHolder mediaSourceHolder = (MediaSourceHolder) this.mediaSourceByMediaPeriod.remove(mediaPeriod);
        if (mediaPeriod instanceof DeferredMediaPeriod) {
            this.deferredMediaPeriods.remove(mediaPeriod);
            ((DeferredMediaPeriod) mediaPeriod).releasePeriod();
        } else {
            mediaSourceHolder.mediaSource.releasePeriod(mediaPeriod);
        }
        mediaSourceHolder.activeMediaPeriods--;
        if (mediaSourceHolder.activeMediaPeriods == null && mediaSourceHolder.isRemoved != null) {
            releaseChildSource(mediaSourceHolder);
        }
    }

    public void releaseSource() {
        super.releaseSource();
        this.mediaSourceHolders.clear();
        this.player = null;
        this.listener = null;
        this.shuffleOrder = this.shuffleOrder.cloneAndClear();
        this.windowCount = 0;
        this.periodCount = 0;
    }

    protected void onChildSourceInfoRefreshed(MediaSourceHolder mediaSourceHolder, MediaSource mediaSource, Timeline timeline, @Nullable Object obj) {
        updateMediaSourceInternal(mediaSourceHolder, timeline);
    }

    public void handleMessage(int i, Object obj) throws ExoPlaybackException {
        if (i == 4) {
            ((EventDispatcher) obj).dispatchEvent();
            return;
        }
        this.preventListenerNotification = true;
        MessageData messageData;
        switch (i) {
            case 0:
                messageData = (MessageData) obj;
                this.shuffleOrder = this.shuffleOrder.cloneAndInsert(messageData.index, 1);
                addMediaSourceInternal(messageData.index, (MediaSource) messageData.customData);
                i = messageData.actionOnCompletion;
                break;
            case 1:
                messageData = (MessageData) obj;
                this.shuffleOrder = this.shuffleOrder.cloneAndInsert(messageData.index, ((Collection) messageData.customData).size());
                addMediaSourcesInternal(messageData.index, (Collection) messageData.customData);
                i = messageData.actionOnCompletion;
                break;
            case 2:
                messageData = (MessageData) obj;
                this.shuffleOrder = this.shuffleOrder.cloneAndRemove(messageData.index);
                removeMediaSourceInternal(messageData.index);
                i = messageData.actionOnCompletion;
                break;
            case 3:
                messageData = (MessageData) obj;
                this.shuffleOrder = this.shuffleOrder.cloneAndRemove(messageData.index);
                this.shuffleOrder = this.shuffleOrder.cloneAndInsert(((Integer) messageData.customData).intValue(), 1);
                moveMediaSourceInternal(messageData.index, ((Integer) messageData.customData).intValue());
                i = messageData.actionOnCompletion;
                break;
            default:
                throw new IllegalStateException();
        }
        this.preventListenerNotification = null;
        maybeNotifyListener(i);
    }

    private void maybeNotifyListener(@Nullable EventDispatcher eventDispatcher) {
        if (!this.preventListenerNotification) {
            this.listener.onSourceInfoRefreshed(this, new ConcatenatedTimeline(this.mediaSourceHolders, this.windowCount, this.periodCount, this.shuffleOrder, this.isAtomic), null);
            if (eventDispatcher != null) {
                this.player.createMessage(this).setType(4).setPayload(eventDispatcher).send();
            }
        }
    }

    private void addMediaSourceInternal(int i, MediaSource mediaSource) {
        DeferredTimeline deferredTimeline = new DeferredTimeline();
        MediaSourceHolder mediaSourceHolder;
        if (i > 0) {
            mediaSourceHolder = (MediaSourceHolder) this.mediaSourceHolders.get(i - 1);
            mediaSourceHolder = new MediaSourceHolder(mediaSource, deferredTimeline, i, mediaSourceHolder.firstWindowIndexInChild + mediaSourceHolder.timeline.getWindowCount(), mediaSourceHolder.firstPeriodIndexInChild + mediaSourceHolder.timeline.getPeriodCount());
        } else {
            mediaSourceHolder = new MediaSourceHolder(mediaSource, deferredTimeline, 0, 0, 0);
        }
        correctOffsets(i, 1, deferredTimeline.getWindowCount(), deferredTimeline.getPeriodCount());
        this.mediaSourceHolders.add(i, r7);
        prepareChildSource(r7, r7.mediaSource);
    }

    private void addMediaSourcesInternal(int i, Collection<MediaSource> collection) {
        for (MediaSource addMediaSourceInternal : collection) {
            int i2 = i + 1;
            addMediaSourceInternal(i, addMediaSourceInternal);
            i = i2;
        }
    }

    private void updateMediaSourceInternal(MediaSourceHolder mediaSourceHolder, Timeline timeline) {
        if (mediaSourceHolder == null) {
            throw new IllegalArgumentException();
        }
        DeferredTimeline deferredTimeline = mediaSourceHolder.timeline;
        if (deferredTimeline.getTimeline() != timeline) {
            int windowCount = timeline.getWindowCount() - deferredTimeline.getWindowCount();
            int periodCount = timeline.getPeriodCount() - deferredTimeline.getPeriodCount();
            if (!(windowCount == 0 && periodCount == 0)) {
                correctOffsets(mediaSourceHolder.childIndex + 1, 0, windowCount, periodCount);
            }
            mediaSourceHolder.timeline = deferredTimeline.cloneWithNewTimeline(timeline);
            if (mediaSourceHolder.isPrepared == null) {
                for (timeline = this.deferredMediaPeriods.size() - 1; timeline >= null; timeline--) {
                    if (((DeferredMediaPeriod) this.deferredMediaPeriods.get(timeline)).mediaSource == mediaSourceHolder.mediaSource) {
                        ((DeferredMediaPeriod) this.deferredMediaPeriods.get(timeline)).createPeriod();
                        this.deferredMediaPeriods.remove(timeline);
                    }
                }
            }
            mediaSourceHolder.isPrepared = true;
            maybeNotifyListener(null);
        }
    }

    private void removeMediaSourceInternal(int i) {
        MediaSourceHolder mediaSourceHolder = (MediaSourceHolder) this.mediaSourceHolders.get(i);
        this.mediaSourceHolders.remove(i);
        Timeline timeline = mediaSourceHolder.timeline;
        correctOffsets(i, -1, -timeline.getWindowCount(), -timeline.getPeriodCount());
        mediaSourceHolder.isRemoved = true;
        if (mediaSourceHolder.activeMediaPeriods == 0) {
            releaseChildSource(mediaSourceHolder);
        }
    }

    private void moveMediaSourceInternal(int i, int i2) {
        int min = Math.min(i, i2);
        int max = Math.max(i, i2);
        int i3 = ((MediaSourceHolder) this.mediaSourceHolders.get(min)).firstWindowIndexInChild;
        int i4 = ((MediaSourceHolder) this.mediaSourceHolders.get(min)).firstPeriodIndexInChild;
        this.mediaSourceHolders.add(i2, this.mediaSourceHolders.remove(i));
        while (min <= max) {
            MediaSourceHolder mediaSourceHolder = (MediaSourceHolder) this.mediaSourceHolders.get(min);
            mediaSourceHolder.firstWindowIndexInChild = i3;
            mediaSourceHolder.firstPeriodIndexInChild = i4;
            i3 += mediaSourceHolder.timeline.getWindowCount();
            i4 += mediaSourceHolder.timeline.getPeriodCount();
            min++;
        }
    }

    private void correctOffsets(int i, int i2, int i3, int i4) {
        this.windowCount += i3;
        this.periodCount += i4;
        while (i < this.mediaSourceHolders.size()) {
            MediaSourceHolder mediaSourceHolder = (MediaSourceHolder) this.mediaSourceHolders.get(i);
            mediaSourceHolder.childIndex += i2;
            mediaSourceHolder = (MediaSourceHolder) this.mediaSourceHolders.get(i);
            mediaSourceHolder.firstWindowIndexInChild += i3;
            mediaSourceHolder = (MediaSourceHolder) this.mediaSourceHolders.get(i);
            mediaSourceHolder.firstPeriodIndexInChild += i4;
            i++;
        }
    }

    private int findMediaSourceHolderByPeriodIndex(int i) {
        this.query.firstPeriodIndexInChild = i;
        int binarySearch = Collections.binarySearch(this.mediaSourceHolders, this.query);
        if (binarySearch < 0) {
            return (-binarySearch) - 2;
        }
        while (binarySearch < this.mediaSourceHolders.size() - 1) {
            int i2 = binarySearch + 1;
            if (((MediaSourceHolder) this.mediaSourceHolders.get(i2)).firstPeriodIndexInChild != i) {
                break;
            }
            binarySearch = i2;
        }
        return binarySearch;
    }
}
