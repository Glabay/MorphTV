package com.google.android.exoplayer2.text.cea;

import android.graphics.Color;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import kotlin.text.Typography;

public final class Cea708Decoder extends CeaDecoder {
    private static final int CC_VALID_FLAG = 4;
    private static final int CHARACTER_BIG_CARONS = 42;
    private static final int CHARACTER_BIG_OE = 44;
    private static final int CHARACTER_BOLD_BULLET = 53;
    private static final int CHARACTER_CLOSE_DOUBLE_QUOTE = 52;
    private static final int CHARACTER_CLOSE_SINGLE_QUOTE = 50;
    private static final int CHARACTER_DIAERESIS_Y = 63;
    private static final int CHARACTER_ELLIPSIS = 37;
    private static final int CHARACTER_FIVE_EIGHTHS = 120;
    private static final int CHARACTER_HORIZONTAL_BORDER = 125;
    private static final int CHARACTER_LOWER_LEFT_BORDER = 124;
    private static final int CHARACTER_LOWER_RIGHT_BORDER = 126;
    private static final int CHARACTER_MN = 127;
    private static final int CHARACTER_NBTSP = 33;
    private static final int CHARACTER_ONE_EIGHTH = 118;
    private static final int CHARACTER_OPEN_DOUBLE_QUOTE = 51;
    private static final int CHARACTER_OPEN_SINGLE_QUOTE = 49;
    private static final int CHARACTER_SEVEN_EIGHTHS = 121;
    private static final int CHARACTER_SM = 61;
    private static final int CHARACTER_SMALL_CARONS = 58;
    private static final int CHARACTER_SMALL_OE = 60;
    private static final int CHARACTER_SOLID_BLOCK = 48;
    private static final int CHARACTER_THREE_EIGHTHS = 119;
    private static final int CHARACTER_TM = 57;
    private static final int CHARACTER_TSP = 32;
    private static final int CHARACTER_UPPER_LEFT_BORDER = 127;
    private static final int CHARACTER_UPPER_RIGHT_BORDER = 123;
    private static final int CHARACTER_VERTICAL_BORDER = 122;
    private static final int COMMAND_BS = 8;
    private static final int COMMAND_CLW = 136;
    private static final int COMMAND_CR = 13;
    private static final int COMMAND_CW0 = 128;
    private static final int COMMAND_CW1 = 129;
    private static final int COMMAND_CW2 = 130;
    private static final int COMMAND_CW3 = 131;
    private static final int COMMAND_CW4 = 132;
    private static final int COMMAND_CW5 = 133;
    private static final int COMMAND_CW6 = 134;
    private static final int COMMAND_CW7 = 135;
    private static final int COMMAND_DF0 = 152;
    private static final int COMMAND_DF1 = 153;
    private static final int COMMAND_DF2 = 154;
    private static final int COMMAND_DF3 = 155;
    private static final int COMMAND_DF4 = 156;
    private static final int COMMAND_DF5 = 157;
    private static final int COMMAND_DF6 = 158;
    private static final int COMMAND_DF7 = 159;
    private static final int COMMAND_DLC = 142;
    private static final int COMMAND_DLW = 140;
    private static final int COMMAND_DLY = 141;
    private static final int COMMAND_DSW = 137;
    private static final int COMMAND_ETX = 3;
    private static final int COMMAND_EXT1 = 16;
    private static final int COMMAND_EXT1_END = 23;
    private static final int COMMAND_EXT1_START = 17;
    private static final int COMMAND_FF = 12;
    private static final int COMMAND_HCR = 14;
    private static final int COMMAND_HDW = 138;
    private static final int COMMAND_NUL = 0;
    private static final int COMMAND_P16_END = 31;
    private static final int COMMAND_P16_START = 24;
    private static final int COMMAND_RST = 143;
    private static final int COMMAND_SPA = 144;
    private static final int COMMAND_SPC = 145;
    private static final int COMMAND_SPL = 146;
    private static final int COMMAND_SWA = 151;
    private static final int COMMAND_TGW = 139;
    private static final int DTVCC_PACKET_DATA = 2;
    private static final int DTVCC_PACKET_START = 3;
    private static final int GROUP_C0_END = 31;
    private static final int GROUP_C1_END = 159;
    private static final int GROUP_C2_END = 31;
    private static final int GROUP_C3_END = 159;
    private static final int GROUP_G0_END = 127;
    private static final int GROUP_G1_END = 255;
    private static final int GROUP_G2_END = 127;
    private static final int GROUP_G3_END = 255;
    private static final int NUM_WINDOWS = 8;
    private static final String TAG = "Cea708Decoder";
    private final ParsableByteArray ccData = new ParsableByteArray();
    private final CueBuilder[] cueBuilders;
    private List<Cue> cues;
    private CueBuilder currentCueBuilder;
    private DtvCcPacket currentDtvCcPacket;
    private int currentWindow;
    private List<Cue> lastCues;
    private final int selectedServiceNumber;
    private final ParsableBitArray serviceBlockPacket = new ParsableBitArray();

