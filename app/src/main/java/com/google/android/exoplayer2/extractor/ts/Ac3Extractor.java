package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class Ac3Extractor implements Extractor {
    private static final int AC3_SYNC_WORD = 2935;
    public static final ExtractorsFactory FACTORY = new C06931();
    private static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private static final int MAX_SNIFF_BYTES = 8192;
    private static final int MAX_SYNC_FRAME_SIZE = 2786;
    private final long firstSampleTimestampUs;
    private final Ac3Reader reader;
    private final ParsableByteArray sampleData;
    private boolean startedPacket;

    /* renamed from: com.google.android.exoplayer2.extractor.ts.Ac3Extractor$1 */
    static class C06931 implements ExtractorsFactory {
        C06931() {
        }

        public Extractor[] createExtractors() {
            return new Extractor[]{new Ac3Extractor()};
        }
    }

    public void release() {
    }

    public Ac3Extractor() {
        this(0);
    }

    public Ac3Extractor(long j) {
        this.firstSampleTimestampUs = j;
        this.reader = new Ac3Reader();
        this.sampleData = new ParsableByteArray((int) MAX_SYNC_FRAME_SIZE);
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        ParsableByteArray parsableByteArray = new ParsableByteArray(10);
        int i = 0;
        while (true) {
            extractorInput.peekFully(parsableByteArray.data, 0, 10);
            parsableByteArray.setPosition(0);
            if (parsableByteArray.readUnsignedInt24() != ID3_TAG) {
                break;
            }
            parsableByteArray.skipBytes(3);
            int readSynchSafeInt = parsableByteArray.readSynchSafeInt();
            i += readSynchSafeInt + 10;
            extractorInput.advancePeekPosition(readSynchSafeInt);
        }
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(i);
        int i2 = i;
        while (true) {
            readSynchSafeInt = 0;
            while (true) {
                extractorInput.peekFully(parsableByteArray.data, 0, 5);
                parsableByteArray.setPosition(0);
                if (parsableByteArray.readUnsignedShort() != AC3_SYNC_WORD) {
                    break;
                }
                readSynchSafeInt++;
                if (readSynchSafeInt >= 4) {
                    return true;
                }
                int parseAc3SyncframeSize = Ac3Util.parseAc3SyncframeSize(parsableByteArray.data);
                if (parseAc3SyncframeSize == -1) {
                    return false;
                }
                extractorInput.advancePeekPosition(parseAc3SyncframeSize - 5);
            }
            extractorInput.resetPeekPosition();
            i2++;
            if (i2 - i >= 8192) {
                return false;
            }
            extractorInput.advancePeekPosition(i2);
        }
    }

    public void init(ExtractorOutput extractorOutput) {
        this.reader.createTracks(extractorOutput, new TrackIdGenerator(0, 1));
        extractorOutput.endTracks();
        extractorOutput.seekMap(new Unseekable(C0649C.TIME_UNSET));
    }

    public void seek(long j, long j2) {
        this.startedPacket = 0;
        this.reader.seek();
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        extractorInput = extractorInput.read(this.sampleData.data, 0, MAX_SYNC_FRAME_SIZE);
        if (extractorInput == -1) {
            return -1;
        }
        this.sampleData.setPosition(0);
        this.sampleData.setLimit(extractorInput);
        if (this.startedPacket == null) {
            this.reader.packetStarted(this.firstSampleTimestampUs, true);
            this.startedPacket = true;
        }
        this.reader.consume(this.sampleData);
        return 0;
    }
}
