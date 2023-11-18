package com.google.android.gms.cast.framework;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Class(creator = "CastOptionsCreator")
@Reserved({1})
public class CastOptions extends AbstractSafeParcelable {
    public static final Creator<CastOptions> CREATOR = new zzb();
    @Field(getter = "getLaunchOptions", id = 5)
    private final LaunchOptions zzcz;
    @Field(getter = "getReceiverApplicationId", id = 2)
    private String zzhc;
    @Field(getter = "getSupportedNamespaces", id = 3)
    private final List<String> zzhd;
    @Field(getter = "getStopReceiverApplicationWhenEndingSession", id = 4)
    private final boolean zzhe;
    @Field(getter = "getResumeSavedSession", id = 6)
    private final boolean zzhf;
    @Field(getter = "getCastMediaOptions", id = 7)
    private final CastMediaOptions zzhg;
    @Field(getter = "getEnableReconnectionService", id = 8)
    private final boolean zzhh;
    @Field(getter = "getVolumeDeltaBeforeIceCreamSandwich", id = 9)
    private final double zzhi;

    @VisibleForTesting
    public static final class Builder {
        private LaunchOptions zzcz = new LaunchOptions();
        private String zzhc;
        private List<String> zzhd = new ArrayList();
        private boolean zzhe;
        private boolean zzhf = true;
        private CastMediaOptions zzhg = new com.google.android.gms.cast.framework.media.CastMediaOptions.Builder().build();
        private boolean zzhh = true;
        private double zzhi = 0.05000000074505806d;

        public final CastOptions build() {
            return new CastOptions(this.zzhc, this.zzhd, this.zzhe, this.zzcz, this.zzhf, this.zzhg, this.zzhh, this.zzhi);
        }

        public final Builder setCastMediaOptions(CastMediaOptions castMediaOptions) {
            this.zzhg = castMediaOptions;
            return this;
        }

        public final Builder setEnableReconnectionService(boolean z) {
            this.zzhh = z;
            return this;
        }

        public final Builder setLaunchOptions(LaunchOptions launchOptions) {
            this.zzcz = launchOptions;
            return this;
        }

        public final Builder setReceiverApplicationId(String str) {
            this.zzhc = str;
            return this;
        }

        public final Builder setResumeSavedSession(boolean z) {
            this.zzhf = z;
            return this;
        }

        public final Builder setStopReceiverApplicationWhenEndingSession(boolean z) {
            this.zzhe = z;
            return this;
        }

        public final Builder setSupportedNamespaces(List<String> list) {
            this.zzhd = list;
            return this;
        }

        public final Builder setVolumeDeltaBeforeIceCreamSandwich(double d) throws IllegalArgumentException {
            if (d > 0.0d) {
                if (d <= 0.5d) {
                    this.zzhi = d;
                    return this;
                }
            }
            throw new IllegalArgumentException("volumeDelta must be greater than 0 and less or equal to 0.5");
        }
    }

    @Constructor
    CastOptions(@Param(id = 2) String str, @Param(id = 3) List<String> list, @Param(id = 4) boolean z, @Param(id = 5) LaunchOptions launchOptions, @Param(id = 6) boolean z2, @Param(id = 7) CastMediaOptions castMediaOptions, @Param(id = 8) boolean z3, @Param(id = 9) double d) {
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        this.zzhc = str;
        int size = list == null ? 0 : list.size();
        this.zzhd = new ArrayList(size);
        if (size > 0) {
            this.zzhd.addAll(list);
        }
        this.zzhe = z;
        if (launchOptions == null) {
            launchOptions = new LaunchOptions();
        }
        this.zzcz = launchOptions;
        this.zzhf = z2;
        this.zzhg = castMediaOptions;
        this.zzhh = z3;
        this.zzhi = d;
    }

    public CastMediaOptions getCastMediaOptions() {
        return this.zzhg;
    }

    public boolean getEnableReconnectionService() {
        return this.zzhh;
    }

    public LaunchOptions getLaunchOptions() {
        return this.zzcz;
    }

    public String getReceiverApplicationId() {
        return this.zzhc;
    }

    public boolean getResumeSavedSession() {
        return this.zzhf;
    }

    public boolean getStopReceiverApplicationWhenEndingSession() {
        return this.zzhe;
    }

    public List<String> getSupportedNamespaces() {
        return Collections.unmodifiableList(this.zzhd);
    }

    public double getVolumeDeltaBeforeIceCreamSandwich() {
        return this.zzhi;
    }

    public final void setReceiverApplicationId(String str) {
        this.zzhc = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getReceiverApplicationId(), false);
        SafeParcelWriter.writeStringList(parcel, 3, getSupportedNamespaces(), false);
        SafeParcelWriter.writeBoolean(parcel, 4, getStopReceiverApplicationWhenEndingSession());
        SafeParcelWriter.writeParcelable(parcel, 5, getLaunchOptions(), i, false);
        SafeParcelWriter.writeBoolean(parcel, 6, getResumeSavedSession());
        SafeParcelWriter.writeParcelable(parcel, 7, getCastMediaOptions(), i, false);
        SafeParcelWriter.writeBoolean(parcel, 8, getEnableReconnectionService());
        SafeParcelWriter.writeDouble(parcel, 9, getVolumeDeltaBeforeIceCreamSandwich());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
