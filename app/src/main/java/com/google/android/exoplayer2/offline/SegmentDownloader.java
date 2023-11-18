package com.google.android.exoplayer2.offline;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.offline.Downloader.ProgressListener;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.upstream.cache.CacheUtil.CachingCounters;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class SegmentDownloader<M, K> implements Downloader {
    private static final int BUFFER_SIZE_BYTES = 131072;
    private final Cache cache;
    private final CacheDataSource dataSource;
    private volatile long downloadedBytes;
    private volatile int downloadedSegments;
    private K[] keys;
    private M manifest;
    private final Uri manifestUri;
    private final CacheDataSource offlineDataSource;
    private final PriorityTaskManager priorityTaskManager;
    private volatile int totalSegments;

    protected static class Segment implements Comparable<Segment> {
        public final DataSpec dataSpec;
        public final long startTimeUs;

        public Segment(long j, DataSpec dataSpec) {
            this.startTimeUs = j;
            this.dataSpec = dataSpec;
        }

        public int compareTo(@NonNull Segment segment) {
            long j = this.startTimeUs - segment.startTimeUs;
            if (j == 0) {
                return null;
            }
            return j < 0 ? -1 : 1;
        }
    }

    public abstract K[] getAllRepresentationKeys() throws IOException;

    protected abstract M getManifest(DataSource dataSource, Uri uri) throws IOException;

    protected abstract List<Segment> getSegments(DataSource dataSource, M m, K[] kArr, boolean z) throws InterruptedException, IOException;

    public SegmentDownloader(Uri uri, DownloaderConstructorHelper downloaderConstructorHelper) {
        this.manifestUri = uri;
        this.cache = downloaderConstructorHelper.getCache();
        this.dataSource = downloaderConstructorHelper.buildCacheDataSource(null);
        this.offlineDataSource = downloaderConstructorHelper.buildCacheDataSource(true);
        this.priorityTaskManager = downloaderConstructorHelper.getPriorityTaskManager();
        resetCounters();
    }

    public final M getManifest() throws IOException {
        return getManifestIfNeeded(false);
    }

    public final void selectRepresentations(K[] kArr) {
        kArr = (kArr == null || kArr.length <= 0) ? null : (Object[]) kArr.clone();
        this.keys = kArr;
        resetCounters();
    }

    public final void init() throws java.lang.InterruptedException, java.io.IOException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r1 = this;
        r0 = 1;
        r1.getManifestIfNeeded(r0);	 Catch:{ IOException -> 0x000d }
        r1.initStatus(r0);	 Catch:{ IOException -> 0x0008, IOException -> 0x0008 }
        return;
    L_0x0008:
        r0 = move-exception;
        r1.resetCounters();
        throw r0;
    L_0x000d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.offline.SegmentDownloader.init():void");
    }

    public final synchronized void download(@Nullable ProgressListener progressListener) throws IOException, InterruptedException {
        this.priorityTaskManager.add(-1000);
        int i = 0;
        try {
            getManifestIfNeeded(false);
            List initStatus = initStatus(false);
            notifyListener(progressListener);
            Collections.sort(initStatus);
            byte[] bArr = new byte[131072];
            CachingCounters cachingCounters = new CachingCounters();
            while (i < initStatus.size()) {
                CacheUtil.cache(((Segment) initStatus.get(i)).dataSpec, this.cache, this.dataSource, bArr, this.priorityTaskManager, -1000, cachingCounters, true);
                this.downloadedBytes += cachingCounters.newlyCachedBytes;
                this.downloadedSegments++;
                notifyListener(progressListener);
                i++;
            }
        } finally {
            this.priorityTaskManager.remove(-1000);
        }
    }

    public final int getTotalSegments() {
        return this.totalSegments;
    }

    public final int getDownloadedSegments() {
        return this.downloadedSegments;
    }

    public final long getDownloadedBytes() {
        return this.downloadedBytes;
    }

    public float getDownloadPercentage() {
        int i = this.totalSegments;
        int i2 = this.downloadedSegments;
        if (i != -1) {
            if (i2 != -1) {
                float f = 100.0f;
                if (i != 0) {
                    f = (((float) i2) * 100.0f) / ((float) i);
                }
                return f;
            }
        }
        return Float.NaN;
    }

    public final void remove() throws java.lang.InterruptedException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r5 = this;
        r0 = 1;
        r5.getManifestIfNeeded(r0);	 Catch:{ IOException -> 0x0004 }
    L_0x0004:
        r5.resetCounters();
        r1 = r5.manifest;
        if (r1 == 0) goto L_0x0035;
    L_0x000b:
        r1 = 0;
        r2 = r5.offlineDataSource;	 Catch:{ IOException -> 0x0019 }
        r3 = r5.manifest;	 Catch:{ IOException -> 0x0019 }
        r4 = r5.getAllRepresentationKeys();	 Catch:{ IOException -> 0x0019 }
        r0 = r5.getSegments(r2, r3, r4, r0);	 Catch:{ IOException -> 0x0019 }
        goto L_0x001a;
    L_0x0019:
        r0 = r1;
    L_0x001a:
        if (r0 == 0) goto L_0x0033;
    L_0x001c:
        r2 = 0;
    L_0x001d:
        r3 = r0.size();
        if (r2 >= r3) goto L_0x0033;
    L_0x0023:
        r3 = r0.get(r2);
        r3 = (com.google.android.exoplayer2.offline.SegmentDownloader.Segment) r3;
        r3 = r3.dataSpec;
        r3 = r3.uri;
        r5.remove(r3);
        r2 = r2 + 1;
        goto L_0x001d;
    L_0x0033:
        r5.manifest = r1;
    L_0x0035:
        r0 = r5.manifestUri;
        r5.remove(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.offline.SegmentDownloader.remove():void");
    }

    private void resetCounters() {
        this.totalSegments = -1;
        this.downloadedSegments = -1;
        this.downloadedBytes = -1;
    }

    private void remove(Uri uri) {
        CacheUtil.remove(this.cache, CacheUtil.generateKey(uri));
    }

    private void notifyListener(ProgressListener progressListener) {
        if (progressListener != null) {
            progressListener.onDownloadProgress(this, getDownloadPercentage(), this.downloadedBytes);
        }
    }

    private synchronized List<Segment> initStatus(boolean z) throws IOException, InterruptedException {
        DataSource dataSource = getDataSource(z);
        if (this.keys == null) {
            this.keys = getAllRepresentationKeys();
        }
        z = getSegments(dataSource, this.manifest, this.keys, z);
        CachingCounters cachingCounters = new CachingCounters();
        this.totalSegments = z.size();
        this.downloadedSegments = 0;
        this.downloadedBytes = 0;
        for (int size = z.size() - 1; size >= 0; size--) {
            CacheUtil.getCached(((Segment) z.get(size)).dataSpec, this.cache, cachingCounters);
            this.downloadedBytes += cachingCounters.alreadyCachedBytes;
            if (cachingCounters.alreadyCachedBytes == cachingCounters.contentLength) {
                this.downloadedSegments++;
                z.remove(size);
            }
        }
        return z;
    }

    private M getManifestIfNeeded(boolean z) throws IOException {
        if (this.manifest == null) {
            this.manifest = getManifest(getDataSource(z), this.manifestUri);
        }
        return this.manifest;
    }

    private DataSource getDataSource(boolean z) {
        return z ? this.offlineDataSource : this.dataSource;
    }
}
