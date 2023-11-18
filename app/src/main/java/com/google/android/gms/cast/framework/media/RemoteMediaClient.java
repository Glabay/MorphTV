package com.google.android.gms.cast.framework.media;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.cast.AdBreakInfo;
import com.google.android.gms.cast.Cast.CastApi;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadOptions;
import com.google.android.gms.cast.MediaLoadOptions.Builder;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.cast.zzbp;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.cast.zzcf;
import com.google.android.gms.internal.cast.zzcn;
import com.google.android.gms.internal.cast.zzdh;
import com.google.android.gms.internal.cast.zzdl;
import com.google.android.gms.internal.cast.zzdm;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONObject;

public class RemoteMediaClient implements MessageReceivedCallback {
    public static final String NAMESPACE = zzdh.NAMESPACE;
    public static final int RESUME_STATE_PAUSE = 2;
    public static final int RESUME_STATE_PLAY = 1;
    public static final int RESUME_STATE_UNCHANGED = 0;
    public static final int STATUS_FAILED = 2100;
    public static final int STATUS_REPLACED = 2103;
    public static final int STATUS_SUCCEEDED = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Object lock = new Object();
    private final zzdh zzeu;
    private final CastApi zzhl;
    private final MediaQueue zzlt;
    private final zza zznl = new zza(this);
    private GoogleApiClient zznm;
    private final List<Listener> zznn = new CopyOnWriteArrayList();
    @VisibleForTesting
    final List<Callback> zzno = new CopyOnWriteArrayList();
    private final Map<ProgressListener, zze> zznp = new ConcurrentHashMap();
    private final Map<Long, zze> zznq = new ConcurrentHashMap();
    private ParseAdsInfoCallback zznr;

    public static abstract class Callback {
        public void onAdBreakStatusUpdated() {
        }

        public void onMetadataUpdated() {
        }

        public void onPreloadStatusUpdated() {
        }

        public void onQueueStatusUpdated() {
        }

        public void onSendingRemoteMediaRequest() {
        }

        public void onStatusUpdated() {
        }

        public void zza(int[] iArr) {
        }

        public void zza(int[] iArr, int i) {
        }

        public void zzb(int[] iArr) {
        }

        public void zzb(MediaQueueItem[] mediaQueueItemArr) {
        }

        public void zzc(int[] iArr) {
        }
    }

    public interface ProgressListener {
        void onProgressUpdated(long j, long j2);
    }

    @Deprecated
    public interface Listener {
        void onAdBreakStatusUpdated();

        void onMetadataUpdated();

        void onPreloadStatusUpdated();

        void onQueueStatusUpdated();

        void onSendingRemoteMediaRequest();

        void onStatusUpdated();
    }

    public interface MediaChannelResult extends Result {
        JSONObject getCustomData();
    }

    public interface ParseAdsInfoCallback {
        List<AdBreakInfo> parseAdBreaksFromMediaStatus(MediaStatus mediaStatus);

        boolean parseIsPlayingAdFromMediaStatus(MediaStatus mediaStatus);
    }

    private class zza implements zzdl {
        private GoogleApiClient zzfy;
        private long zzfz = 0;
        final /* synthetic */ RemoteMediaClient zzns;

        public zza(RemoteMediaClient remoteMediaClient) {
            this.zzns = remoteMediaClient;
        }

        public final void zza(GoogleApiClient googleApiClient) {
            this.zzfy = googleApiClient;
        }

        public final void zza(String str, String str2, long j, String str3) {
            if (this.zzfy == null) {
                throw new IllegalStateException("No GoogleApiClient available");
            }
            this.zzns.zzhl.sendMessage(this.zzfy, str, str2).setResultCallback(new zzau(this, j));
        }

        public final long zzl() {
            long j = this.zzfz + 1;
            this.zzfz = j;
            return j;
        }
    }

    private static class zzb extends BasePendingResult<MediaChannelResult> {
        zzb() {
            super(null);
        }

        @NonNull
        protected final MediaChannelResult a_(Status status) {
            return new zzav(this, status);
        }

        @NonNull
        protected final /* synthetic */ Result createFailedResult(Status status) {
            return a_(status);
        }
    }