    private static final class CueBuilder {
        private static final int BORDER_AND_EDGE_TYPE_NONE = 0;
        private static final int BORDER_AND_EDGE_TYPE_UNIFORM = 3;
        public static final int COLOR_SOLID_BLACK = getArgbColorFromCeaColor(0, 0, 0, 0);
        public static final int COLOR_SOLID_WHITE = getArgbColorFromCeaColor(2, 2, 2, 0);
        public static final int COLOR_TRANSPARENT = getArgbColorFromCeaColor(0, 0, 0, 3);
        private static final int DEFAULT_PRIORITY = 4;
        private static final int DIRECTION_BOTTOM_TO_TOP = 3;
        private static final int DIRECTION_LEFT_TO_RIGHT = 0;
        private static final int DIRECTION_RIGHT_TO_LEFT = 1;
        private static final int DIRECTION_TOP_TO_BOTTOM = 2;
        private static final int HORIZONTAL_SIZE = 209;
        private static final int JUSTIFICATION_CENTER = 2;
        private static final int JUSTIFICATION_FULL = 3;
        private static final int JUSTIFICATION_LEFT = 0;
        private static final int JUSTIFICATION_RIGHT = 1;
        private static final int MAXIMUM_ROW_COUNT = 15;
        private static final int PEN_FONT_STYLE_DEFAULT = 0;
        private static final int PEN_FONT_STYLE_MONOSPACED_WITHOUT_SERIFS = 3;
        private static final int PEN_FONT_STYLE_MONOSPACED_WITH_SERIFS = 1;
        private static final int PEN_FONT_STYLE_PROPORTIONALLY_SPACED_WITHOUT_SERIFS = 4;
        private static final int PEN_FONT_STYLE_PROPORTIONALLY_SPACED_WITH_SERIFS = 2;
        private static final int PEN_OFFSET_NORMAL = 1;
        private static final int PEN_SIZE_STANDARD = 1;
        private static final int[] PEN_STYLE_BACKGROUND = new int[]{COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_TRANSPARENT, COLOR_TRANSPARENT};
        private static final int[] PEN_STYLE_EDGE_TYPE = new int[]{0, 0, 0, 0, 0, 3, 3};
        private static final int[] PEN_STYLE_FONT_STYLE = new int[]{0, 1, 2, 3, 4, 3, 4};
        private static final int RELATIVE_CUE_SIZE = 99;
        private static final int VERTICAL_SIZE = 74;
        private static final int[] WINDOW_STYLE_FILL = new int[]{COLOR_SOLID_BLACK, COLOR_TRANSPARENT, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK, COLOR_TRANSPARENT, COLOR_SOLID_BLACK, COLOR_SOLID_BLACK};
        private static final int[] WINDOW_STYLE_JUSTIFICATION = new int[]{0, 0, 0, 0, 0, 2, 0};
        private static final int[] WINDOW_STYLE_PRINT_DIRECTION = new int[]{0, 0, 0, 0, 0, 0, 2};
        private static final int[] WINDOW_STYLE_SCROLL_DIRECTION = new int[]{3, 3, 3, 3, 3, 3, 1};
        private static final boolean[] WINDOW_STYLE_WORD_WRAP = new boolean[]{false, false, false, true, true, true, false};
        private int anchorId;
        private int backgroundColor;
        private int backgroundColorStartPosition;
        private final SpannableStringBuilder captionStringBuilder = new SpannableStringBuilder();
        private boolean defined;
        private int foregroundColor;
        private int foregroundColorStartPosition;
        private int horizontalAnchor;
        private int italicsStartPosition;
        private int justification;
        private int penStyleId;
        private int priority;
        private boolean relativePositioning;
        private final List<SpannableString> rolledUpCaptions = new LinkedList();
        private int row;
        private int rowCount;
        private boolean rowLock;
        private int underlineStartPosition;
        private int verticalAnchor;
        private boolean visible;
        private int windowFillColor;
        private int windowStyleId;

        public CueBuilder() {
            reset();
        }

        public boolean isEmpty() {
            if (isDefined()) {
                if (!this.rolledUpCaptions.isEmpty() || this.captionStringBuilder.length() != 0) {
                    return false;
                }
            }
            return true;
        }

        public void reset() {
            clear();
            this.defined = false;
            this.visible = false;
            this.priority = 4;
            this.relativePositioning = false;
            this.verticalAnchor = 0;
            this.horizontalAnchor = 0;
            this.anchorId = 0;
            this.rowCount = 15;
            this.rowLock = true;
            this.justification = 0;
            this.windowStyleId = 0;
            this.penStyleId = 0;
            this.windowFillColor = COLOR_SOLID_BLACK;
            this.foregroundColor = COLOR_SOLID_WHITE;
            this.backgroundColor = COLOR_SOLID_BLACK;
        }

        public void clear() {
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.clear();
            this.italicsStartPosition = -1;
            this.underlineStartPosition = -1;
            this.foregroundColorStartPosition = -1;
            this.backgroundColorStartPosition = -1;
            this.row = 0;
        }

        public boolean isDefined() {
            return this.defined;
        }

        public void setVisibility(boolean z) {
            this.visible = z;
        }

        public boolean isVisible() {
            return this.visible;
        }

