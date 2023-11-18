package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.extractor.flv.FlvExtractor;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.extractor.ogg.OggExtractor;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.extractor.wav.WavExtractor;
import java.lang.reflect.Constructor;

public final class DefaultExtractorsFactory implements ExtractorsFactory {
    private static final Constructor<? extends Extractor> FLAC_EXTRACTOR_CONSTRUCTOR;
    private int fragmentedMp4Flags;
    private int matroskaFlags;
    private int mp3Flags;
    private int mp4Flags;
    private int tsFlags;
    private int tsMode = 1;

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
        r0 = "com.google.android.exoplayer2.ext.flac.FlacExtractor";	 Catch:{ ClassNotFoundException -> 0x001d, Exception -> 0x0014 }
        r0 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x001d, Exception -> 0x0014 }
        r1 = com.google.android.exoplayer2.extractor.Extractor.class;	 Catch:{ ClassNotFoundException -> 0x001d, Exception -> 0x0014 }
        r0 = r0.asSubclass(r1);	 Catch:{ ClassNotFoundException -> 0x001d, Exception -> 0x0014 }
        r1 = 0;	 Catch:{ ClassNotFoundException -> 0x001d, Exception -> 0x0014 }
        r1 = new java.lang.Class[r1];	 Catch:{ ClassNotFoundException -> 0x001d, Exception -> 0x0014 }
        r0 = r0.getConstructor(r1);	 Catch:{ ClassNotFoundException -> 0x001d, Exception -> 0x0014 }
        goto L_0x001e;
    L_0x0014:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r2 = "Error instantiating FLAC extension";
        r1.<init>(r2, r0);
        throw r1;
    L_0x001d:
        r0 = 0;
    L_0x001e:
        FLAC_EXTRACTOR_CONSTRUCTOR = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.DefaultExtractorsFactory.<clinit>():void");
    }

    public synchronized DefaultExtractorsFactory setMatroskaExtractorFlags(int i) {
        this.matroskaFlags = i;
        return this;
    }

    public synchronized DefaultExtractorsFactory setMp4ExtractorFlags(int i) {
        this.mp4Flags = i;
        return this;
    }

    public synchronized DefaultExtractorsFactory setFragmentedMp4ExtractorFlags(int i) {
        this.fragmentedMp4Flags = i;
        return this;
    }

    public synchronized DefaultExtractorsFactory setMp3ExtractorFlags(int i) {
        this.mp3Flags = i;
        return this;
    }

    public synchronized DefaultExtractorsFactory setTsExtractorMode(int i) {
        this.tsMode = i;
        return this;
    }

    public synchronized DefaultExtractorsFactory setTsExtractorFlags(int i) {
        this.tsFlags = i;
        return this;
    }

    public synchronized Extractor[] createExtractors() {
        Extractor[] extractorArr;
        extractorArr = new Extractor[(FLAC_EXTRACTOR_CONSTRUCTOR == null ? 11 : 12)];
        extractorArr[0] = new MatroskaExtractor(this.matroskaFlags);
        extractorArr[1] = new FragmentedMp4Extractor(this.fragmentedMp4Flags);
        extractorArr[2] = new Mp4Extractor(this.mp4Flags);
        extractorArr[3] = new Mp3Extractor(this.mp3Flags);
        extractorArr[4] = new AdtsExtractor();
        extractorArr[5] = new Ac3Extractor();
        extractorArr[6] = new TsExtractor(this.tsMode, this.tsFlags);
        extractorArr[7] = new FlvExtractor();
        extractorArr[8] = new OggExtractor();
        extractorArr[9] = new PsExtractor();
        extractorArr[10] = new WavExtractor();
        if (FLAC_EXTRACTOR_CONSTRUCTOR != null) {
            try {
                extractorArr[11] = (Extractor) FLAC_EXTRACTOR_CONSTRUCTOR.newInstance(new Object[0]);
            } catch (Throwable e) {
                throw new IllegalStateException("Unexpected error creating FLAC extractor", e);
            }
        }
        return extractorArr;
    }
}
