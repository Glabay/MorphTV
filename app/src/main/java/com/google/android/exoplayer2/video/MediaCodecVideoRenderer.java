package com.google.android.exoplayer2.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCodec.OnFrameRenderedListener;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener.EventDispatcher;

@TargetApi(16)
public class MediaCodecVideoRenderer extends MediaCodecRenderer {
    private static final String KEY_CROP_BOTTOM = "crop-bottom";
    private static final String KEY_CROP_LEFT = "crop-left";
    private static final String KEY_CROP_RIGHT = "crop-right";
    private static final String KEY_CROP_TOP = "crop-top";
    private static final int MAX_PENDING_OUTPUT_STREAM_OFFSET_COUNT = 10;
    private static final int[] STANDARD_LONG_EDGE_VIDEO_PX = new int[]{1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
    private static final String TAG = "MediaCodecVideoRenderer";
    private final long allowedJoiningTimeMs;
    private int buffersInCodecCount;
    private CodecMaxValues codecMaxValues;
    private boolean codecNeedsSetOutputSurfaceWorkaround;
    private int consecutiveDroppedFrameCount;
    private final Context context;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private final boolean deviceNeedsAutoFrcWorkaround;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrames;
    private Surface dummySurface;
    private final EventDispatcher eventDispatcher;
    private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper;
    private long joiningDeadlineMs;
    private long lastRenderTimeUs;
    private final int maxDroppedFramesToNotify;
    private long outputStreamOffsetUs;
    private int pendingOutputStreamOffsetCount;
    private final long[] pendingOutputStreamOffsetsUs;
    private float pendingPixelWidthHeightRatio;
    private int pendingRotationDegrees;
    private boolean renderedFirstFrame;
    private int reportedHeight;
    private float reportedPixelWidthHeightRatio;
    private int reportedUnappliedRotationDegrees;
    private int reportedWidth;
    private int scalingMode;
    private Format[] streamFormats;
    private Surface surface;
    private boolean tunneling;
    private int tunnelingAudioSessionId;
    OnFrameRenderedListenerV23 tunnelingOnFrameRenderedListener;

    protected static final class CodecMaxValues {
        public final int height;
        public final int inputSize;
        public final int width;

        public CodecMaxValues(int i, int i2, int i3) {
            this.width = i;
            this.height = i2;
            this.inputSize = i3;
        }
    }

    @TargetApi(23)
    private final class OnFrameRenderedListenerV23 implements OnFrameRenderedListener {
        private OnFrameRenderedListenerV23(MediaCodec mediaCodec) {
            mediaCodec.setOnFrameRenderedListener(this, new Handler());
        }

        public void onFrameRendered(@NonNull MediaCodec mediaCodec, long j, long j2) {
            if (this == MediaCodecVideoRenderer.this.tunnelingOnFrameRenderedListener) {
                MediaCodecVideoRenderer.this.maybeNotifyRenderedFirstFrame();
            }
        }
    }

    private static boolean isBufferLate(long j) {
        return j < -30000;
    }

    private static boolean isBufferVeryLate(long j) {
        return j < -500000;
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector) {
        this(context, mediaCodecSelector, 0);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long j) {
        this(context, mediaCodecSelector, j, null, null, -1);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long j, @Nullable Handler handler, @Nullable VideoRendererEventListener videoRendererEventListener, int i) {
        this(context, mediaCodecSelector, j, null, false, handler, videoRendererEventListener, i);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long j, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean z, @Nullable Handler handler, @Nullable VideoRendererEventListener videoRendererEventListener, int i) {
        super(2, mediaCodecSelector, drmSessionManager, z);
        this.allowedJoiningTimeMs = j;
        this.maxDroppedFramesToNotify = i;
        this.context = context.getApplicationContext();
        this.frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(context);
        this.eventDispatcher = new EventDispatcher(handler, videoRendererEventListener);
        this.deviceNeedsAutoFrcWorkaround = deviceNeedsAutoFrcWorkaround();
        this.pendingOutputStreamOffsetsUs = new long[10];
        this.outputStreamOffsetUs = C0649C.TIME_UNSET;
        this.joiningDeadlineMs = C0649C.TIME_UNSET;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.scalingMode = 1;
        clearReportedVideoSize();
    }

    protected int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws DecoderQueryException {
        String str = format.sampleMimeType;
        int i = 0;
        if (!MimeTypes.isVideo(str)) {
            return 0;
        }
        boolean z;
        DrmInitData drmInitData = format.drmInitData;
        if (drmInitData != null) {
            z = false;
            for (int i2 = 0; i2 < drmInitData.schemeDataCount; i2++) {
                z |= drmInitData.get(i2).requiresSecureDecryption;
            }
        } else {
            z = false;
        }
        MediaCodecInfo decoderInfo = mediaCodecSelector.getDecoderInfo(str, z);
        int i3 = 2;
        if (decoderInfo == null) {
            if (!z || mediaCodecSelector.getDecoderInfo(str, false) == null) {
                i3 = 1;
            }
            return i3;
        } else if (BaseRenderer.supportsFormatDrm(drmSessionManager, drmInitData) == null) {
            return 2;
        } else {
            mediaCodecSelector = decoderInfo.isCodecSupported(format.codecs);
            if (mediaCodecSelector != null && format.width > null && format.height > null) {
                if (Util.SDK_INT >= 21) {
                    mediaCodecSelector = decoderInfo.isVideoSizeAndRateSupportedV21(format.width, format.height, (double) format.frameRate);
                } else {
                    mediaCodecSelector = format.width * format.height <= MediaCodecUtil.maxH264DecodableFrameSize() ? true : null;
                    if (mediaCodecSelector == null) {
                        drmSessionManager = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("FalseCheck [legacyFrameSize, ");
                        stringBuilder.append(format.width);
                        stringBuilder.append("x");
                        stringBuilder.append(format.height);
                        stringBuilder.append("] [");
                        stringBuilder.append(Util.DEVICE_DEBUG_INFO);
                        stringBuilder.append("]");
                        Log.d(drmSessionManager, stringBuilder.toString());
                    }
                }
            }
            drmSessionManager = decoderInfo.adaptive != null ? 16 : 8;
            if (decoderInfo.tunneling != null) {
                i = 32;
            }
            return (mediaCodecSelector != null ? 4 : 3) | (drmSessionManager | i);
        }
    }

    protected void onEnabled(boolean z) throws ExoPlaybackException {
        super.onEnabled(z);
        this.tunnelingAudioSessionId = getConfiguration().tunnelingAudioSessionId;
        this.tunneling = this.tunnelingAudioSessionId;
        this.eventDispatcher.enabled(this.decoderCounters);
        this.frameReleaseTimeHelper.enable();
    }

    protected void onStreamChanged(Format[] formatArr, long j) throws ExoPlaybackException {
        this.streamFormats = formatArr;
        if (this.outputStreamOffsetUs == C0649C.TIME_UNSET) {
            this.outputStreamOffsetUs = j;
        } else {
            if (this.pendingOutputStreamOffsetCount == this.pendingOutputStreamOffsetsUs.length) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Too many stream changes, so dropping offset: ");
                stringBuilder.append(this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1]);
                Log.w(str, stringBuilder.toString());
            } else {
                this.pendingOutputStreamOffsetCount++;
            }
            this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1] = j;
        }
        super.onStreamChanged(formatArr, j);
    }

    protected void onPositionReset(long j, boolean z) throws ExoPlaybackException {
        super.onPositionReset(j, z);
        clearRenderedFirstFrame();
        this.consecutiveDroppedFrameCount = 0;
        if (this.pendingOutputStreamOffsetCount != 0) {
            this.outputStreamOffsetUs = this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1];
            this.pendingOutputStreamOffsetCount = 0;
        }
        if (z) {
            setJoiningDeadlineMs();
        } else {
            this.joiningDeadlineMs = C0649C.TIME_UNSET;
        }
    }

    public boolean isReady() {
        if (super.isReady() && (this.renderedFirstFrame || ((this.dummySurface != null && this.surface == this.dummySurface) || getCodec() == null || this.tunneling))) {
            this.joiningDeadlineMs = C0649C.TIME_UNSET;
            return true;
        } else if (this.joiningDeadlineMs == C0649C.TIME_UNSET) {
            return false;
        } else {
            if (SystemClock.elapsedRealtime() < this.joiningDeadlineMs) {
                return true;
            }
            this.joiningDeadlineMs = C0649C.TIME_UNSET;
            return false;
        }
    }

    protected void onStarted() {
        super.onStarted();
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
    }

    protected void onStopped() {
        this.joiningDeadlineMs = C0649C.TIME_UNSET;
        maybeNotifyDroppedFrames();
        super.onStopped();
    }

    protected void onDisabled() {
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.outputStreamOffsetUs = C0649C.TIME_UNSET;
        this.pendingOutputStreamOffsetCount = 0;
        clearReportedVideoSize();
        clearRenderedFirstFrame();
        this.frameReleaseTimeHelper.disable();
        this.tunnelingOnFrameRenderedListener = null;
        this.tunneling = false;
        try {
            super.onDisabled();
        } finally {
            this.decoderCounters.ensureUpdated();
            this.eventDispatcher.disabled(this.decoderCounters);
        }
    }

    public void handleMessage(int i, Object obj) throws ExoPlaybackException {
        if (i == 1) {
            setSurface((Surface) obj);
        } else if (i == 4) {
            this.scalingMode = ((Integer) obj).intValue();
            i = getCodec();
            if (i != 0) {
                setVideoScalingMode(i, this.scalingMode);
            }
        } else {
            super.handleMessage(i, obj);
        }
    }

    private void setSurface(Surface surface) throws ExoPlaybackException {
        if (surface == null) {
            if (this.dummySurface != null) {
                surface = this.dummySurface;
            } else {
                MediaCodecInfo codecInfo = getCodecInfo();
                if (codecInfo != null && shouldUseDummySurface(codecInfo)) {
                    this.dummySurface = DummySurface.newInstanceV17(this.context, codecInfo.secure);
                    surface = this.dummySurface;
                }
            }
        }
        if (this.surface != surface) {
            this.surface = surface;
            int state = getState();
            if (state == 1 || state == 2) {
                MediaCodec codec = getCodec();
                if (Util.SDK_INT < 23 || codec == null || surface == null || this.codecNeedsSetOutputSurfaceWorkaround) {
                    releaseCodec();
                    maybeInitCodec();
                } else {
                    setOutputSurfaceV23(codec, surface);
                }
            }
            if (surface == null || surface == this.dummySurface) {
                clearReportedVideoSize();
                clearRenderedFirstFrame();
                return;
            }
            maybeRenotifyVideoSizeChanged();
            clearRenderedFirstFrame();
            if (state == 2) {
                setJoiningDeadlineMs();
            }
        } else if (surface != null && surface != this.dummySurface) {
            maybeRenotifyVideoSizeChanged();
            maybeRenotifyRenderedFirstFrame();
        }
    }

    protected boolean shouldInitCodec(MediaCodecInfo mediaCodecInfo) {
        if (this.surface == null) {
            if (shouldUseDummySurface(mediaCodecInfo) == null) {
                return null;
            }
        }
        return true;
    }

    protected void configureCodec(MediaCodecInfo mediaCodecInfo, MediaCodec mediaCodec, Format format, MediaCrypto mediaCrypto) throws DecoderQueryException {
        this.codecMaxValues = getCodecMaxValues(mediaCodecInfo, format, this.streamFormats);
        format = getMediaFormat(format, this.codecMaxValues, this.deviceNeedsAutoFrcWorkaround, this.tunnelingAudioSessionId);
        if (this.surface == null) {
            Assertions.checkState(shouldUseDummySurface(mediaCodecInfo));
            if (this.dummySurface == null) {
                this.dummySurface = DummySurface.newInstanceV17(this.context, mediaCodecInfo.secure);
            }
            this.surface = this.dummySurface;
        }
        mediaCodec.configure(format, this.surface, mediaCrypto, 0);
        if (Util.SDK_INT >= 23 && this.tunneling != null) {
            this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(mediaCodec);
        }
    }

    @CallSuper
    protected void releaseCodec() {
        try {
            super.releaseCodec();
        } finally {
            this.buffersInCodecCount = 0;
            if (this.dummySurface != null) {
                if (this.surface == this.dummySurface) {
                    this.surface = null;
                }
                this.dummySurface.release();
                this.dummySurface = null;
            }
        }
    }

    @CallSuper
    protected void flushCodec() throws ExoPlaybackException {
        super.flushCodec();
        this.buffersInCodecCount = 0;
    }

    protected void onCodecInitialized(String str, long j, long j2) {
        this.eventDispatcher.decoderInitialized(str, j, j2);
        this.codecNeedsSetOutputSurfaceWorkaround = codecNeedsSetOutputSurfaceWorkaround(str);
    }

    protected void onInputFormatChanged(Format format) throws ExoPlaybackException {
        super.onInputFormatChanged(format);
        this.eventDispatcher.inputFormatChanged(format);
        this.pendingPixelWidthHeightRatio = getPixelWidthHeightRatio(format);
        this.pendingRotationDegrees = getRotationDegrees(format);
    }

    @CallSuper
    protected void onQueueInputBuffer(DecoderInputBuffer decoderInputBuffer) {
        this.buffersInCodecCount++;
        if (Util.SDK_INT < 23 && this.tunneling != null) {
            maybeNotifyRenderedFirstFrame();
        }
    }

    protected void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
        int integer;
        int integer2;
        Object obj = (mediaFormat.containsKey(KEY_CROP_RIGHT) && mediaFormat.containsKey(KEY_CROP_LEFT) && mediaFormat.containsKey(KEY_CROP_BOTTOM) && mediaFormat.containsKey(KEY_CROP_TOP)) ? 1 : null;
        if (obj != null) {
            integer = (mediaFormat.getInteger(KEY_CROP_RIGHT) - mediaFormat.getInteger(KEY_CROP_LEFT)) + 1;
        } else {
            integer = mediaFormat.getInteger("width");
        }
        this.currentWidth = integer;
        if (obj != null) {
            integer2 = (mediaFormat.getInteger(KEY_CROP_BOTTOM) - mediaFormat.getInteger(KEY_CROP_TOP)) + 1;
        } else {
            integer2 = mediaFormat.getInteger("height");
        }
        this.currentHeight = integer2;
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT < 21) {
            this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
        } else if (this.pendingRotationDegrees == 90 || this.pendingRotationDegrees == 270) {
            mediaFormat = this.currentWidth;
            this.currentWidth = this.currentHeight;
            this.currentHeight = mediaFormat;
            this.currentPixelWidthHeightRatio = 1065353216 / this.currentPixelWidthHeightRatio;
        }
        setVideoScalingMode(mediaCodec, this.scalingMode);
    }

    protected boolean canReconfigureCodec(MediaCodec mediaCodec, boolean z, Format format, Format format2) {
        return (areAdaptationCompatible(z, format, format2) == null || format2.width > this.codecMaxValues.width || format2.height > this.codecMaxValues.height || getMaxInputSize(format2) > this.codecMaxValues.inputSize) ? null : true;
    }

    protected boolean processOutputBuffer(long r23, long r25, android.media.MediaCodec r27, java.nio.ByteBuffer r28, int r29, int r30, long r31, boolean r33) throws com.google.android.exoplayer2.ExoPlaybackException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r22 = this;
        r7 = r22;
        r8 = r25;
        r10 = r27;
        r11 = r29;
        r0 = r31;
    L_0x000a:
        r2 = r7.pendingOutputStreamOffsetCount;
        r12 = 1;
        r13 = 0;
        if (r2 == 0) goto L_0x002d;
    L_0x0010:
        r2 = r7.pendingOutputStreamOffsetsUs;
        r3 = r2[r13];
        r2 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r2 < 0) goto L_0x002d;
    L_0x0018:
        r2 = r7.pendingOutputStreamOffsetsUs;
        r3 = r2[r13];
        r7.outputStreamOffsetUs = r3;
        r2 = r7.pendingOutputStreamOffsetCount;
        r2 = r2 - r12;
        r7.pendingOutputStreamOffsetCount = r2;
        r2 = r7.pendingOutputStreamOffsetsUs;
        r3 = r7.pendingOutputStreamOffsetsUs;
        r4 = r7.pendingOutputStreamOffsetCount;
        java.lang.System.arraycopy(r2, r12, r3, r13, r4);
        goto L_0x000a;
    L_0x002d:
        r2 = r7.outputStreamOffsetUs;
        r14 = r0 - r2;
        if (r33 == 0) goto L_0x0037;
    L_0x0033:
        r7.skipOutputBuffer(r10, r11, r14);
        return r12;
    L_0x0037:
        r2 = 0;
        r2 = r0 - r23;
        r4 = r7.surface;
        r13 = r7.dummySurface;
        if (r4 != r13) goto L_0x004c;
    L_0x0040:
        r0 = isBufferLate(r2);
        if (r0 == 0) goto L_0x004a;
    L_0x0046:
        r7.skipOutputBuffer(r10, r11, r14);
        return r12;
    L_0x004a:
        r0 = 0;
        return r0;
    L_0x004c:
        r16 = android.os.SystemClock.elapsedRealtime();
        r18 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r16 = r16 * r18;
        r4 = r22.getState();
        r13 = 2;
        if (r4 != r13) goto L_0x005d;
    L_0x005b:
        r4 = 1;
        goto L_0x005e;
    L_0x005d:
        r4 = 0;
    L_0x005e:
        r13 = r7.renderedFirstFrame;
        if (r13 == 0) goto L_0x00ef;
    L_0x0062:
        if (r4 == 0) goto L_0x0070;
    L_0x0064:
        r12 = r7.lastRenderTimeUs;
        r5 = r16 - r12;
        r5 = r7.shouldForceRenderOutputBuffer(r2, r5);
        if (r5 == 0) goto L_0x0070;
    L_0x006e:
        goto L_0x00ef;
    L_0x0070:
        if (r4 != 0) goto L_0x0074;
    L_0x0072:
        r4 = 0;
        return r4;
    L_0x0074:
        r4 = 0;
        r4 = r16 - r8;
        r12 = r2 - r4;
        r2 = java.lang.System.nanoTime();
        r12 = r12 * r18;
        r4 = r2 + r12;
        r6 = r7.frameReleaseTimeHelper;
        r12 = r6.adjustReleaseTime(r0, r4);
        r0 = r12 - r2;
        r5 = r0 / r18;
        r0 = r7.shouldDropBuffersToKeyframe(r5, r8);
        if (r0 == 0) goto L_0x00a2;
    L_0x0091:
        r0 = r7;
        r1 = r10;
        r2 = r11;
        r3 = r14;
        r20 = r12;
        r12 = r5;
        r5 = r23;
        r0 = r0.maybeDropBuffersToKeyframe(r1, r2, r3, r5);
        if (r0 == 0) goto L_0x00a5;
    L_0x00a0:
        r0 = 0;
        return r0;
    L_0x00a2:
        r20 = r12;
        r12 = r5;
    L_0x00a5:
        r0 = r7.shouldDropOutputBuffer(r12, r8);
        if (r0 == 0) goto L_0x00b0;
    L_0x00ab:
        r7.dropOutputBuffer(r10, r11, r14);
        r0 = 1;
        return r0;
    L_0x00b0:
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 21;
        if (r0 < r1) goto L_0x00ca;
    L_0x00b6:
        r0 = 50000; // 0xc350 float:7.0065E-41 double:2.47033E-319;
        r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        if (r2 >= 0) goto L_0x00c8;
    L_0x00bd:
        r0 = r7;
        r1 = r10;
        r2 = r11;
        r3 = r14;
        r5 = r20;
        r0.renderOutputBufferV21(r1, r2, r3, r5);
        r0 = 1;
        return r0;
    L_0x00c8:
        r0 = 0;
        goto L_0x00ee;
    L_0x00ca:
        r0 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        if (r2 >= 0) goto L_0x00c8;
    L_0x00d0:
        r0 = 11000; // 0x2af8 float:1.5414E-41 double:5.4347E-320;
        r2 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1));
        if (r2 <= 0) goto L_0x00e9;
    L_0x00d6:
        r0 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r5 = r12 - r0;
        r5 = r5 / r18;	 Catch:{ InterruptedException -> 0x00e0 }
        java.lang.Thread.sleep(r5);	 Catch:{ InterruptedException -> 0x00e0 }
        goto L_0x00e9;
    L_0x00e0:
        r0 = java.lang.Thread.currentThread();
        r0.interrupt();
        r0 = 0;
        return r0;
    L_0x00e9:
        r7.renderOutputBuffer(r10, r11, r14);
        r0 = 1;
        return r0;
    L_0x00ee:
        return r0;
    L_0x00ef:
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 21;
        if (r0 < r1) goto L_0x0102;
    L_0x00f5:
        r5 = java.lang.System.nanoTime();
        r0 = r7;
        r1 = r10;
        r2 = r11;
        r3 = r14;
        r0.renderOutputBufferV21(r1, r2, r3, r5);
    L_0x0100:
        r0 = 1;
        goto L_0x0106;
    L_0x0102:
        r7.renderOutputBuffer(r10, r11, r14);
        goto L_0x0100;
    L_0x0106:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.processOutputBuffer(long, long, android.media.MediaCodec, java.nio.ByteBuffer, int, int, long, boolean):boolean");
    }

    @CallSuper
    protected void onProcessedOutputBuffer(long j) {
        this.buffersInCodecCount--;
    }

    protected boolean shouldDropOutputBuffer(long j, long j2) {
        return isBufferLate(j);
    }

    protected boolean shouldDropBuffersToKeyframe(long j, long j2) {
        return isBufferVeryLate(j);
    }

    protected boolean shouldForceRenderOutputBuffer(long j, long j2) {
        return (isBufferLate(j) == null || j2 <= 100000) ? 0 : 1;
    }

    protected void skipOutputBuffer(MediaCodec mediaCodec, int i, long j) {
        TraceUtil.beginSection("skipVideoBuffer");
        mediaCodec.releaseOutputBuffer(i, 0);
        TraceUtil.endSection();
        mediaCodec = this.decoderCounters;
        mediaCodec.skippedOutputBufferCount++;
    }

    protected void dropOutputBuffer(MediaCodec mediaCodec, int i, long j) {
        TraceUtil.beginSection("dropVideoBuffer");
        mediaCodec.releaseOutputBuffer(i, 0);
        TraceUtil.endSection();
        updateDroppedBufferCounters(1);
    }

    protected boolean maybeDropBuffersToKeyframe(MediaCodec mediaCodec, int i, long j, long j2) throws ExoPlaybackException {
        mediaCodec = skipSource(j2);
        if (mediaCodec == null) {
            return null;
        }
        i = this.decoderCounters;
        i.droppedToKeyframeCount++;
        updateDroppedBufferCounters(this.buffersInCodecCount + mediaCodec);
        flushCodec();
        return true;
    }

    protected void updateDroppedBufferCounters(int i) {
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.droppedBufferCount += i;
        this.droppedFrames += i;
        this.consecutiveDroppedFrameCount += i;
        this.decoderCounters.maxConsecutiveDroppedBufferCount = Math.max(this.consecutiveDroppedFrameCount, this.decoderCounters.maxConsecutiveDroppedBufferCount);
        if (this.droppedFrames >= this.maxDroppedFramesToNotify) {
            maybeNotifyDroppedFrames();
        }
    }

    protected void renderOutputBuffer(MediaCodec mediaCodec, int i, long j) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(i, true);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
        mediaCodec = this.decoderCounters;
        mediaCodec.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = null;
        maybeNotifyRenderedFirstFrame();
    }

    @TargetApi(21)
    protected void renderOutputBufferV21(MediaCodec mediaCodec, int i, long j, long j2) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(i, j2);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
        mediaCodec = this.decoderCounters;
        mediaCodec.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = null;
        maybeNotifyRenderedFirstFrame();
    }

    private boolean shouldUseDummySurface(MediaCodecInfo mediaCodecInfo) {
        return (Util.SDK_INT < 23 || this.tunneling || codecNeedsSetOutputSurfaceWorkaround(mediaCodecInfo.name) || (mediaCodecInfo.secure != null && DummySurface.isSecureSupported(this.context) == null)) ? null : true;
    }

    private void setJoiningDeadlineMs() {
        this.joiningDeadlineMs = this.allowedJoiningTimeMs > 0 ? SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs : C0649C.TIME_UNSET;
    }

    private void clearRenderedFirstFrame() {
        this.renderedFirstFrame = false;
        if (Util.SDK_INT >= 23 && this.tunneling) {
            MediaCodec codec = getCodec();
            if (codec != null) {
                this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
            }
        }
    }

    void maybeNotifyRenderedFirstFrame() {
        if (!this.renderedFirstFrame) {
            this.renderedFirstFrame = true;
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void maybeRenotifyRenderedFirstFrame() {
        if (this.renderedFirstFrame) {
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void clearReportedVideoSize() {
        this.reportedWidth = -1;
        this.reportedHeight = -1;
        this.reportedPixelWidthHeightRatio = -1.0f;
        this.reportedUnappliedRotationDegrees = -1;
    }

    private void maybeNotifyVideoSizeChanged() {
        if (this.currentWidth != -1 || this.currentHeight != -1) {
            if (this.reportedWidth != this.currentWidth || this.reportedHeight != this.currentHeight || this.reportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.reportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio) {
                this.eventDispatcher.videoSizeChanged(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
                this.reportedWidth = this.currentWidth;
                this.reportedHeight = this.currentHeight;
                this.reportedUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
                this.reportedPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
            }
        }
    }

    private void maybeRenotifyVideoSizeChanged() {
        if (this.reportedWidth != -1 || this.reportedHeight != -1) {
            this.eventDispatcher.videoSizeChanged(this.reportedWidth, this.reportedHeight, this.reportedUnappliedRotationDegrees, this.reportedPixelWidthHeightRatio);
        }
    }

    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.eventDispatcher.droppedFrames(this.droppedFrames, elapsedRealtime - this.droppedFrameAccumulationStartTimeMs);
            this.droppedFrames = 0;
            this.droppedFrameAccumulationStartTimeMs = elapsedRealtime;
        }
    }

    @TargetApi(23)
    private static void setOutputSurfaceV23(MediaCodec mediaCodec, Surface surface) {
        mediaCodec.setOutputSurface(surface);
    }

    @TargetApi(21)
    private static void configureTunnelingV21(MediaFormat mediaFormat, int i) {
        mediaFormat.setFeatureEnabled("tunneled-playback", true);
        mediaFormat.setInteger("audio-session-id", i);
    }

    protected CodecMaxValues getCodecMaxValues(MediaCodecInfo mediaCodecInfo, Format format, Format[] formatArr) throws DecoderQueryException {
        int i = format.width;
        int i2 = format.height;
        int maxInputSize = getMaxInputSize(format);
        if (formatArr.length == 1) {
            return new CodecMaxValues(i, i2, maxInputSize);
        }
        int i3 = i2;
        int i4 = maxInputSize;
        i2 = 0;
        maxInputSize = i;
        for (Format format2 : formatArr) {
            if (areAdaptationCompatible(mediaCodecInfo.adaptive, format, format2)) {
                int i5;
                if (format2.width != -1) {
                    if (format2.height != -1) {
                        i5 = 0;
                        i2 |= i5;
                        maxInputSize = Math.max(maxInputSize, format2.width);
                        i3 = Math.max(i3, format2.height);
                        i4 = Math.max(i4, getMaxInputSize(format2));
                    }
                }
                i5 = 1;
                i2 |= i5;
                maxInputSize = Math.max(maxInputSize, format2.width);
                i3 = Math.max(i3, format2.height);
                i4 = Math.max(i4, getMaxInputSize(format2));
            }
        }
        if (i2 != 0) {
            formatArr = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resolutions unknown. Codec max resolution: ");
            stringBuilder.append(maxInputSize);
            stringBuilder.append("x");
            stringBuilder.append(i3);
            Log.w(formatArr, stringBuilder.toString());
            mediaCodecInfo = getCodecMaxSize(mediaCodecInfo, format);
            if (mediaCodecInfo != null) {
                maxInputSize = Math.max(maxInputSize, mediaCodecInfo.x);
                i3 = Math.max(i3, mediaCodecInfo.y);
                i4 = Math.max(i4, getMaxInputSize(format.sampleMimeType, maxInputSize, i3));
                mediaCodecInfo = TAG;
                format = new StringBuilder();
                format.append("Codec max resolution adjusted to: ");
                format.append(maxInputSize);
                format.append("x");
                format.append(i3);
                Log.w(mediaCodecInfo, format.toString());
            }
        }
        return new CodecMaxValues(maxInputSize, i3, i4);
    }

    @SuppressLint({"InlinedApi"})
    protected MediaFormat getMediaFormat(Format format, CodecMaxValues codecMaxValues, boolean z, int i) {
        format = getMediaFormatForPlayback(format);
        format.setInteger("max-width", codecMaxValues.width);
        format.setInteger("max-height", codecMaxValues.height);
        if (codecMaxValues.inputSize != -1) {
            format.setInteger("max-input-size", codecMaxValues.inputSize);
        }
        if (z) {
            format.setInteger("auto-frc", false);
        }
        if (i != 0) {
            configureTunnelingV21(format, i);
        }
        return format;
    }

    private static Point getCodecMaxSize(MediaCodecInfo mediaCodecInfo, Format format) throws DecoderQueryException {
        int i = 0;
        Object obj = format.height > format.width ? 1 : null;
        int i2 = obj != null ? format.height : format.width;
        int i3 = obj != null ? format.width : format.height;
        float f = ((float) i3) / ((float) i2);
        int[] iArr = STANDARD_LONG_EDGE_VIDEO_PX;
        int length = iArr.length;
        while (i < length) {
            int i4 = iArr[i];
            int i5 = (int) (((float) i4) * f);
            if (i4 > i2) {
                if (i5 > i3) {
                    int i6;
                    if (Util.SDK_INT >= 21) {
                        i6 = obj != null ? i5 : i4;
                        if (obj == null) {
                            i4 = i5;
                        }
                        Point alignVideoSizeV21 = mediaCodecInfo.alignVideoSizeV21(i6, i4);
                        if (mediaCodecInfo.isVideoSizeAndRateSupportedV21(alignVideoSizeV21.x, alignVideoSizeV21.y, (double) format.frameRate)) {
                            return alignVideoSizeV21;
                        }
                    } else {
                        i4 = Util.ceilDivide(i4, 16) * 16;
                        i6 = Util.ceilDivide(i5, 16) * 16;
                        if (i4 * i6 <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                            format = obj != null ? i6 : i4;
                            if (obj != null) {
                                i6 = i4;
                            }
                            return new Point(format, i6);
                        }
                    }
                    i++;
                }
            }
            return null;
        }
        return null;
    }

    private static int getMaxInputSize(Format format) {
        if (format.maxInputSize == -1) {
            return getMaxInputSize(format.sampleMimeType, format.width, format.height);
        }
        int i = 0;
        for (int i2 = 0; i2 < format.initializationData.size(); i2++) {
            i += ((byte[]) format.initializationData.get(i2)).length;
        }
        return format.maxInputSize + i;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getMaxInputSize(java.lang.String r5, int r6, int r7) {
        /*
        r0 = -1;
        if (r6 == r0) goto L_0x007e;
    L_0x0003:
        if (r7 != r0) goto L_0x0007;
    L_0x0005:
        goto L_0x007e;
    L_0x0007:
        r1 = r5.hashCode();
        r2 = 4;
        r3 = 3;
        r4 = 2;
        switch(r1) {
            case -1664118616: goto L_0x0044;
            case -1662541442: goto L_0x003a;
            case 1187890754: goto L_0x0030;
            case 1331836730: goto L_0x0026;
            case 1599127256: goto L_0x001c;
            case 1599127257: goto L_0x0012;
            default: goto L_0x0011;
        };
    L_0x0011:
        goto L_0x004e;
    L_0x0012:
        r1 = "video/x-vnd.on2.vp9";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x004e;
    L_0x001a:
        r5 = 5;
        goto L_0x004f;
    L_0x001c:
        r1 = "video/x-vnd.on2.vp8";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x004e;
    L_0x0024:
        r5 = 3;
        goto L_0x004f;
    L_0x0026:
        r1 = "video/avc";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x004e;
    L_0x002e:
        r5 = 2;
        goto L_0x004f;
    L_0x0030:
        r1 = "video/mp4v-es";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x004e;
    L_0x0038:
        r5 = 1;
        goto L_0x004f;
    L_0x003a:
        r1 = "video/hevc";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x004e;
    L_0x0042:
        r5 = 4;
        goto L_0x004f;
    L_0x0044:
        r1 = "video/3gpp";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x004e;
    L_0x004c:
        r5 = 0;
        goto L_0x004f;
    L_0x004e:
        r5 = -1;
    L_0x004f:
        switch(r5) {
            case 0: goto L_0x0075;
            case 1: goto L_0x0075;
            case 2: goto L_0x0059;
            case 3: goto L_0x0056;
            case 4: goto L_0x0053;
            case 5: goto L_0x0053;
            default: goto L_0x0052;
        };
    L_0x0052:
        return r0;
    L_0x0053:
        r6 = r6 * r7;
        goto L_0x0078;
    L_0x0056:
        r6 = r6 * r7;
        goto L_0x0077;
    L_0x0059:
        r5 = "BRAVIA 4K 2015";
        r1 = com.google.android.exoplayer2.util.Util.MODEL;
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x0064;
    L_0x0063:
        return r0;
    L_0x0064:
        r5 = 16;
        r6 = com.google.android.exoplayer2.util.Util.ceilDivide(r6, r5);
        r7 = com.google.android.exoplayer2.util.Util.ceilDivide(r7, r5);
        r6 = r6 * r7;
        r6 = r6 * 16;
        r6 = r6 * 16;
        goto L_0x0077;
    L_0x0075:
        r6 = r6 * r7;
    L_0x0077:
        r2 = 2;
    L_0x0078:
        r6 = r6 * 3;
        r2 = r2 * 2;
        r6 = r6 / r2;
        return r6;
    L_0x007e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.getMaxInputSize(java.lang.String, int, int):int");
    }

    private static void setVideoScalingMode(MediaCodec mediaCodec, int i) {
        mediaCodec.setVideoScalingMode(i);
    }

    private static boolean deviceNeedsAutoFrcWorkaround() {
        return Util.SDK_INT <= 22 && "foster".equals(Util.DEVICE) && "NVIDIA".equals(Util.MANUFACTURER);
    }

    private static boolean codecNeedsSetOutputSurfaceWorkaround(String str) {
        if (!((("deb".equals(Util.DEVICE) || "flo".equals(Util.DEVICE) || "mido".equals(Util.DEVICE) || "santoni".equals(Util.DEVICE)) && "OMX.qcom.video.decoder.avc".equals(str)) || (("tcl_eu".equals(Util.DEVICE) || "SVP-DTV15".equals(Util.DEVICE) || "BRAVIA_ATV2".equals(Util.DEVICE) || Util.DEVICE.startsWith("panell_") || "F3311".equals(Util.DEVICE) || "M5c".equals(Util.DEVICE) || "A7010a48".equals(Util.DEVICE)) && "OMX.MTK.VIDEO.DECODER.AVC".equals(str)))) {
            if ((!"ALE-L21".equals(Util.MODEL) && !"CAM-L21".equals(Util.MODEL)) || "OMX.k3.video.decoder.avc".equals(str) == null) {
                return null;
            }
        }
        return true;
    }

    private static boolean areAdaptationCompatible(boolean z, Format format, Format format2) {
        return format.sampleMimeType.equals(format2.sampleMimeType) && getRotationDegrees(format) == getRotationDegrees(format2) && (z || (format.width == format2.width && format.height == format2.height));
    }

    private static float getPixelWidthHeightRatio(Format format) {
        return format.pixelWidthHeightRatio == -1.0f ? 1.0f : format.pixelWidthHeightRatio;
    }

    private static int getRotationDegrees(Format format) {
        return format.rotationDegrees == -1 ? null : format.rotationDegrees;
    }
}
