package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.util.Util;
import java.util.List;

public abstract class SegmentBase {
    final RangedUri initialization;
    final long presentationTimeOffset;
    final long timescale;

    public static abstract class MultiSegmentBase extends SegmentBase {
        final long duration;
        final List<SegmentTimelineElement> segmentTimeline;
        final long startNumber;

        public abstract int getSegmentCount(long j);

        public abstract RangedUri getSegmentUrl(Representation representation, long j);

        public MultiSegmentBase(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentTimelineElement> list) {
            super(rangedUri, j, j2);
            this.startNumber = j3;
            this.duration = j4;
            this.segmentTimeline = list;
        }

        public long getSegmentNum(long j, long j2) {
            long firstSegmentNum = getFirstSegmentNum();
            j2 = (long) getSegmentCount(j2);
            if (j2 == 0) {
                return firstSegmentNum;
            }
            long j3;
            if (this.segmentTimeline == null) {
                j3 = this.startNumber + (j / ((this.duration * C0649C.MICROS_PER_SECOND) / this.timescale));
                if (j3 >= firstSegmentNum) {
                    if (j2 == -1) {
                        firstSegmentNum = j3;
                    } else {
                        firstSegmentNum = Math.min(j3, (firstSegmentNum + j2) - 1);
                    }
                }
                return firstSegmentNum;
            }
            j3 = (firstSegmentNum + j2) - 1;
            j2 = firstSegmentNum;
            while (j2 <= j3) {
                long j4 = j2 + ((j3 - j2) / 2);
                long segmentTimeUs = getSegmentTimeUs(j4);
                if (segmentTimeUs < j) {
                    j2 = j4 + 1;
                } else if (segmentTimeUs <= j) {
                    return j4;
                } else {
                    j3 = j4 - 1;
                }
            }
            if (j2 != firstSegmentNum) {
                j2 = j3;
            }
            return j2;
        }

        public final long getSegmentDurationUs(long j, long j2) {
            if (this.segmentTimeline != null) {
                return (((SegmentTimelineElement) this.segmentTimeline.get((int) (j - this.startNumber))).duration * C0649C.MICROS_PER_SECOND) / this.timescale;
            }
            int segmentCount = getSegmentCount(j2);
            long segmentTimeUs = (segmentCount == -1 || j != (getFirstSegmentNum() + ((long) segmentCount)) - 1) ? (this.duration * C0649C.MICROS_PER_SECOND) / this.timescale : j2 - getSegmentTimeUs(j);
            return segmentTimeUs;
        }

        public final long getSegmentTimeUs(long j) {
            long j2;
            if (this.segmentTimeline != null) {
                j2 = ((SegmentTimelineElement) this.segmentTimeline.get((int) (j - this.startNumber))).startTime - this.presentationTimeOffset;
            } else {
                j2 = (j - this.startNumber) * this.duration;
            }
            return Util.scaleLargeTimestamp(j2, C0649C.MICROS_PER_SECOND, this.timescale);
        }

        public long getFirstSegmentNum() {
            return this.startNumber;
        }

        public boolean isExplicit() {
            return this.segmentTimeline != null;
        }
    }

    public static class SegmentList extends MultiSegmentBase {
        final List<RangedUri> mediaSegments;

        public boolean isExplicit() {
            return true;
        }

        public SegmentList(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentTimelineElement> list, List<RangedUri> list2) {
            super(rangedUri, j, j2, j3, j4, list);
            this.mediaSegments = list2;
        }

        public RangedUri getSegmentUrl(Representation representation, long j) {
            return (RangedUri) this.mediaSegments.get((int) (j - this.startNumber));
        }

        public int getSegmentCount(long j) {
            return this.mediaSegments.size();
        }
    }

    public static class SegmentTemplate extends MultiSegmentBase {
        final UrlTemplate initializationTemplate;
        final UrlTemplate mediaTemplate;

        public SegmentTemplate(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentTimelineElement> list, UrlTemplate urlTemplate, UrlTemplate urlTemplate2) {
            super(rangedUri, j, j2, j3, j4, list);
            this.initializationTemplate = urlTemplate;
            this.mediaTemplate = urlTemplate2;
        }

        public RangedUri getInitialization(Representation representation) {
            if (this.initializationTemplate != null) {
                return new RangedUri(this.initializationTemplate.buildUri(representation.format.id, 0, representation.format.bitrate, 0), 0, -1);
            }
            return super.getInitialization(representation);
        }

        public RangedUri getSegmentUrl(Representation representation, long j) {
            long j2;
            Representation representation2 = representation;
            if (this.segmentTimeline != null) {
                j2 = ((SegmentTimelineElement) r0.segmentTimeline.get((int) (j - r0.startNumber))).startTime;
            } else {
                j2 = (j - r0.startNumber) * r0.duration;
            }
            return new RangedUri(r0.mediaTemplate.buildUri(representation2.format.id, j, representation2.format.bitrate, j2), 0, -1);
        }

        public int getSegmentCount(long j) {
            if (this.segmentTimeline != null) {
                return this.segmentTimeline.size();
            }
            return j != C0649C.TIME_UNSET ? (int) Util.ceilDivide(j, (this.duration * C0649C.MICROS_PER_SECOND) / this.timescale) : -1;
        }
    }

    public static class SegmentTimelineElement {
        final long duration;
        final long startTime;

        public SegmentTimelineElement(long j, long j2) {
            this.startTime = j;
            this.duration = j2;
        }
    }

    public static class SingleSegmentBase extends SegmentBase {
        final long indexLength;
        final long indexStart;

        public SingleSegmentBase(RangedUri rangedUri, long j, long j2, long j3, long j4) {
            super(rangedUri, j, j2);
            this.indexStart = j3;
            this.indexLength = j4;
        }

        public SingleSegmentBase() {
            this(null, 1, 0, 0, 0);
        }

        public RangedUri getIndex() {
            return this.indexLength <= 0 ? null : new RangedUri(null, this.indexStart, this.indexLength);
        }
    }

    public SegmentBase(RangedUri rangedUri, long j, long j2) {
        this.initialization = rangedUri;
        this.timescale = j;
        this.presentationTimeOffset = j2;
    }

    public RangedUri getInitialization(Representation representation) {
        return this.initialization;
    }

    public long getPresentationTimeOffsetUs() {
        return Util.scaleLargeTimestamp(this.presentationTimeOffset, C0649C.MICROS_PER_SECOND, this.timescale);
    }
}
