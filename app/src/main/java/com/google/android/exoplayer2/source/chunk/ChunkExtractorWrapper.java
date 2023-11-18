package com.google.android.exoplayer2.source.chunk;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

public final class ChunkExtractorWrapper implements ExtractorOutput {
    private final SparseArray<BindingTrackOutput> bindingTrackOutputs = new SparseArray();
    public final Extractor extractor;
    private boolean extractorInitialized;
    private final Format primaryTrackManifestFormat;
    private final int primaryTrackType;
    private Format[] sampleFormats;
    private SeekMap seekMap;
    private TrackOutputProvider trackOutputProvider;

    public interface TrackOutputProvider {
        TrackOutput track(int i, int i2);
    }

    private static final class BindingTrackOutput implements TrackOutput {
        private final int id;
        private final Format manifestFormat;
        public Format sampleFormat;
        private TrackOutput trackOutput;
        private final int type;

        public BindingTrackOutput(int i, int i2, Format format) {
            this.id = i;
            this.type = i2;
            this.manifestFormat = format;
        }

        public void bind(TrackOutputProvider trackOutputProvider) {
            if (trackOutputProvider == null) {
                this.trackOutput = new DummyTrackOutput();
                return;
            }
            this.trackOutput = trackOutputProvider.track(this.id, this.type);
            if (this.sampleFormat != null) {
                this.trackOutput.format(this.sampleFormat);
            }
        }

        public void format(Format format) {
            if (this.manifestFormat != null) {
                format = format.copyWithManifestFormatInfo(this.manifestFormat);
            }
            this.sampleFormat = format;
            this.trackOutput.format(this.sampleFormat);
        }

        public int sampleData(ExtractorInput extractorInput, int i, boolean z) throws IOException, InterruptedException {
            return this.trackOutput.sampleData(extractorInput, i, z);
        }

        public void sampleData(ParsableByteArray parsableByteArray, int i) {
            this.trackOutput.sampleData(parsableByteArray, i);
        }

        public void sampleMetadata(long j, int i, int i2, int i3, CryptoData cryptoData) {
            this.trackOutput.sampleMetadata(j, i, i2, i3, cryptoData);
        }
    }

    public ChunkExtractorWrapper(Extractor extractor, int i, Format format) {
        this.extractor = extractor;
        this.primaryTrackType = i;
        this.primaryTrackManifestFormat = format;
    }

    public SeekMap getSeekMap() {
        return this.seekMap;
    }

    public Format[] getSampleFormats() {
        return this.sampleFormats;
    }

    public void init(TrackOutputProvider trackOutputProvider) {
        this.trackOutputProvider = trackOutputProvider;
        if (this.extractorInitialized) {
            this.extractor.seek(0, 0);
            for (int i = 0; i < this.bindingTrackOutputs.size(); i++) {
                ((BindingTrackOutput) this.bindingTrackOutputs.valueAt(i)).bind(trackOutputProvider);
            }
            return;
        }
        this.extractor.init(this);
        this.extractorInitialized = true;
    }

    public TrackOutput track(int i, int i2) {
        TrackOutput trackOutput = (BindingTrackOutput) this.bindingTrackOutputs.get(i);
        if (trackOutput == null) {
            Assertions.checkState(this.sampleFormats == null);
            trackOutput = new BindingTrackOutput(i, i2, i2 == this.primaryTrackType ? this.primaryTrackManifestFormat : null);
            trackOutput.bind(this.trackOutputProvider);
            this.bindingTrackOutputs.put(i, trackOutput);
        }
        return trackOutput;
    }

    public void endTracks() {
        Format[] formatArr = new Format[this.bindingTrackOutputs.size()];
        for (int i = 0; i < this.bindingTrackOutputs.size(); i++) {
            formatArr[i] = ((BindingTrackOutput) this.bindingTrackOutputs.valueAt(i)).sampleFormat;
        }
        this.sampleFormats = formatArr;
    }

    public void seekMap(SeekMap seekMap) {
        this.seekMap = seekMap;
    }
}
