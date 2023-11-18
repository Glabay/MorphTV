package com.google.android.gms.cast.framework.media;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.Stub;
import com.google.android.gms.internal.cast.zza;

public final class zzc extends zza implements zzb {
    zzc(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.media.IImagePicker");
    }

    public final WebImage onPickImage(MediaMetadata mediaMetadata, int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        com.google.android.gms.internal.cast.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) mediaMetadata);
        obtainAndWriteInterfaceToken.writeInt(i);
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
        WebImage webImage = (WebImage) com.google.android.gms.internal.cast.zzc.zza(transactAndReadException, WebImage.CREATOR);
        transactAndReadException.recycle();
        return webImage;
    }

    public final WebImage zza(MediaMetadata mediaMetadata, ImageHints imageHints) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        com.google.android.gms.internal.cast.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) mediaMetadata);
        com.google.android.gms.internal.cast.zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) imageHints);
        Parcel transactAndReadException = transactAndReadException(4, obtainAndWriteInterfaceToken);
        WebImage webImage = (WebImage) com.google.android.gms.internal.cast.zzc.zza(transactAndReadException, WebImage.CREATOR);
        transactAndReadException.recycle();
        return webImage;
    }

    public final IObjectWrapper zzaw() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken());
        IObjectWrapper asInterface = Stub.asInterface(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return asInterface;
    }

    public final int zzm() throws RemoteException {
        Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken());
        int readInt = transactAndReadException.readInt();
        transactAndReadException.recycle();
        return readInt;
    }
}
