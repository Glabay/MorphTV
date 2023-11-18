package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.text.cea.CeaUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

final class SeiReader {
    private final List<Format> closedCaptionFormats;
    private final TrackOutput[] outputs;

    public SeiReader(List<Format> list) {
        this.closedCaptionFormats = list;
        this.outputs = new TrackOutput[list.size()];
    }

    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator trackIdGenerator) {
        for (int i = 0; i < this.outputs.length; i++) {
            boolean z;
            StringBuilder stringBuilder;
            trackIdGenerator.generateNewId();
            TrackOutput track = extractorOutput.track(trackIdGenerator.getTrackId(), 3);
            Format format = (Format) this.closedCaptionFormats.get(i);
            String str = format.sampleMimeType;
            if (!MimeTypes.APPLICATION_CEA608.equals(str)) {
                if (!MimeTypes.APPLICATION_CEA708.equals(str)) {
                    z = false;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid closed caption mime type provided: ");
                    stringBuilder.append(str);
                    Assertions.checkArgument(z, stringBuilder.toString());
                    track.format(Format.createTextSampleFormat(format.id == null ? format.id : trackIdGenerator.getFormatId(), str, null, -1, format.selectionFlags, format.language, format.accessibilityChannel, null));
                    this.outputs[i] = track;
                }
            }
            z = true;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid closed caption mime type provided: ");
            stringBuilder.append(str);
            Assertions.checkArgument(z, stringBuilder.toString());
            if (format.id == null) {
            }
            track.format(Format.createTextSampleFormat(format.id == null ? format.id : trackIdGenerator.getFormatId(), str, null, -1, format.selectionFlags, format.language, format.accessibilityChannel, null));
            this.outputs[i] = track;
        }
    }

    public void consume(long j, ParsableByteArray parsableByteArray) {
        CeaUtil.consume(j, parsableByteArray, this.outputs);
    }
}
