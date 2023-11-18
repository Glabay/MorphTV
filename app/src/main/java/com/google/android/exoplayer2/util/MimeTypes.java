package com.google.android.exoplayer2.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

public final class MimeTypes {
    public static final String APPLICATION_CAMERA_MOTION = "application/x-camera-motion";
    public static final String APPLICATION_CEA608 = "application/cea-608";
    public static final String APPLICATION_CEA708 = "application/cea-708";
    public static final String APPLICATION_DVBSUBS = "application/dvbsubs";
    public static final String APPLICATION_EMSG = "application/x-emsg";
    public static final String APPLICATION_EXIF = "application/x-exif";
    public static final String APPLICATION_ID3 = "application/id3";
    public static final String APPLICATION_M3U8 = "application/x-mpegURL";
    public static final String APPLICATION_MP4 = "application/mp4";
    public static final String APPLICATION_MP4CEA608 = "application/x-mp4-cea-608";
    public static final String APPLICATION_MP4VTT = "application/x-mp4-vtt";
    public static final String APPLICATION_MPD = "application/dash+xml";
    public static final String APPLICATION_PGS = "application/pgs";
    public static final String APPLICATION_RAWCC = "application/x-rawcc";
    public static final String APPLICATION_SCTE35 = "application/x-scte35";
    public static final String APPLICATION_SS = "application/vnd.ms-sstr+xml";
    public static final String APPLICATION_SUBRIP = "application/x-subrip";
    public static final String APPLICATION_TTML = "application/ttml+xml";
    public static final String APPLICATION_TX3G = "application/x-quicktime-tx3g";
    public static final String APPLICATION_VOBSUB = "application/vobsub";
    public static final String APPLICATION_WEBM = "application/webm";
    public static final String AUDIO_AAC = "audio/mp4a-latm";
    public static final String AUDIO_AC3 = "audio/ac3";
    public static final String AUDIO_ALAC = "audio/alac";
    public static final String AUDIO_ALAW = "audio/g711-alaw";
    public static final String AUDIO_AMR_NB = "audio/3gpp";
    public static final String AUDIO_AMR_WB = "audio/amr-wb";
    public static final String AUDIO_DTS = "audio/vnd.dts";
    public static final String AUDIO_DTS_EXPRESS = "audio/vnd.dts.hd;profile=lbr";
    public static final String AUDIO_DTS_HD = "audio/vnd.dts.hd";
    public static final String AUDIO_E_AC3 = "audio/eac3";
    public static final String AUDIO_E_AC3_JOC = "audio/eac3-joc";
    public static final String AUDIO_FLAC = "audio/flac";
    public static final String AUDIO_MLAW = "audio/g711-mlaw";
    public static final String AUDIO_MP4 = "audio/mp4";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_MPEG_L1 = "audio/mpeg-L1";
    public static final String AUDIO_MPEG_L2 = "audio/mpeg-L2";
    public static final String AUDIO_MSGSM = "audio/gsm";
    public static final String AUDIO_OPUS = "audio/opus";
    public static final String AUDIO_RAW = "audio/raw";
    public static final String AUDIO_TRUEHD = "audio/true-hd";
    public static final String AUDIO_UNKNOWN = "audio/x-unknown";
    public static final String AUDIO_VORBIS = "audio/vorbis";
    public static final String AUDIO_WEBM = "audio/webm";
    public static final String BASE_TYPE_APPLICATION = "application";
    public static final String BASE_TYPE_AUDIO = "audio";
    public static final String BASE_TYPE_TEXT = "text";
    public static final String BASE_TYPE_VIDEO = "video";
    public static final String TEXT_SSA = "text/x-ssa";
    public static final String TEXT_VTT = "text/vtt";
    public static final String VIDEO_H263 = "video/3gpp";
    public static final String VIDEO_H264 = "video/avc";
    public static final String VIDEO_H265 = "video/hevc";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String VIDEO_MP4V = "video/mp4v-es";
    public static final String VIDEO_MPEG = "video/mpeg";
    public static final String VIDEO_MPEG2 = "video/mpeg2";
    public static final String VIDEO_UNKNOWN = "video/x-unknown";
    public static final String VIDEO_VC1 = "video/wvc1";
    public static final String VIDEO_VP8 = "video/x-vnd.on2.vp8";
    public static final String VIDEO_VP9 = "video/x-vnd.on2.vp9";
    public static final String VIDEO_WEBM = "video/webm";

