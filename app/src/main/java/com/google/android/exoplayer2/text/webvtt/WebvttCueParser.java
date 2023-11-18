package com.google.android.exoplayer2.text.webvtt;

import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import com.google.android.exoplayer2.text.webvtt.WebvttCue.Builder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public final class WebvttCueParser {
    private static final char CHAR_AMPERSAND = '&';
    private static final char CHAR_GREATER_THAN = '>';
    private static final char CHAR_LESS_THAN = '<';
    private static final char CHAR_SEMI_COLON = ';';
    private static final char CHAR_SLASH = '/';
    private static final char CHAR_SPACE = ' ';
    public static final Pattern CUE_HEADER_PATTERN = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
    private static final Pattern CUE_SETTING_PATTERN = Pattern.compile("(\\S+?):(\\S+)");
    private static final String ENTITY_AMPERSAND = "amp";
    private static final String ENTITY_GREATER_THAN = "gt";
    private static final String ENTITY_LESS_THAN = "lt";
    private static final String ENTITY_NON_BREAK_SPACE = "nbsp";
    private static final int STYLE_BOLD = 1;
    private static final int STYLE_ITALIC = 2;
    private static final String TAG = "WebvttCueParser";
    private static final String TAG_BOLD = "b";
    private static final String TAG_CLASS = "c";
    private static final String TAG_ITALIC = "i";
    private static final String TAG_LANG = "lang";
    private static final String TAG_UNDERLINE = "u";
    private static final String TAG_VOICE = "v";
    private final StringBuilder textBuilder = new StringBuilder();

    private static final class StartTag {
        private static final String[] NO_CLASSES = new String[0];
        public final String[] classes;
        public final String name;
        public final int position;
        public final String voice;

        private StartTag(String str, int i, String str2, String[] strArr) {
            this.position = i;
            this.name = str;
            this.voice = str2;
            this.classes = strArr;
        }

        public static StartTag buildStartTag(String str, int i) {
            str = str.trim();
            if (str.isEmpty()) {
                return null;
            }
            String str2;
            String trim;
            int indexOf = str.indexOf(StringUtils.SPACE);
            if (indexOf == -1) {
                str2 = "";
            } else {
                trim = str.substring(indexOf).trim();
                str = str.substring(0, indexOf);
                str2 = trim;
            }
            str = str.split("\\.");
            trim = str[0];
            if (str.length > 1) {
                str = (String[]) Arrays.copyOfRange(str, 1, str.length);
            } else {
                str = NO_CLASSES;
            }
            return new StartTag(trim, i, str2, str);
        }

        public static StartTag buildWholeCueVirtualTag() {
            return new StartTag("", 0, "", new String[0]);
        }
    }

    private static final class StyleMatch implements Comparable<StyleMatch> {
        public final int score;
        public final WebvttCssStyle style;

        public StyleMatch(int i, WebvttCssStyle webvttCssStyle) {
            this.score = i;
            this.style = webvttCssStyle;
        }

        public int compareTo(@NonNull StyleMatch styleMatch) {
            return this.score - styleMatch.score;
        }
    }

    public boolean parseCue(ParsableByteArray parsableByteArray, Builder builder, List<WebvttCssStyle> list) {
        Object readLine = parsableByteArray.readLine();
        if (readLine == null) {
            return false;
        }
        Matcher matcher = CUE_HEADER_PATTERN.matcher(readLine);
        if (matcher.matches()) {
            return parseCue(null, matcher, parsableByteArray, builder, this.textBuilder, list);
        }
        CharSequence readLine2 = parsableByteArray.readLine();
        if (readLine2 == null) {
            return false;
        }
        Matcher matcher2 = CUE_HEADER_PATTERN.matcher(readLine2);
        if (!matcher2.matches()) {
            return false;
        }
        return parseCue(readLine.trim(), matcher2, parsableByteArray, builder, this.textBuilder, list);
    }

    static void parseCueSettingsList(java.lang.String r5, com.google.android.exoplayer2.text.webvtt.WebvttCue.Builder r6) {
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
        r0 = CUE_SETTING_PATTERN;
        r5 = r0.matcher(r5);
    L_0x0006:
        r0 = r5.find();
        if (r0 == 0) goto L_0x0089;
    L_0x000c:
        r0 = 1;
        r0 = r5.group(r0);
        r1 = 2;
        r1 = r5.group(r1);
        r2 = "line";	 Catch:{ NumberFormatException -> 0x006d }
        r2 = r2.equals(r0);	 Catch:{ NumberFormatException -> 0x006d }
        if (r2 == 0) goto L_0x0022;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x001e:
        parseLineAttribute(r1, r6);	 Catch:{ NumberFormatException -> 0x006d }
        goto L_0x0006;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x0022:
        r2 = "align";	 Catch:{ NumberFormatException -> 0x006d }
        r2 = r2.equals(r0);	 Catch:{ NumberFormatException -> 0x006d }
        if (r2 == 0) goto L_0x0032;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x002a:
        r0 = parseTextAlignment(r1);	 Catch:{ NumberFormatException -> 0x006d }
        r6.setTextAlignment(r0);	 Catch:{ NumberFormatException -> 0x006d }
        goto L_0x0006;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x0032:
        r2 = "position";	 Catch:{ NumberFormatException -> 0x006d }
        r2 = r2.equals(r0);	 Catch:{ NumberFormatException -> 0x006d }
        if (r2 == 0) goto L_0x003e;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x003a:
        parsePositionAttribute(r1, r6);	 Catch:{ NumberFormatException -> 0x006d }
        goto L_0x0006;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x003e:
        r2 = "size";	 Catch:{ NumberFormatException -> 0x006d }
        r2 = r2.equals(r0);	 Catch:{ NumberFormatException -> 0x006d }
        if (r2 == 0) goto L_0x004e;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x0046:
        r0 = com.google.android.exoplayer2.text.webvtt.WebvttParserUtil.parsePercentage(r1);	 Catch:{ NumberFormatException -> 0x006d }
        r6.setWidth(r0);	 Catch:{ NumberFormatException -> 0x006d }
        goto L_0x0006;	 Catch:{ NumberFormatException -> 0x006d }
    L_0x004e:
        r2 = "WebvttCueParser";	 Catch:{ NumberFormatException -> 0x006d }
        r3 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x006d }
        r3.<init>();	 Catch:{ NumberFormatException -> 0x006d }
        r4 = "Unknown cue setting ";	 Catch:{ NumberFormatException -> 0x006d }
        r3.append(r4);	 Catch:{ NumberFormatException -> 0x006d }
        r3.append(r0);	 Catch:{ NumberFormatException -> 0x006d }
        r0 = ":";	 Catch:{ NumberFormatException -> 0x006d }
        r3.append(r0);	 Catch:{ NumberFormatException -> 0x006d }
        r3.append(r1);	 Catch:{ NumberFormatException -> 0x006d }
        r0 = r3.toString();	 Catch:{ NumberFormatException -> 0x006d }
        android.util.Log.w(r2, r0);	 Catch:{ NumberFormatException -> 0x006d }
        goto L_0x0006;
    L_0x006d:
        r0 = "WebvttCueParser";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Skipping bad cue setting: ";
        r1.append(r2);
        r2 = r5.group();
        r1.append(r2);
        r1 = r1.toString();
        android.util.Log.w(r0, r1);
        goto L_0x0006;
    L_0x0089:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.parseCueSettingsList(java.lang.String, com.google.android.exoplayer2.text.webvtt.WebvttCue$Builder):void");
    }

    static void parseCueText(String str, String str2, Builder builder, List<WebvttCssStyle> list) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        Stack stack = new Stack();
        List arrayList = new ArrayList();
        int i = 0;
        while (i < str2.length()) {
            char charAt = str2.charAt(i);
            if (charAt == '&') {
                i++;
                int indexOf = str2.indexOf(59, i);
                int indexOf2 = str2.indexOf(32, i);
                if (indexOf == -1) {
                    indexOf = indexOf2;
                } else if (indexOf2 != -1) {
                    indexOf = Math.min(indexOf, indexOf2);
                }
                if (indexOf != -1) {
                    applyEntity(str2.substring(i, indexOf), spannableStringBuilder);
                    if (indexOf == indexOf2) {
                        spannableStringBuilder.append(StringUtils.SPACE);
                    }
                    i = indexOf + 1;
                } else {
                    spannableStringBuilder.append(charAt);
                }
            } else if (charAt != '<') {
                spannableStringBuilder.append(charAt);
                i++;
            } else {
                int i2 = i + 1;
                if (i2 < str2.length()) {
                    int i3 = 1;
                    Object obj = str2.charAt(i2) == '/' ? 1 : null;
                    i2 = findEndOfTag(str2, i2);
                    int i4 = i2 - 2;
                    Object obj2 = str2.charAt(i4) == '/' ? 1 : null;
                    if (obj != null) {
                        i3 = 2;
                    }
                    i += i3;
                    if (obj2 == null) {
                        i4 = i2 - 1;
                    }
                    String substring = str2.substring(i, i4);
                    String tagName = getTagName(substring);
                    if (tagName != null) {
                        if (isSupportedTag(tagName)) {
                            if (obj != null) {
                                while (!stack.isEmpty()) {
                                    StartTag startTag = (StartTag) stack.pop();
                                    applySpansForTag(str, startTag, spannableStringBuilder, list, arrayList);
                                    if (startTag.name.equals(tagName)) {
                                        break;
                                    }
                                }
                            } else if (obj2 == null) {
                                stack.push(StartTag.buildStartTag(substring, spannableStringBuilder.length()));
                            }
                        }
                    }
                }
                i = i2;
            }
        }
        while (stack.isEmpty() == null) {
            applySpansForTag(str, (StartTag) stack.pop(), spannableStringBuilder, list, arrayList);
        }
        applySpansForTag(str, StartTag.buildWholeCueVirtualTag(), spannableStringBuilder, list, arrayList);
        builder.setText(spannableStringBuilder);
    }

    private static boolean parseCue(java.lang.String r5, java.util.regex.Matcher r6, com.google.android.exoplayer2.util.ParsableByteArray r7, com.google.android.exoplayer2.text.webvtt.WebvttCue.Builder r8, java.lang.StringBuilder r9, java.util.List<com.google.android.exoplayer2.text.webvtt.WebvttCssStyle> r10) {
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
        r1 = 1;
        r2 = r6.group(r1);	 Catch:{ NumberFormatException -> 0x004a }
        r2 = com.google.android.exoplayer2.text.webvtt.WebvttParserUtil.parseTimestampUs(r2);	 Catch:{ NumberFormatException -> 0x004a }
        r2 = r8.setStartTime(r2);	 Catch:{ NumberFormatException -> 0x004a }
        r3 = 2;	 Catch:{ NumberFormatException -> 0x004a }
        r3 = r6.group(r3);	 Catch:{ NumberFormatException -> 0x004a }
        r3 = com.google.android.exoplayer2.text.webvtt.WebvttParserUtil.parseTimestampUs(r3);	 Catch:{ NumberFormatException -> 0x004a }
        r2.setEndTime(r3);	 Catch:{ NumberFormatException -> 0x004a }
        r2 = 3;
        r6 = r6.group(r2);
        parseCueSettingsList(r6, r8);
        r9.setLength(r0);
    L_0x0025:
        r6 = r7.readLine();
        r0 = android.text.TextUtils.isEmpty(r6);
        if (r0 != 0) goto L_0x0042;
    L_0x002f:
        r0 = r9.length();
        if (r0 <= 0) goto L_0x003a;
    L_0x0035:
        r0 = "\n";
        r9.append(r0);
    L_0x003a:
        r6 = r6.trim();
        r9.append(r6);
        goto L_0x0025;
    L_0x0042:
        r6 = r9.toString();
        parseCueText(r5, r6, r8, r10);
        return r1;
    L_0x004a:
        r5 = "WebvttCueParser";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Skipping cue with bad header: ";
        r7.append(r8);
        r6 = r6.group();
        r7.append(r6);
        r6 = r7.toString();
        android.util.Log.w(r5, r6);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.parseCue(java.lang.String, java.util.regex.Matcher, com.google.android.exoplayer2.util.ParsableByteArray, com.google.android.exoplayer2.text.webvtt.WebvttCue$Builder, java.lang.StringBuilder, java.util.List):boolean");
    }

    private static void parseLineAttribute(String str, Builder builder) throws NumberFormatException {
        int indexOf = str.indexOf(44);
        if (indexOf != -1) {
            builder.setLineAnchor(parsePositionAnchor(str.substring(indexOf + 1)));
            str = str.substring(0, indexOf);
        } else {
            builder.setLineAnchor(Integer.MIN_VALUE);
        }
        if (str.endsWith("%")) {
            builder.setLine(WebvttParserUtil.parsePercentage(str)).setLineType(0);
            return;
        }
        str = Integer.parseInt(str);
        if (str < null) {
            str--;
        }
        builder.setLine((float) str).setLineType(1);
    }

    private static void parsePositionAttribute(String str, Builder builder) throws NumberFormatException {
        int indexOf = str.indexOf(44);
        if (indexOf != -1) {
            builder.setPositionAnchor(parsePositionAnchor(str.substring(indexOf + 1)));
            str = str.substring(0, indexOf);
        } else {
            builder.setPositionAnchor(Integer.MIN_VALUE);
        }
        builder.setPosition(WebvttParserUtil.parsePercentage(str));
    }

    private static int parsePositionAnchor(String str) {
        Object obj;
        String str2;
        StringBuilder stringBuilder;
        int hashCode = str.hashCode();
        if (hashCode != -1364013995) {
            if (hashCode != -1074341483) {
                if (hashCode != 100571) {
                    if (hashCode == 109757538) {
                        if (str.equals(TtmlNode.START)) {
                            obj = null;
                            switch (obj) {
                                case null:
                                    return 0;
                                case 1:
                                case 2:
                                    return 1;
                                case 3:
                                    return 2;
                                default:
                                    str2 = TAG;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Invalid anchor value: ");
                                    stringBuilder.append(str);
                                    Log.w(str2, stringBuilder.toString());
                                    return Integer.MIN_VALUE;
                            }
                        }
                    }
                } else if (str.equals(TtmlNode.END)) {
                    obj = 3;
                    switch (obj) {
                        case null:
                            return 0;
                        case 1:
                        case 2:
                            return 1;
                        case 3:
                            return 2;
                        default:
                            str2 = TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid anchor value: ");
                            stringBuilder.append(str);
                            Log.w(str2, stringBuilder.toString());
                            return Integer.MIN_VALUE;
                    }
                }
            } else if (str.equals("middle")) {
                obj = 2;
                switch (obj) {
                    case null:
                        return 0;
                    case 1:
                    case 2:
                        return 1;
                    case 3:
                        return 2;
                    default:
                        str2 = TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid anchor value: ");
                        stringBuilder.append(str);
                        Log.w(str2, stringBuilder.toString());
                        return Integer.MIN_VALUE;
                }
            }
        } else if (str.equals(TtmlNode.CENTER)) {
            obj = 1;
            switch (obj) {
                case null:
                    return 0;
                case 1:
                case 2:
                    return 1;
                case 3:
                    return 2;
                default:
                    str2 = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid anchor value: ");
                    stringBuilder.append(str);
                    Log.w(str2, stringBuilder.toString());
                    return Integer.MIN_VALUE;
            }
        }
        obj = -1;
        switch (obj) {
            case null:
                return 0;
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                str2 = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid anchor value: ");
                stringBuilder.append(str);
                Log.w(str2, stringBuilder.toString());
                return Integer.MIN_VALUE;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.text.Layout.Alignment parseTextAlignment(java.lang.String r3) {
        /*
        r0 = r3.hashCode();
        switch(r0) {
            case -1364013995: goto L_0x003a;
            case -1074341483: goto L_0x0030;
            case 100571: goto L_0x0026;
            case 3317767: goto L_0x001c;
            case 108511772: goto L_0x0012;
            case 109757538: goto L_0x0008;
            default: goto L_0x0007;
        };
    L_0x0007:
        goto L_0x0044;
    L_0x0008:
        r0 = "start";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0044;
    L_0x0010:
        r0 = 0;
        goto L_0x0045;
    L_0x0012:
        r0 = "right";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0044;
    L_0x001a:
        r0 = 5;
        goto L_0x0045;
    L_0x001c:
        r0 = "left";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0044;
    L_0x0024:
        r0 = 1;
        goto L_0x0045;
    L_0x0026:
        r0 = "end";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0044;
    L_0x002e:
        r0 = 4;
        goto L_0x0045;
    L_0x0030:
        r0 = "middle";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0044;
    L_0x0038:
        r0 = 3;
        goto L_0x0045;
    L_0x003a:
        r0 = "center";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0044;
    L_0x0042:
        r0 = 2;
        goto L_0x0045;
    L_0x0044:
        r0 = -1;
    L_0x0045:
        switch(r0) {
            case 0: goto L_0x0066;
            case 1: goto L_0x0066;
            case 2: goto L_0x0063;
            case 3: goto L_0x0063;
            case 4: goto L_0x0060;
            case 5: goto L_0x0060;
            default: goto L_0x0048;
        };
    L_0x0048:
        r0 = "WebvttCueParser";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid alignment value: ";
        r1.append(r2);
        r1.append(r3);
        r3 = r1.toString();
        android.util.Log.w(r0, r3);
        r3 = 0;
        return r3;
    L_0x0060:
        r3 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        return r3;
    L_0x0063:
        r3 = android.text.Layout.Alignment.ALIGN_CENTER;
        return r3;
    L_0x0066:
        r3 = android.text.Layout.Alignment.ALIGN_NORMAL;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.parseTextAlignment(java.lang.String):android.text.Layout$Alignment");
    }

    private static int findEndOfTag(String str, int i) {
        i = str.indexOf(62, i);
        return i == -1 ? str.length() : i + 1;
    }

    private static void applyEntity(String str, SpannableStringBuilder spannableStringBuilder) {
        Object obj;
        StringBuilder stringBuilder;
        int hashCode = str.hashCode();
        if (hashCode != 3309) {
            if (hashCode != 3464) {
                if (hashCode != 96708) {
                    if (hashCode == 3374865) {
                        if (str.equals(ENTITY_NON_BREAK_SPACE)) {
                            obj = 2;
                            switch (obj) {
                                case null:
                                    spannableStringBuilder.append('<');
                                    return;
                                case 1:
                                    spannableStringBuilder.append('>');
                                    return;
                                case 2:
                                    spannableStringBuilder.append(CHAR_SPACE);
                                    return;
                                case 3:
                                    spannableStringBuilder.append('&');
                                    return;
                                default:
                                    spannableStringBuilder = TAG;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("ignoring unsupported entity: '&");
                                    stringBuilder.append(str);
                                    stringBuilder.append(";'");
                                    Log.w(spannableStringBuilder, stringBuilder.toString());
                                    return;
                            }
                        }
                    }
                } else if (str.equals(ENTITY_AMPERSAND)) {
                    obj = 3;
                    switch (obj) {
                        case null:
                            spannableStringBuilder.append('<');
                            return;
                        case 1:
                            spannableStringBuilder.append('>');
                            return;
                        case 2:
                            spannableStringBuilder.append(CHAR_SPACE);
                            return;
                        case 3:
                            spannableStringBuilder.append('&');
                            return;
                        default:
                            spannableStringBuilder = TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("ignoring unsupported entity: '&");
                            stringBuilder.append(str);
                            stringBuilder.append(";'");
                            Log.w(spannableStringBuilder, stringBuilder.toString());
                            return;
                    }
                }
            } else if (str.equals(ENTITY_LESS_THAN)) {
                obj = null;
                switch (obj) {
                    case null:
                        spannableStringBuilder.append('<');
                        return;
                    case 1:
                        spannableStringBuilder.append('>');
                        return;
                    case 2:
                        spannableStringBuilder.append(CHAR_SPACE);
                        return;
                    case 3:
                        spannableStringBuilder.append('&');
                        return;
                    default:
                        spannableStringBuilder = TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("ignoring unsupported entity: '&");
                        stringBuilder.append(str);
                        stringBuilder.append(";'");
                        Log.w(spannableStringBuilder, stringBuilder.toString());
                        return;
                }
            }
        } else if (str.equals(ENTITY_GREATER_THAN)) {
            obj = 1;
            switch (obj) {
                case null:
                    spannableStringBuilder.append('<');
                    return;
                case 1:
                    spannableStringBuilder.append('>');
                    return;
                case 2:
                    spannableStringBuilder.append(CHAR_SPACE);
                    return;
                case 3:
                    spannableStringBuilder.append('&');
                    return;
                default:
                    spannableStringBuilder = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ignoring unsupported entity: '&");
                    stringBuilder.append(str);
                    stringBuilder.append(";'");
                    Log.w(spannableStringBuilder, stringBuilder.toString());
                    return;
            }
        }
        obj = -1;
        switch (obj) {
            case null:
                spannableStringBuilder.append('<');
                return;
            case 1:
                spannableStringBuilder.append('>');
                return;
            case 2:
                spannableStringBuilder.append(CHAR_SPACE);
                return;
            case 3:
                spannableStringBuilder.append('&');
                return;
            default:
                spannableStringBuilder = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("ignoring unsupported entity: '&");
                stringBuilder.append(str);
                stringBuilder.append(";'");
                Log.w(spannableStringBuilder, stringBuilder.toString());
                return;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isSupportedTag(java.lang.String r3) {
        /*
        r0 = r3.hashCode();
        r1 = 0;
        r2 = 1;
        switch(r0) {
            case 98: goto L_0x003c;
            case 99: goto L_0x0032;
            case 105: goto L_0x0028;
            case 117: goto L_0x001e;
            case 118: goto L_0x0014;
            case 3314158: goto L_0x000a;
            default: goto L_0x0009;
        };
    L_0x0009:
        goto L_0x0046;
    L_0x000a:
        r0 = "lang";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0012:
        r3 = 3;
        goto L_0x0047;
    L_0x0014:
        r0 = "v";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x001c:
        r3 = 5;
        goto L_0x0047;
    L_0x001e:
        r0 = "u";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0026:
        r3 = 4;
        goto L_0x0047;
    L_0x0028:
        r0 = "i";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0030:
        r3 = 2;
        goto L_0x0047;
    L_0x0032:
        r0 = "c";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x003a:
        r3 = 1;
        goto L_0x0047;
    L_0x003c:
        r0 = "b";
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0046;
    L_0x0044:
        r3 = 0;
        goto L_0x0047;
    L_0x0046:
        r3 = -1;
    L_0x0047:
        switch(r3) {
            case 0: goto L_0x004b;
            case 1: goto L_0x004b;
            case 2: goto L_0x004b;
            case 3: goto L_0x004b;
            case 4: goto L_0x004b;
            case 5: goto L_0x004b;
            default: goto L_0x004a;
        };
    L_0x004a:
        return r1;
    L_0x004b:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.isSupportedTag(java.lang.String):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void applySpansForTag(java.lang.String r7, com.google.android.exoplayer2.text.webvtt.WebvttCueParser.StartTag r8, android.text.SpannableStringBuilder r9, java.util.List<com.google.android.exoplayer2.text.webvtt.WebvttCssStyle> r10, java.util.List<com.google.android.exoplayer2.text.webvtt.WebvttCueParser.StyleMatch> r11) {
        /*
        r0 = r8.position;
        r1 = r9.length();
        r2 = r8.name;
        r3 = r2.hashCode();
        r4 = 0;
        r5 = 2;
        r6 = 1;
        switch(r3) {
            case 0: goto L_0x004f;
            case 98: goto L_0x0045;
            case 99: goto L_0x003b;
            case 105: goto L_0x0031;
            case 117: goto L_0x0027;
            case 118: goto L_0x001d;
            case 3314158: goto L_0x0013;
            default: goto L_0x0012;
        };
    L_0x0012:
        goto L_0x0059;
    L_0x0013:
        r3 = "lang";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0059;
    L_0x001b:
        r2 = 4;
        goto L_0x005a;
    L_0x001d:
        r3 = "v";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0059;
    L_0x0025:
        r2 = 5;
        goto L_0x005a;
    L_0x0027:
        r3 = "u";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0059;
    L_0x002f:
        r2 = 2;
        goto L_0x005a;
    L_0x0031:
        r3 = "i";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0059;
    L_0x0039:
        r2 = 1;
        goto L_0x005a;
    L_0x003b:
        r3 = "c";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0059;
    L_0x0043:
        r2 = 3;
        goto L_0x005a;
    L_0x0045:
        r3 = "b";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0059;
    L_0x004d:
        r2 = 0;
        goto L_0x005a;
    L_0x004f:
        r3 = "";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0059;
    L_0x0057:
        r2 = 6;
        goto L_0x005a;
    L_0x0059:
        r2 = -1;
    L_0x005a:
        r3 = 33;
        switch(r2) {
            case 0: goto L_0x0072;
            case 1: goto L_0x0069;
            case 2: goto L_0x0060;
            case 3: goto L_0x007a;
            case 4: goto L_0x007a;
            case 5: goto L_0x007a;
            case 6: goto L_0x007a;
            default: goto L_0x005f;
        };
    L_0x005f:
        return;
    L_0x0060:
        r2 = new android.text.style.UnderlineSpan;
        r2.<init>();
        r9.setSpan(r2, r0, r1, r3);
        goto L_0x007a;
    L_0x0069:
        r2 = new android.text.style.StyleSpan;
        r2.<init>(r5);
        r9.setSpan(r2, r0, r1, r3);
        goto L_0x007a;
    L_0x0072:
        r2 = new android.text.style.StyleSpan;
        r2.<init>(r6);
        r9.setSpan(r2, r0, r1, r3);
    L_0x007a:
        r11.clear();
        getApplicableStyles(r10, r7, r8, r11);
        r7 = r11.size();
    L_0x0084:
        if (r4 >= r7) goto L_0x0094;
    L_0x0086:
        r8 = r11.get(r4);
        r8 = (com.google.android.exoplayer2.text.webvtt.WebvttCueParser.StyleMatch) r8;
        r8 = r8.style;
        applyStyleToText(r9, r8, r0, r1);
        r4 = r4 + 1;
        goto L_0x0084;
    L_0x0094:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.webvtt.WebvttCueParser.applySpansForTag(java.lang.String, com.google.android.exoplayer2.text.webvtt.WebvttCueParser$StartTag, android.text.SpannableStringBuilder, java.util.List, java.util.List):void");
    }

    private static void applyStyleToText(SpannableStringBuilder spannableStringBuilder, WebvttCssStyle webvttCssStyle, int i, int i2) {
        if (webvttCssStyle != null) {
            if (webvttCssStyle.getStyle() != -1) {
                spannableStringBuilder.setSpan(new StyleSpan(webvttCssStyle.getStyle()), i, i2, 33);
            }
            if (webvttCssStyle.isLinethrough()) {
                spannableStringBuilder.setSpan(new StrikethroughSpan(), i, i2, 33);
            }
            if (webvttCssStyle.isUnderline()) {
                spannableStringBuilder.setSpan(new UnderlineSpan(), i, i2, 33);
            }
            if (webvttCssStyle.hasFontColor()) {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(webvttCssStyle.getFontColor()), i, i2, 33);
            }
            if (webvttCssStyle.hasBackgroundColor()) {
                spannableStringBuilder.setSpan(new BackgroundColorSpan(webvttCssStyle.getBackgroundColor()), i, i2, 33);
            }
            if (webvttCssStyle.getFontFamily() != null) {
                spannableStringBuilder.setSpan(new TypefaceSpan(webvttCssStyle.getFontFamily()), i, i2, 33);
            }
            if (webvttCssStyle.getTextAlign() != null) {
                spannableStringBuilder.setSpan(new Standard(webvttCssStyle.getTextAlign()), i, i2, 33);
            }
            switch (webvttCssStyle.getFontSizeUnit()) {
                case 1:
                    spannableStringBuilder.setSpan(new AbsoluteSizeSpan((int) webvttCssStyle.getFontSize(), true), i, i2, 33);
                    break;
                case 2:
                    spannableStringBuilder.setSpan(new RelativeSizeSpan(webvttCssStyle.getFontSize()), i, i2, 33);
                    break;
                case 3:
                    spannableStringBuilder.setSpan(new RelativeSizeSpan(webvttCssStyle.getFontSize() / 100.0f), i, i2, 33);
                    break;
                default:
                    break;
            }
        }
    }

    private static String getTagName(String str) {
        str = str.trim();
        if (str.isEmpty()) {
            return null;
        }
        return str.split("[ \\.]")[0];
    }

    private static void getApplicableStyles(List<WebvttCssStyle> list, String str, StartTag startTag, List<StyleMatch> list2) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            WebvttCssStyle webvttCssStyle = (WebvttCssStyle) list.get(i);
            int specificityScore = webvttCssStyle.getSpecificityScore(str, startTag.name, startTag.classes, startTag.voice);
            if (specificityScore > 0) {
                list2.add(new StyleMatch(specificityScore, webvttCssStyle));
            }
        }
        Collections.sort(list2);
    }
}
