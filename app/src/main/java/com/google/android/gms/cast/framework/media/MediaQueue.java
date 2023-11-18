package com.google.android.gms.cast.framework.media;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.LruCache;
import android.util.SparseIntArray;
import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.cast.zzcu;
import com.google.android.gms.internal.cast.zzdg;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;

@MainThread
public class MediaQueue {
    private final Handler handler;
    @VisibleForTesting
    long zzec;
    private final RemoteMediaClient zzhp;
    private final zzdg zzlc;
    @VisibleForTesting
    private boolean zzld;
    @VisibleForTesting
    List<Integer> zzle;
    @VisibleForTesting
    final SparseIntArray zzlf;
    @VisibleForTesting
    LruCache<Integer, MediaQueueItem> zzlg;
    @VisibleForTesting
    final List<Integer> zzlh;
    @VisibleForTesting
    final Deque<Integer> zzli;
    private final int zzlj;
    private TimerTask zzlk;
    @VisibleForTesting
    PendingResult<MediaChannelResult> zzll;
    @VisibleForTesting
    PendingResult<MediaChannelResult> zzlm;
    @VisibleForTesting
    private ResultCallback<MediaChannelResult> zzln;
    @VisibleForTesting
    private ResultCallback<MediaChannelResult> zzlo;
    @VisibleForTesting
    private zzd zzlp;
    @VisibleForTesting
    private SessionManagerListener<CastSession> zzlq;
    private Set<Callback> zzlr;

    public static abstract class Callback {
        public void itemsInsertedInRange(int i, int i2) {
        }

        public void itemsReloaded() {
        }

        public void itemsRemovedAtIndexes(int[] iArr) {
        }

        public void itemsUpdatedAtIndexes(int[] iArr) {
        }

        public void mediaQueueChanged() {
        }

        public void mediaQueueWillChange() {
        }
    }

    private class zza implements ResultCallback<MediaChannelResult> {
        private final /* synthetic */ MediaQueue zzls;

        private zza(MediaQueue mediaQueue) {
            this.zzls = mediaQueue;
        }

        public final /* synthetic */ void onResult(@NonNull Result result) {
            if (((MediaChannelResult) result).getStatus().getStatusCode() != 0) {
                this.zzls.zzlc.m28w(String.format("Error fetching queue item ids, statusCode=%s, statusMessage=%s", new Object[]{Integer.valueOf(r0), r6.getStatusMessage()}), new Object[0]);
            }
            this.zzls.zzlm = null;
            if (!this.zzls.zzli.isEmpty()) {
                this.zzls.zzay();
            }
        }
    }

    private class zzb implements ResultCallback<MediaChannelResult> {
        private final /* synthetic */ MediaQueue zzls;

        private zzb(MediaQueue mediaQueue) {
            this.zzls = mediaQueue;
        }

        public final /* synthetic */ void onResult(@NonNull Result result) {
            if (((MediaChannelResult) result).getStatus().getStatusCode() != 0) {
                this.zzls.zzlc.m28w(String.format("Error fetching queue items, statusCode=%s, statusMessage=%s", new Object[]{Integer.valueOf(r0), r6.getStatusMessage()}), new Object[0]);
            }
            this.zzls.zzll = null;
            if (!this.zzls.zzli.isEmpty()) {
                this.zzls.zzay();
            }
        }
    }

    private class zzc implements SessionManagerListener<CastSession> {
        private final /* synthetic */ MediaQueue zzls;

        private zzc(MediaQueue mediaQueue) {
            this.zzls = mediaQueue;
        }

        public final /* synthetic */ void onSessionEnded(Session session, int i) {
            this.zzls.zzbc();
            this.zzls.clear();
        }

        public final /* synthetic */ void onSessionEnding(Session session) {
            this.zzls.zzbc();
            this.zzls.clear();
        }

        public final /* bridge */ /* synthetic */ void onSessionResumeFailed(Session session, int i) {
        }

        public final /* synthetic */ void onSessionResumed(Session session, boolean z) {
            CastSession castSession = (CastSession) session;
            if (castSession.getRemoteMediaClient() != null) {
                this.zzls.zza(castSession.getRemoteMediaClient());
            }
        }

