package com.google.android.exoplayer2.source.dash;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.InitializationChunk;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.IOException;

public final class DashUtil {
    public static DashManifest loadManifest(DataSource dataSource, Uri uri) throws IOException {
        ParsingLoadable parsingLoadable = new ParsingLoadable(dataSource, uri, 4, new DashManifestParser());
        parsingLoadable.load();
        return (DashManifest) parsingLoadable.getResult();
    }

    public static DrmInitData loadDrmInitData(DataSource dataSource, Period period) throws IOException, InterruptedException {
        int i = 2;
        Representation firstRepresentation = getFirstRepresentation(period, 2);
        if (firstRepresentation == null) {
            i = 1;
            firstRepresentation = getFirstRepresentation(period, 1);
            if (firstRepresentation == null) {
                return null;
            }
        }
        period = firstRepresentation.format;
        dataSource = loadSampleFormat(dataSource, i, firstRepresentation);
        if (dataSource == null) {
            dataSource = period.drmInitData;
        } else {
            dataSource = dataSource.copyWithManifestFormatInfo(period).drmInitData;
        }
        return dataSource;
    }

    public static Format loadSampleFormat(DataSource dataSource, int i, Representation representation) throws IOException, InterruptedException {
        dataSource = loadInitializationData(dataSource, i, representation, false);
        if (dataSource == null) {
            return null;
        }
        return dataSource.getSampleFormats()[0];
    }

    public static ChunkIndex loadChunkIndex(DataSource dataSource, int i, Representation representation) throws IOException, InterruptedException {
        dataSource = loadInitializationData(dataSource, i, representation, true);
        if (dataSource == null) {
            return null;
        }
        return (ChunkIndex) dataSource.getSeekMap();
    }

    private static ChunkExtractorWrapper loadInitializationData(DataSource dataSource, int i, Representation representation, boolean z) throws IOException, InterruptedException {
        RangedUri initializationUri = representation.getInitializationUri();
        if (initializationUri == null) {
            return null;
        }
        ChunkExtractorWrapper newWrappedExtractor = newWrappedExtractor(i, representation.format);
        if (z) {
            z = representation.getIndexUri();
            if (!z) {
                return null;
            }
            RangedUri attemptMerge = initializationUri.attemptMerge(z, representation.baseUrl);
            if (attemptMerge == null) {
                loadInitializationData(dataSource, representation, newWrappedExtractor, initializationUri);
                initializationUri = z;
            } else {
                initializationUri = attemptMerge;
            }
        }
        loadInitializationData(dataSource, representation, newWrappedExtractor, initializationUri);
        return newWrappedExtractor;
    }

    private static void loadInitializationData(DataSource dataSource, Representation representation, ChunkExtractorWrapper chunkExtractorWrapper, RangedUri rangedUri) throws IOException, InterruptedException {
        new InitializationChunk(dataSource, new DataSpec(rangedUri.resolveUri(representation.baseUrl), rangedUri.start, rangedUri.length, representation.getCacheKey()), representation.format, 0, null, chunkExtractorWrapper).load();
    }

    private static ChunkExtractorWrapper newWrappedExtractor(int i, Format format) {
        Object obj;
        String str = format.containerMimeType;
        if (!str.startsWith(MimeTypes.VIDEO_WEBM)) {
            if (!str.startsWith(MimeTypes.AUDIO_WEBM)) {
                obj = null;
                return new ChunkExtractorWrapper(obj == null ? new MatroskaExtractor() : new FragmentedMp4Extractor(), i, format);
            }
        }
        obj = 1;
        if (obj == null) {
        }
        return new ChunkExtractorWrapper(obj == null ? new MatroskaExtractor() : new FragmentedMp4Extractor(), i, format);
    }

    private static Representation getFirstRepresentation(Period period, int i) {
        i = period.getAdaptationSetIndex(i);
        Representation representation = null;
        if (i == -1) {
            return null;
        }
        period = ((AdaptationSet) period.adaptationSets.get(i)).representations;
        if (period.isEmpty() == 0) {
            representation = (Representation) period.get(0);
        }
        return representation;
    }

    private DashUtil() {
    }
}
