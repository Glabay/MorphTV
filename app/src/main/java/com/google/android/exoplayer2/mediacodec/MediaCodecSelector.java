package com.google.android.exoplayer2.mediacodec;

import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;

public interface MediaCodecSelector {
    public static final MediaCodecSelector DEFAULT = new C06991();

    /* renamed from: com.google.android.exoplayer2.mediacodec.MediaCodecSelector$1 */
    static class C06991 implements MediaCodecSelector {
        C06991() {
        }

        public MediaCodecInfo getDecoderInfo(String str, boolean z) throws DecoderQueryException {
            return MediaCodecUtil.getDecoderInfo(str, z);
        }

        public MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    }

    MediaCodecInfo getDecoderInfo(String str, boolean z) throws DecoderQueryException;

    MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException;
}
