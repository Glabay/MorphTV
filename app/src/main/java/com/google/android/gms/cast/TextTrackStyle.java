package com.google.android.gms.cast;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.internal.cast.zzcu;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "TextTrackStyleCreator")
@Reserved({1})
public final class TextTrackStyle extends AbstractSafeParcelable {
    public static final int COLOR_UNSPECIFIED = 0;
    public static final Creator<TextTrackStyle> CREATOR = new zzbr();
    public static final float DEFAULT_FONT_SCALE = 1.0f;
    public static final int EDGE_TYPE_DEPRESSED = 4;
    public static final int EDGE_TYPE_DROP_SHADOW = 2;
    public static final int EDGE_TYPE_NONE = 0;
    public static final int EDGE_TYPE_OUTLINE = 1;
    public static final int EDGE_TYPE_RAISED = 3;
    public static final int EDGE_TYPE_UNSPECIFIED = -1;
    public static final int FONT_FAMILY_CASUAL = 4;
    public static final int FONT_FAMILY_CURSIVE = 5;
    public static final int FONT_FAMILY_MONOSPACED_SANS_SERIF = 1;
    public static final int FONT_FAMILY_MONOSPACED_SERIF = 3;
    public static final int FONT_FAMILY_SANS_SERIF = 0;
    public static final int FONT_FAMILY_SERIF = 2;
    public static final int FONT_FAMILY_SMALL_CAPITALS = 6;
    public static final int FONT_FAMILY_UNSPECIFIED = -1;
    public static final int FONT_STYLE_BOLD = 1;
    public static final int FONT_STYLE_BOLD_ITALIC = 3;
    public static final int FONT_STYLE_ITALIC = 2;
    public static final int FONT_STYLE_NORMAL = 0;
    public static final int FONT_STYLE_UNSPECIFIED = -1;
    public static final int WINDOW_TYPE_NONE = 0;
    public static final int WINDOW_TYPE_NORMAL = 1;
    public static final int WINDOW_TYPE_ROUNDED = 2;
    public static final int WINDOW_TYPE_UNSPECIFIED = -1;
    @Field(getter = "getBackgroundColor", id = 4)
    private int backgroundColor;
    @Field(getter = "getEdgeColor", id = 6)
    private int edgeColor;
    @Field(getter = "getEdgeType", id = 5)
    private int edgeType;
    @Field(getter = "getFontScale", id = 2)
    private float fontScale;
    @Field(getter = "getFontStyle", id = 12)
    private int fontStyle;
    @Field(getter = "getForegroundColor", id = 3)
    private int foregroundColor;
    @Field(getter = "getWindowColor", id = 8)
    private int windowColor;
    @Field(getter = "getWindowType", id = 7)
    private int zzgh;
    @Field(getter = "getWindowCornerRadius", id = 9)
    private int zzgi;
    @Field(getter = "getFontFamily", id = 10)
    private String zzgj;
    @Field(getter = "getFontGenericFamily", id = 11)
    private int zzgk;
    @Field(id = 13)
    private String zzj;
    private JSONObject zzp;

    public TextTrackStyle() {
        this(1.0f, 0, 0, -1, 0, -1, 0, 0, null, -1, -1, null);
    }

