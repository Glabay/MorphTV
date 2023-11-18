package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import java.io.IOException;
import java.io.InputStream;

public class ImageVideoBitmapDecoder implements ResourceDecoder<ImageVideoWrapper, Bitmap> {
    private static final String TAG = "ImageVideoDecoder";
    private final ResourceDecoder<ParcelFileDescriptor, Bitmap> fileDescriptorDecoder;
    private final ResourceDecoder<InputStream, Bitmap> streamDecoder;

    public String getId() {
        return "ImageVideoBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
    }

    public ImageVideoBitmapDecoder(ResourceDecoder<InputStream, Bitmap> resourceDecoder, ResourceDecoder<ParcelFileDescriptor, Bitmap> resourceDecoder2) {
        this.streamDecoder = resourceDecoder;
        this.fileDescriptorDecoder = resourceDecoder2;
    }

    public Resource<Bitmap> decode(ImageVideoWrapper imageVideoWrapper, int i, int i2) throws IOException {
        Resource<Bitmap> decode;
        InputStream stream = imageVideoWrapper.getStream();
        if (stream != null) {
            try {
                decode = this.streamDecoder.decode(stream, i, i2);
            } catch (Throwable e) {
                if (Log.isLoggable(TAG, 2)) {
                    Log.v(TAG, "Failed to load image from stream, trying FileDescriptor", e);
                }
            }
            if (decode == null) {
                return decode;
            }
            imageVideoWrapper = imageVideoWrapper.getFileDescriptor();
            return imageVideoWrapper == null ? this.fileDescriptorDecoder.decode(imageVideoWrapper, i, i2) : decode;
        }
        decode = null;
        if (decode == null) {
            return decode;
        }
        imageVideoWrapper = imageVideoWrapper.getFileDescriptor();
        if (imageVideoWrapper == null) {
        }
    }
}
