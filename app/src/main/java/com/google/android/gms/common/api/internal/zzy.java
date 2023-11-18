package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Collections;
import java.util.Map;

final class zzy implements OnCompleteListener<Map<zzh<?>, String>> {
    private final /* synthetic */ zzw zzgu;

    private zzy(zzw zzw) {
        this.zzgu = zzw;
    }

    public final void onComplete(@NonNull Task<Map<zzh<?>, String>> task) {
        this.zzgu.zzga.lock();
        try {
            if (this.zzgu.zzgp) {
                if (task.isSuccessful()) {
                    this.zzgu.zzgq = new ArrayMap(this.zzgu.zzgg.size());
                    for (zzv zzm : this.zzgu.zzgg.values()) {
                        this.zzgu.zzgq.put(zzm.zzm(), ConnectionResult.RESULT_SUCCESS);
                    }
                } else {
                    zzw zzw;
                    ConnectionResult zzf;
                    if (task.getException() instanceof AvailabilityException) {
                        AvailabilityException availabilityException = (AvailabilityException) task.getException();
                        if (this.zzgu.zzgn) {
                            this.zzgu.zzgq = new ArrayMap(this.zzgu.zzgg.size());
                            for (zzv zzv : this.zzgu.zzgg.values()) {
                                Map zzd;
                                zzh zzm2 = zzv.zzm();
                                Object connectionResult = availabilityException.getConnectionResult(zzv);
                                if (this.zzgu.zza(zzv, (ConnectionResult) connectionResult)) {
                                    zzd = this.zzgu.zzgq;
                                    connectionResult = new ConnectionResult(16);
                                } else {
                                    zzd = this.zzgu.zzgq;
                                }
                                zzd.put(zzm2, connectionResult);
                            }
                        } else {
                            this.zzgu.zzgq = availabilityException.zzl();
                        }
                        zzw = this.zzgu;
                        zzf = this.zzgu.zzai();
                    } else {
                        Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                        this.zzgu.zzgq = Collections.emptyMap();
                        zzw = this.zzgu;
                        zzf = new ConnectionResult(8);
                    }
                    zzw.zzgt = zzf;
                }
                if (this.zzgu.zzgr != null) {
                    this.zzgu.zzgq.putAll(this.zzgu.zzgr);
                    this.zzgu.zzgt = this.zzgu.zzai();
                }
                if (this.zzgu.zzgt == null) {
                    this.zzgu.zzag();
                    this.zzgu.zzah();
                } else {
                    this.zzgu.zzgp = false;
                    this.zzgu.zzgj.zzc(this.zzgu.zzgt);
                }
                this.zzgu.zzgl.signalAll();
            }
            this.zzgu.zzga.unlock();
        } catch (Throwable th) {
            this.zzgu.zzga.unlock();
        }
    }
}
