package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class BinaryFrame extends Id3Frame {
    public static final Creator<BinaryFrame> CREATOR = new C07051();
    public final byte[] data;

    /* renamed from: com.google.android.exoplayer2.metadata.id3.BinaryFrame$1 */
    static class C07051 implements Creator<BinaryFrame> {
        C07051() {
        }

        public BinaryFrame createFromParcel(Parcel parcel) {
            return new BinaryFrame(parcel);
        }

        public BinaryFrame[] newArray(int i) {
            return new BinaryFrame[i];
        }
    }

    public BinaryFrame(String str, byte[] bArr) {
        super(str);
        this.data = bArr;
    }

    BinaryFrame(Parcel parcel) {
        super(parcel.readString());
        this.data = parcel.createByteArray();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj != null) {
            if (getClass() == obj.getClass()) {
                BinaryFrame binaryFrame = (BinaryFrame) obj;
                if (!this.id.equals(binaryFrame.id) || Arrays.equals(this.data, binaryFrame.data) == null) {
                    z = false;
                }
                return z;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((527 + this.id.hashCode()) * 31) + Arrays.hashCode(this.data);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeByteArray(this.data);
    }
}
