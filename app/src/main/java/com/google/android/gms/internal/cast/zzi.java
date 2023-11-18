package com.google.android.gms.internal.cast;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.zzd;
import com.google.android.gms.cast.framework.media.zzd.zza;
import com.google.android.gms.cast.framework.zzab;
import com.google.android.gms.cast.framework.zzh;
import com.google.android.gms.cast.framework.zzj;
import com.google.android.gms.cast.framework.zzl;
import com.google.android.gms.cast.framework.zzr;
import com.google.android.gms.cast.framework.zzt;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.util.Map;

public final class zzi extends zza implements zzh {
    zzi(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.cast.framework.internal.ICastDynamiteModule");
    }

    public final zzd zza(IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, IObjectWrapper iObjectWrapper3, CastMediaOptions castMediaOptions) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper2);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper3);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) castMediaOptions);
        Parcel transactAndReadException = transactAndReadException(4, obtainAndWriteInterfaceToken);
        zzd zze = zza.zze(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return zze;
    }

    public final zzj zza(IObjectWrapper iObjectWrapper, CastOptions castOptions, zzj zzj, Map map) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) castOptions);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzj);
        obtainAndWriteInterfaceToken.writeMap(map);
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
        zzj zza = zzj.zza.zza(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return zza;
    }

    public final zzl zza(CastOptions castOptions, IObjectWrapper iObjectWrapper, zzh zzh) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) castOptions);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzh);
        Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken);
        zzl zzb = zzl.zza.zzb(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return zzb;
    }

    public final zzr zza(IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, IObjectWrapper iObjectWrapper3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper2);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper3);
        Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken);
        zzr zzc = zzr.zza.zzc(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return zzc;
    }

    public final zzt zza(String str, String str2, zzab zzab) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzab);
        Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken);
        zzt zzd = zzt.zza.zzd(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return zzd;
    }

    public final zzae zza(IObjectWrapper iObjectWrapper, zzag zzag, int i, int i2, boolean z, long j, int i3, int i4, int i5) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzag);
        obtainAndWriteInterfaceToken.writeInt(i);
        obtainAndWriteInterfaceToken.writeInt(i2);
        zzc.zza(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken.writeLong(j);
        obtainAndWriteInterfaceToken.writeInt(i3);
        obtainAndWriteInterfaceToken.writeInt(i4);
        obtainAndWriteInterfaceToken.writeInt(i5);
        Parcel transactAndReadException = transactAndReadException(6, obtainAndWriteInterfaceToken);
        zzae zzf = zzae.zza.zzf(transactAndReadException.readStrongBinder());
        transactAndReadException.recycle();
        return zzf;
    }
}
