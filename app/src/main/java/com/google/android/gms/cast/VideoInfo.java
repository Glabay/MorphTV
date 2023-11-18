package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "VideoInfoCreator")
@Reserved({1})
public final class VideoInfo extends AbstractSafeParcelable {
    public static final Creator<VideoInfo> CREATOR = new zzbt();
    public static final int HDR_TYPE_DV = 3;
    public static final int HDR_TYPE_HDR = 4;
    public static final int HDR_TYPE_HDR10 = 2;
    public static final int HDR_TYPE_SDR = 1;
    public static final int HDR_TYPE_UNKNOWN = 0;
    @Field(getter = "getHeight", id = 3)
    private int height;
    @Field(getter = "getWidth", id = 2)
    private int width;
    @Field(getter = "getHdrType", id = 4)
    private int zzgn;

    @Constructor
    VideoInfo(@Param(id = 2) int i, @Param(id = 3) int i2, @Param(id = 4) int i3) {
        this.width = i;
        this.height = i2;
        this.zzgn = i3;
    }

    static VideoInfo zzg(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        try {
            int i;
            String string = jSONObject.getString("hdrType");
            Object obj = -1;
            int hashCode = string.hashCode();
            if (hashCode != 3218) {
                if (hashCode != 103158) {
                    if (hashCode != 113729) {
                        if (hashCode == 99136405) {
                            if (string.equals("hdr10")) {
                                obj = 1;
                            }
                        }
                    } else if (string.equals("sdr")) {
                        obj = 3;
                    }
                } else if (string.equals("hdr")) {
                    obj = 2;
                }
            } else if (string.equals("dv")) {
                obj = null;
            }
            switch (obj) {
                case null:
                    i = 3;
                    break;
                case 1:
                    i = 2;
                    break;
                case 2:
                    i = 4;
                    break;
                case 3:
                    i = 1;
                    break;
                default:
                    Log.d("VideoInfo", String.format(Locale.ROOT, "Unknown HDR type: %s", new Object[]{string}));
                    i = 0;
                    break;
            }
            return new VideoInfo(jSONObject.getInt("width"), jSONObject.getInt("height"), i);
        } catch (JSONException e) {
            Log.d("VideoInfo", String.format(Locale.ROOT, "Error while creating a VideoInfo instance from JSON: %s", new Object[]{e.getMessage()}));
            return null;
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VideoInfo)) {
            return false;
        }
        VideoInfo videoInfo = (VideoInfo) obj;
        return this.height == videoInfo.getHeight() && this.width == videoInfo.getWidth() && this.zzgn == videoInfo.getHdrType();
    }

    public final int getHdrType() {
        return this.zzgn;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.height), Integer.valueOf(this.width), Integer.valueOf(this.zzgn));
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, getWidth());
        SafeParcelWriter.writeInt(parcel, 3, getHeight());
        SafeParcelWriter.writeInt(parcel, 4, getHdrType());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
