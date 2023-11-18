package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser.ImageType;
import com.bumptech.glide.util.ByteArrayPool;
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;

public abstract class Downsampler implements BitmapDecoder<InputStream> {
    public static final Downsampler AT_LEAST = new C05921();
    public static final Downsampler AT_MOST = new C05932();
    private static final int MARK_POSITION = 5242880;
    public static final Downsampler NONE = new C05943();
    private static final Queue<Options> OPTIONS_QUEUE = Util.createQueue(0);
    private static final String TAG = "Downsampler";
    private static final Set<ImageType> TYPES_THAT_USE_POOL = EnumSet.of(ImageType.JPEG, ImageType.PNG_A, ImageType.PNG);

    /* renamed from: com.bumptech.glide.load.resource.bitmap.Downsampler$1 */
    static class C05921 extends Downsampler {
        public String getId() {
            return "AT_LEAST.com.bumptech.glide.load.data.bitmap";
        }

        C05921() {
        }

        public /* bridge */ /* synthetic */ Bitmap decode(Object obj, BitmapPool bitmapPool, int i, int i2, DecodeFormat decodeFormat) throws Exception {
            return super.decode((InputStream) obj, bitmapPool, i, i2, decodeFormat);
        }

        protected int getSampleSize(int i, int i2, int i3, int i4) {
            return Math.min(i2 / i4, i / i3);
        }
    }

    /* renamed from: com.bumptech.glide.load.resource.bitmap.Downsampler$2 */
    static class C05932 extends Downsampler {
        public String getId() {
            return "AT_MOST.com.bumptech.glide.load.data.bitmap";
        }

        C05932() {
        }

        public /* bridge */ /* synthetic */ Bitmap decode(Object obj, BitmapPool bitmapPool, int i, int i2, DecodeFormat decodeFormat) throws Exception {
            return super.decode((InputStream) obj, bitmapPool, i, i2, decodeFormat);
        }

        protected int getSampleSize(int i, int i2, int i3, int i4) {
            i = (int) Math.ceil((double) Math.max(((float) i2) / ((float) i4), ((float) i) / ((float) i3)));
            i3 = 1;
            i2 = Math.max(1, Integer.highestOneBit(i));
            if (i2 >= i) {
                i3 = 0;
            }
            return i2 << i3;
        }
    }

    /* renamed from: com.bumptech.glide.load.resource.bitmap.Downsampler$3 */
    static class C05943 extends Downsampler {
        public String getId() {
            return "NONE.com.bumptech.glide.load.data.bitmap";
        }

        protected int getSampleSize(int i, int i2, int i3, int i4) {
            return 0;
        }

        C05943() {
        }

        public /* bridge */ /* synthetic */ Bitmap decode(Object obj, BitmapPool bitmapPool, int i, int i2, DecodeFormat decodeFormat) throws Exception {
            return super.decode((InputStream) obj, bitmapPool, i, i2, decodeFormat);
        }
    }

    protected abstract int getSampleSize(int i, int i2, int i3, int i4);

