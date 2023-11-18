package com.google.android.gms.cast.framework.media;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Class(creator = "NotificationOptionsCreator")
@Reserved({1})
public class NotificationOptions extends AbstractSafeParcelable {
    public static final Creator<NotificationOptions> CREATOR = new zzq();
    public static final long SKIP_STEP_TEN_SECONDS_IN_MS = 10000;
    public static final long SKIP_STEP_THIRTY_SECONDS_IN_MS = 30000;
    private static final List<String> zzmc = Arrays.asList(new String[]{MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK, MediaIntentReceiver.ACTION_STOP_CASTING});
    private static final int[] zzmd = new int[]{0, 1};
    @Field(getter = "getActions", id = 2)
    private final List<String> zzme;
    @Field(getter = "getCompatActionIndices", id = 3)
    private final int[] zzmf;
    @Field(getter = "getSkipStepMs", id = 4)
    private final long zzmg;
    @Field(getter = "getTargetActivityClassName", id = 5)
    private final String zzmh;
    @Field(getter = "getSmallIconDrawableResId", id = 6)
    private final int zzmi;
    @Field(getter = "getStopLiveStreamDrawableResId", id = 7)
    private final int zzmj;
    @Field(getter = "getPauseDrawableResId", id = 8)
    private final int zzmk;
    @Field(getter = "getPlayDrawableResId", id = 9)
    private final int zzml;
    @Field(getter = "getSkipNextDrawableResId", id = 10)
    private final int zzmm;
    @Field(getter = "getSkipPrevDrawableResId", id = 11)
    private final int zzmn;
    @Field(getter = "getForwardDrawableResId", id = 12)
    private final int zzmo;
    @Field(getter = "getForward10DrawableResId", id = 13)
    private final int zzmp;
    @Field(getter = "getForward30DrawableResId", id = 14)
    private final int zzmq;
    @Field(getter = "getRewindDrawableResId", id = 15)
    private final int zzmr;
    @Field(getter = "getRewind10DrawableResId", id = 16)
    private final int zzms;
    @Field(getter = "getRewind30DrawableResId", id = 17)
    private final int zzmt;
    @Field(getter = "getDisconnectDrawableResId", id = 18)
    private final int zzmu;
    @Field(getter = "getImageSizeDimenResId", id = 19)
    private final int zzmv;
    @Field(getter = "getCastingToDeviceStringResId", id = 20)
    private final int zzmw;
    @Field(getter = "getStopLiveStreamTitleResId", id = 21)
    private final int zzmx;
    @Field(getter = "getPauseTitleResId", id = 22)
    private final int zzmy;
    @Field(getter = "getPlayTitleResId", id = 23)
    private final int zzmz;
    @Field(getter = "getSkipNextTitleResId", id = 24)
    private final int zzna;
    @Field(getter = "getSkipPrevTitleResId", id = 25)
    private final int zznb;
    @Field(getter = "getForwardTitleResId", id = 26)
    private final int zznc;
    @Field(getter = "getForward10TitleResId", id = 27)
    private final int zznd;
    @Field(getter = "getForward30TitleResId", id = 28)
    private final int zzne;
    @Field(getter = "getRewindTitleResId", id = 29)
    private final int zznf;
    @Field(getter = "getRewind10TitleResId", id = 30)
    private final int zzng;
    @Field(getter = "getRewind30TitleResId", id = 31)
    private final int zznh;
    @Field(getter = "getDisconnectTitleResId", id = 32)
    private final int zzni;
    @Field(getter = "getNotificationActionsProviderAsBinder", id = 33, type = "android.os.IBinder")
    private final zzf zznj;

    public static final class Builder {
        private List<String> zzme = NotificationOptions.zzmc;
        private int[] zzmf = NotificationOptions.zzmd;
        private long zzmg = NotificationOptions.SKIP_STEP_TEN_SECONDS_IN_MS;
        private String zzmh;
        private int zzmi = C0782R.drawable.cast_ic_notification_small_icon;
        private int zzmj = C0782R.drawable.cast_ic_notification_stop_live_stream;
        private int zzmk = C0782R.drawable.cast_ic_notification_pause;
        private int zzml = C0782R.drawable.cast_ic_notification_play;
        private int zzmm = C0782R.drawable.cast_ic_notification_skip_next;
        private int zzmn = C0782R.drawable.cast_ic_notification_skip_prev;
        private int zzmo = C0782R.drawable.cast_ic_notification_forward;
        private int zzmp = C0782R.drawable.cast_ic_notification_forward10;
        private int zzmq = C0782R.drawable.cast_ic_notification_forward30;
        private int zzmr = C0782R.drawable.cast_ic_notification_rewind;
        private int zzms = C0782R.drawable.cast_ic_notification_rewind10;
        private int zzmt = C0782R.drawable.cast_ic_notification_rewind30;
        private int zzmu = C0782R.drawable.cast_ic_notification_disconnect;
        private NotificationActionsProvider zznk;

