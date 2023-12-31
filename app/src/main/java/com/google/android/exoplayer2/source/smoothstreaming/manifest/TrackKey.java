package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;

public final class TrackKey implements Parcelable, Comparable<TrackKey> {
    public static final Creator<TrackKey> CREATOR = new C07511();
    public final int streamElementIndex;
    public final int trackIndex;

    /* renamed from: com.google.android.exoplayer2.source.smoothstreaming.manifest.TrackKey$1 */
    static class C07511 implements Creator<TrackKey> {
        C07511() {
        }

        public TrackKey createFromParcel(Parcel parcel) {
            return new TrackKey(parcel.readInt(), parcel.readInt());
        }

        public TrackKey[] newArray(int i) {
            return new TrackKey[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public TrackKey(int i, int i2) {
        this.streamElementIndex = i;
        this.trackIndex = i2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.streamElementIndex);
        stringBuilder.append(".");
        stringBuilder.append(this.trackIndex);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.streamElementIndex);
        parcel.writeInt(this.trackIndex);
    }

    public int compareTo(@NonNull TrackKey trackKey) {
        int i = this.streamElementIndex - trackKey.streamElementIndex;
        return i == 0 ? this.trackIndex - trackKey.trackIndex : i;
    }
}
