package com.google.android.exoplayer2.extractor.ts;

import android.util.Log;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import java.util.Collections;

public final class H265Reader implements ElementaryStreamReader {
    private static final int BLA_W_LP = 16;
    private static final int CRA_NUT = 21;
    private static final int PPS_NUT = 34;
    private static final int PREFIX_SEI_NUT = 39;
    private static final int RASL_R = 9;
    private static final int SPS_NUT = 33;
    private static final int SUFFIX_SEI_NUT = 40;
    private static final String TAG = "H265Reader";
    private static final int VPS_NUT = 32;
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps = new NalUnitTargetBuffer(34, 128);
    private final boolean[] prefixFlags = new boolean[3];
    private final NalUnitTargetBuffer prefixSei = new NalUnitTargetBuffer(39, 128);
    private SampleReader sampleReader;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper = new ParsableByteArray();
    private final NalUnitTargetBuffer sps = new NalUnitTargetBuffer(33, 128);
    private final NalUnitTargetBuffer suffixSei = new NalUnitTargetBuffer(40, 128);
    private long totalBytesWritten;
    private final NalUnitTargetBuffer vps = new NalUnitTargetBuffer(32, 128);

    private static final class SampleReader {
        private static final int FIRST_SLICE_FLAG_OFFSET = 2;
        private boolean isFirstParameterSet;
        private boolean isFirstSlice;
        private boolean lookingForFirstSliceFlag;
        private int nalUnitBytesRead;
        private boolean nalUnitHasKeyframeData;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private final TrackOutput output;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private boolean writingParameterSets;

        public SampleReader(TrackOutput trackOutput) {
            this.output = trackOutput;
        }

        public void reset() {
            this.lookingForFirstSliceFlag = false;
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.readingSample = false;
            this.writingParameterSets = false;
        }

        public void startNalUnit(long j, int i, int i2, long j2) {
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.nalUnitTimeUs = j2;
            this.nalUnitBytesRead = 0;
            this.nalUnitStartPosition = j;
            j = 1;
            if (i2 >= 32) {
                if (!this.writingParameterSets && this.readingSample) {
                    outputSample(i);
                    this.readingSample = false;
                }
                if (i2 <= 34) {
                    this.isFirstParameterSet = this.writingParameterSets ^ true;
                    this.writingParameterSets = true;
                }
            }
            boolean z = i2 >= 16 && i2 <= 21;
            this.nalUnitHasKeyframeData = z;
            if (!this.nalUnitHasKeyframeData) {
                if (i2 > 9) {
                    j = null;
                }
            }
            this.lookingForFirstSliceFlag = j;
        }

        public void readNalUnitData(byte[] bArr, int i, int i2) {
            if (this.lookingForFirstSliceFlag) {
                int i3 = (i + 2) - this.nalUnitBytesRead;
                if (i3 < i2) {
                    this.isFirstSlice = (bArr[i3] & 128) != null ? 1 : null;
                    this.lookingForFirstSliceFlag = false;
                    return;
                }
                this.nalUnitBytesRead += i2 - i;
            }
        }

        public void endNalUnit(long j, int i) {
            if (this.writingParameterSets && this.isFirstSlice) {
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
                this.writingParameterSets = 0;
            } else if (this.isFirstParameterSet || this.isFirstSlice) {
                if (this.readingSample) {
                    outputSample(i + ((int) (j - this.nalUnitStartPosition)));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.readingSample = 1;
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            }
        }

        private void outputSample(int i) {
            this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe, (int) (this.nalUnitStartPosition - this.samplePosition), i, null);
        }
    }

    public void packetFinished() {
    }

