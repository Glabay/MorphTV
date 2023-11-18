package com.google.android.gms.internal.cast;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface zzae extends IInterface {

    public static abstract class zza extends zzb implements zzae {
        public static zzae zzf(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.cast.framework.media.internal.IFetchBitmapTask");
            return queryLocalInterface instanceof zzae ? (zzae) queryLocalInterface : new zzaf(iBinder);
        }
    }

    Bitmap zzb(Uri uri) throws RemoteException;
}
