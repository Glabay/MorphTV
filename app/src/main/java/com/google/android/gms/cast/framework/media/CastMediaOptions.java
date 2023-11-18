package com.google.android.gms.cast.framework.media;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.cast.zzdg;

@Class(creator = "CastMediaOptionsCreator")
@Reserved({1})
public class CastMediaOptions extends AbstractSafeParcelable {
    public static final Creator<CastMediaOptions> CREATOR = new zza();
    private static final zzdg zzbd = new zzdg("CastMediaOptions");
    @Field(getter = "getMediaIntentReceiverClassName", id = 2)
    private final String zzkr;
    @Field(getter = "getExpandedControllerActivityClassName", id = 3)
    private final String zzks;
    @Field(getter = "getImagePickerAsBinder", id = 4, type = "android.os.IBinder")
    private final zzb zzkt;
    @Field(getter = "getNotificationOptions", id = 5)
    private final NotificationOptions zzku;
    @Field(getter = "getDisableRemoteControlNotification", id = 6)
    private final boolean zzkv;

    public static final class Builder {
        private String zzkr = MediaIntentReceiver.class.getName();
        private String zzks;
        private NotificationOptions zzku = new com.google.android.gms.cast.framework.media.NotificationOptions.Builder().build();
        private ImagePicker zzkw;

        public final CastMediaOptions build() {
            return new CastMediaOptions(this.zzkr, this.zzks, this.zzkw == null ? null : this.zzkw.zzax().asBinder(), this.zzku, false);
        }

        public final Builder setExpandedControllerActivityClassName(String str) {
            this.zzks = str;
            return this;
        }

        public final Builder setImagePicker(ImagePicker imagePicker) {
            this.zzkw = imagePicker;
            return this;
        }

        public final Builder setMediaIntentReceiverClassName(String str) {
            this.zzkr = str;
            return this;
        }

        public final Builder setNotificationOptions(NotificationOptions notificationOptions) {
            this.zzku = notificationOptions;
            return this;
        }
    }

    @Constructor
    CastMediaOptions(@Param(id = 2) String str, @Param(id = 3) String str2, @Param(id = 4) IBinder iBinder, @Param(id = 5) NotificationOptions notificationOptions, @Param(id = 6) boolean z) {
        zzb zzb;
        this.zzkr = str;
        this.zzks = str2;
        if (iBinder == null) {
            zzb = null;
        } else {
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.framework.media.IImagePicker");
            zzb = queryLocalInterface instanceof zzb ? (zzb) queryLocalInterface : new zzc(iBinder);
        }
        this.zzkt = zzb;
        this.zzku = notificationOptions;
        this.zzkv = z;
    }

    public String getExpandedControllerActivityClassName() {
        return this.zzks;
    }

    public ImagePicker getImagePicker() {
        if (this.zzkt != null) {
            try {
                return (ImagePicker) ObjectWrapper.unwrap(this.zzkt.zzaw());
            } catch (Throwable e) {
                zzbd.zza(e, "Unable to call %s on %s.", "getWrappedClientObject", zzb.class.getSimpleName());
            }
        }
        return null;
    }

    public String getMediaIntentReceiverClassName() {
        return this.zzkr;
    }

    public NotificationOptions getNotificationOptions() {
        return this.zzku;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getMediaIntentReceiverClassName(), false);
        SafeParcelWriter.writeString(parcel, 3, getExpandedControllerActivityClassName(), false);
        SafeParcelWriter.writeIBinder(parcel, 4, this.zzkt == null ? null : this.zzkt.asBinder(), false);
        SafeParcelWriter.writeParcelable(parcel, 5, getNotificationOptions(), i, false);
        SafeParcelWriter.writeBoolean(parcel, 6, this.zzkv);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final boolean zzav() {
        return this.zzkv;
    }
}