        public final /* bridge */ /* synthetic */ void onSessionResuming(Session session, String str) {
        }

        public final /* bridge */ /* synthetic */ void onSessionStartFailed(Session session, int i) {
        }

        public final /* synthetic */ void onSessionStarted(Session session, String str) {
            this.zzls.zza(((CastSession) session).getRemoteMediaClient());
        }

        public final /* bridge */ /* synthetic */ void onSessionStarting(Session session) {
        }

        public final /* synthetic */ void onSessionSuspended(Session session, int i) {
            this.zzls.zzbc();
        }
    }

    @VisibleForTesting
    class zzd extends com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback {
        private final /* synthetic */ MediaQueue zzls;

        zzd(MediaQueue mediaQueue) {
            this.zzls = mediaQueue;
        }

        public final void onStatusUpdated() {
            long zza = MediaQueue.zzb(this.zzls.zzhp);
            if (zza != this.zzls.zzec) {
                this.zzls.zzec = zza;
                this.zzls.clear();
                if (this.zzls.zzec != 0) {
                    this.zzls.reload();
                }
            }
        }

        public final void zza(int[] iArr) {
            List zzg = zzcu.zzg(iArr);
            if (!this.zzls.zzle.equals(zzg)) {
                this.zzls.zzbf();
                this.zzls.zzlg.evictAll();
                this.zzls.zzlh.clear();
                this.zzls.zzle = zzg;
                this.zzls.zzbe();
                this.zzls.zzbh();
                this.zzls.zzbg();
            }
        }

        public final void zza(int[] iArr, int i) {
            int length = iArr.length;
            if (i == 0) {
                i = this.zzls.zzle.size();
            } else {
                i = this.zzls.zzlf.get(i, -1);
                if (i == -1) {
                    this.zzls.reload();
                    return;
                }
            }
            this.zzls.zzbf();
            this.zzls.zzle.addAll(i, zzcu.zzg(iArr));
            this.zzls.zzbe();
            this.zzls.zzb(i, length);
            this.zzls.zzbg();
        }

        public final void zzb(int[] iArr) {
            Collection arrayList = new ArrayList();
            int length = iArr.length;
            int i = 0;
            while (i < length) {
                int i2 = iArr[i];
                this.zzls.zzlg.remove(Integer.valueOf(i2));
                i2 = this.zzls.zzlf.get(i2, -1);
                if (i2 == -1) {
                    this.zzls.reload();
                    return;
                } else {
                    arrayList.add(Integer.valueOf(i2));
                    i++;
                }
            }
            Collections.sort(arrayList);
            this.zzls.zzbf();
            this.zzls.zzd(zzcu.zza(arrayList));
            this.zzls.zzbg();
        }

        public final void zzb(MediaQueueItem[] mediaQueueItemArr) {
            Collection hashSet = new HashSet();
            this.zzls.zzlh.clear();
            int length = mediaQueueItemArr.length;
            int i = 0;
            while (i < length) {
                MediaQueueItem mediaQueueItem = mediaQueueItemArr[i];
                int itemId = mediaQueueItem.getItemId();
                this.zzls.zzlg.put(Integer.valueOf(itemId), mediaQueueItem);
                int i2 = this.zzls.zzlf.get(itemId, -1);
                if (i2 == -1) {
                    this.zzls.reload();
                    return;
                } else {
                    hashSet.add(Integer.valueOf(i2));
                    i++;
                }
            }
            for (Integer intValue : this.zzls.zzlh) {
                length = this.zzls.zzlf.get(intValue.intValue(), -1);
                if (length != -1) {
                    hashSet.add(Integer.valueOf(length));
                }
            }
            this.zzls.zzlh.clear();
            Collection arrayList = new ArrayList(hashSet);
            Collections.sort(arrayList);
            this.zzls.zzbf();
            this.zzls.zzd(zzcu.zza(arrayList));
            this.zzls.zzbg();
        }

