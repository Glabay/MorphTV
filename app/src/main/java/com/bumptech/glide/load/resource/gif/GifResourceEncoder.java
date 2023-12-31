package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.LogTime;
import java.io.OutputStream;

public class GifResourceEncoder implements ResourceEncoder<GifDrawable> {
    private static final Factory FACTORY = new Factory();
    private static final String TAG = "GifEncoder";
    private final BitmapPool bitmapPool;
    private final Factory factory;
    private final BitmapProvider provider;

    static class Factory {
        Factory() {
        }

        public GifDecoder buildDecoder(BitmapProvider bitmapProvider) {
            return new GifDecoder(bitmapProvider);
        }

        public GifHeaderParser buildParser() {
            return new GifHeaderParser();
        }

        public AnimatedGifEncoder buildEncoder() {
            return new AnimatedGifEncoder();
        }

        public Resource<Bitmap> buildFrameResource(Bitmap bitmap, BitmapPool bitmapPool) {
            return new BitmapResource(bitmap, bitmapPool);
        }
    }

    public String getId() {
        return "";
    }

    public GifResourceEncoder(BitmapPool bitmapPool) {
        this(bitmapPool, FACTORY);
    }

    GifResourceEncoder(BitmapPool bitmapPool, Factory factory) {
        this.bitmapPool = bitmapPool;
        this.provider = new GifBitmapProvider(bitmapPool);
        this.factory = factory;
    }

    public boolean encode(Resource<GifDrawable> resource, OutputStream outputStream) {
        long logTime = LogTime.getLogTime();
        GifDrawable gifDrawable = (GifDrawable) resource.get();
        Transformation frameTransformation = gifDrawable.getFrameTransformation();
        if (frameTransformation instanceof UnitTransformation) {
            return writeDataDirect(gifDrawable.getData(), outputStream);
        }
        GifDecoder decodeHeaders = decodeHeaders(gifDrawable.getData());
        AnimatedGifEncoder buildEncoder = this.factory.buildEncoder();
        if (buildEncoder.start(outputStream) == null) {
            return false;
        }
        outputStream = null;
        while (outputStream < decodeHeaders.getFrameCount()) {
            Resource transformedFrame = getTransformedFrame(decodeHeaders.getNextFrame(), frameTransformation, gifDrawable);
            if (buildEncoder.addFrame((Bitmap) transformedFrame.get())) {
                try {
                    buildEncoder.setDelay(decodeHeaders.getDelay(decodeHeaders.getCurrentFrameIndex()));
                    decodeHeaders.advance();
                    transformedFrame.recycle();
                    outputStream++;
                } catch (Resource<GifDrawable> resource2) {
                    transformedFrame.recycle();
                    throw resource2;
                }
            }
            transformedFrame.recycle();
            return false;
        }
        outputStream = buildEncoder.finish();
        if (Log.isLoggable(TAG, 2)) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Encoded gif with ");
            stringBuilder.append(decodeHeaders.getFrameCount());
            stringBuilder.append(" frames and ");
            stringBuilder.append(gifDrawable.getData().length);
            stringBuilder.append(" bytes in ");
            stringBuilder.append(LogTime.getElapsedMillis(logTime));
            stringBuilder.append(" ms");
            Log.v(str, stringBuilder.toString());
        }
        return outputStream;
    }

    private boolean writeDataDirect(byte[] bArr, OutputStream outputStream) {
        try {
            outputStream.write(bArr);
            return 1;
        } catch (byte[] bArr2) {
            if (Log.isLoggable(TAG, 3) != null) {
                Log.d(TAG, "Failed to write data to output stream in GifResourceEncoder", bArr2);
            }
            return null;
        }
    }

    private GifDecoder decodeHeaders(byte[] bArr) {
        GifHeaderParser buildParser = this.factory.buildParser();
        buildParser.setData(bArr);
        GifHeader parseHeader = buildParser.parseHeader();
        GifDecoder buildDecoder = this.factory.buildDecoder(this.provider);
        buildDecoder.setData(parseHeader, bArr);
        buildDecoder.advance();
        return buildDecoder;
    }

    private Resource<Bitmap> getTransformedFrame(Bitmap bitmap, Transformation<Bitmap> transformation, GifDrawable gifDrawable) {
        bitmap = this.factory.buildFrameResource(bitmap, this.bitmapPool);
        transformation = transformation.transform(bitmap, gifDrawable.getIntrinsicWidth(), gifDrawable.getIntrinsicHeight());
        if (bitmap.equals(transformation) == null) {
            bitmap.recycle();
        }
        return transformation;
    }
}