        public final NotificationOptions build() {
            IBinder asBinder = this.zznk == null ? null : r0.zznk.zzbi().asBinder();
            List list = r0.zzme;
            int[] iArr = r0.zzmf;
            long j = r0.zzmg;
            String str = r0.zzmh;
            int i = r0.zzmi;
            int i2 = r0.zzmj;
            int i3 = r0.zzmk;
            int i4 = r0.zzml;
            int i5 = r0.zzmm;
            int i6 = r0.zzmn;
            int i7 = r0.zzmo;
            int i8 = r0.zzmp;
            return new NotificationOptions(list, iArr, j, str, i, i2, i3, i4, i5, i6, i7, i8, r0.zzmq, r0.zzmr, r0.zzms, r0.zzmt, r0.zzmu, C0782R.dimen.cast_notification_image_size, C0782R.string.cast_casting_to_device, C0782R.string.cast_stop_live_stream, C0782R.string.cast_pause, C0782R.string.cast_play, C0782R.string.cast_skip_next, C0782R.string.cast_skip_prev, C0782R.string.cast_forward, C0782R.string.cast_forward_10, C0782R.string.cast_forward_30, C0782R.string.cast_rewind, C0782R.string.cast_rewind_10, C0782R.string.cast_rewind_30, C0782R.string.cast_disconnect, asBinder);
        }

        public final Builder setActions(List<String> list, int[] iArr) {
            if (list == null && iArr != null) {
                throw new IllegalArgumentException("When setting actions to null, you must also set compatActionIndices to null.");
            } else if (list == null || iArr != null) {
                int[] zzbk;
                if (list == null || iArr == null) {
                    this.zzme = NotificationOptions.zzmc;
                    zzbk = NotificationOptions.zzmd;
                } else {
                    int size = list.size();
                    if (iArr.length > size) {
                        throw new IllegalArgumentException(String.format(Locale.ROOT, "Invalid number of compat actions: %d > %d.", new Object[]{Integer.valueOf(iArr.length), Integer.valueOf(size)}));
                    }
                    int length = iArr.length;
                    int i = 0;
                    while (i < length) {
                        int i2 = iArr[i];
                        if (i2 >= 0) {
                            if (i2 < size) {
                                i++;
                            }
                        }
                        throw new IllegalArgumentException(String.format(Locale.ROOT, "Index %d in compatActionIndices out of range: [0, %d]", new Object[]{Integer.valueOf(i2), Integer.valueOf(size - 1)}));
                    }
                    this.zzme = new ArrayList(list);
                    zzbk = Arrays.copyOf(iArr, iArr.length);
                }
                this.zzmf = zzbk;
                return this;
            } else {
                throw new IllegalArgumentException("When setting compatActionIndices to null, you must also set actions to null.");
            }
        }

        public final Builder setDisconnectDrawableResId(int i) {
            this.zzmu = i;
            return this;
        }

        public final Builder setForward10DrawableResId(int i) {
            this.zzmp = i;
            return this;
        }

        public final Builder setForward30DrawableResId(int i) {
            this.zzmq = i;
            return this;
        }

        public final Builder setForwardDrawableResId(int i) {
            this.zzmo = i;
            return this;
        }

        public final Builder setNotificationActionsProvider(@NonNull NotificationActionsProvider notificationActionsProvider) {
            if (notificationActionsProvider == null) {
                throw new IllegalArgumentException("notificationActionsProvider cannot be null.");
            }
            this.zznk = notificationActionsProvider;
            return this;
        }

        public final Builder setPauseDrawableResId(int i) {
            this.zzmk = i;
            return this;
        }

        public final Builder setPlayDrawableResId(int i) {
            this.zzml = i;
            return this;
        }

        public final Builder setRewind10DrawableResId(int i) {
            this.zzms = i;
            return this;
        }

