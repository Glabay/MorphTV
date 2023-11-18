package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
public final class Booleans {

    @GwtCompatible
    private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final boolean[] array;
        final int end;
        final int start;

        public boolean isEmpty() {
            return false;
        }

        BooleanArrayAsList(boolean[] zArr) {
            this(zArr, 0, zArr.length);
        }

        BooleanArrayAsList(boolean[] zArr, int i, int i2) {
            this.array = zArr;
            this.start = i;
            this.end = i2;
        }

        public int size() {
            return this.end - this.start;
        }

        public Boolean get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Boolean.valueOf(this.array[this.start + i]);
        }

        public boolean contains(Object obj) {
            return (!(obj instanceof Boolean) || Booleans.indexOf(this.array, ((Boolean) obj).booleanValue(), this.start, this.end) == -1) ? null : true;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Boolean) {
                obj = Booleans.indexOf(this.array, ((Boolean) obj).booleanValue(), this.start, this.end);
                if (obj >= null) {
                    return obj - this.start;
                }
            }
            return -1;
        }

        public int lastIndexOf(Object obj) {
            if (obj instanceof Boolean) {
                obj = Booleans.lastIndexOf(this.array, ((Boolean) obj).booleanValue(), this.start, this.end);
                if (obj >= null) {
                    return obj - this.start;
                }
            }
            return -1;
        }

        public Boolean set(int i, Boolean bool) {
            Preconditions.checkElementIndex(i, size());
            boolean z = this.array[this.start + i];
            this.array[this.start + i] = ((Boolean) Preconditions.checkNotNull(bool)).booleanValue();
            return Boolean.valueOf(z);
        }

        public List<Boolean> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            if (i == i2) {
                return Collections.emptyList();
            }
            return new BooleanArrayAsList(this.array, this.start + i, this.start + i2);
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof BooleanArrayAsList)) {
                return super.equals(obj);
            }
            BooleanArrayAsList booleanArrayAsList = (BooleanArrayAsList) obj;
            int size = size();
            if (booleanArrayAsList.size() != size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (this.array[this.start + i] != booleanArrayAsList.array[booleanArrayAsList.start + i]) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Booleans.hashCode(this.array[i2]);
            }
            return i;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(size() * 7);
            stringBuilder.append(this.array[this.start] ? "[true" : "[false");
            int i = this.start;
            while (true) {
                i++;
                if (i < this.end) {
                    stringBuilder.append(this.array[i] ? ", true" : ", false");
                } else {
                    stringBuilder.append(']');
                    return stringBuilder.toString();
                }
            }
        }

        boolean[] toBooleanArray() {
            int size = size();
            Object obj = new boolean[size];
            System.arraycopy(this.array, this.start, obj, 0, size);
            return obj;
        }
    }

    private enum LexicographicalComparator implements Comparator<boolean[]> {
        INSTANCE;

        public int compare(boolean[] zArr, boolean[] zArr2) {
            int min = Math.min(zArr.length, zArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = Booleans.compare(zArr[i], zArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return zArr.length - zArr2.length;
        }
    }

    public static int compare(boolean z, boolean z2) {
        return z == z2 ? 0 : z ? 1 : -1;
    }

    public static int hashCode(boolean z) {
        return z ? 1231 : 1237;
    }

    private Booleans() {
    }

    public static boolean contains(boolean[] zArr, boolean z) {
        for (boolean z2 : zArr) {
            if (z2 == z) {
                return 1;
            }
        }
        return false;
    }

    public static int indexOf(boolean[] zArr, boolean z) {
        return indexOf(zArr, z, 0, zArr.length);
    }

    private static int indexOf(boolean[] zArr, boolean z, int i, int i2) {
        while (i < i2) {
            if (zArr[i] == z) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(boolean[] zArr, boolean[] zArr2) {
        Preconditions.checkNotNull(zArr, SerializerHandler.TYPE_ARRAY);
        Preconditions.checkNotNull(zArr2, "target");
        if (zArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (zArr.length - zArr2.length) + 1) {
            int i2 = 0;
            while (i2 < zArr2.length) {
                if (zArr[i + i2] != zArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(boolean[] zArr, boolean z) {
        return lastIndexOf(zArr, z, 0, zArr.length);
    }

    private static int lastIndexOf(boolean[] zArr, boolean z, int i, int i2) {
        for (i2--; i2 >= i; i2--) {
            if (zArr[i2] == z) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean[] concat(boolean[]... zArr) {
        int i = 0;
        for (boolean[] length : zArr) {
            i += length.length;
        }
        Object obj = new boolean[i];
        int i2 = 0;
        for (Object obj2 : zArr) {
            System.arraycopy(obj2, 0, obj, i2, obj2.length);
            i2 += obj2.length;
        }
        return obj;
    }

    public static boolean[] ensureCapacity(boolean[] zArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", new Object[]{Integer.valueOf(i)});
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", new Object[]{Integer.valueOf(i2)});
        return zArr.length < i ? copyOf(zArr, i + i2) : zArr;
    }

    private static boolean[] copyOf(boolean[] zArr, int i) {
        Object obj = new boolean[i];
        System.arraycopy(zArr, 0, obj, 0, Math.min(zArr.length, i));
        return obj;
    }

    public static String join(String str, boolean... zArr) {
        Preconditions.checkNotNull(str);
        if (zArr.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(zArr.length * 7);
        stringBuilder.append(zArr[0]);
        for (int i = 1; i < zArr.length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(zArr[i]);
        }
        return stringBuilder.toString();
    }

    public static Comparator<boolean[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static boolean[] toArray(Collection<Boolean> collection) {
        if (collection instanceof BooleanArrayAsList) {
            return ((BooleanArrayAsList) collection).toBooleanArray();
        }
        collection = collection.toArray();
        int length = collection.length;
        boolean[] zArr = new boolean[length];
        for (int i = 0; i < length; i++) {
            zArr[i] = ((Boolean) Preconditions.checkNotNull(collection[i])).booleanValue();
        }
        return zArr;
    }

    public static List<Boolean> asList(boolean... zArr) {
        if (zArr.length == 0) {
            return Collections.emptyList();
        }
        return new BooleanArrayAsList(zArr);
    }

    @Beta
    public static int countTrue(boolean... zArr) {
        int i = 0;
        for (boolean z : zArr) {
            if (z) {
                i++;
            }
        }
        return i;
    }
}
