package com.google.android.exoplayer2.extractor.mp4;

import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.mp4.FixedSampleSizeRechunker.Results;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.HevcConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class AtomParsers {
    private static final String TAG = "AtomParsers";
    private static final int TYPE_clcp = Util.getIntegerCodeForString("clcp");
    private static final int TYPE_meta = Util.getIntegerCodeForString("meta");
    private static final int TYPE_sbtl = Util.getIntegerCodeForString("sbtl");
    private static final int TYPE_soun = Util.getIntegerCodeForString("soun");
    private static final int TYPE_subt = Util.getIntegerCodeForString("subt");
    private static final int TYPE_text = Util.getIntegerCodeForString(MimeTypes.BASE_TYPE_TEXT);
    private static final int TYPE_vide = Util.getIntegerCodeForString("vide");

    private static final class ChunkIterator {
        private final ParsableByteArray chunkOffsets;
        private final boolean chunkOffsetsAreLongs;
        public int index;
        public final int length;
        private int nextSamplesPerChunkChangeIndex;
        public int numSamples;
        public long offset;
        private int remainingSamplesPerChunkChanges;
        private final ParsableByteArray stsc;

        public ChunkIterator(ParsableByteArray parsableByteArray, ParsableByteArray parsableByteArray2, boolean z) {
            this.stsc = parsableByteArray;
            this.chunkOffsets = parsableByteArray2;
            this.chunkOffsetsAreLongs = z;
            parsableByteArray2.setPosition(12);
            this.length = parsableByteArray2.readUnsignedIntToInt();
            parsableByteArray.setPosition(12);
            this.remainingSamplesPerChunkChanges = parsableByteArray.readUnsignedIntToInt();
            parsableByteArray2 = true;
            if (parsableByteArray.readInt() != 1) {
                parsableByteArray2 = null;
            }
            Assertions.checkState(parsableByteArray2, "first_chunk must be 1");
            this.index = -1;
        }

        public boolean moveNext() {
            int i = this.index + 1;
            this.index = i;
            if (i == this.length) {
                return false;
            }
            long readUnsignedLongToLong;
            if (this.chunkOffsetsAreLongs) {
                readUnsignedLongToLong = this.chunkOffsets.readUnsignedLongToLong();
            } else {
                readUnsignedLongToLong = this.chunkOffsets.readUnsignedInt();
            }
            this.offset = readUnsignedLongToLong;
            if (this.index == this.nextSamplesPerChunkChangeIndex) {
                this.numSamples = this.stsc.readUnsignedIntToInt();
                this.stsc.skipBytes(4);
                i = this.remainingSamplesPerChunkChanges - 1;
                this.remainingSamplesPerChunkChanges = i;
                this.nextSamplesPerChunkChangeIndex = i > 0 ? this.stsc.readUnsignedIntToInt() - 1 : -1;
            }
            return true;
        }
    }

    private interface SampleSizeBox {
        int getSampleCount();

        boolean isFixedSampleSize();

        int readNextSampleSize();
    }

    private static final class StsdData {
        public static final int STSD_HEADER_SIZE = 8;
        public Format format;
        public int nalUnitLengthFieldLength;
        public int requiredSampleTransformation = 0;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int i) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[i];
        }
    }

    static final class StszSampleSizeBox implements SampleSizeBox {
        private final ParsableByteArray data;
        private final int fixedSampleSize = this.data.readUnsignedIntToInt();
        private final int sampleCount = this.data.readUnsignedIntToInt();

        public StszSampleSizeBox(LeafAtom leafAtom) {
            this.data = leafAtom.data;
            this.data.setPosition(12);
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public int readNextSampleSize() {
            return this.fixedSampleSize == 0 ? this.data.readUnsignedIntToInt() : this.fixedSampleSize;
        }

        public boolean isFixedSampleSize() {
            return this.fixedSampleSize != 0;
        }
    }

    static final class Stz2SampleSizeBox implements SampleSizeBox {
        private int currentByte;
        private final ParsableByteArray data;
        private final int fieldSize = (this.data.readUnsignedIntToInt() & 255);
        private final int sampleCount = this.data.readUnsignedIntToInt();
        private int sampleIndex;

        public boolean isFixedSampleSize() {
            return false;
        }

        public Stz2SampleSizeBox(LeafAtom leafAtom) {
            this.data = leafAtom.data;
            this.data.setPosition(12);
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public int readNextSampleSize() {
            if (this.fieldSize == 8) {
                return this.data.readUnsignedByte();
            }
            if (this.fieldSize == 16) {
                return this.data.readUnsignedShort();
            }
            int i = this.sampleIndex;
            this.sampleIndex = i + 1;
            if (i % 2 != 0) {
                return this.currentByte & 15;
            }
            this.currentByte = this.data.readUnsignedByte();
            return (this.currentByte & 240) >> 4;
        }
    }

    private static final class TkhdData {
        private final long duration;
        private final int id;
        private final int rotationDegrees;

        public TkhdData(int i, long j, int i2) {
            this.id = i;
            this.duration = j;
            this.rotationDegrees = i2;
        }
    }

    public static Track parseTrak(ContainerAtom containerAtom, LeafAtom leafAtom, long j, DrmInitData drmInitData, boolean z, boolean z2) throws ParserException {
        ContainerAtom containerAtom2 = containerAtom;
        ContainerAtom containerAtomOfType = containerAtom2.getContainerAtomOfType(Atom.TYPE_mdia);
        int parseHdlr = parseHdlr(containerAtomOfType.getLeafAtomOfType(Atom.TYPE_hdlr).data);
        if (parseHdlr == -1) {
            return null;
        }
        long access$000;
        LeafAtom leafAtom2;
        long[] jArr;
        long[] jArr2;
        Track track;
        TkhdData parseTkhd = parseTkhd(containerAtom2.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        long j2 = C0649C.TIME_UNSET;
        if (j == C0649C.TIME_UNSET) {
            access$000 = parseTkhd.duration;
            leafAtom2 = leafAtom;
        } else {
            leafAtom2 = leafAtom;
            access$000 = j;
        }
        long parseMvhd = parseMvhd(leafAtom2.data);
        if (access$000 != C0649C.TIME_UNSET) {
            j2 = Util.scaleLargeTimestamp(access$000, C0649C.MICROS_PER_SECOND, parseMvhd);
        }
        long j3 = j2;
        ContainerAtom containerAtomOfType2 = containerAtomOfType.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
        Pair parseMdhd = parseMdhd(containerAtomOfType.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        StsdData parseStsd = parseStsd(containerAtomOfType2.getLeafAtomOfType(Atom.TYPE_stsd).data, parseTkhd.id, parseTkhd.rotationDegrees, (String) parseMdhd.second, drmInitData, z2);
        if (z) {
            jArr = null;
            jArr2 = jArr;
        } else {
            Pair parseEdts = parseEdts(containerAtom2.getContainerAtomOfType(Atom.TYPE_edts));
            jArr2 = (long[]) parseEdts.second;
            jArr = (long[]) parseEdts.first;
        }
        if (parseStsd.format == null) {
            track = null;
        } else {
            int access$100 = parseTkhd.id;
            j2 = ((Long) parseMdhd.first).longValue();
            Format format = parseStsd.format;
            int i = parseStsd.requiredSampleTransformation;
            TrackEncryptionBox[] trackEncryptionBoxArr = parseStsd.trackEncryptionBoxes;
            int i2 = parseStsd.nalUnitLengthFieldLength;
            Track track2 = new Track(access$100, parseHdlr, j2, parseMvhd, j3, format, i, trackEncryptionBoxArr, i2, jArr, jArr2);
        }
        return track;
    }

    public static TrackSampleTable parseStbl(Track track, ContainerAtom containerAtom, GaplessInfoHolder gaplessInfoHolder) throws ParserException {
        SampleSizeBox stszSampleSizeBox;
        Track track2 = track;
        ContainerAtom containerAtom2 = containerAtom;
        GaplessInfoHolder gaplessInfoHolder2 = gaplessInfoHolder;
        LeafAtom leafAtomOfType = containerAtom2.getLeafAtomOfType(Atom.TYPE_stsz);
        if (leafAtomOfType != null) {
            stszSampleSizeBox = new StszSampleSizeBox(leafAtomOfType);
        } else {
            leafAtomOfType = containerAtom2.getLeafAtomOfType(Atom.TYPE_stz2);
            if (leafAtomOfType == null) {
                throw new ParserException("Track has no sample table size information");
            }
            stszSampleSizeBox = new Stz2SampleSizeBox(leafAtomOfType);
        }
        int sampleCount = stszSampleSizeBox.getSampleCount();
        if (sampleCount == 0) {
            return new TrackSampleTable(new long[0], new int[0], 0, new long[0], new int[0], C0649C.TIME_UNSET);
        }
        boolean z;
        int readUnsignedIntToInt;
        int readUnsignedIntToInt2;
        long[] jArr;
        Object obj;
        int i;
        int i2;
        long j;
        int i3;
        long j2;
        long j3;
        long[] jArr2;
        Object obj2;
        Object obj3;
        long j4;
        int i4;
        Object obj4;
        Object obj5;
        LeafAtom leafAtomOfType2 = containerAtom2.getLeafAtomOfType(Atom.TYPE_stco);
        if (leafAtomOfType2 == null) {
            leafAtomOfType2 = containerAtom2.getLeafAtomOfType(Atom.TYPE_co64);
            z = true;
        } else {
            z = false;
        }
        ParsableByteArray parsableByteArray = leafAtomOfType2.data;
        ParsableByteArray parsableByteArray2 = containerAtom2.getLeafAtomOfType(Atom.TYPE_stsc).data;
        ParsableByteArray parsableByteArray3 = containerAtom2.getLeafAtomOfType(Atom.TYPE_stts).data;
        LeafAtom leafAtomOfType3 = containerAtom2.getLeafAtomOfType(Atom.TYPE_stss);
        ParsableByteArray parsableByteArray4 = leafAtomOfType3 != null ? leafAtomOfType3.data : null;
        LeafAtom leafAtomOfType4 = containerAtom2.getLeafAtomOfType(Atom.TYPE_ctts);
        ParsableByteArray parsableByteArray5 = leafAtomOfType4 != null ? leafAtomOfType4.data : null;
        ChunkIterator chunkIterator = new ChunkIterator(parsableByteArray2, parsableByteArray, z);
        parsableByteArray3.setPosition(12);
        int readUnsignedIntToInt3 = parsableByteArray3.readUnsignedIntToInt() - 1;
        int readUnsignedIntToInt4 = parsableByteArray3.readUnsignedIntToInt();
        int readUnsignedIntToInt5 = parsableByteArray3.readUnsignedIntToInt();
        if (parsableByteArray5 != null) {
            parsableByteArray5.setPosition(12);
            readUnsignedIntToInt = parsableByteArray5.readUnsignedIntToInt();
        } else {
            readUnsignedIntToInt = 0;
        }
        int i5 = -1;
        if (parsableByteArray4 != null) {
            parsableByteArray4.setPosition(12);
            readUnsignedIntToInt2 = parsableByteArray4.readUnsignedIntToInt();
            if (readUnsignedIntToInt2 > 0) {
                i5 = parsableByteArray4.readUnsignedIntToInt() - 1;
            } else {
                parsableByteArray4 = null;
            }
        } else {
            readUnsignedIntToInt2 = 0;
        }
        Object obj6 = (stszSampleSizeBox.isFixedSampleSize() && MimeTypes.AUDIO_RAW.equals(track2.format.sampleMimeType) && readUnsignedIntToInt3 == 0 && readUnsignedIntToInt == 0 && readUnsignedIntToInt2 == 0) ? 1 : null;
        long j5 = 0;
        Object obj7;
        int i6;
        if (obj6 == null) {
            int i7;
            Object obj8;
            Object obj9;
            int i8;
            obj6 = new long[sampleCount];
            obj7 = new int[sampleCount];
            jArr = new long[sampleCount];
            int i9 = readUnsignedIntToInt2;
            obj = new int[sampleCount];
            int i10 = readUnsignedIntToInt3;
            int i11 = readUnsignedIntToInt4;
            ParsableByteArray parsableByteArray6 = parsableByteArray3;
            i = readUnsignedIntToInt5;
            i6 = i5;
            long j6 = 0;
            readUnsignedIntToInt4 = i9;
            i2 = 0;
            readUnsignedIntToInt3 = 0;
            i5 = 0;
            int i12 = 0;
            int i13 = 0;
            i9 = readUnsignedIntToInt;
            j = j6;
            while (readUnsignedIntToInt3 < sampleCount) {
                int i14;
                int i15;
                while (i12 == 0) {
                    i3 = sampleCount;
                    Assertions.checkState(chunkIterator.moveNext());
                    i7 = readUnsignedIntToInt4;
                    i14 = i;
                    long j7 = chunkIterator.offset;
                    i12 = chunkIterator.numSamples;
                    j6 = j7;
                    sampleCount = i3;
                    readUnsignedIntToInt4 = i7;
                    i = i14;
                }
                i3 = sampleCount;
                i7 = readUnsignedIntToInt4;
                i14 = i;
                if (parsableByteArray5 != null) {
                    while (i5 == 0 && i9 > 0) {
                        i5 = parsableByteArray5.readUnsignedIntToInt();
                        i13 = parsableByteArray5.readInt();
                        i9--;
                    }
                    i5--;
                }
                sampleCount = i13;
                obj6[readUnsignedIntToInt3] = j6;
                obj7[readUnsignedIntToInt3] = stszSampleSizeBox.readNextSampleSize();
                if (obj7[readUnsignedIntToInt3] > i2) {
                    i2 = obj7[readUnsignedIntToInt3];
                }
                jArr[readUnsignedIntToInt3] = j + ((long) sampleCount);
                obj[readUnsignedIntToInt3] = parsableByteArray4 == null ? 1 : 0;
                if (readUnsignedIntToInt3 == i6) {
                    obj[readUnsignedIntToInt3] = 1;
                    i = i7 - 1;
                    if (i > 0) {
                        i6 = parsableByteArray4.readUnsignedIntToInt() - 1;
                    }
                    readUnsignedIntToInt4 = i6;
                    obj8 = obj6;
                    obj9 = obj;
                } else {
                    readUnsignedIntToInt4 = i6;
                    obj8 = obj6;
                    obj9 = obj;
                    i = i7;
                }
                i6 = i14;
                long j8 = j + ((long) i6);
                i11--;
                if (i11 == 0) {
                    i8 = i10;
                    if (i8 > 0) {
                        parsableByteArray = parsableByteArray6;
                        i6 = parsableByteArray.readUnsignedIntToInt();
                        readUnsignedIntToInt5 = parsableByteArray.readInt();
                        i10 = i8 - 1;
                        i11 = i6;
                        i15 = sampleCount;
                        j2 = j6 + ((long) obj7[readUnsignedIntToInt3]);
                        i12--;
                        readUnsignedIntToInt3++;
                        i6 = readUnsignedIntToInt4;
                        readUnsignedIntToInt4 = i;
                        i = readUnsignedIntToInt5;
                        j6 = j2;
                        sampleCount = i3;
                        j = j8;
                        obj6 = obj8;
                        i13 = i15;
                        parsableByteArray6 = parsableByteArray;
                        obj = obj9;
                    } else {
                        parsableByteArray = parsableByteArray6;
                    }
                } else {
                    parsableByteArray = parsableByteArray6;
                    i8 = i10;
                }
                readUnsignedIntToInt5 = i6;
                i10 = i8;
                i15 = sampleCount;
                j2 = j6 + ((long) obj7[readUnsignedIntToInt3]);
                i12--;
                readUnsignedIntToInt3++;
                i6 = readUnsignedIntToInt4;
                readUnsignedIntToInt4 = i;
                i = readUnsignedIntToInt5;
                j6 = j2;
                sampleCount = i3;
                j = j8;
                obj6 = obj8;
                i13 = i15;
                parsableByteArray6 = parsableByteArray;
                obj = obj9;
            }
            i3 = sampleCount;
            obj8 = obj6;
            obj9 = obj;
            i7 = readUnsignedIntToInt4;
            i8 = i10;
            j3 = j + ((long) i13);
            Assertions.checkArgument(i5 == 0);
            while (i9 > 0) {
                Assertions.checkArgument(parsableByteArray5.readUnsignedIntToInt() == 0);
                parsableByteArray5.readInt();
                i9--;
            }
            if (i7 == 0 && i11 == 0 && i12 == 0) {
                if (i8 == 0) {
                    sampleCount = i2;
                    track2 = track;
                    jArr2 = jArr;
                    obj2 = obj7;
                    obj3 = obj8;
                    obj6 = obj9;
                }
            }
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Inconsistent stbl box for track ");
            sampleCount = i2;
            track2 = track;
            stringBuilder.append(track2.id);
            stringBuilder.append(": remainingSynchronizationSamples ");
            stringBuilder.append(i7);
            stringBuilder.append(", remainingSamplesAtTimestampDelta ");
            stringBuilder.append(i11);
            stringBuilder.append(", remainingSamplesInChunk ");
            stringBuilder.append(i12);
            stringBuilder.append(", remainingTimestampDeltaChanges ");
            stringBuilder.append(i8);
            Log.w(str, stringBuilder.toString());
            jArr2 = jArr;
            obj2 = obj7;
            obj3 = obj8;
            obj6 = obj9;
        } else {
            i3 = sampleCount;
            long[] jArr3 = new long[chunkIterator.length];
            int[] iArr = new int[chunkIterator.length];
            while (chunkIterator.moveNext()) {
                jArr3[chunkIterator.index] = chunkIterator.offset;
                iArr[chunkIterator.index] = chunkIterator.numSamples;
            }
            Results rechunk = FixedSampleSizeRechunker.rechunk(stszSampleSizeBox.readNextSampleSize(), jArr3, iArr, (long) readUnsignedIntToInt5);
            obj6 = rechunk.offsets;
            obj7 = rechunk.sizes;
            i6 = rechunk.maximumSize;
            jArr = rechunk.timestamps;
            obj = rechunk.flags;
            j3 = rechunk.duration;
            sampleCount = i6;
            obj3 = obj6;
            obj6 = obj;
            jArr2 = jArr;
            obj2 = obj7;
        }
        long scaleLargeTimestamp = Util.scaleLargeTimestamp(j3, C0649C.MICROS_PER_SECOND, track2.timescale);
        if (track2.editListDurations != null) {
            GaplessInfoHolder gaplessInfoHolder3 = gaplessInfoHolder;
            if (!gaplessInfoHolder.hasGaplessInfo()) {
                long j9;
                int i16;
                int i17;
                Object obj10;
                long scaleLargeTimestamp2;
                Object obj11;
                int i18;
                Track track3;
                Object obj12;
                Object obj13;
                long scaleLargeTimestamp3;
                int i19;
                boolean z2;
                Object obj14;
                int i20;
                Object obj15;
                int i21;
                long j10;
                if (track2.editListDurations.length == 1 && track2.type == 1 && jArr2.length >= 2) {
                    long j11 = track2.editListMediaTimes[0];
                    long scaleLargeTimestamp4 = j11 + Util.scaleLargeTimestamp(track2.editListDurations[0], track2.timescale, track2.movieTimescale);
                    if (jArr2[0] <= j11 && j11 < jArr2[1] && jArr2[jArr2.length - 1] < scaleLargeTimestamp4 && scaleLargeTimestamp4 <= j3) {
                        long j12 = j3 - scaleLargeTimestamp4;
                        long scaleLargeTimestamp5 = Util.scaleLargeTimestamp(j11 - jArr2[0], (long) track2.format.sampleRate, track2.timescale);
                        j9 = j3;
                        j3 = Util.scaleLargeTimestamp(j12, (long) track2.format.sampleRate, track2.timescale);
                        if (!(scaleLargeTimestamp5 == 0 && j3 == 0) && scaleLargeTimestamp5 <= 2147483647L && j3 <= 2147483647L) {
                            gaplessInfoHolder3.encoderDelay = (int) scaleLargeTimestamp5;
                            gaplessInfoHolder3.encoderPadding = (int) j3;
                            Util.scaleLargeTimestampsInPlace(jArr2, C0649C.MICROS_PER_SECOND, track2.timescale);
                            return new TrackSampleTable(obj3, obj2, sampleCount, jArr2, obj6, scaleLargeTimestamp);
                        }
                        if (track2.editListDurations.length == 1 || track2.editListDurations[0] != 0) {
                            z = track2.type != 1;
                            readUnsignedIntToInt4 = 0;
                            i = 0;
                            i16 = 0;
                            i17 = 0;
                            while (readUnsignedIntToInt4 < track2.editListDurations.length) {
                                j4 = scaleLargeTimestamp;
                                scaleLargeTimestamp = track2.editListMediaTimes[readUnsignedIntToInt4];
                                if (scaleLargeTimestamp == -1) {
                                    obj10 = obj2;
                                    i4 = sampleCount;
                                    scaleLargeTimestamp2 = Util.scaleLargeTimestamp(track2.editListDurations[readUnsignedIntToInt4], track2.timescale, track2.movieTimescale);
                                    readUnsignedIntToInt5 = Util.binarySearchCeil(jArr2, scaleLargeTimestamp, true, true);
                                    obj11 = obj3;
                                    i2 = Util.binarySearchCeil(jArr2, scaleLargeTimestamp + scaleLargeTimestamp2, z, false);
                                    i16 += i2 - readUnsignedIntToInt5;
                                    i18 = i17 == readUnsignedIntToInt5 ? 1 : 0;
                                    i17 = i2;
                                    i = i18 | i;
                                } else {
                                    obj11 = obj3;
                                    obj10 = obj2;
                                    i4 = sampleCount;
                                }
                                readUnsignedIntToInt4++;
                                scaleLargeTimestamp = j4;
                                obj2 = obj10;
                                sampleCount = i4;
                                obj3 = obj11;
                                track2 = track;
                            }
                            obj11 = obj3;
                            obj10 = obj2;
                            i4 = sampleCount;
                            j4 = scaleLargeTimestamp;
                            i2 = (i16 == i3 ? 1 : 0) | i;
                            obj3 = i2 == 0 ? new long[i16] : obj11;
                            obj2 = i2 == 0 ? new int[i16] : obj10;
                            sampleCount = i2 == 0 ? 0 : i4;
                            obj = i2 == 0 ? new int[i16] : obj6;
                            jArr = new long[i16];
                            i16 = sampleCount;
                            track3 = track;
                            readUnsignedIntToInt4 = 0;
                            i = 0;
                            while (readUnsignedIntToInt4 < track3.editListDurations.length) {
                                j = track3.editListMediaTimes[readUnsignedIntToInt4];
                                j2 = track3.editListDurations[readUnsignedIntToInt4];
                                if (j == -1) {
                                    obj12 = obj6;
                                    obj13 = obj;
                                    scaleLargeTimestamp3 = j + Util.scaleLargeTimestamp(j2, track3.timescale, track3.movieTimescale);
                                    readUnsignedIntToInt2 = Util.binarySearchCeil(jArr2, j, true, true);
                                    i17 = Util.binarySearchCeil(jArr2, scaleLargeTimestamp3, z, false);
                                    if (i2 == 0) {
                                        i19 = i17 - readUnsignedIntToInt2;
                                        obj6 = obj11;
                                        System.arraycopy(obj6, readUnsignedIntToInt2, obj3, i, i19);
                                        z2 = z;
                                        obj4 = obj10;
                                        System.arraycopy(obj4, readUnsignedIntToInt2, obj2, i, i19);
                                        obj14 = obj3;
                                        i20 = i16;
                                        obj15 = obj12;
                                        obj3 = obj13;
                                        System.arraycopy(obj15, readUnsignedIntToInt2, obj3, i, i19);
                                    } else {
                                        obj14 = obj3;
                                        z2 = z;
                                        i20 = i16;
                                        obj4 = obj10;
                                        obj6 = obj11;
                                        obj15 = obj12;
                                        obj3 = obj13;
                                    }
                                    i19 = i20;
                                    while (readUnsignedIntToInt2 < i17) {
                                        obj5 = obj15;
                                        i21 = i17;
                                        j10 = j;
                                        jArr[i] = Util.scaleLargeTimestamp(j5, C0649C.MICROS_PER_SECOND, track3.movieTimescale) + Util.scaleLargeTimestamp(jArr2[readUnsignedIntToInt2] - j, C0649C.MICROS_PER_SECOND, track3.timescale);
                                        if (i2 != 0 && obj2[i] > i19) {
                                            i19 = obj4[readUnsignedIntToInt2];
                                        }
                                        i++;
                                        readUnsignedIntToInt2++;
                                        i17 = i21;
                                        obj15 = obj5;
                                        j = j10;
                                    }
                                    obj5 = obj15;
                                    i16 = i19;
                                } else {
                                    obj14 = obj3;
                                    obj5 = obj6;
                                    obj3 = obj;
                                    z2 = z;
                                    i20 = i16;
                                    obj4 = obj10;
                                    obj6 = obj11;
                                }
                                readUnsignedIntToInt4++;
                                obj = obj3;
                                obj11 = obj6;
                                obj10 = obj4;
                                j5 += j2;
                                z = z2;
                                obj3 = obj14;
                                obj6 = obj5;
                            }
                            obj14 = obj3;
                            obj5 = obj6;
                            obj3 = obj;
                            i20 = i16;
                            obj4 = obj10;
                            obj6 = obj11;
                            j2 = Util.scaleLargeTimestamp(j5, C0649C.MICROS_PER_SECOND, track3.timescale);
                            readUnsignedIntToInt2 = 0;
                            for (i2 = 0; i2 < obj3.length && readUnsignedIntToInt2 == 0; i2++) {
                                readUnsignedIntToInt2 |= (readUnsignedIntToInt4 & 1) == 0 ? 1 : 0;
                            }
                            if (readUnsignedIntToInt2 == 0) {
                                return new TrackSampleTable(obj14, obj2, i20, jArr, obj3, j2);
                            }
                            Log.w(TAG, "Ignoring edit list: Edited sample sequence does not contain a sync sample.");
                            Util.scaleLargeTimestampsInPlace(jArr2, C0649C.MICROS_PER_SECOND, track3.timescale);
                            return new TrackSampleTable(obj6, obj4, i4, jArr2, obj5, j4);
                        }
                        long j13 = track2.editListMediaTimes[0];
                        for (readUnsignedIntToInt2 = 0; readUnsignedIntToInt2 < jArr2.length; readUnsignedIntToInt2++) {
                            jArr2[readUnsignedIntToInt2] = Util.scaleLargeTimestamp(jArr2[readUnsignedIntToInt2] - j13, C0649C.MICROS_PER_SECOND, track2.timescale);
                        }
                        return new TrackSampleTable(obj3, obj2, sampleCount, jArr2, obj6, Util.scaleLargeTimestamp(j9 - j13, C0649C.MICROS_PER_SECOND, track2.timescale));
                    }
                }
                j9 = j3;
                if (track2.editListDurations.length == 1) {
                }
                if (track2.type != 1) {
                }
                readUnsignedIntToInt4 = 0;
                i = 0;
                i16 = 0;
                i17 = 0;
                while (readUnsignedIntToInt4 < track2.editListDurations.length) {
                    j4 = scaleLargeTimestamp;
                    scaleLargeTimestamp = track2.editListMediaTimes[readUnsignedIntToInt4];
                    if (scaleLargeTimestamp == -1) {
                        obj11 = obj3;
                        obj10 = obj2;
                        i4 = sampleCount;
                    } else {
                        obj10 = obj2;
                        i4 = sampleCount;
                        scaleLargeTimestamp2 = Util.scaleLargeTimestamp(track2.editListDurations[readUnsignedIntToInt4], track2.timescale, track2.movieTimescale);
                        readUnsignedIntToInt5 = Util.binarySearchCeil(jArr2, scaleLargeTimestamp, true, true);
                        obj11 = obj3;
                        i2 = Util.binarySearchCeil(jArr2, scaleLargeTimestamp + scaleLargeTimestamp2, z, false);
                        i16 += i2 - readUnsignedIntToInt5;
                        if (i17 == readUnsignedIntToInt5) {
                        }
                        i17 = i2;
                        i = i18 | i;
                    }
                    readUnsignedIntToInt4++;
                    scaleLargeTimestamp = j4;
                    obj2 = obj10;
                    sampleCount = i4;
                    obj3 = obj11;
                    track2 = track;
                }
                obj11 = obj3;
                obj10 = obj2;
                i4 = sampleCount;
                j4 = scaleLargeTimestamp;
                if (i16 == i3) {
                }
                i2 = (i16 == i3 ? 1 : 0) | i;
                if (i2 == 0) {
                }
                if (i2 == 0) {
                }
                if (i2 == 0) {
                }
                if (i2 == 0) {
                }
                jArr = new long[i16];
                i16 = sampleCount;
                track3 = track;
                readUnsignedIntToInt4 = 0;
                i = 0;
                while (readUnsignedIntToInt4 < track3.editListDurations.length) {
                    j = track3.editListMediaTimes[readUnsignedIntToInt4];
                    j2 = track3.editListDurations[readUnsignedIntToInt4];
                    if (j == -1) {
                        obj14 = obj3;
                        obj5 = obj6;
                        obj3 = obj;
                        z2 = z;
                        i20 = i16;
                        obj4 = obj10;
                        obj6 = obj11;
                    } else {
                        obj12 = obj6;
                        obj13 = obj;
                        scaleLargeTimestamp3 = j + Util.scaleLargeTimestamp(j2, track3.timescale, track3.movieTimescale);
                        readUnsignedIntToInt2 = Util.binarySearchCeil(jArr2, j, true, true);
                        i17 = Util.binarySearchCeil(jArr2, scaleLargeTimestamp3, z, false);
                        if (i2 == 0) {
                            obj14 = obj3;
                            z2 = z;
                            i20 = i16;
                            obj4 = obj10;
                            obj6 = obj11;
                            obj15 = obj12;
                            obj3 = obj13;
                        } else {
                            i19 = i17 - readUnsignedIntToInt2;
                            obj6 = obj11;
                            System.arraycopy(obj6, readUnsignedIntToInt2, obj3, i, i19);
                            z2 = z;
                            obj4 = obj10;
                            System.arraycopy(obj4, readUnsignedIntToInt2, obj2, i, i19);
                            obj14 = obj3;
                            i20 = i16;
                            obj15 = obj12;
                            obj3 = obj13;
                            System.arraycopy(obj15, readUnsignedIntToInt2, obj3, i, i19);
                        }
                        i19 = i20;
                        while (readUnsignedIntToInt2 < i17) {
                            obj5 = obj15;
                            i21 = i17;
                            j10 = j;
                            jArr[i] = Util.scaleLargeTimestamp(j5, C0649C.MICROS_PER_SECOND, track3.movieTimescale) + Util.scaleLargeTimestamp(jArr2[readUnsignedIntToInt2] - j, C0649C.MICROS_PER_SECOND, track3.timescale);
                            i19 = obj4[readUnsignedIntToInt2];
                            i++;
                            readUnsignedIntToInt2++;
                            i17 = i21;
                            obj15 = obj5;
                            j = j10;
                        }
                        obj5 = obj15;
                        i16 = i19;
                    }
                    readUnsignedIntToInt4++;
                    obj = obj3;
                    obj11 = obj6;
                    obj10 = obj4;
                    j5 += j2;
                    z = z2;
                    obj3 = obj14;
                    obj6 = obj5;
                }
                obj14 = obj3;
                obj5 = obj6;
                obj3 = obj;
                i20 = i16;
                obj4 = obj10;
                obj6 = obj11;
                j2 = Util.scaleLargeTimestamp(j5, C0649C.MICROS_PER_SECOND, track3.timescale);
                readUnsignedIntToInt2 = 0;
                for (int readUnsignedIntToInt42 : obj3) {
                    if ((readUnsignedIntToInt42 & 1) == 0) {
                    }
                    readUnsignedIntToInt2 |= (readUnsignedIntToInt42 & 1) == 0 ? 1 : 0;
                }
                if (readUnsignedIntToInt2 == 0) {
                    return new TrackSampleTable(obj14, obj2, i20, jArr, obj3, j2);
                }
                Log.w(TAG, "Ignoring edit list: Edited sample sequence does not contain a sync sample.");
                Util.scaleLargeTimestampsInPlace(jArr2, C0649C.MICROS_PER_SECOND, track3.timescale);
                return new TrackSampleTable(obj6, obj4, i4, jArr2, obj5, j4);
            }
        }
        obj4 = obj2;
        i4 = sampleCount;
        obj5 = obj6;
        j4 = scaleLargeTimestamp;
        obj6 = obj3;
        Util.scaleLargeTimestampsInPlace(jArr2, C0649C.MICROS_PER_SECOND, track2.timescale);
        return new TrackSampleTable(obj6, obj4, i4, jArr2, obj5, j4);
    }

    public static Metadata parseUdta(LeafAtom leafAtom, boolean z) {
        if (z) {
            return null;
        }
        leafAtom = leafAtom.data;
        leafAtom.setPosition(8);
        while (leafAtom.bytesLeft() >= 8) {
            int position = leafAtom.getPosition();
            int readInt = leafAtom.readInt();
            if (leafAtom.readInt() == Atom.TYPE_meta) {
                leafAtom.setPosition(position);
                return parseMetaAtom(leafAtom, position + readInt);
            }
            leafAtom.skipBytes(readInt - 8);
        }
        return null;
    }

    private static Metadata parseMetaAtom(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.skipBytes(12);
        while (parsableByteArray.getPosition() < i) {
            int position = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_ilst) {
                parsableByteArray.setPosition(position);
                return parseIlst(parsableByteArray, position + readInt);
            }
            parsableByteArray.skipBytes(readInt - 8);
        }
        return null;
    }

    private static Metadata parseIlst(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.skipBytes(8);
        List arrayList = new ArrayList();
        while (parsableByteArray.getPosition() < i) {
            Entry parseIlstElement = MetadataUtil.parseIlstElement(parsableByteArray);
            if (parseIlstElement != null) {
                arrayList.add(parseIlstElement);
            }
        }
        return arrayList.isEmpty() != null ? null : new Metadata(arrayList);
    }

    private static long parseMvhd(ParsableByteArray parsableByteArray) {
        int i = 8;
        parsableByteArray.setPosition(8);
        if (Atom.parseFullAtomVersion(parsableByteArray.readInt()) != 0) {
            i = 16;
        }
        parsableByteArray.skipBytes(i);
        return parsableByteArray.readUnsignedInt();
    }

    private static TkhdData parseTkhd(ParsableByteArray parsableByteArray) {
        Object obj;
        int i = 8;
        parsableByteArray.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(parseFullAtomVersion == 0 ? 8 : 16);
        int readInt = parsableByteArray.readInt();
        parsableByteArray.skipBytes(4);
        int position = parsableByteArray.getPosition();
        if (parseFullAtomVersion == 0) {
            i = 4;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            if (parsableByteArray.data[position + i3] != (byte) -1) {
                obj = null;
                break;
            }
        }
        obj = 1;
        long j = C0649C.TIME_UNSET;
        if (obj != null) {
            parsableByteArray.skipBytes(i);
        } else {
            long readUnsignedInt = parseFullAtomVersion == 0 ? parsableByteArray.readUnsignedInt() : parsableByteArray.readUnsignedLongToLong();
            if (readUnsignedInt != 0) {
                j = readUnsignedInt;
            }
        }
        parsableByteArray.skipBytes(16);
        i = parsableByteArray.readInt();
        parseFullAtomVersion = parsableByteArray.readInt();
        parsableByteArray.skipBytes(4);
        int readInt2 = parsableByteArray.readInt();
        parsableByteArray = parsableByteArray.readInt();
        if (i == 0 && parseFullAtomVersion == 65536 && readInt2 == SupportMenu.CATEGORY_MASK && parsableByteArray == null) {
            i2 = 90;
        } else if (i == 0 && parseFullAtomVersion == SupportMenu.CATEGORY_MASK && readInt2 == 65536 && parsableByteArray == null) {
            i2 = 270;
        } else if (i == SupportMenu.CATEGORY_MASK && parseFullAtomVersion == 0 && readInt2 == 0 && parsableByteArray == -65536) {
            i2 = 180;
        }
        return new TkhdData(readInt, j, i2);
    }

    private static int parseHdlr(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(16);
        parsableByteArray = parsableByteArray.readInt();
        if (parsableByteArray == TYPE_soun) {
            return 1;
        }
        if (parsableByteArray == TYPE_vide) {
            return 2;
        }
        if (!(parsableByteArray == TYPE_text || parsableByteArray == TYPE_sbtl || parsableByteArray == TYPE_subt)) {
            if (parsableByteArray != TYPE_clcp) {
                return parsableByteArray == TYPE_meta ? 4 : -1;
            }
        }
        return 3;
    }

    private static Pair<Long, String> parseMdhd(ParsableByteArray parsableByteArray) {
        int i = 8;
        parsableByteArray.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(parseFullAtomVersion == 0 ? 8 : 16);
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        if (parseFullAtomVersion == 0) {
            i = 4;
        }
        parsableByteArray.skipBytes(i);
        parsableByteArray = parsableByteArray.readUnsignedShort();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append((char) (((parsableByteArray >> 10) & 31) + 96));
        stringBuilder.append((char) (((parsableByteArray >> 5) & 31) + 96));
        stringBuilder.append((char) ((parsableByteArray & 31) + 96));
        return Pair.create(Long.valueOf(readUnsignedInt), stringBuilder.toString());
    }

    private static StsdData parseStsd(ParsableByteArray parsableByteArray, int i, int i2, String str, DrmInitData drmInitData, boolean z) throws ParserException {
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        parsableByteArray2.setPosition(12);
        int readInt = parsableByteArray.readInt();
        StsdData stsdData = new StsdData(readInt);
        for (int i3 = 0; i3 < readInt; i3++) {
            int position = parsableByteArray.getPosition();
            int readInt2 = parsableByteArray.readInt();
            Assertions.checkArgument(readInt2 > 0, "childAtomSize should be positive");
            int readInt3 = parsableByteArray.readInt();
            if (!(readInt3 == Atom.TYPE_avc1 || readInt3 == Atom.TYPE_avc3 || readInt3 == Atom.TYPE_encv || readInt3 == Atom.TYPE_mp4v || readInt3 == Atom.TYPE_hvc1 || readInt3 == Atom.TYPE_hev1 || readInt3 == Atom.TYPE_s263 || readInt3 == Atom.TYPE_vp08)) {
                if (readInt3 != Atom.TYPE_vp09) {
                    if (!(readInt3 == Atom.TYPE_mp4a || readInt3 == Atom.TYPE_enca || readInt3 == Atom.TYPE_ac_3 || readInt3 == Atom.TYPE_ec_3 || readInt3 == Atom.TYPE_dtsc || readInt3 == Atom.TYPE_dtse || readInt3 == Atom.TYPE_dtsh || readInt3 == Atom.TYPE_dtsl || readInt3 == Atom.TYPE_samr || readInt3 == Atom.TYPE_sawb || readInt3 == Atom.TYPE_lpcm || readInt3 == Atom.TYPE_sowt || readInt3 == Atom.TYPE__mp3)) {
                        if (readInt3 != Atom.TYPE_alac) {
                            if (!(readInt3 == Atom.TYPE_TTML || readInt3 == Atom.TYPE_tx3g || readInt3 == Atom.TYPE_wvtt || readInt3 == Atom.TYPE_stpp)) {
                                if (readInt3 != Atom.TYPE_c608) {
                                    if (readInt3 == Atom.TYPE_camm) {
                                        stsdData.format = Format.createSampleFormat(Integer.toString(i), MimeTypes.APPLICATION_CAMERA_MOTION, null, -1, null);
                                    }
                                    parsableByteArray2.setPosition(position + readInt2);
                                }
                            }
                            parseTextSampleEntry(parsableByteArray2, readInt3, position, readInt2, i, str, stsdData);
                            parsableByteArray2.setPosition(position + readInt2);
                        }
                    }
                    parseAudioSampleEntry(parsableByteArray2, readInt3, position, readInt2, i, str, z, drmInitData, stsdData, i3);
                    parsableByteArray2.setPosition(position + readInt2);
                }
            }
            parseVideoSampleEntry(parsableByteArray2, readInt3, position, readInt2, i, i2, drmInitData, stsdData, i3);
            parsableByteArray2.setPosition(position + readInt2);
        }
        return stsdData;
    }

    private static void parseTextSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, String str, StsdData stsdData) throws ParserException {
        String str2;
        String str3;
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int i5 = i;
        StsdData stsdData2 = stsdData;
        parsableByteArray2.setPosition((i2 + 8) + 8);
        List list = null;
        long j = Long.MAX_VALUE;
        if (i5 == Atom.TYPE_TTML) {
            str2 = MimeTypes.APPLICATION_TTML;
        } else if (i5 == Atom.TYPE_tx3g) {
            String str4 = MimeTypes.APPLICATION_TX3G;
            int i6 = (i3 - 8) - 8;
            Object obj = new byte[i6];
            parsableByteArray2.readBytes(obj, 0, i6);
            list = Collections.singletonList(obj);
            str3 = str4;
            stsdData2.format = Format.createTextSampleFormat(Integer.toString(i4), str3, null, -1, 0, str, -1, null, j, list);
        } else if (i5 == Atom.TYPE_wvtt) {
            str2 = MimeTypes.APPLICATION_MP4VTT;
        } else if (i5 == Atom.TYPE_stpp) {
            str2 = MimeTypes.APPLICATION_TTML;
            j = 0;
        } else if (i5 == Atom.TYPE_c608) {
            str2 = MimeTypes.APPLICATION_MP4CEA608;
            stsdData2.requiredSampleTransformation = 1;
        } else {
            throw new IllegalStateException();
        }
        str3 = str2;
        stsdData2.format = Format.createTextSampleFormat(Integer.toString(i4), str3, null, -1, 0, str, -1, null, j, list);
    }

    private static void parseVideoSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, int i5, DrmInitData drmInitData, StsdData stsdData, int i6) throws ParserException {
        Pair parseSampleEntryEncryptionData;
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int i7 = i2;
        int i8 = i3;
        DrmInitData drmInitData2 = drmInitData;
        StsdData stsdData2 = stsdData;
        parsableByteArray2.setPosition((i7 + 8) + 8);
        parsableByteArray2.skipBytes(16);
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        int readUnsignedShort2 = parsableByteArray.readUnsignedShort();
        parsableByteArray2.skipBytes(50);
        int position = parsableByteArray.getPosition();
        String str = null;
        int i9 = i;
        if (i9 == Atom.TYPE_encv) {
            parseSampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray2, i7, i8);
            if (parseSampleEntryEncryptionData != null) {
                i9 = ((Integer) parseSampleEntryEncryptionData.first).intValue();
                if (drmInitData2 == null) {
                    drmInitData2 = null;
                } else {
                    drmInitData2 = drmInitData2.copyWithSchemeType(((TrackEncryptionBox) parseSampleEntryEncryptionData.second).schemeType);
                }
                stsdData2.trackEncryptionBoxes[i6] = (TrackEncryptionBox) parseSampleEntryEncryptionData.second;
            }
            parsableByteArray2.setPosition(position);
        }
        DrmInitData drmInitData3 = drmInitData2;
        List list = null;
        byte[] bArr = list;
        Object obj = null;
        float f = 1.0f;
        int i10 = -1;
        while (position - i7 < i8) {
            parsableByteArray2.setPosition(position);
            int position2 = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            if (readInt != 0 || parsableByteArray.getPosition() - i7 != i8) {
                Assertions.checkArgument(readInt > 0, "childAtomSize should be positive");
                int readInt2 = parsableByteArray.readInt();
                if (readInt2 == Atom.TYPE_avcC) {
                    Assertions.checkState(str == null);
                    str = MimeTypes.VIDEO_H264;
                    parsableByteArray2.setPosition(position2 + 8);
                    AvcConfig parse = AvcConfig.parse(parsableByteArray);
                    list = parse.initializationData;
                    stsdData2.nalUnitLengthFieldLength = parse.nalUnitLengthFieldLength;
                    if (obj == null) {
                        f = parse.pixelWidthAspectRatio;
                    }
                } else if (readInt2 == Atom.TYPE_hvcC) {
                    Assertions.checkState(str == null);
                    str = MimeTypes.VIDEO_H265;
                    parsableByteArray2.setPosition(position2 + 8);
                    HevcConfig parse2 = HevcConfig.parse(parsableByteArray);
                    list = parse2.initializationData;
                    stsdData2.nalUnitLengthFieldLength = parse2.nalUnitLengthFieldLength;
                } else if (readInt2 == Atom.TYPE_vpcC) {
                    Assertions.checkState(str == null);
                    str = i9 == Atom.TYPE_vp08 ? MimeTypes.VIDEO_VP8 : MimeTypes.VIDEO_VP9;
                } else if (readInt2 == Atom.TYPE_d263) {
                    Assertions.checkState(str == null);
                    str = MimeTypes.VIDEO_H263;
                } else if (readInt2 == Atom.TYPE_esds) {
                    Assertions.checkState(str == null);
                    parseSampleEntryEncryptionData = parseEsdsFromParent(parsableByteArray2, position2);
                    str = (String) parseSampleEntryEncryptionData.first;
                    list = Collections.singletonList(parseSampleEntryEncryptionData.second);
                } else if (readInt2 == Atom.TYPE_pasp) {
                    f = parsePaspFromParent(parsableByteArray2, position2);
                    obj = 1;
                } else if (readInt2 == Atom.TYPE_sv3d) {
                    bArr = parseProjFromParent(parsableByteArray2, position2, readInt);
                } else if (readInt2 == Atom.TYPE_st3d) {
                    readInt2 = parsableByteArray.readUnsignedByte();
                    parsableByteArray2.skipBytes(3);
                    if (readInt2 == 0) {
                        switch (parsableByteArray.readUnsignedByte()) {
                            case 0:
                                i10 = 0;
                                break;
                            case 1:
                                i10 = 1;
                                break;
                            case 2:
                                i10 = 2;
                                break;
                            case 3:
                                i10 = 3;
                                break;
                            default:
                                break;
                        }
                    }
                }
                position += readInt;
            } else if (str == null) {
                stsdData2.format = Format.createVideoSampleFormat(Integer.toString(i4), str, null, -1, -1, readUnsignedShort, readUnsignedShort2, -1.0f, list, i5, f, bArr, i10, null, drmInitData3);
            }
        }
        if (str == null) {
            stsdData2.format = Format.createVideoSampleFormat(Integer.toString(i4), str, null, -1, -1, readUnsignedShort, readUnsignedShort2, -1.0f, list, i5, f, bArr, i10, null, drmInitData3);
        }
    }

    private static Pair<long[], long[]> parseEdts(ContainerAtom containerAtom) {
        if (containerAtom != null) {
            containerAtom = containerAtom.getLeafAtomOfType(Atom.TYPE_elst);
            if (containerAtom != null) {
                containerAtom = containerAtom.data;
                containerAtom.setPosition(8);
                int parseFullAtomVersion = Atom.parseFullAtomVersion(containerAtom.readInt());
                int readUnsignedIntToInt = containerAtom.readUnsignedIntToInt();
                Object obj = new long[readUnsignedIntToInt];
                Object obj2 = new long[readUnsignedIntToInt];
                for (int i = 0; i < readUnsignedIntToInt; i++) {
                    obj[i] = parseFullAtomVersion == 1 ? containerAtom.readUnsignedLongToLong() : containerAtom.readUnsignedInt();
                    obj2[i] = parseFullAtomVersion == 1 ? containerAtom.readLong() : (long) containerAtom.readInt();
                    if (containerAtom.readShort() != (short) 1) {
                        throw new IllegalArgumentException("Unsupported media rate.");
                    }
                    containerAtom.skipBytes(2);
                }
                return Pair.create(obj, obj2);
            }
        }
        return Pair.create(null, null);
    }

    private static float parsePaspFromParent(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.setPosition(i + 8);
        return ((float) parsableByteArray.readUnsignedIntToInt()) / ((float) parsableByteArray.readUnsignedIntToInt());
    }

    private static void parseAudioSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, String str, boolean z, DrmInitData drmInitData, StsdData stsdData, int i5) throws ParserException {
        int readUnsignedShort;
        int round;
        int position;
        int i6;
        Pair parseSampleEntryEncryptionData;
        DrmInitData drmInitData2;
        String str2;
        String str3;
        int i7;
        int i8;
        Object obj;
        int readInt;
        int i9;
        String str4;
        int i10;
        DrmInitData drmInitData3;
        Object obj2;
        StsdData stsdData2;
        Object obj3;
        String str5;
        int i11;
        Object obj4;
        List list;
        ParsableByteArray parsableByteArray2 = parsableByteArray;
        int i12 = i2;
        int i13 = i3;
        String str6 = str;
        DrmInitData drmInitData4 = drmInitData;
        StsdData stsdData3 = stsdData;
        parsableByteArray2.setPosition((i12 + 8) + 8);
        if (z) {
            readUnsignedShort = parsableByteArray.readUnsignedShort();
            parsableByteArray2.skipBytes(6);
        } else {
            parsableByteArray2.skipBytes(8);
            readUnsignedShort = 0;
        }
        if (readUnsignedShort != 0) {
            if (readUnsignedShort != 1) {
                if (readUnsignedShort == 2) {
                    parsableByteArray2.skipBytes(16);
                    round = (int) Math.round(parsableByteArray.readDouble());
                    readUnsignedShort = parsableByteArray.readUnsignedIntToInt();
                    parsableByteArray2.skipBytes(20);
                    position = parsableByteArray.getPosition();
                    i6 = i;
                    if (i6 == Atom.TYPE_enca) {
                        parseSampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray2, i12, i13);
                        if (parseSampleEntryEncryptionData != null) {
                            i6 = ((Integer) parseSampleEntryEncryptionData.first).intValue();
                            if (drmInitData4 != null) {
                                drmInitData4 = null;
                            } else {
                                drmInitData4 = drmInitData4.copyWithSchemeType(((TrackEncryptionBox) parseSampleEntryEncryptionData.second).schemeType);
                            }
                            stsdData3.trackEncryptionBoxes[i5] = (TrackEncryptionBox) parseSampleEntryEncryptionData.second;
                        }
                        parsableByteArray2.setPosition(position);
                    }
                    drmInitData2 = drmInitData4;
                    if (i6 == Atom.TYPE_ac_3) {
                        str2 = MimeTypes.AUDIO_AC3;
                    } else if (i6 == Atom.TYPE_ec_3) {
                        str2 = MimeTypes.AUDIO_E_AC3;
                    } else if (i6 != Atom.TYPE_dtsc) {
                        str2 = MimeTypes.AUDIO_DTS;
                    } else {
                        if (i6 != Atom.TYPE_dtsh) {
                            if (i6 == Atom.TYPE_dtsl) {
                                if (i6 == Atom.TYPE_dtse) {
                                    str2 = MimeTypes.AUDIO_DTS_EXPRESS;
                                } else if (i6 == Atom.TYPE_samr) {
                                    str2 = MimeTypes.AUDIO_AMR_NB;
                                } else if (i6 != Atom.TYPE_sawb) {
                                    str2 = MimeTypes.AUDIO_AMR_WB;
                                } else {
                                    if (i6 != Atom.TYPE_lpcm) {
                                        if (i6 == Atom.TYPE_sowt) {
                                            str2 = i6 != Atom.TYPE__mp3 ? MimeTypes.AUDIO_MPEG : i6 != Atom.TYPE_alac ? MimeTypes.AUDIO_ALAC : null;
                                        }
                                    }
                                    str2 = MimeTypes.AUDIO_RAW;
                                }
                            }
                        }
                        str2 = MimeTypes.AUDIO_DTS_HD;
                    }
                    str3 = str2;
                    i7 = round;
                    i8 = readUnsignedShort;
                    i6 = position;
                    obj = null;
                    while (i6 - i12 < i13) {
                        parsableByteArray2.setPosition(i6);
                        readUnsignedShort = parsableByteArray.readInt();
                        Assertions.checkArgument(readUnsignedShort <= 0, "childAtomSize should be positive");
                        readInt = parsableByteArray.readInt();
                        if (readInt != Atom.TYPE_esds) {
                            if (z || readInt != Atom.TYPE_wave) {
                                if (readInt == Atom.TYPE_dac3) {
                                    parsableByteArray2.setPosition(i6 + 8);
                                    stsdData3.format = Ac3Util.parseAc3AnnexFFormat(parsableByteArray2, Integer.toString(i4), str6, drmInitData2);
                                } else if (readInt != Atom.TYPE_dec3) {
                                    parsableByteArray2.setPosition(i6 + 8);
                                    stsdData3.format = Ac3Util.parseEAc3AnnexFFormat(parsableByteArray2, Integer.toString(i4), str6, drmInitData2);
                                } else {
                                    if (readInt != Atom.TYPE_ddts) {
                                        i9 = readUnsignedShort;
                                        str4 = str3;
                                        i10 = i6;
                                        drmInitData3 = drmInitData2;
                                        obj2 = obj;
                                        stsdData2 = stsdData3;
                                        stsdData2.format = Format.createAudioSampleFormat(Integer.toString(i4), str3, null, -1, -1, i8, i7, null, drmInitData3, 0, str6);
                                    } else {
                                        i9 = readUnsignedShort;
                                        obj2 = obj;
                                        str4 = str3;
                                        i10 = i6;
                                        drmInitData3 = drmInitData2;
                                        stsdData2 = stsdData3;
                                        if (readInt == Atom.TYPE_alac) {
                                            readUnsignedShort = i9;
                                            obj3 = new byte[readUnsignedShort];
                                            position = i10;
                                            parsableByteArray2.setPosition(position);
                                            parsableByteArray2.readBytes(obj3, 0, readUnsignedShort);
                                            i6 = position + readUnsignedShort;
                                            stsdData3 = stsdData2;
                                            obj = obj3;
                                            drmInitData2 = drmInitData3;
                                            str3 = str4;
                                            i13 = i3;
                                        }
                                    }
                                    readUnsignedShort = i9;
                                    position = i10;
                                    obj3 = obj2;
                                    i6 = position + readUnsignedShort;
                                    stsdData3 = stsdData2;
                                    obj = obj3;
                                    drmInitData2 = drmInitData3;
                                    str3 = str4;
                                    i13 = i3;
                                }
                                obj2 = obj;
                                str4 = str3;
                                position = i6;
                                drmInitData3 = drmInitData2;
                                stsdData2 = stsdData3;
                                obj3 = obj2;
                                i6 = position + readUnsignedShort;
                                stsdData3 = stsdData2;
                                obj = obj3;
                                drmInitData2 = drmInitData3;
                                str3 = str4;
                                i13 = i3;
                            }
                        }
                        obj2 = obj;
                        str4 = str3;
                        position = i6;
                        drmInitData3 = drmInitData2;
                        stsdData2 = stsdData3;
                        if (readInt != Atom.TYPE_esds) {
                            i6 = position;
                        } else {
                            i6 = findEsdsPosition(parsableByteArray2, position, readUnsignedShort);
                        }
                        if (i6 == -1) {
                            Pair parseEsdsFromParent = parseEsdsFromParent(parsableByteArray2, i6);
                            str5 = (String) parseEsdsFromParent.first;
                            obj3 = (byte[]) parseEsdsFromParent.second;
                            if (MimeTypes.AUDIO_AAC.equals(str5)) {
                                Pair parseAacAudioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(obj3);
                                i7 = ((Integer) parseAacAudioSpecificConfig.first).intValue();
                                i8 = ((Integer) parseAacAudioSpecificConfig.second).intValue();
                            }
                        } else {
                            str5 = str4;
                            obj3 = obj2;
                        }
                        str4 = str5;
                        i6 = position + readUnsignedShort;
                        stsdData3 = stsdData2;
                        obj = obj3;
                        drmInitData2 = drmInitData3;
                        str3 = str4;
                        i13 = i3;
                    }
                    obj2 = obj;
                    str4 = str3;
                    drmInitData3 = drmInitData2;
                    stsdData2 = stsdData3;
                    if (stsdData2.format == null) {
                        str2 = str4;
                        if (str2 != null) {
                            i11 = MimeTypes.AUDIO_RAW.equals(str2) ? 2 : -1;
                            String num = Integer.toString(i4);
                            obj4 = obj2;
                            if (obj4 != null) {
                                list = null;
                            } else {
                                list = Collections.singletonList(obj4);
                            }
                            stsdData2.format = Format.createAudioSampleFormat(num, str2, null, -1, -1, i8, i7, i11, list, drmInitData3, 0, str6);
                        }
                    }
                }
                return;
            }
        }
        i11 = parsableByteArray.readUnsignedShort();
        parsableByteArray2.skipBytes(6);
        round = parsableByteArray.readUnsignedFixedPoint1616();
        if (readUnsignedShort == 1) {
            parsableByteArray2.skipBytes(16);
        }
        readUnsignedShort = i11;
        position = parsableByteArray.getPosition();
        i6 = i;
        if (i6 == Atom.TYPE_enca) {
            parseSampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray2, i12, i13);
            if (parseSampleEntryEncryptionData != null) {
                i6 = ((Integer) parseSampleEntryEncryptionData.first).intValue();
                if (drmInitData4 != null) {
                    drmInitData4 = drmInitData4.copyWithSchemeType(((TrackEncryptionBox) parseSampleEntryEncryptionData.second).schemeType);
                } else {
                    drmInitData4 = null;
                }
                stsdData3.trackEncryptionBoxes[i5] = (TrackEncryptionBox) parseSampleEntryEncryptionData.second;
            }
            parsableByteArray2.setPosition(position);
        }
        drmInitData2 = drmInitData4;
        if (i6 == Atom.TYPE_ac_3) {
            str2 = MimeTypes.AUDIO_AC3;
        } else if (i6 == Atom.TYPE_ec_3) {
            str2 = MimeTypes.AUDIO_E_AC3;
        } else if (i6 != Atom.TYPE_dtsc) {
            if (i6 != Atom.TYPE_dtsh) {
                if (i6 == Atom.TYPE_dtsl) {
                    if (i6 == Atom.TYPE_dtse) {
                        str2 = MimeTypes.AUDIO_DTS_EXPRESS;
                    } else if (i6 == Atom.TYPE_samr) {
                        str2 = MimeTypes.AUDIO_AMR_NB;
                    } else if (i6 != Atom.TYPE_sawb) {
                        if (i6 != Atom.TYPE_lpcm) {
                            if (i6 == Atom.TYPE_sowt) {
                                if (i6 != Atom.TYPE__mp3) {
                                    if (i6 != Atom.TYPE_alac) {
                                    }
                                }
                            }
                        }
                        str2 = MimeTypes.AUDIO_RAW;
                    } else {
                        str2 = MimeTypes.AUDIO_AMR_WB;
                    }
                }
            }
            str2 = MimeTypes.AUDIO_DTS_HD;
        } else {
            str2 = MimeTypes.AUDIO_DTS;
        }
        str3 = str2;
        i7 = round;
        i8 = readUnsignedShort;
        i6 = position;
        obj = null;
        while (i6 - i12 < i13) {
            parsableByteArray2.setPosition(i6);
            readUnsignedShort = parsableByteArray.readInt();
            if (readUnsignedShort <= 0) {
            }
            Assertions.checkArgument(readUnsignedShort <= 0, "childAtomSize should be positive");
            readInt = parsableByteArray.readInt();
            if (readInt != Atom.TYPE_esds) {
                if (z) {
                }
                if (readInt == Atom.TYPE_dac3) {
                    parsableByteArray2.setPosition(i6 + 8);
                    stsdData3.format = Ac3Util.parseAc3AnnexFFormat(parsableByteArray2, Integer.toString(i4), str6, drmInitData2);
                } else if (readInt != Atom.TYPE_dec3) {
                    if (readInt != Atom.TYPE_ddts) {
                        i9 = readUnsignedShort;
                        obj2 = obj;
                        str4 = str3;
                        i10 = i6;
                        drmInitData3 = drmInitData2;
                        stsdData2 = stsdData3;
                        if (readInt == Atom.TYPE_alac) {
                            readUnsignedShort = i9;
                            obj3 = new byte[readUnsignedShort];
                            position = i10;
                            parsableByteArray2.setPosition(position);
                            parsableByteArray2.readBytes(obj3, 0, readUnsignedShort);
                            i6 = position + readUnsignedShort;
                            stsdData3 = stsdData2;
                            obj = obj3;
                            drmInitData2 = drmInitData3;
                            str3 = str4;
                            i13 = i3;
                        }
                    } else {
                        i9 = readUnsignedShort;
                        str4 = str3;
                        i10 = i6;
                        drmInitData3 = drmInitData2;
                        obj2 = obj;
                        stsdData2 = stsdData3;
                        stsdData2.format = Format.createAudioSampleFormat(Integer.toString(i4), str3, null, -1, -1, i8, i7, null, drmInitData3, 0, str6);
                    }
                    readUnsignedShort = i9;
                    position = i10;
                    obj3 = obj2;
                    i6 = position + readUnsignedShort;
                    stsdData3 = stsdData2;
                    obj = obj3;
                    drmInitData2 = drmInitData3;
                    str3 = str4;
                    i13 = i3;
                } else {
                    parsableByteArray2.setPosition(i6 + 8);
                    stsdData3.format = Ac3Util.parseEAc3AnnexFFormat(parsableByteArray2, Integer.toString(i4), str6, drmInitData2);
                }
                obj2 = obj;
                str4 = str3;
                position = i6;
                drmInitData3 = drmInitData2;
                stsdData2 = stsdData3;
                obj3 = obj2;
                i6 = position + readUnsignedShort;
                stsdData3 = stsdData2;
                obj = obj3;
                drmInitData2 = drmInitData3;
                str3 = str4;
                i13 = i3;
            }
            obj2 = obj;
            str4 = str3;
            position = i6;
            drmInitData3 = drmInitData2;
            stsdData2 = stsdData3;
            if (readInt != Atom.TYPE_esds) {
                i6 = findEsdsPosition(parsableByteArray2, position, readUnsignedShort);
            } else {
                i6 = position;
            }
            if (i6 == -1) {
                str5 = str4;
                obj3 = obj2;
            } else {
                Pair parseEsdsFromParent2 = parseEsdsFromParent(parsableByteArray2, i6);
                str5 = (String) parseEsdsFromParent2.first;
                obj3 = (byte[]) parseEsdsFromParent2.second;
                if (MimeTypes.AUDIO_AAC.equals(str5)) {
                    Pair parseAacAudioSpecificConfig2 = CodecSpecificDataUtil.parseAacAudioSpecificConfig(obj3);
                    i7 = ((Integer) parseAacAudioSpecificConfig2.first).intValue();
                    i8 = ((Integer) parseAacAudioSpecificConfig2.second).intValue();
                }
            }
            str4 = str5;
            i6 = position + readUnsignedShort;
            stsdData3 = stsdData2;
            obj = obj3;
            drmInitData2 = drmInitData3;
            str3 = str4;
            i13 = i3;
        }
        obj2 = obj;
        str4 = str3;
        drmInitData3 = drmInitData2;
        stsdData2 = stsdData3;
        if (stsdData2.format == null) {
            str2 = str4;
            if (str2 != null) {
                if (MimeTypes.AUDIO_RAW.equals(str2)) {
                }
                String num2 = Integer.toString(i4);
                obj4 = obj2;
                if (obj4 != null) {
                    list = Collections.singletonList(obj4);
                } else {
                    list = null;
                }
                stsdData2.format = Format.createAudioSampleFormat(num2, str2, null, -1, -1, i8, i7, i11, list, drmInitData3, 0, str6);
            }
        }
    }

    private static int findEsdsPosition(ParsableByteArray parsableByteArray, int i, int i2) {
        int position = parsableByteArray.getPosition();
        while (position - i < i2) {
            parsableByteArray.setPosition(position);
            int readInt = parsableByteArray.readInt();
            Assertions.checkArgument(readInt > 0, "childAtomSize should be positive");
            if (parsableByteArray.readInt() == Atom.TYPE_esds) {
                return position;
            }
            position += readInt;
        }
        return -1;
    }

    private static Pair<String, byte[]> parseEsdsFromParent(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.setPosition((i + 8) + 4);
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        parsableByteArray.skipBytes(2);
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        if ((readUnsignedByte & 128) != 0) {
            parsableByteArray.skipBytes(2);
        }
        if ((readUnsignedByte & 64) != 0) {
            parsableByteArray.skipBytes(parsableByteArray.readUnsignedShort());
        }
        if ((readUnsignedByte & 32) != 0) {
            parsableByteArray.skipBytes(2);
        }
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        String mimeTypeFromMp4ObjectType = MimeTypes.getMimeTypeFromMp4ObjectType(parsableByteArray.readUnsignedByte());
        if (!(MimeTypes.AUDIO_MPEG.equals(mimeTypeFromMp4ObjectType) || MimeTypes.AUDIO_DTS.equals(mimeTypeFromMp4ObjectType))) {
            if (!MimeTypes.AUDIO_DTS_HD.equals(mimeTypeFromMp4ObjectType)) {
                parsableByteArray.skipBytes(12);
                parsableByteArray.skipBytes(1);
                i = parseExpandableClassSize(parsableByteArray);
                Object obj = new byte[i];
                parsableByteArray.readBytes(obj, 0, i);
                return Pair.create(mimeTypeFromMp4ObjectType, obj);
            }
        }
        return Pair.create(mimeTypeFromMp4ObjectType, null);
    }

    private static Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData(ParsableByteArray parsableByteArray, int i, int i2) {
        int position = parsableByteArray.getPosition();
        while (position - i < i2) {
            parsableByteArray.setPosition(position);
            int readInt = parsableByteArray.readInt();
            Assertions.checkArgument(readInt > 0, "childAtomSize should be positive");
            if (parsableByteArray.readInt() == Atom.TYPE_sinf) {
                Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent = parseCommonEncryptionSinfFromParent(parsableByteArray, position, readInt);
                if (parseCommonEncryptionSinfFromParent != null) {
                    return parseCommonEncryptionSinfFromParent;
                }
            }
            position += readInt;
        }
        return null;
    }

    static Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent(ParsableByteArray parsableByteArray, int i, int i2) {
        int i3 = i + 8;
        String str = null;
        Object obj = str;
        int i4 = -1;
        int i5 = 0;
        while (i3 - i < i2) {
            parsableByteArray.setPosition(i3);
            int readInt = parsableByteArray.readInt();
            int readInt2 = parsableByteArray.readInt();
            if (readInt2 == Atom.TYPE_frma) {
                obj = Integer.valueOf(parsableByteArray.readInt());
            } else if (readInt2 == Atom.TYPE_schm) {
                parsableByteArray.skipBytes(4);
                str = parsableByteArray.readString(4);
            } else if (readInt2 == Atom.TYPE_schi) {
                i4 = i3;
                i5 = readInt;
            }
            i3 += readInt;
        }
        if (C0649C.CENC_TYPE_cenc.equals(str) == 0 && C0649C.CENC_TYPE_cbc1.equals(str) == 0 && C0649C.CENC_TYPE_cens.equals(str) == 0) {
            if (C0649C.CENC_TYPE_cbcs.equals(str) == 0) {
                return null;
            }
        }
        i = 1;
        Assertions.checkArgument(obj != null ? 1 : 0, "frma atom is mandatory");
        Assertions.checkArgument(i4 != -1 ? 1 : 0, "schi atom is mandatory");
        parsableByteArray = parseSchiFromParent(parsableByteArray, i4, i5, str);
        if (parsableByteArray == null) {
            i = 0;
        }
        Assertions.checkArgument(i, "tenc atom is mandatory");
        return Pair.create(obj, parsableByteArray);
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parsableByteArray, int i, int i2, String str) {
        TrackEncryptionBox trackEncryptionBox;
        int i3;
        int i4;
        int i5 = i + 8;
        while (true) {
            trackEncryptionBox = null;
            if (i5 - i >= i2) {
                return null;
            }
            parsableByteArray.setPosition(i5);
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_tenc) {
                break;
            }
            i5 += readInt;
        }
        i = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(1);
        if (i == 0) {
            parsableByteArray.skipBytes(1);
            i3 = 0;
            i4 = 0;
        } else {
            i = parsableByteArray.readUnsignedByte();
            i4 = i & 15;
            i3 = (i & 240) >> 4;
        }
        boolean z = parsableByteArray.readUnsignedByte() == 1;
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        byte[] bArr = new byte[16];
        parsableByteArray.readBytes(bArr, 0, bArr.length);
        if (z && readUnsignedByte == 0) {
            i = parsableByteArray.readUnsignedByte();
            trackEncryptionBox = new byte[i];
            parsableByteArray.readBytes(trackEncryptionBox, 0, i);
        }
        return new TrackEncryptionBox(z, str, readUnsignedByte, bArr, i3, i4, trackEncryptionBox);
    }

    private static byte[] parseProjFromParent(ParsableByteArray parsableByteArray, int i, int i2) {
        int i3 = i + 8;
        while (i3 - i < i2) {
            parsableByteArray.setPosition(i3);
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == Atom.TYPE_proj) {
                return Arrays.copyOfRange(parsableByteArray.data, i3, readInt + i3);
            }
            i3 += readInt;
        }
        return null;
    }

    private static int parseExpandableClassSize(ParsableByteArray parsableByteArray) {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int i = readUnsignedByte & 127;
        while ((readUnsignedByte & 128) == 128) {
            readUnsignedByte = parsableByteArray.readUnsignedByte();
            i = (i << 7) | (readUnsignedByte & 127);
        }
        return i;
    }

    private AtomParsers() {
    }
}
