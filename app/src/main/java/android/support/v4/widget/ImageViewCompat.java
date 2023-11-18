package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

public class ImageViewCompat {
    static final ImageViewCompatImpl IMPL;

    interface ImageViewCompatImpl {
        ColorStateList getImageTintList(ImageView imageView);

        Mode getImageTintMode(ImageView imageView);

        void setImageTintList(ImageView imageView, ColorStateList colorStateList);

        void setImageTintMode(ImageView imageView, Mode mode);
    }

    static class BaseViewCompatImpl implements ImageViewCompatImpl {
        BaseViewCompatImpl() {
        }

        public ColorStateList getImageTintList(ImageView imageView) {
            return imageView instanceof TintableImageSourceView ? ((TintableImageSourceView) imageView).getSupportImageTintList() : null;
        }

        public void setImageTintList(ImageView imageView, ColorStateList colorStateList) {
            if (imageView instanceof TintableImageSourceView) {
                ((TintableImageSourceView) imageView).setSupportImageTintList(colorStateList);
            }
        }

        public void setImageTintMode(ImageView imageView, Mode mode) {
            if (imageView instanceof TintableImageSourceView) {
                ((TintableImageSourceView) imageView).setSupportImageTintMode(mode);
            }
        }

        public Mode getImageTintMode(ImageView imageView) {
            return imageView instanceof TintableImageSourceView ? ((TintableImageSourceView) imageView).getSupportImageTintMode() : null;
        }
    }

    @RequiresApi(21)
    static class LollipopViewCompatImpl extends BaseViewCompatImpl {
        LollipopViewCompatImpl() {
        }

        public ColorStateList getImageTintList(ImageView imageView) {
            return imageView.getImageTintList();
        }

        public void setImageTintList(ImageView imageView, ColorStateList colorStateList) {
            imageView.setImageTintList(colorStateList);
            if (VERSION.SDK_INT == 21) {
                colorStateList = imageView.getDrawable();
                Object obj = (imageView.getImageTintList() == null || imageView.getImageTintMode() == null) ? null : 1;
                if (colorStateList != null && obj != null) {
                    if (colorStateList.isStateful()) {
                        colorStateList.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(colorStateList);
                }
            }
        }

        public void setImageTintMode(ImageView imageView, Mode mode) {
            imageView.setImageTintMode(mode);
            if (VERSION.SDK_INT == 21) {
                mode = imageView.getDrawable();
                Object obj = (imageView.getImageTintList() == null || imageView.getImageTintMode() == null) ? null : 1;
                if (mode != null && obj != null) {
                    if (mode.isStateful()) {
                        mode.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(mode);
                }
            }
        }

        public Mode getImageTintMode(ImageView imageView) {
            return imageView.getImageTintMode();
        }
    }

    static {
        if (VERSION.SDK_INT >= 21) {
            IMPL = new LollipopViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    @Nullable
    public static ColorStateList getImageTintList(@NonNull ImageView imageView) {
        return IMPL.getImageTintList(imageView);
    }

    public static void setImageTintList(@NonNull ImageView imageView, @Nullable ColorStateList colorStateList) {
        IMPL.setImageTintList(imageView, colorStateList);
    }

    @Nullable
    public static Mode getImageTintMode(@NonNull ImageView imageView) {
        return IMPL.getImageTintMode(imageView);
    }

    public static void setImageTintMode(@NonNull ImageView imageView, @Nullable Mode mode) {
        IMPL.setImageTintMode(imageView, mode);
    }

    private ImageViewCompat() {
    }
}
