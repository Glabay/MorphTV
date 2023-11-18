package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

abstract class TagPayloadReader {
    protected final TrackOutput output;

    public static final class UnsupportedFormatException extends ParserException {
        public UnsupportedFormatException(String str) {
            super(str);
        }
    }

    protected abstract boolean parseHeader(ParsableByteArray parsableByteArray) throws ParserException;

    protected abstract void parsePayload(ParsableByteArray parsableByteArray, long j) throws ParserException;

    public abstract void seek();

    protected TagPayloadReader(TrackOutput trackOutput) {
        this.output = trackOutput;
    }

    public final void consume(ParsableByteArray parsableByteArray, long j) throws ParserException {
        if (parseHeader(parsableByteArray)) {
            parsePayload(parsableByteArray, j);
        }
    }
}
