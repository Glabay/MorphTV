package com.bumptech.glide.gifdecoder;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GifHeaderParser {
    static final int DEFAULT_FRAME_DELAY = 10;
    private static final int MAX_BLOCK_SIZE = 256;
    static final int MIN_FRAME_DELAY = 3;
    public static final String TAG = "GifHeaderParser";
    private final byte[] block = new byte[256];
    private int blockSize = 0;
    private GifHeader header;
    private ByteBuffer rawData;

    public GifHeaderParser setData(byte[] bArr) {
        reset();
        if (bArr != null) {
            this.rawData = ByteBuffer.wrap(bArr);
            this.rawData.rewind();
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
        } else {
            this.rawData = null;
            this.header.status = 2;
        }
        return this;
    }

    public void clear() {
        this.rawData = null;
        this.header = null;
    }

    private void reset() {
        this.rawData = null;
        Arrays.fill(this.block, (byte) 0);
        this.header = new GifHeader();
        this.blockSize = 0;
    }

    public GifHeader parseHeader() {
        if (this.rawData == null) {
            throw new IllegalStateException("You must call setData() before parseHeader()");
        } else if (err()) {
            return this.header;
        } else {
            readHeader();
            if (!err()) {
                readContents();
                if (this.header.frameCount < 0) {
                    this.header.status = 1;
                }
            }
            return this.header;
        }
    }

    private void readContents() {
        Object obj = null;
        while (obj == null && !err()) {
            int read = read();
            if (read == 33) {
                read = read();
                if (read == 1) {
                    skip();
                } else if (read != 249) {
                    switch (read) {
                        case 254:
                            skip();
                            break;
                        case 255:
                            readBlock();
                            String str = "";
                            for (read = 0; read < 11; read++) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(str);
                                stringBuilder.append((char) this.block[read]);
                                str = stringBuilder.toString();
                            }
                            if (!str.equals("NETSCAPE2.0")) {
                                skip();
                                break;
                            } else {
                                readNetscapeExt();
                                break;
                            }
                        default:
                            skip();
                            break;
                    }
                } else {
                    this.header.currentFrame = new GifFrame();
                    readGraphicControlExt();
                }
            } else if (read == 44) {
                if (this.header.currentFrame == null) {
                    this.header.currentFrame = new GifFrame();
                }
                readBitmap();
            } else if (read != 59) {
                this.header.status = 1;
            } else {
                obj = 1;
            }
        }
    }

    private void readGraphicControlExt() {
        read();
        int read = read();
        this.header.currentFrame.dispose = (read & 28) >> 2;
        boolean z = true;
        if (this.header.currentFrame.dispose == 0) {
            this.header.currentFrame.dispose = 1;
        }
        GifFrame gifFrame = this.header.currentFrame;
        if ((read & 1) == 0) {
            z = false;
        }
        gifFrame.transparency = z;
        read = readShort();
        if (read < 3) {
            read = 10;
        }
        this.header.currentFrame.delay = read * 10;
        this.header.currentFrame.transIndex = read();
        read();
    }

    private void readBitmap() {
        this.header.currentFrame.ix = readShort();
        this.header.currentFrame.iy = readShort();
        this.header.currentFrame.iw = readShort();
        this.header.currentFrame.ih = readShort();
        int read = read();
        boolean z = false;
        Object obj = (read & 128) != 0 ? 1 : null;
        int pow = (int) Math.pow(2.0d, (double) ((read & 7) + 1));
        GifFrame gifFrame = this.header.currentFrame;
        if ((read & 64) != 0) {
            z = true;
        }
        gifFrame.interlace = z;
        if (obj != null) {
            this.header.currentFrame.lct = readColorTable(pow);
        } else {
            this.header.currentFrame.lct = null;
        }
        this.header.currentFrame.bufferFrameStart = this.rawData.position();
        skipImageData();
        if (!err()) {
            GifHeader gifHeader = this.header;
            gifHeader.frameCount++;
            this.header.frames.add(this.header.currentFrame);
        }
    }

    private void readNetscapeExt() {
        do {
            readBlock();
            if (this.block[0] == (byte) 1) {
                this.header.loopCount = (this.block[1] & 255) | ((this.block[2] & 255) << 8);
            }
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }

    private void readHeader() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append((char) read());
            str = stringBuilder.toString();
        }
        if (str.startsWith("GIF")) {
            readLSD();
            if (this.header.gctFlag && !err()) {
                this.header.gct = readColorTable(this.header.gctSize);
                this.header.bgColor = this.header.gct[this.header.bgIndex];
            }
            return;
        }
        this.header.status = 1;
    }

    private void readLSD() {
        this.header.width = readShort();
        this.header.height = readShort();
        int read = read();
        this.header.gctFlag = (read & 128) != 0;
        this.header.gctSize = 2 << (read & 7);
        this.header.bgIndex = read();
        this.header.pixelAspect = read();
    }

    private int[] readColorTable(int i) {
        int[] iArr;
        byte[] bArr = new byte[(i * 3)];
        try {
            this.rawData.get(bArr);
            iArr = new int[256];
            int i2 = 0;
            int i3 = 0;
            while (i2 < i) {
                int i4 = i3 + 1;
                try {
                    int i5 = i4 + 1;
                    int i6 = i5 + 1;
                    int i7 = i2 + 1;
                    iArr[i2] = ((((bArr[i3] & 255) << 16) | ViewCompat.MEASURED_STATE_MASK) | ((bArr[i4] & 255) << 8)) | (bArr[i5] & 255);
                    i3 = i6;
                    i2 = i7;
                } catch (BufferUnderflowException e) {
                    i = e;
                }
            }
        } catch (BufferUnderflowException e2) {
            i = e2;
            iArr = null;
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Format Error Reading Color Table", i);
            }
            this.header.status = 1;
            return iArr;
        }
        return iArr;
    }

    private void skipImageData() {
        read();
        skip();
    }

    private void skip() {
        int read;
        do {
            read = read();
            this.rawData.position(this.rawData.position() + read);
        } while (read > 0);
    }

    private int readBlock() {
        this.blockSize = read();
        int i = 0;
        if (this.blockSize > 0) {
            int i2 = 0;
            while (i < this.blockSize) {
                try {
                    i2 = this.blockSize - i;
                    this.rawData.get(this.block, i, i2);
                    i += i2;
                } catch (Throwable e) {
                    if (Log.isLoggable(TAG, 3)) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Error Reading Block n: ");
                        stringBuilder.append(i);
                        stringBuilder.append(" count: ");
                        stringBuilder.append(i2);
                        stringBuilder.append(" blockSize: ");
                        stringBuilder.append(this.blockSize);
                        Log.d(str, stringBuilder.toString(), e);
                    }
                    this.header.status = 1;
                }
            }
        }
        return i;
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
        r2 = this;
        r0 = r2.rawData;	 Catch:{ Exception -> 0x0009 }
        r0 = r0.get();	 Catch:{ Exception -> 0x0009 }
        r0 = r0 & 255;
        goto L_0x000f;
    L_0x0009:
        r0 = r2.header;
        r1 = 1;
        r0.status = r1;
        r0 = 0;
    L_0x000f:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifdecoder.GifHeaderParser.read():int");
    }

    private int readShort() {
        return this.rawData.getShort();
    }

    private boolean err() {
        return this.header.status != 0;
    }
}
