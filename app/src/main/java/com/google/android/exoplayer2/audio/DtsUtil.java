package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableBitArray;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class DtsUtil {
    private static final int[] CHANNELS_BY_AMODE = new int[]{1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8};
    private static final byte FIRST_BYTE_14B_BE = (byte) 31;
    private static final byte FIRST_BYTE_14B_LE = (byte) -1;
    private static final byte FIRST_BYTE_BE = Byte.MAX_VALUE;
    private static final byte FIRST_BYTE_LE = (byte) -2;
    private static final int[] SAMPLE_RATE_BY_SFREQ = new int[]{-1, 8000, 16000, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, 48000, -1, -1};
    private static final int SYNC_VALUE_14B_BE = 536864768;
    private static final int SYNC_VALUE_14B_LE = -14745368;
    private static final int SYNC_VALUE_BE = 2147385345;
    private static final int SYNC_VALUE_LE = -25230976;
    private static final int[] TWICE_BITRATE_KBPS_BY_RATE = new int[]{64, 112, 128, 192, 224, 256, 384, 448, 512, 640, 768, MediaRouterJellybean.DEVICE_OUT_BLUETOOTH, 1024, 1152, 1280, 1536, 1920, 2048, 2304, 2560, 2688, 2816, 2823, 2944, 3072, 3840, 4096, 6144, 7680};

    public static boolean isSyncWord(int i) {
        if (!(i == SYNC_VALUE_BE || i == SYNC_VALUE_LE || i == SYNC_VALUE_14B_BE)) {
            if (i != SYNC_VALUE_14B_LE) {
                return false;
            }
        }
        return true;
    }

    public static Format parseDtsFormat(byte[] bArr, String str, String str2, DrmInitData drmInitData) {
        ParsableBitArray normalizedFrameHeader = getNormalizedFrameHeader(bArr);
        normalizedFrameHeader.skipBits(60);
        int i = CHANNELS_BY_AMODE[normalizedFrameHeader.readBits(6)];
        int i2 = SAMPLE_RATE_BY_SFREQ[normalizedFrameHeader.readBits(4)];
        int readBits = normalizedFrameHeader.readBits(5);
        int i3 = readBits >= TWICE_BITRATE_KBPS_BY_RATE.length ? -1 : (TWICE_BITRATE_KBPS_BY_RATE[readBits] * 1000) / 2;
        normalizedFrameHeader.skipBits(10);
        return Format.createAudioSampleFormat(str, MimeTypes.AUDIO_DTS, null, i3, -1, i + (normalizedFrameHeader.readBits(2) > 0 ? 1 : 0), i2, null, drmInitData, 0, str2);
    }

    public static int parseDtsAudioSampleCount(byte[] bArr) {
        byte b = bArr[0];
        if (b != (byte) 31) {
            switch (b) {
                case (byte) -2:
                    bArr = ((bArr[4] & 252) >> 2) | ((bArr[5] & 1) << 6);
                    break;
                case (byte) -1:
                    bArr = ((bArr[7] & 60) >> 2) | ((bArr[4] & 7) << 4);
                    break;
                default:
                    bArr = ((bArr[5] & 252) >> 2) | ((bArr[4] & 1) << 6);
                    break;
            }
        }
        bArr = ((bArr[6] & 60) >> 2) | ((bArr[5] & 7) << 4);
        return (bArr + 1) * 32;
    }

    public static int parseDtsAudioSampleCount(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        byte b = byteBuffer.get(position);
        if (b != (byte) 31) {
            switch (b) {
                case (byte) -2:
                    byteBuffer = ((byteBuffer.get(position + 4) & 252) >> 2) | ((byteBuffer.get(position + 5) & 1) << 6);
                    break;
                case (byte) -1:
                    byteBuffer = ((byteBuffer.get(position + 7) & 60) >> 2) | ((byteBuffer.get(position + 4) & 7) << 4);
                    break;
                default:
                    byteBuffer = ((byteBuffer.get(position + 5) & 252) >> 2) | ((byteBuffer.get(position + 4) & 1) << 6);
                    break;
            }
        }
        byteBuffer = ((byteBuffer.get(position + 6) & 60) >> 2) | ((byteBuffer.get(position + 5) & 7) << 4);
        return (byteBuffer + 1) * 32;
    }

    public static int getDtsFrameSize(byte[] bArr) {
        int i = 0;
        byte b = bArr[0];
        if (b != (byte) 31) {
            switch (b) {
                case (byte) -2:
                    bArr = (((bArr[6] & 240) >> 4) | (((bArr[4] & 3) << 12) | ((bArr[7] & 255) << 4))) + 1;
                    break;
                case (byte) -1:
                    bArr = (((bArr[9] & 60) >> 2) | (((bArr[7] & 3) << 12) | ((bArr[6] & 255) << 4))) + 1;
                    break;
                default:
                    bArr = (((bArr[7] & 240) >> 4) | (((bArr[5] & 3) << 12) | ((bArr[6] & 255) << 4))) + 1;
                    break;
            }
        }
        bArr = (((bArr[8] & 60) >> 2) | (((bArr[6] & 3) << 12) | ((bArr[7] & 255) << 4))) + 1;
        i = 1;
        return i != 0 ? (bArr * 16) / 14 : bArr;
    }

    private static ParsableBitArray getNormalizedFrameHeader(byte[] bArr) {
        if (bArr[0] == Byte.MAX_VALUE) {
            return new ParsableBitArray(bArr);
        }
        bArr = Arrays.copyOf(bArr, bArr.length);
        if (isLittleEndianFrameHeader(bArr)) {
            for (int i = 0; i < bArr.length - 1; i += 2) {
                byte b = bArr[i];
                int i2 = i + 1;
                bArr[i] = bArr[i2];
                bArr[i2] = b;
            }
        }
        ParsableBitArray parsableBitArray = new ParsableBitArray(bArr);
        if (bArr[0] == (byte) 31) {
            ParsableBitArray parsableBitArray2 = new ParsableBitArray(bArr);
            while (parsableBitArray2.bitsLeft() >= 16) {
                parsableBitArray2.skipBits(2);
                parsableBitArray.putInt(parsableBitArray2.readBits(14), 14);
            }
        }
        parsableBitArray.reset(bArr);
        return parsableBitArray;
    }

    private static boolean isLittleEndianFrameHeader(byte[] bArr) {
        return bArr[0] == FIRST_BYTE_LE || bArr[0] == -1;
    }

    private DtsUtil() {
    }
}