        public void defineWindow(boolean z, boolean z2, boolean z3, int i, boolean z4, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            boolean z5 = z2;
            int i9 = i7;
            int i10 = i8;
            this.defined = true;
            this.visible = z;
            this.rowLock = z5;
            this.priority = i;
            this.relativePositioning = z4;
            this.verticalAnchor = i2;
            this.horizontalAnchor = i3;
            this.anchorId = i6;
            int i11 = i4 + 1;
            if (this.rowCount != i11) {
                r8.rowCount = i11;
                while (true) {
                    if ((!z5 || r8.rolledUpCaptions.size() < r8.rowCount) && r8.rolledUpCaptions.size() < 15) {
                        break;
                    }
                    r8.rolledUpCaptions.remove(0);
                }
            }
            if (!(i9 == 0 || r8.windowStyleId == i9)) {
                r8.windowStyleId = i9;
                int i12 = i9 - 1;
                setWindowAttributes(WINDOW_STYLE_FILL[i12], COLOR_TRANSPARENT, WINDOW_STYLE_WORD_WRAP[i12], 0, WINDOW_STYLE_PRINT_DIRECTION[i12], WINDOW_STYLE_SCROLL_DIRECTION[i12], WINDOW_STYLE_JUSTIFICATION[i12]);
            }
            if (i10 != 0 && r8.penStyleId != i10) {
                r8.penStyleId = i10;
                i10--;
                setPenAttributes(0, 1, 1, false, false, PEN_STYLE_EDGE_TYPE[i10], PEN_STYLE_FONT_STYLE[i10]);
                setPenColor(COLOR_SOLID_WHITE, PEN_STYLE_BACKGROUND[i10], COLOR_SOLID_BLACK);
            }
        }

        public void setWindowAttributes(int i, int i2, boolean z, int i3, int i4, int i5, int i6) {
            this.windowFillColor = i;
            this.justification = i6;
        }

        public void setPenAttributes(int i, int i2, int i3, boolean z, boolean z2, int i4, int i5) {
            if (this.italicsStartPosition != -1) {
                if (!z) {
                    this.captionStringBuilder.setSpan(new StyleSpan(2), this.italicsStartPosition, this.captionStringBuilder.length(), 33);
                    this.italicsStartPosition = -1;
                }
            } else if (z) {
                this.italicsStartPosition = this.captionStringBuilder.length();
            }
            if (this.underlineStartPosition != -1) {
                if (!z2) {
                    this.captionStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, this.captionStringBuilder.length(), 33);
                    this.underlineStartPosition = -1;
                }
            } else if (z2) {
                this.underlineStartPosition = this.captionStringBuilder.length();
            }
        }

        public void setPenColor(int i, int i2, int i3) {
            if (!(this.foregroundColorStartPosition == -1 || this.foregroundColor == i)) {
                this.captionStringBuilder.setSpan(new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, this.captionStringBuilder.length(), 33);
            }
            if (i != COLOR_SOLID_WHITE) {
                this.foregroundColorStartPosition = this.captionStringBuilder.length();
                this.foregroundColor = i;
            }
            if (!(this.backgroundColorStartPosition == -1 || this.backgroundColor == i2)) {
                this.captionStringBuilder.setSpan(new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, this.captionStringBuilder.length(), 33);
            }
            if (i2 != COLOR_SOLID_BLACK) {
                this.backgroundColorStartPosition = this.captionStringBuilder.length();
                this.backgroundColor = i2;
            }
        }

        public void setPenLocation(int i, int i2) {
            if (this.row != i) {
                append(10);
            }
            this.row = i;
        }

        public void backspace() {
            int length = this.captionStringBuilder.length();
            if (length > 0) {
                this.captionStringBuilder.delete(length - 1, length);
            }
        }

        public void append(char c) {
            if (c == '\n') {
                this.rolledUpCaptions.add(buildSpannableString());
                this.captionStringBuilder.clear();
                if (this.italicsStartPosition != '￿') {
                    this.italicsStartPosition = 0;
                }
                if (this.underlineStartPosition != '￿') {
                    this.underlineStartPosition = 0;
                }
                if (this.foregroundColorStartPosition != '￿') {
                    this.foregroundColorStartPosition = 0;
                }
                if (this.backgroundColorStartPosition != '￿') {
                    this.backgroundColorStartPosition = 0;
                }
                while (true) {
                    if ((this.rowLock != '\u0000' && this.rolledUpCaptions.size() >= this.rowCount) || this.rolledUpCaptions.size() >= '\u000f') {
                        this.rolledUpCaptions.remove(0);
                    } else {
                        return;
                    }
                }
            }
            this.captionStringBuilder.append(c);
        }

