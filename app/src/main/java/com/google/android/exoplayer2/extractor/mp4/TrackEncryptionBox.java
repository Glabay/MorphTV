package com.google.android.exoplayer2.extractor.mp4;

import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.util.Assertions;

public final class TrackEncryptionBox {
    private static final String TAG = "TrackEncryptionBox";
    public final CryptoData cryptoData;
    public final byte[] defaultInitializationVector;
    public final int initializationVectorSize;
    public final boolean isEncrypted;
    @Nullable
    public final String schemeType;

    public TrackEncryptionBox(boolean z, @Nullable String str, int i, byte[] bArr, int i2, int i3, @Nullable byte[] bArr2) {
        int i4 = 0;
        int i5 = i == 0 ? 1 : 0;
        if (bArr2 == null) {
            i4 = 1;
        }
        Assertions.checkArgument(i4 ^ i5);
        this.isEncrypted = z;
        this.schemeType = str;
        this.initializationVectorSize = i;
        this.defaultInitializationVector = bArr2;
        this.cryptoData = new CryptoData(schemeToCryptoMode(str), bArr, i2, i3);
    }

    private static int schemeToCryptoMode(@Nullable String str) {
        if (str == null) {
            return 1;
        }
        Object obj = -1;
        int hashCode = str.hashCode();
        if (hashCode != 3046605) {
            if (hashCode != 3046671) {
                if (hashCode != 3049879) {
                    if (hashCode == 3049895) {
                        if (str.equals(C0649C.CENC_TYPE_cens)) {
                            obj = 1;
                        }
                    }
                } else if (str.equals(C0649C.CENC_TYPE_cenc)) {
                    obj = null;
                }
            } else if (str.equals(C0649C.CENC_TYPE_cbcs)) {
                obj = 3;
            }
        } else if (str.equals(C0649C.CENC_TYPE_cbc1)) {
            obj = 2;
        }
        switch (obj) {
            case null:
            case 1:
                return 1;
            case 2:
            case 3:
                return 2;
            default:
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported protection scheme type '");
                stringBuilder.append(str);
                stringBuilder.append("'. Assuming AES-CTR crypto mode.");
                Log.w(str2, stringBuilder.toString());
                return 1;
        }
    }
}
