package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class PrivFrame extends Id3Frame {
    public static final Creator<PrivFrame> CREATOR = new C07101();
    public static final String ID = "PRIV";
    public final String owner;
    public final byte[] privateData;

    /* renamed from: com.google.android.exoplayer2.metadata.id3.PrivFrame$1 */
    static class C07101 implements Creator<PrivFrame> {
        C07101() {
        }

        public PrivFrame createFromParcel(Parcel parcel) {
            return new PrivFrame(parcel);
        }

        public PrivFrame[] newArray(int i) {
            return new PrivFrame[i];
        }
    }

    public PrivFrame(String str, byte[] bArr) {
        super(ID);
        this.owner = str;
        this.privateData = bArr;
    }

    PrivFrame(Parcel parcel) {
        super(ID);
        this.owner = parcel.readString();
        this.privateData = parcel.createByteArray();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj != null) {
            if (getClass() == obj.getClass()) {
                PrivFrame privFrame = (PrivFrame) obj;
                if (!Util.areEqual(this.owner, privFrame.owner) || Arrays.equals(this.privateData, privFrame.privateData) == null) {
                    z = false;
                }
                return z;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((527 + (this.owner != null ? this.owner.hashCode() : 0)) * 31) + Arrays.hashCode(this.privateData);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.owner);
        parcel.writeByteArray(this.privateData);
    }
}
