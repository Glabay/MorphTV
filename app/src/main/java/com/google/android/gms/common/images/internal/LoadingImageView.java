package com.google.android.gms.common.images.internal;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.google.android.gms.base.C0780R;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.google.android.gms.common.images.ImageRequest;
import com.google.android.gms.common.images.ImageRequest.ImageViewImageRequest;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.util.PlatformVersion;

public final class LoadingImageView extends ImageView {
    public static final int ASPECT_RATIO_ADJUST_HEIGHT = 2;
    public static final int ASPECT_RATIO_ADJUST_NONE = 0;
    public static final int ASPECT_RATIO_ADJUST_WIDTH = 1;
    private static ImageManager zzqm;
    private OnImageLoadedListener mOnImageLoadedListener;
    private int mPostProcessingFlags;
    private boolean zzpl;
    private boolean zzpm;
    private Uri zzqn;
    private int zzqo;
    private boolean zzqp;
    private int zzqq;
    private ClipPathProvider zzqr;
    private int zzqs;
    private float zzqt;

    public interface ClipPathProvider {
        Path getClipPath(int i, int i2);
    }

    public LoadingImageView(Context context) {
        this(context, null, 0);
    }

    public LoadingImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LoadingImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzqo = 0;
        this.zzpl = true;
        this.zzpm = false;
        this.zzqp = false;
        this.zzqq = 0;
        this.mPostProcessingFlags = 0;
        this.zzqs = 0;
        this.zzqt = 1.0f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0780R.styleable.LoadingImageView);
        this.zzqs = obtainStyledAttributes.getInt(C0780R.styleable.LoadingImageView_imageAspectRatioAdjust, 0);
        this.zzqt = obtainStyledAttributes.getFloat(C0780R.styleable.LoadingImageView_imageAspectRatio, 1.0f);
        setCircleCropEnabled(obtainStyledAttributes.getBoolean(C0780R.styleable.LoadingImageView_circleCrop, false));
        obtainStyledAttributes.recycle();
    }

    private final void zzc(boolean z) {
        if (this.mOnImageLoadedListener != null) {
            this.mOnImageLoadedListener.onImageLoaded(this.zzqn, null, z);
        }
    }

    public final void clearAspectRatioAdjust() {
        if (this.zzqs != 0) {
            this.zzqs = 0;
            requestLayout();
        }
    }

    public final void clearImage() {
        loadUri(null);
        this.zzqp = true;
    }

    public final int getLoadedNoDataPlaceholderResId() {
        return this.zzqo;
    }

    public final Uri getLoadedUri() {
        return this.zzqn;
    }

    public final void loadUri(Uri uri) {
        loadUri(uri, 0, true, false);
    }

    public final void loadUri(Uri uri, int i) {
        loadUri(uri, i, true, false);
    }

    public final void loadUri(Uri uri, int i, boolean z) {
        loadUri(uri, i, z, false);
    }

    public final void loadUri(Uri uri, int i, boolean z, boolean z2) {
        boolean z3 = true;
        boolean equals = uri != null ? uri.equals(this.zzqn) : this.zzqn == null;
        if (equals) {
            if (this.zzqn != null) {
                zzc(true);
                return;
            } else if (this.zzqo == i) {
                zzc(false);
                return;
            }
        }
        if (zzqm == null) {
            zzqm = ImageManager.create(getContext(), getContext().getApplicationContext().getPackageName().equals(GooglePlayServicesUtilLight.GOOGLE_PLAY_GAMES_PACKAGE));
        }
        if (!this.zzpm) {
            if (!this.zzqp) {
                z3 = false;
            }
        }
        this.zzqp = false;
        ImageRequest imageViewImageRequest = new ImageViewImageRequest((ImageView) this, uri);
        imageViewImageRequest.setNoDataPlaceholder(i);
        imageViewImageRequest.setCrossFadeEnabled(this.zzpl);
        imageViewImageRequest.setCrossFadeAlwaysEnabled(z3);
        imageViewImageRequest.setLoadingPlaceholderEnabled(z);
        imageViewImageRequest.setPostProcessingFlags(this.mPostProcessingFlags);
        imageViewImageRequest.setOnImageLoadedListener(this.mOnImageLoadedListener);
        imageViewImageRequest.setUseNewDrawable(z2);
        zzqm.loadImage(imageViewImageRequest);
    }

    protected final void onDraw(Canvas canvas) {
        if (this.zzqr != null) {
            canvas.clipPath(this.zzqr.getClipPath(getWidth(), getHeight()));
        }
        super.onDraw(canvas);
        if (this.zzqq != 0) {
            canvas.drawColor(this.zzqq);
        }
    }

    protected final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        switch (this.zzqs) {
            case 1:
                i2 = getMeasuredHeight();
                i = (int) (((float) i2) * this.zzqt);
                break;
            case 2:
                i = getMeasuredWidth();
                i2 = (int) (((float) i) / this.zzqt);
                break;
            default:
                return;
        }
        setMeasuredDimension(i, i2);
    }

    public final void setCircleCropEnabled(boolean z) {
        this.mPostProcessingFlags = z ? this.mPostProcessingFlags | 1 : this.mPostProcessingFlags & -2;
    }

    public final void setClipPathProvider(ClipPathProvider clipPathProvider) {
        this.zzqr = clipPathProvider;
        if (!PlatformVersion.isAtLeastJellyBean()) {
            setLayerType(1, null);
        }
    }

    public final void setCrossFadeAlwaysEnabled(boolean z) {
        this.zzpm = z;
    }

    public final void setCrossFadeEnabled(boolean z) {
        this.zzpl = z;
    }

    public final void setImageAspectRatioAdjust(int i, float f) {
        boolean z;
        boolean z2 = false;
        if (!(i == 0 || i == 1)) {
            if (i != 2) {
                z = false;
                Asserts.checkState(z);
                if (f > 0.0f) {
                    z2 = true;
                }
                Asserts.checkState(z2);
                this.zzqs = i;
                this.zzqt = f;
                requestLayout();
            }
        }
        z = true;
        Asserts.checkState(z);
        if (f > 0.0f) {
            z2 = true;
        }
        Asserts.checkState(z2);
        this.zzqs = i;
        this.zzqt = f;
        requestLayout();
    }

    public final void setLoadedNoDataPlaceholderResId(int i) {
        this.zzqo = i;
    }

    public final void setLoadedUri(Uri uri) {
        this.zzqn = uri;
    }

    public final void setOnImageLoadedListener(OnImageLoadedListener onImageLoadedListener) {
        this.mOnImageLoadedListener = onImageLoadedListener;
    }

    public final void setTintColor(int i) {
        this.zzqq = i;
        setColorFilter(this.zzqq != 0 ? ColorFilters.COLOR_FILTER_BW : null);
        invalidate();
    }

    public final void setTintColorId(int i) {
        if (i > 0) {
            Resources resources = getResources();
            if (resources != null) {
                i = resources.getColor(i);
                setTintColor(i);
            }
        }
        i = 0;
        setTintColor(i);
    }
}
