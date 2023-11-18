package com.android.morpheustv.player;

import android.net.Uri;
import android.util.Base64;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyHLSPlaylistParser implements Parser<HlsPlaylist> {
    private static final String ATTR_CLOSED_CAPTIONS_NONE = "CLOSED-CAPTIONS=NONE";
    private static final String BOOLEAN_FALSE = "NO";
    private static final String BOOLEAN_TRUE = "YES";
    private static final String KEYFORMAT_IDENTITY = "identity";
    private static final String KEYFORMAT_WIDEVINE_PSSH_BINARY = "urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed";
    private static final String KEYFORMAT_WIDEVINE_PSSH_JSON = "com.widevine";
    private static final String METHOD_AES_128 = "AES-128";
    private static final String METHOD_NONE = "NONE";
    private static final String METHOD_SAMPLE_AES = "SAMPLE-AES";
    private static final String METHOD_SAMPLE_AES_CENC = "SAMPLE-AES-CENC";
    private static final String METHOD_SAMPLE_AES_CTR = "SAMPLE-AES-CTR";
    private static final String PLAYLIST_HEADER = "#EXTM3U";
    private static final Pattern REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
    private static final Pattern REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
    private static final Pattern REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
    private static final Pattern REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final Pattern REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
    private static final Pattern REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
    private static final Pattern REGEX_FORCED = compileBooleanAttrPattern("FORCED");
    private static final Pattern REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
    private static final Pattern REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
    private static final Pattern REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
    private static final Pattern REGEX_IV = Pattern.compile("IV=([^,.*]+)");
    private static final Pattern REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
    private static final Pattern REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
    private static final Pattern REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
    private static final Pattern REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)");
    private static final Pattern REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
    private static final Pattern REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
    private static final Pattern REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
    private static final Pattern REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
    private static final Pattern REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
    private static final Pattern REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final Pattern REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private static final String TAG_BYTERANGE = "#EXT-X-BYTERANGE";
    private static final String TAG_DISCONTINUITY = "#EXT-X-DISCONTINUITY";
    private static final String TAG_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";
    private static final String TAG_ENDLIST = "#EXT-X-ENDLIST";
    private static final String TAG_GAP = "#EXT-X-GAP";
    private static final String TAG_INDEPENDENT_SEGMENTS = "#EXT-X-INDEPENDENT-SEGMENTS";
    private static final String TAG_INIT_SEGMENT = "#EXT-X-MAP";
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_MEDIA_DURATION = "#EXTINF";
    private static final String TAG_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";
    private static final String TAG_PLAYLIST_TYPE = "#EXT-X-PLAYLIST-TYPE";
    private static final String TAG_PREFIX = "#EXT";
    private static final String TAG_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME";
    private static final String TAG_START = "#EXT-X-START";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";
    private static final String TAG_TARGET_DURATION = "#EXT-X-TARGETDURATION";
    private static final String TAG_VERSION = "#EXT-X-VERSION";
    private static final String TYPE_AUDIO = "AUDIO";
    private static final String TYPE_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";
    private static final String TYPE_SUBTITLES = "SUBTITLES";
    private static final String TYPE_VIDEO = "VIDEO";

    private static class LineIterator {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue<String> queue, BufferedReader bufferedReader) {
            this.extraLines = queue;
            this.reader = bufferedReader;
        }

        public boolean hasNext() throws IOException {
            if (this.next != null) {
                return true;
            }
            if (this.extraLines.isEmpty()) {
                do {
                    String readLine = this.reader.readLine();
                    this.next = readLine;
                    if (readLine == null) {
                        return false;
                    }
                    this.next = this.next.trim();
                } while (this.next.isEmpty());
                return true;
            }
            this.next = (String) this.extraLines.poll();
            return true;
        }

        public String next() throws IOException {
            if (!hasNext()) {
                return null;
            }
            String str = this.next;
            this.next = null;
            return str;
        }
    }

    public HlsPlaylist parse(Uri uri, InputStream inputStream) throws IOException {
        Closeable bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        inputStream = new ArrayDeque();
        try {
            if (checkPlaylistHeader(bufferedReader)) {
                String readLine;
                while (true) {
                    readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        readLine = readLine.trim();
                        if (!readLine.isEmpty()) {
                            if (!readLine.startsWith(TAG_STREAM_INF)) {
                                if (readLine.startsWith(TAG_TARGET_DURATION) || readLine.startsWith(TAG_MEDIA_SEQUENCE) || readLine.startsWith(TAG_MEDIA_DURATION) || readLine.startsWith(TAG_KEY) || readLine.startsWith(TAG_BYTERANGE) || readLine.equals(TAG_DISCONTINUITY) || readLine.equals(TAG_DISCONTINUITY_SEQUENCE)) {
                                    break;
                                } else if (readLine.equals(TAG_ENDLIST)) {
                                    break;
                                } else {
                                    inputStream.add(readLine);
                                }
                            } else {
                                inputStream.add(readLine);
                                uri = parseMasterPlaylist(new LineIterator(inputStream, bufferedReader), uri.toString());
                                Util.closeQuietly(bufferedReader);
                                return uri;
                            }
                        }
                    } else {
                        Util.closeQuietly(bufferedReader);
                        throw new ParserException("Failed to parse the playlist, could not identify any tags.");
                    }
                }
                inputStream.add(readLine);
                uri = parseMediaPlaylist(new LineIterator(inputStream, bufferedReader), uri.toString());
                return uri;
            }
            throw new UnrecognizedInputFormatException("Input does not start with the #EXTM3U header.", uri);
        } finally {
            Util.closeQuietly(bufferedReader);
        }
    }

    private static boolean checkPlaylistHeader(BufferedReader bufferedReader) throws IOException {
        int read = bufferedReader.read();
        if (read == 239) {
            if (bufferedReader.read() == 187) {
                if (bufferedReader.read() == 191) {
                    read = bufferedReader.read();
                }
            }
            return false;
        }
        char skipIgnorableWhitespace = skipIgnorableWhitespace(bufferedReader, true, read);
        int length = PLAYLIST_HEADER.length();
        char c = skipIgnorableWhitespace;
        for (read = 0; read < length; read++) {
            if (c != PLAYLIST_HEADER.charAt(read)) {
                return false;
            }
            c = bufferedReader.read();
        }
        return Util.isLinebreak(skipIgnorableWhitespace(bufferedReader, false, c));
    }

    private static int skipIgnorableWhitespace(BufferedReader bufferedReader, boolean z, int i) throws IOException {
        while (i != -1 && Character.isWhitespace(i) && (z || !Util.isLinebreak(i))) {
            i = bufferedReader.read();
        }
        return i;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist parseMasterPlaylist(com.android.morpheustv.player.MyHLSPlaylistParser.LineIterator r28, java.lang.String r29) throws java.io.IOException {
        /*
        r1 = new java.util.HashSet;
        r1.<init>();
        r2 = new java.util.HashMap;
        r2.<init>();
        r6 = new java.util.ArrayList;
        r6.<init>();
        r7 = new java.util.ArrayList;
        r7.<init>();
        r8 = new java.util.ArrayList;
        r8.<init>();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r5 = new java.util.ArrayList;
        r5.<init>();
        r4 = 0;
        r9 = 0;
    L_0x0025:
        r10 = r28.hasNext();
        r11 = 1;
        if (r10 == 0) goto L_0x00f9;
    L_0x002c:
        r10 = r28.next();
        r13 = "#EXT";
        r13 = r10.startsWith(r13);
        if (r13 == 0) goto L_0x003b;
    L_0x0038:
        r5.add(r10);
    L_0x003b:
        r13 = "#EXT-X-MEDIA";
        r13 = r10.startsWith(r13);
        if (r13 == 0) goto L_0x0047;
    L_0x0043:
        r3.add(r10);
        goto L_0x0025;
    L_0x0047:
        r13 = "#EXT-X-STREAM-INF";
        r13 = r10.startsWith(r13);
        if (r13 == 0) goto L_0x0025;
    L_0x004f:
        r13 = "CLOSED-CAPTIONS=NONE";
        r13 = r10.contains(r13);
        r9 = r9 | r13;
        r13 = 400000; // 0x61a80 float:5.6052E-40 double:1.976263E-318;
        r14 = REGEX_BANDWIDTH;	 Catch:{ Exception -> 0x006e }
        r14 = parseLongAttr(r10, r14);	 Catch:{ Exception -> 0x006e }
        r16 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r14 = r14 / r16;
        r16 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r18 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r18 >= 0) goto L_0x006b;
    L_0x006a:
        r13 = (int) r14;
    L_0x006b:
        r19 = r13;
        goto L_0x0076;
    L_0x006e:
        r0 = move-exception;
        r14 = r0;
        r14.printStackTrace();
        r19 = 400000; // 0x61a80 float:5.6052E-40 double:1.976263E-318;
    L_0x0076:
        if (r19 != 0) goto L_0x0079;
    L_0x0078:
        goto L_0x0025;
    L_0x0079:
        r13 = REGEX_CODECS;
        r13 = parseOptionalStringAttr(r10, r13);
        r14 = REGEX_RESOLUTION;
        r14 = parseOptionalStringAttr(r10, r14);
        if (r14 == 0) goto L_0x00a7;
    L_0x0087:
        r15 = "x";
        r14 = r14.split(r15);
        r15 = r14[r4];
        r15 = java.lang.Integer.parseInt(r15);
        r14 = r14[r11];
        r14 = java.lang.Integer.parseInt(r14);
        if (r15 <= 0) goto L_0x00a0;
    L_0x009b:
        if (r14 > 0) goto L_0x009e;
    L_0x009d:
        goto L_0x00a0;
    L_0x009e:
        r12 = r15;
        goto L_0x00a2;
    L_0x00a0:
        r12 = -1;
        r14 = -1;
    L_0x00a2:
        r20 = r12;
        r21 = r14;
        goto L_0x00ab;
    L_0x00a7:
        r20 = -1;
        r21 = -1;
    L_0x00ab:
        r12 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r14 = REGEX_FRAME_RATE;
        r14 = parseOptionalStringAttr(r10, r14);
        if (r14 == 0) goto L_0x00bc;
    L_0x00b5:
        r12 = java.lang.Float.parseFloat(r14);
        r22 = r12;
        goto L_0x00be;
    L_0x00bc:
        r22 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
    L_0x00be:
        r12 = REGEX_AUDIO;
        r10 = parseOptionalStringAttr(r10, r12);
        if (r10 == 0) goto L_0x00cf;
    L_0x00c6:
        if (r13 == 0) goto L_0x00cf;
    L_0x00c8:
        r11 = com.google.android.exoplayer2.util.Util.getCodecsOfType(r13, r11);
        r2.put(r10, r11);
    L_0x00cf:
        r10 = r28.next();
        r11 = r1.add(r10);
        if (r11 == 0) goto L_0x0025;
    L_0x00d9:
        r11 = r6.size();
        r15 = java.lang.Integer.toString(r11);
        r16 = "application/x-mpegURL";
        r17 = 0;
        r23 = 0;
        r24 = 0;
        r18 = r13;
        r11 = com.google.android.exoplayer2.Format.createVideoContainerFormat(r15, r16, r17, r18, r19, r20, r21, r22, r23, r24);
        r12 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r12.<init>(r10, r11);
        r6.add(r12);
        goto L_0x0025;
    L_0x00f9:
        r10 = 0;
        r13 = 0;
        r14 = 0;
    L_0x00fc:
        r15 = r3.size();
        if (r10 >= r15) goto L_0x01f8;
    L_0x0102:
        r15 = r3.get(r10);
        r15 = (java.lang.String) r15;
        r24 = parseSelectionFlags(r15);
        r1 = REGEX_URI;
        r1 = parseOptionalStringAttr(r15, r1);
        r4 = REGEX_NAME;
        r16 = parseStringAttr(r15, r4);
        r4 = REGEX_LANGUAGE;
        r25 = parseOptionalStringAttr(r15, r4);
        r4 = REGEX_GROUP_ID;
        r4 = parseOptionalStringAttr(r15, r4);
        r11 = REGEX_TYPE;
        r11 = parseStringAttr(r15, r11);
        r12 = r11.hashCode();
        r26 = r3;
        r3 = -959297733; // 0xffffffffc6d2473b float:-26915.615 double:NaN;
        r27 = r13;
        r13 = 2;
        if (r12 == r3) goto L_0x0157;
    L_0x0138:
        r3 = -333210994; // 0xffffffffec239a8e float:-7.911391E26 double:NaN;
        if (r12 == r3) goto L_0x014d;
    L_0x013d:
        r3 = 62628790; // 0x3bba3b6 float:1.1028458E-36 double:3.09427336E-316;
        if (r12 == r3) goto L_0x0143;
    L_0x0142:
        goto L_0x0161;
    L_0x0143:
        r3 = "AUDIO";
        r3 = r11.equals(r3);
        if (r3 == 0) goto L_0x0161;
    L_0x014b:
        r3 = 0;
        goto L_0x0162;
    L_0x014d:
        r3 = "CLOSED-CAPTIONS";
        r3 = r11.equals(r3);
        if (r3 == 0) goto L_0x0161;
    L_0x0155:
        r3 = 2;
        goto L_0x0162;
    L_0x0157:
        r3 = "SUBTITLES";
        r3 = r11.equals(r3);
        if (r3 == 0) goto L_0x0161;
    L_0x015f:
        r3 = 1;
        goto L_0x0162;
    L_0x0161:
        r3 = -1;
    L_0x0162:
        switch(r3) {
            case 0: goto L_0x01c2;
            case 1: goto L_0x01a9;
            case 2: goto L_0x0167;
            default: goto L_0x0165;
        };
    L_0x0165:
        goto L_0x01ee;
    L_0x0167:
        r1 = REGEX_INSTREAM_ID;
        r1 = parseStringAttr(r15, r1);
        r3 = "CC";
        r3 = r1.startsWith(r3);
        if (r3 == 0) goto L_0x0184;
    L_0x0175:
        r3 = "application/cea-608";
        r1 = r1.substring(r13);
        r1 = java.lang.Integer.parseInt(r1);
    L_0x017f:
        r23 = r1;
        r18 = r3;
        goto L_0x0190;
    L_0x0184:
        r3 = "application/cea-708";
        r4 = 7;
        r1 = r1.substring(r4);
        r1 = java.lang.Integer.parseInt(r1);
        goto L_0x017f;
    L_0x0190:
        if (r14 != 0) goto L_0x0197;
    L_0x0192:
        r14 = new java.util.ArrayList;
        r14.<init>();
    L_0x0197:
        r17 = 0;
        r19 = 0;
        r20 = -1;
        r21 = r24;
        r22 = r25;
        r1 = com.google.android.exoplayer2.Format.createTextContainerFormat(r16, r17, r18, r19, r20, r21, r22, r23);
        r14.add(r1);
        goto L_0x01ee;
    L_0x01a9:
        r17 = "application/x-mpegURL";
        r18 = "text/vtt";
        r19 = 0;
        r20 = -1;
        r21 = r24;
        r22 = r25;
        r3 = com.google.android.exoplayer2.Format.createTextContainerFormat(r16, r17, r18, r19, r20, r21, r22);
        r4 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r4.<init>(r1, r3);
        r8.add(r4);
        goto L_0x01ee;
    L_0x01c2:
        r3 = r2.get(r4);
        r3 = (java.lang.String) r3;
        if (r3 == 0) goto L_0x01d1;
    L_0x01ca:
        r4 = com.google.android.exoplayer2.util.MimeTypes.getMediaMimeType(r3);
        r18 = r4;
        goto L_0x01d3;
    L_0x01d1:
        r18 = 0;
    L_0x01d3:
        r17 = "application/x-mpegURL";
        r20 = -1;
        r21 = -1;
        r22 = -1;
        r23 = 0;
        r19 = r3;
        r13 = com.google.android.exoplayer2.Format.createAudioContainerFormat(r16, r17, r18, r19, r20, r21, r22, r23, r24, r25);
        if (r1 != 0) goto L_0x01e6;
    L_0x01e5:
        goto L_0x01f0;
    L_0x01e6:
        r3 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r3.<init>(r1, r13);
        r7.add(r3);
    L_0x01ee:
        r13 = r27;
    L_0x01f0:
        r10 = r10 + 1;
        r3 = r26;
        r4 = 0;
        r11 = 1;
        goto L_0x00fc;
    L_0x01f8:
        r27 = r13;
        if (r9 == 0) goto L_0x0202;
    L_0x01fc:
        r1 = java.util.Collections.emptyList();
        r10 = r1;
        goto L_0x0203;
    L_0x0202:
        r10 = r14;
    L_0x0203:
        r1 = new com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
        r3 = r1;
        r4 = r29;
        r9 = r27;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.player.MyHLSPlaylistParser.parseMasterPlaylist(com.android.morpheustv.player.MyHLSPlaylistParser$LineIterator, java.lang.String):com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist");
    }

    private static int parseSelectionFlags(String str) {
        int i = 0;
        int parseBooleanAttribute = parseBooleanAttribute(str, REGEX_DEFAULT, false) | (parseBooleanAttribute(str, REGEX_FORCED, false) ? 2 : 0);
        if (parseBooleanAttribute(str, REGEX_AUTOSELECT, false) != null) {
            i = 4;
        }
        return parseBooleanAttribute | i;
    }

    private static HlsMediaPlaylist parseMediaPlaylist(LineIterator lineIterator, String str) throws IOException {
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        int i = 0;
        int i2 = 1;
        long j = -9223372036854775807L;
        long j2 = j;
        int i3 = 0;
        int i4 = 0;
        long j3 = 0;
        long j4 = 0;
        boolean z = false;
        int i5 = 0;
        long j5 = 0;
        int i6 = 1;
        boolean z2 = false;
        boolean z3 = false;
        DrmInitData drmInitData = null;
        Segment segment = null;
        long j6 = 0;
        boolean z4 = false;
        long j7 = -1;
        long j8 = 0;
        String str2 = null;
        String str3 = null;
        loop0:
        while (true) {
            long j9 = 0;
            while (lineIterator.hasNext()) {
                String next = lineIterator.next();
                if (next.startsWith(TAG_PREFIX)) {
                    arrayList2.add(next);
                }
                if (next.startsWith(TAG_PLAYLIST_TYPE)) {
                    next = parseStringAttr(next, REGEX_PLAYLIST_TYPE);
                    if ("VOD".equals(next)) {
                        i4 = 1;
                    } else if ("EVENT".equals(next)) {
                        i4 = 2;
                    }
                } else if (next.startsWith(TAG_START)) {
                    j = (long) (parseDoubleAttr(next, REGEX_TIME_OFFSET) * 1000000.0d);
                } else if (next.startsWith(TAG_INIT_SEGMENT)) {
                    String parseStringAttr = parseStringAttr(next, REGEX_URI);
                    next = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE);
                    if (next != null) {
                        r2 = next.split("@");
                        j7 = Long.parseLong(r2[i]);
                        if (r2.length > i2) {
                            j6 = Long.parseLong(r2[i2]);
                        }
                    }
                    segment = new Segment(parseStringAttr, j6, j7);
                    j6 = 0;
                    j7 = -1;
                } else if (next.startsWith(TAG_TARGET_DURATION)) {
                    j2 = C0649C.MICROS_PER_SECOND * ((long) parseIntAttr(next, REGEX_TARGET_DURATION));
                } else if (next.startsWith(TAG_MEDIA_SEQUENCE)) {
                    j3 = parseLongAttr(next, REGEX_MEDIA_SEQUENCE);
                    j5 = j3;
                } else if (next.startsWith(TAG_VERSION)) {
                    i6 = parseIntAttr(next, REGEX_VERSION);
                } else if (next.startsWith(TAG_MEDIA_DURATION)) {
                    j9 = (long) (parseDoubleAttr(next, REGEX_MEDIA_DURATION) * 1000000.0d);
                } else {
                    if (next.startsWith(TAG_KEY)) {
                        String parseOptionalStringAttr = parseOptionalStringAttr(next, REGEX_METHOD);
                        String parseOptionalStringAttr2 = parseOptionalStringAttr(next, REGEX_KEYFORMAT);
                        if (METHOD_NONE.equals(parseOptionalStringAttr)) {
                            str2 = null;
                            str3 = null;
                        } else {
                            String str4;
                            String parseOptionalStringAttr3 = parseOptionalStringAttr(next, REGEX_IV);
                            if (!KEYFORMAT_IDENTITY.equals(parseOptionalStringAttr2)) {
                                if (parseOptionalStringAttr2 != null) {
                                    if (parseOptionalStringAttr == null || parseWidevineSchemeData(next, parseOptionalStringAttr2) == null) {
                                        str4 = parseOptionalStringAttr3;
                                        str3 = str4;
                                        str2 = null;
                                    } else {
                                        if (!METHOD_SAMPLE_AES_CENC.equals(parseOptionalStringAttr)) {
                                            if (!METHOD_SAMPLE_AES_CTR.equals(parseOptionalStringAttr)) {
                                                parseOptionalStringAttr = C0649C.CENC_TYPE_cbcs;
                                                str4 = parseOptionalStringAttr3;
                                                drmInitData = new DrmInitData(parseOptionalStringAttr, r2);
                                                str3 = str4;
                                                str2 = null;
                                            }
                                        }
                                        parseOptionalStringAttr = C0649C.CENC_TYPE_cenc;
                                        str4 = parseOptionalStringAttr3;
                                        drmInitData = new DrmInitData(parseOptionalStringAttr, r2);
                                        str3 = str4;
                                        str2 = null;
                                    }
                                }
                            }
                            str4 = parseOptionalStringAttr3;
                            if (METHOD_AES_128.equals(parseOptionalStringAttr)) {
                                str2 = parseStringAttr(next, REGEX_URI);
                                str3 = str4;
                            }
                            str3 = str4;
                            str2 = null;
                        }
                    } else if (next.startsWith(TAG_BYTERANGE)) {
                        r2 = parseStringAttr(next, REGEX_BYTERANGE).split("@");
                        j7 = Long.parseLong(r2[0]);
                        if (r2.length > 1) {
                            j6 = Long.parseLong(r2[1]);
                        }
                    } else if (next.startsWith(TAG_DISCONTINUITY_SEQUENCE)) {
                        i5 = Integer.parseInt(next.substring(next.indexOf(58) + 1));
                        i = 0;
                        i2 = 1;
                        z = true;
                    } else if (next.equals(TAG_DISCONTINUITY)) {
                        i3++;
                    } else if (next.startsWith(TAG_PROGRAM_DATE_TIME)) {
                        if (j4 == 0) {
                            j4 = C0649C.msToUs(Util.parseXsDateTime(next.substring(next.indexOf(58) + 1))) - j8;
                        }
                    } else if (next.equals(TAG_GAP)) {
                        i = 0;
                        i2 = 1;
                        z4 = true;
                    } else if (next.equals(TAG_INDEPENDENT_SEGMENTS)) {
                        i = 0;
                        i2 = 1;
                        z2 = true;
                    } else if (next.equals(TAG_ENDLIST)) {
                        i = 0;
                        i2 = 1;
                        z3 = true;
                    } else if (!next.startsWith("#")) {
                        String toHexString = str2 == null ? null : str3 != null ? str3 : Long.toHexString(j3);
                        long j10 = j3 + 1;
                        if (j7 == -1) {
                            j6 = 0;
                        }
                        arrayList.add(new Segment(next, j9, i3, j8, str2, toHexString, j6, j7, z4));
                        j3 = j8 + j9;
                        if (j7 != -1) {
                            j6 += j7;
                        }
                        j8 = j3;
                        j7 = -1;
                        j3 = j10;
                        i = 0;
                        i2 = 1;
                        z4 = false;
                    }
                    i = 0;
                    i2 = 1;
                }
            }
            break loop0;
        }
        return new HlsMediaPlaylist(i4, str, arrayList2, j, j4, z, i5, j5, i6, j2, z2, z3, j4 != 0, drmInitData, segment, arrayList);
    }

    private static SchemeData parseWidevineSchemeData(String str, String str2) throws ParserException {
        if (KEYFORMAT_WIDEVINE_PSSH_BINARY.equals(str2)) {
            str = parseStringAttr(str, REGEX_URI);
            return new SchemeData(C0649C.WIDEVINE_UUID, MimeTypes.VIDEO_MP4, Base64.decode(str.substring(str.indexOf(44)), 0));
        } else if (KEYFORMAT_WIDEVINE_PSSH_JSON.equals(str2) == null) {
            return null;
        } else {
            try {
                return new SchemeData(C0649C.WIDEVINE_UUID, "hls", str.getBytes("UTF-8"));
            } catch (Throwable e) {
                throw new ParserException(e);
            }
        }
    }

    private static int parseIntAttr(String str, Pattern pattern) throws ParserException {
        return Integer.parseInt(parseStringAttr(str, pattern));
    }

    private static long parseLongAttr(String str, Pattern pattern) throws ParserException {
        return Long.parseLong(parseStringAttr(str, pattern));
    }

    private static double parseDoubleAttr(String str, Pattern pattern) throws ParserException {
        return Double.parseDouble(parseStringAttr(str, pattern));
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern) {
        str = pattern.matcher(str);
        return str.find() != null ? str.group(1) : null;
    }

    private static String parseStringAttr(String str, Pattern pattern) throws ParserException {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Couldn't match ");
        stringBuilder.append(pattern.pattern());
        stringBuilder.append(" in ");
        stringBuilder.append(str);
        throw new ParserException(stringBuilder.toString());
    }

    private static boolean parseBooleanAttribute(String str, Pattern pattern, boolean z) {
        str = pattern.matcher(str);
        return str.find() != null ? str.group(1).equals(BOOLEAN_TRUE) : z;
    }

    private static Pattern compileBooleanAttrPattern(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("=(");
        stringBuilder.append(BOOLEAN_FALSE);
        stringBuilder.append("|");
        stringBuilder.append(BOOLEAN_TRUE);
        stringBuilder.append(")");
        return Pattern.compile(stringBuilder.toString());
    }
}
