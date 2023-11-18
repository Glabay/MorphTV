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
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "MediaTrackCreator")
@Reserved({1})
public final class MediaTrack extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<MediaTrack> CREATOR = new zzao();
    public static final int SUBTYPE_CAPTIONS = 2;
    public static final int SUBTYPE_CHAPTERS = 4;
    public static final int SUBTYPE_DESCRIPTIONS = 3;
    public static final int SUBTYPE_METADATA = 5;
    public static final int SUBTYPE_NONE = 0;
    public static final int SUBTYPE_SUBTITLES = 1;
    public static final int SUBTYPE_UNKNOWN = -1;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_VIDEO = 3;
    @Field(getter = "getId", id = 2)
    private long id;
    @Field(getter = "getName", id = 6)
    private String name;
    @Field(getter = "getType", id = 3)
    private int type;
    @Field(getter = "getLanguage", id = 7)
    private String zzcy;
    @Field(getter = "getContentType", id = 5)
    private String zzda;
    @Field(getter = "getSubtype", id = 8)
    private int zzes;
    @Field(id = 9)
    private String zzj;
    @Field(getter = "getContentId", id = 4)
    private String zzk;
    private JSONObject zzp;

    @VisibleForTesting
    public static class Builder {
        private final MediaTrack zzet;

        public Builder(long j, int i) throws IllegalArgumentException {
            this.zzet = new MediaTrack(j, i);
        }

        public MediaTrack build() {
            return this.zzet;
        }

        public Builder setContentId(String str) {
            this.zzet.setContentId(str);
            return this;
        }

        public Builder setContentType(String str) {
            this.zzet.setContentType(str);
            return this;
        }

        public Builder setCustomData(JSONObject jSONObject) {
            this.zzet.setCustomData(jSONObject);
            return this;
        }

        public Builder setLanguage(String str) {
            this.zzet.setLanguage(str);
            return this;
        }

        public Builder setLanguage(Locale locale) {
            this.zzet.setLanguage(zzcu.zza(locale));
            return this;
        }

        public Builder setName(String str) {
            this.zzet.setName(str);
            return this;
        }

        public Builder setSubtype(int i) throws IllegalArgumentException {
            this.zzet.zzb(i);
            return this;
        }
    }

    MediaTrack(long j, int i) throws IllegalArgumentException {
        this(0, 0, null, null, null, null, -1, null);
        this.id = j;
        if (i > 0) {
            if (i <= 3) {
                this.type = i;
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder(24);
        stringBuilder.append("invalid type ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
    MediaTrack(@com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 2) long r1, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 3) int r3, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 4) java.lang.String r4, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 5) java.lang.String r5, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 6) java.lang.String r6, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 7) java.lang.String r7, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 8) int r8, @com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param(id = 9) java.lang.String r9) {
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
        r0.id = r1;
        r0.type = r3;
        r0.zzk = r4;
        r0.zzda = r5;
        r0.name = r6;
        r0.zzcy = r7;
        r0.zzes = r8;
        r0.zzj = r9;
        r1 = r0.zzj;
        r2 = 0;
        if (r1 == 0) goto L_0x0027;
    L_0x0018:
        r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0022 }
        r3 = r0.zzj;	 Catch:{ JSONException -> 0x0022 }
        r1.<init>(r3);	 Catch:{ JSONException -> 0x0022 }
        r0.zzp = r1;	 Catch:{ JSONException -> 0x0022 }
        return;
    L_0x0022:
        r0.zzp = r2;
        r0.zzj = r2;
        return;
    L_0x0027:
        r0.zzp = r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaTrack.<init>(long, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String):void");
    }

    MediaTrack(JSONObject jSONObject) throws JSONException {
        int i;
        this(0, 0, null, null, null, null, -1, null);
        this.id = jSONObject.getLong("trackId");
        String string = jSONObject.getString("type");
        if ("TEXT".equals(string)) {
            this.type = 1;
        } else if ("AUDIO".equals(string)) {
            this.type = 2;
        } else if ("VIDEO".equals(string)) {
            this.type = 3;
        } else {
            String str = "invalid type: ";
            string = String.valueOf(string);
            throw new JSONException(string.length() != 0 ? str.concat(string) : new String(str));
        }
        this.zzk = jSONObject.optString("trackContentId", null);
        this.zzda = jSONObject.optString("trackContentType", null);
        this.name = jSONObject.optString("name", null);
        this.zzcy = jSONObject.optString("language", null);
        if (jSONObject.has("subtype")) {
            string = jSONObject.getString("subtype");
            if ("SUBTITLES".equals(string)) {
                this.zzes = 1;
            } else if ("CAPTIONS".equals(string)) {
                this.zzes = 2;
            } else if ("DESCRIPTIONS".equals(string)) {
                this.zzes = 3;
            } else if ("CHAPTERS".equals(string)) {
                i = 4;
            } else if ("METADATA".equals(string)) {
                i = 5;
            } else {
                str = "invalid subtype: ";
                string = String.valueOf(string);
                throw new JSONException(string.length() != 0 ? str.concat(string) : new String(str));
            }
            this.zzp = jSONObject.optJSONObject("customData");
        }
        i = 0;
        this.zzes = i;
        this.zzp = jSONObject.optJSONObject("customData");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaTrack)) {
            return false;
        }
        MediaTrack mediaTrack = (MediaTrack) obj;
        return (this.zzp == null ? 1 : null) != (mediaTrack.zzp == null ? 1 : null) ? false : (this.zzp == null || mediaTrack.zzp == null || JsonUtils.areJsonValuesEquivalent(this.zzp, mediaTrack.zzp)) && this.id == mediaTrack.id && this.type == mediaTrack.type && zzcu.zza(this.zzk, mediaTrack.zzk) && zzcu.zza(this.zzda, mediaTrack.zzda) && zzcu.zza(this.name, mediaTrack.name) && zzcu.zza(this.zzcy, mediaTrack.zzcy) && this.zzes == mediaTrack.zzes;
    }

    public final String getContentId() {
        return this.zzk;
    }

    public final String getContentType() {
        return this.zzda;
    }

    public final JSONObject getCustomData() {
        return this.zzp;
    }

    public final long getId() {
        return this.id;
    }

    public final String getLanguage() {
        return this.zzcy;
    }

    public final String getName() {
        return this.name;
    }

    public final int getSubtype() {
        return this.zzes;
    }

    public final int getType() {
        return this.type;
    }

    public final int hashCode() {
        return Objects.hashCode(Long.valueOf(this.id), Integer.valueOf(this.type), this.zzk, this.zzda, this.name, this.zzcy, Integer.valueOf(this.zzes), String.valueOf(this.zzp));
    }

    public final void setContentId(String str) {
        this.zzk = str;
    }

    public final void setContentType(String str) {
        this.zzda = str;
    }

    final void setCustomData(JSONObject jSONObject) {
        this.zzp = jSONObject;
    }

    final void setLanguage(String str) {
        this.zzcy = str;
    }

    final void setName(String str) {
        this.name = str;
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
        r1 = "trackId";	 Catch:{ JSONException -> 0x0081 }
        r2 = r4.id;	 Catch:{ JSONException -> 0x0081 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
        r1 = r4.type;	 Catch:{ JSONException -> 0x0081 }
        switch(r1) {
            case 1: goto L_0x001f;
            case 2: goto L_0x001a;
            case 3: goto L_0x0012;
            default: goto L_0x0011;
        };	 Catch:{ JSONException -> 0x0081 }
    L_0x0011:
        goto L_0x0024;	 Catch:{ JSONException -> 0x0081 }
    L_0x0012:
        r1 = "type";	 Catch:{ JSONException -> 0x0081 }
        r2 = "VIDEO";	 Catch:{ JSONException -> 0x0081 }
    L_0x0016:
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
        goto L_0x0024;	 Catch:{ JSONException -> 0x0081 }
    L_0x001a:
        r1 = "type";	 Catch:{ JSONException -> 0x0081 }
        r2 = "AUDIO";	 Catch:{ JSONException -> 0x0081 }
        goto L_0x0016;	 Catch:{ JSONException -> 0x0081 }
    L_0x001f:
        r1 = "type";	 Catch:{ JSONException -> 0x0081 }
        r2 = "TEXT";	 Catch:{ JSONException -> 0x0081 }
        goto L_0x0016;	 Catch:{ JSONException -> 0x0081 }
    L_0x0024:
        r1 = r4.zzk;	 Catch:{ JSONException -> 0x0081 }
        if (r1 == 0) goto L_0x002f;	 Catch:{ JSONException -> 0x0081 }
    L_0x0028:
        r1 = "trackContentId";	 Catch:{ JSONException -> 0x0081 }
        r2 = r4.zzk;	 Catch:{ JSONException -> 0x0081 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
    L_0x002f:
        r1 = r4.zzda;	 Catch:{ JSONException -> 0x0081 }
        if (r1 == 0) goto L_0x003a;	 Catch:{ JSONException -> 0x0081 }
    L_0x0033:
        r1 = "trackContentType";	 Catch:{ JSONException -> 0x0081 }
        r2 = r4.zzda;	 Catch:{ JSONException -> 0x0081 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
    L_0x003a:
        r1 = r4.name;	 Catch:{ JSONException -> 0x0081 }
        if (r1 == 0) goto L_0x0045;	 Catch:{ JSONException -> 0x0081 }
    L_0x003e:
        r1 = "name";	 Catch:{ JSONException -> 0x0081 }
        r2 = r4.name;	 Catch:{ JSONException -> 0x0081 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
    L_0x0045:
        r1 = r4.zzcy;	 Catch:{ JSONException -> 0x0081 }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ JSONException -> 0x0081 }
        if (r1 != 0) goto L_0x0054;	 Catch:{ JSONException -> 0x0081 }
    L_0x004d:
        r1 = "language";	 Catch:{ JSONException -> 0x0081 }
        r2 = r4.zzcy;	 Catch:{ JSONException -> 0x0081 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
    L_0x0054:
        r1 = r4.zzes;	 Catch:{ JSONException -> 0x0081 }
        switch(r1) {
            case 1: goto L_0x0071;
            case 2: goto L_0x006c;
            case 3: goto L_0x0067;
            case 4: goto L_0x0062;
            case 5: goto L_0x005a;
            default: goto L_0x0059;
        };	 Catch:{ JSONException -> 0x0081 }
    L_0x0059:
        goto L_0x0076;	 Catch:{ JSONException -> 0x0081 }
    L_0x005a:
        r1 = "subtype";	 Catch:{ JSONException -> 0x0081 }
        r2 = "METADATA";	 Catch:{ JSONException -> 0x0081 }
    L_0x005e:
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
        goto L_0x0076;	 Catch:{ JSONException -> 0x0081 }
    L_0x0062:
        r1 = "subtype";	 Catch:{ JSONException -> 0x0081 }
        r2 = "CHAPTERS";	 Catch:{ JSONException -> 0x0081 }
        goto L_0x005e;	 Catch:{ JSONException -> 0x0081 }
    L_0x0067:
        r1 = "subtype";	 Catch:{ JSONException -> 0x0081 }
        r2 = "DESCRIPTIONS";	 Catch:{ JSONException -> 0x0081 }
        goto L_0x005e;	 Catch:{ JSONException -> 0x0081 }
    L_0x006c:
        r1 = "subtype";	 Catch:{ JSONException -> 0x0081 }
        r2 = "CAPTIONS";	 Catch:{ JSONException -> 0x0081 }
        goto L_0x005e;	 Catch:{ JSONException -> 0x0081 }
    L_0x0071:
        r1 = "subtype";	 Catch:{ JSONException -> 0x0081 }
        r2 = "SUBTITLES";	 Catch:{ JSONException -> 0x0081 }
        goto L_0x005e;	 Catch:{ JSONException -> 0x0081 }
    L_0x0076:
        r1 = r4.zzp;	 Catch:{ JSONException -> 0x0081 }
        if (r1 == 0) goto L_0x0081;	 Catch:{ JSONException -> 0x0081 }
    L_0x007a:
        r1 = "customData";	 Catch:{ JSONException -> 0x0081 }
        r2 = r4.zzp;	 Catch:{ JSONException -> 0x0081 }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x0081 }
    L_0x0081:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.cast.MediaTrack.toJson():org.json.JSONObject");
    }

    public final void writeToParcel(Parcel parcel, int i) {
        this.zzj = this.zzp == null ? null : this.zzp.toString();
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeLong(parcel, 2, getId());
        SafeParcelWriter.writeInt(parcel, 3, getType());
        SafeParcelWriter.writeString(parcel, 4, getContentId(), false);
        SafeParcelWriter.writeString(parcel, 5, getContentType(), false);
        SafeParcelWriter.writeString(parcel, 6, getName(), false);
        SafeParcelWriter.writeString(parcel, 7, getLanguage(), false);
        SafeParcelWriter.writeInt(parcel, 8, getSubtype());
        SafeParcelWriter.writeString(parcel, 9, this.zzj, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    final void zzb(int i) throws IllegalArgumentException {
        if (i >= 0) {
            if (i <= 5) {
                if (i == 0 || this.type == 1) {
                    this.zzes = i;
                    return;
                }
                throw new IllegalArgumentException("subtypes are only valid for text tracks");
            }
        }
        StringBuilder stringBuilder = new StringBuilder(27);
        stringBuilder.append("invalid subtype ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