        public final void zzc(int[] iArr) {
            Collection arrayList = new ArrayList();
            for (int i : iArr) {
                this.zzls.zzlg.remove(Integer.valueOf(i));
                int i2 = this.zzls.zzlf.get(i, -1);
                if (i2 == -1) {
                    this.zzls.reload();
                    return;
                }
                this.zzls.zzlf.delete(i);
                arrayList.add(Integer.valueOf(i2));
            }
            if (!arrayList.isEmpty()) {
                Collections.sort(arrayList);
                this.zzls.zzbf();
                this.zzls.zzle.removeAll(zzcu.zzg(iArr));
                this.zzls.zzbe();
                this.zzls.zze(zzcu.zza(arrayList));
                this.zzls.zzbg();
            }
        }
    }

    MediaQueue(@NonNull RemoteMediaClient remoteMediaClient) {
        this(remoteMediaClient, 20, 20);
    }

    @VisibleForTesting
    private MediaQueue(@NonNull RemoteMediaClient remoteMediaClient, int i, int i2) {
        this.zzlr = new HashSet();
        this.zzlc = new zzdg("MediaQueue");
        this.zzhp = remoteMediaClient;
        this.zzlj = Math.max(20, 1);
        Session currentCastSession = CastContext.getSharedInstance().getSessionManager().getCurrentCastSession();
        this.zzle = new ArrayList();
        this.zzlf = new SparseIntArray();
        this.zzlh = new ArrayList();
        this.zzli = new ArrayDeque(20);
        this.handler = new Handler(Looper.getMainLooper());
        zzh(20);
        this.zzlk = new zzk(this);
        this.zzln = new zzb();
        this.zzlo = new zza();
        this.zzlp = new zzd(this);
        this.zzlq = new zzc();
        CastContext.getSharedInstance().getSessionManager().addSessionManagerListener(this.zzlq, CastSession.class);
        if (currentCastSession != null && currentCastSession.isConnected()) {
            zza(currentCastSession.getRemoteMediaClient());
        }
    }

    private final void zzaz() {
        this.handler.removeCallbacks(this.zzlk);
    }

    private static long zzb(RemoteMediaClient remoteMediaClient) {
        MediaStatus mediaStatus = remoteMediaClient.getMediaStatus();
        if (mediaStatus != null) {
            if (!mediaStatus.zzk()) {
                return mediaStatus.zzj();
            }
        }
        return 0;
    }

    private final void zzb(int i, int i2) {
        for (Callback itemsInsertedInRange : this.zzlr) {
            itemsInsertedInRange.itemsInsertedInRange(i, i2);
        }
    }

    private final void zzba() {
        if (this.zzlm != null) {
            this.zzlm.cancel();
            this.zzlm = null;
        }
    }

    private final void zzbb() {
        if (this.zzll != null) {
            this.zzll.cancel();
            this.zzll = null;
        }
    }

    private final void zzbe() {
        this.zzlf.clear();
        for (int i = 0; i < this.zzle.size(); i++) {
            this.zzlf.put(((Integer) this.zzle.get(i)).intValue(), i);
        }
    }

    private final void zzbf() {
        for (Callback mediaQueueWillChange : this.zzlr) {
            mediaQueueWillChange.mediaQueueWillChange();
        }
    }

    private final void zzbg() {
        for (Callback mediaQueueChanged : this.zzlr) {
            mediaQueueChanged.mediaQueueChanged();
        }
    }

    private final void zzbh() {
        for (Callback itemsReloaded : this.zzlr) {
            itemsReloaded.itemsReloaded();
        }
    }

    private final void zzd(int[] iArr) {
        for (Callback itemsUpdatedAtIndexes : this.zzlr) {
            itemsUpdatedAtIndexes.itemsUpdatedAtIndexes(iArr);
        }
    }

    private final void zze(int[] iArr) {
        for (Callback itemsRemovedAtIndexes : this.zzlr) {
            itemsRemovedAtIndexes.itemsRemovedAtIndexes(iArr);
        }
    }

    private final void zzh(int i) {
        this.zzlg = new zzl(this, i);
    }

    @VisibleForTesting
    public final void clear() {
        zzbf();
        this.zzle.clear();
        this.zzlf.clear();
        this.zzlg.evictAll();
        this.zzlh.clear();
        zzaz();
        this.zzli.clear();
        zzba();
        zzbb();
        zzbh();
        zzbg();
    }

