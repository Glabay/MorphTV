package com.google.common.primitives;

import java.util.Comparator;

enum Doubles$LexicographicalComparator implements Comparator<double[]> {
    INSTANCE;

    public int compare(double[] dArr, double[] dArr2) {
        int min = Math.min(dArr.length, dArr2.length);
        for (int i = 0; i < min; i++) {
            int compare = Double.compare(dArr[i], dArr2[i]);
            if (compare != 0) {
                return compare;
            }
        }
        return dArr.length - dArr2.length;
    }
}
