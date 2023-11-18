package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.sha1_hash;
import com.frostwire.jlibtorrent.swig.sha1_hash_vector;
import java.util.ArrayList;

public final class Sha1Hash implements Comparable<Sha1Hash>, Cloneable {
    /* renamed from: h */
    private final sha1_hash f32h;

    public Sha1Hash(sha1_hash sha1_hash) {
        this.f32h = sha1_hash;
    }

    public Sha1Hash(byte[] bArr) {
        if (bArr.length != 20) {
            throw new IllegalArgumentException("bytes array must be of length 20");
        }
        this.f32h = new sha1_hash(Vectors.bytes2byte_vector(bArr));
    }

    public Sha1Hash(String str) {
        this(Hex.decode(str));
    }

    public Sha1Hash() {
        this(new sha1_hash());
    }

    public sha1_hash swig() {
        return this.f32h;
    }

    public void clear() {
        this.f32h.clear();
    }

    public boolean isAllZeros() {
        return this.f32h.is_all_zeros();
    }

    public int countLeadingZeroes() {
        return this.f32h.count_leading_zeroes();
    }

    public String toHex() {
        return this.f32h.to_hex();
    }

    public int compareTo(Sha1Hash sha1Hash) {
        return sha1_hash.compare(this.f32h, sha1Hash.f32h);
    }

    public String toString() {
        return toHex();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Sha1Hash) {
            return this.f32h.op_eq(((Sha1Hash) obj).f32h);
        }
        return null;
    }

    public int hashCode() {
        return this.f32h.hash_code();
    }

    public Sha1Hash clone() {
        return new Sha1Hash(new sha1_hash(this.f32h));
    }

    public static Sha1Hash max() {
        return new Sha1Hash(sha1_hash.max());
    }

    public static Sha1Hash min() {
        return new Sha1Hash(sha1_hash.min());
    }

    static ArrayList<Sha1Hash> convert(sha1_hash_vector sha1_hash_vector) {
        int size = (int) sha1_hash_vector.size();
        ArrayList<Sha1Hash> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new Sha1Hash(sha1_hash_vector.get(i)));
        }
        return arrayList;
    }
}
