package com.google.android.exoplayer2.metadata.id3;

import android.util.Log;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.lang3.CharEncoding;

public final class Id3Decoder implements MetadataDecoder {
    private static final int FRAME_FLAG_V3_HAS_GROUP_IDENTIFIER = 32;
    private static final int FRAME_FLAG_V3_IS_COMPRESSED = 128;
    private static final int FRAME_FLAG_V3_IS_ENCRYPTED = 64;
    private static final int FRAME_FLAG_V4_HAS_DATA_LENGTH = 1;
    private static final int FRAME_FLAG_V4_HAS_GROUP_IDENTIFIER = 64;
    private static final int FRAME_FLAG_V4_IS_COMPRESSED = 8;
    private static final int FRAME_FLAG_V4_IS_ENCRYPTED = 4;
    private static final int FRAME_FLAG_V4_IS_UNSYNCHRONIZED = 2;
    public static final int ID3_HEADER_LENGTH = 10;
    public static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private static final int ID3_TEXT_ENCODING_ISO_8859_1 = 0;
    private static final int ID3_TEXT_ENCODING_UTF_16 = 1;
    private static final int ID3_TEXT_ENCODING_UTF_16BE = 2;
    private static final int ID3_TEXT_ENCODING_UTF_8 = 3;
    private static final String TAG = "Id3Decoder";
    private final FramePredicate framePredicate;

    public interface FramePredicate {
        boolean evaluate(int i, int i2, int i3, int i4, int i5);
    }

    private static final class Id3Header {
        private final int framesSize;
        private final boolean isUnsynchronized;
        private final int majorVersion;

        public Id3Header(int i, boolean z, int i2) {
            this.majorVersion = i;
            this.isUnsynchronized = z;
            this.framesSize = i2;
        }
    }

    private static int delimiterLength(int i) {
        if (i != 0) {
            if (i != 3) {
                return 2;
            }
        }
        return 1;
    }

    private static String getCharsetName(int i) {
        switch (i) {
            case 0:
                return CharEncoding.ISO_8859_1;
            case 1:
                return "UTF-16";
            case 2:
                return CharEncoding.UTF_16BE;
            case 3:
                return "UTF-8";
            default:
                return CharEncoding.ISO_8859_1;
        }
    }

    public Id3Decoder() {
        this(null);
    }

    public Id3Decoder(FramePredicate framePredicate) {
        this.framePredicate = framePredicate;
    }

    public Metadata decode(MetadataInputBuffer metadataInputBuffer) {
        metadataInputBuffer = metadataInputBuffer.data;
        return decode(metadataInputBuffer.array(), metadataInputBuffer.limit());
    }

