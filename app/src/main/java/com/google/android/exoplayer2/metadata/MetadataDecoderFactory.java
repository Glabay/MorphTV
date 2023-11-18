package com.google.android.exoplayer2.metadata;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.metadata.emsg.EventMessageDecoder;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.scte35.SpliceInfoDecoder;
import com.google.android.exoplayer2.util.MimeTypes;

public interface MetadataDecoderFactory {
    public static final MetadataDecoderFactory DEFAULT = new C07021();

    /* renamed from: com.google.android.exoplayer2.metadata.MetadataDecoderFactory$1 */
    static class C07021 implements MetadataDecoderFactory {
        C07021() {
        }

        public boolean supportsFormat(Format format) {
            format = format.sampleMimeType;
            if (!(MimeTypes.APPLICATION_ID3.equals(format) || MimeTypes.APPLICATION_EMSG.equals(format))) {
                if (MimeTypes.APPLICATION_SCTE35.equals(format) == null) {
                    return null;
                }
            }
            return true;
        }

        public MetadataDecoder createDecoder(Format format) {
            format = format.sampleMimeType;
            int hashCode = format.hashCode();
            if (hashCode != -1248341703) {
                if (hashCode != 1154383568) {
                    if (hashCode == 1652648887) {
                        if (format.equals(MimeTypes.APPLICATION_SCTE35) != null) {
                            format = 2;
                            switch (format) {
                                case null:
                                    return new Id3Decoder();
                                case 1:
                                    return new EventMessageDecoder();
                                case 2:
                                    return new SpliceInfoDecoder();
                                default:
                                    throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
                            }
                        }
                    }
                } else if (format.equals(MimeTypes.APPLICATION_EMSG) != null) {
                    format = true;
                    switch (format) {
                        case null:
                            return new Id3Decoder();
                        case 1:
                            return new EventMessageDecoder();
                        case 2:
                            return new SpliceInfoDecoder();
                        default:
                            throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
                    }
                }
            } else if (format.equals(MimeTypes.APPLICATION_ID3) != null) {
                format = null;
                switch (format) {
                    case null:
                        return new Id3Decoder();
                    case 1:
                        return new EventMessageDecoder();
                    case 2:
                        return new SpliceInfoDecoder();
                    default:
                        throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
                }
            }
            format = -1;
            switch (format) {
                case null:
                    return new Id3Decoder();
                case 1:
                    return new EventMessageDecoder();
                case 2:
                    return new SpliceInfoDecoder();
                default:
                    throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
            }
        }
    }

    MetadataDecoder createDecoder(Format format);

    boolean supportsFormat(Format format);
}
