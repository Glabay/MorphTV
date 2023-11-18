package com.google.android.gms.internal.cast;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.google.android.gms.cast.framework.media.ImageHints;

public final class zzx implements zzab {
    private final Context zzgs;
    private final ImageHints zzow;
    private Uri zzox;
    private zzz zzoy;
    private zzac zzoz;
    private Bitmap zzpa;
    private boolean zzpb;
    private zzy zzpc;

    public zzx(Context context) {
        this(context, new ImageHints(-1, 0, 0));
    }

    public zzx(Context context, @NonNull ImageHints imageHints) {
        this.zzgs = context;
        this.zzow = imageHints;
        this.zzoz = new zzac();
        reset();
    }

    private final void reset() {
        if (this.zzoy != null) {
            this.zzoy.cancel(true);
            this.zzoy = null;
        }
        this.zzox = null;
        this.zzpa = null;
        this.zzpb = false;
    }

    public final void clear() {
        reset();
        this.zzpc = null;
    }

    public final void onPostExecute(Bitmap bitmap) {
        this.zzpa = bitmap;
        this.zzpb = true;
        if (this.zzpc != null) {
            this.zzpc.zza(this.zzpa);
        }
        this.zzoy = null;
    }

    public final void zza(zzy zzy) {
        this.zzpc = zzy;
    }

    public final boolean zza(Uri uri) {
        if (uri == null) {
            reset();
            return true;
        } else if (uri.equals(this.zzox)) {
            return this.zzpb;
        } else {
            zzz zzz;
            Uri uri2;
            reset();
            this.zzox = uri;
            if (this.zzow.getWidthInPixels() != 0) {
                if (this.zzow.getHeightInPixels() != 0) {
                    this.zzoy = new zzz(this.zzgs, this.zzow.getWidthInPixels(), this.zzow.getHeightInPixels(), false, this);
                    zzz = this.zzoy;
                    uri2 = this.zzox;
                    zzz.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Uri[]{uri2});
                    return false;
                }
            }
            this.zzoy = new zzz(this.zzgs, this);
            zzz = this.zzoy;
            uri2 = this.zzox;
            zzz.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Uri[]{uri2});
            return false;
        }
    }
}