    public Bitmap decode(InputStream inputStream, BitmapPool bitmapPool, int i, int i2, DecodeFormat decodeFormat) {
        Throwable e;
        Throwable th;
        Options options;
        int i3;
        BitmapPool bitmapPool2 = bitmapPool;
        ByteArrayPool byteArrayPool = ByteArrayPool.get();
        byte[] bytes = byteArrayPool.getBytes();
        byte[] bytes2 = byteArrayPool.getBytes();
        Options defaultOptions = getDefaultOptions();
        InputStream recyclableBufferedInputStream = new RecyclableBufferedInputStream(inputStream, bytes2);
        InputStream obtain = ExceptionCatchingInputStream.obtain(recyclableBufferedInputStream);
        MarkEnforcingInputStream markEnforcingInputStream = new MarkEnforcingInputStream(obtain);
        try {
            obtain.mark(MARK_POSITION);
            try {
                int orientation = new ImageHeaderParser(obtain).getOrientation();
                try {
                    obtain.reset();
                } catch (Throwable e2) {
                    if (Log.isLoggable(TAG, 5)) {
                        Log.w(TAG, "Cannot reset the input stream", e2);
                    }
                } catch (Throwable e22) {
                    th = e22;
                    options = defaultOptions;
                    byteArrayPool.releaseBytes(bytes);
                    byteArrayPool.releaseBytes(bytes2);
                    obtain.release();
                    releaseOptions(options);
                    throw th;
                }
                i3 = orientation;
            } catch (Throwable e222) {
                Throwable th2 = e222;
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot determine the image orientation from header", th2);
                }
                obtain.reset();
                i3 = 0;
            } catch (Throwable e2222) {
                th = e2222;
                options = defaultOptions;
                obtain.reset();
                throw th;
            }
        } catch (Throwable e22222) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Cannot reset the input stream", e22222);
            }
        } catch (Throwable th3) {
            e22222 = th3;
            options = defaultOptions;
            th = e22222;
            byteArrayPool.releaseBytes(bytes);
            byteArrayPool.releaseBytes(bytes2);
            obtain.release();
            releaseOptions(options);
            throw th;
        }
        defaultOptions.inTempStorage = bytes;
        int[] dimensions = getDimensions(markEnforcingInputStream, recyclableBufferedInputStream, defaultOptions);
        int i4 = dimensions[0];
        int i5 = dimensions[1];
        Options options2 = defaultOptions;
        Options options3 = defaultOptions;
        int i6 = i3;
        try {
            Bitmap downsampleWithSize = downsampleWithSize(markEnforcingInputStream, recyclableBufferedInputStream, options2, bitmapPool2, i4, i5, getRoundedSampleSize(TransformationUtils.getExifOrientationDegrees(i3), i4, i5, i, i2), decodeFormat);
            th = obtain.getException();
            if (th != null) {
                try {
                    throw new RuntimeException(th);
                } catch (Throwable e222222) {
                    th = e222222;
                    options = options3;
                    byteArrayPool.releaseBytes(bytes);
                    byteArrayPool.releaseBytes(bytes2);
                    obtain.release();
                    releaseOptions(options);
                    throw th;
                }
            }
            Bitmap bitmap = null;
            if (downsampleWithSize != null) {
                bitmap = TransformationUtils.rotateImageExif(downsampleWithSize, bitmapPool2, i6);
                if (!(downsampleWithSize.equals(bitmap) || bitmapPool2.put(downsampleWithSize))) {
                    downsampleWithSize.recycle();
                }
            }
            byteArrayPool.releaseBytes(bytes);
            byteArrayPool.releaseBytes(bytes2);
            obtain.release();
            releaseOptions(options3);
            return bitmap;
        } catch (Throwable th4) {
            e222222 = th4;
            options = options3;
            th = e222222;
            byteArrayPool.releaseBytes(bytes);
            byteArrayPool.releaseBytes(bytes2);
            obtain.release();
            releaseOptions(options);
            throw th;
        }
    }

    private int getRoundedSampleSize(int i, int i2, int i3, int i4, int i5) {
        if (i5 == Integer.MIN_VALUE) {
            i5 = i3;
        }
        if (i4 == Integer.MIN_VALUE) {
            i4 = i2;
        }
        if (i != 90) {
            if (i != 270) {
                i = getSampleSize(i2, i3, i4, i5);
                if (i != 0) {
                    i = 0;
                } else {
                    i = Integer.highestOneBit(i);
                }
                return Math.max(1, i);
            }
        }
        i = getSampleSize(i3, i2, i4, i5);
        if (i != 0) {
            i = Integer.highestOneBit(i);
        } else {
            i = 0;
        }
        return Math.max(1, i);
    }

    private Bitmap downsampleWithSize(MarkEnforcingInputStream markEnforcingInputStream, RecyclableBufferedInputStream recyclableBufferedInputStream, Options options, BitmapPool bitmapPool, int i, int i2, int i3, DecodeFormat decodeFormat) {
        decodeFormat = getConfig(markEnforcingInputStream, decodeFormat);
        options.inSampleSize = i3;
        options.inPreferredConfig = decodeFormat;
        if ((options.inSampleSize == 1 || 19 <= VERSION.SDK_INT) && shouldUsePool(markEnforcingInputStream)) {
            double d = (double) i3;
            setInBitmap(options, bitmapPool.getDirty((int) Math.ceil(((double) i) / d), (int) Math.ceil(((double) i2) / d), decodeFormat));
        }
        return decodeStream(markEnforcingInputStream, recyclableBufferedInputStream, options);
    }

    private static boolean shouldUsePool(InputStream inputStream) {
        if (19 <= VERSION.SDK_INT) {
            return true;
        }
        inputStream.mark(1024);
        boolean contains;
        try {
            contains = TYPES_THAT_USE_POOL.contains(new ImageHeaderParser(inputStream).getType());
            return contains;
        } catch (IOException e) {
            contains = e;
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Cannot determine the image type from header", contains);
            }
            return null;
        } finally {
            try {
                inputStream.reset();
            } catch (InputStream inputStream2) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Cannot reset the input stream", inputStream2);
                }
            }
        }
    }

    private static Config getConfig(InputStream inputStream, DecodeFormat decodeFormat) {
        if (!(decodeFormat == DecodeFormat.ALWAYS_ARGB_8888 || decodeFormat == DecodeFormat.PREFER_ARGB_8888)) {
            if (VERSION.SDK_INT != 16) {
                boolean z = false;
                inputStream.mark(1024);
                boolean hasAlpha;
                try {
                    hasAlpha = new ImageHeaderParser(inputStream).hasAlpha();
                    z = hasAlpha;
                } catch (IOException e) {
                    hasAlpha = e;
                    if (Log.isLoggable(TAG, 5)) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot determine whether the image has alpha or not from header for format ");
                        stringBuilder.append(decodeFormat);
                        Log.w(str, stringBuilder.toString(), hasAlpha);
                    }
                    if (z) {
                        inputStream = Config.ARGB_8888;
                    } else {
                        inputStream = Config.RGB_565;
                    }
                    return inputStream;
                } finally {
                    try {
                        inputStream.reset();
                    } catch (InputStream inputStream2) {
                        if (Log.isLoggable(TAG, 5)) {
                            Log.w(TAG, "Cannot reset the input stream", inputStream2);
                        }
                    }
                }
                if (z) {
                    inputStream2 = Config.ARGB_8888;
                } else {
                    inputStream2 = Config.RGB_565;
                }
                return inputStream2;
            }
        }
        return Config.ARGB_8888;
    }

    public int[] getDimensions(MarkEnforcingInputStream markEnforcingInputStream, RecyclableBufferedInputStream recyclableBufferedInputStream, Options options) {
        options.inJustDecodeBounds = true;
        decodeStream(markEnforcingInputStream, recyclableBufferedInputStream, options);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private static Bitmap decodeStream(MarkEnforcingInputStream markEnforcingInputStream, RecyclableBufferedInputStream recyclableBufferedInputStream, Options options) {
        if (options.inJustDecodeBounds) {
            markEnforcingInputStream.mark(MARK_POSITION);
        } else {
            recyclableBufferedInputStream.fixMarkLimit();
        }
        recyclableBufferedInputStream = BitmapFactory.decodeStream(markEnforcingInputStream, null, options);
        try {
            if (options.inJustDecodeBounds) {
                markEnforcingInputStream.reset();
            }
        } catch (MarkEnforcingInputStream markEnforcingInputStream2) {
            if (Log.isLoggable(TAG, 6)) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception loading inDecodeBounds=");
                stringBuilder.append(options.inJustDecodeBounds);
                stringBuilder.append(" sample=");
                stringBuilder.append(options.inSampleSize);
                Log.e(str, stringBuilder.toString(), markEnforcingInputStream2);
            }
        }
        return recyclableBufferedInputStream;
    }

    @TargetApi(11)
    private static void setInBitmap(Options options, Bitmap bitmap) {
        if (11 <= VERSION.SDK_INT) {
            options.inBitmap = bitmap;
        }
    }

    @TargetApi(11)
    private static synchronized Options getDefaultOptions() {
        Options options;
        synchronized (Downsampler.class) {
            synchronized (OPTIONS_QUEUE) {
                options = (Options) OPTIONS_QUEUE.poll();
            }
            if (options == null) {
                options = new Options();
                resetOptions(options);
            }
        }
        return options;
    }

    private static void releaseOptions(Options options) {
        resetOptions(options);
        synchronized (OPTIONS_QUEUE) {
            OPTIONS_QUEUE.offer(options);
        }
    }

    @TargetApi(11)
    private static void resetOptions(Options options) {
        options.inTempStorage = null;
        options.inDither = false;
        options.inScaled = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = null;
        options.inJustDecodeBounds = false;
        options.outWidth = 0;
        options.outHeight = 0;
        options.outMimeType = null;
        if (11 <= VERSION.SDK_INT) {
            options.inBitmap = null;
            options.inMutable = true;
        }
    }
}
