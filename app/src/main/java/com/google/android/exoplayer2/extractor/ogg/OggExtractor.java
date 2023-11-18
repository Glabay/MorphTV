package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

public class OggExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = new C06911();
    private static final int MAX_VERIFICATION_BYTES = 8;
    private ExtractorOutput output;
    private StreamReader streamReader;
    private boolean streamReaderInitialized;

    /* renamed from: com.google.android.exoplayer2.extractor.ogg.OggExtractor$1 */
    static class C06911 implements ExtractorsFactory {
        C06911() {
        }

        public Extractor[] createExtractors() {
            return new Extractor[]{new OggExtractor()};
        }
    }

    public void release() {
    }

    public boolean sniff(com.google.android.exoplayer2.extractor.ExtractorInput r1) throws java.io.IOException, java.lang.InterruptedException {
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
        r0 = this;
        r1 = r0.sniffInternal(r1);	 Catch:{ ParserException -> 0x0005 }
        return r1;
    L_0x0005:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.ogg.OggExtractor.sniff(com.google.android.exoplayer2.extractor.ExtractorInput):boolean");
    }

    public void init(ExtractorOutput extractorOutput) {
        this.output = extractorOutput;
    }

    public void seek(long j, long j2) {
        if (this.streamReader != null) {
            this.streamReader.seek(j, j2);
        }
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        if (this.streamReader == null) {
            if (sniffInternal(extractorInput)) {
                extractorInput.resetPeekPosition();
            } else {
                throw new ParserException("Failed to determine bitstream type");
            }
        }
        if (!this.streamReaderInitialized) {
            TrackOutput track = this.output.track(0, 1);
            this.output.endTracks();
            this.streamReader.init(this.output, track);
            this.streamReaderInitialized = true;
        }
        return this.streamReader.read(extractorInput, positionHolder);
    }

    private boolean sniffInternal(ExtractorInput extractorInput) throws IOException, InterruptedException {
        OggPageHeader oggPageHeader = new OggPageHeader();
        if (oggPageHeader.populate(extractorInput, true)) {
            if ((oggPageHeader.type & 2) == 2) {
                int min = Math.min(oggPageHeader.bodySize, 8);
                ParsableByteArray parsableByteArray = new ParsableByteArray(min);
                extractorInput.peekFully(parsableByteArray.data, 0, min);
                if (FlacReader.verifyBitstreamType(resetPosition(parsableByteArray)) != null) {
                    this.streamReader = new FlacReader();
                } else if (VorbisReader.verifyBitstreamType(resetPosition(parsableByteArray)) != null) {
                    this.streamReader = new VorbisReader();
                } else if (OpusReader.verifyBitstreamType(resetPosition(parsableByteArray)) == null) {
                    return false;
                } else {
                    this.streamReader = new OpusReader();
                }
                return true;
            }
        }
        return false;
    }

    private static ParsableByteArray resetPosition(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(0);
        return parsableByteArray;
    }
}
