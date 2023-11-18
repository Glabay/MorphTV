package com.google.android.exoplayer2.mediacodec;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.uwetrottmann.trakt5.TraktV2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"InlinedApi"})
@TargetApi(16)
public final class MediaCodecUtil {
    private static final SparseIntArray AVC_LEVEL_NUMBER_TO_CONST = new SparseIntArray();
    private static final SparseIntArray AVC_PROFILE_NUMBER_TO_CONST = new SparseIntArray();
    private static final String CODEC_ID_AVC1 = "avc1";
    private static final String CODEC_ID_AVC2 = "avc2";
    private static final String CODEC_ID_HEV1 = "hev1";
    private static final String CODEC_ID_HVC1 = "hvc1";
    private static final String GOOGLE_RAW_DECODER_NAME = "OMX.google.raw.decoder";
    private static final Map<String, Integer> HEVC_CODEC_STRING_TO_PROFILE_LEVEL = new HashMap();
    private static final String MTK_RAW_DECODER_NAME = "OMX.MTK.AUDIO.DECODER.RAW";
    private static final MediaCodecInfo PASSTHROUGH_DECODER_INFO = MediaCodecInfo.newPassthroughInstance(GOOGLE_RAW_DECODER_NAME);
    private static final Pattern PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
    private static final String TAG = "MediaCodecUtil";
    private static final HashMap<CodecKey, List<MediaCodecInfo>> decoderInfosCache = new HashMap();
    private static int maxH264DecodableFrameSize = -1;

    private static final class CodecKey {
        public final String mimeType;
        public final boolean secure;

        public CodecKey(String str, boolean z) {
            this.mimeType = str;
            this.secure = z;
        }

        public int hashCode() {
            return (((this.mimeType == null ? 0 : this.mimeType.hashCode()) + 31) * 31) + (this.secure ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (obj.getClass() == CodecKey.class) {
                    CodecKey codecKey = (CodecKey) obj;
                    if (!TextUtils.equals(this.mimeType, codecKey.mimeType) || this.secure != codecKey.secure) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }
    }

    public static class DecoderQueryException extends Exception {
        private DecoderQueryException(Throwable th) {
            super("Failed to query underlying media codecs", th);
        }
    }

    private interface MediaCodecListCompat {
        int getCodecCount();

        MediaCodecInfo getCodecInfoAt(int i);

        boolean isSecurePlaybackSupported(String str, CodecCapabilities codecCapabilities);

        boolean secureDecodersExplicit();
    }

    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat {
        public boolean secureDecodersExplicit() {
            return false;
        }

        private MediaCodecListCompatV16() {
        }

        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        public MediaCodecInfo getCodecInfoAt(int i) {
            return MediaCodecList.getCodecInfoAt(i);
        }

        public boolean isSecurePlaybackSupported(String str, CodecCapabilities codecCapabilities) {
            return MimeTypes.VIDEO_H264.equals(str);
        }
    }

    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final int codecKind;
        private MediaCodecInfo[] mediaCodecInfos;

        public boolean secureDecodersExplicit() {
            return true;
        }

        public MediaCodecListCompatV21(boolean z) {
            this.codecKind = z;
        }

        public int getCodecCount() {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }

        public MediaCodecInfo getCodecInfoAt(int i) {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[i];
        }

        public boolean isSecurePlaybackSupported(String str, CodecCapabilities codecCapabilities) {
            return codecCapabilities.isFeatureSupported("secure-playback");
        }

        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
    }

    private static int avcLevelToMaxFrameSize(int i) {
        switch (i) {
            case 1:
                return 25344;
            case 2:
                return 25344;
            case 8:
                return 101376;
            case 16:
                return 101376;
            case 32:
                return 101376;
            case 64:
                return 202752;
            case 128:
                return 414720;
            case 256:
                return 414720;
            case 512:
                return 921600;
            case 1024:
                return 1310720;
            case 2048:
                return 2097152;
            case 4096:
                return 2097152;
            case 8192:
                return 2228224;
            case 16384:
                return 5652480;
            case 32768:
                return 9437184;
            case 65536:
                return 9437184;
            default:
                return -1;
        }
    }

