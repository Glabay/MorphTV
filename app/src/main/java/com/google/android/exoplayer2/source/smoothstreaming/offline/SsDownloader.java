package com.google.android.exoplayer2.source.smoothstreaming.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.SegmentDownloader;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest.StreamElement;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsUtil;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.TrackKey;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SsDownloader extends SegmentDownloader<SsManifest, TrackKey> {
    public SsDownloader(Uri uri, DownloaderConstructorHelper downloaderConstructorHelper) {
        super(SsUtil.fixManifestUri(uri), downloaderConstructorHelper);
    }

    public TrackKey[] getAllRepresentationKeys() throws IOException {
        ArrayList arrayList = new ArrayList();
        SsManifest ssManifest = (SsManifest) getManifest();
        for (int i = 0; i < ssManifest.streamElements.length; i++) {
            StreamElement streamElement = ssManifest.streamElements[i];
            for (int i2 = 0; i2 < streamElement.formats.length; i2++) {
                arrayList.add(new TrackKey(i, i2));
            }
        }
        return (TrackKey[]) arrayList.toArray(new TrackKey[arrayList.size()]);
    }

    protected SsManifest getManifest(DataSource dataSource, Uri uri) throws IOException {
        ParsingLoadable parsingLoadable = new ParsingLoadable(dataSource, uri, 4, new SsManifestParser());
        parsingLoadable.load();
        return (SsManifest) parsingLoadable.getResult();
    }

    protected List<Segment> getSegments(DataSource dataSource, SsManifest ssManifest, TrackKey[] trackKeyArr, boolean z) throws InterruptedException, IOException {
        dataSource = new ArrayList();
        for (TrackKey trackKey : trackKeyArr) {
            StreamElement streamElement = ssManifest.streamElements[trackKey.streamElementIndex];
            for (int i = 0; i < streamElement.chunkCount; i++) {
                dataSource.add(new Segment(streamElement.getStartTimeUs(i), new DataSpec(streamElement.buildRequestUri(trackKey.trackIndex, i))));
            }
        }
        return dataSource;
    }
}