    @Nullable
    public static String getMimeTypeFromMp4ObjectType(int i) {
        switch (i) {
            case 32:
                return VIDEO_MP4V;
            case 33:
                return VIDEO_H264;
            case 35:
                return VIDEO_H265;
            case 64:
            case 102:
            case 103:
            case 104:
                return AUDIO_AAC;
            case 96:
            case 97:
                return VIDEO_MPEG2;
            case 105:
            case 107:
                return AUDIO_MPEG;
            case 165:
                return AUDIO_AC3;
            case 166:
                return AUDIO_E_AC3;
            case 169:
            case 172:
                return AUDIO_DTS;
            case 170:
            case 171:
                return AUDIO_DTS_HD;
            case 173:
                return AUDIO_OPUS;
            default:
                return null;
        }
    }

    private MimeTypes() {
    }

    public static boolean isAudio(String str) {
        return BASE_TYPE_AUDIO.equals(getTopLevelType(str));
    }

    public static boolean isVideo(String str) {
        return BASE_TYPE_VIDEO.equals(getTopLevelType(str));
    }

    public static boolean isText(String str) {
        return BASE_TYPE_TEXT.equals(getTopLevelType(str));
    }

    public static boolean isApplication(String str) {
        return BASE_TYPE_APPLICATION.equals(getTopLevelType(str));
    }

    public static String getVideoMediaMimeType(String str) {
        if (str == null) {
            return null;
        }
        for (String mediaMimeType : str.split(",")) {
            String mediaMimeType2 = getMediaMimeType(mediaMimeType2);
            if (mediaMimeType2 != null && isVideo(mediaMimeType2)) {
                return mediaMimeType2;
            }
        }
        return null;
    }

    public static String getAudioMediaMimeType(String str) {
        if (str == null) {
            return null;
        }
        for (String mediaMimeType : str.split(",")) {
            String mediaMimeType2 = getMediaMimeType(mediaMimeType2);
            if (mediaMimeType2 != null && isAudio(mediaMimeType2)) {
                return mediaMimeType2;
            }
        }
        return null;
    }