        public SpannableString buildSpannableString() {
            CharSequence spannableStringBuilder = new SpannableStringBuilder(this.captionStringBuilder);
            int length = spannableStringBuilder.length();
            if (length > 0) {
                if (this.italicsStartPosition != -1) {
                    spannableStringBuilder.setSpan(new StyleSpan(2), this.italicsStartPosition, length, 33);
                }
                if (this.underlineStartPosition != -1) {
                    spannableStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, length, 33);
                }
                if (this.foregroundColorStartPosition != -1) {
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(this.foregroundColor), this.foregroundColorStartPosition, length, 33);
                }
                if (this.backgroundColorStartPosition != -1) {
                    spannableStringBuilder.setSpan(new BackgroundColorSpan(this.backgroundColor), this.backgroundColorStartPosition, length, 33);
                }
            }
            return new SpannableString(spannableStringBuilder);
        }

        public Cea708Cue build() {
            if (isEmpty()) {
                return null;
            }
            Alignment alignment;
            float f;
            float f2;
            CharSequence spannableStringBuilder = new SpannableStringBuilder();
            for (int i = 0; i < this.rolledUpCaptions.size(); i++) {
                spannableStringBuilder.append((CharSequence) this.rolledUpCaptions.get(i));
                spannableStringBuilder.append('\n');
            }
            spannableStringBuilder.append(buildSpannableString());
            switch (this.justification) {
                case 0:
                case 3:
                    alignment = Alignment.ALIGN_NORMAL;
                    break;
                case 1:
                    alignment = Alignment.ALIGN_OPPOSITE;
                    break;
                case 2:
                    alignment = Alignment.ALIGN_CENTER;
                    break;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected justification value: ");
                    stringBuilder.append(this.justification);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
            Alignment alignment2 = alignment;
            if (this.relativePositioning) {
                f = ((float) this.horizontalAnchor) / 99.0f;
                f2 = ((float) this.verticalAnchor) / 99.0f;
            } else {
                f = ((float) this.horizontalAnchor) / 209.0f;
                f2 = ((float) this.verticalAnchor) / 74.0f;
            }
            float f3 = (f * 0.9f) + 0.05f;
            float f4 = (f2 * 0.9f) + 0.05f;
            int i2 = this.anchorId % 3 == 0 ? 0 : this.anchorId % 3 == 1 ? 1 : 2;
            int i3 = this.anchorId / 3 == 0 ? 0 : this.anchorId / 3 == 1 ? 1 : 2;
            return new Cea708Cue(spannableStringBuilder, alignment2, f4, 0, i2, f3, i3, Float.MIN_VALUE, this.windowFillColor != COLOR_SOLID_BLACK, this.windowFillColor, this.priority);
        }

        public static int getArgbColorFromCeaColor(int i, int i2, int i3) {
            return getArgbColorFromCeaColor(i, i2, i3, 0);
        }

        public static int getArgbColorFromCeaColor(int i, int i2, int i3, int i4) {
            Assertions.checkIndex(i, 0, 4);
            Assertions.checkIndex(i2, 0, 4);
            Assertions.checkIndex(i3, 0, 4);
            Assertions.checkIndex(i4, 0, 4);
            int i5 = 255;
            switch (i4) {
                case 2:
                    i4 = 127;
                    break;
                case 3:
                    i4 = 0;
                    break;
                default:
                    i4 = 255;
                    break;
            }
            i = i > 1 ? 255 : 0;
            i2 = i2 > 1 ? 255 : 0;
            if (i3 <= 1) {
                i5 = 0;
            }
            return Color.argb(i4, i, i2, i5);
        }
    }

    private static final class DtvCcPacket {
        int currentIndex = 0;
        public final byte[] packetData;
        public final int packetSize;
        public final int sequenceNumber;

        public DtvCcPacket(int i, int i2) {
            this.sequenceNumber = i;
            this.packetSize = i2;
            this.packetData = new byte[((i2 * 2) - 1)];
        }
    }

    public String getName() {
        return TAG;
    }

    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
        return super.dequeueInputBuffer();
    }

    public /* bridge */ /* synthetic */ SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
        return super.dequeueOutputBuffer();
    }

    public /* bridge */ /* synthetic */ void queueInputBuffer(SubtitleInputBuffer subtitleInputBuffer) throws SubtitleDecoderException {
        super.queueInputBuffer(subtitleInputBuffer);
    }

    public /* bridge */ /* synthetic */ void release() {
        super.release();
    }

    public /* bridge */ /* synthetic */ void setPositionUs(long j) {
        super.setPositionUs(j);
    }

    public Cea708Decoder(int i) {
        if (i == -1) {
            i = 1;
        }
        this.selectedServiceNumber = i;
        this.cueBuilders = new CueBuilder[8];
        for (int i2 = 0; i2 < 8; i2++) {
            this.cueBuilders[i2] = new CueBuilder();
        }
        this.currentCueBuilder = this.cueBuilders[0];
        resetCueBuilders();
    }

    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        this.currentWindow = 0;
        this.currentCueBuilder = this.cueBuilders[this.currentWindow];
        resetCueBuilders();
        this.currentDtvCcPacket = null;
    }

    protected boolean isNewSubtitleDataAvailable() {
        return this.cues != this.lastCues;
    }

    protected Subtitle createSubtitle() {
        this.lastCues = this.cues;
        return new CeaSubtitle(this.cues);
    }

    protected void decode(SubtitleInputBuffer subtitleInputBuffer) {
        this.ccData.reset(subtitleInputBuffer.data.array(), subtitleInputBuffer.data.limit());
        while (this.ccData.bytesLeft() >= 3) {
            subtitleInputBuffer = this.ccData.readUnsignedByte() & 7;
            int i = subtitleInputBuffer & 3;
            boolean z = false;
            subtitleInputBuffer = (subtitleInputBuffer & 4) == 4 ? true : null;
            byte readUnsignedByte = (byte) this.ccData.readUnsignedByte();
            byte readUnsignedByte2 = (byte) this.ccData.readUnsignedByte();
            if (i == 2 || i == 3) {
                if (subtitleInputBuffer != null) {
                    DtvCcPacket dtvCcPacket;
                    if (i == 3) {
                        finalizeCurrentPacket();
                        subtitleInputBuffer = (readUnsignedByte & 192) >> 6;
                        int i2 = readUnsignedByte & 63;
                        if (i2 == 0) {
                            i2 = 64;
                        }
                        this.currentDtvCcPacket = new DtvCcPacket(subtitleInputBuffer, i2);
                        subtitleInputBuffer = this.currentDtvCcPacket.packetData;
                        dtvCcPacket = this.currentDtvCcPacket;
                        i = dtvCcPacket.currentIndex;
                        dtvCcPacket.currentIndex = i + 1;
                        subtitleInputBuffer[i] = readUnsignedByte2;
                    } else {
                        if (i == 2) {
                            z = true;
                        }
                        Assertions.checkArgument(z);
                        if (this.currentDtvCcPacket == null) {
                            Log.e(TAG, "Encountered DTVCC_PACKET_DATA before DTVCC_PACKET_START");
                        } else {
                            subtitleInputBuffer = this.currentDtvCcPacket.packetData;
                            dtvCcPacket = this.currentDtvCcPacket;
                            i = dtvCcPacket.currentIndex;
                            dtvCcPacket.currentIndex = i + 1;
                            subtitleInputBuffer[i] = readUnsignedByte;
                            subtitleInputBuffer = this.currentDtvCcPacket.packetData;
                            dtvCcPacket = this.currentDtvCcPacket;
                            i = dtvCcPacket.currentIndex;
                            dtvCcPacket.currentIndex = i + 1;
                            subtitleInputBuffer[i] = readUnsignedByte2;
                        }
                    }
                    if (this.currentDtvCcPacket.currentIndex == (this.currentDtvCcPacket.packetSize * 2) - 1) {
                        finalizeCurrentPacket();
                    }
                }
            }
        }
    }

    private void finalizeCurrentPacket() {
        if (this.currentDtvCcPacket != null) {
            processCurrentPacket();
            this.currentDtvCcPacket = null;
        }
    }

    private void processCurrentPacket() {
        if (this.currentDtvCcPacket.currentIndex != (this.currentDtvCcPacket.packetSize * 2) - 1) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DtvCcPacket ended prematurely; size is ");
            stringBuilder.append((this.currentDtvCcPacket.packetSize * 2) - 1);
            stringBuilder.append(", but current index is ");
            stringBuilder.append(this.currentDtvCcPacket.currentIndex);
            stringBuilder.append(" (sequence number ");
            stringBuilder.append(this.currentDtvCcPacket.sequenceNumber);
            stringBuilder.append("); ignoring packet");
            Log.w(str, stringBuilder.toString());
            return;
        }
        this.serviceBlockPacket.reset(this.currentDtvCcPacket.packetData, this.currentDtvCcPacket.currentIndex);
        int readBits = this.serviceBlockPacket.readBits(3);
        int readBits2 = this.serviceBlockPacket.readBits(5);
        if (readBits == 7) {
            this.serviceBlockPacket.skipBits(2);
            readBits += this.serviceBlockPacket.readBits(6);
        }
        if (readBits2 == 0) {
            if (readBits != 0) {
                String str2 = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("serviceNumber is non-zero (");
                stringBuilder2.append(readBits);
                stringBuilder2.append(") when blockSize is 0");
                Log.w(str2, stringBuilder2.toString());
            }
        } else if (readBits == this.selectedServiceNumber) {
            Object obj = null;
            while (this.serviceBlockPacket.bitsLeft() > 0) {
                readBits2 = this.serviceBlockPacket.readBits(8);
                String str3;
                StringBuilder stringBuilder3;
                if (readBits2 == 16) {
                    readBits2 = this.serviceBlockPacket.readBits(8);
                    if (readBits2 <= 31) {
                        handleC2Command(readBits2);
                    } else if (readBits2 <= 127) {
                        handleG2Character(readBits2);
                    } else if (readBits2 <= 159) {
                        handleC3Command(readBits2);
                    } else if (readBits2 <= 255) {
                        handleG3Character(readBits2);
                    } else {
                        str3 = TAG;
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("Invalid extended command: ");
                        stringBuilder3.append(readBits2);
                        Log.w(str3, stringBuilder3.toString());
                    }
                } else if (readBits2 <= 31) {
                    handleC0Command(readBits2);
                } else if (readBits2 <= 127) {
                    handleG0Character(readBits2);
                } else if (readBits2 <= 159) {
                    handleC1Command(readBits2);
                } else if (readBits2 <= 255) {
                    handleG1Character(readBits2);
                } else {
                    str3 = TAG;
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Invalid base command: ");
                    stringBuilder3.append(readBits2);
                    Log.w(str3, stringBuilder3.toString());
                }
                obj = 1;
            }
            if (obj != null) {
                this.cues = getDisplayCues();
            }
        }
    }

    private void handleC0Command(int i) {
        if (i == 0) {
            return;
        }
        if (i == 3) {
            this.cues = getDisplayCues();
        } else if (i != 8) {
            switch (i) {
                case 12:
                    resetCueBuilders();
                    return;
                case 13:
                    this.currentCueBuilder.append('\n');
                    return;
                case 14:
                    return;
                default:
                    if (i >= 17 && i <= 23) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Currently unsupported COMMAND_EXT1 Command: ");
                        stringBuilder.append(i);
                        Log.w(str, stringBuilder.toString());
                        this.serviceBlockPacket.skipBits(8);
                        return;
                    } else if (i < 24 || i > 31) {
                        r0 = TAG;
                        r1 = new StringBuilder();
                        r1.append("Invalid C0 command: ");
                        r1.append(i);
                        Log.w(r0, r1.toString());
                        return;
                    } else {
                        r0 = TAG;
                        r1 = new StringBuilder();
                        r1.append("Currently unsupported COMMAND_P16 Command: ");
                        r1.append(i);
                        Log.w(r0, r1.toString());
                        this.serviceBlockPacket.skipBits(16);
                        return;
                    }
            }
        } else {
            this.currentCueBuilder.backspace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleC1Command(int r5) {
        /*
        r4 = this;
        r0 = 16;
        r1 = 8;
        r2 = 1;
        switch(r5) {
            case 128: goto L_0x010c;
            case 129: goto L_0x010c;
            case 130: goto L_0x010c;
            case 131: goto L_0x010c;
            case 132: goto L_0x010c;
            case 133: goto L_0x010c;
            case 134: goto L_0x010c;
            case 135: goto L_0x010c;
            case 136: goto L_0x00f6;
            case 137: goto L_0x00df;
            case 138: goto L_0x00c8;
            case 139: goto L_0x00ac;
            case 140: goto L_0x0096;
            case 141: goto L_0x008f;
            case 142: goto L_0x011a;
            case 143: goto L_0x008a;
            case 144: goto L_0x0076;
            case 145: goto L_0x0060;
            case 146: goto L_0x004c;
            default: goto L_0x0008;
        };
    L_0x0008:
        switch(r5) {
            case 151: goto L_0x0036;
            case 152: goto L_0x0023;
            case 153: goto L_0x0023;
            case 154: goto L_0x0023;
            case 155: goto L_0x0023;
            case 156: goto L_0x0023;
            case 157: goto L_0x0023;
            case 158: goto L_0x0023;
            case 159: goto L_0x0023;
            default: goto L_0x000b;
        };
    L_0x000b:
        r0 = "Cea708Decoder";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid C1 command: ";
        r1.append(r2);
        r1.append(r5);
        r5 = r1.toString();
        android.util.Log.w(r0, r5);
        goto L_0x011a;
    L_0x0023:
        r5 = r5 + -152;
        r4.handleDefineWindow(r5);
        r0 = r4.currentWindow;
        if (r0 == r5) goto L_0x011a;
    L_0x002c:
        r4.currentWindow = r5;
        r0 = r4.cueBuilders;
        r5 = r0[r5];
        r4.currentCueBuilder = r5;
        goto L_0x011a;
    L_0x0036:
        r5 = r4.currentCueBuilder;
        r5 = r5.isDefined();
        if (r5 != 0) goto L_0x0047;
    L_0x003e:
        r5 = r4.serviceBlockPacket;
        r0 = 32;
        r5.skipBits(r0);
        goto L_0x011a;
    L_0x0047:
        r4.handleSetWindowAttributes();
        goto L_0x011a;
    L_0x004c:
        r5 = r4.currentCueBuilder;
        r5 = r5.isDefined();
        if (r5 != 0) goto L_0x005b;
    L_0x0054:
        r5 = r4.serviceBlockPacket;
        r5.skipBits(r0);
        goto L_0x011a;
    L_0x005b:
        r4.handleSetPenLocation();
        goto L_0x011a;
    L_0x0060:
        r5 = r4.currentCueBuilder;
        r5 = r5.isDefined();
        if (r5 != 0) goto L_0x0071;
    L_0x0068:
        r5 = r4.serviceBlockPacket;
        r0 = 24;
        r5.skipBits(r0);
        goto L_0x011a;
    L_0x0071:
        r4.handleSetPenColor();
        goto L_0x011a;
    L_0x0076:
        r5 = r4.currentCueBuilder;
        r5 = r5.isDefined();
        if (r5 != 0) goto L_0x0085;
    L_0x007e:
        r5 = r4.serviceBlockPacket;
        r5.skipBits(r0);
        goto L_0x011a;
    L_0x0085:
        r4.handleSetPenAttributes();
        goto L_0x011a;
    L_0x008a:
        r4.resetCueBuilders();
        goto L_0x011a;
    L_0x008f:
        r5 = r4.serviceBlockPacket;
        r5.skipBits(r1);
        goto L_0x011a;
    L_0x0096:
        if (r2 > r1) goto L_0x011a;
    L_0x0098:
        r5 = r4.serviceBlockPacket;
        r5 = r5.readBit();
        if (r5 == 0) goto L_0x00a9;
    L_0x00a0:
        r5 = r4.cueBuilders;
        r0 = 8 - r2;
        r5 = r5[r0];
        r5.reset();
    L_0x00a9:
        r2 = r2 + 1;
        goto L_0x0096;
    L_0x00ac:
        r5 = 1;
    L_0x00ad:
        if (r5 > r1) goto L_0x011a;
    L_0x00af:
        r0 = r4.serviceBlockPacket;
        r0 = r0.readBit();
        if (r0 == 0) goto L_0x00c5;
    L_0x00b7:
        r0 = r4.cueBuilders;
        r3 = 8 - r5;
        r0 = r0[r3];
        r3 = r0.isVisible();
        r3 = r3 ^ r2;
        r0.setVisibility(r3);
    L_0x00c5:
        r5 = r5 + 1;
        goto L_0x00ad;
    L_0x00c8:
        if (r2 > r1) goto L_0x011a;
    L_0x00ca:
        r5 = r4.serviceBlockPacket;
        r5 = r5.readBit();
        if (r5 == 0) goto L_0x00dc;
    L_0x00d2:
        r5 = r4.cueBuilders;
        r0 = 8 - r2;
        r5 = r5[r0];
        r0 = 0;
        r5.setVisibility(r0);
    L_0x00dc:
        r2 = r2 + 1;
        goto L_0x00c8;
    L_0x00df:
        r5 = 1;
    L_0x00e0:
        if (r5 > r1) goto L_0x011a;
    L_0x00e2:
        r0 = r4.serviceBlockPacket;
        r0 = r0.readBit();
        if (r0 == 0) goto L_0x00f3;
    L_0x00ea:
        r0 = r4.cueBuilders;
        r3 = 8 - r5;
        r0 = r0[r3];
        r0.setVisibility(r2);
    L_0x00f3:
        r5 = r5 + 1;
        goto L_0x00e0;
    L_0x00f6:
        if (r2 > r1) goto L_0x011a;
    L_0x00f8:
        r5 = r4.serviceBlockPacket;
        r5 = r5.readBit();
        if (r5 == 0) goto L_0x0109;
    L_0x0100:
        r5 = r4.cueBuilders;
        r0 = 8 - r2;
        r5 = r5[r0];
        r5.clear();
    L_0x0109:
        r2 = r2 + 1;
        goto L_0x00f6;
    L_0x010c:
        r5 = r5 + -128;
        r0 = r4.currentWindow;
        if (r0 == r5) goto L_0x011a;
    L_0x0112:
        r4.currentWindow = r5;
        r0 = r4.cueBuilders;
        r5 = r0[r5];
        r4.currentCueBuilder = r5;
    L_0x011a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.cea.Cea708Decoder.handleC1Command(int):void");
    }

    private void handleC2Command(int i) {
        if (i > 7) {
            if (i <= 15) {
                this.serviceBlockPacket.skipBits(8);
            } else if (i <= 23) {
                this.serviceBlockPacket.skipBits(16);
            } else if (i <= 31) {
                this.serviceBlockPacket.skipBits(24);
            }
        }
    }

    private void handleC3Command(int i) {
        if (i <= 135) {
            this.serviceBlockPacket.skipBits(32);
        } else if (i <= 143) {
            this.serviceBlockPacket.skipBits(40);
        } else if (i <= 159) {
            this.serviceBlockPacket.skipBits(2);
            this.serviceBlockPacket.skipBits(this.serviceBlockPacket.readBits(6) * 8);
        }
    }

    private void handleG0Character(int i) {
        if (i == 127) {
            this.currentCueBuilder.append('♫');
        } else {
            this.currentCueBuilder.append((char) (i & 255));
        }
    }

    private void handleG1Character(int i) {
        this.currentCueBuilder.append((char) (i & 255));
    }

    private void handleG2Character(int i) {
        if (i == 37) {
            this.currentCueBuilder.append(Typography.ellipsis);
        } else if (i == 42) {
            this.currentCueBuilder.append('Š');
        } else if (i == 44) {
            this.currentCueBuilder.append('Œ');
        } else if (i != 63) {
            switch (i) {
                case 32:
                    this.currentCueBuilder.append(' ');
                    return;
                case 33:
                    this.currentCueBuilder.append(Typography.nbsp);
                    return;
                default:
                    switch (i) {
                        case 48:
                            this.currentCueBuilder.append('█');
                            return;
                        case 49:
                            this.currentCueBuilder.append(Typography.leftSingleQuote);
                            return;
                        case 50:
                            this.currentCueBuilder.append(Typography.rightSingleQuote);
                            return;
                        case 51:
                            this.currentCueBuilder.append(Typography.leftDoubleQuote);
                            return;
                        case 52:
                            this.currentCueBuilder.append(Typography.rightDoubleQuote);
                            return;
                        case 53:
                            this.currentCueBuilder.append(Typography.bullet);
                            return;
                        default:
                            switch (i) {
                                case 57:
                                    this.currentCueBuilder.append(Typography.tm);
                                    return;
                                case 58:
                                    this.currentCueBuilder.append('š');
                                    return;
                                default:
                                    switch (i) {
                                        case 60:
                                            this.currentCueBuilder.append('œ');
                                            return;
                                        case 61:
                                            this.currentCueBuilder.append('℠');
                                            return;
                                        default:
                                            switch (i) {
                                                case 118:
                                                    this.currentCueBuilder.append('⅛');
                                                    return;
                                                case 119:
                                                    this.currentCueBuilder.append('⅜');
                                                    return;
                                                case CHARACTER_FIVE_EIGHTHS /*120*/:
                                                    this.currentCueBuilder.append('⅝');
                                                    return;
                                                case CHARACTER_SEVEN_EIGHTHS /*121*/:
                                                    this.currentCueBuilder.append('⅞');
                                                    return;
                                                case 122:
                                                    this.currentCueBuilder.append('│');
                                                    return;
                                                case CHARACTER_UPPER_RIGHT_BORDER /*123*/:
                                                    this.currentCueBuilder.append('┐');
                                                    return;
                                                case 124:
                                                    this.currentCueBuilder.append('└');
                                                    return;
                                                case CHARACTER_HORIZONTAL_BORDER /*125*/:
                                                    this.currentCueBuilder.append('─');
                                                    return;
                                                case 126:
                                                    this.currentCueBuilder.append('┘');
                                                    return;
                                                case 127:
                                                    this.currentCueBuilder.append('┌');
                                                    return;
                                                default:
                                                    String str = TAG;
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append("Invalid G2 character: ");
                                                    stringBuilder.append(i);
                                                    Log.w(str, stringBuilder.toString());
                                                    return;
                                            }
                                    }
                            }
                    }
            }
        } else {
            this.currentCueBuilder.append('Ÿ');
        }
    }

    private void handleG3Character(int i) {
        if (i == 160) {
            this.currentCueBuilder.append('㏄');
            return;
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid G3 character: ");
        stringBuilder.append(i);
        Log.w(str, stringBuilder.toString());
        this.currentCueBuilder.append('_');
    }

    private void handleSetPenAttributes() {
        this.currentCueBuilder.setPenAttributes(this.serviceBlockPacket.readBits(4), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBit(), this.serviceBlockPacket.readBit(), this.serviceBlockPacket.readBits(3), this.serviceBlockPacket.readBits(3));
    }

    private void handleSetPenColor() {
        int argbColorFromCeaColor = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        int argbColorFromCeaColor2 = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        this.serviceBlockPacket.skipBits(2);
        this.currentCueBuilder.setPenColor(argbColorFromCeaColor, argbColorFromCeaColor2, CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2)));
    }

    private void handleSetPenLocation() {
        this.serviceBlockPacket.skipBits(4);
        int readBits = this.serviceBlockPacket.readBits(4);
        this.serviceBlockPacket.skipBits(2);
        this.currentCueBuilder.setPenLocation(readBits, this.serviceBlockPacket.readBits(6));
    }

    private void handleSetWindowAttributes() {
        int argbColorFromCeaColor = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        int readBits = this.serviceBlockPacket.readBits(2);
        int argbColorFromCeaColor2 = CueBuilder.getArgbColorFromCeaColor(this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2), this.serviceBlockPacket.readBits(2));
        if (this.serviceBlockPacket.readBit()) {
            readBits |= 4;
        }
        int i = readBits;
        boolean readBit = this.serviceBlockPacket.readBit();
        int readBits2 = this.serviceBlockPacket.readBits(2);
        int readBits3 = this.serviceBlockPacket.readBits(2);
        int readBits4 = this.serviceBlockPacket.readBits(2);
        this.serviceBlockPacket.skipBits(8);
        this.currentCueBuilder.setWindowAttributes(argbColorFromCeaColor, argbColorFromCeaColor2, readBit, i, readBits2, readBits3, readBits4);
    }

    private void handleDefineWindow(int i) {
        CueBuilder cueBuilder = this.cueBuilders[i];
        this.serviceBlockPacket.skipBits(2);
        boolean readBit = this.serviceBlockPacket.readBit();
        boolean readBit2 = this.serviceBlockPacket.readBit();
        boolean readBit3 = this.serviceBlockPacket.readBit();
        i = this.serviceBlockPacket.readBits(3);
        boolean readBit4 = this.serviceBlockPacket.readBit();
        int readBits = this.serviceBlockPacket.readBits(7);
        int readBits2 = this.serviceBlockPacket.readBits(8);
        int readBits3 = this.serviceBlockPacket.readBits(4);
        int readBits4 = this.serviceBlockPacket.readBits(4);
        this.serviceBlockPacket.skipBits(2);
        int readBits5 = this.serviceBlockPacket.readBits(6);
        this.serviceBlockPacket.skipBits(2);
        cueBuilder.defineWindow(readBit, readBit2, readBit3, i, readBit4, readBits, readBits2, readBits4, readBits5, readBits3, this.serviceBlockPacket.readBits(3), this.serviceBlockPacket.readBits(3));
    }

    private List<Cue> getDisplayCues() {
        List arrayList = new ArrayList();
        int i = 0;
        while (i < 8) {
            if (!this.cueBuilders[i].isEmpty() && this.cueBuilders[i].isVisible()) {
                arrayList.add(this.cueBuilders[i].build());
            }
            i++;
        }
        Collections.sort(arrayList);
        return Collections.unmodifiableList(arrayList);
    }

    private void resetCueBuilders() {
        for (int i = 0; i < 8; i++) {
            this.cueBuilders[i].reset();
        }
    }
}
