package com.google.android.gms.cast.framework.media;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;
import com.google.android.gms.internal.cast.zza;
import java.util.List;

public final class zzh extends zza implements zzf {
    zzh(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.media.INotificationActionsProvider");
    }

    public final int[] getCompactViewActionIndices() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(4, obtainAndWriteInterfaceToken());
        int[] createIntArray = transactAndReadException.createIntArray();
        transactAndReadException.recycle();
        return createIntArray;
    }

    public final List<NotificationAction> getNotificationActions() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken());
        List createTypedArrayList = transactAndReadException.createTypedArrayList(NotificationAction.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final IObjectWrapper zzaw() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken());
        IObjectWrapper asInterface = Stub.asInterface(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return asInterface;
    }

    public final int zzm() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken());
        int readInt = transactAndReadException.readInt();
        transactAndReadException.recycle();
        return readInt;
    }
}
