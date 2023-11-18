package com.android.morpheustv.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class MyDefaultRenderersFactory implements RenderersFactory {
    public static final long DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS = 5000;
    public static final int EXTENSION_RENDERER_MODE_OFF = 0;
    public static final int EXTENSION_RENDERER_MODE_ON = 1;
    public static final int EXTENSION_RENDERER_MODE_PREFER = 2;
    protected static final int MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY = 50;
    private static final String TAG = "DefaultRenderersFactory";
    private final long allowedVideoJoiningTimeMs;
    private final Context context;
    @Nullable
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final int extensionRendererMode;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExtensionRendererMode {
    }

    protected void buildMiscellaneousRenderers(Context context, Handler handler, int i, ArrayList<Renderer> arrayList) {
    }

    public MyDefaultRenderersFactory(Context context) {
        this(context, null);
    }

    public MyDefaultRenderersFactory(Context context, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        this(context, drmSessionManager, 0);
    }

    public MyDefaultRenderersFactory(Context context, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int i) {
        this(context, drmSessionManager, i, 5000);
    }

    public MyDefaultRenderersFactory(Context context, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int i, long j) {
        this.context = context;
        this.drmSessionManager = drmSessionManager;
        this.extensionRendererMode = i;
        this.allowedVideoJoiningTimeMs = j;
    }

    public Renderer[] createRenderers(Handler handler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textOutput, MetadataOutput metadataOutput) {
        ArrayList arrayList = new ArrayList();
        buildVideoRenderers(this.context, this.drmSessionManager, this.allowedVideoJoiningTimeMs, handler, videoRendererEventListener, this.extensionRendererMode, arrayList);
        buildAudioRenderers(this.context, this.drmSessionManager, buildAudioProcessors(), handler, audioRendererEventListener, this.extensionRendererMode, arrayList);
        ArrayList arrayList2 = arrayList;
        buildTextRenderers(this.context, textOutput, handler.getLooper(), this.extensionRendererMode, arrayList2);
        buildMetadataRenderers(this.context, metadataOutput, handler.getLooper(), this.extensionRendererMode, arrayList2);
        buildMiscellaneousRenderers(this.context, handler, this.extensionRendererMode, arrayList);
        return (Renderer[]) arrayList.toArray(new Renderer[arrayList.size()]);
    }

    protected void buildVideoRenderers(android.content.Context r15, @android.support.annotation.Nullable com.google.android.exoplayer2.drm.DrmSessionManager<com.google.android.exoplayer2.drm.FrameworkMediaCrypto> r16, long r17, android.os.Handler r19, com.google.android.exoplayer2.video.VideoRendererEventListener r20, int r21, java.util.ArrayList<com.google.android.exoplayer2.Renderer> r22) {
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
        r14 = this;
        r1 = r21;
        r2 = r22;
        r13 = new com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
        r5 = com.google.android.exoplayer2.mediacodec.MediaCodecSelector.DEFAULT;
        r9 = 0;
        r12 = 50;
        r3 = r13;
        r4 = r15;
        r6 = r17;
        r8 = r16;
        r10 = r19;
        r11 = r20;
        r3.<init>(r4, r5, r6, r8, r9, r10, r11, r12);
        r2.add(r13);
        if (r1 != 0) goto L_0x001e;
    L_0x001d:
        return;
    L_0x001e:
        r3 = r22.size();
        r4 = 2;
        if (r1 != r4) goto L_0x0027;
    L_0x0025:
        r3 = r3 + -1;
    L_0x0027:
        r1 = "com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer";	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r5 = 5;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6 = new java.lang.Class[r5];	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r7 = java.lang.Boolean.TYPE;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r8 = 0;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6[r8] = r7;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r7 = java.lang.Long.TYPE;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r9 = 1;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6[r9] = r7;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r7 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6[r4] = r7;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r7 = com.google.android.exoplayer2.video.VideoRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r10 = 3;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6[r10] = r7;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r7 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r11 = 4;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6[r11] = r7;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r1 = r1.getConstructor(r6);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r5 = new java.lang.Object[r5];	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6 = java.lang.Boolean.valueOf(r9);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r5[r8] = r6;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r6 = java.lang.Long.valueOf(r17);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r5[r9] = r6;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r5[r4] = r19;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r5[r10] = r20;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r4 = 50;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r5[r11] = r4;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r1 = r1.newInstance(r5);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r1 = (com.google.android.exoplayer2.Renderer) r1;	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r2.add(r3, r1);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r1 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        r2 = "Loaded LibvpxVideoRenderer.";	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        android.util.Log.i(r1, r2);	 Catch:{ ClassNotFoundException -> 0x0081, Exception -> 0x0077 }
        goto L_0x0081;
    L_0x0077:
        r0 = move-exception;
        r1 = r0;
        r2 = new java.lang.RuntimeException;
        r3 = "Error instantiating VP9 extension";
        r2.<init>(r3, r1);
        throw r2;
    L_0x0081:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.player.MyDefaultRenderersFactory.buildVideoRenderers(android.content.Context, com.google.android.exoplayer2.drm.DrmSessionManager, long, android.os.Handler, com.google.android.exoplayer2.video.VideoRendererEventListener, int, java.util.ArrayList):void");
    }

    protected void buildAudioRenderers(android.content.Context r13, @android.support.annotation.Nullable com.google.android.exoplayer2.drm.DrmSessionManager<com.google.android.exoplayer2.drm.FrameworkMediaCrypto> r14, com.google.android.exoplayer2.audio.AudioProcessor[] r15, android.os.Handler r16, com.google.android.exoplayer2.audio.AudioRendererEventListener r17, int r18, java.util.ArrayList<com.google.android.exoplayer2.Renderer> r19) {
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
        r12 = this;
        r9 = r18;
        r10 = r19;
        r11 = new com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
        r2 = com.google.android.exoplayer2.mediacodec.MediaCodecSelector.DEFAULT;
        r7 = com.google.android.exoplayer2.audio.AudioCapabilities.getCapabilities(r13);
        r4 = 1;
        r1 = r11;
        r3 = r14;
        r5 = r16;
        r6 = r17;
        r8 = r15;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);
        r10.add(r11);
        if (r9 != 0) goto L_0x001d;
    L_0x001c:
        return;
    L_0x001d:
        r1 = r19.size();
        r2 = 2;
        if (r9 != r2) goto L_0x0026;
    L_0x0024:
        r1 = r1 + -1;
    L_0x0026:
        r3 = 0;
        r4 = 3;
        r5 = 1;
        r6 = "com.google.android.exoplayer2.ext.opus.LibopusAudioRenderer";	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r6 = java.lang.Class.forName(r6);	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r8 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7[r3] = r8;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r8 = com.google.android.exoplayer2.audio.AudioRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7[r5] = r8;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r8 = com.google.android.exoplayer2.audio.AudioProcessor[].class;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7[r2] = r8;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r6 = r6.getConstructor(r7);	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7 = new java.lang.Object[r4];	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7[r3] = r16;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7[r5] = r17;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7[r2] = r15;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r6 = r6.newInstance(r7);	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r6 = (com.google.android.exoplayer2.Renderer) r6;	 Catch:{ ClassNotFoundException -> 0x0066, Exception -> 0x005c }
        r7 = r1 + 1;
        r10.add(r1, r6);	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005c }
        r1 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005c }
        r6 = "Loaded LibopusAudioRenderer.";	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005c }
        android.util.Log.i(r1, r6);	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005c }
        goto L_0x0067;
    L_0x005c:
        r0 = move-exception;
        r1 = r0;
        r2 = new java.lang.RuntimeException;
        r3 = "Error instantiating Opus extension";
        r2.<init>(r3, r1);
        throw r2;
    L_0x0066:
        r7 = r1;
    L_0x0067:
        r1 = "com.google.android.exoplayer2.ext.flac.LibflacAudioRenderer";	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r8 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6[r3] = r8;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r8 = com.google.android.exoplayer2.audio.AudioRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6[r5] = r8;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r8 = com.google.android.exoplayer2.audio.AudioProcessor[].class;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6[r2] = r8;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r1 = r1.getConstructor(r6);	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6 = new java.lang.Object[r4];	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6[r3] = r16;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6[r5] = r17;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6[r2] = r15;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r1 = r1.newInstance(r6);	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r1 = (com.google.android.exoplayer2.Renderer) r1;	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x009a }
        r6 = r7 + 1;
        r10.add(r7, r1);	 Catch:{ ClassNotFoundException -> 0x00a5, Exception -> 0x009a }
        r1 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x00a5, Exception -> 0x009a }
        r7 = "Loaded LibflacAudioRenderer.";	 Catch:{ ClassNotFoundException -> 0x00a5, Exception -> 0x009a }
        android.util.Log.i(r1, r7);	 Catch:{ ClassNotFoundException -> 0x00a5, Exception -> 0x009a }
        goto L_0x00a5;
    L_0x009a:
        r0 = move-exception;
        r1 = r0;
        r2 = new java.lang.RuntimeException;
        r3 = "Error instantiating FLAC extension";
        r2.<init>(r3, r1);
        throw r2;
    L_0x00a4:
        r6 = r7;
    L_0x00a5:
        r1 = "com.google.android.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer";	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r7 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r8 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r7[r3] = r8;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r8 = com.google.android.exoplayer2.audio.AudioRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r7[r5] = r8;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r8 = com.google.android.exoplayer2.audio.AudioProcessor[].class;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r7[r2] = r8;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r1 = r1.getConstructor(r7);	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r4 = new java.lang.Object[r4];	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r4[r3] = r16;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r4[r5] = r17;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r4[r2] = r15;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r1 = r1.newInstance(r4);	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r1 = (com.google.android.exoplayer2.Renderer) r1;	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r10.add(r6, r1);	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r1 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        r2 = "Loaded FfmpegAudioRenderer.";	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        android.util.Log.i(r1, r2);	 Catch:{ ClassNotFoundException -> 0x00e0, Exception -> 0x00d6 }
        goto L_0x00e0;
    L_0x00d6:
        r0 = move-exception;
        r1 = r0;
        r2 = new java.lang.RuntimeException;
        r3 = "Error instantiating FFmpeg extension";
        r2.<init>(r3, r1);
        throw r2;
    L_0x00e0:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.player.MyDefaultRenderersFactory.buildAudioRenderers(android.content.Context, com.google.android.exoplayer2.drm.DrmSessionManager, com.google.android.exoplayer2.audio.AudioProcessor[], android.os.Handler, com.google.android.exoplayer2.audio.AudioRendererEventListener, int, java.util.ArrayList):void");
    }

    protected void buildTextRenderers(Context context, TextOutput textOutput, Looper looper, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new MyTextRenderer(textOutput, looper));
    }

    protected void buildMetadataRenderers(Context context, MetadataOutput metadataOutput, Looper looper, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new MetadataRenderer(metadataOutput, looper));
    }

    protected AudioProcessor[] buildAudioProcessors() {
        return new AudioProcessor[0];
    }
}
