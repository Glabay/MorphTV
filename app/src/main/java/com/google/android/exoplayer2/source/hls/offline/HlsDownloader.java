package com.google.android.exoplayer2.source.hls.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.SegmentDownloader;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.UriUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class HlsDownloader extends SegmentDownloader<HlsMasterPlaylist, String> {
    public HlsDownloader(Uri uri, DownloaderConstructorHelper downloaderConstructorHelper) {
        super(uri, downloaderConstructorHelper);
    }

    public String[] getAllRepresentationKeys() throws IOException {
        ArrayList arrayList = new ArrayList();
        HlsMasterPlaylist hlsMasterPlaylist = (HlsMasterPlaylist) getManifest();
        extractUrls(hlsMasterPlaylist.variants, arrayList);
        extractUrls(hlsMasterPlaylist.audios, arrayList);
        extractUrls(hlsMasterPlaylist.subtitles, arrayList);
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    protected HlsMasterPlaylist getManifest(DataSource dataSource, Uri uri) throws IOException {
        dataSource = loadManifest(dataSource, uri);
        if ((dataSource instanceof HlsMasterPlaylist) != null) {
            return (HlsMasterPlaylist) dataSource;
        }
        return HlsMasterPlaylist.createSingleVariantMasterPlaylist(dataSource.baseUri);
    }

    protected List<Segment> getSegments(DataSource dataSource, HlsMasterPlaylist hlsMasterPlaylist, String[] strArr, boolean z) throws InterruptedException, IOException {
        HashSet hashSet = new HashSet();
        List arrayList = new ArrayList();
        for (String resolveToUri : strArr) {
            HlsMediaPlaylist hlsMediaPlaylist = null;
            Uri resolveToUri2 = UriUtil.resolveToUri(hlsMasterPlaylist.baseUri, resolveToUri);
            try {
                hlsMediaPlaylist = (HlsMediaPlaylist) loadManifest(dataSource, resolveToUri2);
            } catch (IOException e) {
                if (!z) {
                    throw e;
                }
            }
            arrayList.add(new Segment(hlsMediaPlaylist != null ? hlsMediaPlaylist.startTimeUs : Long.MIN_VALUE, new DataSpec(resolveToUri2)));
            if (hlsMediaPlaylist != null) {
                Segment segment = hlsMediaPlaylist.initializationSegment;
                if (segment != null) {
                    addSegment(arrayList, hlsMediaPlaylist, segment, hashSet);
                }
                List list = hlsMediaPlaylist.segments;
                for (int i = 0; i < list.size(); i++) {
                    addSegment(arrayList, hlsMediaPlaylist, (Segment) list.get(i), hashSet);
                }
            }
        }
        return arrayList;
    }

    private static HlsPlaylist loadManifest(DataSource dataSource, Uri uri) throws IOException {
        ParsingLoadable parsingLoadable = new ParsingLoadable(dataSource, uri, 4, new HlsPlaylistParser());
        parsingLoadable.load();
        return (HlsPlaylist) parsingLoadable.getResult();
    }

    private static void addSegment(ArrayList<Segment> arrayList, HlsMediaPlaylist hlsMediaPlaylist, Segment segment, HashSet<Uri> hashSet) {
        ArrayList<Segment> arrayList2 = arrayList;
        HlsMediaPlaylist hlsMediaPlaylist2 = hlsMediaPlaylist;
        Segment segment2 = segment;
        long j = hlsMediaPlaylist2.startTimeUs + segment2.relativeStartTimeUs;
        if (segment2.fullSegmentEncryptionKeyUri != null) {
            Uri resolveToUri = UriUtil.resolveToUri(hlsMediaPlaylist2.baseUri, segment2.fullSegmentEncryptionKeyUri);
            if (hashSet.add(resolveToUri)) {
                arrayList2.add(new Segment(j, new DataSpec(resolveToUri)));
            }
        }
        arrayList2.add(new Segment(j, new DataSpec(UriUtil.resolveToUri(hlsMediaPlaylist2.baseUri, segment2.url), segment2.byterangeOffset, segment2.byterangeLength, null)));
    }

    private static void extractUrls(List<HlsUrl> list, ArrayList<String> arrayList) {
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(((HlsUrl) list.get(i)).url);
        }
    }
}
