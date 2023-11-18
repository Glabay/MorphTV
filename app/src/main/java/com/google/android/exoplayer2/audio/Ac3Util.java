package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;

public final class Ac3Util {
    private static final int AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT = 1536;
    private static final int AUDIO_SAMPLES_PER_AUDIO_BLOCK = 256;
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD = new int[]{32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384, 448, 512, 576, 640};
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = new int[]{1, 2, 3, 6};
    private static final int[] CHANNEL_COUNT_BY_ACMOD = new int[]{2, 1, 2, 3, 3, 4, 4, 5};
    private static final int[] SAMPLE_RATE_BY_FSCOD = new int[]{48000, 44100, 32000};
    private static final int[] SAMPLE_RATE_BY_FSCOD2 = new int[]{24000, 22050, 16000};
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = new int[]{69, 87, 104, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};
    public static final int TRUEHD_RECHUNK_SAMPLE_COUNT = 8;
    public static final int TRUEHD_SYNCFRAME_PREFIX_LENGTH = 12;

    public static final class Ac3SyncFrameInfo {
        public static final int STREAM_TYPE_TYPE0 = 0;
        public static final int STREAM_TYPE_TYPE1 = 1;
        public static final int STREAM_TYPE_TYPE2 = 2;
        public static final int STREAM_TYPE_UNDEFINED = -1;
        public final int channelCount;
        public final int frameSize;
        public final String mimeType;
        public final int sampleCount;
        public final int sampleRate;
        public final int streamType;

        private Ac3SyncFrameInfo(String str, int i, int i2, int i3, int i4, int i5) {
            this.mimeType = str;
            this.streamType = i;
            this.channelCount = i2;
            this.sampleRate = i3;
            this.frameSize = i4;
            this.sampleCount = i5;
        }
    }

    public static int getAc3SyncframeAudioSampleCount() {
        return AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
    }

    public static Format parseAc3AnnexFFormat(ParsableByteArray parsableByteArray, String str, String str2, DrmInitData drmInitData) {
        int i = SAMPLE_RATE_BY_FSCOD[(parsableByteArray.readUnsignedByte() & 192) >> 6];
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int i2 = CHANNEL_COUNT_BY_ACMOD[(readUnsignedByte & 56) >> 3];
        if ((readUnsignedByte & 4) != 0) {
            i2++;
        }
        return Format.createAudioSampleFormat(str, MimeTypes.AUDIO_AC3, null, -1, -1, i2, i, null, drmInitData, 0, str2);
    }

    public static Format parseEAc3AnnexFFormat(ParsableByteArray parsableByteArray, String str, String str2, DrmInitData drmInitData) {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        parsableByteArray2.skipBytes(2);
        int i = SAMPLE_RATE_BY_FSCOD[(parsableByteArray2.readUnsignedByte() & 192) >> 6];
        int readUnsignedByte = parsableByteArray2.readUnsignedByte();
        int i2 = CHANNEL_COUNT_BY_ACMOD[(readUnsignedByte & 14) >> 1];
        if ((readUnsignedByte & 1) != 0) {
            i2++;
        }
        if (((parsableByteArray2.readUnsignedByte() & 30) >> 1) > 0 && (2 & parsableByteArray2.readUnsignedByte()) != 0) {
            i2 += 2;
        }
        int i3 = i2;
        String str3 = MimeTypes.AUDIO_E_AC3;
        if (parsableByteArray2.bytesLeft() > 0 && (parsableByteArray2.readUnsignedByte() & 1) != 0) {
            str3 = MimeTypes.AUDIO_E_AC3_JOC;
        }
        return Format.createAudioSampleFormat(str, str3, null, -1, -1, i3, i, null, drmInitData, 0, str2);
    }

