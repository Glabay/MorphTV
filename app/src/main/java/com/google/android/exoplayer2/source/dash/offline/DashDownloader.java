package com.google.android.exoplayer2.source.dash.offline;

import android.net.Uri;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.offline.DownloadException;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.SegmentDownloader;
import com.google.android.exoplayer2.source.dash.DashSegmentIndex;
import com.google.android.exoplayer2.source.dash.DashUtil;
import com.google.android.exoplayer2.source.dash.DashWrappingSegmentIndex;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.source.dash.manifest.RepresentationKey;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class DashDownloader extends SegmentDownloader<DashManifest, RepresentationKey> {
    public DashDownloader(Uri uri, DownloaderConstructorHelper downloaderConstructorHelper) {
        super(uri, downloaderConstructorHelper);
    }

    public RepresentationKey[] getAllRepresentationKeys() throws IOException {
        ArrayList arrayList = new ArrayList();
        DashManifest dashManifest = (DashManifest) getManifest();
        for (int i = 0; i < dashManifest.getPeriodCount(); i++) {
            List list = dashManifest.getPeriod(i).adaptationSets;
            for (int i2 = 0; i2 < list.size(); i2++) {
                int size = ((AdaptationSet) list.get(i2)).representations.size();
                for (int i3 = 0; i3 < size; i3++) {
                    arrayList.add(new RepresentationKey(i, i2, i3));
                }
            }
        }
        return (RepresentationKey[]) arrayList.toArray(new RepresentationKey[arrayList.size()]);
    }

    protected DashManifest getManifest(DataSource dataSource, Uri uri) throws IOException {
        return DashUtil.loadManifest(dataSource, uri);
    }

    protected List<Segment> getSegments(DataSource dataSource, DashManifest dashManifest, RepresentationKey[] representationKeyArr, boolean z) throws InterruptedException, IOException {
        DashManifest dashManifest2 = dashManifest;
        RepresentationKey[] representationKeyArr2 = representationKeyArr;
        List arrayList = new ArrayList();
        int length = representationKeyArr2.length;
        int i = 0;
        while (i < length) {
            RepresentationKey representationKey = representationKeyArr2[i];
            try {
                DashSegmentIndex segmentIndex = getSegmentIndex(dataSource, dashManifest2, representationKey);
                if (segmentIndex == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("No index for representation: ");
                    stringBuilder.append(representationKey);
                    throw new DownloadException(stringBuilder.toString());
                }
                int segmentCount = segmentIndex.getSegmentCount(dashManifest2.getPeriodDurationUs(representationKey.periodIndex));
                if (segmentCount == -1) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unbounded index for representation: ");
                    stringBuilder2.append(representationKey);
                    throw new DownloadException(stringBuilder2.toString());
                }
                Period period = dashManifest2.getPeriod(representationKey.periodIndex);
                Representation representation = (Representation) ((AdaptationSet) period.adaptationSets.get(representationKey.adaptationSetIndex)).representations.get(representationKey.representationIndex);
                long msToUs = C0649C.msToUs(period.startMs);
                String str = representation.baseUrl;
                RangedUri initializationUri = representation.getInitializationUri();
                if (initializationUri != null) {
                    addSegment(arrayList, msToUs, str, initializationUri);
                }
                RangedUri indexUri = representation.getIndexUri();
                if (indexUri != null) {
                    addSegment(arrayList, msToUs, str, indexUri);
                }
                long firstSegmentNum = segmentIndex.getFirstSegmentNum();
                long j = (firstSegmentNum + ((long) segmentCount)) - 1;
                while (firstSegmentNum <= j) {
                    addSegment(arrayList, msToUs + segmentIndex.getTimeUs(firstSegmentNum), str, segmentIndex.getSegmentUrl(firstSegmentNum));
                    firstSegmentNum++;
                }
                i++;
                dashManifest2 = dashManifest;
                representationKeyArr2 = representationKeyArr;
            } catch (IOException e) {
                IOException iOException = e;
                if (!z) {
                    throw iOException;
                }
            }
        }
        DashDownloader dashDownloader = this;
        return arrayList;
    }

    private DashSegmentIndex getSegmentIndex(DataSource dataSource, DashManifest dashManifest, RepresentationKey representationKey) throws IOException, InterruptedException {
        AdaptationSet adaptationSet = (AdaptationSet) dashManifest.getPeriod(representationKey.periodIndex).adaptationSets.get(representationKey.adaptationSetIndex);
        Representation representation = (Representation) adaptationSet.representations.get(representationKey.representationIndex);
        DashSegmentIndex index = representation.getIndex();
        if (index != null) {
            return index;
        }
        dataSource = DashUtil.loadChunkIndex(dataSource, adaptationSet.type, representation);
        if (dataSource == null) {
            dataSource = null;
        } else {
            dataSource = new DashWrappingSegmentIndex(dataSource);
        }
        return dataSource;
    }

    private static void addSegment(ArrayList<Segment> arrayList, long j, String str, RangedUri rangedUri) {
        arrayList.add(new Segment(j, new DataSpec(rangedUri.resolveUri(str), rangedUri.start, rangedUri.length, null)));
    }
}
