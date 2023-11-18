package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public final class TransformationUtils {
    public static final int PAINT_FLAGS = 6;
    private static final String TAG = "TransformationUtils";

    public static int getExifOrientationDegrees(int i) {
        switch (i) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 6:
                return 90;
            case 7:
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    private TransformationUtils() {
    }

    public static Bitmap centerCrop(Bitmap bitmap, Bitmap bitmap2, int i, int i2) {
        if (bitmap2 == null) {
            return null;
        }
        if (bitmap2.getWidth() == i && bitmap2.getHeight() == i2) {
            return bitmap2;
        }
        float height;
        float width;
        Matrix matrix = new Matrix();
        float f = 0.0f;
        if (bitmap2.getWidth() * i2 > bitmap2.getHeight() * i) {
            height = ((float) i2) / ((float) bitmap2.getHeight());
            width = (((float) i) - (((float) bitmap2.getWidth()) * height)) * 0.5f;
        } else {
            height = ((float) i) / ((float) bitmap2.getWidth());
            f = (((float) i2) - (((float) bitmap2.getHeight()) * height)) * 0.5f;
            width = 0.0f;
        }
        matrix.setScale(height, height);
        matrix.postTranslate((float) ((int) (width + 0.5f)), (float) ((int) (f + 0.5f)));
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(i, i2, getSafeConfig(bitmap2));
        }
        setAlpha(bitmap2, bitmap);
        new Canvas(bitmap).drawBitmap(bitmap2, matrix, new Paint(6));
        return bitmap;
    }

    public static Bitmap fitCenter(Bitmap bitmap, BitmapPool bitmapPool, int i, int i2) {
        if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
            if (Log.isLoggable(TAG, 2) != null) {
                Log.v(TAG, "requested target size matches input, returning input");
            }
            return bitmap;
        }
        float min = Math.min(((float) i) / ((float) bitmap.getWidth()), ((float) i2) / ((float) bitmap.getHeight()));
        int width = (int) (((float) bitmap.getWidth()) * min);
        int height = (int) (((float) bitmap.getHeight()) * min);
        if (bitmap.getWidth() == width && bitmap.getHeight() == height) {
            if (Log.isLoggable(TAG, 2) != null) {
                Log.v(TAG, "adjusted target size matches input, returning input");
            }
            return bitmap;
        }
        Config safeConfig = getSafeConfig(bitmap);
        bitmapPool = bitmapPool.get(width, height, safeConfig);
        if (bitmapPool == null) {
            bitmapPool = Bitmap.createBitmap(width, height, safeConfig);
        }
        setAlpha(bitmap, bitmapPool);
        if (Log.isLoggable(TAG, 2)) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("request: ");
            stringBuilder.append(i);
            stringBuilder.append("x");
            stringBuilder.append(i2);
            Log.v(str, stringBuilder.toString());
            i = TAG;
            i2 = new StringBuilder();
            i2.append("toFit:   ");
            i2.append(bitmap.getWidth());
            i2.append("x");
            i2.append(bitmap.getHeight());
            Log.v(i, i2.toString());
            i = TAG;
            i2 = new StringBuilder();
            i2.append("toReuse: ");
            i2.append(bitmapPool.getWidth());
            i2.append("x");
            i2.append(bitmapPool.getHeight());
            Log.v(i, i2.toString());
            i = TAG;
            i2 = new StringBuilder();
            i2.append("minPct:   ");
            i2.append(min);
            Log.v(i, i2.toString());
        }
        i = new Canvas(bitmapPool);
        i2 = new Matrix();
        i2.setScale(min, min);
        i.drawBitmap(bitmap, i2, new Paint(6));
        return bitmapPool;
    }

    @TargetApi(12)
    public static void setAlpha(Bitmap bitmap, Bitmap bitmap2) {
        if (VERSION.SDK_INT >= 12 && bitmap2 != null) {
            bitmap2.setHasAlpha(bitmap.hasAlpha());
        }
    }

    @TargetApi(5)
    @Deprecated
    public static int getOrientation(String str) {
        try {
            return getExifOrientationDegrees(new ExifInterface(str).getAttributeInt("Orientation", 0));
        } catch (Throwable e) {
            if (Log.isLoggable(TAG, 6)) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to get orientation for image with path=");
                stringBuilder.append(str);
                Log.e(str2, stringBuilder.toString(), e);
            }
            return 0;
        }
    }

    @Deprecated
    public static Bitmap orientImage(String str, Bitmap bitmap) {
        return rotateImage(bitmap, getOrientation(str));
    }

    public static Bitmap rotateImage(Bitmap bitmap, int i) {
        if (i == 0) {
            return bitmap;
        }
        try {
            Matrix matrix = new Matrix();
            matrix.setRotate((float) i);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (int i2) {
            if (!Log.isLoggable(TAG, 6)) {
                return bitmap;
            }
            Log.e(TAG, "Exception when trying to orient image", i2);
            return bitmap;
        }
    }

    public static Bitmap rotateImageExif(Bitmap bitmap, BitmapPool bitmapPool, int i) {
        Matrix matrix = new Matrix();
        initializeMatrixForRotation(i, matrix);
        if (matrix.isIdentity() != 0) {
            return bitmap;
        }
        i = new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight());
        matrix.mapRect(i);
        int round = Math.round(i.width());
        int round2 = Math.round(i.height());
        Config safeConfig = getSafeConfig(bitmap);
        bitmapPool = bitmapPool.get(round, round2, safeConfig);
        if (bitmapPool == null) {
            bitmapPool = Bitmap.createBitmap(round, round2, safeConfig);
        }
        matrix.postTranslate(-i.left, -i.top);
        new Canvas(bitmapPool).drawBitmap(bitmap, matrix, new Paint(6));
        return bitmapPool;
    }

    private static Config getSafeConfig(Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888;
    }

    static void initializeMatrixForRotation(int i, Matrix matrix) {
        switch (i) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                return;
            case 3:
                matrix.setRotate(180.0f);
                return;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 6:
                matrix.setRotate(90.0f);
                return;
            case 7:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 8:
                matrix.setRotate(-90.0f);
                return;
            default:
                return;
        }
    }
}