    public Metadata decode(byte[] bArr, int i) {
        List arrayList = new ArrayList();
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr, i);
        bArr = decodeHeader(parsableByteArray);
        if (bArr == null) {
            return null;
        }
        int position = parsableByteArray.getPosition();
        int i2 = bArr.majorVersion == 2 ? 6 : 10;
        int access$100 = bArr.framesSize;
        if (bArr.isUnsynchronized) {
            access$100 = removeUnsynchronization(parsableByteArray, bArr.framesSize);
        }
        parsableByteArray.setLimit(position + access$100);
        boolean z = false;
        if (!validateFrames(parsableByteArray, bArr.majorVersion, i2, false)) {
            if (bArr.majorVersion == 4 && validateFrames(parsableByteArray, 4, i2, true)) {
                z = true;
            } else {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to validate ID3 tag with majorVersion=");
                stringBuilder.append(bArr.majorVersion);
                Log.w(str, stringBuilder.toString());
                return null;
            }
        }
        while (parsableByteArray.bytesLeft() >= i2) {
            i = decodeFrame(bArr.majorVersion, parsableByteArray, z, i2, this.framePredicate);
            if (i != 0) {
                arrayList.add(i);
            }
        }
        return new Metadata(arrayList);
    }

    private static Id3Header decodeHeader(ParsableByteArray parsableByteArray) {
        if (parsableByteArray.bytesLeft() < 10) {
            Log.w(TAG, "Data too short to be an ID3 tag");
            return null;
        }
        int readUnsignedInt24 = parsableByteArray.readUnsignedInt24();
        if (readUnsignedInt24 != ID3_TAG) {
            parsableByteArray = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected first three bytes of ID3 tag header: ");
            stringBuilder.append(readUnsignedInt24);
            Log.w(parsableByteArray, stringBuilder.toString());
            return null;
        }
        readUnsignedInt24 = parsableByteArray.readUnsignedByte();
        boolean z = true;
        parsableByteArray.skipBytes(1);
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int readSynchSafeInt = parsableByteArray.readSynchSafeInt();
        if (readUnsignedInt24 == 2) {
            if (((readUnsignedByte & 64) != null ? true : null) != null) {
                Log.w(TAG, "Skipped ID3 tag with majorVersion=2 and undefined compression scheme");
                return null;
            }
        } else if (readUnsignedInt24 == 3) {
            if (((readUnsignedByte & 64) != 0 ? 1 : null) != null) {
                r1 = parsableByteArray.readInt();
                parsableByteArray.skipBytes(r1);
                readSynchSafeInt -= r1 + 4;
            }
        } else if (readUnsignedInt24 == 4) {
            if (((readUnsignedByte & 64) != 0 ? 1 : null) != null) {
                r1 = parsableByteArray.readSynchSafeInt();
                parsableByteArray.skipBytes(r1 - 4);
                readSynchSafeInt -= r1;
            }
            if (((readUnsignedByte & 16) != null ? true : null) != null) {
                readSynchSafeInt -= 10;
            }
        } else {
            parsableByteArray = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Skipped ID3 tag with unsupported majorVersion=");
            stringBuilder.append(readUnsignedInt24);
            Log.w(parsableByteArray, stringBuilder.toString());
            return null;
        }
        if (readUnsignedInt24 >= 4 || (readUnsignedByte & 128) == null) {
            z = false;
        }
        return new Id3Header(readUnsignedInt24, z, readSynchSafeInt);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean validateFrames(com.google.android.exoplayer2.util.ParsableByteArray r20, int r21, int r22, boolean r23) {
        /*
        r1 = r20;
        r2 = r21;
        r3 = r20.getPosition();
    L_0x0008:
        r4 = r20.bytesLeft();	 Catch:{ all -> 0x00bc }
        r5 = 1;
        r6 = r22;
        if (r4 < r6) goto L_0x00b8;
    L_0x0011:
        r4 = 3;
        r7 = 0;
        if (r2 < r4) goto L_0x0022;
    L_0x0015:
        r8 = r20.readInt();	 Catch:{ all -> 0x00bc }
        r9 = r20.readUnsignedInt();	 Catch:{ all -> 0x00bc }
        r11 = r20.readUnsignedShort();	 Catch:{ all -> 0x00bc }
        goto L_0x002c;
    L_0x0022:
        r8 = r20.readUnsignedInt24();	 Catch:{ all -> 0x00bc }
        r9 = r20.readUnsignedInt24();	 Catch:{ all -> 0x00bc }
        r9 = (long) r9;
        r11 = 0;
    L_0x002c:
        r12 = 0;
        if (r8 != 0) goto L_0x003a;
    L_0x0030:
        r8 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1));
        if (r8 != 0) goto L_0x003a;
    L_0x0034:
        if (r11 != 0) goto L_0x003a;
    L_0x0036:
        r1.setPosition(r3);
        return r5;
    L_0x003a:
        r8 = 4;
        if (r2 != r8) goto L_0x0074;
    L_0x003d:
        if (r23 != 0) goto L_0x0074;
    L_0x003f:
        r14 = 8421504; // 0x808080 float:1.180104E-38 double:4.160776E-317;
        r16 = r9 & r14;
        r14 = (r16 > r12 ? 1 : (r16 == r12 ? 0 : -1));
        if (r14 == 0) goto L_0x004c;
    L_0x0048:
        r1.setPosition(r3);
        return r7;
    L_0x004c:
        r12 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r14 = r9 & r12;
        r16 = 8;
        r16 = r9 >> r16;
        r18 = r16 & r12;
        r16 = 7;
        r16 = r18 << r16;
        r18 = r14 | r16;
        r14 = 16;
        r14 = r9 >> r14;
        r16 = r14 & r12;
        r14 = 14;
        r14 = r16 << r14;
        r16 = r18 | r14;
        r14 = 24;
        r9 = r9 >> r14;
        r14 = r9 & r12;
        r9 = 21;
        r9 = r14 << r9;
        r12 = r16 | r9;
        goto L_0x0075;
    L_0x0074:
        r12 = r9;
    L_0x0075:
        if (r2 != r8) goto L_0x0084;
    L_0x0077:
        r4 = r11 & 64;
        if (r4 == 0) goto L_0x007d;
    L_0x007b:
        r4 = 1;
        goto L_0x007e;
    L_0x007d:
        r4 = 0;
    L_0x007e:
        r8 = r11 & 1;
        if (r8 == 0) goto L_0x0093;
    L_0x0082:
        r8 = 1;
        goto L_0x0094;
    L_0x0084:
        if (r2 != r4) goto L_0x0092;
    L_0x0086:
        r4 = r11 & 32;
        if (r4 == 0) goto L_0x008c;
    L_0x008a:
        r4 = 1;
        goto L_0x008d;
    L_0x008c:
        r4 = 0;
    L_0x008d:
        r8 = r11 & 128;
        if (r8 == 0) goto L_0x0093;
    L_0x0091:
        goto L_0x0082;
    L_0x0092:
        r4 = 0;
    L_0x0093:
        r8 = 0;
    L_0x0094:
        if (r4 == 0) goto L_0x0097;
    L_0x0096:
        goto L_0x0098;
    L_0x0097:
        r5 = 0;
    L_0x0098:
        if (r8 == 0) goto L_0x009c;
    L_0x009a:
        r5 = r5 + 4;
    L_0x009c:
        r4 = (long) r5;
        r8 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1));
        if (r8 >= 0) goto L_0x00a5;
    L_0x00a1:
        r1.setPosition(r3);
        return r7;
    L_0x00a5:
        r4 = r20.bytesLeft();	 Catch:{ all -> 0x00bc }
        r4 = (long) r4;
        r8 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r8 >= 0) goto L_0x00b2;
    L_0x00ae:
        r1.setPosition(r3);
        return r7;
    L_0x00b2:
        r4 = (int) r12;
        r1.skipBytes(r4);	 Catch:{ all -> 0x00bc }
        goto L_0x0008;
    L_0x00b8:
        r1.setPosition(r3);
        return r5;
    L_0x00bc:
        r0 = move-exception;
        r2 = r0;
        r1.setPosition(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.metadata.id3.Id3Decoder.validateFrames(com.google.android.exoplayer2.util.ParsableByteArray, int, int, boolean):boolean");
    }

    private static com.google.android.exoplayer2.metadata.id3.Id3Frame decodeFrame(int r20, com.google.android.exoplayer2.util.ParsableByteArray r21, boolean r22, int r23, com.google.android.exoplayer2.metadata.id3.Id3Decoder.FramePredicate r24) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r7 = r20;
        r8 = r21;
        r9 = r21.readUnsignedByte();
        r10 = r21.readUnsignedByte();
        r11 = r21.readUnsignedByte();
        r12 = 3;
        if (r7 < r12) goto L_0x0019;
    L_0x0013:
        r1 = r21.readUnsignedByte();
        r14 = r1;
        goto L_0x001a;
    L_0x0019:
        r14 = 0;
    L_0x001a:
        r15 = 4;
        if (r7 != r15) goto L_0x003d;
    L_0x001d:
        r1 = r21.readUnsignedIntToInt();
        if (r22 != 0) goto L_0x003a;
    L_0x0023:
        r2 = r1 & 255;
        r3 = r1 >> 8;
        r3 = r3 & 255;
        r3 = r3 << 7;
        r2 = r2 | r3;
        r3 = r1 >> 16;
        r3 = r3 & 255;
        r3 = r3 << 14;
        r2 = r2 | r3;
        r1 = r1 >> 24;
        r1 = r1 & 255;
        r1 = r1 << 21;
        r1 = r1 | r2;
    L_0x003a:
        r16 = r1;
        goto L_0x0049;
    L_0x003d:
        if (r7 != r12) goto L_0x0044;
    L_0x003f:
        r1 = r21.readUnsignedIntToInt();
        goto L_0x003a;
    L_0x0044:
        r1 = r21.readUnsignedInt24();
        goto L_0x003a;
    L_0x0049:
        if (r7 < r12) goto L_0x0051;
    L_0x004b:
        r1 = r21.readUnsignedShort();
        r6 = r1;
        goto L_0x0052;
    L_0x0051:
        r6 = 0;
    L_0x0052:
        r17 = 0;
        if (r9 != 0) goto L_0x0068;
    L_0x0056:
        if (r10 != 0) goto L_0x0068;
    L_0x0058:
        if (r11 != 0) goto L_0x0068;
    L_0x005a:
        if (r14 != 0) goto L_0x0068;
    L_0x005c:
        if (r16 != 0) goto L_0x0068;
    L_0x005e:
        if (r6 != 0) goto L_0x0068;
    L_0x0060:
        r1 = r21.limit();
        r8.setPosition(r1);
        return r17;
    L_0x0068:
        r1 = r21.getPosition();
        r5 = r1 + r16;
        r1 = r21.limit();
        if (r5 <= r1) goto L_0x0083;
    L_0x0074:
        r1 = "Id3Decoder";
        r2 = "Frame size exceeds remaining tag data";
        android.util.Log.w(r1, r2);
        r1 = r21.limit();
        r8.setPosition(r1);
        return r17;
    L_0x0083:
        if (r24 == 0) goto L_0x0098;
    L_0x0085:
        r1 = r24;
        r2 = r7;
        r3 = r9;
        r4 = r10;
        r13 = r5;
        r5 = r11;
        r15 = r6;
        r6 = r14;
        r1 = r1.evaluate(r2, r3, r4, r5, r6);
        if (r1 != 0) goto L_0x009a;
    L_0x0094:
        r8.setPosition(r13);
        return r17;
    L_0x0098:
        r13 = r5;
        r15 = r6;
    L_0x009a:
        r1 = 1;
        if (r7 != r12) goto L_0x00b7;
    L_0x009d:
        r2 = r15 & 128;
        if (r2 == 0) goto L_0x00a3;
    L_0x00a1:
        r2 = 1;
        goto L_0x00a4;
    L_0x00a3:
        r2 = 0;
    L_0x00a4:
        r3 = r15 & 64;
        if (r3 == 0) goto L_0x00aa;
    L_0x00a8:
        r3 = 1;
        goto L_0x00ab;
    L_0x00aa:
        r3 = 0;
    L_0x00ab:
        r4 = r15 & 32;
        if (r4 == 0) goto L_0x00b1;
    L_0x00af:
        r4 = 1;
        goto L_0x00b2;
    L_0x00b1:
        r4 = 0;
    L_0x00b2:
        r18 = r4;
        r5 = 0;
        r4 = r2;
        goto L_0x00ee;
    L_0x00b7:
        r2 = 4;
        if (r7 != r2) goto L_0x00e8;
    L_0x00ba:
        r2 = r15 & 64;
        if (r2 == 0) goto L_0x00c0;
    L_0x00be:
        r2 = 1;
        goto L_0x00c1;
    L_0x00c0:
        r2 = 0;
    L_0x00c1:
        r3 = r15 & 8;
        if (r3 == 0) goto L_0x00c7;
    L_0x00c5:
        r3 = 1;
        goto L_0x00c8;
    L_0x00c7:
        r3 = 0;
    L_0x00c8:
        r4 = r15 & 4;
        if (r4 == 0) goto L_0x00ce;
    L_0x00cc:
        r4 = 1;
        goto L_0x00cf;
    L_0x00ce:
        r4 = 0;
    L_0x00cf:
        r5 = r15 & 2;
        if (r5 == 0) goto L_0x00d5;
    L_0x00d3:
        r5 = 1;
        goto L_0x00d6;
    L_0x00d5:
        r5 = 0;
    L_0x00d6:
        r6 = r15 & 1;
        if (r6 == 0) goto L_0x00dd;
    L_0x00da:
        r18 = 1;
        goto L_0x00df;
    L_0x00dd:
        r18 = 0;
    L_0x00df:
        r19 = r18;
        r18 = r2;
        r2 = r3;
        r3 = r4;
        r4 = r19;
        goto L_0x00ee;
    L_0x00e8:
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r18 = 0;
    L_0x00ee:
        if (r2 != 0) goto L_0x021b;
    L_0x00f0:
        if (r3 == 0) goto L_0x00f4;
    L_0x00f2:
        goto L_0x021b;
    L_0x00f4:
        if (r18 == 0) goto L_0x00fb;
    L_0x00f6:
        r16 = r16 + -1;
        r8.skipBytes(r1);
    L_0x00fb:
        if (r4 == 0) goto L_0x0103;
    L_0x00fd:
        r16 = r16 + -4;
        r1 = 4;
        r8.skipBytes(r1);
    L_0x0103:
        r1 = r16;
        if (r5 == 0) goto L_0x010b;
    L_0x0107:
        r1 = removeUnsynchronization(r8, r1);
    L_0x010b:
        r12 = r1;
        r1 = 84;
        r2 = 88;
        r3 = 2;
        if (r9 != r1) goto L_0x0121;
    L_0x0113:
        if (r10 != r2) goto L_0x0121;
    L_0x0115:
        if (r11 != r2) goto L_0x0121;
    L_0x0117:
        if (r7 == r3) goto L_0x011b;
    L_0x0119:
        if (r14 != r2) goto L_0x0121;
    L_0x011b:
        r1 = decodeTxxxFrame(r8, r12);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0121:
        if (r9 != r1) goto L_0x0131;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0123:
        r1 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r1 = decodeTextInformationFrame(r8, r12, r1);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x012d:
        r0 = move-exception;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r1 = r0;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x0217;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0131:
        r4 = 87;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r9 != r4) goto L_0x0143;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0135:
        if (r10 != r2) goto L_0x0143;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0137:
        if (r11 != r2) goto L_0x0143;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0139:
        if (r7 == r3) goto L_0x013d;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x013b:
        if (r14 != r2) goto L_0x0143;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x013d:
        r1 = decodeWxxxFrame(r8, r12);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0143:
        r2 = 87;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r9 != r2) goto L_0x0151;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0147:
        r1 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r1 = decodeUrlLinkFrame(r8, r12, r1);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0151:
        r2 = 73;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r4 = 80;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r9 != r4) goto L_0x0167;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0157:
        r5 = 82;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r10 != r5) goto L_0x0167;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x015b:
        if (r11 != r2) goto L_0x0167;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x015d:
        r5 = 86;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r14 != r5) goto L_0x0167;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0161:
        r1 = decodePrivFrame(r8, r12);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0167:
        r5 = 71;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r6 = 79;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r9 != r5) goto L_0x017f;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x016d:
        r5 = 69;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r10 != r5) goto L_0x017f;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0171:
        if (r11 != r6) goto L_0x017f;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0173:
        r5 = 66;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r14 == r5) goto L_0x0179;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0177:
        if (r7 != r3) goto L_0x017f;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0179:
        r1 = decodeGeobFrame(r8, r12);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x017f:
        r5 = 67;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r7 != r3) goto L_0x018a;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0183:
        if (r9 != r4) goto L_0x0199;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0185:
        if (r10 != r2) goto L_0x0199;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0187:
        if (r11 != r5) goto L_0x0199;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0189:
        goto L_0x0194;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x018a:
        r15 = 65;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r9 != r15) goto L_0x0199;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x018e:
        if (r10 != r4) goto L_0x0199;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0190:
        if (r11 != r2) goto L_0x0199;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0192:
        if (r14 != r5) goto L_0x0199;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0194:
        r1 = decodeApicFrame(r8, r12, r7);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0199:
        if (r9 != r5) goto L_0x01ac;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x019b:
        if (r10 != r6) goto L_0x01ac;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x019d:
        r2 = 77;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r11 != r2) goto L_0x01ac;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01a1:
        r2 = 77;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r14 == r2) goto L_0x01a7;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01a5:
        if (r7 != r3) goto L_0x01ac;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01a7:
        r1 = decodeCommentFrame(r8, r12);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01ac:
        if (r9 != r5) goto L_0x01c6;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01ae:
        r2 = 72;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r10 != r2) goto L_0x01c6;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01b2:
        r2 = 65;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        if (r11 != r2) goto L_0x01c6;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01b6:
        if (r14 != r4) goto L_0x01c6;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01b8:
        r1 = r8;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r2 = r12;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3 = r7;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r4 = r22;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r5 = r23;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r6 = r24;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r1 = decodeChapterFrame(r1, r2, r3, r4, r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01c6:
        if (r9 != r5) goto L_0x01dc;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01c8:
        if (r10 != r1) goto L_0x01dc;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01ca:
        if (r11 != r6) goto L_0x01dc;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01cc:
        if (r14 != r5) goto L_0x01dc;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01ce:
        r1 = r8;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r2 = r12;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3 = r7;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r4 = r22;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r5 = r23;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r6 = r24;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r1 = decodeChapterTOCFrame(r1, r2, r3, r4, r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        goto L_0x01e4;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01dc:
        r1 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r1 = decodeBinaryFrame(r8, r12, r1);	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01e4:
        if (r1 != 0) goto L_0x0208;	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x01e6:
        r2 = "Id3Decoder";	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3.<init>();	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r4 = "Failed to decode frame: id=";	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3.append(r4);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r4 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3.append(r4);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r4 = ", frameSize=";	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3.append(r4);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3.append(r12);	 Catch:{ UnsupportedEncodingException -> 0x020c }
        r3 = r3.toString();	 Catch:{ UnsupportedEncodingException -> 0x020c }
        android.util.Log.w(r2, r3);	 Catch:{ UnsupportedEncodingException -> 0x020c }
    L_0x0208:
        r8.setPosition(r13);
        return r1;
    L_0x020c:
        r1 = "Id3Decoder";	 Catch:{ all -> 0x012d }
        r2 = "Unsupported character encoding";	 Catch:{ all -> 0x012d }
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x012d }
        r8.setPosition(r13);
        return r17;
    L_0x0217:
        r8.setPosition(r13);
        throw r1;
    L_0x021b:
        r1 = "Id3Decoder";
        r2 = "Skipping unsupported compressed or encrypted frame";
        android.util.Log.w(r1, r2);
        r8.setPosition(r13);
        return r17;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.metadata.id3.Id3Decoder.decodeFrame(int, com.google.android.exoplayer2.util.ParsableByteArray, boolean, int, com.google.android.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):com.google.android.exoplayer2.metadata.id3.Id3Frame");
    }

    private static TextInformationFrame decodeTxxxFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        if (i < 1) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        parsableByteArray = indexOfEos(bArr, 0, readUnsignedByte);
        i = new String(bArr, 0, parsableByteArray, charsetName);
        parsableByteArray += delimiterLength(readUnsignedByte);
        return new TextInformationFrame("TXXX", i, decodeStringIfValid(bArr, parsableByteArray, indexOfEos(bArr, parsableByteArray, readUnsignedByte), charsetName));
    }

    private static TextInformationFrame decodeTextInformationFrame(ParsableByteArray parsableByteArray, int i, String str) throws UnsupportedEncodingException {
        if (i < 1) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        return new TextInformationFrame(str, null, new String(bArr, 0, indexOfEos(bArr, 0, readUnsignedByte), charsetName));
    }

    private static UrlLinkFrame decodeWxxxFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        if (i < 1) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        parsableByteArray = indexOfEos(bArr, 0, readUnsignedByte);
        i = new String(bArr, 0, parsableByteArray, charsetName);
        parsableByteArray += delimiterLength(readUnsignedByte);
        return new UrlLinkFrame("WXXX", i, decodeStringIfValid(bArr, parsableByteArray, indexOfZeroByte(bArr, parsableByteArray), CharEncoding.ISO_8859_1));
    }

    private static UrlLinkFrame decodeUrlLinkFrame(ParsableByteArray parsableByteArray, int i, String str) throws UnsupportedEncodingException {
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        return new UrlLinkFrame(str, null, new String(bArr, 0, indexOfZeroByte(bArr, 0), CharEncoding.ISO_8859_1));
    }

    private static PrivFrame decodePrivFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        parsableByteArray = indexOfZeroByte(bArr, 0);
        return new PrivFrame(new String(bArr, 0, parsableByteArray, CharEncoding.ISO_8859_1), copyOfRangeIfValid(bArr, parsableByteArray + 1, bArr.length));
    }

    private static GeobFrame decodeGeobFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        parsableByteArray = indexOfZeroByte(bArr, 0);
        i = new String(bArr, 0, parsableByteArray, CharEncoding.ISO_8859_1);
        parsableByteArray++;
        int indexOfEos = indexOfEos(bArr, parsableByteArray, readUnsignedByte);
        parsableByteArray = decodeStringIfValid(bArr, parsableByteArray, indexOfEos, charsetName);
        indexOfEos += delimiterLength(readUnsignedByte);
        int indexOfEos2 = indexOfEos(bArr, indexOfEos, readUnsignedByte);
        return new GeobFrame(i, parsableByteArray, decodeStringIfValid(bArr, indexOfEos, indexOfEos2, charsetName), copyOfRangeIfValid(bArr, indexOfEos2 + delimiterLength(readUnsignedByte), bArr.length));
    }

    private static ApicFrame decodeApicFrame(ParsableByteArray parsableByteArray, int i, int i2) throws UnsupportedEncodingException {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        i--;
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        if (i2 == 2) {
            i = new StringBuilder();
            i.append("image/");
            i.append(Util.toLowerInvariant(new String(bArr, 0, 3, CharEncoding.ISO_8859_1)));
            i = i.toString();
            if (i.equals("image/jpg") != 0) {
                i = "image/jpeg";
            }
            i2 = i;
            i = 2;
        } else {
            i = indexOfZeroByte(bArr, 0);
            i2 = Util.toLowerInvariant(new String(bArr, 0, i, CharEncoding.ISO_8859_1));
            if (i2.indexOf(47) == -1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("image/");
                stringBuilder.append(i2);
                i2 = stringBuilder.toString();
            }
        }
        int i3 = bArr[i + 1] & 255;
        i += 2;
        parsableByteArray = indexOfEos(bArr, i, readUnsignedByte);
        return new ApicFrame(i2, new String(bArr, i, parsableByteArray - i, charsetName), i3, copyOfRangeIfValid(bArr, parsableByteArray + delimiterLength(readUnsignedByte), bArr.length));
    }

    private static CommentFrame decodeCommentFrame(ParsableByteArray parsableByteArray, int i) throws UnsupportedEncodingException {
        if (i < 4) {
            return null;
        }
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        String charsetName = getCharsetName(readUnsignedByte);
        byte[] bArr = new byte[3];
        parsableByteArray.readBytes(bArr, 0, 3);
        String str = new String(bArr, 0, 3);
        i -= 4;
        byte[] bArr2 = new byte[i];
        parsableByteArray.readBytes(bArr2, 0, i);
        parsableByteArray = indexOfEos(bArr2, 0, readUnsignedByte);
        i = new String(bArr2, 0, parsableByteArray, charsetName);
        parsableByteArray += delimiterLength(readUnsignedByte);
        return new CommentFrame(str, i, decodeStringIfValid(bArr2, parsableByteArray, indexOfEos(bArr2, parsableByteArray, readUnsignedByte), charsetName));
    }

    private static ChapterFrame decodeChapterFrame(ParsableByteArray parsableByteArray, int i, int i2, boolean z, int i3, FramePredicate framePredicate) throws UnsupportedEncodingException {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int position = parsableByteArray2.getPosition();
        int indexOfZeroByte = indexOfZeroByte(parsableByteArray2.data, position);
        String str = new String(parsableByteArray2.data, position, indexOfZeroByte - position, CharEncoding.ISO_8859_1);
        parsableByteArray2.setPosition(indexOfZeroByte + 1);
        int readInt = parsableByteArray2.readInt();
        int readInt2 = parsableByteArray2.readInt();
        long readUnsignedInt = parsableByteArray2.readUnsignedInt();
        long j = readUnsignedInt == InternalZipConstants.ZIP_64_LIMIT ? -1 : readUnsignedInt;
        readUnsignedInt = parsableByteArray2.readUnsignedInt();
        long j2 = readUnsignedInt == InternalZipConstants.ZIP_64_LIMIT ? -1 : readUnsignedInt;
        ArrayList arrayList = new ArrayList();
        position += i;
        while (parsableByteArray2.getPosition() < position) {
            Id3Frame decodeFrame = decodeFrame(i2, parsableByteArray2, z, i3, framePredicate);
            if (decodeFrame != null) {
                arrayList.add(decodeFrame);
            }
        }
        Id3Frame[] id3FrameArr = new Id3Frame[arrayList.size()];
        arrayList.toArray(id3FrameArr);
        return new ChapterFrame(str, readInt, readInt2, j, j2, id3FrameArr);
    }

    private static ChapterTocFrame decodeChapterTOCFrame(ParsableByteArray parsableByteArray, int i, int i2, boolean z, int i3, FramePredicate framePredicate) throws UnsupportedEncodingException {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int position = parsableByteArray2.getPosition();
        int indexOfZeroByte = indexOfZeroByte(parsableByteArray2.data, position);
        String str = new String(parsableByteArray2.data, position, indexOfZeroByte - position, CharEncoding.ISO_8859_1);
        parsableByteArray2.setPosition(indexOfZeroByte + 1);
        indexOfZeroByte = parsableByteArray2.readUnsignedByte();
        boolean z2 = (indexOfZeroByte & 2) != 0;
        boolean z3 = (indexOfZeroByte & 1) != 0;
        int readUnsignedByte = parsableByteArray2.readUnsignedByte();
        String[] strArr = new String[readUnsignedByte];
        for (int i4 = 0; i4 < readUnsignedByte; i4++) {
            int position2 = parsableByteArray2.getPosition();
            int indexOfZeroByte2 = indexOfZeroByte(parsableByteArray2.data, position2);
            strArr[i4] = new String(parsableByteArray2.data, position2, indexOfZeroByte2 - position2, CharEncoding.ISO_8859_1);
            parsableByteArray2.setPosition(indexOfZeroByte2 + 1);
        }
        ArrayList arrayList = new ArrayList();
        position += i;
        while (parsableByteArray2.getPosition() < position) {
            Id3Frame decodeFrame = decodeFrame(i2, parsableByteArray2, z, i3, framePredicate);
            if (decodeFrame != null) {
                arrayList.add(decodeFrame);
            }
        }
        Id3Frame[] id3FrameArr = new Id3Frame[arrayList.size()];
        arrayList.toArray(id3FrameArr);
        return new ChapterTocFrame(str, z2, z3, strArr, id3FrameArr);
    }

    private static BinaryFrame decodeBinaryFrame(ParsableByteArray parsableByteArray, int i, String str) {
        byte[] bArr = new byte[i];
        parsableByteArray.readBytes(bArr, 0, i);
        return new BinaryFrame(str, bArr);
    }

    private static int removeUnsynchronization(ParsableByteArray parsableByteArray, int i) {
        Object obj = parsableByteArray.data;
        parsableByteArray = parsableByteArray.getPosition();
        while (true) {
            int i2 = parsableByteArray + 1;
            if (i2 >= i) {
                return i;
            }
            if ((obj[parsableByteArray] & 255) == 255 && obj[i2] == (byte) 0) {
                System.arraycopy(obj, parsableByteArray + 2, obj, i2, (i - parsableByteArray) - 2);
                i--;
            }
            parsableByteArray = i2;
        }
    }

    private static String getFrameId(int i, int i2, int i3, int i4, int i5) {
        if (i == 2) {
            return String.format(Locale.US, "%c%c%c", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)});
        }
        return String.format(Locale.US, "%c%c%c%c", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5)});
    }

    private static int indexOfEos(byte[] bArr, int i, int i2) {
        i = indexOfZeroByte(bArr, i);
        if (i2 != 0) {
            if (i2 != 3) {
                while (i < bArr.length - 1) {
                    if (i % 2 == 0 && bArr[i + 1] == 0) {
                        return i;
                    }
                    i = indexOfZeroByte(bArr, i + 1);
                }
                return bArr.length;
            }
        }
        return i;
    }

    private static int indexOfZeroByte(byte[] bArr, int i) {
        while (i < bArr.length) {
            if (bArr[i] == (byte) 0) {
                return i;
            }
            i++;
        }
        return bArr.length;
    }

    private static byte[] copyOfRangeIfValid(byte[] bArr, int i, int i2) {
        if (i2 <= i) {
            return new byte[null];
        }
        return Arrays.copyOfRange(bArr, i, i2);
    }

    private static String decodeStringIfValid(byte[] bArr, int i, int i2, String str) throws UnsupportedEncodingException {
        if (i2 > i) {
            if (i2 <= bArr.length) {
                return new String(bArr, i, i2 - i, str);
            }
        }
        return "";
    }
}
