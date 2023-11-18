package com.google.common.primitives;

import java.util.Comparator;

enum Ints$LexicographicalComparator implements Comparator<int[]> {
    INSTANCE;

    public int compare(int[] iArr, int[] iArr2) {
        int min = Math.min(iArr.length, iArr2.length);
        for (int i = 0; i < min; i++) {
            int compare = Ints.compare(iArr[i], iArr2[i]);
            if (compare != 0) {
                return compare;
            }
        }
        return iArr.length - iArr2.length;
    }
}
