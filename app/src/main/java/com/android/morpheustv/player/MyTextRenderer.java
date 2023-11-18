package com.android.morpheustv.player;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderFactory;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

public final class MyTextRenderer extends BaseRenderer implements Callback {
    private static final int MSG_UPDATE_OUTPUT = 0;
    private static final int REPLACEMENT_STATE_NONE = 0;
    private static final int REPLACEMENT_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REPLACEMENT_STATE_WAIT_END_OF_STREAM = 2;
    public static long offset;
    private SubtitleDecoder decoder;
    private final SubtitleDecoderFactory decoderFactory;
    private int decoderReplacementState;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private SubtitleInputBuffer nextInputBuffer;
    private SubtitleOutputBuffer nextSubtitle;
    private int nextSubtitleEventIndex;
    private final TextOutput output;
    private final Handler outputHandler;
    private boolean outputStreamEnded;
    private Format streamFormat;
    private SubtitleOutputBuffer subtitle;

    @Deprecated
    public interface Output extends TextOutput {
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface ReplacementState {
    }

    public boolean isReady() {
        return true;
    }

    public MyTextRenderer(TextOutput textOutput, Looper looper) {
        this(textOutput, looper, SubtitleDecoderFactory.DEFAULT);
    }

    public MyTextRenderer(TextOutput textOutput, Looper looper, SubtitleDecoderFactory subtitleDecoderFactory) {
        super(3);
        this.output = (TextOutput) Assertions.checkNotNull(textOutput);
        if (looper == null) {
            textOutput = null;
        } else {
            textOutput = new Handler(looper, this);
        }
        this.outputHandler = textOutput;
        this.decoderFactory = subtitleDecoderFactory;
        this.formatHolder = new FormatHolder();
    }

    public int supportsFormat(Format format) {
        if (!this.decoderFactory.supportsFormat(format)) {
            return MimeTypes.isText(format.sampleMimeType) != null ? 1 : null;
        } else {
            return BaseRenderer.supportsFormatDrm(null, format.drmInitData) != null ? 4 : 2;
        }
    }

    protected void onStreamChanged(Format[] formatArr, long j) throws ExoPlaybackException {
        this.streamFormat = formatArr[0];
        if (this.decoder != null) {
            this.decoderReplacementState = 1;
        } else {
            this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
        }
    }

    protected void onPositionReset(long j, boolean z) {
        clearOutput();
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.decoderReplacementState != null) {
            replaceDecoder();
            return;
        }
        releaseBuffers();
        this.decoder.flush();
    }

    public void render(long j, long j2) throws ExoPlaybackException {
        if (this.outputStreamEnded == null) {
            long j3 = j + offset;
            this.nextSubtitle = null;
            if (this.nextSubtitle == null) {
                this.decoder.setPositionUs(j3);
                try {
                    this.nextSubtitle = (SubtitleOutputBuffer) this.decoder.dequeueOutputBuffer();
                } catch (long j4) {
                    throw ExoPlaybackException.createForRenderer(j4, getIndex());
                }
            }
            if (getState() == 2) {
                Object obj;
                if (this.subtitle != null) {
                    long nextEventTime = getNextEventTime();
                    obj = null;
                    while (nextEventTime <= j3) {
                        this.nextSubtitleEventIndex++;
                        nextEventTime = getNextEventTime();
                        obj = 1;
                    }
                } else {
                    obj = null;
                }
                if (this.nextSubtitle != null) {
                    if (this.nextSubtitle.isEndOfStream()) {
                        if (obj == null && getNextEventTime() == Long.MAX_VALUE) {
                            if (this.decoderReplacementState == 2) {
                                replaceDecoder();
                            } else {
                                releaseBuffers();
                                this.outputStreamEnded = true;
                            }
                        }
                    } else if (this.nextSubtitle.timeUs <= j3) {
                        if (this.subtitle != null) {
                            this.subtitle.release();
                        }
                        this.subtitle = this.nextSubtitle;
                        this.nextSubtitle = null;
                        this.nextSubtitleEventIndex = this.subtitle.getNextEventTimeIndex(j3);
                        obj = 1;
                    }
                }
                if (obj != null) {
                    updateOutput(this.subtitle.getCues(j3));
                }
                if (this.decoderReplacementState != 2) {
                    while (!this.inputStreamEnded) {
                        try {
                            if (this.nextInputBuffer == null) {
                                this.nextInputBuffer = (SubtitleInputBuffer) this.decoder.dequeueInputBuffer();
                                if (this.nextInputBuffer == null) {
                                    return;
                                }
                            }
                            if (this.decoderReplacementState == 1) {
                                this.nextInputBuffer.setFlags(4);
                                this.decoder.queueInputBuffer(this.nextInputBuffer);
                                this.nextInputBuffer = null;
                                this.decoderReplacementState = 2;
                                return;
                            }
                            int readSource = readSource(this.formatHolder, this.nextInputBuffer, false);
                            if (readSource == -4) {
                                if (this.nextInputBuffer.isEndOfStream()) {
                                    this.inputStreamEnded = true;
                                } else {
                                    this.nextInputBuffer.subsampleOffsetUs = this.formatHolder.format.subsampleOffsetUs;
                                    this.nextInputBuffer.flip();
                                }
                                this.decoder.queueInputBuffer(this.nextInputBuffer);
                                this.nextInputBuffer = null;
                            } else if (readSource == -3) {
                                return;
                            }
                        } catch (long j42) {
                            throw ExoPlaybackException.createForRenderer(j42, getIndex());
                        }
                    }
                }
            }
        }
    }

    protected void onDisabled() {
        this.streamFormat = null;
        clearOutput();
        releaseDecoder();
    }

    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    private void releaseBuffers() {
        this.nextInputBuffer = null;
        this.nextSubtitleEventIndex = -1;
        if (this.subtitle != null) {
            this.subtitle.release();
            this.subtitle = null;
        }
        if (this.nextSubtitle != null) {
            this.nextSubtitle.release();
            this.nextSubtitle = null;
        }
    }

    private void releaseDecoder() {
        releaseBuffers();
        this.decoder.release();
        this.decoder = null;
        this.decoderReplacementState = 0;
    }

    private void replaceDecoder() {
        releaseDecoder();
        this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
    }

    private long getNextEventTime() {
        if (this.nextSubtitleEventIndex != -1) {
            if (this.nextSubtitleEventIndex < this.subtitle.getEventTimeCount()) {
                return this.subtitle.getEventTime(this.nextSubtitleEventIndex);
            }
        }
        return Long.MAX_VALUE;
    }

    private void updateOutput(List<Cue> list) {
        if (this.outputHandler != null) {
            this.outputHandler.obtainMessage(0, list).sendToTarget();
        } else {
            invokeUpdateOutputInternal(list);
        }
    }

    private void clearOutput() {
        updateOutput(Collections.emptyList());
    }

    public boolean handleMessage(Message message) {
        if (message.what != 0) {
            throw new IllegalStateException();
        }
        invokeUpdateOutputInternal((List) message.obj);
        return true;
    }

    private void invokeUpdateOutputInternal(List<Cue> list) {
        this.output.onCues(list);
    }
}
