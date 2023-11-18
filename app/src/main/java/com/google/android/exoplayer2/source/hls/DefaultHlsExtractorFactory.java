package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.util.Collections;
import java.util.List;

public final class DefaultHlsExtractorFactory implements HlsExtractorFactory {
    public static final String AAC_FILE_EXTENSION = ".aac";
    public static final String AC3_FILE_EXTENSION = ".ac3";
    public static final String EC3_FILE_EXTENSION = ".ec3";
    public static final String M4_FILE_EXTENSION_PREFIX = ".m4";
    public static final String MP3_FILE_EXTENSION = ".mp3";
    public static final String MP4_FILE_EXTENSION = ".mp4";
    public static final String MP4_FILE_EXTENSION_PREFIX = ".mp4";
    public static final String VTT_FILE_EXTENSION = ".vtt";
    public static final String WEBVTT_FILE_EXTENSION = ".webvtt";

    public Pair<Extractor, Boolean> createExtractor(Extractor extractor, Uri uri, Format format, List<Format> list, DrmInitData drmInitData, TimestampAdjuster timestampAdjuster) {
        uri = uri.getLastPathSegment();
        if (uri == null) {
            uri = "";
        }
        boolean z = false;
        if (!(MimeTypes.TEXT_VTT.equals(format.sampleMimeType) || uri.endsWith(WEBVTT_FILE_EXTENSION))) {
            if (!uri.endsWith(VTT_FILE_EXTENSION)) {
                if (uri.endsWith(AAC_FILE_EXTENSION)) {
                    extractor = new AdtsExtractor();
                } else {
                    if (!uri.endsWith(AC3_FILE_EXTENSION)) {
                        if (!uri.endsWith(EC3_FILE_EXTENSION)) {
                            if (uri.endsWith(MP3_FILE_EXTENSION)) {
                                extractor = new Mp3Extractor(0, null);
                            } else {
                                if (extractor == null) {
                                    if (uri.endsWith(".mp4") == null && uri.startsWith(M4_FILE_EXTENSION_PREFIX, uri.length() - 4) == null) {
                                        if (uri.startsWith(".mp4", uri.length() - 5) == null) {
                                            extractor = 16;
                                            if (list != null) {
                                                extractor = 48;
                                            } else {
                                                list = Collections.emptyList();
                                            }
                                            uri = format.codecs;
                                            if (TextUtils.isEmpty(uri) == null) {
                                                if (MimeTypes.AUDIO_AAC.equals(MimeTypes.getAudioMediaMimeType(uri)) == null) {
                                                    extractor |= 2;
                                                }
                                                if (MimeTypes.VIDEO_H264.equals(MimeTypes.getVideoMediaMimeType(uri)) == null) {
                                                    extractor |= 4;
                                                }
                                            }
                                            extractor = new TsExtractor(2, timestampAdjuster, new DefaultTsPayloadReaderFactory(extractor, list));
                                        }
                                    }
                                    if (list == null) {
                                        list = Collections.emptyList();
                                    }
                                    Extractor fragmentedMp4Extractor = new FragmentedMp4Extractor(0, timestampAdjuster, null, drmInitData, list);
                                }
                                return Pair.create(extractor, Boolean.valueOf(z));
                            }
                        }
                    }
                    extractor = new Ac3Extractor();
                }
                z = true;
                return Pair.create(extractor, Boolean.valueOf(z));
            }
        }
        extractor = new WebvttExtractor(format.language, timestampAdjuster);
        return Pair.create(extractor, Boolean.valueOf(z));
    }
}
