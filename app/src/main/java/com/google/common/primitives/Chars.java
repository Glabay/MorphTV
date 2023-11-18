package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible(emulated = true)
public final class Chars {
    public static final int BYTES = 2;

    @GwtCompatible
    private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final char[] array;
        final int end;
        final int start;

        public boolean isEmpty() {
            return false;
        }

        CharArrayAsList(char[] cArr) {
            this(cArr, 0, cArr.length);
        }

        CharArrayAsList(char[] cArr, int i, int i2) {
            this.array = cArr;
            this.start = i;
            this.end = i2;
        }

        public int size() {
            return this.end - this.start;
        }

        public Character get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Character.valueOf(this.array[this.start + i]);
        }

        public boolean contains(Object obj) {
            return (!(obj instanceof Character) || Chars.indexOf(this.array, ((Character) obj).charValue(), this.start, this.end) == -1) ? null : true;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Character) {
                obj = Chars.indexOf(this.array, ((Character) obj).charValue(), this.start, this.end);
                if (obj >= null) {
                    return obj - this.start;
                }
            }
            return -1;
        }

        public int lastIndexOf(Object obj) {
            if (obj instanceof Character) {
                obj = Chars.lastIndexOf(this.array, ((Character) obj).charValue(), this.start, this.end);
                if (obj >= null) {
                    return obj - this.start;
                }
            }
            return -1;
        }

        public Character set(int i, Character ch) {
            Preconditions.checkElementIndex(i, size());
            char c = this.array[this.start + i];
            this.array[this.start + i] = ((Character) Preconditions.checkNotNull(ch)).charValue();
            return Character.valueOf(c);
        }

        public List<Character> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            if (i == i2) {
                return Collections.emptyList();
            }
            return new CharArrayAsList(this.array, this.start + i, this.start + i2);
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof CharArrayAsList)) {
                return super.equals(obj);
            }
            CharArrayAsList charArrayAsList = (CharArrayAsList) obj;
            int size = size();
            if (charArrayAsList.size() != size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (this.array[this.start + i] != charArrayAsList.array[charArrayAsList.start + i]) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Chars.hashCode(this.array[i2]);
            }
            return i;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(size() * 3);
            stringBuilder.append('[');
            stringBuilder.append(this.array[this.start]);
            int i = this.start;
            while (true) {
                i++;
                if (i < this.end) {
                    stringBuilder.append(", ");
                    stringBuilder.append(this.array[i]);
                } else {
                    stringBuilder.append(']');
                    return stringBuilder.toString();
                }
            }
        }

        char[] toCharArray() {
            int size = size();
            Object obj = new char[size];
            System.arraycopy(this.array, this.start, obj, 0, size);
            return obj;
        }
    }

    private enum LexicographicalComparator implements Comparator<char[]> {
        INSTANCE;

        public int compare(char[] cArr, char[] cArr2) {
            int min = Math.min(cArr.length, cArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = Chars.compare(cArr[i], cArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return cArr.length - cArr2.length;
        }
    }

    public static int compare(char c, char c2) {
        return c - c2;
    }

    @GwtIncompatible("doesn't work")
    public static char fromBytes(byte b, byte b2) {
        return (char) ((b << 8) | (b2 & 255));
    }

    public static int hashCode(char c) {
        return c;
    }

    public static char saturatedCast(long j) {
        return j > 65535 ? '￿' : j < 0 ? '\u0000' : (char) ((int) j);
    }

    private Chars() {
    }

    public static char checkedCast(long j) {
        char c = (char) ((int) j);
        if (((long) c) == j) {
            return c;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Out of range: ");
        stringBuilder.append(j);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static boolean contains(char[] cArr, char c) {
        for (char c2 : cArr) {
            if (c2 == c) {
                return 1;
            }
        }
        return false;
    }

    public static int indexOf(char[] cArr, char c) {
        return indexOf(cArr, c, 0, cArr.length);
    }

    private static int indexOf(char[] cArr, char c, int i, int i2) {
        while (i < i2) {
            if (cArr[i] == c) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(char[] cArr, char[] cArr2) {
        Preconditions.checkNotNull(cArr, SerializerHandler.TYPE_ARRAY);
        Preconditions.checkNotNull(cArr2, "target");
        if (cArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (cArr.length - cArr2.length) + 1) {
            int i2 = 0;
            while (i2 < cArr2.length) {
                if (cArr[i + i2] != cArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(char[] cArr, char c) {
        return lastIndexOf(cArr, c, 0, cArr.length);
    }

    private static int lastIndexOf(char[] cArr, char c, int i, int i2) {
        for (i2--; i2 >= i; i2--) {
            if (cArr[i2] == c) {
                return i2;
            }
        }
        return -1;
    }

    public static char min(char... cArr) {
        Preconditions.checkArgument(cArr.length > 0);
        char c = cArr[0];
        for (int i = 1; i < cArr.length; i++) {
            if (cArr[i] < c) {
                c = cArr[i];
            }
        }
        return c;
    }

    public static char max(char... cArr) {
        Preconditions.checkArgument(cArr.length > 0);
        char c = cArr[0];
        for (int i = 1; i < cArr.length; i++) {
            if (cArr[i] > c) {
                c = cArr[i];
            }
        }
        return c;
    }

    public static char[] concat(char[]... cArr) {
        int i = 0;
        for (char[] length : cArr) {
            i += length.length;
        }
        Object obj = new char[i];
        int i2 = 0;
        for (Object obj2 : cArr) {
            System.arraycopy(obj2, 0, obj, i2, obj2.length);
            i2 += obj2.length;
        }
        return obj;
    }

    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(char c) {
        return new byte[]{(byte) (c >> 8), (byte) c};
    }

    @GwtIncompatible("doesn't work")
    public static char fromByteArray(byte[] bArr) {
        Preconditions.checkArgument(bArr.length >= 2, "array too small: %s < %s", new Object[]{Integer.valueOf(bArr.length), Integer.valueOf(2)});
        return fromBytes(bArr[0], bArr[1]);
    }

    public static char[] ensureCapacity(char[] cArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", new Object[]{Integer.valueOf(i)});
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", new Object[]{Integer.valueOf(i2)});
        return cArr.length < i ? copyOf(cArr, i + i2) : cArr;
    }

    private static char[] copyOf(char[] cArr, int i) {
        Object obj = new char[i];
        System.arraycopy(cArr, 0, obj, 0, Math.min(cArr.length, i));
        return obj;
    }

    public static String join(String str, char... cArr) {
        Preconditions.checkNotNull(str);
        int length = cArr.length;
        if (length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder((str.length() * (length - 1)) + length);
        stringBuilder.append(cArr[0]);
        for (int i = 1; i < length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(cArr[i]);
        }
        return stringBuilder.toString();
    }

    public static Comparator<char[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static char[] toArray(Collection<Character> collection) {
        if (collection instanceof CharArrayAsList) {
            return ((CharArrayAsList) collection).toCharArray();
        }
        collection = collection.toArray();
        int length = collection.length;
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            cArr[i] = ((Character) Preconditions.checkNotNull(collection[i])).charValue();
        }
        return cArr;
    }

    public static List<Character> asList(char... cArr) {
        if (cArr.length == 0) {
            return Collections.emptyList();
        }
        return new CharArrayAsList(cArr);
    }
}