    public H265Reader(SeiReader seiReader) {
        this.seiReader = seiReader;
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.vps.reset();
        this.sps.reset();
        this.pps.reset();
        this.prefixSei.reset();
        this.suffixSei.reset();
        this.sampleReader.reset();
        this.totalBytesWritten = 0;
    }

    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output);
        this.seiReader.createTracks(extractorOutput, trackIdGenerator);
    }

    public void packetStarted(long j, boolean z) {
        this.pesTimeUs = j;
    }

    public void consume(ParsableByteArray parsableByteArray) {
        H265Reader h265Reader = this;
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        while (parsableByteArray.bytesLeft() > 0) {
            int position = parsableByteArray.getPosition();
            int limit = parsableByteArray.limit();
            byte[] bArr = parsableByteArray2.data;
            h265Reader.totalBytesWritten += (long) parsableByteArray.bytesLeft();
            h265Reader.output.sampleData(parsableByteArray2, parsableByteArray.bytesLeft());
            while (position < limit) {
                int findNalUnit = NalUnitUtil.findNalUnit(bArr, position, limit, h265Reader.prefixFlags);
                if (findNalUnit == limit) {
                    nalUnitData(bArr, position, limit);
                    return;
                }
                int h265NalUnitType = NalUnitUtil.getH265NalUnitType(bArr, findNalUnit);
                int i = findNalUnit - position;
                if (i > 0) {
                    nalUnitData(bArr, position, findNalUnit);
                }
                int i2 = limit - findNalUnit;
                long j = h265Reader.totalBytesWritten - ((long) i2);
                int i3 = i < 0 ? -i : 0;
                long j2 = j;
                int i4 = i2;
                endNalUnit(j2, i4, i3, h265Reader.pesTimeUs);
                startNalUnit(j2, i4, h265NalUnitType, h265Reader.pesTimeUs);
                position = findNalUnit + 3;
            }
        }
    }

    private void startNalUnit(long j, int i, int i2, long j2) {
        if (this.hasOutputFormat) {
            this.sampleReader.startNalUnit(j, i, i2, j2);
        } else {
            this.vps.startNalUnit(i2);
            this.sps.startNalUnit(i2);
            this.pps.startNalUnit(i2);
        }
        this.prefixSei.startNalUnit(i2);
        this.suffixSei.startNalUnit(i2);
    }

    private void nalUnitData(byte[] bArr, int i, int i2) {
        if (this.hasOutputFormat) {
            this.sampleReader.readNalUnitData(bArr, i, i2);
        } else {
            this.vps.appendToNalUnit(bArr, i, i2);
            this.sps.appendToNalUnit(bArr, i, i2);
            this.pps.appendToNalUnit(bArr, i, i2);
        }
        this.prefixSei.appendToNalUnit(bArr, i, i2);
        this.suffixSei.appendToNalUnit(bArr, i, i2);
    }

    private void endNalUnit(long j, int i, int i2, long j2) {
        if (this.hasOutputFormat) {
            this.sampleReader.endNalUnit(j, i);
        } else {
            this.vps.endNalUnit(i2);
            this.sps.endNalUnit(i2);
            this.pps.endNalUnit(i2);
            if (!(this.vps.isCompleted() == null || this.sps.isCompleted() == null || this.pps.isCompleted() == null)) {
                this.output.format(parseMediaFormat(this.formatId, this.vps, this.sps, this.pps));
                this.hasOutputFormat = 1;
            }
        }
        if (this.prefixSei.endNalUnit(i2) != null) {
            this.seiWrapper.reset(this.prefixSei.nalData, NalUnitUtil.unescapeStream(this.prefixSei.nalData, this.prefixSei.nalLength));
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(j2, this.seiWrapper);
        }
        if (this.suffixSei.endNalUnit(i2) != null) {
            this.seiWrapper.reset(this.suffixSei.nalData, NalUnitUtil.unescapeStream(this.suffixSei.nalData, this.suffixSei.nalLength));
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(j2, this.seiWrapper);
        }
    }

    private static Format parseMediaFormat(String str, NalUnitTargetBuffer nalUnitTargetBuffer, NalUnitTargetBuffer nalUnitTargetBuffer2, NalUnitTargetBuffer nalUnitTargetBuffer3) {
        float f;
        NalUnitTargetBuffer nalUnitTargetBuffer4 = nalUnitTargetBuffer;
        NalUnitTargetBuffer nalUnitTargetBuffer5 = nalUnitTargetBuffer2;
        NalUnitTargetBuffer nalUnitTargetBuffer6 = nalUnitTargetBuffer3;
        Object obj = new byte[((nalUnitTargetBuffer4.nalLength + nalUnitTargetBuffer5.nalLength) + nalUnitTargetBuffer6.nalLength)];
        int i = 0;
        System.arraycopy(nalUnitTargetBuffer4.nalData, 0, obj, 0, nalUnitTargetBuffer4.nalLength);
        System.arraycopy(nalUnitTargetBuffer5.nalData, 0, obj, nalUnitTargetBuffer4.nalLength, nalUnitTargetBuffer5.nalLength);
        System.arraycopy(nalUnitTargetBuffer6.nalData, 0, obj, nalUnitTargetBuffer4.nalLength + nalUnitTargetBuffer5.nalLength, nalUnitTargetBuffer6.nalLength);
        ParsableNalUnitBitArray parsableNalUnitBitArray = new ParsableNalUnitBitArray(nalUnitTargetBuffer5.nalData, 0, nalUnitTargetBuffer5.nalLength);
        parsableNalUnitBitArray.skipBits(44);
        int readBits = parsableNalUnitBitArray.readBits(3);
        parsableNalUnitBitArray.skipBit();
        parsableNalUnitBitArray.skipBits(88);
        parsableNalUnitBitArray.skipBits(8);
        int i2 = 0;
        for (int i3 = 0; i3 < readBits; i3++) {
            if (parsableNalUnitBitArray.readBit()) {
                i2 += 89;
            }
            if (parsableNalUnitBitArray.readBit()) {
                i2 += 8;
            }
        }
        parsableNalUnitBitArray.skipBits(i2);
        if (readBits > 0) {
            parsableNalUnitBitArray.skipBits((8 - readBits) * 2);
        }
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        i2 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        if (i2 == 3) {
            parsableNalUnitBitArray.skipBit();
        }
        int readUnsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        int readUnsignedExpGolombCodedInt2 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        if (parsableNalUnitBitArray.readBit()) {
            int i4;
            int readUnsignedExpGolombCodedInt3 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            int readUnsignedExpGolombCodedInt4 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            int readUnsignedExpGolombCodedInt5 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            int readUnsignedExpGolombCodedInt6 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            if (i2 != 1) {
                if (i2 != 2) {
                    i4 = 1;
                    readUnsignedExpGolombCodedInt -= i4 * (readUnsignedExpGolombCodedInt3 + readUnsignedExpGolombCodedInt4);
                    readUnsignedExpGolombCodedInt2 -= (i2 != 1 ? 2 : 1) * (readUnsignedExpGolombCodedInt5 + readUnsignedExpGolombCodedInt6);
                }
            }
            i4 = 2;
            if (i2 != 1) {
            }
            readUnsignedExpGolombCodedInt -= i4 * (readUnsignedExpGolombCodedInt3 + readUnsignedExpGolombCodedInt4);
            readUnsignedExpGolombCodedInt2 -= (i2 != 1 ? 2 : 1) * (readUnsignedExpGolombCodedInt5 + readUnsignedExpGolombCodedInt6);
        }
        int i5 = readUnsignedExpGolombCodedInt;
        int i6 = readUnsignedExpGolombCodedInt2;
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        readUnsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        i2 = parsableNalUnitBitArray.readBit() ? 0 : readBits;
        while (i2 <= readBits) {
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            i2++;
        }
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        if (parsableNalUnitBitArray.readBit() && parsableNalUnitBitArray.readBit()) {
            skipScalingList(parsableNalUnitBitArray);
        }
        parsableNalUnitBitArray.skipBits(2);
        if (parsableNalUnitBitArray.readBit()) {
            parsableNalUnitBitArray.skipBits(8);
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
            parsableNalUnitBitArray.skipBit();
        }
        skipShortTermRefPicSets(parsableNalUnitBitArray);
        if (parsableNalUnitBitArray.readBit()) {
            while (i < parsableNalUnitBitArray.readUnsignedExpGolombCodedInt()) {
                parsableNalUnitBitArray.skipBits((readUnsignedExpGolombCodedInt + 4) + 1);
                i++;
            }
        }
        parsableNalUnitBitArray.skipBits(2);
        float f2 = 1.0f;
        if (parsableNalUnitBitArray.readBit() && parsableNalUnitBitArray.readBit()) {
            readBits = parsableNalUnitBitArray.readBits(8);
            if (readBits == 255) {
                int readBits2 = parsableNalUnitBitArray.readBits(16);
                int readBits3 = parsableNalUnitBitArray.readBits(16);
                if (!(readBits2 == 0 || readBits3 == 0)) {
                    f2 = ((float) readBits2) / ((float) readBits3);
                }
                f = f2;
            } else if (readBits < NalUnitUtil.ASPECT_RATIO_IDC_VALUES.length) {
                f = NalUnitUtil.ASPECT_RATIO_IDC_VALUES[readBits];
            } else {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected aspect_ratio_idc value: ");
                stringBuilder.append(readBits);
                Log.w(str2, stringBuilder.toString());
            }
            return Format.createVideoSampleFormat(str, MimeTypes.VIDEO_H265, null, -1, -1, i5, i6, -1.0f, Collections.singletonList(obj), -1, f, null);
        }
        f = 1.0f;
        return Format.createVideoSampleFormat(str, MimeTypes.VIDEO_H265, null, -1, -1, i5, i6, -1.0f, Collections.singletonList(obj), -1, f, null);
    }

    private static void skipScalingList(ParsableNalUnitBitArray parsableNalUnitBitArray) {
        for (int i = 0; i < 4; i++) {
            int i2 = 0;
            while (i2 < 6) {
                int min;
                if (parsableNalUnitBitArray.readBit()) {
                    min = Math.min(64, 1 << ((i << 1) + 4));
                    if (i > 1) {
                        parsableNalUnitBitArray.readSignedExpGolombCodedInt();
                    }
                    for (int i3 = 0; i3 < min; i3++) {
                        parsableNalUnitBitArray.readSignedExpGolombCodedInt();
                    }
                } else {
                    parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                }
                min = 3;
                if (i != 3) {
                    min = 1;
                }
                i2 += min;
            }
        }
    }

    private static void skipShortTermRefPicSets(ParsableNalUnitBitArray parsableNalUnitBitArray) {
        int readUnsignedExpGolombCodedInt = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < readUnsignedExpGolombCodedInt; i2++) {
            if (i2 != 0) {
                z = parsableNalUnitBitArray.readBit();
            }
            int i3;
            if (z) {
                parsableNalUnitBitArray.skipBit();
                parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                for (i3 = 0; i3 <= i; i3++) {
                    if (parsableNalUnitBitArray.readBit()) {
                        parsableNalUnitBitArray.skipBit();
                    }
                }
            } else {
                i = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                i3 = parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                int i4 = i + i3;
                for (int i5 = 0; i5 < i; i5++) {
                    parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                    parsableNalUnitBitArray.skipBit();
                }
                for (i = 0; i < i3; i++) {
                    parsableNalUnitBitArray.readUnsignedExpGolombCodedInt();
                    parsableNalUnitBitArray.skipBit();
                }
                i = i4;
            }
        }
    }
}
