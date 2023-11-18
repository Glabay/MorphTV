package com.bumptech.glide.load.resource.bitmap;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImageHeaderParser {
    private static final int[] BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    private static final int EXIF_MAGIC_NUMBER = 65496;
    private static final int EXIF_SEGMENT_TYPE = 225;
    private static final int GIF_HEADER = 4671814;
    private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
    private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
    private static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
    private static final int MARKER_EOI = 217;
    private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
    private static final int ORIENTATION_TAG_TYPE = 274;
    private static final int PNG_HEADER = -1991225785;
    private static final int SEGMENT_SOS = 218;
    private static final int SEGMENT_START_ID = 255;
    private static final String TAG = "ImageHeaderParser";
    private final StreamReader streamReader;

    public enum ImageType {
        GIF(true),
        JPEG(false),
        PNG_A(true),
        PNG(false),
        UNKNOWN(false);
        
        private final boolean hasAlpha;

        private ImageType(boolean z) {
            this.hasAlpha = z;
        }

        public boolean hasAlpha() {
            return this.hasAlpha;
        }
    }

    private static class RandomAccessReader {
        private final ByteBuffer data;

        public RandomAccessReader(byte[] bArr) {
            this.data = ByteBuffer.wrap(bArr);
            this.data.order(ByteOrder.BIG_ENDIAN);
        }

        public void order(ByteOrder byteOrder) {
            this.data.order(byteOrder);
        }

        public int length() {
            return this.data.array().length;
        }

        public int getInt32(int i) {
            return this.data.getInt(i);
        }

        public short getInt16(int i) {
            return this.data.getShort(i);
        }
    }

    private static class StreamReader {
        private final InputStream is;

        public StreamReader(InputStream inputStream) {
            this.is = inputStream;
        }

        public int getUInt16() throws IOException {
            return ((this.is.read() << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (this.is.read() & 255);
        }

        public short getUInt8() throws IOException {
            return (short) (this.is.read() & 255);
        }

        public long skip(long j) throws IOException {
            if (j < 0) {
                return 0;
            }
            long j2 = j;
            while (j2 > 0) {
                long j3;
                long skip = this.is.skip(j2);
                if (skip > 0) {
                    j3 = j2 - skip;
                } else if (this.is.read() == -1) {
                    break;
                } else {
                    j3 = j2 - 1;
                }
                j2 = j3;
            }
            return j - j2;
        }

        public int read(byte[] bArr) throws IOException {
            int length = bArr.length;
            while (length > 0) {
                int read = this.is.read(bArr, bArr.length - length, length);
                if (read == -1) {
                    break;
                }
                length -= read;
            }
            return bArr.length - length;
        }

        public int getByte() throws IOException {
            return this.is.read();
        }
    }

    private static int calcTagOffset(int i, int i2) {
        return (i + 2) + (i2 * 12);
    }

    private static boolean handles(int i) {
        if (!((i & EXIF_MAGIC_NUMBER) == EXIF_MAGIC_NUMBER || i == MOTOROLA_TIFF_MAGIC_NUMBER)) {
            if (i != INTEL_TIFF_MAGIC_NUMBER) {
                return false;
            }
        }
        return true;
    }

    static {
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
        r0 = 13;
        r0 = new int[r0];
        r0 = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
        BYTES_PER_FORMAT = r0;
        r0 = 0;
        r0 = new byte[r0];
        r1 = "Exif\u0000\u0000";	 Catch:{ UnsupportedEncodingException -> 0x0015 }
        r2 = "UTF-8";	 Catch:{ UnsupportedEncodingException -> 0x0015 }
        r1 = r1.getBytes(r2);	 Catch:{ UnsupportedEncodingException -> 0x0015 }
        r0 = r1;
    L_0x0015:
        JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.ImageHeaderParser.<clinit>():void");
    }

    public ImageHeaderParser(InputStream inputStream) {
        this.streamReader = new StreamReader(inputStream);
    }

    public boolean hasAlpha() throws IOException {
        return getType().hasAlpha();
    }

    public ImageType getType() throws IOException {
        int uInt16 = this.streamReader.getUInt16();
        if (uInt16 == EXIF_MAGIC_NUMBER) {
            return ImageType.JPEG;
        }
        uInt16 = ((uInt16 << 16) & SupportMenu.CATEGORY_MASK) | (this.streamReader.getUInt16() & 65535);
        if (uInt16 == PNG_HEADER) {
            this.streamReader.skip(21);
            return this.streamReader.getByte() >= 3 ? ImageType.PNG_A : ImageType.PNG;
        } else if ((uInt16 >> 8) == GIF_HEADER) {
            return ImageType.GIF;
        } else {
            return ImageType.UNKNOWN;
        }
    }

    public int getOrientation() throws IOException {
        if (!handles(this.streamReader.getUInt16())) {
            return -1;
        }
        byte[] exifSegment = getExifSegment();
        Object obj = null;
        Object obj2 = (exifSegment == null || exifSegment.length <= JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) ? null : 1;
        if (obj2 != null) {
            for (int i = 0; i < JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length; i++) {
                if (exifSegment[i] != JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[i]) {
                    break;
                }
            }
        }
        obj = obj2;
        if (obj != null) {
            return parseExifSegment(new RandomAccessReader(exifSegment));
        }
        return -1;
    }

    private byte[] getExifSegment() throws IOException {
        long skip;
        long j;
        do {
            short uInt8 = this.streamReader.getUInt8();
            if (uInt8 != (short) 255) {
                if (Log.isLoggable(TAG, 3)) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown segmentId=");
                    stringBuilder.append(uInt8);
                    Log.d(str, stringBuilder.toString());
                }
                return null;
            }
            uInt8 = this.streamReader.getUInt8();
            if (uInt8 == (short) 218) {
                return null;
            }
            if (uInt8 == (short) 217) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Found MARKER_EOI in exif segment");
                }
                return null;
            }
            int uInt16 = this.streamReader.getUInt16() - 2;
            if (uInt8 != (short) 225) {
                j = (long) uInt16;
                skip = this.streamReader.skip(j);
            } else {
                byte[] bArr = new byte[uInt16];
                int read = this.streamReader.read(bArr);
                if (read == uInt16) {
                    return bArr;
                }
                if (Log.isLoggable(TAG, 3)) {
                    String str2 = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unable to read segment data, type: ");
                    stringBuilder2.append(uInt8);
                    stringBuilder2.append(", length: ");
                    stringBuilder2.append(uInt16);
                    stringBuilder2.append(", actually read: ");
                    stringBuilder2.append(read);
                    Log.d(str2, stringBuilder2.toString());
                }
                return null;
            }
        } while (skip == j);
        if (Log.isLoggable(TAG, 3)) {
            str2 = TAG;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to skip enough data, type: ");
            stringBuilder2.append(uInt8);
            stringBuilder2.append(", wanted to skip: ");
            stringBuilder2.append(uInt16);
            stringBuilder2.append(", but actually skipped: ");
            stringBuilder2.append(skip);
            Log.d(str2, stringBuilder2.toString());
        }
        return null;
    }

    private static int parseExifSegment(RandomAccessReader randomAccessReader) {
        ByteOrder byteOrder;
        int length = JPEG_EXIF_SEGMENT_PREAMBLE.length();
        short int16 = randomAccessReader.getInt16(length);
        if (int16 == (short) 19789) {
            byteOrder = ByteOrder.BIG_ENDIAN;
        } else if (int16 == (short) 18761) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        } else {
            if (Log.isLoggable(TAG, 3)) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown endianness = ");
                stringBuilder.append(int16);
                Log.d(str, stringBuilder.toString());
            }
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        randomAccessReader.order(byteOrder);
        int int32 = randomAccessReader.getInt32(length + 4) + length;
        short int162 = randomAccessReader.getInt16(int32);
        for (short s = (short) 0; s < int162; s++) {
            int calcTagOffset = calcTagOffset(int32, s);
            short int163 = randomAccessReader.getInt16(calcTagOffset);
            if (int163 == (short) 274) {
                String str2;
                StringBuilder stringBuilder2;
                short int164 = randomAccessReader.getInt16(calcTagOffset + 2);
                if (int164 >= (short) 1) {
                    if (int164 <= (short) 12) {
                        int int322 = randomAccessReader.getInt32(calcTagOffset + 4);
                        if (int322 >= 0) {
                            if (Log.isLoggable(TAG, 3)) {
                                String str3 = TAG;
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("Got tagIndex=");
                                stringBuilder3.append(s);
                                stringBuilder3.append(" tagType=");
                                stringBuilder3.append(int163);
                                stringBuilder3.append(" formatCode=");
                                stringBuilder3.append(int164);
                                stringBuilder3.append(" componentCount=");
                                stringBuilder3.append(int322);
                                Log.d(str3, stringBuilder3.toString());
                            }
                            int322 += BYTES_PER_FORMAT[int164];
                            if (int322 <= 4) {
                                calcTagOffset += 8;
                                if (calcTagOffset >= 0) {
                                    if (calcTagOffset <= randomAccessReader.length()) {
                                        if (int322 >= 0) {
                                            if (int322 + calcTagOffset <= randomAccessReader.length()) {
                                                return randomAccessReader.getInt16(calcTagOffset);
                                            }
                                        }
                                        if (Log.isLoggable(TAG, 3)) {
                                            str2 = TAG;
                                            StringBuilder stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("Illegal number of bytes for TI tag data tagType=");
                                            stringBuilder4.append(int163);
                                            Log.d(str2, stringBuilder4.toString());
                                        }
                                    }
                                }
                                if (Log.isLoggable(TAG, 3)) {
                                    String str4 = TAG;
                                    StringBuilder stringBuilder5 = new StringBuilder();
                                    stringBuilder5.append("Illegal tagValueOffset=");
                                    stringBuilder5.append(calcTagOffset);
                                    stringBuilder5.append(" tagType=");
                                    stringBuilder5.append(int163);
                                    Log.d(str4, stringBuilder5.toString());
                                }
                            } else if (Log.isLoggable(TAG, 3)) {
                                str2 = TAG;
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Got byte count > 4, not orientation, continuing, formatCode=");
                                stringBuilder2.append(int164);
                                Log.d(str2, stringBuilder2.toString());
                            }
                        } else if (Log.isLoggable(TAG, 3)) {
                            Log.d(TAG, "Negative tiff component count");
                        }
                    }
                }
                if (Log.isLoggable(TAG, 3)) {
                    str2 = TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Got invalid format code=");
                    stringBuilder2.append(int164);
                    Log.d(str2, stringBuilder2.toString());
                }
            }
        }
        return -1;
    }
}
