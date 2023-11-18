package com.bumptech.glide.gifdecoder;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GifDecoder {
    private static final Config BITMAP_CONFIG = Config.ARGB_8888;
    private static final int DISPOSAL_BACKGROUND = 2;
    private static final int DISPOSAL_NONE = 1;
    private static final int DISPOSAL_PREVIOUS = 3;
    private static final int DISPOSAL_UNSPECIFIED = 0;
    private static final int INITIAL_FRAME_POINTER = -1;
    private static final int MAX_STACK_SIZE = 4096;
    private static final int NULL_CODE = -1;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    public static final int STATUS_PARTIAL_DECODE = 3;
    private static final String TAG = "GifDecoder";
    public static final int TOTAL_ITERATION_COUNT_FOREVER = 0;
    private int[] act;
    private BitmapProvider bitmapProvider;
    private final byte[] block = new byte[256];
    private byte[] data;
    private int framePointer;
    private GifHeader header;
    private byte[] mainPixels;
    private int[] mainScratch;
    private GifHeaderParser parser;
    private final int[] pct = new int[256];
    private byte[] pixelStack;
    private short[] prefix;
    private Bitmap previousImage;
    private ByteBuffer rawData;
    private boolean savePrevious;
    private int status;
    private byte[] suffix;

    public interface BitmapProvider {
        Bitmap obtain(int i, int i2, Config config);

        void release(Bitmap bitmap);
    }

    public GifDecoder(BitmapProvider bitmapProvider) {
        this.bitmapProvider = bitmapProvider;
        this.header = new GifHeader();
    }

    public int getWidth() {
        return this.header.width;
    }

    public int getHeight() {
        return this.header.height;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getStatus() {
        return this.status;
    }

    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }

    public int getDelay(int i) {
        return (i < 0 || i >= this.header.frameCount) ? -1 : ((GifFrame) this.header.frames.get(i)).delay;
    }

    public int getNextDelay() {
        if (this.header.frameCount > 0) {
            if (this.framePointer >= 0) {
                return getDelay(this.framePointer);
            }
        }
        return -1;
    }

    public int getFrameCount() {
        return this.header.frameCount;
    }

    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    public void resetFrameIndex() {
        this.framePointer = -1;
    }

    @Deprecated
    public int getLoopCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        return this.header.loopCount;
    }

    public int getNetscapeLoopCount() {
        return this.header.loopCount;
    }

    public int getTotalIterationCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        if (this.header.loopCount == 0) {
            return 0;
        }
        return this.header.loopCount + 1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized android.graphics.Bitmap getNextFrame() {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = r7.header;	 Catch:{ all -> 0x00ca }
        r0 = r0.frameCount;	 Catch:{ all -> 0x00ca }
        r1 = 3;
        r2 = 1;
        if (r0 <= 0) goto L_0x000d;
    L_0x0009:
        r0 = r7.framePointer;	 Catch:{ all -> 0x00ca }
        if (r0 >= 0) goto L_0x003b;
    L_0x000d:
        r0 = TAG;	 Catch:{ all -> 0x00ca }
        r0 = android.util.Log.isLoggable(r0, r1);	 Catch:{ all -> 0x00ca }
        if (r0 == 0) goto L_0x0039;
    L_0x0015:
        r0 = TAG;	 Catch:{ all -> 0x00ca }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ca }
        r3.<init>();	 Catch:{ all -> 0x00ca }
        r4 = "unable to decode frame, frameCount=";
        r3.append(r4);	 Catch:{ all -> 0x00ca }
        r4 = r7.header;	 Catch:{ all -> 0x00ca }
        r4 = r4.frameCount;	 Catch:{ all -> 0x00ca }
        r3.append(r4);	 Catch:{ all -> 0x00ca }
        r4 = " framePointer=";
        r3.append(r4);	 Catch:{ all -> 0x00ca }
        r4 = r7.framePointer;	 Catch:{ all -> 0x00ca }
        r3.append(r4);	 Catch:{ all -> 0x00ca }
        r3 = r3.toString();	 Catch:{ all -> 0x00ca }
        android.util.Log.d(r0, r3);	 Catch:{ all -> 0x00ca }
    L_0x0039:
        r7.status = r2;	 Catch:{ all -> 0x00ca }
    L_0x003b:
        r0 = r7.status;	 Catch:{ all -> 0x00ca }
        r3 = 0;
        if (r0 == r2) goto L_0x00a8;
    L_0x0040:
        r0 = r7.status;	 Catch:{ all -> 0x00ca }
        r4 = 2;
        if (r0 != r4) goto L_0x0046;
    L_0x0045:
        goto L_0x00a8;
    L_0x0046:
        r0 = 0;
        r7.status = r0;	 Catch:{ all -> 0x00ca }
        r4 = r7.header;	 Catch:{ all -> 0x00ca }
        r4 = r4.frames;	 Catch:{ all -> 0x00ca }
        r5 = r7.framePointer;	 Catch:{ all -> 0x00ca }
        r4 = r4.get(r5);	 Catch:{ all -> 0x00ca }
        r4 = (com.bumptech.glide.gifdecoder.GifFrame) r4;	 Catch:{ all -> 0x00ca }
        r5 = r7.framePointer;	 Catch:{ all -> 0x00ca }
        r5 = r5 - r2;
        if (r5 < 0) goto L_0x0065;
    L_0x005a:
        r6 = r7.header;	 Catch:{ all -> 0x00ca }
        r6 = r6.frames;	 Catch:{ all -> 0x00ca }
        r5 = r6.get(r5);	 Catch:{ all -> 0x00ca }
        r5 = (com.bumptech.glide.gifdecoder.GifFrame) r5;	 Catch:{ all -> 0x00ca }
        goto L_0x0066;
    L_0x0065:
        r5 = r3;
    L_0x0066:
        r6 = r4.lct;	 Catch:{ all -> 0x00ca }
        if (r6 == 0) goto L_0x006d;
    L_0x006a:
        r6 = r4.lct;	 Catch:{ all -> 0x00ca }
        goto L_0x0071;
    L_0x006d:
        r6 = r7.header;	 Catch:{ all -> 0x00ca }
        r6 = r6.gct;	 Catch:{ all -> 0x00ca }
    L_0x0071:
        r7.act = r6;	 Catch:{ all -> 0x00ca }
        r6 = r7.act;	 Catch:{ all -> 0x00ca }
        if (r6 != 0) goto L_0x008a;
    L_0x0077:
        r0 = TAG;	 Catch:{ all -> 0x00ca }
        r0 = android.util.Log.isLoggable(r0, r1);	 Catch:{ all -> 0x00ca }
        if (r0 == 0) goto L_0x0086;
    L_0x007f:
        r0 = TAG;	 Catch:{ all -> 0x00ca }
        r1 = "No Valid Color Table";
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x00ca }
    L_0x0086:
        r7.status = r2;	 Catch:{ all -> 0x00ca }
        monitor-exit(r7);
        return r3;
    L_0x008a:
        r1 = r4.transparency;	 Catch:{ all -> 0x00ca }
        if (r1 == 0) goto L_0x00a2;
    L_0x008e:
        r1 = r7.act;	 Catch:{ all -> 0x00ca }
        r2 = r7.pct;	 Catch:{ all -> 0x00ca }
        r3 = r7.act;	 Catch:{ all -> 0x00ca }
        r3 = r3.length;	 Catch:{ all -> 0x00ca }
        java.lang.System.arraycopy(r1, r0, r2, r0, r3);	 Catch:{ all -> 0x00ca }
        r1 = r7.pct;	 Catch:{ all -> 0x00ca }
        r7.act = r1;	 Catch:{ all -> 0x00ca }
        r1 = r7.act;	 Catch:{ all -> 0x00ca }
        r2 = r4.transIndex;	 Catch:{ all -> 0x00ca }
        r1[r2] = r0;	 Catch:{ all -> 0x00ca }
    L_0x00a2:
        r0 = r7.setPixels(r4, r5);	 Catch:{ all -> 0x00ca }
        monitor-exit(r7);
        return r0;
    L_0x00a8:
        r0 = TAG;	 Catch:{ all -> 0x00ca }
        r0 = android.util.Log.isLoggable(r0, r1);	 Catch:{ all -> 0x00ca }
        if (r0 == 0) goto L_0x00c8;
    L_0x00b0:
        r0 = TAG;	 Catch:{ all -> 0x00ca }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ca }
        r1.<init>();	 Catch:{ all -> 0x00ca }
        r2 = "Unable to decode frame, status=";
        r1.append(r2);	 Catch:{ all -> 0x00ca }
        r2 = r7.status;	 Catch:{ all -> 0x00ca }
        r1.append(r2);	 Catch:{ all -> 0x00ca }
        r1 = r1.toString();	 Catch:{ all -> 0x00ca }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x00ca }
    L_0x00c8:
        monitor-exit(r7);
        return r3;
    L_0x00ca:
        r0 = move-exception;
        monitor-exit(r7);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifdecoder.GifDecoder.getNextFrame():android.graphics.Bitmap");
    }

    public int read(InputStream inputStream, int i) {
        if (inputStream != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i > 0 ? i + 4096 : 16384);
                i = new byte[16384];
                while (true) {
                    int read = inputStream.read(i, 0, i.length);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(i, 0, read);
                }
                byteArrayOutputStream.flush();
                read(byteArrayOutputStream.toByteArray());
            } catch (int i2) {
                Log.w(TAG, "Error reading data from stream", i2);
            }
        } else {
            this.status = 2;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (InputStream inputStream2) {
                Log.w(TAG, "Error closing stream", inputStream2);
            }
        }
        return this.status;
    }

    public void clear() {
        this.header = null;
        this.data = null;
        this.mainPixels = null;
        this.mainScratch = null;
        if (this.previousImage != null) {
            this.bitmapProvider.release(this.previousImage);
        }
        this.previousImage = null;
        this.rawData = null;
    }

    public void setData(GifHeader gifHeader, byte[] bArr) {
        this.header = gifHeader;
        this.data = bArr;
        this.status = 0;
        this.framePointer = -1;
        this.rawData = ByteBuffer.wrap(bArr);
        this.rawData.rewind();
        this.rawData.order(ByteOrder.LITTLE_ENDIAN);
        this.savePrevious = false;
        for (GifFrame gifFrame : gifHeader.frames) {
            if (gifFrame.dispose == 3) {
                this.savePrevious = 1;
                break;
            }
        }
        this.mainPixels = new byte[(gifHeader.width * gifHeader.height)];
        this.mainScratch = new int[(gifHeader.width * gifHeader.height)];
    }

    private GifHeaderParser getHeaderParser() {
        if (this.parser == null) {
            this.parser = new GifHeaderParser();
        }
        return this.parser;
    }

    public int read(byte[] bArr) {
        this.data = bArr;
        this.header = getHeaderParser().setData(bArr).parseHeader();
        if (bArr != null) {
            this.rawData = ByteBuffer.wrap(bArr);
            this.rawData.rewind();
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
            this.mainPixels = new byte[(this.header.width * this.header.height)];
            this.mainScratch = new int[(this.header.width * this.header.height)];
            this.savePrevious = null;
            for (GifFrame gifFrame : this.header.frames) {
                if (gifFrame.dispose == 3) {
                    this.savePrevious = 1;
                    break;
                }
            }
        }
        return this.status;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap setPixels(com.bumptech.glide.gifdecoder.GifFrame r14, com.bumptech.glide.gifdecoder.GifFrame r15) {
        /*
        r13 = this;
        r0 = r13.header;
        r0 = r0.width;
        r1 = r13.header;
        r9 = r1.height;
        r10 = r13.mainScratch;
        r11 = 0;
        if (r15 != 0) goto L_0x0010;
    L_0x000d:
        java.util.Arrays.fill(r10, r11);
    L_0x0010:
        r12 = 2;
        if (r15 == 0) goto L_0x0060;
    L_0x0013:
        r1 = r15.dispose;
        if (r1 <= 0) goto L_0x0060;
    L_0x0017:
        r1 = r15.dispose;
        if (r1 != r12) goto L_0x004b;
    L_0x001b:
        r1 = r14.transparency;
        if (r1 != 0) goto L_0x002f;
    L_0x001f:
        r1 = r13.header;
        r1 = r1.bgColor;
        r2 = r14.lct;
        if (r2 == 0) goto L_0x0030;
    L_0x0027:
        r2 = r13.header;
        r2 = r2.bgIndex;
        r3 = r14.transIndex;
        if (r2 != r3) goto L_0x0030;
    L_0x002f:
        r1 = 0;
    L_0x0030:
        r2 = r15.iy;
        r2 = r2 * r0;
        r3 = r15.ix;
        r2 = r2 + r3;
        r3 = r15.ih;
        r3 = r3 * r0;
        r3 = r3 + r2;
    L_0x003c:
        if (r2 >= r3) goto L_0x0060;
    L_0x003e:
        r4 = r15.iw;
        r4 = r4 + r2;
        r5 = r2;
    L_0x0042:
        if (r5 >= r4) goto L_0x0049;
    L_0x0044:
        r10[r5] = r1;
        r5 = r5 + 1;
        goto L_0x0042;
    L_0x0049:
        r2 = r2 + r0;
        goto L_0x003c;
    L_0x004b:
        r15 = r15.dispose;
        r1 = 3;
        if (r15 != r1) goto L_0x0060;
    L_0x0050:
        r15 = r13.previousImage;
        if (r15 == 0) goto L_0x0060;
    L_0x0054:
        r1 = r13.previousImage;
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r2 = r10;
        r4 = r0;
        r7 = r0;
        r8 = r9;
        r1.getPixels(r2, r3, r4, r5, r6, r7, r8);
    L_0x0060:
        r13.decodeBitmapData(r14);
        r15 = 8;
        r1 = 1;
        r15 = 0;
        r2 = 1;
        r3 = 8;
    L_0x006a:
        r4 = r14.ih;
        if (r11 >= r4) goto L_0x00c8;
    L_0x006e:
        r4 = r14.interlace;
        if (r4 == 0) goto L_0x0087;
    L_0x0072:
        r4 = r14.ih;
        r5 = 4;
        if (r15 < r4) goto L_0x0084;
    L_0x0077:
        r2 = r2 + 1;
        switch(r2) {
            case 2: goto L_0x0083;
            case 3: goto L_0x0080;
            case 4: goto L_0x007d;
            default: goto L_0x007c;
        };
    L_0x007c:
        goto L_0x0084;
    L_0x007d:
        r15 = 1;
        r3 = 2;
        goto L_0x0084;
    L_0x0080:
        r15 = 2;
        r3 = 4;
        goto L_0x0084;
    L_0x0083:
        r15 = 4;
    L_0x0084:
        r4 = r15 + r3;
        goto L_0x0089;
    L_0x0087:
        r4 = r15;
        r15 = r11;
    L_0x0089:
        r5 = r14.iy;
        r15 = r15 + r5;
        r5 = r13.header;
        r5 = r5.height;
        if (r15 >= r5) goto L_0x00c4;
    L_0x0092:
        r5 = r13.header;
        r5 = r5.width;
        r15 = r15 * r5;
        r5 = r14.ix;
        r5 = r5 + r15;
        r6 = r14.iw;
        r6 = r6 + r5;
        r7 = r13.header;
        r7 = r7.width;
        r7 = r7 + r15;
        if (r7 >= r6) goto L_0x00aa;
    L_0x00a5:
        r6 = r13.header;
        r6 = r6.width;
        r6 = r6 + r15;
    L_0x00aa:
        r15 = r14.iw;
        r15 = r15 * r11;
    L_0x00ae:
        if (r5 >= r6) goto L_0x00c4;
    L_0x00b0:
        r7 = r13.mainPixels;
        r8 = r15 + 1;
        r15 = r7[r15];
        r15 = r15 & 255;
        r7 = r13.act;
        r15 = r7[r15];
        if (r15 == 0) goto L_0x00c0;
    L_0x00be:
        r10[r5] = r15;
    L_0x00c0:
        r5 = r5 + 1;
        r15 = r8;
        goto L_0x00ae;
    L_0x00c4:
        r11 = r11 + 1;
        r15 = r4;
        goto L_0x006a;
    L_0x00c8:
        r15 = r13.savePrevious;
        if (r15 == 0) goto L_0x00ea;
    L_0x00cc:
        r15 = r14.dispose;
        if (r15 == 0) goto L_0x00d4;
    L_0x00d0:
        r14 = r14.dispose;
        if (r14 != r1) goto L_0x00ea;
    L_0x00d4:
        r14 = r13.previousImage;
        if (r14 != 0) goto L_0x00de;
    L_0x00d8:
        r14 = r13.getNextBitmap();
        r13.previousImage = r14;
    L_0x00de:
        r1 = r13.previousImage;
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r2 = r10;
        r4 = r0;
        r7 = r0;
        r8 = r9;
        r1.setPixels(r2, r3, r4, r5, r6, r7, r8);
    L_0x00ea:
        r14 = r13.getNextBitmap();
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r1 = r14;
        r2 = r10;
        r4 = r0;
        r7 = r0;
        r8 = r9;
        r1.setPixels(r2, r3, r4, r5, r6, r7, r8);
        return r14;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifdecoder.GifDecoder.setPixels(com.bumptech.glide.gifdecoder.GifFrame, com.bumptech.glide.gifdecoder.GifFrame):android.graphics.Bitmap");
    }

    private void decodeBitmapData(GifFrame gifFrame) {
        int i;
        int i2;
        GifDecoder gifDecoder = this;
        GifFrame gifFrame2 = gifFrame;
        if (gifFrame2 != null) {
            gifDecoder.rawData.position(gifFrame2.bufferFrameStart);
        }
        if (gifFrame2 == null) {
            i = gifDecoder.header.width * gifDecoder.header.height;
        } else {
            i = gifFrame2.ih * gifFrame2.iw;
        }
        if (gifDecoder.mainPixels == null || gifDecoder.mainPixels.length < i) {
            gifDecoder.mainPixels = new byte[i];
        }
        if (gifDecoder.prefix == null) {
            gifDecoder.prefix = new short[4096];
        }
        if (gifDecoder.suffix == null) {
            gifDecoder.suffix = new byte[4096];
        }
        if (gifDecoder.pixelStack == null) {
            gifDecoder.pixelStack = new byte[FragmentTransaction.TRANSIT_FRAGMENT_OPEN];
        }
        int read = read();
        int i3 = 1;
        int i4 = 1 << read;
        int i5 = i4 + 1;
        int i6 = i4 + 2;
        read++;
        int i7 = (1 << read) - 1;
        for (i2 = 0; i2 < i4; i2++) {
            gifDecoder.prefix[i2] = (short) 0;
            gifDecoder.suffix[i2] = (byte) i2;
        }
        i2 = -1;
        int i8 = read;
        int i9 = i6;
        int i10 = i7;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        int i17 = 0;
        int i18 = 0;
        int i19 = -1;
        while (i11 < i) {
            int i20;
            int i21 = 3;
            if (i12 == 0) {
                i12 = readBlock();
                if (i12 <= 0) {
                    gifDecoder.status = 3;
                    break;
                }
                i15 = 0;
            }
            i14 += (gifDecoder.block[i15] & 255) << i16;
            i15 += i3;
            i12 += i2;
            int i22 = i16 + 8;
            int i23 = i17;
            i3 = i19;
            i16 = i11;
            i17 = i13;
            i13 = i9;
            i11 = i8;
            while (i22 >= i11) {
                i2 = i14 & i10;
                i14 >>= i11;
                i22 -= i11;
                if (i2 != i4) {
                    if (i2 > i13) {
                        gifDecoder.status = i21;
                    } else if (i2 != i5) {
                        if (i3 == -1) {
                            i9 = i18 + 1;
                            gifDecoder.pixelStack[i18] = gifDecoder.suffix[i2];
                            i3 = i2;
                            i23 = i3;
                            i18 = i9;
                        } else {
                            int i24;
                            int i25;
                            if (i2 >= i13) {
                                i9 = i18 + 1;
                                i24 = read;
                                gifDecoder.pixelStack[i18] = (byte) i23;
                                read = i3;
                                i18 = i9;
                            } else {
                                i24 = read;
                                read = i2;
                            }
                            while (read >= i4) {
                                i9 = i18 + 1;
                                i25 = i22;
                                gifDecoder.pixelStack[i18] = gifDecoder.suffix[read];
                                read = gifDecoder.prefix[read];
                                i18 = i9;
                                i22 = i25;
                            }
                            i25 = i22;
                            read = gifDecoder.suffix[read] & 255;
                            i21 = i18 + 1;
                            i20 = i4;
                            byte b = (byte) read;
                            gifDecoder.pixelStack[i18] = b;
                            if (i13 < 4096) {
                                gifDecoder.prefix[i13] = (short) i3;
                                gifDecoder.suffix[i13] = b;
                                i13++;
                                if ((i13 & i10) == 0) {
                                    if (i13 < 4096) {
                                        i11++;
                                        i10 += i13;
                                    }
                                }
                            }
                            i18 = i21;
                            while (i18 > 0) {
                                i18--;
                                i4 = i17 + 1;
                                gifDecoder.mainPixels[i17] = gifDecoder.pixelStack[i18];
                                i16++;
                                i17 = i4;
                            }
                            i23 = read;
                            i3 = i2;
                            read = i24;
                            i22 = i25;
                            i4 = i20;
                        }
                        i21 = 3;
                    }
                    i19 = i3;
                    i8 = i11;
                    i9 = i13;
                    i11 = i16;
                    i13 = i17;
                    i17 = i23;
                    i3 = 1;
                    i2 = -1;
                    i16 = i22;
                    break;
                }
                i11 = read;
                i13 = i6;
                i10 = i7;
                i3 = -1;
                i2 = -1;
            }
            i20 = i4;
            i19 = i3;
            i8 = i11;
            i9 = i13;
            i11 = i16;
            i13 = i17;
            i3 = 1;
            i17 = i23;
            i16 = i22;
            read = read;
        }
        while (i13 < i) {
            gifDecoder.mainPixels[i13] = (byte) 0;
            i13++;
        }
    }

    private int read() {
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
        r0 = r1.rawData;	 Catch:{ Exception -> 0x0009 }
        r0 = r0.get();	 Catch:{ Exception -> 0x0009 }
        r0 = r0 & 255;
        goto L_0x000d;
    L_0x0009:
        r0 = 1;
        r1.status = r0;
        r0 = 0;
    L_0x000d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifdecoder.GifDecoder.read():int");
    }

    private int readBlock() {
        int read = read();
        int i = 0;
        if (read > 0) {
            while (i < read) {
                int i2 = read - i;
                try {
                    this.rawData.get(this.block, i, i2);
                    i += i2;
                } catch (Throwable e) {
                    Log.w(TAG, "Error Reading Block", e);
                    this.status = 1;
                }
            }
        }
        return i;
    }

    private Bitmap getNextBitmap() {
        Bitmap obtain = this.bitmapProvider.obtain(this.header.width, this.header.height, BITMAP_CONFIG);
        if (obtain == null) {
            obtain = Bitmap.createBitmap(this.header.width, this.header.height, BITMAP_CONFIG);
        }
        setAlpha(obtain);
        return obtain;
    }

    @TargetApi(12)
    private static void setAlpha(Bitmap bitmap) {
        if (VERSION.SDK_INT >= 12) {
            bitmap.setHasAlpha(true);
        }
    }
}