    public static java.lang.String getMediaMimeType(java.lang.String r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = 0;
        if (r3 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r3 = r3.trim();
        r1 = "avc1";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x0101;
    L_0x0010:
        r1 = "avc3";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x001a;
    L_0x0018:
        goto L_0x0101;
    L_0x001a:
        r1 = "hev1";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x00fe;
    L_0x0022:
        r1 = "hvc1";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x002c;
    L_0x002a:
        goto L_0x00fe;
    L_0x002c:
        r1 = "vp9";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x00fb;
    L_0x0034:
        r1 = "vp09";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x003e;
    L_0x003c:
        goto L_0x00fb;
    L_0x003e:
        r1 = "vp8";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x00f8;
    L_0x0046:
        r1 = "vp08";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x0050;
    L_0x004e:
        goto L_0x00f8;
    L_0x0050:
        r1 = "mp4a";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x0086;
    L_0x0058:
        r1 = "mp4a.";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x0080;
    L_0x0060:
        r1 = 5;
        r3 = r3.substring(r1);
        r1 = r3.length();
        r2 = 2;
        if (r1 < r2) goto L_0x0080;
    L_0x006c:
        r1 = 0;
        r3 = r3.substring(r1, r2);	 Catch:{ NumberFormatException -> 0x0080 }
        r3 = com.google.android.exoplayer2.util.Util.toUpperInvariant(r3);	 Catch:{ NumberFormatException -> 0x0080 }
        r1 = 16;	 Catch:{ NumberFormatException -> 0x0080 }
        r3 = java.lang.Integer.parseInt(r3, r1);	 Catch:{ NumberFormatException -> 0x0080 }
        r3 = getMimeTypeFromMp4ObjectType(r3);	 Catch:{ NumberFormatException -> 0x0080 }
        goto L_0x0081;
    L_0x0080:
        r3 = r0;
    L_0x0081:
        if (r3 != 0) goto L_0x0085;
    L_0x0083:
        r3 = "audio/mp4a-latm";
    L_0x0085:
        return r3;
    L_0x0086:
        r1 = "ac-3";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x00f5;
    L_0x008e:
        r1 = "dac3";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x0097;
    L_0x0096:
        goto L_0x00f5;
    L_0x0097:
        r1 = "ec-3";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x00f2;
    L_0x009f:
        r1 = "dec3";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x00a8;
    L_0x00a7:
        goto L_0x00f2;
    L_0x00a8:
        r1 = "ec+3";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x00b3;
    L_0x00b0:
        r3 = "audio/eac3-joc";
        return r3;
    L_0x00b3:
        r1 = "dtsc";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x00ef;
    L_0x00bb:
        r1 = "dtse";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x00c4;
    L_0x00c3:
        goto L_0x00ef;
    L_0x00c4:
        r1 = "dtsh";
        r1 = r3.startsWith(r1);
        if (r1 != 0) goto L_0x00ec;
    L_0x00cc:
        r1 = "dtsl";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x00d5;
    L_0x00d4:
        goto L_0x00ec;
    L_0x00d5:
        r1 = "opus";
        r1 = r3.startsWith(r1);
        if (r1 == 0) goto L_0x00e0;
    L_0x00dd:
        r3 = "audio/opus";
        return r3;
    L_0x00e0:
        r1 = "vorbis";
        r3 = r3.startsWith(r1);
        if (r3 == 0) goto L_0x00eb;
    L_0x00e8:
        r3 = "audio/vorbis";
        return r3;
    L_0x00eb:
        return r0;
    L_0x00ec:
        r3 = "audio/vnd.dts.hd";
        return r3;
    L_0x00ef:
        r3 = "audio/vnd.dts";
        return r3;
    L_0x00f2:
        r3 = "audio/eac3";
        return r3;
    L_0x00f5:
        r3 = "audio/ac3";
        return r3;
    L_0x00f8:
        r3 = "video/x-vnd.on2.vp8";
        return r3;
    L_0x00fb:
        r3 = "video/x-vnd.on2.vp9";
        return r3;
    L_0x00fe:
        r3 = "video/hevc";
        return r3;
    L_0x0101:
        r3 = "video/avc";
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.MimeTypes.getMediaMimeType(java.lang.String):java.lang.String");
    }

    public static int getTrackType(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (isAudio(str)) {
            return 1;
        }
        if (isVideo(str)) {
            return 2;
        }
        if (!(isText(str) || APPLICATION_CEA608.equals(str) || APPLICATION_CEA708.equals(str) || APPLICATION_MP4CEA608.equals(str) || APPLICATION_SUBRIP.equals(str) || APPLICATION_TTML.equals(str) || APPLICATION_TX3G.equals(str) || APPLICATION_MP4VTT.equals(str) || APPLICATION_RAWCC.equals(str) || APPLICATION_VOBSUB.equals(str) || APPLICATION_PGS.equals(str))) {
            if (!APPLICATION_DVBSUBS.equals(str)) {
                if (!(APPLICATION_ID3.equals(str) || APPLICATION_EMSG.equals(str) || APPLICATION_SCTE35.equals(str))) {
                    if (APPLICATION_CAMERA_MOTION.equals(str) == null) {
                        return -1;
                    }
                }
                return 4;
            }
        }
        return 3;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getEncoding(java.lang.String r3) {
        /*
        r0 = r3.hashCode();
        r1 = 0;
        r2 = 5;
        switch(r0) {
            case -2123537834: goto L_0x003c;
            case -1095064472: goto L_0x0032;
            case 187078296: goto L_0x0028;
            case 1504578661: goto L_0x001e;
            case 1505942594: goto L_0x0014;
            case 1556697186: goto L_0x000a;
            default: goto L_0x0009;
        };
    L_0x0009:
        goto L_0x0046;
    L_0x000a:
        r0 = "audio/true-hd";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0012:
        r3 = 5;
        goto L_0x0047;
    L_0x0014:
        r0 = "audio/vnd.dts.hd";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x001c:
        r3 = 4;
        goto L_0x0047;
    L_0x001e:
        r0 = "audio/eac3";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0026:
        r3 = 1;
        goto L_0x0047;
    L_0x0028:
        r0 = "audio/ac3";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0030:
        r3 = 0;
        goto L_0x0047;
    L_0x0032:
        r0 = "audio/vnd.dts";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x003a:
        r3 = 3;
        goto L_0x0047;
    L_0x003c:
        r0 = "audio/eac3-joc";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0044:
        r3 = 2;
        goto L_0x0047;
    L_0x0046:
        r3 = -1;
    L_0x0047:
        switch(r3) {
            case 0: goto L_0x0055;
            case 1: goto L_0x0053;
            case 2: goto L_0x0053;
            case 3: goto L_0x0051;
            case 4: goto L_0x004e;
            case 5: goto L_0x004b;
            default: goto L_0x004a;
        };
    L_0x004a:
        return r1;
    L_0x004b:
        r3 = 14;
        return r3;
    L_0x004e:
        r3 = 8;
        return r3;
    L_0x0051:
        r3 = 7;
        return r3;
    L_0x0053:
        r3 = 6;
        return r3;
    L_0x0055:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.MimeTypes.getEncoding(java.lang.String):int");
    }

    public static int getTrackTypeOfCodec(String str) {
        return getTrackType(getMediaMimeType(str));
    }

    private static String getTopLevelType(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(47);
        if (indexOf != -1) {
            return str.substring(0, indexOf);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid mime type: ");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