    public static Ac3SyncFrameInfo parseAc3SyncframeInfo(ParsableBitArray parsableBitArray) {
        int readBits;
        int i;
        String str;
        int i2;
        int i3;
        int i4;
        ParsableBitArray parsableBitArray2 = parsableBitArray;
        int position = parsableBitArray.getPosition();
        parsableBitArray2.skipBits(40);
        Object obj = parsableBitArray2.readBits(5) == 16 ? 1 : null;
        parsableBitArray2.setPosition(position);
        int readBits2;
        int readBits3;
        int readBits4;
        String str2;
        if (obj != null) {
            int i5;
            int i6;
            int i7;
            parsableBitArray2.skipBits(16);
            position = parsableBitArray2.readBits(2);
            parsableBitArray2.skipBits(3);
            readBits2 = (parsableBitArray2.readBits(11) + 1) * 2;
            readBits = parsableBitArray2.readBits(2);
            if (readBits == 3) {
                i5 = SAMPLE_RATE_BY_FSCOD2[parsableBitArray2.readBits(2)];
                i6 = 3;
                i7 = 6;
            } else {
                i6 = parsableBitArray2.readBits(2);
                i7 = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[i6];
                i5 = SAMPLE_RATE_BY_FSCOD[readBits];
            }
            int i8 = i7 * 256;
            readBits3 = parsableBitArray2.readBits(3);
            boolean readBit = parsableBitArray.readBit();
            int i9 = CHANNEL_COUNT_BY_ACMOD[readBits3] + readBit;
            parsableBitArray2.skipBits(10);
            if (parsableBitArray.readBit()) {
                parsableBitArray2.skipBits(8);
            }
            if (readBits3 == 0) {
                parsableBitArray2.skipBits(5);
                if (parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(8);
                }
            }
            if (position == 1 && parsableBitArray.readBit()) {
                parsableBitArray2.skipBits(16);
            }
            if (parsableBitArray.readBit()) {
                if (readBits3 > 2) {
                    parsableBitArray2.skipBits(2);
                }
                if ((readBits3 & 1) != 0 && readBits3 > 2) {
                    parsableBitArray2.skipBits(6);
                }
                if ((readBits3 & 4) != 0) {
                    parsableBitArray2.skipBits(6);
                }
                if (readBit && parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(5);
                }
                if (position == 0) {
                    if (parsableBitArray.readBit()) {
                        parsableBitArray2.skipBits(6);
                    }
                    if (readBits3 == 0 && parsableBitArray.readBit()) {
                        parsableBitArray2.skipBits(6);
                    }
                    if (parsableBitArray.readBit()) {
                        parsableBitArray2.skipBits(6);
                    }
                    readBits4 = parsableBitArray2.readBits(2);
                    if (readBits4 == 1) {
                        parsableBitArray2.skipBits(5);
                    } else if (readBits4 == 2) {
                        parsableBitArray2.skipBits(12);
                    } else if (readBits4 == 3) {
                        readBits4 = parsableBitArray2.readBits(5);
                        if (parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(5);
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(4);
                            }
                            if (parsableBitArray.readBit()) {
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(4);
                                }
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(4);
                                }
                            }
                        }
                        if (parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(5);
                            if (parsableBitArray.readBit()) {
                                parsableBitArray2.skipBits(7);
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(8);
                                }
                            }
                        }
                        parsableBitArray2.skipBits((readBits4 + 2) * 8);
                        parsableBitArray.byteAlign();
                    }
                    if (readBits3 < 2) {
                        if (parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(14);
                        }
                        if (readBits3 == 0 && parsableBitArray.readBit()) {
                            parsableBitArray2.skipBits(14);
                        }
                    }
                    if (parsableBitArray.readBit()) {
                        if (i6 == 0) {
                            parsableBitArray2.skipBits(5);
                        } else {
                            for (readBits4 = 0; readBits4 < i7; readBits4++) {
                                if (parsableBitArray.readBit()) {
                                    parsableBitArray2.skipBits(5);
                                }
                            }
                        }
                    }
                }
            }
            int i10;
            if (parsableBitArray.readBit()) {
                parsableBitArray2.skipBits(5);
                if (readBits3 == 2) {
                    parsableBitArray2.skipBits(4);
                }
                if (readBits3 >= 6) {
                    parsableBitArray2.skipBits(2);
                }
                if (parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(8);
                }
                if (readBits3 == 0 && parsableBitArray.readBit()) {
                    parsableBitArray2.skipBits(8);
                }
                i10 = 3;
                if (readBits < 3) {
                    parsableBitArray.skipBit();
                }
            } else {
                i10 = 3;
            }
            if (position == 0 && i6 != r2) {
                parsableBitArray.skipBit();
            }
            if (position == 2 && (i6 == r2 || parsableBitArray.readBit())) {
                parsableBitArray2.skipBits(6);
            }
            str2 = MimeTypes.AUDIO_E_AC3;
            if (parsableBitArray.readBit() && parsableBitArray2.readBits(6) == 1 && parsableBitArray2.readBits(8) == 1) {
                str2 = MimeTypes.AUDIO_E_AC3_JOC;
            }
            i = position;
            str = str2;
            i2 = readBits2;
            i3 = i5;
            readBits = i8;
            i4 = i9;
        } else {
            str2 = MimeTypes.AUDIO_AC3;
            parsableBitArray2.skipBits(32);
            readBits2 = parsableBitArray2.readBits(2);
            readBits3 = getAc3SyncframeSize(readBits2, parsableBitArray2.readBits(6));
            parsableBitArray2.skipBits(8);
            readBits4 = parsableBitArray2.readBits(3);
            if (!((readBits4 & 1) == 0 || readBits4 == 1)) {
                parsableBitArray2.skipBits(2);
            }
            if ((readBits4 & 4) != 0) {
                parsableBitArray2.skipBits(2);
            }
            if (readBits4 == 2) {
                parsableBitArray2.skipBits(2);
            }
            str = str2;
            i2 = readBits3;
            i3 = SAMPLE_RATE_BY_FSCOD[readBits2];
            i4 = CHANNEL_COUNT_BY_ACMOD[readBits4] + parsableBitArray.readBit();
            i = -1;
            readBits = AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
        }
        return new Ac3SyncFrameInfo(str, i, i4, i3, i2, readBits);
    }

    public static int parseAc3SyncframeSize(byte[] bArr) {
        if (bArr.length < 5) {
            return -1;
        }
        return getAc3SyncframeSize((bArr[4] & 192) >> 6, bArr[4] & 63);
    }

    public static int parseEAc3SyncframeAudioSampleCount(ByteBuffer byteBuffer) {
        int i = 6;
        if (((byteBuffer.get(byteBuffer.position() + 4) & 192) >> 6) != 3) {
            i = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(byteBuffer.get(byteBuffer.position() + 4) & 48) >> 4];
        }
        return i * 256;
    }

    public static int parseTrueHdSyncframeAudioSampleCount(byte[] bArr) {
        if (bArr[4] == (byte) -8 && bArr[5] == (byte) 114 && bArr[6] == (byte) 111) {
            if (bArr[7] == (byte) -70) {
                return 40 << (bArr[8] & 7);
            }
        }
        return null;
    }

    public static int parseTrueHdSyncframeAudioSampleCount(ByteBuffer byteBuffer) {
        if (byteBuffer.getInt(byteBuffer.position() + 4) != -1167101192) {
            return null;
        }
        return 40 << (byteBuffer.get(byteBuffer.position() + 8) & 7);
    }

    private static int getAc3SyncframeSize(int i, int i2) {
        int i3 = i2 / 2;
        if (i >= 0 && i < SAMPLE_RATE_BY_FSCOD.length && i2 >= 0) {
            if (i3 < SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1.length) {
                i = SAMPLE_RATE_BY_FSCOD[i];
                if (i == 44100) {
                    return (SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1[i3] + (i2 % 2)) * 2;
                }
                i2 = BITRATE_BY_HALF_FRMSIZECOD[i3];
                return i == 32000 ? i2 * 6 : i2 * 4;
            }
        }
        return -1;
    }

    private Ac3Util() {
    }
}