    @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
    TextTrackStyle(@com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 2) float r1, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 3) int r2, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 4) int r3, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 5) int r4, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 6) int r5, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 7) int r6, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 8) int r7, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 9) int r8, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 10) java.lang.String r9, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 11) int r10, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 12) int r11, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 13) java.lang.String r12) {
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
        r0 = this;
        r0.<init>();
        r0.fontScale = r1;
        r0.foregroundColor = r2;
        r0.backgroundColor = r3;
        r0.edgeType = r4;
        r0.edgeColor = r5;
        r0.zzgh = r6;
        r0.windowColor = r7;
        r0.zzgi = r8;
        r0.zzgj = r9;
        r0.zzgk = r10;
        r0.fontStyle = r11;
        r0.zzj = r12;
        r1 = r0.zzj;
        r2 = 0;
        if (r1 == 0) goto L_0x002f;
    L_0x0020:
        r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x002a }
        r3 = r0.zzj;	 Catch:{ JSONException -> 0x002a }
        r1.<init>(r3);	 Catch:{ JSONException -> 0x002a }
        r0.zzp = r1;	 Catch:{ JSONException -> 0x002a }
        return;
    L_0x002a:
        r0.zzp = r2;
        r0.zzj = r2;
        return;
    L_0x002f:
        r0.zzp = r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.TextTrackStyle.<init>(float, int, int, int, int, int, int, int, java.lang.String, int, int, java.lang.String):void");
    }

    @TargetApi(19)
    public static TextTrackStyle fromSystemSettings(Context context) {
        TextTrackStyle textTrackStyle = new TextTrackStyle();
        if (!PlatformVersion.isAtLeastKitKat()) {
            return textTrackStyle;
        }
        CaptioningManager captioningManager = (CaptioningManager) context.getSystemService("captioning");
        textTrackStyle.setFontScale(captioningManager.getFontScale());
        CaptionStyle userStyle = captioningManager.getUserStyle();
        textTrackStyle.setBackgroundColor(userStyle.backgroundColor);
        textTrackStyle.setForegroundColor(userStyle.foregroundColor);
        switch (userStyle.edgeType) {
            case 1:
                textTrackStyle.setEdgeType(1);
                break;
            case 2:
                textTrackStyle.setEdgeType(2);
                break;
            default:
                textTrackStyle.setEdgeType(0);
                break;
        }
        textTrackStyle.setEdgeColor(userStyle.edgeColor);
        Typeface typeface = userStyle.getTypeface();
        if (typeface != null) {
            if (Typeface.MONOSPACE.equals(typeface)) {
                textTrackStyle.setFontGenericFamily(1);
            } else if (Typeface.SANS_SERIF.equals(typeface) || !Typeface.SERIF.equals(typeface)) {
                textTrackStyle.setFontGenericFamily(0);
            } else {
                textTrackStyle.setFontGenericFamily(2);
            }
            boolean isBold = typeface.isBold();
            boolean isItalic = typeface.isItalic();
            if (isBold && isItalic) {
                textTrackStyle.setFontStyle(3);
                return textTrackStyle;
            } else if (isBold) {
                textTrackStyle.setFontStyle(1);
                return textTrackStyle;
            } else if (isItalic) {
                textTrackStyle.setFontStyle(2);
                return textTrackStyle;
            } else {
                textTrackStyle.setFontStyle(0);
            }
        }
        return textTrackStyle;
    }

    private static String zzd(int i) {
        return String.format("#%02X%02X%02X%02X", new Object[]{Integer.valueOf(Color.red(i)), Integer.valueOf(Color.green(i)), Integer.valueOf(Color.blue(i)), Integer.valueOf(Color.alpha(i))});
    }

    private static int zzh(java.lang.String r7) {
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
        if (r7 == 0) goto L_0x003e;
    L_0x0003:
        r1 = r7.length();
        r2 = 9;
        if (r1 != r2) goto L_0x003e;
    L_0x000b:
        r1 = r7.charAt(r0);
        r3 = 35;
        if (r1 != r3) goto L_0x003e;
    L_0x0013:
        r1 = 1;
        r3 = 3;
        r1 = r7.substring(r1, r3);	 Catch:{ NumberFormatException -> 0x003e }
        r4 = 16;	 Catch:{ NumberFormatException -> 0x003e }
        r1 = java.lang.Integer.parseInt(r1, r4);	 Catch:{ NumberFormatException -> 0x003e }
        r5 = 5;	 Catch:{ NumberFormatException -> 0x003e }
        r3 = r7.substring(r3, r5);	 Catch:{ NumberFormatException -> 0x003e }
        r3 = java.lang.Integer.parseInt(r3, r4);	 Catch:{ NumberFormatException -> 0x003e }
        r6 = 7;	 Catch:{ NumberFormatException -> 0x003e }
        r5 = r7.substring(r5, r6);	 Catch:{ NumberFormatException -> 0x003e }
        r5 = java.lang.Integer.parseInt(r5, r4);	 Catch:{ NumberFormatException -> 0x003e }
        r7 = r7.substring(r6, r2);	 Catch:{ NumberFormatException -> 0x003e }
        r7 = java.lang.Integer.parseInt(r7, r4);	 Catch:{ NumberFormatException -> 0x003e }
        r7 = android.graphics.Color.argb(r7, r1, r3, r5);	 Catch:{ NumberFormatException -> 0x003e }
        return r7;
    L_0x003e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.TextTrackStyle.zzh(java.lang.String):int");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TextTrackStyle)) {
            return false;
        }
        TextTrackStyle textTrackStyle = (TextTrackStyle) obj;
        return (this.zzp == null ? 1 : null) != (textTrackStyle.zzp == null ? 1 : null) ? false : (this.zzp == null || textTrackStyle.zzp == null || JsonUtils.areJsonValuesEquivalent(this.zzp, textTrackStyle.zzp)) && this.fontScale == textTrackStyle.fontScale && this.foregroundColor == textTrackStyle.foregroundColor && this.backgroundColor == textTrackStyle.backgroundColor && this.edgeType == textTrackStyle.edgeType && this.edgeColor == textTrackStyle.edgeColor && this.zzgh == textTrackStyle.zzgh && this.zzgi == textTrackStyle.zzgi && zzcu.zza(this.zzgj, textTrackStyle.zzgj) && this.zzgk == textTrackStyle.zzgk && this.fontStyle == textTrackStyle.fontStyle;
    }

    public final int getBackgroundColor() {
        return this.backgroundColor;
    }

    public final JSONObject getCustomData() {
        return this.zzp;
    }

    public final int getEdgeColor() {
        return this.edgeColor;
    }

    public final int getEdgeType() {
        return this.edgeType;
    }

    public final String getFontFamily() {
        return this.zzgj;
    }

    public final int getFontGenericFamily() {
        return this.zzgk;
    }

    public final float getFontScale() {
        return this.fontScale;
    }

    public final int getFontStyle() {
        return this.fontStyle;
    }

    public final int getForegroundColor() {
        return this.foregroundColor;
    }

    public final int getWindowColor() {
        return this.windowColor;
    }

    public final int getWindowCornerRadius() {
        return this.zzgi;
    }

    public final int getWindowType() {
        return this.zzgh;
    }

    public final int hashCode() {
        return Objects.hashCode(Float.valueOf(this.fontScale), Integer.valueOf(this.foregroundColor), Integer.valueOf(this.backgroundColor), Integer.valueOf(this.edgeType), Integer.valueOf(this.edgeColor), Integer.valueOf(this.zzgh), Integer.valueOf(this.windowColor), Integer.valueOf(this.zzgi), this.zzgj, Integer.valueOf(this.zzgk), Integer.valueOf(this.fontStyle), String.valueOf(this.zzp));
    }

    public final void setBackgroundColor(int i) {
        this.backgroundColor = i;
    }

    public final void setCustomData(JSONObject jSONObject) {
        this.zzp = jSONObject;
    }

    public final void setEdgeColor(int i) {
        this.edgeColor = i;
    }

    public final void setEdgeType(int i) {
        if (i >= 0) {
            if (i <= 4) {
                this.edgeType = i;
                return;
            }
        }
        throw new IllegalArgumentException("invalid edgeType");
    }

    public final void setFontFamily(String str) {
        this.zzgj = str;
    }

    public final void setFontGenericFamily(int i) {
        if (i >= 0) {
            if (i <= 6) {
                this.zzgk = i;
                return;
            }
        }
        throw new IllegalArgumentException("invalid fontGenericFamily");
    }

    public final void setFontScale(float f) {
        this.fontScale = f;
    }

    public final void setFontStyle(int i) {
        if (i >= 0) {
            if (i <= 3) {
                this.fontStyle = i;
                return;
            }
        }
        throw new IllegalArgumentException("invalid fontStyle");
    }

    public final void setForegroundColor(int i) {
        this.foregroundColor = i;
    }

    public final void setWindowColor(int i) {
        this.windowColor = i;
    }

    public final void setWindowCornerRadius(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("invalid windowCornerRadius");
        }
        this.zzgi = i;
    }

    public final void setWindowType(int i) {
        if (i >= 0) {
            if (i <= 2) {
                this.zzgh = i;
                return;
            }
        }
        throw new IllegalArgumentException("invalid windowType");
    }

    public final org.json.JSONObject toJson() {
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
        r4 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "fontScale";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.fontScale;	 Catch:{ JSONException -> 0x00ee }
        r2 = (double) r2;	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
        r1 = r4.foregroundColor;	 Catch:{ JSONException -> 0x00ee }
        if (r1 == 0) goto L_0x001c;	 Catch:{ JSONException -> 0x00ee }
    L_0x0011:
        r1 = "foregroundColor";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.foregroundColor;	 Catch:{ JSONException -> 0x00ee }
        r2 = zzd(r2);	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
    L_0x001c:
        r1 = r4.backgroundColor;	 Catch:{ JSONException -> 0x00ee }
        if (r1 == 0) goto L_0x002b;	 Catch:{ JSONException -> 0x00ee }
    L_0x0020:
        r1 = "backgroundColor";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.backgroundColor;	 Catch:{ JSONException -> 0x00ee }
        r2 = zzd(r2);	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
    L_0x002b:
        r1 = r4.edgeType;	 Catch:{ JSONException -> 0x00ee }
        switch(r1) {
            case 0: goto L_0x0048;
            case 1: goto L_0x0043;
            case 2: goto L_0x003e;
            case 3: goto L_0x0039;
            case 4: goto L_0x0031;
            default: goto L_0x0030;
        };	 Catch:{ JSONException -> 0x00ee }
    L_0x0030:
        goto L_0x004d;	 Catch:{ JSONException -> 0x00ee }
    L_0x0031:
        r1 = "edgeType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "DEPRESSED";	 Catch:{ JSONException -> 0x00ee }
    L_0x0035:
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
        goto L_0x004d;	 Catch:{ JSONException -> 0x00ee }
    L_0x0039:
        r1 = "edgeType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "RAISED";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x0035;	 Catch:{ JSONException -> 0x00ee }
    L_0x003e:
        r1 = "edgeType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "DROP_SHADOW";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x0035;	 Catch:{ JSONException -> 0x00ee }
    L_0x0043:
        r1 = "edgeType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "OUTLINE";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x0035;	 Catch:{ JSONException -> 0x00ee }
    L_0x0048:
        r1 = "edgeType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "NONE";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x0035;	 Catch:{ JSONException -> 0x00ee }
    L_0x004d:
        r1 = r4.edgeColor;	 Catch:{ JSONException -> 0x00ee }
        if (r1 == 0) goto L_0x005c;	 Catch:{ JSONException -> 0x00ee }
    L_0x0051:
        r1 = "edgeColor";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.edgeColor;	 Catch:{ JSONException -> 0x00ee }
        r2 = zzd(r2);	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
    L_0x005c:
        r1 = r4.zzgh;	 Catch:{ JSONException -> 0x00ee }
        switch(r1) {
            case 0: goto L_0x006f;
            case 1: goto L_0x006a;
            case 2: goto L_0x0062;
            default: goto L_0x0061;
        };	 Catch:{ JSONException -> 0x00ee }
    L_0x0061:
        goto L_0x0074;	 Catch:{ JSONException -> 0x00ee }
    L_0x0062:
        r1 = "windowType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "ROUNDED_CORNERS";	 Catch:{ JSONException -> 0x00ee }
    L_0x0066:
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
        goto L_0x0074;	 Catch:{ JSONException -> 0x00ee }
    L_0x006a:
        r1 = "windowType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "NORMAL";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x0066;	 Catch:{ JSONException -> 0x00ee }
    L_0x006f:
        r1 = "windowType";	 Catch:{ JSONException -> 0x00ee }
        r2 = "NONE";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x0066;	 Catch:{ JSONException -> 0x00ee }
    L_0x0074:
        r1 = r4.windowColor;	 Catch:{ JSONException -> 0x00ee }
        if (r1 == 0) goto L_0x0083;	 Catch:{ JSONException -> 0x00ee }
    L_0x0078:
        r1 = "windowColor";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.windowColor;	 Catch:{ JSONException -> 0x00ee }
        r2 = zzd(r2);	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
    L_0x0083:
        r1 = r4.zzgh;	 Catch:{ JSONException -> 0x00ee }
        r2 = 2;	 Catch:{ JSONException -> 0x00ee }
        if (r1 != r2) goto L_0x008f;	 Catch:{ JSONException -> 0x00ee }
    L_0x0088:
        r1 = "windowRoundedCornerRadius";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.zzgi;	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
    L_0x008f:
        r1 = r4.zzgj;	 Catch:{ JSONException -> 0x00ee }
        if (r1 == 0) goto L_0x009a;	 Catch:{ JSONException -> 0x00ee }
    L_0x0093:
        r1 = "fontFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.zzgj;	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
    L_0x009a:
        r1 = r4.zzgk;	 Catch:{ JSONException -> 0x00ee }
        switch(r1) {
            case 0: goto L_0x00c1;
            case 1: goto L_0x00bc;
            case 2: goto L_0x00b7;
            case 3: goto L_0x00b2;
            case 4: goto L_0x00ad;
            case 5: goto L_0x00a8;
            case 6: goto L_0x00a0;
            default: goto L_0x009f;
        };	 Catch:{ JSONException -> 0x00ee }
    L_0x009f:
        goto L_0x00c6;	 Catch:{ JSONException -> 0x00ee }
    L_0x00a0:
        r1 = "fontGenericFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = "SMALL_CAPITALS";	 Catch:{ JSONException -> 0x00ee }
    L_0x00a4:
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00c6;	 Catch:{ JSONException -> 0x00ee }
    L_0x00a8:
        r1 = "fontGenericFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = "CURSIVE";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00a4;	 Catch:{ JSONException -> 0x00ee }
    L_0x00ad:
        r1 = "fontGenericFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = "CASUAL";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00a4;	 Catch:{ JSONException -> 0x00ee }
    L_0x00b2:
        r1 = "fontGenericFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = "MONOSPACED_SERIF";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00a4;	 Catch:{ JSONException -> 0x00ee }
    L_0x00b7:
        r1 = "fontGenericFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = "SERIF";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00a4;	 Catch:{ JSONException -> 0x00ee }
    L_0x00bc:
        r1 = "fontGenericFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = "MONOSPACED_SANS_SERIF";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00a4;	 Catch:{ JSONException -> 0x00ee }
    L_0x00c1:
        r1 = "fontGenericFamily";	 Catch:{ JSONException -> 0x00ee }
        r2 = "SANS_SERIF";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00a4;	 Catch:{ JSONException -> 0x00ee }
    L_0x00c6:
        r1 = r4.fontStyle;	 Catch:{ JSONException -> 0x00ee }
        switch(r1) {
            case 0: goto L_0x00de;
            case 1: goto L_0x00d9;
            case 2: goto L_0x00d4;
            case 3: goto L_0x00cc;
            default: goto L_0x00cb;
        };	 Catch:{ JSONException -> 0x00ee }
    L_0x00cb:
        goto L_0x00e3;	 Catch:{ JSONException -> 0x00ee }
    L_0x00cc:
        r1 = "fontStyle";	 Catch:{ JSONException -> 0x00ee }
        r2 = "BOLD_ITALIC";	 Catch:{ JSONException -> 0x00ee }
    L_0x00d0:
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00e3;	 Catch:{ JSONException -> 0x00ee }
    L_0x00d4:
        r1 = "fontStyle";	 Catch:{ JSONException -> 0x00ee }
        r2 = "ITALIC";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00d0;	 Catch:{ JSONException -> 0x00ee }
    L_0x00d9:
        r1 = "fontStyle";	 Catch:{ JSONException -> 0x00ee }
        r2 = "BOLD";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00d0;	 Catch:{ JSONException -> 0x00ee }
    L_0x00de:
        r1 = "fontStyle";	 Catch:{ JSONException -> 0x00ee }
        r2 = "NORMAL";	 Catch:{ JSONException -> 0x00ee }
        goto L_0x00d0;	 Catch:{ JSONException -> 0x00ee }
    L_0x00e3:
        r1 = r4.zzp;	 Catch:{ JSONException -> 0x00ee }
        if (r1 == 0) goto L_0x00ee;	 Catch:{ JSONException -> 0x00ee }
    L_0x00e7:
        r1 = "customData";	 Catch:{ JSONException -> 0x00ee }
        r2 = r4.zzp;	 Catch:{ JSONException -> 0x00ee }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00ee }
    L_0x00ee:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.TextTrackStyle.toJson():org.json.JSONObject");
    }

    public final void writeToParcel(Parcel parcel, int i) {
        this.zzj = this.zzp == null ? null : this.zzp.toString();
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeFloat(parcel, 2, getFontScale());
        SafeParcelWriter.writeInt(parcel, 3, getForegroundColor());
        SafeParcelWriter.writeInt(parcel, 4, getBackgroundColor());
        SafeParcelWriter.writeInt(parcel, 5, getEdgeType());
        SafeParcelWriter.writeInt(parcel, 6, getEdgeColor());
        SafeParcelWriter.writeInt(parcel, 7, getWindowType());
        SafeParcelWriter.writeInt(parcel, 8, getWindowColor());
        SafeParcelWriter.writeInt(parcel, 9, getWindowCornerRadius());
        SafeParcelWriter.writeString(parcel, 10, getFontFamily(), false);
        SafeParcelWriter.writeInt(parcel, 11, getFontGenericFamily());
        SafeParcelWriter.writeInt(parcel, 12, getFontStyle());
        SafeParcelWriter.writeString(parcel, 13, this.zzj, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    public final void zze(JSONObject jSONObject) throws JSONException {
        String string;
        this.fontScale = (float) jSONObject.optDouble("fontScale", 1.0d);
        this.foregroundColor = zzh(jSONObject.optString("foregroundColor"));
        this.backgroundColor = zzh(jSONObject.optString(TtmlNode.ATTR_TTS_BACKGROUND_COLOR));
        if (jSONObject.has("edgeType")) {
            string = jSONObject.getString("edgeType");
            if ("NONE".equals(string)) {
                this.edgeType = 0;
            } else if ("OUTLINE".equals(string)) {
                this.edgeType = 1;
            } else if ("DROP_SHADOW".equals(string)) {
                this.edgeType = 2;
            } else if ("RAISED".equals(string)) {
                this.edgeType = 3;
            } else if ("DEPRESSED".equals(string)) {
                this.edgeType = 4;
            }
        }
        this.edgeColor = zzh(jSONObject.optString("edgeColor"));
        if (jSONObject.has("windowType")) {
            string = jSONObject.getString("windowType");
            if ("NONE".equals(string)) {
                this.zzgh = 0;
            } else if ("NORMAL".equals(string)) {
                this.zzgh = 1;
            } else if ("ROUNDED_CORNERS".equals(string)) {
                this.zzgh = 2;
            }
        }
        this.windowColor = zzh(jSONObject.optString("windowColor"));
        if (this.zzgh == 2) {
            this.zzgi = jSONObject.optInt("windowRoundedCornerRadius", 0);
        }
        this.zzgj = jSONObject.optString(TtmlNode.ATTR_TTS_FONT_FAMILY, null);
        if (jSONObject.has("fontGenericFamily")) {
            string = jSONObject.getString("fontGenericFamily");
            if ("SANS_SERIF".equals(string)) {
                this.zzgk = 0;
            } else if ("MONOSPACED_SANS_SERIF".equals(string)) {
                this.zzgk = 1;
            } else if ("SERIF".equals(string)) {
                this.zzgk = 2;
            } else if ("MONOSPACED_SERIF".equals(string)) {
                this.zzgk = 3;
            } else if ("CASUAL".equals(string)) {
                this.zzgk = 4;
            } else {
                int i;
                if ("CURSIVE".equals(string)) {
                    i = 5;
                } else if ("SMALL_CAPITALS".equals(string)) {
                    i = 6;
                }
                this.zzgk = i;
            }
        }
        if (jSONObject.has(TtmlNode.ATTR_TTS_FONT_STYLE)) {
            string = jSONObject.getString(TtmlNode.ATTR_TTS_FONT_STYLE);
            if ("NORMAL".equals(string)) {
                this.fontStyle = 0;
            } else if ("BOLD".equals(string)) {
                this.fontStyle = 1;
            } else if ("ITALIC".equals(string)) {
                this.fontStyle = 2;
            } else if ("BOLD_ITALIC".equals(string)) {
                this.fontStyle = 3;
            }
        }
        this.zzp = jSONObject.optJSONObject("customData");
    }
}
