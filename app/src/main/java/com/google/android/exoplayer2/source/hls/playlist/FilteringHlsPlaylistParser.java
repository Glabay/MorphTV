package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class FilteringHlsPlaylistParser implements Parser<HlsPlaylist> {
    private final List<String> filter;
    private final HlsPlaylistParser hlsPlaylistParser = new HlsPlaylistParser();

    public FilteringHlsPlaylistParser(List<String> list) {
        this.filter = list;
    }

    public HlsPlaylist parse(Uri uri, InputStream inputStream) throws IOException {
        uri = this.hlsPlaylistParser.parse(uri, inputStream);
        return (uri instanceof HlsMasterPlaylist) != null ? ((HlsMasterPlaylist) uri).copy(this.filter) : uri;
    }
}
