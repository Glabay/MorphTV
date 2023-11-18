package com.google.android.gms.cast;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.cast.zzcu;
import java.util.Locale;

@Class(creator = "LaunchOptionsCreator")
@Reserved({1})
public class LaunchOptions extends AbstractSafeParcelable {
    public static final Creator<LaunchOptions> CREATOR = new zzah();
    @Field(getter = "getRelaunchIfRunning", id = 2)
    private boolean zzcx;
    @Field(getter = "getLanguage", id = 3)
    private String zzcy;

    @VisibleForTesting
    public static final class Builder {
        private LaunchOptions zzcz = new LaunchOptions();

        public final LaunchOptions build() {
            return this.zzcz;
        }

        public final Builder setLocale(Locale locale) {
            this.zzcz.setLanguage(zzcu.zza(locale));
            return this;
        }

        public final Builder setRelaunchIfRunning(boolean z) {
            this.zzcz.setRelaunchIfRunning(z);
            return this;
        }
    }

    public LaunchOptions() {
        this(false, zzcu.zza(Locale.getDefault()));
    }

    @Constructor
    LaunchOptions(@Param(id = 2) boolean z, @Param(id = 3) String str) {
        this.zzcx = z;
        this.zzcy = str;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LaunchOptions)) {
            return false;
        }
        LaunchOptions launchOptions = (LaunchOptions) obj;
        return this.zzcx == launchOptions.zzcx && zzcu.zza(this.zzcy, launchOptions.zzcy);
    }

    public String getLanguage() {
        return this.zzcy;
    }

    public boolean getRelaunchIfRunning() {
        return this.zzcx;
    }

    public int hashCode() {
        return Objects.hashCode(Boolean.valueOf(this.zzcx), this.zzcy);
    }

    public void setLanguage(String str) {
        this.zzcy = str;
    }

    public void setRelaunchIfRunning(boolean z) {
        this.zzcx = z;
    }

    public String toString() {
        return String.format("LaunchOptions(relaunchIfRunning=%b, language=%s)", new Object[]{Boolean.valueOf(this.zzcx), this.zzcy});
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBoolean(parcel, 2, getRelaunchIfRunning());
        SafeParcelWriter.writeString(parcel, 3, getLanguage(), false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
