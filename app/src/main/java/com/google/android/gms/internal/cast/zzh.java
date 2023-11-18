package com.google.android.gms.internal.cast;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.zzd;
import com.google.android.gms.cast.framework.zzab;
import com.google.android.gms.cast.framework.zzj;
import com.google.android.gms.cast.framework.zzl;
import com.google.android.gms.cast.framework.zzr;
import com.google.android.gms.cast.framework.zzt;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.util.Map;

public interface zzh extends IInterface {
    zzd zza(IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, IObjectWrapper iObjectWrapper3, CastMediaOptions castMediaOptions) throws RemoteException;

    zzj zza(IObjectWrapper iObjectWrapper, CastOptions castOptions, zzj zzj, Map map) throws RemoteException;

    zzl zza(CastOptions castOptions, IObjectWrapper iObjectWrapper, com.google.android.gms.cast.framework.zzh zzh) throws RemoteException;

    zzr zza(IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, IObjectWrapper iObjectWrapper3) throws RemoteException;

    zzt zza(String str, String str2, zzab zzab) throws RemoteException;

    zzae zza(IObjectWrapper iObjectWrapper, zzag zzag, int i, int i2, boolean z, long j, int i3, int i4, int i5) throws RemoteException;
}
