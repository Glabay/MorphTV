package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class FilteringSsManifestParser implements Parser<SsManifest> {
    private final List<TrackKey> filter;
    private final SsManifestParser ssManifestParser = new SsManifestParser();

    public FilteringSsManifestParser(List<TrackKey> list) {
        this.filter = list;
    }

    public SsManifest parse(Uri uri, InputStream inputStream) throws IOException {
        return this.ssManifestParser.parse(uri, inputStream).copy(this.filter);
    }
}
