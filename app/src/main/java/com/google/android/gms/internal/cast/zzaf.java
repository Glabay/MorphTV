package com.google.android.gms.internal.cast;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public final class zzaf extends zza implements zzae {
    zzaf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.media.internal.IFetchBitmapTask");
    }

    public final Bitmap zzb(Uri uri) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) uri);
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
        Bitmap bitmap = (Bitmap) zzc.zza(transactAndReadException, Bitmap.CREATOR);
        transactAndReadException.recycle();
        return bitmap;
    }
}
