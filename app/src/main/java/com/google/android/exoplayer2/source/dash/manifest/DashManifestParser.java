package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentList;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTimelineElement;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import com.google.common.net.HttpHeaders;
import de.timroes.axmlrpc.XMLRPCClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.lingala.zip4j.util.InternalZipConstants;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class DashManifestParser extends DefaultHandler implements Parser<DashManifest> {
    private static final Pattern CEA_608_ACCESSIBILITY_PATTERN = Pattern.compile("CC([1-4])=.*");
    private static final Pattern CEA_708_ACCESSIBILITY_PATTERN = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");
    private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
    private static final String TAG = "MpdParser";
    private final String contentId;
    private final XmlPullParserFactory xmlParserFactory;

    protected static final class RepresentationInfo {
        public final String baseUrl;
        public final ArrayList<SchemeData> drmSchemeDatas;
        public final String drmSchemeType;
        public final Format format;
        public final ArrayList<Descriptor> inbandEventStreams;
        public final long revisionId;
        public final SegmentBase segmentBase;

        public RepresentationInfo(Format format, String str, SegmentBase segmentBase, String str2, ArrayList<SchemeData> arrayList, ArrayList<Descriptor> arrayList2, long j) {
            this.format = format;
            this.baseUrl = str;
            this.segmentBase = segmentBase;
            this.drmSchemeType = str2;
            this.drmSchemeDatas = arrayList;
            this.inbandEventStreams = arrayList2;
            this.revisionId = j;
        }
    }

    protected void parseAdaptationSetChild(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
    }

    public DashManifestParser() {
        this(null);
    }

    public DashManifestParser(String str) {
        this.contentId = str;
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (String str2) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", str2);
        }
    }

    public DashManifest parse(Uri uri, InputStream inputStream) throws IOException {
        try {
            XmlPullParser newPullParser = this.xmlParserFactory.newPullParser();
            newPullParser.setInput(inputStream, null);
            if (newPullParser.next() == 2) {
                if ("MPD".equals(newPullParser.getName()) != null) {
                    return parseMediaPresentationDescription(newPullParser, uri.toString());
                }
            }
            throw new ParserException("inputStream does not contain a valid media presentation description");
        } catch (Throwable e) {
            throw new ParserException(e);
        }
    }

    protected DashManifest parseMediaPresentationDescription(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        long j;
        DashManifestParser dashManifestParser;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        long parseDateTime = parseDateTime(xmlPullParser2, "availabilityStartTime", C0649C.TIME_UNSET);
        long parseDuration = parseDuration(xmlPullParser2, "mediaPresentationDuration", C0649C.TIME_UNSET);
        long parseDuration2 = parseDuration(xmlPullParser2, "minBufferTime", C0649C.TIME_UNSET);
        String attributeValue = xmlPullParser2.getAttributeValue(null, "type");
        Object obj = null;
        boolean z = attributeValue != null && attributeValue.equals("dynamic");
        long parseDuration3 = z ? parseDuration(xmlPullParser2, "minimumUpdatePeriod", C0649C.TIME_UNSET) : C0649C.TIME_UNSET;
        long parseDuration4 = z ? parseDuration(xmlPullParser2, "timeShiftBufferDepth", C0649C.TIME_UNSET) : C0649C.TIME_UNSET;
        long parseDuration5 = z ? parseDuration(xmlPullParser2, "suggestedPresentationDelay", C0649C.TIME_UNSET) : C0649C.TIME_UNSET;
        long parseDateTime2 = parseDateTime(xmlPullParser2, "publishTime", C0649C.TIME_UNSET);
        List arrayList = new ArrayList();
        String str2 = str;
        long j2 = z ? C0649C.TIME_UNSET : 0;
        Object obj2 = null;
        Uri uri = null;
        UtcTimingElement utcTimingElement = null;
        while (true) {
            Object obj3;
            long j3;
            String str3;
            xmlPullParser.next();
            long j4 = parseDuration4;
            if (!XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "UTCTiming")) {
                    j = parseDuration3;
                    utcTimingElement = parseUtcTiming(xmlPullParser);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, HttpHeaders.LOCATION)) {
                    j = parseDuration3;
                    uri = Uri.parse(xmlPullParser.nextText());
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Period") && r21 == null) {
                    obj3 = obj;
                    Pair parsePeriod = parsePeriod(xmlPullParser2, str2, j2);
                    j3 = j2;
                    Period period = (Period) parsePeriod.first;
                    str3 = str2;
                    j = parseDuration3;
                    if (period.startMs != C0649C.TIME_UNSET) {
                        long longValue = ((Long) parsePeriod.second).longValue();
                        if (longValue == C0649C.TIME_UNSET) {
                            j3 = C0649C.TIME_UNSET;
                        } else {
                            j3 = period.startMs + longValue;
                        }
                        arrayList.add(period);
                        obj = obj3;
                        j2 = j3;
                        str2 = str3;
                        if (XmlPullParserUtil.isEndTag(xmlPullParser2, "MPD")) {
                            break;
                        }
                        parseDuration4 = j4;
                        parseDuration3 = j;
                    } else if (z) {
                        obj = obj3;
                        j2 = j3;
                        str2 = str3;
                        obj2 = 1;
                        if (XmlPullParserUtil.isEndTag(xmlPullParser2, "MPD")) {
                            break;
                        }
                        parseDuration4 = j4;
                        parseDuration3 = j;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unable to determine start of period ");
                        stringBuilder.append(arrayList.size());
                        throw new ParserException(stringBuilder.toString());
                    }
                }
                dashManifestParser = this;
                if (XmlPullParserUtil.isEndTag(xmlPullParser2, "MPD")) {
                    break;
                }
                parseDuration4 = j4;
                parseDuration3 = j;
            } else if (obj == null) {
                dashManifestParser = this;
                str2 = parseBaseUrl(xmlPullParser2, str2);
                j = parseDuration3;
                obj = 1;
                if (XmlPullParserUtil.isEndTag(xmlPullParser2, "MPD")) {
                    break;
                }
                parseDuration4 = j4;
                parseDuration3 = j;
            }
            dashManifestParser = this;
            j3 = j2;
            obj3 = obj;
            str3 = str2;
            j = parseDuration3;
            obj = obj3;
            j2 = j3;
            str2 = str3;
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "MPD")) {
                break;
            }
            parseDuration4 = j4;
            parseDuration3 = j;
        }
        if (parseDuration == C0649C.TIME_UNSET) {
            if (j2 != C0649C.TIME_UNSET) {
                parseDuration = j2;
            } else if (!z) {
                throw new ParserException("Unable to determine duration of static manifest.");
            }
        }
        if (arrayList.isEmpty()) {
            throw new ParserException("No periods found.");
        }
        return dashManifestParser.buildMediaPresentationDescription(parseDateTime, parseDuration, parseDuration2, z, j, j4, parseDuration5, parseDateTime2, utcTimingElement, uri, arrayList);
    }

    protected DashManifest buildMediaPresentationDescription(long j, long j2, long j3, boolean z, long j4, long j5, long j6, long j7, UtcTimingElement utcTimingElement, Uri uri, List<Period> list) {
        return new DashManifest(j, j2, j3, z, j4, j5, j6, j7, utcTimingElement, uri, list);
    }

    protected UtcTimingElement parseUtcTiming(XmlPullParser xmlPullParser) {
        return buildUtcTimingElement(xmlPullParser.getAttributeValue(null, "schemeIdUri"), xmlPullParser.getAttributeValue(null, XMLRPCClient.VALUE));
    }

    protected UtcTimingElement buildUtcTimingElement(String str, String str2) {
        return new UtcTimingElement(str, str2);
    }

    protected Pair<Period, Long> parsePeriod(XmlPullParser xmlPullParser, String str, long j) throws XmlPullParserException, IOException {
        String attributeValue = xmlPullParser.getAttributeValue(null, TtmlNode.ATTR_ID);
        long parseDuration = parseDuration(xmlPullParser, TtmlNode.START, j);
        j = parseDuration(xmlPullParser, "duration", C0649C.TIME_UNSET);
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        Object obj = null;
        SegmentBase segmentBase = null;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (obj == null) {
                    str = parseBaseUrl(xmlPullParser, str);
                    obj = 1;
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "AdaptationSet")) {
                arrayList.add(parseAdaptationSet(xmlPullParser, str, segmentBase));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "EventStream")) {
                arrayList2.add(parseEventStream(xmlPullParser));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentBase")) {
                segmentBase = parseSegmentBase(xmlPullParser, null);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentList")) {
                segmentBase = parseSegmentList(xmlPullParser, null);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTemplate")) {
                segmentBase = parseSegmentTemplate(xmlPullParser, null);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Period"));
        return Pair.create(buildPeriod(attributeValue, parseDuration, arrayList, arrayList2), Long.valueOf(j));
    }

    protected Period buildPeriod(String str, long j, List<AdaptationSet> list, List<EventStream> list2) {
        return new Period(str, j, list, list2);
    }

    protected AdaptationSet parseAdaptationSet(XmlPullParser xmlPullParser, String str, SegmentBase segmentBase) throws XmlPullParserException, IOException {
        List list;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        int i;
        DashManifestParser dashManifestParser = this;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        int parseInt = parseInt(xmlPullParser2, TtmlNode.ATTR_ID, -1);
        int parseContentType = parseContentType(xmlPullParser);
        String str2 = null;
        String attributeValue = xmlPullParser2.getAttributeValue(null, "mimeType");
        String attributeValue2 = xmlPullParser2.getAttributeValue(null, "codecs");
        int parseInt2 = parseInt(xmlPullParser2, "width", -1);
        int parseInt3 = parseInt(xmlPullParser2, "height", -1);
        float parseFrameRate = parseFrameRate(xmlPullParser2, -1.0f);
        int parseInt4 = parseInt(xmlPullParser2, "audioSamplingRate", -1);
        String attributeValue3 = xmlPullParser2.getAttributeValue(null, "lang");
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        String str3 = str;
        SegmentBase segmentBase2 = segmentBase;
        int i2 = parseContentType;
        String str4 = attributeValue3;
        String str5 = null;
        Object obj = null;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            String parseBaseUrl;
            String str6;
            String str7;
            XmlPullParser xmlPullParser3;
            List list2;
            xmlPullParser.next();
            int i5;
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (obj == null) {
                    parseBaseUrl = parseBaseUrl(xmlPullParser2, str3);
                    str6 = str4;
                    list = arrayList9;
                    arrayList = arrayList8;
                    arrayList2 = arrayList7;
                    arrayList3 = arrayList6;
                    arrayList4 = arrayList5;
                    str7 = str2;
                    xmlPullParser3 = xmlPullParser2;
                    obj = 1;
                }
                i5 = i2;
                str6 = str4;
                parseBaseUrl = str3;
                list = arrayList9;
                arrayList = arrayList8;
                arrayList2 = arrayList7;
                arrayList3 = arrayList6;
                arrayList4 = arrayList5;
                str7 = str2;
                xmlPullParser3 = xmlPullParser2;
                i = i5;
                if (!XmlPullParserUtil.isEndTag(xmlPullParser3, "AdaptationSet")) {
                    break;
                }
                xmlPullParser2 = xmlPullParser3;
                arrayList6 = arrayList3;
                i2 = i;
                str3 = parseBaseUrl;
                arrayList8 = arrayList;
                arrayList7 = arrayList2;
                arrayList5 = arrayList4;
                str2 = str7;
                list2 = list;
                str4 = str6;
            } else {
                if (!XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentProtection")) {
                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentComponent")) {
                        str6 = checkLanguageConsistency(str4, xmlPullParser2.getAttributeValue(str2, "lang"));
                        parseBaseUrl = str3;
                        list = arrayList9;
                        arrayList = arrayList8;
                        arrayList2 = arrayList7;
                        arrayList3 = arrayList6;
                        arrayList4 = arrayList5;
                        str7 = str2;
                        xmlPullParser3 = xmlPullParser2;
                        i = checkContentTypeConsistency(i2, parseContentType(xmlPullParser));
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Role")) {
                        i3 |= parseRole(xmlPullParser);
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AudioChannelConfiguration")) {
                        i4 = parseAudioChannelConfiguration(xmlPullParser);
                    } else {
                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Accessibility")) {
                            arrayList7.add(parseDescriptor(xmlPullParser2, "Accessibility"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SupplementalProperty")) {
                            arrayList8.add(parseDescriptor(xmlPullParser2, "SupplementalProperty"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Representation")) {
                            int i6 = i2;
                            str6 = str4;
                            parseBaseUrl = str3;
                            ArrayList arrayList10 = arrayList9;
                            arrayList = arrayList8;
                            arrayList2 = arrayList7;
                            r34 = arrayList6;
                            arrayList4 = arrayList5;
                            str7 = str2;
                            RepresentationInfo parseRepresentation = parseRepresentation(xmlPullParser2, str3, attributeValue, attributeValue2, parseInt2, parseInt3, parseFrameRate, i4, parseInt4, str6, i3, arrayList2, segmentBase2);
                            int checkContentTypeConsistency = checkContentTypeConsistency(i6, getContentType(parseRepresentation.format));
                            list = arrayList10;
                            list.add(parseRepresentation);
                            i = checkContentTypeConsistency;
                            arrayList3 = r34;
                            xmlPullParser3 = xmlPullParser;
                        } else {
                            SegmentBase parseSegmentBase;
                            str6 = str4;
                            parseBaseUrl = str3;
                            list = arrayList9;
                            arrayList = arrayList8;
                            arrayList2 = arrayList7;
                            r34 = arrayList6;
                            arrayList4 = arrayList5;
                            str7 = str2;
                            i5 = i2;
                            xmlPullParser3 = xmlPullParser;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser3, "SegmentBase")) {
                                parseSegmentBase = parseSegmentBase(xmlPullParser3, (SingleSegmentBase) segmentBase2);
                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "SegmentList")) {
                                parseSegmentBase = parseSegmentList(xmlPullParser3, (SegmentList) segmentBase2);
                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "SegmentTemplate")) {
                                parseSegmentBase = parseSegmentTemplate(xmlPullParser3, (SegmentTemplate) segmentBase2);
                            } else {
                                if (XmlPullParserUtil.isStartTag(xmlPullParser3, "InbandEventStream")) {
                                    arrayList3 = r34;
                                    arrayList3.add(parseDescriptor(xmlPullParser3, "InbandEventStream"));
                                } else {
                                    arrayList3 = r34;
                                    if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
                                        parseAdaptationSetChild(xmlPullParser);
                                    }
                                }
                                i = i5;
                            }
                            segmentBase2 = parseSegmentBase;
                            i = i5;
                            arrayList3 = r34;
                        }
                        i5 = i2;
                        str6 = str4;
                        parseBaseUrl = str3;
                        list = arrayList9;
                        arrayList = arrayList8;
                        arrayList2 = arrayList7;
                        arrayList3 = arrayList6;
                        arrayList4 = arrayList5;
                        str7 = str2;
                        xmlPullParser3 = xmlPullParser2;
                        i = i5;
                    }
                    if (!XmlPullParserUtil.isEndTag(xmlPullParser3, "AdaptationSet")) {
                        break;
                    }
                    xmlPullParser2 = xmlPullParser3;
                    arrayList6 = arrayList3;
                    i2 = i;
                    str3 = parseBaseUrl;
                    arrayList8 = arrayList;
                    arrayList7 = arrayList2;
                    arrayList5 = arrayList4;
                    str2 = str7;
                    list2 = list;
                    str4 = str6;
                } else {
                    Pair parseContentProtection = parseContentProtection(xmlPullParser);
                    if (parseContentProtection.first != null) {
                        str5 = (String) parseContentProtection.first;
                    }
                    if (parseContentProtection.second != null) {
                        arrayList5.add(parseContentProtection.second);
                    }
                }
                str6 = str4;
                parseBaseUrl = str3;
                Object obj2 = arrayList9;
                arrayList = arrayList8;
                arrayList2 = arrayList7;
                arrayList3 = arrayList6;
                arrayList4 = arrayList5;
                str7 = str2;
                xmlPullParser3 = xmlPullParser2;
            }
            i = i2;
            if (!XmlPullParserUtil.isEndTag(xmlPullParser3, "AdaptationSet")) {
                break;
            }
            xmlPullParser2 = xmlPullParser3;
            arrayList6 = arrayList3;
            i2 = i;
            str3 = parseBaseUrl;
            arrayList8 = arrayList;
            arrayList7 = arrayList2;
            arrayList5 = arrayList4;
            str2 = str7;
            list2 = list;
            str4 = str6;
        }
        List arrayList11 = new ArrayList(list.size());
        for (int i7 = 0; i7 < list.size(); i7++) {
            arrayList11.add(buildRepresentation((RepresentationInfo) list.get(i7), dashManifestParser.contentId, str5, arrayList4, arrayList3));
        }
        return buildAdaptationSet(parseInt, i, arrayList11, arrayList2, arrayList);
    }

    protected AdaptationSet buildAdaptationSet(int i, int i2, List<Representation> list, List<Descriptor> list2, List<Descriptor> list3) {
        return new AdaptationSet(i, i2, list, list2, list3);
    }

    protected int parseContentType(XmlPullParser xmlPullParser) {
        xmlPullParser = xmlPullParser.getAttributeValue(null, "contentType");
        if (TextUtils.isEmpty(xmlPullParser)) {
            return -1;
        }
        if (MimeTypes.BASE_TYPE_AUDIO.equals(xmlPullParser)) {
            return 1;
        }
        if (MimeTypes.BASE_TYPE_VIDEO.equals(xmlPullParser)) {
            return 2;
        }
        if (MimeTypes.BASE_TYPE_TEXT.equals(xmlPullParser) != null) {
            return 3;
        }
        return -1;
    }

    protected int getContentType(Format format) {
        format = format.sampleMimeType;
        if (TextUtils.isEmpty(format)) {
            return -1;
        }
        if (MimeTypes.isVideo(format)) {
            return 2;
        }
        if (MimeTypes.isAudio(format)) {
            return 1;
        }
        if (mimeTypeIsRawText(format) != null) {
            return 3;
        }
        return -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected android.util.Pair<java.lang.String, com.google.android.exoplayer2.drm.DrmInitData.SchemeData> parseContentProtection(org.xmlpull.v1.XmlPullParser r11) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r10 = this;
        r0 = "schemeIdUri";
        r1 = 0;
        r0 = r11.getAttributeValue(r1, r0);
        r2 = 1;
        r3 = 0;
        if (r0 == 0) goto L_0x008f;
    L_0x000b:
        r0 = com.google.android.exoplayer2.util.Util.toLowerInvariant(r0);
        r4 = -1;
        r5 = r0.hashCode();
        r6 = 489446379; // 0x1d2c5beb float:2.281153E-21 double:2.418186413E-315;
        if (r5 == r6) goto L_0x0038;
    L_0x0019:
        r6 = 755418770; // 0x2d06c692 float:7.66111E-12 double:3.732264625E-315;
        if (r5 == r6) goto L_0x002e;
    L_0x001e:
        r6 = 1812765994; // 0x6c0c9d2a float:6.799672E26 double:8.956254016E-315;
        if (r5 == r6) goto L_0x0024;
    L_0x0023:
        goto L_0x0042;
    L_0x0024:
        r5 = "urn:mpeg:dash:mp4protection:2011";
        r0 = r0.equals(r5);
        if (r0 == 0) goto L_0x0042;
    L_0x002c:
        r0 = 0;
        goto L_0x0043;
    L_0x002e:
        r5 = "urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed";
        r0 = r0.equals(r5);
        if (r0 == 0) goto L_0x0042;
    L_0x0036:
        r0 = 2;
        goto L_0x0043;
    L_0x0038:
        r5 = "urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95";
        r0 = r0.equals(r5);
        if (r0 == 0) goto L_0x0042;
    L_0x0040:
        r0 = 1;
        goto L_0x0043;
    L_0x0042:
        r0 = -1;
    L_0x0043:
        switch(r0) {
            case 0: goto L_0x004e;
            case 1: goto L_0x004a;
            case 2: goto L_0x0047;
            default: goto L_0x0046;
        };
    L_0x0046:
        goto L_0x008f;
    L_0x0047:
        r0 = com.google.android.exoplayer2.C0649C.WIDEVINE_UUID;
        goto L_0x004c;
    L_0x004a:
        r0 = com.google.android.exoplayer2.C0649C.PLAYREADY_UUID;
    L_0x004c:
        r4 = r1;
        goto L_0x0091;
    L_0x004e:
        r0 = "value";
        r0 = r11.getAttributeValue(r1, r0);
        r4 = "cenc:default_KID";
        r4 = r11.getAttributeValue(r1, r4);
        r5 = android.text.TextUtils.isEmpty(r4);
        if (r5 != 0) goto L_0x008b;
    L_0x0060:
        r5 = "00000000-0000-0000-0000-000000000000";
        r5 = r5.equals(r4);
        if (r5 != 0) goto L_0x008b;
    L_0x0068:
        r5 = "\\s+";
        r4 = r4.split(r5);
        r5 = r4.length;
        r5 = new java.util.UUID[r5];
        r6 = 0;
    L_0x0072:
        r7 = r4.length;
        if (r6 >= r7) goto L_0x0080;
    L_0x0075:
        r7 = r4[r6];
        r7 = java.util.UUID.fromString(r7);
        r5[r6] = r7;
        r6 = r6 + 1;
        goto L_0x0072;
    L_0x0080:
        r4 = com.google.android.exoplayer2.C0649C.COMMON_PSSH_UUID;
        r4 = com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil.buildPsshAtom(r4, r5, r1);
        r5 = com.google.android.exoplayer2.C0649C.COMMON_PSSH_UUID;
        r6 = r0;
        r0 = r5;
        goto L_0x0092;
    L_0x008b:
        r6 = r0;
        r0 = r1;
        r4 = r0;
        goto L_0x0092;
    L_0x008f:
        r0 = r1;
        r4 = r0;
    L_0x0091:
        r6 = r4;
    L_0x0092:
        r5 = 0;
    L_0x0093:
        r11.next();
        r7 = "widevine:license";
        r7 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r11, r7);
        if (r7 == 0) goto L_0x00b2;
    L_0x009e:
        r5 = "robustness_level";
        r5 = r11.getAttributeValue(r1, r5);
        if (r5 == 0) goto L_0x00b0;
    L_0x00a6:
        r7 = "HW";
        r5 = r5.startsWith(r7);
        if (r5 == 0) goto L_0x00b0;
    L_0x00ae:
        r5 = 1;
        goto L_0x0103;
    L_0x00b0:
        r5 = 0;
        goto L_0x0103;
    L_0x00b2:
        if (r4 != 0) goto L_0x0103;
    L_0x00b4:
        r7 = "cenc:pssh";
        r7 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r11, r7);
        r8 = 4;
        if (r7 == 0) goto L_0x00df;
    L_0x00bd:
        r7 = r11.next();
        if (r7 != r8) goto L_0x00df;
    L_0x00c3:
        r0 = r11.getText();
        r0 = android.util.Base64.decode(r0, r3);
        r4 = com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil.parseUuid(r0);
        if (r4 != 0) goto L_0x00db;
    L_0x00d1:
        r0 = "MpdParser";
        r7 = "Skipping malformed cenc:pssh data";
        android.util.Log.w(r0, r7);
        r0 = r4;
        r4 = r1;
        goto L_0x0103;
    L_0x00db:
        r9 = r4;
        r4 = r0;
        r0 = r9;
        goto L_0x0103;
    L_0x00df:
        r7 = com.google.android.exoplayer2.C0649C.PLAYREADY_UUID;
        r7 = r7.equals(r0);
        if (r7 == 0) goto L_0x0103;
    L_0x00e7:
        r7 = "mspr:pro";
        r7 = com.google.android.exoplayer2.util.XmlPullParserUtil.isStartTag(r11, r7);
        if (r7 == 0) goto L_0x0103;
    L_0x00ef:
        r7 = r11.next();
        if (r7 != r8) goto L_0x0103;
    L_0x00f5:
        r4 = com.google.android.exoplayer2.C0649C.PLAYREADY_UUID;
        r7 = r11.getText();
        r7 = android.util.Base64.decode(r7, r3);
        r4 = com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil.buildPsshAtom(r4, r7);
    L_0x0103:
        r7 = "ContentProtection";
        r7 = com.google.android.exoplayer2.util.XmlPullParserUtil.isEndTag(r11, r7);
        if (r7 == 0) goto L_0x0093;
    L_0x010b:
        if (r0 == 0) goto L_0x0114;
    L_0x010d:
        r1 = new com.google.android.exoplayer2.drm.DrmInitData$SchemeData;
        r11 = "video/mp4";
        r1.<init>(r0, r11, r4, r5);
    L_0x0114:
        r11 = android.util.Pair.create(r6, r1);
        return r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.parseContentProtection(org.xmlpull.v1.XmlPullParser):android.util.Pair<java.lang.String, com.google.android.exoplayer2.drm.DrmInitData$SchemeData>");
    }

    protected int parseRole(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", null);
        String parseString2 = parseString(xmlPullParser, XMLRPCClient.VALUE, null);
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Role"));
        return ("urn:mpeg:dash:role:2011".equals(parseString) == null || "main".equals(parseString2) == null) ? null : 1;
    }

    protected RepresentationInfo parseRepresentation(XmlPullParser xmlPullParser, String str, String str2, String str3, int i, int i2, float f, int i3, int i4, String str4, int i5, List<Descriptor> list, SegmentBase segmentBase) throws XmlPullParserException, IOException {
        String str5;
        String str6;
        String str7;
        SegmentBase segmentBase2;
        SegmentBase segmentBase3;
        DashManifestParser dashManifestParser = this;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        String attributeValue = xmlPullParser2.getAttributeValue(null, TtmlNode.ATTR_ID);
        int parseInt = parseInt(xmlPullParser2, "bandwidth", -1);
        String parseString = parseString(xmlPullParser2, "mimeType", str2);
        String parseString2 = parseString(xmlPullParser2, "codecs", str3);
        int parseInt2 = parseInt(xmlPullParser2, "width", i);
        int parseInt3 = parseInt(xmlPullParser2, "height", i2);
        float parseFrameRate = parseFrameRate(xmlPullParser2, f);
        int parseInt4 = parseInt(xmlPullParser2, "audioSamplingRate", i4);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        Object obj = null;
        int i6 = i3;
        SegmentBase segmentBase4 = segmentBase;
        String str8 = null;
        String str9 = str;
        while (true) {
            xmlPullParser.next();
            str5 = parseString2;
            if (!XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AudioChannelConfiguration")) {
                    str6 = str9;
                    i6 = parseAudioChannelConfiguration(xmlPullParser);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentBase")) {
                    segmentBase4 = parseSegmentBase(xmlPullParser2, (SingleSegmentBase) segmentBase4);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentList")) {
                    segmentBase4 = parseSegmentList(xmlPullParser2, (SegmentList) segmentBase4);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTemplate")) {
                    segmentBase4 = parseSegmentTemplate(xmlPullParser2, (SegmentTemplate) segmentBase4);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentProtection")) {
                    Pair parseContentProtection = parseContentProtection(xmlPullParser);
                    str6 = str9;
                    if (parseContentProtection.first != null) {
                        str8 = (String) parseContentProtection.first;
                    }
                    if (parseContentProtection.second != null) {
                        arrayList.add(parseContentProtection.second);
                    }
                } else {
                    str6 = str9;
                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "InbandEventStream")) {
                        arrayList2.add(parseDescriptor(xmlPullParser2, "InbandEventStream"));
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SupplementalProperty")) {
                        arrayList3.add(parseDescriptor(xmlPullParser2, "SupplementalProperty"));
                    }
                }
                str7 = str8;
                segmentBase2 = segmentBase4;
                if (XmlPullParserUtil.isEndTag(xmlPullParser2, "Representation")) {
                    break;
                }
                segmentBase4 = segmentBase2;
                str8 = str7;
                parseString2 = str5;
                str9 = str6;
            } else if (obj == null) {
                str9 = parseBaseUrl(xmlPullParser2, str9);
                obj = 1;
            }
            str6 = str9;
            str7 = str8;
            segmentBase2 = segmentBase4;
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "Representation")) {
                break;
            }
            segmentBase4 = segmentBase2;
            str8 = str7;
            parseString2 = str5;
            str9 = str6;
        }
        ArrayList arrayList4 = arrayList2;
        Format buildFormat = buildFormat(attributeValue, parseString, parseInt2, parseInt3, parseFrameRate, i6, parseInt4, parseInt, str4, i5, list, str5, arrayList3);
        if (segmentBase2 != null) {
            segmentBase3 = segmentBase2;
        } else {
            segmentBase3 = new SingleSegmentBase();
        }
        return new RepresentationInfo(buildFormat, str6, segmentBase3, str7, arrayList, arrayList4, -1);
    }

    protected Format buildFormat(String str, String str2, int i, int i2, float f, int i3, int i4, int i5, String str3, int i6, List<Descriptor> list, String str4, List<Descriptor> list2) {
        String str5;
        String str6 = str2;
        String str7 = str4;
        String sampleMimeType = getSampleMimeType(str6, str7);
        if (sampleMimeType != null) {
            if (MimeTypes.AUDIO_E_AC3.equals(sampleMimeType)) {
                sampleMimeType = parseEac3SupplementalProperties(list2);
            }
            str5 = sampleMimeType;
            if (MimeTypes.isVideo(str5)) {
                return Format.createVideoContainerFormat(str, str6, str5, str7, i5, i, i2, f, null, i6);
            }
            if (MimeTypes.isAudio(str5)) {
                return Format.createAudioContainerFormat(str, str6, str5, str7, i5, i3, i4, null, i6, str3);
            }
            if (mimeTypeIsRawText(str5)) {
                int parseCea608AccessibilityChannel;
                int i7;
                if (MimeTypes.APPLICATION_CEA608.equals(str5)) {
                    parseCea608AccessibilityChannel = parseCea608AccessibilityChannel(list);
                } else if (MimeTypes.APPLICATION_CEA708.equals(str5)) {
                    parseCea608AccessibilityChannel = parseCea708AccessibilityChannel(list);
                } else {
                    i7 = -1;
                    return Format.createTextContainerFormat(str, str6, str5, str7, i5, i6, str3, i7);
                }
                i7 = parseCea608AccessibilityChannel;
                return Format.createTextContainerFormat(str, str6, str5, str7, i5, i6, str3, i7);
            }
        }
        str5 = sampleMimeType;
        return Format.createContainerFormat(str, str6, str5, str7, i5, i6, str3);
    }

    protected Representation buildRepresentation(RepresentationInfo representationInfo, String str, String str2, ArrayList<SchemeData> arrayList, ArrayList<Descriptor> arrayList2) {
        Format format = representationInfo.format;
        if (representationInfo.drmSchemeType != null) {
            str2 = representationInfo.drmSchemeType;
        }
        List list = representationInfo.drmSchemeDatas;
        list.addAll(arrayList);
        if (list.isEmpty() == null) {
            filterRedundantIncompleteSchemeDatas(list);
            format = format.copyWithDrmInitData(new DrmInitData(str2, list));
        }
        Format format2 = format;
        List list2 = representationInfo.inbandEventStreams;
        list2.addAll(arrayList2);
        return Representation.newInstance(str, representationInfo.revisionId, format2, representationInfo.baseUrl, representationInfo.segmentBase, list2);
    }

    protected SingleSegmentBase parseSegmentBase(XmlPullParser xmlPullParser, SingleSegmentBase singleSegmentBase) throws XmlPullParserException, IOException {
        long parseLong;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        SingleSegmentBase singleSegmentBase2 = singleSegmentBase;
        long parseLong2 = parseLong(xmlPullParser2, "timescale", singleSegmentBase2 != null ? singleSegmentBase2.timescale : 1);
        long j = 0;
        long parseLong3 = parseLong(xmlPullParser2, "presentationTimeOffset", singleSegmentBase2 != null ? singleSegmentBase2.presentationTimeOffset : 0);
        long j2 = singleSegmentBase2 != null ? singleSegmentBase2.indexStart : 0;
        if (singleSegmentBase2 != null) {
            j = singleSegmentBase2.indexLength;
        }
        String str = null;
        String attributeValue = xmlPullParser2.getAttributeValue(null, "indexRange");
        if (attributeValue != null) {
            String[] split = attributeValue.split("-");
            j = Long.parseLong(split[0]);
            parseLong = (Long.parseLong(split[1]) - j) + 1;
        } else {
            parseLong = j;
            j = j2;
        }
        if (singleSegmentBase2 != null) {
            str = singleSegmentBase2.initialization;
        }
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Initialization")) {
                str = parseInitialization(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser2, "SegmentBase"));
        return buildSingleSegmentBase(str, parseLong2, parseLong3, j, parseLong);
    }

    protected SingleSegmentBase buildSingleSegmentBase(RangedUri rangedUri, long j, long j2, long j3, long j4) {
        return new SingleSegmentBase(rangedUri, j, j2, j3, j4);
    }

    protected SegmentList parseSegmentList(XmlPullParser xmlPullParser, SegmentList segmentList) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        SegmentList segmentList2 = segmentList;
        long j = 1;
        long parseLong = parseLong(xmlPullParser2, "timescale", segmentList2 != null ? segmentList2.timescale : 1);
        long parseLong2 = parseLong(xmlPullParser2, "presentationTimeOffset", segmentList2 != null ? segmentList2.presentationTimeOffset : 0);
        long parseLong3 = parseLong(xmlPullParser2, "duration", segmentList2 != null ? segmentList2.duration : C0649C.TIME_UNSET);
        String str = "startNumber";
        if (segmentList2 != null) {
            j = segmentList2.startNumber;
        }
        long parseLong4 = parseLong(xmlPullParser2, str, j);
        List list = null;
        RangedUri rangedUri = null;
        List list2 = rangedUri;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTimeline")) {
                list2 = parseSegmentTimeline(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentURL")) {
                if (list == null) {
                    list = new ArrayList();
                }
                list.add(parseSegmentUrl(xmlPullParser));
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser2, "SegmentList"));
        if (segmentList2 != null) {
            if (rangedUri == null) {
                rangedUri = segmentList2.initialization;
            }
            if (list2 == null) {
                list2 = segmentList2.segmentTimeline;
            }
            if (list == null) {
                list = segmentList2.mediaSegments;
            }
        }
        return buildSegmentList(rangedUri, parseLong, parseLong2, parseLong4, parseLong3, list2, list);
    }

    protected SegmentList buildSegmentList(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentTimelineElement> list, List<RangedUri> list2) {
        return new SegmentList(rangedUri, j, j2, j3, j4, list, list2);
    }

    protected SegmentTemplate parseSegmentTemplate(XmlPullParser xmlPullParser, SegmentTemplate segmentTemplate) throws XmlPullParserException, IOException {
        DashManifestParser dashManifestParser = this;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        SegmentTemplate segmentTemplate2 = segmentTemplate;
        long j = 1;
        long parseLong = parseLong(xmlPullParser2, "timescale", segmentTemplate2 != null ? segmentTemplate2.timescale : 1);
        long parseLong2 = parseLong(xmlPullParser2, "presentationTimeOffset", segmentTemplate2 != null ? segmentTemplate2.presentationTimeOffset : 0);
        long parseLong3 = parseLong(xmlPullParser2, "duration", segmentTemplate2 != null ? segmentTemplate2.duration : C0649C.TIME_UNSET);
        String str = "startNumber";
        if (segmentTemplate2 != null) {
            j = segmentTemplate2.startNumber;
        }
        long parseLong4 = parseLong(xmlPullParser2, str, j);
        RangedUri rangedUri = null;
        UrlTemplate parseUrlTemplate = parseUrlTemplate(xmlPullParser2, "media", segmentTemplate2 != null ? segmentTemplate2.mediaTemplate : null);
        UrlTemplate parseUrlTemplate2 = parseUrlTemplate(xmlPullParser2, "initialization", segmentTemplate2 != null ? segmentTemplate2.initializationTemplate : null);
        List list = null;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTimeline")) {
                list = parseSegmentTimeline(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser2, "SegmentTemplate"));
        if (segmentTemplate2 != null) {
            if (rangedUri == null) {
                rangedUri = segmentTemplate2.initialization;
            }
            if (list == null) {
                list = segmentTemplate2.segmentTimeline;
            }
        }
        return buildSegmentTemplate(rangedUri, parseLong, parseLong2, parseLong4, parseLong3, list, parseUrlTemplate2, parseUrlTemplate);
    }

    protected SegmentTemplate buildSegmentTemplate(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentTimelineElement> list, UrlTemplate urlTemplate, UrlTemplate urlTemplate2) {
        return new SegmentTemplate(rangedUri, j, j2, j3, j4, list, urlTemplate, urlTemplate2);
    }

    protected EventStream parseEventStream(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", "");
        String parseString2 = parseString(xmlPullParser, XMLRPCClient.VALUE, "");
        long parseLong = parseLong(xmlPullParser, "timescale", 1);
        List arrayList = new ArrayList();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Event")) {
                arrayList.add(parseEvent(xmlPullParser, parseString, parseString2, parseLong, byteArrayOutputStream));
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "EventStream"));
        long[] jArr = new long[arrayList.size()];
        EventMessage[] eventMessageArr = new EventMessage[arrayList.size()];
        for (xmlPullParser = null; xmlPullParser < arrayList.size(); xmlPullParser++) {
            EventMessage eventMessage = (EventMessage) arrayList.get(xmlPullParser);
            jArr[xmlPullParser] = eventMessage.presentationTimeUs;
            eventMessageArr[xmlPullParser] = eventMessage;
        }
        return buildEventStream(parseString, parseString2, parseLong, jArr, eventMessageArr);
    }

    protected EventStream buildEventStream(String str, String str2, long j, long[] jArr, EventMessage[] eventMessageArr) {
        return new EventStream(str, str2, j, jArr, eventMessageArr);
    }

    protected EventMessage parseEvent(XmlPullParser xmlPullParser, String str, String str2, long j, ByteArrayOutputStream byteArrayOutputStream) throws IOException, XmlPullParserException {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        long parseLong = parseLong(xmlPullParser2, TtmlNode.ATTR_ID, 0);
        long parseLong2 = parseLong(xmlPullParser2, "duration", C0649C.TIME_UNSET);
        long parseLong3 = parseLong(xmlPullParser2, "presentationTime", 0);
        return buildEvent(str, str2, parseLong, Util.scaleLargeTimestamp(parseLong2, 1000, j), parseEventObject(xmlPullParser2, byteArrayOutputStream), Util.scaleLargeTimestamp(parseLong3, C0649C.MICROS_PER_SECOND, j));
    }

    protected byte[] parseEventObject(XmlPullParser xmlPullParser, ByteArrayOutputStream byteArrayOutputStream) throws XmlPullParserException, IOException {
        byteArrayOutputStream.reset();
        XmlSerializer newSerializer = Xml.newSerializer();
        newSerializer.setOutput(byteArrayOutputStream, null);
        xmlPullParser.nextToken();
        while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Event")) {
            int i = 0;
            switch (xmlPullParser.getEventType()) {
                case 0:
                    newSerializer.startDocument(null, Boolean.valueOf(false));
                    break;
                case 1:
                    newSerializer.endDocument();
                    break;
                case 2:
                    newSerializer.startTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    while (i < xmlPullParser.getAttributeCount()) {
                        newSerializer.attribute(xmlPullParser.getAttributeNamespace(i), xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
                        i++;
                    }
                    break;
                case 3:
                    newSerializer.endTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    break;
                case 4:
                    newSerializer.text(xmlPullParser.getText());
                    break;
                case 5:
                    newSerializer.cdsect(xmlPullParser.getText());
                    break;
                case 6:
                    newSerializer.entityRef(xmlPullParser.getText());
                    break;
                case 7:
                    newSerializer.ignorableWhitespace(xmlPullParser.getText());
                    break;
                case 8:
                    newSerializer.processingInstruction(xmlPullParser.getText());
                    break;
                case 9:
                    newSerializer.comment(xmlPullParser.getText());
                    break;
                case 10:
                    newSerializer.docdecl(xmlPullParser.getText());
                    break;
                default:
                    break;
            }
            xmlPullParser.nextToken();
        }
        newSerializer.flush();
        return byteArrayOutputStream.toByteArray();
    }

    protected EventMessage buildEvent(String str, String str2, long j, long j2, byte[] bArr, long j3) {
        return new EventMessage(str, str2, j2, j, bArr, j3);
    }

    protected List<SegmentTimelineElement> parseSegmentTimeline(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        List<SegmentTimelineElement> arrayList = new ArrayList();
        long j = 0;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "S")) {
                j = parseLong(xmlPullParser, "t", j);
                long parseLong = parseLong(xmlPullParser, "d", C0649C.TIME_UNSET);
                int i = 0;
                int parseInt = parseInt(xmlPullParser, InternalZipConstants.READ_MODE, 0) + 1;
                while (i < parseInt) {
                    arrayList.add(buildSegmentTimelineElement(j, parseLong));
                    i++;
                    j += parseLong;
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTimeline"));
        return arrayList;
    }

    protected SegmentTimelineElement buildSegmentTimelineElement(long j, long j2) {
        return new SegmentTimelineElement(j, j2);
    }

    protected UrlTemplate parseUrlTemplate(XmlPullParser xmlPullParser, String str, UrlTemplate urlTemplate) {
        xmlPullParser = xmlPullParser.getAttributeValue(null, str);
        return xmlPullParser != null ? UrlTemplate.compile(xmlPullParser) : urlTemplate;
    }

    protected RangedUri parseInitialization(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "sourceURL", "range");
    }

    protected RangedUri parseSegmentUrl(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "media", "mediaRange");
    }

    protected RangedUri parseRangedUrl(XmlPullParser xmlPullParser, String str, String str2) {
        long parseLong;
        long parseLong2;
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        xmlPullParser = xmlPullParser.getAttributeValue(null, str2);
        if (xmlPullParser != null) {
            xmlPullParser = xmlPullParser.split("-");
            parseLong = Long.parseLong(xmlPullParser[0]);
            if (xmlPullParser.length == 2) {
                parseLong2 = (Long.parseLong(xmlPullParser[1]) - parseLong) + 1;
                return buildRangedUri(attributeValue, parseLong, parseLong2);
            }
        }
        parseLong = 0;
        parseLong2 = -1;
        return buildRangedUri(attributeValue, parseLong, parseLong2);
    }

    protected RangedUri buildRangedUri(String str, long j, long j2) {
        return new RangedUri(str, j, j2);
    }

    protected int parseAudioChannelConfiguration(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", null);
        int i = -1;
        if ("urn:mpeg:dash:23003:3:audio_channel_configuration:2011".equals(parseString)) {
            i = parseInt(xmlPullParser, XMLRPCClient.VALUE, -1);
        } else if ("tag:dolby.com,2014:dash:audio_channel_configuration:2011".equals(parseString)) {
            i = parseDolbyChannelConfiguration(xmlPullParser);
        }
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "AudioChannelConfiguration"));
        return i;
    }

    private static void filterRedundantIncompleteSchemeDatas(ArrayList<SchemeData> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            SchemeData schemeData = (SchemeData) arrayList.get(size);
            if (!schemeData.hasData()) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (((SchemeData) arrayList.get(i)).canReplace(schemeData)) {
                        arrayList.remove(size);
                        break;
                    }
                }
            }
        }
    }

    private static String getSampleMimeType(String str, String str2) {
        if (MimeTypes.isAudio(str)) {
            return MimeTypes.getAudioMediaMimeType(str2);
        }
        if (MimeTypes.isVideo(str)) {
            return MimeTypes.getVideoMediaMimeType(str2);
        }
        if (mimeTypeIsRawText(str)) {
            return str;
        }
        if (!MimeTypes.APPLICATION_MP4.equals(str)) {
            if (!(MimeTypes.APPLICATION_RAWCC.equals(str) == null || str2 == null)) {
                if (str2.contains("cea708") != null) {
                    return MimeTypes.APPLICATION_CEA708;
                }
                if (!(str2.contains("eia608") == null && str2.contains("cea608") == null)) {
                    return MimeTypes.APPLICATION_CEA608;
                }
            }
            return null;
        } else if ("stpp".equals(str2) != null) {
            return MimeTypes.APPLICATION_TTML;
        } else {
            if ("wvtt".equals(str2) != null) {
                return MimeTypes.APPLICATION_MP4VTT;
            }
        }
        return null;
    }

    private static boolean mimeTypeIsRawText(String str) {
        if (!(MimeTypes.isText(str) || MimeTypes.APPLICATION_TTML.equals(str) || MimeTypes.APPLICATION_MP4VTT.equals(str) || MimeTypes.APPLICATION_CEA708.equals(str))) {
            if (MimeTypes.APPLICATION_CEA608.equals(str) == null) {
                return null;
            }
        }
        return true;
    }

    private static String checkLanguageConsistency(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        Assertions.checkState(str.equals(str2));
        return str;
    }

    private static int checkContentTypeConsistency(int i, int i2) {
        if (i == -1) {
            return i2;
        }
        if (i2 == -1) {
            return i;
        }
        Assertions.checkState(i == i2 ? 1 : 0);
        return i;
    }

    protected static Descriptor parseDescriptor(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", "");
        String parseString2 = parseString(xmlPullParser, XMLRPCClient.VALUE, null);
        String parseString3 = parseString(xmlPullParser, TtmlNode.ATTR_ID, null);
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, str));
        return new Descriptor(parseString, parseString2, parseString3);
    }

    protected static int parseCea608AccessibilityChannel(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("urn:scte:dash:cc:cea-608:2015".equals(descriptor.schemeIdUri) && descriptor.value != null) {
                Matcher matcher = CEA_608_ACCESSIBILITY_PATTERN.matcher(descriptor.value);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to parse CEA-608 channel number from: ");
                stringBuilder.append(descriptor.value);
                Log.w(str, stringBuilder.toString());
            }
        }
        return -1;
    }

    protected static int parseCea708AccessibilityChannel(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("urn:scte:dash:cc:cea-708:2015".equals(descriptor.schemeIdUri) && descriptor.value != null) {
                Matcher matcher = CEA_708_ACCESSIBILITY_PATTERN.matcher(descriptor.value);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to parse CEA-708 service block number from: ");
                stringBuilder.append(descriptor.value);
                Log.w(str, stringBuilder.toString());
            }
        }
        return -1;
    }

    protected static String parseEac3SupplementalProperties(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = (Descriptor) list.get(i);
            if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(descriptor.schemeIdUri) && "ec+3".equals(descriptor.value)) {
                return MimeTypes.AUDIO_E_AC3_JOC;
            }
        }
        return MimeTypes.AUDIO_E_AC3;
    }

    protected static float parseFrameRate(XmlPullParser xmlPullParser, float f) {
        xmlPullParser = xmlPullParser.getAttributeValue(null, "frameRate");
        if (xmlPullParser == null) {
            return f;
        }
        xmlPullParser = FRAME_RATE_PATTERN.matcher(xmlPullParser);
        if (!xmlPullParser.matches()) {
            return f;
        }
        f = Integer.parseInt(xmlPullParser.group(Float.MIN_VALUE));
        xmlPullParser = xmlPullParser.group(2);
        return !TextUtils.isEmpty(xmlPullParser) ? ((float) f) / ((float) Integer.parseInt(xmlPullParser)) : (float) f;
    }

    protected static long parseDuration(XmlPullParser xmlPullParser, String str, long j) {
        xmlPullParser = xmlPullParser.getAttributeValue(null, str);
        if (xmlPullParser == null) {
            return j;
        }
        return Util.parseXsDuration(xmlPullParser);
    }

    protected static long parseDateTime(XmlPullParser xmlPullParser, String str, long j) throws ParserException {
        xmlPullParser = xmlPullParser.getAttributeValue(null, str);
        if (xmlPullParser == null) {
            return j;
        }
        return Util.parseXsDateTime(xmlPullParser);
    }

    protected static String parseBaseUrl(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        xmlPullParser.next();
        return UriUtil.resolve(str, xmlPullParser.getText());
    }

    protected static int parseInt(XmlPullParser xmlPullParser, String str, int i) {
        xmlPullParser = xmlPullParser.getAttributeValue(null, str);
        return xmlPullParser == null ? i : Integer.parseInt(xmlPullParser);
    }

    protected static long parseLong(XmlPullParser xmlPullParser, String str, long j) {
        xmlPullParser = xmlPullParser.getAttributeValue(null, str);
        return xmlPullParser == null ? j : Long.parseLong(xmlPullParser);
    }

    protected static String parseString(XmlPullParser xmlPullParser, String str, String str2) {
        xmlPullParser = xmlPullParser.getAttributeValue(null, str);
        return xmlPullParser == null ? str2 : xmlPullParser;
    }

    protected static int parseDolbyChannelConfiguration(XmlPullParser xmlPullParser) {
        xmlPullParser = Util.toLowerInvariant(xmlPullParser.getAttributeValue(null, XMLRPCClient.VALUE));
        if (xmlPullParser == null) {
            return -1;
        }
        int hashCode = xmlPullParser.hashCode();
        if (hashCode != 1596796) {
            if (hashCode != 2937391) {
                if (hashCode != 3094035) {
                    if (hashCode == 3133436) {
                        if (xmlPullParser.equals("fa01") != null) {
                            xmlPullParser = 3;
                            switch (xmlPullParser) {
                                case null:
                                    return 1;
                                case 1:
                                    return 2;
                                case 2:
                                    return 6;
                                case 3:
                                    return 8;
                                default:
                                    return -1;
                            }
                        }
                    }
                } else if (xmlPullParser.equals("f801") != null) {
                    xmlPullParser = 2;
                    switch (xmlPullParser) {
                        case null:
                            return 1;
                        case 1:
                            return 2;
                        case 2:
                            return 6;
                        case 3:
                            return 8;
                        default:
                            return -1;
                    }
                }
            } else if (xmlPullParser.equals("a000") != null) {
                xmlPullParser = true;
                switch (xmlPullParser) {
                    case null:
                        return 1;
                    case 1:
                        return 2;
                    case 2:
                        return 6;
                    case 3:
                        return 8;
                    default:
                        return -1;
                }
            }
        } else if (xmlPullParser.equals("4000") != null) {
            xmlPullParser = null;
            switch (xmlPullParser) {
                case null:
                    return 1;
                case 1:
                    return 2;
                case 2:
                    return 6;
                case 3:
                    return 8;
                default:
                    return -1;
            }
        }
        xmlPullParser = -1;
        switch (xmlPullParser) {
            case null:
                return 1;
            case 1:
                return 2;
            case 2:
                return 6;
            case 3:
                return 8;
            default:
                return -1;
        }
    }
}