    public PendingResult<MediaChannelResult> fetchMoreItemsRelativeToIndex(int i, int i2, int i3) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (this.zzld) {
            if (this.zzec != 0) {
                i = itemIdAtIndex(i);
                return i == 0 ? RemoteMediaClient.zza((int) CastStatusCodes.INVALID_REQUEST, "index out of bound") : this.zzhp.zza(i, i2, i3);
            }
        }
        return RemoteMediaClient.zza(2100, "No active media session");
    }

    @Nullable
    public MediaQueueItem getItemAtIndex(int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return getItemAtIndex(i, true);
    }

    @Nullable
    public MediaQueueItem getItemAtIndex(int i, boolean z) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (i >= 0) {
            if (i < this.zzle.size()) {
                i = ((Integer) this.zzle.get(i)).intValue();
                MediaQueueItem mediaQueueItem = (MediaQueueItem) this.zzlg.get(Integer.valueOf(i));
                if (mediaQueueItem == null && z && !this.zzli.contains(Integer.valueOf(i))) {
                    while (this.zzli.size() >= this.zzlj) {
                        this.zzli.removeFirst();
                    }
                    this.zzli.add(Integer.valueOf(i));
                    zzay();
                }
                return mediaQueueItem;
            }
        }
        return null;
    }

    public int getItemCount() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzle.size();
    }

    public int[] getItemIds() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return zzcu.zza(this.zzle);
    }

    public int indexOfItemWithId(int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzlf.get(i, -1);
    }

    public int itemIdAtIndex(int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return (i < 0 || i >= this.zzle.size()) ? 0 : ((Integer) this.zzle.get(i)).intValue();
    }

    public void registerCallback(Callback callback) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        this.zzlr.add(callback);
    }

    public final void reload() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (this.zzld && this.zzec != 0 && this.zzlm == null) {
            zzba();
            zzbb();
            this.zzlm = this.zzhp.zzbm();
            this.zzlm.setResultCallback(this.zzlo);
        }
    }

    public void setCacheCapacity(int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        LruCache lruCache = this.zzlg;
        Collection arrayList = new ArrayList();
        zzh(i);
        int size = lruCache.size();
        for (Entry entry : lruCache.snapshot().entrySet()) {
            if (size > i) {
                int i2 = this.zzlf.get(((Integer) entry.getKey()).intValue(), -1);
                if (i2 != -1) {
                    arrayList.add(Integer.valueOf(i2));
                }
            } else {
                this.zzlg.put((Integer) entry.getKey(), (MediaQueueItem) entry.getValue());
            }
            size--;
        }
        if (!arrayList.isEmpty()) {
            Collections.sort(arrayList);
            zzbf();
            zzd(zzcu.zza(arrayList));
            zzbg();
        }
    }

    public void unregisterCallback(Callback callback) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        this.zzlr.remove(callback);
    }

    @VisibleForTesting
    final void zza(RemoteMediaClient remoteMediaClient) {
        if (remoteMediaClient != null && this.zzhp == remoteMediaClient) {
            this.zzld = true;
            remoteMediaClient.registerCallback(this.zzlp);
            long zzb = zzb(remoteMediaClient);
            this.zzec = zzb;
            if (zzb != 0) {
                reload();
            }
        }
    }

    public final void zzay() {
        zzaz();
        this.handler.postDelayed(this.zzlk, 500);
    }

    @VisibleForTesting
    final void zzbc() {
        this.zzhp.unregisterCallback(this.zzlp);
        this.zzld = false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.VisibleForTesting
    final void zzbd() {
        /*
        r5 = this;
        r0 = r5.zzli;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r0 = r5.zzll;
        if (r0 != 0) goto L_0x0035;
    L_0x000d:
        r0 = r5.zzld;
        if (r0 == 0) goto L_0x0035;
    L_0x0011:
        r0 = r5.zzec;
        r2 = 0;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 != 0) goto L_0x001a;
    L_0x0019:
        return;
    L_0x001a:
        r0 = r5.zzhp;
        r1 = r5.zzli;
        r1 = com.google.android.gms.internal.cast.zzcu.zza(r1);
        r0 = r0.zzf(r1);
        r5.zzll = r0;
        r0 = r5.zzll;
        r1 = r5.zzln;
        r0.setResultCallback(r1);
        r0 = r5.zzli;
        r0.clear();
        return;
    L_0x0035:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.MediaQueue.zzbd():void");
    }
}
