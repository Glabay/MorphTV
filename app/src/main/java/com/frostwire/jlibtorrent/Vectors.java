package com.frostwire.jlibtorrent;

import com.frostwire.jlibtorrent.swig.byte_const_span;
import com.frostwire.jlibtorrent.swig.byte_vector;
import com.frostwire.jlibtorrent.swig.int64_vector;
import com.frostwire.jlibtorrent.swig.int_vector;
import com.frostwire.jlibtorrent.swig.string_vector;
import java.util.ArrayList;
import java.util.List;

public final class Vectors {
    private Vectors() {
    }

    public static byte[] byte_vector2bytes(byte_vector byte_vector) {
        int size = (int) byte_vector.size();
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = byte_vector.get(i);
        }
        return bArr;
    }

    public static void byte_vector2bytes(byte_vector byte_vector, byte[] bArr) {
        int size = (int) byte_vector.size();
        for (int i = 0; i < size; i++) {
            bArr[i] = byte_vector.get(i);
        }
    }

    public static byte_vector bytes2byte_vector(byte[] bArr) {
        byte_vector byte_vector = new byte_vector();
        for (byte push_back : bArr) {
            byte_vector.push_back(push_back);
        }
        return byte_vector;
    }

    public static void bytes2byte_vector(byte[] bArr, byte_vector byte_vector) {
        for (int i = 0; i < bArr.length; i++) {
            byte_vector.set(i, bArr[i]);
        }
    }

    public static int[] int_vector2ints(int_vector int_vector) {
        int size = (int) int_vector.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = int_vector.get(i);
        }
        return iArr;
    }

    public static long[] int64_vector2longs(int64_vector int64_vector) {
        int size = (int) int64_vector.size();
        long[] jArr = new long[size];
        for (int i = 0; i < size; i++) {
            jArr[i] = int64_vector.get(i);
        }
        return jArr;
    }

    public static byte[] byte_span2bytes(byte_const_span byte_const_span) {
        int size = (int) byte_const_span.size();
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = byte_const_span.get((long) i);
        }
        return bArr;
    }

    public static byte_vector new_byte_vector(int i) {
        byte_vector byte_vector = new byte_vector();
        for (int i2 = 0; i2 < i; i2++) {
            byte_vector.push_back((byte) 0);
        }
        return byte_vector;
    }

    public static List<String> string_vector2list(string_vector string_vector) {
        int size = (int) string_vector.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(string_vector.get(i));
        }
        return arrayList;
    }

    public static String byte_vector2string(byte_vector byte_vector, String str) {
        try {
            return new String(byte_vector2bytes(byte_vector), str);
        } catch (byte_vector byte_vector2) {
            throw new RuntimeException(byte_vector2);
        }
    }
}
