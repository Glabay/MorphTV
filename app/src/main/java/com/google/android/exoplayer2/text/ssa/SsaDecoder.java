package com.google.android.exoplayer2.text.ssa;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class SsaDecoder extends SimpleSubtitleDecoder {
    private static final String DIALOGUE_LINE_PREFIX = "Dialogue: ";
    private static final String FORMAT_LINE_PREFIX = "Format: ";
    private static final Pattern SSA_TIMECODE_PATTERN = Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+)(?::|\\.)(\\d+)");
    private static final String TAG = "SsaDecoder";
    private int formatEndIndex;
    private int formatKeyCount;
    private int formatStartIndex;
    private int formatTextIndex;
    private final boolean haveInitializationData;

    public SsaDecoder() {
        this(null);
    }

    public SsaDecoder(List<byte[]> list) {
        super(TAG);
        if (list == null || list.isEmpty()) {
            this.haveInitializationData = false;
            return;
        }
        this.haveInitializationData = true;
        String str = new String((byte[]) list.get(0));
        Assertions.checkArgument(str.startsWith(FORMAT_LINE_PREFIX));
        parseFormatLine(str);
        parseHeader(new ParsableByteArray((byte[]) list.get(1)));
    }

    protected SsaSubtitle decode(byte[] bArr, int i, boolean z) {
        z = new ArrayList();
        LongArray longArray = new LongArray();
        ParsableByteArray parsableByteArray = new ParsableByteArray(bArr, i);
        if (this.haveInitializationData == null) {
            parseHeader(parsableByteArray);
        }
        parseEventBody(parsableByteArray, z, longArray);
        bArr = new Cue[z.size()];
        z.toArray(bArr);
        return new SsaSubtitle(bArr, longArray.toArray());
    }

    private void parseHeader(ParsableByteArray parsableByteArray) {
        String readLine;
        do {
            readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return;
            }
        } while (!readLine.startsWith("[Events]"));
    }

    private void parseEventBody(ParsableByteArray parsableByteArray, List<Cue> list, LongArray longArray) {
        while (true) {
            String readLine = parsableByteArray.readLine();
            if (readLine == null) {
                return;
            }
            if (!this.haveInitializationData && readLine.startsWith(FORMAT_LINE_PREFIX)) {
                parseFormatLine(readLine);
            } else if (readLine.startsWith(DIALOGUE_LINE_PREFIX)) {
                parseDialogueLine(readLine, list, longArray);
            }
        }
    }

    private void parseFormatLine(String str) {
        str = TextUtils.split(str.substring(FORMAT_LINE_PREFIX.length()), ",");
        this.formatKeyCount = str.length;
        this.formatStartIndex = -1;
        this.formatEndIndex = -1;
        this.formatTextIndex = -1;
        for (int i = 0; i < this.formatKeyCount; i++) {
            Object obj;
            String toLowerInvariant = Util.toLowerInvariant(str[i].trim());
            int hashCode = toLowerInvariant.hashCode();
            if (hashCode != 100571) {
                if (hashCode != 3556653) {
                    if (hashCode == 109757538) {
                        if (toLowerInvariant.equals(TtmlNode.START)) {
                            obj = null;
                            switch (obj) {
                                case null:
                                    this.formatStartIndex = i;
                                    break;
                                case 1:
                                    this.formatEndIndex = i;
                                    break;
                                case 2:
                                    this.formatTextIndex = i;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } else if (toLowerInvariant.equals(MimeTypes.BASE_TYPE_TEXT)) {
                    obj = 2;
                    switch (obj) {
                        case null:
                            this.formatStartIndex = i;
                            break;
                        case 1:
                            this.formatEndIndex = i;
                            break;
                        case 2:
                            this.formatTextIndex = i;
                            break;
                        default:
                            break;
                    }
                }
            } else if (toLowerInvariant.equals(TtmlNode.END)) {
                obj = 1;
                switch (obj) {
                    case null:
                        this.formatStartIndex = i;
                        break;
                    case 1:
                        this.formatEndIndex = i;
                        break;
                    case 2:
                        this.formatTextIndex = i;
                        break;
                    default:
                        break;
                }
            }
            obj = -1;
            switch (obj) {
                case null:
                    this.formatStartIndex = i;
                    break;
                case 1:
                    this.formatEndIndex = i;
                    break;
                case 2:
                    this.formatTextIndex = i;
                    break;
                default:
                    break;
            }
        }
        if (this.formatStartIndex == -1 || this.formatEndIndex == -1 || this.formatTextIndex == -1) {
            this.formatKeyCount = 0;
        }
    }

    private void parseDialogueLine(String str, List<Cue> list, LongArray longArray) {
        if (this.formatKeyCount == 0) {
            list = TAG;
            longArray = new StringBuilder();
            longArray.append("Skipping dialogue line before complete format: ");
            longArray.append(str);
            Log.w(list, longArray.toString());
            return;
        }
        String[] split = str.substring(DIALOGUE_LINE_PREFIX.length()).split(",", this.formatKeyCount);
        if (split.length != this.formatKeyCount) {
            list = TAG;
            longArray = new StringBuilder();
            longArray.append("Skipping dialogue line with fewer columns than format: ");
            longArray.append(str);
            Log.w(list, longArray.toString());
            return;
        }
        long parseTimecodeUs = parseTimecodeUs(split[this.formatStartIndex]);
        if (parseTimecodeUs == C0649C.TIME_UNSET) {
            list = TAG;
            longArray = new StringBuilder();
            longArray.append("Skipping invalid timing: ");
            longArray.append(str);
            Log.w(list, longArray.toString());
            return;
        }
        long j;
        String str2 = split[this.formatEndIndex];
        if (str2.trim().isEmpty()) {
            j = C0649C.TIME_UNSET;
        } else {
            j = parseTimecodeUs(str2);
            if (j == C0649C.TIME_UNSET) {
                list = TAG;
                longArray = new StringBuilder();
                longArray.append("Skipping invalid timing: ");
                longArray.append(str);
                Log.w(list, longArray.toString());
                return;
            }
        }
        list.add(new Cue(split[this.formatTextIndex].replaceAll("\\{.*?\\}", "").replaceAll("\\\\N", "\n").replaceAll("\\\\n", "\n")));
        longArray.add(parseTimecodeUs);
        if (j != C0649C.TIME_UNSET) {
            list.add(null);
            longArray.add(j);
        }
    }

    public static long parseTimecodeUs(String str) {
        str = SSA_TIMECODE_PATTERN.matcher(str);
        if (str.matches()) {
            return (((((Long.parseLong(str.group(1)) * 60) * 60) * C0649C.MICROS_PER_SECOND) + ((Long.parseLong(str.group(2)) * 60) * C0649C.MICROS_PER_SECOND)) + (Long.parseLong(str.group(3)) * C0649C.MICROS_PER_SECOND)) + (Long.parseLong(str.group(4)) * NotificationOptions.SKIP_STEP_TEN_SECONDS_IN_MS);
        }
        return C0649C.TIME_UNSET;
    }
}
