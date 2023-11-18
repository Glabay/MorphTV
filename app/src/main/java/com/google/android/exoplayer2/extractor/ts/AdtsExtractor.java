package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class AdtsExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = new C06941();
    private static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private static final int MAX_PACKET_SIZE = 200;
    private static final int MAX_SNIFF_BYTES = 8192;
    private final long firstSampleTimestampUs;
    private final ParsableByteArray packetBuffer;
    private final AdtsReader reader;
    private boolean startedPacket;

    /* renamed from: com.google.android.exoplayer2.extractor.ts.AdtsExtractor$1 */
    static class C06941 implements ExtractorsFactory {
        C06941() {
        }

        public Extractor[] createExtractors() {
            return new Extractor[]{new AdtsExtractor()};
        }
    }

    public void release() {
    }

    public AdtsExtractor() {
        this(0);
    }

    public AdtsExtractor(long j) {
        this.firstSampleTimestampUs = j;
        this.reader = new AdtsReader(true);
        this.packetBuffer = new ParsableByteArray(200);
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        ParsableByteArray parsableByteArray = new ParsableByteArray(10);
        ParsableBitArray parsableBitArray = new ParsableBitArray(parsableByteArray.data);
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
            int i3 = 0;
            while (true) {
                extractorInput.peekFully(parsableByteArray.data, 0, 2);
                parsableByteArray.setPosition(0);
                if ((parsableByteArray.readUnsignedShort() & 65526) != 65520) {
                    break;
                }
                readSynchSafeInt++;
                if (readSynchSafeInt >= 4 && i3 > 188) {
                    return true;
                }
                extractorInput.peekFully(parsableByteArray.data, 0, 4);
                parsableBitArray.setPosition(14);
                int readBits = parsableBitArray.readBits(13);
                if (readBits <= 6) {
                    return false;
                }
                extractorInput.advancePeekPosition(readBits - 6);
                i3 += readBits;
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
        extractorInput = extractorInput.read(this.packetBuffer.data, 0, 200);
        if (extractorInput == -1) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(extractorInput);
        if (this.startedPacket == null) {
            this.reader.packetStarted(this.firstSampleTimestampUs, true);
            this.startedPacket = true;
        }
        this.reader.consume(this.packetBuffer);
        return 0;
    }
}