    @VisibleForTesting
    abstract class zzc extends zzcf<MediaChannelResult> {
        zzdm zzgc;
        private final /* synthetic */ RemoteMediaClient zzns;
        private final boolean zzoc;

        zzc(RemoteMediaClient remoteMediaClient, GoogleApiClient googleApiClient) {
            this(remoteMediaClient, googleApiClient, false);
        }

        zzc(RemoteMediaClient remoteMediaClient, GoogleApiClient googleApiClient, boolean z) {
            this.zzns = remoteMediaClient;
            super(googleApiClient);
            this.zzoc = z;
            this.zzgc = new zzaw(this, remoteMediaClient);
        }

        public /* synthetic */ Result createFailedResult(Status status) {
            return new zzax(this, status);
        }

        protected /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
            zzcn zzcn = (zzcn) anyClient;
            if (!this.zzoc) {
                for (Listener onSendingRemoteMediaRequest : this.zzns.zznn) {
                    onSendingRemoteMediaRequest.onSendingRemoteMediaRequest();
                }
                for (Callback onSendingRemoteMediaRequest2 : this.zzns.zzno) {
                    onSendingRemoteMediaRequest2.onSendingRemoteMediaRequest();
                }
            }
            zzb(zzcn);
        }

        abstract void zzb(zzcn zzcn);
    }

    private static final class zzd implements MediaChannelResult {
        private final Status zzge;
        private final JSONObject zzp;

        zzd(Status status, JSONObject jSONObject) {
            this.zzge = status;
            this.zzp = jSONObject;
        }

        public final JSONObject getCustomData() {
            return this.zzp;
        }

        public final Status getStatus() {
            return this.zzge;
        }
    }

    private class zze {
        final /* synthetic */ RemoteMediaClient zzns;
        private final Set<ProgressListener> zzof = new HashSet();
        private final long zzog;
        private final Runnable zzoh;
        private boolean zzoi;

        public zze(RemoteMediaClient remoteMediaClient, long j) {
            this.zzns = remoteMediaClient;
            this.zzog = j;
            this.zzoh = new zzay(this, remoteMediaClient);
        }

        public final boolean hasListener() {
            return !this.zzof.isEmpty();
        }

        public final boolean isStarted() {
            return this.zzoi;
        }

        public final void start() {
            this.zzns.handler.removeCallbacks(this.zzoh);
            this.zzoi = true;
            this.zzns.handler.postDelayed(this.zzoh, this.zzog);
        }

        public final void stop() {
            this.zzns.handler.removeCallbacks(this.zzoh);
            this.zzoi = false;
        }

        public final void zza(ProgressListener progressListener) {
            this.zzof.add(progressListener);
        }

        public final void zzb(ProgressListener progressListener) {
            this.zzof.remove(progressListener);
        }

        public final long zzbq() {
            return this.zzog;
        }
    }

    public RemoteMediaClient(@NonNull zzdh zzdh, @NonNull CastApi castApi) {
        this.zzhl = castApi;
        this.zzeu = (zzdh) Preconditions.checkNotNull(zzdh);
        this.zzeu.zza(new zzr(this));
        this.zzeu.zza(this.zznl);
        this.zzlt = new MediaQueue(this);
    }

    private final com.google.android.gms.cast.framework.media.RemoteMediaClient.zzc zza(com.google.android.gms.cast.framework.media.RemoteMediaClient.zzc r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = r2.zznm;	 Catch:{ IllegalStateException -> 0x0006 }
        r0.execute(r3);	 Catch:{ IllegalStateException -> 0x0006 }
        return r3;
    L_0x0006:
        r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0016 }
        r1 = 2100; // 0x834 float:2.943E-42 double:1.0375E-320;	 Catch:{ all -> 0x0016 }
        r0.<init>(r1);	 Catch:{ all -> 0x0016 }
        r0 = r3.createFailedResult(r0);	 Catch:{ all -> 0x0016 }
        r0 = (com.google.android.gms.cast.framework.media.RemoteMediaClient.MediaChannelResult) r0;	 Catch:{ all -> 0x0016 }
        r3.setResult(r0);	 Catch:{ all -> 0x0016 }
    L_0x0016:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.RemoteMediaClient.zza(com.google.android.gms.cast.framework.media.RemoteMediaClient$zzc):com.google.android.gms.cast.framework.media.RemoteMediaClient$zzc");
    }

    public static PendingResult<MediaChannelResult> zza(int i, String str) {
        PendingResult zzb = new zzb();
        zzb.setResult(zzb.a_(new Status(i, str)));
        return zzb;
    }

    private final void zza(Set<ProgressListener> set) {
        Set<ProgressListener> hashSet = new HashSet(set);
        if (!(isPlaying() || isPaused())) {
            if (!isBuffering()) {
                if (isLoadingNextItem()) {
                    MediaQueueItem loadingItem = getLoadingItem();
                    if (!(loadingItem == null || loadingItem.getMedia() == null)) {
                        for (ProgressListener onProgressUpdated : hashSet) {
                            onProgressUpdated.onProgressUpdated(0, loadingItem.getMedia().getStreamDuration());
                        }
                    }
                    return;
                }
                for (ProgressListener onProgressUpdated2 : hashSet) {
                    onProgressUpdated2.onProgressUpdated(0, 0);
                }
                return;
            }
        }
        for (ProgressListener onProgressUpdated22 : hashSet) {
            onProgressUpdated22.onProgressUpdated(getApproximateStreamPosition(), getStreamDuration());
        }
    }

    private final boolean zzbn() {
        return this.zznm != null;
    }

    private final void zzbo() {
        for (zze zze : this.zznq.values()) {
            if (hasMediaSession() && !zze.isStarted()) {
                zze.start();
            } else if (!hasMediaSession() && zze.isStarted()) {
                zze.stop();
            }
            if (zze.isStarted() && (isBuffering() || isPaused() || isLoadingNextItem())) {
                zza(zze.zzof);
            }
        }
    }

    private final int zzc(int i) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        for (int i2 = 0; i2 < mediaStatus.getQueueItemCount(); i2++) {
            if (mediaStatus.getQueueItem(i2).getItemId() == i) {
                return i2;
            }
        }
        return -1;
    }

    @Deprecated
    public void addListener(Listener listener) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (listener != null) {
            this.zznn.add(listener);
        }
    }

    public boolean addProgressListener(ProgressListener progressListener, long j) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (progressListener != null) {
            if (!this.zznp.containsKey(progressListener)) {
                zze zze = (zze) this.zznq.get(Long.valueOf(j));
                if (zze == null) {
                    zze = new zze(this, j);
                    this.zznq.put(Long.valueOf(j), zze);
                }
                zze.zza(progressListener);
                this.zznp.put(progressListener, zze);
                if (hasMediaSession()) {
                    zze.start();
                }
                return true;
            }
        }
        return false;
    }

    public long getApproximateAdBreakClipPositionMs() {
        long approximateAdBreakClipPositionMs;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            approximateAdBreakClipPositionMs = this.zzeu.getApproximateAdBreakClipPositionMs();
        }
        return approximateAdBreakClipPositionMs;
    }

    public long getApproximateStreamPosition() {
        long approximateStreamPosition;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            approximateStreamPosition = this.zzeu.getApproximateStreamPosition();
        }
        return approximateStreamPosition;
    }

    public MediaQueueItem getCurrentItem() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return mediaStatus == null ? null : mediaStatus.getQueueItemById(mediaStatus.getCurrentItemId());
    }

    public int getIdleReason() {
        int idleReason;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            MediaStatus mediaStatus = getMediaStatus();
            idleReason = mediaStatus != null ? mediaStatus.getIdleReason() : 0;
        }
        return idleReason;
    }

    public MediaQueueItem getLoadingItem() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return mediaStatus == null ? null : mediaStatus.getQueueItemById(mediaStatus.getLoadingItemId());
    }

    public MediaInfo getMediaInfo() {
        MediaInfo mediaInfo;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            mediaInfo = this.zzeu.getMediaInfo();
        }
        return mediaInfo;
    }

    public MediaQueue getMediaQueue() {
        MediaQueue mediaQueue;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            mediaQueue = this.zzlt;
        }
        return mediaQueue;
    }

    public MediaStatus getMediaStatus() {
        MediaStatus mediaStatus;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            mediaStatus = this.zzeu.getMediaStatus();
        }
        return mediaStatus;
    }

    public String getNamespace() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return this.zzeu.getNamespace();
    }

    public int getPlayerState() {
        int playerState;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            MediaStatus mediaStatus = getMediaStatus();
            playerState = mediaStatus != null ? mediaStatus.getPlayerState() : 1;
        }
        return playerState;
    }

    public MediaQueueItem getPreloadedItem() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return mediaStatus == null ? null : mediaStatus.getQueueItemById(mediaStatus.getPreloadedItemId());
    }

    public long getStreamDuration() {
        long streamDuration;
        synchronized (this.lock) {
            Preconditions.checkMainThread("Must be called from the main thread.");
            streamDuration = this.zzeu.getStreamDuration();
        }
        return streamDuration;
    }

    public boolean hasMediaSession() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (!(isBuffering() || isPlaying() || isPaused())) {
            if (!isLoadingNextItem()) {
                return false;
            }
        }
        return true;
    }

    public boolean isBuffering() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return mediaStatus != null && mediaStatus.getPlayerState() == 4;
    }

    public boolean isLiveStream() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaInfo mediaInfo = getMediaInfo();
        return mediaInfo != null && mediaInfo.getStreamType() == 2;
    }

    public boolean isLoadingNextItem() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return (mediaStatus == null || mediaStatus.getLoadingItemId() == 0) ? false : true;
    }

    public boolean isPaused() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return mediaStatus != null && (mediaStatus.getPlayerState() == 3 || (isLiveStream() && getIdleReason() == 2));
    }

    public boolean isPlaying() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return mediaStatus != null && mediaStatus.getPlayerState() == 2;
    }

    public boolean isPlayingAd() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        MediaStatus mediaStatus = getMediaStatus();
        return mediaStatus != null && mediaStatus.isPlayingAd();
    }

    @Deprecated
    public PendingResult<MediaChannelResult> load(MediaInfo mediaInfo) {
        return load(mediaInfo, new Builder().build());
    }

    public PendingResult<MediaChannelResult> load(MediaInfo mediaInfo, MediaLoadOptions mediaLoadOptions) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzac(this, this.zznm, mediaInfo, mediaLoadOptions));
    }

    @Deprecated
    public PendingResult<MediaChannelResult> load(MediaInfo mediaInfo, boolean z) {
        return load(mediaInfo, new Builder().setAutoplay(z).build());
    }

    @Deprecated
    public PendingResult<MediaChannelResult> load(MediaInfo mediaInfo, boolean z, long j) {
        return load(mediaInfo, new Builder().setAutoplay(z).setPlayPosition(j).build());
    }

    @Deprecated
    public PendingResult<MediaChannelResult> load(MediaInfo mediaInfo, boolean z, long j, JSONObject jSONObject) {
        return load(mediaInfo, new Builder().setAutoplay(z).setPlayPosition(j).setCustomData(jSONObject).build());
    }

    @Deprecated
    public PendingResult<MediaChannelResult> load(MediaInfo mediaInfo, boolean z, long j, long[] jArr, JSONObject jSONObject) {
        return load(mediaInfo, new Builder().setAutoplay(z).setPlayPosition(j).setActiveTrackIds(jArr).setCustomData(jSONObject).build());
    }

    public void onMessageReceived(CastDevice castDevice, String str, String str2) {
        this.zzeu.zzn(str2);
    }

    public PendingResult<MediaChannelResult> pause() {
        return pause(null);
    }

    public PendingResult<MediaChannelResult> pause(JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzan(this, this.zznm, jSONObject));
    }

    public PendingResult<MediaChannelResult> play() {
        return play(null);
    }

    public PendingResult<MediaChannelResult> play(JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzap(this, this.zznm, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueAppendItem(MediaQueueItem mediaQueueItem, JSONObject jSONObject) throws IllegalArgumentException {
        return queueInsertItems(new MediaQueueItem[]{mediaQueueItem}, 0, jSONObject);
    }

    public PendingResult<MediaChannelResult> queueInsertAndPlayItem(MediaQueueItem mediaQueueItem, int i, long j, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzy(this, this.zznm, mediaQueueItem, i, j, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueInsertAndPlayItem(MediaQueueItem mediaQueueItem, int i, JSONObject jSONObject) {
        return queueInsertAndPlayItem(mediaQueueItem, i, -1, jSONObject);
    }

    public PendingResult<MediaChannelResult> queueInsertItems(MediaQueueItem[] mediaQueueItemArr, int i, JSONObject jSONObject) throws IllegalArgumentException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzx(this, this.zznm, mediaQueueItemArr, i, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueJumpToItem(int i, long j, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzah(this, this.zznm, i, j, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueJumpToItem(int i, JSONObject jSONObject) {
        return queueJumpToItem(i, -1, jSONObject);
    }

    public PendingResult<MediaChannelResult> queueLoad(MediaQueueItem[] mediaQueueItemArr, int i, int i2, long j, JSONObject jSONObject) throws IllegalArgumentException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzw(r9, r9.zznm, mediaQueueItemArr, i, i2, j, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueLoad(MediaQueueItem[] mediaQueueItemArr, int i, int i2, JSONObject jSONObject) throws IllegalArgumentException {
        return queueLoad(mediaQueueItemArr, i, i2, -1, jSONObject);
    }

    public PendingResult<MediaChannelResult> queueMoveItemToNewIndex(int i, int i2, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzai(this, this.zznm, i, i2, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueNext(JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzae(this, this.zznm, jSONObject));
    }

    public PendingResult<MediaChannelResult> queuePrev(JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzad(this, this.zznm, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueRemoveItem(int i, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzag(this, this.zznm, i, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueRemoveItems(int[] iArr, JSONObject jSONObject) throws IllegalArgumentException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzaa(this, this.zznm, iArr, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueReorderItems(int[] iArr, int i, JSONObject jSONObject) throws IllegalArgumentException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzab(this, this.zznm, iArr, i, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueSetRepeatMode(int i, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzaf(this, this.zznm, i, jSONObject));
    }

    public PendingResult<MediaChannelResult> queueUpdateItems(MediaQueueItem[] mediaQueueItemArr, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzz(this, this.zznm, mediaQueueItemArr, jSONObject));
    }

    public void registerCallback(Callback callback) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (callback != null) {
            this.zzno.add(callback);
        }
    }

    @Deprecated
    public void removeListener(Listener listener) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (listener != null) {
            this.zznn.remove(listener);
        }
    }

    public void removeProgressListener(ProgressListener progressListener) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        zze zze = (zze) this.zznp.remove(progressListener);
        if (zze != null) {
            zze.zzb(progressListener);
            if (!zze.hasListener()) {
                this.zznq.remove(Long.valueOf(zze.zzbq()));
                zze.stop();
            }
        }
    }

    public PendingResult<MediaChannelResult> requestStatus() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzt(this, this.zznm));
    }

    public PendingResult<MediaChannelResult> seek(long j) {
        return seek(j, 0, null);
    }

    public PendingResult<MediaChannelResult> seek(long j, int i) {
        return seek(j, i, null);
    }

    public PendingResult<MediaChannelResult> seek(long j, int i, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzaq(this, this.zznm, j, i, jSONObject));
    }

    public PendingResult<MediaChannelResult> setActiveMediaTracks(long[] jArr) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (!zzbn()) {
            return zza(17, null);
        }
        if (jArr != null) {
            return zza(new zzu(this, this.zznm, jArr));
        }
        throw new IllegalArgumentException("trackIds cannot be null");
    }

    public void setParseAdsInfoCallback(ParseAdsInfoCallback parseAdsInfoCallback) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        this.zznr = parseAdsInfoCallback;
    }

    public PendingResult<MediaChannelResult> setPlaybackRate(double d) {
        return setPlaybackRate(d, null);
    }

    public PendingResult<MediaChannelResult> setPlaybackRate(double d, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (!zzbn()) {
            return zza(17, null);
        }
        if (Double.compare(d, 2.0d) <= 0) {
            if (Double.compare(d, 0.5d) >= 0) {
                return zza(new zzat(this, this.zznm, d, jSONObject));
            }
        }
        throw new IllegalArgumentException("playbackRate must be between PLAYBACK_RATE_MIN and PLAYBACK_RATE_MAX");
    }

    public PendingResult<MediaChannelResult> setStreamMute(boolean z) {
        return setStreamMute(z, null);
    }

    public PendingResult<MediaChannelResult> setStreamMute(boolean z, JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzas(this, this.zznm, z, jSONObject));
    }

    public PendingResult<MediaChannelResult> setStreamVolume(double d) throws IllegalArgumentException {
        return setStreamVolume(d, null);
    }

    public PendingResult<MediaChannelResult> setStreamVolume(double d, JSONObject jSONObject) throws IllegalArgumentException {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (!zzbn()) {
            return zza(17, null);
        }
        if (!Double.isInfinite(d)) {
            if (!Double.isNaN(d)) {
                return zza(new zzar(this, this.zznm, d, jSONObject));
            }
        }
        StringBuilder stringBuilder = new StringBuilder(41);
        stringBuilder.append("Volume cannot be ");
        stringBuilder.append(d);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public PendingResult<MediaChannelResult> setTextTrackStyle(TextTrackStyle textTrackStyle) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (!zzbn()) {
            return zza(17, null);
        }
        if (textTrackStyle != null) {
            return zza(new zzv(this, this.zznm, textTrackStyle));
        }
        throw new IllegalArgumentException("trackStyle cannot be null");
    }

    public PendingResult<MediaChannelResult> skipAd() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzs(this, this.zznm));
    }

    public PendingResult<MediaChannelResult> stop() {
        return stop(null);
    }

    public PendingResult<MediaChannelResult> stop(JSONObject jSONObject) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzao(this, this.zznm, jSONObject));
    }

    public void togglePlayback() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        int playerState = getPlayerState();
        if (playerState != 4) {
            if (playerState != 2) {
                play();
                return;
            }
        }
        pause();
    }

    public void unregisterCallback(Callback callback) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        if (callback != null) {
            this.zzno.remove(callback);
        }
    }

    public final PendingResult<MediaChannelResult> zza(int i, int i2, int i3) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzal(this, this.zznm, true, i, i2, i3));
    }

    public final PendingResult<MediaChannelResult> zza(String str, List<zzbp> list) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzam(this, this.zznm, true, str, null));
    }

    public final void zzb(com.google.android.gms.common.api.GoogleApiClient r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = r3.zznm;
        if (r0 != r4) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r3.zznm;
        if (r0 == 0) goto L_0x0024;
    L_0x0009:
        r0 = r3.zzeu;
        r0.zzcm();
        r0 = r3.zzhl;	 Catch:{ IOException -> 0x0019 }
        r1 = r3.zznm;	 Catch:{ IOException -> 0x0019 }
        r2 = r3.getNamespace();	 Catch:{ IOException -> 0x0019 }
        r0.removeMessageReceivedCallbacks(r1, r2);	 Catch:{ IOException -> 0x0019 }
    L_0x0019:
        r0 = r3.zznl;
        r1 = 0;
        r0.zza(r1);
        r0 = r3.handler;
        r0.removeCallbacksAndMessages(r1);
    L_0x0024:
        r3.zznm = r4;
        r4 = r3.zznm;
        if (r4 == 0) goto L_0x0031;
    L_0x002a:
        r4 = r3.zznl;
        r0 = r3.zznm;
        r4.zza(r0);
    L_0x0031:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.framework.media.RemoteMediaClient.zzb(com.google.android.gms.common.api.GoogleApiClient):void");
    }

    public final void zzbl() throws IOException {
        if (this.zznm != null) {
            this.zzhl.setMessageReceivedCallbacks(this.zznm, getNamespace(), this);
        }
    }

    public final PendingResult<MediaChannelResult> zzbm() {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzaj(this, this.zznm, true));
    }

    public final PendingResult<MediaChannelResult> zzf(int[] iArr) {
        Preconditions.checkMainThread("Must be called from the main thread.");
        return !zzbn() ? zza(17, null) : zza(new zzak(this, this.zznm, true, iArr));
    }
}
