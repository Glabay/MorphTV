package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean z) {
        if (!z) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean z, @Nullable Object obj) {
        if (!z) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }

    public static void checkArgument(boolean z, @Nullable String str, @Nullable Object... objArr) {
        if (!z) {
            throw new IllegalArgumentException(format(str, objArr));
        }
    }

    public static void checkState(boolean z) {
        if (!z) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean z, @Nullable Object obj) {
        if (!z) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    public static void checkState(boolean z, @Nullable String str, @Nullable Object... objArr) {
        if (!z) {
            throw new IllegalStateException(format(str, objArr));
        }
    }

    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static <T> T checkNotNull(T t, @Nullable Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public static <T> T checkNotNull(T t, @Nullable String str, @Nullable Object... objArr) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(format(str, objArr));
    }

    public static int checkElementIndex(int i, int i2) {
        return checkElementIndex(i, i2, "index");
    }

    public static int checkElementIndex(int i, int i2, @Nullable String str) {
        if (i >= 0) {
            if (i < i2) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException(badElementIndex(i, i2, str));
    }

    private static String badElementIndex(int i, int i2, String str) {
        if (i < 0) {
            return format("%s (%s) must not be negative", str, Integer.valueOf(i));
        } else if (i2 < 0) {
            str = new StringBuilder();
            str.append("negative size: ");
            str.append(i2);
            throw new IllegalArgumentException(str.toString());
        } else {
            return format("%s (%s) must be less than size (%s)", str, Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    public static int checkPositionIndex(int i, int i2) {
        return checkPositionIndex(i, i2, "index");
    }

    public static int checkPositionIndex(int i, int i2, @Nullable String str) {
        if (i >= 0) {
            if (i <= i2) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException(badPositionIndex(i, i2, str));
    }

    private static String badPositionIndex(int i, int i2, String str) {
        if (i < 0) {
            return format("%s (%s) must not be negative", str, Integer.valueOf(i));
        } else if (i2 < 0) {
            str = new StringBuilder();
            str.append("negative size: ");
            str.append(i2);
            throw new IllegalArgumentException(str.toString());
        } else {
            return format("%s (%s) must not be greater than size (%s)", str, Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    public static void checkPositionIndexes(int i, int i2, int i3) {
        if (i >= 0 && i2 >= i) {
            if (i2 <= i3) {
                return;
            }
        }
        throw new IndexOutOfBoundsException(badPositionIndexes(i, i2, i3));
    }

    private static String badPositionIndexes(int i, int i2, int i3) {
        if (i >= 0) {
            if (i <= i3) {
                if (i2 >= 0) {
                    if (i2 <= i3) {
                        return format("end index (%s) must not be less than start index (%s)", Integer.valueOf(i2), Integer.valueOf(i));
                    }
                }
                return badPositionIndex(i2, i3, "end index");
            }
        }
        return badPositionIndex(i, i3, "start index");
    }

    static String format(String str, @Nullable Object... objArr) {
        str = String.valueOf(str);
        StringBuilder stringBuilder = new StringBuilder(str.length() + (objArr.length * 16));
        int i = 0;
        int i2 = 0;
        while (i < objArr.length) {
            int indexOf = str.indexOf("%s", i2);
            if (indexOf == -1) {
                break;
            }
            stringBuilder.append(str.substring(i2, indexOf));
            i2 = i + 1;
            stringBuilder.append(objArr[i]);
            int i3 = i2;
            i2 = indexOf + 2;
            i = i3;
        }
        stringBuilder.append(str.substring(i2));
        if (i < objArr.length) {
            stringBuilder.append(" [");
            str = i + 1;
            stringBuilder.append(objArr[i]);
            while (str < objArr.length) {
                stringBuilder.append(", ");
                i = str + 1;
                stringBuilder.append(objArr[str]);
                str = i;
            }
            stringBuilder.append(']');
        }
        return stringBuilder.toString();
    }
}
