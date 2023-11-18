package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Mp3Extractor implements Extractor {
    public static final ExtractorsFactory FACTORY = new C06861();
    public static final int FLAG_DISABLE_ID3_METADATA = 2;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    private static final int MAX_SNIFF_BYTES = 16384;
    private static final int MAX_SYNC_BYTES = 131072;
    private static final int MPEG_AUDIO_HEADER_MASK = -128000;
    private static final int SCRATCH_LENGTH = 10;
    private static final int SEEK_HEADER_INFO = Util.getIntegerCodeForString("Info");
    private static final int SEEK_HEADER_UNSET = 0;
    private static final int SEEK_HEADER_VBRI = Util.getIntegerCodeForString("VBRI");
    private static final int SEEK_HEADER_XING = Util.getIntegerCodeForString("Xing");
    private long basisTimeUs;
    private ExtractorOutput extractorOutput;
    private final int flags;
    private final long forcedFirstSampleTimestampUs;
    private final GaplessInfoHolder gaplessInfoHolder;
    private Metadata metadata;
    private int sampleBytesRemaining;
    private long samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;

    interface Seeker extends SeekMap {
        long getTimeUs(long j);
    }

    /* renamed from: com.google.android.exoplayer2.extractor.mp3.Mp3Extractor$1 */
    static class C06861 implements ExtractorsFactory {
        C06861() {
        }

        public Extractor[] createExtractors() {
            return new Extractor[]{new Mp3Extractor()};
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    private static boolean headersMatch(int i, long j) {
        return ((long) (i & MPEG_AUDIO_HEADER_MASK)) == (j & -128000);
    }

    public void release() {
    }

    public Mp3Extractor() {
        this(0);
    }

    public Mp3Extractor(int i) {
        this(i, C0649C.TIME_UNSET);
    }

    public Mp3Extractor(int i, long j) {
        this.flags = i;
        this.forcedFirstSampleTimestampUs = j;
        this.scratch = new ParsableByteArray((int) 10);
        this.synchronizedHeader = new MpegAudioHeader();
        this.gaplessInfoHolder = new GaplessInfoHolder();
        this.basisTimeUs = 1;
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return synchronize(extractorInput, true);
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
    }

    public void seek(long j, long j2) {
        this.synchronizedHeaderData = 0;
        this.basisTimeUs = C0649C.TIME_UNSET;
        this.samplesRead = 0;
        this.sampleBytesRemaining = 0;
    }

    public int read(com.google.android.exoplayer2.extractor.ExtractorInput r20, com.google.android.exoplayer2.extractor.PositionHolder r21) throws java.io.IOException, java.lang.InterruptedException {
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
        r19 = this;
        r0 = r19;
        r1 = r0.synchronizedHeaderData;
        if (r1 != 0) goto L_0x000f;
    L_0x0006:
        r1 = 0;
        r2 = r20;
        r0.synchronize(r2, r1);	 Catch:{ EOFException -> 0x000d }
        goto L_0x0011;
    L_0x000d:
        r1 = -1;
        return r1;
    L_0x000f:
        r2 = r20;
    L_0x0011:
        r1 = r0.seeker;
        if (r1 != 0) goto L_0x006f;
    L_0x0015:
        r1 = r19.maybeReadSeekFrame(r20);
        r0.seeker = r1;
        r1 = r0.seeker;
        if (r1 == 0) goto L_0x002d;
    L_0x001f:
        r1 = r0.seeker;
        r1 = r1.isSeekable();
        if (r1 != 0) goto L_0x0033;
    L_0x0027:
        r1 = r0.flags;
        r1 = r1 & 1;
        if (r1 == 0) goto L_0x0033;
    L_0x002d:
        r1 = r19.getConstantBitrateSeeker(r20);
        r0.seeker = r1;
    L_0x0033:
        r1 = r0.extractorOutput;
        r3 = r0.seeker;
        r1.seekMap(r3);
        r1 = r0.trackOutput;
        r3 = 0;
        r4 = r0.synchronizedHeader;
        r4 = r4.mimeType;
        r5 = 0;
        r6 = -1;
        r7 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r8 = r0.synchronizedHeader;
        r8 = r8.channels;
        r9 = r0.synchronizedHeader;
        r9 = r9.sampleRate;
        r10 = -1;
        r11 = r0.gaplessInfoHolder;
        r11 = r11.encoderDelay;
        r12 = r0.gaplessInfoHolder;
        r12 = r12.encoderPadding;
        r13 = 0;
        r14 = 0;
        r16 = 0;
        r15 = r0.flags;
        r15 = r15 & 2;
        if (r15 == 0) goto L_0x0064;
    L_0x0060:
        r15 = 0;
    L_0x0061:
        r17 = r15;
        goto L_0x0067;
    L_0x0064:
        r15 = r0.metadata;
        goto L_0x0061;
    L_0x0067:
        r15 = 0;
        r3 = com.google.android.exoplayer2.Format.createAudioSampleFormat(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);
        r1.format(r3);
    L_0x006f:
        r1 = r19.readSample(r20);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp3.Mp3Extractor.read(com.google.android.exoplayer2.extractor.ExtractorInput, com.google.android.exoplayer2.extractor.PositionHolder):int");
    }

    private int readSample(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.sampleBytesRemaining == 0) {
            extractorInput.resetPeekPosition();
            if (!extractorInput.peekFully(this.scratch.data, 0, 4, true)) {
                return -1;
            }
            this.scratch.setPosition(0);
            int readInt = this.scratch.readInt();
            if (headersMatch(readInt, (long) this.synchronizedHeaderData)) {
                if (MpegAudioHeader.getFrameSize(readInt) != -1) {
                    MpegAudioHeader.populateHeader(readInt, this.synchronizedHeader);
                    if (this.basisTimeUs == C0649C.TIME_UNSET) {
                        this.basisTimeUs = this.seeker.getTimeUs(extractorInput.getPosition());
                        if (this.forcedFirstSampleTimestampUs != C0649C.TIME_UNSET) {
                            this.basisTimeUs += this.forcedFirstSampleTimestampUs - this.seeker.getTimeUs(0);
                        }
                    }
                    this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
                }
            }
            extractorInput.skipFully(1);
            this.synchronizedHeaderData = 0;
            return 0;
        }
        extractorInput = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
        if (extractorInput == -1) {
            return -1;
        }
        this.sampleBytesRemaining -= extractorInput;
        if (this.sampleBytesRemaining > null) {
            return 0;
        }
        this.trackOutput.sampleMetadata(this.basisTimeUs + ((this.samplesRead * C0649C.MICROS_PER_SECOND) / ((long) this.synchronizedHeader.sampleRate)), 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += (long) this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean synchronize(com.google.android.exoplayer2.extractor.ExtractorInput r13, boolean r14) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r12 = this;
        if (r14 == 0) goto L_0x0005;
    L_0x0002:
        r0 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        goto L_0x0007;
    L_0x0005:
        r0 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
    L_0x0007:
        r13.resetPeekPosition();
        r1 = r13.getPosition();
        r3 = 0;
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        r1 = 0;
        if (r5 != 0) goto L_0x0027;
    L_0x0015:
        r12.peekId3Data(r13);
        r2 = r13.getPeekPosition();
        r2 = (int) r2;
        if (r14 != 0) goto L_0x0022;
    L_0x001f:
        r13.skipFully(r2);
    L_0x0022:
        r5 = r2;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        goto L_0x002b;
    L_0x0027:
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
    L_0x002b:
        r6 = r12.scratch;
        r6 = r6.data;
        r7 = 1;
        if (r2 <= 0) goto L_0x0034;
    L_0x0032:
        r8 = 1;
        goto L_0x0035;
    L_0x0034:
        r8 = 0;
    L_0x0035:
        r9 = 4;
        r6 = r13.peekFully(r6, r1, r9, r8);
        if (r6 != 0) goto L_0x003d;
    L_0x003c:
        goto L_0x0086;
    L_0x003d:
        r6 = r12.scratch;
        r6.setPosition(r1);
        r6 = r12.scratch;
        r6 = r6.readInt();
        if (r3 == 0) goto L_0x0051;
    L_0x004a:
        r10 = (long) r3;
        r8 = headersMatch(r6, r10);
        if (r8 == 0) goto L_0x0058;
    L_0x0051:
        r8 = com.google.android.exoplayer2.extractor.MpegAudioHeader.getFrameSize(r6);
        r10 = -1;
        if (r8 != r10) goto L_0x0079;
    L_0x0058:
        r2 = r4 + 1;
        if (r4 != r0) goto L_0x0067;
    L_0x005c:
        if (r14 != 0) goto L_0x0066;
    L_0x005e:
        r13 = new com.google.android.exoplayer2.ParserException;
        r14 = "Searched too many bytes.";
        r13.<init>(r14);
        throw r13;
    L_0x0066:
        return r1;
    L_0x0067:
        if (r14 == 0) goto L_0x0072;
    L_0x0069:
        r13.resetPeekPosition();
        r3 = r5 + r2;
        r13.advancePeekPosition(r3);
        goto L_0x0075;
    L_0x0072:
        r13.skipFully(r7);
    L_0x0075:
        r4 = r2;
        r2 = 0;
        r3 = 0;
        goto L_0x002b;
    L_0x0079:
        r2 = r2 + 1;
        if (r2 != r7) goto L_0x0084;
    L_0x007d:
        r3 = r12.synchronizedHeader;
        com.google.android.exoplayer2.extractor.MpegAudioHeader.populateHeader(r6, r3);
        r3 = r6;
        goto L_0x0093;
    L_0x0084:
        if (r2 != r9) goto L_0x0093;
    L_0x0086:
        if (r14 == 0) goto L_0x008d;
    L_0x0088:
        r5 = r5 + r4;
        r13.skipFully(r5);
        goto L_0x0090;
    L_0x008d:
        r13.resetPeekPosition();
    L_0x0090:
        r12.synchronizedHeaderData = r3;
        return r7;
    L_0x0093:
        r8 = r8 + -4;
        r13.advancePeekPosition(r8);
        goto L_0x002b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp3.Mp3Extractor.synchronize(com.google.android.exoplayer2.extractor.ExtractorInput, boolean):boolean");
    }

    private void peekId3Data(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int i = 0;
        while (true) {
            extractorInput.peekFully(this.scratch.data, 0, 10);
            this.scratch.setPosition(0);
            if (this.scratch.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
                extractorInput.resetPeekPosition();
                extractorInput.advancePeekPosition(i);
                return;
            }
            this.scratch.skipBytes(3);
            int readSynchSafeInt = this.scratch.readSynchSafeInt();
            int i2 = readSynchSafeInt + 10;
            if (this.metadata == null) {
                Object obj = new byte[i2];
                System.arraycopy(this.scratch.data, 0, obj, 0, 10);
                extractorInput.peekFully(obj, 10, readSynchSafeInt);
                this.metadata = new Id3Decoder((this.flags & 2) != 0 ? GaplessInfoHolder.GAPLESS_INFO_ID3_FRAME_PREDICATE : null).decode(obj, i2);
                if (this.metadata != null) {
                    this.gaplessInfoHolder.setFromMetadata(this.metadata);
                }
            } else {
                extractorInput.advancePeekPosition(readSynchSafeInt);
            }
            i += i2;
        }
    }

    private Seeker maybeReadSeekFrame(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int i;
        int seekFrameHeader;
        Seeker create;
        ParsableByteArray parsableByteArray = new ParsableByteArray(this.synchronizedHeader.frameSize);
        extractorInput.peekFully(parsableByteArray.data, 0, this.synchronizedHeader.frameSize);
        if ((this.synchronizedHeader.version & 1) != 0) {
            if (this.synchronizedHeader.channels != 1) {
                i = 36;
                seekFrameHeader = getSeekFrameHeader(parsableByteArray, i);
                if (seekFrameHeader != SEEK_HEADER_XING) {
                    if (seekFrameHeader == SEEK_HEADER_INFO) {
                        if (seekFrameHeader == SEEK_HEADER_VBRI) {
                            create = VbriSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
                            extractorInput.skipFully(this.synchronizedHeader.frameSize);
                        } else {
                            create = null;
                            extractorInput.resetPeekPosition();
                        }
                        return create;
                    }
                }
                create = XingSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
                if (!(create == null || this.gaplessInfoHolder.hasGaplessInfo())) {
                    extractorInput.resetPeekPosition();
                    extractorInput.advancePeekPosition(i + 141);
                    extractorInput.peekFully(this.scratch.data, 0, 3);
                    this.scratch.setPosition(0);
                    this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
                }
                extractorInput.skipFully(this.synchronizedHeader.frameSize);
                if (!(create == null || create.isSeekable() || seekFrameHeader != SEEK_HEADER_INFO)) {
                    return getConstantBitrateSeeker(extractorInput);
                }
                return create;
            }
        } else if (this.synchronizedHeader.channels == 1) {
            i = 13;
            seekFrameHeader = getSeekFrameHeader(parsableByteArray, i);
            if (seekFrameHeader != SEEK_HEADER_XING) {
                if (seekFrameHeader == SEEK_HEADER_INFO) {
                    if (seekFrameHeader == SEEK_HEADER_VBRI) {
                        create = null;
                        extractorInput.resetPeekPosition();
                    } else {
                        create = VbriSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
                        extractorInput.skipFully(this.synchronizedHeader.frameSize);
                    }
                    return create;
                }
            }
            create = XingSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
            extractorInput.resetPeekPosition();
            extractorInput.advancePeekPosition(i + 141);
            extractorInput.peekFully(this.scratch.data, 0, 3);
            this.scratch.setPosition(0);
            this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
            extractorInput.skipFully(this.synchronizedHeader.frameSize);
            return getConstantBitrateSeeker(extractorInput);
        }
        i = 21;
        seekFrameHeader = getSeekFrameHeader(parsableByteArray, i);
        if (seekFrameHeader != SEEK_HEADER_XING) {
            if (seekFrameHeader == SEEK_HEADER_INFO) {
                if (seekFrameHeader == SEEK_HEADER_VBRI) {
                    create = VbriSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
                    extractorInput.skipFully(this.synchronizedHeader.frameSize);
                } else {
                    create = null;
                    extractorInput.resetPeekPosition();
                }
                return create;
            }
        }
        create = XingSeeker.create(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader, parsableByteArray);
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(i + 141);
        extractorInput.peekFully(this.scratch.data, 0, 3);
        this.scratch.setPosition(0);
        this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
        extractorInput.skipFully(this.synchronizedHeader.frameSize);
        return getConstantBitrateSeeker(extractorInput);
    }

    private Seeker getConstantBitrateSeeker(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
        return new ConstantBitrateSeeker(extractorInput.getLength(), extractorInput.getPosition(), this.synchronizedHeader);
    }

    private static int getSeekFrameHeader(ParsableByteArray parsableByteArray, int i) {
        if (parsableByteArray.limit() >= i + 4) {
            parsableByteArray.setPosition(i);
            i = parsableByteArray.readInt();
            if (i == SEEK_HEADER_XING || i == SEEK_HEADER_INFO) {
                return i;
            }
        }
        if (parsableByteArray.limit() >= 40) {
            parsableByteArray.setPosition(36);
            if (parsableByteArray.readInt() == SEEK_HEADER_VBRI) {
                return SEEK_HEADER_VBRI;
            }
        }
        return null;
    }
}
