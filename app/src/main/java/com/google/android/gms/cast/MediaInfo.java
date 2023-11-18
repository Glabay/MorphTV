package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.cast.zzcu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "MediaInfoCreator")
@Reserved({1})
public class MediaInfo extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<MediaInfo> CREATOR = new zzai();
    public static final int STREAM_TYPE_BUFFERED = 1;
    public static final int STREAM_TYPE_INVALID = -1;
    public static final int STREAM_TYPE_LIVE = 2;
    public static final int STREAM_TYPE_NONE = 0;
    public static final long UNKNOWN_DURATION = -1;
    @Field(getter = "getStreamType", id = 3)
    private int streamType;
    @Field(getter = "getContentType", id = 4)
    private String zzda;
    @Field(getter = "getMetadata", id = 5)
    private MediaMetadata zzdb;
    @Field(getter = "getStreamDuration", id = 6)
    private long zzdc;
    @Field(getter = "getMediaTracks", id = 7)
    private List<MediaTrack> zzdd;
    @Field(getter = "getTextTrackStyle", id = 8)
    private TextTrackStyle zzde;
    @Field(getter = "getAdBreaks", id = 10)
    private List<AdBreakInfo> zzdf;
    @Field(getter = "getAdBreakClips", id = 11)
    private List<AdBreakClipInfo> zzdg;
    @Field(getter = "getEntity", id = 12)
    private String zzdh;
    @Field(id = 9)
    private String zzj;
    @Field(getter = "getContentId", id = 2)
    private final String zzk;
    private JSONObject zzp;

    @VisibleForTesting
    public static class Builder {
        private final MediaInfo zzdi;

        public Builder(String str) throws IllegalArgumentException {
            this.zzdi = new MediaInfo(str);
        }

        public Builder(String str, String str2) throws IllegalArgumentException {
            this.zzdi = new MediaInfo(str, str2);
        }

        public MediaInfo build() {
            return this.zzdi;
        }

        public Builder setAdBreakClips(List<AdBreakClipInfo> list) {
            this.zzdi.zzc(list);
            return this;
        }

        public Builder setAdBreaks(List<AdBreakInfo> list) {
            this.zzdi.zzb(list);
            return this;
        }

        public Builder setContentType(String str) {
            this.zzdi.setContentType(str);
            return this;
        }

        public Builder setCustomData(JSONObject jSONObject) {
            this.zzdi.setCustomData(jSONObject);
            return this;
        }

        public Builder setEntity(String str) {
            this.zzdi.zzd(str);
            return this;
        }

        public Builder setMediaTracks(List<MediaTrack> list) {
            this.zzdi.zza((List) list);
            return this;
        }

        public Builder setMetadata(MediaMetadata mediaMetadata) {
            this.zzdi.zza(mediaMetadata);
            return this;
        }

        public Builder setStreamDuration(long j) throws IllegalArgumentException {
            this.zzdi.zza(j);
            return this;
        }

        public Builder setStreamType(int i) throws IllegalArgumentException {
            this.zzdi.setStreamType(i);
            return this;
        }

        public Builder setTextTrackStyle(TextTrackStyle textTrackStyle) {
            this.zzdi.setTextTrackStyle(textTrackStyle);
            return this;
        }
    }

    MediaInfo(String str) throws IllegalArgumentException {
        this(str, -1, null, null, -1, null, null, null, null, null, null);
        if (str == null) {
            throw new IllegalArgumentException("contentID cannot be null");
        }
    }

    @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
    MediaInfo(@com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 2) @android.support.annotation.NonNull java.lang.String r1, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 3) int r2, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 4) java.lang.String r3, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 5) com.google.android.gms.cast.MediaMetadata r4, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 6) long r5, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 7) java.util.List<com.google.android.gms.cast.MediaTrack> r7, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 8) com.google.android.gms.cast.TextTrackStyle r8, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 9) java.lang.String r9, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 10) java.util.List<com.google.android.gms.cast.AdBreakInfo> r10, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 11) java.util.List<com.google.android.gms.cast.AdBreakClipInfo> r11, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 12) java.lang.String r12) {
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
        r0.zzk = r1;
        r0.streamType = r2;
        r0.zzda = r3;
        r0.zzdb = r4;
        r0.zzdc = r5;
        r0.zzdd = r7;
        r0.zzde = r8;
        r0.zzj = r9;
        r1 = r0.zzj;
        r2 = 0;
        if (r1 == 0) goto L_0x0027;
    L_0x0018:
        r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0022 }
        r3 = r0.zzj;	 Catch:{ JSONException -> 0x0022 }
        r1.<init>(r3);	 Catch:{ JSONException -> 0x0022 }
        r0.zzp = r1;	 Catch:{ JSONException -> 0x0022 }
        goto L_0x0029;
    L_0x0022:
        r0.zzp = r2;
        r0.zzj = r2;
        goto L_0x0029;
    L_0x0027:
        r0.zzp = r2;
    L_0x0029:
        r0.zzdf = r10;
        r0.zzdg = r11;
        r0.zzdh = r12;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaInfo.<init>(java.lang.String, int, java.lang.String, com.google.android.gms.cast.MediaMetadata, long, java.util.List, com.google.android.gms.cast.TextTrackStyle, java.lang.String, java.util.List, java.util.List, java.lang.String):void");
    }

    MediaInfo(String str, String str2) throws IllegalArgumentException {
        this(str, -1, null, null, -1, null, null, null, null, null, str2);
        if (str == null) {
            throw new IllegalArgumentException("contentID cannot be null");
        }
    }

    MediaInfo(JSONObject jSONObject) throws JSONException {
        this(jSONObject.getString("contentId"), -1, null, null, -1, null, null, null, null, null, null);
        String string = jSONObject.getString("streamType");
        int i = 0;
        if ("NONE".equals(string)) {
            this.streamType = 0;
        } else {
            int i2 = "BUFFERED".equals(string) ? 1 : "LIVE".equals(string) ? 2 : -1;
            this.streamType = i2;
        }
        TextTrackStyle textTrackStyle = null;
        this.zzda = jSONObject.optString("contentType", null);
        if (jSONObject.has(TtmlNode.TAG_METADATA)) {
            JSONObject jSONObject2 = jSONObject.getJSONObject(TtmlNode.TAG_METADATA);
            this.zzdb = new MediaMetadata(jSONObject2.getInt("metadataType"));
            this.zzdb.zze(jSONObject2);
        }
        this.zzdc = -1;
        if (jSONObject.has("duration") && !jSONObject.isNull("duration")) {
            double optDouble = jSONObject.optDouble("duration", 0.0d);
            if (!(Double.isNaN(optDouble) || Double.isInfinite(optDouble))) {
                this.zzdc = (long) (optDouble * 1000.0d);
            }
        }
        if (jSONObject.has("tracks")) {
            this.zzdd = new ArrayList();
            JSONArray jSONArray = jSONObject.getJSONArray("tracks");
            while (i < jSONArray.length()) {
                this.zzdd.add(new MediaTrack(jSONArray.getJSONObject(i)));
                i++;
            }
        } else {
            this.zzdd = null;
        }
        if (jSONObject.has("textTrackStyle")) {
            jSONObject2 = jSONObject.getJSONObject("textTrackStyle");
            textTrackStyle = new TextTrackStyle();
            textTrackStyle.zze(jSONObject2);
        }
        this.zzde = textTrackStyle;
        zzd(jSONObject);
        this.zzp = jSONObject.optJSONObject("customData");
        if (jSONObject.has("entity")) {
            this.zzdh = jSONObject.getString("entity");
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaInfo)) {
            return false;
        }
        MediaInfo mediaInfo = (MediaInfo) obj;
        return (this.zzp == null ? 1 : null) != (mediaInfo.zzp == null ? 1 : null) ? false : (this.zzp == null || mediaInfo.zzp == null || JsonUtils.areJsonValuesEquivalent(this.zzp, mediaInfo.zzp)) && zzcu.zza(this.zzk, mediaInfo.zzk) && this.streamType == mediaInfo.streamType && zzcu.zza(this.zzda, mediaInfo.zzda) && zzcu.zza(this.zzdb, mediaInfo.zzdb) && this.zzdc == mediaInfo.zzdc && zzcu.zza(this.zzdd, mediaInfo.zzdd) && zzcu.zza(this.zzde, mediaInfo.zzde) && zzcu.zza(this.zzdf, mediaInfo.zzdf) && zzcu.zza(this.zzdg, mediaInfo.zzdg) && zzcu.zza(this.zzdh, mediaInfo.zzdh);
    }

    public List<AdBreakClipInfo> getAdBreakClips() {
        return this.zzdg == null ? null : Collections.unmodifiableList(this.zzdg);
    }

    public List<AdBreakInfo> getAdBreaks() {
        return this.zzdf == null ? null : Collections.unmodifiableList(this.zzdf);
    }

    public String getContentId() {
        return this.zzk;
    }

    public String getContentType() {
        return this.zzda;
    }

    public JSONObject getCustomData() {
        return this.zzp;
    }

    public String getEntity() {
        return this.zzdh;
    }

    public List<MediaTrack> getMediaTracks() {
        return this.zzdd;
    }

    public MediaMetadata getMetadata() {
        return this.zzdb;
    }

    public long getStreamDuration() {
        return this.zzdc;
    }

    public int getStreamType() {
        return this.streamType;
    }

    public TextTrackStyle getTextTrackStyle() {
        return this.zzde;
    }

    public int hashCode() {
        return Objects.hashCode(this.zzk, Integer.valueOf(this.streamType), this.zzda, this.zzdb, Long.valueOf(this.zzdc), String.valueOf(this.zzp), this.zzdd, this.zzde, this.zzdf, this.zzdg, this.zzdh);
    }

    final void setContentType(String str) {
        this.zzda = str;
    }

    final void setCustomData(JSONObject jSONObject) {
        this.zzp = jSONObject;
    }

    final void setStreamType(int i) throws IllegalArgumentException {
        if (i >= -1) {
            if (i <= 2) {
                this.streamType = i;
                return;
            }
        }
        throw new IllegalArgumentException("invalid stream type");
    }

    public void setTextTrackStyle(TextTrackStyle textTrackStyle) {
        this.zzde = textTrackStyle;
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
        r6 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "contentId";	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzk;	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
        r1 = r6.streamType;	 Catch:{ JSONException -> 0x00f3 }
        switch(r1) {
            case 1: goto L_0x0017;
            case 2: goto L_0x0014;
            default: goto L_0x0011;
        };	 Catch:{ JSONException -> 0x00f3 }
    L_0x0011:
        r1 = "NONE";	 Catch:{ JSONException -> 0x00f3 }
        goto L_0x0019;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0014:
        r1 = "LIVE";	 Catch:{ JSONException -> 0x00f3 }
        goto L_0x0019;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0017:
        r1 = "BUFFERED";	 Catch:{ JSONException -> 0x00f3 }
    L_0x0019:
        r2 = "streamType";	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x00f3 }
        r1 = r6.zzda;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x0029;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0022:
        r1 = "contentType";	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzda;	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
    L_0x0029:
        r1 = r6.zzdb;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x0038;	 Catch:{ JSONException -> 0x00f3 }
    L_0x002d:
        r1 = "metadata";	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzdb;	 Catch:{ JSONException -> 0x00f3 }
        r2 = r2.toJson();	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
    L_0x0038:
        r1 = r6.zzdc;	 Catch:{ JSONException -> 0x00f3 }
        r3 = -1;	 Catch:{ JSONException -> 0x00f3 }
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ JSONException -> 0x00f3 }
        if (r5 > 0) goto L_0x0048;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0040:
        r1 = "duration";	 Catch:{ JSONException -> 0x00f3 }
        r2 = org.json.JSONObject.NULL;	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
        goto L_0x0056;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0048:
        r1 = "duration";	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzdc;	 Catch:{ JSONException -> 0x00f3 }
        r2 = (double) r2;	 Catch:{ JSONException -> 0x00f3 }
        r4 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;	 Catch:{ JSONException -> 0x00f3 }
        r2 = r2 / r4;	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
    L_0x0056:
        r1 = r6.zzdd;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x007e;	 Catch:{ JSONException -> 0x00f3 }
    L_0x005a:
        r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x00f3 }
        r1.<init>();	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzdd;	 Catch:{ JSONException -> 0x00f3 }
        r2 = r2.iterator();	 Catch:{ JSONException -> 0x00f3 }
    L_0x0065:
        r3 = r2.hasNext();	 Catch:{ JSONException -> 0x00f3 }
        if (r3 == 0) goto L_0x0079;	 Catch:{ JSONException -> 0x00f3 }
    L_0x006b:
        r3 = r2.next();	 Catch:{ JSONException -> 0x00f3 }
        r3 = (com.google.android.gms.cast.MediaTrack) r3;	 Catch:{ JSONException -> 0x00f3 }
        r3 = r3.toJson();	 Catch:{ JSONException -> 0x00f3 }
        r1.put(r3);	 Catch:{ JSONException -> 0x00f3 }
        goto L_0x0065;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0079:
        r2 = "tracks";	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x00f3 }
    L_0x007e:
        r1 = r6.zzde;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x008d;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0082:
        r1 = "textTrackStyle";	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzde;	 Catch:{ JSONException -> 0x00f3 }
        r2 = r2.toJson();	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
    L_0x008d:
        r1 = r6.zzp;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x0098;	 Catch:{ JSONException -> 0x00f3 }
    L_0x0091:
        r1 = "customData";	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzp;	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
    L_0x0098:
        r1 = r6.zzdh;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x00a3;	 Catch:{ JSONException -> 0x00f3 }
    L_0x009c:
        r1 = "entity";	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzdh;	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x00f3 }
    L_0x00a3:
        r1 = r6.zzdf;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x00cb;	 Catch:{ JSONException -> 0x00f3 }
    L_0x00a7:
        r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x00f3 }
        r1.<init>();	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzdf;	 Catch:{ JSONException -> 0x00f3 }
        r2 = r2.iterator();	 Catch:{ JSONException -> 0x00f3 }
    L_0x00b2:
        r3 = r2.hasNext();	 Catch:{ JSONException -> 0x00f3 }
        if (r3 == 0) goto L_0x00c6;	 Catch:{ JSONException -> 0x00f3 }
    L_0x00b8:
        r3 = r2.next();	 Catch:{ JSONException -> 0x00f3 }
        r3 = (com.google.android.gms.cast.AdBreakInfo) r3;	 Catch:{ JSONException -> 0x00f3 }
        r3 = r3.toJson();	 Catch:{ JSONException -> 0x00f3 }
        r1.put(r3);	 Catch:{ JSONException -> 0x00f3 }
        goto L_0x00b2;	 Catch:{ JSONException -> 0x00f3 }
    L_0x00c6:
        r2 = "breaks";	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x00f3 }
    L_0x00cb:
        r1 = r6.zzdg;	 Catch:{ JSONException -> 0x00f3 }
        if (r1 == 0) goto L_0x00f3;	 Catch:{ JSONException -> 0x00f3 }
    L_0x00cf:
        r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x00f3 }
        r1.<init>();	 Catch:{ JSONException -> 0x00f3 }
        r2 = r6.zzdg;	 Catch:{ JSONException -> 0x00f3 }
        r2 = r2.iterator();	 Catch:{ JSONException -> 0x00f3 }
    L_0x00da:
        r3 = r2.hasNext();	 Catch:{ JSONException -> 0x00f3 }
        if (r3 == 0) goto L_0x00ee;	 Catch:{ JSONException -> 0x00f3 }
    L_0x00e0:
        r3 = r2.next();	 Catch:{ JSONException -> 0x00f3 }
        r3 = (com.google.android.gms.cast.AdBreakClipInfo) r3;	 Catch:{ JSONException -> 0x00f3 }
        r3 = r3.toJson();	 Catch:{ JSONException -> 0x00f3 }
        r1.put(r3);	 Catch:{ JSONException -> 0x00f3 }
        goto L_0x00da;	 Catch:{ JSONException -> 0x00f3 }
    L_0x00ee:
        r2 = "breakClips";	 Catch:{ JSONException -> 0x00f3 }
        r0.put(r2, r1);	 Catch:{ JSONException -> 0x00f3 }
    L_0x00f3:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaInfo.toJson():org.json.JSONObject");
    }

    public void writeToParcel(Parcel parcel, int i) {
        this.zzj = this.zzp == null ? null : this.zzp.toString();
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getContentId(), false);
        SafeParcelWriter.writeInt(parcel, 3, getStreamType());
        SafeParcelWriter.writeString(parcel, 4, getContentType(), false);
        SafeParcelWriter.writeParcelable(parcel, 5, getMetadata(), i, false);
        SafeParcelWriter.writeLong(parcel, 6, getStreamDuration());
        SafeParcelWriter.writeTypedList(parcel, 7, getMediaTracks(), false);
        SafeParcelWriter.writeParcelable(parcel, 8, getTextTrackStyle(), i, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzj, false);
        SafeParcelWriter.writeTypedList(parcel, 10, getAdBreaks(), false);
        SafeParcelWriter.writeTypedList(parcel, 11, getAdBreakClips(), false);
        SafeParcelWriter.writeString(parcel, 12, getEntity(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    final void zza(long j) throws IllegalArgumentException {
        if (j >= 0 || j == -1) {
            this.zzdc = j;
            return;
        }
        throw new IllegalArgumentException("Invalid stream duration");
    }

    final void zza(MediaMetadata mediaMetadata) {
        this.zzdb = mediaMetadata;
    }

    final void zza(List<MediaTrack> list) {
        this.zzdd = list;
    }

    @VisibleForTesting
    public final void zzb(List<AdBreakInfo> list) {
        this.zzdf = list;
    }

    @VisibleForTesting
    final void zzc(List<AdBreakClipInfo> list) {
        this.zzdg = list;
    }

    @VisibleForTesting
    public final void zzd(String str) {
        this.zzdh = str;
    }

    final void zzd(JSONObject jSONObject) throws JSONException {
        int i = 0;
        if (jSONObject.has("breaks")) {
            JSONArray jSONArray = jSONObject.getJSONArray("breaks");
            this.zzdf = new ArrayList(jSONArray.length());
            int i2 = 0;
            while (i2 < jSONArray.length()) {
                AdBreakInfo zzb = AdBreakInfo.zzb(jSONArray.getJSONObject(i2));
                if (zzb == null) {
                    this.zzdf.clear();
                    break;
                } else {
                    this.zzdf.add(zzb);
                    i2++;
                }
            }
        }
        if (jSONObject.has("breakClips")) {
            JSONArray jSONArray2 = jSONObject.getJSONArray("breakClips");
            this.zzdg = new ArrayList(jSONArray2.length());
            while (i < jSONArray2.length()) {
                AdBreakClipInfo zza = AdBreakClipInfo.zza(jSONArray2.getJSONObject(i));
                if (zza != null) {
                    this.zzdg.add(zza);
                    i++;
                } else {
                    this.zzdg.clear();
                    return;
                }
            }
        }
    }
}
