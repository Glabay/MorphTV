package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import java.util.Locale;
import org.json.JSONObject;

@Class(creator = "WebImageCreator")
public final class WebImage extends AbstractSafeParcelable {
    public static final Creator<WebImage> CREATOR = new WebImageCreator();
    @VersionField(id = 1)
    private final int zzal;
    @Field(getter = "getWidth", id = 3)
    private final int zzps;
    @Field(getter = "getHeight", id = 4)
    private final int zzpt;
    @Field(getter = "getUrl", id = 2)
    private final Uri zzpu;

    @Constructor
    WebImage(@Param(id = 1) int i, @Param(id = 2) Uri uri, @Param(id = 3) int i2, @Param(id = 4) int i3) {
        this.zzal = i;
        this.zzpu = uri;
        this.zzps = i2;
        this.zzpt = i3;
    }

    public WebImage(Uri uri) throws IllegalArgumentException {
        this(uri, 0, 0);
    }

    public WebImage(Uri uri, int i, int i2) throws IllegalArgumentException {
        this(1, uri, i, i2);
        if (uri == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (i >= 0) {
            if (i2 >= 0) {
                return;
            }
        }
        throw new IllegalArgumentException("width and height must not be negative");
    }

    public WebImage(JSONObject jSONObject) throws IllegalArgumentException {
        this(zza(jSONObject), jSONObject.optInt("width", 0), jSONObject.optInt("height", 0));
    }

    private static android.net.Uri zza(org.json.JSONObject r1) {
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
        r0 = "url";
        r0 = r1.has(r0);
        if (r0 == 0) goto L_0x0013;
    L_0x0008:
        r0 = "url";	 Catch:{ JSONException -> 0x0013 }
        r1 = r1.getString(r0);	 Catch:{ JSONException -> 0x0013 }
        r1 = android.net.Uri.parse(r1);	 Catch:{ JSONException -> 0x0013 }
        return r1;
    L_0x0013:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.images.WebImage.zza(org.json.JSONObject):android.net.Uri");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof WebImage)) {
            return false;
        }
        WebImage webImage = (WebImage) obj;
        return Objects.equal(this.zzpu, webImage.zzpu) && this.zzps == webImage.zzps && this.zzpt == webImage.zzpt;
    }

    public final int getHeight() {
        return this.zzpt;
    }

    public final Uri getUrl() {
        return this.zzpu;
    }

    public final int getWidth() {
        return this.zzps;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzpu, Integer.valueOf(this.zzps), Integer.valueOf(this.zzpt));
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
        r3 = this;
        r0 = new org.json.JSONObject;
        r0.<init>();
        r1 = "url";	 Catch:{ JSONException -> 0x001e }
        r2 = r3.zzpu;	 Catch:{ JSONException -> 0x001e }
        r2 = r2.toString();	 Catch:{ JSONException -> 0x001e }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x001e }
        r1 = "width";	 Catch:{ JSONException -> 0x001e }
        r2 = r3.zzps;	 Catch:{ JSONException -> 0x001e }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x001e }
        r1 = "height";	 Catch:{ JSONException -> 0x001e }
        r2 = r3.zzpt;	 Catch:{ JSONException -> 0x001e }
        r0.put(r1, r2);	 Catch:{ JSONException -> 0x001e }
    L_0x001e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.images.WebImage.toJson():org.json.JSONObject");
    }

    public final String toString() {
        return String.format(Locale.US, "Image %dx%d %s", new Object[]{Integer.valueOf(this.zzps), Integer.valueOf(this.zzpt), this.zzpu.toString()});
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeParcelable(parcel, 2, getUrl(), i, false);
        SafeParcelWriter.writeInt(parcel, 3, getWidth());
        SafeParcelWriter.writeInt(parcel, 4, getHeight());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
