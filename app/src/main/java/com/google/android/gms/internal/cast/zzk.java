package com.google.android.gms.internal.cast;

public abstract class zzk extends zzb implements zzj {
    public zzk() {
        super("com.google.android.gms.cast.framework.internal.IMediaRouter");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final boolean dispatchTransaction(int r2, android.os.Parcel r3, android.os.Parcel r4, int r5) throws android.os.RemoteException {
        /*
        r1 = this;
        switch(r2) {
            case 1: goto L_0x007a;
            case 2: goto L_0x0067;
            case 3: goto L_0x005b;
            case 4: goto L_0x0044;
            case 5: goto L_0x003c;
            case 6: goto L_0x0038;
            case 7: goto L_0x0033;
            case 8: goto L_0x0023;
            case 9: goto L_0x0017;
            case 10: goto L_0x0009;
            case 11: goto L_0x0005;
            default: goto L_0x0003;
        };
    L_0x0003:
        r2 = 0;
        return r2;
    L_0x0005:
        r1.zzam();
        goto L_0x0076;
    L_0x0009:
        r1.zzm();
        r4.writeNoException();
        r2 = 12451009; // 0xbdfcc1 float:1.744758E-38 double:6.151616E-317;
        r4.writeInt(r2);
        goto L_0x00a2;
    L_0x0017:
        r2 = r1.zzal();
        r4.writeNoException();
        r4.writeString(r2);
        goto L_0x00a2;
    L_0x0023:
        r2 = r3.readString();
        r2 = r1.zzl(r2);
        r4.writeNoException();
        com.google.android.gms.internal.cast.zzc.zzb(r4, r2);
        goto L_0x00a2;
    L_0x0033:
        r2 = r1.zzak();
        goto L_0x0054;
    L_0x0038:
        r1.zzaj();
        goto L_0x0076;
    L_0x003c:
        r2 = r3.readString();
        r1.zzk(r2);
        goto L_0x0076;
    L_0x0044:
        r2 = android.os.Bundle.CREATOR;
        r2 = com.google.android.gms.internal.cast.zzc.zza(r3, r2);
        r2 = (android.os.Bundle) r2;
        r3 = r3.readInt();
        r2 = r1.zzb(r2, r3);
    L_0x0054:
        r4.writeNoException();
        com.google.android.gms.internal.cast.zzc.zza(r4, r2);
        goto L_0x00a2;
    L_0x005b:
        r2 = android.os.Bundle.CREATOR;
        r2 = com.google.android.gms.internal.cast.zzc.zza(r3, r2);
        r2 = (android.os.Bundle) r2;
        r1.zzd(r2);
        goto L_0x0076;
    L_0x0067:
        r2 = android.os.Bundle.CREATOR;
        r2 = com.google.android.gms.internal.cast.zzc.zza(r3, r2);
        r2 = (android.os.Bundle) r2;
        r3 = r3.readInt();
        r1.zza(r2, r3);
    L_0x0076:
        r4.writeNoException();
        goto L_0x00a2;
    L_0x007a:
        r2 = android.os.Bundle.CREATOR;
        r2 = com.google.android.gms.internal.cast.zzc.zza(r3, r2);
        r2 = (android.os.Bundle) r2;
        r3 = r3.readStrongBinder();
        if (r3 != 0) goto L_0x008a;
    L_0x0088:
        r3 = 0;
        goto L_0x009e;
    L_0x008a:
        r5 = "com.google.android.gms.cast.framework.internal.IMediaRouterCallback";
        r5 = r3.queryLocalInterface(r5);
        r0 = r5 instanceof com.google.android.gms.internal.cast.zzl;
        if (r0 == 0) goto L_0x0098;
    L_0x0094:
        r3 = r5;
        r3 = (com.google.android.gms.internal.cast.zzl) r3;
        goto L_0x009e;
    L_0x0098:
        r5 = new com.google.android.gms.internal.cast.zzm;
        r5.<init>(r3);
        r3 = r5;
    L_0x009e:
        r1.zza(r2, r3);
        goto L_0x0076;
    L_0x00a2:
        r2 = 1;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzk.dispatchTransaction(int, android.os.Parcel, android.os.Parcel, int):boolean");
    }
}