    static {
        AVC_PROFILE_NUMBER_TO_CONST.put(66, 1);
        AVC_PROFILE_NUMBER_TO_CONST.put(77, 2);
        AVC_PROFILE_NUMBER_TO_CONST.put(88, 4);
        AVC_PROFILE_NUMBER_TO_CONST.put(100, 8);
        AVC_LEVEL_NUMBER_TO_CONST.put(10, 1);
        AVC_LEVEL_NUMBER_TO_CONST.put(11, 4);
        AVC_LEVEL_NUMBER_TO_CONST.put(12, 8);
        AVC_LEVEL_NUMBER_TO_CONST.put(13, 16);
        AVC_LEVEL_NUMBER_TO_CONST.put(20, 32);
        AVC_LEVEL_NUMBER_TO_CONST.put(21, 64);
        AVC_LEVEL_NUMBER_TO_CONST.put(22, 128);
        AVC_LEVEL_NUMBER_TO_CONST.put(30, 256);
        AVC_LEVEL_NUMBER_TO_CONST.put(31, 512);
        AVC_LEVEL_NUMBER_TO_CONST.put(32, 1024);
        AVC_LEVEL_NUMBER_TO_CONST.put(40, 2048);
        AVC_LEVEL_NUMBER_TO_CONST.put(41, 4096);
        AVC_LEVEL_NUMBER_TO_CONST.put(42, 8192);
        AVC_LEVEL_NUMBER_TO_CONST.put(50, 16384);
        AVC_LEVEL_NUMBER_TO_CONST.put(51, 32768);
        AVC_LEVEL_NUMBER_TO_CONST.put(52, 65536);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L30", Integer.valueOf(1));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L60", Integer.valueOf(4));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L63", Integer.valueOf(16));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L90", Integer.valueOf(64));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L93", Integer.valueOf(256));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L120", Integer.valueOf(1024));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L123", Integer.valueOf(4096));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L150", Integer.valueOf(16384));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L153", Integer.valueOf(65536));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L156", Integer.valueOf(262144));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L180", Integer.valueOf(1048576));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L183", Integer.valueOf(4194304));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L186", Integer.valueOf(16777216));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H30", Integer.valueOf(2));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H60", Integer.valueOf(8));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H63", Integer.valueOf(32));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H90", Integer.valueOf(128));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H93", Integer.valueOf(512));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H120", Integer.valueOf(2048));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H123", Integer.valueOf(8192));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H150", Integer.valueOf(32768));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H153", Integer.valueOf(131072));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H156", Integer.valueOf(524288));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H180", Integer.valueOf(2097152));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H183", Integer.valueOf(8388608));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H186", Integer.valueOf(33554432));
    }

    private MediaCodecUtil() {
    }

    public static void warmDecoderInfoCache(String str, boolean z) {
        try {
            getDecoderInfos(str, z);
        } catch (String str2) {
            Log.e(TAG, "Codec warming failed", str2);
        }
    }

    public static MediaCodecInfo getPassthroughDecoderInfo() {
        return PASSTHROUGH_DECODER_INFO;
    }

    @Nullable
    public static MediaCodecInfo getDecoderInfo(String str, boolean z) throws DecoderQueryException {
        str = getDecoderInfos(str, z);
        return str.isEmpty() ? null : (MediaCodecInfo) str.get(false);
    }

    public static synchronized List<MediaCodecInfo> getDecoderInfos(String str, boolean z) throws DecoderQueryException {
        synchronized (MediaCodecUtil.class) {
            CodecKey codecKey = new CodecKey(str, z);
            List<MediaCodecInfo> list = (List) decoderInfosCache.get(codecKey);
            if (list != null) {
                return list;
            }
            MediaCodecListCompat mediaCodecListCompatV21 = Util.SDK_INT >= 21 ? new MediaCodecListCompatV21(z) : new MediaCodecListCompatV16();
            List decoderInfosInternal = getDecoderInfosInternal(codecKey, mediaCodecListCompatV21, str);
            if (z && decoderInfosInternal.isEmpty() && true <= Util.SDK_INT && Util.SDK_INT <= true) {
                mediaCodecListCompatV21 = new MediaCodecListCompatV16();
                decoderInfosInternal = getDecoderInfosInternal(codecKey, mediaCodecListCompatV21, str);
                if (!decoderInfosInternal.isEmpty()) {
                    z = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MediaCodecList API didn't list secure decoder for: ");
                    stringBuilder.append(str);
                    stringBuilder.append(". Assuming: ");
                    stringBuilder.append(((MediaCodecInfo) decoderInfosInternal.get(0)).name);
                    Log.w(z, stringBuilder.toString());
                }
            }
            if (MimeTypes.AUDIO_E_AC3_JOC.equals(str)) {
                decoderInfosInternal.addAll(getDecoderInfosInternal(new CodecKey(MimeTypes.AUDIO_E_AC3, codecKey.secure), mediaCodecListCompatV21, str));
            }
            applyWorkarounds(decoderInfosInternal);
            str = Collections.unmodifiableList(decoderInfosInternal);
            decoderInfosCache.put(codecKey, str);
            return str;
        }
    }

    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        if (maxH264DecodableFrameSize == -1) {
            int i = 0;
            MediaCodecInfo decoderInfo = getDecoderInfo(MimeTypes.VIDEO_H264, false);
            if (decoderInfo != null) {
                CodecProfileLevel[] profileLevels = decoderInfo.getProfileLevels();
                int length = profileLevels.length;
                int i2 = 0;
                while (i < length) {
                    i2 = Math.max(avcLevelToMaxFrameSize(profileLevels[i].level), i2);
                    i++;
                }
                i = Math.max(i2, Util.SDK_INT >= 21 ? 345600 : 172800);
            }
            maxH264DecodableFrameSize = i;
        }
        return maxH264DecodableFrameSize;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.util.Pair<java.lang.Integer, java.lang.Integer> getCodecProfileAndLevel(java.lang.String r6) {
        /*
        r0 = 0;
        if (r6 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = "\\.";
        r1 = r6.split(r1);
        r2 = 0;
        r3 = r1[r2];
        r4 = -1;
        r5 = r3.hashCode();
        switch(r5) {
            case 3006243: goto L_0x0033;
            case 3006244: goto L_0x0029;
            case 3199032: goto L_0x0020;
            case 3214780: goto L_0x0016;
            default: goto L_0x0015;
        };
    L_0x0015:
        goto L_0x003d;
    L_0x0016:
        r2 = "hvc1";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x003d;
    L_0x001e:
        r2 = 1;
        goto L_0x003e;
    L_0x0020:
        r5 = "hev1";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x003d;
    L_0x0028:
        goto L_0x003e;
    L_0x0029:
        r2 = "avc2";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x003d;
    L_0x0031:
        r2 = 3;
        goto L_0x003e;
    L_0x0033:
        r2 = "avc1";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x003d;
    L_0x003b:
        r2 = 2;
        goto L_0x003e;
    L_0x003d:
        r2 = -1;
    L_0x003e:
        switch(r2) {
            case 0: goto L_0x0047;
            case 1: goto L_0x0047;
            case 2: goto L_0x0042;
            case 3: goto L_0x0042;
            default: goto L_0x0041;
        };
    L_0x0041:
        return r0;
    L_0x0042:
        r6 = getAvcProfileAndLevel(r6, r1);
        return r6;
    L_0x0047:
        r6 = getHevcProfileAndLevel(r6, r1);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.getCodecProfileAndLevel(java.lang.String):android.util.Pair<java.lang.Integer, java.lang.Integer>");
    }

    private static ArrayList<MediaCodecInfo> getDecoderInfosInternal(CodecKey codecKey, MediaCodecListCompat mediaCodecListCompat, String str) throws DecoderQueryException {
        int i;
        MediaCodecInfo mediaCodecInfo;
        Exception e;
        Exception exception;
        String str2;
        StringBuilder stringBuilder;
        CodecKey codecKey2 = codecKey;
        MediaCodecListCompat mediaCodecListCompat2 = mediaCodecListCompat;
        try {
            ArrayList<MediaCodecInfo> arrayList = new ArrayList();
            String str3 = codecKey2.mimeType;
            int codecCount = mediaCodecListCompat.getCodecCount();
            boolean secureDecodersExplicit = mediaCodecListCompat.secureDecodersExplicit();
            int i2 = 0;
            while (i2 < codecCount) {
                MediaCodecInfo codecInfoAt = mediaCodecListCompat2.getCodecInfoAt(i2);
                String name = codecInfoAt.getName();
                if (isCodecUsableDecoder(codecInfoAt, name, secureDecodersExplicit, str)) {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    int length = supportedTypes.length;
                    int i3 = 0;
                    while (i3 < length) {
                        String str4 = supportedTypes[i3];
                        if (str4.equalsIgnoreCase(str3)) {
                            try {
                                CodecCapabilities capabilitiesForType = codecInfoAt.getCapabilitiesForType(str4);
                                i = codecCount;
                                StringBuilder stringBuilder2;
                                try {
                                    boolean isSecurePlaybackSupported = mediaCodecListCompat2.isSecurePlaybackSupported(str3, capabilitiesForType);
                                    boolean codecNeedsDisableAdaptationWorkaround = codecNeedsDisableAdaptationWorkaround(name);
                                    if (secureDecodersExplicit) {
                                        mediaCodecInfo = codecInfoAt;
                                        try {
                                            if (codecKey2.secure != isSecurePlaybackSupported) {
                                                if (!secureDecodersExplicit || codecKey2.secure) {
                                                    if (!secureDecodersExplicit && isSecurePlaybackSupported) {
                                                        stringBuilder2 = new StringBuilder();
                                                        stringBuilder2.append(name);
                                                        stringBuilder2.append(".secure");
                                                        arrayList.add(MediaCodecInfo.newInstance(stringBuilder2.toString(), str3, capabilitiesForType, codecNeedsDisableAdaptationWorkaround, true));
                                                        return arrayList;
                                                    }
                                                }
                                            }
                                        } catch (Exception e2) {
                                            e = e2;
                                            exception = e;
                                            if (Util.SDK_INT > 23) {
                                            }
                                            str2 = TAG;
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append("Failed to query codec ");
                                            stringBuilder.append(name);
                                            stringBuilder.append(" (");
                                            stringBuilder.append(str4);
                                            stringBuilder.append(")");
                                            Log.e(str2, stringBuilder.toString());
                                            throw exception;
                                        }
                                    }
                                    mediaCodecInfo = codecInfoAt;
                                    if (secureDecodersExplicit) {
                                    }
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(name);
                                    stringBuilder2.append(".secure");
                                    arrayList.add(MediaCodecInfo.newInstance(stringBuilder2.toString(), str3, capabilitiesForType, codecNeedsDisableAdaptationWorkaround, true));
                                    return arrayList;
                                    arrayList.add(MediaCodecInfo.newInstance(name, str3, capabilitiesForType, codecNeedsDisableAdaptationWorkaround, false));
                                } catch (Exception e3) {
                                    e = e3;
                                    mediaCodecInfo = codecInfoAt;
                                    exception = e;
                                    if (Util.SDK_INT > 23 || arrayList.isEmpty()) {
                                        str2 = TAG;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("Failed to query codec ");
                                        stringBuilder.append(name);
                                        stringBuilder.append(" (");
                                        stringBuilder.append(str4);
                                        stringBuilder.append(")");
                                        Log.e(str2, stringBuilder.toString());
                                        throw exception;
                                    }
                                    String str5 = TAG;
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("Skipping codec ");
                                    stringBuilder2.append(name);
                                    stringBuilder2.append(" (failed to query capabilities)");
                                    Log.e(str5, stringBuilder2.toString());
                                    i3++;
                                    codecCount = i;
                                    codecInfoAt = mediaCodecInfo;
                                    mediaCodecListCompat2 = mediaCodecListCompat;
                                }
                            } catch (Exception e4) {
                                e = e4;
                                i = codecCount;
                                mediaCodecInfo = codecInfoAt;
                                exception = e;
                                if (Util.SDK_INT > 23) {
                                }
                                str2 = TAG;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Failed to query codec ");
                                stringBuilder.append(name);
                                stringBuilder.append(" (");
                                stringBuilder.append(str4);
                                stringBuilder.append(")");
                                Log.e(str2, stringBuilder.toString());
                                throw exception;
                            }
                        }
                        i = codecCount;
                        mediaCodecInfo = codecInfoAt;
                        i3++;
                        codecCount = i;
                        codecInfoAt = mediaCodecInfo;
                        mediaCodecListCompat2 = mediaCodecListCompat;
                    }
                    continue;
                }
                i2++;
                codecCount = codecCount;
                mediaCodecListCompat2 = mediaCodecListCompat;
            }
            return arrayList;
        } catch (Throwable e5) {
            throw new DecoderQueryException(e5);
        }
    }

    private static boolean isCodecUsableDecoder(MediaCodecInfo mediaCodecInfo, String str, boolean z, String str2) {
        if (mediaCodecInfo.isEncoder() == null) {
            if (z || str.endsWith(".secure") == null) {
                if (Util.SDK_INT < true && ("CIPAACDecoder".equals(str) != null || "CIPMP3Decoder".equals(str) != null || "CIPVorbisDecoder".equals(str) != null || "CIPAMRNBDecoder".equals(str) != null || "AACDecoder".equals(str) != null || "MP3Decoder".equals(str) != null)) {
                    return false;
                }
                if (Util.SDK_INT < 18 && "OMX.SEC.MP3.Decoder".equals(str) != null) {
                    return false;
                }
                if (Util.SDK_INT < 18 && "OMX.MTK.AUDIO.DECODER.AAC".equals(str) != null && ("a70".equals(Util.DEVICE) != null || ("Xiaomi".equals(Util.MANUFACTURER) != null && Util.DEVICE.startsWith("HM") != null))) {
                    return false;
                }
                if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.mp3".equals(str) != null && ("dlxu".equals(Util.DEVICE) != null || "protou".equals(Util.DEVICE) != null || "ville".equals(Util.DEVICE) != null || "villeplus".equals(Util.DEVICE) != null || "villec2".equals(Util.DEVICE) != null || Util.DEVICE.startsWith("gee") != null || "C6602".equals(Util.DEVICE) != null || "C6603".equals(Util.DEVICE) != null || "C6606".equals(Util.DEVICE) != null || "C6616".equals(Util.DEVICE) != null || "L36h".equals(Util.DEVICE) != null || "SO-02E".equals(Util.DEVICE) != null)) {
                    return false;
                }
                if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.aac".equals(str) != null && ("C1504".equals(Util.DEVICE) != null || "C1505".equals(Util.DEVICE) != null || "C1604".equals(Util.DEVICE) != null || "C1605".equals(Util.DEVICE) != null)) {
                    return false;
                }
                if (Util.SDK_INT < true && (("OMX.SEC.aac.dec".equals(str) != null || "OMX.Exynos.AAC.Decoder".equals(str) != null) && Util.MANUFACTURER.equals("samsung") != null && (Util.DEVICE.startsWith("zeroflte") != null || Util.DEVICE.startsWith("zerolte") != null || Util.DEVICE.startsWith("zenlte") != null || Util.DEVICE.equals("SC-05G") != null || Util.DEVICE.equals("marinelteatt") != null || Util.DEVICE.equals("404SC") != null || Util.DEVICE.equals("SC-04G") != null || Util.DEVICE.equals("SCV31") != null))) {
                    return false;
                }
                if (Util.SDK_INT <= 19 && "OMX.SEC.vp8.dec".equals(str) != null && "samsung".equals(Util.MANUFACTURER) != null && (Util.DEVICE.startsWith("d2") != null || Util.DEVICE.startsWith("serrano") != null || Util.DEVICE.startsWith("jflte") != null || Util.DEVICE.startsWith("santos") != null || Util.DEVICE.startsWith("t0") != null)) {
                    return false;
                }
                if (Util.SDK_INT <= 19 && Util.DEVICE.startsWith("jflte") != null && "OMX.qcom.video.decoder.vp8".equals(str) != null) {
                    return false;
                }
                if (MimeTypes.AUDIO_E_AC3_JOC.equals(str2) == null || "OMX.MTK.AUDIO.DECODER.DSPAC3".equals(str) == null) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static void applyWorkarounds(List<MediaCodecInfo> list) {
        if (Util.SDK_INT < 26) {
            int i = 1;
            if (list.size() > 1 && MTK_RAW_DECODER_NAME.equals(((MediaCodecInfo) list.get(0)).name)) {
                while (i < list.size()) {
                    MediaCodecInfo mediaCodecInfo = (MediaCodecInfo) list.get(i);
                    if (GOOGLE_RAW_DECODER_NAME.equals(mediaCodecInfo.name)) {
                        list.remove(i);
                        list.add(0, mediaCodecInfo);
                        return;
                    }
                    i++;
                }
            }
        }
    }

    private static boolean codecNeedsDisableAdaptationWorkaround(String str) {
        return (Util.SDK_INT > 22 || (!(Util.MODEL.equals("ODROID-XU3") || Util.MODEL.equals("Nexus 10")) || (!"OMX.Exynos.AVC.Decoder".equals(str) && "OMX.Exynos.AVC.Decoder.secure".equals(str) == null))) ? null : true;
    }

    private static Pair<Integer, Integer> getHevcProfileAndLevel(String str, String[] strArr) {
        if (strArr.length < 4) {
            strArr = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring malformed HEVC codec string: ");
            stringBuilder.append(str);
            Log.w(strArr, stringBuilder.toString());
            return null;
        }
        Matcher matcher = PROFILE_PATTERN.matcher(strArr[1]);
        if (matcher.matches()) {
            str = matcher.group(1);
            if ("1".equals(str)) {
                str = true;
            } else if (TraktV2.API_VERSION.equals(str)) {
                str = 2;
            } else {
                strArr = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown HEVC profile string: ");
                stringBuilder.append(str);
                Log.w(strArr, stringBuilder.toString());
                return null;
            }
            Integer num = (Integer) HEVC_CODEC_STRING_TO_PROFILE_LEVEL.get(strArr[3]);
            if (num != null) {
                return new Pair(Integer.valueOf(str), num);
            }
            str = TAG;
            strArr = new StringBuilder();
            strArr.append("Unknown HEVC level string: ");
            strArr.append(matcher.group(1));
            Log.w(str, strArr.toString());
            return null;
        }
        strArr = TAG;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Ignoring malformed HEVC codec string: ");
        stringBuilder.append(str);
        Log.w(strArr, stringBuilder.toString());
        return null;
    }

    private static android.util.Pair<java.lang.Integer, java.lang.Integer> getAvcProfileAndLevel(java.lang.String r5, java.lang.String[] r6) {
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
        r0 = r6.length;
        r1 = 2;
        r2 = 0;
        if (r0 >= r1) goto L_0x001c;
    L_0x0005:
        r6 = "MediaCodecUtil";
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Ignoring malformed AVC codec string: ";
        r0.append(r1);
        r0.append(r5);
        r5 = r0.toString();
        android.util.Log.w(r6, r5);
        return r2;
    L_0x001c:
        r0 = 1;
        r3 = r6[r0];	 Catch:{ NumberFormatException -> 0x00cb }
        r3 = r3.length();	 Catch:{ NumberFormatException -> 0x00cb }
        r4 = 6;	 Catch:{ NumberFormatException -> 0x00cb }
        if (r3 != r4) goto L_0x0048;	 Catch:{ NumberFormatException -> 0x00cb }
    L_0x0026:
        r3 = r6[r0];	 Catch:{ NumberFormatException -> 0x00cb }
        r4 = 0;	 Catch:{ NumberFormatException -> 0x00cb }
        r1 = r3.substring(r4, r1);	 Catch:{ NumberFormatException -> 0x00cb }
        r3 = 16;	 Catch:{ NumberFormatException -> 0x00cb }
        r1 = java.lang.Integer.parseInt(r1, r3);	 Catch:{ NumberFormatException -> 0x00cb }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ NumberFormatException -> 0x00cb }
        r6 = r6[r0];	 Catch:{ NumberFormatException -> 0x00cb }
        r0 = 4;	 Catch:{ NumberFormatException -> 0x00cb }
        r6 = r6.substring(r0);	 Catch:{ NumberFormatException -> 0x00cb }
        r6 = java.lang.Integer.parseInt(r6, r3);	 Catch:{ NumberFormatException -> 0x00cb }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ NumberFormatException -> 0x00cb }
        r0 = r1;	 Catch:{ NumberFormatException -> 0x00cb }
        goto L_0x0060;	 Catch:{ NumberFormatException -> 0x00cb }
    L_0x0048:
        r3 = r6.length;	 Catch:{ NumberFormatException -> 0x00cb }
        r4 = 3;	 Catch:{ NumberFormatException -> 0x00cb }
        if (r3 < r4) goto L_0x00b4;	 Catch:{ NumberFormatException -> 0x00cb }
    L_0x004c:
        r0 = r6[r0];	 Catch:{ NumberFormatException -> 0x00cb }
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x00cb }
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ NumberFormatException -> 0x00cb }
        r6 = r6[r1];	 Catch:{ NumberFormatException -> 0x00cb }
        r6 = java.lang.Integer.parseInt(r6);	 Catch:{ NumberFormatException -> 0x00cb }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ NumberFormatException -> 0x00cb }
    L_0x0060:
        r5 = AVC_PROFILE_NUMBER_TO_CONST;
        r1 = r0.intValue();
        r5 = r5.get(r1);
        r5 = java.lang.Integer.valueOf(r5);
        if (r5 != 0) goto L_0x0087;
    L_0x0070:
        r5 = "MediaCodecUtil";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r1 = "Unknown AVC profile: ";
        r6.append(r1);
        r6.append(r0);
        r6 = r6.toString();
        android.util.Log.w(r5, r6);
        return r2;
    L_0x0087:
        r0 = AVC_LEVEL_NUMBER_TO_CONST;
        r1 = r6.intValue();
        r0 = r0.get(r1);
        r0 = java.lang.Integer.valueOf(r0);
        if (r0 != 0) goto L_0x00ae;
    L_0x0097:
        r5 = "MediaCodecUtil";
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Unknown AVC level: ";
        r0.append(r1);
        r0.append(r6);
        r6 = r0.toString();
        android.util.Log.w(r5, r6);
        return r2;
    L_0x00ae:
        r6 = new android.util.Pair;
        r6.<init>(r5, r0);
        return r6;
    L_0x00b4:
        r6 = "MediaCodecUtil";	 Catch:{ NumberFormatException -> 0x00cb }
        r0 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x00cb }
        r0.<init>();	 Catch:{ NumberFormatException -> 0x00cb }
        r1 = "Ignoring malformed AVC codec string: ";	 Catch:{ NumberFormatException -> 0x00cb }
        r0.append(r1);	 Catch:{ NumberFormatException -> 0x00cb }
        r0.append(r5);	 Catch:{ NumberFormatException -> 0x00cb }
        r0 = r0.toString();	 Catch:{ NumberFormatException -> 0x00cb }
        android.util.Log.w(r6, r0);	 Catch:{ NumberFormatException -> 0x00cb }
        return r2;
    L_0x00cb:
        r6 = "MediaCodecUtil";
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Ignoring malformed AVC codec string: ";
        r0.append(r1);
        r0.append(r5);
        r5 = r0.toString();
        android.util.Log.w(r6, r5);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.getAvcProfileAndLevel(java.lang.String, java.lang.String[]):android.util.Pair<java.lang.Integer, java.lang.Integer>");
    }
}
