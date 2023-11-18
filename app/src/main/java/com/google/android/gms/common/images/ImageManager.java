package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.images.ImageRequest.ImageViewImageRequest;
import com.google.android.gms.common.images.ImageRequest.ListenerImageRequest;
import com.google.android.gms.common.images.internal.PostProcessedResourceCache;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.time.DateUtils;

public final class ImageManager {
    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    private static final Object zzov = new Object();
    private static HashSet<Uri> zzow = new HashSet();
    private static ImageManager zzox;
    private static ImageManager zzoy;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService zzoz = Executors.newFixedThreadPool(4);
    private final zza zzpa;
    private final PostProcessedResourceCache zzpb;
    private final Map<ImageRequest, ImageReceiver> zzpc;
    private final Map<Uri, ImageReceiver> zzpd;
    private final Map<Uri, Long> zzpe;

    @KeepName
    private final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        private final ArrayList<ImageRequest> zzpf = new ArrayList();
        private final /* synthetic */ ImageManager zzpg;

        ImageReceiver(ImageManager imageManager, Uri uri) {
            this.zzpg = imageManager;
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
        }

        public final void onReceiveResult(int i, Bundle bundle) {
            this.zzpg.zzoz.execute(new zzb(this.zzpg, this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }

        public final void zza(ImageRequest imageRequest) {
            Asserts.checkMainThread("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zzpf.add(imageRequest);
        }

        public final void zzb(ImageRequest imageRequest) {
            Asserts.checkMainThread("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zzpf.remove(imageRequest);
        }

        public final void zzco() {
            Intent intent = new Intent(Constants.ACTION_LOAD_IMAGE);
            intent.putExtra(Constants.EXTRA_URI, this.mUri);
            intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, this);
            intent.putExtra(Constants.EXTRA_PRIORITY, 3);
            this.zzpg.mContext.sendBroadcast(intent);
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    private static final class zza extends LruCache<zza, Bitmap> {
        public zza(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            super((int) (((float) ((((context.getApplicationInfo().flags & 1048576) != 0 ? 1 : null) != null ? activityManager.getLargeMemoryClass() : activityManager.getMemoryClass()) * 1048576)) * 0.33f));
        }

        protected final /* synthetic */ void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
            super.entryRemoved(z, (zza) obj, (Bitmap) obj2, (Bitmap) obj3);
        }

        protected final /* synthetic */ int sizeOf(Object obj, Object obj2) {
            Bitmap bitmap = (Bitmap) obj2;
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
    }

    private final class zzb implements Runnable {
        private final Uri mUri;
        private final /* synthetic */ ImageManager zzpg;
        private final ParcelFileDescriptor zzph;

        public zzb(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.zzpg = imageManager;
            this.mUri = uri;
            this.zzph = parcelFileDescriptor;
        }

        public final void run() {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r12 = this;
            r0 = "LoadBitmapFromDiskRunnable can't be executed in the main thread";
            com.google.android.gms.common.internal.Asserts.checkNotMainThread(r0);
            r0 = r12.zzph;
            r1 = 1;
            r2 = 0;
            r3 = 0;
            if (r0 == 0) goto L_0x0051;
        L_0x000c:
            r0 = r12.zzph;	 Catch:{ OutOfMemoryError -> 0x0018 }
            r0 = r0.getFileDescriptor();	 Catch:{ OutOfMemoryError -> 0x0018 }
            r0 = android.graphics.BitmapFactory.decodeFileDescriptor(r0);	 Catch:{ OutOfMemoryError -> 0x0018 }
            r3 = r0;
            goto L_0x0040;
        L_0x0018:
            r0 = move-exception;
            r2 = "ImageManager";
            r4 = r12.mUri;
            r4 = java.lang.String.valueOf(r4);
            r5 = java.lang.String.valueOf(r4);
            r5 = r5.length();
            r5 = r5 + 34;
            r6 = new java.lang.StringBuilder;
            r6.<init>(r5);
            r5 = "OOM while loading bitmap for uri: ";
            r6.append(r5);
            r6.append(r4);
            r4 = r6.toString();
            android.util.Log.e(r2, r4, r0);
            r2 = 1;
        L_0x0040:
            r0 = r12.zzph;	 Catch:{ IOException -> 0x0046 }
            r0.close();	 Catch:{ IOException -> 0x0046 }
            goto L_0x004e;
        L_0x0046:
            r0 = move-exception;
            r4 = "ImageManager";
            r5 = "closed failed";
            android.util.Log.e(r4, r5, r0);
        L_0x004e:
            r10 = r2;
            r9 = r3;
            goto L_0x0053;
        L_0x0051:
            r9 = r3;
            r10 = 0;
        L_0x0053:
            r0 = new java.util.concurrent.CountDownLatch;
            r0.<init>(r1);
            r1 = r12.zzpg;
            r1 = r1.mHandler;
            r2 = new com.google.android.gms.common.images.ImageManager$zze;
            r7 = r12.zzpg;
            r8 = r12.mUri;
            r6 = r2;
            r11 = r0;
            r6.<init>(r7, r8, r9, r10, r11);
            r1.post(r2);
            r0.await();	 Catch:{ InterruptedException -> 0x0070 }
            return;
        L_0x0070:
            r0 = "ImageManager";
            r1 = r12.mUri;
            r1 = java.lang.String.valueOf(r1);
            r2 = java.lang.String.valueOf(r1);
            r2 = r2.length();
            r2 = r2 + 32;
            r3 = new java.lang.StringBuilder;
            r3.<init>(r2);
            r2 = "Latch interrupted while posting ";
            r3.append(r2);
            r3.append(r1);
            r1 = r3.toString();
            android.util.Log.w(r0, r1);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.images.ImageManager.zzb.run():void");
        }
    }

    private final class zzc implements Runnable {
        private final /* synthetic */ ImageManager zzpg;
        private final ImageRequest zzpi;

        public zzc(ImageManager imageManager, ImageRequest imageRequest) {
            this.zzpg = imageManager;
            this.zzpi = imageRequest;
        }

        public final void run() {
            Asserts.checkMainThread("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) this.zzpg.zzpc.get(this.zzpi);
            if (imageReceiver != null) {
                this.zzpg.zzpc.remove(this.zzpi);
                imageReceiver.zzb(this.zzpi);
            }
            zza zza = this.zzpi.zzpk;
            if (zza.uri == null) {
                this.zzpi.zza(this.zzpg.mContext, this.zzpg.zzpb, true);
                return;
            }
            Bitmap zza2 = this.zzpg.zza(zza);
            if (zza2 != null) {
                this.zzpi.zza(this.zzpg.mContext, zza2, true);
                return;
            }
            Long l = (Long) this.zzpg.zzpe.get(zza.uri);
            if (l != null) {
                if (SystemClock.elapsedRealtime() - l.longValue() < DateUtils.MILLIS_PER_HOUR) {
                    this.zzpi.zza(this.zzpg.mContext, this.zzpg.zzpb, true);
                    return;
                }
                this.zzpg.zzpe.remove(zza.uri);
            }
            this.zzpi.zza(this.zzpg.mContext, this.zzpg.zzpb);
            ImageReceiver imageReceiver2 = (ImageReceiver) this.zzpg.zzpd.get(zza.uri);
            if (imageReceiver2 == null) {
                imageReceiver2 = new ImageReceiver(this.zzpg, zza.uri);
                this.zzpg.zzpd.put(zza.uri, imageReceiver2);
            }
            imageReceiver2.zza(this.zzpi);
            if (!(this.zzpi instanceof ListenerImageRequest)) {
                this.zzpg.zzpc.put(this.zzpi, imageReceiver2);
            }
            synchronized (ImageManager.zzov) {
                if (!ImageManager.zzow.contains(zza.uri)) {
                    ImageManager.zzow.add(zza.uri);
                    imageReceiver2.zzco();
                }
            }
        }
    }

    private static final class zzd implements ComponentCallbacks2 {
        private final zza zzpa;

        public zzd(zza zza) {
            this.zzpa = zza;
        }

        public final void onConfigurationChanged(Configuration configuration) {
        }

        public final void onLowMemory() {
            this.zzpa.evictAll();
        }

        public final void onTrimMemory(int i) {
            if (i >= 60) {
                this.zzpa.evictAll();
                return;
            }
            if (i >= 20) {
                this.zzpa.trimToSize(this.zzpa.size() / 2);
            }
        }
    }

    private final class zze implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private final CountDownLatch zzfd;
        private final /* synthetic */ ImageManager zzpg;
        private boolean zzpj;

        public zze(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.zzpg = imageManager;
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzpj = z;
            this.zzfd = countDownLatch;
        }

        public final void run() {
            Asserts.checkMainThread("OnBitmapLoadedRunnable must be executed in the main thread");
            Object obj = this.mBitmap != null ? 1 : null;
            if (this.zzpg.zzpa != null) {
                if (this.zzpj) {
                    this.zzpg.zzpa.evictAll();
                    System.gc();
                    this.zzpj = false;
                    this.zzpg.mHandler.post(this);
                    return;
                } else if (obj != null) {
                    this.zzpg.zzpa.put(new zza(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) this.zzpg.zzpd.remove(this.mUri);
            if (imageReceiver != null) {
                ArrayList zza = imageReceiver.zzpf;
                int size = zza.size();
                for (int i = 0; i < size; i++) {
                    ImageRequest imageRequest = (ImageRequest) zza.get(i);
                    if (obj != null) {
                        imageRequest.zza(this.zzpg.mContext, this.mBitmap, false);
                    } else {
                        this.zzpg.zzpe.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                        imageRequest.zza(this.zzpg.mContext, this.zzpg.zzpb, false);
                    }
                    if (!(imageRequest instanceof ListenerImageRequest)) {
                        this.zzpg.zzpc.remove(imageRequest);
                    }
                }
            }
            this.zzfd.countDown();
            synchronized (ImageManager.zzov) {
                ImageManager.zzow.remove(this.mUri);
            }
        }
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
        if (z) {
            this.zzpa = new zza(this.mContext);
            this.mContext.registerComponentCallbacks(new zzd(this.zzpa));
        } else {
            this.zzpa = null;
        }
        this.zzpb = new PostProcessedResourceCache();
        this.zzpc = new HashMap();
        this.zzpd = new HashMap();
        this.zzpe = new HashMap();
    }

    public static ImageManager create(Context context) {
        return create(context, false);
    }

    public static ImageManager create(Context context, boolean z) {
        if (z) {
            if (zzoy == null) {
                zzoy = new ImageManager(context, true);
            }
            return zzoy;
        }
        if (zzox == null) {
            zzox = new ImageManager(context, false);
        }
        return zzox;
    }

    private final Bitmap zza(zza zza) {
        return this.zzpa == null ? null : (Bitmap) this.zzpa.get(zza);
    }

    public final void loadImage(ImageView imageView, int i) {
        loadImage(new ImageViewImageRequest(imageView, i));
    }

    public final void loadImage(ImageView imageView, Uri uri) {
        loadImage(new ImageViewImageRequest(imageView, uri));
    }

    public final void loadImage(ImageView imageView, Uri uri, int i) {
        ImageRequest imageViewImageRequest = new ImageViewImageRequest(imageView, uri);
        imageViewImageRequest.setNoDataPlaceholder(i);
        loadImage(imageViewImageRequest);
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        loadImage(new ListenerImageRequest(onImageLoadedListener, uri));
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        ImageRequest listenerImageRequest = new ListenerImageRequest(onImageLoadedListener, uri);
        listenerImageRequest.setNoDataPlaceholder(i);
        loadImage(listenerImageRequest);
    }

    public final void loadImage(ImageRequest imageRequest) {
        Asserts.checkMainThread("ImageManager.loadImage() must be called in the main thread");
        new zzc(this, imageRequest).run();
    }
}