        public final Builder setRewind30DrawableResId(int i) {
            this.zzmt = i;
            return this;
        }

        public final Builder setRewindDrawableResId(int i) {
            this.zzmr = i;
            return this;
        }

        public final Builder setSkipNextDrawableResId(int i) {
            this.zzmm = i;
            return this;
        }

        public final Builder setSkipPrevDrawableResId(int i) {
            this.zzmn = i;
            return this;
        }

        public final Builder setSkipStepMs(long j) {
            Preconditions.checkArgument(j > 0, "skipStepMs must be positive.");
            this.zzmg = j;
            return this;
        }

        public final Builder setSmallIconDrawableResId(int i) {
            this.zzmi = i;
            return this;
        }

        public final Builder setStopLiveStreamDrawableResId(int i) {
            this.zzmj = i;
            return this;
        }

        public final Builder setTargetActivityClassName(String str) {
            this.zzmh = str;
            return this;
        }
    }

    @Constructor
    public NotificationOptions(@Param(id = 2) List<String> list, @Param(id = 3) int[] iArr, @Param(id = 4) long j, @Param(id = 5) String str, @Param(id = 6) int i, @Param(id = 7) int i2, @Param(id = 8) int i3, @Param(id = 9) int i4, @Param(id = 10) int i5, @Param(id = 11) int i6, @Param(id = 12) int i7, @Param(id = 13) int i8, @Param(id = 14) int i9, @Param(id = 15) int i10, @Param(id = 16) int i11, @Param(id = 17) int i12, @Param(id = 18) int i13, @Param(id = 19) int i14, @Param(id = 20) int i15, @Param(id = 21) int i16, @Param(id = 22) int i17, @Param(id = 23) int i18, @Param(id = 24) int i19, @Param(id = 25) int i20, @Param(id = 26) int i21, @Param(id = 27) int i22, @Param(id = 28) int i23, @Param(id = 29) int i24, @Param(id = 30) int i25, @Param(id = 31) int i26, @Param(id = 32) int i27, @Param(id = 33) IBinder iBinder) {
        AbstractSafeParcelable abstractSafeParcelable = this;
        List<String> list2 = list;
        int[] iArr2 = iArr;
        IBinder iBinder2 = iBinder;
        zzf zzf = null;
        if (list2 != null) {
            abstractSafeParcelable.zzme = new ArrayList(list2);
        } else {
            abstractSafeParcelable.zzme = null;
        }
        if (iArr2 != null) {
            abstractSafeParcelable.zzmf = Arrays.copyOf(iArr2, iArr2.length);
        } else {
            abstractSafeParcelable.zzmf = null;
        }
        abstractSafeParcelable.zzmg = j;
        abstractSafeParcelable.zzmh = str;
        abstractSafeParcelable.zzmi = i;
        abstractSafeParcelable.zzmj = i2;
        abstractSafeParcelable.zzmk = i3;
        abstractSafeParcelable.zzml = i4;
        abstractSafeParcelable.zzmm = i5;
        abstractSafeParcelable.zzmn = i6;
        abstractSafeParcelable.zzmo = i7;
        abstractSafeParcelable.zzmp = i8;
        abstractSafeParcelable.zzmq = i9;
        abstractSafeParcelable.zzmr = i10;
        abstractSafeParcelable.zzms = i11;
        abstractSafeParcelable.zzmt = i12;
        abstractSafeParcelable.zzmu = i13;
        abstractSafeParcelable.zzmv = i14;
        abstractSafeParcelable.zzmw = i15;
        abstractSafeParcelable.zzmx = i16;
        abstractSafeParcelable.zzmy = i17;
        abstractSafeParcelable.zzmz = i18;
        abstractSafeParcelable.zzna = i19;
        abstractSafeParcelable.zznb = i20;
        abstractSafeParcelable.zznc = i21;
        abstractSafeParcelable.zznd = i22;
        abstractSafeParcelable.zzne = i23;
        abstractSafeParcelable.zznf = i24;
        abstractSafeParcelable.zzng = i25;
        abstractSafeParcelable.zznh = i26;
        abstractSafeParcelable.zzni = i27;
        if (iBinder2 != null) {
            IInterface queryLocalInterface = iBinder2.queryLocalInterface("com.google.android.gms.cast.framework.media.INotificationActionsProvider");
            zzf = queryLocalInterface instanceof zzf ? (zzf) queryLocalInterface : new zzh(iBinder2);
        }
        abstractSafeParcelable.zznj = zzf;
    }

