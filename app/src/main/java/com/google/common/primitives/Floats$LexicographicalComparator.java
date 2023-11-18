package com.google.common.primitives;

import java.util.Comparator;

enum Floats$LexicographicalComparator implements Comparator<float[]> {
    INSTANCE;

    public int compare(float[] fArr, float[] fArr2) {
        int min = Math.min(fArr.length, fArr2.length);
        for (int i = 0; i < min; i++) {
            int compare = Float.compare(fArr[i], fArr2[i]);
            if (compare != 0) {
                return compare;
            }
        }
        return fArr.length - fArr2.length;
    }
}
