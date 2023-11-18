package com.google.android.gms.cast.framework.media;

import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.cast.zzb;
import com.google.android.gms.internal.cast.zzc;
import java.util.List;

public abstract class zzg extends zzb implements zzf {
    public zzg() {
        super("com.google.android.gms.cast.framework.media.INotificationActionsProvider");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                i = zzm();
                parcel2.writeNoException();
                parcel2.writeInt(i);
                break;
            case 2:
                IInterface zzaw = zzaw();
                parcel2.writeNoException();
                zzc.zza(parcel2, zzaw);
                break;
            case 3:
                List notificationActions = getNotificationActions();
                parcel2.writeNoException();
                parcel2.writeTypedList(notificationActions);
                break;
            case 4:
                int[] compactViewActionIndices = getCompactViewActionIndices();
                parcel2.writeNoException();
                parcel2.writeIntArray(compactViewActionIndices);
                break;
            default:
                return false;
        }
        return true;
    }
}