    public List<String> getActions() {
        return this.zzme;
    }

    public int getCastingToDeviceStringResId() {
        return this.zzmw;
    }

    public int[] getCompatActionIndices() {
        return Arrays.copyOf(this.zzmf, this.zzmf.length);
    }

    public int getDisconnectDrawableResId() {
        return this.zzmu;
    }

    public int getForward10DrawableResId() {
        return this.zzmp;
    }

    public int getForward30DrawableResId() {
        return this.zzmq;
    }

    public int getForwardDrawableResId() {
        return this.zzmo;
    }

    public int getPauseDrawableResId() {
        return this.zzmk;
    }

    public int getPlayDrawableResId() {
        return this.zzml;
    }

    public int getRewind10DrawableResId() {
        return this.zzms;
    }

    public int getRewind30DrawableResId() {
        return this.zzmt;
    }

    public int getRewindDrawableResId() {
        return this.zzmr;
    }

    public int getSkipNextDrawableResId() {
        return this.zzmm;
    }

    public int getSkipPrevDrawableResId() {
        return this.zzmn;
    }

    public long getSkipStepMs() {
        return this.zzmg;
    }

    public int getSmallIconDrawableResId() {
        return this.zzmi;
    }

    public int getStopLiveStreamDrawableResId() {
        return this.zzmj;
    }

    public int getStopLiveStreamTitleResId() {
        return this.zzmx;
    }

    public String getTargetActivityClassName() {
        return this.zzmh;
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringList(parcel, 2, getActions(), false);
        SafeParcelWriter.writeIntArray(parcel, 3, getCompatActionIndices(), false);
        SafeParcelWriter.writeLong(parcel, 4, getSkipStepMs());
        SafeParcelWriter.writeString(parcel, 5, getTargetActivityClassName(), false);
        SafeParcelWriter.writeInt(parcel, 6, getSmallIconDrawableResId());
        SafeParcelWriter.writeInt(parcel, 7, getStopLiveStreamDrawableResId());
        SafeParcelWriter.writeInt(parcel, 8, getPauseDrawableResId());
        SafeParcelWriter.writeInt(parcel, 9, getPlayDrawableResId());
        SafeParcelWriter.writeInt(parcel, 10, getSkipNextDrawableResId());
        SafeParcelWriter.writeInt(parcel, 11, getSkipPrevDrawableResId());
        SafeParcelWriter.writeInt(parcel, 12, getForwardDrawableResId());
        SafeParcelWriter.writeInt(parcel, 13, getForward10DrawableResId());
        SafeParcelWriter.writeInt(parcel, 14, getForward30DrawableResId());
        SafeParcelWriter.writeInt(parcel, 15, getRewindDrawableResId());
        SafeParcelWriter.writeInt(parcel, 16, getRewind10DrawableResId());
        SafeParcelWriter.writeInt(parcel, 17, getRewind30DrawableResId());
        SafeParcelWriter.writeInt(parcel, 18, getDisconnectDrawableResId());
        SafeParcelWriter.writeInt(parcel, 19, this.zzmv);
        SafeParcelWriter.writeInt(parcel, 20, getCastingToDeviceStringResId());
        SafeParcelWriter.writeInt(parcel, 21, getStopLiveStreamTitleResId());
        SafeParcelWriter.writeInt(parcel, 22, this.zzmy);
        SafeParcelWriter.writeInt(parcel, 23, this.zzmz);
        SafeParcelWriter.writeInt(parcel, 24, this.zzna);
        SafeParcelWriter.writeInt(parcel, 25, this.zznb);
        SafeParcelWriter.writeInt(parcel, 26, this.zznc);
        SafeParcelWriter.writeInt(parcel, 27, this.zznd);
        SafeParcelWriter.writeInt(parcel, 28, this.zzne);
        SafeParcelWriter.writeInt(parcel, 29, this.zznf);
        SafeParcelWriter.writeInt(parcel, 30, this.zzng);
        SafeParcelWriter.writeInt(parcel, 31, this.zznh);
        SafeParcelWriter.writeInt(parcel, 32, this.zzni);
        SafeParcelWriter.writeIBinder(parcel, 33, this.zznj == null ? null : this.zznj.asBinder(), false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
